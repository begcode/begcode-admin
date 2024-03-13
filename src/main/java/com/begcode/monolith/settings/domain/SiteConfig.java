package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 网站配置
 */
@TableName(value = "site_config")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteConfig extends AbstractAuditingEntity<Long, SiteConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @NotNull
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 分类Key
     */
    @NotNull
    @TableField(value = "category_key")
    private String categoryKey;

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
     * 配置项列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = CommonFieldData.class,
        deepBind = true,
        condition = "id=owner_entity_id AND owner_entity_name = 'SiteConfig' ",
        orderBy = "sortValue:ASC"
    )
    @JsonIgnoreProperties(value = { "siteConfig", "dictionary" }, allowSetters = true)
    private List<CommonFieldData> items = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SiteConfig id(Long id) {
        this.id = id;
        return this;
    }

    public SiteConfig categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public SiteConfig categoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    public SiteConfig disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public SiteConfig sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public SiteConfig builtIn(Boolean builtIn) {
        this.builtIn = builtIn;
        return this;
    }

    public SiteConfig items(List<CommonFieldData> commonFieldData) {
        this.items = commonFieldData;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((SiteConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfig{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", categoryKey='" + getCategoryKey() + "'" +
            ", disabled='" + getDisabled() + "'" +
            ", sortValue=" + getSortValue() +
            ", builtIn='" + getBuiltIn() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
