package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.ResetFrequency;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * Class for filtering ResetFrequency
     */
    public static class ResetFrequencyFilter extends Filter<ResetFrequency> {

        public ResetFrequencyFilter() {}

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

    @BindQuery(entity = FillRuleItem.class, column = "id", condition = "fill_rule_id=this.id")
    private LongFilter ruleItemsId;

    @BindQuery(entity = FillRuleItem.class, column = "date_pattern", condition = "fill_rule_id=this.id")
    private StringFilter ruleItemsDatePattern;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SysFillRuleCriteria and;

    @BindQuery(ignore = true)
    private SysFillRuleCriteria or;

    private Boolean distinct;

    public SysFillRuleCriteria() {}

    public SysFillRuleCriteria(SysFillRuleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.desc = other.optionalDesc().map(StringFilter::copy).orElse(null);
        this.enabled = other.optionalEnabled().map(BooleanFilter::copy).orElse(null);
        this.resetFrequency = other.optionalResetFrequency().map(ResetFrequencyFilter::copy).orElse(null);
        this.seqValue = other.optionalSeqValue().map(LongFilter::copy).orElse(null);
        this.fillValue = other.optionalFillValue().map(StringFilter::copy).orElse(null);
        this.implClass = other.optionalImplClass().map(StringFilter::copy).orElse(null);
        this.params = other.optionalParams().map(StringFilter::copy).orElse(null);
        this.resetStartTime = other.optionalResetStartTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.resetEndTime = other.optionalResetEndTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.resetTime = other.optionalResetTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.ruleItemsId = other.optionalRuleItemsId().map(LongFilter::copy).orElse(null);
        this.ruleItemsDatePattern = other.optionalRuleItemsDatePattern().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SysFillRuleCriteria copy() {
        return new SysFillRuleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDesc() {
        return desc;
    }

    public Optional<StringFilter> optionalDesc() {
        return Optional.ofNullable(desc);
    }

    public StringFilter desc() {
        if (desc == null) {
            setDesc(new StringFilter());
        }
        return desc;
    }

    public void setDesc(StringFilter desc) {
        this.desc = desc;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public Optional<BooleanFilter> optionalEnabled() {
        return Optional.ofNullable(enabled);
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            setEnabled(new BooleanFilter());
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public ResetFrequencyFilter getResetFrequency() {
        return resetFrequency;
    }

    public Optional<ResetFrequencyFilter> optionalResetFrequency() {
        return Optional.ofNullable(resetFrequency);
    }

    public ResetFrequencyFilter resetFrequency() {
        if (resetFrequency == null) {
            setResetFrequency(new ResetFrequencyFilter());
        }
        return resetFrequency;
    }

    public void setResetFrequency(ResetFrequencyFilter resetFrequency) {
        this.resetFrequency = resetFrequency;
    }

    public LongFilter getSeqValue() {
        return seqValue;
    }

    public Optional<LongFilter> optionalSeqValue() {
        return Optional.ofNullable(seqValue);
    }

    public LongFilter seqValue() {
        if (seqValue == null) {
            setSeqValue(new LongFilter());
        }
        return seqValue;
    }

    public void setSeqValue(LongFilter seqValue) {
        this.seqValue = seqValue;
    }

    public StringFilter getFillValue() {
        return fillValue;
    }

    public Optional<StringFilter> optionalFillValue() {
        return Optional.ofNullable(fillValue);
    }

    public StringFilter fillValue() {
        if (fillValue == null) {
            setFillValue(new StringFilter());
        }
        return fillValue;
    }

    public void setFillValue(StringFilter fillValue) {
        this.fillValue = fillValue;
    }

    public StringFilter getImplClass() {
        return implClass;
    }

    public Optional<StringFilter> optionalImplClass() {
        return Optional.ofNullable(implClass);
    }

    public StringFilter implClass() {
        if (implClass == null) {
            setImplClass(new StringFilter());
        }
        return implClass;
    }

    public void setImplClass(StringFilter implClass) {
        this.implClass = implClass;
    }

    public StringFilter getParams() {
        return params;
    }

    public Optional<StringFilter> optionalParams() {
        return Optional.ofNullable(params);
    }

    public StringFilter params() {
        if (params == null) {
            setParams(new StringFilter());
        }
        return params;
    }

    public void setParams(StringFilter params) {
        this.params = params;
    }

    public ZonedDateTimeFilter getResetStartTime() {
        return resetStartTime;
    }

    public Optional<ZonedDateTimeFilter> optionalResetStartTime() {
        return Optional.ofNullable(resetStartTime);
    }

    public ZonedDateTimeFilter resetStartTime() {
        if (resetStartTime == null) {
            setResetStartTime(new ZonedDateTimeFilter());
        }
        return resetStartTime;
    }

    public void setResetStartTime(ZonedDateTimeFilter resetStartTime) {
        this.resetStartTime = resetStartTime;
    }

    public ZonedDateTimeFilter getResetEndTime() {
        return resetEndTime;
    }

    public Optional<ZonedDateTimeFilter> optionalResetEndTime() {
        return Optional.ofNullable(resetEndTime);
    }

    public ZonedDateTimeFilter resetEndTime() {
        if (resetEndTime == null) {
            setResetEndTime(new ZonedDateTimeFilter());
        }
        return resetEndTime;
    }

    public void setResetEndTime(ZonedDateTimeFilter resetEndTime) {
        this.resetEndTime = resetEndTime;
    }

    public ZonedDateTimeFilter getResetTime() {
        return resetTime;
    }

    public Optional<ZonedDateTimeFilter> optionalResetTime() {
        return Optional.ofNullable(resetTime);
    }

    public ZonedDateTimeFilter resetTime() {
        if (resetTime == null) {
            setResetTime(new ZonedDateTimeFilter());
        }
        return resetTime;
    }

    public void setResetTime(ZonedDateTimeFilter resetTime) {
        this.resetTime = resetTime;
    }

    public LongFilter getRuleItemsId() {
        return ruleItemsId;
    }

    public Optional<LongFilter> optionalRuleItemsId() {
        return Optional.ofNullable(ruleItemsId);
    }

    public LongFilter ruleItemsId() {
        if (ruleItemsId == null) {
            setRuleItemsId(new LongFilter());
        }
        return ruleItemsId;
    }

    public void setRuleItemsId(LongFilter ruleItemsId) {
        this.ruleItemsId = ruleItemsId;
    }

    public StringFilter getRuleItemsDatePattern() {
        return ruleItemsDatePattern;
    }

    public Optional<StringFilter> optionalRuleItemsDatePattern() {
        return Optional.ofNullable(ruleItemsDatePattern);
    }

    public StringFilter ruleItemsDatePattern() {
        if (ruleItemsDatePattern == null) {
            setRuleItemsDatePattern(new StringFilter());
        }
        return ruleItemsDatePattern;
    }

    public void setRuleItemsDatePattern(StringFilter ruleItemsDatePattern) {
        this.ruleItemsDatePattern = ruleItemsDatePattern;
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

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRuleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDesc().map(f -> "desc=" + f + ", ").orElse("") +
            optionalEnabled().map(f -> "enabled=" + f + ", ").orElse("") +
            optionalResetFrequency().map(f -> "resetFrequency=" + f + ", ").orElse("") +
            optionalSeqValue().map(f -> "seqValue=" + f + ", ").orElse("") +
            optionalFillValue().map(f -> "fillValue=" + f + ", ").orElse("") +
            optionalImplClass().map(f -> "implClass=" + f + ", ").orElse("") +
            optionalParams().map(f -> "params=" + f + ", ").orElse("") +
            optionalResetStartTime().map(f -> "resetStartTime=" + f + ", ").orElse("") +
            optionalResetEndTime().map(f -> "resetEndTime=" + f + ", ").orElse("") +
            optionalResetTime().map(f -> "resetTime=" + f + ", ").orElse("") +
            optionalRuleItemsId().map(f -> "ruleItemsId=" + f + ", ").orElse("") +
            optionalRuleItemsDatePattern().map(f -> "ruleItemsDatePattern=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
