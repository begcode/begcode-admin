package com.begcode.monolith.service.dto;

import com.begcode.monolith.domain.enumeration.TargetType;
import com.begcode.monolith.domain.enumeration.ViewPermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.ViewPermission}的DTO。
 */
@Schema(description = "可视权限\n权限分为菜单权限、按钮权限等\n")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPermissionDTO implements Serializable {

    private Long id;

    /**
     * 权限名称
     */
    @NotNull
    @Schema(description = "权限名称", required = true)
    private String text;

    /**
     * 权限类型
     */
    @Schema(description = "权限类型")
    private ViewPermissionType type;

    /**
     * i18n主键
     */
    @Schema(description = "i18n主键")
    private String i18n;

    /**
     * 显示分组名
     */
    @Schema(description = "显示分组名")
    private Boolean group;

    /**
     * 路由
     */
    @Schema(description = "路由")
    private String link;

    /**
     * 外部链接
     */
    @Schema(description = "外部链接")
    private String externalLink;

    /**
     * 链接目标
     */
    @Schema(description = "链接目标")
    private TargetType target;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 禁用菜单
     */
    @Schema(description = "禁用菜单")
    private Boolean disabled;

    /**
     * 隐藏菜单
     */
    @Schema(description = "隐藏菜单")
    private Boolean hide;

    /**
     * 隐藏面包屑
     */
    @Schema(description = "隐藏面包屑")
    private Boolean hideInBreadcrumb;

    /**
     * 快捷菜单项
     */
    @Schema(description = "快捷菜单项")
    private Boolean shortcut;

    /**
     * 菜单根节点
     */
    @Schema(description = "菜单根节点")
    private Boolean shortcutRoot;

    /**
     * 允许复用
     */
    @Schema(description = "允许复用")
    private Boolean reuse;

    /**
     * 权限代码
     * (ROLE_开头)
     */
    @NotNull
    @Schema(description = "权限代码\n(ROLE_开头)", required = true)
    private String code;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer order;

    /**
     * api权限标识串
     */
    @Schema(description = "api权限标识串")
    private String apiPermissionCodes;

    /**
     * 组件名称
     */
    @Schema(description = "组件名称")
    private String componentFile;

    /**
     * 重定向路径
     */
    @Schema(description = "重定向路径")
    private String redirect;

    /**
     * 上级
     */
    @Schema(description = "上级")
    private ViewPermissionDTO parent;

    private Long parentId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public ViewPermissionDTO id(Long id) {
        this.id = id;
        return this;
    }

    public ViewPermissionDTO text(String text) {
        this.text = text;
        return this;
    }

    public ViewPermissionDTO type(ViewPermissionType type) {
        this.type = type;
        return this;
    }

    public ViewPermissionDTO i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public ViewPermissionDTO group(Boolean group) {
        this.group = group;
        return this;
    }

    public ViewPermissionDTO link(String link) {
        this.link = link;
        return this;
    }

    public ViewPermissionDTO externalLink(String externalLink) {
        this.externalLink = externalLink;
        return this;
    }

    public ViewPermissionDTO target(TargetType target) {
        this.target = target;
        return this;
    }

    public ViewPermissionDTO icon(String icon) {
        this.icon = icon;
        return this;
    }

    public ViewPermissionDTO disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public ViewPermissionDTO hide(Boolean hide) {
        this.hide = hide;
        return this;
    }

    public ViewPermissionDTO hideInBreadcrumb(Boolean hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
        return this;
    }

    public ViewPermissionDTO shortcut(Boolean shortcut) {
        this.shortcut = shortcut;
        return this;
    }

    public ViewPermissionDTO shortcutRoot(Boolean shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
        return this;
    }

    public ViewPermissionDTO reuse(Boolean reuse) {
        this.reuse = reuse;
        return this;
    }

    public ViewPermissionDTO code(String code) {
        this.code = code;
        return this;
    }

    public ViewPermissionDTO description(String description) {
        this.description = description;
        return this;
    }

    public ViewPermissionDTO order(Integer order) {
        this.order = order;
        return this;
    }

    public ViewPermissionDTO apiPermissionCodes(String apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
        return this;
    }

    public ViewPermissionDTO componentFile(String componentFile) {
        this.componentFile = componentFile;
        return this;
    }

    public ViewPermissionDTO redirect(String redirect) {
        this.redirect = redirect;
        return this;
    }

    public ViewPermissionDTO parent(ViewPermissionDTO parent) {
        this.parent = parent;
        return this;
    }

    public ViewPermissionDTO parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ViewPermissionDTO viewPermissionDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, viewPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermissionDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", type='" + getType() + "'" +
            ", i18n='" + getI18n() + "'" +
            ", group='" + getGroup() + "'" +
            ", link='" + getLink() + "'" +
            ", externalLink='" + getExternalLink() + "'" +
            ", target='" + getTarget() + "'" +
            ", icon='" + getIcon() + "'" +
            ", disabled='" + getDisabled() + "'" +
            ", hide='" + getHide() + "'" +
            ", hideInBreadcrumb='" + getHideInBreadcrumb() + "'" +
            ", shortcut='" + getShortcut() + "'" +
            ", shortcutRoot='" + getShortcutRoot() + "'" +
            ", reuse='" + getReuse() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", apiPermissionCodes='" + getApiPermissionCodes() + "'" +
            ", componentFile='" + getComponentFile() + "'" +
            ", redirect='" + getRedirect() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
