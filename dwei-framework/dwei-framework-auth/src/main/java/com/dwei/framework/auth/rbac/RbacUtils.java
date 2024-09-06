package com.dwei.framework.auth.rbac;

import com.dwei.core.lock.DistributedLock;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RBAC工具类
 *
 * @author hww
 */
@Slf4j
public abstract class RbacUtils {

    private RbacUtils() {

    }

    /**
     * 全量刷新
     *
     * @param compel 是否强制刷新
     */
    public synchronized static void refresh(boolean compel) {
        if (!compel && RedisUtils.support().isExist(RbacConstant.FLAG_KEY)) return;

        DistributedLock distributedLock = SpringContextUtils.getBean(DistributedLock.class);
        distributedLock.tryLock(RbacConstant.LOCK, 1000, () -> {
            log.info("RBAC缓存全量刷新");
            // todo
            return null;
        });
    }

}
