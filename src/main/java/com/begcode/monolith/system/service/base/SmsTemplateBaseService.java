package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.repository.SmsTemplateRepository;
import com.begcode.monolith.system.service.SmsSupplierService;
import com.begcode.monolith.system.service.dto.SmsTemplateDTO;
import com.begcode.monolith.system.service.mapper.SmsTemplateMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.SmsTemplate}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SmsTemplateBaseService<R extends SmsTemplateRepository, E extends SmsTemplate>
    extends BaseServiceImpl<SmsTemplateRepository, SmsTemplate> {

    private static final Logger log = LoggerFactory.getLogger(SmsTemplateBaseService.class);
    private final List<String> relationNames = List.of("supplier");

    protected final SmsSupplierService smsSupplierService;

    protected final SmsTemplateRepository smsTemplateRepository;

    protected final CacheManager cacheManager;

    protected final SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateBaseService(
        SmsSupplierService smsSupplierService,
        SmsTemplateRepository smsTemplateRepository,
        CacheManager cacheManager,
        SmsTemplateMapper smsTemplateMapper
    ) {
        this.smsSupplierService = smsSupplierService;
        this.smsTemplateRepository = smsTemplateRepository;
        this.cacheManager = cacheManager;
        this.smsTemplateMapper = smsTemplateMapper;
    }

    /**
     * Save a smsTemplate.
     *
     * @param smsTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SmsTemplateDTO save(SmsTemplateDTO smsTemplateDTO) {
        log.debug("Request to save SmsTemplate : {}", smsTemplateDTO);
        SmsTemplate smsTemplate = smsTemplateMapper.toEntity(smsTemplateDTO);
        smsTemplate.setSupplierId(
            Optional.ofNullable(smsTemplateDTO.getSupplier()).map(smsSupplierDTO -> smsSupplierDTO.getId()).orElse(null)
        );
        this.saveOrUpdate(smsTemplate);
        return findOne(smsTemplate.getId()).orElseThrow();
    }

    /**
     * Update a smsTemplate.
     *
     * @param smsTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsTemplateDTO update(SmsTemplateDTO smsTemplateDTO) {
        log.debug("Request to update SmsTemplate : {}", smsTemplateDTO);
        SmsTemplate smsTemplate = smsTemplateMapper.toEntity(smsTemplateDTO);
        smsTemplate.setSupplierId(
            Optional.ofNullable(smsTemplateDTO.getSupplier()).map(smsSupplierDTO -> smsSupplierDTO.getId()).orElse(null)
        );
        this.saveOrUpdate(smsTemplate);
        return findOne(smsTemplate.getId()).orElseThrow();
    }

    /**
     * Partially update a smsTemplate.
     *
     * @param smsTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SmsTemplateDTO> partialUpdate(SmsTemplateDTO smsTemplateDTO) {
        log.debug("Request to partially update SmsTemplate : {}", smsTemplateDTO);

        return smsTemplateRepository
            .findById(smsTemplateDTO.getId())
            .map(existingSmsTemplate -> {
                smsTemplateMapper.partialUpdate(existingSmsTemplate, smsTemplateDTO);

                return existingSmsTemplate;
            })
            .map(tempSmsTemplate -> {
                smsTemplateRepository.save(tempSmsTemplate);
                return smsTemplateMapper.toDto(smsTemplateRepository.selectById(tempSmsTemplate.getId()));
            });
    }

    /**
     * Get all the smsTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SmsTemplateDTO> findAll(Page<SmsTemplate> pageable) {
        log.debug("Request to get all SmsTemplates");
        return this.page(pageable).convert(smsTemplateMapper::toDto);
    }

    /**
     * Get one smsTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SmsTemplateDTO> findOne(Long id) {
        log.debug("Request to get SmsTemplate : {}", id);
        return Optional.ofNullable(smsTemplateRepository.selectById(id))
            .map(smsTemplate -> {
                Binder.bindRelations(smsTemplate);
                return smsTemplate;
            })
            .map(smsTemplateMapper::toDto);
    }

    /**
     * Delete the smsTemplate by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SmsTemplate : {}", id);

        smsTemplateRepository.deleteById(id);
    }

    public void importSmsTemplateFromSupplier(Long supplierId) {
        try {
            List<SmsTemplate> smsTemplates = smsSupplierService.querySmsTemplateList(supplierId);
            LambdaQueryWrapper<SmsTemplate> queryWrapper = new LambdaQueryWrapper<>();

            smsTemplates.forEach(smsTemplate -> {
                queryWrapper.clear();
                queryWrapper.eq(SmsTemplate::getSupplierId, supplierId);
                queryWrapper.eq(SmsTemplate::getCode, smsTemplates.get(0).getCode());
                Optional.ofNullable(smsTemplateRepository.selectOne(queryWrapper)).ifPresentOrElse(
                    existSmsTemplate -> {
                        existSmsTemplate.setContent(smsTemplate.getContent());
                        existSmsTemplate.setEnabled(smsTemplate.getEnabled());
                        existSmsTemplate.setName(smsTemplate.getName());
                        existSmsTemplate.setRemark(smsTemplate.getRemark());
                        existSmsTemplate.setSendType(smsTemplate.getSendType());
                        existSmsTemplate.setTestJson(smsTemplate.getTestJson());
                        existSmsTemplate.setType(smsTemplate.getType());
                        smsTemplateRepository.updateById(existSmsTemplate);
                    },
                    () -> {
                        smsTemplate.setSupplierId(supplierId);
                        smsTemplateRepository.insert(smsTemplate);
                    }
                );
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update specified field by smsTemplate
     */
    @Transactional
    public void updateBatch(SmsTemplateDTO changeSmsTemplateDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SmsTemplate> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSmsTemplateDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SmsTemplate> smsTemplateList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(smsTemplateList)) {
                smsTemplateList.forEach(smsTemplate -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                smsTemplate,
                                relationName,
                                BeanUtil.getFieldValue(smsTemplateMapper.toEntity(changeSmsTemplateDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(smsTemplate, relationshipNames);
                });
            }
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
