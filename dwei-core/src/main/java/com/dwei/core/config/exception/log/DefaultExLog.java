package com.dwei.core.config.exception.log;

import com.dwei.core.utils.CtxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

/**
 * 默认的系统崩溃日志处理
 * 简单打印下文件日志
 */
@Slf4j
@Component("defaultExLog")
public class DefaultExLog extends AbstractHandlerLog {

    @Override
    public void doLogHandle(ContentCachingRequestWrapper request, Throwable ex) {
        String traceId = CtxUtils.getTraceId();
        String sessionId = WebUtils.getSessionId(request);
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("错误信息：traceId:[{}] - sessionId:[{}] - path: [{}] {} - ex:{}",
                traceId, sessionId, method, path, ex.getClass().getName());
        log.error("全局异常：", ex);
    }

    @Override
    protected boolean enableLimit() {
        // 文件日志就不限流了
        return false;
    }

}
