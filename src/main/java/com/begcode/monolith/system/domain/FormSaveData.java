package com.begcode.monolith.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

/**
 * 表单数据
 */
@TableName(value = "form_save_data")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormSaveData extends AbstractAuditingEntity<Long, FormSaveData> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 表单数据
     */
    @TableField(value = "form_data")
    private String formData;

    /**
     * 表单配置
     */
    @TableField(exist = false)
    @BindEntity(entity = FormConfig.class, condition = "this.form_config_id=id")
    @JsonIgnoreProperties(value = { "businessType" }, allowSetters = true)
    private FormConfig formConfig;

    @TableField(value = "form_config_id")
    private Long formConfigId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FormSaveData id(Long id) {
        this.id = id;
        return this;
    }

    public FormSaveData formData(String formData) {
        this.formData = formData;
        return this;
    }

    public FormSaveData formConfig(FormConfig formConfig) {
        this.formConfig = formConfig;
        return this;
    }

    public FormSaveData formConfigId(Long formConfigId) {
        this.formConfigId = formConfigId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormSaveData)) {
            return false;
        }
        return getId() != null && getId().equals(((FormSaveData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormSaveData{" +
            "id=" + getId() +
            ", formData='" + getFormData() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
