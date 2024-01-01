package com.begcode.monolith.oss.service.dto;

import com.begcode.monolith.domain.enumeration.OssProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.oss.domain.OssConfig}的DTO。
 */
@Schema(description = "对象存储配置")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OssConfigDTO implements Serializable {

    private Long id;

    /**
     * 提供商
     */
    @NotNull
    @Schema(description = "提供商", required = true)
    private OssProvider provider;

    /**
     * 平台
     */
    @NotNull
    @Size(max = 40)
    @Schema(description = "平台", required = true)
    private String platform;

    /**
     * 启用
     */
    @Schema(description = "启用")
    private Boolean enabled;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 配置数据
     */
    @Schema(description = "配置数据")
    private String configData;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public OssConfigDTO id(Long id) {
        this.id = id;
        return this;
    }

    public OssConfigDTO provider(OssProvider provider) {
        this.provider = provider;
        return this;
    }

    public OssConfigDTO platform(String platform) {
        this.platform = platform;
        return this;
    }

    public OssConfigDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public OssConfigDTO remark(String remark) {
        this.remark = remark;
        return this;
    }

    public OssConfigDTO configData(String configData) {
        this.configData = configData;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
