package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.CommonFieldDataAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.CommonFieldType;
import com.begcode.monolith.settings.domain.CommonFieldData;
import com.begcode.monolith.settings.repository.CommonFieldDataRepository;
import com.begcode.monolith.settings.service.dto.CommonFieldDataDTO;
import com.begcode.monolith.settings.service.mapper.CommonFieldDataMapper;
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
 * Integration tests for the {@link CommonFieldDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
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

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

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
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);
        var returnedCommonFieldDataDTO = om.readValue(
            restCommonFieldDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commonFieldDataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommonFieldDataDTO.class
        );

        // Validate the CommonFieldData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommonFieldData = commonFieldDataMapper.toEntity(returnedCommonFieldDataDTO);
        assertCommonFieldDataUpdatableFieldsEquals(returnedCommonFieldData, getPersistedCommonFieldData(returnedCommonFieldData));
    }

    @Test
    @Transactional
    void createCommonFieldDataWithExistingId() throws Exception {
        // Create the CommonFieldData with an existing ID
        commonFieldData.setId(1L);
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonFieldDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commonFieldDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
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

        defaultCommonFieldDataFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCommonFieldDataFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCommonFieldDataFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name equals to
        defaultCommonFieldDataFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name in
        defaultCommonFieldDataFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name is not null
        defaultCommonFieldDataFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name contains
        defaultCommonFieldDataFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where name does not contain
        defaultCommonFieldDataFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value equals to
        defaultCommonFieldDataFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value in
        defaultCommonFieldDataFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value is not null
        defaultCommonFieldDataFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value contains
        defaultCommonFieldDataFiltering("value.contains=" + DEFAULT_VALUE, "value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where value does not contain
        defaultCommonFieldDataFiltering("value.doesNotContain=" + UPDATED_VALUE, "value.doesNotContain=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label equals to
        defaultCommonFieldDataFiltering("label.equals=" + DEFAULT_LABEL, "label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label in
        defaultCommonFieldDataFiltering("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL, "label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label is not null
        defaultCommonFieldDataFiltering("label.specified=true", "label.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label contains
        defaultCommonFieldDataFiltering("label.contains=" + DEFAULT_LABEL, "label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where label does not contain
        defaultCommonFieldDataFiltering("label.doesNotContain=" + UPDATED_LABEL, "label.doesNotContain=" + DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType equals to
        defaultCommonFieldDataFiltering("valueType.equals=" + DEFAULT_VALUE_TYPE, "valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType in
        defaultCommonFieldDataFiltering(
            "valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE,
            "valueType.in=" + UPDATED_VALUE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where valueType is not null
        defaultCommonFieldDataFiltering("valueType.specified=true", "valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark equals to
        defaultCommonFieldDataFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark in
        defaultCommonFieldDataFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark is not null
        defaultCommonFieldDataFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark contains
        defaultCommonFieldDataFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where remark does not contain
        defaultCommonFieldDataFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue equals to
        defaultCommonFieldDataFiltering("sortValue.equals=" + DEFAULT_SORT_VALUE, "sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue in
        defaultCommonFieldDataFiltering(
            "sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE,
            "sortValue.in=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is not null
        defaultCommonFieldDataFiltering("sortValue.specified=true", "sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is greater than or equal to
        defaultCommonFieldDataFiltering(
            "sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is less than or equal to
        defaultCommonFieldDataFiltering(
            "sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is less than
        defaultCommonFieldDataFiltering("sortValue.lessThan=" + UPDATED_SORT_VALUE, "sortValue.lessThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where sortValue is greater than
        defaultCommonFieldDataFiltering("sortValue.greaterThan=" + SMALLER_SORT_VALUE, "sortValue.greaterThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled equals to
        defaultCommonFieldDataFiltering("disabled.equals=" + DEFAULT_DISABLED, "disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled in
        defaultCommonFieldDataFiltering("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED, "disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where disabled is not null
        defaultCommonFieldDataFiltering("disabled.specified=true", "disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName equals to
        defaultCommonFieldDataFiltering(
            "ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName in
        defaultCommonFieldDataFiltering(
            "ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName is not null
        defaultCommonFieldDataFiltering("ownerEntityName.specified=true", "ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName contains
        defaultCommonFieldDataFiltering(
            "ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityName does not contain
        defaultCommonFieldDataFiltering(
            "ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId equals to
        defaultCommonFieldDataFiltering(
            "ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId in
        defaultCommonFieldDataFiltering(
            "ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is not null
        defaultCommonFieldDataFiltering("ownerEntityId.specified=true", "ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is greater than or equal to
        defaultCommonFieldDataFiltering(
            "ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is less than or equal to
        defaultCommonFieldDataFiltering(
            "ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is less than
        defaultCommonFieldDataFiltering(
            "ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCommonFieldDataByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        // Get all the commonFieldDataList where ownerEntityId is greater than
        defaultCommonFieldDataFiltering(
            "ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    private void defaultCommonFieldDataFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCommonFieldDataShouldBeFound(shouldBeFound);
        defaultCommonFieldDataShouldNotBeFound(shouldNotBeFound);
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

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(commonFieldDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommonFieldDataToMatchAllProperties(updatedCommonFieldData);
    }

    @Test
    @Transactional
    void putNonExistingCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonFieldDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commonFieldDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonFieldDataWithPatch() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commonFieldData using partial update
        CommonFieldData partialUpdatedCommonFieldData = new CommonFieldData();
        partialUpdatedCommonFieldData.setId(commonFieldData.getId());

        partialUpdatedCommonFieldData
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .sortValue(UPDATED_SORT_VALUE)
            .disabled(UPDATED_DISABLED)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID);

        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonFieldData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommonFieldData))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommonFieldDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommonFieldData, commonFieldData),
            getPersistedCommonFieldData(commonFieldData)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommonFieldDataWithPatch() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedCommonFieldData))
            )
            .andExpect(status().isOk());

        // Validate the CommonFieldData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommonFieldDataUpdatableFieldsEquals(
            partialUpdatedCommonFieldData,
            getPersistedCommonFieldData(partialUpdatedCommonFieldData)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonFieldDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commonFieldDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonFieldData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commonFieldData.setId(longCount.incrementAndGet());

        // Create the CommonFieldData
        CommonFieldDataDTO commonFieldDataDTO = commonFieldDataMapper.toDto(commonFieldData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonFieldDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commonFieldDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonFieldData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonFieldData() throws Exception {
        // Initialize the database
        commonFieldDataRepository.save(commonFieldData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commonFieldData
        restCommonFieldDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonFieldData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commonFieldDataRepository.selectCount(null);
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

    protected CommonFieldData getPersistedCommonFieldData(CommonFieldData commonFieldData) {
        return commonFieldDataRepository.findById(commonFieldData.getId()).orElseThrow();
    }

    protected void assertPersistedCommonFieldDataToMatchAllProperties(CommonFieldData expectedCommonFieldData) {
        assertCommonFieldDataAllPropertiesEquals(expectedCommonFieldData, getPersistedCommonFieldData(expectedCommonFieldData));
    }

    protected void assertPersistedCommonFieldDataToMatchUpdatableProperties(CommonFieldData expectedCommonFieldData) {
        assertCommonFieldDataAllUpdatablePropertiesEquals(expectedCommonFieldData, getPersistedCommonFieldData(expectedCommonFieldData));
    }
}
