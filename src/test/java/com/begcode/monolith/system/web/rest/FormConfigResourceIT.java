package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.FormConfigAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.repository.FormConfigRepository;
import com.begcode.monolith.system.service.FormConfigService;
import com.begcode.monolith.system.service.dto.FormConfigDTO;
import com.begcode.monolith.system.service.mapper.FormConfigMapper;
import com.begcode.monolith.web.rest.BusinessTypeResourceIT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link FormConfigResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class FormConfigResourceIT {

    private static final String DEFAULT_FORM_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FORM_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FORM_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/form-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FormConfigRepository formConfigRepository;

    @Mock
    private FormConfigRepository formConfigRepositoryMock;

    @Autowired
    private FormConfigMapper formConfigMapper;

    @Mock
    private FormConfigService formConfigServiceMock;

    @Autowired
    private MockMvc restFormConfigMockMvc;

    private FormConfig formConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormConfig createEntity() {
        FormConfig formConfig = new FormConfig()
            .formKey(DEFAULT_FORM_KEY)
            .formName(DEFAULT_FORM_NAME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return formConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormConfig createUpdatedEntity() {
        FormConfig formConfig = new FormConfig()
            .formKey(UPDATED_FORM_KEY)
            .formName(UPDATED_FORM_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return formConfig;
    }

    @BeforeEach
    public void initTest() {
        formConfig = createEntity();
    }

    @Test
    @Transactional
    void createFormConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);
        var returnedFormConfigDTO = om.readValue(
            restFormConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FormConfigDTO.class
        );

        // Validate the FormConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFormConfig = formConfigMapper.toEntity(returnedFormConfigDTO);
        assertFormConfigUpdatableFieldsEquals(returnedFormConfig, getPersistedFormConfig(returnedFormConfig));
    }

    @Test
    @Transactional
    void createFormConfigWithExistingId() throws Exception {
        // Create the FormConfig with an existing ID
        formConfig.setId(1L);
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormConfigs() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].formKey").value(hasItem(DEFAULT_FORM_KEY)))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormConfigsWithEagerRelationshipsIsEnabled() throws Exception {
        when(formConfigServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFormConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formConfigServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormConfigsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formConfigServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFormConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(formConfigRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get the formConfig
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, formConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formConfig.getId().intValue()))
            .andExpect(jsonPath("$.formKey").value(DEFAULT_FORM_KEY))
            .andExpect(jsonPath("$.formName").value(DEFAULT_FORM_NAME))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFormConfigsByIdFiltering() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        Long id = formConfig.getId();

        defaultFormConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFormConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFormConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formKey equals to
        defaultFormConfigFiltering("formKey.equals=" + DEFAULT_FORM_KEY, "formKey.equals=" + UPDATED_FORM_KEY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormKeyIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formKey in
        defaultFormConfigFiltering("formKey.in=" + DEFAULT_FORM_KEY + "," + UPDATED_FORM_KEY, "formKey.in=" + UPDATED_FORM_KEY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formKey is not null
        defaultFormConfigFiltering("formKey.specified=true", "formKey.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormKeyContainsSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formKey contains
        defaultFormConfigFiltering("formKey.contains=" + DEFAULT_FORM_KEY, "formKey.contains=" + UPDATED_FORM_KEY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormKeyNotContainsSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formKey does not contain
        defaultFormConfigFiltering("formKey.doesNotContain=" + UPDATED_FORM_KEY, "formKey.doesNotContain=" + DEFAULT_FORM_KEY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formName equals to
        defaultFormConfigFiltering("formName.equals=" + DEFAULT_FORM_NAME, "formName.equals=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormNameIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formName in
        defaultFormConfigFiltering("formName.in=" + DEFAULT_FORM_NAME + "," + UPDATED_FORM_NAME, "formName.in=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formName is not null
        defaultFormConfigFiltering("formName.specified=true", "formName.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormNameContainsSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formName contains
        defaultFormConfigFiltering("formName.contains=" + DEFAULT_FORM_NAME, "formName.contains=" + UPDATED_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormConfigsByFormNameNotContainsSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where formName does not contain
        defaultFormConfigFiltering("formName.doesNotContain=" + UPDATED_FORM_NAME, "formName.doesNotContain=" + DEFAULT_FORM_NAME);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy equals to
        defaultFormConfigFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy in
        defaultFormConfigFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy is not null
        defaultFormConfigFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy is greater than or equal to
        defaultFormConfigFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy is less than or equal to
        defaultFormConfigFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy is less than
        defaultFormConfigFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdBy is greater than
        defaultFormConfigFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdDate equals to
        defaultFormConfigFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdDate in
        defaultFormConfigFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where createdDate is not null
        defaultFormConfigFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy equals to
        defaultFormConfigFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy in
        defaultFormConfigFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy is not null
        defaultFormConfigFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy is greater than or equal to
        defaultFormConfigFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy is less than or equal to
        defaultFormConfigFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy is less than
        defaultFormConfigFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedBy is greater than
        defaultFormConfigFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedDate equals to
        defaultFormConfigFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedDate in
        defaultFormConfigFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        // Get all the formConfigList where lastModifiedDate is not null
        defaultFormConfigFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFormConfigsByBusinessTypeIsEqualToSomething() throws Exception {
        BusinessType businessType = BusinessTypeResourceIT.createEntity();
        formConfig.setBusinessType(businessType);
        formConfigRepository.insert(formConfig);
        Long businessTypeId = businessType.getId();
        // Get all the formConfigList where businessType equals to businessTypeId
        defaultFormConfigShouldBeFound("businessTypeId.equals=" + businessTypeId);

        // Get all the formConfigList where businessType equals to (businessTypeId + 1)
        defaultFormConfigShouldNotBeFound("businessTypeId.equals=" + (businessTypeId + 1));
    }

    private void defaultFormConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFormConfigShouldBeFound(shouldBeFound);
        defaultFormConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormConfigShouldBeFound(String filter) throws Exception {
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].formKey").value(hasItem(DEFAULT_FORM_KEY)))
            .andExpect(jsonPath("$.[*].formName").value(hasItem(DEFAULT_FORM_NAME)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormConfigShouldNotBeFound(String filter) throws Exception {
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormConfig() throws Exception {
        // Get the formConfig
        restFormConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formConfig
        FormConfig updatedFormConfig = formConfigRepository.findById(formConfig.getId()).orElseThrow();
        updatedFormConfig
            .formKey(UPDATED_FORM_KEY)
            .formName(UPDATED_FORM_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(updatedFormConfig);

        restFormConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFormConfigToMatchAllProperties(updatedFormConfig);
    }

    @Test
    @Transactional
    void putNonExistingFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormConfigWithPatch() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formConfig using partial update
        FormConfig partialUpdatedFormConfig = new FormConfig();
        partialUpdatedFormConfig.setId(formConfig.getId());

        partialUpdatedFormConfig
            .formKey(UPDATED_FORM_KEY)
            .formName(UPDATED_FORM_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restFormConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormConfig))
            )
            .andExpect(status().isOk());

        // Validate the FormConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFormConfig, formConfig),
            getPersistedFormConfig(formConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateFormConfigWithPatch() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formConfig using partial update
        FormConfig partialUpdatedFormConfig = new FormConfig();
        partialUpdatedFormConfig.setId(formConfig.getId());

        partialUpdatedFormConfig
            .formKey(UPDATED_FORM_KEY)
            .formName(UPDATED_FORM_NAME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFormConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormConfig))
            )
            .andExpect(status().isOk());

        // Validate the FormConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormConfigUpdatableFieldsEquals(partialUpdatedFormConfig, getPersistedFormConfig(partialUpdatedFormConfig));
    }

    @Test
    @Transactional
    void patchNonExistingFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formConfig.setId(longCount.incrementAndGet());

        // Create the FormConfig
        FormConfigDTO formConfigDTO = formConfigMapper.toDto(formConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormConfig() throws Exception {
        // Initialize the database
        formConfigRepository.save(formConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the formConfig
        restFormConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, formConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return formConfigRepository.selectCount(null);
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

    protected FormConfig getPersistedFormConfig(FormConfig formConfig) {
        return formConfigRepository.findById(formConfig.getId()).orElseThrow();
    }

    protected void assertPersistedFormConfigToMatchAllProperties(FormConfig expectedFormConfig) {
        assertFormConfigAllPropertiesEquals(expectedFormConfig, getPersistedFormConfig(expectedFormConfig));
    }

    protected void assertPersistedFormConfigToMatchUpdatableProperties(FormConfig expectedFormConfig) {
        assertFormConfigAllUpdatablePropertiesEquals(expectedFormConfig, getPersistedFormConfig(expectedFormConfig));
    }
}
