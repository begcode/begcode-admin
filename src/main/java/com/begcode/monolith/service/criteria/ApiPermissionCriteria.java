package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * Class for filtering ApiPermissionType
     */
    public static class ApiPermissionTypeFilter extends Filter<ApiPermissionType> {

        public ApiPermissionTypeFilter() {}

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

    @BindQuery(entity = ApiPermission.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = ApiPermission.class, column = "name", condition = "this.parent_id=id")
    private StringFilter parentName;

    private LongFilter authoritiesId;

    private StringFilter authoritiesName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private ApiPermissionCriteria and;

    @BindQuery(ignore = true)
    private ApiPermissionCriteria or;

    private Boolean distinct;

    public ApiPermissionCriteria() {}

    public ApiPermissionCriteria(ApiPermissionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.serviceName = other.optionalServiceName().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(ApiPermissionTypeFilter::copy).orElse(null);
        this.method = other.optionalMethod().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ApiPermissionStateFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenName = other.optionalChildrenName().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentName = other.optionalParentName().map(StringFilter::copy).orElse(null);
        this.authoritiesId = other.optionalAuthoritiesId().map(LongFilter::copy).orElse(null);
        this.authoritiesName = other.optionalAuthoritiesName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ApiPermissionCriteria copy() {
        return new ApiPermissionCriteria(this);
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

    public StringFilter getServiceName() {
        return serviceName;
    }

    public Optional<StringFilter> optionalServiceName() {
        return Optional.ofNullable(serviceName);
    }

    public StringFilter serviceName() {
        if (serviceName == null) {
            setServiceName(new StringFilter());
        }
        return serviceName;
    }

    public void setServiceName(StringFilter serviceName) {
        this.serviceName = serviceName;
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ApiPermissionTypeFilter getType() {
        return type;
    }

    public Optional<ApiPermissionTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public ApiPermissionTypeFilter type() {
        if (type == null) {
            setType(new ApiPermissionTypeFilter());
        }
        return type;
    }

    public void setType(ApiPermissionTypeFilter type) {
        this.type = type;
    }

    public StringFilter getMethod() {
        return method;
    }

    public Optional<StringFilter> optionalMethod() {
        return Optional.ofNullable(method);
    }

    public StringFilter method() {
        if (method == null) {
            setMethod(new StringFilter());
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public ApiPermissionStateFilter getStatus() {
        return status;
    }

    public Optional<ApiPermissionStateFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ApiPermissionStateFilter status() {
        if (status == null) {
            setStatus(new ApiPermissionStateFilter());
        }
        return status;
    }

    public void setStatus(ApiPermissionStateFilter status) {
        this.status = status;
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
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
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
            parentId,
            authoritiesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiPermissionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalServiceName().map(f -> "serviceName=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalMethod().map(f -> "method=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenName().map(f -> "childrenName=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalParentName().map(f -> "parentName=" + f + ", ").orElse("") +
            optionalAuthoritiesId().map(f -> "authoritiesId=" + f + ", ").orElse("") +
            optionalAuthoritiesName().map(f -> "authoritiesName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
