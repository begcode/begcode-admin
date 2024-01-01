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
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.service.ApiPermissionQueryService;
import com.begcode.monolith.service.ApiPermissionService;
import com.begcode.monolith.service.criteria.ApiPermissionCriteria;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.util.ExportUtil;
import com.begcode.monolith.util.web.IPageUtil;
import com.begcode.monolith.util.web.PageableUtils;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
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

 * 管理实体{@link com.begcode.monolith.domain.ApiPermission}的REST Controller。
 */
public class ApiPermissionBaseResource {

    protected final Logger log = LoggerFactory.getLogger(ApiPermissionBaseResource.class);

    protected static final String ENTITY_NAME = "systemApiPermission";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final ApiPermissionService apiPermissionService;

    protected final ApiPermissionRepository apiPermissionRepository;

    protected final ApiPermissionQueryService apiPermissionQueryService;

    public ApiPermissionBaseResource(
        ApiPermissionService apiPermissionService,
        ApiPermissionRepository apiPermissionRepository,
        ApiPermissionQueryService apiPermissionQueryService
    ) {
        this.apiPermissionService = apiPermissionService;
        this.apiPermissionRepository = apiPermissionRepository;
        this.apiPermissionQueryService = apiPermissionQueryService;
    }

    /**
     * {@code POST  /api-permissions} : Create a new apiPermission.
     *
     * @param apiPermissionDTO the apiPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiPermissionDTO, or with status {@code 400 (Bad Request)} if the apiPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建API权限", description = "创建并返回一个新的API权限")
    @AutoLog(value = "新建API权限", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<ApiPermissionDTO> createApiPermission(@RequestBody ApiPermissionDTO apiPermissionDTO) throws URISyntaxException {
        log.debug("REST request to save ApiPermission : {}", apiPermissionDTO);
        if (apiPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new apiPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiPermissionDTO result = apiPermissionService.save(apiPermissionDTO);
        return ResponseEntity
            .created(new URI("/api/api-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions/:id} : Updates an existing apiPermission.
     *
     * @param id the id of the apiPermissionDTO to save.
     * @param apiPermissionDTO the apiPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新API权限", description = "根据主键更新并返回一个更新后的API权限")
    @AutoLog(value = "更新API权限", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ApiPermissionDTO> updateApiPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiPermissionDTO apiPermissionDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update ApiPermission : {}, {}", id, apiPermissionDTO);
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (apiPermissionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApiPermissionDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            apiPermissionService.updateBatch(apiPermissionDTO, batchFields, batchIds);
            result = apiPermissionService.findOne(id).orElseThrow();
        } else {
            result = apiPermissionService.update(apiPermissionDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-permissions/relations/:operateType} : Updates relationships an existing apiPermission.
     *
     * @param operateType the operateType of the apiPermissionDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新API权限关联关系", description = "根据主键更新API权限关联关系")
    @AutoLog(value = "更新API权限关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update ApiPermission : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        apiPermissionService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /api-permissions/:id} : Partial updates given fields of an existing apiPermission, field will ignore if it is null
     *
     * @param id the id of the apiPermissionDTO to save.
     * @param apiPermissionDTO the apiPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the apiPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the apiPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the apiPermissionDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新API权限", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的API权限")
    @AutoLog(value = "部分更新API权限", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ApiPermissionDTO> partialUpdateApiPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiPermissionDTO apiPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApiPermission partially : {}, {}", id, apiPermissionDTO);
        if (apiPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (apiPermissionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApiPermissionDTO> result = apiPermissionService.partialUpdate(apiPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /api-permissions} : get all the apiPermissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiPermissions in body.
     */
    @GetMapping("")
    @Operation(tags = "获取API权限分页列表", description = "获取API权限的分页列表数据")
    @AutoLog(value = "获取API权限分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ApiPermissionDTO>> getAllApiPermissions(
        ApiPermissionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ApiPermissions by criteria: {}", criteria);

        IPage<ApiPermissionDTO> page;
        page = apiPermissionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<ApiPermissionDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /api-permissions/count} : count all the apiPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countApiPermissions(ApiPermissionCriteria criteria) {
        log.debug("REST request to count ApiPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(apiPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /api-permissions/:id} : get the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的API权限", description = "获取指定主键的API权限信息")
    @AutoLog(value = "获取指定主键的API权限", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<ApiPermissionDTO> getApiPermission(@PathVariable("id") Long id) {
        log.debug("REST request to get ApiPermission : {}", id);
        Optional<ApiPermissionDTO> apiPermissionDTO = apiPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apiPermissionDTO);
    }

    /**
     * {@code DELETE  /api-permissions/:id} : delete the "id" apiPermission.
     *
     * @param id the id of the apiPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的API权限", description = "删除指定主键的API权限信息")
    @AutoLog(value = "删除指定主键的API权限", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteApiPermission(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApiPermission : {}", id);

        apiPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /api-permissions/export : export the apiPermissions.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "API权限EXCEL导出", description = "导出全部API权限为EXCEL文件")
    @AutoLog(value = "API权限EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<ApiPermissionDTO> data = apiPermissionService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("API权限一览表", "API权限"), ApiPermissionDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "API权限_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /api-permissions/import : import the apiPermissions from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the apiPermissionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "API权限EXCEL导入", description = "根据API权限EXCEL文件导入全部数据")
    @AutoLog(value = "API权限EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ApiPermissionDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ApiPermissionDTO.class, params);
        list.forEach(apiPermissionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /api-permissions/tree : get all the apiPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/tree")
    @Operation(tags = "获取API权限树形列表", description = "获取API权限的树形列表数据")
    @AutoLog(value = "获取API权限树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ApiPermissionDTO>> getAllApiPermissionsofTree(ApiPermissionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get a page of ApiPermissions");
        IPage<ApiPermissionDTO> page = apiPermissionQueryService.findAllTop(criteria, PageableUtils.toPage(pageable));
        PageRecord<ApiPermissionDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size((int) page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * GET  /api-permissions/{parentId}/tree : get all the apiPermissions for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/{parentId}/tree")
    @Operation(tags = "获取API权限指定节点下的树形列表", description = "获取API权限指定节点下的树形列表数据")
    @AutoLog(value = "获取API权限指定节点下的树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<List<ApiPermissionDTO>> getAllApiPermissionsofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ApiPermissions of parentId");
        List<ApiPermissionDTO> list = apiPermissionQueryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code DELETE  /api-permissions} : delete all the "ids" ApiPermissions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个API权限", description = "根据主键删除多个API权限")
    @AutoLog(value = "删除多个API权限", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteApiPermissionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ApiPermissions : {}", ids);
        if (ids != null) {
            ids.forEach(apiPermissionService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对API权限进行统计", description = "条件和统计的配置通过API权限的Criteria类来实现")
    @AutoLog(value = "根据条件对API权限进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(ApiPermissionCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = apiPermissionQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    /**
     * GET  /generate : regenerate all api permissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apiPermissions in body
     */
    @GetMapping("/generate")
    public ResponseEntity<Void> generate() {
        log.debug("REST request to get a page of ApiPermissions");
        apiPermissionService.regenerateApiPermissions();
        return ResponseEntity.ok().build();
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
