package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 上传文件
 */
@TableName(value = "upload_file")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadFile extends AbstractAuditingEntity<Long, UploadFile> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Url地址
     */
    @NotNull
    @TableField(value = "url")
    private String url;

    /**
     * 完整文件名
     * 不含路径
     */
    @TableField(value = "full_name")
    private String fullName;

    /**
     * 文件名
     * 不含扩展名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 缩略图Url地址
     */
    @TableField(value = "thumb")
    private String thumb;

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
     * 存储目录
     */
    @TableField(value = "folder")
    private String folder;

    /**
     * 实体名称
     */
    @TableField(value = "owner_entity_name")
    private String ownerEntityName;

    /**
     * 使用实体ID
     */
    @TableField(value = "owner_entity_id")
    private Long ownerEntityId;

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
     * 创建时间
     */
    @TableField(value = "create_at")
    private ZonedDateTime createAt;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 被引次数
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

    public UploadFile id(Long id) {
        this.id = id;
        return this;
    }

    public UploadFile url(String url) {
        this.url = url;
        return this;
    }

    public UploadFile fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UploadFile name(String name) {
        this.name = name;
        return this;
    }

    public UploadFile thumb(String thumb) {
        this.thumb = thumb;
        return this;
    }

    public UploadFile ext(String ext) {
        this.ext = ext;
        return this;
    }

    public UploadFile type(String type) {
        this.type = type;
        return this;
    }

    public UploadFile path(String path) {
        this.path = path;
        return this;
    }

    public UploadFile folder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadFile ownerEntityName(String ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
        return this;
    }

    public UploadFile ownerEntityId(Long ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
        return this;
    }

    public UploadFile businessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
        return this;
    }

    public UploadFile businessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
        return this;
    }

    public UploadFile businessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
        return this;
    }

    public UploadFile createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UploadFile fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public UploadFile referenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
        return this;
    }

    public UploadFile category(ResourceCategory resourceCategory) {
        this.category = resourceCategory;
        return this;
    }

    public UploadFile categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadFile)) {
            return false;
        }
        return getId() != null && getId().equals(((UploadFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadFile{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", name='" + getName() + "'" +
            ", thumb='" + getThumb() + "'" +
            ", ext='" + getExt() + "'" +
            ", type='" + getType() + "'" +
            ", path='" + getPath() + "'" +
            ", folder='" + getFolder() + "'" +
            ", ownerEntityName='" + getOwnerEntityName() + "'" +
            ", ownerEntityId=" + getOwnerEntityId() +
            ", businessTitle='" + getBusinessTitle() + "'" +
            ", businessDesc='" + getBusinessDesc() + "'" +
            ", businessStatus='" + getBusinessStatus() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", fileSize=" + getFileSize() +
            ", referenceCount=" + getReferenceCount() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
