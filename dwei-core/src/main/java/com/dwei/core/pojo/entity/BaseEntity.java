package com.dwei.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.dwei.common.utils.IdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * 表映射实体基础抽象类
 *
 * @author hww
 */
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public abstract class BaseEntity implements Serializable {

    @TableId
    private Long id;
    /** 创建时间 */
    private Instant createTime;
    /** 更新时间 */
    private Instant updateTime;
    /** 更新时间 */
    private Instant deleteTime;

    @JsonIgnore
    public void initId() {
        if (Objects.isNull(this.id)) this.id = IdUtils.nextId();
    }

    @JsonIgnore
    public void update() {
        initId();
        initCreateTime();
        this.updateTime = Instant.now();
    }

    @JsonIgnore
    public void initCreateTime() {
        if (Objects.isNull(this.createTime)) this.createTime = Instant.now();
    }

    @JsonIgnore
    public void del() {
        if (Objects.isNull(this.deleteTime)) this.deleteTime = Instant.now();
    }

    @JsonIgnore
    public boolean exist() {
        return Objects.isNull(this.deleteTime);
    }

    @Override
    public int hashCode() {
        if (getId() == null) return super.hashCode();
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o instanceof BaseEntity that)) return id != null && Objects.equals(id, that.id);
        return false;
    }

}
