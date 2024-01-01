package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 通告对象类型
 */
public enum ReceiverType {
    ALL("ALL", "全部"),
    USER("USER", "用户"),
    DEPARTMENT("DEPARTMENT", "部门"),
    AUTHORITY("AUTHORITY", "角色"),
    POSITION("POSITION", "岗位");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    ReceiverType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ReceiverType getByValue(String value) {
        for (ReceiverType enumReceiverType : ReceiverType.values()) {
            if (enumReceiverType.getValue().equals(value)) {
                return enumReceiverType;
            }
        }
        return null;
    }

    public static ReceiverType getByDesc(String desc) {
        for (ReceiverType enumReceiverType : ReceiverType.values()) {
            if (enumReceiverType.getDesc().equals(desc)) {
                return enumReceiverType;
            }
        }
        return null;
    }
}
