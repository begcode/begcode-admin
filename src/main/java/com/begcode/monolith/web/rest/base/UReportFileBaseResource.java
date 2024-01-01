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
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.UReportFileQueryService;
import com.begcode.monolith.service.UReportFileService;
import com.begcode.monolith.service.criteria.UReportFileCriteria;
import com.begcode.monolith.service.dto.UReportFileDTO;
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

 * 管理实体{@link com.begcode.monolith.domain.UReportFile}的REST Controller。
 */
public class UReportFileBaseResource {

    protected final Logger log = LoggerFactory.getLogger(UReportFileBaseResource.class);

    protected static final String ENTITY_NAME = "reportUReportFile";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final UReportFileService uReportFileService;

    protected final UReportFileRepository uReportFileRepository;

    protected final UReportFileQueryService uReportFileQueryService;

    public UReportFileBaseResource(
        UReportFileService uReportFileService,
        UReportFileRepository uReportFileRepository,
        UReportFileQueryService uReportFileQueryService
    ) {
        this.uReportFileService = uReportFileService;
        this.uReportFileRepository = uReportFileRepository;
        this.uReportFileQueryService = uReportFileQueryService;
    }

    /**
     * {@code POST  /u-report-files} : Create a new uReportFile.
     *
     * @param uReportFileDTO the uReportFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uReportFileDTO, or with status {@code 400 (Bad Request)} if the uReportFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建报表存储", description = "创建并返回一个新的报表存储")
    @AutoLog(value = "新建报表存储", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<UReportFileDTO> createUReportFile(@Valid @RequestBody UReportFileDTO uReportFileDTO) throws URISyntaxException {
        log.debug("REST request to save UReportFile : {}", uReportFileDTO);
        if (uReportFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new uReportFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UReportFileDTO result = uReportFileService.save(uReportFileDTO);
        return ResponseEntity
            .created(new URI("/api/u-report-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /u-report-files/:id} : Updates an existing uReportFile.
     *
     * @param id the id of the uReportFileDTO to save.
     * @param uReportFileDTO the uReportFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新报表存储", description = "根据主键更新并返回一个更新后的报表存储")
    @AutoLog(value = "更新报表存储", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UReportFileDTO> updateUReportFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UReportFileDTO uReportFileDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update UReportFile : {}, {}", id, uReportFileDTO);
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uReportFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uReportFileRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UReportFileDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            uReportFileService.updateBatch(uReportFileDTO, batchFields, batchIds);
            result = uReportFileService.findOne(id).orElseThrow();
        } else {
            result = uReportFileService.update(uReportFileDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uReportFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /u-report-files/relations/:operateType} : Updates relationships an existing uReportFile.
     *
     * @param operateType the operateType of the uReportFileDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新报表存储关联关系", description = "根据主键更新报表存储关联关系")
    @AutoLog(value = "更新报表存储关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update UReportFile : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        uReportFileService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /u-report-files/:id} : Partial updates given fields of an existing uReportFile, field will ignore if it is null
     *
     * @param id the id of the uReportFileDTO to save.
     * @param uReportFileDTO the uReportFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uReportFileDTO,
     * or with status {@code 400 (Bad Request)} if the uReportFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uReportFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uReportFileDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新报表存储", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的报表存储")
    @AutoLog(value = "部分更新报表存储", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UReportFileDTO> partialUpdateUReportFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UReportFileDTO uReportFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UReportFile partially : {}, {}", id, uReportFileDTO);
        if (uReportFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uReportFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uReportFileRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UReportFileDTO> result = uReportFileService.partialUpdate(uReportFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uReportFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /u-report-files} : get all the uReportFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uReportFiles in body.
     */
    @GetMapping("")
    @Operation(tags = "获取报表存储分页列表", description = "获取报表存储的分页列表数据")
    @AutoLog(value = "获取报表存储分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<UReportFileDTO>> getAllUReportFiles(
        UReportFileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UReportFiles by criteria: {}", criteria);

        IPage<UReportFileDTO> page;
        page = uReportFileQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<UReportFileDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /u-report-files/count} : count all the uReportFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUReportFiles(UReportFileCriteria criteria) {
        log.debug("REST request to count UReportFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(uReportFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /u-report-files/:id} : get the "id" uReportFile.
     *
     * @param id the id of the uReportFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uReportFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的报表存储", description = "获取指定主键的报表存储信息")
    @AutoLog(value = "获取指定主键的报表存储", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<UReportFileDTO> getUReportFile(@PathVariable("id") Long id) {
        log.debug("REST request to get UReportFile : {}", id);
        Optional<UReportFileDTO> uReportFileDTO = uReportFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uReportFileDTO);
    }

    /**
     * {@code DELETE  /u-report-files/:id} : delete the "id" uReportFile.
     *
     * @param id the id of the uReportFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的报表存储", description = "删除指定主键的报表存储信息")
    @AutoLog(value = "删除指定主键的报表存储", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUReportFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete UReportFile : {}", id);

        uReportFileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /u-report-files/export : export the uReportFiles.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "报表存储EXCEL导出", description = "导出全部报表存储为EXCEL文件")
    @AutoLog(value = "报表存储EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<UReportFileDTO> data = uReportFileService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("报表存储一览表", "报表存储"), UReportFileDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "报表存储_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /u-report-files/import : import the uReportFiles from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uReportFileDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "报表存储EXCEL导入", description = "根据报表存储EXCEL文件导入全部数据")
    @AutoLog(value = "报表存储EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<UReportFileDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), UReportFileDTO.class, params);
        list.forEach(uReportFileService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /u-report-files} : delete all the "ids" UReportFiles.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个报表存储", description = "根据主键删除多个报表存储")
    @AutoLog(value = "删除多个报表存储", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUReportFilesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UReportFiles : {}", ids);
        if (ids != null) {
            ids.forEach(uReportFileService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对报表存储进行统计", description = "条件和统计的配置通过报表存储的Criteria类来实现")
    @AutoLog(value = "根据条件对报表存储进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(UReportFileCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = uReportFileQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
