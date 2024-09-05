package com.dwei.core.config.mvc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 耗时计算注解支持类
 *
 * @author hww
 */
@Slf4j
@Aspect
@Component
public class CostTimeAspect {

    @Pointcut("@annotation(com.dwei.core.annotation.CostTime)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        logCostTime(point, time);
        return result;
    }

    /**
     * todo 超时时间配置
     *
     * @param time cost time
     */
    private void logCostTime(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        if (time > 200L) {
            log.warn("请求超过200ms class:" + className + " method:" + methodName + " cost:" + time + "ms");
        } else {
            log.info("class:" + className + " method:" + methodName + " cost:" + time + "ms");
        }
    }

}
