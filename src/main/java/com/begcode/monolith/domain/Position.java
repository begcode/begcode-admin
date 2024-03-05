package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 岗位
 *
 */
@TableName(value = "position")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 岗位代码
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "code")
    private String code;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "name")
    private String name;

    /**
     * 排序
     */
    @TableField(value = "sort_no")
    private Integer sortNo;

    /**
     * 描述
     */
    @Size(max = 200)
    @TableField(value = "description")
    private String description;

    /**
     * 员工列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "id=position_id")
    @JsonIgnoreProperties(value = { "department", "position", "authorities" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Position id(Long id) {
        this.id = id;
        return this;
    }

    public Position code(String code) {
        this.code = code;
        return this;
    }

    public Position name(String name) {
        this.name = name;
        return this;
    }

    public Position sortNo(Integer sortNo) {
        this.sortNo = sortNo;
        return this;
    }

    public Position description(String description) {
        this.description = description;
        return this;
    }

    public Position users(List<User> users) {
        this.users = users;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return getId() != null && getId().equals(((Position) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
