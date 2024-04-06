package com.begcode.monolith.log.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.log.domain.SysLog;
import com.begcode.monolith.log.repository.SysLogRepository;
import com.begcode.monolith.log.service.dto.SysLogDTO;
import com.begcode.monolith.log.service.mapper.SysLogMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.log.domain.SysLog}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SysLogBaseService<R extends SysLogRepository, E extends SysLog> extends BaseServiceImpl<SysLogRepository, SysLog> {

    private final Logger log = LoggerFactory.getLogger(SysLogBaseService.class);
    private final List<String> relationNames = List.of();

    protected final SysLogRepository sysLogRepository;

    protected final CacheManager cacheManager;

    protected final SysLogMapper sysLogMapper;

    public SysLogBaseService(SysLogRepository sysLogRepository, CacheManager cacheManager, SysLogMapper sysLogMapper) {
        this.sysLogRepository = sysLogRepository;
        this.cacheManager = cacheManager;
        this.sysLogMapper = sysLogMapper;
    }

    /**
     * Save a sysLog.
     *
     * @param sysLogDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SysLogDTO save(SysLogDTO sysLogDTO) {
        log.debug("Request to save SysLog : {}", sysLogDTO);
        SysLog sysLog = sysLogMapper.toEntity(sysLogDTO);

        this.saveOrUpdate(sysLog);
        return findOne(sysLog.getId()).orElseThrow();
    }

    /**
     * Update a sysLog.
     *
     * @param sysLogDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SysLogDTO update(SysLogDTO sysLogDTO) {
        log.debug("Request to update SysLog : {}", sysLogDTO);
        SysLog sysLog = sysLogMapper.toEntity(sysLogDTO);

        this.saveOrUpdate(sysLog);
        return findOne(sysLog.getId()).orElseThrow();
    }

    /**
     * Partially update a sysLog.
     *
     * @param sysLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SysLogDTO> partialUpdate(SysLogDTO sysLogDTO) {
        log.debug("Request to partially update SysLog : {}", sysLogDTO);

        return sysLogRepository
            .findById(sysLogDTO.getId())
            .map(existingSysLog -> {
                sysLogMapper.partialUpdate(existingSysLog, sysLogDTO);

                return existingSysLog;
            })
            .map(tempSysLog -> {
                sysLogRepository.save(tempSysLog);
                return sysLogMapper.toDto(sysLogRepository.selectById(tempSysLog.getId()));
            });
    }

    /**
     * Get all the sysLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SysLogDTO> findAll(Page<SysLog> pageable) {
        log.debug("Request to get all SysLogs");
        return this.page(pageable).convert(sysLogMapper::toDto);
    }

    /**
     * Get one sysLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SysLogDTO> findOne(Long id) {
        log.debug("Request to get SysLog : {}", id);
        return Optional.ofNullable(sysLogRepository.selectById(id))
            .map(sysLog -> {
                Binder.bindRelations(sysLog);
                return sysLog;
            })
            .map(sysLogMapper::toDto);
    }

    /**
     * Delete the sysLog by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SysLog : {}", id);

        sysLogRepository.deleteById(id);
    }

    /**
     * Update specified field by sysLog
     */
    @Transactional
    public void updateBatch(SysLogDTO changeSysLogDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SysLog> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSysLogDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SysLog> sysLogList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(sysLogList)) {
                sysLogList.forEach(sysLog -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                sysLog,
                                relationName,
                                BeanUtil.getFieldValue(sysLogMapper.toEntity(changeSysLogDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(sysLog, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            SysLog byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
