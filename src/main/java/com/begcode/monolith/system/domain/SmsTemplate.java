package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import com.diboot.core.binding.annotation.*;
import java.io.Serializable;
import lombok.*;

/**
 * 消息模板
 */
@TableName(value = "sms_template")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsTemplate extends AbstractAuditingEntity<Long, SmsTemplate> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板标题
     */
    @TableField(value = "name")
    private String name;

    /**
     * 模板CODE
     */
    @TableField(value = "code")
    private String code;

    /**
     * 通知类型
     */
    @TableField(value = "send_type")
    private MessageSendType sendType;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 模板测试json
     */
    @TableField(value = "test_json")
    private String testJson;

    /**
     * 模板类型
     */
    @TableField(value = "type")
    private SmsTemplateType type;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 启用
     */
    @TableField(value = "enabled")
    private Boolean enabled;

    /**
     * 短信服务商
     */
    @TableField(exist = false)
    @BindEntity(entity = SmsSupplier.class, condition = "this.supplier_id=id")
    private SmsSupplier supplier;

    @TableField(value = "supplier_id")
    private Long supplierId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SmsTemplate id(Long id) {
        this.id = id;
        return this;
    }

    public SmsTemplate name(String name) {
        this.name = name;
        return this;
    }

    public SmsTemplate code(String code) {
        this.code = code;
        return this;
    }

    public SmsTemplate sendType(MessageSendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public SmsTemplate content(String content) {
        this.content = content;
        return this;
    }

    public SmsTemplate testJson(String testJson) {
        this.testJson = testJson;
        return this;
    }

    public SmsTemplate type(SmsTemplateType type) {
        this.type = type;
        return this;
    }

    public SmsTemplate remark(String remark) {
        this.remark = remark;
        return this;
    }

    public SmsTemplate enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SmsTemplate supplier(SmsSupplier smsSupplier) {
        this.supplier = smsSupplier;
        return this;
    }

    public SmsTemplate supplierId(Long supplierId) {
        this.supplierId = supplierId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((SmsTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", sendType='" + getSendType() + "'" +
            ", content='" + getContent() + "'" +
            ", testJson='" + getTestJson() + "'" +
            ", type='" + getType() + "'" +
            ", remark='" + getRemark() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
