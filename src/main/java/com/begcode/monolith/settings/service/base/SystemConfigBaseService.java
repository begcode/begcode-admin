package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.domain.SystemConfig;
import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.dto.SystemConfigDTO;
import com.begcode.monolith.settings.service.mapper.SystemConfigMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.SystemConfig}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SystemConfigBaseService<R extends SystemConfigRepository, E extends SystemConfig>
    extends BaseServiceImpl<SystemConfigRepository, SystemConfig> {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigBaseService.class);
    private final List<String> relationNames = List.of("items");

    protected final SystemConfigRepository systemConfigRepository;

    protected final CacheManager cacheManager;

    protected final SystemConfigMapper systemConfigMapper;

    public SystemConfigBaseService(
        SystemConfigRepository systemConfigRepository,
        CacheManager cacheManager,
        SystemConfigMapper systemConfigMapper
    ) {
        this.systemConfigRepository = systemConfigRepository;
        this.cacheManager = cacheManager;
        this.systemConfigMapper = systemConfigMapper;
    }

    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to save SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        this.createEntityAndRelatedEntities(
                systemConfig,
                systemConfig.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SystemConfig"
            );
        return findOne(systemConfig.getId()).orElseThrow();
    }

    /**
     * Update a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemConfigDTO update(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to update SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        this.updateEntityAndRelatedEntities(
                systemConfig,
                systemConfig.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SystemConfig"
            );
        return findOne(systemConfig.getId()).orElseThrow();
    }

    /**
     * Partially update a systemConfig.
     *
     * @param systemConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to partially update SystemConfig : {}", systemConfigDTO);

        return systemConfigRepository
            .findById(systemConfigDTO.getId())
            .map(existingSystemConfig -> {
                systemConfigMapper.partialUpdate(existingSystemConfig, systemConfigDTO);

                return existingSystemConfig;
            })
            .map(tempSystemConfig -> {
                systemConfigRepository.save(tempSystemConfig);
                return systemConfigMapper.toDto(systemConfigRepository.selectById(tempSystemConfig.getId()));
            });
    }

    /**
     * Get all the systemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SystemConfigDTO> findAll(Page<SystemConfig> pageable) {
        log.debug("Request to get all SystemConfigs");
        return this.page(pageable).convert(systemConfigMapper::toDto);
    }

    /**
     * Get one systemConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SystemConfigDTO> findOne(Long id) {
        log.debug("Request to get SystemConfig : {}", id);
        return Optional.ofNullable(systemConfigRepository.selectById(id))
            .map(systemConfig -> {
                Binder.bindRelations(systemConfig);
                return systemConfig;
            })
            .map(systemConfigMapper::toDto);
    }

    /**
     * Delete the systemConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SystemConfig : {}", id);

        this.deleteEntityAndRelatedEntities(
                id,
                CommonFieldData.class,
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SystemConfig"
            );
    }

    public <T> T getSystemConfigByName(String name, Class<T> t) {
        SystemConfig baseConfig = this.baseMapper.selectOne(new LambdaQueryWrapper<SystemConfig>().eq(SystemConfig::getCategoryKey, name));
        if (Objects.isNull(baseConfig)) {
            return null;
        }
        Binder.bindRelations(baseConfig);
        // 将baseConfig.getItems转为Map对象。
        Map<String, String> configMap = baseConfig
            .getItems()
            .stream()
            .collect(Collectors.toMap(CommonFieldData::getName, CommonFieldData::getValue, (k1, k2) -> k1));
        // 将configMap转为对应的对象。
        try {
            return BeanUtil.toBean(configMap, t);
        } catch (Exception e) {
            log.error("getSystemConfigByName error", e);
        }
        return null;
    }

    public Map<String, String> getMapByName(String name) {
        SystemConfig baseConfig = this.baseMapper.selectOne(new LambdaQueryWrapper<SystemConfig>().eq(SystemConfig::getCategoryKey, name));
        if (Objects.isNull(baseConfig)) {
            return new HashMap<>();
        }
        Binder.bindRelations(baseConfig);
        // 将baseConfig.getItems转为Map对象。
        Map<String, String> configMap = baseConfig
            .getItems()
            .stream()
            .collect(Collectors.toMap(CommonFieldData::getName, CommonFieldData::getValue, (k1, k2) -> k1));
        // 将configMap转为对应的对象。
        return configMap;
    }

    /**
     * Update specified field by systemConfig
     */
    @Transactional
    public void updateBatch(SystemConfigDTO changeSystemConfigDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SystemConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSystemConfigDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SystemConfig> systemConfigList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(systemConfigList)) {
                systemConfigList.forEach(systemConfig -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                systemConfig,
                                relationName,
                                BeanUtil.getFieldValue(systemConfigMapper.toEntity(changeSystemConfigDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(systemConfig, relationshipNames);
                });
            }
        }
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<SystemConfig> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(SystemConfig::getSortValue, changeSortValue).eq(SystemConfig::getId, id).update();
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
                        .filter(systemConfig -> systemConfig.getSortValue() != null)
                        .collect(Collectors.toMap(SystemConfig::getId, SystemConfig::getSortValue));
                    return (
                        lambdaUpdate().set(SystemConfig::getSortValue, idSortValueMap.get(beforeId)).eq(SystemConfig::getId, id).update() &&
                        lambdaUpdate().set(SystemConfig::getSortValue, idSortValueMap.get(id)).eq(SystemConfig::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(systemConfig -> systemConfig.getSortValue() != null)
                        .collect(Collectors.toMap(SystemConfig::getId, SystemConfig::getSortValue));
                    return (
                        lambdaUpdate().set(SystemConfig::getSortValue, idSortValueMap.get(afterId)).eq(SystemConfig::getId, id).update() &&
                        lambdaUpdate().set(SystemConfig::getSortValue, idSortValueMap.get(id)).eq(SystemConfig::getId, afterId).update()
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
                    .filter(systemConfig -> systemConfig.getSortValue() != null)
                    .collect(Collectors.toMap(SystemConfig::getId, SystemConfig::getSortValue));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(SystemConfig::getSortValue, newSortValue).eq(SystemConfig::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<SystemConfig> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(SystemConfig::getSortValue, newSortValue).eq(SystemConfig::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(SystemConfig::getSortValue, newSortValue).eq(SystemConfig::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<SystemConfig> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(SystemConfig::getSortValue, newSortValue).eq(SystemConfig::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(SystemConfig::getSortValue, newSortValue).eq(SystemConfig::getId, id).update();
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
