package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.CommonFieldType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.CommonFieldData} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.CommonFieldDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-field-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommonFieldDataCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CommonFieldType
     */
    public static class CommonFieldTypeFilter extends Filter<CommonFieldType> {

        public CommonFieldTypeFilter() {}

        public CommonFieldTypeFilter(CommonFieldTypeFilter filter) {
            super(filter);
        }

        @Override
        public CommonFieldTypeFilter copy() {
            return new CommonFieldTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.value")
    private StringFilter value;

    @BindQuery(column = "self.label")
    private StringFilter label;

    @BindQuery(column = "self.value_type")
    private CommonFieldTypeFilter valueType;

    @BindQuery(column = "self.remark")
    private StringFilter remark;

    @BindQuery(column = "self.sort_value")
    private IntegerFilter sortValue;

    @BindQuery(column = "self.disabled")
    private BooleanFilter disabled;

    @BindQuery(column = "self.owner_entity_name")
    private StringFilter ownerEntityName;

    @BindQuery(column = "self.owner_entity_id")
    private LongFilter ownerEntityId;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private CommonFieldDataCriteria and;

    @BindQuery(ignore = true)
    private CommonFieldDataCriteria or;

    private Boolean distinct;

    public CommonFieldDataCriteria() {}

    public CommonFieldDataCriteria(CommonFieldDataCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.value = other.optionalValue().map(StringFilter::copy).orElse(null);
        this.label = other.optionalLabel().map(StringFilter::copy).orElse(null);
        this.valueType = other.optionalValueType().map(CommonFieldTypeFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.sortValue = other.optionalSortValue().map(IntegerFilter::copy).orElse(null);
        this.disabled = other.optionalDisabled().map(BooleanFilter::copy).orElse(null);
        this.ownerEntityName = other.optionalOwnerEntityName().map(StringFilter::copy).orElse(null);
        this.ownerEntityId = other.optionalOwnerEntityId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CommonFieldDataCriteria copy() {
        return new CommonFieldDataCriteria(this);
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

    public StringFilter getValue() {
        return value;
    }

    public Optional<StringFilter> optionalValue() {
        return Optional.ofNullable(value);
    }

    public StringFilter value() {
        if (value == null) {
            setValue(new StringFilter());
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getLabel() {
        return label;
    }

    public Optional<StringFilter> optionalLabel() {
        return Optional.ofNullable(label);
    }

    public StringFilter label() {
        if (label == null) {
            setLabel(new StringFilter());
        }
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public CommonFieldTypeFilter getValueType() {
        return valueType;
    }

    public Optional<CommonFieldTypeFilter> optionalValueType() {
        return Optional.ofNullable(valueType);
    }

    public CommonFieldTypeFilter valueType() {
        if (valueType == null) {
            setValueType(new CommonFieldTypeFilter());
        }
        return valueType;
    }

    public void setValueType(CommonFieldTypeFilter valueType) {
        this.valueType = valueType;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public Optional<StringFilter> optionalRemark() {
        return Optional.ofNullable(remark);
    }

    public StringFilter remark() {
        if (remark == null) {
            setRemark(new StringFilter());
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
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

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public Optional<BooleanFilter> optionalDisabled() {
        return Optional.ofNullable(disabled);
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            setDisabled(new BooleanFilter());
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public StringFilter getOwnerEntityName() {
        return ownerEntityName;
    }

    public Optional<StringFilter> optionalOwnerEntityName() {
        return Optional.ofNullable(ownerEntityName);
    }

    public StringFilter ownerEntityName() {
        if (ownerEntityName == null) {
            setOwnerEntityName(new StringFilter());
        }
        return ownerEntityName;
    }

    public void setOwnerEntityName(StringFilter ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
    }

    public LongFilter getOwnerEntityId() {
        return ownerEntityId;
    }

    public Optional<LongFilter> optionalOwnerEntityId() {
        return Optional.ofNullable(ownerEntityId);
    }

    public LongFilter ownerEntityId() {
        if (ownerEntityId == null) {
            setOwnerEntityId(new LongFilter());
        }
        return ownerEntityId;
    }

    public void setOwnerEntityId(LongFilter ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    public void setAnd(CommonFieldDataCriteria and) {
        this.and = and;
    }

    public CommonFieldDataCriteria getAnd() {
        return and;
    }

    public CommonFieldDataCriteria and() {
        if (and == null) {
            and = new CommonFieldDataCriteria();
        }
        return and;
    }

    public void setOr(CommonFieldDataCriteria or) {
        this.or = or;
    }

    public CommonFieldDataCriteria getOr() {
        return or;
    }

    public CommonFieldDataCriteria or() {
        if (or == null) {
            or = new CommonFieldDataCriteria();
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
        final CommonFieldDataCriteria that = (CommonFieldDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(value, that.value) &&
            Objects.equals(label, that.label) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(sortValue, that.sortValue) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(ownerEntityName, that.ownerEntityName) &&
            Objects.equals(ownerEntityId, that.ownerEntityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, label, valueType, remark, sortValue, disabled, ownerEntityName, ownerEntityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonFieldDataCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalValue().map(f -> "value=" + f + ", ").orElse("") +
            optionalLabel().map(f -> "label=" + f + ", ").orElse("") +
            optionalValueType().map(f -> "valueType=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
            optionalSortValue().map(f -> "sortValue=" + f + ", ").orElse("") +
            optionalDisabled().map(f -> "disabled=" + f + ", ").orElse("") +
            optionalOwnerEntityName().map(f -> "ownerEntityName=" + f + ", ").orElse("") +
            optionalOwnerEntityId().map(f -> "ownerEntityId=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
