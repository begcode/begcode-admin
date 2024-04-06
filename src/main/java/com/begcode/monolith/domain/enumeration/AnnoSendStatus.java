package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 发布状态
 */
public enum AnnoSendStatus {
    /**
     * 未发布
     */
    NOT_RELEASE("NOT_RELEASE", "未发布"),
    /**
     * 已发布
     */
    RELEASED("RELEASED", "已发布"),
    /**
     * 已撤销
     */
    CANCELED("CANCELED", "已撤销");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    AnnoSendStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AnnoSendStatus getByValue(String value) {
        for (AnnoSendStatus enumAnnoSendStatus : AnnoSendStatus.values()) {
            if (enumAnnoSendStatus.getValue().equals(value)) {
                return enumAnnoSendStatus;
            }
        }
        return null;
    }

    public static AnnoSendStatus getByDesc(String desc) {
        for (AnnoSendStatus enumAnnoSendStatus : AnnoSendStatus.values()) {
            if (enumAnnoSendStatus.getDesc().equals(desc)) {
                return enumAnnoSendStatus;
            }
        }
        return null;
    }
}
