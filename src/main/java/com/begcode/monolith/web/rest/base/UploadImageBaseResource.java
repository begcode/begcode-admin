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
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.UploadImageQueryService;
import com.begcode.monolith.service.UploadImageService;
import com.begcode.monolith.service.criteria.UploadImageCriteria;
import com.begcode.monolith.service.dto.UploadImageDTO;
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

 * 管理实体{@link com.begcode.monolith.domain.UploadImage}的REST Controller。
 */
public class UploadImageBaseResource {

    protected static final Logger log = LoggerFactory.getLogger(UploadImageBaseResource.class);

    protected static final String ENTITY_NAME = "filesUploadImage";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final UploadImageService uploadImageService;

    protected final UploadImageRepository uploadImageRepository;

    protected final UploadImageQueryService uploadImageQueryService;

    public UploadImageBaseResource(
        UploadImageService uploadImageService,
        UploadImageRepository uploadImageRepository,
        UploadImageQueryService uploadImageQueryService
    ) {
        this.uploadImageService = uploadImageService;
        this.uploadImageRepository = uploadImageRepository;
        this.uploadImageQueryService = uploadImageQueryService;
    }

    /**
     * {@code PUT  /upload-images/:id} : Updates an existing uploadImage.
     *
     * @param id the id of the uploadImageDTO to save.
     * @param uploadImageDTO the uploadImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新上传图片", description = "根据主键更新并返回一个更新后的上传图片")
    @AutoLog(value = "更新上传图片", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadImageDTO> updateUploadImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UploadImageDTO uploadImageDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update UploadImage : {}, {}", id, uploadImageDTO);
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadImageRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            uploadImageService.updateBatch(uploadImageDTO, batchFields, batchIds);
            uploadImageDTO = uploadImageService.findOne(id).orElseThrow();
        } else {
            uploadImageDTO = uploadImageService.update(uploadImageDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString()))
            .body(uploadImageDTO);
    }

    /**
     * {@code PATCH  /upload-images/:id} : Partial updates given fields of an existing uploadImage, field will ignore if it is null
     *
     * @param id the id of the uploadImageDTO to save.
     * @param uploadImageDTO the uploadImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新上传图片", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的上传图片")
    @AutoLog(value = "部分更新上传图片", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadImageDTO> partialUpdateUploadImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UploadImageDTO uploadImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UploadImage partially : {}, {}", id, uploadImageDTO);
        if (uploadImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uploadImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (uploadImageRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadImageDTO> result = uploadImageService.partialUpdate(uploadImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString())
        );
    }

    /**
     * {@code PATCH  /upload-images/copy/:id} : Partial updates given fields of an existing uploadImage, field will ignore if it is null
     *
     * @param id the id of the uploadImageDTO to save.
     * @param uploadImageDTO the uploadImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadImageDTO,
     * or with status {@code 400 (Bad Request)} if the uploadImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/copy/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新上传图片", description = "根据主键及实体信息复制新的实体，值为null的属性将忽略，并返回一个复制后的上传图片")
    @AutoLog(value = "复制上传图片", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<UploadImageDTO> copyUploadImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UploadImageDTO uploadImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to copy UploadImage partially : {}, {}", id, uploadImageDTO);

        if (uploadImageRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UploadImageDTO> result = uploadImageService.copy(uploadImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upload-images} : get all the uploadImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploadImages in body.
     */
    @GetMapping("")
    @Operation(tags = "获取上传图片分页列表", description = "获取上传图片的分页列表数据")
    @AutoLog(value = "获取上传图片分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<UploadImageDTO>> getAllUploadImages(
        UploadImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UploadImages by criteria: {}", criteria);

        IPage<UploadImageDTO> page;
        page = uploadImageQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<UploadImageDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /upload-images/count} : count all the uploadImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUploadImages(UploadImageCriteria criteria) {
        log.debug("REST request to count UploadImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(uploadImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upload-images/:id} : get the "id" uploadImage.
     *
     * @param id the id of the uploadImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uploadImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的上传图片", description = "获取指定主键的上传图片信息")
    @AutoLog(value = "获取指定主键的上传图片", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<UploadImageDTO> getUploadImage(@PathVariable("id") Long id) {
        log.debug("REST request to get UploadImage : {}", id);
        Optional<UploadImageDTO> uploadImageDTO = uploadImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uploadImageDTO);
    }

    /**
     * {@code DELETE  /upload-images/:id} : delete the "id" uploadImage.
     *
     * @param id the id of the uploadImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的上传图片", description = "删除指定主键的上传图片信息")
    @AutoLog(value = "删除指定主键的上传图片", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUploadImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete UploadImage : {}", id);

        uploadImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /upload-images/export : export the uploadImages.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "上传图片EXCEL导出", description = "导出全部上传图片为EXCEL文件")
    @AutoLog(value = "上传图片EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<UploadImageDTO> data = uploadImageService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("上传图片一览表", "上传图片"), UploadImageDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "上传图片_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /upload-images/import : import the uploadImages from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the uploadImageDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "上传图片EXCEL导入", description = "根据上传图片EXCEL文件导入全部数据")
    @AutoLog(value = "上传图片EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<UploadImageDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), UploadImageDTO.class, params);
        list.forEach(uploadImageService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code DELETE  /upload-images} : delete all the "ids" UploadImages.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个上传图片", description = "根据主键删除多个上传图片")
    @AutoLog(value = "删除多个上传图片", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteUploadImagesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete UploadImages : {}", ids);
        if (ids != null) {
            ids.forEach(uploadImageService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对上传图片进行统计", description = "条件和统计的配置通过上传图片的Criteria类来实现")
    @AutoLog(value = "根据条件对上传图片进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(UploadImageCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = uploadImageQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }

    /**
     * {@code POST  /} : Create a new uploadImage.
     *
     * @param uploadImageDTO the uploadImageDTO to create.
     * @param image the imageFile to upload.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uploadImageDTO, or with status {@code 400 (Bad Request)} if the uploadImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UploadImageDTO> createUploadImage(
        @RequestPart(required = false) UploadImageDTO uploadImageDTO,
        @RequestPart(name = "image", required = false) MultipartFile image,
        @RequestPart(name = "images", required = false) MultipartFile[] images
    ) throws URISyntaxException {
        log.debug("REST request to save UploadImage : {}", image.getOriginalFilename());
        if (image.isEmpty() && ArrayUtils.isEmpty(images)) {
            throw new BadRequestAlertException("A new uploadImage cannot null", ENTITY_NAME, "imageisnull");
        }
        if (Objects.isNull(uploadImageDTO)) {
            uploadImageDTO = new UploadImageDTO();
        }
        List<MultipartFile> uploadImages = new ArrayList<>();
        if (!image.isEmpty()) {
            uploadImages.add(image);
        }
        if (ArrayUtils.isNotEmpty(images)) {
            uploadImages.addAll(Arrays.asList(images));
        }
        List<UploadImageDTO> allUploadImageDTOS = new ArrayList<>();
        for (MultipartFile uploadImage : uploadImages) {
            uploadImageDTO.setImage(uploadImage);
            allUploadImageDTOS.add(uploadImageService.save(uploadImageDTO));
            uploadImageDTO = BeanUtil.toBean(uploadImageDTO, UploadImageDTO.class);
        }
        UploadImageDTO result = allUploadImageDTOS.get(0);
        return ResponseEntity.created(new URI("/api/upload-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
