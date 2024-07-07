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
        return (List<T>) sysFillRuleRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
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
        QueryWrapper<SysFillRule> queryWrapper = new DynamicJoinQueryWrapper<>(SysFillRuleCriteria.class, null);
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
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.code", criteria.getCode());
        fieldNameMap.put("self.desc", criteria.getDesc());
        fieldNameMap.put("self.enabled", criteria.getEnabled());
        fieldNameMap.put("self.reset_frequency", criteria.getResetFrequency());
        fieldNameMap.put("self.seq_value", criteria.getSeqValue());
        fieldNameMap.put("self.fill_value", criteria.getFillValue());
        fieldNameMap.put("self.impl_class", criteria.getImplClass());
        fieldNameMap.put("self.params", criteria.getParams());
        fieldNameMap.put("self.reset_start_time", criteria.getResetStartTime());
        fieldNameMap.put("self.reset_end_time", criteria.getResetEndTime());
        fieldNameMap.put("self.reset_time", criteria.getResetTime());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> {
                getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields);
            });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SysFillRule.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
