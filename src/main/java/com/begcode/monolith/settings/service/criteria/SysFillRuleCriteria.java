package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.ResetFrequency;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.SysFillRule} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.SysFillRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sys-fill-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysFillRuleCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SysFillRuleCriteria and;

    @BindQuery(ignore = true)
    private SysFillRuleCriteria or;

    /**
     * Class for filtering ResetFrequency
     */
    public static class ResetFrequencyFilter extends Filter<ResetFrequency> {

        public ResetFrequencyFilter() {}

        public ResetFrequencyFilter(String value) {
            ResetFrequency enumValue = ResetFrequency.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = ResetFrequency.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public ResetFrequencyFilter(ResetFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public ResetFrequencyFilter copy() {
            return new ResetFrequencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.`desc`")
    private StringFilter desc;

    @BindQuery(column = "self.enabled")
    private BooleanFilter enabled;

    @BindQuery(column = "self.reset_frequency")
    private ResetFrequencyFilter resetFrequency;

    @BindQuery(column = "self.seq_value")
    private LongFilter seqValue;

    @BindQuery(column = "self.fill_value")
    private StringFilter fillValue;

    @BindQuery(column = "self.impl_class")
    private StringFilter implClass;

    @BindQuery(column = "self.params")
    private StringFilter params;

    @BindQuery(column = "self.reset_start_time")
    private ZonedDateTimeFilter resetStartTime;

    @BindQuery(column = "self.reset_end_time")
    private ZonedDateTimeFilter resetEndTime;

    @BindQuery(column = "self.reset_time")
    private ZonedDateTimeFilter resetTime;

    @BindQuery(entity = FillRuleItem.class, column = "id", condition = "id=fill_rule_id")
    private LongFilter ruleItemsId;

    @BindQuery(entity = FillRuleItem.class, column = "date_pattern", condition = "id=fill_rule_id")
    private StringFilter ruleItemsDatePattern;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SysFillRuleCriteria() {}

    public SysFillRuleCriteria(SysFillRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.desc = other.desc == null ? null : other.desc.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.resetFrequency = other.resetFrequency == null ? null : other.resetFrequency.copy();
        this.seqValue = other.seqValue == null ? null : other.seqValue.copy();
        this.fillValue = other.fillValue == null ? null : other.fillValue.copy();
        this.implClass = other.implClass == null ? null : other.implClass.copy();
        this.params = other.params == null ? null : other.params.copy();
        this.resetStartTime = other.resetStartTime == null ? null : other.resetStartTime.copy();
        this.resetEndTime = other.resetEndTime == null ? null : other.resetEndTime.copy();
        this.resetTime = other.resetTime == null ? null : other.resetTime.copy();
        this.ruleItemsId = other.ruleItemsId == null ? null : other.ruleItemsId.copy();
        this.ruleItemsDatePattern = other.ruleItemsDatePattern == null ? null : other.ruleItemsDatePattern.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SysFillRuleCriteria copy() {
        return new SysFillRuleCriteria(this);
    }

    public void setAnd(SysFillRuleCriteria and) {
        this.and = and;
    }

    public SysFillRuleCriteria getAnd() {
        return and;
    }

    public SysFillRuleCriteria and() {
        if (and == null) {
            and = new SysFillRuleCriteria();
        }
        return and;
    }

    public void setOr(SysFillRuleCriteria or) {
        this.or = or;
    }

    public SysFillRuleCriteria getOr() {
        return or;
    }

    public SysFillRuleCriteria or() {
        if (or == null) {
            or = new SysFillRuleCriteria();
        }
        return or;
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDesc() {
        return desc;
    }

    public StringFilter desc() {
        if (desc == null) {
            desc = new StringFilter();
        }
        return desc;
    }

    public void setDesc(StringFilter desc) {
        this.desc = desc;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public ResetFrequencyFilter getResetFrequency() {
        return resetFrequency;
    }

    public ResetFrequencyFilter resetFrequency() {
        if (resetFrequency == null) {
            resetFrequency = new ResetFrequencyFilter();
        }
        return resetFrequency;
    }

    public void setResetFrequency(ResetFrequencyFilter resetFrequency) {
        this.resetFrequency = resetFrequency;
    }

    public LongFilter getSeqValue() {
        return seqValue;
    }

    public LongFilter seqValue() {
        if (seqValue == null) {
            seqValue = new LongFilter();
        }
        return seqValue;
    }

    public void setSeqValue(LongFilter seqValue) {
        this.seqValue = seqValue;
    }

    public StringFilter getFillValue() {
        return fillValue;
    }

    public StringFilter fillValue() {
        if (fillValue == null) {
            fillValue = new StringFilter();
        }
        return fillValue;
    }

    public void setFillValue(StringFilter fillValue) {
        this.fillValue = fillValue;
    }

    public StringFilter getImplClass() {
        return implClass;
    }

    public StringFilter implClass() {
        if (implClass == null) {
            implClass = new StringFilter();
        }
        return implClass;
    }

    public void setImplClass(StringFilter implClass) {
        this.implClass = implClass;
    }

    public StringFilter getParams() {
        return params;
    }

    public StringFilter params() {
        if (params == null) {
            params = new StringFilter();
        }
        return params;
    }

    public void setParams(StringFilter params) {
        this.params = params;
    }

    public ZonedDateTimeFilter getResetStartTime() {
        return resetStartTime;
    }

    public ZonedDateTimeFilter resetStartTime() {
        if (resetStartTime == null) {
            resetStartTime = new ZonedDateTimeFilter();
        }
        return resetStartTime;
    }

    public void setResetStartTime(ZonedDateTimeFilter resetStartTime) {
        this.resetStartTime = resetStartTime;
    }

    public ZonedDateTimeFilter getResetEndTime() {
        return resetEndTime;
    }

    public ZonedDateTimeFilter resetEndTime() {
        if (resetEndTime == null) {
            resetEndTime = new ZonedDateTimeFilter();
        }
        return resetEndTime;
    }

    public void setResetEndTime(ZonedDateTimeFilter resetEndTime) {
        this.resetEndTime = resetEndTime;
    }

    public ZonedDateTimeFilter getResetTime() {
        return resetTime;
    }

    public ZonedDateTimeFilter resetTime() {
        if (resetTime == null) {
            resetTime = new ZonedDateTimeFilter();
        }
        return resetTime;
    }

    public void setResetTime(ZonedDateTimeFilter resetTime) {
        this.resetTime = resetTime;
    }

    public LongFilter getRuleItemsId() {
        return ruleItemsId;
    }

    public LongFilter ruleItemsId() {
        if (ruleItemsId == null) {
            ruleItemsId = new LongFilter();
        }
        return ruleItemsId;
    }

    public void setRuleItemsId(LongFilter ruleItemsId) {
        this.ruleItemsId = ruleItemsId;
    }

    public StringFilter getRuleItemsDatePattern() {
        return ruleItemsDatePattern;
    }

    public StringFilter ruleItemsDatePattern() {
        if (ruleItemsDatePattern == null) {
            ruleItemsDatePattern = new StringFilter();
        }
        return ruleItemsDatePattern;
    }

    public void setRuleItemsDatePattern(StringFilter ruleItemsDatePattern) {
        this.ruleItemsDatePattern = ruleItemsDatePattern;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SysFillRuleCriteria that = (SysFillRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(desc, that.desc) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(resetFrequency, that.resetFrequency) &&
            Objects.equals(seqValue, that.seqValue) &&
            Objects.equals(fillValue, that.fillValue) &&
            Objects.equals(implClass, that.implClass) &&
            Objects.equals(params, that.params) &&
            Objects.equals(resetStartTime, that.resetStartTime) &&
            Objects.equals(resetEndTime, that.resetEndTime) &&
            Objects.equals(resetTime, that.resetTime) &&
            Objects.equals(ruleItemsId, that.ruleItemsId) &&
            Objects.equals(ruleItemsDatePattern, that.ruleItemsDatePattern) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            desc,
            enabled,
            resetFrequency,
            seqValue,
            fillValue,
            implClass,
            params,
            resetStartTime,
            resetEndTime,
            resetTime,
            ruleItemsId,
            ruleItemsDatePattern,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (desc != null ? "desc=" + desc + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (resetFrequency != null ? "resetFrequency=" + resetFrequency + ", " : "") +
            (seqValue != null ? "seqValue=" + seqValue + ", " : "") +
            (fillValue != null ? "fillValue=" + fillValue + ", " : "") +
            (implClass != null ? "implClass=" + implClass + ", " : "") +
            (params != null ? "params=" + params + ", " : "") +
            (resetStartTime != null ? "resetStartTime=" + resetStartTime + ", " : "") +
            (resetEndTime != null ? "resetEndTime=" + resetEndTime + ", " : "") +
            (resetTime != null ? "resetTime=" + resetTime + ", " : "") +
            (ruleItemsId != null ? "ruleItemsId=" + ruleItemsId + ", " : "") +
            (ruleItemsDatePattern != null ? "ruleItemsDatePattern=" + ruleItemsDatePattern + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
