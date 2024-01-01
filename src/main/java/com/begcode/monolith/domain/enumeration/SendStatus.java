package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 推送状态
 */
public enum SendStatus {
    WAITING("WAITING", "未推送"),
    SUCCESS("SUCCESS", "推送成功"),
    FAILURE("FAILURE", "推送失败"),
    NOT_TRY("NOT_TRY", "失败不再发送");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    SendStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SendStatus getByValue(String value) {
        for (SendStatus enumSendStatus : SendStatus.values()) {
            if (enumSendStatus.getValue().equals(value)) {
                return enumSendStatus;
            }
        }
        return null;
    }

    public static SendStatus getByDesc(String desc) {
        for (SendStatus enumSendStatus : SendStatus.values()) {
            if (enumSendStatus.getDesc().equals(desc)) {
                return enumSendStatus;
            }
        }
        return null;
    }
}
