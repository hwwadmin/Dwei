package com.dwei.core.config.mvc.formatter;

import com.dwei.core.annotation.StringTrim;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.HashSet;
import java.util.Set;

/**
 * 字符串Trim格式化工厂
 *
 * @author hww
 */
public class StringTrimFormatterFactory implements AnnotationFormatterFactory<StringTrim> {

    private final StringTrimFormatter formatter;

    public StringTrimFormatterFactory() {
        this.formatter = new StringTrimFormatter();
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(String.class);
        return fieldTypes;
    }

    @Override
    public Printer<?> getPrinter(StringTrim stringTrim, Class<?> aClass) {
        return this.formatter;
    }

    @Override
    public Parser<?> getParser(StringTrim stringTrim, Class<?> aClass) {
        return this.formatter;
    }

}
