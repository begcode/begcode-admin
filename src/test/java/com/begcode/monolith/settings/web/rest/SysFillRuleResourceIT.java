package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.SysFillRuleAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.ResetFrequency;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
import com.begcode.monolith.settings.service.mapper.SysFillRuleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SysFillRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class SysFillRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final ResetFrequency DEFAULT_RESET_FREQUENCY = ResetFrequency.NONE;
    private static final ResetFrequency UPDATED_RESET_FREQUENCY = ResetFrequency.DAILY;

    private static final Long DEFAULT_SEQ_VALUE = 1L;
    private static final Long UPDATED_SEQ_VALUE = 2L;
    private static final Long SMALLER_SEQ_VALUE = 1L - 1L;

    private static final String DEFAULT_FILL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FILL_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_IMPL_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_IMPL_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RESET_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESET_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESET_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_RESET_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESET_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESET_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_RESET_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESET_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESET_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/sys-fill-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysFillRuleRepository sysFillRuleRepository;

    @Autowired
    private SysFillRuleMapper sysFillRuleMapper;

    @Autowired
    private MockMvc restSysFillRuleMockMvc;

    private SysFillRule sysFillRule;

    private SysFillRule insertedSysFillRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysFillRule createEntity() {
        SysFillRule sysFillRule = new SysFillRule()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .desc(DEFAULT_DESC)
            .enabled(DEFAULT_ENABLED)
            .resetFrequency(DEFAULT_RESET_FREQUENCY)
            .seqValue(DEFAULT_SEQ_VALUE)
            .fillValue(DEFAULT_FILL_VALUE)
            .implClass(DEFAULT_IMPL_CLASS)
            .params(DEFAULT_PARAMS)
            .resetStartTime(DEFAULT_RESET_START_TIME)
            .resetEndTime(DEFAULT_RESET_END_TIME)
            .resetTime(DEFAULT_RESET_TIME);
        return sysFillRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysFillRule createUpdatedEntity() {
        SysFillRule sysFillRule = new SysFillRule()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .desc(UPDATED_DESC)
            .enabled(UPDATED_ENABLED)
            .resetFrequency(UPDATED_RESET_FREQUENCY)
            .seqValue(UPDATED_SEQ_VALUE)
            .fillValue(UPDATED_FILL_VALUE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .resetStartTime(UPDATED_RESET_START_TIME)
            .resetEndTime(UPDATED_RESET_END_TIME)
            .resetTime(UPDATED_RESET_TIME);
        return sysFillRule;
    }

    @BeforeEach
    public void initTest() {
        sysFillRule = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSysFillRule != null) {
            sysFillRuleRepository.deleteById(insertedSysFillRule.getId());
            insertedSysFillRule = null;
        }
    }

    @Test
    @Transactional
    void createSysFillRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);
        var returnedSysFillRuleDTO = om.readValue(
            restSysFillRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysFillRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SysFillRuleDTO.class
        );

        // Validate the SysFillRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSysFillRule = sysFillRuleMapper.toEntity(returnedSysFillRuleDTO);
        assertSysFillRuleUpdatableFieldsEquals(returnedSysFillRule, getPersistedSysFillRule(returnedSysFillRule));

        insertedSysFillRule = returnedSysFillRule;
    }

    @Test
    @Transactional
    void createSysFillRuleWithExistingId() throws Exception {
        // Create the SysFillRule with an existing ID
        sysFillRule.setId(1L);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysFillRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysFillRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysFillRules() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysFillRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].resetFrequency").value(hasItem(DEFAULT_RESET_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].seqValue").value(hasItem(DEFAULT_SEQ_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].fillValue").value(hasItem(DEFAULT_FILL_VALUE)))
            .andExpect(jsonPath("$.[*].implClass").value(hasItem(DEFAULT_IMPL_CLASS)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].resetStartTime").value(hasItem(sameInstant(DEFAULT_RESET_START_TIME))))
            .andExpect(jsonPath("$.[*].resetEndTime").value(hasItem(sameInstant(DEFAULT_RESET_END_TIME))))
            .andExpect(jsonPath("$.[*].resetTime").value(hasItem(sameInstant(DEFAULT_RESET_TIME))));
    }

    @Test
    @Transactional
    void getSysFillRule() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get the sysFillRule
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, sysFillRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysFillRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.resetFrequency").value(DEFAULT_RESET_FREQUENCY.toString()))
            .andExpect(jsonPath("$.seqValue").value(DEFAULT_SEQ_VALUE.intValue()))
            .andExpect(jsonPath("$.fillValue").value(DEFAULT_FILL_VALUE))
            .andExpect(jsonPath("$.implClass").value(DEFAULT_IMPL_CLASS))
            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS))
            .andExpect(jsonPath("$.resetStartTime").value(sameInstant(DEFAULT_RESET_START_TIME)))
            .andExpect(jsonPath("$.resetEndTime").value(sameInstant(DEFAULT_RESET_END_TIME)))
            .andExpect(jsonPath("$.resetTime").value(sameInstant(DEFAULT_RESET_TIME)));
    }

    @Test
    @Transactional
    void getSysFillRulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        Long id = sysFillRule.getId();

        defaultSysFillRuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSysFillRuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSysFillRuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where name equals to
        defaultSysFillRuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where name in
        defaultSysFillRuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where name is not null
        defaultSysFillRuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where name contains
        defaultSysFillRuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where name does not contain
        defaultSysFillRuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where code equals to
        defaultSysFillRuleFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where code in
        defaultSysFillRuleFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where code is not null
        defaultSysFillRuleFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where code contains
        defaultSysFillRuleFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where code does not contain
        defaultSysFillRuleFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where desc equals to
        defaultSysFillRuleFiltering("desc.equals=" + DEFAULT_DESC, "desc.equals=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where desc in
        defaultSysFillRuleFiltering("desc.in=" + DEFAULT_DESC + "," + UPDATED_DESC, "desc.in=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where desc is not null
        defaultSysFillRuleFiltering("desc.specified=true", "desc.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where desc contains
        defaultSysFillRuleFiltering("desc.contains=" + DEFAULT_DESC, "desc.contains=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where desc does not contain
        defaultSysFillRuleFiltering("desc.doesNotContain=" + UPDATED_DESC, "desc.doesNotContain=" + DEFAULT_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where enabled equals to
        defaultSysFillRuleFiltering("enabled.equals=" + DEFAULT_ENABLED, "enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where enabled in
        defaultSysFillRuleFiltering("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED, "enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where enabled is not null
        defaultSysFillRuleFiltering("enabled.specified=true", "enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency equals to
        defaultSysFillRuleFiltering("resetFrequency.equals=" + DEFAULT_RESET_FREQUENCY, "resetFrequency.equals=" + UPDATED_RESET_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency in
        defaultSysFillRuleFiltering(
            "resetFrequency.in=" + DEFAULT_RESET_FREQUENCY + "," + UPDATED_RESET_FREQUENCY,
            "resetFrequency.in=" + UPDATED_RESET_FREQUENCY
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency is not null
        defaultSysFillRuleFiltering("resetFrequency.specified=true", "resetFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue equals to
        defaultSysFillRuleFiltering("seqValue.equals=" + DEFAULT_SEQ_VALUE, "seqValue.equals=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue in
        defaultSysFillRuleFiltering("seqValue.in=" + DEFAULT_SEQ_VALUE + "," + UPDATED_SEQ_VALUE, "seqValue.in=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue is not null
        defaultSysFillRuleFiltering("seqValue.specified=true", "seqValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue is greater than or equal to
        defaultSysFillRuleFiltering("seqValue.greaterThanOrEqual=" + DEFAULT_SEQ_VALUE, "seqValue.greaterThanOrEqual=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue is less than or equal to
        defaultSysFillRuleFiltering("seqValue.lessThanOrEqual=" + DEFAULT_SEQ_VALUE, "seqValue.lessThanOrEqual=" + SMALLER_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue is less than
        defaultSysFillRuleFiltering("seqValue.lessThan=" + UPDATED_SEQ_VALUE, "seqValue.lessThan=" + DEFAULT_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where seqValue is greater than
        defaultSysFillRuleFiltering("seqValue.greaterThan=" + SMALLER_SEQ_VALUE, "seqValue.greaterThan=" + DEFAULT_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where fillValue equals to
        defaultSysFillRuleFiltering("fillValue.equals=" + DEFAULT_FILL_VALUE, "fillValue.equals=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where fillValue in
        defaultSysFillRuleFiltering("fillValue.in=" + DEFAULT_FILL_VALUE + "," + UPDATED_FILL_VALUE, "fillValue.in=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where fillValue is not null
        defaultSysFillRuleFiltering("fillValue.specified=true", "fillValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where fillValue contains
        defaultSysFillRuleFiltering("fillValue.contains=" + DEFAULT_FILL_VALUE, "fillValue.contains=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where fillValue does not contain
        defaultSysFillRuleFiltering("fillValue.doesNotContain=" + UPDATED_FILL_VALUE, "fillValue.doesNotContain=" + DEFAULT_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where implClass equals to
        defaultSysFillRuleFiltering("implClass.equals=" + DEFAULT_IMPL_CLASS, "implClass.equals=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where implClass in
        defaultSysFillRuleFiltering("implClass.in=" + DEFAULT_IMPL_CLASS + "," + UPDATED_IMPL_CLASS, "implClass.in=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where implClass is not null
        defaultSysFillRuleFiltering("implClass.specified=true", "implClass.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where implClass contains
        defaultSysFillRuleFiltering("implClass.contains=" + DEFAULT_IMPL_CLASS, "implClass.contains=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where implClass does not contain
        defaultSysFillRuleFiltering("implClass.doesNotContain=" + UPDATED_IMPL_CLASS, "implClass.doesNotContain=" + DEFAULT_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where params equals to
        defaultSysFillRuleFiltering("params.equals=" + DEFAULT_PARAMS, "params.equals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where params in
        defaultSysFillRuleFiltering("params.in=" + DEFAULT_PARAMS + "," + UPDATED_PARAMS, "params.in=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where params is not null
        defaultSysFillRuleFiltering("params.specified=true", "params.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where params contains
        defaultSysFillRuleFiltering("params.contains=" + DEFAULT_PARAMS, "params.contains=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where params does not contain
        defaultSysFillRuleFiltering("params.doesNotContain=" + UPDATED_PARAMS, "params.doesNotContain=" + DEFAULT_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime equals to
        defaultSysFillRuleFiltering(
            "resetStartTime.equals=" + DEFAULT_RESET_START_TIME,
            "resetStartTime.equals=" + UPDATED_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime in
        defaultSysFillRuleFiltering(
            "resetStartTime.in=" + DEFAULT_RESET_START_TIME + "," + UPDATED_RESET_START_TIME,
            "resetStartTime.in=" + UPDATED_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is not null
        defaultSysFillRuleFiltering("resetStartTime.specified=true", "resetStartTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is greater than or equal to
        defaultSysFillRuleFiltering(
            "resetStartTime.greaterThanOrEqual=" + DEFAULT_RESET_START_TIME,
            "resetStartTime.greaterThanOrEqual=" + UPDATED_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is less than or equal to
        defaultSysFillRuleFiltering(
            "resetStartTime.lessThanOrEqual=" + DEFAULT_RESET_START_TIME,
            "resetStartTime.lessThanOrEqual=" + SMALLER_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is less than
        defaultSysFillRuleFiltering(
            "resetStartTime.lessThan=" + UPDATED_RESET_START_TIME,
            "resetStartTime.lessThan=" + DEFAULT_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is greater than
        defaultSysFillRuleFiltering(
            "resetStartTime.greaterThan=" + SMALLER_RESET_START_TIME,
            "resetStartTime.greaterThan=" + DEFAULT_RESET_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime equals to
        defaultSysFillRuleFiltering("resetEndTime.equals=" + DEFAULT_RESET_END_TIME, "resetEndTime.equals=" + UPDATED_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime in
        defaultSysFillRuleFiltering(
            "resetEndTime.in=" + DEFAULT_RESET_END_TIME + "," + UPDATED_RESET_END_TIME,
            "resetEndTime.in=" + UPDATED_RESET_END_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is not null
        defaultSysFillRuleFiltering("resetEndTime.specified=true", "resetEndTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is greater than or equal to
        defaultSysFillRuleFiltering(
            "resetEndTime.greaterThanOrEqual=" + DEFAULT_RESET_END_TIME,
            "resetEndTime.greaterThanOrEqual=" + UPDATED_RESET_END_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is less than or equal to
        defaultSysFillRuleFiltering(
            "resetEndTime.lessThanOrEqual=" + DEFAULT_RESET_END_TIME,
            "resetEndTime.lessThanOrEqual=" + SMALLER_RESET_END_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is less than
        defaultSysFillRuleFiltering("resetEndTime.lessThan=" + UPDATED_RESET_END_TIME, "resetEndTime.lessThan=" + DEFAULT_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is greater than
        defaultSysFillRuleFiltering(
            "resetEndTime.greaterThan=" + SMALLER_RESET_END_TIME,
            "resetEndTime.greaterThan=" + DEFAULT_RESET_END_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime equals to
        defaultSysFillRuleFiltering("resetTime.equals=" + DEFAULT_RESET_TIME, "resetTime.equals=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime in
        defaultSysFillRuleFiltering("resetTime.in=" + DEFAULT_RESET_TIME + "," + UPDATED_RESET_TIME, "resetTime.in=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime is not null
        defaultSysFillRuleFiltering("resetTime.specified=true", "resetTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime is greater than or equal to
        defaultSysFillRuleFiltering(
            "resetTime.greaterThanOrEqual=" + DEFAULT_RESET_TIME,
            "resetTime.greaterThanOrEqual=" + UPDATED_RESET_TIME
        );
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime is less than or equal to
        defaultSysFillRuleFiltering("resetTime.lessThanOrEqual=" + DEFAULT_RESET_TIME, "resetTime.lessThanOrEqual=" + SMALLER_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime is less than
        defaultSysFillRuleFiltering("resetTime.lessThan=" + UPDATED_RESET_TIME, "resetTime.lessThan=" + DEFAULT_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        // Get all the sysFillRuleList where resetTime is greater than
        defaultSysFillRuleFiltering("resetTime.greaterThan=" + SMALLER_RESET_TIME, "resetTime.greaterThan=" + DEFAULT_RESET_TIME);
    }

    private void defaultSysFillRuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSysFillRuleShouldBeFound(shouldBeFound);
        defaultSysFillRuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSysFillRuleShouldBeFound(String filter) throws Exception {
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysFillRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].resetFrequency").value(hasItem(DEFAULT_RESET_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].seqValue").value(hasItem(DEFAULT_SEQ_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].fillValue").value(hasItem(DEFAULT_FILL_VALUE)))
            .andExpect(jsonPath("$.[*].implClass").value(hasItem(DEFAULT_IMPL_CLASS)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].resetStartTime").value(hasItem(sameInstant(DEFAULT_RESET_START_TIME))))
            .andExpect(jsonPath("$.[*].resetEndTime").value(hasItem(sameInstant(DEFAULT_RESET_END_TIME))))
            .andExpect(jsonPath("$.[*].resetTime").value(hasItem(sameInstant(DEFAULT_RESET_TIME))));

        // Check, that the count call also returns 1
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSysFillRuleShouldNotBeFound(String filter) throws Exception {
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSysFillRule() throws Exception {
        // Get the sysFillRule
        restSysFillRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSysFillRule() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysFillRule
        SysFillRule updatedSysFillRule = sysFillRuleRepository.findById(sysFillRule.getId()).orElseThrow();
        updatedSysFillRule
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .desc(UPDATED_DESC)
            .enabled(UPDATED_ENABLED)
            .resetFrequency(UPDATED_RESET_FREQUENCY)
            .seqValue(UPDATED_SEQ_VALUE)
            .fillValue(UPDATED_FILL_VALUE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .resetStartTime(UPDATED_RESET_START_TIME)
            .resetEndTime(UPDATED_RESET_END_TIME)
            .resetTime(UPDATED_RESET_TIME);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(updatedSysFillRule);

        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sysFillRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysFillRuleToMatchAllProperties(updatedSysFillRule);
    }

    @Test
    @Transactional
    void putNonExistingSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysFillRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysFillRule using partial update
        SysFillRule partialUpdatedSysFillRule = new SysFillRule();
        partialUpdatedSysFillRule.setId(sysFillRule.getId());

        partialUpdatedSysFillRule
            .code(UPDATED_CODE)
            .resetFrequency(UPDATED_RESET_FREQUENCY)
            .fillValue(UPDATED_FILL_VALUE)
            .resetStartTime(UPDATED_RESET_START_TIME)
            .resetTime(UPDATED_RESET_TIME);

        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysFillRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysFillRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSysFillRule, sysFillRule),
            getPersistedSysFillRule(sysFillRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysFillRule using partial update
        SysFillRule partialUpdatedSysFillRule = new SysFillRule();
        partialUpdatedSysFillRule.setId(sysFillRule.getId());

        partialUpdatedSysFillRule
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .desc(UPDATED_DESC)
            .enabled(UPDATED_ENABLED)
            .resetFrequency(UPDATED_RESET_FREQUENCY)
            .seqValue(UPDATED_SEQ_VALUE)
            .fillValue(UPDATED_FILL_VALUE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .resetStartTime(UPDATED_RESET_START_TIME)
            .resetEndTime(UPDATED_RESET_END_TIME)
            .resetTime(UPDATED_RESET_TIME);

        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysFillRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysFillRuleUpdatableFieldsEquals(partialUpdatedSysFillRule, getPersistedSysFillRule(partialUpdatedSysFillRule));
    }

    @Test
    @Transactional
    void patchNonExistingSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysFillRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sysFillRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysFillRule() throws Exception {
        // Initialize the database
        insertedSysFillRule = sysFillRuleRepository.saveAndGet(sysFillRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sysFillRule
        restSysFillRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysFillRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sysFillRuleRepository.selectCount(null);
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

    protected SysFillRule getPersistedSysFillRule(SysFillRule sysFillRule) {
        return sysFillRuleRepository.findById(sysFillRule.getId()).orElseThrow();
    }

    protected void assertPersistedSysFillRuleToMatchAllProperties(SysFillRule expectedSysFillRule) {
        assertSysFillRuleAllPropertiesEquals(expectedSysFillRule, getPersistedSysFillRule(expectedSysFillRule));
    }

    protected void assertPersistedSysFillRuleToMatchUpdatableProperties(SysFillRule expectedSysFillRule) {
        assertSysFillRuleAllUpdatablePropertiesEquals(expectedSysFillRule, getPersistedSysFillRule(expectedSysFillRule));
    }
}
