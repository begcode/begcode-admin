package com.begcode.monolith.settings.service.dto;

import com.begcode.monolith.domain.enumeration.ResetFrequency;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.settings.domain.SysFillRule}的DTO。
 */
@Schema(description = "填充规则")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysFillRuleDTO implements Serializable {

    private Long id;

    /**
     * 规则名称
     */
    @Schema(description = "规则名称")
    private String name;

    /**
     * 规则Code
     */
    @Schema(description = "规则Code")
    private String code;

    /**
     * 规则描述
     */
    @Schema(description = "规则描述")
    private String desc;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enabled;

    /**
     * 重置频率
     */
    @Schema(description = "重置频率")
    private ResetFrequency resetFrequency;

    /**
     * 序列值
     */
    @Schema(description = "序列值")
    private Long seqValue;

    /**
     * 生成值
     */
    @Schema(description = "生成值")
    private String fillValue;

    /**
     * 规则实现类
     */
    @Schema(description = "规则实现类")
    private String implClass;

    /**
     * 规则参数
     */
    @Schema(description = "规则参数")
    private String params;

    /**
     * 重置开始日期
     */
    @Schema(description = "重置开始日期")
    private ZonedDateTime resetStartTime;

    /**
     * 重置结束日期
     */
    @Schema(description = "重置结束日期")
    private ZonedDateTime resetEndTime;

    /**
     * 重置时间
     */
    @Schema(description = "重置时间")
    private ZonedDateTime resetTime;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public SysFillRuleDTO id(Long id) {
        this.id = id;
        return this;
    }

    public SysFillRuleDTO name(String name) {
        this.name = name;
        return this;
    }

    public SysFillRuleDTO code(String code) {
        this.code = code;
        return this;
    }

    public SysFillRuleDTO desc(String desc) {
        this.desc = desc;
        return this;
    }

    public SysFillRuleDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SysFillRuleDTO resetFrequency(ResetFrequency resetFrequency) {
        this.resetFrequency = resetFrequency;
        return this;
    }

    public SysFillRuleDTO seqValue(Long seqValue) {
        this.seqValue = seqValue;
        return this;
    }

    public SysFillRuleDTO fillValue(String fillValue) {
        this.fillValue = fillValue;
        return this;
    }

    public SysFillRuleDTO implClass(String implClass) {
        this.implClass = implClass;
        return this;
    }

    public SysFillRuleDTO params(String params) {
        this.params = params;
        return this;
    }

    public SysFillRuleDTO resetStartTime(ZonedDateTime resetStartTime) {
        this.resetStartTime = resetStartTime;
        return this;
    }

    public SysFillRuleDTO resetEndTime(ZonedDateTime resetEndTime) {
        this.resetEndTime = resetEndTime;
        return this;
    }

    public SysFillRuleDTO resetTime(ZonedDateTime resetTime) {
        this.resetTime = resetTime;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SysFillRuleDTO sysFillRuleDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sysFillRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", desc='" + getDesc() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", resetFrequency='" + getResetFrequency() + "'" +
            ", seqValue=" + getSeqValue() +
            ", fillValue='" + getFillValue() + "'" +
            ", implClass='" + getImplClass() + "'" +
            ", params='" + getParams() + "'" +
            ", resetStartTime='" + getResetStartTime() + "'" +
            ", resetEndTime='" + getResetEndTime() + "'" +
            ", resetTime='" + getResetTime() + "'" +
            "}";
    }
}
