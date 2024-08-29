package com.dwei.core.config.exception;

import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.exception.*;
import com.dwei.common.result.Response;
import com.dwei.common.utils.StringUtils;
import com.dwei.core.config.exception.log.ExceptionHandlerLog;
import com.dwei.core.config.exception.log.ExceptionHandlerLogManager;
import com.dwei.core.utils.ExStackUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author hww
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionHandlerLogManager exceptionHandlerLogManager;

    public GlobalExceptionHandler(ExceptionHandlerLogManager exceptionHandlerLogManager) {
        this.exceptionHandlerLogManager = exceptionHandlerLogManager;
    }

    private void log(HttpServletRequest request, Exception ex) {
        try {
            List<ExceptionHandlerLog> handlerLogs = exceptionHandlerLogManager.getHandlerLogs();
            for (ExceptionHandlerLog handlerLog : handlerLogs) {
                handlerLog.log(request, ex);
            }
        } catch (Exception e) {
            log.error("日志记录异常", e);
        }
    }

    private Response<?> packageResponse(HttpServletRequest request, Exception ex, StatusCodeEnum statusCodeEnum) {
        return packageResponse(request, ex, statusCodeEnum, null);
    }

    /**
     * 标准化返回结果
     */
    private Response<?> packageResponse(HttpServletRequest request, Exception ex, StatusCodeEnum statusCode, String message) {
        log(request, ex);
        ExStackUtils.putException(request, ex);
        if (Objects.equals(statusCode, StatusCodeEnum.unknown) || Objects.equals(statusCode, StatusCodeEnum.databaseError)) {
            return Response.fail(statusCode, statusCode.getDefaultMessage());
        }
        if (StringUtils.hasText(message)) {
            return Response.fail(statusCode, message);
        }
        return Response.fail(statusCode, ex.getMessage());
    }

    @ExceptionHandler(value = {UtilsException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> utilsException(HttpServletRequest request, UtilsException ex) {
        return packageResponse(request, ex, StatusCodeEnum.utilsError);
    }

    @ExceptionHandler(value = {IllegalValidatedException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> illegalValidatedException(HttpServletRequest request, IllegalValidatedException ex) {
        return packageResponse(request, ex, StatusCodeEnum.parameterError);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> illegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) {
        return packageResponse(request, ex, StatusCodeEnum.assertError);
    }

    @ExceptionHandler(value = {BizException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> businessException(HttpServletRequest request, BizException ex) {
        return packageResponse(request, ex, StatusCodeEnum.getEnum(ex.getCode()));
    }

    /**
     * 请求参数解析错误
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> requestException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        return packageResponse(request, ex, StatusCodeEnum.parameterError);
    }

    /**
     * 请求参数解析错误
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> requestException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return packageResponse(request, ex, StatusCodeEnum.parameterError, "请求格式错误，请联系开发人员");
    }

    /**
     * 捕获未定义的异常
     * 所有异常都不直接抛出
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public Response<?> exception(HttpServletRequest request, Exception ex) {
        if (Objects.nonNull(ex)) {
            // 判断是否为数据库操作失败
            String exceptionCp = ex.getClass().getName();
            if (exceptionCp.startsWith("javax.persistence") || exceptionCp.startsWith("org.hibernate")
                    || exceptionCp.startsWith("java.sql") || exceptionCp.startsWith("org.springframework.dao")) {
                return packageResponse(request, ex, StatusCodeEnum.databaseError);
            }
        }
        // 根据更多不同的类型进行返回值的确定
        return packageResponse(request, ex, StatusCodeEnum.unknown);
    }

    /**
     * 处理 json 请求体调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder info = new StringBuilder("请求参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("[%s] %s；", fieldError.getField(), fieldError.getDefaultMessage());
            info.append(msg);
        }
        return packageResponse(request, e, StatusCodeEnum.parameterError, info.toString());
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<?> paramsException(HttpServletRequest request, ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = path.toString().split("\\.");
            message.append(pathArr[1])
                    .append(violation.getMessage())
                    .append("\n");
        }
        return packageResponse(request, e, StatusCodeEnum.parameterError, message.toString());
    }

    /**
     * 处理 form data方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Response<?> formDaraParamsException(HttpServletRequest request, BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + o.getDefaultMessage())
                .collect(Collectors.toList());
        String message = StringUtils.join(collect);
        return packageResponse(request, e, StatusCodeEnum.parameterError, message);
    }

    /**
     * 请求方法不被允许异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<?> httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return packageResponse(request, e, StatusCodeEnum.methodNotAllowed);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response<?> httpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        return packageResponse(request, e, StatusCodeEnum.methodNotAllowed);
    }

    /**
     * 接口不存在跑出异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response<?> noHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        return packageResponse(request, e, StatusCodeEnum.notfound);
    }

    @ExceptionHandler(NotImplementedException.class)
    public Response<?> notImplementedException(HttpServletRequest request, NoHandlerFoundException e) {
        return packageResponse(request, e, StatusCodeEnum.notImplemented);
    }

}
