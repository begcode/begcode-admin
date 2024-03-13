package com.begcode.monolith.oss.web.rest;

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OssConfigRepository ossConfigRepository;

    @Autowired
    private OssConfigMapper ossConfigMapper;

    @Autowired
    private MockMvc restOssConfigMockMvc;

    private OssConfig ossConfig;

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

    @Test
    @Transactional
    void createOssConfig() throws Exception {
        int databaseSizeBeforeCreate = ossConfigRepository.findAll().size();
        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);
        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeCreate + 1);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testOssConfig.getPlatform()).isEqualTo(DEFAULT_PLATFORM);
        assertThat(testOssConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testOssConfig.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testOssConfig.getConfigData()).isEqualTo(DEFAULT_CONFIG_DATA);
    }

    @Test
    @Transactional
    void createOssConfigWithExistingId() throws Exception {
        // Create the OssConfig with an existing ID
        ossConfig.setId(1L);
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        int databaseSizeBeforeCreate = ossConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = ossConfigRepository.findAll().size();
        // set the field null
        ossConfig.setProvider(null);

        // Create the OssConfig, which fails.
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlatformIsRequired() throws Exception {
        int databaseSizeBeforeTest = ossConfigRepository.findAll().size();
        // set the field null
        ossConfig.setPlatform(null);

        // Create the OssConfig, which fails.
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        restOssConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isBadRequest());

        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOssConfigs() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

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
        ossConfigRepository.save(ossConfig);

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
        ossConfigRepository.save(ossConfig);

        Long id = ossConfig.getId();

        defaultOssConfigShouldBeFound("id.equals=" + id);
        defaultOssConfigShouldNotBeFound("id.notEquals=" + id);

        defaultOssConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOssConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultOssConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOssConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where provider equals to DEFAULT_PROVIDER
        defaultOssConfigShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the ossConfigList where provider equals to UPDATED_PROVIDER
        defaultOssConfigShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultOssConfigShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the ossConfigList where provider equals to UPDATED_PROVIDER
        defaultOssConfigShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOssConfigsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where provider is not null
        defaultOssConfigShouldBeFound("provider.specified=true");

        // Get all the ossConfigList where provider is null
        defaultOssConfigShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where platform equals to DEFAULT_PLATFORM
        defaultOssConfigShouldBeFound("platform.equals=" + DEFAULT_PLATFORM);

        // Get all the ossConfigList where platform equals to UPDATED_PLATFORM
        defaultOssConfigShouldNotBeFound("platform.equals=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where platform in DEFAULT_PLATFORM or UPDATED_PLATFORM
        defaultOssConfigShouldBeFound("platform.in=" + DEFAULT_PLATFORM + "," + UPDATED_PLATFORM);

        // Get all the ossConfigList where platform equals to UPDATED_PLATFORM
        defaultOssConfigShouldNotBeFound("platform.in=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where platform is not null
        defaultOssConfigShouldBeFound("platform.specified=true");

        // Get all the ossConfigList where platform is null
        defaultOssConfigShouldNotBeFound("platform.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where platform contains DEFAULT_PLATFORM
        defaultOssConfigShouldBeFound("platform.contains=" + DEFAULT_PLATFORM);

        // Get all the ossConfigList where platform contains UPDATED_PLATFORM
        defaultOssConfigShouldNotBeFound("platform.contains=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByPlatformNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where platform does not contain DEFAULT_PLATFORM
        defaultOssConfigShouldNotBeFound("platform.doesNotContain=" + DEFAULT_PLATFORM);

        // Get all the ossConfigList where platform does not contain UPDATED_PLATFORM
        defaultOssConfigShouldBeFound("platform.doesNotContain=" + UPDATED_PLATFORM);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where enabled equals to DEFAULT_ENABLED
        defaultOssConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the ossConfigList where enabled equals to UPDATED_ENABLED
        defaultOssConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultOssConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the ossConfigList where enabled equals to UPDATED_ENABLED
        defaultOssConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllOssConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where enabled is not null
        defaultOssConfigShouldBeFound("enabled.specified=true");

        // Get all the ossConfigList where enabled is null
        defaultOssConfigShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where remark equals to DEFAULT_REMARK
        defaultOssConfigShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark equals to UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultOssConfigShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the ossConfigList where remark equals to UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where remark is not null
        defaultOssConfigShouldBeFound("remark.specified=true");

        // Get all the ossConfigList where remark is null
        defaultOssConfigShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where remark contains DEFAULT_REMARK
        defaultOssConfigShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark contains UPDATED_REMARK
        defaultOssConfigShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where remark does not contain DEFAULT_REMARK
        defaultOssConfigShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the ossConfigList where remark does not contain UPDATED_REMARK
        defaultOssConfigShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsEqualToSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where configData equals to DEFAULT_CONFIG_DATA
        defaultOssConfigShouldBeFound("configData.equals=" + DEFAULT_CONFIG_DATA);

        // Get all the ossConfigList where configData equals to UPDATED_CONFIG_DATA
        defaultOssConfigShouldNotBeFound("configData.equals=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsInShouldWork() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where configData in DEFAULT_CONFIG_DATA or UPDATED_CONFIG_DATA
        defaultOssConfigShouldBeFound("configData.in=" + DEFAULT_CONFIG_DATA + "," + UPDATED_CONFIG_DATA);

        // Get all the ossConfigList where configData equals to UPDATED_CONFIG_DATA
        defaultOssConfigShouldNotBeFound("configData.in=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where configData is not null
        defaultOssConfigShouldBeFound("configData.specified=true");

        // Get all the ossConfigList where configData is null
        defaultOssConfigShouldNotBeFound("configData.specified=false");
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where configData contains DEFAULT_CONFIG_DATA
        defaultOssConfigShouldBeFound("configData.contains=" + DEFAULT_CONFIG_DATA);

        // Get all the ossConfigList where configData contains UPDATED_CONFIG_DATA
        defaultOssConfigShouldNotBeFound("configData.contains=" + UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void getAllOssConfigsByConfigDataNotContainsSomething() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        // Get all the ossConfigList where configData does not contain DEFAULT_CONFIG_DATA
        defaultOssConfigShouldNotBeFound("configData.doesNotContain=" + DEFAULT_CONFIG_DATA);

        // Get all the ossConfigList where configData does not contain UPDATED_CONFIG_DATA
        defaultOssConfigShouldBeFound("configData.doesNotContain=" + UPDATED_CONFIG_DATA);
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
        ossConfigRepository.save(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testOssConfig.getPlatform()).isEqualTo(UPDATED_PLATFORM);
        assertThat(testOssConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void putNonExistingOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ossConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

        // Update the ossConfig using partial update
        OssConfig partialUpdatedOssConfig = new OssConfig();
        partialUpdatedOssConfig.setId(ossConfig.getId());

        partialUpdatedOssConfig.platform(UPDATED_PLATFORM).remark(UPDATED_REMARK).configData(UPDATED_CONFIG_DATA);

        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOssConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testOssConfig.getPlatform()).isEqualTo(UPDATED_PLATFORM);
        assertThat(testOssConfig.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void fullUpdateOssConfigWithPatch() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOssConfig))
            )
            .andExpect(status().isOk());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
        OssConfig testOssConfig = ossConfigList.get(ossConfigList.size() - 1);
        assertThat(testOssConfig.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testOssConfig.getPlatform()).isEqualTo(UPDATED_PLATFORM);
        assertThat(testOssConfig.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testOssConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testOssConfig.getConfigData()).isEqualTo(UPDATED_CONFIG_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ossConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOssConfig() throws Exception {
        int databaseSizeBeforeUpdate = ossConfigRepository.findAll().size();
        ossConfig.setId(longCount.incrementAndGet());

        // Create the OssConfig
        OssConfigDTO ossConfigDTO = ossConfigMapper.toDto(ossConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOssConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ossConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OssConfig in the database
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOssConfig() throws Exception {
        // Initialize the database
        ossConfigRepository.save(ossConfig);

        int databaseSizeBeforeDelete = ossConfigRepository.findAll().size();

        // Delete the ossConfig
        restOssConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, ossConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OssConfig> ossConfigList = ossConfigRepository.findAll();
        assertThat(ossConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
