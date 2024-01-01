package com.begcode.monolith.oss.service.criteria;

import com.begcode.monolith.domain.enumeration.OssProvider;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
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

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr;

    @BindQuery(ignore = true)
    private OssConfigCriteria and;

    @BindQuery(ignore = true)
    private OssConfigCriteria or;

    /**
     * Class for filtering OssProvider
     */
    public static class OssProviderFilter extends Filter<OssProvider> {

        public OssProviderFilter() {}

        public OssProviderFilter(String value) {
            OssProvider enumValue = OssProvider.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = OssProvider.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

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
    private Boolean distinct;

    public OssConfigCriteria() {}

    public OssConfigCriteria(OssConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provider = other.provider == null ? null : other.provider.copy();
        this.platform = other.platform == null ? null : other.platform.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.configData = other.configData == null ? null : other.configData.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OssConfigCriteria copy() {
        return new OssConfigCriteria(this);
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

    public OssProviderFilter getProvider() {
        return provider;
    }

    public OssProviderFilter provider() {
        if (provider == null) {
            provider = new OssProviderFilter();
        }
        return provider;
    }

    public void setProvider(OssProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getPlatform() {
        return platform;
    }

    public StringFilter platform() {
        if (platform == null) {
            platform = new StringFilter();
        }
        return platform;
    }

    public void setPlatform(StringFilter platform) {
        this.platform = platform;
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
            (id != null ? "id=" + id + ", " : "") +
            (provider != null ? "provider=" + provider + ", " : "") +
            (platform != null ? "platform=" + platform + ", " : "") +
            (enabled != null ? "enabled=" + enabled + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
            (configData != null ? "configData=" + configData + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
