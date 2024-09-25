package com.dwei.framework.log;

import com.dwei.core.utils.RedisUtils;

/**
 * CrashLog常量
 *
 * @author hww
 */
public interface CrashLogConstants {

    String LOCK = RedisUtils.support().format("lock:cl");
    String CRASH_LOG_CACHE_KEY = RedisUtils.support().format("cl:");
    String CRASH_LOG_CACHE_DATA_KEY = CRASH_LOG_CACHE_KEY + "d";

}
