package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.ViewPermission;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.Authority} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.AuthorityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /authorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private AuthorityCriteria and;

    @BindQuery(ignore = true)
    private AuthorityCriteria or;

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.info")
    private StringFilter info;

    @BindQuery(column = "self.`order`")
    private IntegerFilter order;

    @BindQuery(column = "self.display")
    private BooleanFilter display;

    @BindQuery(entity = Authority.class, column = "id", condition = "id=parent_id")
    private LongFilter childrenId;

    @BindQuery(entity = Authority.class, column = "name", condition = "id=parent_id")
    private StringFilter childrenName;

    @BindQuery(
        entity = ViewPermission.class,
        column = "id",
        condition = "this.id=rel_jhi_authority__view_permissions.jhi_authority_id AND rel_jhi_authority__view_permissions.view_permissions_id=id"
    )
    private LongFilter viewPermissionsId;

    @BindQuery(
        entity = ViewPermission.class,
        column = "text",
        condition = "this.id=rel_jhi_authority__view_permissions.jhi_authority_id AND rel_jhi_authority__view_permissions.view_permissions_id=id"
    )
    private StringFilter viewPermissionsText;

    @BindQuery(
        entity = ApiPermission.class,
        column = "id",
        condition = "this.id=rel_jhi_authority__api_permissions.jhi_authority_id AND rel_jhi_authority__api_permissions.api_permissions_id=id"
    )
    private LongFilter apiPermissionsId;

    @BindQuery(
        entity = ApiPermission.class,
        column = "name",
        condition = "this.id=rel_jhi_authority__api_permissions.jhi_authority_id AND rel_jhi_authority__api_permissions.api_permissions_id=id"
    )
    private StringFilter apiPermissionsName;

    @BindQuery(column = "self.parent_id")
    private LongFilter parentId;

    @BindQuery(entity = Authority.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    private LongFilter usersId;

    private StringFilter usersFirstName;

    private LongFilter departmentId;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public AuthorityCriteria() {}

    public AuthorityCriteria(AuthorityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.display = other.display == null ? null : other.display.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.viewPermissionsId = other.viewPermissionsId == null ? null : other.viewPermissionsId.copy();
        this.viewPermissionsText = other.viewPermissionsText == null ? null : other.viewPermissionsText.copy();
        this.apiPermissionsId = other.apiPermissionsId == null ? null : other.apiPermissionsId.copy();
        this.apiPermissionsName = other.apiPermissionsName == null ? null : other.apiPermissionsName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.usersFirstName = other.usersFirstName == null ? null : other.usersFirstName.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AuthorityCriteria copy() {
        return new AuthorityCriteria(this);
    }

    public void setAnd(AuthorityCriteria and) {
        this.and = and;
    }

    public AuthorityCriteria getAnd() {
        return and;
    }

    public AuthorityCriteria and() {
        if (and == null) {
            and = new AuthorityCriteria();
        }
        return and;
    }

    public void setOr(AuthorityCriteria or) {
        this.or = or;
    }

    public AuthorityCriteria getOr() {
        return or;
    }

    public AuthorityCriteria or() {
        if (or == null) {
            or = new AuthorityCriteria();
        }
        return or;
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getInfo() {
        return info;
    }

    public StringFilter info() {
        if (info == null) {
            info = new StringFilter();
        }
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public BooleanFilter getDisplay() {
        return display;
    }

    public BooleanFilter display() {
        if (display == null) {
            display = new BooleanFilter();
        }
        return display;
    }

    public void setDisplay(BooleanFilter display) {
        this.display = display;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            childrenName = new StringFilter();
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getViewPermissionsId() {
        return viewPermissionsId;
    }

    public LongFilter viewPermissionsId() {
        if (viewPermissionsId == null) {
            viewPermissionsId = new LongFilter();
        }
        return viewPermissionsId;
    }

    public void setViewPermissionsId(LongFilter viewPermissionsId) {
        this.viewPermissionsId = viewPermissionsId;
    }

    public StringFilter getViewPermissionsText() {
        return viewPermissionsText;
    }

    public StringFilter viewPermissionsText() {
        if (viewPermissionsText == null) {
            viewPermissionsText = new StringFilter();
        }
        return viewPermissionsText;
    }

    public void setViewPermissionsText(StringFilter viewPermissionsText) {
        this.viewPermissionsText = viewPermissionsText;
    }

    public LongFilter getApiPermissionsId() {
        return apiPermissionsId;
    }

    public LongFilter apiPermissionsId() {
        if (apiPermissionsId == null) {
            apiPermissionsId = new LongFilter();
        }
        return apiPermissionsId;
    }

    public void setApiPermissionsId(LongFilter apiPermissionsId) {
        this.apiPermissionsId = apiPermissionsId;
    }

    public StringFilter getApiPermissionsName() {
        return apiPermissionsName;
    }

    public StringFilter apiPermissionsName() {
        if (apiPermissionsName == null) {
            apiPermissionsName = new StringFilter();
        }
        return apiPermissionsName;
    }

    public void setApiPermissionsName(StringFilter apiPermissionsName) {
        this.apiPermissionsName = apiPermissionsName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public StringFilter parentName() {
        if (parentName == null) {
            parentName = new StringFilter();
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public LongFilter usersId() {
        if (usersId == null) {
            usersId = new LongFilter();
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public StringFilter getUsersFirstName() {
        return usersFirstName;
    }

    public StringFilter usersFirstName() {
        if (usersFirstName == null) {
            usersFirstName = new StringFilter();
        }
        return usersFirstName;
    }

    public void setUsersFirstName(StringFilter usersFirstName) {
        this.usersFirstName = usersFirstName;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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
        final AuthorityCriteria that = (AuthorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(info, that.info) &&
            Objects.equals(order, that.order) &&
            Objects.equals(display, that.display) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(viewPermissionsId, that.viewPermissionsId) &&
            Objects.equals(viewPermissionsText, that.viewPermissionsText) &&
            Objects.equals(apiPermissionsId, that.apiPermissionsId) &&
            Objects.equals(apiPermissionsName, that.apiPermissionsName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(usersFirstName, that.usersFirstName) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            info,
            order,
            display,
            childrenId,
            childrenName,
            viewPermissionsId,
            viewPermissionsText,
            apiPermissionsId,
            apiPermissionsName,
            parentId,
            parentName,
            usersId,
            usersFirstName,
            departmentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (info != null ? "info=" + info + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (display != null ? "display=" + display + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
            (viewPermissionsId != null ? "viewPermissionsId=" + viewPermissionsId + ", " : "") +
            (viewPermissionsText != null ? "viewPermissionsText=" + viewPermissionsText + ", " : "") +
            (apiPermissionsId != null ? "apiPermissionsId=" + apiPermissionsId + ", " : "") +
            (apiPermissionsName != null ? "apiPermissionsName=" + apiPermissionsName + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (parentName != null ? "parentName=" + parentName + ", " : "") +
            (usersId != null ? "usersId=" + usersId + ", " : "") +
            (usersFirstName != null ? "usersFirstName=" + usersFirstName + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
