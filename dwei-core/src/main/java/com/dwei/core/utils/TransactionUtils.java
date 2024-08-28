package com.dwei.core.utils;

import com.dwei.common.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * 显示（编程式）事务工具类
 * 基于spring的TransactionTemplate进行封装
 * 主要应用在无法依赖@Transactional，需要显示创建事务的场景时候使用
 *
 * @author hww
 */
@Slf4j
public class TransactionUtils {

    private static PlatformTransactionManager transactionManager;

    public static PlatformTransactionManager getPlatformTransactionManager() {
        if (Objects.nonNull(transactionManager)) return transactionManager;
        synchronized (TransactionUtils.class) {
            if (Objects.nonNull(transactionManager)) return transactionManager;
            transactionManager = SpringContextUtils.getBean(PlatformTransactionManager.class);
            return transactionManager;
        }
    }

    public static TransactionTemplate getTransactionTemplate(TransactionDefinition transactionDefinition) {
        Assert.nonNull(transactionDefinition, "事务配置缺失");
        return new TransactionTemplate(getPlatformTransactionManager(), transactionDefinition);
    }

    public static TransactionDefinition getDefaultTransactionDefinition() {
        // 使用默认的事务配置，和@Transactional的默认配置一致
        return new DefaultTransactionDefinition();
    }

    public static <T> T execute(Callable<T> main) {
        return execute(null, main, null);
    }

    public static <T> T execute(Callable<T> main, Function<Exception, T> error) {
        return execute(null, main, error);
    }

    public static <T> T execute(TransactionDefinition transactionDefinition, Callable<T> main) {
        return execute(transactionDefinition, main, null);
    }

    public static <T> T execute(TransactionDefinition transactionDefinition, Callable<T> main, Function<Exception, T> error) {
        Assert.nonNull(main, "事务执行函数缺失");
        var transactionDefine = Objects.nonNull(transactionDefinition) ? transactionDefinition : getDefaultTransactionDefinition();
        return getTransactionTemplate(transactionDefine)
                .execute(transactionStatus -> {
                    try {
                        return main.call();
                    } catch (Exception e) {
                        log.error("事务执行异常", e);
                        transactionStatus.setRollbackOnly();
                        if (Objects.isNull(error)) return null;
                        try {
                            return error.apply(e);
                        } catch (Exception ex) {
                            log.error("事务异常处理函数执行异常", ex);
                            return null;
                        }
                    }
                });
    }

}
