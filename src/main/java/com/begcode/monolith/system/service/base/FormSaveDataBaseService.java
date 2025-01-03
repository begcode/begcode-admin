package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.dto.FormSaveDataDTO;
import com.begcode.monolith.system.service.mapper.FormSaveDataMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.FormSaveData}.
 */
@SuppressWarnings("UnusedReturnValue")
public class FormSaveDataBaseService<R extends FormSaveDataRepository, E extends FormSaveData>
    extends BaseServiceImpl<FormSaveDataRepository, FormSaveData> {

    private static final Logger log = LoggerFactory.getLogger(FormSaveDataBaseService.class);
    private final List<String> relationNames = List.of("formConfig");

    protected final FormSaveDataRepository formSaveDataRepository;

    protected final CacheManager cacheManager;

    protected final FormSaveDataMapper formSaveDataMapper;

    public FormSaveDataBaseService(
        FormSaveDataRepository formSaveDataRepository,
        CacheManager cacheManager,
        FormSaveDataMapper formSaveDataMapper
    ) {
        this.formSaveDataRepository = formSaveDataRepository;
        this.cacheManager = cacheManager;
        this.formSaveDataMapper = formSaveDataMapper;
    }

    /**
     * Save a formSaveData.
     *
     * @param formSaveDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public FormSaveDataDTO save(FormSaveDataDTO formSaveDataDTO) {
        log.debug("Request to save FormSaveData : {}", formSaveDataDTO);
        FormSaveData formSaveData = formSaveDataMapper.toEntity(formSaveDataDTO);
        formSaveData.setFormConfigId(Optional.ofNullable(formSaveData.getFormConfig()).map(FormConfig::getId).orElse(null));
        this.saveOrUpdate(formSaveData);
        return findOne(formSaveData.getId()).orElseThrow();
    }

    /**
     * Update a formSaveData.
     *
     * @param formSaveDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public FormSaveDataDTO update(FormSaveDataDTO formSaveDataDTO) {
        log.debug("Request to update FormSaveData : {}", formSaveDataDTO);
        FormSaveData formSaveData = formSaveDataMapper.toEntity(formSaveDataDTO);
        formSaveData.setFormConfigId(Optional.ofNullable(formSaveData.getFormConfig()).map(FormConfig::getId).orElse(null));
        this.saveOrUpdate(formSaveData);
        return findOne(formSaveData.getId()).orElseThrow();
    }

    /**
     * Partially update a formSaveData.
     *
     * @param formSaveDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<FormSaveDataDTO> partialUpdate(FormSaveDataDTO formSaveDataDTO) {
        log.debug("Request to partially update FormSaveData : {}", formSaveDataDTO);

        return formSaveDataRepository
            .findById(formSaveDataDTO.getId())
            .map(existingFormSaveData -> {
                formSaveDataMapper.partialUpdate(existingFormSaveData, formSaveDataDTO);

                return existingFormSaveData;
            })
            .map(tempFormSaveData -> {
                formSaveDataRepository.save(tempFormSaveData);
                return formSaveDataMapper.toDto(formSaveDataRepository.selectById(tempFormSaveData.getId()));
            });
    }

    /**
     * Get all the formSaveData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<FormSaveDataDTO> findAll(Page<FormSaveData> pageable) {
        log.debug("Request to get all FormSaveData");
        return this.page(pageable).convert(formSaveDataMapper::toDto);
    }

    /**
     * Get one formSaveData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FormSaveDataDTO> findOne(Long id) {
        log.debug("Request to get FormSaveData : {}", id);
        return Optional.ofNullable(formSaveDataRepository.selectById(id))
            .map(formSaveData -> {
                Binder.bindRelations(formSaveData);
                return formSaveData;
            })
            .map(formSaveDataMapper::toDto);
    }

    /**
     * Delete the formSaveData by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete FormSaveData : {}", id);

        formSaveDataRepository.deleteById(id);
    }

    /**
     * Update specified field by formSaveData
     */
    @Transactional
    public void updateBatch(FormSaveDataDTO changeFormSaveDataDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<FormSaveData> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeFormSaveDataDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<FormSaveData> formSaveDataList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(formSaveDataList)) {
                formSaveDataList.forEach(formSaveData -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                formSaveData,
                                relationName,
                                BeanUtil.getFieldValue(formSaveDataMapper.toEntity(changeFormSaveDataDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(formSaveData, relationshipNames);
                });
            }
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
