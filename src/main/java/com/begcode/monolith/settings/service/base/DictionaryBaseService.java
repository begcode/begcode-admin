package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.DynamicEnum;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.dto.DictionaryDTO;
import com.begcode.monolith.settings.service.mapper.DictionaryMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.DictionaryServiceExtProvider;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.diboot.core.vo.DictionaryVO;
import com.diboot.core.vo.LabelValue;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.Dictionary}.
 */
@SuppressWarnings("UnusedReturnValue")
public class DictionaryBaseService<R extends DictionaryRepository, E extends Dictionary>
    extends BaseServiceImpl<DictionaryRepository, Dictionary>
    implements DictionaryServiceExtProvider {

    private final Logger log = LoggerFactory.getLogger(DictionaryBaseService.class);
    private final List<String> relationNames = List.of("items");

    protected final DictionaryRepository dictionaryRepository;

    protected final CacheManager cacheManager;

    protected final DictionaryMapper dictionaryMapper;

    public DictionaryBaseService(DictionaryRepository dictionaryRepository, CacheManager cacheManager, DictionaryMapper dictionaryMapper) {
        this.dictionaryRepository = dictionaryRepository;
        this.cacheManager = cacheManager;
        this.dictionaryMapper = dictionaryMapper;
    }

    /**
     * Save a dictionary.
     *
     * @param dictionaryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public DictionaryDTO save(DictionaryDTO dictionaryDTO) {
        log.debug("Request to save Dictionary : {}", dictionaryDTO);
        Dictionary dictionary = dictionaryMapper.toEntity(dictionaryDTO);

        this.createEntityAndRelatedEntities(
                dictionary,
                dictionary.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "Dictionary"
            );
        return findOne(dictionary.getId()).orElseThrow();
    }

    /**
     * Update a dictionary.
     *
     * @param dictionaryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public DictionaryDTO update(DictionaryDTO dictionaryDTO) {
        log.debug("Request to update Dictionary : {}", dictionaryDTO);
        Dictionary dictionary = dictionaryMapper.toEntity(dictionaryDTO);

        this.createEntityAndRelatedEntities(
                dictionary,
                dictionary.getItems(),
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "Dictionary"
            );
        return findOne(dictionary.getId()).orElseThrow();
    }

    /**
     * Partially update a dictionary.
     *
     * @param dictionaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<DictionaryDTO> partialUpdate(DictionaryDTO dictionaryDTO) {
        log.debug("Request to partially update Dictionary : {}", dictionaryDTO);

        return dictionaryRepository
            .findById(dictionaryDTO.getId())
            .map(existingDictionary -> {
                dictionaryMapper.partialUpdate(existingDictionary, dictionaryDTO);

                return existingDictionary;
            })
            .map(tempDictionary -> {
                dictionaryRepository.save(tempDictionary);
                return dictionaryMapper.toDto(dictionaryRepository.selectById(tempDictionary.getId()));
            });
    }

    /**
     * Get all the dictionaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<DictionaryDTO> findAll(Page<Dictionary> pageable) {
        log.debug("Request to get all Dictionaries");
        return this.page(pageable).convert(dictionaryMapper::toDto);
    }

    /**
     * Get one dictionary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DictionaryDTO> findOne(Long id) {
        log.debug("Request to get Dictionary : {}", id);
        return Optional.ofNullable(dictionaryRepository.selectById(id))
            .map(dictionary -> {
                Binder.bindRelations(dictionary);
                return dictionary;
            })
            .map(dictionaryMapper::toDto);
    }

    /**
     * Delete the dictionary by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete Dictionary : {}", id);

        this.deleteEntityAndRelatedEntities(
                id,
                CommonFieldData.class,
                CommonFieldData::setOwnerEntityId,
                CommonFieldData::setOwnerEntityName,
                "Dictionary"
            );
    }

    @Override
    public void bindItemLabel(List voList, String setFieldName, String getFieldName, String type) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<Dictionary>().eq(Dictionary::getDictKey, type);
        List<Dictionary> entityList = super.getEntityList(queryWrapper, null);
        if (CollectionUtils.isNotEmpty(entityList)) {
            Dictionary dictionary = entityList.get(0);
            Binder.bindRelations(dictionary);
            Map<String, String> map = dictionary
                .getItems()
                .stream()
                .collect(Collectors.toMap(CommonFieldData::getValue, CommonFieldData::getLabel));
            for (Object item : voList) {
                Object value = BeanUtil.getProperty(item, getFieldName);
                if (Objects.isNull(value)) {
                    continue;
                }
                Object label = map.get(value);
                if (label == null) {
                    if (value instanceof String && ((String) value).contains(",")) {
                        List<String> labelList = new ArrayList<>();
                        for (String key : ((String) value).split(",")) {
                            labelList.add(map.get(key));
                        }
                        label = String.join(",", labelList);
                    } else if (value instanceof Collection) {
                        List<String> labelList = new ArrayList<>();
                        for (Object key : (Collection) value) {
                            labelList.add(map.get((String) key));
                        }
                        label = labelList;
                    }
                }
                if (Objects.nonNull(label)) {
                    BeanUtil.setProperty(item, setFieldName, label);
                }
            }
        }
    }

    @Override
    public List<LabelValue> getLabelValueList(String dictType) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<Dictionary>().eq(Dictionary::getDictKey, dictType);
        List<Dictionary> entityList = super.getEntityList(queryWrapper, null);
        if (CollectionUtils.isNotEmpty(entityList)) {
            Dictionary dictionary = entityList.get(0);
            Binder.bindRelations(dictionary);
            return dictionary
                .getItems()
                .stream()
                .map(item -> new LabelValue(item.getLabel(), item.getValue()))
                .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean existsDictType(String dictType) {
        return this.exists(new LambdaQueryWrapper<Dictionary>().eq(Dictionary::getDictKey, dictType));
    }

    @Override
    @Deprecated
    public boolean createDictAndChildren(DictionaryVO dictionaryVO) {
        return false;
    }

    @Override
    @Deprecated
    public List<com.diboot.core.entity.Dictionary> getDictDefinitionList() {
        return null;
    }

    @Override
    @Deprecated
    public List<DictionaryVO> getDictDefinitionVOList() {
        return null;
    }

    @Override
    @Deprecated
    public Map<String, LabelValue> getValue2ItemMap(String type) {
        return null;
    }

    @Override
    @Deprecated
    public Map<String, LabelValue> getLabel2ItemMap(String type) {
        return null;
    }

    public void updateEnums(DictionaryDTO dictionaryDTO) {
        try {
            Class clazz = Class.forName("com.begcode.monolith.domain.enumeration." + dictionaryDTO.getDictKey());
            DynamicEnum.addEnum0(clazz, "d", new Class[0], "");
        } catch (ClassNotFoundException e) {
            log.error("updateEnums error: " + dictionaryDTO.getDictKey(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Update specified field by dictionary
     */
    @Transactional
    public void updateBatch(DictionaryDTO changeDictionaryDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<Dictionary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeDictionaryDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<Dictionary> dictionaryList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(dictionaryList)) {
                dictionaryList.forEach(dictionary -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                dictionary,
                                relationName,
                                BeanUtil.getFieldValue(dictionaryMapper.toEntity(changeDictionaryDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(dictionary, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            Dictionary byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<Dictionary> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(Dictionary::getSortValue, changeSortValue).eq(Dictionary::getId, id).update();
                } else {
                    return false;
                }
            }
            case STEP: {
                if (ObjectUtils.allNotNull(id, beforeId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(beforeId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(dictionary -> dictionary.getSortValue() != null)
                        .collect(Collectors.toMap(Dictionary::getId, Dictionary::getSortValue));
                    return (
                        lambdaUpdate().set(Dictionary::getSortValue, idSortValueMap.get(beforeId)).eq(Dictionary::getId, id).update() &&
                        lambdaUpdate().set(Dictionary::getSortValue, idSortValueMap.get(id)).eq(Dictionary::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(dictionary -> dictionary.getSortValue() != null)
                        .collect(Collectors.toMap(Dictionary::getId, Dictionary::getSortValue));
                    return (
                        lambdaUpdate().set(Dictionary::getSortValue, idSortValueMap.get(afterId)).eq(Dictionary::getId, id).update() &&
                        lambdaUpdate().set(Dictionary::getSortValue, idSortValueMap.get(id)).eq(Dictionary::getId, afterId).update()
                    );
                } else {
                    return false;
                }
            }
            case DROP: {
                Set<Long> ids = new HashSet<>();
                ids.add(id);
                if (ObjectUtils.isNotEmpty(beforeId)) {
                    ids.add(beforeId);
                }
                if (ObjectUtils.isNotEmpty(afterId)) {
                    ids.add(afterId);
                }
                Map<Long, Integer> idSortValueMap = listByIds(ids)
                    .stream()
                    .filter(dictionary -> dictionary.getSortValue() != null)
                    .collect(Collectors.toMap(Dictionary::getId, Dictionary::getSortValue));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Dictionary::getSortValue, newSortValue).eq(Dictionary::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Dictionary> list = this.list(queryWrapper.orderByAsc("sort_value"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortValue(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortValue();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortValue();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Dictionary::getSortValue, newSortValue).eq(Dictionary::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(Dictionary::getSortValue, newSortValue).eq(Dictionary::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Dictionary> list = this.list(queryWrapper.orderByAsc("sort_value"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortValue(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortValue();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortValue();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Dictionary::getSortValue, newSortValue).eq(Dictionary::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Dictionary::getSortValue, newSortValue).eq(Dictionary::getId, id).update();
                    }
                } else {
                    // todo 异常
                    return false;
                }
            }
            default:
                return false;
        }
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
