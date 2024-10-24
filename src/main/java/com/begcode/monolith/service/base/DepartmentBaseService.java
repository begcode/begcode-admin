package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.dto.DepartmentDTO;
import com.begcode.monolith.service.mapper.DepartmentMapper;
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

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.Department}.
 */
@SuppressWarnings("UnusedReturnValue")
public class DepartmentBaseService<R extends DepartmentRepository, E extends Department>
    extends BaseServiceImpl<DepartmentRepository, Department> {

    private static final Logger log = LoggerFactory.getLogger(DepartmentBaseService.class);

    private final List<String> relationCacheNames = List.of(
        com.begcode.monolith.domain.Department.class.getName() + ".parent",
        com.begcode.monolith.domain.Authority.class.getName() + ".department",
        com.begcode.monolith.domain.Department.class.getName() + ".children",
        com.begcode.monolith.domain.User.class.getName() + ".department"
    );
    private final List<String> relationNames = List.of("children", "authorities", "parent", "users");

    protected final DepartmentRepository departmentRepository;

    protected final CacheManager cacheManager;

    protected final DepartmentMapper departmentMapper;

    public DepartmentBaseService(DepartmentRepository departmentRepository, CacheManager cacheManager, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.cacheManager = cacheManager;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Save a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        log.debug("Request to save Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        clearChildrenCache();
        this.createOrUpdateAndRelatedRelations(department, List.of("authorities"));
        return findOne(department.getId()).orElseThrow();
    }

    /**
     * Update a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        log.debug("Request to update Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        clearChildrenCache();
        this.createOrUpdateAndRelatedRelations(department, List.of("authorities"));
        return findOne(department.getId()).orElseThrow();
    }

    /**
     * Partially update a department.
     *
     * @param departmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<DepartmentDTO> partialUpdate(DepartmentDTO departmentDTO) {
        log.debug("Request to partially update Department : {}", departmentDTO);

        return departmentRepository
            .findById(departmentDTO.getId())
            .map(existingDepartment -> {
                departmentMapper.partialUpdate(existingDepartment, departmentDTO);

                return existingDepartment;
            })
            .map(tempDepartment -> {
                departmentRepository.save(tempDepartment);
                return departmentMapper.toDto(departmentRepository.selectById(tempDepartment.getId()));
            });
    }

    /**
     * Get all the departments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<DepartmentDTO> findAll(Page<Department> pageable) {
        log.debug("Request to get all Departments");
        return this.page(pageable).convert(departmentMapper::toDto);
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DepartmentDTO> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return Optional.ofNullable(departmentRepository.selectById(id))
            .map(department -> {
                Binder.bindRelations(department);
                return department;
            })
            .map(departmentMapper::toDto);
    }

    /**
     * Delete the department by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);

        Department department = departmentRepository.selectById(id);
        if (department.getChildren() != null) {
            department.getChildren().forEach(subDepartment -> subDepartment.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        // 关联属性设置为空集合
        department.authorities(new ArrayList<>());
        this.createOrUpdateN2NRelations(department, Arrays.asList("authorities"));
        departmentRepository.deleteById(id);
    }

    /**
     * Update specified field by department
     */
    @Transactional
    public void updateBatch(DepartmentDTO changeDepartmentDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeDepartmentDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<Department> departmentList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(departmentList)) {
                departmentList.forEach(department -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                department,
                                relationName,
                                BeanUtil.getFieldValue(departmentMapper.toEntity(changeDepartmentDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(department, relationshipNames);
                });
            }
        }
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.begcode.monolith.domain.Department.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            Department byId = getById(id);
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
