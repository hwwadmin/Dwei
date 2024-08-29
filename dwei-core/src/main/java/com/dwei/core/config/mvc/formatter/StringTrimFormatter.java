package com.dwei.core.config.mvc.formatter;

import com.dwei.common.utils.ObjectUtils;
import com.dwei.common.utils.StringUtils;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * 字符串Trim格式化
 *
 * @author hww
 */
public class StringTrimFormatter implements Formatter<String> {

    @Override
    public String parse(String s, Locale locale) {
        return ObjectUtils.nonNull(s) ? StringUtils.trim(s) : s;
    }

    @Override
    public String print(String s, Locale locale) {
        return ObjectUtils.nonNull(s) ? StringUtils.trim(s) : s;
    }

}
