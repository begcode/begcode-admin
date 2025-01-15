package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Position;
import com.begcode.monolith.repository.PositionRepository;
import com.begcode.monolith.service.criteria.PositionCriteria;
import com.begcode.monolith.service.dto.PositionDTO;
import com.begcode.monolith.service.mapper.PositionMapper;
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
 * 用于对数据库中的{@link Position}实体执行复杂查询的Service。
 * 主要输入是一个{@link PositionCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link PositionDTO}列表{@link List} 或 {@link PositionDTO} 的分页列表 {@link Page}。
 */
@Service
public class PositionQueryService implements QueryService<Position> {

    private static final Logger log = LoggerFactory.getLogger(PositionQueryService.class);

    protected final PositionRepository positionRepository;

    protected final PositionMapper positionMapper;

    public PositionQueryService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    /**
     * Return a {@link List} of {@link PositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<PositionDTO> findByCriteria(PositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return positionMapper.toDto(Binder.joinQueryList(queryWrapper, Position.class));
    }

    /**
     * Return a {@link IPage} of {@link PositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<PositionDTO> findByCriteria(PositionCriteria criteria, Page<Position> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, Position.class, page).convert(position -> {
            Binder.bindRelations(position, new String[] {});
            return positionMapper.toDto(position);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(PositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return positionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, PositionCriteria criteria) {
        return positionRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, PositionCriteria criteria) {
        return positionRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link PositionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Position> createQueryWrapper(PositionCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            PositionCriteria keywordsCriteria = new PositionCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortNo().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.description().setContains(keywords);
            PositionCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<Position> queryWrapper = new DynamicJoinQueryWrapper<>(PositionCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, Position.class);
    }

    /**
     * Function to convert {@link PositionCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Position> createQueryWrapperNoJoin(PositionCriteria criteria) {
        QueryWrapper<Position> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, Position.class);
    }

    /**
     * Return a {@link IPage} of {@link PositionDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<PositionDTO> findByQueryWrapper(QueryWrapper<Position> queryWrapper, Page<Position> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return positionRepository.selectPage(page, queryWrapper).convert(positionMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(PositionCriteria criteria) {
        QueryWrapper<Position> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<PositionCriteria, Position> dynamicQuery = (DynamicJoinQueryWrapper<
                            PositionCriteria,
                            Position
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
            return Binder.joinQueryMapsPage(queryWrapper, Position.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
