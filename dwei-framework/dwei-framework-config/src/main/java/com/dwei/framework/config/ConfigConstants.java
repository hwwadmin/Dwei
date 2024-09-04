package com.dwei.framework.config;

import com.dwei.core.utils.RedisUtils;

public interface ConfigConstants {

    String LOCK = RedisUtils.support().format("lock:config");
    String CONFIG_CACHE_KEY = RedisUtils.support().format("config:");
    String CONFIG_CACHE_All_PATTER = CONFIG_CACHE_KEY + "*";
    String CONFIG_CACHE_FLAG_KEY = CONFIG_CACHE_KEY + "f";
    String CONFIG_CACHE_DATA_KEY = CONFIG_CACHE_KEY + "d";

}
