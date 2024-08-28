package com.dwei.common.exception;


import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.utils.StringUtils;

/**
 * 业务异常
 *
 * @author hww
 */
public class BizException extends AbstractRuntimeException {

    public BizException(String detailMessage) {
        super(detailMessage);
    }

    public BizException(Throwable e) {
        super(e);
    }

    public BizException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    private BizException(int code, String detailMessage) {
        super(code, detailMessage);
    }

    private BizException(int code, String detailMessage, Throwable e) {
        super(code, detailMessage, e);
    }

    public static BizException exception(StatusCodeEnum statusCodeEnum) {
        return new BizException(statusCodeEnum.getCode(), statusCodeEnum.getDefaultMessage());
    }

    public static BizException exception(StatusCodeEnum statusCodeEnum, Throwable e) {
        return new BizException(statusCodeEnum.getCode(), statusCodeEnum.getDefaultMessage(), e);
    }

    public static BizException exception(String detailMessage) {
        return new BizException(detailMessage);
    }

    public static BizException exception(String detailMessage, Object... params) {
        return new BizException(StringUtils.format(detailMessage, params));
    }

    public static BizException exception(Throwable e) {
        return new BizException(e);
    }

    public static BizException exception(String detailMessage, Throwable e) {
        return new BizException(detailMessage, e);
    }

    public static BizException exception(int code, String detailMessage, Throwable e) {
        return new BizException(code, detailMessage, e);
    }

}
