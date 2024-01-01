package com.begcode.monolith.service.dto;

import com.begcode.monolith.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.UploadImage}的DTO。
 */
@Schema(description = "上传图片")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadImageDTO extends AbstractAuditingEntity<Long, UploadImageDTO> {

    private Long id;

    /**
     * 完整文件名
     * 不含路径
     */
    @Schema(description = "完整文件名\n不含路径")
    private String fullName;

    /**
     * 文件名
     * 不含扩展名
     */
    @Schema(description = "文件名\n不含扩展名")
    private String name;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String ext;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String type;

    /**
     * Web Url地址
     */
    @Schema(description = "Web Url地址")
    private String url;

    /**
     * 本地路径
     */
    @Schema(description = "本地路径")
    private String path;

    /**
     * 本地存储目录
     */
    @Schema(description = "本地存储目录")
    private String folder;

    /**
     * 使用实体名称
     */
    @Schema(description = "使用实体名称")
    private String ownerEntityName;

    /**
     * 使用实体ID
     */
    @Schema(description = "使用实体ID")
    private String ownerEntityId;

    /**
     * 业务标题
     */
    @Schema(description = "业务标题")
    private String businessTitle;

    /**
     * 业务自定义描述内容
     */
    @Schema(description = "业务自定义描述内容")
    private String businessDesc;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    private String businessStatus;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 小图Url
     */
    @Schema(description = "小图Url")
    private String smartUrl;

    /**
     * 中等图Url
     */
    @Schema(description = "中等图Url")
    private String mediumUrl;

    /**
     * 文件被引用次数
     */
    @Schema(description = "文件被引用次数")
    private Long referenceCount;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private ZonedDateTime createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private ZonedDateTime lastModifiedDate;

    /**
     * 所属分类
     */
    @Schema(description = "所属分类")
    private ResourceCategoryDTO category;

    private Long categoryId;

    private MultipartFile image;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public UploadImageDTO id(Long id) {
        this.id = id;
        return this;
    }

    public UploadImageDTO fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UploadImageDTO name(String name) {
        this.name = name;
        return this;
    }

    public UploadImageDTO ext(String ext) {
        this.ext = ext;
        return this;
    }

    public UploadImageDTO type(String type) {
        this.type = type;
        return this;
    }

    public UploadImageDTO url(String url) {
        this.url = url;
        return this;
    }

    public UploadImageDTO path(String path) {
        this.path = path;
        return this;
    }

    public UploadImageDTO folder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadImageDTO ownerEntityName(String ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
        return this;
    }

    public UploadImageDTO ownerEntityId(String ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
        return this;
    }

    public UploadImageDTO businessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
        return this;
    }

    public UploadImageDTO businessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
        return this;
    }

    public UploadImageDTO businessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
        return this;
    }

    public UploadImageDTO createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UploadImageDTO fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public UploadImageDTO smartUrl(String smartUrl) {
        this.smartUrl = smartUrl;
        return this;
    }

    public UploadImageDTO mediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
        return this;
    }

    public UploadImageDTO referenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
        return this;
    }

    public UploadImageDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public UploadImageDTO createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public UploadImageDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public UploadImageDTO lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public UploadImageDTO category(ResourceCategoryDTO category) {
        this.category = category;
        return this;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
