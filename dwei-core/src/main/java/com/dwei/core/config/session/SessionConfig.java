package com.dwei.core.config.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启用session-redis配置
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {

}
