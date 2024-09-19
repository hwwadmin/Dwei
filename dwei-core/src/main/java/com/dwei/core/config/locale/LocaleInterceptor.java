package com.dwei.core.config.locale;

import com.dwei.core.utils.LocaleUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Locale拦截器
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        LocaleUtils.initLocale(request, response);
        return true;
    }

}
