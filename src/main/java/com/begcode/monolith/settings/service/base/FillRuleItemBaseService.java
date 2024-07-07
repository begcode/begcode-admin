package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.repository.FillRuleItemRepository;
import com.begcode.monolith.settings.service.dto.FillRuleItemDTO;
import com.begcode.monolith.settings.service.mapper.FillRuleItemMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.FillRuleItem}.
 */
@SuppressWarnings("UnusedReturnValue")
public class FillRuleItemBaseService<R extends FillRuleItemRepository, E extends FillRuleItem>
    extends BaseServiceImpl<FillRuleItemRepository, FillRuleItem> {

    private static final Logger log = LoggerFactory.getLogger(FillRuleItemBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.settings.domain.SysFillRule.class.getName() + ".ruleItems"
    );
    private final List<String> relationNames = List.of("fillRule");

    protected final FillRuleItemRepository fillRuleItemRepository;

    protected final CacheManager cacheManager;

    protected final FillRuleItemMapper fillRuleItemMapper;

    public FillRuleItemBaseService(
        FillRuleItemRepository fillRuleItemRepository,
        CacheManager cacheManager,
        FillRuleItemMapper fillRuleItemMapper
    ) {
        this.fillRuleItemRepository = fillRuleItemRepository;
        this.cacheManager = cacheManager;
        this.fillRuleItemMapper = fillRuleItemMapper;
    }

    /**
     * Save a fillRuleItem.
     *
     * @param fillRuleItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public FillRuleItemDTO save(FillRuleItemDTO fillRuleItemDTO) {
        log.debug("Request to save FillRuleItem : {}", fillRuleItemDTO);
        FillRuleItem fillRuleItem = fillRuleItemMapper.toEntity(fillRuleItemDTO);
        this.saveOrUpdate(fillRuleItem);
        return findOne(fillRuleItem.getId()).orElseThrow();
    }

    /**
     * Update a fillRuleItem.
     *
     * @param fillRuleItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public FillRuleItemDTO update(FillRuleItemDTO fillRuleItemDTO) {
        log.debug("Request to update FillRuleItem : {}", fillRuleItemDTO);
        FillRuleItem fillRuleItem = fillRuleItemMapper.toEntity(fillRuleItemDTO);
        this.saveOrUpdate(fillRuleItem);
        return findOne(fillRuleItem.getId()).orElseThrow();
    }

    /**
     * Partially update a fillRuleItem.
     *
     * @param fillRuleItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<FillRuleItemDTO> partialUpdate(FillRuleItemDTO fillRuleItemDTO) {
        log.debug("Request to partially update FillRuleItem : {}", fillRuleItemDTO);

        return fillRuleItemRepository
            .findById(fillRuleItemDTO.getId())
            .map(existingFillRuleItem -> {
                fillRuleItemMapper.partialUpdate(existingFillRuleItem, fillRuleItemDTO);

                return existingFillRuleItem;
            })
            .map(tempFillRuleItem -> {
                fillRuleItemRepository.save(tempFillRuleItem);
                return fillRuleItemMapper.toDto(fillRuleItemRepository.selectById(tempFillRuleItem.getId()));
            });
    }

    /**
     * Get all the fillRuleItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<FillRuleItemDTO> findAll(Page<FillRuleItem> pageable) {
        log.debug("Request to get all FillRuleItems");
        return this.page(pageable).convert(fillRuleItemMapper::toDto);
    }

    /**
     * Get one fillRuleItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FillRuleItemDTO> findOne(Long id) {
        log.debug("Request to get FillRuleItem : {}", id);
        return Optional.ofNullable(fillRuleItemRepository.selectById(id))
            .map(fillRuleItem -> {
                Binder.bindRelations(fillRuleItem);
                return fillRuleItem;
            })
            .map(fillRuleItemMapper::toDto);
    }

    /**
     * Delete the fillRuleItem by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete FillRuleItem : {}", id);

        fillRuleItemRepository.deleteById(id);
    }

    /**
     * Update specified field by fillRuleItem
     */
    @Transactional
    public void updateBatch(FillRuleItemDTO changeFillRuleItemDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<FillRuleItem> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeFillRuleItemDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<FillRuleItem> fillRuleItemList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(fillRuleItemList)) {
                fillRuleItemList.forEach(fillRuleItem -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                fillRuleItem,
                                relationName,
                                BeanUtil.getFieldValue(fillRuleItemMapper.toEntity(changeFillRuleItemDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(fillRuleItem, relationshipNames);
                });
            }
        }
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
        QueryWrapper<FillRuleItem> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(FillRuleItem::getSortValue, changeSortValue).eq(FillRuleItem::getId, id).update();
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
                        .filter(fillRuleItem -> fillRuleItem.getSortValue() != null)
                        .collect(Collectors.toMap(FillRuleItem::getId, FillRuleItem::getSortValue));
                    return (
                        lambdaUpdate().set(FillRuleItem::getSortValue, idSortValueMap.get(beforeId)).eq(FillRuleItem::getId, id).update() &&
                        lambdaUpdate().set(FillRuleItem::getSortValue, idSortValueMap.get(id)).eq(FillRuleItem::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(fillRuleItem -> fillRuleItem.getSortValue() != null)
                        .collect(Collectors.toMap(FillRuleItem::getId, FillRuleItem::getSortValue));
                    return (
                        lambdaUpdate().set(FillRuleItem::getSortValue, idSortValueMap.get(afterId)).eq(FillRuleItem::getId, id).update() &&
                        lambdaUpdate().set(FillRuleItem::getSortValue, idSortValueMap.get(id)).eq(FillRuleItem::getId, afterId).update()
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
                    .filter(fillRuleItem -> fillRuleItem.getSortValue() != null)
                    .collect(Collectors.toMap(FillRuleItem::getId, FillRuleItem::getSortValue));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(FillRuleItem::getSortValue, newSortValue).eq(FillRuleItem::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<FillRuleItem> list = this.list(queryWrapper.orderByAsc("sort_value"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortValue(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortValue();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortValue();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(FillRuleItem::getSortValue, newSortValue).eq(FillRuleItem::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(FillRuleItem::getSortValue, newSortValue).eq(FillRuleItem::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<FillRuleItem> list = this.list(queryWrapper.orderByAsc("sort_value"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortValue(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortValue();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortValue();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(FillRuleItem::getSortValue, newSortValue).eq(FillRuleItem::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(FillRuleItem::getSortValue, newSortValue).eq(FillRuleItem::getId, id).update();
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
