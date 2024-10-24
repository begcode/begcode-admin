package com.begcode.monolith.settings.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.criteria.DictionaryCriteria;
import com.begcode.monolith.settings.service.dto.DictionaryDTO;
import com.begcode.monolith.settings.service.mapper.DictionaryMapper;
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
 * 用于对数据库中的{@link Dictionary}实体执行复杂查询的Service。
 * 主要输入是一个{@link DictionaryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DictionaryDTO}列表{@link List} 或 {@link DictionaryDTO} 的分页列表 {@link Page}。
 */
@Service
public class DictionaryQueryService implements QueryService<Dictionary> {

    private static final Logger log = LoggerFactory.getLogger(DictionaryQueryService.class);

    protected final DictionaryRepository dictionaryRepository;

    protected final DictionaryMapper dictionaryMapper;

    public DictionaryQueryService(DictionaryRepository dictionaryRepository, DictionaryMapper dictionaryMapper) {
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryMapper = dictionaryMapper;
    }

    /**
     * Return a {@link List} of {@link DictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<DictionaryDTO> findByCriteria(DictionaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Dictionary> queryWrapper = createQueryWrapper(criteria);
        return dictionaryMapper.toDto(Binder.joinQueryList(queryWrapper, Dictionary.class));
    }

    /**
     * Return a {@link IPage} of {@link DictionaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<DictionaryDTO> findByCriteria(DictionaryCriteria criteria, Page<Dictionary> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Dictionary> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, Dictionary.class, page).convert(dictionary -> {
            Binder.bindRelations(dictionary, new String[] {});
            return dictionaryMapper.toDto(dictionary);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(DictionaryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return dictionaryRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DictionaryCriteria criteria) {
        return (List<T>) dictionaryRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DictionaryCriteria criteria) {
        return dictionaryRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link DictionaryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Dictionary> createQueryWrapper(DictionaryCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            DictionaryCriteria keywordsCriteria = new DictionaryCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.sortValue().setEquals(Integer.valueOf(keywords));
            }
            keywordsCriteria.dictName().setContains(keywords);
            keywordsCriteria.dictKey().setContains(keywords);
            DictionaryCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<Dictionary> queryWrapper = new DynamicJoinQueryWrapper<>(DictionaryCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, Dictionary.class);
    }

    /**
     * Function to convert {@link DictionaryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Dictionary> createQueryWrapperNoJoin(DictionaryCriteria criteria) {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, Dictionary.class);
    }

    /**
     * Return a {@link IPage} of {@link DictionaryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<DictionaryDTO> findByQueryWrapper(QueryWrapper<Dictionary> queryWrapper, Page<Dictionary> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return dictionaryRepository.selectPage(page, queryWrapper).convert(dictionaryMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(DictionaryCriteria criteria) {
        QueryWrapper<Dictionary> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.dict_name", criteria.getDictName());
        fieldNameMap.put("self.dict_key", criteria.getDictKey());
        fieldNameMap.put("self.disabled", criteria.getDisabled());
        fieldNameMap.put("self.sort_value", criteria.getSortValue());
        fieldNameMap.put("self.built_in", criteria.getBuiltIn());
        fieldNameMap.put("self.sync_enum", criteria.getSyncEnum());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, Dictionary.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
