package com.begcode.monolith.settings.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.RegionCodeLevel;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 行政区划码
 */
@TableName(value = "region_code")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 地区代码
     */
    @TableField(value = "area_code")
    private String areaCode;

    /**
     * 城市代码
     */
    @TableField(value = "city_code")
    private String cityCode;

    /**
     * 全名
     */
    @TableField(value = "merger_name")
    private String mergerName;

    /**
     * 短名称
     */
    @TableField(value = "short_name")
    private String shortName;

    /**
     * 邮政编码
     */
    @TableField(value = "zip_code")
    private String zipCode;

    /**
     * 等级
     */
    @TableField(value = "level")
    private RegionCodeLevel level;

    /**
     * 经度
     */
    @TableField(value = "lng")
    private Double lng;

    /**
     * 纬度
     */
    @TableField(value = "lat")
    private Double lat;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = RegionCode.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private List<RegionCode> children = new ArrayList<>();

    /**
     * 上级节点
     */
    @TableField(exist = false)
    @BindEntity(entity = RegionCode.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private RegionCode parent;

    @TableField(value = "parent_id")
    private Long parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public RegionCode id(Long id) {
        this.id = id;
        return this;
    }

    public RegionCode name(String name) {
        this.name = name;
        return this;
    }

    public RegionCode areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public RegionCode cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public RegionCode mergerName(String mergerName) {
        this.mergerName = mergerName;
        return this;
    }

    public RegionCode shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public RegionCode zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public RegionCode level(RegionCodeLevel level) {
        this.level = level;
        return this;
    }

    public RegionCode lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public RegionCode lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public RegionCode children(List<RegionCode> regionCodes) {
        this.children = regionCodes;
        return this;
    }

    public RegionCode parent(RegionCode regionCode) {
        this.parent = regionCode;
        return this;
    }

    public RegionCode parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionCode)) {
            return false;
        }
        return getId() != null && getId().equals(((RegionCode) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionCode{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", areaCode='" + getAreaCode() + "'" +
            ", cityCode='" + getCityCode() + "'" +
            ", mergerName='" + getMergerName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", level='" + getLevel() + "'" +
            ", lng=" + getLng() +
            ", lat=" + getLat() +
            "}";
    }
}
