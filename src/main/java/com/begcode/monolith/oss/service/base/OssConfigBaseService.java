package com.begcode.monolith.oss.service.base;

import cn.hutool.core.bean.BeanUtil;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import cn.xuyanwu.spring.file.storage.platform.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.SpringBootUtil;
import com.begcode.monolith.oss.domain.OssConfig;
import com.begcode.monolith.oss.repository.OssConfigRepository;
import com.begcode.monolith.oss.service.dto.OssConfigDTO;
import com.begcode.monolith.oss.service.mapper.OssConfigMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.oss.domain.OssConfig}.
 */
public class OssConfigBaseService<R extends OssConfigRepository, E extends OssConfig>
    extends BaseServiceImpl<OssConfigRepository, OssConfig> {

    private final Logger log = LoggerFactory.getLogger(OssConfigBaseService.class);
    private final List<String> relationNames = Arrays.asList();

    protected final FileStorageService fileStorageService;

    protected final OssConfigRepository ossConfigRepository;

    protected final CacheManager cacheManager;

    protected final OssConfigMapper ossConfigMapper;

    public OssConfigBaseService(
        FileStorageService fileStorageService,
        OssConfigRepository ossConfigRepository,
        CacheManager cacheManager,
        OssConfigMapper ossConfigMapper
    ) {
        this.fileStorageService = fileStorageService;
        this.ossConfigRepository = ossConfigRepository;
        this.cacheManager = cacheManager;
        this.ossConfigMapper = ossConfigMapper;
    }

    /**
     * Save a ossConfig.
     *
     * @param ossConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public OssConfigDTO save(OssConfigDTO ossConfigDTO) {
        log.debug("Request to save OssConfig : {}", ossConfigDTO);
        OssConfig ossConfig = ossConfigMapper.toEntity(ossConfigDTO);

        this.saveOrUpdate(ossConfig);
        this.initPlatforms();
        return findOne(ossConfig.getId()).orElseThrow();
    }

    /**
     * Update a ossConfig.
     *
     * @param ossConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public OssConfigDTO update(OssConfigDTO ossConfigDTO) {
        log.debug("Request to update OssConfig : {}", ossConfigDTO);

        OssConfig ossConfig = ossConfigMapper.toEntity(ossConfigDTO);

        ossConfigRepository.updateById(ossConfig);
        this.initPlatforms();
        return findOne(ossConfigDTO.getId()).orElseThrow();
    }

    /**
     * Partially update a ossConfig.
     *
     * @param ossConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<OssConfigDTO> partialUpdate(OssConfigDTO ossConfigDTO) {
        log.debug("Request to partially update OssConfig : {}", ossConfigDTO);

        return ossConfigRepository
            .findById(ossConfigDTO.getId())
            .map(existingOssConfig -> {
                ossConfigMapper.partialUpdate(existingOssConfig, ossConfigDTO);

                return existingOssConfig;
            })
            .map(tempOssConfig -> {
                ossConfigRepository.save(tempOssConfig);
                return ossConfigMapper.toDto(ossConfigRepository.selectById(tempOssConfig.getId()));
            });
    }

    /**
     * Get all the ossConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<OssConfigDTO> findAll(Page<OssConfig> pageable) {
        log.debug("Request to get all OssConfigs");
        return this.page(pageable).convert(ossConfigMapper::toDto);
    }

    /**
     * Get one ossConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<OssConfigDTO> findOne(Long id) {
        log.debug("Request to get OssConfig : {}", id);
        return Optional
            .ofNullable(ossConfigRepository.selectById(id))
            .map(ossConfig -> {
                Binder.bindRelations(ossConfig);
                return ossConfig;
            })
            .map(ossConfigMapper::toDto);
    }

    /**
     * Delete the ossConfig by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete OssConfig : {}", id);

        ossConfigRepository.deleteById(id);
    }

    public void initPlatforms() {
        LambdaQueryWrapper<OssConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OssConfig::getEnabled, true);
        List<OssConfig> ossConfigs = ossConfigRepository.selectList(queryWrapper);
        //获得存储平台 List
        CopyOnWriteArrayList<FileStorage> storageList = fileStorageService.getFileStorageList();
        for (FileStorage storage : storageList) {
            storage.close();
        }
        storageList.clear();
        if (CollectionUtils.isEmpty(ossConfigs)) {
            LocalPlusFileStorage storage = new LocalPlusFileStorage();
            String storagePath = "data/";
            String domain = "";
            String basePath = "/upload/";
            storagePath = SpringBootUtil.getApplicationPathEndWithSeparator() + storagePath;
            storage.setStoragePath(storagePath);
            storage.setBasePath(basePath);
            storage.setDomain(domain);
            storage.setPlatform("local");
            storageList.add(storage);
        }
        for (OssConfig ossConfig : ossConfigs) {
            switch (ossConfig.getProvider()) {
                case LOCAL -> {
                    LocalPlusFileStorage storage = new LocalPlusFileStorage();
                    ObjectMapper objectMapper = new ObjectMapper();
                    // 将json字符串转为对象
                    String json = ossConfig.getConfigData();
                    try {
                        Map map = objectMapper.readValue(json, Map.class);
                        String storagePath = (String) map.get("storagePath");
                        if (StringUtils.isBlank(storagePath)) {
                            storagePath = "data/";
                        }
                        String domain = (String) map.get("domain");
                        if (StringUtils.isBlank(domain)) {
                            domain = "";
                        }
                        String basePath = (String) map.get("basePath");
                        if (StringUtils.isBlank(basePath)) {
                            basePath = "/upload/";
                        }
                        if (!storagePath.startsWith("/")) {
                            storagePath = SpringBootUtil.getApplicationPathEndWithSeparator() + storagePath;
                        }
                        storage.setStoragePath(storagePath);
                        storage.setBasePath(basePath);
                        storage.setDomain(domain);
                        storage.setPlatform(ossConfig.getPlatform());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    storageList.add(storage);
                }
                case ALI -> {
                    AliyunOssFileStorage aliyunOssFileStorage = new AliyunOssFileStorage();
                    storageList.add(aliyunOssFileStorage);
                }
                case MINIO -> {
                    MinIOFileStorage minIOFileStorage = new MinIOFileStorage();
                    storageList.add(minIOFileStorage);
                }
                case QINIU -> {
                    QiniuKodoFileStorage qiniuKodoFileStorage = new QiniuKodoFileStorage();
                    storageList.add(qiniuKodoFileStorage);
                }
                case TENCENT -> {
                    TencentCosFileStorage tencentCosFileStorage = new TencentCosFileStorage();
                    storageList.add(tencentCosFileStorage);
                }
                default -> log.warn("未知的存储平台:{}", ossConfig.getProvider());
            }
        }
    }

    /**
     * Update specified field by ossConfig
     */
    @Transactional
    public void updateBatch(OssConfigDTO changeOssConfigDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<OssConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(fieldName ->
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeOssConfigDTO, fieldName)
                )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<OssConfig> ossConfigList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(ossConfigList)) {
                ossConfigList.forEach(ossConfig -> {
                    relationshipNames.forEach(relationName ->
                        BeanUtil.setFieldValue(
                            ossConfig,
                            relationName,
                            BeanUtil.getFieldValue(ossConfigMapper.toEntity(changeOssConfigDTO), relationName)
                        )
                    );
                    this.createOrUpdateAndRelatedRelations(ossConfig, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            OssConfig byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
