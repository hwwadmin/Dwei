package com.dwei.core.utils;

import com.dwei.common.constants.WebFrameworkConstants;
import org.slf4j.MDC;

/**
 * 上下文工具类
 *
 * @author hww
 */
public abstract class CtxUtils {

    /**
     * 获取项目名
     */
    public static String getProjectName() {
        return SpringContextUtils.getConfig("spring.application.name", "dwei");
    }

    /**
     * 设置链路id
     */
    public static void putTraceId(String traceId) {
        MDC.put(WebFrameworkConstants.TRACE_ID, traceId);
    }

    /**
     * 获取链路id
     */
    public static String getTraceId() {
        return MDC.get(WebFrameworkConstants.TRACE_ID);
    }

}
