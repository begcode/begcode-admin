package com.begcode.monolith.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.service.ResourceCategoryQueryService;
import com.begcode.monolith.service.ResourceCategoryService;
import com.begcode.monolith.service.criteria.ResourceCategoryCriteria;
import com.begcode.monolith.service.dto.ResourceCategoryDTO;
import com.begcode.monolith.util.ExportUtil;
import com.begcode.monolith.util.web.IPageUtil;
import com.begcode.monolith.util.web.PageableUtils;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PageRecord;
import tech.jhipster.web.util.ResponseUtil;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * 管理实体{@link com.begcode.monolith.domain.ResourceCategory}的REST Controller。
 */
public class ResourceCategoryBaseResource {

    protected final Logger log = LoggerFactory.getLogger(ResourceCategoryBaseResource.class);

    protected static final String ENTITY_NAME = "filesResourceCategory";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final ResourceCategoryService resourceCategoryService;

    protected final ResourceCategoryRepository resourceCategoryRepository;

    protected final ResourceCategoryQueryService resourceCategoryQueryService;

    public ResourceCategoryBaseResource(
        ResourceCategoryService resourceCategoryService,
        ResourceCategoryRepository resourceCategoryRepository,
        ResourceCategoryQueryService resourceCategoryQueryService
    ) {
        this.resourceCategoryService = resourceCategoryService;
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.resourceCategoryQueryService = resourceCategoryQueryService;
    }

    /**
     * {@code POST  /resource-categories} : Create a new resourceCategory.
     *
     * @param resourceCategoryDTO the resourceCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceCategoryDTO, or with status {@code 400 (Bad Request)} if the resourceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建资源分类", description = "创建并返回一个新的资源分类")
    @AutoLog(value = "新建资源分类", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<ResourceCategoryDTO> createResourceCategory(@Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceCategory : {}", resourceCategoryDTO);
        if (resourceCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceCategoryDTO result = resourceCategoryService.save(resourceCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/resource-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/:id} : Updates an existing resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新资源分类", description = "根据主键更新并返回一个更新后的资源分类")
    @AutoLog(value = "更新资源分类", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ResourceCategoryDTO> updateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResourceCategoryDTO resourceCategoryDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update ResourceCategory : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (resourceCategoryRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourceCategoryDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            resourceCategoryService.updateBatch(resourceCategoryDTO, batchFields, batchIds);
            result = resourceCategoryService.findOne(id).orElseThrow();
        } else {
            result = resourceCategoryService.update(resourceCategoryDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-categories/relations/:operateType} : Updates relationships an existing resourceCategory.
     *
     * @param operateType the operateType of the resourceCategoryDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新资源分类关联关系", description = "根据主键更新资源分类关联关系")
    @AutoLog(value = "更新资源分类关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update ResourceCategory : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        resourceCategoryService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /resource-categories/:id} : Partial updates given fields of an existing resourceCategory, field will ignore if it is null
     *
     * @param id the id of the resourceCategoryDTO to save.
     * @param resourceCategoryDTO the resourceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the resourceCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceCategoryDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新资源分类", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的资源分类")
    @AutoLog(value = "部分更新资源分类", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ResourceCategoryDTO> partialUpdateResourceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResourceCategoryDTO resourceCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceCategory partially : {}, {}", id, resourceCategoryDTO);
        if (resourceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (resourceCategoryRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceCategoryDTO> result = resourceCategoryService.partialUpdate(resourceCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resource-categories} : get all the resourceCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceCategories in body.
     */
    @GetMapping("")
    @Operation(tags = "获取资源分类分页列表", description = "获取资源分类的分页列表数据")
    @AutoLog(value = "获取资源分类分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ResourceCategoryDTO>> getAllResourceCategories(
        ResourceCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ResourceCategories by criteria: {}", criteria);

        IPage<ResourceCategoryDTO> page;
        page = resourceCategoryQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<ResourceCategoryDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /resource-categories/count} : count all the resourceCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countResourceCategories(ResourceCategoryCriteria criteria) {
        log.debug("REST request to count ResourceCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(resourceCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /resource-categories/:id} : get the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的资源分类", description = "获取指定主键的资源分类信息")
    @AutoLog(value = "获取指定主键的资源分类", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<ResourceCategoryDTO> getResourceCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get ResourceCategory : {}", id);
        Optional<ResourceCategoryDTO> resourceCategoryDTO = resourceCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceCategoryDTO);
    }

    /**
     * {@code DELETE  /resource-categories/:id} : delete the "id" resourceCategory.
     *
     * @param id the id of the resourceCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的资源分类", description = "删除指定主键的资源分类信息")
    @AutoLog(value = "删除指定主键的资源分类", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteResourceCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete ResourceCategory : {}", id);

        resourceCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /resource-categories/export : export the resourceCategories.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "资源分类EXCEL导出", description = "导出全部资源分类为EXCEL文件")
    @AutoLog(value = "资源分类EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<ResourceCategoryDTO> data = resourceCategoryService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("资源分类一览表", "资源分类"), ResourceCategoryDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "资源分类_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /resource-categories/import : import the resourceCategories from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the resourceCategoryDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "资源分类EXCEL导入", description = "根据资源分类EXCEL文件导入全部数据")
    @AutoLog(value = "资源分类EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ResourceCategoryDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ResourceCategoryDTO.class, params);
        list.forEach(resourceCategoryService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /resource-categories/tree : get all the resourceCategories for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/tree")
    @Operation(tags = "获取资源分类树形列表", description = "获取资源分类的树形列表数据")
    @AutoLog(value = "获取资源分类树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ResourceCategoryDTO>> getAllResourceCategoriesofTree(
        ResourceCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of ResourceCategories");
        IPage<ResourceCategoryDTO> page = resourceCategoryQueryService.findAllTop(criteria, PageableUtils.toPage(pageable));
        PageRecord<ResourceCategoryDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size((int) page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * GET  /resource-categories/{parentId}/tree : get all the resourceCategories for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of resourceCategories in body
     */
    @GetMapping("/{parentId}/tree")
    @Operation(tags = "获取资源分类指定节点下的树形列表", description = "获取资源分类指定节点下的树形列表数据")
    @AutoLog(value = "获取资源分类指定节点下的树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<List<ResourceCategoryDTO>> getAllResourceCategoriesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ResourceCategories of parentId");
        List<ResourceCategoryDTO> list = resourceCategoryQueryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code DELETE  /resource-categories} : delete all the "ids" ResourceCategories.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个资源分类", description = "根据主键删除多个资源分类")
    @AutoLog(value = "删除多个资源分类", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteResourceCategoriesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ResourceCategories : {}", ids);
        if (ids != null) {
            ids.forEach(resourceCategoryService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对资源分类进行统计", description = "条件和统计的配置通过资源分类的Criteria类来实现")
    @AutoLog(value = "根据条件对资源分类进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(ResourceCategoryCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = resourceCategoryQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
