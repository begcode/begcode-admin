package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 短信消息
 */
@TableName(value = "sms_message")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsMessage extends AbstractAuditingEntity<Long, SmsMessage> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 发送方式
     */
    @TableField(value = "send_type")
    private MessageSendType sendType;

    /**
     * 接收人
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 发送所需参数
     * Json格式
     */
    @TableField(value = "params")
    private String params;

    /**
     * 推送内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 推送时间
     */
    @TableField(value = "send_time")
    private ZonedDateTime sendTime;

    /**
     * 推送状态
     */
    @TableField(value = "send_status")
    private SendStatus sendStatus;

    /**
     * 发送次数 超过5次不再发送
     */
    @TableField(value = "retry_num")
    private Integer retryNum;

    /**
     * 推送失败原因
     */
    @TableField(value = "fail_result")
    private String failResult;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SmsMessage id(Long id) {
        this.id = id;
        return this;
    }

    public SmsMessage title(String title) {
        this.title = title;
        return this;
    }

    public SmsMessage sendType(MessageSendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public SmsMessage receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public SmsMessage params(String params) {
        this.params = params;
        return this;
    }

    public SmsMessage content(String content) {
        this.content = content;
        return this;
    }

    public SmsMessage sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public SmsMessage sendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public SmsMessage retryNum(Integer retryNum) {
        this.retryNum = retryNum;
        return this;
    }

    public SmsMessage failResult(String failResult) {
        this.failResult = failResult;
        return this;
    }

    public SmsMessage remark(String remark) {
        this.remark = remark;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((SmsMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsMessage{" +
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
