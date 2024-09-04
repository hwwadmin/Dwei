package com.dwei.framework.auth.sa;

import com.dwei.common.utils.Lists;
import com.dwei.core.config.interceptor.InterceptorInfo;
import com.dwei.core.config.interceptor.InterceptorRegister;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SaToken拦截器注册
 *
 * @author hww
 */
@Component
@Slf4j
public class SaTokenInterceptorRegister implements InterceptorRegister {

    @Value("${dwei.api.ignore}")
    private List<String> notMatchConfig;

    private final SaTokenInterceptor interceptor;

    // 只设置可能需要的静态文件，如果是接口需要开放使用注解 @SaIgnore
    private static final List<String> notMatchList = ImmutableList.<String>builder()
            .add("*.js")
            .add("*.css")
            .add("*.html")
            .add("*.png")
            .add("*.jpg")
            .add("*.jpeg")
            .build();

    public SaTokenInterceptorRegister(SaTokenInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public List<InterceptorInfo> getInterceptorInfos() {
        List<InterceptorInfo> interceptorInfos = Lists.of();

        // 忽略校验的路径
        List<String> ignorePathList = Lists.of();
        ignorePathList.addAll(notMatchList);
        ignorePathList.addAll(notMatchConfig);

        interceptorInfos.add(InterceptorInfo.builder()
                .name("SaInterceptor")
                .instance(interceptor)
                .order(1)
                .pathPatterns(Lists.of("/**"))
                .excludePathPatterns(ignorePathList)
                .build());
        return interceptorInfos;
    }

}
