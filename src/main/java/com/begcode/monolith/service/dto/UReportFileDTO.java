package com.begcode.monolith.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.UReportFile}的DTO。
 */
@Schema(description = "报表存储")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UReportFileDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Excel(name = "名称")
    private String name;

    /**
     * 内容
     */
    @Schema(description = "内容")
    @Excel(name = "内容")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Excel(name = "更新时间")
    private ZonedDateTime updateAt;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public UReportFileDTO id(Long id) {
        this.id = id;
        return this;
    }

    public UReportFileDTO name(String name) {
        this.name = name;
        return this;
    }

    public UReportFileDTO content(String content) {
        this.content = content;
        return this;
    }

    public UReportFileDTO createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UReportFileDTO updateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof UReportFileDTO uReportFileDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uReportFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UReportFileDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
