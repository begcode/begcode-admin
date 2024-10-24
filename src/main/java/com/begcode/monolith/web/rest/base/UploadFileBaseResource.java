package com.begcode.monolith.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.repository.UploadFileRepository;
import com.begcode.monolith.service.UploadFileQueryService;
import com.begcode.monolith.service.UploadFileService;
import com.begcode.monolith.service.criteria.UploadFileCriteria;
import com.begcode.monolith.service.dto.UploadFileDTO;
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
import org.apache.commons.lang3.ArrayUtils;
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

 * 管理实体{@link com.begcode.monolith.domain.UploadFile}的REST Controller。
 */
public class UploadFileBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(UploadFileBaseResource.class);

    protected static final String ENTITY_NAME = "filesUploadFile";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final UploadFileService uploadFileService;

    protected final UploadFileRepository uploadFileRepository;

    protected final UploadFileQueryService uploadFileQueryService;

    public UploadFileBaseResource(
        UploadFileService uploadFileService,
        UploadFileRepository uploadFileRepository,
        UploadFileQueryService uploadFileQueryService
    ) {
        this.uploadFileService = uploadFileService;
        this.uploadFileRepository = uploadFileRepository;
        this.uploadFileQueryService = uploadFileQueryService;
    }

    /**
     * {@code PUT  /upload-files/:id} : Updates an existing uploadFile.
     *
     * @param id the id of the uploadFileDTO to save.
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新上传文件", description = "根据主键更新并返回一个更新后的上传文件")
    @AutoLog(value = "更新上传文件", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadFileDTO> updateUploadFile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UploadFileDTO uploadFileDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update UploadFile : {}, {}", id, uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadFileRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            uploadFileService.updateBatch(uploadFileDTO, batchFields, batchIds);
            uploadFileDTO = uploadFileService.findOne(id).orElseThrow();
        } else {
            uploadFileDTO = uploadFileService.update(uploadFileDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString()))
            .body(uploadFileDTO);
    }

    /**
     * {@code PATCH  /upload-files/:id} : Partial updates given fields of an existing uploadFile, field will ignore if it is null
     *
     * @param id the id of the uploadFileDTO to save.
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新上传文件", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的上传文件")
    @AutoLog(value = "部分更新上传文件", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadFileDTO> partialUpdateUploadFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UploadFileDTO uploadFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UploadFile partially : {}, {}", id, uploadFileDTO);
        if (uploadFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadFileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadFileRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadFileDTO> result = uploadFileService.partialUpdate(uploadFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString())
        );
    }

    /**
     * {@code PATCH  /upload-files/copy/:id} : Partial updates given fields of an existing uploadFile, field will ignore if it is null
     *
     * @param id the id of the uploadFileDTO to save.
     * @param uploadFileDTO the uploadFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/copy/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新上传文件", description = "根据主键及实体信息复制新的实体，值为null的属性将忽略，并返回一个复制后的上传文件")
    @AutoLog(value = "复制上传文件", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadFileDTO> copyUploadFile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UploadFileDTO uploadFileDTO
    ) throws URISyntaxException {
        log.debug("REST request to copy UploadFile partially : {}, {}", id, uploadFileDTO);

        if (uploadFileRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadFileDTO> result = uploadFileService.copy(uploadFileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadFileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upload-files} : get all the uploadFiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadFiles in body.
     */
    @GetMapping("")
    @Operation(tags = "获取上传文件分页列表", description = "获取上传文件的分页列表数据")
    @AutoLog(value = "获取上传文件分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<UploadFileDTO>> getAllUploadFiles(
        UploadFileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UploadFiles by criteria: {}", criteria);

        IPage<UploadFileDTO> page;
        page = uploadFileQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<UploadFileDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /upload-files/count} : count all the uploadFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUploadFiles(UploadFileCriteria criteria) {
        log.debug("REST request to count UploadFiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(uploadFileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upload-files/:id} : get the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的上传文件", description = "获取指定主键的上传文件信息")
    @AutoLog(value = "获取指定主键的上传文件", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<UploadFileDTO> getUploadFile(@PathVariable("id") Long id) {
        log.debug("REST request to get UploadFile : {}", id);
        Optional<UploadFileDTO> uploadFileDTO = uploadFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadFileDTO);
    }

    /**
     * {@code DELETE  /upload-files/:id} : delete the "id" uploadFile.
     *
     * @param id the id of the uploadFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的上传文件", description = "删除指定主键的上传文件信息")
    @AutoLog(value = "删除指定主键的上传文件", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUploadFile(@PathVariable("id") Long id) {
        log.debug("REST request to delete UploadFile : {}", id);

        uploadFileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /upload-files/export : export the uploadFiles.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "上传文件EXCEL导出", description = "导出全部上传文件为EXCEL文件")
    @AutoLog(value = "上传文件EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<UploadFileDTO> data = uploadFileService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("上传文件一览表", "上传文件"), UploadFileDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "上传文件_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /upload-files/import : import the uploadFiles from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadFileDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "上传文件EXCEL导入", description = "根据上传文件EXCEL文件导入全部数据")
    @AutoLog(value = "上传文件EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<UploadFileDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), UploadFileDTO.class, params);
        list.forEach(uploadFileService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /upload-files} : delete all the "ids" UploadFiles.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个上传文件", description = "根据主键删除多个上传文件")
    @AutoLog(value = "删除多个上传文件", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUploadFilesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UploadFiles : {}", ids);
        if (ids != null) {
            ids.forEach(uploadFileService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对上传文件进行统计", description = "条件和统计的配置通过上传文件的Criteria类来实现")
    @AutoLog(value = "根据条件对上传文件进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(UploadFileCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = uploadFileQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    /**
     * {@code POST  /} : Create a new uploadFile.
     *
     * @param uploadFileDTO the UploadFileDTO to create.
     * @param file the uploadFile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadFileDTO, or with status {@code 400 (Bad Request)} if the uploadFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UploadFileDTO> createUploadFile(
        @RequestPart(required = false) UploadFileDTO uploadFileDTO,
        @RequestPart(name = "file", required = false) MultipartFile file,
        @RequestPart(name = "files", required = false) MultipartFile[] files
    ) throws Exception {
        log.debug("REST request to save UploadFile : {}, {}", file.getOriginalFilename(), files);
        if (file.isEmpty() && ArrayUtils.isEmpty(files)) {
            throw new BadRequestAlertException("A new uploadImage cannot null", ENTITY_NAME, "imageisnull");
        }
        if (Objects.isNull(uploadFileDTO)) {
            uploadFileDTO = new UploadFileDTO();
        }
        List<MultipartFile> uploadFiles = new ArrayList<>();
        if (!file.isEmpty()) {
            uploadFiles.add(file);
        }
        if (ArrayUtils.isNotEmpty(files)) {
            uploadFiles.addAll(Arrays.asList(files));
        }
        List<UploadFileDTO> allUploadFileDTOS = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {
            uploadFileDTO.setFile(uploadFile);
            allUploadFileDTOS.add(uploadFileService.save(uploadFileDTO));
            uploadFileDTO = BeanUtil.toBean(uploadFileDTO, UploadFileDTO.class);
        }
        UploadFileDTO result = allUploadFileDTOS.get(0);
        return ResponseEntity.created(new URI("/api/upload-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
