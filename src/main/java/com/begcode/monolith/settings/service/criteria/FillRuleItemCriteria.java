package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.FieldParamType;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private FillRuleItemCriteria and;

    @BindQuery(ignore = true)
    private FillRuleItemCriteria or;

    /**
     * Class for filtering FieldParamType
     */
    public static class FieldParamTypeFilter extends Filter<FieldParamType> {

        public FieldParamTypeFilter() {}

        public FieldParamTypeFilter(String value) {
            FieldParamType enumValue = FieldParamType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = FieldParamType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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

    @BindQuery(column = "self.fill_rule_id")
    private LongFilter fillRuleId;

    @BindQuery(entity = SysFillRule.class, column = "name", condition = "this.fill_rule_id=id")
    private StringFilter fillRuleName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public FillRuleItemCriteria() {}

    public FillRuleItemCriteria(FillRuleItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sortValue = other.sortValue == null ? null : other.sortValue.copy();
        this.fieldParamType = other.fieldParamType == null ? null : other.fieldParamType.copy();
        this.fieldParamValue = other.fieldParamValue == null ? null : other.fieldParamValue.copy();
        this.datePattern = other.datePattern == null ? null : other.datePattern.copy();
        this.seqLength = other.seqLength == null ? null : other.seqLength.copy();
        this.seqIncrement = other.seqIncrement == null ? null : other.seqIncrement.copy();
        this.seqStartValue = other.seqStartValue == null ? null : other.seqStartValue.copy();
        this.fillRuleId = other.fillRuleId == null ? null : other.fillRuleId.copy();
        this.fillRuleName = other.fillRuleName == null ? null : other.fillRuleName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FillRuleItemCriteria copy() {
        return new FillRuleItemCriteria(this);
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

    public IntegerFilter getSortValue() {
        return sortValue;
    }

    public IntegerFilter sortValue() {
        if (sortValue == null) {
            sortValue = new IntegerFilter();
        }
        return sortValue;
    }

    public void setSortValue(IntegerFilter sortValue) {
        this.sortValue = sortValue;
    }

    public FieldParamTypeFilter getFieldParamType() {
        return fieldParamType;
    }

    public FieldParamTypeFilter fieldParamType() {
        if (fieldParamType == null) {
            fieldParamType = new FieldParamTypeFilter();
        }
        return fieldParamType;
    }

    public void setFieldParamType(FieldParamTypeFilter fieldParamType) {
        this.fieldParamType = fieldParamType;
    }

    public StringFilter getFieldParamValue() {
        return fieldParamValue;
    }

    public StringFilter fieldParamValue() {
        if (fieldParamValue == null) {
            fieldParamValue = new StringFilter();
        }
        return fieldParamValue;
    }

    public void setFieldParamValue(StringFilter fieldParamValue) {
        this.fieldParamValue = fieldParamValue;
    }

    public StringFilter getDatePattern() {
        return datePattern;
    }

    public StringFilter datePattern() {
        if (datePattern == null) {
            datePattern = new StringFilter();
        }
        return datePattern;
    }

    public void setDatePattern(StringFilter datePattern) {
        this.datePattern = datePattern;
    }

    public IntegerFilter getSeqLength() {
        return seqLength;
    }

    public IntegerFilter seqLength() {
        if (seqLength == null) {
            seqLength = new IntegerFilter();
        }
        return seqLength;
    }

    public void setSeqLength(IntegerFilter seqLength) {
        this.seqLength = seqLength;
    }

    public IntegerFilter getSeqIncrement() {
        return seqIncrement;
    }

    public IntegerFilter seqIncrement() {
        if (seqIncrement == null) {
            seqIncrement = new IntegerFilter();
        }
        return seqIncrement;
    }

    public void setSeqIncrement(IntegerFilter seqIncrement) {
        this.seqIncrement = seqIncrement;
    }

    public IntegerFilter getSeqStartValue() {
        return seqStartValue;
    }

    public IntegerFilter seqStartValue() {
        if (seqStartValue == null) {
            seqStartValue = new IntegerFilter();
        }
        return seqStartValue;
    }

    public void setSeqStartValue(IntegerFilter seqStartValue) {
        this.seqStartValue = seqStartValue;
    }

    public LongFilter getFillRuleId() {
        return fillRuleId;
    }

    public LongFilter fillRuleId() {
        if (fillRuleId == null) {
            fillRuleId = new LongFilter();
        }
        return fillRuleId;
    }

    public void setFillRuleId(LongFilter fillRuleId) {
        this.fillRuleId = fillRuleId;
    }

    public StringFilter getFillRuleName() {
        return fillRuleName;
    }

    public StringFilter fillRuleName() {
        if (fillRuleName == null) {
            fillRuleName = new StringFilter();
        }
        return fillRuleName;
    }

    public void setFillRuleName(StringFilter fillRuleName) {
        this.fillRuleName = fillRuleName;
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
            Objects.equals(fillRuleName, that.fillRuleName) &&
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
            fillRuleName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FillRuleItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sortValue != null ? "sortValue=" + sortValue + ", " : "") +
            (fieldParamType != null ? "fieldParamType=" + fieldParamType + ", " : "") +
            (fieldParamValue != null ? "fieldParamValue=" + fieldParamValue + ", " : "") +
            (datePattern != null ? "datePattern=" + datePattern + ", " : "") +
            (seqLength != null ? "seqLength=" + seqLength + ", " : "") +
            (seqIncrement != null ? "seqIncrement=" + seqIncrement + ", " : "") +
            (seqStartValue != null ? "seqStartValue=" + seqStartValue + ", " : "") +
            (fillRuleId != null ? "fillRuleId=" + fillRuleId + ", " : "") +
            (fillRuleName != null ? "fillRuleName=" + fillRuleName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
