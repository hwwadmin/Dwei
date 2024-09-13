package com.dwei.common.constants;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 系统级别常量定义
 *
 * @author hww
 */
public interface AppConstants {

    ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);
    ZoneId ZONE_ID = ZONE_OFFSET;
    String GMT8 = "GMT+8"; // 东8区

    /**
     * 空字符串
     */
    String EMPTY_STRING = "";

    /**
     * 空格
     */
    String SPACE_STRING = " ";

    /**
     * 填充字符串
     */
    String FILL_STRING = "-";

    /**
     * 下划线
     */
    String UNDER_LINE = "_";

    /**
     * 加号
     */
    String MARK_ADD = "+";

    /**
     * 错误字符串
     */
    String ERROR_STRING = "x";

    /**
     * null标识
     */
    String MARK_NULL = "null";

    /**
     * 成功字符串
     */
    String SUCCEED_STRING = "succeed";

    /**
     * 问号
     */
    String MARK_QUESTION = "?";

    /**
     * 星号
     */
    String MARK_ASTERISK = "*";

    /**
     * 井号
     */
    String MARK_WELL = "#";

    /**
     * 感叹号
     */
    String MARK_EXCLAMATORY = "!";

    /**
     * &号
     */
    String MARK_AND = "&";

    /**
     * 逗号
     */
    String MARK_DOT = ".";

    /**
     * 冒号
     */
    String MARK_COLON = ":";

    /**
     * 中文句号
     */
    String MARK_PERIOD = "。";

    /**
     * 中文句号
     */
    String MARK_LINE = System.lineSeparator();

    /**
     * =号
     */
    String MARK_EQUAL = "=";

    /**
     * /号
     */
    String MARK_SLANT = "/";

    /**
     * {}号
     */
    String MARK_EMPTY_JSONOBJECT = "{}";

    /**
     * []号
     */
    String MARK_EMPTY_JSONARRAY = "[]";

    /**
     * 两个字符串拼接掩码
     */
    String MARK_TWOSTRING_JOIN = "%s%s";

    /**
     * 换行符，和操作系统走
     */
    String NEWLINE_STRING = System.getProperty("line.separator");

    /**
     * 数字0
     */
    int ZERO_INT = 0;

    /**
     * 数字-1
     */
    int NEGATIVE_ONE_INT = -1;

    /**
     * 数字0
     */
    long ZERO_LONG = 0L;

    /**
     * 未知异常
     */
    String UNKNOWN_THROWABLE = "未知异常";

    /**
     * 日期范围间隔符
     */
    String STRING_RANG_DATE = "至";

    /**
     * true字符串
     */
    String STRING_TRUE = "true";

    /**
     * false字符串
     */
    String STRING_FALSE = "false";

    /**
     * true数据库的数字表达
     */
    String BOOLEAN_TRUE = "1";

    /**
     * 英文逗号
     */
    String STRING_CSV = ",";

    /**
     * 英文左中括号
     */
    String STRING_LEFT_MIDDLE_BRACKETS = "[";

    /**
     * 英文左右括号
     */
    String STRING_RIGHT_MIDDLE_BRACKETS = "]";
    /**
     * 英文分号
     */
    String STRING_SEMICOLON = ";";

    /**
     * 系统默认分隔符
     */
    String APP_SEPARATOR_TOKEN = "*|@#$=)%!";

    /**
     * 校验国际化文件路径
     */
    String VALIDATOR_MESSAGE_PATH = "i18n//messages";

    /**
     * 没有权限操作提示
     */
    String UNAUTHORIZED_OPERATION = "没有权限操作";

    /**
     * 工作区域目录标识
     */
    String WORK_PATH = "${work_path}";

    /**
     * 类目录标识
     */
    String CLASS_PATH = "${class_path}";

    /**
     * 最大逻辑硬盘
     */
    String MAX_DISK = "${max_disk}";

    /**
     * 最大空闲逻辑硬盘
     */
    String MAX_FREE_DISK = "${max_free_disk}";

    /**
     * 一分钟的毫秒数
     */
    long TS_ONE_MINUTE = 60_000L;

    /**
     * 一小时的毫秒数
     */
    long TS_ONE_HOUR = 3_600_000L;

    /**
     * 一天的毫秒数
     */
    long TS_ONE_DATE = 86_400_000L;

    /**
     * 一周的毫秒数
     */
    long TS_ONE_WEEK = 604_800_000L;

    /**
     * 浏览器-IE
     */
    String BROWSER_IE = "msie";

    /**
     * 浏览器-firefox
     */
    String BROWSER_FIREFOX = "firefox";
}
