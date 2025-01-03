package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.service.criteria.ApiPermissionCriteria;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.service.mapper.ApiPermissionMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
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
 * 用于对数据库中的{@link ApiPermission}实体执行复杂查询的Service。
 * 主要输入是一个{@link ApiPermissionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ApiPermissionDTO}列表{@link List} 或 {@link ApiPermissionDTO} 的分页列表 {@link Page}。
 */
@Service
public class ApiPermissionQueryService implements QueryService<ApiPermission> {

    private static final Logger log = LoggerFactory.getLogger(ApiPermissionQueryService.class);

    protected final ApiPermissionRepository apiPermissionRepository;

    protected final ApiPermissionMapper apiPermissionMapper;

    public ApiPermissionQueryService(ApiPermissionRepository apiPermissionRepository, ApiPermissionMapper apiPermissionMapper) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionMapper = apiPermissionMapper;
    }

    /**
     * Return a {@link List} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return apiPermissionMapper.toDto(Binder.joinQueryList(queryWrapper, ApiPermission.class));
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ApiPermissionDTO> findByCriteria(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ApiPermission.class, page).convert(apiPermission -> {
            Binder.bindRelations(apiPermission, new String[] { "children" });
            return apiPermissionMapper.toDto(apiPermission);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ApiPermissionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return apiPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the apiPermissions for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ApiPermissionDTO> findAllTop(ApiPermissionCriteria criteria, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, ApiPermission.class, page).convert(apiPermission -> {
            Binder.bindRelations(apiPermission, new String[] { "parent" });
            return apiPermissionMapper.toDto(apiPermission);
        });
    }

    /**
     * Get all the apiPermissions for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ApiPermissionDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ApiPermissions for parent is parentId");
        return apiPermissionRepository
            .selectList(new LambdaUpdateWrapper<ApiPermission>().eq(ApiPermission::getParentId, parentId))
            .stream()
            .map(apiPermission -> {
                Binder.bindRelations(apiPermission, new String[] { "parent" });
                return apiPermissionMapper.toDto(apiPermission);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return apiPermissionRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ApiPermissionCriteria criteria) {
        return apiPermissionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ApiPermission> createQueryWrapper(ApiPermissionCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            ApiPermissionCriteria keywordsCriteria = new ApiPermissionCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.serviceName().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            keywordsCriteria.method().setContains(keywords);
            keywordsCriteria.url().setContains(keywords);
            ApiPermissionCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<ApiPermission> queryWrapper = new DynamicJoinQueryWrapper<>(ApiPermissionCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, ApiPermission.class);
    }

    /**
     * Function to convert {@link ApiPermissionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ApiPermission> createQueryWrapperNoJoin(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, ApiPermission.class);
    }

    /**
     * Return a {@link IPage} of {@link ApiPermissionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ApiPermissionDTO> findByQueryWrapper(QueryWrapper<ApiPermission> queryWrapper, Page<ApiPermission> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return apiPermissionRepository.selectPage(page, queryWrapper).convert(apiPermissionMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ApiPermissionCriteria criteria) {
        QueryWrapper<ApiPermission> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.service_name", criteria.getServiceName());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.code", criteria.getCode());
        fieldNameMap.put("self.description", criteria.getDescription());
        fieldNameMap.put("self.type", criteria.getType());
        fieldNameMap.put("self.method", criteria.getMethod());
        fieldNameMap.put("self.url", criteria.getUrl());
        fieldNameMap.put("self.status", criteria.getStatus());
        fieldNameMap.put("self.parent_id", criteria.getParentId());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, ApiPermission.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
