package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.mapper.AuthorityMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.Authority}.
 */
@SuppressWarnings("UnusedReturnValue")
public class AuthorityBaseService<R extends AuthorityRepository, E extends Authority>
    extends BaseServiceImpl<AuthorityRepository, Authority> {

    private static final Logger log = LoggerFactory.getLogger(AuthorityBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.domain.Authority.class.getName() + ".parent",
        com.begcode.monolith.domain.ViewPermission.class.getName() + ".authorities",
        com.begcode.monolith.domain.ApiPermission.class.getName() + ".authorities",
        com.begcode.monolith.domain.Authority.class.getName() + ".children",
        com.begcode.monolith.domain.User.class.getName() + ".authorities",
        com.begcode.monolith.domain.Department.class.getName() + ".authorities"
    );
    private final List<String> relationNames = List.of("children", "viewPermissions", "apiPermissions", "parent", "users", "department");

    protected final AuthorityRepository authorityRepository;

    protected final CacheManager cacheManager;

    protected final AuthorityMapper authorityMapper;

    public AuthorityBaseService(AuthorityRepository authorityRepository, CacheManager cacheManager, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AuthorityDTO save(AuthorityDTO authorityDTO) {
        log.debug("Request to save Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        clearChildrenCache();

        this.createOrUpdateAndRelatedRelations(authority, Arrays.asList("viewPermissions", "apiPermissions"));
        return findOne(authority.getId()).orElseThrow();
    }

    /**
     * Update a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public AuthorityDTO update(AuthorityDTO authorityDTO) {
        log.debug("Request to update Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        clearChildrenCache();

        this.createOrUpdateAndRelatedRelations(authority, Arrays.asList("viewPermissions", "apiPermissions"));
        return findOne(authority.getId()).orElseThrow();
    }

    /**
     * Partially update a authority.
     *
     * @param authorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<AuthorityDTO> partialUpdate(AuthorityDTO authorityDTO) {
        log.debug("Request to partially update Authority : {}", authorityDTO);

        return authorityRepository
            .findById(authorityDTO.getId())
            .map(existingAuthority -> {
                authorityMapper.partialUpdate(existingAuthority, authorityDTO);

                return existingAuthority;
            })
            .map(tempAuthority -> {
                authorityRepository.save(tempAuthority);
                return authorityMapper.toDto(authorityRepository.selectById(tempAuthority.getId()));
            });
    }

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<AuthorityDTO> findAll(Page<Authority> pageable) {
        log.debug("Request to get all Authorities");
        return this.page(pageable).convert(authorityMapper::toDto);
    }

    /**
     * Get one authority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AuthorityDTO> findOne(Long id) {
        log.debug("Request to get Authority : {}", id);
        return Optional.ofNullable(authorityRepository.selectById(id))
            .map(authority -> {
                Binder.bindRelations(authority);
                return authority;
            })
            .map(authorityMapper::toDto);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete Authority : {}", id);

        Authority authority = authorityRepository.selectById(id);
        if (authority.getChildren() != null) {
            authority.getChildren().forEach(subAuthority -> subAuthority.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        // 关联属性设置为空集合
        authority.viewPermissions(new ArrayList<>()).apiPermissions(new ArrayList<>());
        this.createOrUpdateN2NRelations(authority, Arrays.asList("viewPermissions", "apiPermissions"));
        authorityRepository.deleteById(id);
    }

    /**
     * Get one authority by code.
     *
     * @param code the id of the entity.
     * @return the entity.
     */
    public Optional<AuthorityDTO> findFirstByCode(String code) {
        log.debug("Request to get Authority : {}", code);
        return authorityRepository.findFirstByCode(code).map(authorityMapper::toDto);
    }

    /**
     * Update specified field by authority
     */
    @Transactional
    public void updateBatch(AuthorityDTO changeAuthorityDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<Authority> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeAuthorityDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<Authority> authorityList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(authorityList)) {
                authorityList.forEach(authority -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                authority,
                                relationName,
                                BeanUtil.getFieldValue(authorityMapper.toEntity(changeAuthorityDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(authority, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.begcode.monolith.domain.Authority.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            Authority byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
            if (relationshipName.equals("users")) {
                if (CollectionUtils.isNotEmpty(otherEntityIds)) {
                    List<Long> ids = otherEntityIds.stream().map(Long::valueOf).toList();
                    List<User> userExist = byId.getUsers();
                    if (operateType.equals("add")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> userExist.stream().noneMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            collect.forEach(addId -> userExist.add(new User().id(addId)));
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("users"));
                        }
                    } else if (operateType.equals("delete")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> userExist.stream().anyMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            List<User> userAdd = userExist.stream().filter(vp -> !collect.contains(vp.getId())).toList();
                            byId.setUsers(userAdd);
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("users"));
                        }
                    }
                }
            }
        });
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<Authority> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(Authority::getOrder, changeSortValue).eq(Authority::getId, id).update();
                } else {
                    return false;
                }
            }
            case STEP: {
                if (ObjectUtils.allNotNull(id, beforeId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(beforeId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(authority -> authority.getOrder() != null)
                        .collect(Collectors.toMap(Authority::getId, Authority::getOrder));
                    return (
                        lambdaUpdate().set(Authority::getOrder, idSortValueMap.get(beforeId)).eq(Authority::getId, id).update() &&
                        lambdaUpdate().set(Authority::getOrder, idSortValueMap.get(id)).eq(Authority::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(authority -> authority.getOrder() != null)
                        .collect(Collectors.toMap(Authority::getId, Authority::getOrder));
                    return (
                        lambdaUpdate().set(Authority::getOrder, idSortValueMap.get(afterId)).eq(Authority::getId, id).update() &&
                        lambdaUpdate().set(Authority::getOrder, idSortValueMap.get(id)).eq(Authority::getId, afterId).update()
                    );
                } else {
                    return false;
                }
            }
            case DROP: {
                Set<Long> ids = new HashSet<>();
                ids.add(id);
                if (ObjectUtils.isNotEmpty(beforeId)) {
                    ids.add(beforeId);
                }
                if (ObjectUtils.isNotEmpty(afterId)) {
                    ids.add(afterId);
                }
                Map<Long, Integer> idSortValueMap = listByIds(ids)
                    .stream()
                    .filter(authority -> authority.getOrder() != null)
                    .collect(Collectors.toMap(Authority::getId, Authority::getOrder));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Authority::getOrder, newSortValue).eq(Authority::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Authority> list = this.list(queryWrapper.orderByAsc("order"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setOrder(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getOrder();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getOrder();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Authority::getOrder, newSortValue).eq(Authority::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(Authority::getOrder, newSortValue).eq(Authority::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Authority> list = this.list(queryWrapper.orderByAsc("order"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setOrder(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getOrder();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getOrder();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Authority::getOrder, newSortValue).eq(Authority::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Authority::getOrder, newSortValue).eq(Authority::getId, id).update();
                    }
                } else {
                    // todo 异常
                    return false;
                }
            }
            default:
                return false;
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
