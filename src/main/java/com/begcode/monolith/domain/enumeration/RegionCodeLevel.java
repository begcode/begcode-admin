package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 区域等级
 */
public enum RegionCodeLevel {
    PROVINCE("PROVINCE", "省"),
    CITY("CITY", "市"),
    COUNTY("COUNTY", "区"),
    TOWN("TOWN", "镇"),
    VILLAGE("VILLAGE", "村");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    RegionCodeLevel(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static RegionCodeLevel getByValue(String value) {
        for (RegionCodeLevel enumRegionCodeLevel : RegionCodeLevel.values()) {
            if (enumRegionCodeLevel.getValue().equals(value)) {
                return enumRegionCodeLevel;
            }
        }
        return null;
    }

    public static RegionCodeLevel getByDesc(String desc) {
        for (RegionCodeLevel enumRegionCodeLevel : RegionCodeLevel.values()) {
            if (enumRegionCodeLevel.getDesc().equals(desc)) {
                return enumRegionCodeLevel;
            }
        }
        return null;
    }
}
