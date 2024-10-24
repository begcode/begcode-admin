package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.domain.UploadImage;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.ResourceCategory} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.ResourceCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /resource-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.title")
    private StringFilter title;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.order_number")
    private IntegerFilter orderNumber;

    @BindQuery(entity = ResourceCategory.class, column = "id", condition = "parent_id=this.id")
    private LongFilter childrenId;

    @BindQuery(entity = ResourceCategory.class, column = "title", condition = "parent_id=this.id")
    private StringFilter childrenTitle;

    @BindQuery(entity = ResourceCategory.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = ResourceCategory.class, column = "title", condition = "this.parent_id=id")
    private StringFilter parentTitle;

    @BindQuery(entity = UploadImage.class, column = "id", condition = "category_id=this.id")
    private LongFilter imagesId;

    @BindQuery(entity = UploadImage.class, column = "url", condition = "category_id=this.id")
    private StringFilter imagesUrl;

    @BindQuery(entity = UploadFile.class, column = "id", condition = "category_id=this.id")
    private LongFilter filesId;

    @BindQuery(entity = UploadFile.class, column = "url", condition = "category_id=this.id")
    private StringFilter filesUrl;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private ResourceCategoryCriteria and;

    @BindQuery(ignore = true)
    private ResourceCategoryCriteria or;

    private Boolean distinct;

    public ResourceCategoryCriteria() {}

    public ResourceCategoryCriteria(ResourceCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.orderNumber = other.optionalOrderNumber().map(IntegerFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenTitle = other.optionalChildrenTitle().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentTitle = other.optionalParentTitle().map(StringFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.imagesUrl = other.optionalImagesUrl().map(StringFilter::copy).orElse(null);
        this.filesId = other.optionalFilesId().map(LongFilter::copy).orElse(null);
        this.filesUrl = other.optionalFilesUrl().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ResourceCategoryCriteria copy() {
        return new ResourceCategoryCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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

    public IntegerFilter getOrderNumber() {
        return orderNumber;
    }

    public Optional<IntegerFilter> optionalOrderNumber() {
        return Optional.ofNullable(orderNumber);
    }

    public IntegerFilter orderNumber() {
        if (orderNumber == null) {
            setOrderNumber(new IntegerFilter());
        }
        return orderNumber;
    }

    public void setOrderNumber(IntegerFilter orderNumber) {
        this.orderNumber = orderNumber;
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

    public StringFilter getChildrenTitle() {
        return childrenTitle;
    }

    public Optional<StringFilter> optionalChildrenTitle() {
        return Optional.ofNullable(childrenTitle);
    }

    public StringFilter childrenTitle() {
        if (childrenTitle == null) {
            setChildrenTitle(new StringFilter());
        }
        return childrenTitle;
    }

    public void setChildrenTitle(StringFilter childrenTitle) {
        this.childrenTitle = childrenTitle;
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

    public StringFilter getParentTitle() {
        return parentTitle;
    }

    public Optional<StringFilter> optionalParentTitle() {
        return Optional.ofNullable(parentTitle);
    }

    public StringFilter parentTitle() {
        if (parentTitle == null) {
            setParentTitle(new StringFilter());
        }
        return parentTitle;
    }

    public void setParentTitle(StringFilter parentTitle) {
        this.parentTitle = parentTitle;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public Optional<LongFilter> optionalImagesId() {
        return Optional.ofNullable(imagesId);
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            setImagesId(new LongFilter());
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public StringFilter getImagesUrl() {
        return imagesUrl;
    }

    public Optional<StringFilter> optionalImagesUrl() {
        return Optional.ofNullable(imagesUrl);
    }

    public StringFilter imagesUrl() {
        if (imagesUrl == null) {
            setImagesUrl(new StringFilter());
        }
        return imagesUrl;
    }

    public void setImagesUrl(StringFilter imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public LongFilter getFilesId() {
        return filesId;
    }

    public Optional<LongFilter> optionalFilesId() {
        return Optional.ofNullable(filesId);
    }

    public LongFilter filesId() {
        if (filesId == null) {
            setFilesId(new LongFilter());
        }
        return filesId;
    }

    public void setFilesId(LongFilter filesId) {
        this.filesId = filesId;
    }

    public StringFilter getFilesUrl() {
        return filesUrl;
    }

    public Optional<StringFilter> optionalFilesUrl() {
        return Optional.ofNullable(filesUrl);
    }

    public StringFilter filesUrl() {
        if (filesUrl == null) {
            setFilesUrl(new StringFilter());
        }
        return filesUrl;
    }

    public void setFilesUrl(StringFilter filesUrl) {
        this.filesUrl = filesUrl;
    }

    public void setAnd(ResourceCategoryCriteria and) {
        this.and = and;
    }

    public ResourceCategoryCriteria getAnd() {
        return and;
    }

    public ResourceCategoryCriteria and() {
        if (and == null) {
            and = new ResourceCategoryCriteria();
        }
        return and;
    }

    public void setOr(ResourceCategoryCriteria or) {
        this.or = or;
    }

    public ResourceCategoryCriteria getOr() {
        return or;
    }

    public ResourceCategoryCriteria or() {
        if (or == null) {
            or = new ResourceCategoryCriteria();
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
        final ResourceCategoryCriteria that = (ResourceCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(code, that.code) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(filesId, that.filesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, code, orderNumber, childrenId, parentId, imagesId, filesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceCategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalOrderNumber().map(f -> "orderNumber=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenTitle().map(f -> "childrenTitle=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalParentTitle().map(f -> "parentTitle=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalImagesUrl().map(f -> "imagesUrl=" + f + ", ").orElse("") +
            optionalFilesId().map(f -> "filesId=" + f + ", ").orElse("") +
            optionalFilesUrl().map(f -> "filesUrl=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
