package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.User;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.Position} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.PositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /positions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private PositionCriteria and;

    @BindQuery(ignore = true)
    private PositionCriteria or;

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.sort_no")
    private IntegerFilter sortNo;

    @BindQuery(column = "self.description")
    private StringFilter description;

    @BindQuery(entity = User.class, column = "id", condition = "id=position_id")
    private LongFilter usersId;

    @BindQuery(entity = User.class, column = "first_name", condition = "id=position_id")
    private StringFilter usersFirstName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public PositionCriteria() {}

    public PositionCriteria(PositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sortNo = other.sortNo == null ? null : other.sortNo.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.usersFirstName = other.usersFirstName == null ? null : other.usersFirstName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PositionCriteria copy() {
        return new PositionCriteria(this);
    }

    public void setAnd(PositionCriteria and) {
        this.and = and;
    }

    public PositionCriteria getAnd() {
        return and;
    }

    public PositionCriteria and() {
        if (and == null) {
            and = new PositionCriteria();
        }
        return and;
    }

    public void setOr(PositionCriteria or) {
        this.or = or;
    }

    public PositionCriteria getOr() {
        return or;
    }

    public PositionCriteria or() {
        if (or == null) {
            or = new PositionCriteria();
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

    public IntegerFilter getSortNo() {
        return sortNo;
    }

    public IntegerFilter sortNo() {
        if (sortNo == null) {
            sortNo = new IntegerFilter();
        }
        return sortNo;
    }

    public void setSortNo(IntegerFilter sortNo) {
        this.sortNo = sortNo;
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
        final PositionCriteria that = (PositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sortNo, that.sortNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(usersFirstName, that.usersFirstName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, sortNo, description, usersId, usersFirstName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (sortNo != null ? "sortNo=" + sortNo + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (usersId != null ? "usersId=" + usersId + ", " : "") +
            (usersFirstName != null ? "usersFirstName=" + usersFirstName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
