package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.FormSaveDataAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.system.domain.FormConfig;
import com.begcode.monolith.system.domain.FormSaveData;
import com.begcode.monolith.system.repository.FormSaveDataRepository;
import com.begcode.monolith.system.service.FormSaveDataService;
import com.begcode.monolith.system.service.dto.FormSaveDataDTO;
import com.begcode.monolith.system.service.mapper.FormSaveDataMapper;
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
 * Integration tests for the {@link FormSaveDataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class FormSaveDataResourceIT {

    private static final String DEFAULT_FORM_DATA = "AAAAAAAAAA";
    private static final String UPDATED_FORM_DATA = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/form-save-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FormSaveDataRepository formSaveDataRepository;

    @Mock
    private FormSaveDataRepository formSaveDataRepositoryMock;

    @Autowired
    private FormSaveDataMapper formSaveDataMapper;

    @Mock
    private FormSaveDataService formSaveDataServiceMock;

    @Autowired
    private MockMvc restFormSaveDataMockMvc;

    private FormSaveData formSaveData;

    private FormSaveData insertedFormSaveData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormSaveData createEntity() {
        FormSaveData formSaveData = new FormSaveData()
            .formData(DEFAULT_FORM_DATA)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return formSaveData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormSaveData createUpdatedEntity() {
        FormSaveData formSaveData = new FormSaveData()
            .formData(UPDATED_FORM_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return formSaveData;
    }

    @BeforeEach
    public void initTest() {
        formSaveData = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFormSaveData != null) {
            formSaveDataRepository.deleteById(insertedFormSaveData.getId());
            insertedFormSaveData = null;
        }
    }

    @Test
    @Transactional
    void createFormSaveData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);
        var returnedFormSaveDataDTO = om.readValue(
            restFormSaveDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formSaveDataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FormSaveDataDTO.class
        );

        // Validate the FormSaveData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFormSaveData = formSaveDataMapper.toEntity(returnedFormSaveDataDTO);
        assertFormSaveDataUpdatableFieldsEquals(returnedFormSaveData, getPersistedFormSaveData(returnedFormSaveData));

        insertedFormSaveData = returnedFormSaveData;
    }

    @Test
    @Transactional
    void createFormSaveDataWithExistingId() throws Exception {
        // Create the FormSaveData with an existing ID
        formSaveData.setId(1L);
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormSaveDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formSaveDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormSaveData() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formSaveData.getId().intValue())))
            .andExpect(jsonPath("$.[*].formData").value(hasItem(DEFAULT_FORM_DATA.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormSaveDataWithEagerRelationshipsIsEnabled() throws Exception {
        when(formSaveDataServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFormSaveDataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formSaveDataServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormSaveDataWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formSaveDataServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFormSaveDataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(formSaveDataRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getFormSaveData() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get the formSaveData
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL_ID, formSaveData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formSaveData.getId().intValue()))
            .andExpect(jsonPath("$.formData").value(DEFAULT_FORM_DATA.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFormSaveDataByIdFiltering() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        Long id = formSaveData.getId();

        defaultFormSaveDataFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFormSaveDataFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFormSaveDataFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy equals to
        defaultFormSaveDataFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy in
        defaultFormSaveDataFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy is not null
        defaultFormSaveDataFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy is greater than or equal to
        defaultFormSaveDataFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy is less than or equal to
        defaultFormSaveDataFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy is less than
        defaultFormSaveDataFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdBy is greater than
        defaultFormSaveDataFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdDate equals to
        defaultFormSaveDataFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdDate in
        defaultFormSaveDataFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where createdDate is not null
        defaultFormSaveDataFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy equals to
        defaultFormSaveDataFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy in
        defaultFormSaveDataFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy is not null
        defaultFormSaveDataFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy is greater than or equal to
        defaultFormSaveDataFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy is less than or equal to
        defaultFormSaveDataFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy is less than
        defaultFormSaveDataFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedBy is greater than
        defaultFormSaveDataFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedDate equals to
        defaultFormSaveDataFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedDate in
        defaultFormSaveDataFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFormSaveDataByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        // Get all the formSaveDataList where lastModifiedDate is not null
        defaultFormSaveDataFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFormSaveDataByFormConfigIsEqualToSomething() throws Exception {
        FormConfig formConfig = FormConfigResourceIT.createEntity();
        formSaveData.setFormConfig(formConfig);
        formSaveDataRepository.insert(formSaveData);
        Long formConfigId = formConfig.getId();
        // Get all the formSaveDataList where formConfig equals to formConfigId
        defaultFormSaveDataShouldBeFound("formConfigId.equals=" + formConfigId);

        // Get all the formSaveDataList where formConfig equals to (formConfigId + 1)
        defaultFormSaveDataShouldNotBeFound("formConfigId.equals=" + (formConfigId + 1));
    }

    private void defaultFormSaveDataFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFormSaveDataShouldBeFound(shouldBeFound);
        defaultFormSaveDataShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormSaveDataShouldBeFound(String filter) throws Exception {
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formSaveData.getId().intValue())))
            .andExpect(jsonPath("$.[*].formData").value(hasItem(DEFAULT_FORM_DATA.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormSaveDataShouldNotBeFound(String filter) throws Exception {
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormSaveDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormSaveData() throws Exception {
        // Get the formSaveData
        restFormSaveDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormSaveData() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formSaveData
        FormSaveData updatedFormSaveData = formSaveDataRepository.findById(formSaveData.getId()).orElseThrow();
        updatedFormSaveData
            .formData(UPDATED_FORM_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(updatedFormSaveData);

        restFormSaveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formSaveDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formSaveDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFormSaveDataToMatchAllProperties(updatedFormSaveData);
    }

    @Test
    @Transactional
    void putNonExistingFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formSaveDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formSaveDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formSaveDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formSaveDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormSaveDataWithPatch() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formSaveData using partial update
        FormSaveData partialUpdatedFormSaveData = new FormSaveData();
        partialUpdatedFormSaveData.setId(formSaveData.getId());

        partialUpdatedFormSaveData.lastModifiedBy(UPDATED_LAST_MODIFIED_BY).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFormSaveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormSaveData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormSaveData))
            )
            .andExpect(status().isOk());

        // Validate the FormSaveData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormSaveDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFormSaveData, formSaveData),
            getPersistedFormSaveData(formSaveData)
        );
    }

    @Test
    @Transactional
    void fullUpdateFormSaveDataWithPatch() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formSaveData using partial update
        FormSaveData partialUpdatedFormSaveData = new FormSaveData();
        partialUpdatedFormSaveData.setId(formSaveData.getId());

        partialUpdatedFormSaveData
            .formData(UPDATED_FORM_DATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restFormSaveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormSaveData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormSaveData))
            )
            .andExpect(status().isOk());

        // Validate the FormSaveData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormSaveDataUpdatableFieldsEquals(partialUpdatedFormSaveData, getPersistedFormSaveData(partialUpdatedFormSaveData));
    }

    @Test
    @Transactional
    void patchNonExistingFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formSaveDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formSaveDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formSaveDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormSaveData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formSaveData.setId(longCount.incrementAndGet());

        // Create the FormSaveData
        FormSaveDataDTO formSaveDataDTO = formSaveDataMapper.toDto(formSaveData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormSaveDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formSaveDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormSaveData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormSaveData() throws Exception {
        // Initialize the database
        insertedFormSaveData = formSaveDataRepository.saveAndGet(formSaveData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the formSaveData
        restFormSaveDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, formSaveData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return formSaveDataRepository.selectCount(null);
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

    protected FormSaveData getPersistedFormSaveData(FormSaveData formSaveData) {
        return formSaveDataRepository.findById(formSaveData.getId()).orElseThrow();
    }

    protected void assertPersistedFormSaveDataToMatchAllProperties(FormSaveData expectedFormSaveData) {
        assertFormSaveDataAllPropertiesEquals(expectedFormSaveData, getPersistedFormSaveData(expectedFormSaveData));
    }

    protected void assertPersistedFormSaveDataToMatchUpdatableProperties(FormSaveData expectedFormSaveData) {
        assertFormSaveDataAllUpdatablePropertiesEquals(expectedFormSaveData, getPersistedFormSaveData(expectedFormSaveData));
    }
}
