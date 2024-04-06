package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 打开方式
 */
public enum AnnoOpenType {
    /**
     * URL地址
     */
    URL("URL", "URL地址"),
    /**
     * 组件
     */
    COMPONENT("COMPONENT", "组件"),
    /**
     * 弹窗详情
     */
    MODAL_DETAIL("MODAL_DETAIL", "弹窗详情");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    AnnoOpenType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AnnoOpenType getByValue(String value) {
        for (AnnoOpenType enumAnnoOpenType : AnnoOpenType.values()) {
            if (enumAnnoOpenType.getValue().equals(value)) {
                return enumAnnoOpenType;
            }
        }
        return null;
    }

    public static AnnoOpenType getByDesc(String desc) {
        for (AnnoOpenType enumAnnoOpenType : AnnoOpenType.values()) {
            if (enumAnnoOpenType.getDesc().equals(desc)) {
                return enumAnnoOpenType;
            }
        }
        return null;
    }
}
