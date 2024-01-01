package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 短信模板类型
 */
public enum SmsTemplateType {
    MESSAGE("MESSAGE", "通知短信"),
    VERIFICATION("VERIFICATION", "验证码"),
    PROMOTION("PROMOTION", "推广短信");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    SmsTemplateType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static SmsTemplateType getByValue(String value) {
        for (SmsTemplateType enumSmsTemplateType : SmsTemplateType.values()) {
            if (enumSmsTemplateType.getValue().equals(value)) {
                return enumSmsTemplateType;
            }
        }
        return null;
    }

    public static SmsTemplateType getByDesc(String desc) {
        for (SmsTemplateType enumSmsTemplateType : SmsTemplateType.values()) {
            if (enumSmsTemplateType.getDesc().equals(desc)) {
                return enumSmsTemplateType;
            }
        }
        return null;
    }
}
