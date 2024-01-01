package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 优先级
 */
public enum PriorityLevel {
    HIGH("HIGH", "高"),
    MEDIUM("MEDIUM", "中"),
    LOW("LOW", "低");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    PriorityLevel(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PriorityLevel getByValue(String value) {
        for (PriorityLevel enumPriorityLevel : PriorityLevel.values()) {
            if (enumPriorityLevel.getValue().equals(value)) {
                return enumPriorityLevel;
            }
        }
        return null;
    }

    public static PriorityLevel getByDesc(String desc) {
        for (PriorityLevel enumPriorityLevel : PriorityLevel.values()) {
            if (enumPriorityLevel.getDesc().equals(desc)) {
                return enumPriorityLevel;
            }
        }
        return null;
    }
}
