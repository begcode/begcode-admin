package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * 报表存储
 */
@TableName(value = "u_report_file")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UReportFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @NotNull
    @TableField(value = "name")
    private String name;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "create_at")
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @TableField(value = "update_at")
    private ZonedDateTime updateAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UReportFile id(Long id) {
        this.id = id;
        return this;
    }

    public UReportFile name(String name) {
        this.name = name;
        return this;
    }

    public UReportFile content(String content) {
        this.content = content;
        return this;
    }

    public UReportFile createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UReportFile updateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UReportFile)) {
            return false;
        }
        return getId() != null && getId().equals(((UReportFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
