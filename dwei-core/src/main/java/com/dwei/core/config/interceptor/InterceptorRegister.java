package com.dwei.core.config.interceptor;

import java.util.List;

/**
 * 拦截器注册接口
 *
 * @author hww
 */
public interface InterceptorRegister {

    List<InterceptorInfo> getInterceptorInfos();

}
