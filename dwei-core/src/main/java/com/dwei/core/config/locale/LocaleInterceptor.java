package com.dwei.core.config.locale;

import com.dwei.common.utils.Lists;
import com.dwei.core.config.interceptor.InterceptorAdaptor;
import com.dwei.core.config.interceptor.InterceptorInfo;
import com.dwei.core.utils.LocaleUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Locale拦截器
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LocaleInterceptor extends InterceptorAdaptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        LocaleUtils.initLocale(request, response);
        return true;
    }

    @Override
    public InterceptorInfo interceptorInfo() {
        return InterceptorInfo.builder()
                .name("Locale拦截器")
                .instance(this)
                .order(0)
                .pathPatterns(Lists.of("/**"))
                .build();
    }

}
