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
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.repository.FillRuleItemRepository;
import com.begcode.monolith.settings.service.FillRuleItemQueryService;
import com.begcode.monolith.settings.service.FillRuleItemService;
import com.begcode.monolith.settings.service.criteria.FillRuleItemCriteria;
import com.begcode.monolith.settings.service.dto.FillRuleItemDTO;
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

 * 管理实体{@link com.begcode.monolith.settings.domain.FillRuleItem}的REST Controller。
 */
public class FillRuleItemBaseResource {

    protected final Logger log = LoggerFactory.getLogger(FillRuleItemBaseResource.class);

    protected static final String ENTITY_NAME = "settingsFillRuleItem";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final FillRuleItemService fillRuleItemService;

    protected final FillRuleItemRepository fillRuleItemRepository;

    protected final FillRuleItemQueryService fillRuleItemQueryService;

    public FillRuleItemBaseResource(
        FillRuleItemService fillRuleItemService,
        FillRuleItemRepository fillRuleItemRepository,
        FillRuleItemQueryService fillRuleItemQueryService
    ) {
        this.fillRuleItemService = fillRuleItemService;
        this.fillRuleItemRepository = fillRuleItemRepository;
        this.fillRuleItemQueryService = fillRuleItemQueryService;
    }

    /**
     * {@code POST  /fill-rule-items} : Create a new fillRuleItem.
     *
     * @param fillRuleItemDTO the fillRuleItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fillRuleItemDTO, or with status {@code 400 (Bad Request)} if the fillRuleItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建填充规则条目", description = "创建并返回一个新的填充规则条目")
    @AutoLog(value = "新建填充规则条目", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<FillRuleItemDTO> createFillRuleItem(@RequestBody FillRuleItemDTO fillRuleItemDTO) throws URISyntaxException {
        log.debug("REST request to save FillRuleItem : {}", fillRuleItemDTO);
        if (fillRuleItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new fillRuleItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fillRuleItemDTO = fillRuleItemService.save(fillRuleItemDTO);
        return ResponseEntity.created(new URI("/api/fill-rule-items/" + fillRuleItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fillRuleItemDTO.getId().toString()))
            .body(fillRuleItemDTO);
    }

    /**
     * {@code PUT  /fill-rule-items/:id} : Updates an existing fillRuleItem.
     *
     * @param id the id of the fillRuleItemDTO to save.
     * @param fillRuleItemDTO the fillRuleItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fillRuleItemDTO,
     * or with status {@code 400 (Bad Request)} if the fillRuleItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fillRuleItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新填充规则条目", description = "根据主键更新并返回一个更新后的填充规则条目")
    @AutoLog(value = "更新填充规则条目", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FillRuleItemDTO> updateFillRuleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FillRuleItemDTO fillRuleItemDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update FillRuleItem : {}, {}", id, fillRuleItemDTO);
        if (fillRuleItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fillRuleItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (fillRuleItemRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            fillRuleItemService.updateBatch(fillRuleItemDTO, batchFields, batchIds);
            fillRuleItemDTO = fillRuleItemService.findOne(id).orElseThrow();
        } else {
            fillRuleItemDTO = fillRuleItemService.update(fillRuleItemDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fillRuleItemDTO.getId().toString()))
            .body(fillRuleItemDTO);
    }

    /**
     * {@code PUT  /fill-rule-items/relations/:operateType} : Updates relationships an existing fillRuleItem.
     *
     * @param operateType the operateType of the fillRuleItemDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fillRuleItemDTO,
     * or with status {@code 400 (Bad Request)} if the fillRuleItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fillRuleItemDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新填充规则条目关联关系", description = "根据主键更新填充规则条目关联关系")
    @AutoLog(value = "更新填充规则条目关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update FillRuleItem : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        fillRuleItemService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PUT  /fill-rule-items/sort-value/:id/:type} : Updates sort value fillRuleItem.
     *
     * @param id the id of the fillRuleItemDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fillRuleItemDTO,
     * or with status {@code 400 (Bad Request)} if the fillRuleItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fillRuleItemDTO couldn't be updated.
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
        @RequestBody(required = false) FillRuleItemCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (fillRuleItemRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<FillRuleItem> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = fillRuleItemQueryService.createQueryWrapper(criteria);
        }
        Boolean result = fillRuleItemService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /fill-rule-items/:id} : Partial updates given fields of an existing fillRuleItem, field will ignore if it is null
     *
     * @param id the id of the fillRuleItemDTO to save.
     * @param fillRuleItemDTO the fillRuleItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fillRuleItemDTO,
     * or with status {@code 400 (Bad Request)} if the fillRuleItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fillRuleItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fillRuleItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        tags = "部分更新填充规则条目",
        description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的填充规则条目"
    )
    @AutoLog(value = "部分更新填充规则条目", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<FillRuleItemDTO> partialUpdateFillRuleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FillRuleItemDTO fillRuleItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FillRuleItem partially : {}, {}", id, fillRuleItemDTO);
        if (fillRuleItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fillRuleItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (fillRuleItemRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FillRuleItemDTO> result = fillRuleItemService.partialUpdate(fillRuleItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fillRuleItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fill-rule-items} : get all the fillRuleItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fillRuleItems in body.
     */
    @GetMapping("")
    @Operation(tags = "获取填充规则条目分页列表", description = "获取填充规则条目的分页列表数据")
    @AutoLog(value = "获取填充规则条目分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<FillRuleItemDTO>> getAllFillRuleItems(
        FillRuleItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FillRuleItems by criteria: {}", criteria);

        IPage<FillRuleItemDTO> page;
        page = fillRuleItemQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<FillRuleItemDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /fill-rule-items/count} : count all the fillRuleItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFillRuleItems(FillRuleItemCriteria criteria) {
        log.debug("REST request to count FillRuleItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(fillRuleItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fill-rule-items/:id} : get the "id" fillRuleItem.
     *
     * @param id the id of the fillRuleItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fillRuleItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的填充规则条目", description = "获取指定主键的填充规则条目信息")
    @AutoLog(value = "获取指定主键的填充规则条目", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<FillRuleItemDTO> getFillRuleItem(@PathVariable("id") Long id) {
        log.debug("REST request to get FillRuleItem : {}", id);
        Optional<FillRuleItemDTO> fillRuleItemDTO = fillRuleItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fillRuleItemDTO);
    }

    /**
     * {@code DELETE  /fill-rule-items/:id} : delete the "id" fillRuleItem.
     *
     * @param id the id of the fillRuleItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的填充规则条目", description = "删除指定主键的填充规则条目信息")
    @AutoLog(value = "删除指定主键的填充规则条目", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFillRuleItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete FillRuleItem : {}", id);

        fillRuleItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /fill-rule-items/export : export the fillRuleItems.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "填充规则条目EXCEL导出", description = "导出全部填充规则条目为EXCEL文件")
    @AutoLog(value = "填充规则条目EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<FillRuleItemDTO> data = fillRuleItemService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("填充规则条目一览表", "填充规则条目"),
            FillRuleItemDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "填充规则条目_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /fill-rule-items/import : import the fillRuleItems from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the fillRuleItemDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "填充规则条目EXCEL导入", description = "根据填充规则条目EXCEL文件导入全部数据")
    @AutoLog(value = "填充规则条目EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<FillRuleItemDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), FillRuleItemDTO.class, params);
        list.forEach(fillRuleItemService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /fill-rule-items} : delete all the "ids" FillRuleItems.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个填充规则条目", description = "根据主键删除多个填充规则条目")
    @AutoLog(value = "删除多个填充规则条目", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteFillRuleItemsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete FillRuleItems : {}", ids);
        if (ids != null) {
            ids.forEach(fillRuleItemService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对填充规则条目进行统计", description = "条件和统计的配置通过填充规则条目的Criteria类来实现")
    @AutoLog(value = "根据条件对填充规则条目进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(FillRuleItemCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = fillRuleItemQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
