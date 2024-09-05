package com.dwei.framework.dict.utils;

import com.dwei.common.pojo.Pair;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.Maps;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.repository.IDictRepository;
import com.dwei.framework.dict.DictConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典工具类
 *
 * @author hww
 */
@Slf4j
public abstract class DictUtils {

    private DictUtils() {

    }

    /**
     * 根据字典类型编码获取字典
     */
    public static Pair<DictEntity, List<DictDataEntity>> get(String dictCode) {
        var dict = (DictEntity) RedisUtils.support().getOps4hash().get(DictConstant.DICT_CACHE_TYPE_KEY, dictCode);
        if (ObjectUtils.isNull(dict)) return Pair.of();
        //noinspection unchecked
        var dictData = (List<DictDataEntity>) RedisUtils.support().getOps4hash().get(DictConstant.DICT_CACHE_DATA_KEY, dictCode);
        return Pair.of(dict, dictData);
    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public synchronized static void refresh(boolean compel) {
        if (!compel && RedisUtils.support().isExist(DictConstant.DICT_CACHE_FLAG_KEY)) return;

        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        distributedLock.tryLock(DictConstant.LOCK, 1000, () -> {
            log.info("刷新字典缓存");
            IDictRepository dictRepository = SpringContextUtils.getBean(IDictRepository.class);
            IDictDataRepository dictDataRepository = SpringContextUtils.getBean(IDictDataRepository.class);

            var dict = dictRepository.lambdaQuery().eq(DictEntity::getEnable, true).list();
            var dictData = dictDataRepository.lambdaQuery().eq(DictDataEntity::getEnable, true).list();

            RedisUtils.support().deleteKeysByPattern(DictConstant.DICT_CACHE_All_PATTER);
            RedisUtils.support().getOps4str().set(DictConstant.DICT_CACHE_FLAG_KEY, true);
            if (ObjectUtils.nonNull(dict))
                RedisUtils.support().getOps4hash().putAll(DictConstant.DICT_CACHE_TYPE_KEY, Maps.pack(Lists.toMap(dict, DictEntity::getCode)));
            if (ObjectUtils.nonNull(dictData))
                RedisUtils.support().getOps4hash().putAll(DictConstant.DICT_CACHE_DATA_KEY, Maps.pack(Lists.toGroup(dictData, DictDataEntity::getDictCode)));

            return null;
        });
    }

    /**
     * 根据字典类型编码刷新
     */
    public synchronized static void refresh(String dictCode) {
        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        distributedLock.tryLock(DictConstant.LOCK, 1000, () -> {
            log.info("刷新指定字典缓存[{}]", dictCode);
            IDictRepository dictRepository = SpringContextUtils.getBean(IDictRepository.class);
            IDictDataRepository dictDataRepository = SpringContextUtils.getBean(IDictDataRepository.class);

            RedisUtils.support().getOps4hash().del(DictConstant.DICT_CACHE_TYPE_KEY, dictCode);
            RedisUtils.support().getOps4hash().del(DictConstant.DICT_CACHE_DATA_KEY, dictCode);

            var dict = dictRepository.findByCode(dictCode);
            if (ObjectUtils.isNull(dict)) return null;
            if (!dict.getEnable()) return null;
            RedisUtils.support().getOps4hash().put(DictConstant.DICT_CACHE_TYPE_KEY, dictCode, dict);

            var dictData = dictDataRepository.findByDictCode(dictCode)
                    .stream()
                    .filter(DictDataEntity::getEnable)
                    .collect(Collectors.toList());
            if (ObjectUtils.isNull(dictData)) return null;
            RedisUtils.support().getOps4hash().put(DictConstant.DICT_CACHE_DATA_KEY, dictCode, dictData);

            return null;
        });
    }

}
