package com.begcode.monolith.log.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import java.io.Serializable;
import lombok.*;

/**
 * 系统日志
 */
@TableName(value = "sys_log")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysLog extends AbstractAuditingEntity<Long, SysLog> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求路径
     */
    @TableField(value = "request_url")
    private String requestUrl;

    /**
     * 日志类型
     */
    @TableField(value = "log_type")
    private LogType logType;

    /**
     * 日志内容
     */
    @TableField(value = "log_content")
    private String logContent;

    /**
     * 操作类型
     */
    @TableField(value = "operate_type")
    private OperateType operateType;

    /**
     * 操作用户账号
     */
    @TableField(value = "userid")
    private String userid;

    /**
     * 操作用户名称
     */
    @TableField(value = "username")
    private String username;

    /**
     * IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 请求java方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求参数
     */
    @TableField(value = "request_param")
    private String requestParam;

    /**
     * 请求类型
     */
    @TableField(value = "request_type")
    private String requestType;

    /**
     * 耗时
     */
    @TableField(value = "cost_time")
    private Long costTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SysLog id(Long id) {
        this.id = id;
        return this;
    }

    public SysLog requestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public SysLog logType(LogType logType) {
        this.logType = logType;
        return this;
    }

    public SysLog logContent(String logContent) {
        this.logContent = logContent;
        return this;
    }

    public SysLog operateType(OperateType operateType) {
        this.operateType = operateType;
        return this;
    }

    public SysLog userid(String userid) {
        this.userid = userid;
        return this;
    }

    public SysLog username(String username) {
        this.username = username;
        return this;
    }

    public SysLog ip(String ip) {
        this.ip = ip;
        return this;
    }

    public SysLog method(String method) {
        this.method = method;
        return this;
    }

    public SysLog requestParam(String requestParam) {
        this.requestParam = requestParam;
        return this;
    }

    public SysLog requestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public SysLog costTime(Long costTime) {
        this.costTime = costTime;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysLog)) {
            return false;
        }
        return getId() != null && getId().equals(((SysLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysLog{" +
            "id=" + getId() +
            ", requestUrl='" + getRequestUrl() + "'" +
            ", logType='" + getLogType() + "'" +
            ", logContent='" + getLogContent() + "'" +
            ", operateType='" + getOperateType() + "'" +
            ", userid='" + getUserid() + "'" +
            ", username='" + getUsername() + "'" +
            ", ip='" + getIp() + "'" +
            ", method='" + getMethod() + "'" +
            ", requestParam='" + getRequestParam() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", costTime=" + getCostTime() +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
