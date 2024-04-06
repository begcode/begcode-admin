package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Api权限类型
 */
public enum ApiPermissionType {
    /**
     * 业务
     */
    BUSINESS("BUSINESS", "业务"),
    /**
     * API接口
     */
    API("API", "API接口"),
    /**
     * 实体
     */
    ENTITY("ENTITY", "实体"),
    /**
     * 微服务
     */
    MICRO_SERVICE("MICRO_SERVICE", "微服务");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    ApiPermissionType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static ApiPermissionType getByValue(String value) {
        for (ApiPermissionType enumApiPermissionType : ApiPermissionType.values()) {
            if (enumApiPermissionType.getValue().equals(value)) {
                return enumApiPermissionType;
            }
        }
        return null;
    }

    public static ApiPermissionType getByDesc(String desc) {
        for (ApiPermissionType enumApiPermissionType : ApiPermissionType.values()) {
            if (enumApiPermissionType.getDesc().equals(desc)) {
                return enumApiPermissionType;
            }
        }
        return null;
    }
}
