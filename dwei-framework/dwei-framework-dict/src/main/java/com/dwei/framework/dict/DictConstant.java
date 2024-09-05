package com.dwei.framework.dict;

import com.dwei.core.utils.RedisUtils;

/**
 * 字典常量
 *
 * @author hww
 */
public interface DictConstant {

    String LOCK = RedisUtils.support().format("lock:dict");
    String DICT_CACHE_KEY = RedisUtils.support().format("dict:");
    String DICT_CACHE_All_PATTER = DICT_CACHE_KEY + "*";
    String DICT_CACHE_FLAG_KEY = DICT_CACHE_KEY + "f";
    String DICT_CACHE_TYPE_KEY = DICT_CACHE_KEY + "t";
    String DICT_CACHE_DATA_KEY = DICT_CACHE_KEY + "d";

}
