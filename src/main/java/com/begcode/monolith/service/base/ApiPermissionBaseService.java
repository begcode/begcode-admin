package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.security.annotation.PermissionDefine;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.service.mapper.ApiPermissionMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.ApiPermission}.
 */
@SuppressWarnings("UnusedReturnValue")
public class ApiPermissionBaseService<R extends ApiPermissionRepository, E extends ApiPermission>
    extends BaseServiceImpl<ApiPermissionRepository, ApiPermission> {

    private static final Logger log = LoggerFactory.getLogger(ApiPermissionBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.domain.ApiPermission.class.getName() + ".parent",
        com.begcode.monolith.domain.ApiPermission.class.getName() + ".children",
        com.begcode.monolith.domain.Authority.class.getName() + ".apiPermissions"
    );
    private final List<String> relationNames = List.of("children", "parent", "authorities");

    protected final ApiPermissionRepository apiPermissionRepository;

    protected final CacheManager cacheManager;

    protected final ApiPermissionMapper apiPermissionMapper;

    protected final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ApiPermissionBaseService(
        ApiPermissionRepository apiPermissionRepository,
        CacheManager cacheManager,
        ApiPermissionMapper apiPermissionMapper,
        RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        this.apiPermissionRepository = apiPermissionRepository;
        this.cacheManager = cacheManager;
        this.apiPermissionMapper = apiPermissionMapper;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    /**
     * Save a apiPermission.
     *
     * @param apiPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ApiPermissionDTO save(ApiPermissionDTO apiPermissionDTO) {
        log.debug("Request to save ApiPermission : {}", apiPermissionDTO);
        ApiPermission apiPermission = apiPermissionMapper.toEntity(apiPermissionDTO);
        clearChildrenCache();

        this.saveOrUpdate(apiPermission);
        return findOne(apiPermission.getId()).orElseThrow();
    }

    /**
     * Update a apiPermission.
     *
     * @param apiPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiPermissionDTO update(ApiPermissionDTO apiPermissionDTO) {
        log.debug("Request to update ApiPermission : {}", apiPermissionDTO);
        ApiPermission apiPermission = apiPermissionMapper.toEntity(apiPermissionDTO);
        clearChildrenCache();

        this.saveOrUpdate(apiPermission);
        return findOne(apiPermission.getId()).orElseThrow();
    }

    /**
     * Partially update a apiPermission.
     *
     * @param apiPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ApiPermissionDTO> partialUpdate(ApiPermissionDTO apiPermissionDTO) {
        log.debug("Request to partially update ApiPermission : {}", apiPermissionDTO);

        return apiPermissionRepository
            .findById(apiPermissionDTO.getId())
            .map(existingApiPermission -> {
                apiPermissionMapper.partialUpdate(existingApiPermission, apiPermissionDTO);

                return existingApiPermission;
            })
            .map(tempApiPermission -> {
                apiPermissionRepository.save(tempApiPermission);
                return apiPermissionMapper.toDto(apiPermissionRepository.selectById(tempApiPermission.getId()));
            });
    }

    /**
     * Get all the apiPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ApiPermissionDTO> findAll(Page<ApiPermission> pageable) {
        log.debug("Request to get all ApiPermissions");
        return this.page(pageable).convert(apiPermissionMapper::toDto);
    }

    /**
     * Get one apiPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ApiPermissionDTO> findOne(Long id) {
        log.debug("Request to get ApiPermission : {}", id);
        return Optional.ofNullable(apiPermissionRepository.selectById(id))
            .map(apiPermission -> {
                Binder.bindRelations(apiPermission);
                return apiPermission;
            })
            .map(apiPermissionMapper::toDto);
    }

    /**
     * Delete the apiPermission by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete ApiPermission : {}", id);

        ApiPermission apiPermission = apiPermissionRepository.selectById(id);
        if (apiPermission.getChildren() != null) {
            apiPermission.getChildren().forEach(subApiPermission -> subApiPermission.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        apiPermissionRepository.deleteById(id);
    }

    /**
     * Get all the apiPermissions.
     *
     * @param type the ApiPermissionType.
     * @return the list of entities.
     */
    public List<ApiPermissionDTO> findAllByType(ApiPermissionType type) {
        log.debug("Request to get all ApiPermissions by type");
        return apiPermissionMapper.toDto(apiPermissionRepository.findAllByType(type));
    }

    /**
     * regenerate ApiPermissions from Annotation
     */
    @Transactional
    public void regenerateApiPermissions() {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            PermissionDefine permissionDefineAnnotation = method.getMethodAnnotation(PermissionDefine.class);
            if (permissionDefineAnnotation != null) {
                // 处理group
                ApiPermission apiPermissionGroup = apiPermissionRepository
                    .findOneByCode(permissionDefineAnnotation.groupCode())
                    .orElseGet(() -> {
                        ApiPermission permission = new ApiPermission()
                            .code(permissionDefineAnnotation.groupCode())
                            .name(permissionDefineAnnotation.groupName())
                            .type(ApiPermissionType.BUSINESS);
                        apiPermissionRepository.insert(permission);
                        return this.getById(permission.getId());
                    });
                // 处理实体
                ApiPermission apiPermissionEntity = apiPermissionRepository
                    .findOneByCode(permissionDefineAnnotation.groupCode() + "_" + permissionDefineAnnotation.entityCode())
                    .orElseGet(() -> {
                        ApiPermission permission = new ApiPermission()
                            .code(permissionDefineAnnotation.groupCode() + "_" + permissionDefineAnnotation.entityCode())
                            .name(permissionDefineAnnotation.entityName())
                            .type(ApiPermissionType.ENTITY);
                        apiPermissionRepository.insert(permission);
                        return this.getById(permission.getId());
                    });
                this.saveOrUpdate(apiPermissionEntity.parent(apiPermissionGroup));
                // 处理permission
                // 获得相关的methodType

                ApiPermission apiPermission = apiPermissionRepository
                    .findOneByCode(
                        permissionDefineAnnotation.groupCode() +
                        "_" +
                        permissionDefineAnnotation.entityCode() +
                        "_" +
                        permissionDefineAnnotation.permissionCode()
                    )
                    .orElseGet(() -> {
                        ApiPermission permission = new ApiPermission()
                            .code(
                                permissionDefineAnnotation.groupCode() +
                                "_" +
                                permissionDefineAnnotation.entityCode() +
                                "_" +
                                permissionDefineAnnotation.permissionCode()
                            )
                            .name(permissionDefineAnnotation.permissionName())
                            .parent(apiPermissionEntity)
                            .type(ApiPermissionType.API)
                            .status(permissionDefineAnnotation.state());
                        apiPermissionRepository.insert(permission);
                        return this.getById(permission.getId());
                    });

                GetMapping getMappingAnnotation = method.getMethodAnnotation(GetMapping.class);
                PostMapping postMappingAnnotation = method.getMethodAnnotation(PostMapping.class);
                DeleteMapping deleteMappingAnnotation = method.getMethodAnnotation(DeleteMapping.class);
                PutMapping putMappingAnnotation = method.getMethodAnnotation(PutMapping.class);
                RequestMapping requestMappingAnnotation = method.getMethodAnnotation(RequestMapping.class);
                StringBuilder methodType = new StringBuilder();
                if (getMappingAnnotation != null) {
                    methodType = new StringBuilder("GET");
                }
                if (postMappingAnnotation != null) {
                    methodType = new StringBuilder("POST");
                }
                if (deleteMappingAnnotation != null) {
                    methodType = new StringBuilder("DELETE");
                }
                if (putMappingAnnotation != null) {
                    methodType = new StringBuilder("PUT");
                }
                if (requestMappingAnnotation != null) {
                    if (requestMappingAnnotation.method().length > 0) {
                        RequestMethod[] methods = requestMappingAnnotation.method();
                        for (RequestMethod r : methods) {
                            if (methodType.indexOf(r.name()) == -1) {
                                methodType.append(",").append(r.name());
                            }
                        }
                        if (methodType.charAt(0) == ',') {
                            methodType.deleteCharAt(0);
                        }
                    }
                }
                // url
                PatternsRequestCondition patternsCondition = m.getKey().getPatternsCondition();
                String url = patternsCondition.toString();
                apiPermission.method(methodType.toString()).url(url).status(permissionDefineAnnotation.state());
                this.saveOrUpdate(apiPermission.parent(apiPermissionEntity));
            }
        }
    }

    public List<ApiPermissionDTO> findAllApiPermissionsByCurrentUser() {
        if (SecurityUtils.isAuthenticated()) {
            return apiPermissionMapper.toDto(
                apiPermissionRepository.findAllApiPermissionsByCurrentUser(SecurityUtils.getCurrentUserId().orElseThrow())
            );
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Update specified field by apiPermission
     */
    @Transactional
    public void updateBatch(ApiPermissionDTO changeApiPermissionDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<ApiPermission> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeApiPermissionDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<ApiPermission> apiPermissionList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(apiPermissionList)) {
                apiPermissionList.forEach(apiPermission -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                apiPermission,
                                relationName,
                                BeanUtil.getFieldValue(apiPermissionMapper.toEntity(changeApiPermissionDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(apiPermission, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.begcode.monolith.domain.ApiPermission.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            ApiPermission byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
            if (relationshipName.equals("authorities")) {
                if (CollectionUtils.isNotEmpty(otherEntityIds)) {
                    List<Long> ids = otherEntityIds.stream().map(Long::valueOf).toList();
                    List<Authority> authorityExist = byId.getAuthorities();
                    if (operateType.equals("add")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> authorityExist.stream().noneMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            collect.forEach(addId -> authorityExist.add(new Authority().id(addId)));
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("authorities"));
                        }
                    } else if (operateType.equals("delete")) {
                        List<Long> collect = ids
                            .stream()
                            .filter(relId -> authorityExist.stream().anyMatch(vp -> vp.getId().equals(relId)))
                            .toList();
                        if (CollectionUtils.isNotEmpty(collect)) {
                            List<Authority> authorityAdd = authorityExist.stream().filter(vp -> !collect.contains(vp.getId())).toList();
                            byId.setAuthorities(authorityAdd);
                            // 更新
                            this.createOrUpdateAndRelatedRelations(byId, List.of("authorities"));
                        }
                    }
                }
            }
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
