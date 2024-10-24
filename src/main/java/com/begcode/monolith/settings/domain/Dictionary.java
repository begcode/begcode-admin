package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 数据字典
 */
@TableName(value = "dictionary")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字典名称
     */
    @NotNull
    @TableField(value = "dict_name")
    private String dictName;

    /**
     * 字典Key
     */
    @NotNull
    @TableField(value = "dict_key")
    private String dictKey;

    /**
     * 是否禁用
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 排序
     */
    @TableField(value = "sort_value")
    private Integer sortValue;

    /**
     * 是否内置
     */
    @TableField(value = "built_in")
    private Boolean builtIn;

    /**
     * 更新枚举
     */
    @TableField(value = "sync_enum")
    private Boolean syncEnum;

    /**
     * 字典项列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = CommonFieldData.class,
        deepBind = true,
        condition = "id=owner_entity_id AND owner_entity_name = 'Dictionary' ",
        orderBy = "sortValue:ASC"
    )
    @JsonIgnoreProperties(value = { "systemConfig", "dictionary" }, allowSetters = true)
    private List<CommonFieldData> items = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Dictionary id(Long id) {
        this.id = id;
        return this;
    }

    public Dictionary dictName(String dictName) {
        this.dictName = dictName;
        return this;
    }

    public Dictionary dictKey(String dictKey) {
        this.dictKey = dictKey;
        return this;
    }

    public Dictionary disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public Dictionary sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public Dictionary builtIn(Boolean builtIn) {
        this.builtIn = builtIn;
        return this;
    }

    public Dictionary syncEnum(Boolean syncEnum) {
        this.syncEnum = syncEnum;
        return this;
    }

    public Dictionary items(List<CommonFieldData> commonFieldData) {
        this.items = commonFieldData;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dictionary)) {
            return false;
        }
        return getId() != null && getId().equals(((Dictionary) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dictionary{" +
            "id=" + getId() +
            ", dictName='" + getDictName() + "'" +
            ", dictKey='" + getDictKey() + "'" +
            ", disabled='" + getDisabled() + "'" +
            ", sortValue=" + getSortValue() +
            ", builtIn='" + getBuiltIn() + "'" +
            ", syncEnum='" + getSyncEnum() + "'" +
            "}";
    }
}
