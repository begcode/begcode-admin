package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 通告阅读记录
 */
@TableName(value = "announcement_record")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnouncementRecord extends AbstractAuditingEntity<Long, AnnouncementRecord> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通告ID
     */
    @TableField(value = "annt_id")
    private Long anntId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否已读
     */
    @TableField(value = "has_read")
    private Boolean hasRead;

    /**
     * 阅读时间
     */
    @TableField(value = "read_time")
    private ZonedDateTime readTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public AnnouncementRecord id(Long id) {
        this.id = id;
        return this;
    }

    public AnnouncementRecord anntId(Long anntId) {
        this.anntId = anntId;
        return this;
    }

    public AnnouncementRecord userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public AnnouncementRecord hasRead(Boolean hasRead) {
        this.hasRead = hasRead;
        return this;
    }

    public AnnouncementRecord readTime(ZonedDateTime readTime) {
        this.readTime = readTime;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnouncementRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((AnnouncementRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
