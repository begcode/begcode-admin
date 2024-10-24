package com.begcode.monolith.log.service.criteria;

import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * Class for filtering LogType
     */
    public static class LogTypeFilter extends Filter<LogType> {

        public LogTypeFilter() {}

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

    @BindQuery(column = "self.request_url")
    private StringFilter requestUrl;

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

    @BindQuery(column = "self.request_type")
    private StringFilter requestType;

    @BindQuery(column = "self.cost_time")
    private LongFilter costTime;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SysLogCriteria and;

    @BindQuery(ignore = true)
    private SysLogCriteria or;

    private Boolean distinct;

    public SysLogCriteria() {}

    public SysLogCriteria(SysLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.requestUrl = other.optionalRequestUrl().map(StringFilter::copy).orElse(null);
        this.logType = other.optionalLogType().map(LogTypeFilter::copy).orElse(null);
        this.logContent = other.optionalLogContent().map(StringFilter::copy).orElse(null);
        this.operateType = other.optionalOperateType().map(OperateTypeFilter::copy).orElse(null);
        this.userid = other.optionalUserid().map(StringFilter::copy).orElse(null);
        this.username = other.optionalUsername().map(StringFilter::copy).orElse(null);
        this.ip = other.optionalIp().map(StringFilter::copy).orElse(null);
        this.method = other.optionalMethod().map(StringFilter::copy).orElse(null);
        this.requestType = other.optionalRequestType().map(StringFilter::copy).orElse(null);
        this.costTime = other.optionalCostTime().map(LongFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SysLogCriteria copy() {
        return new SysLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRequestUrl() {
        return requestUrl;
    }

    public Optional<StringFilter> optionalRequestUrl() {
        return Optional.ofNullable(requestUrl);
    }

    public StringFilter requestUrl() {
        if (requestUrl == null) {
            setRequestUrl(new StringFilter());
        }
        return requestUrl;
    }

    public void setRequestUrl(StringFilter requestUrl) {
        this.requestUrl = requestUrl;
    }

    public LogTypeFilter getLogType() {
        return logType;
    }

    public Optional<LogTypeFilter> optionalLogType() {
        return Optional.ofNullable(logType);
    }

    public LogTypeFilter logType() {
        if (logType == null) {
            setLogType(new LogTypeFilter());
        }
        return logType;
    }

    public void setLogType(LogTypeFilter logType) {
        this.logType = logType;
    }

    public StringFilter getLogContent() {
        return logContent;
    }

    public Optional<StringFilter> optionalLogContent() {
        return Optional.ofNullable(logContent);
    }

    public StringFilter logContent() {
        if (logContent == null) {
            setLogContent(new StringFilter());
        }
        return logContent;
    }

    public void setLogContent(StringFilter logContent) {
        this.logContent = logContent;
    }

    public OperateTypeFilter getOperateType() {
        return operateType;
    }

    public Optional<OperateTypeFilter> optionalOperateType() {
        return Optional.ofNullable(operateType);
    }

    public OperateTypeFilter operateType() {
        if (operateType == null) {
            setOperateType(new OperateTypeFilter());
        }
        return operateType;
    }

    public void setOperateType(OperateTypeFilter operateType) {
        this.operateType = operateType;
    }

    public StringFilter getUserid() {
        return userid;
    }

    public Optional<StringFilter> optionalUserid() {
        return Optional.ofNullable(userid);
    }

    public StringFilter userid() {
        if (userid == null) {
            setUserid(new StringFilter());
        }
        return userid;
    }

    public void setUserid(StringFilter userid) {
        this.userid = userid;
    }

    public StringFilter getUsername() {
        return username;
    }

    public Optional<StringFilter> optionalUsername() {
        return Optional.ofNullable(username);
    }

    public StringFilter username() {
        if (username == null) {
            setUsername(new StringFilter());
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getIp() {
        return ip;
    }

    public Optional<StringFilter> optionalIp() {
        return Optional.ofNullable(ip);
    }

    public StringFilter ip() {
        if (ip == null) {
            setIp(new StringFilter());
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getMethod() {
        return method;
    }

    public Optional<StringFilter> optionalMethod() {
        return Optional.ofNullable(method);
    }

    public StringFilter method() {
        if (method == null) {
            setMethod(new StringFilter());
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getRequestType() {
        return requestType;
    }

    public Optional<StringFilter> optionalRequestType() {
        return Optional.ofNullable(requestType);
    }

    public StringFilter requestType() {
        if (requestType == null) {
            setRequestType(new StringFilter());
        }
        return requestType;
    }

    public void setRequestType(StringFilter requestType) {
        this.requestType = requestType;
    }

    public LongFilter getCostTime() {
        return costTime;
    }

    public Optional<LongFilter> optionalCostTime() {
        return Optional.ofNullable(costTime);
    }

    public LongFilter costTime() {
        if (costTime == null) {
            setCostTime(new LongFilter());
        }
        return costTime;
    }

    public void setCostTime(LongFilter costTime) {
        this.costTime = costTime;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<LongFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new LongFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<LongFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new LongFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
            Objects.equals(requestUrl, that.requestUrl) &&
            Objects.equals(logType, that.logType) &&
            Objects.equals(logContent, that.logContent) &&
            Objects.equals(operateType, that.operateType) &&
            Objects.equals(userid, that.userid) &&
            Objects.equals(username, that.username) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(method, that.method) &&
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
            requestUrl,
            logType,
            logContent,
            operateType,
            userid,
            username,
            ip,
            method,
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRequestUrl().map(f -> "requestUrl=" + f + ", ").orElse("") +
            optionalLogType().map(f -> "logType=" + f + ", ").orElse("") +
            optionalLogContent().map(f -> "logContent=" + f + ", ").orElse("") +
            optionalOperateType().map(f -> "operateType=" + f + ", ").orElse("") +
            optionalUserid().map(f -> "userid=" + f + ", ").orElse("") +
            optionalUsername().map(f -> "username=" + f + ", ").orElse("") +
            optionalIp().map(f -> "ip=" + f + ", ").orElse("") +
            optionalMethod().map(f -> "method=" + f + ", ").orElse("") +
            optionalRequestType().map(f -> "requestType=" + f + ", ").orElse("") +
            optionalCostTime().map(f -> "costTime=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
