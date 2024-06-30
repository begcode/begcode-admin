package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.CommonFieldDataRepository;
import com.begcode.monolith.settings.service.dto.CommonFieldDataDTO;
import com.begcode.monolith.settings.service.mapper.CommonFieldDataMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.CommonFieldData}.
 */
@SuppressWarnings("UnusedReturnValue")
public class CommonFieldDataBaseService<R extends CommonFieldDataRepository, E extends CommonFieldData>
    extends BaseServiceImpl<CommonFieldDataRepository, CommonFieldData> {

    private static final Logger log = LoggerFactory.getLogger(CommonFieldDataBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.settings.domain.SiteConfig.class.getName() + ".items",
        com.begcode.monolith.settings.domain.Dictionary.class.getName() + ".items"
    );
    private final List<String> relationNames = List.of("siteConfig", "dictionary");

    protected final CommonFieldDataRepository commonFieldDataRepository;

    protected final CacheManager cacheManager;

    protected final CommonFieldDataMapper commonFieldDataMapper;

    public CommonFieldDataBaseService(
        CommonFieldDataRepository commonFieldDataRepository,
        CacheManager cacheManager,
        CommonFieldDataMapper commonFieldDataMapper
    ) {
        this.commonFieldDataRepository = commonFieldDataRepository;
        this.cacheManager = cacheManager;
        this.commonFieldDataMapper = commonFieldDataMapper;
    }

    /**
     * Save a commonFieldData.
     *
     * @param commonFieldDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public CommonFieldDataDTO save(CommonFieldDataDTO commonFieldDataDTO) {
        log.debug("Request to save CommonFieldData : {}", commonFieldDataDTO);
        CommonFieldData commonFieldData = commonFieldDataMapper.toEntity(commonFieldDataDTO);

        this.saveOrUpdate(commonFieldData);
        return findOne(commonFieldData.getId()).orElseThrow();
    }

    /**
     * Update a commonFieldData.
     *
     * @param commonFieldDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonFieldDataDTO update(CommonFieldDataDTO commonFieldDataDTO) {
        log.debug("Request to update CommonFieldData : {}", commonFieldDataDTO);
        CommonFieldData commonFieldData = commonFieldDataMapper.toEntity(commonFieldDataDTO);

        this.saveOrUpdate(commonFieldData);
        return findOne(commonFieldData.getId()).orElseThrow();
    }

    /**
     * Partially update a commonFieldData.
     *
     * @param commonFieldDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<CommonFieldDataDTO> partialUpdate(CommonFieldDataDTO commonFieldDataDTO) {
        log.debug("Request to partially update CommonFieldData : {}", commonFieldDataDTO);

        return commonFieldDataRepository
            .findById(commonFieldDataDTO.getId())
            .map(existingCommonFieldData -> {
                commonFieldDataMapper.partialUpdate(existingCommonFieldData, commonFieldDataDTO);

                return existingCommonFieldData;
            })
            .map(tempCommonFieldData -> {
                commonFieldDataRepository.save(tempCommonFieldData);
                return commonFieldDataMapper.toDto(commonFieldDataRepository.selectById(tempCommonFieldData.getId()));
            });
    }

    /**
     * Get all the commonFieldData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<CommonFieldDataDTO> findAll(Page<CommonFieldData> pageable) {
        log.debug("Request to get all CommonFieldData");
        return this.page(pageable).convert(commonFieldDataMapper::toDto);
    }

    /**
     * Get one commonFieldData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<CommonFieldDataDTO> findOne(Long id) {
        log.debug("Request to get CommonFieldData : {}", id);
        return Optional.ofNullable(commonFieldDataRepository.selectById(id))
            .map(commonFieldData -> {
                Binder.bindRelations(commonFieldData);
                return commonFieldData;
            })
            .map(commonFieldDataMapper::toDto);
    }

    /**
     * Delete the commonFieldData by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete CommonFieldData : {}", id);

        commonFieldDataRepository.deleteById(id);
    }

    /**
     * Update specified field by commonFieldData
     */
    @Transactional
    public void updateBatch(CommonFieldDataDTO changeCommonFieldDataDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<CommonFieldData> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeCommonFieldDataDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<CommonFieldData> commonFieldDataList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(commonFieldDataList)) {
                commonFieldDataList.forEach(commonFieldData -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                commonFieldData,
                                relationName,
                                BeanUtil.getFieldValue(commonFieldDataMapper.toEntity(changeCommonFieldDataDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(commonFieldData, relationshipNames);
                });
            }
        }
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            CommonFieldData byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<CommonFieldData> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(CommonFieldData::getSortValue, changeSortValue).eq(CommonFieldData::getId, id).update();
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
                        .filter(commonFieldData -> commonFieldData.getSortValue() != null)
                        .collect(Collectors.toMap(CommonFieldData::getId, CommonFieldData::getSortValue));
                    return (
                        lambdaUpdate()
                            .set(CommonFieldData::getSortValue, idSortValueMap.get(beforeId))
                            .eq(CommonFieldData::getId, id)
                            .update() &&
                        lambdaUpdate()
                            .set(CommonFieldData::getSortValue, idSortValueMap.get(id))
                            .eq(CommonFieldData::getId, beforeId)
                            .update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(commonFieldData -> commonFieldData.getSortValue() != null)
                        .collect(Collectors.toMap(CommonFieldData::getId, CommonFieldData::getSortValue));
                    return (
                        lambdaUpdate()
                            .set(CommonFieldData::getSortValue, idSortValueMap.get(afterId))
                            .eq(CommonFieldData::getId, id)
                            .update() &&
                        lambdaUpdate()
                            .set(CommonFieldData::getSortValue, idSortValueMap.get(id))
                            .eq(CommonFieldData::getId, afterId)
                            .update()
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
                    .filter(commonFieldData -> commonFieldData.getSortValue() != null)
                    .collect(Collectors.toMap(CommonFieldData::getId, CommonFieldData::getSortValue));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(CommonFieldData::getSortValue, newSortValue).eq(CommonFieldData::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<CommonFieldData> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(CommonFieldData::getSortValue, newSortValue).eq(CommonFieldData::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(CommonFieldData::getSortValue, newSortValue).eq(CommonFieldData::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<CommonFieldData> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(CommonFieldData::getSortValue, newSortValue).eq(CommonFieldData::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(CommonFieldData::getSortValue, newSortValue).eq(CommonFieldData::getId, id).update();
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
