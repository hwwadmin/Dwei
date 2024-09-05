package com.dwei.core.config.mvc.aspect;

import com.dwei.core.utils.CtxUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * http请求 AOP
 *
 * @author hww
 */
@Aspect
@Component
public class RequestAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 添加链路id
        CtxUtils.putTraceId();

        // 处理逻辑需要的时候继续添加，或者从这边开个接口出去让外部模块去实现
        // ...

        return point.proceed();
    }

}
