package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.repository.SmsTemplateRepository;
import com.begcode.monolith.system.service.criteria.SmsTemplateCriteria;
import com.begcode.monolith.system.service.dto.SmsTemplateDTO;
import com.begcode.monolith.system.service.mapper.SmsTemplateMapper;
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
 * 用于对数据库中的{@link SmsTemplate}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsTemplateCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsTemplateDTO}列表{@link List} 或 {@link SmsTemplateDTO} 的分页列表 {@link Page}。
 */
@Service
public class SmsTemplateQueryService implements QueryService<SmsTemplate> {

    private static final Logger log = LoggerFactory.getLogger(SmsTemplateQueryService.class);

    protected final SmsTemplateRepository smsTemplateRepository;

    protected final SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateQueryService(SmsTemplateRepository smsTemplateRepository, SmsTemplateMapper smsTemplateMapper) {
        this.smsTemplateRepository = smsTemplateRepository;
        this.smsTemplateMapper = smsTemplateMapper;
    }

    /**
     * Return a {@link List} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return smsTemplateMapper.toDto(Binder.joinQueryList(queryWrapper, SmsTemplate.class));
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SmsTemplateDTO> findByCriteria(SmsTemplateCriteria criteria, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SmsTemplate.class, page).convert(smsTemplate -> {
            Binder.bindRelations(smsTemplate, new String[] {});
            return smsTemplateMapper.toDto(smsTemplate);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SmsTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return smsTemplateRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return (List<T>) smsTemplateRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsTemplateCriteria criteria) {
        return smsTemplateRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SmsTemplateCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsTemplate> createQueryWrapper(SmsTemplateCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SmsTemplateCriteria keywordsCriteria = new SmsTemplateCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.content().setContains(keywords);
            keywordsCriteria.testJson().setContains(keywords);
            keywordsCriteria.remark().setContains(keywords);
            SmsTemplateCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SmsTemplate> queryWrapper = new DynamicJoinQueryWrapper<>(SmsTemplateCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SmsTemplate.class);
    }

    /**
     * Function to convert {@link SmsTemplateCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsTemplate> createQueryWrapperNoJoin(SmsTemplateCriteria criteria) {
        QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SmsTemplate.class);
    }

    /**
     * Return a {@link IPage} of {@link SmsTemplateDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SmsTemplateDTO> findByQueryWrapper(QueryWrapper<SmsTemplate> queryWrapper, Page<SmsTemplate> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsTemplateRepository.selectPage(page, queryWrapper).convert(smsTemplateMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SmsTemplateCriteria criteria) {
        QueryWrapper<SmsTemplate> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.code", criteria.getCode());
        fieldNameMap.put("self.send_type", criteria.getSendType());
        fieldNameMap.put("self.content", criteria.getContent());
        fieldNameMap.put("self.test_json", criteria.getTestJson());
        fieldNameMap.put("self.type", criteria.getType());
        fieldNameMap.put("self.remark", criteria.getRemark());
        fieldNameMap.put("self.enabled", criteria.getEnabled());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SmsTemplate.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
