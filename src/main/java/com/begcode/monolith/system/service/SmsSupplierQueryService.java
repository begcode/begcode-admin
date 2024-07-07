package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.criteria.SmsSupplierCriteria;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import com.begcode.monolith.system.service.mapper.SmsSupplierMapper;
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
 * 用于对数据库中的{@link SmsSupplier}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsSupplierCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsSupplierDTO}列表{@link List} 或 {@link SmsSupplierDTO} 的分页列表 {@link Page}。
 */
@Service
public class SmsSupplierQueryService implements QueryService<SmsSupplier> {

    private static final Logger log = LoggerFactory.getLogger(SmsSupplierQueryService.class);

    protected final SmsSupplierRepository smsSupplierRepository;

    protected final SmsSupplierMapper smsSupplierMapper;

    public SmsSupplierQueryService(SmsSupplierRepository smsSupplierRepository, SmsSupplierMapper smsSupplierMapper) {
        this.smsSupplierRepository = smsSupplierRepository;
        this.smsSupplierMapper = smsSupplierMapper;
    }

    /**
     * Return a {@link List} of {@link SmsSupplierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SmsSupplierDTO> findByCriteria(SmsSupplierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsSupplier> queryWrapper = createQueryWrapper(criteria);
        return smsSupplierMapper.toDto(Binder.joinQueryList(queryWrapper, SmsSupplier.class));
    }

    /**
     * Return a {@link IPage} of {@link SmsSupplierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SmsSupplierDTO> findByCriteria(SmsSupplierCriteria criteria, Page<SmsSupplier> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsSupplier> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SmsSupplier.class, page).convert(smsSupplier -> {
            Binder.bindRelations(smsSupplier, new String[] {});
            return smsSupplierMapper.toDto(smsSupplier);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SmsSupplierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return smsSupplierRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsSupplierCriteria criteria) {
        return (List<T>) smsSupplierRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsSupplierCriteria criteria) {
        return smsSupplierRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SmsSupplierCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsSupplier> createQueryWrapper(SmsSupplierCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SmsSupplierCriteria keywordsCriteria = new SmsSupplierCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.configData().setContains(keywords);
            keywordsCriteria.signName().setContains(keywords);
            keywordsCriteria.remark().setContains(keywords);
            SmsSupplierCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SmsSupplier> queryWrapper = new DynamicJoinQueryWrapper<>(SmsSupplierCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SmsSupplier.class);
    }

    /**
     * Function to convert {@link SmsSupplierCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsSupplier> createQueryWrapperNoJoin(SmsSupplierCriteria criteria) {
        QueryWrapper<SmsSupplier> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SmsSupplier.class);
    }

    /**
     * Return a {@link IPage} of {@link SmsSupplierDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SmsSupplierDTO> findByQueryWrapper(QueryWrapper<SmsSupplier> queryWrapper, Page<SmsSupplier> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsSupplierRepository.selectPage(page, queryWrapper).convert(smsSupplierMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SmsSupplierCriteria criteria) {
        QueryWrapper<SmsSupplier> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.provider", criteria.getProvider());
        fieldNameMap.put("self.config_data", criteria.getConfigData());
        fieldNameMap.put("self.sign_name", criteria.getSignName());
        fieldNameMap.put("self.remark", criteria.getRemark());
        fieldNameMap.put("self.enabled", criteria.getEnabled());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> {
                getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields);
            });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SmsSupplier.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
