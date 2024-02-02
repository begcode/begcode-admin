package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.settings.domain.CommonFieldData;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private DictionaryCriteria and;

    @BindQuery(ignore = true)
    private DictionaryCriteria or;

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

    @BindQuery(entity = CommonFieldData.class, column = "id", condition = "owner_entity_name = 'Dictionary' AND owner_entity_id=id")
    private LongFilter itemsId;

    @BindQuery(entity = CommonFieldData.class, column = "name", condition = "owner_entity_name = 'Dictionary' AND owner_entity_id=id")
    private StringFilter itemsName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public DictionaryCriteria() {}

    public DictionaryCriteria(DictionaryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dictName = other.dictName == null ? null : other.dictName.copy();
        this.dictKey = other.dictKey == null ? null : other.dictKey.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.sortValue = other.sortValue == null ? null : other.sortValue.copy();
        this.builtIn = other.builtIn == null ? null : other.builtIn.copy();
        this.syncEnum = other.syncEnum == null ? null : other.syncEnum.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.itemsName = other.itemsName == null ? null : other.itemsName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DictionaryCriteria copy() {
        return new DictionaryCriteria(this);
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

    public StringFilter getDictName() {
        return dictName;
    }

    public StringFilter dictName() {
        if (dictName == null) {
            dictName = new StringFilter();
        }
        return dictName;
    }

    public void setDictName(StringFilter dictName) {
        this.dictName = dictName;
    }

    public StringFilter getDictKey() {
        return dictKey;
    }

    public StringFilter dictKey() {
        if (dictKey == null) {
            dictKey = new StringFilter();
        }
        return dictKey;
    }

    public void setDictKey(StringFilter dictKey) {
        this.dictKey = dictKey;
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

    public BooleanFilter getSyncEnum() {
        return syncEnum;
    }

    public BooleanFilter syncEnum() {
        if (syncEnum == null) {
            syncEnum = new BooleanFilter();
        }
        return syncEnum;
    }

    public void setSyncEnum(BooleanFilter syncEnum) {
        this.syncEnum = syncEnum;
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
            Objects.equals(itemsName, that.itemsName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictName, dictKey, disabled, sortValue, builtIn, syncEnum, itemsId, itemsName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DictionaryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dictName != null ? "dictName=" + dictName + ", " : "") +
            (dictKey != null ? "dictKey=" + dictKey + ", " : "") +
            (disabled != null ? "disabled=" + disabled + ", " : "") +
            (sortValue != null ? "sortValue=" + sortValue + ", " : "") +
            (builtIn != null ? "builtIn=" + builtIn + ", " : "") +
            (syncEnum != null ? "syncEnum=" + syncEnum + ", " : "") +
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
