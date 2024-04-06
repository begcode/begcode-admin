package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.AnnoBusinessType;
import com.begcode.monolith.domain.enumeration.AnnoCategory;
import com.begcode.monolith.domain.enumeration.AnnoOpenType;
import com.begcode.monolith.domain.enumeration.AnnoSendStatus;
import com.begcode.monolith.domain.enumeration.PriorityLevel;
import com.begcode.monolith.domain.enumeration.ReceiverType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.Announcement} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.AnnouncementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /announcements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnouncementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PriorityLevel
     */
    public static class PriorityLevelFilter extends Filter<PriorityLevel> {

        public PriorityLevelFilter() {}

        public PriorityLevelFilter(PriorityLevelFilter filter) {
            super(filter);
        }

        @Override
        public PriorityLevelFilter copy() {
            return new PriorityLevelFilter(this);
        }
    }

    /**
     * Class for filtering AnnoCategory
     */
    public static class AnnoCategoryFilter extends Filter<AnnoCategory> {

        public AnnoCategoryFilter() {}

        public AnnoCategoryFilter(AnnoCategoryFilter filter) {
            super(filter);
        }

        @Override
        public AnnoCategoryFilter copy() {
            return new AnnoCategoryFilter(this);
        }
    }

    /**
     * Class for filtering ReceiverType
     */
    public static class ReceiverTypeFilter extends Filter<ReceiverType> {

        public ReceiverTypeFilter() {}

        public ReceiverTypeFilter(ReceiverTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReceiverTypeFilter copy() {
            return new ReceiverTypeFilter(this);
        }
    }

    /**
     * Class for filtering AnnoSendStatus
     */
    public static class AnnoSendStatusFilter extends Filter<AnnoSendStatus> {

        public AnnoSendStatusFilter() {}

        public AnnoSendStatusFilter(AnnoSendStatusFilter filter) {
            super(filter);
        }

        @Override
        public AnnoSendStatusFilter copy() {
            return new AnnoSendStatusFilter(this);
        }
    }

    /**
     * Class for filtering AnnoBusinessType
     */
    public static class AnnoBusinessTypeFilter extends Filter<AnnoBusinessType> {

        public AnnoBusinessTypeFilter() {}

        public AnnoBusinessTypeFilter(AnnoBusinessTypeFilter filter) {
            super(filter);
        }

        @Override
        public AnnoBusinessTypeFilter copy() {
            return new AnnoBusinessTypeFilter(this);
        }
    }

    /**
     * Class for filtering AnnoOpenType
     */
    public static class AnnoOpenTypeFilter extends Filter<AnnoOpenType> {

        public AnnoOpenTypeFilter() {}

        public AnnoOpenTypeFilter(AnnoOpenTypeFilter filter) {
            super(filter);
        }

        @Override
        public AnnoOpenTypeFilter copy() {
            return new AnnoOpenTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.title")
    private StringFilter title;

    @BindQuery(column = "self.start_time")
    private ZonedDateTimeFilter startTime;

    @BindQuery(column = "self.end_time")
    private ZonedDateTimeFilter endTime;

    @BindQuery(column = "self.sender_id")
    private LongFilter senderId;

    @BindQuery(column = "self.priority")
    private PriorityLevelFilter priority;

    @BindQuery(column = "self.category")
    private AnnoCategoryFilter category;

    @BindQuery(column = "self.receiver_type")
    private ReceiverTypeFilter receiverType;

    @BindQuery(column = "self.send_status")
    private AnnoSendStatusFilter sendStatus;

    @BindQuery(column = "self.send_time")
    private ZonedDateTimeFilter sendTime;

    @BindQuery(column = "self.cancel_time")
    private ZonedDateTimeFilter cancelTime;

    @BindQuery(column = "self.business_type")
    private AnnoBusinessTypeFilter businessType;

    @BindQuery(column = "self.business_id")
    private LongFilter businessId;

    @BindQuery(column = "self.open_type")
    private AnnoOpenTypeFilter openType;

    @BindQuery(column = "self.open_page")
    private StringFilter openPage;

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
    private AnnouncementCriteria and;

    @BindQuery(ignore = true)
    private AnnouncementCriteria or;

    private Boolean distinct;

    public AnnouncementCriteria() {}

    public AnnouncementCriteria(AnnouncementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.senderId = other.optionalSenderId().map(LongFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(PriorityLevelFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(AnnoCategoryFilter::copy).orElse(null);
        this.receiverType = other.optionalReceiverType().map(ReceiverTypeFilter::copy).orElse(null);
        this.sendStatus = other.optionalSendStatus().map(AnnoSendStatusFilter::copy).orElse(null);
        this.sendTime = other.optionalSendTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.cancelTime = other.optionalCancelTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.businessType = other.optionalBusinessType().map(AnnoBusinessTypeFilter::copy).orElse(null);
        this.businessId = other.optionalBusinessId().map(LongFilter::copy).orElse(null);
        this.openType = other.optionalOpenType().map(AnnoOpenTypeFilter::copy).orElse(null);
        this.openPage = other.optionalOpenPage().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AnnouncementCriteria copy() {
        return new AnnouncementCriteria(this);
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

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public Optional<ZonedDateTimeFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            setStartTime(new ZonedDateTimeFilter());
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getEndTime() {
        return endTime;
    }

    public Optional<ZonedDateTimeFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public ZonedDateTimeFilter endTime() {
        if (endTime == null) {
            setEndTime(new ZonedDateTimeFilter());
        }
        return endTime;
    }

    public void setEndTime(ZonedDateTimeFilter endTime) {
        this.endTime = endTime;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public Optional<LongFilter> optionalSenderId() {
        return Optional.ofNullable(senderId);
    }

    public LongFilter senderId() {
        if (senderId == null) {
            setSenderId(new LongFilter());
        }
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public PriorityLevelFilter getPriority() {
        return priority;
    }

    public Optional<PriorityLevelFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public PriorityLevelFilter priority() {
        if (priority == null) {
            setPriority(new PriorityLevelFilter());
        }
        return priority;
    }

    public void setPriority(PriorityLevelFilter priority) {
        this.priority = priority;
    }

    public AnnoCategoryFilter getCategory() {
        return category;
    }

    public Optional<AnnoCategoryFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public AnnoCategoryFilter category() {
        if (category == null) {
            setCategory(new AnnoCategoryFilter());
        }
        return category;
    }

    public void setCategory(AnnoCategoryFilter category) {
        this.category = category;
    }

    public ReceiverTypeFilter getReceiverType() {
        return receiverType;
    }

    public Optional<ReceiverTypeFilter> optionalReceiverType() {
        return Optional.ofNullable(receiverType);
    }

    public ReceiverTypeFilter receiverType() {
        if (receiverType == null) {
            setReceiverType(new ReceiverTypeFilter());
        }
        return receiverType;
    }

    public void setReceiverType(ReceiverTypeFilter receiverType) {
        this.receiverType = receiverType;
    }

    public AnnoSendStatusFilter getSendStatus() {
        return sendStatus;
    }

    public Optional<AnnoSendStatusFilter> optionalSendStatus() {
        return Optional.ofNullable(sendStatus);
    }

    public AnnoSendStatusFilter sendStatus() {
        if (sendStatus == null) {
            setSendStatus(new AnnoSendStatusFilter());
        }
        return sendStatus;
    }

    public void setSendStatus(AnnoSendStatusFilter sendStatus) {
        this.sendStatus = sendStatus;
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

    public ZonedDateTimeFilter getCancelTime() {
        return cancelTime;
    }

    public Optional<ZonedDateTimeFilter> optionalCancelTime() {
        return Optional.ofNullable(cancelTime);
    }

    public ZonedDateTimeFilter cancelTime() {
        if (cancelTime == null) {
            setCancelTime(new ZonedDateTimeFilter());
        }
        return cancelTime;
    }

    public void setCancelTime(ZonedDateTimeFilter cancelTime) {
        this.cancelTime = cancelTime;
    }

    public AnnoBusinessTypeFilter getBusinessType() {
        return businessType;
    }

    public Optional<AnnoBusinessTypeFilter> optionalBusinessType() {
        return Optional.ofNullable(businessType);
    }

    public AnnoBusinessTypeFilter businessType() {
        if (businessType == null) {
            setBusinessType(new AnnoBusinessTypeFilter());
        }
        return businessType;
    }

    public void setBusinessType(AnnoBusinessTypeFilter businessType) {
        this.businessType = businessType;
    }

    public LongFilter getBusinessId() {
        return businessId;
    }

    public Optional<LongFilter> optionalBusinessId() {
        return Optional.ofNullable(businessId);
    }

    public LongFilter businessId() {
        if (businessId == null) {
            setBusinessId(new LongFilter());
        }
        return businessId;
    }

    public void setBusinessId(LongFilter businessId) {
        this.businessId = businessId;
    }

    public AnnoOpenTypeFilter getOpenType() {
        return openType;
    }

    public Optional<AnnoOpenTypeFilter> optionalOpenType() {
        return Optional.ofNullable(openType);
    }

    public AnnoOpenTypeFilter openType() {
        if (openType == null) {
            setOpenType(new AnnoOpenTypeFilter());
        }
        return openType;
    }

    public void setOpenType(AnnoOpenTypeFilter openType) {
        this.openType = openType;
    }

    public StringFilter getOpenPage() {
        return openPage;
    }

    public Optional<StringFilter> optionalOpenPage() {
        return Optional.ofNullable(openPage);
    }

    public StringFilter openPage() {
        if (openPage == null) {
            setOpenPage(new StringFilter());
        }
        return openPage;
    }

    public void setOpenPage(StringFilter openPage) {
        this.openPage = openPage;
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

    public void setAnd(AnnouncementCriteria and) {
        this.and = and;
    }

    public AnnouncementCriteria getAnd() {
        return and;
    }

    public AnnouncementCriteria and() {
        if (and == null) {
            and = new AnnouncementCriteria();
        }
        return and;
    }

    public void setOr(AnnouncementCriteria or) {
        this.or = or;
    }

    public AnnouncementCriteria getOr() {
        return or;
    }

    public AnnouncementCriteria or() {
        if (or == null) {
            or = new AnnouncementCriteria();
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
        final AnnouncementCriteria that = (AnnouncementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(category, that.category) &&
            Objects.equals(receiverType, that.receiverType) &&
            Objects.equals(sendStatus, that.sendStatus) &&
            Objects.equals(sendTime, that.sendTime) &&
            Objects.equals(cancelTime, that.cancelTime) &&
            Objects.equals(businessType, that.businessType) &&
            Objects.equals(businessId, that.businessId) &&
            Objects.equals(openType, that.openType) &&
            Objects.equals(openPage, that.openPage) &&
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
            startTime,
            endTime,
            senderId,
            priority,
            category,
            receiverType,
            sendStatus,
            sendTime,
            cancelTime,
            businessType,
            businessId,
            openType,
            openPage,
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
        return "AnnouncementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalSenderId().map(f -> "senderId=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalReceiverType().map(f -> "receiverType=" + f + ", ").orElse("") +
            optionalSendStatus().map(f -> "sendStatus=" + f + ", ").orElse("") +
            optionalSendTime().map(f -> "sendTime=" + f + ", ").orElse("") +
            optionalCancelTime().map(f -> "cancelTime=" + f + ", ").orElse("") +
            optionalBusinessType().map(f -> "businessType=" + f + ", ").orElse("") +
            optionalBusinessId().map(f -> "businessId=" + f + ", ").orElse("") +
            optionalOpenType().map(f -> "openType=" + f + ", ").orElse("") +
            optionalOpenPage().map(f -> "openPage=" + f + ", ").orElse("") +
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
