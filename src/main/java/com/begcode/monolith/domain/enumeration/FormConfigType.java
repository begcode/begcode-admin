package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 表单配置类型
 */
public enum FormConfigType {
    /**
     * 模型表单
     */
    MODEL_FORM("MODEL_FORM", "模型表单"),
    /**
     * 数据表单
     */
    DATA_FORM("DATA_FORM", "数据表单");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    FormConfigType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static FormConfigType getByValue(String value) {
        for (FormConfigType enumFormConfigType : FormConfigType.values()) {
            if (enumFormConfigType.getValue().equals(value)) {
                return enumFormConfigType;
            }
        }
        return null;
    }

    public static FormConfigType getByDesc(String desc) {
        for (FormConfigType enumFormConfigType : FormConfigType.values()) {
            if (enumFormConfigType.getDesc().equals(desc)) {
                return enumFormConfigType;
            }
        }
        return null;
    }
}
