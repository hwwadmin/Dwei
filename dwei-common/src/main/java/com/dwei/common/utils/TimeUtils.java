package com.dwei.common.utils;

import com.dwei.common.enums.DateFormatEnum;
import com.dwei.common.enums.DateUnitEnum;
import com.dwei.common.exception.UtilsException;
import com.google.common.collect.Maps;
import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 时间工具类
 * 主要是对JDK8的time包进行封装
 * 包含对日期、时刻、时间戳的相关操作
 *
 * @author hww
 */
public abstract class TimeUtils {

    // 默认使用中国时区UTC+8
    private static final ZoneOffset zone = ZoneOffset.of("+8");
    // DateTimeFormatter缓存集合，key是字符串pattern
    private static final Map<String, DateTimeFormatter> formatCache = Maps.newHashMap();
    private static final Object sync = new Object();

    private static final String empty = "";

    static {
        // 初始化当前系统通常需要的一些日期格式
        putDateTimeFormatter2Cache(DateFormatEnum.YYMM.getValue());
        putDateTimeFormatter2Cache(DateFormatEnum.YYYYMM.getValue());
        putDateTimeFormatter2Cache(DateFormatEnum.YYYYMMDD.getValue());
        putDateTimeFormatter2Cache(DateFormatEnum.YYYYMMDDHHMMSS.getValue());
        putDateTimeFormatter2Cache(DateFormatEnum.YYYYVMMVDD.getValue());
        putDateTimeFormatter2Cache(DateFormatEnum.YYYYVMMVDDHHSS.getValue());
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        if (!formatCache.containsKey(pattern)) {
            putDateTimeFormatter2Cache(pattern);
        }
        return formatCache.get(pattern);
    }

    private static void putDateTimeFormatter2Cache(String pattern) {
        synchronized (sync) {
            if (formatCache.containsKey(pattern)) {
                return;
            }
            formatCache.put(pattern, DateTimeFormatter.ofPattern(pattern).withZone(zone));
        }
    }


    /**
     * 获取当前时间戳
     */
    public static Long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时刻
     */
    public static Instant getCurrentInstant() {
        return Instant.now();
    }

    /**
     * 获取当前日期
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }


    // region transform

    /**
     * 时间戳转时刻
     */
    public static Instant timestamp2Instant(Long timeStamp) {
        return Objects.nonNull(timeStamp) ? Instant.ofEpochMilli(timeStamp) : null;
    }

    /**
     * 时间戳转日期
     */
    public static LocalDateTime timestamp2Date(Long timeStamp) {
        return Objects.nonNull(timeStamp) ? instant2Date(timestamp2Instant(timeStamp)) : null;
    }

    /**
     * 时刻转时间戳
     */
    public static Long instant2Timestamp(Instant instant) {
        return Objects.nonNull(instant) ? instant.toEpochMilli() : null;
    }

    /**
     * 时刻转日期
     */
    public static LocalDateTime instant2Date(Instant instant) {
        return Objects.nonNull(instant) ? LocalDateTime.ofInstant(instant, zone) : null;
    }

    /**
     * 日期转时间戳
     */
    public static Long date2Timestamp(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) return null;
        Instant instant = date2Instant(localDateTime);
        if (Objects.isNull(instant)) return null;
        return instant.toEpochMilli();
    }

    /**
     * 日期转时刻
     */
    public static Instant date2Instant(LocalDateTime localDateTime) {
        return Objects.nonNull(localDateTime) ? localDateTime.toInstant(zone) : null;
    }

    // endregion

    // region format

    /**
     * 时间戳类型格式化
     */
    public static String format(Long timeStamp, DateFormatEnum pattern) {
        if (Objects.isNull(timeStamp)) return empty;
        Assert.nonNull(pattern);
        return format(timeStamp, pattern.getValue());
    }

    /**
     * 时间戳类型格式化
     */
    public static String format(Long timeStamp, String pattern) {
        if (Objects.isNull(timeStamp)) return empty;
        Assert.isStrNotBlank(pattern);
        Instant instant = timestamp2Instant(timeStamp);
        Assert.nonNull(instant);
        return getDateTimeFormatter(pattern).format(instant);
    }

    /**
     * 时刻类型格式化
     */
    public static String format(Instant instant, DateFormatEnum pattern) {
        if (Objects.isNull(instant)) return empty;
        Assert.nonNull(pattern);
        return format(instant, pattern.getValue());
    }

    /**
     * 时刻类型格式化
     */
    public static String format(Instant instant, String pattern) {
        if (Objects.isNull(instant)) return empty;
        Assert.isStrNotBlank(pattern);
        return getDateTimeFormatter(pattern).format(instant);
    }

    /**
     * 日期类型格式化
     */
    public static String format(LocalDateTime localDateTime, DateFormatEnum pattern) {
        if (Objects.isNull(localDateTime)) return empty;
        Assert.nonNull(pattern);
        return format(localDateTime, pattern.getValue());
    }

    /**
     * 日期类型格式化
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (Objects.isNull(localDateTime)) return empty;
        Assert.isStrNotBlank(pattern);
        return getDateTimeFormatter(pattern).format(localDateTime);
    }

    /**
     * 使用当期时间格式化
     */
    public static String formatNow(DateFormatEnum pattern) {
        Assert.nonNull(pattern);
        return formatNow(pattern.getValue());
    }

    /**
     * 使用当期时间格式化
     */
    public static String formatNow(String pattern) {
        Assert.isStrNotBlank(pattern);
        return format(getCurrentInstant(), pattern);
    }

    // endregion

    // region of

    /**
     * 设置时间
     * 参数需要符合时间规定，如果不符合会引发异常。
     *
     * @param year         年
     * @param month        月份，1-12
     * @param dayOfMonth   日期，31以内
     * @param hour         小时，0-23
     * @param minute       分钟，0-59
     * @param second       秒，0-59
     * @param nanoOfSecond 毫秒，0-999
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second,
                                   int nanoOfSecond) {
        try {
            return LocalDateTime.of(year, Month.of(month), dayOfMonth, hour, minute, second, nanoOfSecond);
        } catch (Exception e) {
            throw UtilsException.exception(String.format("构建日期对象错误：%s %s %s %s %s %s %s", year, month, dayOfMonth,
                    hour, minute, second, nanoOfSecond), e);
        }
    }

    /**
     * 设置时间
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth) {
        return of(year, month, dayOfMonth, 0, 0, 0, 0);
    }

    /**
     * 设置时间
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return of(year, month, dayOfMonth, hour, minute, second, 0);
    }

    // endregion

    // region parse

    /**
     * 解析小时时间，时间格式满足以下格式
     * {@link DateTimeFormatter#ISO_LOCAL_TIME}.
     *
     * @param time 被解析的时间，类似 "10:15:30"
     */
    public static @NonNull Instant parseTime(String time) {
        Assert.nonNull(time, "时间缺失");
        return LocalTime.parse(time).atDate(LocalDate.now()).toInstant(ZoneOffset.ofHours(8));
    }

    // endregion

    /**
     * 获取当前年份
     */
    public static int getYear() {
        return getCurrentDateTime().getYear();
    }

    /**
     * 获取当前月份
     */
    public static int getMonth() {
        return getCurrentDateTime().getMonth().getValue();
    }

    /**
     * 获取当前日期
     */
    public static int getDayOfMonth() {
        return getCurrentDateTime().getDayOfMonth();
    }

    /**
     * 获取当前小时
     */
    public static int getHour() {
        return getCurrentDateTime().getHour();
    }

    /**
     * 获取当前分钟
     */
    public static int getMinute() {
        return getCurrentDateTime().getMinute();
    }

    /**
     * 指定时间偏移
     *
     * @param time   起点时间
     * @param unit   偏移的时间单位
     * @param offset 偏移量
     */
    public static LocalDateTime offset(LocalDateTime time, DateUnitEnum unit, int offset) {
        if (offset == 0) {
            // 偏移量为0直接返回也不需要去抛异常
            return time;
        }
        return switch (unit) {
            case YEAR -> offset > 0 ? time.plusYears(offset) : time.minusYears(-offset);
            case MONTH -> offset > 0 ? time.plusMonths(offset) : time.minusMonths(-offset);
            case DAY -> offset > 0 ? time.plusDays(offset) : time.minusDays(-offset);
            case HOUR -> offset > 0 ? time.plusHours(offset) : time.minusHours(-offset);
            case MINUTE -> offset > 0 ? time.plusMinutes(offset) : time.minusMinutes(-offset);
            case SECOND -> offset > 0 ? time.plusSeconds(offset) : time.minusSeconds(-offset);
            case NANOSECOND -> offset > 0 ? time.plusNanos(offset) : time.minusNanos(-offset);
        };
    }

    /**
     * 判断两个日期相差的时长
     *
     * @param begin 起始日期
     * @param end   结束日期
     * @param unit  相差的单位
     * @return 日期差
     */
    public static long between(LocalDateTime begin, LocalDateTime end, DateUnitEnum unit) {
        return switch (unit) {
            case YEAR -> end.getYear() - begin.getYear();
            case MONTH -> {
                long years = between(begin, end, DateUnitEnum.YEAR);
                yield years * 12 + (end.getMonthValue() - begin.getMonthValue());
            }
            case DAY -> Duration.between(begin, end).toDays();
            case HOUR -> Duration.between(begin, end).toHours();
            case MINUTE -> Duration.between(begin, end).toMinutes();
            case SECOND -> Duration.between(begin, end).toMillis() / 1000;
            case NANOSECOND -> Duration.between(begin, end).toNanos();
            default -> 0L;
        };
    }

    /**
     * 判断两个日期相差的时长
     *
     * @param begin 起始日期
     * @param end   结束日期
     * @param unit  相差的单位
     * @return 日期差
     */
    public static long between(Instant begin, Instant end, DateUnitEnum unit) {
        return between(instant2Date(begin), instant2Date(end), unit);
    }

    /**
     * 时间段是否交集
     */
    public static boolean isOverlap(Instant realStartTime, Instant realEndTime, Instant startTime, Instant endTime) {
        // x>b||a>y 无交集
        // 则有交集的逻辑为 !(x>b||a>y)
        // 根据德摩根公式，可化简为 x<=b && a<=y
        return startTime.isBefore(realEndTime) && endTime.isAfter(realStartTime);
    }

}
