package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * API权限
 * 菜单或按钮下有相关的api权限
 */
@TableName(value = "api_permission")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    @TableField(value = "service_name")
    private String serviceName;

    /**
     * 权限名称
     */
    @TableField(value = "name")
    private String name;

    @TableField(value = "code")
    private String code;

    /**
     * 权限描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 类型
     */
    @TableField(value = "type")
    private ApiPermissionType type;

    /**
     * 请求类型
     */
    @TableField(value = "method")
    private String method;

    /**
     * url 地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 状态
     */
    @TableField(value = "status")
    private ApiPermissionState status;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = ApiPermission.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ApiPermission> children = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ApiPermission.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private ApiPermission parent;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        condition = "this.id=rel_jhi_authority__api_permissions.api_permissions_id AND rel_jhi_authority__api_permissions.jhi_authority_id=id"
    )
    @JsonIgnoreProperties(
        value = { "children", "viewPermissions", "apiPermissions", "parent", "users", "departments" },
        allowSetters = true
    )
    private List<Authority> authorities = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ApiPermission id(Long id) {
        this.id = id;
        return this;
    }

    public ApiPermission serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ApiPermission name(String name) {
        this.name = name;
        return this;
    }

    public ApiPermission code(String code) {
        this.code = code;
        return this;
    }

    public ApiPermission description(String description) {
        this.description = description;
        return this;
    }

    public ApiPermission type(ApiPermissionType type) {
        this.type = type;
        return this;
    }

    public ApiPermission method(String method) {
        this.method = method;
        return this;
    }

    public ApiPermission url(String url) {
        this.url = url;
        return this;
    }

    public ApiPermission status(ApiPermissionState status) {
        this.status = status;
        return this;
    }

    public ApiPermission children(List<ApiPermission> apiPermissions) {
        this.children = apiPermissions;
        return this;
    }

    public ApiPermission parent(ApiPermission apiPermission) {
        this.parent = apiPermission;
        return this;
    }

    public ApiPermission authorities(List<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiPermission)) {
            return false;
        }
        return getId() != null && getId().equals(((ApiPermission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
