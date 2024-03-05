package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 角色
 */
@TableName(value = "jhi_authority")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色代号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 信息
     */
    @TableField(value = "info")
    private String info;

    /**
     * 排序
     */
    @TableField(value = "`order`")
    private Integer order;

    /**
     * 展示
     */
    @TableField(value = "display")
    private Boolean display;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = Authority.class, deepBind = true, condition = "id=parent_id", orderBy = "order:ASC")
    @JsonIgnoreProperties(
        value = { "children", "viewPermissions", "apiPermissions", "parent", "users", "departments" },
        allowSetters = true
    )
    private List<Authority> children = new ArrayList<>();

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = ViewPermission.class,
        joinTable = "rel_jhi_authority__view_permissions",
        joinColumn = "jhi_authority_id",
        inverseJoinColumn = "view_permissions_id",
        condition = "this.id=rel_jhi_authority__view_permissions.jhi_authority_id AND rel_jhi_authority__view_permissions.view_permissions_id=id",
        orderBy = "order:ASC"
    )
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ViewPermission> viewPermissions = new ArrayList<>();

    /**
     * Api权限列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = ApiPermission.class,
        joinTable = "rel_jhi_authority__api_permissions",
        joinColumn = "jhi_authority_id",
        inverseJoinColumn = "api_permissions_id",
        condition = "this.id=rel_jhi_authority__api_permissions.jhi_authority_id AND rel_jhi_authority__api_permissions.api_permissions_id=id"
    )
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ApiPermission> apiPermissions = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = Authority.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(
        value = { "children", "viewPermissions", "apiPermissions", "parent", "users", "departments" },
        allowSetters = true
    )
    private Authority parent;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 用户列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = User.class,
        condition = "this.id=rel_jhi_user__authorities.authorities_id AND rel_jhi_user__authorities.jhi_user_id=id"
    )
    @JsonIgnoreProperties(value = { "department", "position", "authorities" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    @TableField(exist = false)
    @BindEntityList(
        entity = Department.class,
        condition = "this.id=rel_department__authorities.authorities_id AND rel_department__authorities.department_id=id"
    )
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users" }, allowSetters = true)
    private List<Department> departments = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Authority id(Long id) {
        this.id = id;
        return this;
    }

    public Authority name(String name) {
        this.name = name;
        return this;
    }

    public Authority code(String code) {
        this.code = code;
        return this;
    }

    public Authority info(String info) {
        this.info = info;
        return this;
    }

    public Authority order(Integer order) {
        this.order = order;
        return this;
    }

    public Authority display(Boolean display) {
        this.display = display;
        return this;
    }

    public Authority children(List<Authority> authorities) {
        this.children = authorities;
        return this;
    }

    public Authority viewPermissions(List<ViewPermission> viewPermissions) {
        this.viewPermissions = viewPermissions;
        return this;
    }

    public Authority apiPermissions(List<ApiPermission> apiPermissions) {
        this.apiPermissions = apiPermissions;
        return this;
    }

    public Authority parent(Authority authority) {
        this.parent = authority;
        return this;
    }

    public Authority users(List<User> users) {
        this.users = users;
        return this;
    }

    public Authority departments(List<Department> departments) {
        this.departments = departments;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return getId() != null && getId().equals(((Authority) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
