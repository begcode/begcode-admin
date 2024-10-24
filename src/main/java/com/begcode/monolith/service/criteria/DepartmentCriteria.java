package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.User;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.Department} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.DepartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.address")
    private StringFilter address;

    @BindQuery(column = "self.phone_num")
    private StringFilter phoneNum;

    @BindQuery(column = "self.logo")
    private StringFilter logo;

    @BindQuery(column = "self.contact")
    private StringFilter contact;

    @BindQuery(entity = Department.class, column = "id", condition = "parent_id=this.id")
    private LongFilter childrenId;

    @BindQuery(entity = Department.class, column = "name", condition = "parent_id=this.id")
    private StringFilter childrenName;

    @BindQuery(
        entity = Authority.class,
        column = "id",
        condition = "this.id=rel_department__authorities.department_id AND rel_department__authorities.authorities_id=id"
    )
    private LongFilter authoritiesId;

    @BindQuery(
        entity = Authority.class,
        column = "name",
        condition = "this.id=rel_department__authorities.department_id AND rel_department__authorities.authorities_id=id"
    )
    private StringFilter authoritiesName;

    @BindQuery(entity = Department.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = Department.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    @BindQuery(entity = User.class, column = "id", condition = "department_id=this.id")
    private LongFilter usersId;

    @BindQuery(entity = User.class, column = "first_name", condition = "department_id=this.id")
    private StringFilter usersFirstName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private DepartmentCriteria and;

    @BindQuery(ignore = true)
    private DepartmentCriteria or;

    private Boolean distinct;

    public DepartmentCriteria() {}

    public DepartmentCriteria(DepartmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.phoneNum = other.optionalPhoneNum().map(StringFilter::copy).orElse(null);
        this.logo = other.optionalLogo().map(StringFilter::copy).orElse(null);
        this.contact = other.optionalContact().map(StringFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenName = other.optionalChildrenName().map(StringFilter::copy).orElse(null);
        this.authoritiesId = other.optionalAuthoritiesId().map(LongFilter::copy).orElse(null);
        this.authoritiesName = other.optionalAuthoritiesName().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentName = other.optionalParentName().map(StringFilter::copy).orElse(null);
        this.usersId = other.optionalUsersId().map(LongFilter::copy).orElse(null);
        this.usersFirstName = other.optionalUsersFirstName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DepartmentCriteria copy() {
        return new DepartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhoneNum() {
        return phoneNum;
    }

    public Optional<StringFilter> optionalPhoneNum() {
        return Optional.ofNullable(phoneNum);
    }

    public StringFilter phoneNum() {
        if (phoneNum == null) {
            setPhoneNum(new StringFilter());
        }
        return phoneNum;
    }

    public void setPhoneNum(StringFilter phoneNum) {
        this.phoneNum = phoneNum;
    }

    public StringFilter getLogo() {
        return logo;
    }

    public Optional<StringFilter> optionalLogo() {
        return Optional.ofNullable(logo);
    }

    public StringFilter logo() {
        if (logo == null) {
            setLogo(new StringFilter());
        }
        return logo;
    }

    public void setLogo(StringFilter logo) {
        this.logo = logo;
    }

    public StringFilter getContact() {
        return contact;
    }

    public Optional<StringFilter> optionalContact() {
        return Optional.ofNullable(contact);
    }

    public StringFilter contact() {
        if (contact == null) {
            setContact(new StringFilter());
        }
        return contact;
    }

    public void setContact(StringFilter contact) {
        this.contact = contact;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public Optional<LongFilter> optionalChildrenId() {
        return Optional.ofNullable(childrenId);
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            setChildrenId(new LongFilter());
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public Optional<StringFilter> optionalChildrenName() {
        return Optional.ofNullable(childrenName);
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            setChildrenName(new StringFilter());
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getAuthoritiesId() {
        return authoritiesId;
    }

    public Optional<LongFilter> optionalAuthoritiesId() {
        return Optional.ofNullable(authoritiesId);
    }

    public LongFilter authoritiesId() {
        if (authoritiesId == null) {
            setAuthoritiesId(new LongFilter());
        }
        return authoritiesId;
    }

    public void setAuthoritiesId(LongFilter authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public StringFilter getAuthoritiesName() {
        return authoritiesName;
    }

    public Optional<StringFilter> optionalAuthoritiesName() {
        return Optional.ofNullable(authoritiesName);
    }

    public StringFilter authoritiesName() {
        if (authoritiesName == null) {
            setAuthoritiesName(new StringFilter());
        }
        return authoritiesName;
    }

    public void setAuthoritiesName(StringFilter authoritiesName) {
        this.authoritiesName = authoritiesName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public Optional<LongFilter> optionalParentId() {
        return Optional.ofNullable(parentId);
    }

    public LongFilter parentId() {
        if (parentId == null) {
            setParentId(new LongFilter());
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public Optional<StringFilter> optionalParentName() {
        return Optional.ofNullable(parentName);
    }

    public StringFilter parentName() {
        if (parentName == null) {
            setParentName(new StringFilter());
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public Optional<LongFilter> optionalUsersId() {
        return Optional.ofNullable(usersId);
    }

    public LongFilter usersId() {
        if (usersId == null) {
            setUsersId(new LongFilter());
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public StringFilter getUsersFirstName() {
        return usersFirstName;
    }

    public Optional<StringFilter> optionalUsersFirstName() {
        return Optional.ofNullable(usersFirstName);
    }

    public StringFilter usersFirstName() {
        if (usersFirstName == null) {
            setUsersFirstName(new StringFilter());
        }
        return usersFirstName;
    }

    public void setUsersFirstName(StringFilter usersFirstName) {
        this.usersFirstName = usersFirstName;
    }

    public void setAnd(DepartmentCriteria and) {
        this.and = and;
    }

    public DepartmentCriteria getAnd() {
        return and;
    }

    public DepartmentCriteria and() {
        if (and == null) {
            and = new DepartmentCriteria();
        }
        return and;
    }

    public void setOr(DepartmentCriteria or) {
        this.or = or;
    }

    public DepartmentCriteria getOr() {
        return or;
    }

    public DepartmentCriteria or() {
        if (or == null) {
            or = new DepartmentCriteria();
        }
        return or;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepartmentCriteria that = (DepartmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phoneNum, that.phoneNum) &&
            Objects.equals(logo, that.logo) &&
            Objects.equals(contact, that.contact) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, address, phoneNum, logo, contact, childrenId, authoritiesId, parentId, usersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalPhoneNum().map(f -> "phoneNum=" + f + ", ").orElse("") +
            optionalLogo().map(f -> "logo=" + f + ", ").orElse("") +
            optionalContact().map(f -> "contact=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenName().map(f -> "childrenName=" + f + ", ").orElse("") +
            optionalAuthoritiesId().map(f -> "authoritiesId=" + f + ", ").orElse("") +
            optionalAuthoritiesName().map(f -> "authoritiesName=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalParentName().map(f -> "parentName=" + f + ", ").orElse("") +
            optionalUsersId().map(f -> "usersId=" + f + ", ").orElse("") +
            optionalUsersFirstName().map(f -> "usersFirstName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
