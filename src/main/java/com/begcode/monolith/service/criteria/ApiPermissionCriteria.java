package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.ApiPermission} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.ApiPermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /api-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiPermissionCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private ApiPermissionCriteria and;

    @BindQuery(ignore = true)
    private ApiPermissionCriteria or;

    /**
     * Class for filtering ApiPermissionType
     */
    public static class ApiPermissionTypeFilter extends Filter<ApiPermissionType> {

        public ApiPermissionTypeFilter() {}

        public ApiPermissionTypeFilter(String value) {
            ApiPermissionType enumValue = ApiPermissionType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = ApiPermissionType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public ApiPermissionTypeFilter(ApiPermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ApiPermissionTypeFilter copy() {
            return new ApiPermissionTypeFilter(this);
        }
    }

    /**
     * Class for filtering ApiPermissionState
     */
    public static class ApiPermissionStateFilter extends Filter<ApiPermissionState> {

        public ApiPermissionStateFilter() {}

        public ApiPermissionStateFilter(String value) {
            ApiPermissionState enumValue = ApiPermissionState.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = ApiPermissionState.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public ApiPermissionStateFilter(ApiPermissionStateFilter filter) {
            super(filter);
        }

        @Override
        public ApiPermissionStateFilter copy() {
            return new ApiPermissionStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.service_name")
    private StringFilter serviceName;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.description")
    private StringFilter description;

    @BindQuery(column = "self.type")
    private ApiPermissionTypeFilter type;

    @BindQuery(column = "self.method")
    private StringFilter method;

    @BindQuery(column = "self.url")
    private StringFilter url;

    @BindQuery(column = "self.status")
    private ApiPermissionStateFilter status;

    @BindQuery(entity = ApiPermission.class, column = "id", condition = "id=parent_id")
    private LongFilter childrenId;

    @BindQuery(entity = ApiPermission.class, column = "name", condition = "id=parent_id")
    private StringFilter childrenName;

    @BindQuery(column = "self.parent_id")
    private LongFilter parentId;

    @BindQuery(entity = ApiPermission.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    private LongFilter authoritiesId;

    private StringFilter authoritiesName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public ApiPermissionCriteria() {}

    public ApiPermissionCriteria(ApiPermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serviceName = other.serviceName == null ? null : other.serviceName.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
        this.authoritiesId = other.authoritiesId == null ? null : other.authoritiesId.copy();
        this.authoritiesName = other.authoritiesName == null ? null : other.authoritiesName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ApiPermissionCriteria copy() {
        return new ApiPermissionCriteria(this);
    }

    public void setAnd(ApiPermissionCriteria and) {
        this.and = and;
    }

    public ApiPermissionCriteria getAnd() {
        return and;
    }

    public ApiPermissionCriteria and() {
        if (and == null) {
            and = new ApiPermissionCriteria();
        }
        return and;
    }

    public void setOr(ApiPermissionCriteria or) {
        this.or = or;
    }

    public ApiPermissionCriteria getOr() {
        return or;
    }

    public ApiPermissionCriteria or() {
        if (or == null) {
            or = new ApiPermissionCriteria();
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

    public StringFilter getServiceName() {
        return serviceName;
    }

    public StringFilter serviceName() {
        if (serviceName == null) {
            serviceName = new StringFilter();
        }
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ApiPermissionTypeFilter getType() {
        return type;
    }

    public ApiPermissionTypeFilter type() {
        if (type == null) {
            type = new ApiPermissionTypeFilter();
        }
        return type;
    }

    public void setType(ApiPermissionTypeFilter type) {
        this.type = type;
    }

    public StringFilter getMethod() {
        return method;
    }

    public StringFilter method() {
        if (method == null) {
            method = new StringFilter();
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public ApiPermissionStateFilter getStatus() {
        return status;
    }

    public ApiPermissionStateFilter status() {
        if (status == null) {
            status = new ApiPermissionStateFilter();
        }
        return status;
    }

    public void setStatus(ApiPermissionStateFilter status) {
        this.status = status;
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

    public LongFilter getAuthoritiesId() {
        return authoritiesId;
    }

    public LongFilter authoritiesId() {
        if (authoritiesId == null) {
            authoritiesId = new LongFilter();
        }
        return authoritiesId;
    }

    public void setAuthoritiesId(LongFilter authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public StringFilter getAuthoritiesName() {
        return authoritiesName;
    }

    public StringFilter authoritiesName() {
        if (authoritiesName == null) {
            authoritiesName = new StringFilter();
        }
        return authoritiesName;
    }

    public void setAuthoritiesName(StringFilter authoritiesName) {
        this.authoritiesName = authoritiesName;
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
        final ApiPermissionCriteria that = (ApiPermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serviceName, that.serviceName) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(type, that.type) &&
            Objects.equals(method, that.method) &&
            Objects.equals(url, that.url) &&
            Objects.equals(status, that.status) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(authoritiesName, that.authoritiesName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            serviceName,
            name,
            code,
            description,
            type,
            method,
            url,
            status,
            childrenId,
            childrenName,
            parentId,
            parentName,
            authoritiesId,
            authoritiesName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiPermissionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serviceName != null ? "serviceName=" + serviceName + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (method != null ? "method=" + method + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (parentName != null ? "parentName=" + parentName + ", " : "") +
            (authoritiesId != null ? "authoritiesId=" + authoritiesId + ", " : "") +
            (authoritiesName != null ? "authoritiesName=" + authoritiesName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
