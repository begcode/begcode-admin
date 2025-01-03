package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.UReportFile;
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.criteria.UReportFileCriteria;
import com.begcode.monolith.service.dto.UReportFileDTO;
import com.begcode.monolith.service.mapper.UReportFileMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
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
 * 用于对数据库中的{@link UReportFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UReportFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UReportFileDTO}列表{@link List} 或 {@link UReportFileDTO} 的分页列表 {@link Page}。
 */
@Service
public class UReportFileQueryService implements QueryService<UReportFile> {

    private static final Logger log = LoggerFactory.getLogger(UReportFileQueryService.class);

    protected final UReportFileRepository uReportFileRepository;

    protected final UReportFileMapper uReportFileMapper;

    public UReportFileQueryService(UReportFileRepository uReportFileRepository, UReportFileMapper uReportFileMapper) {
        this.uReportFileRepository = uReportFileRepository;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Return a {@link List} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UReportFileDTO> findByCriteria(UReportFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return uReportFileMapper.toDto(Binder.joinQueryList(queryWrapper, UReportFile.class));
    }

    /**
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UReportFileDTO> findByCriteria(UReportFileCriteria criteria, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, UReportFile.class, page).convert(uReportFile -> {
            Binder.bindRelations(uReportFile, new String[] {});
            return uReportFileMapper.toDto(uReportFile);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UReportFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return uReportFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return uReportFileRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UReportFileCriteria criteria) {
        return uReportFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UReportFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UReportFile> createQueryWrapper(UReportFileCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UReportFileCriteria keywordsCriteria = new UReportFileCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            UReportFileCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<UReportFile> queryWrapper = new DynamicJoinQueryWrapper<>(UReportFileCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, UReportFile.class);
    }

    /**
     * Function to convert {@link UReportFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UReportFile> createQueryWrapperNoJoin(UReportFileCriteria criteria) {
        QueryWrapper<UReportFile> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, UReportFile.class);
    }

    /**
     * Return a {@link IPage} of {@link UReportFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UReportFileDTO> findByQueryWrapper(QueryWrapper<UReportFile> queryWrapper, Page<UReportFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uReportFileRepository.selectPage(page, queryWrapper).convert(uReportFileMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UReportFileCriteria criteria) {
        QueryWrapper<UReportFile> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.create_at", criteria.getCreateAt());
        fieldNameMap.put("self.update_at", criteria.getUpdateAt());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, UReportFile.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
