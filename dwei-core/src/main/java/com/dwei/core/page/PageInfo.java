package com.dwei.core.page;

import com.dwei.common.utils.ObjectUtils;
import com.dwei.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {

    /** 当前记录起始索引 */
    private Integer pageNum;
    /** 每页显示记录数 */
    private Integer pageSize;
    /** 排序列 */
    private String orderByColumn;
    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    public void setIsAsc(String isAsc) {
        if (ObjectUtils.nonNull(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    /**
     * 构造排序
     */
    public String buildOrder() {
        if (ObjectUtils.isNull(orderByColumn)) return "";
        String orderField = StringUtils.toUnderlineCase(orderByColumn);

        // 单字段排序
        if (!orderField.contains(",")) return orderField + " " + isAsc;

        // 多字段字段排序
        String[] orderFields = orderField.split(",");
        String[] ascList = isAsc.split(",");
        if (orderFields.length != ascList.length) {
            throw new IllegalArgumentException("多字段排序设置错误");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderFields.length ; i ++) {
            if (i > 0) sb.append(",");
            sb.append(orderFields[i]).append(" ").append(ascList[i]);
        }
        return sb.toString();
    }

}
