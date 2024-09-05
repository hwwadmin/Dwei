package com.dwei.framework.log;

import com.dwei.core.utils.RedisUtils;

public interface CrashLogConstant {

    String LOCK = RedisUtils.support().format("lock:cl");
    String CRASH_LOG_CACHE_KEY = RedisUtils.support().format("cl:");
    String CRASH_LOG_CACHE_All_PATTER = CRASH_LOG_CACHE_KEY + "*";
    String CRASH_LOG_CACHE_DATA_KEY = CRASH_LOG_CACHE_KEY + "d";

}
