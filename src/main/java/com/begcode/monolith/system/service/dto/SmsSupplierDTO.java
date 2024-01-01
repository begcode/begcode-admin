package com.begcode.monolith.system.service.dto;

import com.begcode.monolith.domain.enumeration.SmsProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.system.domain.SmsSupplier}的DTO。
 */
@Schema(description = "短信服务商配置")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsSupplierDTO implements Serializable {

    private Long id;

    /**
     * 提供商
     */
    @Schema(description = "提供商")
    private SmsProvider provider;

    /**
     * 配置数据
     */
    @Schema(description = "配置数据")
    private String configData;

    /**
     * 短信签名
     */
    @Schema(description = "短信签名")
    private String signName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 启用
     */
    @Schema(description = "启用")
    private Boolean enabled;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public SmsSupplierDTO id(Long id) {
        this.id = id;
        return this;
    }

    public SmsSupplierDTO provider(SmsProvider provider) {
        this.provider = provider;
        return this;
    }

    public SmsSupplierDTO configData(String configData) {
        this.configData = configData;
        return this;
    }

    public SmsSupplierDTO signName(String signName) {
        this.signName = signName;
        return this;
    }

    public SmsSupplierDTO remark(String remark) {
        this.remark = remark;
        return this;
    }

    public SmsSupplierDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
