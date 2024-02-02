package com.begcode.monolith.settings.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.dto.SiteConfigDTO;
import com.begcode.monolith.settings.service.mapper.SiteConfigMapper;
import com.begcode.monolith.web.rest.TestUtil;
import com.begcode.monolith.web.rest.TestUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SiteConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class SiteConfigResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final Integer DEFAULT_SORT_VALUE = 1;
    private static final Integer UPDATED_SORT_VALUE = 2;
    private static final Integer SMALLER_SORT_VALUE = 1 - 1;

    private static final Boolean DEFAULT_BUILT_IN = false;
    private static final Boolean UPDATED_BUILT_IN = true;

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

    private static final String ENTITY_API_URL = "/api/site-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    @Autowired
    private SiteConfigMapper siteConfigMapper;

    @Autowired
    private MockMvc restSiteConfigMockMvc;

    private SiteConfig siteConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfig createEntity() {
        SiteConfig siteConfig = new SiteConfig()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .categoryKey(DEFAULT_CATEGORY_KEY)
            .disabled(DEFAULT_DISABLED)
            .sortValue(DEFAULT_SORT_VALUE)
            .builtIn(DEFAULT_BUILT_IN)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return siteConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfig createUpdatedEntity() {
        SiteConfig siteConfig = new SiteConfig()
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return siteConfig;
    }

    @BeforeEach
    public void initTest() {
        siteConfig = createEntity();
    }

    @Test
    @Transactional
    void createSiteConfig() throws Exception {
        int databaseSizeBeforeCreate = siteConfigRepository.findAll().size();
        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);
        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testSiteConfig.getCategoryKey()).isEqualTo(DEFAULT_CATEGORY_KEY);
        assertThat(testSiteConfig.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testSiteConfig.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testSiteConfig.getBuiltIn()).isEqualTo(DEFAULT_BUILT_IN);
        assertThat(testSiteConfig.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSiteConfig.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSiteConfig.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSiteConfig.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSiteConfigWithExistingId() throws Exception {
        // Create the SiteConfig with an existing ID
        siteConfig.setId(1L);
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        int databaseSizeBeforeCreate = siteConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteConfigRepository.findAll().size();
        // set the field null
        siteConfig.setCategoryName(null);

        // Create the SiteConfig, which fails.
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteConfigRepository.findAll().size();
        // set the field null
        siteConfig.setCategoryKey(null);

        // Create the SiteConfig, which fails.
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSiteConfigs() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryKey").value(hasItem(DEFAULT_CATEGORY_KEY)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].builtIn").value(hasItem(DEFAULT_BUILT_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get the siteConfig
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, siteConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteConfig.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.categoryKey").value(DEFAULT_CATEGORY_KEY))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.sortValue").value(DEFAULT_SORT_VALUE))
            .andExpect(jsonPath("$.builtIn").value(DEFAULT_BUILT_IN.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSiteConfigsByIdFiltering() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        Long id = siteConfig.getId();

        defaultSiteConfigShouldBeFound("id.equals=" + id);
        defaultSiteConfigShouldNotBeFound("id.notEquals=" + id);

        defaultSiteConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSiteConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultSiteConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSiteConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultSiteConfigShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the siteConfigList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultSiteConfigShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultSiteConfigShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the siteConfigList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultSiteConfigShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryName is not null
        defaultSiteConfigShouldBeFound("categoryName.specified=true");

        // Get all the siteConfigList where categoryName is null
        defaultSiteConfigShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultSiteConfigShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the siteConfigList where categoryName contains UPDATED_CATEGORY_NAME
        defaultSiteConfigShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultSiteConfigShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the siteConfigList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultSiteConfigShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryKey equals to DEFAULT_CATEGORY_KEY
        defaultSiteConfigShouldBeFound("categoryKey.equals=" + DEFAULT_CATEGORY_KEY);

        // Get all the siteConfigList where categoryKey equals to UPDATED_CATEGORY_KEY
        defaultSiteConfigShouldNotBeFound("categoryKey.equals=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryKey in DEFAULT_CATEGORY_KEY or UPDATED_CATEGORY_KEY
        defaultSiteConfigShouldBeFound("categoryKey.in=" + DEFAULT_CATEGORY_KEY + "," + UPDATED_CATEGORY_KEY);

        // Get all the siteConfigList where categoryKey equals to UPDATED_CATEGORY_KEY
        defaultSiteConfigShouldNotBeFound("categoryKey.in=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryKey is not null
        defaultSiteConfigShouldBeFound("categoryKey.specified=true");

        // Get all the siteConfigList where categoryKey is null
        defaultSiteConfigShouldNotBeFound("categoryKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryKey contains DEFAULT_CATEGORY_KEY
        defaultSiteConfigShouldBeFound("categoryKey.contains=" + DEFAULT_CATEGORY_KEY);

        // Get all the siteConfigList where categoryKey contains UPDATED_CATEGORY_KEY
        defaultSiteConfigShouldNotBeFound("categoryKey.contains=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where categoryKey does not contain DEFAULT_CATEGORY_KEY
        defaultSiteConfigShouldNotBeFound("categoryKey.doesNotContain=" + DEFAULT_CATEGORY_KEY);

        // Get all the siteConfigList where categoryKey does not contain UPDATED_CATEGORY_KEY
        defaultSiteConfigShouldBeFound("categoryKey.doesNotContain=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where disabled equals to DEFAULT_DISABLED
        defaultSiteConfigShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the siteConfigList where disabled equals to UPDATED_DISABLED
        defaultSiteConfigShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultSiteConfigShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the siteConfigList where disabled equals to UPDATED_DISABLED
        defaultSiteConfigShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where disabled is not null
        defaultSiteConfigShouldBeFound("disabled.specified=true");

        // Get all the siteConfigList where disabled is null
        defaultSiteConfigShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue equals to DEFAULT_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.equals=" + DEFAULT_SORT_VALUE);

        // Get all the siteConfigList where sortValue equals to UPDATED_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue in DEFAULT_SORT_VALUE or UPDATED_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE);

        // Get all the siteConfigList where sortValue equals to UPDATED_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue is not null
        defaultSiteConfigShouldBeFound("sortValue.specified=true");

        // Get all the siteConfigList where sortValue is null
        defaultSiteConfigShouldNotBeFound("sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue is greater than or equal to DEFAULT_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the siteConfigList where sortValue is greater than or equal to UPDATED_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue is less than or equal to DEFAULT_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the siteConfigList where sortValue is less than or equal to SMALLER_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue is less than DEFAULT_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.lessThan=" + DEFAULT_SORT_VALUE);

        // Get all the siteConfigList where sortValue is less than UPDATED_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.lessThan=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where sortValue is greater than DEFAULT_SORT_VALUE
        defaultSiteConfigShouldNotBeFound("sortValue.greaterThan=" + DEFAULT_SORT_VALUE);

        // Get all the siteConfigList where sortValue is greater than SMALLER_SORT_VALUE
        defaultSiteConfigShouldBeFound("sortValue.greaterThan=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where builtIn equals to DEFAULT_BUILT_IN
        defaultSiteConfigShouldBeFound("builtIn.equals=" + DEFAULT_BUILT_IN);

        // Get all the siteConfigList where builtIn equals to UPDATED_BUILT_IN
        defaultSiteConfigShouldNotBeFound("builtIn.equals=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where builtIn in DEFAULT_BUILT_IN or UPDATED_BUILT_IN
        defaultSiteConfigShouldBeFound("builtIn.in=" + DEFAULT_BUILT_IN + "," + UPDATED_BUILT_IN);

        // Get all the siteConfigList where builtIn equals to UPDATED_BUILT_IN
        defaultSiteConfigShouldNotBeFound("builtIn.in=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where builtIn is not null
        defaultSiteConfigShouldBeFound("builtIn.specified=true");

        // Get all the siteConfigList where builtIn is null
        defaultSiteConfigShouldNotBeFound("builtIn.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy equals to DEFAULT_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the siteConfigList where createdBy equals to UPDATED_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the siteConfigList where createdBy equals to UPDATED_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy is not null
        defaultSiteConfigShouldBeFound("createdBy.specified=true");

        // Get all the siteConfigList where createdBy is null
        defaultSiteConfigShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the siteConfigList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the siteConfigList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy is less than DEFAULT_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the siteConfigList where createdBy is less than UPDATED_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdBy is greater than DEFAULT_CREATED_BY
        defaultSiteConfigShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the siteConfigList where createdBy is greater than SMALLER_CREATED_BY
        defaultSiteConfigShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdDate equals to DEFAULT_CREATED_DATE
        defaultSiteConfigShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the siteConfigList where createdDate equals to UPDATED_CREATED_DATE
        defaultSiteConfigShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultSiteConfigShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the siteConfigList where createdDate equals to UPDATED_CREATED_DATE
        defaultSiteConfigShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where createdDate is not null
        defaultSiteConfigShouldBeFound("createdDate.specified=true");

        // Get all the siteConfigList where createdDate is null
        defaultSiteConfigShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is not null
        defaultSiteConfigShouldBeFound("lastModifiedBy.specified=true");

        // Get all the siteConfigList where lastModifiedBy is null
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is greater than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy is greater than or equal to UPDATED_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is less than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy is less than or equal to SMALLER_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is less than DEFAULT_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy is less than UPDATED_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is greater than DEFAULT_LAST_MODIFIED_BY
        defaultSiteConfigShouldNotBeFound("lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the siteConfigList where lastModifiedBy is greater than SMALLER_LAST_MODIFIED_BY
        defaultSiteConfigShouldBeFound("lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSiteConfigShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the siteConfigList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSiteConfigShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultSiteConfigShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the siteConfigList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSiteConfigShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        // Get all the siteConfigList where lastModifiedDate is not null
        defaultSiteConfigShouldBeFound("lastModifiedDate.specified=true");

        // Get all the siteConfigList where lastModifiedDate is null
        defaultSiteConfigShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteConfigShouldBeFound(String filter) throws Exception {
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryKey").value(hasItem(DEFAULT_CATEGORY_KEY)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].builtIn").value(hasItem(DEFAULT_BUILT_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteConfigShouldNotBeFound(String filter) throws Exception {
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSiteConfig() throws Exception {
        // Get the siteConfig
        restSiteConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig
        SiteConfig updatedSiteConfig = siteConfigRepository.findById(siteConfig.getId()).orElseThrow();
        updatedSiteConfig
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(updatedSiteConfig);

        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testSiteConfig.getCategoryKey()).isEqualTo(UPDATED_CATEGORY_KEY);
        assertThat(testSiteConfig.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testSiteConfig.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testSiteConfig.getBuiltIn()).isEqualTo(UPDATED_BUILT_IN);
        assertThat(testSiteConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSiteConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSiteConfig.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSiteConfig.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig using partial update
        SiteConfig partialUpdatedSiteConfig = new SiteConfig();
        partialUpdatedSiteConfig.setId(siteConfig.getId());

        partialUpdatedSiteConfig
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testSiteConfig.getCategoryKey()).isEqualTo(UPDATED_CATEGORY_KEY);
        assertThat(testSiteConfig.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testSiteConfig.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testSiteConfig.getBuiltIn()).isEqualTo(DEFAULT_BUILT_IN);
        assertThat(testSiteConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSiteConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSiteConfig.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSiteConfig.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig using partial update
        SiteConfig partialUpdatedSiteConfig = new SiteConfig();
        partialUpdatedSiteConfig.setId(siteConfig.getId());

        partialUpdatedSiteConfig
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testSiteConfig.getCategoryKey()).isEqualTo(UPDATED_CATEGORY_KEY);
        assertThat(testSiteConfig.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testSiteConfig.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testSiteConfig.getBuiltIn()).isEqualTo(UPDATED_BUILT_IN);
        assertThat(testSiteConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSiteConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSiteConfig.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSiteConfig.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.save(siteConfig);

        int databaseSizeBeforeDelete = siteConfigRepository.findAll().size();

        // Delete the siteConfig
        restSiteConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
