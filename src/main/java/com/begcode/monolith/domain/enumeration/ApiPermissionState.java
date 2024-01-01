package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Api权限状态
 */
public enum ApiPermissionState {
    CONFIGURABLE("CONFIGURABLE", "可配置"),
    PERMIT_ALL("PERMIT_ALL", "允许所有"),
    UNREACHABLE("UNREACHABLE", "不可达"),
    AUTHENTICATE("AUTHENTICATE", "认证");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    ApiPermissionState(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ApiPermissionState getByValue(String value) {
        for (ApiPermissionState enumApiPermissionState : ApiPermissionState.values()) {
            if (enumApiPermissionState.getValue().equals(value)) {
                return enumApiPermissionState;
            }
        }
        return null;
    }

    public static ApiPermissionState getByDesc(String desc) {
        for (ApiPermissionState enumApiPermissionState : ApiPermissionState.values()) {
            if (enumApiPermissionState.getDesc().equals(desc)) {
                return enumApiPermissionState;
            }
        }
        return null;
    }
}
