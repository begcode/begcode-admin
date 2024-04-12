package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.SmsSupplierAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.SmsProvider;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import com.begcode.monolith.system.service.mapper.SmsSupplierMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SmsSupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class SmsSupplierResourceIT {

    private static final SmsProvider DEFAULT_PROVIDER = SmsProvider.ALIBABA;
    private static final SmsProvider UPDATED_PROVIDER = SmsProvider.HUAWEI;

    private static final String DEFAULT_CONFIG_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_SIGN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/sms-suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SmsSupplierRepository smsSupplierRepository;

    @Autowired
    private SmsSupplierMapper smsSupplierMapper;

    @Autowired
    private MockMvc restSmsSupplierMockMvc;

    private SmsSupplier smsSupplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsSupplier createEntity() {
        SmsSupplier smsSupplier = new SmsSupplier()
            .provider(DEFAULT_PROVIDER)
            .configData(DEFAULT_CONFIG_DATA)
            .signName(DEFAULT_SIGN_NAME)
            .remark(DEFAULT_REMARK)
            .enabled(DEFAULT_ENABLED);
        return smsSupplier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsSupplier createUpdatedEntity() {
        SmsSupplier smsSupplier = new SmsSupplier()
            .provider(UPDATED_PROVIDER)
            .configData(UPDATED_CONFIG_DATA)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        return smsSupplier;
    }

    @BeforeEach
    public void initTest() {
        smsSupplier = createEntity();
    }

    @Test
    @Transactional
    void createSmsSupplier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);
        var returnedSmsSupplierDTO = om.readValue(
            restSmsSupplierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsSupplierDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SmsSupplierDTO.class
        );

        // Validate the SmsSupplier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSmsSupplier = smsSupplierMapper.toEntity(returnedSmsSupplierDTO);
        assertSmsSupplierUpdatableFieldsEquals(returnedSmsSupplier, getPersistedSmsSupplier(returnedSmsSupplier));
    }

    @Test
    @Transactional
    void createSmsSupplierWithExistingId() throws Exception {
        // Create the SmsSupplier with an existing ID
        smsSupplier.setId(1L);
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsSupplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSmsSuppliers() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].configData").value(hasItem(DEFAULT_CONFIG_DATA)))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getSmsSupplier() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get the smsSupplier
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, smsSupplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsSupplier.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.configData").value(DEFAULT_CONFIG_DATA))
            .andExpect(jsonPath("$.signName").value(DEFAULT_SIGN_NAME))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getSmsSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        Long id = smsSupplier.getId();

        defaultSmsSupplierFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSmsSupplierFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSmsSupplierFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider equals to
        defaultSmsSupplierFiltering("provider.equals=" + DEFAULT_PROVIDER, "provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider in
        defaultSmsSupplierFiltering("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER, "provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider is not null
        defaultSmsSupplierFiltering("provider.specified=true", "provider.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData equals to
        defaultSmsSupplierFiltering("configData.equals=" + DEFAULT_CONFIG_DATA, "configData.equals=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData in
        defaultSmsSupplierFiltering(
            "configData.in=" + DEFAULT_CONFIG_DATA + "," + UPDATED_CONFIG_DATA,
            "configData.in=" + UPDATED_CONFIG_DATA
        );
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData is not null
        defaultSmsSupplierFiltering("configData.specified=true", "configData.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData contains
        defaultSmsSupplierFiltering("configData.contains=" + DEFAULT_CONFIG_DATA, "configData.contains=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData does not contain
        defaultSmsSupplierFiltering("configData.doesNotContain=" + UPDATED_CONFIG_DATA, "configData.doesNotContain=" + DEFAULT_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName equals to
        defaultSmsSupplierFiltering("signName.equals=" + DEFAULT_SIGN_NAME, "signName.equals=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName in
        defaultSmsSupplierFiltering("signName.in=" + DEFAULT_SIGN_NAME + "," + UPDATED_SIGN_NAME, "signName.in=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName is not null
        defaultSmsSupplierFiltering("signName.specified=true", "signName.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName contains
        defaultSmsSupplierFiltering("signName.contains=" + DEFAULT_SIGN_NAME, "signName.contains=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName does not contain
        defaultSmsSupplierFiltering("signName.doesNotContain=" + UPDATED_SIGN_NAME, "signName.doesNotContain=" + DEFAULT_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark equals to
        defaultSmsSupplierFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark in
        defaultSmsSupplierFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark is not null
        defaultSmsSupplierFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark contains
        defaultSmsSupplierFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark does not contain
        defaultSmsSupplierFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled equals to
        defaultSmsSupplierFiltering("enabled.equals=" + DEFAULT_ENABLED, "enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled in
        defaultSmsSupplierFiltering("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED, "enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled is not null
        defaultSmsSupplierFiltering("enabled.specified=true", "enabled.specified=false");
    }

    private void defaultSmsSupplierFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSmsSupplierShouldBeFound(shouldBeFound);
        defaultSmsSupplierShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsSupplierShouldBeFound(String filter) throws Exception {
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsSupplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].configData").value(hasItem(DEFAULT_CONFIG_DATA)))
            .andExpect(jsonPath("$.[*].signName").value(hasItem(DEFAULT_SIGN_NAME)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsSupplierShouldNotBeFound(String filter) throws Exception {
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmsSupplier() throws Exception {
        // Get the smsSupplier
        restSmsSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSmsSupplier() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsSupplier
        SmsSupplier updatedSmsSupplier = smsSupplierRepository.findById(smsSupplier.getId()).orElseThrow();
        updatedSmsSupplier
            .provider(UPDATED_PROVIDER)
            .configData(UPDATED_CONFIG_DATA)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(updatedSmsSupplier);

        restSmsSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsSupplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsSupplierDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSmsSupplierToMatchAllProperties(updatedSmsSupplier);
    }

    @Test
    @Transactional
    void putNonExistingSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsSupplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsSupplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsSupplierWithPatch() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsSupplier using partial update
        SmsSupplier partialUpdatedSmsSupplier = new SmsSupplier();
        partialUpdatedSmsSupplier.setId(smsSupplier.getId());

        partialUpdatedSmsSupplier.configData(UPDATED_CONFIG_DATA).signName(UPDATED_SIGN_NAME);

        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsSupplier))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsSupplierUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSmsSupplier, smsSupplier),
            getPersistedSmsSupplier(smsSupplier)
        );
    }

    @Test
    @Transactional
    void fullUpdateSmsSupplierWithPatch() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsSupplier using partial update
        SmsSupplier partialUpdatedSmsSupplier = new SmsSupplier();
        partialUpdatedSmsSupplier.setId(smsSupplier.getId());

        partialUpdatedSmsSupplier
            .provider(UPDATED_PROVIDER)
            .configData(UPDATED_CONFIG_DATA)
            .signName(UPDATED_SIGN_NAME)
            .remark(UPDATED_REMARK)
            .enabled(UPDATED_ENABLED);

        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsSupplier))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsSupplierUpdatableFieldsEquals(partialUpdatedSmsSupplier, getPersistedSmsSupplier(partialUpdatedSmsSupplier));
    }

    @Test
    @Transactional
    void patchNonExistingSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsSupplierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(smsSupplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsSupplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsSupplier() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the smsSupplier
        restSmsSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsSupplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return smsSupplierRepository.selectCount(null);
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

    protected SmsSupplier getPersistedSmsSupplier(SmsSupplier smsSupplier) {
        return smsSupplierRepository.findById(smsSupplier.getId()).orElseThrow();
    }

    protected void assertPersistedSmsSupplierToMatchAllProperties(SmsSupplier expectedSmsSupplier) {
        assertSmsSupplierAllPropertiesEquals(expectedSmsSupplier, getPersistedSmsSupplier(expectedSmsSupplier));
    }

    protected void assertPersistedSmsSupplierToMatchUpdatableProperties(SmsSupplier expectedSmsSupplier) {
        assertSmsSupplierAllUpdatablePropertiesEquals(expectedSmsSupplier, getPersistedSmsSupplier(expectedSmsSupplier));
    }
}
