package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.AnnoBusinessType;
import com.begcode.monolith.domain.enumeration.AnnoCategory;
import com.begcode.monolith.domain.enumeration.AnnoOpenType;
import com.begcode.monolith.domain.enumeration.AnnoSendStatus;
import com.begcode.monolith.domain.enumeration.PriorityLevel;
import com.begcode.monolith.domain.enumeration.ReceiverType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 系统通告
 */
@TableName(value = "announcement")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Announcement extends AbstractAuditingEntity<Long, Announcement> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @TableField(value = "id")
    private Long id;

    /**
     * 标题
     */
    @NotNull
    @TableField(value = "title")
    private String title;

    /**
     * 摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private ZonedDateTime startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private ZonedDateTime endTime;

    /**
     * 发布人Id
     */
    @TableField(value = "sender_id")
    private Long senderId;

    /**
     * 优先级
     * （L低，M中，H高）
     */
    @TableField(value = "priority")
    private PriorityLevel priority;

    /**
     * 消息类型
     * 通知公告,系统消息
     */
    @TableField(value = "category")
    private AnnoCategory category;

    /**
     * 通告对象类型
     * （USER:指定用户，ALL:全体用户）
     */
    @NotNull
    @TableField(value = "receiver_type")
    private ReceiverType receiverType;

    /**
     * 发布状态
     *
     */
    @TableField(value = "send_status")
    private AnnoSendStatus sendStatus;

    /**
     * 发布时间
     */
    @TableField(value = "send_time")
    private ZonedDateTime sendTime;

    /**
     * 撤销时间
     */
    @TableField(value = "cancel_time")
    private ZonedDateTime cancelTime;

    /**
     * 业务类型
     * (email:邮件 bpm:流程)
     */
    @TableField(value = "business_type")
    private AnnoBusinessType businessType;

    /**
     * 业务id
     */
    @TableField(value = "business_id")
    private Long businessId;

    /**
     * 打开方式
     */
    @TableField(value = "open_type")
    private AnnoOpenType openType;

    /**
     * 组件/路由 地址
     */
    @TableField(value = "open_page")
    private String openPage;

    /**
     * 指定接收者id
     */
    @TableField(value = "receiver_ids")
    private String receiverIds;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Announcement id(Long id) {
        this.id = id;
        return this;
    }

    public Announcement title(String title) {
        this.title = title;
        return this;
    }

    public Announcement summary(String summary) {
        this.summary = summary;
        return this;
    }

    public Announcement content(String content) {
        this.content = content;
        return this;
    }

    public Announcement startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Announcement endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Announcement senderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public Announcement priority(PriorityLevel priority) {
        this.priority = priority;
        return this;
    }

    public Announcement category(AnnoCategory category) {
        this.category = category;
        return this;
    }

    public Announcement receiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
        return this;
    }

    public Announcement sendStatus(AnnoSendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public Announcement sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public Announcement cancelTime(ZonedDateTime cancelTime) {
        this.cancelTime = cancelTime;
        return this;
    }

    public Announcement businessType(AnnoBusinessType businessType) {
        this.businessType = businessType;
        return this;
    }

    public Announcement businessId(Long businessId) {
        this.businessId = businessId;
        return this;
    }

    public Announcement openType(AnnoOpenType openType) {
        this.openType = openType;
        return this;
    }

    public Announcement openPage(String openPage) {
        this.openPage = openPage;
        return this;
    }

    public Announcement receiverIds(String receiverIds) {
        this.receiverIds = receiverIds;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Announcement)) {
            return false;
        }
        return getId() != null && getId().equals(((Announcement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
