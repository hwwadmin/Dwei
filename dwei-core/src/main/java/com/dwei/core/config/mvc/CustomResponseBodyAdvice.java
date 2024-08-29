package com.dwei.core.config.mvc;

import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.result.Response;
import com.dwei.core.annotation.WrapResponseIgnore;
import com.dwei.core.config.exception.GlobalExceptionHandler;
import com.dwei.core.pojo.response.ListResponse;
import lombok.NonNull;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Collection;

@ControllerAdvice(basePackages = "com.yomi")
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if (method == null) return false;
        WrapResponseIgnore ignore = method.getAnnotation(WrapResponseIgnore.class);
        if (ignore != null) return false;
        Class<?> declaringClass;
        try {
            declaringClass = method.getDeclaringClass();
        } catch (Exception e) {
            return false;
        }
        if (declaringClass == GlobalExceptionHandler.class) return false; // 异常处理结果不统一封装
        return method.getReturnType() != Response.class;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (body instanceof String) return Response.fail(StatusCodeEnum.commonError, "返回体不符合规范");
        if (body instanceof Collection<?>) return Response.success(new ListResponse<>(body));
        return Response.success(body);
    }


    @InitBinder
    public void dataBind(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
