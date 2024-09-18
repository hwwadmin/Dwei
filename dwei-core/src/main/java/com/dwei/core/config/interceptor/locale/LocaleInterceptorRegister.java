package com.dwei.core.config.interceptor.locale;

import com.dwei.common.utils.Lists;
import com.dwei.core.config.interceptor.InterceptorInfo;
import com.dwei.core.config.interceptor.InterceptorRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Locale拦截器注册
 *
 * @author hww
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class LocaleInterceptorRegister implements InterceptorRegister {

    private final LocaleInterceptor localeInterceptor;

    @Override
    public List<InterceptorInfo> getInterceptorInfos() {
        List<InterceptorInfo> interceptorInfos = Lists.of();

        interceptorInfos.add(InterceptorInfo.builder()
                .name("Locale拦截器")
                .instance(localeInterceptor)
                .order(0)
                .pathPatterns(Lists.of("/**"))
                .build());
        return interceptorInfos;
    }

}
