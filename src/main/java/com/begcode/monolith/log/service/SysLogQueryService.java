package com.begcode.monolith.log.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.log.domain.SysLog;
import com.begcode.monolith.log.repository.SysLogRepository;
import com.begcode.monolith.log.service.criteria.SysLogCriteria;
import com.begcode.monolith.log.service.dto.SysLogDTO;
import com.begcode.monolith.log.service.mapper.SysLogMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link SysLog}实体执行复杂查询的Service。
 * 主要输入是一个{@link SysLogCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SysLogDTO}列表{@link List} 或 {@link SysLogDTO} 的分页列表 {@link Page}。
 */
@Service
public class SysLogQueryService implements QueryService<SysLog> {

    private static final Logger log = LoggerFactory.getLogger(SysLogQueryService.class);

    protected final SysLogRepository sysLogRepository;

    protected final SysLogMapper sysLogMapper;

    public SysLogQueryService(SysLogRepository sysLogRepository, SysLogMapper sysLogMapper) {
        this.sysLogRepository = sysLogRepository;
        this.sysLogMapper = sysLogMapper;
    }

    /**
     * Return a {@link List} of {@link SysLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SysLogDTO> findByCriteria(SysLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return sysLogMapper.toDto(Binder.joinQueryList(queryWrapper, SysLog.class));
    }

    /**
     * Return a {@link IPage} of {@link SysLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SysLogDTO> findByCriteria(SysLogCriteria criteria, Page<SysLog> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, SysLog.class, page).convert(sysLog -> {
            Binder.bindRelations(sysLog, new String[] {});
            return sysLogMapper.toDto(sysLog);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SysLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return sysLogRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SysLogCriteria criteria) {
        return (List<T>) sysLogRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SysLogCriteria criteria) {
        return sysLogRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link SysLogCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SysLog> createQueryWrapper(SysLogCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            SysLogCriteria keywordsCriteria = new SysLogCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.costTime().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.logContent().setContains(keywords);
            keywordsCriteria.userid().setContains(keywords);
            keywordsCriteria.username().setContains(keywords);
            keywordsCriteria.ip().setContains(keywords);
            keywordsCriteria.method().setContains(keywords);
            keywordsCriteria.requestUrl().setContains(keywords);
            keywordsCriteria.requestType().setContains(keywords);
            SysLogCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<SysLog> queryWrapper = new DynamicJoinQueryWrapper<>(SysLogCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, SysLog.class);
    }

    /**
     * Function to convert {@link SysLogCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SysLog> createQueryWrapperNoJoin(SysLogCriteria criteria) {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, SysLog.class);
    }

    /**
     * Return a {@link IPage} of {@link SysLogDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SysLogDTO> findByQueryWrapper(QueryWrapper<SysLog> queryWrapper, Page<SysLog> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return sysLogRepository.selectPage(page, queryWrapper).convert(sysLogMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SysLogCriteria criteria) {
        QueryWrapper<SysLog> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.log_type", criteria.getLogType());
        fieldNameMap.put("self.log_content", criteria.getLogContent());
        fieldNameMap.put("self.operate_type", criteria.getOperateType());
        fieldNameMap.put("self.userid", criteria.getUserid());
        fieldNameMap.put("self.username", criteria.getUsername());
        fieldNameMap.put("self.ip", criteria.getIp());
        fieldNameMap.put("self.method", criteria.getMethod());
        fieldNameMap.put("self.request_url", criteria.getRequestUrl());
        fieldNameMap.put("self.request_type", criteria.getRequestType());
        fieldNameMap.put("self.cost_time", criteria.getCostTime());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> {
                getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields);
            });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, SysLog.class, null).getRecords();
        }
        return Collections.emptyList();
    }

    public Map<String, Object> logInfo() {
        Map<String, Object> result = new HashMap<>();
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        SysLogCriteria sysLogCriteria = new SysLogCriteria();
        sysLogCriteria.logType().setEquals(LogType.LOGIN);
        Long totalVisitCount = countByCriteria(sysLogCriteria);
        result.put("totalVisitCount", totalVisitCount);
        sysLogCriteria.createdDate().setGreaterThanOrEqual(dayStart.toInstant()).setLessThan(dayEnd.toInstant());
        Long todayVisitCount = countByCriteria(sysLogCriteria);
        result.put("todayVisitCount", todayVisitCount);
        Long todayIp = countByFieldNameAndCriteria("ip", true, sysLogCriteria);
        result.put("todayIp", todayIp);
        return result;
    }

    public List<Map<String, Object>> countVisit() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        String[] selections = new String[4];
        selections[0] = "count(*) as visit";
        selections[1] = "count(distinct(ip)) as ip";
        selections[2] = "DATE_FORMAT(created_date, '%Y-%m-%d') as day";
        selections[3] = "DATE_FORMAT(created_date, '%m-%d') as type";
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(selections);
        queryWrapper.ge("created_date", dayStart);
        queryWrapper.lt("created_date", dayEnd);
        queryWrapper.eq("log_type", LogType.LOGIN);
        queryWrapper.groupBy("day", "type");
        queryWrapper.orderBy(true, true, "day");
        return sysLogRepository.selectMaps(queryWrapper);
    }
}
