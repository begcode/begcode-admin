package com.begcode.monolith.system.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.AnnoCategory;
import com.begcode.monolith.domain.enumeration.AnnoSendStatus;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.domain.enumeration.ReceiverType;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.AnnouncementQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordQueryService;
import com.begcode.monolith.system.service.AnnouncementRecordService;
import com.begcode.monolith.system.service.AnnouncementService;
import com.begcode.monolith.system.service.criteria.AnnouncementCriteria;
import com.begcode.monolith.system.service.criteria.AnnouncementRecordCriteria;
import com.begcode.monolith.system.service.dto.AnnouncementDTO;
import com.begcode.monolith.system.service.dto.AnnouncementRecordDTO;
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
import java.util.stream.Collectors;
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

 * 管理实体{@link com.begcode.monolith.system.domain.Announcement}的REST Controller。
 */
public class AnnouncementBaseResource {

    protected final Logger log = LoggerFactory.getLogger(AnnouncementBaseResource.class);

    protected static final String ENTITY_NAME = "systemAnnouncement";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final AnnouncementRecordService announcementRecordService;

    protected final AnnouncementRecordQueryService announcementRecordQueryService;

    protected final AnnouncementService announcementService;

    protected final AnnouncementRepository announcementRepository;

    protected final AnnouncementQueryService announcementQueryService;

    public AnnouncementBaseResource(
        AnnouncementRecordService announcementRecordService,
        AnnouncementRecordQueryService announcementRecordQueryService,
        AnnouncementService announcementService,
        AnnouncementRepository announcementRepository,
        AnnouncementQueryService announcementQueryService
    ) {
        this.announcementRecordService = announcementRecordService;
        this.announcementRecordQueryService = announcementRecordQueryService;
        this.announcementService = announcementService;
        this.announcementRepository = announcementRepository;
        this.announcementQueryService = announcementQueryService;
    }

    /**
     * {@code POST  /announcements} : Create a new announcement.
     *
     * @param announcementDTO the announcementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new announcementDTO, or with status {@code 400 (Bad Request)} if the announcement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建系统通告", description = "创建并返回一个新的系统通告")
    @AutoLog(value = "新建系统通告", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO)
        throws URISyntaxException {
        log.debug("REST request to save Announcement : {}", announcementDTO);
        if (announcementDTO.getId() != null) {
            throw new BadRequestAlertException("A new announcement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnouncementDTO result = announcementService.save(announcementDTO);
        return ResponseEntity
            .created(new URI("/api/announcements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcements/:id} : Updates an existing announcement.
     *
     * @param id the id of the announcementDTO to save.
     * @param announcementDTO the announcementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新系统通告", description = "根据主键更新并返回一个更新后的系统通告")
    @AutoLog(value = "更新系统通告", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnnouncementDTO announcementDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update Announcement : {}, {}", id, announcementDTO);
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnouncementDTO result = null;
        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            announcementService.updateBatch(announcementDTO, batchFields, batchIds);
            result = announcementService.findOne(id).orElseThrow();
        } else {
            result = announcementService.update(announcementDTO);
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /announcements/relations/:operateType} : Updates relationships an existing announcement.
     *
     * @param operateType the operateType of the announcementDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新系统通告关联关系", description = "根据主键更新系统通告关联关系")
    @AutoLog(value = "更新系统通告关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update Announcement : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        announcementService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PATCH  /announcements/:id} : Partial updates given fields of an existing announcement, field will ignore if it is null
     *
     * @param id the id of the announcementDTO to save.
     * @param announcementDTO the announcementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementDTO,
     * or with status {@code 400 (Bad Request)} if the announcementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the announcementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the announcementDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新系统通告", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的系统通告")
    @AutoLog(value = "部分更新系统通告", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AnnouncementDTO> partialUpdateAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnnouncementDTO announcementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Announcement partially : {}, {}", id, announcementDTO);
        if (announcementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, announcementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (announcementRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnouncementDTO> result = announcementService.partialUpdate(announcementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, announcementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /announcements} : get all the announcements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of announcements in body.
     */
    @GetMapping("")
    @Operation(tags = "获取系统通告分页列表", description = "获取系统通告的分页列表数据")
    @AutoLog(value = "获取系统通告分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<AnnouncementDTO>> getAllAnnouncements(
        AnnouncementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Announcements by criteria: {}", criteria);

        IPage<AnnouncementDTO> page;
        page = announcementQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<AnnouncementDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /announcements/count} : count all the announcements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAnnouncements(AnnouncementCriteria criteria) {
        log.debug("REST request to count Announcements by criteria: {}", criteria);
        return ResponseEntity.ok().body(announcementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /announcements/:id} : get the "id" announcement.
     *
     * @param id the id of the announcementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的系统通告", description = "获取指定主键的系统通告信息")
    @AutoLog(value = "获取指定主键的系统通告", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable("id") Long id) {
        log.debug("REST request to get Announcement : {}", id);
        Optional<AnnouncementDTO> announcementDTO = announcementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementDTO);
    }

    /**
     * {@code DELETE  /announcements/:id} : delete the "id" announcement.
     *
     * @param id the id of the announcementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的系统通告", description = "删除指定主键的系统通告信息")
    @AutoLog(value = "删除指定主键的系统通告", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable("id") Long id) {
        log.debug("REST request to delete Announcement : {}", id);

        announcementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /announcements/export : export the announcements.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "系统通告EXCEL导出", description = "导出全部系统通告为EXCEL文件")
    @AutoLog(value = "系统通告EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<AnnouncementDTO> data = announcementService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("系统通告一览表", "系统通告"), AnnouncementDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "系统通告_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /announcements/import : import the announcements from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the announcementDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "系统通告EXCEL导入", description = "根据系统通告EXCEL文件导入全部数据")
    @AutoLog(value = "系统通告EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AnnouncementDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), AnnouncementDTO.class, params);
        list.forEach(announcementService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /announcements} : delete all the "ids" Announcements.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个系统通告", description = "根据主键删除多个系统通告")
    @AutoLog(value = "删除多个系统通告", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAnnouncementsByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Announcements : {}", ids);
        if (ids != null) {
            ids.forEach(announcementService::delete);
        }
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对系统通告进行统计", description = "条件和统计的配置通过系统通告的Criteria类来实现")
    @AutoLog(value = "根据条件对系统通告进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(AnnouncementCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = announcementQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    /**
     * {@code GET  /current-user/unread/{category}} : get the  announcements of unread for current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/current-user/unread/{category}")
    public ResponseEntity<PageRecord<AnnouncementDTO>> getUnReadAnnouncements(@PathVariable AnnoCategory category, Pageable page) {
        AnnouncementCriteria acriteria = new AnnouncementCriteria();
        acriteria.sendStatus().setEquals(AnnoSendStatus.RELEASED);
        acriteria.receiverType().setEquals(ReceiverType.ALL);
        announcementRecordService.updateRecord(announcementQueryService.findByCriteria(acriteria));
        AnnouncementRecordCriteria criteria = new AnnouncementRecordCriteria();
        criteria.userId().setEquals(SecurityUtils.getCurrentUserId().orElseThrow());
        criteria.hasRead().setEquals(false);
        IPage<AnnouncementDTO> byCriteria;
        List<Long> ids = announcementRecordQueryService
            .findByCriteria(criteria)
            .stream()
            .map(AnnouncementRecordDTO::getAnntId)
            .collect(Collectors.toList());
        if (!ids.isEmpty()) {
            AnnouncementCriteria announcementCriteria = new AnnouncementCriteria();
            announcementCriteria.id().setIn(ids);
            announcementCriteria.category().setEquals(category);
            byCriteria = announcementQueryService.findByCriteria(announcementCriteria, PageableUtils.toPage(page));
        } else {
            byCriteria = new Page<>(0, page.getPageSize(), 0);
        }
        PageRecord<AnnouncementDTO> result = new PageRecord<>();
        result.records(byCriteria.getRecords()).size(byCriteria.getSize()).total(byCriteria.getTotal()).page(byCriteria.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), byCriteria);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code PUT  /:id/release} : release the "id" announcement.
     *
     * @param id the id of the announcementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the announcementDTO, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/{id}/release")
    public ResponseEntity<AnnouncementDTO> releaseAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get Announcement : {}", id);
        announcementService.release(id);
        Optional<AnnouncementDTO> announcementDTO = announcementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(announcementDTO);
    }

    /**
     * {@code PUT  /:id/read} : Updates an existing announcementRecord to read by current user.
     *
     * @param id the id of the announcementId to read.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated announcementRecordDTO,
     * or with status {@code 400 (Bad Request)} if the announcementRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the announcementRecordDTO couldn't be updated.
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> updateAnnouncementRecordRead(@PathVariable(value = "id", required = false) final Long id) {
        log.debug("REST request to update AnnouncementRecord : {}", id);
        announcementRecordService.setRead(id);
        return ResponseEntity.ok().build();
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
