package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.criteria.DepartmentCriteria;
import com.begcode.monolith.service.dto.DepartmentDTO;
import com.begcode.monolith.service.mapper.DepartmentMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link Department}实体执行复杂查询的Service。
 * 主要输入是一个{@link DepartmentCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link DepartmentDTO}列表{@link List} 或 {@link DepartmentDTO} 的分页列表 {@link Page}。
 */
@Service
public class DepartmentQueryService implements QueryService<Department> {

    private static final Logger log = LoggerFactory.getLogger(DepartmentQueryService.class);

    protected final DepartmentRepository departmentRepository;

    protected final DepartmentMapper departmentMapper;

    public DepartmentQueryService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<DepartmentDTO> findByCriteria(DepartmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return departmentMapper.toDto(Binder.joinQueryList(queryWrapper, Department.class));
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<DepartmentDTO> findByCriteria(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, Department.class, page).convert(department -> {
            Binder.bindRelations(department, new String[] { "children" });
            return departmentMapper.toDto(department);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(DepartmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return departmentRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Get all the departments for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<DepartmentDTO> findAllTop(DepartmentCriteria criteria, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, Department.class, page).convert(department -> {
            Binder.bindRelations(department, new String[] { "parent" });
            return departmentMapper.toDto(department);
        });
    }

    /**
     * Get all the departments for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<DepartmentDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Departments for parent is parentId");
        return departmentRepository
            .selectList(new LambdaUpdateWrapper<Department>().eq(Department::getParentId, parentId))
            .stream()
            .map(department -> {
                Binder.bindRelations(department, new String[] { "parent" });
                return departmentMapper.toDto(department);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return (List<T>) departmentRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, DepartmentCriteria criteria) {
        return departmentRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Department> createQueryWrapper(DepartmentCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            DepartmentCriteria keywordsCriteria = new DepartmentCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.code().setContains(keywords);
            keywordsCriteria.address().setContains(keywords);
            keywordsCriteria.phoneNum().setContains(keywords);
            keywordsCriteria.logo().setContains(keywords);
            keywordsCriteria.contact().setContains(keywords);
            DepartmentCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<Department> queryWrapper = new DynamicJoinQueryWrapper<>(DepartmentCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, Department.class);
    }

    /**
     * Function to convert {@link DepartmentCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Department> createQueryWrapperNoJoin(DepartmentCriteria criteria) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, Department.class);
    }

    /**
     * Return a {@link IPage} of {@link DepartmentDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<DepartmentDTO> findByQueryWrapper(QueryWrapper<Department> queryWrapper, Page<Department> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return departmentRepository.selectPage(page, queryWrapper).convert(departmentMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(DepartmentCriteria criteria) {
        QueryWrapper<Department> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.code", criteria.getCode());
        fieldNameMap.put("self.address", criteria.getAddress());
        fieldNameMap.put("self.phone_num", criteria.getPhoneNum());
        fieldNameMap.put("self.logo", criteria.getLogo());
        fieldNameMap.put("self.contact", criteria.getContact());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, Department.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
