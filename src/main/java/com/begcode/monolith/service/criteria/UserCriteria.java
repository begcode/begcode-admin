package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Position;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private UserCriteria and;

    @BindQuery(ignore = true)
    private UserCriteria or;

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

    @BindQuery(column = "self.department_id")
    private LongFilter departmentId;

    @BindQuery(entity = Department.class, column = "name", condition = "this.department_id=id")
    private StringFilter departmentName;

    @BindQuery(column = "self.position_id")
    private LongFilter positionId;

    @BindQuery(entity = Position.class, column = "name", condition = "this.position_id=id")
    private StringFilter positionName;

    @BindQuery(
        entity = Authority.class,
        column = "id",
        condition = "this.id=rel_jhi_user__authorities.jhi_user_id AND rel_jhi_user__authorities.authorities_id=id"
    )
    private LongFilter authoritiesId;

    @BindQuery(
        entity = Authority.class,
        column = "name",
        condition = "this.id=rel_jhi_user__authorities.jhi_user_id AND rel_jhi_user__authorities.authorities_id=id"
    )
    private StringFilter authoritiesName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public UserCriteria() {}

    public UserCriteria(UserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.langKey = other.langKey == null ? null : other.langKey.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.positionId = other.positionId == null ? null : other.positionId.copy();
        this.positionName = other.positionName == null ? null : other.positionName.copy();
        this.authoritiesId = other.authoritiesId == null ? null : other.authoritiesId.copy();
        this.authoritiesName = other.authoritiesName == null ? null : other.authoritiesName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public StringFilter login() {
        if (login == null) {
            login = new StringFilter();
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public StringFilter mobile() {
        if (mobile == null) {
            mobile = new StringFilter();
        }
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public ZonedDateTimeFilter getBirthday() {
        return birthday;
    }

    public ZonedDateTimeFilter birthday() {
        if (birthday == null) {
            birthday = new ZonedDateTimeFilter();
        }
        return birthday;
    }

    public void setBirthday(ZonedDateTimeFilter birthday) {
        this.birthday = birthday;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getLangKey() {
        return langKey;
    }

    public StringFilter langKey() {
        if (langKey == null) {
            langKey = new StringFilter();
        }
        return langKey;
    }

    public void setLangKey(StringFilter langKey) {
        this.langKey = langKey;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            createdBy = new LongFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new LongFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public LongFilter positionId() {
        if (positionId == null) {
            positionId = new LongFilter();
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
    }

    public StringFilter getPositionName() {
        return positionName;
    }

    public StringFilter positionName() {
        if (positionName == null) {
            positionName = new StringFilter();
        }
        return positionName;
    }

    public void setPositionName(StringFilter positionName) {
        this.positionName = positionName;
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
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(positionName, that.positionName) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(authoritiesName, that.authoritiesName) &&
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
            departmentName,
            positionId,
            positionName,
            authoritiesId,
            authoritiesName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (login != null ? "login=" + login + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (mobile != null ? "mobile=" + mobile + ", " : "") +
            (birthday != null ? "birthday=" + birthday + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (langKey != null ? "langKey=" + langKey + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
            (positionId != null ? "positionId=" + positionId + ", " : "") +
            (positionName != null ? "positionName=" + positionName + ", " : "") +
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
