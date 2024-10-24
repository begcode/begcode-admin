package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 上传图片
 */
@TableName(value = "upload_image")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadImage extends AbstractAuditingEntity<Long, UploadImage> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 完整文件名
     * 不含路径
     */
    @TableField(value = "full_name")
    private String fullName;

    /**
     * 业务标题
     */
    @TableField(value = "business_title")
    private String businessTitle;

    /**
     * 业务自定义描述内容
     */
    @TableField(value = "business_desc")
    private String businessDesc;

    /**
     * 业务状态
     */
    @TableField(value = "business_status")
    private String businessStatus;

    /**
     * Url地址
     */
    @NotNull
    @TableField(value = "url")
    private String url;

    /**
     * 文件名
     * 不含扩展名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 扩展名
     */
    @TableField(value = "ext")
    private String ext;

    /**
     * 文件类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 本地路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 本地存储目录
     */
    @TableField(value = "folder")
    private String folder;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 使用实体名称
     */
    @TableField(value = "owner_entity_name")
    private String ownerEntityName;

    /**
     * 使用实体ID
     */
    @TableField(value = "owner_entity_id")
    private Long ownerEntityId;

    /**
     * 创建时间
     */
    @TableField(value = "create_at")
    private ZonedDateTime createAt;

    /**
     * 小图Url
     */
    @TableField(value = "smart_url")
    private String smartUrl;

    /**
     * 中等图Url
     */
    @TableField(value = "medium_url")
    private String mediumUrl;

    /**
     * 文件被引用次数
     */
    @TableField(value = "reference_count")
    private Long referenceCount;

    /**
     * 所属分类
     */
    @TableField(exist = false)
    @BindEntity(entity = ResourceCategory.class, condition = "this.category_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "images", "files" }, allowSetters = true)
    private ResourceCategory category;

    @TableField(value = "category_id")
    private Long categoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UploadImage id(Long id) {
        this.id = id;
        return this;
    }

    public UploadImage fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UploadImage businessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
        return this;
    }

    public UploadImage businessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
        return this;
    }

    public UploadImage businessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
        return this;
    }

    public UploadImage url(String url) {
        this.url = url;
        return this;
    }

    public UploadImage name(String name) {
        this.name = name;
        return this;
    }

    public UploadImage ext(String ext) {
        this.ext = ext;
        return this;
    }

    public UploadImage type(String type) {
        this.type = type;
        return this;
    }

    public UploadImage path(String path) {
        this.path = path;
        return this;
    }

    public UploadImage folder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadImage fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public UploadImage ownerEntityName(String ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
        return this;
    }

    public UploadImage ownerEntityId(Long ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
        return this;
    }

    public UploadImage createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UploadImage smartUrl(String smartUrl) {
        this.smartUrl = smartUrl;
        return this;
    }

    public UploadImage mediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
        return this;
    }

    public UploadImage referenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
        return this;
    }

    public UploadImage category(ResourceCategory resourceCategory) {
        this.category = resourceCategory;
        return this;
    }

    public UploadImage categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadImage)) {
            return false;
        }
        return getId() != null && getId().equals(((UploadImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadImage{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", businessTitle='" + getBusinessTitle() + "'" +
            ", businessDesc='" + getBusinessDesc() + "'" +
            ", businessStatus='" + getBusinessStatus() + "'" +
            ", url='" + getUrl() + "'" +
            ", name='" + getName() + "'" +
            ", ext='" + getExt() + "'" +
            ", type='" + getType() + "'" +
            ", path='" + getPath() + "'" +
            ", folder='" + getFolder() + "'" +
            ", fileSize=" + getFileSize() +
            ", ownerEntityName='" + getOwnerEntityName() + "'" +
            ", ownerEntityId=" + getOwnerEntityId() +
            ", createAt='" + getCreateAt() + "'" +
            ", smartUrl='" + getSmartUrl() + "'" +
            ", mediumUrl='" + getMediumUrl() + "'" +
            ", referenceCount=" + getReferenceCount() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
