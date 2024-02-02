package com.begcode.monolith.settings.service.criteria;

import com.begcode.monolith.domain.enumeration.RegionCodeLevel;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.domain.RegionCode;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private RegionCodeCriteria and;

    @BindQuery(ignore = true)
    private RegionCodeCriteria or;

    /**
     * Class for filtering RegionCodeLevel
     */
    public static class RegionCodeLevelFilter extends Filter<RegionCodeLevel> {

        public RegionCodeLevelFilter() {}

        public RegionCodeLevelFilter(String value) {
            RegionCodeLevel enumValue = RegionCodeLevel.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = RegionCodeLevel.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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

    @BindQuery(entity = RegionCode.class, column = "id", condition = "id=parent_id")
    private LongFilter childrenId;

    @BindQuery(entity = RegionCode.class, column = "name", condition = "id=parent_id")
    private StringFilter childrenName;

    @BindQuery(column = "self.parent_id")
    private LongFilter parentId;

    @BindQuery(entity = RegionCode.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public RegionCodeCriteria() {}

    public RegionCodeCriteria(RegionCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.areaCode = other.areaCode == null ? null : other.areaCode.copy();
        this.cityCode = other.cityCode == null ? null : other.cityCode.copy();
        this.mergerName = other.mergerName == null ? null : other.mergerName.copy();
        this.shortName = other.shortName == null ? null : other.shortName.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.lng = other.lng == null ? null : other.lng.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RegionCodeCriteria copy() {
        return new RegionCodeCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAreaCode() {
        return areaCode;
    }

    public StringFilter areaCode() {
        if (areaCode == null) {
            areaCode = new StringFilter();
        }
        return areaCode;
    }

    public void setAreaCode(StringFilter areaCode) {
        this.areaCode = areaCode;
    }

    public StringFilter getCityCode() {
        return cityCode;
    }

    public StringFilter cityCode() {
        if (cityCode == null) {
            cityCode = new StringFilter();
        }
        return cityCode;
    }

    public void setCityCode(StringFilter cityCode) {
        this.cityCode = cityCode;
    }

    public StringFilter getMergerName() {
        return mergerName;
    }

    public StringFilter mergerName() {
        if (mergerName == null) {
            mergerName = new StringFilter();
        }
        return mergerName;
    }

    public void setMergerName(StringFilter mergerName) {
        this.mergerName = mergerName;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public StringFilter shortName() {
        if (shortName == null) {
            shortName = new StringFilter();
        }
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            zipCode = new StringFilter();
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public RegionCodeLevelFilter getLevel() {
        return level;
    }

    public RegionCodeLevelFilter level() {
        if (level == null) {
            level = new RegionCodeLevelFilter();
        }
        return level;
    }

    public void setLevel(RegionCodeLevelFilter level) {
        this.level = level;
    }

    public DoubleFilter getLng() {
        return lng;
    }

    public DoubleFilter lng() {
        if (lng == null) {
            lng = new DoubleFilter();
        }
        return lng;
    }

    public void setLng(DoubleFilter lng) {
        this.lng = lng;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public DoubleFilter lat() {
        if (lat == null) {
            lat = new DoubleFilter();
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            childrenName = new StringFilter();
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public StringFilter parentName() {
        if (parentName == null) {
            parentName = new StringFilter();
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
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
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            areaCode,
            cityCode,
            mergerName,
            shortName,
            zipCode,
            level,
            lng,
            lat,
            childrenId,
            childrenName,
            parentId,
            parentName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (areaCode != null ? "areaCode=" + areaCode + ", " : "") +
            (cityCode != null ? "cityCode=" + cityCode + ", " : "") +
            (mergerName != null ? "mergerName=" + mergerName + ", " : "") +
            (shortName != null ? "shortName=" + shortName + ", " : "") +
            (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (lng != null ? "lng=" + lng + ", " : "") +
            (lat != null ? "lat=" + lat + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (parentName != null ? "parentName=" + parentName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
