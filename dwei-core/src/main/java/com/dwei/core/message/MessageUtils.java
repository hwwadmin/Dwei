package com.dwei.core.message;

import com.dwei.common.utils.Assert;
import com.dwei.core.utils.LocaleUtils;
import com.dwei.core.utils.SpringContextUtils;
import org.springframework.context.MessageSource;

/**
 * i18n资源工具类
 *
 * @author hww
 */
public class MessageUtils {

    private MessageUtils() {

    }

    /**
     * 根据消息键和参数获取消息
     *
     * @param code 消息键，且不存在的对应code时即默认为返回的字符串
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        return message(code, code, args);
    }

    /**
     * 根据消息键和参数获取消息
     *
     * @param code 消息键
     * @param defaultMessage 默认字符串
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, String defaultMessage, Object... args) {
        Assert.isStrBlank(code);
        MessageSource messageSource = SpringContextUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, defaultMessage, LocaleUtils.getLocale());
    }

}
