package com.begcode.monolith.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.ResourceCategory}的DTO。
 */
@Schema(description = "资源分类")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceCategoryDTO implements Serializable {

    private Long id;

    /**
     * 标题
     */
    @Size(max = 40)
    @Schema(description = "标题")
    @Excel(name = "标题")
    private String title;

    /**
     * 代码
     */
    @Size(max = 20)
    @Schema(description = "代码")
    @Excel(name = "代码")
    private String code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @Excel(name = "排序")
    private Integer orderNumber;

    /**
     * 下级列表
     */
    @Schema(description = "下级列表")
    @Excel(name = "下级列表")
    private List<ResourceCategoryDTO> children = new ArrayList<>();

    /**
     * 上级
     */
    @Schema(description = "上级")
    @Excel(name = "上级")
    private ResourceCategoryDTO parent;

    private Long parentId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public ResourceCategoryDTO id(Long id) {
        this.id = id;
        return this;
    }

    public ResourceCategoryDTO title(String title) {
        this.title = title;
        return this;
    }

    public ResourceCategoryDTO code(String code) {
        this.code = code;
        return this;
    }

    public ResourceCategoryDTO orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public ResourceCategoryDTO children(List<ResourceCategoryDTO> children) {
        this.children = children;
        return this;
    }

    public ResourceCategoryDTO parent(ResourceCategoryDTO parent) {
        this.parent = parent;
        return this;
    }

    public ResourceCategoryDTO parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ResourceCategoryDTO resourceCategoryDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCategoryDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", code='" + getCode() + "'" +
            ", orderNumber=" + getOrderNumber() +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
