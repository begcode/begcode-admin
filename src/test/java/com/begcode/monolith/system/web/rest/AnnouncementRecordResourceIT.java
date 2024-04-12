package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.AnnouncementRecordAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.system.domain.AnnouncementRecord;
import com.begcode.monolith.system.repository.AnnouncementRecordRepository;
import com.begcode.monolith.system.service.dto.AnnouncementRecordDTO;
import com.begcode.monolith.system.service.mapper.AnnouncementRecordMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AnnouncementRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class AnnouncementRecordResourceIT {

    private static final Long DEFAULT_ANNT_ID = 1L;
    private static final Long UPDATED_ANNT_ID = 2L;
    private static final Long SMALLER_ANNT_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Boolean DEFAULT_HAS_READ = false;
    private static final Boolean UPDATED_HAS_READ = true;

    private static final ZonedDateTime DEFAULT_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READ_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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

    private static final String ENTITY_API_URL = "/api/announcement-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnnouncementRecordRepository announcementRecordRepository;

    @Autowired
    private AnnouncementRecordMapper announcementRecordMapper;

    @Autowired
    private MockMvc restAnnouncementRecordMockMvc;

    private AnnouncementRecord announcementRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnouncementRecord createEntity() {
        AnnouncementRecord announcementRecord = new AnnouncementRecord()
            .anntId(DEFAULT_ANNT_ID)
            .userId(DEFAULT_USER_ID)
            .hasRead(DEFAULT_HAS_READ)
            .readTime(DEFAULT_READ_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return announcementRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnouncementRecord createUpdatedEntity() {
        AnnouncementRecord announcementRecord = new AnnouncementRecord()
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return announcementRecord;
    }

    @BeforeEach
    public void initTest() {
        announcementRecord = createEntity();
    }

    @Test
    @Transactional
    void createAnnouncementRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);
        var returnedAnnouncementRecordDTO = om.readValue(
            restAnnouncementRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnnouncementRecordDTO.class
        );

        // Validate the AnnouncementRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnnouncementRecord = announcementRecordMapper.toEntity(returnedAnnouncementRecordDTO);
        assertAnnouncementRecordUpdatableFieldsEquals(
            returnedAnnouncementRecord,
            getPersistedAnnouncementRecord(returnedAnnouncementRecord)
        );
    }

    @Test
    @Transactional
    void createAnnouncementRecordWithExistingId() throws Exception {
        // Create the AnnouncementRecord with an existing ID
        announcementRecord.setId(1L);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnouncementRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecords() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcementRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].anntId").value(hasItem(DEFAULT_ANNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hasRead").value(hasItem(DEFAULT_HAS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].readTime").value(hasItem(sameInstant(DEFAULT_READ_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get the announcementRecord
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, announcementRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(announcementRecord.getId().intValue()))
            .andExpect(jsonPath("$.anntId").value(DEFAULT_ANNT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.hasRead").value(DEFAULT_HAS_READ.booleanValue()))
            .andExpect(jsonPath("$.readTime").value(sameInstant(DEFAULT_READ_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAnnouncementRecordsByIdFiltering() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        Long id = announcementRecord.getId();

        defaultAnnouncementRecordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAnnouncementRecordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAnnouncementRecordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId equals to
        defaultAnnouncementRecordFiltering("anntId.equals=" + DEFAULT_ANNT_ID, "anntId.equals=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId in
        defaultAnnouncementRecordFiltering("anntId.in=" + DEFAULT_ANNT_ID + "," + UPDATED_ANNT_ID, "anntId.in=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId is not null
        defaultAnnouncementRecordFiltering("anntId.specified=true", "anntId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId is greater than or equal to
        defaultAnnouncementRecordFiltering("anntId.greaterThanOrEqual=" + DEFAULT_ANNT_ID, "anntId.greaterThanOrEqual=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId is less than or equal to
        defaultAnnouncementRecordFiltering("anntId.lessThanOrEqual=" + DEFAULT_ANNT_ID, "anntId.lessThanOrEqual=" + SMALLER_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId is less than
        defaultAnnouncementRecordFiltering("anntId.lessThan=" + UPDATED_ANNT_ID, "anntId.lessThan=" + DEFAULT_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where anntId is greater than
        defaultAnnouncementRecordFiltering("anntId.greaterThan=" + SMALLER_ANNT_ID, "anntId.greaterThan=" + DEFAULT_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId equals to
        defaultAnnouncementRecordFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId in
        defaultAnnouncementRecordFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId is not null
        defaultAnnouncementRecordFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId is greater than or equal to
        defaultAnnouncementRecordFiltering("userId.greaterThanOrEqual=" + DEFAULT_USER_ID, "userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId is less than or equal to
        defaultAnnouncementRecordFiltering("userId.lessThanOrEqual=" + DEFAULT_USER_ID, "userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId is less than
        defaultAnnouncementRecordFiltering("userId.lessThan=" + UPDATED_USER_ID, "userId.lessThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where userId is greater than
        defaultAnnouncementRecordFiltering("userId.greaterThan=" + SMALLER_USER_ID, "userId.greaterThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where hasRead equals to
        defaultAnnouncementRecordFiltering("hasRead.equals=" + DEFAULT_HAS_READ, "hasRead.equals=" + UPDATED_HAS_READ);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where hasRead in
        defaultAnnouncementRecordFiltering("hasRead.in=" + DEFAULT_HAS_READ + "," + UPDATED_HAS_READ, "hasRead.in=" + UPDATED_HAS_READ);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where hasRead is not null
        defaultAnnouncementRecordFiltering("hasRead.specified=true", "hasRead.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime equals to
        defaultAnnouncementRecordFiltering("readTime.equals=" + DEFAULT_READ_TIME, "readTime.equals=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime in
        defaultAnnouncementRecordFiltering(
            "readTime.in=" + DEFAULT_READ_TIME + "," + UPDATED_READ_TIME,
            "readTime.in=" + UPDATED_READ_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime is not null
        defaultAnnouncementRecordFiltering("readTime.specified=true", "readTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime is greater than or equal to
        defaultAnnouncementRecordFiltering(
            "readTime.greaterThanOrEqual=" + DEFAULT_READ_TIME,
            "readTime.greaterThanOrEqual=" + UPDATED_READ_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime is less than or equal to
        defaultAnnouncementRecordFiltering(
            "readTime.lessThanOrEqual=" + DEFAULT_READ_TIME,
            "readTime.lessThanOrEqual=" + SMALLER_READ_TIME
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime is less than
        defaultAnnouncementRecordFiltering("readTime.lessThan=" + UPDATED_READ_TIME, "readTime.lessThan=" + DEFAULT_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where readTime is greater than
        defaultAnnouncementRecordFiltering("readTime.greaterThan=" + SMALLER_READ_TIME, "readTime.greaterThan=" + DEFAULT_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy equals to
        defaultAnnouncementRecordFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy in
        defaultAnnouncementRecordFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy is not null
        defaultAnnouncementRecordFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy is greater than or equal to
        defaultAnnouncementRecordFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy is less than or equal to
        defaultAnnouncementRecordFiltering(
            "createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy is less than
        defaultAnnouncementRecordFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdBy is greater than
        defaultAnnouncementRecordFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdDate equals to
        defaultAnnouncementRecordFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdDate in
        defaultAnnouncementRecordFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where createdDate is not null
        defaultAnnouncementRecordFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy equals to
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy in
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy is not null
        defaultAnnouncementRecordFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy is greater than or equal to
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy is less than or equal to
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy is less than
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedBy is greater than
        defaultAnnouncementRecordFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedDate equals to
        defaultAnnouncementRecordFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedDate in
        defaultAnnouncementRecordFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        // Get all the announcementRecordList where lastModifiedDate is not null
        defaultAnnouncementRecordFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultAnnouncementRecordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAnnouncementRecordShouldBeFound(shouldBeFound);
        defaultAnnouncementRecordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnouncementRecordShouldBeFound(String filter) throws Exception {
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcementRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].anntId").value(hasItem(DEFAULT_ANNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hasRead").value(hasItem(DEFAULT_HAS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].readTime").value(hasItem(sameInstant(DEFAULT_READ_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnouncementRecordShouldNotBeFound(String filter) throws Exception {
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnnouncementRecord() throws Exception {
        // Get the announcementRecord
        restAnnouncementRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcementRecord
        AnnouncementRecord updatedAnnouncementRecord = announcementRecordRepository.findById(announcementRecord.getId()).orElseThrow();
        updatedAnnouncementRecord
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(updatedAnnouncementRecord);

        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnnouncementRecordToMatchAllProperties(updatedAnnouncementRecord);
    }

    @Test
    @Transactional
    void putNonExistingAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(announcementRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnouncementRecordWithPatch() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcementRecord using partial update
        AnnouncementRecord partialUpdatedAnnouncementRecord = new AnnouncementRecord();
        partialUpdatedAnnouncementRecord.setId(announcementRecord.getId());

        partialUpdatedAnnouncementRecord
            .anntId(UPDATED_ANNT_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncementRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnouncementRecord))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnouncementRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAnnouncementRecord, announcementRecord),
            getPersistedAnnouncementRecord(announcementRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateAnnouncementRecordWithPatch() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the announcementRecord using partial update
        AnnouncementRecord partialUpdatedAnnouncementRecord = new AnnouncementRecord();
        partialUpdatedAnnouncementRecord.setId(announcementRecord.getId());

        partialUpdatedAnnouncementRecord
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncementRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnouncementRecord))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnouncementRecordUpdatableFieldsEquals(
            partialUpdatedAnnouncementRecord,
            getPersistedAnnouncementRecord(partialUpdatedAnnouncementRecord)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnouncementRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        announcementRecord.setId(longCount.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(announcementRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnouncementRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.save(announcementRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the announcementRecord
        restAnnouncementRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, announcementRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return announcementRecordRepository.selectCount(null);
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

    protected AnnouncementRecord getPersistedAnnouncementRecord(AnnouncementRecord announcementRecord) {
        return announcementRecordRepository.findById(announcementRecord.getId()).orElseThrow();
    }

    protected void assertPersistedAnnouncementRecordToMatchAllProperties(AnnouncementRecord expectedAnnouncementRecord) {
        assertAnnouncementRecordAllPropertiesEquals(expectedAnnouncementRecord, getPersistedAnnouncementRecord(expectedAnnouncementRecord));
    }

    protected void assertPersistedAnnouncementRecordToMatchUpdatableProperties(AnnouncementRecord expectedAnnouncementRecord) {
        assertAnnouncementRecordAllUpdatablePropertiesEquals(
            expectedAnnouncementRecord,
            getPersistedAnnouncementRecord(expectedAnnouncementRecord)
        );
    }
}
