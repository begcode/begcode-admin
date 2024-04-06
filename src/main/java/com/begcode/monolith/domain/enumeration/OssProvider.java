package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * OSS提供商
 */
public enum OssProvider {
    /**
     * 本地
     */
    LOCAL("LOCAL", "本地"),
    /**
     * MINIO
     */
    MINIO("MINIO", "MINIO"),
    /**
     * 七牛云
     */
    QINIU("QINIU", "七牛云"),
    /**
     * 阿里云
     */
    ALI("ALI", "阿里云"),
    /**
     * 腾讯云
     */
    TENCENT("TENCENT", "腾讯云");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    OssProvider(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OssProvider getByValue(String value) {
        for (OssProvider enumOssProvider : OssProvider.values()) {
            if (enumOssProvider.getValue().equals(value)) {
                return enumOssProvider;
            }
        }
        return null;
    }

    public static OssProvider getByDesc(String desc) {
        for (OssProvider enumOssProvider : OssProvider.values()) {
            if (enumOssProvider.getDesc().equals(desc)) {
                return enumOssProvider;
            }
        }
        return null;
    }
}
