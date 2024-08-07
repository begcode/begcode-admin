package com.begcode.monolith.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.Department}的DTO。
 */
@Schema(description = "部门")
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartmentDTO implements Serializable {

    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @Excel(name = "名称")
    private String name;

    /**
     * 代码
     */
    @Schema(description = "代码")
    @Excel(name = "代码")
    private String code;

    /**
     * 地址
     */
    @Schema(description = "地址")
    @Excel(name = "地址")
    private String address;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @Excel(name = "联系电话")
    private String phoneNum;

    /**
     * logo地址
     */
    @Schema(description = "logo地址")
    @Excel(name = "logo地址")
    private String logo;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    @Excel(name = "联系人")
    private String contact;

    /**
     * 下级部门
     */
    @Schema(description = "下级部门")
    @Excel(name = "下级部门")
    private List<DepartmentDTO> children = new ArrayList<>();

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    @Excel(name = "角色列表")
    private List<AuthorityDTO> authorities = new ArrayList<>();

    /**
     * 上级
     */
    @Schema(description = "上级")
    @Excel(name = "上级")
    private DepartmentDTO parent;

    private Long parentId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public DepartmentDTO id(Long id) {
        this.id = id;
        return this;
    }

    public DepartmentDTO name(String name) {
        this.name = name;
        return this;
    }

    public DepartmentDTO code(String code) {
        this.code = code;
        return this;
    }

    public DepartmentDTO address(String address) {
        this.address = address;
        return this;
    }

    public DepartmentDTO phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public DepartmentDTO logo(String logo) {
        this.logo = logo;
        return this;
    }

    public DepartmentDTO contact(String contact) {
        this.contact = contact;
        return this;
    }

    public DepartmentDTO children(List<DepartmentDTO> children) {
        this.children = children;
        return this;
    }

    public DepartmentDTO authorities(List<AuthorityDTO> authorities) {
        this.authorities = authorities;
        return this;
    }

    public DepartmentDTO parent(DepartmentDTO parent) {
        this.parent = parent;
        return this;
    }

    public DepartmentDTO parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DepartmentDTO departmentDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", logo='" + getLogo() + "'" +
            ", contact='" + getContact() + "'" +
            ", children=" + getChildren() +
            ", authorities=" + getAuthorities() +
            ", parent=" + getParent() +
            "}";
    }
}
