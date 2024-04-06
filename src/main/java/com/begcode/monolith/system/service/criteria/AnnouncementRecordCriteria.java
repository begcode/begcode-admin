package com.begcode.monolith.system.service.criteria;

import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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
    private AnnouncementRecordCriteria and;

    @BindQuery(ignore = true)
    private AnnouncementRecordCriteria or;

    private Boolean distinct;

    public AnnouncementRecordCriteria() {}

    public AnnouncementRecordCriteria(AnnouncementRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.anntId = other.optionalAnntId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.hasRead = other.optionalHasRead().map(BooleanFilter::copy).orElse(null);
        this.readTime = other.optionalReadTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AnnouncementRecordCriteria copy() {
        return new AnnouncementRecordCriteria(this);
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

    public LongFilter getAnntId() {
        return anntId;
    }

    public Optional<LongFilter> optionalAnntId() {
        return Optional.ofNullable(anntId);
    }

    public LongFilter anntId() {
        if (anntId == null) {
            setAnntId(new LongFilter());
        }
        return anntId;
    }

    public void setAnntId(LongFilter anntId) {
        this.anntId = anntId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getHasRead() {
        return hasRead;
    }

    public Optional<BooleanFilter> optionalHasRead() {
        return Optional.ofNullable(hasRead);
    }

    public BooleanFilter hasRead() {
        if (hasRead == null) {
            setHasRead(new BooleanFilter());
        }
        return hasRead;
    }

    public void setHasRead(BooleanFilter hasRead) {
        this.hasRead = hasRead;
    }

    public ZonedDateTimeFilter getReadTime() {
        return readTime;
    }

    public Optional<ZonedDateTimeFilter> optionalReadTime() {
        return Optional.ofNullable(readTime);
    }

    public ZonedDateTimeFilter readTime() {
        if (readTime == null) {
            setReadTime(new ZonedDateTimeFilter());
        }
        return readTime;
    }

    public void setReadTime(ZonedDateTimeFilter readTime) {
        this.readTime = readTime;
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAnntId().map(f -> "anntId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalHasRead().map(f -> "hasRead=" + f + ", ").orElse("") +
            optionalReadTime().map(f -> "readTime=" + f + ", ").orElse("") +
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
