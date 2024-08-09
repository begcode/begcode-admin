package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.AnnouncementAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.AnnoBusinessType;
import com.begcode.monolith.domain.enumeration.AnnoCategory;
import com.begcode.monolith.domain.enumeration.AnnoOpenType;
import com.begcode.monolith.domain.enumeration.AnnoSendStatus;
import com.begcode.monolith.domain.enumeration.PriorityLevel;
import com.begcode.monolith.domain.enumeration.ReceiverType;
import com.begcode.monolith.system.domain.Announcement;
import com.begcode.monolith.system.repository.AnnouncementRepository;
import com.begcode.monolith.system.service.dto.AnnouncementDTO;
import com.begcode.monolith.system.service.mapper.AnnouncementMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AnnouncementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class AnnouncementResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_SENDER_ID = 1L;
    private static final Long UPDATED_SENDER_ID = 2L;
    private static final Long SMALLER_SENDER_ID = 1L - 1L;

    private static final PriorityLevel DEFAULT_PRIORITY = PriorityLevel.HIGH;
    private static final PriorityLevel UPDATED_PRIORITY = PriorityLevel.MEDIUM;

    private static final AnnoCategory DEFAULT_CATEGORY = AnnoCategory.SYSTEM_INFO;
    private static final AnnoCategory UPDATED_CATEGORY = AnnoCategory.NOTICE;

    private static final ReceiverType DEFAULT_RECEIVER_TYPE = ReceiverType.ALL;
    private static final ReceiverType UPDATED_RECEIVER_TYPE = ReceiverType.USER;

    private static final AnnoSendStatus DEFAULT_SEND_STATUS = AnnoSendStatus.NOT_RELEASE;
    private static final AnnoSendStatus UPDATED_SEND_STATUS = AnnoSendStatus.RELEASED;

    private static final ZonedDateTime DEFAULT_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CANCEL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CANCEL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CANCEL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final AnnoBusinessType DEFAULT_BUSINESS_TYPE = AnnoBusinessType.EMAIL;
    private static final AnnoBusinessType UPDATED_BUSINESS_TYPE = AnnoBusinessType.WORKFLOW;

    private static final Long DEFAULT_BUSINESS_ID = 1L;
    private static final Long UPDATED_BUSINESS_ID = 2L;
    private static final Long SMALLER_BUSINESS_ID = 1L - 1L;

    private static final AnnoOpenType DEFAULT_OPEN_TYPE = AnnoOpenType.URL;
    private static final AnnoOpenType UPDATED_OPEN_TYPE = AnnoOpenType.COMPONENT;

    private static final String DEFAULT_OPEN_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_PAGE = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_IDS = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_IDS = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;
    private static final Long SMALLER_CREATED_BY = 1L - 1L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;
    private static final Long SMALLER_LAST_MODIFIED_BY = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/announcements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private MockMvc restAnnouncementMockMvc;

    private Announcement announcement;

    private Announcement insertedAnnouncement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Announcement createEntity() {
        Announcement announcement = new Announcement()
            .title(DEFAULT_TITLE)
            .summary(DEFAULT_SUMMARY)
            .content(DEFAULT_CONTENT)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .senderId(DEFAULT_SENDER_ID)
            .priority(DEFAULT_PRIORITY)
            .category(DEFAULT_CATEGORY)
            .receiverType(DEFAULT_RECEIVER_TYPE)
            .sendStatus(DEFAULT_SEND_STATUS)
            .sendTime(DEFAULT_SEND_TIME)
            .cancelTime(DEFAULT_CANCEL_TIME)
            .businessType(DEFAULT_BUSINESS_TYPE)
            .businessId(DEFAULT_BUSINESS_ID)
            .openType(DEFAULT_OPEN_TYPE)
            .openPage(DEFAULT_OPEN_PAGE)
            .receiverIds(DEFAULT_RECEIVER_IDS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return announcement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Announcement createUpdatedEntity() {
        Announcement announcement = new Announcement()
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return announcement;
    }

    @BeforeEach
    public void initTest() {
        announcement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAnnouncement != null) {
            announcementRepository.deleteById(insertedAnnouncement.getId());
            insertedAnnouncement = null;
        }
    }

    @Test
    @Transactional
    void createAnnouncement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);
        var returnedAnnouncementDTO = om.readValue(
            restAnnouncementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnnouncementDTO.class
        );

        // Validate the Announcement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnnouncement = announcementMapper.toEntity(returnedAnnouncementDTO);
        assertAnnouncementUpdatableFieldsEquals(returnedAnnouncement, getPersistedAnnouncement(returnedAnnouncement));

        insertedAnnouncement = returnedAnnouncement;
    }

    @Test
    @Transactional
    void createAnnouncementWithExistingId() throws Exception {
        // Create the Announcement with an existing ID
        announcement.setId(1L);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        announcement.setTitle(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiverTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        announcement.setReceiverType(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnnouncements() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].receiverType").value(hasItem(DEFAULT_RECEIVER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].cancelTime").value(hasItem(sameInstant(DEFAULT_CANCEL_TIME))))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].openType").value(hasItem(DEFAULT_OPEN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openPage").value(hasItem(DEFAULT_OPEN_PAGE)))
            .andExpect(jsonPath("$.[*].receiverIds").value(hasItem(DEFAULT_RECEIVER_IDS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAnnouncement() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get the announcement
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL_ID, announcement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(announcement.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.senderId").value(DEFAULT_SENDER_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.receiverType").value(DEFAULT_RECEIVER_TYPE.toString()))
            .andExpect(jsonPath("$.sendStatus").value(DEFAULT_SEND_STATUS.toString()))
            .andExpect(jsonPath("$.sendTime").value(sameInstant(DEFAULT_SEND_TIME)))
            .andExpect(jsonPath("$.cancelTime").value(sameInstant(DEFAULT_CANCEL_TIME)))
            .andExpect(jsonPath("$.businessType").value(DEFAULT_BUSINESS_TYPE.toString()))
            .andExpect(jsonPath("$.businessId").value(DEFAULT_BUSINESS_ID.intValue()))
            .andExpect(jsonPath("$.openType").value(DEFAULT_OPEN_TYPE.toString()))
            .andExpect(jsonPath("$.openPage").value(DEFAULT_OPEN_PAGE))
            .andExpect(jsonPath("$.receiverIds").value(DEFAULT_RECEIVER_IDS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAnnouncementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        Long id = announcement.getId();

        defaultAnnouncementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAnnouncementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAnnouncementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where title equals to
        defaultAnnouncementFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where title in
        defaultAnnouncementFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where title is not null
        defaultAnnouncementFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where title contains
        defaultAnnouncementFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where title does not contain
        defaultAnnouncementFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime equals to
        defaultAnnouncementFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime in
        defaultAnnouncementFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime is not null
        defaultAnnouncementFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime is greater than or equal to
        defaultAnnouncementFiltering(
            "startTime.greaterThanOrEqual=" + DEFAULT_START_TIME,
            "startTime.greaterThanOrEqual=" + UPDATED_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime is less than or equal to
        defaultAnnouncementFiltering("startTime.lessThanOrEqual=" + DEFAULT_START_TIME, "startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime is less than
        defaultAnnouncementFiltering("startTime.lessThan=" + UPDATED_START_TIME, "startTime.lessThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where startTime is greater than
        defaultAnnouncementFiltering("startTime.greaterThan=" + SMALLER_START_TIME, "startTime.greaterThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime equals to
        defaultAnnouncementFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime in
        defaultAnnouncementFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime is not null
        defaultAnnouncementFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime is greater than or equal to
        defaultAnnouncementFiltering("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME, "endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime is less than or equal to
        defaultAnnouncementFiltering("endTime.lessThanOrEqual=" + DEFAULT_END_TIME, "endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime is less than
        defaultAnnouncementFiltering("endTime.lessThan=" + UPDATED_END_TIME, "endTime.lessThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where endTime is greater than
        defaultAnnouncementFiltering("endTime.greaterThan=" + SMALLER_END_TIME, "endTime.greaterThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId equals to
        defaultAnnouncementFiltering("senderId.equals=" + DEFAULT_SENDER_ID, "senderId.equals=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId in
        defaultAnnouncementFiltering("senderId.in=" + DEFAULT_SENDER_ID + "," + UPDATED_SENDER_ID, "senderId.in=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId is not null
        defaultAnnouncementFiltering("senderId.specified=true", "senderId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId is greater than or equal to
        defaultAnnouncementFiltering(
            "senderId.greaterThanOrEqual=" + DEFAULT_SENDER_ID,
            "senderId.greaterThanOrEqual=" + UPDATED_SENDER_ID
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId is less than or equal to
        defaultAnnouncementFiltering("senderId.lessThanOrEqual=" + DEFAULT_SENDER_ID, "senderId.lessThanOrEqual=" + SMALLER_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId is less than
        defaultAnnouncementFiltering("senderId.lessThan=" + UPDATED_SENDER_ID, "senderId.lessThan=" + DEFAULT_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where senderId is greater than
        defaultAnnouncementFiltering("senderId.greaterThan=" + SMALLER_SENDER_ID, "senderId.greaterThan=" + DEFAULT_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where priority equals to
        defaultAnnouncementFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where priority in
        defaultAnnouncementFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where priority is not null
        defaultAnnouncementFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where category equals to
        defaultAnnouncementFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where category in
        defaultAnnouncementFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where category is not null
        defaultAnnouncementFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where receiverType equals to
        defaultAnnouncementFiltering("receiverType.equals=" + DEFAULT_RECEIVER_TYPE, "receiverType.equals=" + UPDATED_RECEIVER_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where receiverType in
        defaultAnnouncementFiltering(
            "receiverType.in=" + DEFAULT_RECEIVER_TYPE + "," + UPDATED_RECEIVER_TYPE,
            "receiverType.in=" + UPDATED_RECEIVER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where receiverType is not null
        defaultAnnouncementFiltering("receiverType.specified=true", "receiverType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendStatus equals to
        defaultAnnouncementFiltering("sendStatus.equals=" + DEFAULT_SEND_STATUS, "sendStatus.equals=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendStatus in
        defaultAnnouncementFiltering(
            "sendStatus.in=" + DEFAULT_SEND_STATUS + "," + UPDATED_SEND_STATUS,
            "sendStatus.in=" + UPDATED_SEND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendStatus is not null
        defaultAnnouncementFiltering("sendStatus.specified=true", "sendStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime equals to
        defaultAnnouncementFiltering("sendTime.equals=" + DEFAULT_SEND_TIME, "sendTime.equals=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime in
        defaultAnnouncementFiltering("sendTime.in=" + DEFAULT_SEND_TIME + "," + UPDATED_SEND_TIME, "sendTime.in=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime is not null
        defaultAnnouncementFiltering("sendTime.specified=true", "sendTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime is greater than or equal to
        defaultAnnouncementFiltering(
            "sendTime.greaterThanOrEqual=" + DEFAULT_SEND_TIME,
            "sendTime.greaterThanOrEqual=" + UPDATED_SEND_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime is less than or equal to
        defaultAnnouncementFiltering("sendTime.lessThanOrEqual=" + DEFAULT_SEND_TIME, "sendTime.lessThanOrEqual=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime is less than
        defaultAnnouncementFiltering("sendTime.lessThan=" + UPDATED_SEND_TIME, "sendTime.lessThan=" + DEFAULT_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where sendTime is greater than
        defaultAnnouncementFiltering("sendTime.greaterThan=" + SMALLER_SEND_TIME, "sendTime.greaterThan=" + DEFAULT_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime equals to
        defaultAnnouncementFiltering("cancelTime.equals=" + DEFAULT_CANCEL_TIME, "cancelTime.equals=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime in
        defaultAnnouncementFiltering(
            "cancelTime.in=" + DEFAULT_CANCEL_TIME + "," + UPDATED_CANCEL_TIME,
            "cancelTime.in=" + UPDATED_CANCEL_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime is not null
        defaultAnnouncementFiltering("cancelTime.specified=true", "cancelTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime is greater than or equal to
        defaultAnnouncementFiltering(
            "cancelTime.greaterThanOrEqual=" + DEFAULT_CANCEL_TIME,
            "cancelTime.greaterThanOrEqual=" + UPDATED_CANCEL_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime is less than or equal to
        defaultAnnouncementFiltering(
            "cancelTime.lessThanOrEqual=" + DEFAULT_CANCEL_TIME,
            "cancelTime.lessThanOrEqual=" + SMALLER_CANCEL_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime is less than
        defaultAnnouncementFiltering("cancelTime.lessThan=" + UPDATED_CANCEL_TIME, "cancelTime.lessThan=" + DEFAULT_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where cancelTime is greater than
        defaultAnnouncementFiltering("cancelTime.greaterThan=" + SMALLER_CANCEL_TIME, "cancelTime.greaterThan=" + DEFAULT_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessType equals to
        defaultAnnouncementFiltering("businessType.equals=" + DEFAULT_BUSINESS_TYPE, "businessType.equals=" + UPDATED_BUSINESS_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessType in
        defaultAnnouncementFiltering(
            "businessType.in=" + DEFAULT_BUSINESS_TYPE + "," + UPDATED_BUSINESS_TYPE,
            "businessType.in=" + UPDATED_BUSINESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessType is not null
        defaultAnnouncementFiltering("businessType.specified=true", "businessType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId equals to
        defaultAnnouncementFiltering("businessId.equals=" + DEFAULT_BUSINESS_ID, "businessId.equals=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId in
        defaultAnnouncementFiltering(
            "businessId.in=" + DEFAULT_BUSINESS_ID + "," + UPDATED_BUSINESS_ID,
            "businessId.in=" + UPDATED_BUSINESS_ID
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId is not null
        defaultAnnouncementFiltering("businessId.specified=true", "businessId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId is greater than or equal to
        defaultAnnouncementFiltering(
            "businessId.greaterThanOrEqual=" + DEFAULT_BUSINESS_ID,
            "businessId.greaterThanOrEqual=" + UPDATED_BUSINESS_ID
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId is less than or equal to
        defaultAnnouncementFiltering(
            "businessId.lessThanOrEqual=" + DEFAULT_BUSINESS_ID,
            "businessId.lessThanOrEqual=" + SMALLER_BUSINESS_ID
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId is less than
        defaultAnnouncementFiltering("businessId.lessThan=" + UPDATED_BUSINESS_ID, "businessId.lessThan=" + DEFAULT_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where businessId is greater than
        defaultAnnouncementFiltering("businessId.greaterThan=" + SMALLER_BUSINESS_ID, "businessId.greaterThan=" + DEFAULT_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openType equals to
        defaultAnnouncementFiltering("openType.equals=" + DEFAULT_OPEN_TYPE, "openType.equals=" + UPDATED_OPEN_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openType in
        defaultAnnouncementFiltering("openType.in=" + DEFAULT_OPEN_TYPE + "," + UPDATED_OPEN_TYPE, "openType.in=" + UPDATED_OPEN_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openType is not null
        defaultAnnouncementFiltering("openType.specified=true", "openType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openPage equals to
        defaultAnnouncementFiltering("openPage.equals=" + DEFAULT_OPEN_PAGE, "openPage.equals=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openPage in
        defaultAnnouncementFiltering("openPage.in=" + DEFAULT_OPEN_PAGE + "," + UPDATED_OPEN_PAGE, "openPage.in=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openPage is not null
        defaultAnnouncementFiltering("openPage.specified=true", "openPage.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openPage contains
        defaultAnnouncementFiltering("openPage.contains=" + DEFAULT_OPEN_PAGE, "openPage.contains=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where openPage does not contain
        defaultAnnouncementFiltering("openPage.doesNotContain=" + UPDATED_OPEN_PAGE, "openPage.doesNotContain=" + DEFAULT_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy equals to
        defaultAnnouncementFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy in
        defaultAnnouncementFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy is not null
        defaultAnnouncementFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy is greater than or equal to
        defaultAnnouncementFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy is less than or equal to
        defaultAnnouncementFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy is less than
        defaultAnnouncementFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdBy is greater than
        defaultAnnouncementFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdDate equals to
        defaultAnnouncementFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdDate in
        defaultAnnouncementFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where createdDate is not null
        defaultAnnouncementFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy equals to
        defaultAnnouncementFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy in
        defaultAnnouncementFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy is not null
        defaultAnnouncementFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy is greater than or equal to
        defaultAnnouncementFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy is less than or equal to
        defaultAnnouncementFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy is less than
        defaultAnnouncementFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedBy is greater than
        defaultAnnouncementFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedDate equals to
        defaultAnnouncementFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedDate in
        defaultAnnouncementFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        // Get all the announcementList where lastModifiedDate is not null
        defaultAnnouncementFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultAnnouncementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAnnouncementShouldBeFound(shouldBeFound);
        defaultAnnouncementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnouncementShouldBeFound(String filter) throws Exception {
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].receiverType").value(hasItem(DEFAULT_RECEIVER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].cancelTime").value(hasItem(sameInstant(DEFAULT_CANCEL_TIME))))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].openType").value(hasItem(DEFAULT_OPEN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openPage").value(hasItem(DEFAULT_OPEN_PAGE)))
            .andExpect(jsonPath("$.[*].receiverIds").value(hasItem(DEFAULT_RECEIVER_IDS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnouncementShouldNotBeFound(String filter) throws Exception {
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnnouncement() throws Exception {
        // Get the announcement
        restAnnouncementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnouncement() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcement
        Announcement updatedAnnouncement = announcementRepository.findById(announcement.getId()).orElseThrow();
        updatedAnnouncement
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(updatedAnnouncement);

        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnnouncementToMatchAllProperties(updatedAnnouncement);
    }

    @Test
    @Transactional
    void putNonExistingAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnouncementWithPatch() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcement using partial update
        Announcement partialUpdatedAnnouncement = new Announcement();
        partialUpdatedAnnouncement.setId(announcement.getId());

        partialUpdatedAnnouncement
            .startTime(UPDATED_START_TIME)
            .senderId(UPDATED_SENDER_ID)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .openType(UPDATED_OPEN_TYPE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnouncementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAnnouncement, announcement),
            getPersistedAnnouncement(announcement)
        );
    }

    @Test
    @Transactional
    void fullUpdateAnnouncementWithPatch() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcement using partial update
        Announcement partialUpdatedAnnouncement = new Announcement();
        partialUpdatedAnnouncement.setId(announcement.getId());

        partialUpdatedAnnouncement
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnouncementUpdatableFieldsEquals(partialUpdatedAnnouncement, getPersistedAnnouncement(partialUpdatedAnnouncement));
    }

    @Test
    @Transactional
    void patchNonExistingAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnouncement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcement.setId(longCount.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(announcementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Announcement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnouncement() throws Exception {
        // Initialize the database
        insertedAnnouncement = announcementRepository.saveAndGet(announcement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the announcement
        restAnnouncementMockMvc
            .perform(delete(ENTITY_API_URL_ID, announcement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return announcementRepository.selectCount(null);
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Announcement getPersistedAnnouncement(Announcement announcement) {
        return announcementRepository.findById(announcement.getId()).orElseThrow();
    }

    protected void assertPersistedAnnouncementToMatchAllProperties(Announcement expectedAnnouncement) {
        assertAnnouncementAllPropertiesEquals(expectedAnnouncement, getPersistedAnnouncement(expectedAnnouncement));
    }

    protected void assertPersistedAnnouncementToMatchUpdatableProperties(Announcement expectedAnnouncement) {
        assertAnnouncementAllUpdatablePropertiesEquals(expectedAnnouncement, getPersistedAnnouncement(expectedAnnouncement));
    }
}
