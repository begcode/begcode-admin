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
import com.begcode.monolith.repository.BusinessTypeRepository;
import com.begcode.monolith.service.BusinessTypeQueryService;
import com.begcode.monolith.service.BusinessTypeService;
import com.begcode.monolith.service.criteria.BusinessTypeCriteria;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
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

 * 管理实体{@link com.begcode.monolith.domain.BusinessType}的REST Controller。
 */
public class BusinessTypeBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(BusinessTypeBaseResource.class);

    protected static final String ENTITY_NAME = "settingsBusinessType";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final BusinessTypeService businessTypeService;

    protected final BusinessTypeRepository businessTypeRepository;

    protected final BusinessTypeQueryService businessTypeQueryService;

    public BusinessTypeBaseResource(
        BusinessTypeService businessTypeService,
        BusinessTypeRepository businessTypeRepository,
        BusinessTypeQueryService businessTypeQueryService
    ) {
        this.businessTypeService = businessTypeService;
        this.businessTypeRepository = businessTypeRepository;
        this.businessTypeQueryService = businessTypeQueryService;
    }

    /**
     * {@code POST  /business-types} : Create a new businessType.
     *
     * @param businessTypeDTO the businessTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessTypeDTO, or with status {@code 400 (Bad Request)} if the businessType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建业务类型", description = "创建并返回一个新的业务类型")
    @AutoLog(value = "新建业务类型", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<BusinessTypeDTO> createBusinessType(@RequestBody BusinessTypeDTO businessTypeDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessType : {}", businessTypeDTO);
        if (businessTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        businessTypeDTO = businessTypeService.save(businessTypeDTO);
        return ResponseEntity.created(new URI("/api/business-types/" + businessTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(businessTypeDTO);
    }

    /**
     * {@code PUT  /business-types/:id} : Updates an existing businessType.
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新业务类型", description = "根据主键更新并返回一个更新后的业务类型")
    @AutoLog(value = "更新业务类型", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<BusinessTypeDTO> updateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusinessTypeDTO businessTypeDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update BusinessType : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (businessTypeRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            businessTypeService.updateBatch(businessTypeDTO, batchFields, batchIds);
            businessTypeDTO = businessTypeService.findOne(id).orElseThrow();
        } else {
            businessTypeDTO = businessTypeService.update(businessTypeDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString()))
            .body(businessTypeDTO);
    }

    /**
     * {@code PUT  /business-types/relations/:operateType} : Updates relationships an existing businessType.
     *
     * @param operateType the operateType of the businessTypeDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新业务类型关联关系", description = "根据主键更新业务类型关联关系")
    @AutoLog(value = "更新业务类型关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update BusinessType : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        businessTypeService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /business-types/:id} : Partial updates given fields of an existing businessType, field will ignore if it is null
     *
     * @param id the id of the businessTypeDTO to save.
     * @param businessTypeDTO the businessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the businessTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新业务类型", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的业务类型")
    @AutoLog(value = "部分更新业务类型", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<BusinessTypeDTO> partialUpdateBusinessType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BusinessTypeDTO businessTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessType partially : {}, {}", id, businessTypeDTO);
        if (businessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (businessTypeRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessTypeDTO> result = businessTypeService.partialUpdate(businessTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-types} : get all the businessTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessTypes in body.
     */
    @GetMapping("")
    @Operation(tags = "获取业务类型分页列表", description = "获取业务类型的分页列表数据")
    @AutoLog(value = "获取业务类型分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<BusinessTypeDTO>> getAllBusinessTypes(
        BusinessTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BusinessTypes by criteria: {}", criteria);

        IPage<BusinessTypeDTO> page;
        page = businessTypeQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<BusinessTypeDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /business-types/count} : count all the businessTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBusinessTypes(BusinessTypeCriteria criteria) {
        log.debug("REST request to count BusinessTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-types/:id} : get the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的业务类型", description = "获取指定主键的业务类型信息")
    @AutoLog(value = "获取指定主键的业务类型", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<BusinessTypeDTO> getBusinessType(@PathVariable("id") Long id) {
        log.debug("REST request to get BusinessType : {}", id);
        Optional<BusinessTypeDTO> businessTypeDTO = businessTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessTypeDTO);
    }

    /**
     * {@code DELETE  /business-types/:id} : delete the "id" businessType.
     *
     * @param id the id of the businessTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的业务类型", description = "删除指定主键的业务类型信息")
    @AutoLog(value = "删除指定主键的业务类型", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteBusinessType(@PathVariable("id") Long id) {
        log.debug("REST request to delete BusinessType : {}", id);

        businessTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /business-types/export : export the businessTypes.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "业务类型EXCEL导出", description = "导出全部业务类型为EXCEL文件")
    @AutoLog(value = "业务类型EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<BusinessTypeDTO> data = businessTypeService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("业务类型一览表", "业务类型"), BusinessTypeDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "业务类型_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /business-types/import : import the businessTypes from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the businessTypeDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "业务类型EXCEL导入", description = "根据业务类型EXCEL文件导入全部数据")
    @AutoLog(value = "业务类型EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<BusinessTypeDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), BusinessTypeDTO.class, params);
        list.forEach(businessTypeService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /business-types} : delete all the "ids" BusinessTypes.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个业务类型", description = "根据主键删除多个业务类型")
    @AutoLog(value = "删除多个业务类型", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteBusinessTypesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete BusinessTypes : {}", ids);
        if (ids != null) {
            ids.forEach(businessTypeService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对业务类型进行统计", description = "条件和统计的配置通过业务类型的Criteria类来实现")
    @AutoLog(value = "根据条件对业务类型进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(BusinessTypeCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = businessTypeQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
