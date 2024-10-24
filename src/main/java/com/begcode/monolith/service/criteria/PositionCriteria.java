package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.User;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    @BindQuery(entity = User.class, column = "id", condition = "position_id=this.id")
    private LongFilter usersId;

    @BindQuery(entity = User.class, column = "first_name", condition = "position_id=this.id")
    private StringFilter usersFirstName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private PositionCriteria and;

    @BindQuery(ignore = true)
    private PositionCriteria or;

    private Boolean distinct;

    public PositionCriteria() {}

    public PositionCriteria(PositionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.sortNo = other.optionalSortNo().map(IntegerFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.usersId = other.optionalUsersId().map(LongFilter::copy).orElse(null);
        this.usersFirstName = other.optionalUsersFirstName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionCriteria copy() {
        return new PositionCriteria(this);
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

    public IntegerFilter getSortNo() {
        return sortNo;
    }

    public Optional<IntegerFilter> optionalSortNo() {
        return Optional.ofNullable(sortNo);
    }

    public IntegerFilter sortNo() {
        if (sortNo == null) {
            setSortNo(new IntegerFilter());
        }
        return sortNo;
    }

    public void setSortNo(IntegerFilter sortNo) {
        this.sortNo = sortNo;
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
        final PositionCriteria that = (PositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sortNo, that.sortNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, sortNo, description, usersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSortNo().map(f -> "sortNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
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
