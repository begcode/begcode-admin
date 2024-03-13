package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.AnnoSendStatus;
import com.begcode.monolith.security.SecurityUtils;
import com.begcode.monolith.service.UserQueryService;
import com.begcode.monolith.service.criteria.UserCriteria;
import com.begcode.monolith.system.domain.Announcement;
import com.begcode.monolith.system.domain.AnnouncementRecord;
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.dto.AnnouncementDTO;
import com.begcode.monolith.system.service.mapper.AnnouncementMapper;
import com.begcode.monolith.web.rest.errors.BadRequestAlertException;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import java.time.ZonedDateTime;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.Announcement}.
 */
public class AnnouncementBaseService<R extends AnnouncementRepository, E extends Announcement>
    extends BaseServiceImpl<AnnouncementRepository, Announcement> {

    private final Logger log = LoggerFactory.getLogger(AnnouncementBaseService.class);
    private final List<String> relationNames = Arrays.asList();

    protected final UserQueryService userQueryService;

    protected final AnnouncementRecordRepository announcementRecordRepository;

    protected final AnnouncementRepository announcementRepository;

    protected final CacheManager cacheManager;

    protected final AnnouncementMapper announcementMapper;

    public AnnouncementBaseService(
        UserQueryService userQueryService,
        AnnouncementRecordRepository announcementRecordRepository,
        AnnouncementRepository announcementRepository,
        CacheManager cacheManager,
        AnnouncementMapper announcementMapper
    ) {
        this.userQueryService = userQueryService;
        this.announcementRecordRepository = announcementRecordRepository;
        this.announcementRepository = announcementRepository;
        this.cacheManager = cacheManager;
        this.announcementMapper = announcementMapper;
    }

    /**
     * Save a announcement.
     *
     * @param announcementDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public AnnouncementDTO save(AnnouncementDTO announcementDTO) {
        log.debug("Request to save Announcement : {}", announcementDTO);
        Announcement announcement = announcementMapper.toEntity(announcementDTO);

        this.saveOrUpdate(announcement);
        return findOne(announcement.getId()).orElseThrow();
    }

    /**
     * Update a announcement.
     *
     * @param announcementDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public AnnouncementDTO update(AnnouncementDTO announcementDTO) {
        log.debug("Request to update Announcement : {}", announcementDTO);

        Announcement announcement = announcementMapper.toEntity(announcementDTO);

        announcementRepository.updateById(announcement);
        return findOne(announcementDTO.getId()).orElseThrow();
    }

    /**
     * Partially update a announcement.
     *
     * @param announcementDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<AnnouncementDTO> partialUpdate(AnnouncementDTO announcementDTO) {
        log.debug("Request to partially update Announcement : {}", announcementDTO);

        return announcementRepository
            .findById(announcementDTO.getId())
            .map(existingAnnouncement -> {
                announcementMapper.partialUpdate(existingAnnouncement, announcementDTO);

                return existingAnnouncement;
            })
            .map(tempAnnouncement -> {
                announcementRepository.save(tempAnnouncement);
                return announcementMapper.toDto(announcementRepository.selectById(tempAnnouncement.getId()));
            });
    }

    /**
     * Get all the announcements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<AnnouncementDTO> findAll(Page<Announcement> pageable) {
        log.debug("Request to get all Announcements");
        return this.page(pageable).convert(announcementMapper::toDto);
    }

    /**
     * Get one announcement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AnnouncementDTO> findOne(Long id) {
        log.debug("Request to get Announcement : {}", id);
        return Optional
            .ofNullable(announcementRepository.selectById(id))
            .map(announcement -> {
                Binder.bindRelations(announcement);
                return announcement;
            })
            .map(announcementMapper::toDto);
    }

    /**
     * Delete the announcement by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete Announcement : {}", id);

        announcementRepository.deleteById(id);
    }

    @Transactional
    public void release(Long id) {
        Announcement announcement = announcementRepository.selectById(id);
        if (announcement != null) {
            if (announcement.getSendStatus() == AnnoSendStatus.RELEASED) {
                return;
            }
            Long[] userIds = {};
            UserCriteria criteria = new UserCriteria();
            List<Long> receiverIds = new ArrayList<>();
            Optional
                .ofNullable(announcement.getReceiverIds())
                .ifPresent(receiverIdData -> receiverIds.addAll(Arrays.stream((receiverIdData.split(","))).map(Long::valueOf).toList()));
            switch (announcement.getReceiverType()) {
                case ALL:
                    userIds = userIds;
                    break;
                case USER:
                    userIds = receiverIds.toArray(userIds);
                    break;
                case POSITION:
                    criteria.positionId().setIn(receiverIds);
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
                case DEPARTMENT:
                    criteria.departmentId().setIn(receiverIds);
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
                case AUTHORITY:
                    criteria.authoritiesId().setIn(receiverIds);
                    userIds = userQueryService.getFieldByCriteria(Long.class, "id", true, criteria).toArray(userIds);
                    break;
            }
            for (Long userId : userIds) {
                announcementRecordRepository.save(new AnnouncementRecord().anntId(announcement.getId()).userId(userId).hasRead(false));
            }
            announcement
                .sendStatus(AnnoSendStatus.RELEASED)
                .sendTime(ZonedDateTime.now())
                .senderId(SecurityUtils.getCurrentUserId().orElse(null));
            announcementRepository.save(announcement);
        } else {
            throw new BadRequestAlertException("未找到指定Id的通知", "Announcement", "IdNotFound");
        }
    }

    /**
     * Update specified field by announcement
     */
    @Transactional
    public void updateBatch(AnnouncementDTO changeAnnouncementDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<Announcement> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(fieldName ->
                updateWrapper.set(
                    CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                    BeanUtil.getFieldValue(changeAnnouncementDTO, fieldName)
                )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<Announcement> announcementList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(announcementList)) {
                announcementList.forEach(announcement -> {
                    relationshipNames.forEach(relationName ->
                        BeanUtil.setFieldValue(
                            announcement,
                            relationName,
                            BeanUtil.getFieldValue(announcementMapper.toEntity(changeAnnouncementDTO), relationName)
                        )
                    );
                    this.createOrUpdateAndRelatedRelations(announcement, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            Announcement byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
