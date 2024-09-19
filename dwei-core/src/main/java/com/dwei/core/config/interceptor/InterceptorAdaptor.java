package com.dwei.core.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 拦截器通用适配
 *
 * @author hww
 */
public abstract class InterceptorAdaptor implements HandlerInterceptor, InterceptorRegister {

    @Override
    public List<InterceptorInfo> getInterceptorInfos() {
        return List.of(interceptorInfo());
    }

    public abstract InterceptorInfo interceptorInfo();

}
