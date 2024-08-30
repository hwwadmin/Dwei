package com.dwei.common.enums;

import java.util.Objects;

/**
 * 是否枚举
 *
 * @author hww
 */
public enum YesNoEnum {

    /** 是 */
    Y,
    /** 否 */
    N,
    ;

    public boolean isYes() {
        return Objects.equals(this, Y);
    }

    public boolean isNo() {
        return Objects.equals(this, N);
    }

}
