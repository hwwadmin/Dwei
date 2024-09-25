package com.dwei.framework.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.config.exception.log.AbstractHandlerLog;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.CtxUtils;
import com.dwei.core.utils.ExStackUtils;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.RequestUtils;
import com.dwei.domain.entity.CrashLogEntity;
import com.dwei.domain.repository.ICrashLogRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 崩溃日志处理
 *
 * @author hww
 */
@Component("crashLog")
@RequiredArgsConstructor
public class CrashLog extends AbstractHandlerLog {

    private static final String LOCK = RedisUtils.support().format("lock:cl");
    private static final String CRASH_LOG_CACHE_KEY = RedisUtils.support().format("cl:");
    private static final String CRASH_LOG_CACHE_DATA_KEY = CRASH_LOG_CACHE_KEY + "d";

    private final ICrashLogRepository crashLogRepository;
    private final DistributedLock distributedLock;

    @Override
    public void doLogHandle(ContentCachingRequestWrapper request, Throwable ex) {
        submit(request, ex);
    }

    /**
     * 崩溃异常提交
     * 提交给缓存，再由定时器定时批量入库
     */
    public void submit(ContentCachingRequestWrapper request, Throwable ex) {
        var logEntity = new CrashLogEntity();
        logEntity.setUri(request.getRequestURI());
        logEntity.setMethod(request.getMethod());
        logEntity.setTraceId(CtxUtils.getTraceId(request));
        logEntity.setHeadParam(RequestUtils.getHeadParamStr(request));
        logEntity.setRequestParam(RequestUtils.getRequestParamStr(request));
        logEntity.setBodyParam(RequestUtils.getBodyParam(request));
        logEntity.setMessage(ExceptionUtil.getSimpleMessage(ex));
        logEntity.setStackException(ExStackUtils.getStack(ex));

        RedisUtils.support().getOps4list().leftPush(CRASH_LOG_CACHE_DATA_KEY, JsonUtils.bean2JsonStr(logEntity));
    }

    /**
     * 定时消费掉崩溃异常缓存
     */
    @Scheduled(cron = "*/10 * * * * ?")
    protected void consumerCacheScheduled() {
        distributedLock.tryLock(LOCK, 300, () -> {
            List<CrashLogEntity> entities = Lists.newArrayList();
            AtomicInteger seq = new AtomicInteger(100); // 一次消费的最大条数
            while (seq.getAndDecrement() > 0) {
                var cache = RedisUtils.support().getOps4list().rightPop(CRASH_LOG_CACHE_DATA_KEY);
                if (Objects.isNull(cache)) break;
                var logEntity = JsonUtils.jsonStr2Bean((String) cache, CrashLogEntity.class);
                entities.add(logEntity);
            }
            if (ObjectUtils.nonNull(entities)) crashLogRepository.saveBatch(entities);
            return null;
        });
    }

}
