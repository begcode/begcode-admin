package com.begcode.monolith.settings.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.common.IFillRuleHandler;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
import com.begcode.monolith.settings.service.mapper.SysFillRuleMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.Arrays;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.settings.domain.SysFillRule}.
 */
public class SysFillRuleBaseService<R extends SysFillRuleRepository, E extends SysFillRule>
    extends BaseServiceImpl<SysFillRuleRepository, SysFillRule>
    implements IFillRuleHandler {

    private final Logger log = LoggerFactory.getLogger(SysFillRuleBaseService.class);
    private final List<String> relationNames = Arrays.asList("ruleItems");

    protected final SysFillRuleRepository sysFillRuleRepository;

    protected final CacheManager cacheManager;

    protected final SysFillRuleMapper sysFillRuleMapper;

    public SysFillRuleBaseService(
        SysFillRuleRepository sysFillRuleRepository,
        CacheManager cacheManager,
        SysFillRuleMapper sysFillRuleMapper
    ) {
        this.sysFillRuleRepository = sysFillRuleRepository;
        this.cacheManager = cacheManager;
        this.sysFillRuleMapper = sysFillRuleMapper;
    }

    /**
     * Save a sysFillRule.
     *
     * @param sysFillRuleDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SysFillRuleDTO save(SysFillRuleDTO sysFillRuleDTO) {
        log.debug("Request to save SysFillRule : {}", sysFillRuleDTO);
        SysFillRule sysFillRule = sysFillRuleMapper.toEntity(sysFillRuleDTO);

        this.createEntityAndRelatedEntities(sysFillRule, sysFillRule.getRuleItems(), FillRuleItem::setFillRuleId);
        return findOne(sysFillRule.getId()).orElseThrow();
    }

    /**
     * Update a sysFillRule.
     *
     * @param sysFillRuleDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SysFillRuleDTO update(SysFillRuleDTO sysFillRuleDTO) {
        log.debug("Request to update SysFillRule : {}", sysFillRuleDTO);

        SysFillRule sysFillRule = sysFillRuleMapper.toEntity(sysFillRuleDTO);

        this.updateEntityAndRelatedEntities(sysFillRule, sysFillRule.getRuleItems(), FillRuleItem::setFillRuleId);
        return findOne(sysFillRuleDTO.getId()).orElseThrow();
    }

    /**
     * Partially update a sysFillRule.
     *
     * @param sysFillRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SysFillRuleDTO> partialUpdate(SysFillRuleDTO sysFillRuleDTO) {
        log.debug("Request to partially update SysFillRule : {}", sysFillRuleDTO);

        return sysFillRuleRepository
            .findById(sysFillRuleDTO.getId())
            .map(existingSysFillRule -> {
                sysFillRuleMapper.partialUpdate(existingSysFillRule, sysFillRuleDTO);

                return existingSysFillRule;
            })
            .map(tempSysFillRule -> {
                sysFillRuleRepository.save(tempSysFillRule);
                return sysFillRuleMapper.toDto(sysFillRuleRepository.selectById(tempSysFillRule.getId()));
            });
    }

    /**
     * Get all the sysFillRules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SysFillRuleDTO> findAll(Page<SysFillRule> pageable) {
        log.debug("Request to get all SysFillRules");
        return this.page(pageable).convert(sysFillRuleMapper::toDto);
    }

    /**
     * Get one sysFillRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SysFillRuleDTO> findOne(Long id) {
        log.debug("Request to get SysFillRule : {}", id);
        return Optional
            .ofNullable(sysFillRuleRepository.selectById(id))
            .map(sysFillRule -> {
                Binder.bindRelations(sysFillRule);
                return sysFillRule;
            })
            .map(sysFillRuleMapper::toDto);
    }

    /**
     * Delete the sysFillRule by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SysFillRule : {}", id);

        this.deleteEntityAndRelatedEntities(id, FillRuleItem.class, FillRuleItem::setFillRuleId);
    }

    @Override
    public Object execute(String params, String formData) {
        return null;
    }

    /**
     * Update specified field by sysFillRule
     */
    @Transactional
    public void updateBatch(SysFillRuleDTO changeSysFillRuleDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SysFillRule> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(fieldName ->
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeSysFillRuleDTO, fieldName)
                )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SysFillRule> sysFillRuleList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(sysFillRuleList)) {
                sysFillRuleList.forEach(sysFillRule -> {
                    relationshipNames.forEach(relationName ->
                        BeanUtil.setFieldValue(
                            sysFillRule,
                            relationName,
                            BeanUtil.getFieldValue(sysFillRuleMapper.toEntity(changeSysFillRuleDTO), relationName)
                        )
                    );
                    this.createOrUpdateAndRelatedRelations(sysFillRule, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            SysFillRule byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
