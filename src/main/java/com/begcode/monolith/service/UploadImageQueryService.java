package com.begcode.monolith.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.UploadImage;
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.criteria.UploadImageCriteria;
import com.begcode.monolith.service.dto.UploadImageDTO;
import com.begcode.monolith.service.mapper.UploadImageMapper;
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
 * 用于对数据库中的{@link UploadImage}实体执行复杂查询的Service。
 * 主要输入是一个{@link UploadImageCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link UploadImageDTO}列表{@link List} 或 {@link UploadImageDTO} 的分页列表 {@link Page}。
 */
@Service
public class UploadImageQueryService implements QueryService<UploadImage> {

    private static final Logger log = LoggerFactory.getLogger(UploadImageQueryService.class);

    protected final UploadImageRepository uploadImageRepository;

    protected final UploadImageMapper uploadImageMapper;

    public UploadImageQueryService(UploadImageRepository uploadImageRepository, UploadImageMapper uploadImageMapper) {
        this.uploadImageRepository = uploadImageRepository;
        this.uploadImageMapper = uploadImageMapper;
    }

    /**
     * Return a {@link List} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<UploadImageDTO> findByCriteria(UploadImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        return uploadImageMapper.toDto(Binder.joinQueryList(queryWrapper, UploadImage.class));
    }

    /**
     * Return a {@link IPage} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<UploadImageDTO> findByCriteria(UploadImageCriteria criteria, Page<UploadImage> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        return Binder.joinQueryPage(queryWrapper, UploadImage.class, page).convert(uploadImage -> {
            Binder.bindRelations(uploadImage, new String[] {});
            return uploadImageMapper.toDto(uploadImage);
        });
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(UploadImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        return uploadImageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, UploadImageCriteria criteria) {
        return (List<T>) uploadImageRepository.selectObjs(createQueryWrapperNoJoin(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, UploadImageCriteria criteria) {
        return uploadImageRepository.selectCount(createQueryWrapperNoJoin(criteria));
    }

    /**
     * Function to convert {@link UploadImageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadImage> createQueryWrapper(UploadImageCriteria criteria) {
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            String keywords = criteria.getJhiCommonSearchKeywords();
            criteria.setJhiCommonSearchKeywords(null);
            UploadImageCriteria keywordsCriteria = new UploadImageCriteria();
            keywordsCriteria.setUseOr(true);
            if (StringUtils.isNumeric(keywords)) {
                keywordsCriteria.id().setEquals(Long.valueOf(keywords));
                keywordsCriteria.fileSize().setEquals(Long.valueOf(keywords));
                keywordsCriteria.ownerEntityId().setEquals(Long.valueOf(keywords));
                keywordsCriteria.referenceCount().setEquals(Long.valueOf(keywords));
                keywordsCriteria.createdBy().setEquals(Long.valueOf(keywords));
                keywordsCriteria.lastModifiedBy().setEquals(Long.valueOf(keywords));
            }
            keywordsCriteria.fullName().setContains(keywords);
            keywordsCriteria.businessTitle().setContains(keywords);
            keywordsCriteria.businessDesc().setContains(keywords);
            keywordsCriteria.businessStatus().setContains(keywords);
            keywordsCriteria.url().setContains(keywords);
            keywordsCriteria.name().setContains(keywords);
            keywordsCriteria.ext().setContains(keywords);
            keywordsCriteria.type().setContains(keywords);
            keywordsCriteria.path().setContains(keywords);
            keywordsCriteria.folder().setContains(keywords);
            keywordsCriteria.ownerEntityName().setContains(keywords);
            keywordsCriteria.smartUrl().setContains(keywords);
            keywordsCriteria.mediumUrl().setContains(keywords);
            UploadImageCriteria tempCriteria = criteria;
            while (tempCriteria.getAnd() != null) {
                tempCriteria = tempCriteria.getAnd();
            }
            tempCriteria.setAnd(keywordsCriteria);
        }
        QueryWrapper<UploadImage> queryWrapper = new DynamicJoinQueryWrapper<>(UploadImageCriteria.class, null);
        return createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria, UploadImage.class);
    }

    /**
     * Function to convert {@link UploadImageCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<UploadImage> createQueryWrapperNoJoin(UploadImageCriteria criteria) {
        QueryWrapper<UploadImage> queryWrapper = new QueryWrapper<>();
        return createQueryWrapperNoJoin(queryWrapper, criteria.getUseOr(), criteria, UploadImage.class);
    }

    /**
     * Return a {@link IPage} of {@link UploadImageDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<UploadImageDTO> findByQueryWrapper(QueryWrapper<UploadImage> queryWrapper, Page<UploadImage> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return uploadImageRepository.selectPage(page, queryWrapper).convert(uploadImageMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(UploadImageCriteria criteria) {
        QueryWrapper<UploadImage> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        Map<String, Filter<?>> fieldNameMap = new HashMap<>();
        fieldNameMap.put("self.id", criteria.getId());
        fieldNameMap.put("self.full_name", criteria.getFullName());
        fieldNameMap.put("self.business_title", criteria.getBusinessTitle());
        fieldNameMap.put("self.business_desc", criteria.getBusinessDesc());
        fieldNameMap.put("self.business_status", criteria.getBusinessStatus());
        fieldNameMap.put("self.url", criteria.getUrl());
        fieldNameMap.put("self.name", criteria.getName());
        fieldNameMap.put("self.ext", criteria.getExt());
        fieldNameMap.put("self.type", criteria.getType());
        fieldNameMap.put("self.path", criteria.getPath());
        fieldNameMap.put("self.folder", criteria.getFolder());
        fieldNameMap.put("self.file_size", criteria.getFileSize());
        fieldNameMap.put("self.owner_entity_name", criteria.getOwnerEntityName());
        fieldNameMap.put("self.owner_entity_id", criteria.getOwnerEntityId());
        fieldNameMap.put("self.create_at", criteria.getCreateAt());
        fieldNameMap.put("self.smart_url", criteria.getSmartUrl());
        fieldNameMap.put("self.medium_url", criteria.getMediumUrl());
        fieldNameMap.put("self.reference_count", criteria.getReferenceCount());
        fieldNameMap.put("self.created_by", criteria.getCreatedBy());
        fieldNameMap.put("self.created_date", criteria.getCreatedDate());
        fieldNameMap.put("self.last_modified_by", criteria.getLastModifiedBy());
        fieldNameMap.put("self.last_modified_date", criteria.getLastModifiedDate());
        fieldNameMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> getAggregateAndGroupBy(entry.getValue(), entry.getKey(), selectFields, groupByFields));
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return Binder.joinQueryMapsPage(queryWrapper, UploadImage.class, null).getRecords();
        }
        return Collections.emptyList();
    }
}
