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
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.FormSaveDataQueryService;
import com.begcode.monolith.system.service.FormSaveDataService;
import com.begcode.monolith.system.service.criteria.FormSaveDataCriteria;
import com.begcode.monolith.system.service.dto.FormSaveDataDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.FormSaveData}的REST Controller。
 */
public class FormSaveDataBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(FormSaveDataBaseResource.class);

    protected static final String ENTITY_NAME = "settingsFormSaveData";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final FormSaveDataService formSaveDataService;

    protected final FormSaveDataRepository formSaveDataRepository;

    protected final FormSaveDataQueryService formSaveDataQueryService;

    public FormSaveDataBaseResource(
        FormSaveDataService formSaveDataService,
        FormSaveDataRepository formSaveDataRepository,
        FormSaveDataQueryService formSaveDataQueryService
    ) {
        this.formSaveDataService = formSaveDataService;
        this.formSaveDataRepository = formSaveDataRepository;
        this.formSaveDataQueryService = formSaveDataQueryService;
    }

    /**
     * {@code POST  /form-save-data} : Create a new formSaveData.
     *
     * @param formSaveDataDTO the formSaveDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formSaveDataDTO, or with status {@code 400 (Bad Request)} if the formSaveData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建表单数据", description = "创建并返回一个新的表单数据")
    @AutoLog(value = "新建表单数据", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<FormSaveDataDTO> createFormSaveData(@RequestBody FormSaveDataDTO formSaveDataDTO) throws URISyntaxException {
        log.debug("REST request to save FormSaveData : {}", formSaveDataDTO);
        if (formSaveDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new formSaveData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        formSaveDataDTO = formSaveDataService.save(formSaveDataDTO);
        return ResponseEntity.created(new URI("/api/form-save-data/" + formSaveDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, formSaveDataDTO.getId().toString()))
            .body(formSaveDataDTO);
    }

    /**
     * {@code PUT  /form-save-data/:id} : Updates an existing formSaveData.
     *
     * @param id the id of the formSaveDataDTO to save.
     * @param formSaveDataDTO the formSaveDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formSaveDataDTO,
     * or with status {@code 400 (Bad Request)} if the formSaveDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formSaveDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新表单数据", description = "根据主键更新并返回一个更新后的表单数据")
    @AutoLog(value = "更新表单数据", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FormSaveDataDTO> updateFormSaveData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormSaveDataDTO formSaveDataDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update FormSaveData : {}, {}", id, formSaveDataDTO);
        if (formSaveDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formSaveDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (formSaveDataRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            formSaveDataService.updateBatch(formSaveDataDTO, batchFields, batchIds);
            formSaveDataDTO = formSaveDataService.findOne(id).orElseThrow();
        } else {
            formSaveDataDTO = formSaveDataService.update(formSaveDataDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formSaveDataDTO.getId().toString()))
            .body(formSaveDataDTO);
    }

    /**
     * {@code PATCH  /form-save-data/:id} : Partial updates given fields of an existing formSaveData, field will ignore if it is null
     *
     * @param id the id of the formSaveDataDTO to save.
     * @param formSaveDataDTO the formSaveDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formSaveDataDTO,
     * or with status {@code 400 (Bad Request)} if the formSaveDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formSaveDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formSaveDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新表单数据", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的表单数据")
    @AutoLog(value = "部分更新表单数据", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FormSaveDataDTO> partialUpdateFormSaveData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormSaveDataDTO formSaveDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormSaveData partially : {}, {}", id, formSaveDataDTO);
        if (formSaveDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formSaveDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (formSaveDataRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormSaveDataDTO> result = formSaveDataService.partialUpdate(formSaveDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formSaveDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-save-data} : get all the formSaveData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formSaveData in body.
     */
    @GetMapping("")
    @Operation(tags = "获取表单数据分页列表", description = "获取表单数据的分页列表数据")
    @AutoLog(value = "获取表单数据分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<FormSaveDataDTO>> getAllFormSaveData(
        FormSaveDataCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FormSaveData by criteria: {}", criteria);

        IPage<FormSaveDataDTO> page;
        page = formSaveDataQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<FormSaveDataDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /form-save-data/count} : count all the formSaveData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFormSaveData(FormSaveDataCriteria criteria) {
        log.debug("REST request to count FormSaveData by criteria: {}", criteria);
        return ResponseEntity.ok().body(formSaveDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /form-save-data/:id} : get the "id" formSaveData.
     *
     * @param id the id of the formSaveDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formSaveDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的表单数据", description = "获取指定主键的表单数据信息")
    @AutoLog(value = "获取指定主键的表单数据", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<FormSaveDataDTO> getFormSaveData(@PathVariable("id") Long id) {
        log.debug("REST request to get FormSaveData : {}", id);
        Optional<FormSaveDataDTO> formSaveDataDTO = formSaveDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formSaveDataDTO);
    }

    /**
     * {@code DELETE  /form-save-data/:id} : delete the "id" formSaveData.
     *
     * @param id the id of the formSaveDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的表单数据", description = "删除指定主键的表单数据信息")
    @AutoLog(value = "删除指定主键的表单数据", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFormSaveData(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormSaveData : {}", id);

        formSaveDataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /form-save-data/export : export the formSaveData.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "表单数据EXCEL导出", description = "导出全部表单数据为EXCEL文件")
    @AutoLog(value = "表单数据EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<FormSaveDataDTO> data = formSaveDataService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("表单数据一览表", "表单数据"), FormSaveDataDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "表单数据_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /form-save-data/import : import the formSaveData from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the formSaveDataDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "表单数据EXCEL导入", description = "根据表单数据EXCEL文件导入全部数据")
    @AutoLog(value = "表单数据EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<FormSaveDataDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), FormSaveDataDTO.class, params);
        list.forEach(formSaveDataService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /form-save-data} : delete all the "ids" FormSaveData.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个表单数据", description = "根据主键删除多个表单数据")
    @AutoLog(value = "删除多个表单数据", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFormSaveDataByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete FormSaveData : {}", ids);
        if (ids != null) {
            ids.forEach(formSaveDataService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对表单数据进行统计", description = "条件和统计的配置通过表单数据的Criteria类来实现")
    @AutoLog(value = "根据条件对表单数据进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(FormSaveDataCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = formSaveDataQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
