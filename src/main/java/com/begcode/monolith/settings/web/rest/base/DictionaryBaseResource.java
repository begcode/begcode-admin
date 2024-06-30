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
import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.DictionaryQueryService;
import com.begcode.monolith.settings.service.DictionaryService;
import com.begcode.monolith.settings.service.criteria.DictionaryCriteria;
import com.begcode.monolith.settings.service.dto.DictionaryDTO;
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

 * 管理实体{@link com.begcode.monolith.settings.domain.Dictionary}的REST Controller。
 */
public class DictionaryBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(DictionaryBaseResource.class);

    protected static final String ENTITY_NAME = "settingsDictionary";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final DictionaryService dictionaryService;

    protected final DictionaryRepository dictionaryRepository;

    protected final DictionaryQueryService dictionaryQueryService;

    public DictionaryBaseResource(
        DictionaryService dictionaryService,
        DictionaryRepository dictionaryRepository,
        DictionaryQueryService dictionaryQueryService
    ) {
        this.dictionaryService = dictionaryService;
        this.dictionaryRepository = dictionaryRepository;
        this.dictionaryQueryService = dictionaryQueryService;
    }

    /**
     * {@code POST  /dictionaries} : Create a new dictionary.
     *
     * @param dictionaryDTO the dictionaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dictionaryDTO, or with status {@code 400 (Bad Request)} if the dictionary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建数据字典", description = "创建并返回一个新的数据字典")
    @AutoLog(value = "新建数据字典", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<DictionaryDTO> createDictionary(@Valid @RequestBody DictionaryDTO dictionaryDTO) throws URISyntaxException {
        log.debug("REST request to save Dictionary : {}", dictionaryDTO);
        if (dictionaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new dictionary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dictionaryDTO = dictionaryService.save(dictionaryDTO);
        return ResponseEntity.created(new URI("/api/dictionaries/" + dictionaryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dictionaryDTO.getId().toString()))
            .body(dictionaryDTO);
    }

    /**
     * {@code PUT  /dictionaries/:id} : Updates an existing dictionary.
     *
     * @param id the id of the dictionaryDTO to save.
     * @param dictionaryDTO the dictionaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新数据字典", description = "根据主键更新并返回一个更新后的数据字典")
    @AutoLog(value = "更新数据字典", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<DictionaryDTO> updateDictionary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DictionaryDTO dictionaryDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update Dictionary : {}, {}", id, dictionaryDTO);
        if (dictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dictionaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (dictionaryRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            dictionaryService.updateBatch(dictionaryDTO, batchFields, batchIds);
            dictionaryDTO = dictionaryService.findOne(id).orElseThrow();
        } else {
            dictionaryDTO = dictionaryService.update(dictionaryDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dictionaryDTO.getId().toString()))
            .body(dictionaryDTO);
    }

    /**
     * {@code PUT  /dictionaries/relations/:operateType} : Updates relationships an existing dictionary.
     *
     * @param operateType the operateType of the dictionaryDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dictionaryDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新数据字典关联关系", description = "根据主键更新数据字典关联关系")
    @AutoLog(value = "更新数据字典关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update Dictionary : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        dictionaryService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PUT  /dictionaries/sort-value/:id/:type} : Updates sort value dictionary.
     *
     * @param id the id of the dictionaryDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dictionaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dictionaryDTO couldn't be updated.
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
        @RequestBody(required = false) DictionaryCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (dictionaryRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<Dictionary> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = dictionaryQueryService.createQueryWrapper(criteria);
        }
        Boolean result = dictionaryService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /dictionaries/:id} : Partial updates given fields of an existing dictionary, field will ignore if it is null
     *
     * @param id the id of the dictionaryDTO to save.
     * @param dictionaryDTO the dictionaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dictionaryDTO,
     * or with status {@code 400 (Bad Request)} if the dictionaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dictionaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dictionaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新数据字典", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的数据字典")
    @AutoLog(value = "部分更新数据字典", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<DictionaryDTO> partialUpdateDictionary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DictionaryDTO dictionaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dictionary partially : {}, {}", id, dictionaryDTO);
        if (dictionaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dictionaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (dictionaryRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DictionaryDTO> result = dictionaryService.partialUpdate(dictionaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dictionaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dictionaries} : get all the dictionaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dictionaries in body.
     */
    @GetMapping("")
    @Operation(tags = "获取数据字典分页列表", description = "获取数据字典的分页列表数据")
    @AutoLog(value = "获取数据字典分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<DictionaryDTO>> getAllDictionaries(
        DictionaryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Dictionaries by criteria: {}", criteria);

        IPage<DictionaryDTO> page;
        page = dictionaryQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<DictionaryDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /dictionaries/count} : count all the dictionaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDictionaries(DictionaryCriteria criteria) {
        log.debug("REST request to count Dictionaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(dictionaryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dictionaries/:id} : get the "id" dictionary.
     *
     * @param id the id of the dictionaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dictionaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的数据字典", description = "获取指定主键的数据字典信息")
    @AutoLog(value = "获取指定主键的数据字典", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<DictionaryDTO> getDictionary(@PathVariable("id") Long id) {
        log.debug("REST request to get Dictionary : {}", id);
        Optional<DictionaryDTO> dictionaryDTO = dictionaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dictionaryDTO);
    }

    /**
     * {@code DELETE  /dictionaries/:id} : delete the "id" dictionary.
     *
     * @param id the id of the dictionaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的数据字典", description = "删除指定主键的数据字典信息")
    @AutoLog(value = "删除指定主键的数据字典", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteDictionary(@PathVariable("id") Long id) {
        log.debug("REST request to delete Dictionary : {}", id);

        dictionaryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /dictionaries/export : export the dictionaries.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "数据字典EXCEL导出", description = "导出全部数据字典为EXCEL文件")
    @AutoLog(value = "数据字典EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<DictionaryDTO> data = dictionaryService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("数据字典一览表", "数据字典"), DictionaryDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "数据字典_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /dictionaries/import : import the dictionaries from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the dictionaryDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "数据字典EXCEL导入", description = "根据数据字典EXCEL文件导入全部数据")
    @AutoLog(value = "数据字典EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<DictionaryDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), DictionaryDTO.class, params);
        list.forEach(dictionaryService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /dictionaries} : delete all the "ids" Dictionaries.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个数据字典", description = "根据主键删除多个数据字典")
    @AutoLog(value = "删除多个数据字典", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteDictionariesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Dictionaries : {}", ids);
        if (ids != null) {
            ids.forEach(dictionaryService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对数据字典进行统计", description = "条件和统计的配置通过数据字典的Criteria类来实现")
    @AutoLog(value = "根据条件对数据字典进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(DictionaryCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = dictionaryQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
