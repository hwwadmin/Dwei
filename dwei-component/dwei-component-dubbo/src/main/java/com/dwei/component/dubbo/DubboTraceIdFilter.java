package com.dwei.component.dubbo;

import com.dwei.common.constants.WebFrameworkConstants;
import com.dwei.common.utils.IdUtils;
import com.dwei.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * Dubbo过滤器 配置链路id
 *
 * @author hww
 */
@Activate
@Slf4j
public class DubboTraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        var traceId = RpcContext.getServiceContext().getAttachment(WebFrameworkConstants.TRACE_ID);

        if (ObjectUtils.isNull(traceId)) traceId = MDC.get(WebFrameworkConstants.TRACE_ID);
        if (ObjectUtils.isNull(traceId)) traceId = IdUtils.nextIdStr();

        MDC.put(WebFrameworkConstants.TRACE_ID, traceId);
        RpcContext.getServiceContext().setAttachment(WebFrameworkConstants.TRACE_ID, traceId);

        return invoker.invoke(invocation);
    }

}
