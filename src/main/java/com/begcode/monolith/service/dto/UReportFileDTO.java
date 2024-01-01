package com.begcode.monolith.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.UReportFile}的DTO。
 */
@Schema(description = "报表存储")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UReportFileDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @NotNull
    @Schema(description = "名称", required = true)
    private String name;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
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

}
