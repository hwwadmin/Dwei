package com.dwei.core.config;

import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.config.interceptor.InterceptorInfo;
import com.dwei.core.config.interceptor.InterceptorManager;
import com.dwei.core.config.mvc.formatter.StringTrimFormatterFactory;
import com.dwei.core.utils.SpringContextUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.dwei.common.jackson.serializer.*;
import com.dwei.common.jackson.deserializer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import java.sql.Time;
import java.time.Instant;
import java.util.List;

/**
 * mvc配置器
 *
 * @author hww
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final InterceptorManager interceptorManager;

    /**
     * 前端静态资源目录配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/views/**").addResourceLocations("classpath:/views/");
    }

    /**
     * 设置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }

    /**
     * 添加拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<InterceptorInfo> interceptorInfos = interceptorManager.getInterceptorInfos();
        if (ObjectUtils.isNull(interceptorInfos)) return;
        // 拦截器注册
        interceptorInfos.forEach(t -> {
            HandlerInterceptor interceptor = t.getInstance();
            if (ObjectUtils.isNull(interceptor)) interceptor = SpringContextUtils.getBean(t.getInterceptor());
            InterceptorRegistration interceptorRegistration = registry.addInterceptor(interceptor);
            if (ObjectUtils.nonNull(t.getOrder())) interceptorRegistration.order(t.getOrder());
            if (ObjectUtils.nonNull(t.getPathPatterns())) interceptorRegistration.addPathPatterns(t.getPathPatterns());
            if (ObjectUtils.nonNull(t.getExcludePathPatterns())) interceptorRegistration.excludePathPatterns(t.getExcludePathPatterns());
            log.info("[{}]拦截器注册成功", t.getName());
        });
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new StringTrimFormatterFactory());
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        ObjectMapper objectMapper = JsonUtils.createObjectMapper(false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        SimpleModule module = new SimpleModule();
        // Serializer
        module.addSerializer(Long.class, new Long2StringSerializer());
        module.addSerializer(Instant.class, new Instant2LongSerializer());
//        module.addSerializer(BigDecimal.class, new BigDecimal2StringScale2Serializer());
//        module.addSerializer(BaseEnum.class, new BaseEnumSerializer());
//        module.addSerializer(Boolean.class, new Boolean2IntSerializer());
        // Deserializer
        module.addDeserializer(Boolean.class, new Int2BooleanDeserializer());
        module.addDeserializer(Integer.class, new Boolean2IntDeserializer());
        module.addDeserializer(Time.class, new String2TimeDeserializer());
        module.addDeserializer(Instant.class, new String2InstantDeserializer());
//        module.addDeserializer(String.class, new StringNullDeserializer());
        objectMapper.registerModule(module);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
