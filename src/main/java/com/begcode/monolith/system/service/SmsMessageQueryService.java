package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.SmsMessage;
import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.criteria.SmsMessageCriteria;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
import com.begcode.monolith.system.service.mapper.SmsMessageMapper;
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
 * 用于对数据库中的{@link SmsMessage}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsMessageCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsMessageDTO}列表{@link List} 或 {@link SmsMessageDTO} 的分页列表 {@link Page}。
 */
@Service
public class SmsMessageQueryService implements QueryService<SmsMessage> {

    private static final Logger log = LoggerFactory.getLogger(SmsMessageQueryService.class);

    protected final SmsMessageRepository smsMessageRepository;

    protected final SmsMessageMapper smsMessageMapper;

    public SmsMessageQueryService(SmsMessageRepository smsMessageRepository, SmsMessageMapper smsMessageMapper) {
        this.smsMessageRepository = smsMessageRepository;
        this.smsMessageMapper = smsMessageMapper;
    }

    /**
     * Return a {@link List} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SmsMessageDTO> findByCriteria(SmsMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return smsMessageMapper.toDto(Binder.joinQueryList(queryWrapper, SmsMessage.class));
    }

    /**
     * Return a {@link IPage} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SmsMessageDTO> findByCriteria(SmsMessageCriteria criteria, Page<SmsMessage> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SmsMessage.class, page).convert(smsMessage -> {
            Binder.bindRelations(smsMessage, new String[] {});
            return smsMessageMapper.toDto(smsMessage);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SmsMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return smsMessageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsMessageCriteria criteria) {
        return smsMessageRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsMessageCriteria criteria) {
        return smsMessageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SmsMessageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsMessage> createQueryWrapper(SmsMessageCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SmsMessageCriteria keywordsCriteria = new SmsMessageCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.retryNum().setEquals(Integer.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.title().setContains(keywords);
            keywordsCriteria.receiver().setContains(keywords);
            keywordsCriteria.params().setContains(keywords);
            keywordsCriteria.failResult().setContains(keywords);
            keywordsCriteria.remark().setContains(keywords);
            SmsMessageCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<SmsMessage> queryWrapper = new DynamicJoinQueryWrapper<>(SmsMessageCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SmsMessage.class);
    }

    /**
     * Function to convert {@link SmsMessageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsMessage> createQueryWrapperNoJoin(SmsMessageCriteria criteria) {
        QueryWrapper<SmsMessage> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SmsMessage.class);
    }

    /**
     * Return a {@link IPage} of {@link SmsMessageDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SmsMessageDTO> findByQueryWrapper(QueryWrapper<SmsMessage> queryWrapper, Page<SmsMessage> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsMessageRepository.selectPage(page, queryWrapper).convert(smsMessageMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SmsMessageCriteria criteria) {
        QueryWrapper<SmsMessage> queryWrapper = createQueryWrapper(criteria);
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
                    DynamicJoinQueryWrapper<SmsMessageCriteria, SmsMessage> dynamicQuery = (DynamicJoinQueryWrapper<
                            SmsMessageCriteria,
                            SmsMessage
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
            return Binder.joinQueryMapsPage(queryWrapper, SmsMessage.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
