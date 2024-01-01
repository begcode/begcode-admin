package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private SmsMessageCriteria and;

    @BindQuery(ignore = true)
    private SmsMessageCriteria or;

    /**
     * Class for filtering MessageSendType
     */
    public static class MessageSendTypeFilter extends Filter<MessageSendType> {

        public MessageSendTypeFilter() {}

        public MessageSendTypeFilter(String value) {
            MessageSendType enumValue = MessageSendType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = MessageSendType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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

        public SendStatusFilter(String value) {
            SendStatus enumValue = SendStatus.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = SendStatus.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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
    private ZonedDateTimeFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private ZonedDateTimeFilter lastModifiedDate;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SmsMessageCriteria() {}

    public SmsMessageCriteria(SmsMessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.sendType = other.sendType == null ? null : other.sendType.copy();
        this.receiver = other.receiver == null ? null : other.receiver.copy();
        this.params = other.params == null ? null : other.params.copy();
        this.sendTime = other.sendTime == null ? null : other.sendTime.copy();
        this.sendStatus = other.sendStatus == null ? null : other.sendStatus.copy();
        this.retryNum = other.retryNum == null ? null : other.retryNum.copy();
        this.failResult = other.failResult == null ? null : other.failResult.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SmsMessageCriteria copy() {
        return new SmsMessageCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public MessageSendTypeFilter getSendType() {
        return sendType;
    }

    public MessageSendTypeFilter sendType() {
        if (sendType == null) {
            sendType = new MessageSendTypeFilter();
        }
        return sendType;
    }

    public void setSendType(MessageSendTypeFilter sendType) {
        this.sendType = sendType;
    }

    public StringFilter getReceiver() {
        return receiver;
    }

    public StringFilter receiver() {
        if (receiver == null) {
            receiver = new StringFilter();
        }
        return receiver;
    }

    public void setReceiver(StringFilter receiver) {
        this.receiver = receiver;
    }

    public StringFilter getParams() {
        return params;
    }

    public StringFilter params() {
        if (params == null) {
            params = new StringFilter();
        }
        return params;
    }

    public void setParams(StringFilter params) {
        this.params = params;
    }

    public ZonedDateTimeFilter getSendTime() {
        return sendTime;
    }

    public ZonedDateTimeFilter sendTime() {
        if (sendTime == null) {
            sendTime = new ZonedDateTimeFilter();
        }
        return sendTime;
    }

    public void setSendTime(ZonedDateTimeFilter sendTime) {
        this.sendTime = sendTime;
    }

    public SendStatusFilter getSendStatus() {
        return sendStatus;
    }

    public SendStatusFilter sendStatus() {
        if (sendStatus == null) {
            sendStatus = new SendStatusFilter();
        }
        return sendStatus;
    }

    public void setSendStatus(SendStatusFilter sendStatus) {
        this.sendStatus = sendStatus;
    }

    public IntegerFilter getRetryNum() {
        return retryNum;
    }

    public IntegerFilter retryNum() {
        if (retryNum == null) {
            retryNum = new IntegerFilter();
        }
        return retryNum;
    }

    public void setRetryNum(IntegerFilter retryNum) {
        this.retryNum = retryNum;
    }

    public StringFilter getFailResult() {
        return failResult;
    }

    public StringFilter failResult() {
        if (failResult == null) {
            failResult = new StringFilter();
        }
        return failResult;
    }

    public void setFailResult(StringFilter failResult) {
        this.failResult = failResult;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
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
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (sendType != null ? "sendType=" + sendType + ", " : "") +
            (receiver != null ? "receiver=" + receiver + ", " : "") +
            (params != null ? "params=" + params + ", " : "") +
            (sendTime != null ? "sendTime=" + sendTime + ", " : "") +
            (sendStatus != null ? "sendStatus=" + sendStatus + ", " : "") +
            (retryNum != null ? "retryNum=" + retryNum + ", " : "") +
            (failResult != null ? "failResult=" + failResult + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
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
