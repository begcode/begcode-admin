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
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.CommonFieldDataRepository;
import com.begcode.monolith.settings.service.CommonFieldDataQueryService;
import com.begcode.monolith.settings.service.CommonFieldDataService;
import com.begcode.monolith.settings.service.criteria.CommonFieldDataCriteria;
import com.begcode.monolith.settings.service.dto.CommonFieldDataDTO;
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

 * 管理实体{@link com.begcode.monolith.settings.domain.CommonFieldData}的REST Controller。
 */
public class CommonFieldDataBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(CommonFieldDataBaseResource.class);

    protected static final String ENTITY_NAME = "settingsCommonFieldData";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final CommonFieldDataService commonFieldDataService;

    protected final CommonFieldDataRepository commonFieldDataRepository;

    protected final CommonFieldDataQueryService commonFieldDataQueryService;

    public CommonFieldDataBaseResource(
        CommonFieldDataService commonFieldDataService,
        CommonFieldDataRepository commonFieldDataRepository,
        CommonFieldDataQueryService commonFieldDataQueryService
    ) {
        this.commonFieldDataService = commonFieldDataService;
        this.commonFieldDataRepository = commonFieldDataRepository;
        this.commonFieldDataQueryService = commonFieldDataQueryService;
    }

    /**
     * {@code POST  /common-field-data} : Create a new commonFieldData.
     *
     * @param commonFieldDataDTO the commonFieldDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonFieldDataDTO, or with status {@code 400 (Bad Request)} if the commonFieldData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建通用字段数据", description = "创建并返回一个新的通用字段数据")
    @AutoLog(value = "新建通用字段数据", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<CommonFieldDataDTO> createCommonFieldData(@RequestBody CommonFieldDataDTO commonFieldDataDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommonFieldData : {}", commonFieldDataDTO);
        if (commonFieldDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new commonFieldData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commonFieldDataDTO = commonFieldDataService.save(commonFieldDataDTO);
        return ResponseEntity.created(new URI("/api/common-field-data/" + commonFieldDataDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commonFieldDataDTO.getId().toString()))
            .body(commonFieldDataDTO);
    }

    /**
     * {@code PUT  /common-field-data/:id} : Updates an existing commonFieldData.
     *
     * @param id the id of the commonFieldDataDTO to save.
     * @param commonFieldDataDTO the commonFieldDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonFieldDataDTO,
     * or with status {@code 400 (Bad Request)} if the commonFieldDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonFieldDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新通用字段数据", description = "根据主键更新并返回一个更新后的通用字段数据")
    @AutoLog(value = "更新通用字段数据", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<CommonFieldDataDTO> updateCommonFieldData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommonFieldDataDTO commonFieldDataDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update CommonFieldData : {}, {}", id, commonFieldDataDTO);
        if (commonFieldDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonFieldDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (commonFieldDataRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            commonFieldDataService.updateBatch(commonFieldDataDTO, batchFields, batchIds);
            commonFieldDataDTO = commonFieldDataService.findOne(id).orElseThrow();
        } else {
            commonFieldDataDTO = commonFieldDataService.update(commonFieldDataDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonFieldDataDTO.getId().toString()))
            .body(commonFieldDataDTO);
    }

    /**
     * {@code PUT  /common-field-data/relations/:operateType} : Updates relationships an existing commonFieldData.
     *
     * @param operateType the operateType of the commonFieldDataDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonFieldDataDTO,
     * or with status {@code 400 (Bad Request)} if the commonFieldDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonFieldDataDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新通用字段数据关联关系", description = "根据主键更新通用字段数据关联关系")
    @AutoLog(value = "更新通用字段数据关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update CommonFieldData : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        commonFieldDataService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PUT  /common-field-data/sort-value/:id/:type} : Updates sort value commonFieldData.
     *
     * @param id the id of the commonFieldDataDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonFieldDataDTO,
     * or with status {@code 400 (Bad Request)} if the commonFieldDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonFieldDataDTO couldn't be updated.
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
        @RequestBody(required = false) CommonFieldDataCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (commonFieldDataRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<CommonFieldData> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = commonFieldDataQueryService.createQueryWrapper(criteria);
        }
        Boolean result = commonFieldDataService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /common-field-data/:id} : Partial updates given fields of an existing commonFieldData, field will ignore if it is null
     *
     * @param id the id of the commonFieldDataDTO to save.
     * @param commonFieldDataDTO the commonFieldDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonFieldDataDTO,
     * or with status {@code 400 (Bad Request)} if the commonFieldDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commonFieldDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commonFieldDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        tags = "部分更新通用字段数据",
        description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的通用字段数据"
    )
    @AutoLog(value = "部分更新通用字段数据", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<CommonFieldDataDTO> partialUpdateCommonFieldData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommonFieldDataDTO commonFieldDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommonFieldData partially : {}, {}", id, commonFieldDataDTO);
        if (commonFieldDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonFieldDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (commonFieldDataRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommonFieldDataDTO> result = commonFieldDataService.partialUpdate(commonFieldDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonFieldDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /common-field-data} : get all the commonFieldData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonFieldData in body.
     */
    @GetMapping("")
    @Operation(tags = "获取通用字段数据分页列表", description = "获取通用字段数据的分页列表数据")
    @AutoLog(value = "获取通用字段数据分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<CommonFieldDataDTO>> getAllCommonFieldData(
        CommonFieldDataCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CommonFieldData by criteria: {}", criteria);

        IPage<CommonFieldDataDTO> page;
        page = commonFieldDataQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<CommonFieldDataDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /common-field-data/count} : count all the commonFieldData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCommonFieldData(CommonFieldDataCriteria criteria) {
        log.debug("REST request to count CommonFieldData by criteria: {}", criteria);
        return ResponseEntity.ok().body(commonFieldDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /common-field-data/:id} : get the "id" commonFieldData.
     *
     * @param id the id of the commonFieldDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonFieldDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的通用字段数据", description = "获取指定主键的通用字段数据信息")
    @AutoLog(value = "获取指定主键的通用字段数据", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<CommonFieldDataDTO> getCommonFieldData(@PathVariable("id") Long id) {
        log.debug("REST request to get CommonFieldData : {}", id);
        Optional<CommonFieldDataDTO> commonFieldDataDTO = commonFieldDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonFieldDataDTO);
    }

    /**
     * {@code DELETE  /common-field-data/:id} : delete the "id" commonFieldData.
     *
     * @param id the id of the commonFieldDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的通用字段数据", description = "删除指定主键的通用字段数据信息")
    @AutoLog(value = "删除指定主键的通用字段数据", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteCommonFieldData(@PathVariable("id") Long id) {
        log.debug("REST request to delete CommonFieldData : {}", id);

        commonFieldDataService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /common-field-data/export : export the commonFieldData.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "通用字段数据EXCEL导出", description = "导出全部通用字段数据为EXCEL文件")
    @AutoLog(value = "通用字段数据EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<CommonFieldDataDTO> data = commonFieldDataService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("通用字段数据一览表", "通用字段数据"),
            CommonFieldDataDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "通用字段数据_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /common-field-data/import : import the commonFieldData from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the commonFieldDataDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "通用字段数据EXCEL导入", description = "根据通用字段数据EXCEL文件导入全部数据")
    @AutoLog(value = "通用字段数据EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<CommonFieldDataDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), CommonFieldDataDTO.class, params);
        list.forEach(commonFieldDataService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /common-field-data} : delete all the "ids" CommonFieldData.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个通用字段数据", description = "根据主键删除多个通用字段数据")
    @AutoLog(value = "删除多个通用字段数据", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteCommonFieldDataByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete CommonFieldData : {}", ids);
        if (ids != null) {
            ids.forEach(commonFieldDataService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对通用字段数据进行统计", description = "条件和统计的配置通过通用字段数据的Criteria类来实现")
    @AutoLog(value = "根据条件对通用字段数据进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(CommonFieldDataCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = commonFieldDataQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
