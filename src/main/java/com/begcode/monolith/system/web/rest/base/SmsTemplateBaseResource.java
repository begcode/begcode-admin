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
import com.begcode.monolith.system.repository.SmsTemplateRepository;
import com.begcode.monolith.system.service.SmsTemplateQueryService;
import com.begcode.monolith.system.service.SmsTemplateService;
import com.begcode.monolith.system.service.criteria.SmsTemplateCriteria;
import com.begcode.monolith.system.service.dto.SmsTemplateDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.SmsTemplate}的REST Controller。
 */
public class SmsTemplateBaseResource {

    protected final Logger log = LoggerFactory.getLogger(SmsTemplateBaseResource.class);

    protected static final String ENTITY_NAME = "filesSmsTemplate";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final SmsTemplateService smsTemplateService;

    protected final SmsTemplateRepository smsTemplateRepository;

    protected final SmsTemplateQueryService smsTemplateQueryService;

    public SmsTemplateBaseResource(
        SmsTemplateService smsTemplateService,
        SmsTemplateRepository smsTemplateRepository,
        SmsTemplateQueryService smsTemplateQueryService
    ) {
        this.smsTemplateService = smsTemplateService;
        this.smsTemplateRepository = smsTemplateRepository;
        this.smsTemplateQueryService = smsTemplateQueryService;
    }

    /**
     * {@code POST  /sms-templates} : Create a new smsTemplate.
     *
     * @param smsTemplateDTO the smsTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsTemplateDTO, or with status {@code 400 (Bad Request)} if the smsTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建消息模板", description = "创建并返回一个新的消息模板")
    @AutoLog(value = "新建消息模板", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<SmsTemplateDTO> createSmsTemplate(@RequestBody SmsTemplateDTO smsTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save SmsTemplate : {}", smsTemplateDTO);
        if (smsTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsTemplateDTO result = smsTemplateService.save(smsTemplateDTO);
        return ResponseEntity
            .created(new URI("/api/sms-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-templates/:id} : Updates an existing smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to save.
     * @param smsTemplateDTO the smsTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新消息模板", description = "根据主键更新并返回一个更新后的消息模板")
    @AutoLog(value = "更新消息模板", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsTemplateDTO> updateSmsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsTemplateDTO smsTemplateDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update SmsTemplate : {}, {}", id, smsTemplateDTO);
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsTemplateRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SmsTemplateDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            smsTemplateService.updateBatch(smsTemplateDTO, batchFields, batchIds);
            result = smsTemplateService.findOne(id).orElseThrow();
        } else {
            result = smsTemplateService.update(smsTemplateDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-templates/relations/:operateType} : Updates relationships an existing smsTemplate.
     *
     * @param operateType the operateType of the smsTemplateDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新消息模板关联关系", description = "根据主键更新消息模板关联关系")
    @AutoLog(value = "更新消息模板关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update SmsTemplate : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        smsTemplateService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /sms-templates/:id} : Partial updates given fields of an existing smsTemplate, field will ignore if it is null
     *
     * @param id the id of the smsTemplateDTO to save.
     * @param smsTemplateDTO the smsTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the smsTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsTemplateDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新消息模板", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的消息模板")
    @AutoLog(value = "部分更新消息模板", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsTemplateDTO> partialUpdateSmsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsTemplateDTO smsTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsTemplate partially : {}, {}", id, smsTemplateDTO);
        if (smsTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsTemplateRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsTemplateDTO> result = smsTemplateService.partialUpdate(smsTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-templates} : get all the smsTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsTemplates in body.
     */
    @GetMapping("")
    @Operation(tags = "获取消息模板分页列表", description = "获取消息模板的分页列表数据")
    @AutoLog(value = "获取消息模板分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<SmsTemplateDTO>> getAllSmsTemplates(
        SmsTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SmsTemplates by criteria: {}", criteria);

        IPage<SmsTemplateDTO> page;
        page = smsTemplateQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<SmsTemplateDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /sms-templates/count} : count all the smsTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSmsTemplates(SmsTemplateCriteria criteria) {
        log.debug("REST request to count SmsTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-templates/:id} : get the "id" smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的消息模板", description = "获取指定主键的消息模板信息")
    @AutoLog(value = "获取指定主键的消息模板", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<SmsTemplateDTO> getSmsTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to get SmsTemplate : {}", id);
        Optional<SmsTemplateDTO> smsTemplateDTO = smsTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsTemplateDTO);
    }

    /**
     * {@code DELETE  /sms-templates/:id} : delete the "id" smsTemplate.
     *
     * @param id the id of the smsTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的消息模板", description = "删除指定主键的消息模板信息")
    @AutoLog(value = "删除指定主键的消息模板", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to delete SmsTemplate : {}", id);

        smsTemplateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /sms-templates/export : export the smsTemplates.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "消息模板EXCEL导出", description = "导出全部消息模板为EXCEL文件")
    @AutoLog(value = "消息模板EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<SmsTemplateDTO> data = smsTemplateService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("消息模板一览表", "消息模板"), SmsTemplateDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "消息模板_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /sms-templates/import : import the smsTemplates from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsTemplateDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "消息模板EXCEL导入", description = "根据消息模板EXCEL文件导入全部数据")
    @AutoLog(value = "消息模板EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SmsTemplateDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SmsTemplateDTO.class, params);
        list.forEach(smsTemplateService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-templates} : delete all the "ids" SmsTemplates.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个消息模板", description = "根据主键删除多个消息模板")
    @AutoLog(value = "删除多个消息模板", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsTemplatesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsTemplates : {}", ids);
        if (ids != null) {
            ids.forEach(smsTemplateService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对消息模板进行统计", description = "条件和统计的配置通过消息模板的Criteria类来实现")
    @AutoLog(value = "根据条件对消息模板进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(SmsTemplateCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = smsTemplateQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    @GetMapping("/sync-from-supplier/{supplierId}")
    @Operation(tags = "同步短信服务商短信模板", description = "同步短信服务商短信模板，supplierId为短信服务商ID")
    @AutoLog(value = "同步短信服务商短信模板", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public void stats(@PathVariable("supplierId") Long supplierId) {
        log.debug("REST request to get supplierId: {}", supplierId);
        smsTemplateService.importSmsTemplateFromSupplier(supplierId);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
