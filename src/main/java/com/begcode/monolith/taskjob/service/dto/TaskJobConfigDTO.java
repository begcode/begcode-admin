package com.begcode.monolith.taskjob.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.AbstractAuditingEntity;
import com.begcode.monolith.domain.enumeration.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.taskjob.domain.TaskJobConfig}的DTO。
 */
@Schema(description = "任务配置")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskJobConfigDTO extends AbstractAuditingEntity<Long, TaskJobConfigDTO> {

    private Long id;

    /**
     * 任务名称
     */
    @Schema(description = "任务名称")
    @Excel(name = "任务名称")
    private String name;

    /**
     * 任务类名
     */
    @Schema(description = "任务类名")
    @Excel(name = "任务类名")
    private String jobClassName;

    /**
     * cron表达式
     */
    @Schema(description = "cron表达式")
    @Excel(name = "cron表达式")
    private String cronExpression;

    /**
     * 参数
     */
    @Schema(description = "参数")
    @Excel(name = "参数")
    private String parameter;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @Excel(name = "描述")
    private String description;

    /**
     * 任务状态
     */
    @Schema(description = "任务状态")
    @Excel(name = "任务状态")
    private JobStatus jobStatus;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    @Excel(name = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间")
    private Instant createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    @Excel(name = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @Excel(name = "修改时间")
    private Instant lastModifiedDate;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public TaskJobConfigDTO id(Long id) {
        this.id = id;
        return this;
    }

    public TaskJobConfigDTO name(String name) {
        this.name = name;
        return this;
    }

    public TaskJobConfigDTO jobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
        return this;
    }

    public TaskJobConfigDTO cronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public TaskJobConfigDTO parameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public TaskJobConfigDTO description(String description) {
        this.description = description;
        return this;
    }

    public TaskJobConfigDTO jobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public TaskJobConfigDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public TaskJobConfigDTO createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TaskJobConfigDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public TaskJobConfigDTO lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TaskJobConfigDTO taskJobConfigDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskJobConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskJobConfigDTO{" +
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
