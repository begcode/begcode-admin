package com.begcode.monolith.service.criteria;

import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.enumeration.TargetType;
import com.begcode.monolith.domain.enumeration.ViewPermissionType;
import com.diboot.core.binding.query.BindQuery;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.begcode.monolith.domain.ViewPermission} entity. This class is used
 * in {@link com.begcode.monolith.web.rest.ViewPermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /view-permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ViewPermissionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ViewPermissionType
     */
    public static class ViewPermissionTypeFilter extends Filter<ViewPermissionType> {

        public ViewPermissionTypeFilter() {}

        public ViewPermissionTypeFilter(ViewPermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ViewPermissionTypeFilter copy() {
            return new ViewPermissionTypeFilter(this);
        }
    }

    /**
     * Class for filtering TargetType
     */
    public static class TargetTypeFilter extends Filter<TargetType> {

        public TargetTypeFilter() {}

        public TargetTypeFilter(TargetTypeFilter filter) {
            super(filter);
        }

        @Override
        public TargetTypeFilter copy() {
            return new TargetTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    @BindQuery(column = "self.id")
    private LongFilter id;

    @BindQuery(column = "self.text")
    private StringFilter text;

    @BindQuery(column = "self.type")
    private ViewPermissionTypeFilter type;

    @BindQuery(column = "self.i_18_n")
    private StringFilter i18n;

    @BindQuery(column = "self.`group`")
    private BooleanFilter group;

    @BindQuery(column = "self.`link`")
    private StringFilter link;

    @BindQuery(column = "self.external_link")
    private StringFilter externalLink;

    @BindQuery(column = "self.target")
    private TargetTypeFilter target;

    @BindQuery(column = "self.icon")
    private StringFilter icon;

    @BindQuery(column = "self.disabled")
    private BooleanFilter disabled;

    @BindQuery(column = "self.hide")
    private BooleanFilter hide;

    @BindQuery(column = "self.hide_in_breadcrumb")
    private BooleanFilter hideInBreadcrumb;

    @BindQuery(column = "self.shortcut")
    private BooleanFilter shortcut;

    @BindQuery(column = "self.shortcut_root")
    private BooleanFilter shortcutRoot;

    @BindQuery(column = "self.reuse")
    private BooleanFilter reuse;

    @BindQuery(column = "self.code")
    private StringFilter code;

    @BindQuery(column = "self.description")
    private StringFilter description;

    @BindQuery(column = "self.`order`")
    private IntegerFilter order;

    @BindQuery(column = "self.api_permission_codes")
    private StringFilter apiPermissionCodes;

    @BindQuery(column = "self.component_file")
    private StringFilter componentFile;

    @BindQuery(column = "self.redirect")
    private StringFilter redirect;

    @BindQuery(entity = ViewPermission.class, column = "id", condition = "id=parent_id")
    private LongFilter childrenId;

    @BindQuery(entity = ViewPermission.class, column = "text", condition = "id=parent_id")
    private StringFilter childrenText;

    @BindQuery(entity = ViewPermission.class, column = "id", condition = "this.parent_id=id")
    private LongFilter parentId;

    @BindQuery(entity = ViewPermission.class, column = "text", condition = "this.parent_id=id")
    private StringFilter parentText;

    private LongFilter authoritiesId;

    private StringFilter authoritiesName;

    @BindQuery(ignore = true)
    private String jhiCommonSearchKeywords;

    @BindQuery(ignore = true)
    private Boolean useOr = false;

    @BindQuery(ignore = true)
    private ViewPermissionCriteria and;

    @BindQuery(ignore = true)
    private ViewPermissionCriteria or;

    private Boolean distinct;

    public ViewPermissionCriteria() {}

    public ViewPermissionCriteria(ViewPermissionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.text = other.optionalText().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(ViewPermissionTypeFilter::copy).orElse(null);
        this.i18n = other.optionalI18n().map(StringFilter::copy).orElse(null);
        this.group = other.optionalGroup().map(BooleanFilter::copy).orElse(null);
        this.link = other.optionalLink().map(StringFilter::copy).orElse(null);
        this.externalLink = other.optionalExternalLink().map(StringFilter::copy).orElse(null);
        this.target = other.optionalTarget().map(TargetTypeFilter::copy).orElse(null);
        this.icon = other.optionalIcon().map(StringFilter::copy).orElse(null);
        this.disabled = other.optionalDisabled().map(BooleanFilter::copy).orElse(null);
        this.hide = other.optionalHide().map(BooleanFilter::copy).orElse(null);
        this.hideInBreadcrumb = other.optionalHideInBreadcrumb().map(BooleanFilter::copy).orElse(null);
        this.shortcut = other.optionalShortcut().map(BooleanFilter::copy).orElse(null);
        this.shortcutRoot = other.optionalShortcutRoot().map(BooleanFilter::copy).orElse(null);
        this.reuse = other.optionalReuse().map(BooleanFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.order = other.optionalOrder().map(IntegerFilter::copy).orElse(null);
        this.apiPermissionCodes = other.optionalApiPermissionCodes().map(StringFilter::copy).orElse(null);
        this.componentFile = other.optionalComponentFile().map(StringFilter::copy).orElse(null);
        this.redirect = other.optionalRedirect().map(StringFilter::copy).orElse(null);
        this.childrenId = other.optionalChildrenId().map(LongFilter::copy).orElse(null);
        this.childrenText = other.optionalChildrenText().map(StringFilter::copy).orElse(null);
        this.parentId = other.optionalParentId().map(LongFilter::copy).orElse(null);
        this.parentText = other.optionalParentText().map(StringFilter::copy).orElse(null);
        this.authoritiesId = other.optionalAuthoritiesId().map(LongFilter::copy).orElse(null);
        this.authoritiesName = other.optionalAuthoritiesName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ViewPermissionCriteria copy() {
        return new ViewPermissionCriteria(this);
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

    public StringFilter getText() {
        return text;
    }

    public Optional<StringFilter> optionalText() {
        return Optional.ofNullable(text);
    }

    public StringFilter text() {
        if (text == null) {
            setText(new StringFilter());
        }
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public ViewPermissionTypeFilter getType() {
        return type;
    }

    public Optional<ViewPermissionTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public ViewPermissionTypeFilter type() {
        if (type == null) {
            setType(new ViewPermissionTypeFilter());
        }
        return type;
    }

    public void setType(ViewPermissionTypeFilter type) {
        this.type = type;
    }

    public StringFilter getI18n() {
        return i18n;
    }

    public Optional<StringFilter> optionalI18n() {
        return Optional.ofNullable(i18n);
    }

    public StringFilter i18n() {
        if (i18n == null) {
            setI18n(new StringFilter());
        }
        return i18n;
    }

    public void setI18n(StringFilter i18n) {
        this.i18n = i18n;
    }

    public BooleanFilter getGroup() {
        return group;
    }

    public Optional<BooleanFilter> optionalGroup() {
        return Optional.ofNullable(group);
    }

    public BooleanFilter group() {
        if (group == null) {
            setGroup(new BooleanFilter());
        }
        return group;
    }

    public void setGroup(BooleanFilter group) {
        this.group = group;
    }

    public StringFilter getLink() {
        return link;
    }

    public Optional<StringFilter> optionalLink() {
        return Optional.ofNullable(link);
    }

    public StringFilter link() {
        if (link == null) {
            setLink(new StringFilter());
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public StringFilter getExternalLink() {
        return externalLink;
    }

    public Optional<StringFilter> optionalExternalLink() {
        return Optional.ofNullable(externalLink);
    }

    public StringFilter externalLink() {
        if (externalLink == null) {
            setExternalLink(new StringFilter());
        }
        return externalLink;
    }

    public void setExternalLink(StringFilter externalLink) {
        this.externalLink = externalLink;
    }

    public TargetTypeFilter getTarget() {
        return target;
    }

    public Optional<TargetTypeFilter> optionalTarget() {
        return Optional.ofNullable(target);
    }

    public TargetTypeFilter target() {
        if (target == null) {
            setTarget(new TargetTypeFilter());
        }
        return target;
    }

    public void setTarget(TargetTypeFilter target) {
        this.target = target;
    }

    public StringFilter getIcon() {
        return icon;
    }

    public Optional<StringFilter> optionalIcon() {
        return Optional.ofNullable(icon);
    }

    public StringFilter icon() {
        if (icon == null) {
            setIcon(new StringFilter());
        }
        return icon;
    }

    public void setIcon(StringFilter icon) {
        this.icon = icon;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public Optional<BooleanFilter> optionalDisabled() {
        return Optional.ofNullable(disabled);
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            setDisabled(new BooleanFilter());
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public BooleanFilter getHide() {
        return hide;
    }

    public Optional<BooleanFilter> optionalHide() {
        return Optional.ofNullable(hide);
    }

    public BooleanFilter hide() {
        if (hide == null) {
            setHide(new BooleanFilter());
        }
        return hide;
    }

    public void setHide(BooleanFilter hide) {
        this.hide = hide;
    }

    public BooleanFilter getHideInBreadcrumb() {
        return hideInBreadcrumb;
    }

    public Optional<BooleanFilter> optionalHideInBreadcrumb() {
        return Optional.ofNullable(hideInBreadcrumb);
    }

    public BooleanFilter hideInBreadcrumb() {
        if (hideInBreadcrumb == null) {
            setHideInBreadcrumb(new BooleanFilter());
        }
        return hideInBreadcrumb;
    }

    public void setHideInBreadcrumb(BooleanFilter hideInBreadcrumb) {
        this.hideInBreadcrumb = hideInBreadcrumb;
    }

    public BooleanFilter getShortcut() {
        return shortcut;
    }

    public Optional<BooleanFilter> optionalShortcut() {
        return Optional.ofNullable(shortcut);
    }

    public BooleanFilter shortcut() {
        if (shortcut == null) {
            setShortcut(new BooleanFilter());
        }
        return shortcut;
    }

    public void setShortcut(BooleanFilter shortcut) {
        this.shortcut = shortcut;
    }

    public BooleanFilter getShortcutRoot() {
        return shortcutRoot;
    }

    public Optional<BooleanFilter> optionalShortcutRoot() {
        return Optional.ofNullable(shortcutRoot);
    }

    public BooleanFilter shortcutRoot() {
        if (shortcutRoot == null) {
            setShortcutRoot(new BooleanFilter());
        }
        return shortcutRoot;
    }

    public void setShortcutRoot(BooleanFilter shortcutRoot) {
        this.shortcutRoot = shortcutRoot;
    }

    public BooleanFilter getReuse() {
        return reuse;
    }

    public Optional<BooleanFilter> optionalReuse() {
        return Optional.ofNullable(reuse);
    }

    public BooleanFilter reuse() {
        if (reuse == null) {
            setReuse(new BooleanFilter());
        }
        return reuse;
    }

    public void setReuse(BooleanFilter reuse) {
        this.reuse = reuse;
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

    public IntegerFilter getOrder() {
        return order;
    }

    public Optional<IntegerFilter> optionalOrder() {
        return Optional.ofNullable(order);
    }

    public IntegerFilter order() {
        if (order == null) {
            setOrder(new IntegerFilter());
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public StringFilter getApiPermissionCodes() {
        return apiPermissionCodes;
    }

    public Optional<StringFilter> optionalApiPermissionCodes() {
        return Optional.ofNullable(apiPermissionCodes);
    }

    public StringFilter apiPermissionCodes() {
        if (apiPermissionCodes == null) {
            setApiPermissionCodes(new StringFilter());
        }
        return apiPermissionCodes;
    }

    public void setApiPermissionCodes(StringFilter apiPermissionCodes) {
        this.apiPermissionCodes = apiPermissionCodes;
    }

    public StringFilter getComponentFile() {
        return componentFile;
    }

    public Optional<StringFilter> optionalComponentFile() {
        return Optional.ofNullable(componentFile);
    }

    public StringFilter componentFile() {
        if (componentFile == null) {
            setComponentFile(new StringFilter());
        }
        return componentFile;
    }

    public void setComponentFile(StringFilter componentFile) {
        this.componentFile = componentFile;
    }

    public StringFilter getRedirect() {
        return redirect;
    }

    public Optional<StringFilter> optionalRedirect() {
        return Optional.ofNullable(redirect);
    }

    public StringFilter redirect() {
        if (redirect == null) {
            setRedirect(new StringFilter());
        }
        return redirect;
    }

    public void setRedirect(StringFilter redirect) {
        this.redirect = redirect;
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

    public StringFilter getChildrenText() {
        return childrenText;
    }

    public Optional<StringFilter> optionalChildrenText() {
        return Optional.ofNullable(childrenText);
    }

    public StringFilter childrenText() {
        if (childrenText == null) {
            setChildrenText(new StringFilter());
        }
        return childrenText;
    }

    public void setChildrenText(StringFilter childrenText) {
        this.childrenText = childrenText;
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

    public StringFilter getParentText() {
        return parentText;
    }

    public Optional<StringFilter> optionalParentText() {
        return Optional.ofNullable(parentText);
    }

    public StringFilter parentText() {
        if (parentText == null) {
            setParentText(new StringFilter());
        }
        return parentText;
    }

    public void setParentText(StringFilter parentText) {
        this.parentText = parentText;
    }

    public LongFilter getAuthoritiesId() {
        return authoritiesId;
    }

    public Optional<LongFilter> optionalAuthoritiesId() {
        return Optional.ofNullable(authoritiesId);
    }

    public LongFilter authoritiesId() {
        if (authoritiesId == null) {
            setAuthoritiesId(new LongFilter());
        }
        return authoritiesId;
    }

    public void setAuthoritiesId(LongFilter authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public StringFilter getAuthoritiesName() {
        return authoritiesName;
    }

    public Optional<StringFilter> optionalAuthoritiesName() {
        return Optional.ofNullable(authoritiesName);
    }

    public StringFilter authoritiesName() {
        if (authoritiesName == null) {
            setAuthoritiesName(new StringFilter());
        }
        return authoritiesName;
    }

    public void setAuthoritiesName(StringFilter authoritiesName) {
        this.authoritiesName = authoritiesName;
    }

    public void setAnd(ViewPermissionCriteria and) {
        this.and = and;
    }

    public ViewPermissionCriteria getAnd() {
        return and;
    }

    public ViewPermissionCriteria and() {
        if (and == null) {
            and = new ViewPermissionCriteria();
        }
        return and;
    }

    public void setOr(ViewPermissionCriteria or) {
        this.or = or;
    }

    public ViewPermissionCriteria getOr() {
        return or;
    }

    public ViewPermissionCriteria or() {
        if (or == null) {
            or = new ViewPermissionCriteria();
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
        final ViewPermissionCriteria that = (ViewPermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(type, that.type) &&
            Objects.equals(i18n, that.i18n) &&
            Objects.equals(group, that.group) &&
            Objects.equals(link, that.link) &&
            Objects.equals(externalLink, that.externalLink) &&
            Objects.equals(target, that.target) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(hide, that.hide) &&
            Objects.equals(hideInBreadcrumb, that.hideInBreadcrumb) &&
            Objects.equals(shortcut, that.shortcut) &&
            Objects.equals(shortcutRoot, that.shortcutRoot) &&
            Objects.equals(reuse, that.reuse) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(order, that.order) &&
            Objects.equals(apiPermissionCodes, that.apiPermissionCodes) &&
            Objects.equals(componentFile, that.componentFile) &&
            Objects.equals(redirect, that.redirect) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(authoritiesId, that.authoritiesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            text,
            type,
            i18n,
            group,
            link,
            externalLink,
            target,
            icon,
            disabled,
            hide,
            hideInBreadcrumb,
            shortcut,
            shortcutRoot,
            reuse,
            code,
            description,
            order,
            apiPermissionCodes,
            componentFile,
            redirect,
            childrenId,
            parentId,
            authoritiesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViewPermissionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalText().map(f -> "text=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalI18n().map(f -> "i18n=" + f + ", ").orElse("") +
            optionalGroup().map(f -> "group=" + f + ", ").orElse("") +
            optionalLink().map(f -> "link=" + f + ", ").orElse("") +
            optionalExternalLink().map(f -> "externalLink=" + f + ", ").orElse("") +
            optionalTarget().map(f -> "target=" + f + ", ").orElse("") +
            optionalIcon().map(f -> "icon=" + f + ", ").orElse("") +
            optionalDisabled().map(f -> "disabled=" + f + ", ").orElse("") +
            optionalHide().map(f -> "hide=" + f + ", ").orElse("") +
            optionalHideInBreadcrumb().map(f -> "hideInBreadcrumb=" + f + ", ").orElse("") +
            optionalShortcut().map(f -> "shortcut=" + f + ", ").orElse("") +
            optionalShortcutRoot().map(f -> "shortcutRoot=" + f + ", ").orElse("") +
            optionalReuse().map(f -> "reuse=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalOrder().map(f -> "order=" + f + ", ").orElse("") +
            optionalApiPermissionCodes().map(f -> "apiPermissionCodes=" + f + ", ").orElse("") +
            optionalComponentFile().map(f -> "componentFile=" + f + ", ").orElse("") +
            optionalRedirect().map(f -> "redirect=" + f + ", ").orElse("") +
            optionalChildrenId().map(f -> "childrenId=" + f + ", ").orElse("") +
            optionalChildrenText().map(f -> "childrenText=" + f + ", ").orElse("") +
            optionalParentId().map(f -> "parentId=" + f + ", ").orElse("") +
            optionalParentText().map(f -> "parentText=" + f + ", ").orElse("") +
            optionalAuthoritiesId().map(f -> "authoritiesId=" + f + ", ").orElse("") +
            optionalAuthoritiesName().map(f -> "authoritiesName=" + f + ", ").orElse("") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
