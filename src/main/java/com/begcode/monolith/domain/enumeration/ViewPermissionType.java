package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 可视权限类型
 */
public enum ViewPermissionType {
    /**
     * 菜单
     */
    MENU("MENU", "菜单"),
    /**
     * 按钮
     */
    BUTTON("BUTTON", "按钮");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    ViewPermissionType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ViewPermissionType getByValue(String value) {
        for (ViewPermissionType enumViewPermissionType : ViewPermissionType.values()) {
            if (enumViewPermissionType.getValue().equals(value)) {
                return enumViewPermissionType;
            }
        }
        return null;
    }

    public static ViewPermissionType getByDesc(String desc) {
        for (ViewPermissionType enumViewPermissionType : ViewPermissionType.values()) {
            if (enumViewPermissionType.getDesc().equals(desc)) {
                return enumViewPermissionType;
            }
        }
        return null;
    }
}
