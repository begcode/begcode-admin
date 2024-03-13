package com.begcode.monolith.web.rest;

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
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    @Autowired
    private MockMvc restBusinessTypeMockMvc;

    private BusinessType businessType;

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

    @Test
    @Transactional
    void createBusinessType() throws Exception {
        int databaseSizeBeforeCreate = businessTypeRepository.findAll().size();
        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);
        restBusinessTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessType testBusinessType = businessTypeList.get(businessTypeList.size() - 1);
        assertThat(testBusinessType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBusinessType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessType.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    void createBusinessTypeWithExistingId() throws Exception {
        // Create the BusinessType with an existing ID
        businessType.setId(1L);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        int databaseSizeBeforeCreate = businessTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBusinessTypes() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

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
        businessTypeRepository.save(businessType);

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
        businessTypeRepository.save(businessType);

        Long id = businessType.getId();

        defaultBusinessTypeShouldBeFound("id.equals=" + id);
        defaultBusinessTypeShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where name equals to DEFAULT_NAME
        defaultBusinessTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the businessTypeList where name equals to UPDATED_NAME
        defaultBusinessTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBusinessTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the businessTypeList where name equals to UPDATED_NAME
        defaultBusinessTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where name is not null
        defaultBusinessTypeShouldBeFound("name.specified=true");

        // Get all the businessTypeList where name is null
        defaultBusinessTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where name contains DEFAULT_NAME
        defaultBusinessTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the businessTypeList where name contains UPDATED_NAME
        defaultBusinessTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where name does not contain DEFAULT_NAME
        defaultBusinessTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the businessTypeList where name does not contain UPDATED_NAME
        defaultBusinessTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where code equals to DEFAULT_CODE
        defaultBusinessTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the businessTypeList where code equals to UPDATED_CODE
        defaultBusinessTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBusinessTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the businessTypeList where code equals to UPDATED_CODE
        defaultBusinessTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where code is not null
        defaultBusinessTypeShouldBeFound("code.specified=true");

        // Get all the businessTypeList where code is null
        defaultBusinessTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where code contains DEFAULT_CODE
        defaultBusinessTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the businessTypeList where code contains UPDATED_CODE
        defaultBusinessTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where code does not contain DEFAULT_CODE
        defaultBusinessTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the businessTypeList where code does not contain UPDATED_CODE
        defaultBusinessTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where description equals to DEFAULT_DESCRIPTION
        defaultBusinessTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the businessTypeList where description equals to UPDATED_DESCRIPTION
        defaultBusinessTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBusinessTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the businessTypeList where description equals to UPDATED_DESCRIPTION
        defaultBusinessTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where description is not null
        defaultBusinessTypeShouldBeFound("description.specified=true");

        // Get all the businessTypeList where description is null
        defaultBusinessTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where description contains DEFAULT_DESCRIPTION
        defaultBusinessTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the businessTypeList where description contains UPDATED_DESCRIPTION
        defaultBusinessTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultBusinessTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the businessTypeList where description does not contain UPDATED_DESCRIPTION
        defaultBusinessTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where icon equals to DEFAULT_ICON
        defaultBusinessTypeShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the businessTypeList where icon equals to UPDATED_ICON
        defaultBusinessTypeShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsInShouldWork() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultBusinessTypeShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the businessTypeList where icon equals to UPDATED_ICON
        defaultBusinessTypeShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where icon is not null
        defaultBusinessTypeShouldBeFound("icon.specified=true");

        // Get all the businessTypeList where icon is null
        defaultBusinessTypeShouldNotBeFound("icon.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where icon contains DEFAULT_ICON
        defaultBusinessTypeShouldBeFound("icon.contains=" + DEFAULT_ICON);

        // Get all the businessTypeList where icon contains UPDATED_ICON
        defaultBusinessTypeShouldNotBeFound("icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllBusinessTypesByIconNotContainsSomething() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        // Get all the businessTypeList where icon does not contain DEFAULT_ICON
        defaultBusinessTypeShouldNotBeFound("icon.doesNotContain=" + DEFAULT_ICON);

        // Get all the businessTypeList where icon does not contain UPDATED_ICON
        defaultBusinessTypeShouldBeFound("icon.doesNotContain=" + UPDATED_ICON);
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
        businessTypeRepository.save(businessType);

        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();

        // Update the businessType
        BusinessType updatedBusinessType = businessTypeRepository.findById(businessType.getId()).orElseThrow();
        updatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(updatedBusinessType);

        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
        BusinessType testBusinessType = businessTypeList.get(businessTypeList.size() - 1);
        assertThat(testBusinessType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessType.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void putNonExistingBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).icon(UPDATED_ICON);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
        BusinessType testBusinessType = businessTypeList.get(businessTypeList.size() - 1);
        assertThat(testBusinessType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessType.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void fullUpdateBusinessTypeWithPatch() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();

        // Update the businessType using partial update
        BusinessType partialUpdatedBusinessType = new BusinessType();
        partialUpdatedBusinessType.setId(businessType.getId());

        partialUpdatedBusinessType.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);

        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessType))
            )
            .andExpect(status().isOk());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
        BusinessType testBusinessType = businessTypeList.get(businessTypeList.size() - 1);
        assertThat(testBusinessType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBusinessType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessType.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessType() throws Exception {
        int databaseSizeBeforeUpdate = businessTypeRepository.findAll().size();
        businessType.setId(longCount.incrementAndGet());

        // Create the BusinessType
        BusinessTypeDTO businessTypeDTO = businessTypeMapper.toDto(businessType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessType in the database
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessType() throws Exception {
        // Initialize the database
        businessTypeRepository.save(businessType);

        int databaseSizeBeforeDelete = businessTypeRepository.findAll().size();

        // Delete the businessType
        restBusinessTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessType> businessTypeList = businessTypeRepository.findAll();
        assertThat(businessTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
