package com.dwei.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * id生成工具类
 * 当前直接封装hutool，有具体需求的时候更换实现即可
 *
 * @author hww
 */
public abstract class IdUtils {

    public static long nextId() {
        return IdUtil.getSnowflake().nextId();
    }

    public static String nextIdStr() {
        return IdUtil.getSnowflake().nextIdStr();
    }

}
