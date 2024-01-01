package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.CommonFieldType;
import com.diboot.core.binding.annotation.*;
import java.io.Serializable;
import lombok.*;

/**
 * 通用字段数据
 */
@TableName(value = "common_field_data")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommonFieldData implements Serializable {

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
     * 字段值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 字段标题
     */
    @TableField(value = "label")
    private String label;

    /**
     * 字段类型
     */
    @TableField(value = "value_type")
    private CommonFieldType valueType;

    /**
     * 说明
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 排序
     */
    @TableField(value = "sort_value")
    private Integer sortValue;

    /**
     * 是否禁用
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 实体名称
     */
    @TableField(value = "owner_entity_name")
    private String ownerEntityName;

    /**
     * 使用实体ID
     */
    @TableField(value = "owner_entity_id")
    private String ownerEntityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public CommonFieldData id(Long id) {
        this.id = id;
        return this;
    }

    public CommonFieldData name(String name) {
        this.name = name;
        return this;
    }

    public CommonFieldData value(String value) {
        this.value = value;
        return this;
    }

    public CommonFieldData label(String label) {
        this.label = label;
        return this;
    }

    public CommonFieldData valueType(CommonFieldType valueType) {
        this.valueType = valueType;
        return this;
    }

    public CommonFieldData remark(String remark) {
        this.remark = remark;
        return this;
    }

    public CommonFieldData sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public CommonFieldData disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public CommonFieldData ownerEntityName(String ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
        return this;
    }

    public CommonFieldData ownerEntityId(String ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonFieldData)) {
            return false;
        }
        return getId() != null && getId().equals(((CommonFieldData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
