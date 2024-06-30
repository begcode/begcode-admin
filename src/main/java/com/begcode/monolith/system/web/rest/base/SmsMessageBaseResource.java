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
import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.SmsMessageQueryService;
import com.begcode.monolith.system.service.SmsMessageService;
import com.begcode.monolith.system.service.criteria.SmsMessageCriteria;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.SmsMessage}的REST Controller。
 */
public class SmsMessageBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(SmsMessageBaseResource.class);

    protected static final String ENTITY_NAME = "systemSmsMessage";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final SmsMessageService smsMessageService;

    protected final SmsMessageRepository smsMessageRepository;

    protected final SmsMessageQueryService smsMessageQueryService;

    public SmsMessageBaseResource(
        SmsMessageService smsMessageService,
        SmsMessageRepository smsMessageRepository,
        SmsMessageQueryService smsMessageQueryService
    ) {
        this.smsMessageService = smsMessageService;
        this.smsMessageRepository = smsMessageRepository;
        this.smsMessageQueryService = smsMessageQueryService;
    }

    /**
     * {@code POST  /sms-messages} : Create a new smsMessage.
     *
     * @param smsMessageDTO the smsMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsMessageDTO, or with status {@code 400 (Bad Request)} if the smsMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建短信消息", description = "创建并返回一个新的短信消息")
    @AutoLog(value = "新建短信消息", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<SmsMessageDTO> createSmsMessage(@RequestBody SmsMessageDTO smsMessageDTO) throws URISyntaxException {
        log.debug("REST request to save SmsMessage : {}", smsMessageDTO);
        if (smsMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        smsMessageDTO = smsMessageService.save(smsMessageDTO);
        return ResponseEntity.created(new URI("/api/sms-messages/" + smsMessageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString()))
            .body(smsMessageDTO);
    }

    /**
     * {@code PUT  /sms-messages/:id} : Updates an existing smsMessage.
     *
     * @param id the id of the smsMessageDTO to save.
     * @param smsMessageDTO the smsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新短信消息", description = "根据主键更新并返回一个更新后的短信消息")
    @AutoLog(value = "更新短信消息", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsMessageDTO> updateSmsMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsMessageDTO smsMessageDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update SmsMessage : {}, {}", id, smsMessageDTO);
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsMessageRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            smsMessageService.updateBatch(smsMessageDTO, batchFields, batchIds);
            smsMessageDTO = smsMessageService.findOne(id).orElseThrow();
        } else {
            smsMessageDTO = smsMessageService.update(smsMessageDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString()))
            .body(smsMessageDTO);
    }

    /**
     * {@code PUT  /sms-messages/relations/:operateType} : Updates relationships an existing smsMessage.
     *
     * @param operateType the operateType of the smsMessageDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新短信消息关联关系", description = "根据主键更新短信消息关联关系")
    @AutoLog(value = "更新短信消息关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update SmsMessage : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        smsMessageService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /sms-messages/:id} : Partial updates given fields of an existing smsMessage, field will ignore if it is null
     *
     * @param id the id of the smsMessageDTO to save.
     * @param smsMessageDTO the smsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the smsMessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smsMessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新短信消息", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的短信消息")
    @AutoLog(value = "部分更新短信消息", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SmsMessageDTO> partialUpdateSmsMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SmsMessageDTO smsMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SmsMessage partially : {}, {}", id, smsMessageDTO);
        if (smsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (smsMessageRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmsMessageDTO> result = smsMessageService.partialUpdate(smsMessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsMessageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sms-messages} : get all the smsMessages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsMessages in body.
     */
    @GetMapping("")
    @Operation(tags = "获取短信消息分页列表", description = "获取短信消息的分页列表数据")
    @AutoLog(value = "获取短信消息分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<SmsMessageDTO>> getAllSmsMessages(
        SmsMessageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SmsMessages by criteria: {}", criteria);

        IPage<SmsMessageDTO> page;
        page = smsMessageQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<SmsMessageDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /sms-messages/count} : count all the smsMessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSmsMessages(SmsMessageCriteria criteria) {
        log.debug("REST request to count SmsMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(smsMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sms-messages/:id} : get the "id" smsMessage.
     *
     * @param id the id of the smsMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的短信消息", description = "获取指定主键的短信消息信息")
    @AutoLog(value = "获取指定主键的短信消息", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<SmsMessageDTO> getSmsMessage(@PathVariable("id") Long id) {
        log.debug("REST request to get SmsMessage : {}", id);
        Optional<SmsMessageDTO> smsMessageDTO = smsMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsMessageDTO);
    }

    /**
     * {@code DELETE  /sms-messages/:id} : delete the "id" smsMessage.
     *
     * @param id the id of the smsMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的短信消息", description = "删除指定主键的短信消息信息")
    @AutoLog(value = "删除指定主键的短信消息", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsMessage(@PathVariable("id") Long id) {
        log.debug("REST request to delete SmsMessage : {}", id);

        smsMessageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /sms-messages/export : export the smsMessages.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "短信消息EXCEL导出", description = "导出全部短信消息为EXCEL文件")
    @AutoLog(value = "短信消息EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<SmsMessageDTO> data = smsMessageService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("短信消息一览表", "短信消息"), SmsMessageDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "短信消息_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /sms-messages/import : import the smsMessages from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the smsMessageDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "短信消息EXCEL导入", description = "根据短信消息EXCEL文件导入全部数据")
    @AutoLog(value = "短信消息EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SmsMessageDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SmsMessageDTO.class, params);
        list.forEach(smsMessageService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sms-messages} : delete all the "ids" SmsMessages.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个短信消息", description = "根据主键删除多个短信消息")
    @AutoLog(value = "删除多个短信消息", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSmsMessagesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SmsMessages : {}", ids);
        if (ids != null) {
            ids.forEach(smsMessageService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对短信消息进行统计", description = "条件和统计的配置通过短信消息的Criteria类来实现")
    @AutoLog(value = "根据条件对短信消息进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(SmsMessageCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = smsMessageQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
