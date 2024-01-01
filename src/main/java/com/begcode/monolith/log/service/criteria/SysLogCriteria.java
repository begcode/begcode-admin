package com.begcode.monolith.log.service.criteria;

import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.log.domain.SysLog} entity. This class is used
 * in {@link com.begcode.monolith.log.web.rest.SysLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sys-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SysLogCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private SysLogCriteria and;

    @BindQuery(ignore = true)
    private SysLogCriteria or;

    /**
     * Class for filtering LogType
     */
    public static class LogTypeFilter extends Filter<LogType> {

        public LogTypeFilter() {}

        public LogTypeFilter(String value) {
            LogType enumValue = LogType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = LogType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public LogTypeFilter(LogTypeFilter filter) {
            super(filter);
        }

        @Override
        public LogTypeFilter copy() {
            return new LogTypeFilter(this);
        }
    }

    /**
     * Class for filtering OperateType
     */
    public static class OperateTypeFilter extends Filter<OperateType> {

        public OperateTypeFilter() {}

        public OperateTypeFilter(String value) {
            OperateType enumValue = OperateType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = OperateType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public OperateTypeFilter(OperateTypeFilter filter) {
            super(filter);
        }

        @Override
        public OperateTypeFilter copy() {
            return new OperateTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.log_type")
    private LogTypeFilter logType;

    @BindQuery(column = "self.log_content")
    private StringFilter logContent;

    @BindQuery(column = "self.operate_type")
    private OperateTypeFilter operateType;

    @BindQuery(column = "self.userid")
    private StringFilter userid;

    @BindQuery(column = "self.username")
    private StringFilter username;

    @BindQuery(column = "self.ip")
    private StringFilter ip;

    @BindQuery(column = "self.method")
    private StringFilter method;

    @BindQuery(column = "self.request_url")
    private StringFilter requestUrl;

    @BindQuery(column = "self.request_type")
    private StringFilter requestType;

    @BindQuery(column = "self.cost_time")
    private LongFilter costTime;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private ZonedDateTimeFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private ZonedDateTimeFilter lastModifiedDate;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SysLogCriteria() {}

    public SysLogCriteria(SysLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.logType = other.logType == null ? null : other.logType.copy();
        this.logContent = other.logContent == null ? null : other.logContent.copy();
        this.operateType = other.operateType == null ? null : other.operateType.copy();
        this.userid = other.userid == null ? null : other.userid.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.ip = other.ip == null ? null : other.ip.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.requestUrl = other.requestUrl == null ? null : other.requestUrl.copy();
        this.requestType = other.requestType == null ? null : other.requestType.copy();
        this.costTime = other.costTime == null ? null : other.costTime.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SysLogCriteria copy() {
        return new SysLogCriteria(this);
    }

    public void setAnd(SysLogCriteria and) {
        this.and = and;
    }

    public SysLogCriteria getAnd() {
        return and;
    }

    public SysLogCriteria and() {
        if (and == null) {
            and = new SysLogCriteria();
        }
        return and;
    }

    public void setOr(SysLogCriteria or) {
        this.or = or;
    }

    public SysLogCriteria getOr() {
        return or;
    }

    public SysLogCriteria or() {
        if (or == null) {
            or = new SysLogCriteria();
        }
        return or;
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LogTypeFilter getLogType() {
        return logType;
    }

    public LogTypeFilter logType() {
        if (logType == null) {
            logType = new LogTypeFilter();
        }
        return logType;
    }

    public void setLogType(LogTypeFilter logType) {
        this.logType = logType;
    }

    public StringFilter getLogContent() {
        return logContent;
    }

    public StringFilter logContent() {
        if (logContent == null) {
            logContent = new StringFilter();
        }
        return logContent;
    }

    public void setLogContent(StringFilter logContent) {
        this.logContent = logContent;
    }

    public OperateTypeFilter getOperateType() {
        return operateType;
    }

    public OperateTypeFilter operateType() {
        if (operateType == null) {
            operateType = new OperateTypeFilter();
        }
        return operateType;
    }

    public void setOperateType(OperateTypeFilter operateType) {
        this.operateType = operateType;
    }

    public StringFilter getUserid() {
        return userid;
    }

    public StringFilter userid() {
        if (userid == null) {
            userid = new StringFilter();
        }
        return userid;
    }

    public void setUserid(StringFilter userid) {
        this.userid = userid;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getIp() {
        return ip;
    }

    public StringFilter ip() {
        if (ip == null) {
            ip = new StringFilter();
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getMethod() {
        return method;
    }

    public StringFilter method() {
        if (method == null) {
            method = new StringFilter();
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getRequestUrl() {
        return requestUrl;
    }

    public StringFilter requestUrl() {
        if (requestUrl == null) {
            requestUrl = new StringFilter();
        }
        return requestUrl;
    }

    public void setRequestUrl(StringFilter requestUrl) {
        this.requestUrl = requestUrl;
    }

    public StringFilter getRequestType() {
        return requestType;
    }

    public StringFilter requestType() {
        if (requestType == null) {
            requestType = new StringFilter();
        }
        return requestType;
    }

    public void setRequestType(StringFilter requestType) {
        this.requestType = requestType;
    }

    public LongFilter getCostTime() {
        return costTime;
    }

    public LongFilter costTime() {
        if (costTime == null) {
            costTime = new LongFilter();
        }
        return costTime;
    }

    public void setCostTime(LongFilter costTime) {
        this.costTime = costTime;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            createdBy = new LongFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new LongFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTimeFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ZonedDateTimeFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new ZonedDateTimeFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTimeFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SysLogCriteria that = (SysLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(logType, that.logType) &&
            Objects.equals(logContent, that.logContent) &&
            Objects.equals(operateType, that.operateType) &&
            Objects.equals(userid, that.userid) &&
            Objects.equals(username, that.username) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(method, that.method) &&
            Objects.equals(requestUrl, that.requestUrl) &&
            Objects.equals(requestType, that.requestType) &&
            Objects.equals(costTime, that.costTime) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            logType,
            logContent,
            operateType,
            userid,
            username,
            ip,
            method,
            requestUrl,
            requestType,
            costTime,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (logType != null ? "logType=" + logType + ", " : "") +
            (logContent != null ? "logContent=" + logContent + ", " : "") +
            (operateType != null ? "operateType=" + operateType + ", " : "") +
            (userid != null ? "userid=" + userid + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (ip != null ? "ip=" + ip + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (requestUrl != null ? "requestUrl=" + requestUrl + ", " : "") +
            (requestType != null ? "requestType=" + requestType + ", " : "") +
            (costTime != null ? "costTime=" + costTime + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
