package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.BusinessType;
import com.diboot.core.binding.annotation.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.*;

/**
 * 表单配置
 */
@TableName(value = "form_config")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormConfig extends AbstractAuditingEntity<Long, FormConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表单Key
     */
    @Size(max = 100)
    @TableField(value = "form_key")
    private String formKey;

    /**
     * 名称
     */
    @Size(max = 100)
    @TableField(value = "form_name")
    private String formName;

    /**
     * 表单配置
     */
    @TableField(value = "form_json")
    private String formJson;

    /**
     * 业务类别
     */
    @TableField(exist = false)
    @BindEntity(entity = BusinessType.class, condition = "this.business_type_id=id")
    private BusinessType businessType;

    @TableField(value = "business_type_id")
    private Long businessTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FormConfig id(Long id) {
        this.id = id;
        return this;
    }

    public FormConfig formKey(String formKey) {
        this.formKey = formKey;
        return this;
    }

    public FormConfig formName(String formName) {
        this.formName = formName;
        return this;
    }

    public FormConfig formJson(String formJson) {
        this.formJson = formJson;
        return this;
    }

    public FormConfig businessType(BusinessType businessType) {
        this.businessType = businessType;
        return this;
    }

    public FormConfig businessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((FormConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormConfig{" +
            "id=" + getId() +
            ", formKey='" + getFormKey() + "'" +
            ", formName='" + getFormName() + "'" +
            ", formJson='" + getFormJson() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
