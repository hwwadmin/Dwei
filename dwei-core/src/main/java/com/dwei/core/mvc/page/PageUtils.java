package com.dwei.core.mvc.page;

import com.dwei.common.constants.WebFrameworkConstants;
import com.dwei.common.exception.UtilsException;
import com.dwei.core.utils.RequestUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 分页工具类
 *
 * @author hww
 */
public abstract class PageUtils {

    private PageUtils() {

    }

    /** 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序） */
    private final static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";
    /** 限制orderBy最大长度 */
    private static final int ORDER_BY_MAX_LENGTH = 500;
    /** PageHelper起始页页面为1 */
    private static final int PAGE_NUM_START = 1;
    /** 最小分页5 */
    private static final int PAGE_SIZE_LIMIT_MIN = 5;

    public static void startPage(Integer pageNum, Integer pageSize, String orderBy) {
        if (pageNum == null || pageNum < PAGE_NUM_START) pageNum = PAGE_NUM_START;
        if (pageSize == null || pageSize < PAGE_SIZE_LIMIT_MIN) pageSize = PAGE_SIZE_LIMIT_MIN;
        PageHelper.startPage(pageNum, pageSize, check(orderBy));
    }

    public static void startPage() {
        var pageInfo = getPageDomain();
        startPage(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.buildOrder());
    }

    /**
     * 获取分页参数对象
     */
    public static PageInfo getPageDomain() {
        int pageNum = Optional.ofNullable(RequestUtils.getParameter(WebFrameworkConstants.PAGE_NUM)).map(Integer::parseInt).orElse(PAGE_NUM_START);
        int pageSize = Optional.ofNullable(RequestUtils.getParameter(WebFrameworkConstants.PAGE_SIZE)).map(Integer::parseInt).orElse(PAGE_SIZE_LIMIT_MIN);
        return PageInfo.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .orderByColumn(RequestUtils.getParameter(WebFrameworkConstants.ORDER_BY_COLUMN))
                .isAsc(RequestUtils.getParameter(WebFrameworkConstants.IS_ASC))
                .build();
    }

    /**
     * 检查字符，防止注入绕过
     */
    public static String check(String value) {
        if (StringUtils.isNotBlank(value) && !isValidOrderBySql(value)) {
            throw UtilsException.exception("参数不符合规范，不能进行查询");
        }
        if (StringUtils.length(value) > ORDER_BY_MAX_LENGTH) {
            throw UtilsException.exception("参数已超过最大限制，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

}
