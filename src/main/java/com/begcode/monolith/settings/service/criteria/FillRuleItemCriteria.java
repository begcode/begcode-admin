package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.FieldParamType;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.FillRuleItem} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.FillRuleItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fill-rule-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FillRuleItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FieldParamType
     */
    public static class FieldParamTypeFilter extends Filter<FieldParamType> {

        public FieldParamTypeFilter() {}

        public FieldParamTypeFilter(FieldParamTypeFilter filter) {
            super(filter);
        }

        @Override
        public FieldParamTypeFilter copy() {
            return new FieldParamTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.sort_value")
    private IntegerFilter sortValue;

    @BindQuery(column = "self.field_param_type")
    private FieldParamTypeFilter fieldParamType;

    @BindQuery(column = "self.field_param_value")
    private StringFilter fieldParamValue;

    @BindQuery(column = "self.date_pattern")
    private StringFilter datePattern;

    @BindQuery(column = "self.seq_length")
    private IntegerFilter seqLength;

    @BindQuery(column = "self.seq_increment")
    private IntegerFilter seqIncrement;

    @BindQuery(column = "self.seq_start_value")
    private IntegerFilter seqStartValue;

    @BindQuery(entity = SysFillRule.class, column = "id", condition = "this.fill_rule_id=id")
    private LongFilter fillRuleId;

    @BindQuery(entity = SysFillRule.class, column = "name", condition = "this.fill_rule_id=id")
    private StringFilter fillRuleName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private FillRuleItemCriteria and;

    @BindQuery(ignore = true)
    private FillRuleItemCriteria or;

    private Boolean distinct;

    public FillRuleItemCriteria() {}

    public FillRuleItemCriteria(FillRuleItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sortValue = other.optionalSortValue().map(IntegerFilter::copy).orElse(null);
        this.fieldParamType = other.optionalFieldParamType().map(FieldParamTypeFilter::copy).orElse(null);
        this.fieldParamValue = other.optionalFieldParamValue().map(StringFilter::copy).orElse(null);
        this.datePattern = other.optionalDatePattern().map(StringFilter::copy).orElse(null);
        this.seqLength = other.optionalSeqLength().map(IntegerFilter::copy).orElse(null);
        this.seqIncrement = other.optionalSeqIncrement().map(IntegerFilter::copy).orElse(null);
        this.seqStartValue = other.optionalSeqStartValue().map(IntegerFilter::copy).orElse(null);
        this.fillRuleId = other.optionalFillRuleId().map(LongFilter::copy).orElse(null);
        this.fillRuleName = other.optionalFillRuleName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FillRuleItemCriteria copy() {
        return new FillRuleItemCriteria(this);
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

    public IntegerFilter getSortValue() {
        return sortValue;
    }

    public Optional<IntegerFilter> optionalSortValue() {
        return Optional.ofNullable(sortValue);
    }

    public IntegerFilter sortValue() {
        if (sortValue == null) {
            setSortValue(new IntegerFilter());
        }
        return sortValue;
    }

    public void setSortValue(IntegerFilter sortValue) {
        this.sortValue = sortValue;
    }

    public FieldParamTypeFilter getFieldParamType() {
        return fieldParamType;
    }

    public Optional<FieldParamTypeFilter> optionalFieldParamType() {
        return Optional.ofNullable(fieldParamType);
    }

    public FieldParamTypeFilter fieldParamType() {
        if (fieldParamType == null) {
            setFieldParamType(new FieldParamTypeFilter());
        }
        return fieldParamType;
    }

    public void setFieldParamType(FieldParamTypeFilter fieldParamType) {
        this.fieldParamType = fieldParamType;
    }

    public StringFilter getFieldParamValue() {
        return fieldParamValue;
    }

    public Optional<StringFilter> optionalFieldParamValue() {
        return Optional.ofNullable(fieldParamValue);
    }

    public StringFilter fieldParamValue() {
        if (fieldParamValue == null) {
            setFieldParamValue(new StringFilter());
        }
        return fieldParamValue;
    }

    public void setFieldParamValue(StringFilter fieldParamValue) {
        this.fieldParamValue = fieldParamValue;
    }

    public StringFilter getDatePattern() {
        return datePattern;
    }

    public Optional<StringFilter> optionalDatePattern() {
        return Optional.ofNullable(datePattern);
    }

    public StringFilter datePattern() {
        if (datePattern == null) {
            setDatePattern(new StringFilter());
        }
        return datePattern;
    }

    public void setDatePattern(StringFilter datePattern) {
        this.datePattern = datePattern;
    }

    public IntegerFilter getSeqLength() {
        return seqLength;
    }

    public Optional<IntegerFilter> optionalSeqLength() {
        return Optional.ofNullable(seqLength);
    }

    public IntegerFilter seqLength() {
        if (seqLength == null) {
            setSeqLength(new IntegerFilter());
        }
        return seqLength;
    }

    public void setSeqLength(IntegerFilter seqLength) {
        this.seqLength = seqLength;
    }

    public IntegerFilter getSeqIncrement() {
        return seqIncrement;
    }

    public Optional<IntegerFilter> optionalSeqIncrement() {
        return Optional.ofNullable(seqIncrement);
    }

    public IntegerFilter seqIncrement() {
        if (seqIncrement == null) {
            setSeqIncrement(new IntegerFilter());
        }
        return seqIncrement;
    }

    public void setSeqIncrement(IntegerFilter seqIncrement) {
        this.seqIncrement = seqIncrement;
    }

    public IntegerFilter getSeqStartValue() {
        return seqStartValue;
    }

    public Optional<IntegerFilter> optionalSeqStartValue() {
        return Optional.ofNullable(seqStartValue);
    }

    public IntegerFilter seqStartValue() {
        if (seqStartValue == null) {
            setSeqStartValue(new IntegerFilter());
        }
        return seqStartValue;
    }

    public void setSeqStartValue(IntegerFilter seqStartValue) {
        this.seqStartValue = seqStartValue;
    }

    public LongFilter getFillRuleId() {
        return fillRuleId;
    }

    public Optional<LongFilter> optionalFillRuleId() {
        return Optional.ofNullable(fillRuleId);
    }

    public LongFilter fillRuleId() {
        if (fillRuleId == null) {
            setFillRuleId(new LongFilter());
        }
        return fillRuleId;
    }

    public void setFillRuleId(LongFilter fillRuleId) {
        this.fillRuleId = fillRuleId;
    }

    public StringFilter getFillRuleName() {
        return fillRuleName;
    }

    public Optional<StringFilter> optionalFillRuleName() {
        return Optional.ofNullable(fillRuleName);
    }

    public StringFilter fillRuleName() {
        if (fillRuleName == null) {
            setFillRuleName(new StringFilter());
        }
        return fillRuleName;
    }

    public void setFillRuleName(StringFilter fillRuleName) {
        this.fillRuleName = fillRuleName;
    }

    public void setAnd(FillRuleItemCriteria and) {
        this.and = and;
    }

    public FillRuleItemCriteria getAnd() {
        return and;
    }

    public FillRuleItemCriteria and() {
        if (and == null) {
            and = new FillRuleItemCriteria();
        }
        return and;
    }

    public void setOr(FillRuleItemCriteria or) {
        this.or = or;
    }

    public FillRuleItemCriteria getOr() {
        return or;
    }

    public FillRuleItemCriteria or() {
        if (or == null) {
            or = new FillRuleItemCriteria();
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
        final FillRuleItemCriteria that = (FillRuleItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sortValue, that.sortValue) &&
            Objects.equals(fieldParamType, that.fieldParamType) &&
            Objects.equals(fieldParamValue, that.fieldParamValue) &&
            Objects.equals(datePattern, that.datePattern) &&
            Objects.equals(seqLength, that.seqLength) &&
            Objects.equals(seqIncrement, that.seqIncrement) &&
            Objects.equals(seqStartValue, that.seqStartValue) &&
            Objects.equals(fillRuleId, that.fillRuleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sortValue,
            fieldParamType,
            fieldParamValue,
            datePattern,
            seqLength,
            seqIncrement,
            seqStartValue,
            fillRuleId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FillRuleItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSortValue().map(f -> "sortValue=" + f + ", ").orElse("") +
            optionalFieldParamType().map(f -> "fieldParamType=" + f + ", ").orElse("") +
            optionalFieldParamValue().map(f -> "fieldParamValue=" + f + ", ").orElse("") +
            optionalDatePattern().map(f -> "datePattern=" + f + ", ").orElse("") +
            optionalSeqLength().map(f -> "seqLength=" + f + ", ").orElse("") +
            optionalSeqIncrement().map(f -> "seqIncrement=" + f + ", ").orElse("") +
            optionalSeqStartValue().map(f -> "seqStartValue=" + f + ", ").orElse("") +
            optionalFillRuleId().map(f -> "fillRuleId=" + f + ", ").orElse("") +
            optionalFillRuleName().map(f -> "fillRuleName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
