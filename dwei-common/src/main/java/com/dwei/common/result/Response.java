package com.dwei.common.result;

import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.exception.AbstractRuntimeException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

/**
 * 通用返回响应对象
 *
 * @author hww
 */
@Getter
@JsonPropertyOrder({"code", "msg", "timestamp", "data"})
public class Response<T> extends AbstractRespResult {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    protected Response(T result) {
        super();
        this.data = result;
    }

    protected Response(StatusCodeEnum statusCode, String message) {
        super(statusCode, message);
    }

    protected Response(int code, String message) {
        super(code, message);
    }

    protected Response(int code, String message, T result) {
        super(code, message);
        this.data = result;
    }

    public static <T> Response<T> success() {
        return new Response<>(StatusCodeEnum.succeed, StatusCodeEnum.succeed.getDefaultMessage());
    }

    public static <T> Response<T> success(String message) {
        return new Response<>(StatusCodeEnum.succeed, message);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(result);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(StatusCodeEnum.commonError, message);
    }

    public static <T> Response<T> fail(StatusCodeEnum statusCode) {
        return new Response<>(statusCode, statusCode.getDefaultMessage());
    }

    public static <T> Response<T> fail(StatusCodeEnum statusCode, String message) {
        return new Response<>(statusCode, message);
    }

    public static <T> Response<T> fail(AbstractRuntimeException e) {
        return new Response<>(e.getCode(), e.getMessage());
    }

}
