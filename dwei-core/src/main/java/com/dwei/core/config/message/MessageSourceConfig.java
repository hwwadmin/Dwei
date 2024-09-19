package com.dwei.core.config.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@Slf4j
public class MessageSourceConfig {

    private static final String Path = "config/i18n/i18n";
    private static final String Encoding = "UTF-8";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(Path);
        messageSource.setDefaultEncoding(Encoding);
        log.info("i18n配置, Path:[{}], Encoding:[{}]", Path, Encoding);
        return messageSource;
    }

}
