package com.begcode.monolith.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.*;
import lombok.*;

/**
 * 部门
 */
@TableName(value = "department")
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 联系电话
     */
    @TableField(value = "phone_num")
    private String phoneNum;

    /**
     * logo地址
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 联系人
     */
    @TableField(value = "contact")
    private String contact;

    /**
     * 下级部门
     */
    @TableField(exist = false)
    @BindEntityList(entity = Department.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users" }, allowSetters = true)
    private List<Department> children = new ArrayList<>();

    /**
     * 角色列表
     */
    @TableField(exist = false)
    @BindEntityList(
        entity = Authority.class,
        joinTable = "rel_department__authorities",
        joinColumn = "department_id",
        inverseJoinColumn = "authorities_id",
        condition = "this.id=rel_department__authorities.department_id AND rel_department__authorities.authorities_id=id",
        orderBy = "order:ASC"
    )
    @JsonIgnoreProperties(
        value = { "children", "viewPermissions", "apiPermissions", "parent", "users", "departments" },
        allowSetters = true
    )
    private List<Authority> authorities = new ArrayList<>();

    /**
     * 上级
     */
    @TableField(exist = false)
    @BindEntity(entity = Department.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "authorities", "parent", "users" }, allowSetters = true)
    private Department parent;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 用户列表
     */
    @TableField(exist = false)
    @BindEntityList(entity = User.class, condition = "id=department_id")
    @JsonIgnoreProperties(value = { "department", "position", "authorities" }, allowSetters = true)
    private List<User> users = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Department id(Long id) {
        this.id = id;
        return this;
    }

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public Department code(String code) {
        this.code = code;
        return this;
    }

    public Department address(String address) {
        this.address = address;
        return this;
    }

    public Department phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public Department logo(String logo) {
        this.logo = logo;
        return this;
    }

    public Department contact(String contact) {
        this.contact = contact;
        return this;
    }

    public Department children(List<Department> departments) {
        this.children = departments;
        return this;
    }

    public Department authorities(List<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Department parent(Department department) {
        this.parent = department;
        return this;
    }

    public Department parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Department users(List<User> users) {
        this.users = users;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return getId() != null && getId().equals(((Department) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", logo='" + getLogo() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
