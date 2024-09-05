package com.dwei.framework.log;

import com.dwei.core.config.exception.log.AbstractHandlerLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * 崩溃日志处理
 *
 * @author hww
 */
@Component("crashLog")
@RequiredArgsConstructor
public class CrashLog extends AbstractHandlerLog {

    private final CrashLogConsumer crashLogConsumer;

    @Override
    public void doLogHandle(ContentCachingRequestWrapper request, Throwable ex) {
        crashLogConsumer.submit(request, ex);
    }

}
