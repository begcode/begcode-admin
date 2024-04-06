package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 消息类型
 */
public enum AnnoCategory {
    /**
     * 系统消息
     */
    SYSTEM_INFO("SYSTEM_INFO", "系统消息"),
    /**
     * 通知
     */
    NOTICE("NOTICE", "通知"),
    /**
     * 待办
     */
    TODO("TODO", "待办");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    AnnoCategory(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AnnoCategory getByValue(String value) {
        for (AnnoCategory enumAnnoCategory : AnnoCategory.values()) {
            if (enumAnnoCategory.getValue().equals(value)) {
                return enumAnnoCategory;
            }
        }
        return null;
    }

    public static AnnoCategory getByDesc(String desc) {
        for (AnnoCategory enumAnnoCategory : AnnoCategory.values()) {
            if (enumAnnoCategory.getDesc().equals(desc)) {
                return enumAnnoCategory;
            }
        }
        return null;
    }
}
