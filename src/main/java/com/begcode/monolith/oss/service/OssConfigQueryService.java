package com.begcode.monolith.oss.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.oss.domain.OssConfig;
import com.begcode.monolith.oss.repository.OssConfigRepository;
import com.begcode.monolith.oss.service.criteria.OssConfigCriteria;
import com.begcode.monolith.oss.service.dto.OssConfigDTO;
import com.begcode.monolith.oss.service.mapper.OssConfigMapper;
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
 * 用于对数据库中的{@link OssConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link OssConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link OssConfigDTO}列表{@link List} 或 {@link OssConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class OssConfigQueryService implements QueryService<OssConfig> {

    private static final Logger log = LoggerFactory.getLogger(OssConfigQueryService.class);

    protected final OssConfigRepository ossConfigRepository;

    protected final OssConfigMapper ossConfigMapper;

    public OssConfigQueryService(OssConfigRepository ossConfigRepository, OssConfigMapper ossConfigMapper) {
        this.ossConfigRepository = ossConfigRepository;
        this.ossConfigMapper = ossConfigMapper;
    }

    /**
     * Return a {@link List} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<OssConfigDTO> findByCriteria(OssConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return ossConfigMapper.toDto(Binder.joinQueryList(queryWrapper, OssConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<OssConfigDTO> findByCriteria(OssConfigCriteria criteria, Page<OssConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, OssConfig.class, page).convert(ossConfig -> {
            Binder.bindRelations(ossConfig, new String[] {});
            return ossConfigMapper.toDto(ossConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(OssConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return ossConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, OssConfigCriteria criteria) {
        return ossConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, OssConfigCriteria criteria) {
        return ossConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link OssConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<OssConfig> createQueryWrapper(OssConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            OssConfigCriteria keywordsCriteria = new OssConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.platform().setContains(keywords);
            keywordsCriteria.remark().setContains(keywords);
            keywordsCriteria.configData().setContains(keywords);
            OssConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<OssConfig> queryWrapper = new DynamicJoinQueryWrapper<>(OssConfigCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, OssConfig.class);
    }

    /**
     * Function to convert {@link OssConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<OssConfig> createQueryWrapperNoJoin(OssConfigCriteria criteria) {
        QueryWrapper<OssConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, OssConfig.class);
    }

    /**
     * Return a {@link IPage} of {@link OssConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<OssConfigDTO> findByQueryWrapper(QueryWrapper<OssConfig> queryWrapper, Page<OssConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return ossConfigRepository.selectPage(page, queryWrapper).convert(ossConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(OssConfigCriteria criteria) {
        QueryWrapper<OssConfig> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<OssConfigCriteria, OssConfig> dynamicQuery = (DynamicJoinQueryWrapper<
                            OssConfigCriteria,
                            OssConfig
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
            return Binder.joinQueryMapsPage(queryWrapper, OssConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
