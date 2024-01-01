package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@JsonIgnoreProperties(value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" }, allowGetters = true)
public abstract class AbstractAuditingEntity<T, E> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract T getId();

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @TableField(value = "created_date", fill = FieldFill.INSERT)
    private ZonedDateTime createdDate;

    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private Long lastModifiedBy;

    @TableField(value = "last_modified_date", fill = FieldFill.INSERT_UPDATE)
    private ZonedDateTime lastModifiedDate;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public E createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return (E) this;
    }

    public E lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return (E) this;
    }

    public E lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return (E) this;
    }

    public E createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return (E) this;
    }
}
