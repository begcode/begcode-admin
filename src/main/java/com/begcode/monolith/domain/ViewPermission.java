package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.begcode.monolith.domain.enumeration.TargetType;
import com.begcode.monolith.domain.enumeration.ViewPermissionType;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 可视权限
 * 权限分为菜单权限、按钮权限等
 *
 */
@TableName(value = "view_permission")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @NotNull
    @TableField(value = "text")
    private String text;

    /**
     * 权限类型
     */
    @TableField(value = "type")
    private ViewPermissionType type;

    /**
     * i18n主键
     */
    @TableField(value = "i_18_n")
    private String i18n;

    /**
     * 显示分组名
     */
    @TableField(value = "`group`")
    private Boolean group;

    /**
     * 路由
     */
    @TableField(value = "`link`")
    private String link;

    /**
     * 外部链接
     */
    @TableField(value = "external_link")
    private String externalLink;

    /**
     * 链接目标
     */
    @TableField(value = "target")
    private TargetType target;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 禁用菜单
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 隐藏菜单
     */
    @TableField(value = "hide")
    private Boolean hide;

    /**
     * 隐藏面包屑
     */
    @TableField(value = "hide_in_breadcrumb")
    private Boolean hideInBreadcrumb;

    /**
     * 快捷菜单项
     */
    @TableField(value = "shortcut")
    private Boolean shortcut;

    /**
     * 菜单根节点
     */
    @TableField(value = "shortcut_root")
    private Boolean shortcutRoot;

    /**
     * 允许复用
     */
    @TableField(value = "reuse")
    private Boolean reuse;

    /**
     * 权限代码
     * (ROLE_开头)
     */
    @NotNull
    @TableField(value = "code")
    private String code;

    /**
     * 权限描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 排序
     */
    @TableField(value = "`order`")
    private Integer order;

    /**
     * api权限标识串
     */
    @TableField(value = "api_permission_codes")
    private String apiPermissionCodes;

    /**
     * 组件名称
     */
    @TableField(value = "component_file")
    private String componentFile;

    /**
     * 重定向路径
     */
    @TableField(value = "redirect")
    private String redirect;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = ViewPermission.class, deepBind = true, condition = "id=parent_id", orderBy = "order:ASC")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private List<ViewPermission> children = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = ViewPermission.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent", "authorities" }, allowSetters = true)
    private ViewPermission parent;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        condition = "this.id=rel_jhi_authority__view_permissions.view_permissions_id AND rel_jhi_authority__view_permissions.jhi_authority_id=id"
    )
    @JsonIgnoreProperties(
        value = { "children", "viewPermissions", "apiPermissions", "parent", "users", "departments" },
        allowSetters = true
    )
    private List<Authority> authorities = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ViewPermission id(Long id) {
        this.id = id;
        return this;
    }

    public ViewPermission text(String text) {
        this.text = text;
        return this;
    }

    public ViewPermission type(ViewPermissionType type) {
        this.type = type;
        return this;
    }

    public ViewPermission i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public ViewPermission group(Boolean group) {
        this.group = group;
        return this;
    }

    public ViewPermission link(String link) {
        this.link = link;
        return this;
    }

    public ViewPermission externalLink(String externalLink) {
        this.externalLink = externalLink;
        return this;
    }

    public ViewPermission target(TargetType target) {
        this.target = target;
        return this;
    }

    public ViewPermission icon(String icon) {
        this.icon = icon;
        return this;
    }

    public ViewPermission disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public ViewPermission hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public ViewPermission hideInBreadcrumb(Boolean hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
        return this;
    }

    public ViewPermission shortcut(Boolean shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public ViewPermission shortcutRoot(Boolean shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
        return this;
    }

    public ViewPermission reuse(Boolean reuse) {
        this.reuse = reuse;
        return this;
    }

    public ViewPermission code(String code) {
        this.code = code;
        return this;
    }

    public ViewPermission description(String description) {
        this.description = description;
        return this;
    }

    public ViewPermission order(Integer order) {
        this.order = order;
        return this;
    }

    public ViewPermission apiPermissionCodes(String apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
        return this;
    }

    public ViewPermission componentFile(String componentFile) {
        this.componentFile = componentFile;
        return this;
    }

    public ViewPermission redirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public ViewPermission children(List<ViewPermission> viewPermissions) {
        this.children = viewPermissions;
        return this;
    }

    public ViewPermission parent(ViewPermission viewPermission) {
        this.parent = viewPermission;
        return this;
    }

    public ViewPermission authorities(List<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViewPermission)) {
            return false;
        }
        return getId() != null && getId().equals(((ViewPermission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
