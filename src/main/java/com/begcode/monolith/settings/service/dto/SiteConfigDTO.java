package com.begcode.monolith.settings.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.settings.domain.SiteConfig}的DTO。
 */
@Schema(description = "网站配置")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteConfigDTO extends AbstractAuditingEntity<Long, SiteConfigDTO> {

    private Long id;

    /**
     * 分类名称
     */
    @NotNull
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Excel(name = "分类名称")
    private String categoryName;

    /**
     * 分类Key
     */
    @NotNull
    @Schema(description = "分类Key", requiredMode = Schema.RequiredMode.REQUIRED)
    @Excel(name = "分类Key")
    private String categoryKey;

    /**
     * 是否禁用
     */
    @Schema(description = "是否禁用")
    @Excel(name = "是否禁用")
    private Boolean disabled;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @Excel(name = "排序")
    private Integer sortValue;

    /**
     * 是否内置
     */
    @Schema(description = "是否内置")
    @Excel(name = "是否内置")
    private Boolean builtIn;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    @Excel(name = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private Instant createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    @Excel(name = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @Excel(name = "修改时间")
    private Instant lastModifiedDate;

    /**
     * 配置项列表
     */
    @Schema(description = "配置项列表")
    @Excel(name = "配置项列表")
    private List<CommonFieldDataDTO> items = new ArrayList<>();

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public SiteConfigDTO id(Long id) {
        this.id = id;
        return this;
    }

    public SiteConfigDTO categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public SiteConfigDTO categoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    public SiteConfigDTO disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public SiteConfigDTO sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public SiteConfigDTO builtIn(Boolean builtIn) {
        this.builtIn = builtIn;
        return this;
    }

    public SiteConfigDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public SiteConfigDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public SiteConfigDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public SiteConfigDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public SiteConfigDTO items(List<CommonFieldDataDTO> items) {
        this.items = items;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SiteConfigDTO siteConfigDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, siteConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfigDTO{" +
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
            ", items=" + getItems() +
            "}";
    }
}
