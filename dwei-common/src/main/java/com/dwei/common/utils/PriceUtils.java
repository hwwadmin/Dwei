package com.dwei.common.utils;

import cn.hutool.core.util.NumberUtil;
import com.dwei.common.enums.CurrencyEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 价格工具类
 *
 * @author hww
 */
public abstract class PriceUtils {

    /**
     * 价格保留指定小数位
     *
     * @param amount 价格
     * @param scale  保留的小数位
     */
    public static BigDecimal scale(BigDecimal amount, int scale) {
        return amount.setScale(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * 价格换算
     *
     * @param amount 价格
     * @param source 价格当前单位
     * @param target 价格换算后的单位
     * @param scale  价格换算后保留的小数位
     */
    public static BigDecimal transition(BigDecimal amount, CurrencyEnum source, CurrencyEnum target, int scale) {
        var rate = NumberUtil.div(source.getRate(), target.getRate());
        var result = NumberUtil.mul(amount, rate);
        return scale(result, scale);
    }

    /**
     * 元 -> 角
     */
    public static BigDecimal yuan2jiao(BigDecimal yuan) {
        return transition(yuan, CurrencyEnum.YUAN, CurrencyEnum.JIAO, 1);
    }

    /**
     * 元 -> 分
     */
    public static BigDecimal yuan2fen(BigDecimal yuan) {
        return transition(yuan, CurrencyEnum.YUAN, CurrencyEnum.FEN, 0);
    }

    /**
     * 角 -> 分
     */
    public static BigDecimal jiao2fen(BigDecimal jiao) {
        return transition(jiao, CurrencyEnum.JIAO, CurrencyEnum.FEN, 1);
    }

    /**
     * 分 -> 元
     */
    public static BigDecimal fen2yuan(BigDecimal fen) {
        return transition(fen, CurrencyEnum.FEN, CurrencyEnum.YUAN, 2);
    }

    /**
     * 分 -> 角
     */
    public static BigDecimal fen2jiao(BigDecimal fen) {
        return transition(fen, CurrencyEnum.FEN, CurrencyEnum.JIAO, 1);
    }

    /**
     * 角 -> 元
     */
    public static BigDecimal jiao2yuan(BigDecimal jiao) {
        return transition(jiao, CurrencyEnum.JIAO, CurrencyEnum.YUAN, 1);
    }

}
