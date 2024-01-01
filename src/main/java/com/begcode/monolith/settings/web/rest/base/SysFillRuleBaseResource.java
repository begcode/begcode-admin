package com.begcode.monolith.settings.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.SysFillRuleQueryService;
import com.begcode.monolith.settings.service.SysFillRuleService;
import com.begcode.monolith.settings.service.criteria.SysFillRuleCriteria;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
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

 * 管理实体{@link com.begcode.monolith.settings.domain.SysFillRule}的REST Controller。
 */
public class SysFillRuleBaseResource {

    protected final Logger log = LoggerFactory.getLogger(SysFillRuleBaseResource.class);

    protected static final String ENTITY_NAME = "settingsSysFillRule";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final SysFillRuleService sysFillRuleService;

    protected final SysFillRuleRepository sysFillRuleRepository;

    protected final SysFillRuleQueryService sysFillRuleQueryService;

    public SysFillRuleBaseResource(
        SysFillRuleService sysFillRuleService,
        SysFillRuleRepository sysFillRuleRepository,
        SysFillRuleQueryService sysFillRuleQueryService
    ) {
        this.sysFillRuleService = sysFillRuleService;
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.sysFillRuleQueryService = sysFillRuleQueryService;
    }

    /**
     * {@code POST  /sys-fill-rules} : Create a new sysFillRule.
     *
     * @param sysFillRuleDTO the sysFillRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sysFillRuleDTO, or with status {@code 400 (Bad Request)} if the sysFillRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建填充规则", description = "创建并返回一个新的填充规则")
    @AutoLog(value = "新建填充规则", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<SysFillRuleDTO> createSysFillRule(@RequestBody SysFillRuleDTO sysFillRuleDTO) throws URISyntaxException {
        log.debug("REST request to save SysFillRule : {}", sysFillRuleDTO);
        if (sysFillRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sysFillRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysFillRuleDTO result = sysFillRuleService.save(sysFillRuleDTO);
        return ResponseEntity
            .created(new URI("/api/sys-fill-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/:id} : Updates an existing sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新填充规则", description = "根据主键更新并返回一个更新后的填充规则")
    @AutoLog(value = "更新填充规则", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SysFillRuleDTO> updateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update SysFillRule : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (sysFillRuleRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SysFillRuleDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            sysFillRuleService.updateBatch(sysFillRuleDTO, batchFields, batchIds);
            result = sysFillRuleService.findOne(id).orElseThrow();
        } else {
            result = sysFillRuleService.update(sysFillRuleDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sys-fill-rules/relations/:operateType} : Updates relationships an existing sysFillRule.
     *
     * @param operateType the operateType of the sysFillRuleDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新填充规则关联关系", description = "根据主键更新填充规则关联关系")
    @AutoLog(value = "更新填充规则关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update SysFillRule : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        sysFillRuleService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /sys-fill-rules/:id} : Partial updates given fields of an existing sysFillRule, field will ignore if it is null
     *
     * @param id the id of the sysFillRuleDTO to save.
     * @param sysFillRuleDTO the sysFillRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sysFillRuleDTO,
     * or with status {@code 400 (Bad Request)} if the sysFillRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sysFillRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sysFillRuleDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新填充规则", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的填充规则")
    @AutoLog(value = "部分更新填充规则", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SysFillRuleDTO> partialUpdateSysFillRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SysFillRuleDTO sysFillRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SysFillRule partially : {}, {}", id, sysFillRuleDTO);
        if (sysFillRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sysFillRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (sysFillRuleRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SysFillRuleDTO> result = sysFillRuleService.partialUpdate(sysFillRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sysFillRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sys-fill-rules} : get all the sysFillRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sysFillRules in body.
     */
    @GetMapping("")
    @Operation(tags = "获取填充规则分页列表", description = "获取填充规则的分页列表数据")
    @AutoLog(value = "获取填充规则分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<SysFillRuleDTO>> getAllSysFillRules(
        SysFillRuleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SysFillRules by criteria: {}", criteria);

        IPage<SysFillRuleDTO> page;
        page = sysFillRuleQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<SysFillRuleDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /sys-fill-rules/count} : count all the sysFillRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSysFillRules(SysFillRuleCriteria criteria) {
        log.debug("REST request to count SysFillRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(sysFillRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sys-fill-rules/:id} : get the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sysFillRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的填充规则", description = "获取指定主键的填充规则信息")
    @AutoLog(value = "获取指定主键的填充规则", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<SysFillRuleDTO> getSysFillRule(@PathVariable("id") Long id) {
        log.debug("REST request to get SysFillRule : {}", id);
        Optional<SysFillRuleDTO> sysFillRuleDTO = sysFillRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sysFillRuleDTO);
    }

    /**
     * {@code DELETE  /sys-fill-rules/:id} : delete the "id" sysFillRule.
     *
     * @param id the id of the sysFillRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的填充规则", description = "删除指定主键的填充规则信息")
    @AutoLog(value = "删除指定主键的填充规则", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSysFillRule(@PathVariable("id") Long id) {
        log.debug("REST request to delete SysFillRule : {}", id);

        sysFillRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /sys-fill-rules/export : export the sysFillRules.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "填充规则EXCEL导出", description = "导出全部填充规则为EXCEL文件")
    @AutoLog(value = "填充规则EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<SysFillRuleDTO> data = sysFillRuleService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("填充规则一览表", "填充规则"), SysFillRuleDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "填充规则_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /sys-fill-rules/import : import the sysFillRules from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the sysFillRuleDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "填充规则EXCEL导入", description = "根据填充规则EXCEL文件导入全部数据")
    @AutoLog(value = "填充规则EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SysFillRuleDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SysFillRuleDTO.class, params);
        list.forEach(sysFillRuleService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /sys-fill-rules} : delete all the "ids" SysFillRules.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个填充规则", description = "根据主键删除多个填充规则")
    @AutoLog(value = "删除多个填充规则", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSysFillRulesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SysFillRules : {}", ids);
        if (ids != null) {
            ids.forEach(sysFillRuleService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对填充规则进行统计", description = "条件和统计的配置通过填充规则的Criteria类来实现")
    @AutoLog(value = "根据条件对填充规则进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(SysFillRuleCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = sysFillRuleQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
