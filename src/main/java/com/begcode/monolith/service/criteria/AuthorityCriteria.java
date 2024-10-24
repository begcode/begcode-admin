package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.ViewPermission;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    @BindQuery(entity = Authority.class, column = "id", condition = "parent_id=this.id")
    private LongFilter childrenId;

    @BindQuery(entity = Authority.class, column = "name", condition = "parent_id=this.id")
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

    @BindQuery(entity = Authority.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = Authority.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    private LongFilter usersId;

    private StringFilter usersFirstName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private AuthorityCriteria and;

    @BindQuery(ignore = true)
    private AuthorityCriteria or;

    private Boolean distinct;

    public AuthorityCriteria() {}

    public AuthorityCriteria(AuthorityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.info = other.optionalInfo().map(StringFilter::copy).orElse(null);
        this.order = other.optionalOrder().map(IntegerFilter::copy).orElse(null);
        this.display = other.optionalDisplay().map(BooleanFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenName = other.optionalChildrenName().map(StringFilter::copy).orElse(null);
        this.viewPermissionsId = other.optionalViewPermissionsId().map(LongFilter::copy).orElse(null);
        this.viewPermissionsText = other.optionalViewPermissionsText().map(StringFilter::copy).orElse(null);
        this.apiPermissionsId = other.optionalApiPermissionsId().map(LongFilter::copy).orElse(null);
        this.apiPermissionsName = other.optionalApiPermissionsName().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentName = other.optionalParentName().map(StringFilter::copy).orElse(null);
        this.usersId = other.optionalUsersId().map(LongFilter::copy).orElse(null);
        this.usersFirstName = other.optionalUsersFirstName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AuthorityCriteria copy() {
        return new AuthorityCriteria(this);
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

    public StringFilter getInfo() {
        return info;
    }

    public Optional<StringFilter> optionalInfo() {
        return Optional.ofNullable(info);
    }

    public StringFilter info() {
        if (info == null) {
            setInfo(new StringFilter());
        }
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public Optional<IntegerFilter> optionalOrder() {
        return Optional.ofNullable(order);
    }

    public IntegerFilter order() {
        if (order == null) {
            setOrder(new IntegerFilter());
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public BooleanFilter getDisplay() {
        return display;
    }

    public Optional<BooleanFilter> optionalDisplay() {
        return Optional.ofNullable(display);
    }

    public BooleanFilter display() {
        if (display == null) {
            setDisplay(new BooleanFilter());
        }
        return display;
    }

    public void setDisplay(BooleanFilter display) {
        this.display = display;
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

    public LongFilter getViewPermissionsId() {
        return viewPermissionsId;
    }

    public Optional<LongFilter> optionalViewPermissionsId() {
        return Optional.ofNullable(viewPermissionsId);
    }

    public LongFilter viewPermissionsId() {
        if (viewPermissionsId == null) {
            setViewPermissionsId(new LongFilter());
        }
        return viewPermissionsId;
    }

    public void setViewPermissionsId(LongFilter viewPermissionsId) {
        this.viewPermissionsId = viewPermissionsId;
    }

    public StringFilter getViewPermissionsText() {
        return viewPermissionsText;
    }

    public Optional<StringFilter> optionalViewPermissionsText() {
        return Optional.ofNullable(viewPermissionsText);
    }

    public StringFilter viewPermissionsText() {
        if (viewPermissionsText == null) {
            setViewPermissionsText(new StringFilter());
        }
        return viewPermissionsText;
    }

    public void setViewPermissionsText(StringFilter viewPermissionsText) {
        this.viewPermissionsText = viewPermissionsText;
    }

    public LongFilter getApiPermissionsId() {
        return apiPermissionsId;
    }

    public Optional<LongFilter> optionalApiPermissionsId() {
        return Optional.ofNullable(apiPermissionsId);
    }

    public LongFilter apiPermissionsId() {
        if (apiPermissionsId == null) {
            setApiPermissionsId(new LongFilter());
        }
        return apiPermissionsId;
    }

    public void setApiPermissionsId(LongFilter apiPermissionsId) {
        this.apiPermissionsId = apiPermissionsId;
    }

    public StringFilter getApiPermissionsName() {
        return apiPermissionsName;
    }

    public Optional<StringFilter> optionalApiPermissionsName() {
        return Optional.ofNullable(apiPermissionsName);
    }

    public StringFilter apiPermissionsName() {
        if (apiPermissionsName == null) {
            setApiPermissionsName(new StringFilter());
        }
        return apiPermissionsName;
    }

    public void setApiPermissionsName(StringFilter apiPermissionsName) {
        this.apiPermissionsName = apiPermissionsName;
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
        final AuthorityCriteria that = (AuthorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(info, that.info) &&
            Objects.equals(order, that.order) &&
            Objects.equals(display, that.display) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(viewPermissionsId, that.viewPermissionsId) &&
            Objects.equals(apiPermissionsId, that.apiPermissionsId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(usersId, that.usersId) &&
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
            viewPermissionsId,
            apiPermissionsId,
            parentId,
            usersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalInfo().map(f -> "info=" + f + ", ").orElse("") +
            optionalOrder().map(f -> "order=" + f + ", ").orElse("") +
            optionalDisplay().map(f -> "display=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenName().map(f -> "childrenName=" + f + ", ").orElse("") +
            optionalViewPermissionsId().map(f -> "viewPermissionsId=" + f + ", ").orElse("") +
            optionalViewPermissionsText().map(f -> "viewPermissionsText=" + f + ", ").orElse("") +
            optionalApiPermissionsId().map(f -> "apiPermissionsId=" + f + ", ").orElse("") +
            optionalApiPermissionsName().map(f -> "apiPermissionsName=" + f + ", ").orElse("") +
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
