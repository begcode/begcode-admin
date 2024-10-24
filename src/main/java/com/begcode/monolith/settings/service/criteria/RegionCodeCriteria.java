package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.RegionCodeLevel;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.domain.RegionCode;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.settings.domain.RegionCode} entity. This class is used
 * in {@link com.begcode.monolith.settings.web.rest.RegionCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /region-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegionCodeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RegionCodeLevel
     */
    public static class RegionCodeLevelFilter extends Filter<RegionCodeLevel> {

        public RegionCodeLevelFilter() {}

        public RegionCodeLevelFilter(RegionCodeLevelFilter filter) {
            super(filter);
        }

        @Override
        public RegionCodeLevelFilter copy() {
            return new RegionCodeLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.area_code")
    private StringFilter areaCode;

    @BindQuery(column = "self.city_code")
    private StringFilter cityCode;

    @BindQuery(column = "self.merger_name")
    private StringFilter mergerName;

    @BindQuery(column = "self.short_name")
    private StringFilter shortName;

    @BindQuery(column = "self.zip_code")
    private StringFilter zipCode;

    @BindQuery(column = "self.level")
    private RegionCodeLevelFilter level;

    @BindQuery(column = "self.lng")
    private DoubleFilter lng;

    @BindQuery(column = "self.lat")
    private DoubleFilter lat;

    @BindQuery(entity = RegionCode.class, column = "id", condition = "parent_id=this.id")
    private LongFilter childrenId;

    @BindQuery(entity = RegionCode.class, column = "name", condition = "parent_id=this.id")
    private StringFilter childrenName;

    @BindQuery(entity = RegionCode.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = RegionCode.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private RegionCodeCriteria and;

    @BindQuery(ignore = true)
    private RegionCodeCriteria or;

    private Boolean distinct;

    public RegionCodeCriteria() {}

    public RegionCodeCriteria(RegionCodeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.areaCode = other.optionalAreaCode().map(StringFilter::copy).orElse(null);
        this.cityCode = other.optionalCityCode().map(StringFilter::copy).orElse(null);
        this.mergerName = other.optionalMergerName().map(StringFilter::copy).orElse(null);
        this.shortName = other.optionalShortName().map(StringFilter::copy).orElse(null);
        this.zipCode = other.optionalZipCode().map(StringFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(RegionCodeLevelFilter::copy).orElse(null);
        this.lng = other.optionalLng().map(DoubleFilter::copy).orElse(null);
        this.lat = other.optionalLat().map(DoubleFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenName = other.optionalChildrenName().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentName = other.optionalParentName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RegionCodeCriteria copy() {
        return new RegionCodeCriteria(this);
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

    public StringFilter getAreaCode() {
        return areaCode;
    }

    public Optional<StringFilter> optionalAreaCode() {
        return Optional.ofNullable(areaCode);
    }

    public StringFilter areaCode() {
        if (areaCode == null) {
            setAreaCode(new StringFilter());
        }
        return areaCode;
    }

    public void setAreaCode(StringFilter areaCode) {
        this.areaCode = areaCode;
    }

    public StringFilter getCityCode() {
        return cityCode;
    }

    public Optional<StringFilter> optionalCityCode() {
        return Optional.ofNullable(cityCode);
    }

    public StringFilter cityCode() {
        if (cityCode == null) {
            setCityCode(new StringFilter());
        }
        return cityCode;
    }

    public void setCityCode(StringFilter cityCode) {
        this.cityCode = cityCode;
    }

    public StringFilter getMergerName() {
        return mergerName;
    }

    public Optional<StringFilter> optionalMergerName() {
        return Optional.ofNullable(mergerName);
    }

    public StringFilter mergerName() {
        if (mergerName == null) {
            setMergerName(new StringFilter());
        }
        return mergerName;
    }

    public void setMergerName(StringFilter mergerName) {
        this.mergerName = mergerName;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public Optional<StringFilter> optionalShortName() {
        return Optional.ofNullable(shortName);
    }

    public StringFilter shortName() {
        if (shortName == null) {
            setShortName(new StringFilter());
        }
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public Optional<StringFilter> optionalZipCode() {
        return Optional.ofNullable(zipCode);
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            setZipCode(new StringFilter());
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public RegionCodeLevelFilter getLevel() {
        return level;
    }

    public Optional<RegionCodeLevelFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public RegionCodeLevelFilter level() {
        if (level == null) {
            setLevel(new RegionCodeLevelFilter());
        }
        return level;
    }

    public void setLevel(RegionCodeLevelFilter level) {
        this.level = level;
    }

    public DoubleFilter getLng() {
        return lng;
    }

    public Optional<DoubleFilter> optionalLng() {
        return Optional.ofNullable(lng);
    }

    public DoubleFilter lng() {
        if (lng == null) {
            setLng(new DoubleFilter());
        }
        return lng;
    }

    public void setLng(DoubleFilter lng) {
        this.lng = lng;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public Optional<DoubleFilter> optionalLat() {
        return Optional.ofNullable(lat);
    }

    public DoubleFilter lat() {
        if (lat == null) {
            setLat(new DoubleFilter());
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public Optional<LongFilter> optionalChildrenId() {
        return Optional.ofNullable(childrenId);
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            setChildrenId(new LongFilter());
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public Optional<StringFilter> optionalChildrenName() {
        return Optional.ofNullable(childrenName);
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            setChildrenName(new StringFilter());
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public Optional<StringFilter> optionalParentName() {
        return Optional.ofNullable(parentName);
    }

    public StringFilter parentName() {
        if (parentName == null) {
            setParentName(new StringFilter());
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
    }

    public void setAnd(RegionCodeCriteria and) {
        this.and = and;
    }

    public RegionCodeCriteria getAnd() {
        return and;
    }

    public RegionCodeCriteria and() {
        if (and == null) {
            and = new RegionCodeCriteria();
        }
        return and;
    }

    public void setOr(RegionCodeCriteria or) {
        this.or = or;
    }

    public RegionCodeCriteria getOr() {
        return or;
    }

    public RegionCodeCriteria or() {
        if (or == null) {
            or = new RegionCodeCriteria();
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
        final RegionCodeCriteria that = (RegionCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(areaCode, that.areaCode) &&
            Objects.equals(cityCode, that.cityCode) &&
            Objects.equals(mergerName, that.mergerName) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(level, that.level) &&
            Objects.equals(lng, that.lng) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, areaCode, cityCode, mergerName, shortName, zipCode, level, lng, lat, childrenId, parentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCodeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAreaCode().map(f -> "areaCode=" + f + ", ").orElse("") +
            optionalCityCode().map(f -> "cityCode=" + f + ", ").orElse("") +
            optionalMergerName().map(f -> "mergerName=" + f + ", ").orElse("") +
            optionalShortName().map(f -> "shortName=" + f + ", ").orElse("") +
            optionalZipCode().map(f -> "zipCode=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalLng().map(f -> "lng=" + f + ", ").orElse("") +
            optionalLat().map(f -> "lat=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenName().map(f -> "childrenName=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalParentName().map(f -> "parentName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
