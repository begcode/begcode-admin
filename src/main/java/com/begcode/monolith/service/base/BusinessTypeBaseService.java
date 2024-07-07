package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.repository.BusinessTypeRepository;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
import com.begcode.monolith.service.mapper.BusinessTypeMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.BusinessType}.
 */
@SuppressWarnings("UnusedReturnValue")
public class BusinessTypeBaseService<R extends BusinessTypeRepository, E extends BusinessType>
    extends BaseServiceImpl<BusinessTypeRepository, BusinessType> {

    private static final Logger log = LoggerFactory.getLogger(BusinessTypeBaseService.class);
    private final List<String> relationNames = List.of();

    protected final BusinessTypeRepository businessTypeRepository;

    protected final CacheManager cacheManager;

    protected final BusinessTypeMapper businessTypeMapper;

    public BusinessTypeBaseService(
        BusinessTypeRepository businessTypeRepository,
        CacheManager cacheManager,
        BusinessTypeMapper businessTypeMapper
    ) {
        this.businessTypeRepository = businessTypeRepository;
        this.cacheManager = cacheManager;
        this.businessTypeMapper = businessTypeMapper;
    }

    /**
     * Save a businessType.
     *
     * @param businessTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public BusinessTypeDTO save(BusinessTypeDTO businessTypeDTO) {
        log.debug("Request to save BusinessType : {}", businessTypeDTO);
        BusinessType businessType = businessTypeMapper.toEntity(businessTypeDTO);
        this.saveOrUpdate(businessType);
        return findOne(businessType.getId()).orElseThrow();
    }

    /**
     * Update a businessType.
     *
     * @param businessTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessTypeDTO update(BusinessTypeDTO businessTypeDTO) {
        log.debug("Request to update BusinessType : {}", businessTypeDTO);
        BusinessType businessType = businessTypeMapper.toEntity(businessTypeDTO);
        this.saveOrUpdate(businessType);
        return findOne(businessType.getId()).orElseThrow();
    }

    /**
     * Partially update a businessType.
     *
     * @param businessTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<BusinessTypeDTO> partialUpdate(BusinessTypeDTO businessTypeDTO) {
        log.debug("Request to partially update BusinessType : {}", businessTypeDTO);

        return businessTypeRepository
            .findById(businessTypeDTO.getId())
            .map(existingBusinessType -> {
                businessTypeMapper.partialUpdate(existingBusinessType, businessTypeDTO);

                return existingBusinessType;
            })
            .map(tempBusinessType -> {
                businessTypeRepository.save(tempBusinessType);
                return businessTypeMapper.toDto(businessTypeRepository.selectById(tempBusinessType.getId()));
            });
    }

    /**
     * Get all the businessTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<BusinessTypeDTO> findAll(Page<BusinessType> pageable) {
        log.debug("Request to get all BusinessTypes");
        return this.page(pageable).convert(businessTypeMapper::toDto);
    }

    /**
     * Get one businessType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<BusinessTypeDTO> findOne(Long id) {
        log.debug("Request to get BusinessType : {}", id);
        return Optional.ofNullable(businessTypeRepository.selectById(id))
            .map(businessType -> {
                Binder.bindRelations(businessType);
                return businessType;
            })
            .map(businessTypeMapper::toDto);
    }

    /**
     * Delete the businessType by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete BusinessType : {}", id);

        businessTypeRepository.deleteById(id);
    }

    /**
     * Update specified field by businessType
     */
    @Transactional
    public void updateBatch(BusinessTypeDTO changeBusinessTypeDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<BusinessType> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeBusinessTypeDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<BusinessType> businessTypeList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(businessTypeList)) {
                businessTypeList.forEach(businessType -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                businessType,
                                relationName,
                                BeanUtil.getFieldValue(businessTypeMapper.toEntity(changeBusinessTypeDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(businessType, relationshipNames);
                });
            }
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
