package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 操作类型
 */
public enum OperateType {
    /**
     * 列表
     */
    LIST("LIST", "列表"),
    /**
     * 新增
     */
    ADD("ADD", "新增"),
    /**
     * 编辑
     */
    EDIT("EDIT", "编辑"),
    /**
     * 删除
     */
    DELETE("DELETE", "删除"),
    /**
     * 导入
     */
    IMPORT("IMPORT", "导入"),
    /**
     * 导出
     */
    EXPORT("EXPORT", "导出"),
    /**
     * 其他
     */
    OTHER("OTHER", "其他"),
    /**
     * 登录
     */
    LOGIN("LOGIN", "登录"),
    /**
     * 统计
     */
    STATS("STATS", "统计"),
    /**
     * 查看
     */
    VIEW("VIEW", "查看"),
    /**
     * 审核
     */
    AUDIT("AUDIT", "审核"),
    /**
     * 工作流
     */
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
