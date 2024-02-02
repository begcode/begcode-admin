package com.begcode.monolith.taskjob.service.criteria;

import com.begcode.monolith.domain.enumeration.JobStatus;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.taskjob.domain.TaskJobConfig} entity. This class is used
 * in {@link com.begcode.monolith.taskjob.web.rest.TaskJobConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-job-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskJobConfigCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private TaskJobConfigCriteria and;

    @BindQuery(ignore = true)
    private TaskJobConfigCriteria or;

    /**
     * Class for filtering JobStatus
     */
    public static class JobStatusFilter extends Filter<JobStatus> {

        public JobStatusFilter() {}

        public JobStatusFilter(String value) {
            JobStatus enumValue = JobStatus.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = JobStatus.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public JobStatusFilter(JobStatusFilter filter) {
            super(filter);
        }

        @Override
        public JobStatusFilter copy() {
            return new JobStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.job_class_name")
    private StringFilter jobClassName;

    @BindQuery(column = "self.cron_expression")
    private StringFilter cronExpression;

    @BindQuery(column = "self.parameter")
    private StringFilter parameter;

    @BindQuery(column = "self.description")
    private StringFilter description;

    @BindQuery(column = "self.job_status")
    private JobStatusFilter jobStatus;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public TaskJobConfigCriteria() {}

    public TaskJobConfigCriteria(TaskJobConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.jobClassName = other.jobClassName == null ? null : other.jobClassName.copy();
        this.cronExpression = other.cronExpression == null ? null : other.cronExpression.copy();
        this.parameter = other.parameter == null ? null : other.parameter.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.jobStatus = other.jobStatus == null ? null : other.jobStatus.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaskJobConfigCriteria copy() {
        return new TaskJobConfigCriteria(this);
    }

    public void setAnd(TaskJobConfigCriteria and) {
        this.and = and;
    }

    public TaskJobConfigCriteria getAnd() {
        return and;
    }

    public TaskJobConfigCriteria and() {
        if (and == null) {
            and = new TaskJobConfigCriteria();
        }
        return and;
    }

    public void setOr(TaskJobConfigCriteria or) {
        this.or = or;
    }

    public TaskJobConfigCriteria getOr() {
        return or;
    }

    public TaskJobConfigCriteria or() {
        if (or == null) {
            or = new TaskJobConfigCriteria();
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getJobClassName() {
        return jobClassName;
    }

    public StringFilter jobClassName() {
        if (jobClassName == null) {
            jobClassName = new StringFilter();
        }
        return jobClassName;
    }

    public void setJobClassName(StringFilter jobClassName) {
        this.jobClassName = jobClassName;
    }

    public StringFilter getCronExpression() {
        return cronExpression;
    }

    public StringFilter cronExpression() {
        if (cronExpression == null) {
            cronExpression = new StringFilter();
        }
        return cronExpression;
    }

    public void setCronExpression(StringFilter cronExpression) {
        this.cronExpression = cronExpression;
    }

    public StringFilter getParameter() {
        return parameter;
    }

    public StringFilter parameter() {
        if (parameter == null) {
            parameter = new StringFilter();
        }
        return parameter;
    }

    public void setParameter(StringFilter parameter) {
        this.parameter = parameter;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public JobStatusFilter getJobStatus() {
        return jobStatus;
    }

    public JobStatusFilter jobStatus() {
        if (jobStatus == null) {
            jobStatus = new JobStatusFilter();
        }
        return jobStatus;
    }

    public void setJobStatus(JobStatusFilter jobStatus) {
        this.jobStatus = jobStatus;
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

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
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

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
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
        final TaskJobConfigCriteria that = (TaskJobConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(jobClassName, that.jobClassName) &&
            Objects.equals(cronExpression, that.cronExpression) &&
            Objects.equals(parameter, that.parameter) &&
            Objects.equals(description, that.description) &&
            Objects.equals(jobStatus, that.jobStatus) &&
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
            name,
            jobClassName,
            cronExpression,
            parameter,
            description,
            jobStatus,
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
        return "TaskJobConfigCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (jobClassName != null ? "jobClassName=" + jobClassName + ", " : "") +
            (cronExpression != null ? "cronExpression=" + cronExpression + ", " : "") +
            (parameter != null ? "parameter=" + parameter + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (jobStatus != null ? "jobStatus=" + jobStatus + ", " : "") +
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
