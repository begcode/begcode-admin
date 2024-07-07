package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.UReportFile;
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.dto.UReportFileDTO;
import com.begcode.monolith.service.mapper.UReportFileMapper;
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
 * Service Implementation for managing {@link com.begcode.monolith.domain.UReportFile}.
 */
@SuppressWarnings("UnusedReturnValue")
public class UReportFileBaseService<R extends UReportFileRepository, E extends UReportFile>
    extends BaseServiceImpl<UReportFileRepository, UReportFile> {

    private static final Logger log = LoggerFactory.getLogger(UReportFileBaseService.class);
    private final List<String> relationNames = List.of();

    protected final UReportFileRepository uReportFileRepository;

    protected final CacheManager cacheManager;

    protected final UReportFileMapper uReportFileMapper;

    public UReportFileBaseService(
        UReportFileRepository uReportFileRepository,
        CacheManager cacheManager,
        UReportFileMapper uReportFileMapper
    ) {
        this.uReportFileRepository = uReportFileRepository;
        this.cacheManager = cacheManager;
        this.uReportFileMapper = uReportFileMapper;
    }

    /**
     * Save a uReportFile.
     *
     * @param uReportFileDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public UReportFileDTO save(UReportFileDTO uReportFileDTO) {
        log.debug("Request to save UReportFile : {}", uReportFileDTO);
        UReportFile uReportFile = uReportFileMapper.toEntity(uReportFileDTO);
        this.saveOrUpdate(uReportFile);
        return findOne(uReportFile.getId()).orElseThrow();
    }

    /**
     * Update a uReportFile.
     *
     * @param uReportFileDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public UReportFileDTO update(UReportFileDTO uReportFileDTO) {
        log.debug("Request to update UReportFile : {}", uReportFileDTO);
        UReportFile uReportFile = uReportFileMapper.toEntity(uReportFileDTO);
        this.saveOrUpdate(uReportFile);
        return findOne(uReportFile.getId()).orElseThrow();
    }

    /**
     * Partially update a uReportFile.
     *
     * @param uReportFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UReportFileDTO> partialUpdate(UReportFileDTO uReportFileDTO) {
        log.debug("Request to partially update UReportFile : {}", uReportFileDTO);

        return uReportFileRepository
            .findById(uReportFileDTO.getId())
            .map(existingUReportFile -> {
                uReportFileMapper.partialUpdate(existingUReportFile, uReportFileDTO);

                return existingUReportFile;
            })
            .map(tempUReportFile -> {
                uReportFileRepository.save(tempUReportFile);
                return uReportFileMapper.toDto(uReportFileRepository.selectById(tempUReportFile.getId()));
            });
    }

    /**
     * Get all the uReportFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UReportFileDTO> findAll(Page<UReportFile> pageable) {
        log.debug("Request to get all UReportFiles");
        return this.page(pageable).convert(uReportFileMapper::toDto);
    }

    /**
     * Get one uReportFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UReportFileDTO> findOne(Long id) {
        log.debug("Request to get UReportFile : {}", id);
        return Optional.ofNullable(uReportFileRepository.selectById(id))
            .map(uReportFile -> {
                Binder.bindRelations(uReportFile);
                return uReportFile;
            })
            .map(uReportFileMapper::toDto);
    }

    /**
     * Delete the uReportFile by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete UReportFile : {}", id);

        uReportFileRepository.deleteById(id);
    }

    /**
     * Get the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public Optional<UReportFileDTO> getByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        return uReportFileRepository.getByName(name).map(uReportFileMapper::toDto);
    }

    /**
     * Delete the uReportFile by name.
     *
     * @param name the name of the entity.
     */
    public void deleteByName(String name) {
        log.debug("Request to delete UReportFile : {}", name);
        uReportFileRepository.deleteByName(name);
    }

    /**
     * Update specified field by uReportFile
     */
    @Transactional
    public void updateBatch(UReportFileDTO changeUReportFileDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<UReportFile> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeUReportFileDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<UReportFile> uReportFileList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(uReportFileList)) {
                uReportFileList.forEach(uReportFile -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                uReportFile,
                                relationName,
                                BeanUtil.getFieldValue(uReportFileMapper.toEntity(changeUReportFileDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(uReportFile, relationshipNames);
                });
            }
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
