package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 通告对象类型
 */
public enum ReceiverType {
    /**
     * 全部
     */
    ALL("ALL", "全部"),
    /**
     * 用户
     */
    USER("USER", "用户"),
    /**
     * 部门
     */
    DEPARTMENT("DEPARTMENT", "部门"),
    /**
     * 角色
     */
    AUTHORITY("AUTHORITY", "角色"),
    /**
     * 岗位
     */
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
