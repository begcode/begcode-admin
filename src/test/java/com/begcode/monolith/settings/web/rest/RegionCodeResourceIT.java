package com.begcode.monolith.settings.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.RegionCodeLevel;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.RegionCodeService;
import com.begcode.monolith.settings.service.dto.RegionCodeDTO;
import com.begcode.monolith.settings.service.mapper.RegionCodeMapper;
import com.begcode.monolith.web.rest.TestUtil;
import com.begcode.monolith.web.rest.TestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RegionCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegionCodeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AREA_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MERGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MERGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final RegionCodeLevel DEFAULT_LEVEL = RegionCodeLevel.PROVINCE;
    private static final RegionCodeLevel UPDATED_LEVEL = RegionCodeLevel.CITY;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;
    private static final Double SMALLER_LNG = 1D - 1D;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/region-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegionCodeRepository regionCodeRepository;

    @Mock
    private RegionCodeRepository regionCodeRepositoryMock;

    @Autowired
    private RegionCodeMapper regionCodeMapper;

    @Mock
    private RegionCodeService regionCodeServiceMock;

    @Autowired
    private MockMvc restRegionCodeMockMvc;

    private RegionCode regionCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegionCode createEntity() {
        RegionCode regionCode = new RegionCode()
            .name(DEFAULT_NAME)
            .areaCode(DEFAULT_AREA_CODE)
            .cityCode(DEFAULT_CITY_CODE)
            .mergerName(DEFAULT_MERGER_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .zipCode(DEFAULT_ZIP_CODE)
            .level(DEFAULT_LEVEL)
            .lng(DEFAULT_LNG)
            .lat(DEFAULT_LAT);
        return regionCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegionCode createUpdatedEntity() {
        RegionCode regionCode = new RegionCode()
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);
        return regionCode;
    }

    @BeforeEach
    public void initTest() {
        regionCode = createEntity();
    }

    @Test
    @Transactional
    void createRegionCode() throws Exception {
        int databaseSizeBeforeCreate = regionCodeRepository.findAll().size();
        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);
        restRegionCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeCreate + 1);
        RegionCode testRegionCode = regionCodeList.get(regionCodeList.size() - 1);
        assertThat(testRegionCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegionCode.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testRegionCode.getCityCode()).isEqualTo(DEFAULT_CITY_CODE);
        assertThat(testRegionCode.getMergerName()).isEqualTo(DEFAULT_MERGER_NAME);
        assertThat(testRegionCode.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testRegionCode.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testRegionCode.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testRegionCode.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testRegionCode.getLat()).isEqualTo(DEFAULT_LAT);
    }

    @Test
    @Transactional
    void createRegionCodeWithExistingId() throws Exception {
        // Create the RegionCode with an existing ID
        regionCode.setId(1L);
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        int databaseSizeBeforeCreate = regionCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRegionCodes() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].mergerName").value(hasItem(DEFAULT_MERGER_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegionCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(regionCodeServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restRegionCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(regionCodeServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRegionCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(regionCodeServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restRegionCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(regionCodeRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getRegionCode() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get the regionCode
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, regionCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regionCode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE))
            .andExpect(jsonPath("$.cityCode").value(DEFAULT_CITY_CODE))
            .andExpect(jsonPath("$.mergerName").value(DEFAULT_MERGER_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()));
    }

    @Test
    @Transactional
    void getRegionCodesByIdFiltering() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        Long id = regionCode.getId();

        defaultRegionCodeShouldBeFound("id.equals=" + id);
        defaultRegionCodeShouldNotBeFound("id.notEquals=" + id);

        defaultRegionCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegionCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultRegionCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegionCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name equals to DEFAULT_NAME
        defaultRegionCodeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the regionCodeList where name equals to UPDATED_NAME
        defaultRegionCodeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRegionCodeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the regionCodeList where name equals to UPDATED_NAME
        defaultRegionCodeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name is not null
        defaultRegionCodeShouldBeFound("name.specified=true");

        // Get all the regionCodeList where name is null
        defaultRegionCodeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name contains DEFAULT_NAME
        defaultRegionCodeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the regionCodeList where name contains UPDATED_NAME
        defaultRegionCodeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name does not contain DEFAULT_NAME
        defaultRegionCodeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the regionCodeList where name does not contain UPDATED_NAME
        defaultRegionCodeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode equals to DEFAULT_AREA_CODE
        defaultRegionCodeShouldBeFound("areaCode.equals=" + DEFAULT_AREA_CODE);

        // Get all the regionCodeList where areaCode equals to UPDATED_AREA_CODE
        defaultRegionCodeShouldNotBeFound("areaCode.equals=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode in DEFAULT_AREA_CODE or UPDATED_AREA_CODE
        defaultRegionCodeShouldBeFound("areaCode.in=" + DEFAULT_AREA_CODE + "," + UPDATED_AREA_CODE);

        // Get all the regionCodeList where areaCode equals to UPDATED_AREA_CODE
        defaultRegionCodeShouldNotBeFound("areaCode.in=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode is not null
        defaultRegionCodeShouldBeFound("areaCode.specified=true");

        // Get all the regionCodeList where areaCode is null
        defaultRegionCodeShouldNotBeFound("areaCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode contains DEFAULT_AREA_CODE
        defaultRegionCodeShouldBeFound("areaCode.contains=" + DEFAULT_AREA_CODE);

        // Get all the regionCodeList where areaCode contains UPDATED_AREA_CODE
        defaultRegionCodeShouldNotBeFound("areaCode.contains=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode does not contain DEFAULT_AREA_CODE
        defaultRegionCodeShouldNotBeFound("areaCode.doesNotContain=" + DEFAULT_AREA_CODE);

        // Get all the regionCodeList where areaCode does not contain UPDATED_AREA_CODE
        defaultRegionCodeShouldBeFound("areaCode.doesNotContain=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode equals to DEFAULT_CITY_CODE
        defaultRegionCodeShouldBeFound("cityCode.equals=" + DEFAULT_CITY_CODE);

        // Get all the regionCodeList where cityCode equals to UPDATED_CITY_CODE
        defaultRegionCodeShouldNotBeFound("cityCode.equals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode in DEFAULT_CITY_CODE or UPDATED_CITY_CODE
        defaultRegionCodeShouldBeFound("cityCode.in=" + DEFAULT_CITY_CODE + "," + UPDATED_CITY_CODE);

        // Get all the regionCodeList where cityCode equals to UPDATED_CITY_CODE
        defaultRegionCodeShouldNotBeFound("cityCode.in=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode is not null
        defaultRegionCodeShouldBeFound("cityCode.specified=true");

        // Get all the regionCodeList where cityCode is null
        defaultRegionCodeShouldNotBeFound("cityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode contains DEFAULT_CITY_CODE
        defaultRegionCodeShouldBeFound("cityCode.contains=" + DEFAULT_CITY_CODE);

        // Get all the regionCodeList where cityCode contains UPDATED_CITY_CODE
        defaultRegionCodeShouldNotBeFound("cityCode.contains=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode does not contain DEFAULT_CITY_CODE
        defaultRegionCodeShouldNotBeFound("cityCode.doesNotContain=" + DEFAULT_CITY_CODE);

        // Get all the regionCodeList where cityCode does not contain UPDATED_CITY_CODE
        defaultRegionCodeShouldBeFound("cityCode.doesNotContain=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName equals to DEFAULT_MERGER_NAME
        defaultRegionCodeShouldBeFound("mergerName.equals=" + DEFAULT_MERGER_NAME);

        // Get all the regionCodeList where mergerName equals to UPDATED_MERGER_NAME
        defaultRegionCodeShouldNotBeFound("mergerName.equals=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName in DEFAULT_MERGER_NAME or UPDATED_MERGER_NAME
        defaultRegionCodeShouldBeFound("mergerName.in=" + DEFAULT_MERGER_NAME + "," + UPDATED_MERGER_NAME);

        // Get all the regionCodeList where mergerName equals to UPDATED_MERGER_NAME
        defaultRegionCodeShouldNotBeFound("mergerName.in=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName is not null
        defaultRegionCodeShouldBeFound("mergerName.specified=true");

        // Get all the regionCodeList where mergerName is null
        defaultRegionCodeShouldNotBeFound("mergerName.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName contains DEFAULT_MERGER_NAME
        defaultRegionCodeShouldBeFound("mergerName.contains=" + DEFAULT_MERGER_NAME);

        // Get all the regionCodeList where mergerName contains UPDATED_MERGER_NAME
        defaultRegionCodeShouldNotBeFound("mergerName.contains=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName does not contain DEFAULT_MERGER_NAME
        defaultRegionCodeShouldNotBeFound("mergerName.doesNotContain=" + DEFAULT_MERGER_NAME);

        // Get all the regionCodeList where mergerName does not contain UPDATED_MERGER_NAME
        defaultRegionCodeShouldBeFound("mergerName.doesNotContain=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName equals to DEFAULT_SHORT_NAME
        defaultRegionCodeShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the regionCodeList where shortName equals to UPDATED_SHORT_NAME
        defaultRegionCodeShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultRegionCodeShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the regionCodeList where shortName equals to UPDATED_SHORT_NAME
        defaultRegionCodeShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName is not null
        defaultRegionCodeShouldBeFound("shortName.specified=true");

        // Get all the regionCodeList where shortName is null
        defaultRegionCodeShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName contains DEFAULT_SHORT_NAME
        defaultRegionCodeShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the regionCodeList where shortName contains UPDATED_SHORT_NAME
        defaultRegionCodeShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName does not contain DEFAULT_SHORT_NAME
        defaultRegionCodeShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the regionCodeList where shortName does not contain UPDATED_SHORT_NAME
        defaultRegionCodeShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode equals to DEFAULT_ZIP_CODE
        defaultRegionCodeShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the regionCodeList where zipCode equals to UPDATED_ZIP_CODE
        defaultRegionCodeShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultRegionCodeShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the regionCodeList where zipCode equals to UPDATED_ZIP_CODE
        defaultRegionCodeShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode is not null
        defaultRegionCodeShouldBeFound("zipCode.specified=true");

        // Get all the regionCodeList where zipCode is null
        defaultRegionCodeShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode contains DEFAULT_ZIP_CODE
        defaultRegionCodeShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the regionCodeList where zipCode contains UPDATED_ZIP_CODE
        defaultRegionCodeShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultRegionCodeShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the regionCodeList where zipCode does not contain UPDATED_ZIP_CODE
        defaultRegionCodeShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level equals to DEFAULT_LEVEL
        defaultRegionCodeShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the regionCodeList where level equals to UPDATED_LEVEL
        defaultRegionCodeShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultRegionCodeShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the regionCodeList where level equals to UPDATED_LEVEL
        defaultRegionCodeShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level is not null
        defaultRegionCodeShouldBeFound("level.specified=true");

        // Get all the regionCodeList where level is null
        defaultRegionCodeShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng equals to DEFAULT_LNG
        defaultRegionCodeShouldBeFound("lng.equals=" + DEFAULT_LNG);

        // Get all the regionCodeList where lng equals to UPDATED_LNG
        defaultRegionCodeShouldNotBeFound("lng.equals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng in DEFAULT_LNG or UPDATED_LNG
        defaultRegionCodeShouldBeFound("lng.in=" + DEFAULT_LNG + "," + UPDATED_LNG);

        // Get all the regionCodeList where lng equals to UPDATED_LNG
        defaultRegionCodeShouldNotBeFound("lng.in=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is not null
        defaultRegionCodeShouldBeFound("lng.specified=true");

        // Get all the regionCodeList where lng is null
        defaultRegionCodeShouldNotBeFound("lng.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is greater than or equal to DEFAULT_LNG
        defaultRegionCodeShouldBeFound("lng.greaterThanOrEqual=" + DEFAULT_LNG);

        // Get all the regionCodeList where lng is greater than or equal to UPDATED_LNG
        defaultRegionCodeShouldNotBeFound("lng.greaterThanOrEqual=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is less than or equal to DEFAULT_LNG
        defaultRegionCodeShouldBeFound("lng.lessThanOrEqual=" + DEFAULT_LNG);

        // Get all the regionCodeList where lng is less than or equal to SMALLER_LNG
        defaultRegionCodeShouldNotBeFound("lng.lessThanOrEqual=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsLessThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is less than DEFAULT_LNG
        defaultRegionCodeShouldNotBeFound("lng.lessThan=" + DEFAULT_LNG);

        // Get all the regionCodeList where lng is less than UPDATED_LNG
        defaultRegionCodeShouldBeFound("lng.lessThan=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is greater than DEFAULT_LNG
        defaultRegionCodeShouldNotBeFound("lng.greaterThan=" + DEFAULT_LNG);

        // Get all the regionCodeList where lng is greater than SMALLER_LNG
        defaultRegionCodeShouldBeFound("lng.greaterThan=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat equals to DEFAULT_LAT
        defaultRegionCodeShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the regionCodeList where lat equals to UPDATED_LAT
        defaultRegionCodeShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultRegionCodeShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the regionCodeList where lat equals to UPDATED_LAT
        defaultRegionCodeShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is not null
        defaultRegionCodeShouldBeFound("lat.specified=true");

        // Get all the regionCodeList where lat is null
        defaultRegionCodeShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is greater than or equal to DEFAULT_LAT
        defaultRegionCodeShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the regionCodeList where lat is greater than or equal to UPDATED_LAT
        defaultRegionCodeShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is less than or equal to DEFAULT_LAT
        defaultRegionCodeShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the regionCodeList where lat is less than or equal to SMALLER_LAT
        defaultRegionCodeShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is less than DEFAULT_LAT
        defaultRegionCodeShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the regionCodeList where lat is less than UPDATED_LAT
        defaultRegionCodeShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is greater than DEFAULT_LAT
        defaultRegionCodeShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the regionCodeList where lat is greater than SMALLER_LAT
        defaultRegionCodeShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByParentIsEqualToSomething() throws Exception {
        RegionCode parent = RegionCodeResourceIT.createEntity();
        regionCode.setParent(parent);
        regionCodeRepository.insert(regionCode);
        Long parentId = parent.getId();
        // Get all the regionCodeList where parent equals to parentId
        defaultRegionCodeShouldBeFound("parentId.equals=" + parentId);

        // Get all the regionCodeList where parent equals to (parentId + 1)
        defaultRegionCodeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionCodeShouldBeFound(String filter) throws Exception {
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
            .andExpect(jsonPath("$.[*].cityCode").value(hasItem(DEFAULT_CITY_CODE)))
            .andExpect(jsonPath("$.[*].mergerName").value(hasItem(DEFAULT_MERGER_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())));

        // Check, that the count call also returns 1
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionCodeShouldNotBeFound(String filter) throws Exception {
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegionCode() throws Exception {
        // Get the regionCode
        restRegionCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegionCode() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();

        // Update the regionCode
        RegionCode updatedRegionCode = regionCodeRepository.findById(regionCode.getId()).orElseThrow();
        updatedRegionCode
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(updatedRegionCode);

        restRegionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regionCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
        RegionCode testRegionCode = regionCodeList.get(regionCodeList.size() - 1);
        assertThat(testRegionCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegionCode.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testRegionCode.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testRegionCode.getMergerName()).isEqualTo(UPDATED_MERGER_NAME);
        assertThat(testRegionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testRegionCode.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testRegionCode.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testRegionCode.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testRegionCode.getLat()).isEqualTo(UPDATED_LAT);
    }

    @Test
    @Transactional
    void putNonExistingRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regionCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regionCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegionCodeWithPatch() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();

        // Update the regionCode using partial update
        RegionCode partialUpdatedRegionCode = new RegionCode();
        partialUpdatedRegionCode.setId(regionCode.getId());

        partialUpdatedRegionCode.name(UPDATED_NAME).areaCode(UPDATED_AREA_CODE).shortName(UPDATED_SHORT_NAME).zipCode(UPDATED_ZIP_CODE);

        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegionCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegionCode))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
        RegionCode testRegionCode = regionCodeList.get(regionCodeList.size() - 1);
        assertThat(testRegionCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegionCode.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testRegionCode.getCityCode()).isEqualTo(DEFAULT_CITY_CODE);
        assertThat(testRegionCode.getMergerName()).isEqualTo(DEFAULT_MERGER_NAME);
        assertThat(testRegionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testRegionCode.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testRegionCode.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testRegionCode.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testRegionCode.getLat()).isEqualTo(DEFAULT_LAT);
    }

    @Test
    @Transactional
    void fullUpdateRegionCodeWithPatch() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();

        // Update the regionCode using partial update
        RegionCode partialUpdatedRegionCode = new RegionCode();
        partialUpdatedRegionCode.setId(regionCode.getId());

        partialUpdatedRegionCode
            .name(UPDATED_NAME)
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .mergerName(UPDATED_MERGER_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .zipCode(UPDATED_ZIP_CODE)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);

        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegionCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegionCode))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
        RegionCode testRegionCode = regionCodeList.get(regionCodeList.size() - 1);
        assertThat(testRegionCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegionCode.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testRegionCode.getCityCode()).isEqualTo(UPDATED_CITY_CODE);
        assertThat(testRegionCode.getMergerName()).isEqualTo(UPDATED_MERGER_NAME);
        assertThat(testRegionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testRegionCode.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testRegionCode.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testRegionCode.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testRegionCode.getLat()).isEqualTo(UPDATED_LAT);
    }

    @Test
    @Transactional
    void patchNonExistingRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regionCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegionCode() throws Exception {
        int databaseSizeBeforeUpdate = regionCodeRepository.findAll().size();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(regionCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegionCode in the database
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegionCode() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        int databaseSizeBeforeDelete = regionCodeRepository.findAll().size();

        // Delete the regionCode
        restRegionCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, regionCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegionCode> regionCodeList = regionCodeRepository.findAll();
        assertThat(regionCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
