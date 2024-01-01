package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.SmsProvider;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.system.domain.SmsSupplier} entity. This class is used
 * in {@link com.begcode.monolith.system.web.rest.SmsSupplierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sms-suppliers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsSupplierCriteria implements Serializable, Criteria {

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private SmsSupplierCriteria and;

    @BindQuery(ignore = true)
    private SmsSupplierCriteria or;

    /**
     * Class for filtering SmsProvider
     */
    public static class SmsProviderFilter extends Filter<SmsProvider> {

        public SmsProviderFilter() {}

        public SmsProviderFilter(String value) {
            SmsProvider enumValue = SmsProvider.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = SmsProvider.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public SmsProviderFilter(SmsProviderFilter filter) {
            super(filter);
        }

        @Override
        public SmsProviderFilter copy() {
            return new SmsProviderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.provider")
    private SmsProviderFilter provider;

    @BindQuery(column = "self.config_data")
    private StringFilter configData;

    @BindQuery(column = "self.sign_name")
    private StringFilter signName;

    @BindQuery(column = "self.remark")
    private StringFilter remark;

    @BindQuery(column = "self.enabled")
    private BooleanFilter enabled;

    @BindQuery(ignore = true)
    private Boolean distinct;

    public SmsSupplierCriteria() {}

    public SmsSupplierCriteria(SmsSupplierCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provider = other.provider == null ? null : other.provider.copy();
        this.configData = other.configData == null ? null : other.configData.copy();
        this.signName = other.signName == null ? null : other.signName.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SmsSupplierCriteria copy() {
        return new SmsSupplierCriteria(this);
    }

    public void setAnd(SmsSupplierCriteria and) {
        this.and = and;
    }

    public SmsSupplierCriteria getAnd() {
        return and;
    }

    public SmsSupplierCriteria and() {
        if (and == null) {
            and = new SmsSupplierCriteria();
        }
        return and;
    }

    public void setOr(SmsSupplierCriteria or) {
        this.or = or;
    }

    public SmsSupplierCriteria getOr() {
        return or;
    }

    public SmsSupplierCriteria or() {
        if (or == null) {
            or = new SmsSupplierCriteria();
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

    public SmsProviderFilter getProvider() {
        return provider;
    }

    public SmsProviderFilter provider() {
        if (provider == null) {
            provider = new SmsProviderFilter();
        }
        return provider;
    }

    public void setProvider(SmsProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getConfigData() {
        return configData;
    }

    public StringFilter configData() {
        if (configData == null) {
            configData = new StringFilter();
        }
        return configData;
    }

    public void setConfigData(StringFilter configData) {
        this.configData = configData;
    }

    public StringFilter getSignName() {
        return signName;
    }

    public StringFilter signName() {
        if (signName == null) {
            signName = new StringFilter();
        }
        return signName;
    }

    public void setSignName(StringFilter signName) {
        this.signName = signName;
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
        final SmsSupplierCriteria that = (SmsSupplierCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(configData, that.configData) &&
            Objects.equals(signName, that.signName) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provider, configData, signName, remark, enabled, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsSupplierCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (provider != null ? "provider=" + provider + ", " : "") +
            (configData != null ? "configData=" + configData + ", " : "") +
            (signName != null ? "signName=" + signName + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
