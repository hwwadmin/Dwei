package com.dwei.core.config.mvc.aspect;

import com.dwei.core.annotation.RedisLock;
import com.dwei.core.lock.RedisDistributedLock;
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
class RedisLockAspect {

    public static final String KEY_PREFIX = "byfresh:lock:";
    final ExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Resource
    private RedisDistributedLock redisDistributedLock;

    @Pointcut("@annotation(com.dwei.core.annotation.RedisLock)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doRedisLock(ProceedingJoinPoint point) {
        // valid
        if (!(point.getSignature() instanceof MethodSignature signature))
            throw new IllegalCallerException("@RedisLock 注解只能用于方法上");
        var method = signature.getMethod();
        var redisLock = method.getDeclaredAnnotation(RedisLock.class);
        Object[] args = point.getArgs();
        // generate key
        String key;
        if (redisLock.key().contains("#")) {
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < method.getParameters().length; i++) {
                String name = method.getParameters()[i].getName();
                Object arg = args[i];
                context.setVariable(name, arg);
            }
            Expression expression = spelExpressionParser.parseExpression(redisLock.key());
            key = KEY_PREFIX + expression.getValue(context, String.class);
        } else {
            key = KEY_PREFIX + redisLock.key();
        }
        // try lock
        return redisDistributedLock.tryLock(key, redisLock.expire(), redisLock.waitTime(), redisLock.timeUnit(),
                () -> {
                    try {
                        return point.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
