package com.dwei.common.enums;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 货币类型枚举
 *
 * @author hww
 */
@Getter
public enum CurrencyEnum {

    // 元
    YUAN(new BigDecimal(100)),
    // 角
    JIAO(new BigDecimal(10)),
    // 分
    FEN(new BigDecimal(1)),
    ;

    // 和分的换算比
    private final BigDecimal rate;

    CurrencyEnum(BigDecimal rate) {
        this.rate = rate;
    }

}
