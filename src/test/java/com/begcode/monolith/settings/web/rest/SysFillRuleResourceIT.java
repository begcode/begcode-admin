package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.ResetFrequency;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.repository.SysFillRuleRepository;
import com.begcode.monolith.settings.service.dto.SysFillRuleDTO;
import com.begcode.monolith.settings.service.mapper.SysFillRuleMapper;
import com.begcode.monolith.web.rest.TestUtil;
import com.begcode.monolith.web.rest.TestUtil;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SysFillRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysFillRuleRepository sysFillRuleRepository;

    @Autowired
    private SysFillRuleMapper sysFillRuleMapper;

    @Autowired
    private MockMvc restSysFillRuleMockMvc;

    private SysFillRule sysFillRule;

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

    @Test
    @Transactional
    void createSysFillRule() throws Exception {
        int databaseSizeBeforeCreate = sysFillRuleRepository.findAll().size();
        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);
        restSysFillRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeCreate + 1);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSysFillRule.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testSysFillRule.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSysFillRule.getResetFrequency()).isEqualTo(DEFAULT_RESET_FREQUENCY);
        assertThat(testSysFillRule.getSeqValue()).isEqualTo(DEFAULT_SEQ_VALUE);
        assertThat(testSysFillRule.getFillValue()).isEqualTo(DEFAULT_FILL_VALUE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(DEFAULT_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSysFillRule.getResetStartTime()).isEqualTo(DEFAULT_RESET_START_TIME);
        assertThat(testSysFillRule.getResetEndTime()).isEqualTo(DEFAULT_RESET_END_TIME);
        assertThat(testSysFillRule.getResetTime()).isEqualTo(DEFAULT_RESET_TIME);
    }

    @Test
    @Transactional
    void createSysFillRuleWithExistingId() throws Exception {
        // Create the SysFillRule with an existing ID
        sysFillRule.setId(1L);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        int databaseSizeBeforeCreate = sysFillRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysFillRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysFillRules() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

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
        sysFillRuleRepository.save(sysFillRule);

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
        sysFillRuleRepository.save(sysFillRule);

        Long id = sysFillRule.getId();

        defaultSysFillRuleShouldBeFound("id.equals=" + id);
        defaultSysFillRuleShouldNotBeFound("id.notEquals=" + id);

        defaultSysFillRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSysFillRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultSysFillRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSysFillRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where name equals to DEFAULT_NAME
        defaultSysFillRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name equals to UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSysFillRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sysFillRuleList where name equals to UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where name is not null
        defaultSysFillRuleShouldBeFound("name.specified=true");

        // Get all the sysFillRuleList where name is null
        defaultSysFillRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where name contains DEFAULT_NAME
        defaultSysFillRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name contains UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where name does not contain DEFAULT_NAME
        defaultSysFillRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name does not contain UPDATED_NAME
        defaultSysFillRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where code equals to DEFAULT_CODE
        defaultSysFillRuleShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code equals to UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSysFillRuleShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sysFillRuleList where code equals to UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where code is not null
        defaultSysFillRuleShouldBeFound("code.specified=true");

        // Get all the sysFillRuleList where code is null
        defaultSysFillRuleShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where code contains DEFAULT_CODE
        defaultSysFillRuleShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code contains UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where code does not contain DEFAULT_CODE
        defaultSysFillRuleShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code does not contain UPDATED_CODE
        defaultSysFillRuleShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where desc equals to DEFAULT_DESC
        defaultSysFillRuleShouldBeFound("desc.equals=" + DEFAULT_DESC);

        // Get all the sysFillRuleList where desc equals to UPDATED_DESC
        defaultSysFillRuleShouldNotBeFound("desc.equals=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where desc in DEFAULT_DESC or UPDATED_DESC
        defaultSysFillRuleShouldBeFound("desc.in=" + DEFAULT_DESC + "," + UPDATED_DESC);

        // Get all the sysFillRuleList where desc equals to UPDATED_DESC
        defaultSysFillRuleShouldNotBeFound("desc.in=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where desc is not null
        defaultSysFillRuleShouldBeFound("desc.specified=true");

        // Get all the sysFillRuleList where desc is null
        defaultSysFillRuleShouldNotBeFound("desc.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where desc contains DEFAULT_DESC
        defaultSysFillRuleShouldBeFound("desc.contains=" + DEFAULT_DESC);

        // Get all the sysFillRuleList where desc contains UPDATED_DESC
        defaultSysFillRuleShouldNotBeFound("desc.contains=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByDescNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where desc does not contain DEFAULT_DESC
        defaultSysFillRuleShouldNotBeFound("desc.doesNotContain=" + DEFAULT_DESC);

        // Get all the sysFillRuleList where desc does not contain UPDATED_DESC
        defaultSysFillRuleShouldBeFound("desc.doesNotContain=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where enabled equals to DEFAULT_ENABLED
        defaultSysFillRuleShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the sysFillRuleList where enabled equals to UPDATED_ENABLED
        defaultSysFillRuleShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultSysFillRuleShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the sysFillRuleList where enabled equals to UPDATED_ENABLED
        defaultSysFillRuleShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where enabled is not null
        defaultSysFillRuleShouldBeFound("enabled.specified=true");

        // Get all the sysFillRuleList where enabled is null
        defaultSysFillRuleShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency equals to DEFAULT_RESET_FREQUENCY
        defaultSysFillRuleShouldBeFound("resetFrequency.equals=" + DEFAULT_RESET_FREQUENCY);

        // Get all the sysFillRuleList where resetFrequency equals to UPDATED_RESET_FREQUENCY
        defaultSysFillRuleShouldNotBeFound("resetFrequency.equals=" + UPDATED_RESET_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency in DEFAULT_RESET_FREQUENCY or UPDATED_RESET_FREQUENCY
        defaultSysFillRuleShouldBeFound("resetFrequency.in=" + DEFAULT_RESET_FREQUENCY + "," + UPDATED_RESET_FREQUENCY);

        // Get all the sysFillRuleList where resetFrequency equals to UPDATED_RESET_FREQUENCY
        defaultSysFillRuleShouldNotBeFound("resetFrequency.in=" + UPDATED_RESET_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetFrequency is not null
        defaultSysFillRuleShouldBeFound("resetFrequency.specified=true");

        // Get all the sysFillRuleList where resetFrequency is null
        defaultSysFillRuleShouldNotBeFound("resetFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue equals to DEFAULT_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.equals=" + DEFAULT_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue equals to UPDATED_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.equals=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue in DEFAULT_SEQ_VALUE or UPDATED_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.in=" + DEFAULT_SEQ_VALUE + "," + UPDATED_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue equals to UPDATED_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.in=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue is not null
        defaultSysFillRuleShouldBeFound("seqValue.specified=true");

        // Get all the sysFillRuleList where seqValue is null
        defaultSysFillRuleShouldNotBeFound("seqValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue is greater than or equal to DEFAULT_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.greaterThanOrEqual=" + DEFAULT_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue is greater than or equal to UPDATED_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.greaterThanOrEqual=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue is less than or equal to DEFAULT_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.lessThanOrEqual=" + DEFAULT_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue is less than or equal to SMALLER_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.lessThanOrEqual=" + SMALLER_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsLessThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue is less than DEFAULT_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.lessThan=" + DEFAULT_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue is less than UPDATED_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.lessThan=" + UPDATED_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesBySeqValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where seqValue is greater than DEFAULT_SEQ_VALUE
        defaultSysFillRuleShouldNotBeFound("seqValue.greaterThan=" + DEFAULT_SEQ_VALUE);

        // Get all the sysFillRuleList where seqValue is greater than SMALLER_SEQ_VALUE
        defaultSysFillRuleShouldBeFound("seqValue.greaterThan=" + SMALLER_SEQ_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where fillValue equals to DEFAULT_FILL_VALUE
        defaultSysFillRuleShouldBeFound("fillValue.equals=" + DEFAULT_FILL_VALUE);

        // Get all the sysFillRuleList where fillValue equals to UPDATED_FILL_VALUE
        defaultSysFillRuleShouldNotBeFound("fillValue.equals=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where fillValue in DEFAULT_FILL_VALUE or UPDATED_FILL_VALUE
        defaultSysFillRuleShouldBeFound("fillValue.in=" + DEFAULT_FILL_VALUE + "," + UPDATED_FILL_VALUE);

        // Get all the sysFillRuleList where fillValue equals to UPDATED_FILL_VALUE
        defaultSysFillRuleShouldNotBeFound("fillValue.in=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where fillValue is not null
        defaultSysFillRuleShouldBeFound("fillValue.specified=true");

        // Get all the sysFillRuleList where fillValue is null
        defaultSysFillRuleShouldNotBeFound("fillValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where fillValue contains DEFAULT_FILL_VALUE
        defaultSysFillRuleShouldBeFound("fillValue.contains=" + DEFAULT_FILL_VALUE);

        // Get all the sysFillRuleList where fillValue contains UPDATED_FILL_VALUE
        defaultSysFillRuleShouldNotBeFound("fillValue.contains=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByFillValueNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where fillValue does not contain DEFAULT_FILL_VALUE
        defaultSysFillRuleShouldNotBeFound("fillValue.doesNotContain=" + DEFAULT_FILL_VALUE);

        // Get all the sysFillRuleList where fillValue does not contain UPDATED_FILL_VALUE
        defaultSysFillRuleShouldBeFound("fillValue.doesNotContain=" + UPDATED_FILL_VALUE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where implClass equals to DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.equals=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass equals to UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.equals=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where implClass in DEFAULT_IMPL_CLASS or UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.in=" + DEFAULT_IMPL_CLASS + "," + UPDATED_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass equals to UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.in=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where implClass is not null
        defaultSysFillRuleShouldBeFound("implClass.specified=true");

        // Get all the sysFillRuleList where implClass is null
        defaultSysFillRuleShouldNotBeFound("implClass.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where implClass contains DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.contains=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass contains UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.contains=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where implClass does not contain DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.doesNotContain=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass does not contain UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.doesNotContain=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where params equals to DEFAULT_PARAMS
        defaultSysFillRuleShouldBeFound("params.equals=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params equals to UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.equals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where params in DEFAULT_PARAMS or UPDATED_PARAMS
        defaultSysFillRuleShouldBeFound("params.in=" + DEFAULT_PARAMS + "," + UPDATED_PARAMS);

        // Get all the sysFillRuleList where params equals to UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.in=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where params is not null
        defaultSysFillRuleShouldBeFound("params.specified=true");

        // Get all the sysFillRuleList where params is null
        defaultSysFillRuleShouldNotBeFound("params.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where params contains DEFAULT_PARAMS
        defaultSysFillRuleShouldBeFound("params.contains=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params contains UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.contains=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where params does not contain DEFAULT_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.doesNotContain=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params does not contain UPDATED_PARAMS
        defaultSysFillRuleShouldBeFound("params.doesNotContain=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime equals to DEFAULT_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.equals=" + DEFAULT_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime equals to UPDATED_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.equals=" + UPDATED_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime in DEFAULT_RESET_START_TIME or UPDATED_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.in=" + DEFAULT_RESET_START_TIME + "," + UPDATED_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime equals to UPDATED_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.in=" + UPDATED_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is not null
        defaultSysFillRuleShouldBeFound("resetStartTime.specified=true");

        // Get all the sysFillRuleList where resetStartTime is null
        defaultSysFillRuleShouldNotBeFound("resetStartTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is greater than or equal to DEFAULT_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.greaterThanOrEqual=" + DEFAULT_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime is greater than or equal to UPDATED_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.greaterThanOrEqual=" + UPDATED_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is less than or equal to DEFAULT_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.lessThanOrEqual=" + DEFAULT_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime is less than or equal to SMALLER_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.lessThanOrEqual=" + SMALLER_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is less than DEFAULT_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.lessThan=" + DEFAULT_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime is less than UPDATED_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.lessThan=" + UPDATED_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetStartTime is greater than DEFAULT_RESET_START_TIME
        defaultSysFillRuleShouldNotBeFound("resetStartTime.greaterThan=" + DEFAULT_RESET_START_TIME);

        // Get all the sysFillRuleList where resetStartTime is greater than SMALLER_RESET_START_TIME
        defaultSysFillRuleShouldBeFound("resetStartTime.greaterThan=" + SMALLER_RESET_START_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime equals to DEFAULT_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.equals=" + DEFAULT_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime equals to UPDATED_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.equals=" + UPDATED_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime in DEFAULT_RESET_END_TIME or UPDATED_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.in=" + DEFAULT_RESET_END_TIME + "," + UPDATED_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime equals to UPDATED_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.in=" + UPDATED_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is not null
        defaultSysFillRuleShouldBeFound("resetEndTime.specified=true");

        // Get all the sysFillRuleList where resetEndTime is null
        defaultSysFillRuleShouldNotBeFound("resetEndTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is greater than or equal to DEFAULT_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.greaterThanOrEqual=" + DEFAULT_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime is greater than or equal to UPDATED_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.greaterThanOrEqual=" + UPDATED_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is less than or equal to DEFAULT_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.lessThanOrEqual=" + DEFAULT_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime is less than or equal to SMALLER_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.lessThanOrEqual=" + SMALLER_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is less than DEFAULT_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.lessThan=" + DEFAULT_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime is less than UPDATED_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.lessThan=" + UPDATED_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetEndTime is greater than DEFAULT_RESET_END_TIME
        defaultSysFillRuleShouldNotBeFound("resetEndTime.greaterThan=" + DEFAULT_RESET_END_TIME);

        // Get all the sysFillRuleList where resetEndTime is greater than SMALLER_RESET_END_TIME
        defaultSysFillRuleShouldBeFound("resetEndTime.greaterThan=" + SMALLER_RESET_END_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime equals to DEFAULT_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.equals=" + DEFAULT_RESET_TIME);

        // Get all the sysFillRuleList where resetTime equals to UPDATED_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.equals=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime in DEFAULT_RESET_TIME or UPDATED_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.in=" + DEFAULT_RESET_TIME + "," + UPDATED_RESET_TIME);

        // Get all the sysFillRuleList where resetTime equals to UPDATED_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.in=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime is not null
        defaultSysFillRuleShouldBeFound("resetTime.specified=true");

        // Get all the sysFillRuleList where resetTime is null
        defaultSysFillRuleShouldNotBeFound("resetTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime is greater than or equal to DEFAULT_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.greaterThanOrEqual=" + DEFAULT_RESET_TIME);

        // Get all the sysFillRuleList where resetTime is greater than or equal to UPDATED_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.greaterThanOrEqual=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime is less than or equal to DEFAULT_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.lessThanOrEqual=" + DEFAULT_RESET_TIME);

        // Get all the sysFillRuleList where resetTime is less than or equal to SMALLER_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.lessThanOrEqual=" + SMALLER_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime is less than DEFAULT_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.lessThan=" + DEFAULT_RESET_TIME);

        // Get all the sysFillRuleList where resetTime is less than UPDATED_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.lessThan=" + UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByResetTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        // Get all the sysFillRuleList where resetTime is greater than DEFAULT_RESET_TIME
        defaultSysFillRuleShouldNotBeFound("resetTime.greaterThan=" + DEFAULT_RESET_TIME);

        // Get all the sysFillRuleList where resetTime is greater than SMALLER_RESET_TIME
        defaultSysFillRuleShouldBeFound("resetTime.greaterThan=" + SMALLER_RESET_TIME);
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
        sysFillRuleRepository.save(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSysFillRule.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testSysFillRule.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSysFillRule.getResetFrequency()).isEqualTo(UPDATED_RESET_FREQUENCY);
        assertThat(testSysFillRule.getSeqValue()).isEqualTo(UPDATED_SEQ_VALUE);
        assertThat(testSysFillRule.getFillValue()).isEqualTo(UPDATED_FILL_VALUE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(UPDATED_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getResetStartTime()).isEqualTo(UPDATED_RESET_START_TIME);
        assertThat(testSysFillRule.getResetEndTime()).isEqualTo(UPDATED_RESET_END_TIME);
        assertThat(testSysFillRule.getResetTime()).isEqualTo(UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void putNonExistingSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

        // Update the sysFillRule using partial update
        SysFillRule partialUpdatedSysFillRule = new SysFillRule();
        partialUpdatedSysFillRule.setId(sysFillRule.getId());

        partialUpdatedSysFillRule
            .name(UPDATED_NAME)
            .desc(UPDATED_DESC)
            .enabled(UPDATED_ENABLED)
            .resetFrequency(UPDATED_RESET_FREQUENCY)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .resetStartTime(UPDATED_RESET_START_TIME);

        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysFillRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSysFillRule.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testSysFillRule.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSysFillRule.getResetFrequency()).isEqualTo(UPDATED_RESET_FREQUENCY);
        assertThat(testSysFillRule.getSeqValue()).isEqualTo(DEFAULT_SEQ_VALUE);
        assertThat(testSysFillRule.getFillValue()).isEqualTo(DEFAULT_FILL_VALUE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(UPDATED_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getResetStartTime()).isEqualTo(UPDATED_RESET_START_TIME);
        assertThat(testSysFillRule.getResetEndTime()).isEqualTo(DEFAULT_RESET_END_TIME);
        assertThat(testSysFillRule.getResetTime()).isEqualTo(DEFAULT_RESET_TIME);
    }

    @Test
    @Transactional
    void fullUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSysFillRule.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testSysFillRule.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSysFillRule.getResetFrequency()).isEqualTo(UPDATED_RESET_FREQUENCY);
        assertThat(testSysFillRule.getSeqValue()).isEqualTo(UPDATED_SEQ_VALUE);
        assertThat(testSysFillRule.getFillValue()).isEqualTo(UPDATED_FILL_VALUE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(UPDATED_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getResetStartTime()).isEqualTo(UPDATED_RESET_START_TIME);
        assertThat(testSysFillRule.getResetEndTime()).isEqualTo(UPDATED_RESET_END_TIME);
        assertThat(testSysFillRule.getResetTime()).isEqualTo(UPDATED_RESET_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(longCount.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysFillRule() throws Exception {
        // Initialize the database
        sysFillRuleRepository.save(sysFillRule);

        int databaseSizeBeforeDelete = sysFillRuleRepository.findAll().size();

        // Delete the sysFillRule
        restSysFillRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysFillRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
