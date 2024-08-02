package com.begcode.monolith.oss.web.rest;

import static com.begcode.monolith.oss.domain.OssConfigAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.OssProvider;
import com.begcode.monolith.oss.domain.OssConfig;
import com.begcode.monolith.oss.repository.OssConfigRepository;
import com.begcode.monolith.oss.service.dto.OssConfigDTO;
import com.begcode.monolith.oss.service.mapper.OssConfigMapper;
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
 * Integration tests for the {@link OssConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class OssConfigResourceIT {

    private static final OssProvider DEFAULT_PROVIDER = OssProvider.LOCAL;
    private static final OssProvider UPDATED_PROVIDER = OssProvider.MINIO;

    private static final String DEFAULT_PLATFORM = "AAAAAAAAAA";
    private static final String UPDATED_PLATFORM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_DATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/oss-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OssConfigRepository ossConfigRepository;

    @Autowired
    private OssConfigMapper ossConfigMapper;

    @Autowired
    private MockMvc restOssConfigMockMvc;

    private OssConfig ossConfig;

    private OssConfig insertedOssConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OssConfig createEntity() {
        OssConfig ossConfig = new OssConfig()
            .provider(DEFAULT_PROVIDER)
            .platform(DEFAULT_PLATFORM)
            .enabled(DEFAULT_ENABLED)
            .remark(DEFAULT_REMARK)
            .configData(DEFAULT_CONFIG_DATA);
        return ossConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OssConfig createUpdatedEntity() {
        OssConfig ossConfig = new OssConfig()
            .provider(UPDATED_PROVIDER)
            .platform(UPDATED_PLATFORM)
            .enabled(UPDATED_ENABLED)
            .remark(UPDATED_REMARK)
            .configData(UPDATED_CONFIG_DATA);
        return ossConfig;
    }

    @BeforeEach
    public void initTest() {
        ossConfig = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOssConfig != null) {
            ossConfigRepository.deleteById(insertedOssConfig.getId());
            insertedOssConfig = null;
        }
    }

    @Test
    @Transactional
    void createOssConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);
        var returnedOssConfigDTO = om.readValue(
            restOssConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ossConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OssConfigDTO.class
        );

        // Validate the OssConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOssConfig = ossConfigMapper.toEntity(returnedOssConfigDTO);
        assertOssConfigUpdatableFieldsEquals(returnedOssConfig, getPersistedOssConfig(returnedOssConfig));

        insertedOssConfig = returnedOssConfig;
    }

    @Test
    @Transactional
    void createOssConfigWithExistingId() throws Exception {
        // Create the OssConfig with an existing ID
        ossConfig.setId(1L);
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ossConfig.setProvider(null);

        // Create the OssConfig, which fails.
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlatformIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ossConfig.setPlatform(null);

        // Create the OssConfig, which fails.
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOssConfigs() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ossConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].configData").value(hasItem(DEFAULT_CONFIG_DATA)));
    }

    @Test
    @Transactional
    void getOssConfig() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get the ossConfig
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, ossConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ossConfig.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER.toString()))
            .andExpect(jsonPath("$.platform").value(DEFAULT_PLATFORM))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.configData").value(DEFAULT_CONFIG_DATA));
    }

    @Test
    @Transactional
    void getOssConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        Long id = ossConfig.getId();

        defaultOssConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOssConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOssConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where provider equals to
        defaultOssConfigFiltering("provider.equals=" + DEFAULT_PROVIDER, "provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where provider in
        defaultOssConfigFiltering("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER, "provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where provider is not null
        defaultOssConfigFiltering("provider.specified=true", "provider.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where platform equals to
        defaultOssConfigFiltering("platform.equals=" + DEFAULT_PLATFORM, "platform.equals=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where platform in
        defaultOssConfigFiltering("platform.in=" + DEFAULT_PLATFORM + "," + UPDATED_PLATFORM, "platform.in=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where platform is not null
        defaultOssConfigFiltering("platform.specified=true", "platform.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where platform contains
        defaultOssConfigFiltering("platform.contains=" + DEFAULT_PLATFORM, "platform.contains=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where platform does not contain
        defaultOssConfigFiltering("platform.doesNotContain=" + UPDATED_PLATFORM, "platform.doesNotContain=" + DEFAULT_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where enabled equals to
        defaultOssConfigFiltering("enabled.equals=" + DEFAULT_ENABLED, "enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where enabled in
        defaultOssConfigFiltering("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED, "enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where enabled is not null
        defaultOssConfigFiltering("enabled.specified=true", "enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where remark equals to
        defaultOssConfigFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where remark in
        defaultOssConfigFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where remark is not null
        defaultOssConfigFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where remark contains
        defaultOssConfigFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where remark does not contain
        defaultOssConfigFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where configData equals to
        defaultOssConfigFiltering("configData.equals=" + DEFAULT_CONFIG_DATA, "configData.equals=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where configData in
        defaultOssConfigFiltering(
            "configData.in=" + DEFAULT_CONFIG_DATA + "," + UPDATED_CONFIG_DATA,
            "configData.in=" + UPDATED_CONFIG_DATA
        );
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where configData is not null
        defaultOssConfigFiltering("configData.specified=true", "configData.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where configData contains
        defaultOssConfigFiltering("configData.contains=" + DEFAULT_CONFIG_DATA, "configData.contains=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        // Get all the ossConfigList where configData does not contain
        defaultOssConfigFiltering("configData.doesNotContain=" + UPDATED_CONFIG_DATA, "configData.doesNotContain=" + DEFAULT_CONFIG_DATA);
    }

    private void defaultOssConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOssConfigShouldBeFound(shouldBeFound);
        defaultOssConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOssConfigShouldBeFound(String filter) throws Exception {
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ossConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER.toString())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].configData").value(hasItem(DEFAULT_CONFIG_DATA)));

        // Check, that the count call also returns 1
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOssConfigShouldNotBeFound(String filter) throws Exception {
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOssConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOssConfig() throws Exception {
        // Get the ossConfig
        restOssConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOssConfig() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ossConfig
        OssConfig updatedOssConfig = ossConfigRepository.findById(ossConfig.getId()).orElseThrow();
        updatedOssConfig
            .provider(UPDATED_PROVIDER)
            .platform(UPDATED_PLATFORM)
            .enabled(UPDATED_ENABLED)
            .remark(UPDATED_REMARK)
            .configData(UPDATED_CONFIG_DATA);
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(updatedOssConfig);

        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ossConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOssConfigToMatchAllProperties(updatedOssConfig);
    }

    @Test
    @Transactional
    void putNonExistingOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ossConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ossConfig using partial update
        OssConfig partialUpdatedOssConfig = new OssConfig();
        partialUpdatedOssConfig.setId(ossConfig.getId());

        partialUpdatedOssConfig.provider(UPDATED_PROVIDER).enabled(UPDATED_ENABLED).configData(UPDATED_CONFIG_DATA);

        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOssConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOssConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOssConfig, ossConfig),
            getPersistedOssConfig(ossConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ossConfig using partial update
        OssConfig partialUpdatedOssConfig = new OssConfig();
        partialUpdatedOssConfig.setId(ossConfig.getId());

        partialUpdatedOssConfig
            .provider(UPDATED_PROVIDER)
            .platform(UPDATED_PLATFORM)
            .enabled(UPDATED_ENABLED)
            .remark(UPDATED_REMARK)
            .configData(UPDATED_CONFIG_DATA);

        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOssConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOssConfigUpdatableFieldsEquals(partialUpdatedOssConfig, getPersistedOssConfig(partialUpdatedOssConfig));
    }

    @Test
    @Transactional
    void patchNonExistingOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOssConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ossConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOssConfig() throws Exception {
        // Initialize the database
        insertedOssConfig = ossConfigRepository.saveAndGet(ossConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ossConfig
        restOssConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, ossConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ossConfigRepository.selectCount(null);
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

    protected OssConfig getPersistedOssConfig(OssConfig ossConfig) {
        return ossConfigRepository.findById(ossConfig.getId()).orElseThrow();
    }

    protected void assertPersistedOssConfigToMatchAllProperties(OssConfig expectedOssConfig) {
        assertOssConfigAllPropertiesEquals(expectedOssConfig, getPersistedOssConfig(expectedOssConfig));
    }

    protected void assertPersistedOssConfigToMatchUpdatableProperties(OssConfig expectedOssConfig) {
        assertOssConfigAllUpdatablePropertiesEquals(expectedOssConfig, getPersistedOssConfig(expectedOssConfig));
    }
}
