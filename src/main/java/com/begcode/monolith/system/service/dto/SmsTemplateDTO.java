package com.begcode.monolith.system.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.system.domain.SmsTemplate}的DTO。
 */
@Schema(description = "消息模板")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsTemplateDTO extends AbstractAuditingEntity<Long, SmsTemplateDTO> {

    private Long id;

    /**
     * 模板标题
     */
    @Schema(description = "模板标题")
    @Excel(name = "模板标题")
    private String name;

    /**
     * 模板CODE
     */
    @Schema(description = "模板CODE")
    @Excel(name = "模板CODE")
    private String code;

    /**
     * 通知类型
     */
    @Schema(description = "通知类型")
    @Excel(name = "通知类型")
    private MessageSendType sendType;

    /**
     * 模板内容
     */
    @Schema(description = "模板内容")
    @Excel(name = "模板内容")
    private String content;

    /**
     * 模板测试json
     */
    @Schema(description = "模板测试json")
    @Excel(name = "模板测试json")
    private String testJson;

    /**
     * 模板类型
     */
    @Schema(description = "模板类型")
    @Excel(name = "模板类型")
    private SmsTemplateType type;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @Excel(name = "备注")
    private String remark;

    /**
     * 启用
     */
    @Schema(description = "启用")
    @Excel(name = "启用")
    private Boolean enabled;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    @Excel(name = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private Instant createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    @Excel(name = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @Excel(name = "修改时间")
    private Instant lastModifiedDate;

    /**
     * 短信服务商
     */
    @Schema(description = "短信服务商")
    @Excel(name = "短信服务商")
    private SmsSupplierDTO supplier;

    private Long supplierId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public SmsTemplateDTO id(Long id) {
        this.id = id;
        return this;
    }

    public SmsTemplateDTO name(String name) {
        this.name = name;
        return this;
    }

    public SmsTemplateDTO code(String code) {
        this.code = code;
        return this;
    }

    public SmsTemplateDTO sendType(MessageSendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public SmsTemplateDTO content(String content) {
        this.content = content;
        return this;
    }

    public SmsTemplateDTO testJson(String testJson) {
        this.testJson = testJson;
        return this;
    }

    public SmsTemplateDTO type(SmsTemplateType type) {
        this.type = type;
        return this;
    }

    public SmsTemplateDTO remark(String remark) {
        this.remark = remark;
        return this;
    }

    public SmsTemplateDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SmsTemplateDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public SmsTemplateDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public SmsTemplateDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public SmsTemplateDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public SmsTemplateDTO supplier(SmsSupplierDTO supplier) {
        this.supplier = supplier;
        return this;
    }

    public SmsTemplateDTO supplierId(Long supplierId) {
        this.supplierId = supplierId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SmsTemplateDTO smsTemplateDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, smsTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateDTO{" +
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
            ", supplier=" + getSupplier() +
            "}";
    }
}
