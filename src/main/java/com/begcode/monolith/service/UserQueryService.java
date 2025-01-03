package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.repository.UserRepository;
import com.begcode.monolith.service.criteria.UserCriteria;
import com.begcode.monolith.service.dto.UserDTO;
import com.begcode.monolith.service.mapper.UserMapper;
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
 * 用于对数据库中的{@link User}实体执行复杂查询的Service。
 * 主要输入是一个{@link UserCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UserDTO}列表{@link List} 或 {@link UserDTO} 的分页列表 {@link Page}。
 */
@Service
public class UserQueryService implements QueryService<User> {

    private static final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    protected final UserRepository userRepository;

    protected final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link List} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<User> queryWrapper = createQueryWrapper(criteria);
        return userMapper.toDto(Binder.joinQueryList(queryWrapper, User.class));
    }

    /**
     * Return a {@link IPage} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UserDTO> findByCriteria(UserCriteria criteria, Page<User> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<User> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, User.class, page).convert(user -> {
            Binder.bindRelations(user, new String[] {});
            return userMapper.toDto(user);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return userRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UserCriteria criteria) {
        return userRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UserCriteria criteria) {
        return userRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<User> createQueryWrapper(UserCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UserCriteria keywordsCriteria = new UserCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.login().setContains(keywords);
            keywordsCriteria.firstName().setContains(keywords);
            keywordsCriteria.lastName().setContains(keywords);
            keywordsCriteria.email().setContains(keywords);
            keywordsCriteria.mobile().setContains(keywords);
            keywordsCriteria.langKey().setContains(keywords);
            keywordsCriteria.imageUrl().setContains(keywords);
            UserCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        List<String> fields = CriteriaUtil.getNonNullBindQueryFields(criteria);
        QueryWrapper<User> queryWrapper = new DynamicJoinQueryWrapper<>(UserCriteria.class, fields);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, User.class);
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<User> createQueryWrapperNoJoin(UserCriteria criteria) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, User.class);
    }

    /**
     * Return a {@link IPage} of {@link UserDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UserDTO> findByQueryWrapper(QueryWrapper<User> queryWrapper, Page<User> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return userRepository.selectPage(page, queryWrapper).convert(userMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UserCriteria criteria) {
        QueryWrapper<User> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.login", criteria.getLogin());
        fieldNameMap.put("self.first_name", criteria.getFirstName());
        fieldNameMap.put("self.last_name", criteria.getLastName());
        fieldNameMap.put("self.email", criteria.getEmail());
        fieldNameMap.put("self.mobile", criteria.getMobile());
        fieldNameMap.put("self.birthday", criteria.getBirthday());
        fieldNameMap.put("self.activated", criteria.getActivated());
        fieldNameMap.put("self.lang_key", criteria.getLangKey());
        fieldNameMap.put("self.image_url", criteria.getImageUrl());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap.put("self.department_id", criteria.getDepartmentId());
        fieldNameMap.put("self.position_id", criteria.getPositionId());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, User.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
