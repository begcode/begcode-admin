package com.begcode.monolith.system.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.system.domain.FormSaveData}的DTO。
 */
@Schema(description = "表单数据")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormSaveDataDTO extends AbstractAuditingEntity<Long, FormSaveDataDTO> {

    private Long id;

    /**
     * 表单数据
     */
    @Schema(description = "表单数据")
    @Excel(name = "表单数据")
    private String formData;

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
     * 表单配置
     */
    @Schema(description = "表单配置")
    @Excel(name = "表单配置")
    private FormConfigDTO formConfig;

    private Long formConfigId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public FormSaveDataDTO id(Long id) {
        this.id = id;
        return this;
    }

    public FormSaveDataDTO formData(String formData) {
        this.formData = formData;
        return this;
    }

    public FormSaveDataDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public FormSaveDataDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public FormSaveDataDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public FormSaveDataDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public FormSaveDataDTO formConfig(FormConfigDTO formConfig) {
        this.formConfig = formConfig;
        return this;
    }

    public FormSaveDataDTO formConfigId(Long formConfigId) {
        this.formConfigId = formConfigId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FormSaveDataDTO formSaveDataDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formSaveDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormSaveDataDTO{" +
            "id=" + getId() +
            ", formData='" + getFormData() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", formConfig=" + getFormConfig() +
            "}";
    }
}
