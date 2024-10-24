package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.AnnouncementRecord;
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.service.criteria.AnnouncementRecordCriteria;
import com.begcode.monolith.system.service.dto.AnnouncementRecordDTO;
import com.begcode.monolith.system.service.mapper.AnnouncementRecordMapper;
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
 * 用于对数据库中的{@link AnnouncementRecord}实体执行复杂查询的Service。
 * 主要输入是一个{@link AnnouncementRecordCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AnnouncementRecordDTO}列表{@link List} 或 {@link AnnouncementRecordDTO} 的分页列表 {@link Page}。
 */
@Service
public class AnnouncementRecordQueryService implements QueryService<AnnouncementRecord> {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementRecordQueryService.class);

    protected final AnnouncementRecordRepository announcementRecordRepository;

    protected final AnnouncementRecordMapper announcementRecordMapper;

    public AnnouncementRecordQueryService(
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRecordMapper announcementRecordMapper
    ) {
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRecordMapper = announcementRecordMapper;
    }

    /**
     * Return a {@link List} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return announcementRecordMapper.toDto(Binder.joinQueryList(queryWrapper, AnnouncementRecord.class));
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<AnnouncementRecordDTO> findByCriteria(AnnouncementRecordCriteria criteria, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, AnnouncementRecord.class, page).convert(announcementRecord -> {
            Binder.bindRelations(announcementRecord, new String[] {});
            return announcementRecordMapper.toDto(announcementRecord);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(AnnouncementRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return announcementRecordRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return (List<T>) announcementRecordRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AnnouncementRecordCriteria criteria) {
        return announcementRecordRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link AnnouncementRecordCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<AnnouncementRecord> createQueryWrapper(AnnouncementRecordCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            AnnouncementRecordCriteria keywordsCriteria = new AnnouncementRecordCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.anntId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.userId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            AnnouncementRecordCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<AnnouncementRecord> queryWrapper = new DynamicJoinQueryWrapper<>(AnnouncementRecordCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, AnnouncementRecord.class);
    }

    /**
     * Function to convert {@link AnnouncementRecordCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<AnnouncementRecord> createQueryWrapperNoJoin(AnnouncementRecordCriteria criteria) {
        QueryWrapper<AnnouncementRecord> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, AnnouncementRecord.class);
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementRecordDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<AnnouncementRecordDTO> findByQueryWrapper(QueryWrapper<AnnouncementRecord> queryWrapper, Page<AnnouncementRecord> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return announcementRecordRepository.selectPage(page, queryWrapper).convert(announcementRecordMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(AnnouncementRecordCriteria criteria) {
        QueryWrapper<AnnouncementRecord> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.annt_id", criteria.getAnntId());
        fieldNameMap.put("self.user_id", criteria.getUserId());
        fieldNameMap.put("self.has_read", criteria.getHasRead());
        fieldNameMap.put("self.read_time", criteria.getReadTime());
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
            return Binder.joinQueryMapsPage(queryWrapper, AnnouncementRecord.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
