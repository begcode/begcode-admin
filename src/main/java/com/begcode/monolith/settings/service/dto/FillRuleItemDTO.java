package com.begcode.monolith.settings.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.enumeration.FieldParamType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.settings.domain.FillRuleItem}的DTO。
 */
@Schema(description = "填充规则条目")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FillRuleItemDTO implements Serializable {

    private Long id;

    /**
     * 排序值
     */
    @Schema(description = "排序值")
    @Excel(name = "排序值")
    private Integer sortValue;

    /**
     * 字段参数类型
     */
    @Schema(description = "字段参数类型")
    @Excel(name = "字段参数类型")
    private FieldParamType fieldParamType;

    /**
     * 字段参数值
     */
    @Schema(description = "字段参数值")
    @Excel(name = "字段参数值")
    private String fieldParamValue;

    /**
     * 日期格式
     */
    @Schema(description = "日期格式")
    @Excel(name = "日期格式")
    private String datePattern;

    /**
     * 序列长度
     */
    @Schema(description = "序列长度")
    @Excel(name = "序列长度")
    private Integer seqLength;

    /**
     * 序列增量
     */
    @Schema(description = "序列增量")
    @Excel(name = "序列增量")
    private Integer seqIncrement;

    /**
     * 序列起始值
     */
    @Schema(description = "序列起始值")
    @Excel(name = "序列起始值")
    private Integer seqStartValue;

    /**
     * 填充规则
     */
    @Schema(description = "填充规则")
    @Excel(name = "填充规则")
    private SysFillRuleDTO fillRule;

    private Long fillRuleId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public FillRuleItemDTO id(Long id) {
        this.id = id;
        return this;
    }

    public FillRuleItemDTO sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public FillRuleItemDTO fieldParamType(FieldParamType fieldParamType) {
        this.fieldParamType = fieldParamType;
        return this;
    }

    public FillRuleItemDTO fieldParamValue(String fieldParamValue) {
        this.fieldParamValue = fieldParamValue;
        return this;
    }

    public FillRuleItemDTO datePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public FillRuleItemDTO seqLength(Integer seqLength) {
        this.seqLength = seqLength;
        return this;
    }

    public FillRuleItemDTO seqIncrement(Integer seqIncrement) {
        this.seqIncrement = seqIncrement;
        return this;
    }

    public FillRuleItemDTO seqStartValue(Integer seqStartValue) {
        this.seqStartValue = seqStartValue;
        return this;
    }

    public FillRuleItemDTO fillRule(SysFillRuleDTO fillRule) {
        this.fillRule = fillRule;
        return this;
    }

    public FillRuleItemDTO fillRuleId(Long fillRuleId) {
        this.fillRuleId = fillRuleId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FillRuleItemDTO fillRuleItemDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fillRuleItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FillRuleItemDTO{" +
            "id=" + getId() +
            ", sortValue=" + getSortValue() +
            ", fieldParamType='" + getFieldParamType() + "'" +
            ", fieldParamValue='" + getFieldParamValue() + "'" +
            ", datePattern='" + getDatePattern() + "'" +
            ", seqLength=" + getSeqLength() +
            ", seqIncrement=" + getSeqIncrement() +
            ", seqStartValue=" + getSeqStartValue() +
            ", fillRule=" + getFillRule() +
            "}";
    }
}
