package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.repository.FillRuleItemRepository;
import com.begcode.monolith.settings.service.criteria.FillRuleItemCriteria;
import com.begcode.monolith.settings.service.dto.FillRuleItemDTO;
import com.begcode.monolith.settings.service.mapper.FillRuleItemMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.BindQuery;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link FillRuleItem}实体执行复杂查询的Service。
 * 主要输入是一个{@link FillRuleItemCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link FillRuleItemDTO}列表{@link List} 或 {@link FillRuleItemDTO} 的分页列表 {@link Page}。
 */
@Service
public class FillRuleItemQueryService implements QueryService<FillRuleItem> {

    private static final Logger log = LoggerFactory.getLogger(FillRuleItemQueryService.class);

    protected final FillRuleItemRepository fillRuleItemRepository;

    protected final FillRuleItemMapper fillRuleItemMapper;

    public FillRuleItemQueryService(FillRuleItemRepository fillRuleItemRepository, FillRuleItemMapper fillRuleItemMapper) {
        this.fillRuleItemRepository = fillRuleItemRepository;
        this.fillRuleItemMapper = fillRuleItemMapper;
    }

    /**
     * Return a {@link List} of {@link FillRuleItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<FillRuleItemDTO> findByCriteria(FillRuleItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<FillRuleItem> queryWrapper = createQueryWrapper(criteria);
        return fillRuleItemMapper.toDto(Binder.joinQueryList(queryWrapper, FillRuleItem.class));
    }

    /**
     * Return a {@link IPage} of {@link FillRuleItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<FillRuleItemDTO> findByCriteria(FillRuleItemCriteria criteria, Page<FillRuleItem> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<FillRuleItem> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, FillRuleItem.class, page).convert(fillRuleItem -> {
            Binder.bindRelations(fillRuleItem, new String[] { "fillRule" });
            return fillRuleItemMapper.toDto(fillRuleItem);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(FillRuleItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return fillRuleItemRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, FillRuleItemCriteria criteria) {
        return fillRuleItemRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, FillRuleItemCriteria criteria) {
        return fillRuleItemRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link FillRuleItemCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FillRuleItem> createQueryWrapper(FillRuleItemCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            FillRuleItemCriteria keywordsCriteria = new FillRuleItemCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.seqLength().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.seqIncrement().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.seqStartValue().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.fieldParamValue().setContains(keywords);
            keywordsCriteria.datePattern().setContains(keywords);
            FillRuleItemCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<FillRuleItem> queryWrapper = new DynamicJoinQueryWrapper<>(FillRuleItemCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, FillRuleItem.class);
    }

    /**
     * Function to convert {@link FillRuleItemCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FillRuleItem> createQueryWrapperNoJoin(FillRuleItemCriteria criteria) {
        QueryWrapper<FillRuleItem> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, FillRuleItem.class);
    }

    /**
     * Return a {@link IPage} of {@link FillRuleItemDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<FillRuleItemDTO> findByQueryWrapper(QueryWrapper<FillRuleItem> queryWrapper, Page<FillRuleItem> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return fillRuleItemRepository.selectPage(page, queryWrapper).convert(fillRuleItemMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(FillRuleItemCriteria criteria) {
        QueryWrapper<FillRuleItem> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<FillRuleItemCriteria, FillRuleItem> dynamicQuery = (DynamicJoinQueryWrapper<
                            FillRuleItemCriteria,
                            FillRuleItem
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
            return Binder.joinQueryMapsPage(queryWrapper, FillRuleItem.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
