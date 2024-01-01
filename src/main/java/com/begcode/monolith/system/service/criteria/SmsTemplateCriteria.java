package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private SmsTemplateCriteria and;

    @BindQuery(ignore = true)
    private SmsTemplateCriteria or;

    /**
     * Class for filtering MessageSendType
     */
    public static class MessageSendTypeFilter extends Filter<MessageSendType> {

        public MessageSendTypeFilter() {}

        public MessageSendTypeFilter(String value) {
            MessageSendType enumValue = MessageSendType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = MessageSendType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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

        public SmsTemplateTypeFilter(String value) {
            SmsTemplateType enumValue = SmsTemplateType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = SmsTemplateType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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
    private ZonedDateTimeFilter createdDate;

    @BindQuery(column = "self.last_modified_by")
    private LongFilter lastModifiedBy;

    @BindQuery(column = "self.last_modified_date")
    private ZonedDateTimeFilter lastModifiedDate;

    @BindQuery(column = "self.supplier_id")
    private LongFilter supplierId;

    @BindQuery(entity = SmsSupplier.class, column = "sign_name", condition = "this.supplier_id=id")
    private StringFilter supplierSignName;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SmsTemplateCriteria() {}

    public SmsTemplateCriteria(SmsTemplateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.sendType = other.sendType == null ? null : other.sendType.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.testJson = other.testJson == null ? null : other.testJson.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.supplierSignName = other.supplierSignName == null ? null : other.supplierSignName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SmsTemplateCriteria copy() {
        return new SmsTemplateCriteria(this);
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

    public MessageSendTypeFilter getSendType() {
        return sendType;
    }

    public MessageSendTypeFilter sendType() {
        if (sendType == null) {
            sendType = new MessageSendTypeFilter();
        }
        return sendType;
    }

    public void setSendType(MessageSendTypeFilter sendType) {
        this.sendType = sendType;
    }

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public StringFilter getTestJson() {
        return testJson;
    }

    public StringFilter testJson() {
        if (testJson == null) {
            testJson = new StringFilter();
        }
        return testJson;
    }

    public void setTestJson(StringFilter testJson) {
        this.testJson = testJson;
    }

    public SmsTemplateTypeFilter getType() {
        return type;
    }

    public SmsTemplateTypeFilter type() {
        if (type == null) {
            type = new SmsTemplateTypeFilter();
        }
        return type;
    }

    public void setType(SmsTemplateTypeFilter type) {
        this.type = type;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
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

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
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

    public ZonedDateTimeFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ZonedDateTimeFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new ZonedDateTimeFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTimeFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            supplierId = new LongFilter();
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
    }

    public StringFilter getSupplierSignName() {
        return supplierSignName;
    }

    public StringFilter supplierSignName() {
        if (supplierSignName == null) {
            supplierSignName = new StringFilter();
        }
        return supplierSignName;
    }

    public void setSupplierSignName(StringFilter supplierSignName) {
        this.supplierSignName = supplierSignName;
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
            Objects.equals(supplierSignName, that.supplierSignName) &&
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
            supplierSignName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsTemplateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (sendType != null ? "sendType=" + sendType + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (testJson != null ? "testJson=" + testJson + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (supplierSignName != null ? "supplierSignName=" + supplierSignName + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
