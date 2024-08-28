package com.dwei.common.enums;

import lombok.Getter;

/**
 * 日期类型枚举
 *
 * @author hww
 */
@Getter
public enum DateFormatEnum {

    Y("yyyy"),
    M("MM"),
    D("dd"),
    H("HH"),
    m("mm"),
    YYMM("yyMM"),
    YYMMDD("yyMMdd"),
    HHMMSS("HH:mm:ss"),
    YYYY("yyyy"),
    YYYYMM("yyyyMM"),
    YYYYMMDD("yyyyMMdd"),
    YYYYMMDDHH("yyyyMMdd HH"),
    YYYYMMDDHHMM("yyyyMMdd HH:mm"),
    YYYYMMDDHHMMSS("yyyyMMdd HH:mm:ss"), // recommend to use
    YYYYMMDDHHMMSSS("yyyyMMdd HH:mm:ss:SS"),
    YYYYVMMVDD("yyyy-MM-dd"),
    YYYYVMMVDDHH("yyyy-MM-dd HH"),
    YYYYVMMVDDHHMM("yyyy-MM-dd HH:mm"),
    YYYYVMMVDDHHSS("yyyy-MM-dd HH:mm:ss"), // recommend to use
    YYYYVMMVDDHHSSS("yyyy-MM-dd HH:mm:ss:SS"),
    YYYYLMMLDD("yyyy/MM/dd"),
    YYYYLMMLDDHH("yyyy/MM/dd HH"),
    YYYYLMMLDDHHMM("yyyy/MM/dd HH:mm"),
    YYYYLMMLDDHHSS("yyyy/MM/dd HH:mm:ss"), // recommend to use
    YYYYLMMLDDHHSSS("yyyy/MM/dd HH:mm:ss:SS"),
    YMDHMS("yyyyMMddHHmmss"),
    YMDHMSS("yyyyMMddHHmmssSS"),
    YYVMMVDDHHMM("yy-MM-dd HH:mm"),
    ;

    final String value;

    DateFormatEnum(String value) {
        this.value = value;
    }

}
