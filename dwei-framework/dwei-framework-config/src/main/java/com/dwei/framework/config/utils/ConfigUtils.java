package com.dwei.framework.config.utils;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.Maps;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.repository.IConfigRepository;
import com.dwei.framework.config.ConfigConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 参数配置工具类
 *
 * @author hww
 */
@Slf4j
public abstract class ConfigUtils {

    private ConfigUtils() {

    }

    /**
     * 根据参数键获取参数配置
     */
    public static ConfigEntity get(String key) {
        return (ConfigEntity) RedisUtils.support().getOps4hash().get(ConfigConstants.CONFIG_CACHE_DATA_KEY, key);
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public static synchronized void refresh(boolean compel) {
        if (!compel && RedisUtils.support().isExist(ConfigConstants.CONFIG_CACHE_FLAG_KEY)) return;

        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        distributedLock.tryLock(ConfigConstants.LOCK, 1000, () -> {
            log.info("系统参数配置全量刷新");
            IConfigRepository repository = SpringContextUtils.getBean(IConfigRepository.class);
            List<ConfigEntity> configs = repository.list();

            RedisUtils.support().deleteKeysByPattern(ConfigConstants.CONFIG_CACHE_All_PATTER);
            RedisUtils.support().getOps4str().set(ConfigConstants.CONFIG_CACHE_FLAG_KEY, true);
            RedisUtils.support().getOps4hash().putAll(ConfigConstants.CONFIG_CACHE_DATA_KEY, Maps.pack(Lists.toMap(configs, ConfigEntity::getKey)));

            return null;
        });
    }

    /**
     * 根据字参数键刷新
     */
    public synchronized static void refresh(String key) {
        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        distributedLock.tryLock(ConfigConstants.LOCK, 1000, () -> {
            log.info("刷新指定参数键缓存[{}]", key);
            IConfigRepository repository = SpringContextUtils.getBean(IConfigRepository.class);

            RedisUtils.support().getOps4hash().del(ConfigConstants.CONFIG_CACHE_DATA_KEY, key);

            var config = repository.findByKey(key);
            if (ObjectUtils.isNull(config)) return null;
            RedisUtils.support().getOps4hash().put(ConfigConstants.CONFIG_CACHE_DATA_KEY, key, config);

            return null;
        });
    }

}
