package com.begcode.monolith.system.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.Announcement;
import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.criteria.AnnouncementCriteria;
import com.begcode.monolith.system.service.dto.AnnouncementDTO;
import com.begcode.monolith.system.service.mapper.AnnouncementMapper;
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
 * 用于对数据库中的{@link Announcement}实体执行复杂查询的Service。
 * 主要输入是一个{@link AnnouncementCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AnnouncementDTO}列表{@link List} 或 {@link AnnouncementDTO} 的分页列表 {@link Page}。
 */
@Service
public class AnnouncementQueryService implements QueryService<Announcement> {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementQueryService.class);

    protected final AnnouncementRepository announcementRepository;

    protected final AnnouncementMapper announcementMapper;

    public AnnouncementQueryService(AnnouncementRepository announcementRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Return a {@link List} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<AnnouncementDTO> findByCriteria(AnnouncementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return announcementMapper.toDto(Binder.joinQueryList(queryWrapper, Announcement.class));
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<AnnouncementDTO> findByCriteria(AnnouncementCriteria criteria, Page<Announcement> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, Announcement.class, page).convert(announcement -> {
            Binder.bindRelations(announcement, new String[] {});
            return announcementMapper.toDto(announcement);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(AnnouncementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return announcementRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AnnouncementCriteria criteria) {
        return (List<T>) announcementRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AnnouncementCriteria criteria) {
        return announcementRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link AnnouncementCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Announcement> createQueryWrapper(AnnouncementCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            AnnouncementCriteria keywordsCriteria = new AnnouncementCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.senderId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.businessId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.title().setContains(keywords);
            keywordsCriteria.openPage().setContains(keywords);
            AnnouncementCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<Announcement> queryWrapper = new DynamicJoinQueryWrapper<>(AnnouncementCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, Announcement.class);
    }

    /**
     * Function to convert {@link AnnouncementCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Announcement> createQueryWrapperNoJoin(AnnouncementCriteria criteria) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, Announcement.class);
    }

    /**
     * Return a {@link IPage} of {@link AnnouncementDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<AnnouncementDTO> findByQueryWrapper(QueryWrapper<Announcement> queryWrapper, Page<Announcement> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return announcementRepository.selectPage(page, queryWrapper).convert(announcementMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(AnnouncementCriteria criteria) {
        QueryWrapper<Announcement> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.title", criteria.getTitle());
        fieldNameMap.put("self.start_time", criteria.getStartTime());
        fieldNameMap.put("self.end_time", criteria.getEndTime());
        fieldNameMap.put("self.sender_id", criteria.getSenderId());
        fieldNameMap.put("self.priority", criteria.getPriority());
        fieldNameMap.put("self.category", criteria.getCategory());
        fieldNameMap.put("self.receiver_type", criteria.getReceiverType());
        fieldNameMap.put("self.send_status", criteria.getSendStatus());
        fieldNameMap.put("self.send_time", criteria.getSendTime());
        fieldNameMap.put("self.cancel_time", criteria.getCancelTime());
        fieldNameMap.put("self.business_type", criteria.getBusinessType());
        fieldNameMap.put("self.business_id", criteria.getBusinessId());
        fieldNameMap.put("self.open_type", criteria.getOpenType());
        fieldNameMap.put("self.open_page", criteria.getOpenPage());
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
            return Binder.joinQueryMapsPage(queryWrapper, Announcement.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
