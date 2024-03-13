package com.begcode.monolith.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.Authority}的DTO。
 */
@Schema(description = "角色")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityDTO implements Serializable {

    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String name;

    /**
     * 角色代号
     */
    @Schema(description = "角色代号")
    private String code;

    /**
     * 信息
     */
    @Schema(description = "信息")
    private String info;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer order;

    /**
     * 展示
     */
    @Schema(description = "展示")
    private Boolean display;

    /**
     * 子节点
     */
    @Schema(description = "子节点")
    private List<AuthorityDTO> children = new ArrayList<>();

    /**
     * 菜单列表
     */
    @Schema(description = "菜单列表")
    private List<ViewPermissionDTO> viewPermissions = new ArrayList<>();

    /**
     * Api权限列表
     */
    @Schema(description = "Api权限列表")
    private List<ApiPermissionDTO> apiPermissions = new ArrayList<>();

    /**
     * 上级
     */
    @Schema(description = "上级")
    private AuthorityDTO parent;

    private Long parentId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public AuthorityDTO id(Long id) {
        this.id = id;
        return this;
    }

    public AuthorityDTO name(String name) {
        this.name = name;
        return this;
    }

    public AuthorityDTO code(String code) {
        this.code = code;
        return this;
    }

    public AuthorityDTO info(String info) {
        this.info = info;
        return this;
    }

    public AuthorityDTO order(Integer order) {
        this.order = order;
        return this;
    }

    public AuthorityDTO display(Boolean display) {
        this.display = display;
        return this;
    }

    public AuthorityDTO children(List<AuthorityDTO> children) {
        this.children = children;
        return this;
    }

    public AuthorityDTO viewPermissions(List<ViewPermissionDTO> viewPermissions) {
        this.viewPermissions = viewPermissions;
        return this;
    }

    public AuthorityDTO apiPermissions(List<ApiPermissionDTO> apiPermissions) {
        this.apiPermissions = apiPermissions;
        return this;
    }

    public AuthorityDTO parent(AuthorityDTO parent) {
        this.parent = parent;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityDTO)) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", info='" + getInfo() + "'" +
            ", order=" + getOrder() +
            ", display='" + getDisplay() + "'" +
            ", children=" + getChildren() +
            ", viewPermissions=" + getViewPermissions() +
            ", apiPermissions=" + getApiPermissions() +
            ", parent=" + getParent() +
            "}";
    }
}
