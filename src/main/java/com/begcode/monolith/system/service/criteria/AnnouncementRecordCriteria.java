package com.begcode.monolith.system.service.criteria;

import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.AnnouncementRecord} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.AnnouncementRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /announcement-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnouncementRecordCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private AnnouncementRecordCriteria and;

    @BindQuery(ignore = true)
    private AnnouncementRecordCriteria or;

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.annt_id")
    private LongFilter anntId;

    @BindQuery(column = "self.user_id")
    private LongFilter userId;

    @BindQuery(column = "self.has_read")
    private BooleanFilter hasRead;

    @BindQuery(column = "self.read_time")
    private ZonedDateTimeFilter readTime;

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

    public AnnouncementRecordCriteria() {}

    public AnnouncementRecordCriteria(AnnouncementRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.anntId = other.anntId == null ? null : other.anntId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.hasRead = other.hasRead == null ? null : other.hasRead.copy();
        this.readTime = other.readTime == null ? null : other.readTime.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AnnouncementRecordCriteria copy() {
        return new AnnouncementRecordCriteria(this);
    }

    public void setAnd(AnnouncementRecordCriteria and) {
        this.and = and;
    }

    public AnnouncementRecordCriteria getAnd() {
        return and;
    }

    public AnnouncementRecordCriteria and() {
        if (and == null) {
            and = new AnnouncementRecordCriteria();
        }
        return and;
    }

    public void setOr(AnnouncementRecordCriteria or) {
        this.or = or;
    }

    public AnnouncementRecordCriteria getOr() {
        return or;
    }

    public AnnouncementRecordCriteria or() {
        if (or == null) {
            or = new AnnouncementRecordCriteria();
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

    public LongFilter getAnntId() {
        return anntId;
    }

    public LongFilter anntId() {
        if (anntId == null) {
            anntId = new LongFilter();
        }
        return anntId;
    }

    public void setAnntId(LongFilter anntId) {
        this.anntId = anntId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getHasRead() {
        return hasRead;
    }

    public BooleanFilter hasRead() {
        if (hasRead == null) {
            hasRead = new BooleanFilter();
        }
        return hasRead;
    }

    public void setHasRead(BooleanFilter hasRead) {
        this.hasRead = hasRead;
    }

    public ZonedDateTimeFilter getReadTime() {
        return readTime;
    }

    public ZonedDateTimeFilter readTime() {
        if (readTime == null) {
            readTime = new ZonedDateTimeFilter();
        }
        return readTime;
    }

    public void setReadTime(ZonedDateTimeFilter readTime) {
        this.readTime = readTime;
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
        final AnnouncementRecordCriteria that = (AnnouncementRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anntId, that.anntId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(hasRead, that.hasRead) &&
            Objects.equals(readTime, that.readTime) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anntId, userId, hasRead, readTime, createdBy, createdDate, lastModifiedBy, lastModifiedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (anntId != null ? "anntId=" + anntId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (hasRead != null ? "hasRead=" + hasRead + ", " : "") +
            (readTime != null ? "readTime=" + readTime + ", " : "") +
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
