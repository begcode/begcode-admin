package com.begcode.monolith.web.rest.base;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.aop.logging.AutoLog;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.service.AuthorityQueryService;
import com.begcode.monolith.service.AuthorityService;
import com.begcode.monolith.service.criteria.AuthorityCriteria;
import com.begcode.monolith.service.dto.AuthorityDTO;
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

 * 管理实体{@link com.begcode.monolith.domain.Authority}的REST Controller。
 */
public class AuthorityBaseResource {

    protected final Logger log = LoggerFactory.getLogger(AuthorityBaseResource.class);

    protected static final String ENTITY_NAME = "systemAuthority";

    @Value("${jhipster.clientApp.name}")
    protected String applicationName;

    protected final AuthorityService authorityService;

    protected final AuthorityRepository authorityRepository;

    protected final AuthorityQueryService authorityQueryService;

    public AuthorityBaseResource(
        AuthorityService authorityService,
        AuthorityRepository authorityRepository,
        AuthorityQueryService authorityQueryService
    ) {
        this.authorityService = authorityService;
        this.authorityRepository = authorityRepository;
        this.authorityQueryService = authorityQueryService;
    }

    /**
     * {@code POST  /authorities} : Create a new authority.
     *
     * @param authorityDTO the authorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorityDTO, or with status {@code 400 (Bad Request)} if the authority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Operation(tags = "新建角色", description = "创建并返回一个新的角色")
    @AutoLog(value = "新建角色", logType = LogType.OPERATE, operateType = OperateType.ADD)
    public ResponseEntity<AuthorityDTO> createAuthority(@RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to save Authority : {}", authorityDTO);
        if (authorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new authority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        authorityDTO = authorityService.save(authorityDTO);
        return ResponseEntity.created(new URI("/api/authorities/" + authorityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString()))
            .body(authorityDTO);
    }

    /**
     * {@code PUT  /authorities/:id} : Updates an existing authority.
     *
     * @param id the id of the authorityDTO to save.
     * @param authorityDTO the authorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Operation(tags = "更新角色", description = "根据主键更新并返回一个更新后的角色")
    @AutoLog(value = "更新角色", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AuthorityDTO> updateAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorityDTO authorityDTO,
        @RequestParam(value = "batchIds", required = false) ArrayList<Long> batchIds,
        @RequestParam(value = "batchFields", required = false) ArrayList<String> batchFields
    ) {
        log.debug("REST request to update Authority : {}, {}", id, authorityDTO);
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (authorityRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (CollectionUtils.isNotEmpty(batchFields) && CollectionUtils.isNotEmpty(batchIds)) {
            batchIds = new ArrayList<>(batchIds);
            if (!batchIds.contains(id)) {
                batchIds.add(id);
            }
            authorityService.updateBatch(authorityDTO, batchFields, batchIds);
            authorityDTO = authorityService.findOne(id).orElseThrow();
        } else {
            authorityDTO = authorityService.update(authorityDTO);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString()))
            .body(authorityDTO);
    }

    /**
     * {@code PUT  /authorities/relations/:operateType} : Updates relationships an existing authority.
     *
     * @param operateType the operateType of the authorityDTO to update.
     * @param otherEntityIds the otherEntityIds to update.
     * @param relationshipName the relationshipName to update.
     * @param relatedIds the relation relatedIds.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     */
    @PutMapping("/relations/{operateType}")
    @Operation(tags = "更新角色关联关系", description = "根据主键更新角色关联关系")
    @AutoLog(value = "更新角色关联关系", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<Boolean> updateRelationships(
        @PathVariable(value = "operateType") final String operateType,
        @RequestParam(value = "otherEntityIds") ArrayList<String> otherEntityIds,
        @RequestParam(value = "relationshipName") String relationshipName,
        @RequestParam(value = "relatedIds") ArrayList<Long> relatedIds
    ) {
        log.debug("REST request to update Authority : {}, {}", otherEntityIds, operateType);
        if (CollectionUtils.isEmpty(relatedIds)) {
            return ResponseEntity.ok(true);
        }
        authorityService.updateRelationships(otherEntityIds, relationshipName, relatedIds, operateType);
        return ResponseEntity.ok(true);
    }

    /**
     * {@code PUT  /authorities/sort-value/:id/:type} : Updates sort value authority.
     *
     * @param id the id of the authorityDTO to update.
     * @param beforeId the previous id of current record.
     * @param afterId the later id of current record.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
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
        @RequestBody(required = false) AuthorityCriteria criteria
    ) {
        log.debug("REST request to update SortValue : {}, {}, {}", id, beforeId, afterId);
        if (ObjectUtils.allNull(beforeId, afterId) && !type.equals(SortValueOperateType.VALUE)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (authorityRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        QueryWrapper<Authority> queryWrapper = null;
        if (type.equals(SortValueOperateType.DROP)) {
            queryWrapper = authorityQueryService.createQueryWrapper(criteria);
        }
        Boolean result = authorityService.updateSortValue(id, beforeId, afterId, newSortValue, queryWrapper, type);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /authorities/:id} : Partial updates given fields of an existing authority, field will ignore if it is null
     *
     * @param id the id of the authorityDTO to save.
     * @param authorityDTO the authorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Operation(tags = "部分更新角色", description = "根据主键及实体信息实现部分更新，值为null的属性将忽略，并返回一个更新后的角色")
    @AutoLog(value = "部分更新角色", logType = LogType.OPERATE, operateType = OperateType.EDIT)
    public ResponseEntity<AuthorityDTO> partialUpdateAuthority(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorityDTO authorityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authority partially : {}, {}", id, authorityDTO);
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (authorityRepository.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorityDTO> result = authorityService.partialUpdate(authorityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authorities} : get all the authorities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
     */
    @GetMapping("")
    @Operation(tags = "获取角色分页列表", description = "获取角色的分页列表数据")
    @AutoLog(value = "获取角色分页列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<AuthorityDTO>> getAllAuthorities(
        AuthorityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Authorities by criteria: {}", criteria);

        IPage<AuthorityDTO> page;
        page = authorityQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        PageRecord<AuthorityDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size(page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * {@code GET  /authorities/count} : count all the authorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAuthorities(AuthorityCriteria criteria) {
        log.debug("REST request to count Authorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(authorityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /authorities/:id} : get the "id" authority.
     *
     * @param id the id of the authorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Operation(tags = "获取指定主键的角色", description = "获取指定主键的角色信息")
    @AutoLog(value = "获取指定主键的角色", logType = LogType.OPERATE, operateType = OperateType.VIEW)
    public ResponseEntity<AuthorityDTO> getAuthority(@PathVariable("id") Long id) {
        log.debug("REST request to get Authority : {}", id);
        Optional<AuthorityDTO> authorityDTO = authorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorityDTO);
    }

    /**
     * {@code DELETE  /authorities/:id} : delete the "id" authority.
     *
     * @param id the id of the authorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Operation(tags = "删除指定主键的角色", description = "删除指定主键的角色信息")
    @AutoLog(value = "删除指定主键的角色", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAuthority(@PathVariable("id") Long id) {
        log.debug("REST request to delete Authority : {}", id);

        authorityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * GET  /authorities/export : export the authorities.
     *
     */
    @GetMapping("/export")
    @Operation(tags = "角色EXCEL导出", description = "导出全部角色为EXCEL文件")
    @AutoLog(value = "角色EXCEL导出", logType = LogType.OPERATE, operateType = OperateType.EXPORT)
    public void exportToExcel(HttpServletResponse response) {
        List<AuthorityDTO> data = authorityService.findAll(new Page<>(1, Integer.MAX_VALUE)).getRecords();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("角色一览表", "角色"), AuthorityDTO.class, data);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = "角色_" + sdf.format(new Date()) + ".xlsx";
        try {
            ExportUtil.excel(workbook, filename, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POST  /authorities/import : import the authorities from excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the authorityDTO, or with status 404 (Not Found)
     */
    @PostMapping("/import")
    @Operation(tags = "角色EXCEL导入", description = "根据角色EXCEL文件导入全部数据")
    @AutoLog(value = "角色EXCEL导入", logType = LogType.OPERATE, operateType = OperateType.IMPORT)
    public ResponseEntity<Void> exportToExcel(MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<AuthorityDTO> list = ExcelImportUtil.importExcel(file.getInputStream(), AuthorityDTO.class, params);
        list.forEach(authorityService::save);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /authorities/tree : get all the authorities for parent is null.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of authorities in body
     */
    @GetMapping("/tree")
    @Operation(tags = "获取角色树形列表", description = "获取角色的树形列表数据")
    @AutoLog(value = "获取角色树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<PageRecord<AuthorityDTO>> getAllAuthoritiesofTree(AuthorityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get a page of Authorities");
        IPage<AuthorityDTO> page = authorityQueryService.findAllTop(criteria, PageableUtils.toPage(pageable));
        PageRecord<AuthorityDTO> result = new PageRecord<>();
        result.records(page.getRecords()).size((int) page.getSize()).total(page.getTotal()).page(page.getCurrent());
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    /**
     * GET  /authorities/{parentId}/tree : get all the authorities for parent is parentId.
     *
     * @param parentId the parent of Id
     * @return the ResponseEntity with status 200 (OK) and the list of authorities in body
     */
    @GetMapping("/{parentId}/tree")
    @Operation(tags = "获取角色指定节点下的树形列表", description = "获取角色指定节点下的树形列表数据")
    @AutoLog(value = "获取角色指定节点下的树形列表", logType = LogType.OPERATE, operateType = OperateType.LIST)
    public ResponseEntity<List<AuthorityDTO>> getAllAuthoritiesofParent(@PathVariable Long parentId) {
        log.debug("REST request to get all Authorities of parentId");
        List<AuthorityDTO> list = authorityQueryService.findChildrenByParentId(parentId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code DELETE  /authorities} : delete all the "ids" Authorities.
     *
     * @param ids the ids of the articleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    @Operation(tags = "删除多个角色", description = "根据主键删除多个角色")
    @AutoLog(value = "删除多个角色", logType = LogType.OPERATE, operateType = OperateType.DELETE)
    public ResponseEntity<Void> deleteAuthoritiesByIds(@RequestParam("ids") ArrayList<Long> ids) {
        log.debug("REST request to delete Authorities : {}", ids);
        if (ids != null) {
            ids.forEach(authorityService::delete);
        }
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, (ids != null ? ids.toString() : "NoIds")))
            .build();
    }

    @GetMapping("/stats")
    @Operation(tags = "根据条件对角色进行统计", description = "条件和统计的配置通过角色的Criteria类来实现")
    @AutoLog(value = "根据条件对角色进行统计", logType = LogType.OPERATE, operateType = OperateType.STATS)
    public ResponseEntity<List<Map<String, Object>>> stats(AuthorityCriteria criteria) {
        log.debug("REST request to get stats by criteria: {}", criteria);
        List<Map<String, Object>> statsMapList = authorityQueryService.statsByAggregateCriteria(criteria);
        return ResponseEntity.ok().body(statsMapList);
    }
    // jhipster-needle-rest-resource-add-api - JHipster will add getters and setters here, do not remove

}
