package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.criteria.ViewPermissionCriteria;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import com.begcode.monolith.service.mapper.ViewPermissionMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.BindQuery;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link ViewPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ViewPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ViewPermissionDTO}列表{@link List} 或 {@link ViewPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
public class ViewPermissionQueryService implements QueryService<ViewPermission> {

    private static final Logger log = LoggerFactory.getLogger(ViewPermissionQueryService.class);

    protected final ViewPermissionRepository viewPermissionRepository;

    protected final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionQueryService(ViewPermissionRepository viewPermissionRepository, ViewPermissionMapper viewPermissionMapper) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return viewPermissionMapper.toDto(Binder.joinQueryList(queryWrapper, ViewPermission.class));
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ViewPermissionDTO> findByCriteria(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ViewPermission.class, page).convert(viewPermission -> {
            Binder.bindRelations(viewPermission, new String[] { "children", "authorities" });
            return viewPermissionMapper.toDto(viewPermission);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ViewPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return viewPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the viewPermissions for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ViewPermissionDTO> findAllTop(ViewPermissionCriteria criteria, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ViewPermission.class, page).convert(viewPermission -> {
            Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
            return viewPermissionMapper.toDto(viewPermission);
        });
    }

    /**
     * Get all the viewPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ViewPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ViewPermissions for parent is parentId");
        return viewPermissionRepository
            .selectList(new LambdaUpdateWrapper<ViewPermission>().eq(ViewPermission::getParentId, parentId))
            .stream()
            .map(viewPermission -> {
                Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                return viewPermissionMapper.toDto(viewPermission);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return viewPermissionRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ViewPermissionCriteria criteria) {
        return viewPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ViewPermission> createQueryWrapper(ViewPermissionCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            ViewPermissionCriteria keywordsCriteria = new ViewPermissionCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.order().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.text().setContains(keywords);
            keywordsCriteria.localeKey().setContains(keywords);
            keywordsCriteria.link().setContains(keywords);
            keywordsCriteria.externalLink().setContains(keywords);
            keywordsCriteria.icon().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            keywordsCriteria.apiPermissionCodes().setContains(keywords);
            keywordsCriteria.componentFile().setContains(keywords);
            keywordsCriteria.redirect().setContains(keywords);
            ViewPermissionCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<ViewPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ViewPermissionCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, ViewPermission.class);
    }

    /**
     * Function to convert {@link ViewPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ViewPermission> createQueryWrapperNoJoin(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, ViewPermission.class);
    }

    /**
     * Return a {@link IPage} of {@link ViewPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ViewPermissionDTO> findByQueryWrapper(QueryWrapper<ViewPermission> queryWrapper, Page<ViewPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return viewPermissionRepository.selectPage(page, queryWrapper).convert(viewPermissionMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ViewPermissionCriteria criteria) {
        QueryWrapper<ViewPermission> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Map<String, Object>> fieldNameMap = CriteriaUtil.getNonIgnoredAndNonNullFields(criteria);
        fieldNameMap.forEach((key, value) -> {
            // 获得value 对象BindQuery 注解的column属性值
            Filter<?> filter = (Filter<?>) value.get("value");
            BindQuery bindQuery = (BindQuery) value.get("bindQuery");
            String column = bindQuery.column();
            if (column.startsWith("self.")) {
                getAggregateAndGroupBy(filter, column, "", selectFields, groupByFields);
            } else {
                if (queryWrapper instanceof DynamicJoinQueryWrapper) {
                    DynamicJoinQueryWrapper<ViewPermissionCriteria, ViewPermission> dynamicQuery = (DynamicJoinQueryWrapper<
                            ViewPermissionCriteria,
                            ViewPermission
                        >) queryWrapper;
                    dynamicQuery
                        .getAnnoJoiners()
                        .stream()
                        .filter(annoJoiner -> annoJoiner.getColumnName().equals(column) && annoJoiner.getFieldName().equals(key))
                        .findFirst()
                        .ifPresent(annoJoiner -> {
                            String alias = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, annoJoiner.getFieldName());
                            getAggregateAndGroupBy(filter, annoJoiner.getAlias() + "." + column, alias, selectFields, groupByFields);
                        });
                }
            }
        });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, ViewPermission.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
