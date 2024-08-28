package com.dwei.common.exception;

import com.dwei.common.utils.StringUtils;

/**
 * 参数校验异常
 *
 * @author hww
 */
public class IllegalValidatedException extends AbstractRuntimeException {

    public IllegalValidatedException(String detailMessage) {
        super(detailMessage);
    }

    public IllegalValidatedException(Throwable e) {
        super(e);
    }

    public IllegalValidatedException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    private IllegalValidatedException(int code, String detailMessage, Throwable e) {
        super(code, detailMessage, e);
    }

    public static IllegalValidatedException exception(String detailMessage) {
        return new IllegalValidatedException(detailMessage);
    }

    public static IllegalValidatedException exception(String detailMessage, Object... params) {
        return new IllegalValidatedException(StringUtils.format(detailMessage, params));
    }

    public static IllegalValidatedException exception(Throwable e) {
        return new IllegalValidatedException(e);
    }

    public static IllegalValidatedException exception(String detailMessage, Throwable e) {
        return new IllegalValidatedException(detailMessage, e);
    }

    public static IllegalValidatedException exception(int code, String detailMessage, Throwable e) {
        return new IllegalValidatedException(detailMessage, e);
    }

}
