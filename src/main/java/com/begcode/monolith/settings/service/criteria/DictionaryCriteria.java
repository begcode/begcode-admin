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
 * Criteria class for the {@link com.begcode.monolith.settings.domain.Dictionary} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.DictionaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dictionaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DictionaryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.dict_name")
    private StringFilter dictName;

    @BindQuery(column = "self.dict_key")
    private StringFilter dictKey;

    @BindQuery(column = "self.disabled")
    private BooleanFilter disabled;

    @BindQuery(column = "self.sort_value")
    private IntegerFilter sortValue;

    @BindQuery(column = "self.built_in")
    private BooleanFilter builtIn;

    @BindQuery(column = "self.sync_enum")
    private BooleanFilter syncEnum;

    @BindQuery(entity = CommonFieldData.class, column = "id", condition = "owner_entity_name = 'Dictionary' AND owner_entity_id=this.id")
    private LongFilter itemsId;

    @BindQuery(entity = CommonFieldData.class, column = "name", condition = "owner_entity_name = 'Dictionary' AND owner_entity_id=this.id")
    private StringFilter itemsName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private DictionaryCriteria and;

    @BindQuery(ignore = true)
    private DictionaryCriteria or;

    private Boolean distinct;

    public DictionaryCriteria() {}

    public DictionaryCriteria(DictionaryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dictName = other.optionalDictName().map(StringFilter::copy).orElse(null);
        this.dictKey = other.optionalDictKey().map(StringFilter::copy).orElse(null);
        this.disabled = other.optionalDisabled().map(BooleanFilter::copy).orElse(null);
        this.sortValue = other.optionalSortValue().map(IntegerFilter::copy).orElse(null);
        this.builtIn = other.optionalBuiltIn().map(BooleanFilter::copy).orElse(null);
        this.syncEnum = other.optionalSyncEnum().map(BooleanFilter::copy).orElse(null);
        this.itemsId = other.optionalItemsId().map(LongFilter::copy).orElse(null);
        this.itemsName = other.optionalItemsName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DictionaryCriteria copy() {
        return new DictionaryCriteria(this);
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

    public StringFilter getDictName() {
        return dictName;
    }

    public Optional<StringFilter> optionalDictName() {
        return Optional.ofNullable(dictName);
    }

    public StringFilter dictName() {
        if (dictName == null) {
            setDictName(new StringFilter());
        }
        return dictName;
    }

    public void setDictName(StringFilter dictName) {
        this.dictName = dictName;
    }

    public StringFilter getDictKey() {
        return dictKey;
    }

    public Optional<StringFilter> optionalDictKey() {
        return Optional.ofNullable(dictKey);
    }

    public StringFilter dictKey() {
        if (dictKey == null) {
            setDictKey(new StringFilter());
        }
        return dictKey;
    }

    public void setDictKey(StringFilter dictKey) {
        this.dictKey = dictKey;
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

    public BooleanFilter getSyncEnum() {
        return syncEnum;
    }

    public Optional<BooleanFilter> optionalSyncEnum() {
        return Optional.ofNullable(syncEnum);
    }

    public BooleanFilter syncEnum() {
        if (syncEnum == null) {
            setSyncEnum(new BooleanFilter());
        }
        return syncEnum;
    }

    public void setSyncEnum(BooleanFilter syncEnum) {
        this.syncEnum = syncEnum;
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

    public void setAnd(DictionaryCriteria and) {
        this.and = and;
    }

    public DictionaryCriteria getAnd() {
        return and;
    }

    public DictionaryCriteria and() {
        if (and == null) {
            and = new DictionaryCriteria();
        }
        return and;
    }

    public void setOr(DictionaryCriteria or) {
        this.or = or;
    }

    public DictionaryCriteria getOr() {
        return or;
    }

    public DictionaryCriteria or() {
        if (or == null) {
            or = new DictionaryCriteria();
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
        final DictionaryCriteria that = (DictionaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dictName, that.dictName) &&
            Objects.equals(dictKey, that.dictKey) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(sortValue, that.sortValue) &&
            Objects.equals(builtIn, that.builtIn) &&
            Objects.equals(syncEnum, that.syncEnum) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictName, dictKey, disabled, sortValue, builtIn, syncEnum, itemsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DictionaryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDictName().map(f -> "dictName=" + f + ", ").orElse("") +
            optionalDictKey().map(f -> "dictKey=" + f + ", ").orElse("") +
            optionalDisabled().map(f -> "disabled=" + f + ", ").orElse("") +
            optionalSortValue().map(f -> "sortValue=" + f + ", ").orElse("") +
            optionalBuiltIn().map(f -> "builtIn=" + f + ", ").orElse("") +
            optionalSyncEnum().map(f -> "syncEnum=" + f + ", ").orElse("") +
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
