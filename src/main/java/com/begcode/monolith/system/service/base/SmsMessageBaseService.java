package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.system.domain.SmsMessage;
import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
import com.begcode.monolith.system.service.mapper.SmsMessageMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.SmsMessage}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SmsMessageBaseService<R extends SmsMessageRepository, E extends SmsMessage>
    extends BaseServiceImpl<SmsMessageRepository, SmsMessage> {

    private final Logger log = LoggerFactory.getLogger(SmsMessageBaseService.class);
    private final List<String> relationNames = List.of();

    protected final SmsMessageRepository smsMessageRepository;

    protected final CacheManager cacheManager;

    protected final SmsMessageMapper smsMessageMapper;

    public SmsMessageBaseService(SmsMessageRepository smsMessageRepository, CacheManager cacheManager, SmsMessageMapper smsMessageMapper) {
        this.smsMessageRepository = smsMessageRepository;
        this.cacheManager = cacheManager;
        this.smsMessageMapper = smsMessageMapper;
    }

    /**
     * Save a smsMessage.
     *
     * @param smsMessageDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SmsMessageDTO save(SmsMessageDTO smsMessageDTO) {
        log.debug("Request to save SmsMessage : {}", smsMessageDTO);
        SmsMessage smsMessage = smsMessageMapper.toEntity(smsMessageDTO);

        this.saveOrUpdate(smsMessage);
        return findOne(smsMessage.getId()).orElseThrow();
    }

    /**
     * Update a smsMessage.
     *
     * @param smsMessageDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsMessageDTO update(SmsMessageDTO smsMessageDTO) {
        log.debug("Request to update SmsMessage : {}", smsMessageDTO);
        SmsMessage smsMessage = smsMessageMapper.toEntity(smsMessageDTO);

        this.saveOrUpdate(smsMessage);
        return findOne(smsMessage.getId()).orElseThrow();
    }

    /**
     * Partially update a smsMessage.
     *
     * @param smsMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SmsMessageDTO> partialUpdate(SmsMessageDTO smsMessageDTO) {
        log.debug("Request to partially update SmsMessage : {}", smsMessageDTO);

        return smsMessageRepository
            .findById(smsMessageDTO.getId())
            .map(existingSmsMessage -> {
                smsMessageMapper.partialUpdate(existingSmsMessage, smsMessageDTO);

                return existingSmsMessage;
            })
            .map(tempSmsMessage -> {
                smsMessageRepository.save(tempSmsMessage);
                return smsMessageMapper.toDto(smsMessageRepository.selectById(tempSmsMessage.getId()));
            });
    }

    /**
     * Get all the smsMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SmsMessageDTO> findAll(Page<SmsMessage> pageable) {
        log.debug("Request to get all SmsMessages");
        return this.page(pageable).convert(smsMessageMapper::toDto);
    }

    /**
     * Get one smsMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SmsMessageDTO> findOne(Long id) {
        log.debug("Request to get SmsMessage : {}", id);
        return Optional.ofNullable(smsMessageRepository.selectById(id))
            .map(smsMessage -> {
                Binder.bindRelations(smsMessage);
                return smsMessage;
            })
            .map(smsMessageMapper::toDto);
    }

    /**
     * Delete the smsMessage by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SmsMessage : {}", id);

        smsMessageRepository.deleteById(id);
    }

    /**
     * Update specified field by smsMessage
     */
    @Transactional
    public void updateBatch(SmsMessageDTO changeSmsMessageDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SmsMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSmsMessageDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SmsMessage> smsMessageList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(smsMessageList)) {
                smsMessageList.forEach(smsMessage -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                smsMessage,
                                relationName,
                                BeanUtil.getFieldValue(smsMessageMapper.toEntity(changeSmsMessageDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(smsMessage, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            SmsMessage byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
