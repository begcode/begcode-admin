package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 业务类型
 */
public enum AnnoBusinessType {
    EMAIL("EMAIL", "邮件"),
    WORKFLOW("WORKFLOW", "工作流");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    AnnoBusinessType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AnnoBusinessType getByValue(String value) {
        for (AnnoBusinessType enumAnnoBusinessType : AnnoBusinessType.values()) {
            if (enumAnnoBusinessType.getValue().equals(value)) {
                return enumAnnoBusinessType;
            }
        }
        return null;
    }

    public static AnnoBusinessType getByDesc(String desc) {
        for (AnnoBusinessType enumAnnoBusinessType : AnnoBusinessType.values()) {
            if (enumAnnoBusinessType.getDesc().equals(desc)) {
                return enumAnnoBusinessType;
            }
        }
        return null;
    }
}
