package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.RegionCodeAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.RegionCodeLevel;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.domain.RegionCode;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.repository.RegionCodeRepository;
import com.begcode.monolith.settings.service.RegionCodeService;
import com.begcode.monolith.settings.service.dto.RegionCodeDTO;
import com.begcode.monolith.settings.service.mapper.RegionCodeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Integration tests for the {@link RegionCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
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

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

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
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);
        var returnedRegionCodeDTO = om.readValue(
            restRegionCodeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(regionCodeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RegionCodeDTO.class
        );

        // Validate the RegionCode in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRegionCode = regionCodeMapper.toEntity(returnedRegionCodeDTO);
        assertRegionCodeUpdatableFieldsEquals(returnedRegionCode, getPersistedRegionCode(returnedRegionCode));
    }

    @Test
    @Transactional
    void createRegionCodeWithExistingId() throws Exception {
        // Create the RegionCode with an existing ID
        regionCode.setId(1L);
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(regionCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
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

        defaultRegionCodeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRegionCodeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRegionCodeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name equals to
        defaultRegionCodeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name in
        defaultRegionCodeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name is not null
        defaultRegionCodeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name contains
        defaultRegionCodeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where name does not contain
        defaultRegionCodeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode equals to
        defaultRegionCodeFiltering("areaCode.equals=" + DEFAULT_AREA_CODE, "areaCode.equals=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode in
        defaultRegionCodeFiltering("areaCode.in=" + DEFAULT_AREA_CODE + "," + UPDATED_AREA_CODE, "areaCode.in=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode is not null
        defaultRegionCodeFiltering("areaCode.specified=true", "areaCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode contains
        defaultRegionCodeFiltering("areaCode.contains=" + DEFAULT_AREA_CODE, "areaCode.contains=" + UPDATED_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByAreaCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where areaCode does not contain
        defaultRegionCodeFiltering("areaCode.doesNotContain=" + UPDATED_AREA_CODE, "areaCode.doesNotContain=" + DEFAULT_AREA_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode equals to
        defaultRegionCodeFiltering("cityCode.equals=" + DEFAULT_CITY_CODE, "cityCode.equals=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode in
        defaultRegionCodeFiltering("cityCode.in=" + DEFAULT_CITY_CODE + "," + UPDATED_CITY_CODE, "cityCode.in=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode is not null
        defaultRegionCodeFiltering("cityCode.specified=true", "cityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode contains
        defaultRegionCodeFiltering("cityCode.contains=" + DEFAULT_CITY_CODE, "cityCode.contains=" + UPDATED_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where cityCode does not contain
        defaultRegionCodeFiltering("cityCode.doesNotContain=" + UPDATED_CITY_CODE, "cityCode.doesNotContain=" + DEFAULT_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName equals to
        defaultRegionCodeFiltering("mergerName.equals=" + DEFAULT_MERGER_NAME, "mergerName.equals=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName in
        defaultRegionCodeFiltering(
            "mergerName.in=" + DEFAULT_MERGER_NAME + "," + UPDATED_MERGER_NAME,
            "mergerName.in=" + UPDATED_MERGER_NAME
        );
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName is not null
        defaultRegionCodeFiltering("mergerName.specified=true", "mergerName.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName contains
        defaultRegionCodeFiltering("mergerName.contains=" + DEFAULT_MERGER_NAME, "mergerName.contains=" + UPDATED_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByMergerNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where mergerName does not contain
        defaultRegionCodeFiltering("mergerName.doesNotContain=" + UPDATED_MERGER_NAME, "mergerName.doesNotContain=" + DEFAULT_MERGER_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName equals to
        defaultRegionCodeFiltering("shortName.equals=" + DEFAULT_SHORT_NAME, "shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName in
        defaultRegionCodeFiltering("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME, "shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName is not null
        defaultRegionCodeFiltering("shortName.specified=true", "shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName contains
        defaultRegionCodeFiltering("shortName.contains=" + DEFAULT_SHORT_NAME, "shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where shortName does not contain
        defaultRegionCodeFiltering("shortName.doesNotContain=" + UPDATED_SHORT_NAME, "shortName.doesNotContain=" + DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode equals to
        defaultRegionCodeFiltering("zipCode.equals=" + DEFAULT_ZIP_CODE, "zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode in
        defaultRegionCodeFiltering("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE, "zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode is not null
        defaultRegionCodeFiltering("zipCode.specified=true", "zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode contains
        defaultRegionCodeFiltering("zipCode.contains=" + DEFAULT_ZIP_CODE, "zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where zipCode does not contain
        defaultRegionCodeFiltering("zipCode.doesNotContain=" + UPDATED_ZIP_CODE, "zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level equals to
        defaultRegionCodeFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level in
        defaultRegionCodeFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where level is not null
        defaultRegionCodeFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng equals to
        defaultRegionCodeFiltering("lng.equals=" + DEFAULT_LNG, "lng.equals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng in
        defaultRegionCodeFiltering("lng.in=" + DEFAULT_LNG + "," + UPDATED_LNG, "lng.in=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is not null
        defaultRegionCodeFiltering("lng.specified=true", "lng.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is greater than or equal to
        defaultRegionCodeFiltering("lng.greaterThanOrEqual=" + DEFAULT_LNG, "lng.greaterThanOrEqual=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is less than or equal to
        defaultRegionCodeFiltering("lng.lessThanOrEqual=" + DEFAULT_LNG, "lng.lessThanOrEqual=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsLessThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is less than
        defaultRegionCodeFiltering("lng.lessThan=" + UPDATED_LNG, "lng.lessThan=" + DEFAULT_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lng is greater than
        defaultRegionCodeFiltering("lng.greaterThan=" + SMALLER_LNG, "lng.greaterThan=" + DEFAULT_LNG);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat equals to
        defaultRegionCodeFiltering("lat.equals=" + DEFAULT_LAT, "lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat in
        defaultRegionCodeFiltering("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT, "lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is not null
        defaultRegionCodeFiltering("lat.specified=true", "lat.specified=false");
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is greater than or equal to
        defaultRegionCodeFiltering("lat.greaterThanOrEqual=" + DEFAULT_LAT, "lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is less than or equal to
        defaultRegionCodeFiltering("lat.lessThanOrEqual=" + DEFAULT_LAT, "lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is less than
        defaultRegionCodeFiltering("lat.lessThan=" + UPDATED_LAT, "lat.lessThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllRegionCodesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        // Get all the regionCodeList where lat is greater than
        defaultRegionCodeFiltering("lat.greaterThan=" + SMALLER_LAT, "lat.greaterThan=" + DEFAULT_LAT);
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

    private void defaultRegionCodeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRegionCodeShouldBeFound(shouldBeFound);
        defaultRegionCodeShouldNotBeFound(shouldNotBeFound);
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

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(regionCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRegionCodeToMatchAllProperties(updatedRegionCode);
    }

    @Test
    @Transactional
    void putNonExistingRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regionCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(regionCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegionCodeWithPatch() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the regionCode using partial update
        RegionCode partialUpdatedRegionCode = new RegionCode();
        partialUpdatedRegionCode.setId(regionCode.getId());

        partialUpdatedRegionCode
            .areaCode(UPDATED_AREA_CODE)
            .cityCode(UPDATED_CITY_CODE)
            .shortName(UPDATED_SHORT_NAME)
            .level(UPDATED_LEVEL)
            .lng(UPDATED_LNG)
            .lat(UPDATED_LAT);

        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegionCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegionCode))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegionCodeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRegionCode, regionCode),
            getPersistedRegionCode(regionCode)
        );
    }

    @Test
    @Transactional
    void fullUpdateRegionCodeWithPatch() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedRegionCode))
            )
            .andExpect(status().isOk());

        // Validate the RegionCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegionCodeUpdatableFieldsEquals(partialUpdatedRegionCode, getPersistedRegionCode(partialUpdatedRegionCode));
    }

    @Test
    @Transactional
    void patchNonExistingRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regionCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(regionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegionCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        regionCode.setId(longCount.incrementAndGet());

        // Create the RegionCode
        RegionCodeDTO regionCodeDTO = regionCodeMapper.toDto(regionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegionCodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(regionCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegionCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegionCode() throws Exception {
        // Initialize the database
        regionCodeRepository.save(regionCode);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the regionCode
        restRegionCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, regionCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return regionCodeRepository.selectCount(null);
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

    protected RegionCode getPersistedRegionCode(RegionCode regionCode) {
        return regionCodeRepository.findById(regionCode.getId()).orElseThrow();
    }

    protected void assertPersistedRegionCodeToMatchAllProperties(RegionCode expectedRegionCode) {
        assertRegionCodeAllPropertiesEquals(expectedRegionCode, getPersistedRegionCode(expectedRegionCode));
    }

    protected void assertPersistedRegionCodeToMatchUpdatableProperties(RegionCode expectedRegionCode) {
        assertRegionCodeAllUpdatablePropertiesEquals(expectedRegionCode, getPersistedRegionCode(expectedRegionCode));
    }
}
