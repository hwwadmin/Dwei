package com.dwei.core.utils;

import com.dwei.common.constants.WebFrameworkConstants;
import com.dwei.common.utils.IdUtils;
import com.dwei.common.utils.ObjectUtils;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
    public static void putTraceId() {
        var headParamMap = RequestUtils.getHeadParam();
        String traceId = headParamMap.containsKey(WebFrameworkConstants.TRACE_ID) ?
                headParamMap.get(WebFrameworkConstants.TRACE_ID).getFirst() : IdUtils.nextIdStr();
        putTraceId(traceId);
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
        return getTraceId(null);
    }

    /**
     * 获取链路
     * 优先从指定的request获取
     */
    public static String getTraceId(ContentCachingRequestWrapper request) {
        String traceId = null;

        // read by request
        if (ObjectUtils.nonNull(request)) {
            try {
                traceId = request.getHeader(WebFrameworkConstants.TRACE_ID);
            } catch (Exception e) {
                //
            }
        }

        // read by MDC
        if (ObjectUtils.nonNull(traceId)) {
            try {
                traceId = MDC.get(WebFrameworkConstants.TRACE_ID);
            } catch (Exception e) {
                //
            }
        }

        return traceId;
    }

}
