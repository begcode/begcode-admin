package com.begcode.monolith.system.service.dto;

import com.begcode.monolith.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.system.domain.AnnouncementRecord}的DTO。
 */
@Schema(description = "通告阅读记录")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnouncementRecordDTO extends AbstractAuditingEntity<Long, AnnouncementRecordDTO> {

    private Long id;

    /**
     * 通告ID
     */
    @Schema(description = "通告ID")
    private Long anntId;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 是否已读
     */
    @Schema(description = "是否已读")
    private Boolean hasRead;

    /**
     * 阅读时间
     */
    @Schema(description = "阅读时间")
    private ZonedDateTime readTime;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Instant createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private Instant lastModifiedDate;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public AnnouncementRecordDTO id(Long id) {
        this.id = id;
        return this;
    }

    public AnnouncementRecordDTO anntId(Long anntId) {
        this.anntId = anntId;
        return this;
    }

    public AnnouncementRecordDTO userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public AnnouncementRecordDTO hasRead(Boolean hasRead) {
        this.hasRead = hasRead;
        return this;
    }

    public AnnouncementRecordDTO readTime(ZonedDateTime readTime) {
        this.readTime = readTime;
        return this;
    }

    public AnnouncementRecordDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public AnnouncementRecordDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public AnnouncementRecordDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public AnnouncementRecordDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
