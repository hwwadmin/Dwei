package com.dwei.common.result;

import com.dwei.common.constants.AppConstants;
import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.Objects;

/**
 * 响应抽象类
 *
 * @author hww
 */
@Getter
public abstract class AbstractRespResult {

    /**
     * 响应状态
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String msg;

    protected AbstractRespResult() {
        this.code = StatusCodeEnum.succeed.getCode();
        this.msg = StatusCodeEnum.succeed.getDefaultMessage();
    }

    protected AbstractRespResult(int code, String message) {
        super();
        this.code = code;
        if (ObjectUtils.isNull(message)) {
            this.msg = AppConstants.EMPTY_STRING;
        } else {
            this.msg = message;
        }
    }

    protected AbstractRespResult(StatusCodeEnum statusCode, String message) {
        super();
        this.code = statusCode.getCode();
        if (ObjectUtils.isNull(message)) {
            this.msg = statusCode.getDefaultMessage();
        } else {
            this.msg = message;
        }
    }

    @JsonIgnore
    public boolean isSucceed() {
        return Objects.equals(this.code, StatusCodeEnum.succeed.getCode());
    }

}
