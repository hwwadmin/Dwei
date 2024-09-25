package com.dwei.framework.log;

import com.dwei.common.utils.JsonUtils;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.RedisUtils;
import com.dwei.domain.entity.CrashLogEntity;
import com.dwei.domain.repository.ICrashLogRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CrashLog定时任务
 *
 * @author hww
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CrashLogTask {

    private final ICrashLogRepository crashLogRepository;
    private final DistributedLock distributedLock;

    @Scheduled(cron = "*/10 * * * * ?")
    public void doTask() {
        taskExecute();
    }

    protected void taskExecute() {
        distributedLock.tryLock(CrashLogConstants.LOCK, 300, () -> {
            List<CrashLogEntity> entities = Lists.newArrayList();
            AtomicInteger seq = new AtomicInteger(100); // 一次消费的最大条数
            while (seq.getAndDecrement() > 0) {
                var cache = RedisUtils.support().getOps4list().rightPop(CrashLogConstants.CRASH_LOG_CACHE_DATA_KEY);
                if (Objects.isNull(cache)) break;
                var logEntity = JsonUtils.jsonStr2Bean((String) cache, CrashLogEntity.class);
                entities.add(logEntity);
            }
            if (ObjectUtils.nonNull(entities)) crashLogRepository.saveBatch(entities);
            return null;
        });
    }

}
