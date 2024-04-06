package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 字段类型
 */
public enum CommonFieldType {
    /**
     * 整数
     */
    INTEGER("INTEGER", "整数"),
    /**
     * 长整数
     */
    LONG("LONG", "长整数"),
    /**
     * 布尔
     */
    BOOLEAN("BOOLEAN", "布尔"),
    /**
     * 字符串
     */
    STRING("STRING", "字符串"),
    /**
     * 单精度
     */
    FLOAT("FLOAT", "单精度"),
    /**
     * 双精度
     */
    DOUBLE("DOUBLE", "双精度"),
    /**
     * 日期时间
     */
    ZONED_DATE_TIME("ZONED_DATE_TIME", "日期时间"),
    /**
     * 本地日期
     */
    LOCATE_DATE("LOCATE_DATE", "本地日期"),
    /**
     * 大数字
     */
    BIG_DECIMA("BIG_DECIMA", "大数字"),
    /**
     * 大文本
     */
    TEXTBLOB("TEXTBLOB", "大文本"),
    /**
     * 大图片
     */
    IMAGEBLOB("IMAGEBLOB", "大图片"),
    /**
     * 数组
     */
    ARRAY("ARRAY", "数组"),
    /**
     * 枚举
     */
    ENUM("ENUM", "枚举"),
    /**
     * 上传图片
     */
    UPLOAD_IMAGE("UPLOAD_IMAGE", "上传图片"),
    /**
     * 上传文件
     */
    UPLOAD_FILE("UPLOAD_FILE", "上传文件"),
    /**
     * 实体
     */
    ENTITY("ENTITY", "实体"),
    /**
     * 单选
     */
    RADIO("RADIO", "单选"),
    /**
     * 多选
     */
    MULTI_SELECT("MULTI_SELECT", "多选"),
    /**
     * 数据字典
     */
    DATA_DICTIONARY("DATA_DICTIONARY", "数据字典"),
    /**
     * UUID
     */
    UUID_STRING("UUID_STRING", "UUID"),
    /**
     * 时间戳
     */
    INSTANT("INSTANT", "时间戳");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    CommonFieldType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static CommonFieldType getByValue(String value) {
        for (CommonFieldType enumCommonFieldType : CommonFieldType.values()) {
            if (enumCommonFieldType.getValue().equals(value)) {
                return enumCommonFieldType;
            }
        }
        return null;
    }

    public static CommonFieldType getByDesc(String desc) {
        for (CommonFieldType enumCommonFieldType : CommonFieldType.values()) {
            if (enumCommonFieldType.getDesc().equals(desc)) {
                return enumCommonFieldType;
            }
        }
        return null;
    }
}
