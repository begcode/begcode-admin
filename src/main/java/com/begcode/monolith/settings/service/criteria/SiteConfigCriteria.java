package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.settings.domain.CommonFieldData;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.SiteConfig} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.SiteConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /site-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteConfigCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SiteConfigCriteria and;

    @BindQuery(ignore = true)
    private SiteConfigCriteria or;

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

    @BindQuery(entity = CommonFieldData.class, column = "id", condition = "owner_entity_name = 'SiteConfig' AND owner_entity_id=id")
    private LongFilter itemsId;

    @BindQuery(entity = CommonFieldData.class, column = "name", condition = "owner_entity_name = 'SiteConfig' AND owner_entity_id=id")
    private StringFilter itemsName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SiteConfigCriteria() {}

    public SiteConfigCriteria(SiteConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.categoryKey = other.categoryKey == null ? null : other.categoryKey.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.sortValue = other.sortValue == null ? null : other.sortValue.copy();
        this.builtIn = other.builtIn == null ? null : other.builtIn.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.itemsName = other.itemsName == null ? null : other.itemsName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SiteConfigCriteria copy() {
        return new SiteConfigCriteria(this);
    }

    public void setAnd(SiteConfigCriteria and) {
        this.and = and;
    }

    public SiteConfigCriteria getAnd() {
        return and;
    }

    public SiteConfigCriteria and() {
        if (and == null) {
            and = new SiteConfigCriteria();
        }
        return and;
    }

    public void setOr(SiteConfigCriteria or) {
        this.or = or;
    }

    public SiteConfigCriteria getOr() {
        return or;
    }

    public SiteConfigCriteria or() {
        if (or == null) {
            or = new SiteConfigCriteria();
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

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            categoryName = new StringFilter();
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public StringFilter getCategoryKey() {
        return categoryKey;
    }

    public StringFilter categoryKey() {
        if (categoryKey == null) {
            categoryKey = new StringFilter();
        }
        return categoryKey;
    }

    public void setCategoryKey(StringFilter categoryKey) {
        this.categoryKey = categoryKey;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            disabled = new BooleanFilter();
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
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

    public BooleanFilter getBuiltIn() {
        return builtIn;
    }

    public BooleanFilter builtIn() {
        if (builtIn == null) {
            builtIn = new BooleanFilter();
        }
        return builtIn;
    }

    public void setBuiltIn(BooleanFilter builtIn) {
        this.builtIn = builtIn;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            createdBy = new LongFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new LongFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            itemsId = new LongFilter();
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public StringFilter getItemsName() {
        return itemsName;
    }

    public StringFilter itemsName() {
        if (itemsName == null) {
            itemsName = new StringFilter();
        }
        return itemsName;
    }

    public void setItemsName(StringFilter itemsName) {
        this.itemsName = itemsName;
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
        final SiteConfigCriteria that = (SiteConfigCriteria) o;
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
            Objects.equals(itemsName, that.itemsName) &&
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
            itemsName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfigCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
            (categoryKey != null ? "categoryKey=" + categoryKey + ", " : "") +
            (disabled != null ? "disabled=" + disabled + ", " : "") +
            (sortValue != null ? "sortValue=" + sortValue + ", " : "") +
            (builtIn != null ? "builtIn=" + builtIn + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
            (itemsName != null ? "itemsName=" + itemsName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
