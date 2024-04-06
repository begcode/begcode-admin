package com.begcode.monolith.system.service.dto;

import com.begcode.monolith.domain.AbstractAuditingEntity;
// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

import com.begcode.monolith.service.dto.BusinessTypeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**

 * {@link com.begcode.monolith.system.domain.FormConfig}的DTO。
 */
@Schema(description = "表单配置")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormConfigDTO extends AbstractAuditingEntity<Long, FormConfigDTO> {

    private Long id;

    /**
     * 表单Key
     */
    @Size(max = 100)
    @Schema(description = "表单Key")
    private String formKey;

    /**
     * 名称
     */
    @Size(max = 100)
    @Schema(description = "名称")
    private String formName;

    /**
     * 表单配置
     */
    @Schema(description = "表单配置")
    private String formJson;

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

    /**
     * 业务类别
     */
    @Schema(description = "业务类别")
    private BusinessTypeDTO businessType;

    private Long businessTypeId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public FormConfigDTO id(Long id) {
        this.id = id;
        return this;
    }

    public FormConfigDTO formKey(String formKey) {
        this.formKey = formKey;
        return this;
    }

    public FormConfigDTO formName(String formName) {
        this.formName = formName;
        return this;
    }

    public FormConfigDTO formJson(String formJson) {
        this.formJson = formJson;
        return this;
    }

    public FormConfigDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public FormConfigDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public FormConfigDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public FormConfigDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public FormConfigDTO businessType(BusinessTypeDTO businessType) {
        this.businessType = businessType;
        return this;
    }

    public FormConfigDTO businessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FormConfigDTO formConfigDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormConfigDTO{" +
            "id=" + getId() +
            ", formKey='" + getFormKey() + "'" +
            ", formName='" + getFormName() + "'" +
            ", formJson='" + getFormJson() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", businessType=" + getBusinessType() +
            "}";
    }
}
