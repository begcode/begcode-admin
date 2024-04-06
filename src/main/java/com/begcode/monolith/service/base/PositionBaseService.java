package com.begcode.monolith.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.Position;
import com.begcode.monolith.domain.enumeration.SortValueOperateType;
import com.begcode.monolith.repository.PositionRepository;
import com.begcode.monolith.service.dto.PositionDTO;
import com.begcode.monolith.service.mapper.PositionMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.domain.Position}.
 */
@SuppressWarnings("UnusedReturnValue")
public class PositionBaseService<R extends PositionRepository, E extends Position> extends BaseServiceImpl<PositionRepository, Position> {

    private final Logger log = LoggerFactory.getLogger(PositionBaseService.class);
    private final List<String> relationNames = List.of("users");

    protected final PositionRepository positionRepository;

    protected final CacheManager cacheManager;

    protected final PositionMapper positionMapper;

    public PositionBaseService(PositionRepository positionRepository, CacheManager cacheManager, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.cacheManager = cacheManager;
        this.positionMapper = positionMapper;
    }

    /**
     * Save a position.
     *
     * @param positionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public PositionDTO save(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);

        this.saveOrUpdate(position);
        return findOne(position.getId()).orElseThrow();
    }

    /**
     * Update a position.
     *
     * @param positionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public PositionDTO update(PositionDTO positionDTO) {
        log.debug("Request to update Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);

        this.saveOrUpdate(position);
        return findOne(position.getId()).orElseThrow();
    }

    /**
     * Partially update a position.
     *
     * @param positionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<PositionDTO> partialUpdate(PositionDTO positionDTO) {
        log.debug("Request to partially update Position : {}", positionDTO);

        return positionRepository
            .findById(positionDTO.getId())
            .map(existingPosition -> {
                positionMapper.partialUpdate(existingPosition, positionDTO);

                return existingPosition;
            })
            .map(tempPosition -> {
                positionRepository.save(tempPosition);
                return positionMapper.toDto(positionRepository.selectById(tempPosition.getId()));
            });
    }

    /**
     * Get all the positions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<PositionDTO> findAll(Page<Position> pageable) {
        log.debug("Request to get all Positions");
        return this.page(pageable).convert(positionMapper::toDto);
    }

    /**
     * Get one position by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PositionDTO> findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        return Optional.ofNullable(positionRepository.selectById(id))
            .map(position -> {
                Binder.bindRelations(position);
                return position;
            })
            .map(positionMapper::toDto);
    }

    /**
     * Delete the position by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);

        positionRepository.deleteById(id);
    }

    /**
     * Update specified field by position
     */
    @Transactional
    public void updateBatch(PositionDTO changePositionDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<Position> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changePositionDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<Position> positionList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(positionList)) {
                positionList.forEach(position -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                position,
                                relationName,
                                BeanUtil.getFieldValue(positionMapper.toEntity(changePositionDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(position, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            Position byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }

    @Transactional
    public Boolean updateSortValue(
        Long id,
        Long beforeId,
        Long afterId,
        Integer changeSortValue,
        QueryWrapper<Position> queryWrapper,
        SortValueOperateType type
    ) {
        switch (type) {
            case VALUE: {
                if (ObjectUtils.isNotEmpty(changeSortValue)) {
                    return lambdaUpdate().set(Position::getSortNo, changeSortValue).eq(Position::getId, id).update();
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
                        .filter(position -> position.getSortNo() != null)
                        .collect(Collectors.toMap(Position::getId, Position::getSortNo));
                    return (
                        lambdaUpdate().set(Position::getSortNo, idSortValueMap.get(beforeId)).eq(Position::getId, id).update() &&
                        lambdaUpdate().set(Position::getSortNo, idSortValueMap.get(id)).eq(Position::getId, beforeId).update()
                    );
                } else if (ObjectUtils.allNotNull(id, afterId)) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    ids.add(afterId);
                    Map<Long, Integer> idSortValueMap = listByIds(ids)
                        .stream()
                        .filter(position -> position.getSortNo() != null)
                        .collect(Collectors.toMap(Position::getId, Position::getSortNo));
                    return (
                        lambdaUpdate().set(Position::getSortNo, idSortValueMap.get(afterId)).eq(Position::getId, id).update() &&
                        lambdaUpdate().set(Position::getSortNo, idSortValueMap.get(id)).eq(Position::getId, afterId).update()
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
                    .filter(position -> position.getSortNo() != null)
                    .collect(Collectors.toMap(Position::getId, Position::getSortNo));
                if (ObjectUtils.allNotNull(beforeId, afterId)) {
                    // 计算中间值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (beforeSortValue + afterSortValue) / 2;
                    if (!newSortValue.equals(afterSortValue) && !newSortValue.equals(beforeSortValue)) {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Position::getSortNo, newSortValue).eq(Position::getId, id).update();
                    } else {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Position> list = this.list(queryWrapper.orderByAsc("sort_no"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortNo(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortNo();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortNo();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Position::getSortNo, newSortValue).eq(Position::getId, id).update();
                    }
                } else if (ObjectUtils.isNotEmpty(beforeId)) {
                    // 计算比beforeId实体大的排序值
                    Integer beforeSortValue = idSortValueMap.get(beforeId);
                    Integer newSortValue = (beforeSortValue + 100) - ((beforeSortValue + 100) % 100);
                    // 正常值，保存到数据库。
                    return lambdaUpdate().set(Position::getSortNo, newSortValue).eq(Position::getId, id).update();
                } else if (ObjectUtils.isNotEmpty(afterId)) {
                    // 计算比afterId实体小的排序值
                    Integer afterSortValue = idSortValueMap.get(afterId);
                    Integer newSortValue = (afterSortValue - 100) - ((afterSortValue - 100) % 100);
                    if (newSortValue <= 0) {
                        // 没有排序值插入空间了，重新对相关的所有记录的排序值进行计算。然后再插入相关的值。
                        // 需要确定相应的记录范围
                        List<Position> list = this.list(queryWrapper.orderByAsc("sort_no"));
                        Integer newBeforeSortValue = 0;
                        Integer newAfterSortValue = 0;
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSortNo(100 * (i + 1));
                            if (afterId.equals(list.get(i).getId())) {
                                newBeforeSortValue = list.get(i).getSortNo();
                            }
                            if (beforeId.equals(list.get(i).getId())) {
                                newAfterSortValue = list.get(i).getSortNo();
                            }
                        }
                        newSortValue = (newBeforeSortValue + newAfterSortValue) / 2;
                        updateBatchById(list);
                        return lambdaUpdate().set(Position::getSortNo, newSortValue).eq(Position::getId, id).update();
                    } else {
                        // 正常值，保存到数据库。
                        return lambdaUpdate().set(Position::getSortNo, newSortValue).eq(Position::getId, id).update();
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
