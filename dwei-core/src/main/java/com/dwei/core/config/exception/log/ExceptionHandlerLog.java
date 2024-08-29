package com.dwei.core.config.exception.log;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 系统崩溃日志处理接口
 *
 * @author hww
 */
public interface ExceptionHandlerLog {

    void log(HttpServletRequest request, Throwable ex);

}
