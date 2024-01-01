package com.begcode.monolith.system.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.SmsProvider;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import com.begcode.monolith.system.service.mapper.SmsSupplierMapper;
import com.begcode.monolith.web.rest.TestUtil;
import com.begcode.monolith.web.rest.TestUtil;
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
 * Integration tests for the {@link SmsSupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        int databaseSizeBeforeCreate = smsSupplierRepository.findAll().size();
        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);
        restSmsSupplierMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeCreate + 1);
        SmsSupplier testSmsSupplier = smsSupplierList.get(smsSupplierList.size() - 1);
        assertThat(testSmsSupplier.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testSmsSupplier.getConfigData()).isEqualTo(DEFAULT_CONFIG_DATA);
        assertThat(testSmsSupplier.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testSmsSupplier.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testSmsSupplier.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void createSmsSupplierWithExistingId() throws Exception {
        // Create the SmsSupplier with an existing ID
        smsSupplier.setId(1L);
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        int databaseSizeBeforeCreate = smsSupplierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsSupplierMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeCreate);
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

        defaultSmsSupplierShouldBeFound("id.equals=" + id);
        defaultSmsSupplierShouldNotBeFound("id.notEquals=" + id);

        defaultSmsSupplierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSmsSupplierShouldNotBeFound("id.greaterThan=" + id);

        defaultSmsSupplierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSmsSupplierShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider equals to DEFAULT_PROVIDER
        defaultSmsSupplierShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the smsSupplierList where provider equals to UPDATED_PROVIDER
        defaultSmsSupplierShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultSmsSupplierShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the smsSupplierList where provider equals to UPDATED_PROVIDER
        defaultSmsSupplierShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where provider is not null
        defaultSmsSupplierShouldBeFound("provider.specified=true");

        // Get all the smsSupplierList where provider is null
        defaultSmsSupplierShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData equals to DEFAULT_CONFIG_DATA
        defaultSmsSupplierShouldBeFound("configData.equals=" + DEFAULT_CONFIG_DATA);

        // Get all the smsSupplierList where configData equals to UPDATED_CONFIG_DATA
        defaultSmsSupplierShouldNotBeFound("configData.equals=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData in DEFAULT_CONFIG_DATA or UPDATED_CONFIG_DATA
        defaultSmsSupplierShouldBeFound("configData.in=" + DEFAULT_CONFIG_DATA + "," + UPDATED_CONFIG_DATA);

        // Get all the smsSupplierList where configData equals to UPDATED_CONFIG_DATA
        defaultSmsSupplierShouldNotBeFound("configData.in=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData is not null
        defaultSmsSupplierShouldBeFound("configData.specified=true");

        // Get all the smsSupplierList where configData is null
        defaultSmsSupplierShouldNotBeFound("configData.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData contains DEFAULT_CONFIG_DATA
        defaultSmsSupplierShouldBeFound("configData.contains=" + DEFAULT_CONFIG_DATA);

        // Get all the smsSupplierList where configData contains UPDATED_CONFIG_DATA
        defaultSmsSupplierShouldNotBeFound("configData.contains=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByConfigDataNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where configData does not contain DEFAULT_CONFIG_DATA
        defaultSmsSupplierShouldNotBeFound("configData.doesNotContain=" + DEFAULT_CONFIG_DATA);

        // Get all the smsSupplierList where configData does not contain UPDATED_CONFIG_DATA
        defaultSmsSupplierShouldBeFound("configData.doesNotContain=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName equals to DEFAULT_SIGN_NAME
        defaultSmsSupplierShouldBeFound("signName.equals=" + DEFAULT_SIGN_NAME);

        // Get all the smsSupplierList where signName equals to UPDATED_SIGN_NAME
        defaultSmsSupplierShouldNotBeFound("signName.equals=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName in DEFAULT_SIGN_NAME or UPDATED_SIGN_NAME
        defaultSmsSupplierShouldBeFound("signName.in=" + DEFAULT_SIGN_NAME + "," + UPDATED_SIGN_NAME);

        // Get all the smsSupplierList where signName equals to UPDATED_SIGN_NAME
        defaultSmsSupplierShouldNotBeFound("signName.in=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName is not null
        defaultSmsSupplierShouldBeFound("signName.specified=true");

        // Get all the smsSupplierList where signName is null
        defaultSmsSupplierShouldNotBeFound("signName.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName contains DEFAULT_SIGN_NAME
        defaultSmsSupplierShouldBeFound("signName.contains=" + DEFAULT_SIGN_NAME);

        // Get all the smsSupplierList where signName contains UPDATED_SIGN_NAME
        defaultSmsSupplierShouldNotBeFound("signName.contains=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersBySignNameNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where signName does not contain DEFAULT_SIGN_NAME
        defaultSmsSupplierShouldNotBeFound("signName.doesNotContain=" + DEFAULT_SIGN_NAME);

        // Get all the smsSupplierList where signName does not contain UPDATED_SIGN_NAME
        defaultSmsSupplierShouldBeFound("signName.doesNotContain=" + UPDATED_SIGN_NAME);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark equals to DEFAULT_REMARK
        defaultSmsSupplierShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the smsSupplierList where remark equals to UPDATED_REMARK
        defaultSmsSupplierShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultSmsSupplierShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the smsSupplierList where remark equals to UPDATED_REMARK
        defaultSmsSupplierShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark is not null
        defaultSmsSupplierShouldBeFound("remark.specified=true");

        // Get all the smsSupplierList where remark is null
        defaultSmsSupplierShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark contains DEFAULT_REMARK
        defaultSmsSupplierShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the smsSupplierList where remark contains UPDATED_REMARK
        defaultSmsSupplierShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where remark does not contain DEFAULT_REMARK
        defaultSmsSupplierShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the smsSupplierList where remark does not contain UPDATED_REMARK
        defaultSmsSupplierShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled equals to DEFAULT_ENABLED
        defaultSmsSupplierShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the smsSupplierList where enabled equals to UPDATED_ENABLED
        defaultSmsSupplierShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultSmsSupplierShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the smsSupplierList where enabled equals to UPDATED_ENABLED
        defaultSmsSupplierShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllSmsSuppliersByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        // Get all the smsSupplierList where enabled is not null
        defaultSmsSupplierShouldBeFound("enabled.specified=true");

        // Get all the smsSupplierList where enabled is null
        defaultSmsSupplierShouldNotBeFound("enabled.specified=false");
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

        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
        SmsSupplier testSmsSupplier = smsSupplierList.get(smsSupplierList.size() - 1);
        assertThat(testSmsSupplier.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testSmsSupplier.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
        assertThat(testSmsSupplier.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testSmsSupplier.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsSupplier.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsSupplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsSupplierWithPatch() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();

        // Update the smsSupplier using partial update
        SmsSupplier partialUpdatedSmsSupplier = new SmsSupplier();
        partialUpdatedSmsSupplier.setId(smsSupplier.getId());

        partialUpdatedSmsSupplier.configData(UPDATED_CONFIG_DATA).remark(UPDATED_REMARK);

        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsSupplier))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
        SmsSupplier testSmsSupplier = smsSupplierList.get(smsSupplierList.size() - 1);
        assertThat(testSmsSupplier.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testSmsSupplier.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
        assertThat(testSmsSupplier.getSignName()).isEqualTo(DEFAULT_SIGN_NAME);
        assertThat(testSmsSupplier.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsSupplier.getEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateSmsSupplierWithPatch() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsSupplier))
            )
            .andExpect(status().isOk());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
        SmsSupplier testSmsSupplier = smsSupplierList.get(smsSupplierList.size() - 1);
        assertThat(testSmsSupplier.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testSmsSupplier.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
        assertThat(testSmsSupplier.getSignName()).isEqualTo(UPDATED_SIGN_NAME);
        assertThat(testSmsSupplier.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsSupplier.getEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsSupplierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsSupplier() throws Exception {
        int databaseSizeBeforeUpdate = smsSupplierRepository.findAll().size();
        smsSupplier.setId(longCount.incrementAndGet());

        // Create the SmsSupplier
        SmsSupplierDTO smsSupplierDTO = smsSupplierMapper.toDto(smsSupplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(smsSupplierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsSupplier in the database
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsSupplier() throws Exception {
        // Initialize the database
        smsSupplierRepository.save(smsSupplier);

        int databaseSizeBeforeDelete = smsSupplierRepository.findAll().size();

        // Delete the smsSupplier
        restSmsSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsSupplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmsSupplier> smsSupplierList = smsSupplierRepository.findAll();
        assertThat(smsSupplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
