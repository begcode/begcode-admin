package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.criteria.RegionCodeCriteria;
import com.begcode.monolith.settings.service.dto.RegionCodeDTO;
import com.begcode.monolith.settings.service.mapper.RegionCodeMapper;
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
 * 用于对数据库中的{@link RegionCode}实体执行复杂查询的Service。
 * 主要输入是一个{@link RegionCodeCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link RegionCodeDTO}列表{@link List} 或 {@link RegionCodeDTO} 的分页列表 {@link Page}。
 */
@Service
public class RegionCodeQueryService implements QueryService<RegionCode> {

    private static final Logger log = LoggerFactory.getLogger(RegionCodeQueryService.class);

    protected final RegionCodeRepository regionCodeRepository;

    protected final RegionCodeMapper regionCodeMapper;

    public RegionCodeQueryService(RegionCodeRepository regionCodeRepository, RegionCodeMapper regionCodeMapper) {
        this.regionCodeRepository = regionCodeRepository;
        this.regionCodeMapper = regionCodeMapper;
    }

    /**
     * Return a {@link List} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return regionCodeMapper.toDto(Binder.joinQueryList(queryWrapper, RegionCode.class));
    }

    /**
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<RegionCodeDTO> findByCriteria(RegionCodeCriteria criteria, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, RegionCode.class, page).convert(regionCode -> {
            Binder.bindRelations(regionCode, new String[] { "children" });
            return regionCodeMapper.toDto(regionCode);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(RegionCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return regionCodeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the regionCodes for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<RegionCodeDTO> findAllTop(RegionCodeCriteria criteria, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, RegionCode.class, page).convert(regionCode -> {
            Binder.bindRelations(regionCode, new String[] { "parent" });
            return regionCodeMapper.toDto(regionCode);
        });
    }

    /**
     * Get all the regionCodes for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<RegionCodeDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all RegionCodes for parent is parentId");
        return regionCodeRepository
            .selectList(new LambdaUpdateWrapper<RegionCode>().eq(RegionCode::getParentId, parentId))
            .stream()
            .map(regionCode -> {
                Binder.bindRelations(regionCode, new String[] { "parent" });
                return regionCodeMapper.toDto(regionCode);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return regionCodeRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, RegionCodeCriteria criteria) {
        return regionCodeRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link RegionCodeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<RegionCode> createQueryWrapper(RegionCodeCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            RegionCodeCriteria keywordsCriteria = new RegionCodeCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lng().setEquals(Double.valueOf(keywords));
                keywordsCriteria.lat().setEquals(Double.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.areaCode().setContains(keywords);
            keywordsCriteria.cityCode().setContains(keywords);
            keywordsCriteria.mergerName().setContains(keywords);
            keywordsCriteria.shortName().setContains(keywords);
            keywordsCriteria.zipCode().setContains(keywords);
            RegionCodeCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<RegionCode> queryWrapper = new DynamicJoinQueryWrapper<>(RegionCodeCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, RegionCode.class);
    }

    /**
     * Function to convert {@link RegionCodeCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<RegionCode> createQueryWrapperNoJoin(RegionCodeCriteria criteria) {
        QueryWrapper<RegionCode> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, RegionCode.class);
    }

    /**
     * Return a {@link IPage} of {@link RegionCodeDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<RegionCodeDTO> findByQueryWrapper(QueryWrapper<RegionCode> queryWrapper, Page<RegionCode> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return regionCodeRepository.selectPage(page, queryWrapper).convert(regionCodeMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(RegionCodeCriteria criteria) {
        QueryWrapper<RegionCode> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<RegionCodeCriteria, RegionCode> dynamicQuery = (DynamicJoinQueryWrapper<
                            RegionCodeCriteria,
                            RegionCode
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
            return Binder.joinQueryMapsPage(queryWrapper, RegionCode.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
