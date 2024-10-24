package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.SystemConfig;
import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.criteria.SystemConfigCriteria;
import com.begcode.monolith.settings.service.dto.SystemConfigDTO;
import com.begcode.monolith.settings.service.mapper.SystemConfigMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link SystemConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SystemConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SystemConfigDTO}列表{@link List} 或 {@link SystemConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class SystemConfigQueryService implements QueryService<SystemConfig> {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigQueryService.class);

    protected final SystemConfigRepository systemConfigRepository;

    protected final SystemConfigMapper systemConfigMapper;

    public SystemConfigQueryService(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SystemConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SystemConfig> queryWrapper = createQueryWrapper(criteria);
        return systemConfigMapper.toDto(Binder.joinQueryList(queryWrapper, SystemConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link SystemConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria, Page<SystemConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SystemConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SystemConfig.class, page).convert(systemConfig -> {
            Binder.bindRelations(systemConfig, new String[] {});
            return systemConfigMapper.toDto(systemConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SystemConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return systemConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SystemConfigCriteria criteria) {
        return (List<T>) systemConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SystemConfigCriteria criteria) {
        return systemConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SystemConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SystemConfig> createQueryWrapper(SystemConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SystemConfigCriteria keywordsCriteria = new SystemConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.categoryName().setContains(keywords);
            keywordsCriteria.categoryKey().setContains(keywords);
            SystemConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SystemConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SystemConfigCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SystemConfig.class);
    }

    /**
     * Function to convert {@link SystemConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SystemConfig> createQueryWrapperNoJoin(SystemConfigCriteria criteria) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SystemConfig.class);
    }

    /**
     * Return a {@link IPage} of {@link SystemConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SystemConfigDTO> findByQueryWrapper(QueryWrapper<SystemConfig> queryWrapper, Page<SystemConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return systemConfigRepository.selectPage(page, queryWrapper).convert(systemConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SystemConfigCriteria criteria) {
        QueryWrapper<SystemConfig> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.category_name", criteria.getCategoryName());
        fieldNameMap.put("self.category_key", criteria.getCategoryKey());
        fieldNameMap.put("self.disabled", criteria.getDisabled());
        fieldNameMap.put("self.sort_value", criteria.getSortValue());
        fieldNameMap.put("self.built_in", criteria.getBuiltIn());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SystemConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
