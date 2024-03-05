package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 资源分类
 */
@TableName(value = "resource_category")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @Size(max = 40)
    @TableField(value = "title")
    private String title;

    /**
     * 代码
     */
    @Size(max = 20)
    @TableField(value = "code")
    private String code;

    /**
     * 排序
     */
    @TableField(value = "order_number")
    private Integer orderNumber;

    /**
     * 下级列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = ResourceCategory.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent", "images", "files" }, allowSetters = true)
    private List<ResourceCategory> children = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ResourceCategory.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "images", "files" }, allowSetters = true)
    private ResourceCategory parent;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 图片列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = UploadImage.class, condition = "id=category_id")
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private List<UploadImage> images = new ArrayList<>();

    /**
     * 文件列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = UploadFile.class, condition = "id=category_id")
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private List<UploadFile> files = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ResourceCategory id(Long id) {
        this.id = id;
        return this;
    }

    public ResourceCategory title(String title) {
        this.title = title;
        return this;
    }

    public ResourceCategory code(String code) {
        this.code = code;
        return this;
    }

    public ResourceCategory orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public ResourceCategory children(List<ResourceCategory> resourceCategories) {
        this.children = resourceCategories;
        return this;
    }

    public ResourceCategory parent(ResourceCategory resourceCategory) {
        this.parent = resourceCategory;
        return this;
    }

    public ResourceCategory images(List<UploadImage> uploadImages) {
        this.images = uploadImages;
        return this;
    }

    public ResourceCategory files(List<UploadFile> uploadFiles) {
        this.files = uploadFiles;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((ResourceCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
