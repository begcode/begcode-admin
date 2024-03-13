package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.ResetFrequency;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import lombok.*;

/**
 * 填充规则
 */
@TableName(value = "sys_fill_rule")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysFillRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 规则名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 规则Code
     */
    @TableField(value = "code")
    private String code;

    /**
     * 规则描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 是否启用
     */
    @TableField(value = "enabled")
    private Boolean enabled;

    /**
     * 重置频率
     */
    @TableField(value = "reset_frequency")
    private ResetFrequency resetFrequency;

    /**
     * 序列值
     */
    @TableField(value = "seq_value")
    private Long seqValue;

    /**
     * 生成值
     */
    @TableField(value = "fill_value")
    private String fillValue;

    /**
     * 规则实现类
     */
    @TableField(value = "impl_class")
    private String implClass;

    /**
     * 规则参数
     */
    @TableField(value = "params")
    private String params;

    /**
     * 重置开始日期
     */
    @TableField(value = "reset_start_time")
    private ZonedDateTime resetStartTime;

    /**
     * 重置结束日期
     */
    @TableField(value = "reset_end_time")
    private ZonedDateTime resetEndTime;

    /**
     * 重置时间
     */
    @TableField(value = "reset_time")
    private ZonedDateTime resetTime;

    /**
     * 配置项列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = FillRuleItem.class, deepBind = true, condition = "id=fill_rule_id", orderBy = "sortValue:ASC")
    @JsonIgnoreProperties(value = { "fillRule" }, allowSetters = true)
    private List<FillRuleItem> ruleItems = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SysFillRule id(Long id) {
        this.id = id;
        return this;
    }

    public SysFillRule name(String name) {
        this.name = name;
        return this;
    }

    public SysFillRule code(String code) {
        this.code = code;
        return this;
    }

    public SysFillRule desc(String desc) {
        this.desc = desc;
        return this;
    }

    public SysFillRule enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public SysFillRule resetFrequency(ResetFrequency resetFrequency) {
        this.resetFrequency = resetFrequency;
        return this;
    }

    public SysFillRule seqValue(Long seqValue) {
        this.seqValue = seqValue;
        return this;
    }

    public SysFillRule fillValue(String fillValue) {
        this.fillValue = fillValue;
        return this;
    }

    public SysFillRule implClass(String implClass) {
        this.implClass = implClass;
        return this;
    }

    public SysFillRule params(String params) {
        this.params = params;
        return this;
    }

    public SysFillRule resetStartTime(ZonedDateTime resetStartTime) {
        this.resetStartTime = resetStartTime;
        return this;
    }

    public SysFillRule resetEndTime(ZonedDateTime resetEndTime) {
        this.resetEndTime = resetEndTime;
        return this;
    }

    public SysFillRule resetTime(ZonedDateTime resetTime) {
        this.resetTime = resetTime;
        return this;
    }

    public SysFillRule ruleItems(List<FillRuleItem> fillRuleItems) {
        this.ruleItems = fillRuleItems;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysFillRule)) {
            return false;
        }
        return getId() != null && getId().equals(((SysFillRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRule{" +
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
