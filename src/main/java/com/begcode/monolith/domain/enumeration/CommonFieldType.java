package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 字段类型
 */
public enum CommonFieldType {
    INTEGER("INTEGER", "整数"),
    LONG("LONG", "长整数"),
    BOOLEAN("BOOLEAN", "布尔"),
    STRING("STRING", "字符串"),
    FLOAT("FLOAT", "单精度"),
    DOUBLE("DOUBLE", "双精度"),
    ZONED_DATE_TIME("ZONED_DATE_TIME", "日期时间"),
    LOCATE_DATE("LOCATE_DATE", "本地日期"),
    BIG_DECIMA("BIG_DECIMA", "大数字"),
    L("L", "L"),
    TEXTBLOB("TEXTBLOB", "大文本"),
    IMAGEBLOB("IMAGEBLOB", "大图片"),
    ARRAY("ARRAY", "数组"),
    ENUM("ENUM", "枚举"),
    UPLOAD_IMAGE("UPLOAD_IMAGE", "上传图片"),
    UPLOAD_FILE("UPLOAD_FILE", "上传文件"),
    ENTITY("ENTITY", "实体"),
    RADIO("RADIO", "单选"),
    MULTI_SELECT("MULTI_SELECT", "多选"),
    DATA_DICTIONARY("DATA_DICTIONARY", "数据字典"),
    UUID("UUID", "UUID"),
    INSTANT("INSTANT", "时间戳");

    @EnumValue
    @JsonValue
    private String value;

    private String desc;

    CommonFieldType() {}

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
