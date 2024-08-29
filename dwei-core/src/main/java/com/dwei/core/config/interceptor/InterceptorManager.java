package com.dwei.core.config.interceptor;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 拦截器注册管理
 *
 * @author hww
 */
@Component
public class InterceptorManager {

    private final Map<String, InterceptorRegister> interceptorRegisterMap;

    @Getter
    private final List<InterceptorInfo> interceptorInfos = Lists.of();

    public InterceptorManager(Map<String, InterceptorRegister> interceptorRegisterMap) {
        this.interceptorRegisterMap = interceptorRegisterMap;
    }

    @PostConstruct
    public void initialized() {
        if (ObjectUtils.isNull(interceptorRegisterMap)) return;
        interceptorRegisterMap.values().forEach(register -> interceptorInfos.addAll(register.getInterceptorInfos()));
    }

}
