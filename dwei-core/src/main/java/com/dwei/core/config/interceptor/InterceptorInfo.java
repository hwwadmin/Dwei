package com.dwei.core.config.interceptor;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 拦截器信息
 *
 * @author hww
 */
@Data
@Builder
public class InterceptorInfo {

    /** 拦截器名称 */
    private String name;
    /** 拦截器类 */
    private Class<? extends HandlerInterceptor> clazz;
    /** 拦截器实例 */
    private HandlerInterceptor instance;
    /** 拦截器顺序 */
    private Integer order;
    /** 拦截路径 */
    private List<String> pathPatterns;
    /** 拦截忽略路径 */
    private List<String> excludePathPatterns;

}
