package com.begcode.monolith.oss.service.criteria;

import com.begcode.monolith.domain.enumeration.OssProvider;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.oss.domain.OssConfig} entity. This class is used
 * in {@link com.begcode.monolith.oss.web.rest.OssConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /oss-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OssConfigCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OssProvider
     */
    public static class OssProviderFilter extends Filter<OssProvider> {

        public OssProviderFilter() {}

        public OssProviderFilter(OssProviderFilter filter) {
            super(filter);
        }

        @Override
        public OssProviderFilter copy() {
            return new OssProviderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.provider")
    private OssProviderFilter provider;

    @BindQuery(column = "self.platform")
    private StringFilter platform;

    @BindQuery(column = "self.enabled")
    private BooleanFilter enabled;

    @BindQuery(column = "self.remark")
    private StringFilter remark;

    @BindQuery(column = "self.config_data")
    private StringFilter configData;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private OssConfigCriteria and;

    @BindQuery(ignore = true)
    private OssConfigCriteria or;

    private Boolean distinct;

    public OssConfigCriteria() {}

    public OssConfigCriteria(OssConfigCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.provider = other.optionalProvider().map(OssProviderFilter::copy).orElse(null);
        this.platform = other.optionalPlatform().map(StringFilter::copy).orElse(null);
        this.enabled = other.optionalEnabled().map(BooleanFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.configData = other.optionalConfigData().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OssConfigCriteria copy() {
        return new OssConfigCriteria(this);
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

    public OssProviderFilter getProvider() {
        return provider;
    }

    public Optional<OssProviderFilter> optionalProvider() {
        return Optional.ofNullable(provider);
    }

    public OssProviderFilter provider() {
        if (provider == null) {
            setProvider(new OssProviderFilter());
        }
        return provider;
    }

    public void setProvider(OssProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getPlatform() {
        return platform;
    }

    public Optional<StringFilter> optionalPlatform() {
        return Optional.ofNullable(platform);
    }

    public StringFilter platform() {
        if (platform == null) {
            setPlatform(new StringFilter());
        }
        return platform;
    }

    public void setPlatform(StringFilter platform) {
        this.platform = platform;
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

    public void setAnd(OssConfigCriteria and) {
        this.and = and;
    }

    public OssConfigCriteria getAnd() {
        return and;
    }

    public OssConfigCriteria and() {
        if (and == null) {
            and = new OssConfigCriteria();
        }
        return and;
    }

    public void setOr(OssConfigCriteria or) {
        this.or = or;
    }

    public OssConfigCriteria getOr() {
        return or;
    }

    public OssConfigCriteria or() {
        if (or == null) {
            or = new OssConfigCriteria();
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
        final OssConfigCriteria that = (OssConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(platform, that.platform) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(configData, that.configData) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provider, platform, enabled, remark, configData, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OssConfigCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProvider().map(f -> "provider=" + f + ", ").orElse("") +
            optionalPlatform().map(f -> "platform=" + f + ", ").orElse("") +
            optionalEnabled().map(f -> "enabled=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
            optionalConfigData().map(f -> "configData=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
