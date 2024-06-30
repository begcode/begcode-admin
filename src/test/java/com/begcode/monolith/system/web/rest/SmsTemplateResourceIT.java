package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.SmsTemplateAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.repository.SmsTemplateRepository;
import com.begcode.monolith.system.service.SmsTemplateService;
import com.begcode.monolith.system.service.dto.SmsTemplateDTO;
import com.begcode.monolith.system.service.mapper.SmsTemplateMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SmsTemplateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class SmsTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final MessageSendType DEFAULT_SEND_TYPE = MessageSendType.EMAIL;
    private static final MessageSendType UPDATED_SEND_TYPE = MessageSendType.SMS;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_TEST_JSON = "AAAAAAAAAA";
    private static final String UPDATED_TEST_JSON = "BBBBBBBBBB";

    private static final SmsTemplateType DEFAULT_TYPE = SmsTemplateType.MESSAGE;
    private static final SmsTemplateType UPDATED_TYPE = SmsTemplateType.VERIFICATION;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

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

    private static final String ENTITY_API_URL = "/api/sms-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Mock
    private SmsTemplateRepository smsTemplateRepositoryMock;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Mock
    private SmsTemplateService smsTemplateServiceMock;

    @Autowired
    private MockMvc restSmsTemplateMockMvc;

    private SmsTemplate smsTemplate;

    private SmsTemplate insertedSmsTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsTemplate createEntity() {
        SmsTemplate smsTemplate = new SmsTemplate()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .sendType(DEFAULT_SEND_TYPE)
            .content(DEFAULT_CONTENT)
            .testJson(DEFAULT_TEST_JSON)
            .type(DEFAULT_TYPE)
            .remark(DEFAULT_REMARK)
            .enabled(DEFAULT_ENABLED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return smsTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsTemplate createUpdatedEntity() {
        SmsTemplate smsTemplate = new SmsTemplate()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .sendType(UPDATED_SEND_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return smsTemplate;
    }

    @BeforeEach
    public void initTest() {
        smsTemplate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSmsTemplate != null) {
            smsTemplateRepository.deleteById(insertedSmsTemplate.getId());
            insertedSmsTemplate = null;
        }
    }

    @Test
    @Transactional
    void createSmsTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);
        var returnedSmsTemplateDTO = om.readValue(
            restSmsTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SmsTemplateDTO.class
        );

        // Validate the SmsTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSmsTemplate = smsTemplateMapper.toEntity(returnedSmsTemplateDTO);
        assertSmsTemplateUpdatableFieldsEquals(returnedSmsTemplate, getPersistedSmsTemplate(returnedSmsTemplate));

        insertedSmsTemplate = returnedSmsTemplate;
    }

    @Test
    @Transactional
    void createSmsTemplateWithExistingId() throws Exception {
        // Create the SmsTemplate with an existing ID
        smsTemplate.setId(1L);
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSmsTemplates() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].sendType").value(hasItem(DEFAULT_SEND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].testJson").value(hasItem(DEFAULT_TEST_JSON)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSmsTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(smsTemplateServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restSmsTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(smsTemplateServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSmsTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(smsTemplateServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restSmsTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(smsTemplateRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getSmsTemplate() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get the smsTemplate
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, smsTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.sendType").value(DEFAULT_SEND_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.testJson").value(DEFAULT_TEST_JSON))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSmsTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        Long id = smsTemplate.getId();

        defaultSmsTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSmsTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSmsTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where name equals to
        defaultSmsTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where name in
        defaultSmsTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where name is not null
        defaultSmsTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where name contains
        defaultSmsTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where name does not contain
        defaultSmsTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where code equals to
        defaultSmsTemplateFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where code in
        defaultSmsTemplateFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where code is not null
        defaultSmsTemplateFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where code contains
        defaultSmsTemplateFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where code does not contain
        defaultSmsTemplateFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesBySendTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where sendType equals to
        defaultSmsTemplateFiltering("sendType.equals=" + DEFAULT_SEND_TYPE, "sendType.equals=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesBySendTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where sendType in
        defaultSmsTemplateFiltering("sendType.in=" + DEFAULT_SEND_TYPE + "," + UPDATED_SEND_TYPE, "sendType.in=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesBySendTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where sendType is not null
        defaultSmsTemplateFiltering("sendType.specified=true", "sendType.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where content equals to
        defaultSmsTemplateFiltering("content.equals=" + DEFAULT_CONTENT, "content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where content in
        defaultSmsTemplateFiltering("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT, "content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where content is not null
        defaultSmsTemplateFiltering("content.specified=true", "content.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where content contains
        defaultSmsTemplateFiltering("content.contains=" + DEFAULT_CONTENT, "content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where content does not contain
        defaultSmsTemplateFiltering("content.doesNotContain=" + UPDATED_CONTENT, "content.doesNotContain=" + DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where testJson equals to
        defaultSmsTemplateFiltering("testJson.equals=" + DEFAULT_TEST_JSON, "testJson.equals=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where testJson in
        defaultSmsTemplateFiltering("testJson.in=" + DEFAULT_TEST_JSON + "," + UPDATED_TEST_JSON, "testJson.in=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where testJson is not null
        defaultSmsTemplateFiltering("testJson.specified=true", "testJson.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where testJson contains
        defaultSmsTemplateFiltering("testJson.contains=" + DEFAULT_TEST_JSON, "testJson.contains=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where testJson does not contain
        defaultSmsTemplateFiltering("testJson.doesNotContain=" + UPDATED_TEST_JSON, "testJson.doesNotContain=" + DEFAULT_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where type equals to
        defaultSmsTemplateFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where type in
        defaultSmsTemplateFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where type is not null
        defaultSmsTemplateFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where remark equals to
        defaultSmsTemplateFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where remark in
        defaultSmsTemplateFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where remark is not null
        defaultSmsTemplateFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemarkContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where remark contains
        defaultSmsTemplateFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where remark does not contain
        defaultSmsTemplateFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where enabled equals to
        defaultSmsTemplateFiltering("enabled.equals=" + DEFAULT_ENABLED, "enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where enabled in
        defaultSmsTemplateFiltering("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED, "enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where enabled is not null
        defaultSmsTemplateFiltering("enabled.specified=true", "enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy equals to
        defaultSmsTemplateFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy in
        defaultSmsTemplateFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy is not null
        defaultSmsTemplateFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy is greater than or equal to
        defaultSmsTemplateFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy is less than or equal to
        defaultSmsTemplateFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy is less than
        defaultSmsTemplateFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdBy is greater than
        defaultSmsTemplateFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdDate equals to
        defaultSmsTemplateFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdDate in
        defaultSmsTemplateFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where createdDate is not null
        defaultSmsTemplateFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy equals to
        defaultSmsTemplateFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy in
        defaultSmsTemplateFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy is not null
        defaultSmsTemplateFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy is greater than or equal to
        defaultSmsTemplateFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy is less than or equal to
        defaultSmsTemplateFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy is less than
        defaultSmsTemplateFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedBy is greater than
        defaultSmsTemplateFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedDate equals to
        defaultSmsTemplateFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedDate in
        defaultSmsTemplateFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        // Get all the smsTemplateList where lastModifiedDate is not null
        defaultSmsTemplateFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesBySupplierIsEqualToSomething() throws Exception {
        SmsSupplier supplier = SmsSupplierResourceIT.createEntity();
        smsTemplate.setSupplier(supplier);
        smsTemplateRepository.insert(smsTemplate);
        Long supplierId = supplier.getId();
        // Get all the smsTemplateList where supplier equals to supplierId
        defaultSmsTemplateShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the smsTemplateList where supplier equals to (supplierId + 1)
        defaultSmsTemplateShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    private void defaultSmsTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSmsTemplateShouldBeFound(shouldBeFound);
        defaultSmsTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsTemplateShouldBeFound(String filter) throws Exception {
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].sendType").value(hasItem(DEFAULT_SEND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].testJson").value(hasItem(DEFAULT_TEST_JSON)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsTemplateShouldNotBeFound(String filter) throws Exception {
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmsTemplate() throws Exception {
        // Get the smsTemplate
        restSmsTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSmsTemplate() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsTemplate
        SmsTemplate updatedSmsTemplate = smsTemplateRepository.findById(smsTemplate.getId()).orElseThrow();
        updatedSmsTemplate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .sendType(UPDATED_SEND_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(updatedSmsTemplate);

        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSmsTemplateToMatchAllProperties(updatedSmsTemplate);
    }

    @Test
    @Transactional
    void putNonExistingSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsTemplate using partial update
        SmsTemplate partialUpdatedSmsTemplate = new SmsTemplate();
        partialUpdatedSmsTemplate.setId(smsTemplate.getId());

        partialUpdatedSmsTemplate.type(UPDATED_TYPE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSmsTemplate, smsTemplate),
            getPersistedSmsTemplate(smsTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateSmsTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsTemplate using partial update
        SmsTemplate partialUpdatedSmsTemplate = new SmsTemplate();
        partialUpdatedSmsTemplate.setId(smsTemplate.getId());

        partialUpdatedSmsTemplate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .sendType(UPDATED_SEND_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .type(UPDATED_TYPE)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsTemplateUpdatableFieldsEquals(partialUpdatedSmsTemplate, getPersistedSmsTemplate(partialUpdatedSmsTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsTemplate.setId(longCount.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(smsTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsTemplate() throws Exception {
        // Initialize the database
        insertedSmsTemplate = smsTemplateRepository.saveAndGet(smsTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the smsTemplate
        restSmsTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return smsTemplateRepository.selectCount(null);
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

    protected SmsTemplate getPersistedSmsTemplate(SmsTemplate smsTemplate) {
        return smsTemplateRepository.findById(smsTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedSmsTemplateToMatchAllProperties(SmsTemplate expectedSmsTemplate) {
        assertSmsTemplateAllPropertiesEquals(expectedSmsTemplate, getPersistedSmsTemplate(expectedSmsTemplate));
    }

    protected void assertPersistedSmsTemplateToMatchUpdatableProperties(SmsTemplate expectedSmsTemplate) {
        assertSmsTemplateAllUpdatablePropertiesEquals(expectedSmsTemplate, getPersistedSmsTemplate(expectedSmsTemplate));
    }
}
