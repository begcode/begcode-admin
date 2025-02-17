package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.SystemConfigAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.settings.domain.SystemConfig;
import com.begcode.monolith.settings.repository.SystemConfigRepository;
import com.begcode.monolith.settings.service.SystemConfigService;
import com.begcode.monolith.settings.service.dto.SystemConfigDTO;
import com.begcode.monolith.settings.service.mapper.SystemConfigMapper;
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
 * Integration tests for the {@link SystemConfigResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class SystemConfigResourceIT {

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

    private static final String ENTITY_API_URL = "/api/system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Mock
    private SystemConfigRepository systemConfigRepositoryMock;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Mock
    private SystemConfigService systemConfigServiceMock;

    @Autowired
    private MockMvc restSystemConfigMockMvc;

    private SystemConfig systemConfig;

    private SystemConfig insertedSystemConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createEntity() {
        SystemConfig systemConfig = new SystemConfig()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .categoryKey(DEFAULT_CATEGORY_KEY)
            .disabled(DEFAULT_DISABLED)
            .sortValue(DEFAULT_SORT_VALUE)
            .builtIn(DEFAULT_BUILT_IN)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return systemConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createUpdatedEntity() {
        SystemConfig systemConfig = new SystemConfig()
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return systemConfig;
    }

    @BeforeEach
    public void initTest() {
        systemConfig = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemConfig != null) {
            systemConfigRepository.deleteById(insertedSystemConfig.getId());
            insertedSystemConfig = null;
        }
    }

    @Test
    @Transactional
    void createSystemConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);
        var returnedSystemConfigDTO = om.readValue(
            restSystemConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemConfigDTO.class
        );

        // Validate the SystemConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemConfig = systemConfigMapper.toEntity(returnedSystemConfigDTO);
        assertSystemConfigUpdatableFieldsEquals(returnedSystemConfig, getPersistedSystemConfig(returnedSystemConfig));

        insertedSystemConfig = returnedSystemConfig;
    }

    @Test
    @Transactional
    void createSystemConfigWithExistingId() throws Exception {
        // Create the SystemConfig with an existing ID
        systemConfig.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setCategoryName(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setCategoryKey(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemConfigs() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
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

    @SuppressWarnings({ "unchecked" })
    void getAllSystemConfigsWithEagerRelationshipsIsEnabled() throws Exception {
        when(systemConfigServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restSystemConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemConfigServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemConfigsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(systemConfigServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restSystemConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(systemConfigRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get the systemConfig
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfig.getId().intValue()))
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
    void getSystemConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        Long id = systemConfig.getId();

        defaultSystemConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSystemConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSystemConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryName equals to
        defaultSystemConfigFiltering("categoryName.equals=" + DEFAULT_CATEGORY_NAME, "categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryName in
        defaultSystemConfigFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryName is not null
        defaultSystemConfigFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryName contains
        defaultSystemConfigFiltering("categoryName.contains=" + DEFAULT_CATEGORY_NAME, "categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryName does not contain
        defaultSystemConfigFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryKey equals to
        defaultSystemConfigFiltering("categoryKey.equals=" + DEFAULT_CATEGORY_KEY, "categoryKey.equals=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryKey in
        defaultSystemConfigFiltering(
            "categoryKey.in=" + DEFAULT_CATEGORY_KEY + "," + UPDATED_CATEGORY_KEY,
            "categoryKey.in=" + UPDATED_CATEGORY_KEY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryKey is not null
        defaultSystemConfigFiltering("categoryKey.specified=true", "categoryKey.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryKey contains
        defaultSystemConfigFiltering("categoryKey.contains=" + DEFAULT_CATEGORY_KEY, "categoryKey.contains=" + UPDATED_CATEGORY_KEY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCategoryKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where categoryKey does not contain
        defaultSystemConfigFiltering(
            "categoryKey.doesNotContain=" + UPDATED_CATEGORY_KEY,
            "categoryKey.doesNotContain=" + DEFAULT_CATEGORY_KEY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where disabled equals to
        defaultSystemConfigFiltering("disabled.equals=" + DEFAULT_DISABLED, "disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where disabled in
        defaultSystemConfigFiltering("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED, "disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where disabled is not null
        defaultSystemConfigFiltering("disabled.specified=true", "disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue equals to
        defaultSystemConfigFiltering("sortValue.equals=" + DEFAULT_SORT_VALUE, "sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue in
        defaultSystemConfigFiltering("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE, "sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue is not null
        defaultSystemConfigFiltering("sortValue.specified=true", "sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue is greater than or equal to
        defaultSystemConfigFiltering(
            "sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue is less than or equal to
        defaultSystemConfigFiltering("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE, "sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue is less than
        defaultSystemConfigFiltering("sortValue.lessThan=" + UPDATED_SORT_VALUE, "sortValue.lessThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where sortValue is greater than
        defaultSystemConfigFiltering("sortValue.greaterThan=" + SMALLER_SORT_VALUE, "sortValue.greaterThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByBuiltInIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where builtIn equals to
        defaultSystemConfigFiltering("builtIn.equals=" + DEFAULT_BUILT_IN, "builtIn.equals=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByBuiltInIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where builtIn in
        defaultSystemConfigFiltering("builtIn.in=" + DEFAULT_BUILT_IN + "," + UPDATED_BUILT_IN, "builtIn.in=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByBuiltInIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where builtIn is not null
        defaultSystemConfigFiltering("builtIn.specified=true", "builtIn.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy equals to
        defaultSystemConfigFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy in
        defaultSystemConfigFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy is not null
        defaultSystemConfigFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy is greater than or equal to
        defaultSystemConfigFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy is less than or equal to
        defaultSystemConfigFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy is less than
        defaultSystemConfigFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdBy is greater than
        defaultSystemConfigFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdDate equals to
        defaultSystemConfigFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdDate in
        defaultSystemConfigFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where createdDate is not null
        defaultSystemConfigFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy equals to
        defaultSystemConfigFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy in
        defaultSystemConfigFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy is not null
        defaultSystemConfigFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy is greater than or equal to
        defaultSystemConfigFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy is less than or equal to
        defaultSystemConfigFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy is less than
        defaultSystemConfigFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedBy is greater than
        defaultSystemConfigFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedDate equals to
        defaultSystemConfigFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedDate in
        defaultSystemConfigFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSystemConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        // Get all the systemConfigList where lastModifiedDate is not null
        defaultSystemConfigFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultSystemConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSystemConfigShouldBeFound(shouldBeFound);
        defaultSystemConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemConfigShouldBeFound(String filter) throws Exception {
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
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
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemConfigShouldNotBeFound(String filter) throws Exception {
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig
        SystemConfig updatedSystemConfig = systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
        updatedSystemConfig
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfig);

        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemConfigToMatchAllProperties(updatedSystemConfig);
    }

    @Test
    @Transactional
    void putNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .disabled(UPDATED_DISABLED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemConfig, systemConfig),
            getPersistedSystemConfig(systemConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryKey(UPDATED_CATEGORY_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(partialUpdatedSystemConfig, getPersistedSystemConfig(partialUpdatedSystemConfig));
    }

    @Test
    @Transactional
    void patchNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndGet(systemConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemConfig
        restSystemConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemConfigRepository.selectCount(null);
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

    protected SystemConfig getPersistedSystemConfig(SystemConfig systemConfig) {
        return systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSystemConfigToMatchAllProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllPropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }

    protected void assertPersistedSystemConfigToMatchUpdatableProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllUpdatablePropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }
}
