package com.dwei.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.dwei.core.mvc.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 崩溃日志表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="s_crash_log")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CrashLogEntity extends BaseEntity implements Serializable {

    /**
     * 请求接口uri
     */
    private String uri;

    /**
     * 请求类型
     */
    private String method;

    /**
     * head参数
     */
    private String headParam;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * body参数
     */
    private String bodyParam;

    /**
     * 异常描述
     */
    private String message;

    /**
     * 异常栈信息
     */
    private String stackException;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}