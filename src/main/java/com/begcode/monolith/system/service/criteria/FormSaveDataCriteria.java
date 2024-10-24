package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.system.domain.FormConfig;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.FormSaveData} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.FormSaveDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /form-save-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormSaveDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = FormConfig.class, column = "id", condition = "this.form_config_id=id")
    private LongFilter formConfigId;

    @BindQuery(entity = FormConfig.class, column = "form_name", condition = "this.form_config_id=id")
    private StringFilter formConfigFormName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private FormSaveDataCriteria and;

    @BindQuery(ignore = true)
    private FormSaveDataCriteria or;

    private Boolean distinct;

    public FormSaveDataCriteria() {}

    public FormSaveDataCriteria(FormSaveDataCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.formConfigId = other.optionalFormConfigId().map(LongFilter::copy).orElse(null);
        this.formConfigFormName = other.optionalFormConfigFormName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FormSaveDataCriteria copy() {
        return new FormSaveDataCriteria(this);
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

    public LongFilter getFormConfigId() {
        return formConfigId;
    }

    public Optional<LongFilter> optionalFormConfigId() {
        return Optional.ofNullable(formConfigId);
    }

    public LongFilter formConfigId() {
        if (formConfigId == null) {
            setFormConfigId(new LongFilter());
        }
        return formConfigId;
    }

    public void setFormConfigId(LongFilter formConfigId) {
        this.formConfigId = formConfigId;
    }

    public StringFilter getFormConfigFormName() {
        return formConfigFormName;
    }

    public Optional<StringFilter> optionalFormConfigFormName() {
        return Optional.ofNullable(formConfigFormName);
    }

    public StringFilter formConfigFormName() {
        if (formConfigFormName == null) {
            setFormConfigFormName(new StringFilter());
        }
        return formConfigFormName;
    }

    public void setFormConfigFormName(StringFilter formConfigFormName) {
        this.formConfigFormName = formConfigFormName;
    }

    public void setAnd(FormSaveDataCriteria and) {
        this.and = and;
    }

    public FormSaveDataCriteria getAnd() {
        return and;
    }

    public FormSaveDataCriteria and() {
        if (and == null) {
            and = new FormSaveDataCriteria();
        }
        return and;
    }

    public void setOr(FormSaveDataCriteria or) {
        this.or = or;
    }

    public FormSaveDataCriteria getOr() {
        return or;
    }

    public FormSaveDataCriteria or() {
        if (or == null) {
            or = new FormSaveDataCriteria();
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
        final FormSaveDataCriteria that = (FormSaveDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(formConfigId, that.formConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, formConfigId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormSaveDataCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalFormConfigId().map(f -> "formConfigId=" + f + ", ").orElse("") +
            optionalFormConfigFormName().map(f -> "formConfigFormName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
