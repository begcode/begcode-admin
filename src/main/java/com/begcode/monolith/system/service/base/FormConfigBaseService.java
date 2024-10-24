package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.SpringBootUtil;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.FormConfigRepository;
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
import com.begcode.monolith.system.service.mapper.FormConfigMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.FormConfig}.
 */
@SuppressWarnings("UnusedReturnValue")
public class FormConfigBaseService<R extends FormConfigRepository, E extends FormConfig>
    extends BaseServiceImpl<FormConfigRepository, FormConfig> {

    private static final Logger log = LoggerFactory.getLogger(FormConfigBaseService.class);
    private final List<String> relationNames = List.of("businessType");

    protected final FormConfigRepository formConfigRepository;

    protected final CacheManager cacheManager;

    protected final FormConfigMapper formConfigMapper;

    public FormConfigBaseService(FormConfigRepository formConfigRepository, CacheManager cacheManager, FormConfigMapper formConfigMapper) {
        this.formConfigRepository = formConfigRepository;
        this.cacheManager = cacheManager;
        this.formConfigMapper = formConfigMapper;
    }

    /**
     * Save a formConfig.
     *
     * @param formConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public FormConfigDTO save(FormConfigDTO formConfigDTO) {
        log.debug("Request to save FormConfig : {}", formConfigDTO);
        FormConfig formConfig = formConfigMapper.toEntity(formConfigDTO);
        formConfig.setBusinessTypeId(
            Optional.ofNullable(formConfigDTO.getBusinessType())
                .map(businessTypeBusinessTypeDTO -> businessTypeBusinessTypeDTO.getId())
                .orElse(null)
        );
        this.saveOrUpdate(formConfig);
        return findOne(formConfig.getId()).orElseThrow();
    }

    /**
     * Update a formConfig.
     *
     * @param formConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public FormConfigDTO update(FormConfigDTO formConfigDTO) {
        log.debug("Request to update FormConfig : {}", formConfigDTO);
        FormConfig formConfig = formConfigMapper.toEntity(formConfigDTO);
        formConfig.setBusinessTypeId(
            Optional.ofNullable(formConfigDTO.getBusinessType())
                .map(businessTypeBusinessTypeDTO -> businessTypeBusinessTypeDTO.getId())
                .orElse(null)
        );
        this.saveOrUpdate(formConfig);
        return findOne(formConfig.getId()).orElseThrow();
    }

    /**
     * Partially update a formConfig.
     *
     * @param formConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<FormConfigDTO> partialUpdate(FormConfigDTO formConfigDTO) {
        log.debug("Request to partially update FormConfig : {}", formConfigDTO);

        return formConfigRepository
            .findById(formConfigDTO.getId())
            .map(existingFormConfig -> {
                formConfigMapper.partialUpdate(existingFormConfig, formConfigDTO);

                return existingFormConfig;
            })
            .map(tempFormConfig -> {
                formConfigRepository.save(tempFormConfig);
                return formConfigMapper.toDto(formConfigRepository.selectById(tempFormConfig.getId()));
            });
    }

    /**
     * Get all the formConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<FormConfigDTO> findAll(Page<FormConfig> pageable) {
        log.debug("Request to get all FormConfigs");
        return this.page(pageable).convert(formConfigMapper::toDto);
    }

    /**
     * Get one formConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FormConfigDTO> findOne(Long id) {
        log.debug("Request to get FormConfig : {}", id);
        return Optional.ofNullable(formConfigRepository.selectById(id))
            .map(formConfig -> {
                Binder.bindRelations(formConfig);
                return formConfig;
            })
            .map(formConfigMapper::toDto);
    }

    /**
     * Delete the formConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete FormConfig : {}", id);

        formConfigRepository.deleteById(id);
    }

    public List<String> getFormDataByKey(String formKey) {
        FormConfig formConfig = this.baseMapper.selectOne(new LambdaQueryWrapper<FormConfig>().eq(FormConfig::getFormKey, formKey));
        if (Objects.isNull(formConfig)) {
            return new ArrayList<>();
        }
        FormSaveDataRepository saveDataRepository = SpringBootUtil.getBean(FormSaveDataRepository.class);
        List<FormSaveData> formDataList = saveDataRepository.selectList(
            new LambdaQueryWrapper<FormSaveData>().eq(FormSaveData::getFormConfigId, formConfig.getId())
        );
        return formDataList.stream().map(FormSaveData::getFormData).toList();
    }

    /**
     * Update specified field by formConfig
     */
    @Transactional
    public void updateBatch(FormConfigDTO changeFormConfigDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<FormConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeFormConfigDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<FormConfig> formConfigList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(formConfigList)) {
                formConfigList.forEach(formConfig -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                formConfig,
                                relationName,
                                BeanUtil.getFieldValue(formConfigMapper.toEntity(changeFormConfigDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(formConfig, relationshipNames);
                });
            }
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
