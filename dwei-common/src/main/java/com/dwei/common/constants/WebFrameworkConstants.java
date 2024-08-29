package com.dwei.common.constants;

/**
 * WEB框架常量定义
 *
 * @author hww
 */
public interface WebFrameworkConstants {

    /**
     * 系统名称
     */
    String SYSTEM_NAME = "Zwei";

    /**
     * 日志跟踪ID
     */
    String TRACE_ID = "traceId";

    /**
     * 未知用户编号，默认为-1
     */
    long UNKNOWN_USER_ID = -1;

    /**
     * 系统用户编号
     */
    long SYSTEM_USER_ID = 0;

    /**
     * 未知终端类型
     */
    String UNKNOWN_TERMINAL_TYPE = "-1";

    /**
     * 当前记录起始索引
     */
    String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向
     * "desc" 或者 "asc".
     */
    String IS_ASC = "isAsc";

}
