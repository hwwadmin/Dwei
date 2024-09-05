package com.dwei.framework.config;

import com.dwei.core.utils.RedisUtils;

public interface ConfigConstants {

    String CACHE_PREFIX = RedisUtils.support().format("config:");
    String LOCK = CACHE_PREFIX + "lock";
    String DATA = CACHE_PREFIX + "data:";
    String All_PATTER = DATA + "*";
    String FLAG_KEY = DATA + "f";
    String DATA_KEY = DATA + "d";

}
