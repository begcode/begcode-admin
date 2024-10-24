package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ResourceCategory;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.UploadImage} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.UploadImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /upload-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadImageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.full_name")
    private StringFilter fullName;

    @BindQuery(column = "self.business_title")
    private StringFilter businessTitle;

    @BindQuery(column = "self.business_desc")
    private StringFilter businessDesc;

    @BindQuery(column = "self.business_status")
    private StringFilter businessStatus;

    @BindQuery(column = "self.url")
    private StringFilter url;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.ext")
    private StringFilter ext;

    @BindQuery(column = "self.type")
    private StringFilter type;

    @BindQuery(column = "self.path")
    private StringFilter path;

    @BindQuery(column = "self.folder")
    private StringFilter folder;

    @BindQuery(column = "self.file_size")
    private LongFilter fileSize;

    @BindQuery(column = "self.owner_entity_name")
    private StringFilter ownerEntityName;

    @BindQuery(column = "self.owner_entity_id")
    private LongFilter ownerEntityId;

    @BindQuery(column = "self.create_at")
    private ZonedDateTimeFilter createAt;

    @BindQuery(column = "self.smart_url")
    private StringFilter smartUrl;

    @BindQuery(column = "self.medium_url")
    private StringFilter mediumUrl;

    @BindQuery(column = "self.reference_count")
    private LongFilter referenceCount;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = ResourceCategory.class, column = "id", condition = "this.category_id=id")
    private LongFilter categoryId;

    @BindQuery(entity = ResourceCategory.class, column = "title", condition = "this.category_id=id")
    private StringFilter categoryTitle;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private UploadImageCriteria and;

    @BindQuery(ignore = true)
    private UploadImageCriteria or;

    private Boolean distinct;

    public UploadImageCriteria() {}

    public UploadImageCriteria(UploadImageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.businessTitle = other.optionalBusinessTitle().map(StringFilter::copy).orElse(null);
        this.businessDesc = other.optionalBusinessDesc().map(StringFilter::copy).orElse(null);
        this.businessStatus = other.optionalBusinessStatus().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.ext = other.optionalExt().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.path = other.optionalPath().map(StringFilter::copy).orElse(null);
        this.folder = other.optionalFolder().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.ownerEntityName = other.optionalOwnerEntityName().map(StringFilter::copy).orElse(null);
        this.ownerEntityId = other.optionalOwnerEntityId().map(LongFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.smartUrl = other.optionalSmartUrl().map(StringFilter::copy).orElse(null);
        this.mediumUrl = other.optionalMediumUrl().map(StringFilter::copy).orElse(null);
        this.referenceCount = other.optionalReferenceCount().map(LongFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.categoryTitle = other.optionalCategoryTitle().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UploadImageCriteria copy() {
        return new UploadImageCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getBusinessTitle() {
        return businessTitle;
    }

    public Optional<StringFilter> optionalBusinessTitle() {
        return Optional.ofNullable(businessTitle);
    }

    public StringFilter businessTitle() {
        if (businessTitle == null) {
            setBusinessTitle(new StringFilter());
        }
        return businessTitle;
    }

    public void setBusinessTitle(StringFilter businessTitle) {
        this.businessTitle = businessTitle;
    }

    public StringFilter getBusinessDesc() {
        return businessDesc;
    }

    public Optional<StringFilter> optionalBusinessDesc() {
        return Optional.ofNullable(businessDesc);
    }

    public StringFilter businessDesc() {
        if (businessDesc == null) {
            setBusinessDesc(new StringFilter());
        }
        return businessDesc;
    }

    public void setBusinessDesc(StringFilter businessDesc) {
        this.businessDesc = businessDesc;
    }

    public StringFilter getBusinessStatus() {
        return businessStatus;
    }

    public Optional<StringFilter> optionalBusinessStatus() {
        return Optional.ofNullable(businessStatus);
    }

    public StringFilter businessStatus() {
        if (businessStatus == null) {
            setBusinessStatus(new StringFilter());
        }
        return businessStatus;
    }

    public void setBusinessStatus(StringFilter businessStatus) {
        this.businessStatus = businessStatus;
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

    public StringFilter getExt() {
        return ext;
    }

    public Optional<StringFilter> optionalExt() {
        return Optional.ofNullable(ext);
    }

    public StringFilter ext() {
        if (ext == null) {
            setExt(new StringFilter());
        }
        return ext;
    }

    public void setExt(StringFilter ext) {
        this.ext = ext;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getPath() {
        return path;
    }

    public Optional<StringFilter> optionalPath() {
        return Optional.ofNullable(path);
    }

    public StringFilter path() {
        if (path == null) {
            setPath(new StringFilter());
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getFolder() {
        return folder;
    }

    public Optional<StringFilter> optionalFolder() {
        return Optional.ofNullable(folder);
    }

    public StringFilter folder() {
        if (folder == null) {
            setFolder(new StringFilter());
        }
        return folder;
    }

    public void setFolder(StringFilter folder) {
        this.folder = folder;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getOwnerEntityName() {
        return ownerEntityName;
    }

    public Optional<StringFilter> optionalOwnerEntityName() {
        return Optional.ofNullable(ownerEntityName);
    }

    public StringFilter ownerEntityName() {
        if (ownerEntityName == null) {
            setOwnerEntityName(new StringFilter());
        }
        return ownerEntityName;
    }

    public void setOwnerEntityName(StringFilter ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
    }

    public LongFilter getOwnerEntityId() {
        return ownerEntityId;
    }

    public Optional<LongFilter> optionalOwnerEntityId() {
        return Optional.ofNullable(ownerEntityId);
    }

    public LongFilter ownerEntityId() {
        if (ownerEntityId == null) {
            setOwnerEntityId(new LongFilter());
        }
        return ownerEntityId;
    }

    public void setOwnerEntityId(LongFilter ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreateAt() {
        return Optional.ofNullable(createAt);
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            setCreateAt(new ZonedDateTimeFilter());
        }
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public StringFilter getSmartUrl() {
        return smartUrl;
    }

    public Optional<StringFilter> optionalSmartUrl() {
        return Optional.ofNullable(smartUrl);
    }

    public StringFilter smartUrl() {
        if (smartUrl == null) {
            setSmartUrl(new StringFilter());
        }
        return smartUrl;
    }

    public void setSmartUrl(StringFilter smartUrl) {
        this.smartUrl = smartUrl;
    }

    public StringFilter getMediumUrl() {
        return mediumUrl;
    }

    public Optional<StringFilter> optionalMediumUrl() {
        return Optional.ofNullable(mediumUrl);
    }

    public StringFilter mediumUrl() {
        if (mediumUrl == null) {
            setMediumUrl(new StringFilter());
        }
        return mediumUrl;
    }

    public void setMediumUrl(StringFilter mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public LongFilter getReferenceCount() {
        return referenceCount;
    }

    public Optional<LongFilter> optionalReferenceCount() {
        return Optional.ofNullable(referenceCount);
    }

    public LongFilter referenceCount() {
        if (referenceCount == null) {
            setReferenceCount(new LongFilter());
        }
        return referenceCount;
    }

    public void setReferenceCount(LongFilter referenceCount) {
        this.referenceCount = referenceCount;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public StringFilter getCategoryTitle() {
        return categoryTitle;
    }

    public Optional<StringFilter> optionalCategoryTitle() {
        return Optional.ofNullable(categoryTitle);
    }

    public StringFilter categoryTitle() {
        if (categoryTitle == null) {
            setCategoryTitle(new StringFilter());
        }
        return categoryTitle;
    }

    public void setCategoryTitle(StringFilter categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public void setAnd(UploadImageCriteria and) {
        this.and = and;
    }

    public UploadImageCriteria getAnd() {
        return and;
    }

    public UploadImageCriteria and() {
        if (and == null) {
            and = new UploadImageCriteria();
        }
        return and;
    }

    public void setOr(UploadImageCriteria or) {
        this.or = or;
    }

    public UploadImageCriteria getOr() {
        return or;
    }

    public UploadImageCriteria or() {
        if (or == null) {
            or = new UploadImageCriteria();
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
        final UploadImageCriteria that = (UploadImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(businessTitle, that.businessTitle) &&
            Objects.equals(businessDesc, that.businessDesc) &&
            Objects.equals(businessStatus, that.businessStatus) &&
            Objects.equals(url, that.url) &&
            Objects.equals(name, that.name) &&
            Objects.equals(ext, that.ext) &&
            Objects.equals(type, that.type) &&
            Objects.equals(path, that.path) &&
            Objects.equals(folder, that.folder) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(ownerEntityName, that.ownerEntityName) &&
            Objects.equals(ownerEntityId, that.ownerEntityId) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(smartUrl, that.smartUrl) &&
            Objects.equals(mediumUrl, that.mediumUrl) &&
            Objects.equals(referenceCount, that.referenceCount) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fullName,
            businessTitle,
            businessDesc,
            businessStatus,
            url,
            name,
            ext,
            type,
            path,
            folder,
            fileSize,
            ownerEntityName,
            ownerEntityId,
            createAt,
            smartUrl,
            mediumUrl,
            referenceCount,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            categoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UploadImageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalBusinessTitle().map(f -> "businessTitle=" + f + ", ").orElse("") +
            optionalBusinessDesc().map(f -> "businessDesc=" + f + ", ").orElse("") +
            optionalBusinessStatus().map(f -> "businessStatus=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalExt().map(f -> "ext=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPath().map(f -> "path=" + f + ", ").orElse("") +
            optionalFolder().map(f -> "folder=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalOwnerEntityName().map(f -> "ownerEntityName=" + f + ", ").orElse("") +
            optionalOwnerEntityId().map(f -> "ownerEntityId=" + f + ", ").orElse("") +
            optionalCreateAt().map(f -> "createAt=" + f + ", ").orElse("") +
            optionalSmartUrl().map(f -> "smartUrl=" + f + ", ").orElse("") +
            optionalMediumUrl().map(f -> "mediumUrl=" + f + ", ").orElse("") +
            optionalReferenceCount().map(f -> "referenceCount=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalCategoryTitle().map(f -> "categoryTitle=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
