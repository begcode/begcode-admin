package com.begcode.monolith.settings.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.CommonFieldType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.CommonFieldDataRepository;
import com.begcode.monolith.settings.service.dto.CommonFieldDataDTO;
import com.begcode.monolith.settings.service.mapper.CommonFieldDataMapper;
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
 * Integration tests for the {@link CommonFieldDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class CommonFieldDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final CommonFieldType DEFAULT_VALUE_TYPE = CommonFieldType.INTEGER;
    private static final CommonFieldType UPDATED_VALUE_TYPE = CommonFieldType.LONG;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_VALUE = 1;
    private static final Integer UPDATED_SORT_VALUE = 2;
    private static final Integer SMALLER_SORT_VALUE = 1 - 1;

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final String DEFAULT_OWNER_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_OWNER_ENTITY_ID = 1L;
    private static final Long UPDATED_OWNER_ENTITY_ID = 2L;
    private static final Long SMALLER_OWNER_ENTITY_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/common-field-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonFieldDataRepository commonFieldDataRepository;

    @Autowired
    private CommonFieldDataMapper commonFieldDataMapper;

    @Autowired
    private MockMvc restCommonFieldDataMockMvc;

    private CommonFieldData commonFieldData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonFieldData createEntity() {
        CommonFieldData commonFieldData = new CommonFieldData()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .label(DEFAULT_LABEL)
            .valueType(DEFAULT_VALUE_TYPE)
            .remark(DEFAULT_REMARK)
            .sortValue(DEFAULT_SORT_VALUE)
            .disabled(DEFAULT_DISABLED)
            .ownerEntityName(DEFAULT_OWNER_ENTITY_NAME)
            .ownerEntityId(DEFAULT_OWNER_ENTITY_ID);
        return commonFieldData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonFieldData createUpdatedEntity() {
        CommonFieldData commonFieldData = new CommonFieldData()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .label(UPDATED_LABEL)
            .valueType(UPDATED_VALUE_TYPE)
            .remark(UPDATED_REMARK)
            .sortValue(UPDATED_SORT_VALUE)
            .disabled(UPDATED_DISABLED)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID);
        return commonFieldData;
    }

    @BeforeEach
    public void initTest() {
        commonFieldData = createEntity();
    }

    @Test
    @Transactional
    void createCommonFieldData() throws Exception {
        int databaseSizeBeforeCreate = commonFieldDataRepository.findAll().size();
        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);
        restCommonFieldDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeCreate + 1);
        CommonFieldData testCommonFieldData = commonFieldDataList.get(commonFieldDataList.size() - 1);
        assertThat(testCommonFieldData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonFieldData.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCommonFieldData.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCommonFieldData.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testCommonFieldData.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testCommonFieldData.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testCommonFieldData.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testCommonFieldData.getOwnerEntityName()).isEqualTo(DEFAULT_OWNER_ENTITY_NAME);
        assertThat(testCommonFieldData.getOwnerEntityId()).isEqualTo(DEFAULT_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void createCommonFieldDataWithExistingId() throws Exception {
        // Create the CommonFieldData with an existing ID
        commonFieldData.setId(1L);
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        int databaseSizeBeforeCreate = commonFieldDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonFieldDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommonFieldData() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonFieldData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ownerEntityName").value(hasItem(DEFAULT_OWNER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].ownerEntityId").value(hasItem(DEFAULT_OWNER_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    void getCommonFieldData() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get the commonFieldData
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL_ID, commonFieldData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonFieldData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.sortValue").value(DEFAULT_SORT_VALUE))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.ownerEntityName").value(DEFAULT_OWNER_ENTITY_NAME))
            .andExpect(jsonPath("$.ownerEntityId").value(DEFAULT_OWNER_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    void getCommonFieldDataByIdFiltering() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        Long id = commonFieldData.getId();

        defaultCommonFieldDataShouldBeFound("id.equals=" + id);
        defaultCommonFieldDataShouldNotBeFound("id.notEquals=" + id);

        defaultCommonFieldDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommonFieldDataShouldNotBeFound("id.greaterThan=" + id);

        defaultCommonFieldDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommonFieldDataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name equals to DEFAULT_NAME
        defaultCommonFieldDataShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the commonFieldDataList where name equals to UPDATED_NAME
        defaultCommonFieldDataShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCommonFieldDataShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the commonFieldDataList where name equals to UPDATED_NAME
        defaultCommonFieldDataShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name is not null
        defaultCommonFieldDataShouldBeFound("name.specified=true");

        // Get all the commonFieldDataList where name is null
        defaultCommonFieldDataShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name contains DEFAULT_NAME
        defaultCommonFieldDataShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the commonFieldDataList where name contains UPDATED_NAME
        defaultCommonFieldDataShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name does not contain DEFAULT_NAME
        defaultCommonFieldDataShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the commonFieldDataList where name does not contain UPDATED_NAME
        defaultCommonFieldDataShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value equals to DEFAULT_VALUE
        defaultCommonFieldDataShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the commonFieldDataList where value equals to UPDATED_VALUE
        defaultCommonFieldDataShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultCommonFieldDataShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the commonFieldDataList where value equals to UPDATED_VALUE
        defaultCommonFieldDataShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value is not null
        defaultCommonFieldDataShouldBeFound("value.specified=true");

        // Get all the commonFieldDataList where value is null
        defaultCommonFieldDataShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value contains DEFAULT_VALUE
        defaultCommonFieldDataShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the commonFieldDataList where value contains UPDATED_VALUE
        defaultCommonFieldDataShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value does not contain DEFAULT_VALUE
        defaultCommonFieldDataShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the commonFieldDataList where value does not contain UPDATED_VALUE
        defaultCommonFieldDataShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label equals to DEFAULT_LABEL
        defaultCommonFieldDataShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the commonFieldDataList where label equals to UPDATED_LABEL
        defaultCommonFieldDataShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultCommonFieldDataShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the commonFieldDataList where label equals to UPDATED_LABEL
        defaultCommonFieldDataShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label is not null
        defaultCommonFieldDataShouldBeFound("label.specified=true");

        // Get all the commonFieldDataList where label is null
        defaultCommonFieldDataShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label contains DEFAULT_LABEL
        defaultCommonFieldDataShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the commonFieldDataList where label contains UPDATED_LABEL
        defaultCommonFieldDataShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label does not contain DEFAULT_LABEL
        defaultCommonFieldDataShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the commonFieldDataList where label does not contain UPDATED_LABEL
        defaultCommonFieldDataShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType equals to DEFAULT_VALUE_TYPE
        defaultCommonFieldDataShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the commonFieldDataList where valueType equals to UPDATED_VALUE_TYPE
        defaultCommonFieldDataShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultCommonFieldDataShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the commonFieldDataList where valueType equals to UPDATED_VALUE_TYPE
        defaultCommonFieldDataShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType is not null
        defaultCommonFieldDataShouldBeFound("valueType.specified=true");

        // Get all the commonFieldDataList where valueType is null
        defaultCommonFieldDataShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark equals to DEFAULT_REMARK
        defaultCommonFieldDataShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the commonFieldDataList where remark equals to UPDATED_REMARK
        defaultCommonFieldDataShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultCommonFieldDataShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the commonFieldDataList where remark equals to UPDATED_REMARK
        defaultCommonFieldDataShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark is not null
        defaultCommonFieldDataShouldBeFound("remark.specified=true");

        // Get all the commonFieldDataList where remark is null
        defaultCommonFieldDataShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark contains DEFAULT_REMARK
        defaultCommonFieldDataShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the commonFieldDataList where remark contains UPDATED_REMARK
        defaultCommonFieldDataShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark does not contain DEFAULT_REMARK
        defaultCommonFieldDataShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the commonFieldDataList where remark does not contain UPDATED_REMARK
        defaultCommonFieldDataShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue equals to DEFAULT_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.equals=" + DEFAULT_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue equals to UPDATED_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue in DEFAULT_SORT_VALUE or UPDATED_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue equals to UPDATED_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is not null
        defaultCommonFieldDataShouldBeFound("sortValue.specified=true");

        // Get all the commonFieldDataList where sortValue is null
        defaultCommonFieldDataShouldNotBeFound("sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is greater than or equal to DEFAULT_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue is greater than or equal to UPDATED_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is less than or equal to DEFAULT_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue is less than or equal to SMALLER_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is less than DEFAULT_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.lessThan=" + DEFAULT_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue is less than UPDATED_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.lessThan=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is greater than DEFAULT_SORT_VALUE
        defaultCommonFieldDataShouldNotBeFound("sortValue.greaterThan=" + DEFAULT_SORT_VALUE);

        // Get all the commonFieldDataList where sortValue is greater than SMALLER_SORT_VALUE
        defaultCommonFieldDataShouldBeFound("sortValue.greaterThan=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled equals to DEFAULT_DISABLED
        defaultCommonFieldDataShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the commonFieldDataList where disabled equals to UPDATED_DISABLED
        defaultCommonFieldDataShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultCommonFieldDataShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the commonFieldDataList where disabled equals to UPDATED_DISABLED
        defaultCommonFieldDataShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled is not null
        defaultCommonFieldDataShouldBeFound("disabled.specified=true");

        // Get all the commonFieldDataList where disabled is null
        defaultCommonFieldDataShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName equals to DEFAULT_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldBeFound("ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the commonFieldDataList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldNotBeFound("ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName in DEFAULT_OWNER_ENTITY_NAME or UPDATED_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldBeFound("ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME);

        // Get all the commonFieldDataList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldNotBeFound("ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName is not null
        defaultCommonFieldDataShouldBeFound("ownerEntityName.specified=true");

        // Get all the commonFieldDataList where ownerEntityName is null
        defaultCommonFieldDataShouldNotBeFound("ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName contains DEFAULT_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldBeFound("ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the commonFieldDataList where ownerEntityName contains UPDATED_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldNotBeFound("ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName does not contain DEFAULT_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldNotBeFound("ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the commonFieldDataList where ownerEntityName does not contain UPDATED_OWNER_ENTITY_NAME
        defaultCommonFieldDataShouldBeFound("ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId equals to DEFAULT_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId in DEFAULT_OWNER_ENTITY_ID or UPDATED_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is not null
        defaultCommonFieldDataShouldBeFound("ownerEntityId.specified=true");

        // Get all the commonFieldDataList where ownerEntityId is null
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is greater than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId is greater than or equal to UPDATED_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is less than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId is less than or equal to SMALLER_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is less than DEFAULT_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId is less than UPDATED_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is greater than DEFAULT_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldNotBeFound("ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the commonFieldDataList where ownerEntityId is greater than SMALLER_OWNER_ENTITY_ID
        defaultCommonFieldDataShouldBeFound("ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommonFieldDataShouldBeFound(String filter) throws Exception {
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonFieldData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ownerEntityName").value(hasItem(DEFAULT_OWNER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].ownerEntityId").value(hasItem(DEFAULT_OWNER_ENTITY_ID.intValue())));

        // Check, that the count call also returns 1
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommonFieldDataShouldNotBeFound(String filter) throws Exception {
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommonFieldDataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommonFieldData() throws Exception {
        // Get the commonFieldData
        restCommonFieldDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommonFieldData() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();

        // Update the commonFieldData
        CommonFieldData updatedCommonFieldData = commonFieldDataRepository.findById(commonFieldData.getId()).orElseThrow();
        updatedCommonFieldData
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .label(UPDATED_LABEL)
            .valueType(UPDATED_VALUE_TYPE)
            .remark(UPDATED_REMARK)
            .sortValue(UPDATED_SORT_VALUE)
            .disabled(UPDATED_DISABLED)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID);
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(updatedCommonFieldData);

        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonFieldDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
        CommonFieldData testCommonFieldData = commonFieldDataList.get(commonFieldDataList.size() - 1);
        assertThat(testCommonFieldData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonFieldData.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCommonFieldData.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCommonFieldData.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testCommonFieldData.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCommonFieldData.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testCommonFieldData.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testCommonFieldData.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testCommonFieldData.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void putNonExistingCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonFieldDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonFieldDataWithPatch() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();

        // Update the commonFieldData using partial update
        CommonFieldData partialUpdatedCommonFieldData = new CommonFieldData();
        partialUpdatedCommonFieldData.setId(commonFieldData.getId());

        partialUpdatedCommonFieldData.remark(UPDATED_REMARK).disabled(UPDATED_DISABLED).ownerEntityName(UPDATED_OWNER_ENTITY_NAME);

        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonFieldData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonFieldData))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
        CommonFieldData testCommonFieldData = commonFieldDataList.get(commonFieldDataList.size() - 1);
        assertThat(testCommonFieldData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonFieldData.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCommonFieldData.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testCommonFieldData.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testCommonFieldData.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCommonFieldData.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testCommonFieldData.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testCommonFieldData.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testCommonFieldData.getOwnerEntityId()).isEqualTo(DEFAULT_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void fullUpdateCommonFieldDataWithPatch() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();

        // Update the commonFieldData using partial update
        CommonFieldData partialUpdatedCommonFieldData = new CommonFieldData();
        partialUpdatedCommonFieldData.setId(commonFieldData.getId());

        partialUpdatedCommonFieldData
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .label(UPDATED_LABEL)
            .valueType(UPDATED_VALUE_TYPE)
            .remark(UPDATED_REMARK)
            .sortValue(UPDATED_SORT_VALUE)
            .disabled(UPDATED_DISABLED)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID);

        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonFieldData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonFieldData))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
        CommonFieldData testCommonFieldData = commonFieldDataList.get(commonFieldDataList.size() - 1);
        assertThat(testCommonFieldData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonFieldData.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCommonFieldData.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testCommonFieldData.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testCommonFieldData.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCommonFieldData.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testCommonFieldData.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testCommonFieldData.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testCommonFieldData.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonFieldDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonFieldData() throws Exception {
        int databaseSizeBeforeUpdate = commonFieldDataRepository.findAll().size();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonFieldDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonFieldData in the database
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonFieldData() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        int databaseSizeBeforeDelete = commonFieldDataRepository.findAll().size();

        // Delete the commonFieldData
        restCommonFieldDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonFieldData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonFieldData> commonFieldDataList = commonFieldDataRepository.findAll();
        assertThat(commonFieldDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
