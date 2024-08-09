package com.begcode.monolith.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.ApiPermission}的DTO。
 */
@Schema(description = "API权限\n菜单或按钮下有相关的api权限")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiPermissionDTO implements Serializable {

    private Long id;

    /**
     * 服务名称
     */
    @Schema(description = "服务名称")
    @Excel(name = "服务名称")
    private String serviceName;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @Excel(name = "权限名称")
    private String name;

    private String code;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    @Excel(name = "权限描述")
    private String description;

    /**
     * 类型
     */
    @Schema(description = "类型")
    @Excel(name = "类型")
    private ApiPermissionType type;

    /**
     * 请求类型
     */
    @Schema(description = "请求类型")
    @Excel(name = "请求类型")
    private String method;

    /**
     * url 地址
     */
    @Schema(description = "url 地址")
    @Excel(name = "url 地址")
    private String url;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Excel(name = "状态")
    private ApiPermissionState status;

    /**
     * 子节点
     */
    @Schema(description = "子节点")
    @Excel(name = "子节点")
    private List<ApiPermissionDTO> children = new ArrayList<>();

    /**
     * 上级
     */
    @Schema(description = "上级")
    @Excel(name = "上级")
    private ApiPermissionDTO parent;

    private Long parentId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public ApiPermissionDTO id(Long id) {
        this.id = id;
        return this;
    }

    public ApiPermissionDTO serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ApiPermissionDTO name(String name) {
        this.name = name;
        return this;
    }

    public ApiPermissionDTO code(String code) {
        this.code = code;
        return this;
    }

    public ApiPermissionDTO description(String description) {
        this.description = description;
        return this;
    }

    public ApiPermissionDTO type(ApiPermissionType type) {
        this.type = type;
        return this;
    }

    public ApiPermissionDTO method(String method) {
        this.method = method;
        return this;
    }

    public ApiPermissionDTO url(String url) {
        this.url = url;
        return this;
    }

    public ApiPermissionDTO status(ApiPermissionState status) {
        this.status = status;
        return this;
    }

    public ApiPermissionDTO children(List<ApiPermissionDTO> children) {
        this.children = children;
        return this;
    }

    public ApiPermissionDTO parent(ApiPermissionDTO parent) {
        this.parent = parent;
        return this;
    }

    public ApiPermissionDTO parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ApiPermissionDTO apiPermissionDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, apiPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiPermissionDTO{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", method='" + getMethod() + "'" +
            ", url='" + getUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
