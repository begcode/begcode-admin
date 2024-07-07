package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.repository.UploadFileRepository;
import com.begcode.monolith.service.criteria.UploadFileCriteria;
import com.begcode.monolith.service.dto.UploadFileDTO;
import com.begcode.monolith.service.mapper.UploadFileMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.*;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link UploadFile}实体执行复杂查询的Service。
 * 主要输入是一个{@link UploadFileCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UploadFileDTO}列表{@link List} 或 {@link UploadFileDTO} 的分页列表 {@link Page}。
 */
@Service
public class UploadFileQueryService implements QueryService<UploadFile> {

    private static final Logger log = LoggerFactory.getLogger(UploadFileQueryService.class);

    protected final UploadFileRepository uploadFileRepository;

    protected final UploadFileMapper uploadFileMapper;

    public UploadFileQueryService(UploadFileRepository uploadFileRepository, UploadFileMapper uploadFileMapper) {
        this.uploadFileRepository = uploadFileRepository;
        this.uploadFileMapper = uploadFileMapper;
    }

    /**
     * Return a {@link List} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UploadFileDTO> findByCriteria(UploadFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return uploadFileMapper.toDto(Binder.joinQueryList(queryWrapper, UploadFile.class));
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UploadFileDTO> findByCriteria(UploadFileCriteria criteria, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, UploadFile.class, page).convert(uploadFile -> {
            Binder.bindRelations(uploadFile, new String[] {});
            return uploadFileMapper.toDto(uploadFile);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UploadFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return uploadFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UploadFileCriteria criteria) {
        return (List<T>) uploadFileRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UploadFileCriteria criteria) {
        return uploadFileRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UploadFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadFile> createQueryWrapper(UploadFileCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UploadFileCriteria keywordsCriteria = new UploadFileCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.ownerEntityId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.fileSize().setEquals(Long.valueOf(keywords));
                keywordsCriteria.referenceCount().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.url().setContains(keywords);
            keywordsCriteria.fullName().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.thumb().setContains(keywords);
            keywordsCriteria.ext().setContains(keywords);
            keywordsCriteria.type().setContains(keywords);
            keywordsCriteria.path().setContains(keywords);
            keywordsCriteria.folder().setContains(keywords);
            keywordsCriteria.ownerEntityName().setContains(keywords);
            keywordsCriteria.businessTitle().setContains(keywords);
            keywordsCriteria.businessDesc().setContains(keywords);
            keywordsCriteria.businessStatus().setContains(keywords);
            UploadFileCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<UploadFile> queryWrapper = new DynamicJoinQueryWrapper<>(UploadFileCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, UploadFile.class);
    }

    /**
     * Function to convert {@link UploadFileCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadFile> createQueryWrapperNoJoin(UploadFileCriteria criteria) {
        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, UploadFile.class);
    }

    /**
     * Return a {@link IPage} of {@link UploadFileDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UploadFileDTO> findByQueryWrapper(QueryWrapper<UploadFile> queryWrapper, Page<UploadFile> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uploadFileRepository.selectPage(page, queryWrapper).convert(uploadFileMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UploadFileCriteria criteria) {
        QueryWrapper<UploadFile> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.url", criteria.getUrl());
        fieldNameMap.put("self.full_name", criteria.getFullName());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.thumb", criteria.getThumb());
        fieldNameMap.put("self.ext", criteria.getExt());
        fieldNameMap.put("self.type", criteria.getType());
        fieldNameMap.put("self.path", criteria.getPath());
        fieldNameMap.put("self.folder", criteria.getFolder());
        fieldNameMap.put("self.owner_entity_name", criteria.getOwnerEntityName());
        fieldNameMap.put("self.owner_entity_id", criteria.getOwnerEntityId());
        fieldNameMap.put("self.business_title", criteria.getBusinessTitle());
        fieldNameMap.put("self.business_desc", criteria.getBusinessDesc());
        fieldNameMap.put("self.business_status", criteria.getBusinessStatus());
        fieldNameMap.put("self.create_at", criteria.getCreateAt());
        fieldNameMap.put("self.file_size", criteria.getFileSize());
        fieldNameMap.put("self.reference_count", criteria.getReferenceCount());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> {
                getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields);
            });
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, UploadFile.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
