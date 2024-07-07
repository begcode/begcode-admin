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
import com.begcode.monolith.system.repository.FormConfigRepository;
import com.begcode.monolith.system.service.FormConfigQueryService;
import com.begcode.monolith.system.service.FormConfigService;
import com.begcode.monolith.system.service.criteria.FormConfigCriteria;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.FormConfig}的REST Controller。
 */
public class FormConfigBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(FormConfigBaseResource.class);

    protected static final String ENTITY_NAME = "settingsFormConfig";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final FormConfigService formConfigService;

    protected final FormConfigRepository formConfigRepository;

    protected final FormConfigQueryService formConfigQueryService;

    public FormConfigBaseResource(
        FormConfigService formConfigService,
        FormConfigRepository formConfigRepository,
        FormConfigQueryService formConfigQueryService
    ) {
        this.formConfigService = formConfigService;
        this.formConfigRepository = formConfigRepository;
        this.formConfigQueryService = formConfigQueryService;
    }

    /**
     * {@code POST  /form-configs} : Create a new formConfig.
     *
     * @param formConfigDTO the formConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formConfigDTO, or with status {@code 400 (Bad Request)} if the formConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建表单配置", description = "创建并返回一个新的表单配置")
    @AutoLog(value = "新建表单配置", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<FormConfigDTO> createFormConfig(@Valid @RequestBody FormConfigDTO formConfigDTO) throws URISyntaxException {
        log.debug("REST request to save FormConfig : {}", formConfigDTO);
        if (formConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new formConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        formConfigDTO = formConfigService.save(formConfigDTO);
        return ResponseEntity.created(new URI("/api/form-configs/" + formConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, formConfigDTO.getId().toString()))
            .body(formConfigDTO);
    }

    /**
     * {@code PUT  /form-configs/:id} : Updates an existing formConfig.
     *
     * @param id the id of the formConfigDTO to save.
     * @param formConfigDTO the formConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formConfigDTO,
     * or with status {@code 400 (Bad Request)} if the formConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新表单配置", description = "根据主键更新并返回一个更新后的表单配置")
    @AutoLog(value = "更新表单配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FormConfigDTO> updateFormConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormConfigDTO formConfigDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update FormConfig : {}, {}", id, formConfigDTO);
        if (formConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (formConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            formConfigService.updateBatch(formConfigDTO, batchFields, batchIds);
            formConfigDTO = formConfigService.findOne(id).orElseThrow();
        } else {
            formConfigDTO = formConfigService.update(formConfigDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formConfigDTO.getId().toString()))
            .body(formConfigDTO);
    }

    /**
     * {@code PATCH  /form-configs/:id} : Partial updates given fields of an existing formConfig, field will ignore if it is null
     *
     * @param id the id of the formConfigDTO to save.
     * @param formConfigDTO the formConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formConfigDTO,
     * or with status {@code 400 (Bad Request)} if the formConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新表单配置", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的表单配置")
    @AutoLog(value = "部分更新表单配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FormConfigDTO> partialUpdateFormConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormConfigDTO formConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormConfig partially : {}, {}", id, formConfigDTO);
        if (formConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (formConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormConfigDTO> result = formConfigService.partialUpdate(formConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-configs} : get all the formConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formConfigs in body.
     */
    @GetMapping("")
    @Operation(tags = "获取表单配置分页列表", description = "获取表单配置的分页列表数据")
    @AutoLog(value = "获取表单配置分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<FormConfigDTO>> getAllFormConfigs(
        FormConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FormConfigs by criteria: {}", criteria);

        IPage<FormConfigDTO> page;
        page = formConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<FormConfigDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /form-configs/count} : count all the formConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFormConfigs(FormConfigCriteria criteria) {
        log.debug("REST request to count FormConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(formConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /form-configs/:id} : get the "id" formConfig.
     *
     * @param id the id of the formConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的表单配置", description = "获取指定主键的表单配置信息")
    @AutoLog(value = "获取指定主键的表单配置", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<FormConfigDTO> getFormConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get FormConfig : {}", id);
        Optional<FormConfigDTO> formConfigDTO = formConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formConfigDTO);
    }

    /**
     * {@code DELETE  /form-configs/:id} : delete the "id" formConfig.
     *
     * @param id the id of the formConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的表单配置", description = "删除指定主键的表单配置信息")
    @AutoLog(value = "删除指定主键的表单配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFormConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormConfig : {}", id);

        formConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /form-configs/export : export the formConfigs.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "表单配置EXCEL导出", description = "导出全部表单配置为EXCEL文件")
    @AutoLog(value = "表单配置EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<FormConfigDTO> data = formConfigService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("表单配置一览表", "表单配置"), FormConfigDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "表单配置_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /form-configs/import : import the formConfigs from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the formConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "表单配置EXCEL导入", description = "根据表单配置EXCEL文件导入全部数据")
    @AutoLog(value = "表单配置EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<FormConfigDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), FormConfigDTO.class, params);
        list.forEach(formConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /form-configs} : delete all the "ids" FormConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个表单配置", description = "根据主键删除多个表单配置")
    @AutoLog(value = "删除多个表单配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFormConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete FormConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(formConfigService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对表单配置进行统计", description = "条件和统计的配置通过表单配置的Criteria类来实现")
    @AutoLog(value = "根据条件对表单配置进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(FormConfigCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = formConfigQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
