package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 字段参数类型
 */
public enum FieldParamType {
    DATETIME("DATETIME", "日期时间"),
    NUMBER("NUMBER", "数字序列"),
    FIXED_CHAR("FIXED_CHAR", "固定字符"),
    PARAM("PARAM", "参数");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    FieldParamType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static FieldParamType getByValue(String value) {
        for (FieldParamType enumFieldParamType : FieldParamType.values()) {
            if (enumFieldParamType.getValue().equals(value)) {
                return enumFieldParamType;
            }
        }
        return null;
    }

    public static FieldParamType getByDesc(String desc) {
        for (FieldParamType enumFieldParamType : FieldParamType.values()) {
            if (enumFieldParamType.getDesc().equals(desc)) {
                return enumFieldParamType;
            }
        }
        return null;
    }
}
