package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 通知类型
 */
public enum MessageSendType {
    EMAIL("EMAIL", "邮件"),
    SMS("SMS", "短信"),
    WECHAT("WECHAT", "微信");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    MessageSendType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageSendType getByValue(String value) {
        for (MessageSendType enumMessageSendType : MessageSendType.values()) {
            if (enumMessageSendType.getValue().equals(value)) {
                return enumMessageSendType;
            }
        }
        return null;
    }

    public static MessageSendType getByDesc(String desc) {
        for (MessageSendType enumMessageSendType : MessageSendType.values()) {
            if (enumMessageSendType.getDesc().equals(desc)) {
                return enumMessageSendType;
            }
        }
        return null;
    }
}
