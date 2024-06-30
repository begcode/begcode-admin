package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.dto.SiteConfigDTO;
import com.begcode.monolith.settings.service.mapper.SiteConfigMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.SiteConfig}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SiteConfigBaseService<R extends SiteConfigRepository, E extends SiteConfig>
    extends BaseServiceImpl<SiteConfigRepository, SiteConfig> {

    private static final Logger log = LoggerFactory.getLogger(SiteConfigBaseService.class);
    private final List<String> relationNames = List.of("items");

    protected final SiteConfigRepository siteConfigRepository;

    protected final CacheManager cacheManager;

    protected final SiteConfigMapper siteConfigMapper;

    public SiteConfigBaseService(SiteConfigRepository siteConfigRepository, CacheManager cacheManager, SiteConfigMapper siteConfigMapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.cacheManager = cacheManager;
        this.siteConfigMapper = siteConfigMapper;
    }

    /**
     * Save a siteConfig.
     *
     * @param siteConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SiteConfigDTO save(SiteConfigDTO siteConfigDTO) {
        log.debug("Request to save SiteConfig : {}", siteConfigDTO);
        SiteConfig siteConfig = siteConfigMapper.toEntity(siteConfigDTO);

        this.createEntityAndRelatedEntities(
                siteConfig,
                siteConfig.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SiteConfig"
            );
        return findOne(siteConfig.getId()).orElseThrow();
    }

    /**
     * Update a siteConfig.
     *
     * @param siteConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SiteConfigDTO update(SiteConfigDTO siteConfigDTO) {
        log.debug("Request to update SiteConfig : {}", siteConfigDTO);
        SiteConfig siteConfig = siteConfigMapper.toEntity(siteConfigDTO);

        this.createEntityAndRelatedEntities(
                siteConfig,
                siteConfig.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SiteConfig"
            );
        return findOne(siteConfig.getId()).orElseThrow();
    }

    /**
     * Partially update a siteConfig.
     *
     * @param siteConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SiteConfigDTO> partialUpdate(SiteConfigDTO siteConfigDTO) {
        log.debug("Request to partially update SiteConfig : {}", siteConfigDTO);

        return siteConfigRepository
            .findById(siteConfigDTO.getId())
            .map(existingSiteConfig -> {
                siteConfigMapper.partialUpdate(existingSiteConfig, siteConfigDTO);

                return existingSiteConfig;
            })
            .map(tempSiteConfig -> {
                siteConfigRepository.save(tempSiteConfig);
                return siteConfigMapper.toDto(siteConfigRepository.selectById(tempSiteConfig.getId()));
            });
    }

    /**
     * Get all the siteConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SiteConfigDTO> findAll(Page<SiteConfig> pageable) {
        log.debug("Request to get all SiteConfigs");
        return this.page(pageable).convert(siteConfigMapper::toDto);
    }

    /**
     * Get one siteConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SiteConfigDTO> findOne(Long id) {
        log.debug("Request to get SiteConfig : {}", id);
        return Optional.ofNullable(siteConfigRepository.selectById(id))
            .map(siteConfig -> {
                Binder.bindRelations(siteConfig);
                return siteConfig;
            })
            .map(siteConfigMapper::toDto);
    }

    /**
     * Delete the siteConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SiteConfig : {}", id);

        this.deleteEntityAndRelatedEntities(
                id,
                CommonFieldData.class,
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "SiteConfig"
            );
    }

    public <T> T getSiteConfigByName(String name, Class<T> t) {
        SiteConfig baseConfig = this.baseMapper.selectOne(new LambdaQueryWrapper<SiteConfig>().eq(SiteConfig::getCategoryKey, name));
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
            log.error("getSiteConfigByName error", e);
        }
        return null;
    }

    /**
     * Update specified field by siteConfig
     */
    @Transactional
    public void updateBatch(SiteConfigDTO changeSiteConfigDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SiteConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSiteConfigDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SiteConfig> siteConfigList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(siteConfigList)) {
                siteConfigList.forEach(siteConfig -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                siteConfig,
                                relationName,
                                BeanUtil.getFieldValue(siteConfigMapper.toEntity(changeSiteConfigDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(siteConfig, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            SiteConfig byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<SiteConfig> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(SiteConfig::getSortValue, changeSortValue).eq(SiteConfig::getId, id).update();
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
                        .filter(siteConfig -> siteConfig.getSortValue() != null)
                        .collect(Collectors.toMap(SiteConfig::getId, SiteConfig::getSortValue));
                    return (
                        lambdaUpdate().set(SiteConfig::getSortValue, idSortValueMap.get(beforeId)).eq(SiteConfig::getId, id).update() &&
                        lambdaUpdate().set(SiteConfig::getSortValue, idSortValueMap.get(id)).eq(SiteConfig::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(siteConfig -> siteConfig.getSortValue() != null)
                        .collect(Collectors.toMap(SiteConfig::getId, SiteConfig::getSortValue));
                    return (
                        lambdaUpdate().set(SiteConfig::getSortValue, idSortValueMap.get(afterId)).eq(SiteConfig::getId, id).update() &&
                        lambdaUpdate().set(SiteConfig::getSortValue, idSortValueMap.get(id)).eq(SiteConfig::getId, afterId).update()
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
                    .filter(siteConfig -> siteConfig.getSortValue() != null)
                    .collect(Collectors.toMap(SiteConfig::getId, SiteConfig::getSortValue));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(SiteConfig::getSortValue, newSortValue).eq(SiteConfig::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<SiteConfig> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(SiteConfig::getSortValue, newSortValue).eq(SiteConfig::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(SiteConfig::getSortValue, newSortValue).eq(SiteConfig::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<SiteConfig> list = this.list(queryWrapper.orderByAsc("sort_value"));
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
                        return lambdaUpdate().set(SiteConfig::getSortValue, newSortValue).eq(SiteConfig::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(SiteConfig::getSortValue, newSortValue).eq(SiteConfig::getId, id).update();
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
