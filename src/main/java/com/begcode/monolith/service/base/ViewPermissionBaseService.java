package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import com.begcode.monolith.service.mapper.ViewPermissionMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.domain.ViewPermission}.
 */
@SuppressWarnings("UnusedReturnValue")
public class ViewPermissionBaseService<R extends ViewPermissionRepository, E extends ViewPermission>
    extends BaseServiceImpl<ViewPermissionRepository, ViewPermission> {

    private static final Logger log = LoggerFactory.getLogger(ViewPermissionBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.domain.ViewPermission.class.getName() + ".parent",
        com.begcode.monolith.domain.ViewPermission.class.getName() + ".children",
        com.begcode.monolith.domain.Authority.class.getName() + ".viewPermissions"
    );
    private final List<String> relationNames = List.of("children", "parent", "authorities");

    protected final ViewPermissionRepository viewPermissionRepository;

    protected final CacheManager cacheManager;

    protected final ViewPermissionMapper viewPermissionMapper;

    public ViewPermissionBaseService(
        ViewPermissionRepository viewPermissionRepository,
        CacheManager cacheManager,
        ViewPermissionMapper viewPermissionMapper
    ) {
        this.viewPermissionRepository = viewPermissionRepository;
        this.cacheManager = cacheManager;
        this.viewPermissionMapper = viewPermissionMapper;
    }

    /**
     * Save a viewPermission.
     *
     * @param viewPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ViewPermissionDTO save(ViewPermissionDTO viewPermissionDTO) {
        log.debug("Request to save ViewPermission : {}", viewPermissionDTO);
        ViewPermission viewPermission = viewPermissionMapper.toEntity(viewPermissionDTO);
        clearChildrenCache();
        this.saveOrUpdate(viewPermission);
        return findOne(viewPermission.getId()).orElseThrow();
    }

    /**
     * Update a viewPermission.
     *
     * @param viewPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public ViewPermissionDTO update(ViewPermissionDTO viewPermissionDTO) {
        log.debug("Request to update ViewPermission : {}", viewPermissionDTO);
        ViewPermission viewPermission = viewPermissionMapper.toEntity(viewPermissionDTO);
        clearChildrenCache();
        this.saveOrUpdate(viewPermission);
        return findOne(viewPermission.getId()).orElseThrow();
    }

    /**
     * Partially update a viewPermission.
     *
     * @param viewPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ViewPermissionDTO> partialUpdate(ViewPermissionDTO viewPermissionDTO) {
        log.debug("Request to partially update ViewPermission : {}", viewPermissionDTO);

        return viewPermissionRepository
            .findById(viewPermissionDTO.getId())
            .map(existingViewPermission -> {
                viewPermissionMapper.partialUpdate(existingViewPermission, viewPermissionDTO);

                return existingViewPermission;
            })
            .map(tempViewPermission -> {
                viewPermissionRepository.save(tempViewPermission);
                return viewPermissionMapper.toDto(viewPermissionRepository.selectById(tempViewPermission.getId()));
            });
    }

    /**
     * Get all the viewPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ViewPermissionDTO> findAll(Page<ViewPermission> pageable) {
        log.debug("Request to get all ViewPermissions");
        return this.page(pageable).convert(viewPermissionMapper::toDto);
    }

    /**
     * Get one viewPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ViewPermissionDTO> findOne(Long id) {
        log.debug("Request to get ViewPermission : {}", id);
        return Optional.ofNullable(viewPermissionRepository.selectById(id))
            .map(viewPermission -> {
                Binder.bindRelations(viewPermission);
                return viewPermission;
            })
            .map(viewPermissionMapper::toDto);
    }

    /**
     * Delete the viewPermission by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete ViewPermission : {}", id);

        ViewPermission viewPermission = viewPermissionRepository.selectById(id);
        if (viewPermission.getChildren() != null) {
            viewPermission.getChildren().forEach(subViewPermission -> subViewPermission.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        viewPermissionRepository.deleteById(id);
    }

    /**
     * get all viewPermission by currentUserLogin
     *
     * @return List<ViewPermissionDTO>
     */
    public List<ViewPermissionDTO> getAllByLogin() {
        List<ViewPermission> resultList = new ArrayList<>();
        // 根据login获得用户的角色
        SecurityUtils.getCurrentUserId()
            .ifPresent(userId -> {
                List<ViewPermission> viewPermissionsByUserId = this.viewPermissionRepository.findAllViewPermissionsByUserId(userId);
                List<ViewPermission> addViewPermissions = new ArrayList<>();
                viewPermissionsByUserId.forEach(viewPermission -> {
                    Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                    while (viewPermission != null && viewPermission.getParent() != null) {
                        Long parentId = viewPermission.getParent().getId();
                        if (viewPermissionsByUserId.stream().noneMatch(viewPermissionDTO1 -> viewPermissionDTO1.getId().equals(parentId))) {
                            ViewPermission oneNoChildren = viewPermissionRepository.findById(parentId).orElse(null);
                            if (oneNoChildren != null) {
                                addViewPermissions.add(oneNoChildren);
                                viewPermission = oneNoChildren;
                                Binder.bindRelations(viewPermission, new String[] { "parent", "authorities" });
                            } else {
                                viewPermission = null;
                            }
                        } else {
                            viewPermission = null;
                        }
                    }
                });
                viewPermissionsByUserId.addAll(addViewPermissions);
                // 已经找到所有的当前User相关的ViewPermissions及上级，接下来转换为树形结构。
                viewPermissionsByUserId.forEach(userViewPermission -> {
                    if (userViewPermission.getParent() == null) {
                        Long finalId = userViewPermission.getId();
                        if (resultList.stream().noneMatch(resultViewPermissionDTO -> resultViewPermissionDTO.getId().equals(finalId))) {
                            resultList.add(userViewPermission);
                        }
                    } else {
                        ViewPermission topParent = null;
                        while (userViewPermission != null && userViewPermission.getParent() != null) {
                            Long tempId = userViewPermission.getParent().getId();
                            Optional<ViewPermission> tempViewPermission = viewPermissionsByUserId
                                .stream()
                                .filter(viewPermission -> viewPermission.getId().equals(tempId))
                                .findAny();
                            if (tempViewPermission.isPresent()) {
                                ViewPermission viewPermission = tempViewPermission.orElseThrow();
                                if (viewPermission.getParent() == null) {
                                    topParent = viewPermission;
                                    viewPermission.getChildren().add(userViewPermission);
                                    userViewPermission = null;
                                } else {
                                    viewPermission.getChildren().add(userViewPermission);
                                    userViewPermission = viewPermission;
                                }
                            } else {
                                userViewPermission = null;
                            }
                        }
                        if (topParent != null) {
                            ViewPermission finalTopParent = topParent;
                            Optional<ViewPermission> any = resultList
                                .stream()
                                .filter(resultViewPermission -> resultViewPermission.getId().equals(finalTopParent.getId()))
                                .findAny();
                            if (any.isPresent()) {
                                // 处理子列表
                                List<ViewPermission> resultChildren = any.orElseThrow().getChildren();
                                List<ViewPermission> unCheckChildren = topParent.getChildren();
                                addToResult(resultChildren, unCheckChildren);
                            } else {
                                resultList.add(topParent);
                            }
                        }
                    }
                });
            });
        return viewPermissionMapper.toDto(resultList);
    }

    private void addToResult(List<ViewPermission> resultChildren, List<ViewPermission> unCheckChildren) {
        if (!unCheckChildren.isEmpty()) {
            unCheckChildren.forEach(child -> {
                Long childId = child.getId();
                Optional<ViewPermission> any = resultChildren
                    .stream()
                    .filter(viewPermission -> viewPermission.getId().equals(childId))
                    .findFirst();
                if (any.isPresent()) {
                    addToResult(any.orElseThrow().getChildren(), child.getChildren());
                } else {
                    resultChildren.add(child);
                }
            });
        }
    }

    /**
     * Update specified field by viewPermission
     */
    @Transactional
    public void updateBatch(ViewPermissionDTO changeViewPermissionDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<ViewPermission> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeViewPermissionDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<ViewPermission> viewPermissionList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(viewPermissionList)) {
                viewPermissionList.forEach(viewPermission -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                viewPermission,
                                relationName,
                                BeanUtil.getFieldValue(viewPermissionMapper.toEntity(changeViewPermissionDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(viewPermission, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.begcode.monolith.domain.ViewPermission.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<ViewPermission> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(ViewPermission::getOrder, changeSortValue).eq(ViewPermission::getId, id).update();
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
                        .filter(viewPermission -> viewPermission.getOrder() != null)
                        .collect(Collectors.toMap(ViewPermission::getId, ViewPermission::getOrder));
                    return (
                        lambdaUpdate().set(ViewPermission::getOrder, idSortValueMap.get(beforeId)).eq(ViewPermission::getId, id).update() &&
                        lambdaUpdate().set(ViewPermission::getOrder, idSortValueMap.get(id)).eq(ViewPermission::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(viewPermission -> viewPermission.getOrder() != null)
                        .collect(Collectors.toMap(ViewPermission::getId, ViewPermission::getOrder));
                    return (
                        lambdaUpdate().set(ViewPermission::getOrder, idSortValueMap.get(afterId)).eq(ViewPermission::getId, id).update() &&
                        lambdaUpdate().set(ViewPermission::getOrder, idSortValueMap.get(id)).eq(ViewPermission::getId, afterId).update()
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
                    .filter(viewPermission -> viewPermission.getOrder() != null)
                    .collect(Collectors.toMap(ViewPermission::getId, ViewPermission::getOrder));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(ViewPermission::getOrder, newSortValue).eq(ViewPermission::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<ViewPermission> list = this.list(queryWrapper.orderByAsc("order"));
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
                        return lambdaUpdate().set(ViewPermission::getOrder, newSortValue).eq(ViewPermission::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(ViewPermission::getOrder, newSortValue).eq(ViewPermission::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<ViewPermission> list = this.list(queryWrapper.orderByAsc("order"));
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
                        return lambdaUpdate().set(ViewPermission::getOrder, newSortValue).eq(ViewPermission::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(ViewPermission::getOrder, newSortValue).eq(ViewPermission::getId, id).update();
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
