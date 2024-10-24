package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.settings.domain.CommonFieldData;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.SystemConfig} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.SystemConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.category_name")
    private StringFilter categoryName;

    @BindQuery(column = "self.category_key")
    private StringFilter categoryKey;

    @BindQuery(column = "self.disabled")
    private BooleanFilter disabled;

    @BindQuery(column = "self.sort_value")
    private IntegerFilter sortValue;

    @BindQuery(column = "self.built_in")
    private BooleanFilter builtIn;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = CommonFieldData.class, column = "id", condition = "owner_entity_name = 'SystemConfig' AND owner_entity_id=this.id")
    private LongFilter itemsId;

    @BindQuery(
        entity = CommonFieldData.class,
        column = "name",
        condition = "owner_entity_name = 'SystemConfig' AND owner_entity_id=this.id"
    )
    private StringFilter itemsName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SystemConfigCriteria and;

    @BindQuery(ignore = true)
    private SystemConfigCriteria or;

    private Boolean distinct;

    public SystemConfigCriteria() {}

    public SystemConfigCriteria(SystemConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.categoryName = other.optionalCategoryName().map(StringFilter::copy).orElse(null);
        this.categoryKey = other.optionalCategoryKey().map(StringFilter::copy).orElse(null);
        this.disabled = other.optionalDisabled().map(BooleanFilter::copy).orElse(null);
        this.sortValue = other.optionalSortValue().map(IntegerFilter::copy).orElse(null);
        this.builtIn = other.optionalBuiltIn().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.itemsName = other.optionalItemsName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SystemConfigCriteria copy() {
        return new SystemConfigCriteria(this);
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

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public Optional<StringFilter> optionalCategoryName() {
        return Optional.ofNullable(categoryName);
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            setCategoryName(new StringFilter());
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public StringFilter getCategoryKey() {
        return categoryKey;
    }

    public Optional<StringFilter> optionalCategoryKey() {
        return Optional.ofNullable(categoryKey);
    }

    public StringFilter categoryKey() {
        if (categoryKey == null) {
            setCategoryKey(new StringFilter());
        }
        return categoryKey;
    }

    public void setCategoryKey(StringFilter categoryKey) {
        this.categoryKey = categoryKey;
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

    public BooleanFilter getBuiltIn() {
        return builtIn;
    }

    public Optional<BooleanFilter> optionalBuiltIn() {
        return Optional.ofNullable(builtIn);
    }

    public BooleanFilter builtIn() {
        if (builtIn == null) {
            setBuiltIn(new BooleanFilter());
        }
        return builtIn;
    }

    public void setBuiltIn(BooleanFilter builtIn) {
        this.builtIn = builtIn;
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

    public LongFilter getItemsId() {
        return itemsId;
    }

    public Optional<LongFilter> optionalItemsId() {
        return Optional.ofNullable(itemsId);
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            setItemsId(new LongFilter());
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public StringFilter getItemsName() {
        return itemsName;
    }

    public Optional<StringFilter> optionalItemsName() {
        return Optional.ofNullable(itemsName);
    }

    public StringFilter itemsName() {
        if (itemsName == null) {
            setItemsName(new StringFilter());
        }
        return itemsName;
    }

    public void setItemsName(StringFilter itemsName) {
        this.itemsName = itemsName;
    }

    public void setAnd(SystemConfigCriteria and) {
        this.and = and;
    }

    public SystemConfigCriteria getAnd() {
        return and;
    }

    public SystemConfigCriteria and() {
        if (and == null) {
            and = new SystemConfigCriteria();
        }
        return and;
    }

    public void setOr(SystemConfigCriteria or) {
        this.or = or;
    }

    public SystemConfigCriteria getOr() {
        return or;
    }

    public SystemConfigCriteria or() {
        if (or == null) {
            or = new SystemConfigCriteria();
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
        final SystemConfigCriteria that = (SystemConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(categoryKey, that.categoryKey) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(sortValue, that.sortValue) &&
            Objects.equals(builtIn, that.builtIn) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            categoryName,
            categoryKey,
            disabled,
            sortValue,
            builtIn,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            itemsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCategoryName().map(f -> "categoryName=" + f + ", ").orElse("") +
            optionalCategoryKey().map(f -> "categoryKey=" + f + ", ").orElse("") +
            optionalDisabled().map(f -> "disabled=" + f + ", ").orElse("") +
            optionalSortValue().map(f -> "sortValue=" + f + ", ").orElse("") +
            optionalBuiltIn().map(f -> "builtIn=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalItemsId().map(f -> "itemsId=" + f + ", ").orElse("") +
            optionalItemsName().map(f -> "itemsName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
