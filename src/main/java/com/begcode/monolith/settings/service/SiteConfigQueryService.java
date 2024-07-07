package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.criteria.SiteConfigCriteria;
import com.begcode.monolith.settings.service.dto.SiteConfigDTO;
import com.begcode.monolith.settings.service.mapper.SiteConfigMapper;
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
 * 用于对数据库中的{@link SiteConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SiteConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SiteConfigDTO}列表{@link List} 或 {@link SiteConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class SiteConfigQueryService implements QueryService<SiteConfig> {

    private static final Logger log = LoggerFactory.getLogger(SiteConfigQueryService.class);

    protected final SiteConfigRepository siteConfigRepository;

    protected final SiteConfigMapper siteConfigMapper;

    public SiteConfigQueryService(SiteConfigRepository siteConfigRepository, SiteConfigMapper siteConfigMapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.siteConfigMapper = siteConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return siteConfigMapper.toDto(Binder.joinQueryList(queryWrapper, SiteConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SiteConfigDTO> findByCriteria(SiteConfigCriteria criteria, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SiteConfig.class, page).convert(siteConfig -> {
            Binder.bindRelations(siteConfig, new String[] {});
            return siteConfigMapper.toDto(siteConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SiteConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return siteConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return (List<T>) siteConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SiteConfigCriteria criteria) {
        return siteConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SiteConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SiteConfig> createQueryWrapper(SiteConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SiteConfigCriteria keywordsCriteria = new SiteConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.categoryName().setContains(keywords);
            keywordsCriteria.categoryKey().setContains(keywords);
            SiteConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SiteConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SiteConfigCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SiteConfig.class);
    }

    /**
     * Function to convert {@link SiteConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SiteConfig> createQueryWrapperNoJoin(SiteConfigCriteria criteria) {
        QueryWrapper<SiteConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SiteConfig.class);
    }

    /**
     * Return a {@link IPage} of {@link SiteConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SiteConfigDTO> findByQueryWrapper(QueryWrapper<SiteConfig> queryWrapper, Page<SiteConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return siteConfigRepository.selectPage(page, queryWrapper).convert(siteConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SiteConfigCriteria criteria) {
        QueryWrapper<SiteConfig> queryWrapper = createQueryWrapper(criteria);
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
            .forEach(entry -> {
                getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields);
            });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SiteConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
