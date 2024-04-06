package com.begcode.monolith.system.service.criteria;

import com.begcode.monolith.domain.enumeration.SmsProvider;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * Class for filtering SmsProvider
     */
    public static class SmsProviderFilter extends Filter<SmsProvider> {

        public SmsProviderFilter() {}

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
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private SmsSupplierCriteria and;

    @BindQuery(ignore = true)
    private SmsSupplierCriteria or;

    private Boolean distinct;

    public SmsSupplierCriteria() {}

    public SmsSupplierCriteria(SmsSupplierCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.provider = other.optionalProvider().map(SmsProviderFilter::copy).orElse(null);
        this.configData = other.optionalConfigData().map(StringFilter::copy).orElse(null);
        this.signName = other.optionalSignName().map(StringFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.enabled = other.optionalEnabled().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SmsSupplierCriteria copy() {
        return new SmsSupplierCriteria(this);
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

    public SmsProviderFilter getProvider() {
        return provider;
    }

    public Optional<SmsProviderFilter> optionalProvider() {
        return Optional.ofNullable(provider);
    }

    public SmsProviderFilter provider() {
        if (provider == null) {
            setProvider(new SmsProviderFilter());
        }
        return provider;
    }

    public void setProvider(SmsProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getConfigData() {
        return configData;
    }

    public Optional<StringFilter> optionalConfigData() {
        return Optional.ofNullable(configData);
    }

    public StringFilter configData() {
        if (configData == null) {
            setConfigData(new StringFilter());
        }
        return configData;
    }

    public void setConfigData(StringFilter configData) {
        this.configData = configData;
    }

    public StringFilter getSignName() {
        return signName;
    }

    public Optional<StringFilter> optionalSignName() {
        return Optional.ofNullable(signName);
    }

    public StringFilter signName() {
        if (signName == null) {
            setSignName(new StringFilter());
        }
        return signName;
    }

    public void setSignName(StringFilter signName) {
        this.signName = signName;
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProvider().map(f -> "provider=" + f + ", ").orElse("") +
            optionalConfigData().map(f -> "configData=" + f + ", ").orElse("") +
            optionalSignName().map(f -> "signName=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
            optionalEnabled().map(f -> "enabled=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
