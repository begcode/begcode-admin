package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.UploadImage;
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.dto.UploadImageDTO;
import com.begcode.monolith.service.mapper.UploadImageMapper;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.io.File;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.UploadImage}.
 */
@SuppressWarnings("UnusedReturnValue")
public class UploadImageBaseService<R extends UploadImageRepository, E extends UploadImage>
    extends BaseServiceImpl<UploadImageRepository, UploadImage> {

    private static final Logger log = LoggerFactory.getLogger(UploadImageBaseService.class);

    private final List<String> relationCacheNames = List.of(com.begcode.monolith.domain.ResourceCategory.class.getName() + ".images");
    private final List<String> relationNames = List.of("category");

    protected final FileStorageService fileStorageService;

    protected final UploadImageRepository uploadImageRepository;

    protected final CacheManager cacheManager;

    protected final UploadImageMapper uploadImageMapper;

    public UploadImageBaseService(
        FileStorageService fileStorageService,
        UploadImageRepository uploadImageRepository,
        CacheManager cacheManager,
        UploadImageMapper uploadImageMapper
    ) {
        this.fileStorageService = fileStorageService;
        this.uploadImageRepository = uploadImageRepository;
        this.cacheManager = cacheManager;
        this.uploadImageMapper = uploadImageMapper;
    }

    /**
     * Update a uploadImage.
     *
     * @param uploadImageDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public UploadImageDTO update(UploadImageDTO uploadImageDTO) {
        log.debug("Request to update UploadImage : {}", uploadImageDTO);
        UploadImage uploadImage = uploadImageMapper.toEntity(uploadImageDTO);
        uploadImage.setCategoryId(Optional.ofNullable(uploadImage.getCategory()).map(ResourceCategory::getId).orElse(null));
        this.saveOrUpdate(uploadImage);
        return findOne(uploadImage.getId()).orElseThrow();
    }

    /**
     * Partially update a uploadImage.
     *
     * @param uploadImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadImageDTO> partialUpdate(UploadImageDTO uploadImageDTO) {
        log.debug("Request to partially update UploadImage : {}", uploadImageDTO);

        return uploadImageRepository
            .findById(uploadImageDTO.getId())
            .map(existingUploadImage -> {
                uploadImageMapper.partialUpdate(existingUploadImage, uploadImageDTO);

                return existingUploadImage;
            })
            .map(tempUploadImage -> {
                uploadImageRepository.save(tempUploadImage);
                return uploadImageMapper.toDto(uploadImageRepository.selectById(tempUploadImage.getId()));
            });
    }

    /**
     * copy a uploadImage.
     *
     * @param uploadImageDTO the entity to copy.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<UploadImageDTO> copy(UploadImageDTO uploadImageDTO) {
        log.debug("Request to partially update UploadImage : {}", uploadImageDTO);

        return uploadImageRepository
            .findById(uploadImageDTO.getId())
            .map(existingUploadImage -> {
                uploadImageMapper.partialUpdate(existingUploadImage, uploadImageDTO);

                existingUploadImage.setId(null);
                return existingUploadImage;
            })
            .map(tempUploadImage -> {
                uploadImageRepository.save(tempUploadImage);
                return uploadImageMapper.toDto(uploadImageRepository.selectById(tempUploadImage.getId()));
            });
    }

    /**
     * Get all the uploadImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<UploadImageDTO> findAll(Page<UploadImage> pageable) {
        log.debug("Request to get all UploadImages");
        return this.page(pageable).convert(uploadImageMapper::toDto);
    }

    /**
     * Get one uploadImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UploadImageDTO> findOne(Long id) {
        log.debug("Request to get UploadImage : {}", id);
        return Optional.ofNullable(uploadImageRepository.selectById(id))
            .map(uploadImage -> {
                Binder.bindRelations(uploadImage);
                return uploadImage;
            })
            .map(uploadImageMapper::toDto);
    }

    /**
     * Delete the uploadImage by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete UploadImage : {}", id);

        uploadImageRepository.deleteById(id);
    }

    /**
     * Save a uploadImage.
     *
     * @param uploadImageDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public UploadImageDTO save(UploadImageDTO uploadImageDTO) {
        log.debug("Request to save UploadImage : {}", uploadImageDTO);
        if (!uploadImageDTO.getImage().isEmpty()) {
            final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            uploadImageDTO.setCreateAt(ZonedDateTime.now());
            uploadImageDTO.setFullName(uploadImageDTO.getImage().getOriginalFilename());
            uploadImageDTO.setName(uploadImageDTO.getImage().getName());
            uploadImageDTO.setFolder(yearAndMonth + File.separator);
            uploadImageDTO.setFileSize(uploadImageDTO.getImage().getSize());
            final String savePathNew = yearAndMonth + File.separator;
            FileInfo upload = fileStorageService.of(uploadImageDTO.getImage()).setPath(savePathNew).upload();
            uploadImageDTO.setPath(upload.getBasePath() + upload.getPath() + upload.getFilename());
            uploadImageDTO.setUrl(upload.getUrl());
            uploadImageDTO.setExt(upload.getExt());
        } else {
            throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
        }
        UploadImage uploadImage = uploadImageMapper.toEntity(uploadImageDTO);
        this.saveOrUpdate(uploadImage);
        return uploadImageMapper.toDto(this.getById(uploadImage.getId()));
    }

    @Transactional
    public UploadImageDTO save(File file) {
        log.debug("Request to save UploadImage : {}", file.getName());
        if (file.exists()) {
            UploadImageDTO uploadImageDTO = new UploadImageDTO();
            final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
            uploadImageDTO.setCreateAt(ZonedDateTime.now());
            uploadImageDTO.setFullName(file.getName());
            uploadImageDTO.setName(file.getName());
            uploadImageDTO.setFolder(yearAndMonth + File.separator);
            uploadImageDTO.setFileSize(file.length());
            FileInfo upload = fileStorageService.of(file).setPlatform("local").upload();
            uploadImageDTO.setUrl(upload.getUrl());
            uploadImageDTO.setExt(upload.getExt());
            UploadImage uploadImage = uploadImageMapper.toEntity(uploadImageDTO);
            this.saveOrUpdate(uploadImage);
            return uploadImageMapper.toDto(this.getById(uploadImage.getId()));
        } else {
            throw new BadRequestAlertException("Invalid file", "UploadFile", "imagesnull");
        }
    }

    @Transactional
    public UploadImageDTO save(URL url) {
        log.debug("Request to save UploadImage : {}", url.getPath());
        UploadImageDTO uploadImageDTO = new UploadImageDTO();
        final String yearAndMonth = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
        uploadImageDTO.setCreateAt(ZonedDateTime.now());
        uploadImageDTO.setFolder(yearAndMonth + File.separator);
        FileInfo upload = fileStorageService.of(url).setPlatform("local").upload();
        uploadImageDTO.setFullName(upload.getFilename());
        uploadImageDTO.setName(upload.getFilename());
        uploadImageDTO.setUrl(upload.getUrl());
        uploadImageDTO.setExt(upload.getExt());
        uploadImageDTO.setFileSize(upload.getSize());
        UploadImage uploadImage = uploadImageMapper.toEntity(uploadImageDTO);
        this.saveOrUpdate(uploadImage);
        return uploadImageMapper.toDto(this.getById(uploadImage.getId()));
    }

    /**
     * Update specified field by uploadImage
     */
    @Transactional
    public void updateBatch(UploadImageDTO changeUploadImageDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<UploadImage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeUploadImageDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<UploadImage> uploadImageList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(uploadImageList)) {
                uploadImageList.forEach(uploadImage -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                uploadImage,
                                relationName,
                                BeanUtil.getFieldValue(uploadImageMapper.toEntity(changeUploadImageDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(uploadImage, relationshipNames);
                });
            }
        }
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
