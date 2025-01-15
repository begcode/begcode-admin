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
        return announcementRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
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
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<Announcement> queryWrapper = new DynamicJoinQueryWrapper<>(AnnouncementCriteria.class, fields);
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
                    DynamicJoinQueryWrapper<AnnouncementCriteria, Announcement> dynamicQuery = (DynamicJoinQueryWrapper<
                            AnnouncementCriteria,
                            Announcement
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
            return Binder.joinQueryMapsPage(queryWrapper, Announcement.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
