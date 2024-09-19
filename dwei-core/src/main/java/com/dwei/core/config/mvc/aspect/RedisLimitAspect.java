package com.dwei.core.config.mvc.aspect;

import com.dwei.core.annotation.RedisLimit;
import com.dwei.core.redis.RedisRateLimiter;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
class RedisLimitAspect {

    final ExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Resource
    private RedisRateLimiter redisRateLimiter;

    @Pointcut("@annotation(com.dwei.core.annotation.RedisLimit)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doRedisLimit(ProceedingJoinPoint point) {
        // valid
        if (!(point.getSignature() instanceof MethodSignature signature))
            throw new IllegalCallerException("@RedisLock 注解只能用于方法上");
        Object[] args = point.getArgs();
        var method = signature.getMethod();
        var redisLimit = method.getDeclaredAnnotation(RedisLimit.class);
        // generate key
        String key;
        if (redisLimit.key().contains("#")) {
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < method.getParameters().length; i++) {
                String name = method.getParameters()[i].getName();
                Object arg = args[i];
                context.setVariable(name, arg);
            }
            Expression expression = spelExpressionParser.parseExpression(redisLimit.key());
            key = expression.getValue(context, String.class);
        } else {
            key = redisLimit.key();
        }
        // limit
        return redisRateLimiter.acquire(key, redisLimit.replenishRate(), redisLimit.burstCapacity(),
                redisLimit.requestedTokens(), redisLimit.ttl(), () -> {
                    try {
                        return point.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
