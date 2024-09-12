package com.dwei.framework.config.utils;

import com.dwei.core.mvc.repository.SimpleCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.mapper.ConfigMapper;
import com.dwei.domain.repository.IConfigRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 参数配置工具类
 *
 * @author hww
 */
@Slf4j
public class ConfigUtils {

    private static final String serviceName = "config";
    private final SimpleCacheRepository<IConfigRepository, ConfigMapper, ConfigEntity> cacheRepository;

    private static class ConfigUtilsHolder {
        private static final ConfigUtils instance = new ConfigUtils();
    }

    private static ConfigUtils getInstance() {
        return ConfigUtilsHolder.instance;
    }

    private ConfigUtils() {
        cacheRepository = new SimpleCacheRepository<>(SpringContextUtils.getBean(IConfigRepository.class),
                serviceName, ConfigEntity::getKey,
                (code, repository) -> Optional.ofNullable(repository.findByKey(code)));
    }

    /**
     * 根据参数键获取参数配置
     */
    public static ConfigEntity get(String key) {
        return getInstance().cacheRepository.get(key);
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public static synchronized void refresh(boolean compel) {
        getInstance().cacheRepository.refresh(compel);
    }

    /**
     * 根据字参数键刷新
     */
    public synchronized static void refresh(String key) {
        getInstance().cacheRepository.refresh(key);
    }

}
