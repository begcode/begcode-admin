package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.domain.enumeration.FormConfigType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.FormConfig} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.FormConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /form-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormConfigCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FormConfigType
     */
    public static class FormConfigTypeFilter extends Filter<FormConfigType> {

        public FormConfigTypeFilter() {}

        public FormConfigTypeFilter(FormConfigTypeFilter filter) {
            super(filter);
        }

        @Override
        public FormConfigTypeFilter copy() {
            return new FormConfigTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.form_key")
    private StringFilter formKey;

    @BindQuery(column = "self.form_name")
    private StringFilter formName;

    @BindQuery(column = "self.form_type")
    private FormConfigTypeFilter formType;

    @BindQuery(column = "self.multi_items")
    private BooleanFilter multiItems;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = BusinessType.class, column = "id", condition = "this.business_type_id=id")
    private LongFilter businessTypeId;

    @BindQuery(entity = BusinessType.class, column = "name", condition = "this.business_type_id=id")
    private StringFilter businessTypeName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private FormConfigCriteria and;

    @BindQuery(ignore = true)
    private FormConfigCriteria or;

    private Boolean distinct;

    public FormConfigCriteria() {}

    public FormConfigCriteria(FormConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.formKey = other.optionalFormKey().map(StringFilter::copy).orElse(null);
        this.formName = other.optionalFormName().map(StringFilter::copy).orElse(null);
        this.formType = other.optionalFormType().map(FormConfigTypeFilter::copy).orElse(null);
        this.multiItems = other.optionalMultiItems().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.businessTypeId = other.optionalBusinessTypeId().map(LongFilter::copy).orElse(null);
        this.businessTypeName = other.optionalBusinessTypeName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FormConfigCriteria copy() {
        return new FormConfigCriteria(this);
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

    public StringFilter getFormKey() {
        return formKey;
    }

    public Optional<StringFilter> optionalFormKey() {
        return Optional.ofNullable(formKey);
    }

    public StringFilter formKey() {
        if (formKey == null) {
            setFormKey(new StringFilter());
        }
        return formKey;
    }

    public void setFormKey(StringFilter formKey) {
        this.formKey = formKey;
    }

    public StringFilter getFormName() {
        return formName;
    }

    public Optional<StringFilter> optionalFormName() {
        return Optional.ofNullable(formName);
    }

    public StringFilter formName() {
        if (formName == null) {
            setFormName(new StringFilter());
        }
        return formName;
    }

    public void setFormName(StringFilter formName) {
        this.formName = formName;
    }

    public FormConfigTypeFilter getFormType() {
        return formType;
    }

    public Optional<FormConfigTypeFilter> optionalFormType() {
        return Optional.ofNullable(formType);
    }

    public FormConfigTypeFilter formType() {
        if (formType == null) {
            setFormType(new FormConfigTypeFilter());
        }
        return formType;
    }

    public void setFormType(FormConfigTypeFilter formType) {
        this.formType = formType;
    }

    public BooleanFilter getMultiItems() {
        return multiItems;
    }

    public Optional<BooleanFilter> optionalMultiItems() {
        return Optional.ofNullable(multiItems);
    }

    public BooleanFilter multiItems() {
        if (multiItems == null) {
            setMultiItems(new BooleanFilter());
        }
        return multiItems;
    }

    public void setMultiItems(BooleanFilter multiItems) {
        this.multiItems = multiItems;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<LongFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new LongFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<LongFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new LongFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getBusinessTypeId() {
        return businessTypeId;
    }

    public Optional<LongFilter> optionalBusinessTypeId() {
        return Optional.ofNullable(businessTypeId);
    }

    public LongFilter businessTypeId() {
        if (businessTypeId == null) {
            setBusinessTypeId(new LongFilter());
        }
        return businessTypeId;
    }

    public void setBusinessTypeId(LongFilter businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public StringFilter getBusinessTypeName() {
        return businessTypeName;
    }

    public Optional<StringFilter> optionalBusinessTypeName() {
        return Optional.ofNullable(businessTypeName);
    }

    public StringFilter businessTypeName() {
        if (businessTypeName == null) {
            setBusinessTypeName(new StringFilter());
        }
        return businessTypeName;
    }

    public void setBusinessTypeName(StringFilter businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public void setAnd(FormConfigCriteria and) {
        this.and = and;
    }

    public FormConfigCriteria getAnd() {
        return and;
    }

    public FormConfigCriteria and() {
        if (and == null) {
            and = new FormConfigCriteria();
        }
        return and;
    }

    public void setOr(FormConfigCriteria or) {
        this.or = or;
    }

    public FormConfigCriteria getOr() {
        return or;
    }

    public FormConfigCriteria or() {
        if (or == null) {
            or = new FormConfigCriteria();
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
        final FormConfigCriteria that = (FormConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(formKey, that.formKey) &&
            Objects.equals(formName, that.formName) &&
            Objects.equals(formType, that.formType) &&
            Objects.equals(multiItems, that.multiItems) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(businessTypeId, that.businessTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            formKey,
            formName,
            formType,
            multiItems,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            businessTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFormKey().map(f -> "formKey=" + f + ", ").orElse("") +
            optionalFormName().map(f -> "formName=" + f + ", ").orElse("") +
            optionalFormType().map(f -> "formType=" + f + ", ").orElse("") +
            optionalMultiItems().map(f -> "multiItems=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalBusinessTypeId().map(f -> "businessTypeId=" + f + ", ").orElse("") +
            optionalBusinessTypeName().map(f -> "businessTypeName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
