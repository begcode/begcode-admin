package com.begcode.monolith.taskjob.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.JobStatus;
import java.io.Serializable;
import lombok.*;

/**
 * 定时任务
 */
@TableName(value = "task_job_config")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskJobConfig extends AbstractAuditingEntity<Long, TaskJobConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 任务类名
     */
    @TableField(value = "job_class_name")
    private String jobClassName;

    /**
     * cron表达式
     */
    @TableField(value = "cron_expression")
    private String cronExpression;

    /**
     * 参数
     */
    @TableField(value = "parameter")
    private String parameter;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 任务状态
     */
    @TableField(value = "job_status")
    private JobStatus jobStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TaskJobConfig id(Long id) {
        this.id = id;
        return this;
    }

    public TaskJobConfig name(String name) {
        this.name = name;
        return this;
    }

    public TaskJobConfig jobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
        return this;
    }

    public TaskJobConfig cronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public TaskJobConfig parameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public TaskJobConfig description(String description) {
        this.description = description;
        return this;
    }

    public TaskJobConfig jobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskJobConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((TaskJobConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskJobConfig{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", jobClassName='" + getJobClassName() + "'" +
            ", cronExpression='" + getCronExpression() + "'" +
            ", parameter='" + getParameter() + "'" +
            ", description='" + getDescription() + "'" +
            ", jobStatus='" + getJobStatus() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy=" + getLastModifiedBy() +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
