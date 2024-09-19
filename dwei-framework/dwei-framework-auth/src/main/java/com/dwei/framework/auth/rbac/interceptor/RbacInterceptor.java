package com.dwei.framework.auth.rbac.interceptor;

import com.dwei.common.enums.StatusCodeEnum;
import com.dwei.common.exception.IllegalValidatedException;
import com.dwei.common.utils.Lists;
import com.dwei.core.config.interceptor.InterceptorAdaptor;
import com.dwei.core.config.interceptor.InterceptorInfo;
import com.dwei.framework.auth.rbac.RbacManager;
import com.dwei.framework.auth.token.TokenApi;
import com.google.common.collect.ImmutableList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * RBAC拦截器
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RbacInterceptor extends InterceptorAdaptor {

    private static final String httpType4Operation = "OPTIONS";

    // 只设置可能需要的静态文件，如果是接口需要开放使用注解 @SaIgnore
    private static final List<String> notMatchList = ImmutableList.<String>builder()
            .add("*.js")
            .add("*.css")
            .add("*.html")
            .add("*.png")
            .add("*.jpg")
            .add("*.jpeg")
            .build();

    @Value("${dwei.api.ignore}")
    private List<String> notMatchConfig;

    private final RbacManager rbacManager;
    private final TokenApi tokenApi;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (Objects.equals(httpType4Operation, request.getMethod())) return true;

        // 用户认证
        checkUser(request);

        // 权限认证
        checkAuth(request);

        return true;
    }

    /**
     * 用户认证
     */
    private void checkUser(HttpServletRequest request) {
        try {
            tokenApi.checkLogin();
        } catch (Exception e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.invalidToken.getCode(), StatusCodeEnum.invalidToken.getDefaultMessage(), e);
        }
    }

    /**
     * 权限认证
     */
    private void checkAuth(HttpServletRequest request) {
        try {
            rbacManager.check(request, tokenApi.getToken());
        } catch (Exception e) {
            throw IllegalValidatedException.exception(StatusCodeEnum.unauthorized.getCode(), StatusCodeEnum.unauthorized.getDefaultMessage(), e);
        }
    }

    @Override
    public InterceptorInfo interceptorInfo() {
        // 忽略校验的路径
        List<String> ignorePathList = Lists.of();
        ignorePathList.addAll(notMatchList);
        ignorePathList.addAll(notMatchConfig);

        return InterceptorInfo.builder()
                .name("RBAC拦截器")
                .instance(this)
                .order(1)
                .pathPatterns(Lists.of("/**"))
                .excludePathPatterns(ignorePathList)
                .build();
    }

}
