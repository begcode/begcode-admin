package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Position;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.User} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.UserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.login")
    private StringFilter login;

    @BindQuery(column = "self.first_name")
    private StringFilter firstName;

    @BindQuery(column = "self.last_name")
    private StringFilter lastName;

    @BindQuery(column = "self.email")
    private StringFilter email;

    @BindQuery(column = "self.mobile")
    private StringFilter mobile;

    @BindQuery(column = "self.birthday")
    private ZonedDateTimeFilter birthday;

    @BindQuery(column = "self.activated")
    private BooleanFilter activated;

    @BindQuery(column = "self.lang_key")
    private StringFilter langKey;

    @BindQuery(column = "self.image_url")
    private StringFilter imageUrl;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = Department.class, column = "id", condition = "this.department_id=id")
    private LongFilter departmentId;

    @BindQuery(entity = Department.class, column = "name", condition = "this.department_id=id")
    private StringFilter departmentName;

    @BindQuery(entity = Position.class, column = "id", condition = "this.position_id=id")
    private LongFilter positionId;

    @BindQuery(entity = Position.class, column = "name", condition = "this.position_id=id")
    private StringFilter positionName;

    @BindQuery(
        entity = Authority.class,
        column = "id",
        condition = "this.id=rel_jhi_user__authorities.jhi_user_id AND rel_jhi_user__authorities.authorities_id=[object Object]"
    )
    private LongFilter authoritiesId;

    @BindQuery(
        entity = Authority.class,
        column = "name",
        condition = "this.id=rel_jhi_user__authorities.jhi_user_id AND rel_jhi_user__authorities.authorities_id=[object Object]"
    )
    private StringFilter authoritiesName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private UserCriteria and;

    @BindQuery(ignore = true)
    private UserCriteria or;

    private Boolean distinct;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.login = other.optionalLogin().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.mobile = other.optionalMobile().map(StringFilter::copy).orElse(null);
        this.birthday = other.optionalBirthday().map(ZonedDateTimeFilter::copy).orElse(null);
        this.activated = other.optionalActivated().map(BooleanFilter::copy).orElse(null);
        this.langKey = other.optionalLangKey().map(StringFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.departmentName = other.optionalDepartmentName().map(StringFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.positionName = other.optionalPositionName().map(StringFilter::copy).orElse(null);
        this.authoritiesId = other.optionalAuthoritiesId().map(LongFilter::copy).orElse(null);
        this.authoritiesName = other.optionalAuthoritiesName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public Optional<StringFilter> optionalLogin() {
        return Optional.ofNullable(login);
    }

    public StringFilter login() {
        if (login == null) {
            setLogin(new StringFilter());
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public Optional<StringFilter> optionalMobile() {
        return Optional.ofNullable(mobile);
    }

    public StringFilter mobile() {
        if (mobile == null) {
            setMobile(new StringFilter());
        }
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTimeFilter getBirthday() {
        return birthday;
    }

    public Optional<ZonedDateTimeFilter> optionalBirthday() {
        return Optional.ofNullable(birthday);
    }

    public ZonedDateTimeFilter birthday() {
        if (birthday == null) {
            setBirthday(new ZonedDateTimeFilter());
        }
        return birthday;
    }

    public void setBirthday(ZonedDateTimeFilter birthday) {
        this.birthday = birthday;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public Optional<BooleanFilter> optionalActivated() {
        return Optional.ofNullable(activated);
    }

    public BooleanFilter activated() {
        if (activated == null) {
            setActivated(new BooleanFilter());
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getLangKey() {
        return langKey;
    }

    public Optional<StringFilter> optionalLangKey() {
        return Optional.ofNullable(langKey);
    }

    public StringFilter langKey() {
        if (langKey == null) {
            setLangKey(new StringFilter());
        }
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<LongFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new LongFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<LongFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new LongFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public Optional<StringFilter> optionalDepartmentName() {
        return Optional.ofNullable(departmentName);
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            setDepartmentName(new StringFilter());
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public Optional<LongFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public LongFilter positionId() {
        if (positionId == null) {
            setPositionId(new LongFilter());
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public StringFilter getPositionName() {
        return positionName;
    }

    public Optional<StringFilter> optionalPositionName() {
        return Optional.ofNullable(positionName);
    }

    public StringFilter positionName() {
        if (positionName == null) {
            setPositionName(new StringFilter());
        }
        return positionName;
    }

    public void setPositionName(StringFilter positionName) {
        this.positionName = positionName;
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

    public void setAnd(UserCriteria and) {
        this.and = and;
    }

    public UserCriteria getAnd() {
        return and;
    }

    public UserCriteria and() {
        if (and == null) {
            and = new UserCriteria();
        }
        return and;
    }

    public void setOr(UserCriteria or) {
        this.or = or;
    }

    public UserCriteria getOr() {
        return or;
    }

    public UserCriteria or() {
        if (or == null) {
            or = new UserCriteria();
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
        final UserCriteria that = (UserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(langKey, that.langKey) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            login,
            firstName,
            lastName,
            email,
            mobile,
            birthday,
            activated,
            langKey,
            imageUrl,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            departmentId,
            positionId,
            authoritiesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLogin().map(f -> "login=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalMobile().map(f -> "mobile=" + f + ", ").orElse("") +
            optionalBirthday().map(f -> "birthday=" + f + ", ").orElse("") +
            optionalActivated().map(f -> "activated=" + f + ", ").orElse("") +
            optionalLangKey().map(f -> "langKey=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalDepartmentName().map(f -> "departmentName=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalPositionName().map(f -> "positionName=" + f + ", ").orElse("") +
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
