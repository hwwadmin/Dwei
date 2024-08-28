package com.dwei.common.enums;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 响应状态通用枚举
 * 其中600以前为保留代码 系统重要使用800以后的代码
 * 具体的业务的响应码由各自服务的枚举类定义
 *
 * @author hww
 */
@Getter
public enum StatusCodeEnum {

    succeed(200, "success"),
    // 通用错误码,
    commonError(400, "通用异常"),
    loginError(401, "登陆失败"),
    invalidToken(401, "登陆失效"),
    forbidden(403, "暂无权限访问"),
    unauthorized(403, "用户没有操作权限"),
    notfound(404, "访问资源不存在"),
    methodNotAllowed(405, "错误的请求方式"),
    parameterError(412, "参数校验异常"),
    assertError(412, "业务处理所需资源校验异常"),
    tooManyRequest(429, "请求过多被限流"),
    notImplemented(501, "功能未实现"),

    // 系统错误码
    unknown(-1, "服务器开小差，请稍后再试"),
    componentBoot(-3, "组件引导启动失败"),
    utilsError(604, "帮助通用方法异常"),
    serviceError(607, "系统处理业务逻辑异常"),
    databaseError(608, "数据库操作失败"),
    signatureError(609, "数据签名错误"),
    convertError(610, "数据对象转换异常"),
    tokenError(611, "授权令牌错误"),
    ipError(612, "IP地址防火墙不允许访问"),
    apiTransactionError(613, "事务编号已经存在"),
    storageError(614, "保存数据异常"),

    // 业务错误码

    ;

    final int code;
    final String defaultMessage;

    StatusCodeEnum(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    private static final Cache<Integer, StatusCodeEnum> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();

    public static StatusCodeEnum getEnum(int code) {
        StatusCodeEnum codeEnum = cache.getIfPresent(code);
        if (codeEnum != null) {
            return codeEnum;
        }

        for (StatusCodeEnum anEnum : StatusCodeEnum.values()) {
            if (Objects.equals(code, anEnum.getCode())) {
                cache.put(code, anEnum);
                return anEnum;
            }
        }
        return StatusCodeEnum.commonError;
    }

}
