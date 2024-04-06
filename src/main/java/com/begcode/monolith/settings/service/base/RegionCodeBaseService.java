package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.dto.RegionCodeDTO;
import com.begcode.monolith.settings.service.mapper.RegionCodeMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.RegionCode}.
 */
@SuppressWarnings("UnusedReturnValue")
public class RegionCodeBaseService<R extends RegionCodeRepository, E extends RegionCode>
    extends BaseServiceImpl<RegionCodeRepository, RegionCode> {

    private final Logger log = LoggerFactory.getLogger(RegionCodeBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.settings.domain.RegionCode.class.getName() + ".parent",
        com.begcode.monolith.settings.domain.RegionCode.class.getName() + ".children"
    );
    private final List<String> relationNames = List.of("children", "parent");

    protected final RegionCodeRepository regionCodeRepository;

    protected final CacheManager cacheManager;

    protected final RegionCodeMapper regionCodeMapper;

    public RegionCodeBaseService(RegionCodeRepository regionCodeRepository, CacheManager cacheManager, RegionCodeMapper regionCodeMapper) {
        this.regionCodeRepository = regionCodeRepository;
        this.cacheManager = cacheManager;
        this.regionCodeMapper = regionCodeMapper;
    }

    /**
     * Save a regionCode.
     *
     * @param regionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public RegionCodeDTO save(RegionCodeDTO regionCodeDTO) {
        log.debug("Request to save RegionCode : {}", regionCodeDTO);
        RegionCode regionCode = regionCodeMapper.toEntity(regionCodeDTO);
        clearChildrenCache();

        this.saveOrUpdate(regionCode);
        return findOne(regionCode.getId()).orElseThrow();
    }

    /**
     * Update a regionCode.
     *
     * @param regionCodeDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public RegionCodeDTO update(RegionCodeDTO regionCodeDTO) {
        log.debug("Request to update RegionCode : {}", regionCodeDTO);
        RegionCode regionCode = regionCodeMapper.toEntity(regionCodeDTO);
        clearChildrenCache();

        this.saveOrUpdate(regionCode);
        return findOne(regionCode.getId()).orElseThrow();
    }

    /**
     * Partially update a regionCode.
     *
     * @param regionCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<RegionCodeDTO> partialUpdate(RegionCodeDTO regionCodeDTO) {
        log.debug("Request to partially update RegionCode : {}", regionCodeDTO);

        return regionCodeRepository
            .findById(regionCodeDTO.getId())
            .map(existingRegionCode -> {
                regionCodeMapper.partialUpdate(existingRegionCode, regionCodeDTO);

                return existingRegionCode;
            })
            .map(tempRegionCode -> {
                regionCodeRepository.save(tempRegionCode);
                return regionCodeMapper.toDto(regionCodeRepository.selectById(tempRegionCode.getId()));
            });
    }

    /**
     * Get all the regionCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<RegionCodeDTO> findAll(Page<RegionCode> pageable) {
        log.debug("Request to get all RegionCodes");
        return this.page(pageable).convert(regionCodeMapper::toDto);
    }

    /**
     * Get one regionCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<RegionCodeDTO> findOne(Long id) {
        log.debug("Request to get RegionCode : {}", id);
        return Optional.ofNullable(regionCodeRepository.selectById(id))
            .map(regionCode -> {
                Binder.bindRelations(regionCode);
                return regionCode;
            })
            .map(regionCodeMapper::toDto);
    }

    /**
     * Delete the regionCode by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete RegionCode : {}", id);

        RegionCode regionCode = regionCodeRepository.selectById(id);
        if (regionCode.getChildren() != null) {
            regionCode.getChildren().forEach(subRegionCode -> subRegionCode.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        regionCodeRepository.deleteById(id);
    }

    /**
     * Update specified field by regionCode
     */
    @Transactional
    public void updateBatch(RegionCodeDTO changeRegionCodeDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<RegionCode> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeRegionCodeDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<RegionCode> regionCodeList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(regionCodeList)) {
                regionCodeList.forEach(regionCode -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                regionCode,
                                relationName,
                                BeanUtil.getFieldValue(regionCodeMapper.toEntity(changeRegionCodeDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(regionCode, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(
            cacheManager.getCache(com.begcode.monolith.settings.domain.RegionCode.class.getName() + ".children")
        ).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            RegionCode byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
