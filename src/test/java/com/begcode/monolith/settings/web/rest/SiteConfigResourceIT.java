package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.SiteConfigAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.settings.domain.SiteConfig;
import com.begcode.monolith.settings.repository.SiteConfigRepository;
import com.begcode.monolith.settings.service.dto.SiteConfigDTO;
import com.begcode.monolith.settings.service.mapper.SiteConfigMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
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
 * Integration tests for the {@link SiteConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
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

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    @Autowired
    private SiteConfigMapper siteConfigMapper;

    @Autowired
    private MockMvc restSiteConfigMockMvc;

    private SiteConfig siteConfig;

    private SiteConfig insertedSiteConfig;

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

    @AfterEach
    public void cleanup() {
        if (insertedSiteConfig != null) {
            siteConfigRepository.deleteById(insertedSiteConfig.getId());
            insertedSiteConfig = null;
        }
    }

    @Test
    @Transactional
    void createSiteConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);
        var returnedSiteConfigDTO = om.readValue(
            restSiteConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SiteConfigDTO.class
        );

        // Validate the SiteConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSiteConfig = siteConfigMapper.toEntity(returnedSiteConfigDTO);
        assertSiteConfigUpdatableFieldsEquals(returnedSiteConfig, getPersistedSiteConfig(returnedSiteConfig));

        insertedSiteConfig = returnedSiteConfig;
    }

    @Test
    @Transactional
    void createSiteConfigWithExistingId() throws Exception {
        // Create the SiteConfig with an existing ID
        siteConfig.setId(1L);
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        siteConfig.setCategoryName(null);

        // Create the SiteConfig, which fails.
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        siteConfig.setCategoryKey(null);

        // Create the SiteConfig, which fails.
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSiteConfigs() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

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
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

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
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        Long id = siteConfig.getId();

        defaultSiteConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSiteConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSiteConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryName equals to
        defaultSiteConfigFiltering("categoryName.equals=" + DEFAULT_CATEGORY_NAME, "categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryName in
        defaultSiteConfigFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryName is not null
        defaultSiteConfigFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryName contains
        defaultSiteConfigFiltering("categoryName.contains=" + DEFAULT_CATEGORY_NAME, "categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryName does not contain
        defaultSiteConfigFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryKey equals to
        defaultSiteConfigFiltering("categoryKey.equals=" + DEFAULT_CATEGORY_KEY, "categoryKey.equals=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryKey in
        defaultSiteConfigFiltering(
            "categoryKey.in=" + DEFAULT_CATEGORY_KEY + "," + UPDATED_CATEGORY_KEY,
            "categoryKey.in=" + UPDATED_CATEGORY_KEY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryKey is not null
        defaultSiteConfigFiltering("categoryKey.specified=true", "categoryKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryKey contains
        defaultSiteConfigFiltering("categoryKey.contains=" + DEFAULT_CATEGORY_KEY, "categoryKey.contains=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCategoryKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where categoryKey does not contain
        defaultSiteConfigFiltering(
            "categoryKey.doesNotContain=" + UPDATED_CATEGORY_KEY,
            "categoryKey.doesNotContain=" + DEFAULT_CATEGORY_KEY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where disabled equals to
        defaultSiteConfigFiltering("disabled.equals=" + DEFAULT_DISABLED, "disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where disabled in
        defaultSiteConfigFiltering("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED, "disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where disabled is not null
        defaultSiteConfigFiltering("disabled.specified=true", "disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue equals to
        defaultSiteConfigFiltering("sortValue.equals=" + DEFAULT_SORT_VALUE, "sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue in
        defaultSiteConfigFiltering("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE, "sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue is not null
        defaultSiteConfigFiltering("sortValue.specified=true", "sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue is greater than or equal to
        defaultSiteConfigFiltering(
            "sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue is less than or equal to
        defaultSiteConfigFiltering("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE, "sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue is less than
        defaultSiteConfigFiltering("sortValue.lessThan=" + UPDATED_SORT_VALUE, "sortValue.lessThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where sortValue is greater than
        defaultSiteConfigFiltering("sortValue.greaterThan=" + SMALLER_SORT_VALUE, "sortValue.greaterThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where builtIn equals to
        defaultSiteConfigFiltering("builtIn.equals=" + DEFAULT_BUILT_IN, "builtIn.equals=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where builtIn in
        defaultSiteConfigFiltering("builtIn.in=" + DEFAULT_BUILT_IN + "," + UPDATED_BUILT_IN, "builtIn.in=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByBuiltInIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where builtIn is not null
        defaultSiteConfigFiltering("builtIn.specified=true", "builtIn.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy equals to
        defaultSiteConfigFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy in
        defaultSiteConfigFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy is not null
        defaultSiteConfigFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy is greater than or equal to
        defaultSiteConfigFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy is less than or equal to
        defaultSiteConfigFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy is less than
        defaultSiteConfigFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdBy is greater than
        defaultSiteConfigFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdDate equals to
        defaultSiteConfigFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdDate in
        defaultSiteConfigFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where createdDate is not null
        defaultSiteConfigFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy equals to
        defaultSiteConfigFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy in
        defaultSiteConfigFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is not null
        defaultSiteConfigFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is greater than or equal to
        defaultSiteConfigFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is less than or equal to
        defaultSiteConfigFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is less than
        defaultSiteConfigFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedBy is greater than
        defaultSiteConfigFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedDate equals to
        defaultSiteConfigFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedDate in
        defaultSiteConfigFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSiteConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        // Get all the siteConfigList where lastModifiedDate is not null
        defaultSiteConfigFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultSiteConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSiteConfigShouldBeFound(shouldBeFound);
        defaultSiteConfigShouldNotBeFound(shouldNotBeFound);
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
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(siteConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSiteConfigToMatchAllProperties(updatedSiteConfig);
    }

    @Test
    @Transactional
    void putNonExistingSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the siteConfig using partial update
        SiteConfig partialUpdatedSiteConfig = new SiteConfig();
        partialUpdatedSiteConfig.setId(siteConfig.getId());

        partialUpdatedSiteConfig
            .categoryName(UPDATED_CATEGORY_NAME)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSiteConfig, siteConfig),
            getPersistedSiteConfig(siteConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteConfigUpdatableFieldsEquals(partialUpdatedSiteConfig, getPersistedSiteConfig(partialUpdatedSiteConfig));
    }

    @Test
    @Transactional
    void patchNonExistingSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        siteConfig.setId(longCount.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(siteConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteConfig() throws Exception {
        // Initialize the database
        insertedSiteConfig = siteConfigRepository.saveAndGet(siteConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the siteConfig
        restSiteConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteConfigRepository.selectCount(null);
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

    protected SiteConfig getPersistedSiteConfig(SiteConfig siteConfig) {
        return siteConfigRepository.findById(siteConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSiteConfigToMatchAllProperties(SiteConfig expectedSiteConfig) {
        assertSiteConfigAllPropertiesEquals(expectedSiteConfig, getPersistedSiteConfig(expectedSiteConfig));
    }

    protected void assertPersistedSiteConfigToMatchUpdatableProperties(SiteConfig expectedSiteConfig) {
        assertSiteConfigAllUpdatablePropertiesEquals(expectedSiteConfig, getPersistedSiteConfig(expectedSiteConfig));
    }
}
