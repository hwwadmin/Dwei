package com.dwei.core.config.mvc.processor;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnWebApplication
public class ConfigArgumentResolvers {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public ConfigArgumentResolvers(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @PostConstruct
    public void addArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        if (resolvers == null) return;
        ArrayList<HandlerMethodArgumentResolver> argumentResolvers = Lists.newArrayList(resolvers);
        for (int i = 0; i < resolvers.size(); i++) {
            HandlerMethodArgumentResolver resolver = resolvers.get(i);
            if (resolver instanceof RequestResponseBodyMethodProcessor processor) {
                argumentResolvers.add(i, new RequestBodyMethodProcessor(processor));
                requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
                return;
            }
        }
    }

}
