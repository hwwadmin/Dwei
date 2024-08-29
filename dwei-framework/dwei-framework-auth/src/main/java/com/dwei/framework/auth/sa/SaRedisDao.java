package com.dwei.framework.auth.sa;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.util.SaFoxUtil;
import com.dwei.common.constants.AppConstants;
import com.dwei.core.redis.RedisSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于redis的令牌仓库实现
 *
 * @author hww
 */
@Component
public class SaRedisDao implements SaTokenDao {

    @Value("${sa-token.group:dwei}")
    private String group;

    private final RedisSupport redisSupport;

    public SaRedisDao(RedisSupport redisSupport) {
        this.redisSupport = redisSupport;
    }

    private String addGroupKey(String key) {
        return group + AppConstants.MARK_COLON + key;
    }

    @Override
    public SaSession getSession(String sessionId) {
        return redisSupport.getOps4str().get(addGroupKey(sessionId), SaSession.class);
    }

    @Override
    public String get(String key) {
        return redisSupport.getOps4str().get(addGroupKey(key));
    }

    @Override
    public void set(String key, String value, long timeout) {
        redisSupport.getOps4str().set(addGroupKey(key), value, timeout);
    }

    @Override
    public void update(String key, String value) {
        redisSupport.getOps4str().set(addGroupKey(key), value);
    }

    @Override
    public void delete(String key) {
        redisSupport.del(addGroupKey(key));
    }

    @Override
    public long getTimeout(String key) {
        var expire = redisSupport.getRedisTemplate().getExpire(addGroupKey(key));
        if (expire == null) return -1L;
        return expire;
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        redisSupport.expire(addGroupKey(key), timeout);
    }

    @Override
    public Object getObject(String key) {
        return redisSupport.getOps4str().get(addGroupKey(key));
    }

    @Override
    public void setObject(String key, Object object, long timeout) {
        redisSupport.getOps4str().set(addGroupKey(key), object, timeout);
    }

    @Override
    public void updateObject(String key, Object object) {
        redisSupport.getOps4str().set(addGroupKey(key), object);
    }

    @Override
    public void deleteObject(String key) {
        this.delete(key);
    }

    @Override
    public long getObjectTimeout(String key) {
        return this.getTimeout(key);
    }

    @Override
    public void updateObjectTimeout(String key, long timeout) {
        this.updateTimeout(key, timeout);
    }

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        var key = prefix + "*" + keyword + "*";
        List<String> keys = redisSupport.getOps4special().keys(addGroupKey(key));
        return SaFoxUtil.searchList(keys, start, size, sortType);
    }

}
