package com.dwei.framework.dict.utils;

import com.dwei.common.pojo.Pair;
import com.dwei.core.mvc.repository.ListCacheRepository;
import com.dwei.core.mvc.repository.SimpleCacheRepository;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.mapper.DictDataMapper;
import com.dwei.domain.mapper.DictMapper;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.repository.IDictRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * 字典工具类
 *
 * @author hww
 */
@Slf4j
public class DictUtils {

    private static final String serviceName = "dictType";
    private static final String serviceItemName = "dictData";
    private final SimpleCacheRepository<IDictRepository, DictMapper, DictEntity> cacheRepository;
    private final ListCacheRepository<IDictDataRepository, DictDataMapper, DictDataEntity> cacheItemRepository;

    private static class DictUtilsHolder {
        private static final DictUtils instance = new DictUtils();
    }

    public static DictUtils getInstance() {
        return DictUtilsHolder.instance;
    }

    private DictUtils() {
        cacheRepository = new SimpleCacheRepository<>(
                SpringContextUtils.getBean(IDictRepository.class),
                serviceName,
                DictEntity::getCode,
                (code, repository) -> Optional.ofNullable(repository.findByCode(code)));
        cacheItemRepository = new ListCacheRepository<>(
                SpringContextUtils.getBean(IDictDataRepository.class),
                serviceItemName,
                DictDataEntity::getDictCode,
                (code, repository) -> Optional.ofNullable(repository.findByDictCode(code)));
    }

    /**
     * 根据字典类型编码获取字典
     */
    public static Pair<DictEntity, List<DictDataEntity>> get(String dictCode) {
        return Pair.of(getInstance().cacheRepository.get(dictCode), getInstance().cacheItemRepository.get(dictCode));
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public synchronized static void refresh(boolean compel) {
        getInstance().cacheRepository.refresh(compel);
        getInstance().cacheItemRepository.refresh(compel);
    }

    /**
     * 根据字典类型编码刷新
     */
    public synchronized static void refresh(String dictCode) {
        getInstance().cacheRepository.refresh(dictCode);
        getInstance().cacheItemRepository.refresh(dictCode);
    }

}
