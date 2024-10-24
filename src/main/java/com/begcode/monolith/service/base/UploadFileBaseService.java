package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.repository.UploadFileRepository;
import com.begcode.monolith.service.dto.UploadFileDTO;
import com.begcode.monolith.service.mapper.UploadFileMapper;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.UploadFile}.
 */
@SuppressWarnings("UnusedReturnValue")
public class UploadFileBaseService<R extends UploadFileRepository, E extends UploadFile>
    extends BaseServiceImpl<UploadFileRepository, UploadFile> {

    private static final Logger log = LoggerFactory.getLogger(UploadFileBaseService.class);

    private final List<String> relationCacheNames = List.of(com.begcode.monolith.domain.ResourceCategory.class.getName() + ".files");
    private final List<String> relationNames = List.of("category");

    protected final FileStorageService fileStorageService;

    protected final UploadFileRepository uploadFileRepository;

    protected final CacheManager cacheManager;

    protected final UploadFileMapper uploadFileMapper;

    public UploadFileBaseService(
        FileStorageService fileStorageService,
        UploadFileRepository uploadFileRepository,
        CacheManager cacheManager,
        UploadFileMapper uploadFileMapper
    ) {
        this.fileStorageService = fileStorageService;
        this.uploadFileRepository = uploadFileRepository;
        this.cacheManager = cacheManager;
        this.uploadFileMapper = uploadFileMapper;
    }

    /**
     * Update a uploadFile.
     *
     * @param uploadFileDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public UploadFileDTO update(UploadFileDTO uploadFileDTO) {
        log.debug("Request to update UploadFile : {}", uploadFileDTO);
        UploadFile uploadFile = uploadFileMapper.toEntity(uploadFileDTO);
        uploadFile.setCategoryId(
            Optional.ofNullable(uploadFileDTO.getCategory())
                .map(categoryResourceCategoryDTO -> categoryResourceCategoryDTO.getId())
                .orElse(null)
        );
        this.saveOrUpdate(uploadFile);
        return findOne(uploadFile.getId()).orElseThrow();
    }

    /**
     * Partially update a uploadFile.
     *
     * @param uploadFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadFileDTO> partialUpdate(UploadFileDTO uploadFileDTO) {
        log.debug("Request to partially update UploadFile : {}", uploadFileDTO);

        return uploadFileRepository
            .findById(uploadFileDTO.getId())
            .map(existingUploadFile -> {
                uploadFileMapper.partialUpdate(existingUploadFile, uploadFileDTO);

                return existingUploadFile;
            })
            .map(tempUploadFile -> {
                uploadFileRepository.save(tempUploadFile);
                return uploadFileMapper.toDto(uploadFileRepository.selectById(tempUploadFile.getId()));
            });
    }

    /**
     * copy a uploadFile.
     *
     * @param uploadFileDTO the entity to copy.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadFileDTO> copy(UploadFileDTO uploadFileDTO) {
        log.debug("Request to partially update UploadFile : {}", uploadFileDTO);

        return uploadFileRepository
            .findById(uploadFileDTO.getId())
            .map(existingUploadFile -> {
                uploadFileMapper.partialUpdate(existingUploadFile, uploadFileDTO);

                existingUploadFile.setId(null);
                return existingUploadFile;
            })
            .map(tempUploadFile -> {
                uploadFileRepository.save(tempUploadFile);
                return uploadFileMapper.toDto(uploadFileRepository.selectById(tempUploadFile.getId()));
            });
    }

    /**
     * Get all the uploadFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UploadFileDTO> findAll(Page<UploadFile> pageable) {
        log.debug("Request to get all UploadFiles");
        return this.page(pageable).convert(uploadFileMapper::toDto);
    }

    /**
     * Get one uploadFile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UploadFileDTO> findOne(Long id) {
        log.debug("Request to get UploadFile : {}", id);
        return Optional.ofNullable(uploadFileRepository.selectById(id))
            .map(uploadFile -> {
                Binder.bindRelations(uploadFile);
                return uploadFile;
            })
            .map(uploadFileMapper::toDto);
    }

    /**
     * Delete the uploadFile by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete UploadFile : {}", id);

        uploadFileRepository.deleteById(id);
    }

    /**
     * Save a uploadFile.
     *
     * @param uploadFileDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public UploadFileDTO save(UploadFileDTO uploadFileDTO) {
        log.debug("Request to save UploadFile : {}", uploadFileDTO);
        if (!uploadFileDTO.getFile().isEmpty()) {
            final String extName = FilenameUtils.getExtension(uploadFileDTO.getFile().getOriginalFilename());
            final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            final String savePathNew = yearAndMonth + File.separator;
            final long fileSize = uploadFileDTO.getFile().getSize();
            FileInfo upload = fileStorageService.of(uploadFileDTO.getFile()).setPath(savePathNew).upload();
            uploadFileDTO.setCreateAt(ZonedDateTime.now());
            uploadFileDTO.setExt(extName);
            uploadFileDTO.setFullName(uploadFileDTO.getFile().getOriginalFilename());
            uploadFileDTO.setName(uploadFileDTO.getFile().getName());
            uploadFileDTO.setFolder(savePathNew);
            uploadFileDTO.setPath(upload.getBasePath() + upload.getPath() + upload.getFilename());
            uploadFileDTO.setUrl(upload.getUrl());
            uploadFileDTO.setFileSize(fileSize);
            uploadFileDTO.setThumb(upload.getUrl());
        } else {
            throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
        }
        UploadFile uploadFile = uploadFileMapper.toEntity(uploadFileDTO);
        this.saveOrUpdate(uploadFile);
        return uploadFileMapper.toDto(this.getById(uploadFile.getId()));
    }

    /**
     * Update specified field by uploadFile
     */
    @Transactional
    public void updateBatch(UploadFileDTO changeUploadFileDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<UploadFile> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeUploadFileDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<UploadFile> uploadFileList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(uploadFileList)) {
                uploadFileList.forEach(uploadFile -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                uploadFile,
                                relationName,
                                BeanUtil.getFieldValue(uploadFileMapper.toEntity(changeUploadFileDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(uploadFile, relationshipNames);
                });
            }
        }
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
