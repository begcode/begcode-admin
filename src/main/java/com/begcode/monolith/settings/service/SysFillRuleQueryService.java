package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.criteria.SysFillRuleCriteria;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
import com.begcode.monolith.settings.service.mapper.SysFillRuleMapper;
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
 * 用于对数据库中的{@link SysFillRule}实体执行复杂查询的Service。
 * 主要输入是一个{@link SysFillRuleCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SysFillRuleDTO}列表{@link List} 或 {@link SysFillRuleDTO} 的分页列表 {@link Page}。
 */
@Service
public class SysFillRuleQueryService implements QueryService<SysFillRule> {

    private static final Logger log = LoggerFactory.getLogger(SysFillRuleQueryService.class);

    protected final SysFillRuleRepository sysFillRuleRepository;

    protected final SysFillRuleMapper sysFillRuleMapper;

    public SysFillRuleQueryService(SysFillRuleRepository sysFillRuleRepository, SysFillRuleMapper sysFillRuleMapper) {
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.sysFillRuleMapper = sysFillRuleMapper;
    }

    /**
     * Return a {@link List} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SysFillRuleDTO> findByCriteria(SysFillRuleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return sysFillRuleMapper.toDto(Binder.joinQueryList(queryWrapper, SysFillRule.class));
    }

    /**
     * Return a {@link IPage} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SysFillRuleDTO> findByCriteria(SysFillRuleCriteria criteria, Page<SysFillRule> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SysFillRule.class, page).convert(sysFillRule -> {
            Binder.bindRelations(sysFillRule, new String[] {});
            return sysFillRuleMapper.toDto(sysFillRule);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SysFillRuleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return sysFillRuleRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SysFillRuleCriteria criteria) {
        return sysFillRuleRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SysFillRuleCriteria criteria) {
        return sysFillRuleRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SysFillRuleCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SysFillRule> createQueryWrapper(SysFillRuleCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SysFillRuleCriteria keywordsCriteria = new SysFillRuleCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.seqValue().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.desc().setContains(keywords);
            keywordsCriteria.fillValue().setContains(keywords);
            keywordsCriteria.implClass().setContains(keywords);
            keywordsCriteria.params().setContains(keywords);
            SysFillRuleCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<SysFillRule> queryWrapper = new DynamicJoinQueryWrapper<>(SysFillRuleCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SysFillRule.class);
    }

    /**
     * Function to convert {@link SysFillRuleCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SysFillRule> createQueryWrapperNoJoin(SysFillRuleCriteria criteria) {
        QueryWrapper<SysFillRule> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SysFillRule.class);
    }

    /**
     * Return a {@link IPage} of {@link SysFillRuleDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SysFillRuleDTO> findByQueryWrapper(QueryWrapper<SysFillRule> queryWrapper, Page<SysFillRule> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return sysFillRuleRepository.selectPage(page, queryWrapper).convert(sysFillRuleMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SysFillRuleCriteria criteria) {
        QueryWrapper<SysFillRule> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<SysFillRuleCriteria, SysFillRule> dynamicQuery = (DynamicJoinQueryWrapper<
                            SysFillRuleCriteria,
                            SysFillRule
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
            return Binder.joinQueryMapsPage(queryWrapper, SysFillRule.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
