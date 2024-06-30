package com.begcode.monolith.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.ViewPermissionQueryService;
import com.begcode.monolith.service.ViewPermissionService;
import com.begcode.monolith.service.criteria.ViewPermissionCriteria;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
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
import org.apache.commons.lang3.ObjectUtils;
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

 * 管理实体{@link com.begcode.monolith.domain.ViewPermission}的REST Controller。
 */
public class ViewPermissionBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(ViewPermissionBaseResource.class);

    protected static final String ENTITY_NAME = "systemViewPermission";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final ViewPermissionService viewPermissionService;

    protected final ViewPermissionRepository viewPermissionRepository;

    protected final ViewPermissionQueryService viewPermissionQueryService;

    public ViewPermissionBaseResource(
        ViewPermissionService viewPermissionService,
        ViewPermissionRepository viewPermissionRepository,
        ViewPermissionQueryService viewPermissionQueryService
    ) {
        this.viewPermissionService = viewPermissionService;
        this.viewPermissionRepository = viewPermissionRepository;
        this.viewPermissionQueryService = viewPermissionQueryService;
    }

    /**
     * {@code POST  /view-permissions} : Create a new viewPermission.
     *
     * @param viewPermissionDTO the viewPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewPermissionDTO, or with status {@code 400 (Bad Request)} if the viewPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建可视权限", description = "创建并返回一个新的可视权限")
    @AutoLog(value = "新建可视权限", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<ViewPermissionDTO> createViewPermission(@Valid @RequestBody ViewPermissionDTO viewPermissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ViewPermission : {}", viewPermissionDTO);
        if (viewPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new viewPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        viewPermissionDTO = viewPermissionService.save(viewPermissionDTO);
        return ResponseEntity.created(new URI("/api/view-permissions/" + viewPermissionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString()))
            .body(viewPermissionDTO);
    }

    /**
     * {@code PUT  /view-permissions/:id} : Updates an existing viewPermission.
     *
     * @param id the id of the viewPermissionDTO to save.
     * @param viewPermissionDTO the viewPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新可视权限", description = "根据主键更新并返回一个更新后的可视权限")
    @AutoLog(value = "更新可视权限", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ViewPermissionDTO> updateViewPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ViewPermissionDTO viewPermissionDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update ViewPermission : {}, {}", id, viewPermissionDTO);
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (viewPermissionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            viewPermissionService.updateBatch(viewPermissionDTO, batchFields, batchIds);
            viewPermissionDTO = viewPermissionService.findOne(id).orElseThrow();
        } else {
            viewPermissionDTO = viewPermissionService.update(viewPermissionDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString()))
            .body(viewPermissionDTO);
    }

    /**
     * {@code PUT  /view-permissions/relations/:operateType} : Updates relationships an existing viewPermission.
     *
     * @param operateType the operateType of the viewPermissionDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新可视权限关联关系", description = "根据主键更新可视权限关联关系")
    @AutoLog(value = "更新可视权限关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update ViewPermission : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        viewPermissionService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PUT  /view-permissions/sort-value/:id/:type} : Updates sort value viewPermission.
     *
     * @param id the id of the viewPermissionDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     */
    @PutMapping("/sort-value/{id}/{type}")
    @Operation(tags = "更新排序字段值", description = "根据移动记录ID和插入点前后记录ID更新排序值")
    @AutoLog(value = "更新排序字段值", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateSortValue(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "type") SortValueOperateType type,
        @RequestParam(value = "beforeId", required = false) Long beforeId,
        @RequestParam(value = "afterId", required = false) Long afterId,
        @RequestParam(value = "newSortValue", required = false) Integer newSortValue,
        @RequestBody(required = false) ViewPermissionCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (viewPermissionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<ViewPermission> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = viewPermissionQueryService.createQueryWrapper(criteria);
        }
        Boolean result = viewPermissionService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /view-permissions/:id} : Partial updates given fields of an existing viewPermission, field will ignore if it is null
     *
     * @param id the id of the viewPermissionDTO to save.
     * @param viewPermissionDTO the viewPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the viewPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the viewPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新可视权限", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的可视权限")
    @AutoLog(value = "部分更新可视权限", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<ViewPermissionDTO> partialUpdateViewPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ViewPermissionDTO viewPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ViewPermission partially : {}, {}", id, viewPermissionDTO);
        if (viewPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (viewPermissionRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewPermissionDTO> result = viewPermissionService.partialUpdate(viewPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viewPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /view-permissions} : get all the viewPermissions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPermissions in body.
     */
    @GetMapping("")
    @Operation(tags = "获取可视权限分页列表", description = "获取可视权限的分页列表数据")
    @AutoLog(value = "获取可视权限分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ViewPermissionDTO>> getAllViewPermissions(
        ViewPermissionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ViewPermissions by criteria: {}", criteria);

        IPage<ViewPermissionDTO> page;
        page = viewPermissionQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<ViewPermissionDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /view-permissions/count} : count all the viewPermissions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countViewPermissions(ViewPermissionCriteria criteria) {
        log.debug("REST request to count ViewPermissions by criteria: {}", criteria);
        return ResponseEntity.ok().body(viewPermissionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /view-permissions/:id} : get the "id" viewPermission.
     *
     * @param id the id of the viewPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的可视权限", description = "获取指定主键的可视权限信息")
    @AutoLog(value = "获取指定主键的可视权限", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<ViewPermissionDTO> getViewPermission(@PathVariable("id") Long id) {
        log.debug("REST request to get ViewPermission : {}", id);
        Optional<ViewPermissionDTO> viewPermissionDTO = viewPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewPermissionDTO);
    }

    /**
     * {@code DELETE  /view-permissions/:id} : delete the "id" viewPermission.
     *
     * @param id the id of the viewPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的可视权限", description = "删除指定主键的可视权限信息")
    @AutoLog(value = "删除指定主键的可视权限", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteViewPermission(@PathVariable("id") Long id) {
        log.debug("REST request to delete ViewPermission : {}", id);

        viewPermissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /view-permissions/export : export the viewPermissions.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "可视权限EXCEL导出", description = "导出全部可视权限为EXCEL文件")
    @AutoLog(value = "可视权限EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<ViewPermissionDTO> data = viewPermissionService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("可视权限一览表", "可视权限"), ViewPermissionDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "可视权限_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /view-permissions/import : import the viewPermissions from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the viewPermissionDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "可视权限EXCEL导入", description = "根据可视权限EXCEL文件导入全部数据")
    @AutoLog(value = "可视权限EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<ViewPermissionDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), ViewPermissionDTO.class, params);
        list.forEach(viewPermissionService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /view-permissions/tree : get all the viewPermissions for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of viewPermissions in body
     */
    @GetMapping("/tree")
    @Operation(tags = "获取可视权限树形列表", description = "获取可视权限的树形列表数据")
    @AutoLog(value = "获取可视权限树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<ViewPermissionDTO>> getAllViewPermissionsofTree(ViewPermissionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get a page of ViewPermissions");
        IPage<ViewPermissionDTO> page = viewPermissionQueryService.findAllTop(criteria, PageableUtils.toPage(pageable));
        PageRecord<ViewPermissionDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size((int) page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * GET  /view-permissions/{parentId}/tree : get all the viewPermissions for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of viewPermissions in body
     */
    @GetMapping("/{parentId}/tree")
    @Operation(tags = "获取可视权限指定节点下的树形列表", description = "获取可视权限指定节点下的树形列表数据")
    @AutoLog(value = "获取可视权限指定节点下的树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissionsofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all ViewPermissions of parentId");
        List<ViewPermissionDTO> list = viewPermissionQueryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code DELETE  /view-permissions} : delete all the "ids" ViewPermissions.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个可视权限", description = "根据主键删除多个可视权限")
    @AutoLog(value = "删除多个可视权限", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteViewPermissionsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete ViewPermissions : {}", ids);
        if (ids != null) {
            ids.forEach(viewPermissionService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对可视权限进行统计", description = "条件和统计的配置通过可视权限的Criteria类来实现")
    @AutoLog(value = "根据条件对可视权限进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(ViewPermissionCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = viewPermissionQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    /**
     * {@code GET  /current-user} : get all the viewPermissions of currentUser.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPermissions in body.
     */
    @GetMapping("/current-user")
    public ResponseEntity<List<ViewPermissionDTO>> getAllViewPermissionsByLogin() {
        log.debug("REST request to get ViewPermissions by current-user");
        return ResponseEntity.ok().body(viewPermissionService.getAllByLogin());
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
