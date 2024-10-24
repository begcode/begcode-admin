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
        return (List<T>) fillRuleItemRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
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
        QueryWrapper<FillRuleItem> queryWrapper = new DynamicJoinQueryWrapper<>(FillRuleItemCriteria.class, null);
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
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.sort_value", criteria.getSortValue());
        fieldNameMap.put("self.field_param_type", criteria.getFieldParamType());
        fieldNameMap.put("self.field_param_value", criteria.getFieldParamValue());
        fieldNameMap.put("self.date_pattern", criteria.getDatePattern());
        fieldNameMap.put("self.seq_length", criteria.getSeqLength());
        fieldNameMap.put("self.seq_increment", criteria.getSeqIncrement());
        fieldNameMap.put("self.seq_start_value", criteria.getSeqStartValue());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, FillRuleItem.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
