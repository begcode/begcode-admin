package com.begcode.monolith.system.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.SmsSupplierQueryService;
import com.begcode.monolith.system.service.SmsSupplierService;
import com.begcode.monolith.system.service.criteria.SmsSupplierCriteria;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.SmsSupplier}的REST Controller。
 */
public class SmsSupplierBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(SmsSupplierBaseResource.class);

    protected static final String ENTITY_NAME = "filesSmsSupplier";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final SmsSupplierService smsSupplierService;

    protected final SmsSupplierRepository smsSupplierRepository;

    protected final SmsSupplierQueryService smsSupplierQueryService;

    public SmsSupplierBaseResource(
        SmsSupplierService smsSupplierService,
        SmsSupplierRepository smsSupplierRepository,
        SmsSupplierQueryService smsSupplierQueryService
    ) {
        this.smsSupplierService = smsSupplierService;
        this.smsSupplierRepository = smsSupplierRepository;
        this.smsSupplierQueryService = smsSupplierQueryService;
    }

    /**
     * {@code POST  /sms-suppliers} : Create a new smsSupplier.
     *
     * @param smsSupplierDTO the smsSupplierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsSupplierDTO, or with status {@code 400 (Bad Request)} if the smsSupplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建短信服务商配置", description = "创建并返回一个新的短信服务商配置")
    @AutoLog(value = "新建短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<SmsSupplierDTO> createSmsSupplier(@RequestBody SmsSupplierDTO smsSupplierDTO) throws URISyntaxException {
        log.debug("REST request to save SmsSupplier : {}", smsSupplierDTO);
        if (smsSupplierDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        smsSupplierDTO = smsSupplierService.save(smsSupplierDTO);
        return ResponseEntity.created(new URI("/api/sms-suppliers/" + smsSupplierDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, smsSupplierDTO.getId().toString()))
            .body(smsSupplierDTO);
    }

    /**
     * {@code PUT  /sms-suppliers/:id} : Updates an existing smsSupplier.
     *
     * @param id the id of the smsSupplierDTO to save.
     * @param smsSupplierDTO the smsSupplierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsSupplierDTO,
     * or with status {@code 400 (Bad Request)} if the smsSupplierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsSupplierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新短信服务商配置", description = "根据主键更新并返回一个更新后的短信服务商配置")
    @AutoLog(value = "更新短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsSupplierDTO> updateSmsSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsSupplierDTO smsSupplierDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update SmsSupplier : {}, {}", id, smsSupplierDTO);
        if (smsSupplierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsSupplierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsSupplierRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            smsSupplierService.updateBatch(smsSupplierDTO, batchFields, batchIds);
            smsSupplierDTO = smsSupplierService.findOne(id).orElseThrow();
        } else {
            smsSupplierDTO = smsSupplierService.update(smsSupplierDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsSupplierDTO.getId().toString()))
            .body(smsSupplierDTO);
    }

    /**
     * {@code PUT  /sms-suppliers/relations/:operateType} : Updates relationships an existing smsSupplier.
     *
     * @param operateType the operateType of the smsSupplierDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsSupplierDTO,
     * or with status {@code 400 (Bad Request)} if the smsSupplierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsSupplierDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新短信服务商配置关联关系", description = "根据主键更新短信服务商配置关联关系")
    @AutoLog(value = "更新短信服务商配置关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update SmsSupplier : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        smsSupplierService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /sms-suppliers/:id} : Partial updates given fields of an existing smsSupplier, field will ignore if it is null
     *
     * @param id the id of the smsSupplierDTO to save.
     * @param smsSupplierDTO the smsSupplierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsSupplierDTO,
     * or with status {@code 400 (Bad Request)} if the smsSupplierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsSupplierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsSupplierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        tags = "部分更新短信服务商配置",
        description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的短信服务商配置"
    )
    @AutoLog(value = "部分更新短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsSupplierDTO> partialUpdateSmsSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsSupplierDTO smsSupplierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsSupplier partially : {}, {}", id, smsSupplierDTO);
        if (smsSupplierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsSupplierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsSupplierRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsSupplierDTO> result = smsSupplierService.partialUpdate(smsSupplierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsSupplierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-suppliers} : get all the smsSuppliers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsSuppliers in body.
     */
    @GetMapping("")
    @Operation(tags = "获取短信服务商配置分页列表", description = "获取短信服务商配置的分页列表数据")
    @AutoLog(value = "获取短信服务商配置分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<SmsSupplierDTO>> getAllSmsSuppliers(
        SmsSupplierCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SmsSuppliers by criteria: {}", criteria);

        IPage<SmsSupplierDTO> page;
        page = smsSupplierQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<SmsSupplierDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /sms-suppliers/count} : count all the smsSuppliers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSmsSuppliers(SmsSupplierCriteria criteria) {
        log.debug("REST request to count SmsSuppliers by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsSupplierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-suppliers/:id} : get the "id" smsSupplier.
     *
     * @param id the id of the smsSupplierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsSupplierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的短信服务商配置", description = "获取指定主键的短信服务商配置信息")
    @AutoLog(value = "获取指定主键的短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<SmsSupplierDTO> getSmsSupplier(@PathVariable("id") Long id) {
        log.debug("REST request to get SmsSupplier : {}", id);
        Optional<SmsSupplierDTO> smsSupplierDTO = smsSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsSupplierDTO);
    }

    /**
     * {@code DELETE  /sms-suppliers/:id} : delete the "id" smsSupplier.
     *
     * @param id the id of the smsSupplierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的短信服务商配置", description = "删除指定主键的短信服务商配置信息")
    @AutoLog(value = "删除指定主键的短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsSupplier(@PathVariable("id") Long id) {
        log.debug("REST request to delete SmsSupplier : {}", id);

        smsSupplierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /sms-suppliers/export : export the smsSuppliers.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "短信服务商配置EXCEL导出", description = "导出全部短信服务商配置为EXCEL文件")
    @AutoLog(value = "短信服务商配置EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<SmsSupplierDTO> data = smsSupplierService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("短信服务商配置一览表", "短信服务商配置"),
            SmsSupplierDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "短信服务商配置_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /sms-suppliers/import : import the smsSuppliers from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsSupplierDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "短信服务商配置EXCEL导入", description = "根据短信服务商配置EXCEL文件导入全部数据")
    @AutoLog(value = "短信服务商配置EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SmsSupplierDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SmsSupplierDTO.class, params);
        list.forEach(smsSupplierService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-suppliers} : delete all the "ids" SmsSuppliers.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个短信服务商配置", description = "根据主键删除多个短信服务商配置")
    @AutoLog(value = "删除多个短信服务商配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsSuppliersByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsSuppliers : {}", ids);
        if (ids != null) {
            ids.forEach(smsSupplierService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对短信服务商配置进行统计", description = "条件和统计的配置通过短信服务商配置的Criteria类来实现")
    @AutoLog(value = "根据条件对短信服务商配置进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(SmsSupplierCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = smsSupplierQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
