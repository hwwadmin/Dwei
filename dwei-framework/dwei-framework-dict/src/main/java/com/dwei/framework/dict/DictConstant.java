package com.dwei.framework.dict;

import com.dwei.core.utils.RedisUtils;

/**
 * 字典常量
 *
 * @author hww
 */
public interface DictConstant {

    String CACHE_PREFIX = RedisUtils.support().format("dict:");
    String LOCK = CACHE_PREFIX + "lock";
    String DATA = CACHE_PREFIX + "data:";
    String All_PATTER = DATA + "*";
    String FLAG_KEY = DATA + "f";
    String TYPE_KEY = DATA + "t";
    String DATA_KEY = DATA + "d";

}
