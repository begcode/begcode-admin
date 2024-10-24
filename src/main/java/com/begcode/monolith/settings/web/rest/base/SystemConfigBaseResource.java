package com.begcode.monolith.settings.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.SystemConfig;
import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.SystemConfigQueryService;
import com.begcode.monolith.settings.service.SystemConfigService;
import com.begcode.monolith.settings.service.criteria.SystemConfigCriteria;
import com.begcode.monolith.settings.service.dto.SystemConfigDTO;
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

 * 管理实体{@link com.begcode.monolith.settings.domain.SystemConfig}的REST Controller。
 */
public class SystemConfigBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(SystemConfigBaseResource.class);

    protected static final String ENTITY_NAME = "settingsSystemConfig";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final SystemConfigService systemConfigService;

    protected final SystemConfigRepository systemConfigRepository;

    protected final SystemConfigQueryService systemConfigQueryService;

    public SystemConfigBaseResource(
        SystemConfigService systemConfigService,
        SystemConfigRepository systemConfigRepository,
        SystemConfigQueryService systemConfigQueryService
    ) {
        this.systemConfigService = systemConfigService;
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigQueryService = systemConfigQueryService;
    }

    /**
     * {@code POST  /system-configs} : Create a new systemConfig.
     *
     * @param systemConfigDTO the systemConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemConfigDTO, or with status {@code 400 (Bad Request)} if the systemConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建网站配置", description = "创建并返回一个新的网站配置")
    @AutoLog(value = "新建网站配置", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<SystemConfigDTO> createSystemConfig(@Valid @RequestBody SystemConfigDTO systemConfigDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemConfig : {}", systemConfigDTO);
        if (systemConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemConfigDTO = systemConfigService.save(systemConfigDTO);
        return ResponseEntity.created(new URI("/api/system-configs/" + systemConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString()))
            .body(systemConfigDTO);
    }

    /**
     * {@code PUT  /system-configs/:id} : Updates an existing systemConfig.
     *
     * @param id the id of the systemConfigDTO to save.
     * @param systemConfigDTO the systemConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新网站配置", description = "根据主键更新并返回一个更新后的网站配置")
    @AutoLog(value = "更新网站配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SystemConfigDTO> updateSystemConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemConfigDTO systemConfigDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update SystemConfig : {}, {}", id, systemConfigDTO);
        if (systemConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (systemConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            systemConfigService.updateBatch(systemConfigDTO, batchFields, batchIds);
            systemConfigDTO = systemConfigService.findOne(id).orElseThrow();
        } else {
            systemConfigDTO = systemConfigService.update(systemConfigDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString()))
            .body(systemConfigDTO);
    }

    /**
     * {@code PUT  /system-configs/sort-value/:id/:type} : Updates sort value systemConfig.
     *
     * @param id the id of the systemConfigDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigDTO couldn't be updated.
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
        @RequestBody(required = false) SystemConfigCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (systemConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<SystemConfig> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = systemConfigQueryService.createQueryWrapper(criteria);
        }
        Boolean result = systemConfigService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /system-configs/:id} : Partial updates given fields of an existing systemConfig, field will ignore if it is null
     *
     * @param id the id of the systemConfigDTO to save.
     * @param systemConfigDTO the systemConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新网站配置", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的网站配置")
    @AutoLog(value = "部分更新网站配置", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<SystemConfigDTO> partialUpdateSystemConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemConfigDTO systemConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemConfig partially : {}, {}", id, systemConfigDTO);
        if (systemConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (systemConfigRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemConfigDTO> result = systemConfigService.partialUpdate(systemConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-configs} : get all the systemConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemConfigs in body.
     */
    @GetMapping("")
    @Operation(tags = "获取网站配置分页列表", description = "获取网站配置的分页列表数据")
    @AutoLog(value = "获取网站配置分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<SystemConfigDTO>> getAllSystemConfigs(
        SystemConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SystemConfigs by criteria: {}", criteria);

        IPage<SystemConfigDTO> page;
        page = systemConfigQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<SystemConfigDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /system-configs/count} : count all the systemConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSystemConfigs(SystemConfigCriteria criteria) {
        log.debug("REST request to count SystemConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-configs/:id} : get the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的网站配置", description = "获取指定主键的网站配置信息")
    @AutoLog(value = "获取指定主键的网站配置", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<SystemConfigDTO> getSystemConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get SystemConfig : {}", id);
        Optional<SystemConfigDTO> systemConfigDTO = systemConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemConfigDTO);
    }

    /**
     * {@code DELETE  /system-configs/:id} : delete the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的网站配置", description = "删除指定主键的网站配置信息")
    @AutoLog(value = "删除指定主键的网站配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSystemConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete SystemConfig : {}", id);

        systemConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /system-configs/export : export the systemConfigs.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "网站配置EXCEL导出", description = "导出全部网站配置为EXCEL文件")
    @AutoLog(value = "网站配置EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<SystemConfigDTO> data = systemConfigService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("网站配置一览表", "网站配置"), SystemConfigDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "网站配置_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /system-configs/import : import the systemConfigs from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the systemConfigDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "网站配置EXCEL导入", description = "根据网站配置EXCEL文件导入全部数据")
    @AutoLog(value = "网站配置EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<SystemConfigDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), SystemConfigDTO.class, params);
        list.forEach(systemConfigService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /system-configs} : delete all the "ids" SystemConfigs.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个网站配置", description = "根据主键删除多个网站配置")
    @AutoLog(value = "删除多个网站配置", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteSystemConfigsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete SystemConfigs : {}", ids);
        if (ids != null) {
            ids.forEach(systemConfigService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对网站配置进行统计", description = "条件和统计的配置通过网站配置的Criteria类来实现")
    @AutoLog(value = "根据条件对网站配置进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(SystemConfigCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = systemConfigQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
