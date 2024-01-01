package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import lombok.*;

/**
 * 业务类型
 */
@TableName(value = "business_type")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @TableField(value = "id")
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public BusinessType id(Long id) {
        this.id = id;
        return this;
    }

    public BusinessType name(String name) {
        this.name = name;
        return this;
    }

    public BusinessType code(String code) {
        this.code = code;
        return this;
    }

    public BusinessType description(String description) {
        this.description = description;
        return this;
    }

    public BusinessType icon(String icon) {
        this.icon = icon;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessType)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
