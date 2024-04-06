package com.begcode.monolith.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 任务状态
 */
public enum JobStatus {
    /**
     * 正常
     */
    NORMAL("NORMAL", "正常"),
    /**
     * 暂停
     */
    PAUSED("PAUSED", "暂停"),
    /**
     * 完成
     */
    COMPLETE("COMPLETE", "完成"),
    /**
     * 错误
     */
    ERROR("ERROR", "错误"),
    /**
     * 阻塞
     */
    BLOCKED("BLOCKED", "阻塞");

    @EnumValue
    @JsonValue
    private final String value;

    private final String desc;

    JobStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static JobStatus getByValue(String value) {
        for (JobStatus enumJobStatus : JobStatus.values()) {
            if (enumJobStatus.getValue().equals(value)) {
                return enumJobStatus;
            }
        }
        return null;
    }

    public static JobStatus getByDesc(String desc) {
        for (JobStatus enumJobStatus : JobStatus.values()) {
            if (enumJobStatus.getDesc().equals(desc)) {
                return enumJobStatus;
            }
        }
        return null;
    }
}
