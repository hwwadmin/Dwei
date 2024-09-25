package com.dwei.framework.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.dwei.common.utils.JsonUtils;
import com.dwei.core.config.exception.log.AbstractHandlerLog;
import com.dwei.core.utils.CtxUtils;
import com.dwei.core.utils.ExStackUtils;
import com.dwei.core.utils.RedisUtils;
import com.dwei.core.utils.RequestUtils;
import com.dwei.domain.entity.CrashLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * 崩溃日志处理
 *
 * @author hww
 */
@Component("crashLog")
@RequiredArgsConstructor
public class CrashLog extends AbstractHandlerLog {

    @Override
    public void doLogHandle(ContentCachingRequestWrapper request, Throwable ex) {
        var logEntity = new CrashLogEntity();
        logEntity.setUri(request.getRequestURI());
        logEntity.setMethod(request.getMethod());
        logEntity.setTraceId(CtxUtils.getTraceId(request));
        logEntity.setHeadParam(RequestUtils.getHeadParamStr(request));
        logEntity.setRequestParam(RequestUtils.getRequestParamStr(request));
        logEntity.setBodyParam(RequestUtils.getBodyParam(request));
        logEntity.setMessage(ExceptionUtil.getSimpleMessage(ex));
        logEntity.setStackException(ExStackUtils.getStack(ex));

        // 提交给redis缓存，再由定时器定时批量消费redis入库
        RedisUtils.support().getOps4list().leftPush(CrashLogConstants.CRASH_LOG_CACHE_DATA_KEY, JsonUtils.bean2JsonStr(logEntity));
    }

}
