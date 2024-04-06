package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.SmsTemplate} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.SmsTemplateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sms-templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsTemplateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MessageSendType
     */
    public static class MessageSendTypeFilter extends Filter<MessageSendType> {

        public MessageSendTypeFilter() {}

        public MessageSendTypeFilter(MessageSendTypeFilter filter) {
            super(filter);
        }

        @Override
        public MessageSendTypeFilter copy() {
            return new MessageSendTypeFilter(this);
        }
    }

    /**
     * Class for filtering SmsTemplateType
     */
    public static class SmsTemplateTypeFilter extends Filter<SmsTemplateType> {

        public SmsTemplateTypeFilter() {}

        public SmsTemplateTypeFilter(SmsTemplateTypeFilter filter) {
            super(filter);
        }

        @Override
        public SmsTemplateTypeFilter copy() {
            return new SmsTemplateTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.name")
    private StringFilter name;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.send_type")
    private MessageSendTypeFilter sendType;

    @BindQuery(column = "self.content")
    private StringFilter content;

    @BindQuery(column = "self.test_json")
    private StringFilter testJson;

    @BindQuery(column = "self.type")
    private SmsTemplateTypeFilter type;

    @BindQuery(column = "self.remark")
    private StringFilter remark;

    @BindQuery(column = "self.enabled")
    private BooleanFilter enabled;

    @BindQuery(column = "self.created_by")
    private LongFilter createdBy;

    @BindQuery(column = "self.created_date")
    private InstantFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private InstantFilter lastModifiedDate;

    @BindQuery(entity = SmsSupplier.class, column = "id", condition = "this.supplier_id=id")
    private LongFilter supplierId;

    @BindQuery(entity = SmsSupplier.class, column = "sign_name", condition = "this.supplier_id=id")
    private StringFilter supplierSignName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SmsTemplateCriteria and;

    @BindQuery(ignore = true)
    private SmsTemplateCriteria or;

    private Boolean distinct;

    public SmsTemplateCriteria() {}

    public SmsTemplateCriteria(SmsTemplateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.sendType = other.optionalSendType().map(MessageSendTypeFilter::copy).orElse(null);
        this.content = other.optionalContent().map(StringFilter::copy).orElse(null);
        this.testJson = other.optionalTestJson().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(SmsTemplateTypeFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.enabled = other.optionalEnabled().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(LongFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(LongFilter::copy).orElse(null);
        this.supplierSignName = other.optionalSupplierSignName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SmsTemplateCriteria copy() {
        return new SmsTemplateCriteria(this);
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

    public MessageSendTypeFilter getSendType() {
        return sendType;
    }

    public Optional<MessageSendTypeFilter> optionalSendType() {
        return Optional.ofNullable(sendType);
    }

    public MessageSendTypeFilter sendType() {
        if (sendType == null) {
            setSendType(new MessageSendTypeFilter());
        }
        return sendType;
    }

    public void setSendType(MessageSendTypeFilter sendType) {
        this.sendType = sendType;
    }

    public StringFilter getContent() {
        return content;
    }

    public Optional<StringFilter> optionalContent() {
        return Optional.ofNullable(content);
    }

    public StringFilter content() {
        if (content == null) {
            setContent(new StringFilter());
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public StringFilter getTestJson() {
        return testJson;
    }

    public Optional<StringFilter> optionalTestJson() {
        return Optional.ofNullable(testJson);
    }

    public StringFilter testJson() {
        if (testJson == null) {
            setTestJson(new StringFilter());
        }
        return testJson;
    }

    public void setTestJson(StringFilter testJson) {
        this.testJson = testJson;
    }

    public SmsTemplateTypeFilter getType() {
        return type;
    }

    public Optional<SmsTemplateTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public SmsTemplateTypeFilter type() {
        if (type == null) {
            setType(new SmsTemplateTypeFilter());
        }
        return type;
    }

    public void setType(SmsTemplateTypeFilter type) {
        this.type = type;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public Optional<StringFilter> optionalRemark() {
        return Optional.ofNullable(remark);
    }

    public StringFilter remark() {
        if (remark == null) {
            setRemark(new StringFilter());
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public Optional<BooleanFilter> optionalEnabled() {
        return Optional.ofNullable(enabled);
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            setEnabled(new BooleanFilter());
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
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

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public Optional<LongFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new LongFilter());
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public StringFilter getSupplierSignName() {
        return supplierSignName;
    }

    public Optional<StringFilter> optionalSupplierSignName() {
        return Optional.ofNullable(supplierSignName);
    }

    public StringFilter supplierSignName() {
        if (supplierSignName == null) {
            setSupplierSignName(new StringFilter());
        }
        return supplierSignName;
    }

    public void setSupplierSignName(StringFilter supplierSignName) {
        this.supplierSignName = supplierSignName;
    }

    public void setAnd(SmsTemplateCriteria and) {
        this.and = and;
    }

    public SmsTemplateCriteria getAnd() {
        return and;
    }

    public SmsTemplateCriteria and() {
        if (and == null) {
            and = new SmsTemplateCriteria();
        }
        return and;
    }

    public void setOr(SmsTemplateCriteria or) {
        this.or = or;
    }

    public SmsTemplateCriteria getOr() {
        return or;
    }

    public SmsTemplateCriteria or() {
        if (or == null) {
            or = new SmsTemplateCriteria();
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
        final SmsTemplateCriteria that = (SmsTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(sendType, that.sendType) &&
            Objects.equals(content, that.content) &&
            Objects.equals(testJson, that.testJson) &&
            Objects.equals(type, that.type) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            sendType,
            content,
            testJson,
            type,
            remark,
            enabled,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            supplierId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalSendType().map(f -> "sendType=" + f + ", ").orElse("") +
            optionalContent().map(f -> "content=" + f + ", ").orElse("") +
            optionalTestJson().map(f -> "testJson=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
            optionalEnabled().map(f -> "enabled=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalSupplierSignName().map(f -> "supplierSignName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
