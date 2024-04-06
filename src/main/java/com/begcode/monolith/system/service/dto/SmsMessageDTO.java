package com.begcode.monolith.system.service.dto;

import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.system.domain.SmsMessage}的DTO。
 */
@Schema(description = "短信消息")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsMessageDTO extends AbstractAuditingEntity<Long, SmsMessageDTO> {

    private Long id;

    /**
     * 消息标题
     */
    @Schema(description = "消息标题")
    private String title;

    /**
     * 发送方式
     */
    @Schema(description = "发送方式")
    private MessageSendType sendType;

    /**
     * 接收人
     */
    @Schema(description = "接收人")
    private String receiver;

    /**
     * 发送所需参数
     * Json格式
     */
    @Schema(description = "发送所需参数\nJson格式")
    private String params;

    /**
     * 推送内容
     */
    @Schema(description = "推送内容")
    private String content;

    /**
     * 推送时间
     */
    @Schema(description = "推送时间")
    private ZonedDateTime sendTime;

    /**
     * 推送状态
     */
    @Schema(description = "推送状态")
    private SendStatus sendStatus;

    /**
     * 发送次数 超过5次不再发送
     */
    @Schema(description = "发送次数 超过5次不再发送")
    private Integer retryNum;

    /**
     * 推送失败原因
     */
    @Schema(description = "推送失败原因")
    private String failResult;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

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

    public SmsMessageDTO id(Long id) {
        this.id = id;
        return this;
    }

    public SmsMessageDTO title(String title) {
        this.title = title;
        return this;
    }

    public SmsMessageDTO sendType(MessageSendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public SmsMessageDTO receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public SmsMessageDTO params(String params) {
        this.params = params;
        return this;
    }

    public SmsMessageDTO content(String content) {
        this.content = content;
        return this;
    }

    public SmsMessageDTO sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public SmsMessageDTO sendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public SmsMessageDTO retryNum(Integer retryNum) {
        this.retryNum = retryNum;
        return this;
    }

    public SmsMessageDTO failResult(String failResult) {
        this.failResult = failResult;
        return this;
    }

    public SmsMessageDTO remark(String remark) {
        this.remark = remark;
        return this;
    }

    public SmsMessageDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public SmsMessageDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public SmsMessageDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public SmsMessageDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SmsMessageDTO smsMessageDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, smsMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsMessageDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", sendType='" + getSendType() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", params='" + getParams() + "'" +
            ", content='" + getContent() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            ", sendStatus='" + getSendStatus() + "'" +
            ", retryNum=" + getRetryNum() +
            ", failResult='" + getFailResult() + "'" +
            ", remark='" + getRemark() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
