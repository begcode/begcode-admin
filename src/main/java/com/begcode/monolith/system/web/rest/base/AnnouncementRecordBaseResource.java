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
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.service.AnnouncementRecordQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordService;
import com.begcode.monolith.system.service.criteria.AnnouncementRecordCriteria;
import com.begcode.monolith.system.service.dto.AnnouncementRecordDTO;
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

 * 管理实体{@link com.begcode.monolith.system.domain.AnnouncementRecord}的REST Controller。
 */
public class AnnouncementRecordBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(AnnouncementRecordBaseResource.class);

    protected static final String ENTITY_NAME = "systemAnnouncementRecord";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final AnnouncementRecordService announcementRecordService;

    protected final AnnouncementRecordRepository announcementRecordRepository;

    protected final AnnouncementRecordQueryService announcementRecordQueryService;

    public AnnouncementRecordBaseResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRecordQueryService announcementRecordQueryService
    ) {
        this.announcementRecordService = announcementRecordService;
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRecordQueryService = announcementRecordQueryService;
    }

    /**
     * {@code POST  /announcement-records} : Create a new announcementRecord.
     *
     * @param announcementRecordDTO the announcementRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new announcementRecordDTO, or with status {@code 400 (Bad Request)} if the announcementRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建通告阅读记录", description = "创建并返回一个新的通告阅读记录")
    @AutoLog(value = "新建通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<AnnouncementRecordDTO> createAnnouncementRecord(@RequestBody AnnouncementRecordDTO announcementRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnnouncementRecord : {}", announcementRecordDTO);
        if (announcementRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new announcementRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        announcementRecordDTO = announcementRecordService.save(announcementRecordDTO);
        return ResponseEntity.created(new URI("/api/announcement-records/" + announcementRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString()))
            .body(announcementRecordDTO);
    }

    /**
     * {@code PUT  /announcement-records/:id} : Updates an existing announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to save.
     * @param announcementRecordDTO the announcementRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新通告阅读记录", description = "根据主键更新并返回一个更新后的通告阅读记录")
    @AutoLog(value = "更新通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AnnouncementRecordDTO> updateAnnouncementRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementRecordDTO announcementRecordDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update AnnouncementRecord : {}, {}", id, announcementRecordDTO);
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRecordRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            announcementRecordService.updateBatch(announcementRecordDTO, batchFields, batchIds);
            announcementRecordDTO = announcementRecordService.findOne(id).orElseThrow();
        } else {
            announcementRecordDTO = announcementRecordService.update(announcementRecordDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString()))
            .body(announcementRecordDTO);
    }

    /**
     * {@code PATCH  /announcement-records/:id} : Partial updates given fields of an existing announcementRecord, field will ignore if it is null
     *
     * @param id the id of the announcementRecordDTO to save.
     * @param announcementRecordDTO the announcementRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the announcementRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(
        tags = "部分更新通告阅读记录",
        description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的通告阅读记录"
    )
    @AutoLog(value = "部分更新通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AnnouncementRecordDTO> partialUpdateAnnouncementRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnouncementRecordDTO announcementRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnnouncementRecord partially : {}, {}", id, announcementRecordDTO);
        if (announcementRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRecordRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnouncementRecordDTO> result = announcementRecordService.partialUpdate(announcementRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /announcement-records} : get all the announcementRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of announcementRecords in body.
     */
    @GetMapping("")
    @Operation(tags = "获取通告阅读记录分页列表", description = "获取通告阅读记录的分页列表数据")
    @AutoLog(value = "获取通告阅读记录分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<AnnouncementRecordDTO>> getAllAnnouncementRecords(
        AnnouncementRecordCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AnnouncementRecords by criteria: {}", criteria);

        IPage<AnnouncementRecordDTO> page;
        page = announcementRecordQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<AnnouncementRecordDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /announcement-records/count} : count all the announcementRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAnnouncementRecords(AnnouncementRecordCriteria criteria) {
        log.debug("REST request to count AnnouncementRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(announcementRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /announcement-records/:id} : get the "id" announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的通告阅读记录", description = "获取指定主键的通告阅读记录信息")
    @AutoLog(value = "获取指定主键的通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<AnnouncementRecordDTO> getAnnouncementRecord(@PathVariable("id") Long id) {
        log.debug("REST request to get AnnouncementRecord : {}", id);
        Optional<AnnouncementRecordDTO> announcementRecordDTO = announcementRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementRecordDTO);
    }

    /**
     * {@code DELETE  /announcement-records/:id} : delete the "id" announcementRecord.
     *
     * @param id the id of the announcementRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的通告阅读记录", description = "删除指定主键的通告阅读记录信息")
    @AutoLog(value = "删除指定主键的通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAnnouncementRecord(@PathVariable("id") Long id) {
        log.debug("REST request to delete AnnouncementRecord : {}", id);

        announcementRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /announcement-records/export : export the announcementRecords.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "通告阅读记录EXCEL导出", description = "导出全部通告阅读记录为EXCEL文件")
    @AutoLog(value = "通告阅读记录EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<AnnouncementRecordDTO> data = announcementRecordService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(
            new ExportParams("通告阅读记录一览表", "通告阅读记录"),
            AnnouncementRecordDTO.class,
            data
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "通告阅读记录_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /announcement-records/import : import the announcementRecords from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the announcementRecordDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "通告阅读记录EXCEL导入", description = "根据通告阅读记录EXCEL文件导入全部数据")
    @AutoLog(value = "通告阅读记录EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AnnouncementRecordDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), AnnouncementRecordDTO.class, params);
        list.forEach(announcementRecordService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /announcement-records} : delete all the "ids" AnnouncementRecords.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个通告阅读记录", description = "根据主键删除多个通告阅读记录")
    @AutoLog(value = "删除多个通告阅读记录", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAnnouncementRecordsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete AnnouncementRecords : {}", ids);
        if (ids != null) {
            ids.forEach(announcementRecordService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对通告阅读记录进行统计", description = "条件和统计的配置通过通告阅读记录的Criteria类来实现")
    @AutoLog(value = "根据条件对通告阅读记录进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(AnnouncementRecordCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = announcementRecordQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
