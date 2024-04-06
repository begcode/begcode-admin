package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.SmsMessage} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.SmsMessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sms-messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsMessageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MessageSendType
     */
    public static class MessageSendTypeFilter extends Filter<MessageSendType> {

        public MessageSendTypeFilter() {}

        public MessageSendTypeFilter(MessageSendTypeFilter filter) {
            super(filter);
        }

        @Override
        public MessageSendTypeFilter copy() {
            return new MessageSendTypeFilter(this);
        }
    }

    /**
     * Class for filtering SendStatus
     */
    public static class SendStatusFilter extends Filter<SendStatus> {

        public SendStatusFilter() {}

        public SendStatusFilter(SendStatusFilter filter) {
            super(filter);
        }

        @Override
        public SendStatusFilter copy() {
            return new SendStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.title")
    private StringFilter title;

    @BindQuery(column = "self.send_type")
    private MessageSendTypeFilter sendType;

    @BindQuery(column = "self.receiver")
    private StringFilter receiver;

    @BindQuery(column = "self.params")
    private StringFilter params;

    @BindQuery(column = "self.send_time")
    private ZonedDateTimeFilter sendTime;

    @BindQuery(column = "self.send_status")
    private SendStatusFilter sendStatus;

    @BindQuery(column = "self.retry_num")
    private IntegerFilter retryNum;

    @BindQuery(column = "self.fail_result")
    private StringFilter failResult;

    @BindQuery(column = "self.remark")
    private StringFilter remark;

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
    private SmsMessageCriteria and;

    @BindQuery(ignore = true)
    private SmsMessageCriteria or;

    private Boolean distinct;

    public SmsMessageCriteria() {}

    public SmsMessageCriteria(SmsMessageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.sendType = other.optionalSendType().map(MessageSendTypeFilter::copy).orElse(null);
        this.receiver = other.optionalReceiver().map(StringFilter::copy).orElse(null);
        this.params = other.optionalParams().map(StringFilter::copy).orElse(null);
        this.sendTime = other.optionalSendTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.sendStatus = other.optionalSendStatus().map(SendStatusFilter::copy).orElse(null);
        this.retryNum = other.optionalRetryNum().map(IntegerFilter::copy).orElse(null);
        this.failResult = other.optionalFailResult().map(StringFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SmsMessageCriteria copy() {
        return new SmsMessageCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public MessageSendTypeFilter getSendType() {
        return sendType;
    }

    public Optional<MessageSendTypeFilter> optionalSendType() {
        return Optional.ofNullable(sendType);
    }

    public MessageSendTypeFilter sendType() {
        if (sendType == null) {
            setSendType(new MessageSendTypeFilter());
        }
        return sendType;
    }

    public void setSendType(MessageSendTypeFilter sendType) {
        this.sendType = sendType;
    }

    public StringFilter getReceiver() {
        return receiver;
    }

    public Optional<StringFilter> optionalReceiver() {
        return Optional.ofNullable(receiver);
    }

    public StringFilter receiver() {
        if (receiver == null) {
            setReceiver(new StringFilter());
        }
        return receiver;
    }

    public void setReceiver(StringFilter receiver) {
        this.receiver = receiver;
    }

    public StringFilter getParams() {
        return params;
    }

    public Optional<StringFilter> optionalParams() {
        return Optional.ofNullable(params);
    }

    public StringFilter params() {
        if (params == null) {
            setParams(new StringFilter());
        }
        return params;
    }

    public void setParams(StringFilter params) {
        this.params = params;
    }

    public ZonedDateTimeFilter getSendTime() {
        return sendTime;
    }

    public Optional<ZonedDateTimeFilter> optionalSendTime() {
        return Optional.ofNullable(sendTime);
    }

    public ZonedDateTimeFilter sendTime() {
        if (sendTime == null) {
            setSendTime(new ZonedDateTimeFilter());
        }
        return sendTime;
    }

    public void setSendTime(ZonedDateTimeFilter sendTime) {
        this.sendTime = sendTime;
    }

    public SendStatusFilter getSendStatus() {
        return sendStatus;
    }

    public Optional<SendStatusFilter> optionalSendStatus() {
        return Optional.ofNullable(sendStatus);
    }

    public SendStatusFilter sendStatus() {
        if (sendStatus == null) {
            setSendStatus(new SendStatusFilter());
        }
        return sendStatus;
    }

    public void setSendStatus(SendStatusFilter sendStatus) {
        this.sendStatus = sendStatus;
    }

    public IntegerFilter getRetryNum() {
        return retryNum;
    }

    public Optional<IntegerFilter> optionalRetryNum() {
        return Optional.ofNullable(retryNum);
    }

    public IntegerFilter retryNum() {
        if (retryNum == null) {
            setRetryNum(new IntegerFilter());
        }
        return retryNum;
    }

    public void setRetryNum(IntegerFilter retryNum) {
        this.retryNum = retryNum;
    }

    public StringFilter getFailResult() {
        return failResult;
    }

    public Optional<StringFilter> optionalFailResult() {
        return Optional.ofNullable(failResult);
    }

    public StringFilter failResult() {
        if (failResult == null) {
            setFailResult(new StringFilter());
        }
        return failResult;
    }

    public void setFailResult(StringFilter failResult) {
        this.failResult = failResult;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public Optional<StringFilter> optionalRemark() {
        return Optional.ofNullable(remark);
    }

    public StringFilter remark() {
        if (remark == null) {
            setRemark(new StringFilter());
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
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

    public void setAnd(SmsMessageCriteria and) {
        this.and = and;
    }

    public SmsMessageCriteria getAnd() {
        return and;
    }

    public SmsMessageCriteria and() {
        if (and == null) {
            and = new SmsMessageCriteria();
        }
        return and;
    }

    public void setOr(SmsMessageCriteria or) {
        this.or = or;
    }

    public SmsMessageCriteria getOr() {
        return or;
    }

    public SmsMessageCriteria or() {
        if (or == null) {
            or = new SmsMessageCriteria();
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
        final SmsMessageCriteria that = (SmsMessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(sendType, that.sendType) &&
            Objects.equals(receiver, that.receiver) &&
            Objects.equals(params, that.params) &&
            Objects.equals(sendTime, that.sendTime) &&
            Objects.equals(sendStatus, that.sendStatus) &&
            Objects.equals(retryNum, that.retryNum) &&
            Objects.equals(failResult, that.failResult) &&
            Objects.equals(remark, that.remark) &&
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
            title,
            sendType,
            receiver,
            params,
            sendTime,
            sendStatus,
            retryNum,
            failResult,
            remark,
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
        return "SmsMessageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalSendType().map(f -> "sendType=" + f + ", ").orElse("") +
            optionalReceiver().map(f -> "receiver=" + f + ", ").orElse("") +
            optionalParams().map(f -> "params=" + f + ", ").orElse("") +
            optionalSendTime().map(f -> "sendTime=" + f + ", ").orElse("") +
            optionalSendStatus().map(f -> "sendStatus=" + f + ", ").orElse("") +
            optionalRetryNum().map(f -> "retryNum=" + f + ", ").orElse("") +
            optionalFailResult().map(f -> "failResult=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
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
