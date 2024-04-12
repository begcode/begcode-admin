package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.FillRuleItemAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.FieldParamType;
import com.begcode.monolith.settings.domain.FillRuleItem;
import com.begcode.monolith.settings.domain.SysFillRule;
import com.begcode.monolith.settings.repository.FillRuleItemRepository;
import com.begcode.monolith.settings.service.FillRuleItemService;
import com.begcode.monolith.settings.service.dto.FillRuleItemDTO;
import com.begcode.monolith.settings.service.mapper.FillRuleItemMapper;
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
 * Integration tests for the {@link FillRuleItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class FillRuleItemResourceIT {

    private static final Integer DEFAULT_SORT_VALUE = 1;
    private static final Integer UPDATED_SORT_VALUE = 2;
    private static final Integer SMALLER_SORT_VALUE = 1 - 1;

    private static final FieldParamType DEFAULT_FIELD_PARAM_TYPE = FieldParamType.DATETIME;
    private static final FieldParamType UPDATED_FIELD_PARAM_TYPE = FieldParamType.NUMBER;

    private static final String DEFAULT_FIELD_PARAM_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_PARAM_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_DATE_PATTERN = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQ_LENGTH = 1;
    private static final Integer UPDATED_SEQ_LENGTH = 2;
    private static final Integer SMALLER_SEQ_LENGTH = 1 - 1;

    private static final Integer DEFAULT_SEQ_INCREMENT = 1;
    private static final Integer UPDATED_SEQ_INCREMENT = 2;
    private static final Integer SMALLER_SEQ_INCREMENT = 1 - 1;

    private static final Integer DEFAULT_SEQ_START_VALUE = 1;
    private static final Integer UPDATED_SEQ_START_VALUE = 2;
    private static final Integer SMALLER_SEQ_START_VALUE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/fill-rule-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FillRuleItemRepository fillRuleItemRepository;

    @Mock
    private FillRuleItemRepository fillRuleItemRepositoryMock;

    @Autowired
    private FillRuleItemMapper fillRuleItemMapper;

    @Mock
    private FillRuleItemService fillRuleItemServiceMock;

    @Autowired
    private MockMvc restFillRuleItemMockMvc;

    private FillRuleItem fillRuleItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FillRuleItem createEntity() {
        FillRuleItem fillRuleItem = new FillRuleItem()
            .sortValue(DEFAULT_SORT_VALUE)
            .fieldParamType(DEFAULT_FIELD_PARAM_TYPE)
            .fieldParamValue(DEFAULT_FIELD_PARAM_VALUE)
            .datePattern(DEFAULT_DATE_PATTERN)
            .seqLength(DEFAULT_SEQ_LENGTH)
            .seqIncrement(DEFAULT_SEQ_INCREMENT)
            .seqStartValue(DEFAULT_SEQ_START_VALUE);
        return fillRuleItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FillRuleItem createUpdatedEntity() {
        FillRuleItem fillRuleItem = new FillRuleItem()
            .sortValue(UPDATED_SORT_VALUE)
            .fieldParamType(UPDATED_FIELD_PARAM_TYPE)
            .fieldParamValue(UPDATED_FIELD_PARAM_VALUE)
            .datePattern(UPDATED_DATE_PATTERN)
            .seqLength(UPDATED_SEQ_LENGTH)
            .seqIncrement(UPDATED_SEQ_INCREMENT)
            .seqStartValue(UPDATED_SEQ_START_VALUE);
        return fillRuleItem;
    }

    @BeforeEach
    public void initTest() {
        fillRuleItem = createEntity();
    }

    @Test
    @Transactional
    void createFillRuleItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);
        var returnedFillRuleItemDTO = om.readValue(
            restFillRuleItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fillRuleItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FillRuleItemDTO.class
        );

        // Validate the FillRuleItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFillRuleItem = fillRuleItemMapper.toEntity(returnedFillRuleItemDTO);
        assertFillRuleItemUpdatableFieldsEquals(returnedFillRuleItem, getPersistedFillRuleItem(returnedFillRuleItem));
    }

    @Test
    @Transactional
    void createFillRuleItemWithExistingId() throws Exception {
        // Create the FillRuleItem with an existing ID
        fillRuleItem.setId(1L);
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFillRuleItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fillRuleItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFillRuleItems() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fillRuleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].fieldParamType").value(hasItem(DEFAULT_FIELD_PARAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fieldParamValue").value(hasItem(DEFAULT_FIELD_PARAM_VALUE)))
            .andExpect(jsonPath("$.[*].datePattern").value(hasItem(DEFAULT_DATE_PATTERN)))
            .andExpect(jsonPath("$.[*].seqLength").value(hasItem(DEFAULT_SEQ_LENGTH)))
            .andExpect(jsonPath("$.[*].seqIncrement").value(hasItem(DEFAULT_SEQ_INCREMENT)))
            .andExpect(jsonPath("$.[*].seqStartValue").value(hasItem(DEFAULT_SEQ_START_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFillRuleItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fillRuleItemServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFillRuleItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fillRuleItemServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFillRuleItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fillRuleItemServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restFillRuleItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fillRuleItemRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getFillRuleItem() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get the fillRuleItem
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL_ID, fillRuleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fillRuleItem.getId().intValue()))
            .andExpect(jsonPath("$.sortValue").value(DEFAULT_SORT_VALUE))
            .andExpect(jsonPath("$.fieldParamType").value(DEFAULT_FIELD_PARAM_TYPE.toString()))
            .andExpect(jsonPath("$.fieldParamValue").value(DEFAULT_FIELD_PARAM_VALUE))
            .andExpect(jsonPath("$.datePattern").value(DEFAULT_DATE_PATTERN))
            .andExpect(jsonPath("$.seqLength").value(DEFAULT_SEQ_LENGTH))
            .andExpect(jsonPath("$.seqIncrement").value(DEFAULT_SEQ_INCREMENT))
            .andExpect(jsonPath("$.seqStartValue").value(DEFAULT_SEQ_START_VALUE));
    }

    @Test
    @Transactional
    void getFillRuleItemsByIdFiltering() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        Long id = fillRuleItem.getId();

        defaultFillRuleItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFillRuleItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFillRuleItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue equals to
        defaultFillRuleItemFiltering("sortValue.equals=" + DEFAULT_SORT_VALUE, "sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue in
        defaultFillRuleItemFiltering("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE, "sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is not null
        defaultFillRuleItemFiltering("sortValue.specified=true", "sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is greater than or equal to
        defaultFillRuleItemFiltering(
            "sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is less than or equal to
        defaultFillRuleItemFiltering("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE, "sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is less than
        defaultFillRuleItemFiltering("sortValue.lessThan=" + UPDATED_SORT_VALUE, "sortValue.lessThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is greater than
        defaultFillRuleItemFiltering("sortValue.greaterThan=" + SMALLER_SORT_VALUE, "sortValue.greaterThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType equals to
        defaultFillRuleItemFiltering(
            "fieldParamType.equals=" + DEFAULT_FIELD_PARAM_TYPE,
            "fieldParamType.equals=" + UPDATED_FIELD_PARAM_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType in
        defaultFillRuleItemFiltering(
            "fieldParamType.in=" + DEFAULT_FIELD_PARAM_TYPE + "," + UPDATED_FIELD_PARAM_TYPE,
            "fieldParamType.in=" + UPDATED_FIELD_PARAM_TYPE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType is not null
        defaultFillRuleItemFiltering("fieldParamType.specified=true", "fieldParamType.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue equals to
        defaultFillRuleItemFiltering(
            "fieldParamValue.equals=" + DEFAULT_FIELD_PARAM_VALUE,
            "fieldParamValue.equals=" + UPDATED_FIELD_PARAM_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue in
        defaultFillRuleItemFiltering(
            "fieldParamValue.in=" + DEFAULT_FIELD_PARAM_VALUE + "," + UPDATED_FIELD_PARAM_VALUE,
            "fieldParamValue.in=" + UPDATED_FIELD_PARAM_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue is not null
        defaultFillRuleItemFiltering("fieldParamValue.specified=true", "fieldParamValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue contains
        defaultFillRuleItemFiltering(
            "fieldParamValue.contains=" + DEFAULT_FIELD_PARAM_VALUE,
            "fieldParamValue.contains=" + UPDATED_FIELD_PARAM_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueNotContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue does not contain
        defaultFillRuleItemFiltering(
            "fieldParamValue.doesNotContain=" + UPDATED_FIELD_PARAM_VALUE,
            "fieldParamValue.doesNotContain=" + DEFAULT_FIELD_PARAM_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern equals to
        defaultFillRuleItemFiltering("datePattern.equals=" + DEFAULT_DATE_PATTERN, "datePattern.equals=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern in
        defaultFillRuleItemFiltering(
            "datePattern.in=" + DEFAULT_DATE_PATTERN + "," + UPDATED_DATE_PATTERN,
            "datePattern.in=" + UPDATED_DATE_PATTERN
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern is not null
        defaultFillRuleItemFiltering("datePattern.specified=true", "datePattern.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern contains
        defaultFillRuleItemFiltering("datePattern.contains=" + DEFAULT_DATE_PATTERN, "datePattern.contains=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternNotContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern does not contain
        defaultFillRuleItemFiltering(
            "datePattern.doesNotContain=" + UPDATED_DATE_PATTERN,
            "datePattern.doesNotContain=" + DEFAULT_DATE_PATTERN
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength equals to
        defaultFillRuleItemFiltering("seqLength.equals=" + DEFAULT_SEQ_LENGTH, "seqLength.equals=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength in
        defaultFillRuleItemFiltering("seqLength.in=" + DEFAULT_SEQ_LENGTH + "," + UPDATED_SEQ_LENGTH, "seqLength.in=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is not null
        defaultFillRuleItemFiltering("seqLength.specified=true", "seqLength.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is greater than or equal to
        defaultFillRuleItemFiltering(
            "seqLength.greaterThanOrEqual=" + DEFAULT_SEQ_LENGTH,
            "seqLength.greaterThanOrEqual=" + UPDATED_SEQ_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is less than or equal to
        defaultFillRuleItemFiltering("seqLength.lessThanOrEqual=" + DEFAULT_SEQ_LENGTH, "seqLength.lessThanOrEqual=" + SMALLER_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is less than
        defaultFillRuleItemFiltering("seqLength.lessThan=" + UPDATED_SEQ_LENGTH, "seqLength.lessThan=" + DEFAULT_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is greater than
        defaultFillRuleItemFiltering("seqLength.greaterThan=" + SMALLER_SEQ_LENGTH, "seqLength.greaterThan=" + DEFAULT_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement equals to
        defaultFillRuleItemFiltering("seqIncrement.equals=" + DEFAULT_SEQ_INCREMENT, "seqIncrement.equals=" + UPDATED_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement in
        defaultFillRuleItemFiltering(
            "seqIncrement.in=" + DEFAULT_SEQ_INCREMENT + "," + UPDATED_SEQ_INCREMENT,
            "seqIncrement.in=" + UPDATED_SEQ_INCREMENT
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is not null
        defaultFillRuleItemFiltering("seqIncrement.specified=true", "seqIncrement.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is greater than or equal to
        defaultFillRuleItemFiltering(
            "seqIncrement.greaterThanOrEqual=" + DEFAULT_SEQ_INCREMENT,
            "seqIncrement.greaterThanOrEqual=" + UPDATED_SEQ_INCREMENT
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is less than or equal to
        defaultFillRuleItemFiltering(
            "seqIncrement.lessThanOrEqual=" + DEFAULT_SEQ_INCREMENT,
            "seqIncrement.lessThanOrEqual=" + SMALLER_SEQ_INCREMENT
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is less than
        defaultFillRuleItemFiltering("seqIncrement.lessThan=" + UPDATED_SEQ_INCREMENT, "seqIncrement.lessThan=" + DEFAULT_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is greater than
        defaultFillRuleItemFiltering(
            "seqIncrement.greaterThan=" + SMALLER_SEQ_INCREMENT,
            "seqIncrement.greaterThan=" + DEFAULT_SEQ_INCREMENT
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue equals to
        defaultFillRuleItemFiltering("seqStartValue.equals=" + DEFAULT_SEQ_START_VALUE, "seqStartValue.equals=" + UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue in
        defaultFillRuleItemFiltering(
            "seqStartValue.in=" + DEFAULT_SEQ_START_VALUE + "," + UPDATED_SEQ_START_VALUE,
            "seqStartValue.in=" + UPDATED_SEQ_START_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is not null
        defaultFillRuleItemFiltering("seqStartValue.specified=true", "seqStartValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is greater than or equal to
        defaultFillRuleItemFiltering(
            "seqStartValue.greaterThanOrEqual=" + DEFAULT_SEQ_START_VALUE,
            "seqStartValue.greaterThanOrEqual=" + UPDATED_SEQ_START_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is less than or equal to
        defaultFillRuleItemFiltering(
            "seqStartValue.lessThanOrEqual=" + DEFAULT_SEQ_START_VALUE,
            "seqStartValue.lessThanOrEqual=" + SMALLER_SEQ_START_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is less than
        defaultFillRuleItemFiltering(
            "seqStartValue.lessThan=" + UPDATED_SEQ_START_VALUE,
            "seqStartValue.lessThan=" + DEFAULT_SEQ_START_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is greater than
        defaultFillRuleItemFiltering(
            "seqStartValue.greaterThan=" + SMALLER_SEQ_START_VALUE,
            "seqStartValue.greaterThan=" + DEFAULT_SEQ_START_VALUE
        );
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFillRuleIsEqualToSomething() throws Exception {
        SysFillRule fillRule = SysFillRuleResourceIT.createEntity();
        fillRuleItem.setFillRule(fillRule);
        fillRuleItemRepository.insert(fillRuleItem);
        Long fillRuleId = fillRule.getId();
        // Get all the fillRuleItemList where fillRule equals to fillRuleId
        defaultFillRuleItemShouldBeFound("fillRuleId.equals=" + fillRuleId);

        // Get all the fillRuleItemList where fillRule equals to (fillRuleId + 1)
        defaultFillRuleItemShouldNotBeFound("fillRuleId.equals=" + (fillRuleId + 1));
    }

    private void defaultFillRuleItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFillRuleItemShouldBeFound(shouldBeFound);
        defaultFillRuleItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFillRuleItemShouldBeFound(String filter) throws Exception {
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fillRuleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].fieldParamType").value(hasItem(DEFAULT_FIELD_PARAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fieldParamValue").value(hasItem(DEFAULT_FIELD_PARAM_VALUE)))
            .andExpect(jsonPath("$.[*].datePattern").value(hasItem(DEFAULT_DATE_PATTERN)))
            .andExpect(jsonPath("$.[*].seqLength").value(hasItem(DEFAULT_SEQ_LENGTH)))
            .andExpect(jsonPath("$.[*].seqIncrement").value(hasItem(DEFAULT_SEQ_INCREMENT)))
            .andExpect(jsonPath("$.[*].seqStartValue").value(hasItem(DEFAULT_SEQ_START_VALUE)));

        // Check, that the count call also returns 1
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFillRuleItemShouldNotBeFound(String filter) throws Exception {
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFillRuleItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFillRuleItem() throws Exception {
        // Get the fillRuleItem
        restFillRuleItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFillRuleItem() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fillRuleItem
        FillRuleItem updatedFillRuleItem = fillRuleItemRepository.findById(fillRuleItem.getId()).orElseThrow();
        updatedFillRuleItem
            .sortValue(UPDATED_SORT_VALUE)
            .fieldParamType(UPDATED_FIELD_PARAM_TYPE)
            .fieldParamValue(UPDATED_FIELD_PARAM_VALUE)
            .datePattern(UPDATED_DATE_PATTERN)
            .seqLength(UPDATED_SEQ_LENGTH)
            .seqIncrement(UPDATED_SEQ_INCREMENT)
            .seqStartValue(UPDATED_SEQ_START_VALUE);
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(updatedFillRuleItem);

        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fillRuleItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fillRuleItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFillRuleItemToMatchAllProperties(updatedFillRuleItem);
    }

    @Test
    @Transactional
    void putNonExistingFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fillRuleItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fillRuleItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFillRuleItemWithPatch() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fillRuleItem using partial update
        FillRuleItem partialUpdatedFillRuleItem = new FillRuleItem();
        partialUpdatedFillRuleItem.setId(fillRuleItem.getId());

        partialUpdatedFillRuleItem
            .fieldParamType(UPDATED_FIELD_PARAM_TYPE)
            .datePattern(UPDATED_DATE_PATTERN)
            .seqIncrement(UPDATED_SEQ_INCREMENT);

        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFillRuleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFillRuleItem))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFillRuleItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFillRuleItem, fillRuleItem),
            getPersistedFillRuleItem(fillRuleItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateFillRuleItemWithPatch() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fillRuleItem using partial update
        FillRuleItem partialUpdatedFillRuleItem = new FillRuleItem();
        partialUpdatedFillRuleItem.setId(fillRuleItem.getId());

        partialUpdatedFillRuleItem
            .sortValue(UPDATED_SORT_VALUE)
            .fieldParamType(UPDATED_FIELD_PARAM_TYPE)
            .fieldParamValue(UPDATED_FIELD_PARAM_VALUE)
            .datePattern(UPDATED_DATE_PATTERN)
            .seqLength(UPDATED_SEQ_LENGTH)
            .seqIncrement(UPDATED_SEQ_INCREMENT)
            .seqStartValue(UPDATED_SEQ_START_VALUE);

        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFillRuleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFillRuleItem))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFillRuleItemUpdatableFieldsEquals(partialUpdatedFillRuleItem, getPersistedFillRuleItem(partialUpdatedFillRuleItem));
    }

    @Test
    @Transactional
    void patchNonExistingFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fillRuleItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFillRuleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fillRuleItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FillRuleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFillRuleItem() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fillRuleItem
        restFillRuleItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, fillRuleItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fillRuleItemRepository.selectCount(null);
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

    protected FillRuleItem getPersistedFillRuleItem(FillRuleItem fillRuleItem) {
        return fillRuleItemRepository.findById(fillRuleItem.getId()).orElseThrow();
    }

    protected void assertPersistedFillRuleItemToMatchAllProperties(FillRuleItem expectedFillRuleItem) {
        assertFillRuleItemAllPropertiesEquals(expectedFillRuleItem, getPersistedFillRuleItem(expectedFillRuleItem));
    }

    protected void assertPersistedFillRuleItemToMatchUpdatableProperties(FillRuleItem expectedFillRuleItem) {
        assertFillRuleItemAllUpdatablePropertiesEquals(expectedFillRuleItem, getPersistedFillRuleItem(expectedFillRuleItem));
    }
}
