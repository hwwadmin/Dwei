package com.dwei.framework.auth.rbac;

import com.dwei.core.utils.RedisUtils;

/**
 * RBAC常量
 *
 * @author hww
 */
public interface RbacConstant {

    String CACHE_PREFIX = RedisUtils.support().format("rbac:");
    String LOCK = CACHE_PREFIX + "lock";
    String DATA = CACHE_PREFIX + "data:";
    String All_PATTER = DATA + "*";
    String FLAG_KEY = DATA + "f";
    String TYPE_KEY = DATA + "t";
    String DATA_KEY = DATA + "d";

}
