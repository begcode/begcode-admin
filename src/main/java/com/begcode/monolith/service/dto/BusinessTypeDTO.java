package com.begcode.monolith.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.BusinessType}的DTO。
 */
@Schema(description = "业务类型")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessTypeDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 代码
     */
    @Schema(description = "代码")
    private String code;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public BusinessTypeDTO id(Long id) {
        this.id = id;
        return this;
    }

    public BusinessTypeDTO name(String name) {
        this.name = name;
        return this;
    }

    public BusinessTypeDTO code(String code) {
        this.code = code;
        return this;
    }

    public BusinessTypeDTO description(String description) {
        this.description = description;
        return this;
    }

    public BusinessTypeDTO icon(String icon) {
        this.icon = icon;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
