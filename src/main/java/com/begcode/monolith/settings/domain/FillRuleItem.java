package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.FieldParamType;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

/**
 * 填充规则条目
 */
@TableName(value = "fill_rule_item")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FillRuleItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 排序值
     */
    @TableField(value = "sort_value")
    private Integer sortValue;

    /**
     * 字段参数类型
     */
    @TableField(value = "field_param_type")
    private FieldParamType fieldParamType;

    /**
     * 字段参数值
     */
    @TableField(value = "field_param_value")
    private String fieldParamValue;

    /**
     * 日期格式
     */
    @TableField(value = "date_pattern")
    private String datePattern;

    /**
     * 序列长度
     */
    @TableField(value = "seq_length")
    private Integer seqLength;

    /**
     * 序列增量
     */
    @TableField(value = "seq_increment")
    private Integer seqIncrement;

    /**
     * 序列起始值
     */
    @TableField(value = "seq_start_value")
    private Integer seqStartValue;

    /**
     * 填充规则
     */
    @TableField(exist = false)
    @BindEntity(entity = SysFillRule.class, condition = "this.fill_rule_id=id")
    @JsonIgnoreProperties(value = { "ruleItems" }, allowSetters = true)
    private SysFillRule fillRule;

    @TableField(value = "fill_rule_id")
    private Long fillRuleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public FillRuleItem id(Long id) {
        this.id = id;
        return this;
    }

    public FillRuleItem sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public FillRuleItem fieldParamType(FieldParamType fieldParamType) {
        this.fieldParamType = fieldParamType;
        return this;
    }

    public FillRuleItem fieldParamValue(String fieldParamValue) {
        this.fieldParamValue = fieldParamValue;
        return this;
    }

    public FillRuleItem datePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public FillRuleItem seqLength(Integer seqLength) {
        this.seqLength = seqLength;
        return this;
    }

    public FillRuleItem seqIncrement(Integer seqIncrement) {
        this.seqIncrement = seqIncrement;
        return this;
    }

    public FillRuleItem seqStartValue(Integer seqStartValue) {
        this.seqStartValue = seqStartValue;
        return this;
    }

    public FillRuleItem fillRule(SysFillRule sysFillRule) {
        this.fillRule = sysFillRule;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FillRuleItem)) {
            return false;
        }
        return getId() != null && getId().equals(((FillRuleItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
