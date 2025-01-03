package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.repository.FormConfigRepository;
import com.begcode.monolith.system.service.criteria.FormConfigCriteria;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
import com.begcode.monolith.system.service.mapper.FormConfigMapper;
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
 * 用于对数据库中的{@link FormConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link FormConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link FormConfigDTO}列表{@link List} 或 {@link FormConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class FormConfigQueryService implements QueryService<FormConfig> {

    private static final Logger log = LoggerFactory.getLogger(FormConfigQueryService.class);

    protected final FormConfigRepository formConfigRepository;

    protected final FormConfigMapper formConfigMapper;

    public FormConfigQueryService(FormConfigRepository formConfigRepository, FormConfigMapper formConfigMapper) {
        this.formConfigRepository = formConfigRepository;
        this.formConfigMapper = formConfigMapper;
    }

    /**
     * Return a {@link List} of {@link FormConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<FormConfigDTO> findByCriteria(FormConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<FormConfig> queryWrapper = createQueryWrapper(criteria);
        return formConfigMapper.toDto(Binder.joinQueryList(queryWrapper, FormConfig.class));
    }

    /**
     * Return a {@link IPage} of {@link FormConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<FormConfigDTO> findByCriteria(FormConfigCriteria criteria, Page<FormConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<FormConfig> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, FormConfig.class, page).convert(formConfig -> {
            Binder.bindRelations(formConfig, new String[] {});
            return formConfigMapper.toDto(formConfig);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(FormConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return formConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, FormConfigCriteria criteria) {
        return formConfigRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, FormConfigCriteria criteria) {
        return formConfigRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link FormConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FormConfig> createQueryWrapper(FormConfigCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            FormConfigCriteria keywordsCriteria = new FormConfigCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.formKey().setContains(keywords);
            keywordsCriteria.formName().setContains(keywords);
            FormConfigCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<FormConfig> queryWrapper = new DynamicJoinQueryWrapper<>(FormConfigCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, FormConfig.class);
    }

    /**
     * Function to convert {@link FormConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<FormConfig> createQueryWrapperNoJoin(FormConfigCriteria criteria) {
        QueryWrapper<FormConfig> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, FormConfig.class);
    }

    /**
     * Return a {@link IPage} of {@link FormConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<FormConfigDTO> findByQueryWrapper(QueryWrapper<FormConfig> queryWrapper, Page<FormConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return formConfigRepository.selectPage(page, queryWrapper).convert(formConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(FormConfigCriteria criteria) {
        QueryWrapper<FormConfig> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.form_key", criteria.getFormKey());
        fieldNameMap.put("self.form_name", criteria.getFormName());
        fieldNameMap.put("self.form_type", criteria.getFormType());
        fieldNameMap.put("self.multi_items", criteria.getMultiItems());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap.put("self.business_type_id", criteria.getBusinessTypeId());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, FormConfig.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
