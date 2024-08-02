package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.BusinessTypeAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.BusinessType;
import com.begcode.monolith.repository.BusinessTypeRepository;
import com.begcode.monolith.service.dto.BusinessTypeDTO;
import com.begcode.monolith.service.mapper.BusinessTypeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Integration tests for the {@link BusinessTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class BusinessTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    @Autowired
    private MockMvc restBusinessTypeMockMvc;

    private BusinessType businessType;

    private BusinessType insertedBusinessType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessType createEntity() {
        BusinessType businessType = new BusinessType()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .icon(DEFAULT_ICON);
        return businessType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessType createUpdatedEntity() {
        BusinessType businessType = new BusinessType()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .icon(UPDATED_ICON);
        return businessType;
    }

    @BeforeEach
    public void initTest() {
        businessType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBusinessType != null) {
            businessTypeRepository.deleteById(insertedBusinessType.getId());
            insertedBusinessType = null;
        }
    }

    @Test
    @Transactional
    void createBusinessType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);
        var returnedBusinessTypeDTO = om.readValue(
            restBusinessTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusinessTypeDTO.class
        );

        // Validate the BusinessType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBusinessType = businessTypeMapper.toEntity(returnedBusinessTypeDTO);
        assertBusinessTypeUpdatableFieldsEquals(returnedBusinessType, getPersistedBusinessType(returnedBusinessType));

        insertedBusinessType = returnedBusinessType;
    }

    @Test
    @Transactional
    void createBusinessTypeWithExistingId() throws Exception {
        // Create the BusinessType with an existing ID
        businessType.setId(1L);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusinessTypes() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }

    @Test
    @Transactional
    void getBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get the businessType
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, businessType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }

    @Test
    @Transactional
    void getBusinessTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        Long id = businessType.getId();

        defaultBusinessTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBusinessTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBusinessTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where name equals to
        defaultBusinessTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where name in
        defaultBusinessTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where name is not null
        defaultBusinessTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where name contains
        defaultBusinessTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where name does not contain
        defaultBusinessTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where code equals to
        defaultBusinessTypeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where code in
        defaultBusinessTypeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where code is not null
        defaultBusinessTypeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where code contains
        defaultBusinessTypeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where code does not contain
        defaultBusinessTypeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where description equals to
        defaultBusinessTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where description in
        defaultBusinessTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where description is not null
        defaultBusinessTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where description contains
        defaultBusinessTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where description does not contain
        defaultBusinessTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where icon equals to
        defaultBusinessTypeFiltering("icon.equals=" + DEFAULT_ICON, "icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where icon in
        defaultBusinessTypeFiltering("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON, "icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where icon is not null
        defaultBusinessTypeFiltering("icon.specified=true", "icon.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where icon contains
        defaultBusinessTypeFiltering("icon.contains=" + DEFAULT_ICON, "icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        // Get all the businessTypeList where icon does not contain
        defaultBusinessTypeFiltering("icon.doesNotContain=" + UPDATED_ICON, "icon.doesNotContain=" + DEFAULT_ICON);
    }

    private void defaultBusinessTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBusinessTypeShouldBeFound(shouldBeFound);
        defaultBusinessTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessTypeShouldBeFound(String filter) throws Exception {
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));

        // Check, that the count call also returns 1
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessTypeShouldNotBeFound(String filter) throws Exception {
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusinessType() throws Exception {
        // Get the businessType
        restBusinessTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType
        BusinessType updatedBusinessType = businessTypeRepository.findById(businessType.getId()).orElseThrow();
        updatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(updatedBusinessType);

        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusinessTypeToMatchAllProperties(updatedBusinessType);
    }

    @Test
    @Transactional
    void putNonExistingBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBusinessType, businessType),
            getPersistedBusinessType(businessType)
        );
    }

    @Test
    @Transactional
    void fullUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessTypeUpdatableFieldsEquals(partialUpdatedBusinessType, getPersistedBusinessType(partialUpdatedBusinessType));
    }

    @Test
    @Transactional
    void patchNonExistingBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(businessTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessType() throws Exception {
        // Initialize the database
        insertedBusinessType = businessTypeRepository.saveAndGet(businessType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the businessType
        restBusinessTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return businessTypeRepository.selectCount(null);
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

    protected BusinessType getPersistedBusinessType(BusinessType businessType) {
        return businessTypeRepository.findById(businessType.getId()).orElseThrow();
    }

    protected void assertPersistedBusinessTypeToMatchAllProperties(BusinessType expectedBusinessType) {
        assertBusinessTypeAllPropertiesEquals(expectedBusinessType, getPersistedBusinessType(expectedBusinessType));
    }

    protected void assertPersistedBusinessTypeToMatchUpdatableProperties(BusinessType expectedBusinessType) {
        assertBusinessTypeAllUpdatablePropertiesEquals(expectedBusinessType, getPersistedBusinessType(expectedBusinessType));
    }
}
