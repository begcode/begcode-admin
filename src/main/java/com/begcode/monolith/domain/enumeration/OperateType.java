package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 操作类型
 */
public enum OperateType {
    LIST("LIST", "列表"),
    ADD("ADD", "新增"),
    EDIT("EDIT", "编辑"),
    DELETE("DELETE", "删除"),
    IMPORT("IMPORT", "导入"),
    EXPORT("EXPORT", "导出"),
    OTHER("OTHER", "其他"),
    LOGIN("LOGIN", "登录"),
    STATS("STATS", "统计"),
    VIEW("VIEW", "查看"),
    AUDIT("AUDIT", "审核"),
    WORK_FLOW("WORK_FLOW", "工作流");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    OperateType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OperateType getByValue(String value) {
        for (OperateType enumOperateType : OperateType.values()) {
            if (enumOperateType.getValue().equals(value)) {
                return enumOperateType;
            }
        }
        return null;
    }

    public static OperateType getByDesc(String desc) {
        for (OperateType enumOperateType : OperateType.values()) {
            if (enumOperateType.getDesc().equals(desc)) {
                return enumOperateType;
            }
        }
        return null;
    }
}
