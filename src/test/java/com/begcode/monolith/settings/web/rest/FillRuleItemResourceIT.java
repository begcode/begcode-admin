package com.begcode.monolith.settings.web.rest;

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        int databaseSizeBeforeCreate = fillRuleItemRepository.findAll().size();
        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);
        restFillRuleItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeCreate + 1);
        FillRuleItem testFillRuleItem = fillRuleItemList.get(fillRuleItemList.size() - 1);
        assertThat(testFillRuleItem.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testFillRuleItem.getFieldParamType()).isEqualTo(DEFAULT_FIELD_PARAM_TYPE);
        assertThat(testFillRuleItem.getFieldParamValue()).isEqualTo(DEFAULT_FIELD_PARAM_VALUE);
        assertThat(testFillRuleItem.getDatePattern()).isEqualTo(DEFAULT_DATE_PATTERN);
        assertThat(testFillRuleItem.getSeqLength()).isEqualTo(DEFAULT_SEQ_LENGTH);
        assertThat(testFillRuleItem.getSeqIncrement()).isEqualTo(DEFAULT_SEQ_INCREMENT);
        assertThat(testFillRuleItem.getSeqStartValue()).isEqualTo(DEFAULT_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void createFillRuleItemWithExistingId() throws Exception {
        // Create the FillRuleItem with an existing ID
        fillRuleItem.setId(1L);
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        int databaseSizeBeforeCreate = fillRuleItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFillRuleItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeCreate);
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

        defaultFillRuleItemShouldBeFound("id.equals=" + id);
        defaultFillRuleItemShouldNotBeFound("id.notEquals=" + id);

        defaultFillRuleItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFillRuleItemShouldNotBeFound("id.greaterThan=" + id);

        defaultFillRuleItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFillRuleItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue equals to DEFAULT_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.equals=" + DEFAULT_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue equals to UPDATED_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue in DEFAULT_SORT_VALUE or UPDATED_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue equals to UPDATED_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is not null
        defaultFillRuleItemShouldBeFound("sortValue.specified=true");

        // Get all the fillRuleItemList where sortValue is null
        defaultFillRuleItemShouldNotBeFound("sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is greater than or equal to DEFAULT_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue is greater than or equal to UPDATED_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is less than or equal to DEFAULT_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue is less than or equal to SMALLER_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is less than DEFAULT_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.lessThan=" + DEFAULT_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue is less than UPDATED_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.lessThan=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where sortValue is greater than DEFAULT_SORT_VALUE
        defaultFillRuleItemShouldNotBeFound("sortValue.greaterThan=" + DEFAULT_SORT_VALUE);

        // Get all the fillRuleItemList where sortValue is greater than SMALLER_SORT_VALUE
        defaultFillRuleItemShouldBeFound("sortValue.greaterThan=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType equals to DEFAULT_FIELD_PARAM_TYPE
        defaultFillRuleItemShouldBeFound("fieldParamType.equals=" + DEFAULT_FIELD_PARAM_TYPE);

        // Get all the fillRuleItemList where fieldParamType equals to UPDATED_FIELD_PARAM_TYPE
        defaultFillRuleItemShouldNotBeFound("fieldParamType.equals=" + UPDATED_FIELD_PARAM_TYPE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType in DEFAULT_FIELD_PARAM_TYPE or UPDATED_FIELD_PARAM_TYPE
        defaultFillRuleItemShouldBeFound("fieldParamType.in=" + DEFAULT_FIELD_PARAM_TYPE + "," + UPDATED_FIELD_PARAM_TYPE);

        // Get all the fillRuleItemList where fieldParamType equals to UPDATED_FIELD_PARAM_TYPE
        defaultFillRuleItemShouldNotBeFound("fieldParamType.in=" + UPDATED_FIELD_PARAM_TYPE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamType is not null
        defaultFillRuleItemShouldBeFound("fieldParamType.specified=true");

        // Get all the fillRuleItemList where fieldParamType is null
        defaultFillRuleItemShouldNotBeFound("fieldParamType.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue equals to DEFAULT_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldBeFound("fieldParamValue.equals=" + DEFAULT_FIELD_PARAM_VALUE);

        // Get all the fillRuleItemList where fieldParamValue equals to UPDATED_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldNotBeFound("fieldParamValue.equals=" + UPDATED_FIELD_PARAM_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue in DEFAULT_FIELD_PARAM_VALUE or UPDATED_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldBeFound("fieldParamValue.in=" + DEFAULT_FIELD_PARAM_VALUE + "," + UPDATED_FIELD_PARAM_VALUE);

        // Get all the fillRuleItemList where fieldParamValue equals to UPDATED_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldNotBeFound("fieldParamValue.in=" + UPDATED_FIELD_PARAM_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue is not null
        defaultFillRuleItemShouldBeFound("fieldParamValue.specified=true");

        // Get all the fillRuleItemList where fieldParamValue is null
        defaultFillRuleItemShouldNotBeFound("fieldParamValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue contains DEFAULT_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldBeFound("fieldParamValue.contains=" + DEFAULT_FIELD_PARAM_VALUE);

        // Get all the fillRuleItemList where fieldParamValue contains UPDATED_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldNotBeFound("fieldParamValue.contains=" + UPDATED_FIELD_PARAM_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByFieldParamValueNotContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where fieldParamValue does not contain DEFAULT_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldNotBeFound("fieldParamValue.doesNotContain=" + DEFAULT_FIELD_PARAM_VALUE);

        // Get all the fillRuleItemList where fieldParamValue does not contain UPDATED_FIELD_PARAM_VALUE
        defaultFillRuleItemShouldBeFound("fieldParamValue.doesNotContain=" + UPDATED_FIELD_PARAM_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern equals to DEFAULT_DATE_PATTERN
        defaultFillRuleItemShouldBeFound("datePattern.equals=" + DEFAULT_DATE_PATTERN);

        // Get all the fillRuleItemList where datePattern equals to UPDATED_DATE_PATTERN
        defaultFillRuleItemShouldNotBeFound("datePattern.equals=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern in DEFAULT_DATE_PATTERN or UPDATED_DATE_PATTERN
        defaultFillRuleItemShouldBeFound("datePattern.in=" + DEFAULT_DATE_PATTERN + "," + UPDATED_DATE_PATTERN);

        // Get all the fillRuleItemList where datePattern equals to UPDATED_DATE_PATTERN
        defaultFillRuleItemShouldNotBeFound("datePattern.in=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern is not null
        defaultFillRuleItemShouldBeFound("datePattern.specified=true");

        // Get all the fillRuleItemList where datePattern is null
        defaultFillRuleItemShouldNotBeFound("datePattern.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern contains DEFAULT_DATE_PATTERN
        defaultFillRuleItemShouldBeFound("datePattern.contains=" + DEFAULT_DATE_PATTERN);

        // Get all the fillRuleItemList where datePattern contains UPDATED_DATE_PATTERN
        defaultFillRuleItemShouldNotBeFound("datePattern.contains=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsByDatePatternNotContainsSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where datePattern does not contain DEFAULT_DATE_PATTERN
        defaultFillRuleItemShouldNotBeFound("datePattern.doesNotContain=" + DEFAULT_DATE_PATTERN);

        // Get all the fillRuleItemList where datePattern does not contain UPDATED_DATE_PATTERN
        defaultFillRuleItemShouldBeFound("datePattern.doesNotContain=" + UPDATED_DATE_PATTERN);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength equals to DEFAULT_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.equals=" + DEFAULT_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength equals to UPDATED_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.equals=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength in DEFAULT_SEQ_LENGTH or UPDATED_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.in=" + DEFAULT_SEQ_LENGTH + "," + UPDATED_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength equals to UPDATED_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.in=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is not null
        defaultFillRuleItemShouldBeFound("seqLength.specified=true");

        // Get all the fillRuleItemList where seqLength is null
        defaultFillRuleItemShouldNotBeFound("seqLength.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is greater than or equal to DEFAULT_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.greaterThanOrEqual=" + DEFAULT_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength is greater than or equal to UPDATED_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.greaterThanOrEqual=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is less than or equal to DEFAULT_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.lessThanOrEqual=" + DEFAULT_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength is less than or equal to SMALLER_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.lessThanOrEqual=" + SMALLER_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is less than DEFAULT_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.lessThan=" + DEFAULT_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength is less than UPDATED_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.lessThan=" + UPDATED_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqLength is greater than DEFAULT_SEQ_LENGTH
        defaultFillRuleItemShouldNotBeFound("seqLength.greaterThan=" + DEFAULT_SEQ_LENGTH);

        // Get all the fillRuleItemList where seqLength is greater than SMALLER_SEQ_LENGTH
        defaultFillRuleItemShouldBeFound("seqLength.greaterThan=" + SMALLER_SEQ_LENGTH);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement equals to DEFAULT_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.equals=" + DEFAULT_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement equals to UPDATED_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.equals=" + UPDATED_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement in DEFAULT_SEQ_INCREMENT or UPDATED_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.in=" + DEFAULT_SEQ_INCREMENT + "," + UPDATED_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement equals to UPDATED_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.in=" + UPDATED_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is not null
        defaultFillRuleItemShouldBeFound("seqIncrement.specified=true");

        // Get all the fillRuleItemList where seqIncrement is null
        defaultFillRuleItemShouldNotBeFound("seqIncrement.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is greater than or equal to DEFAULT_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.greaterThanOrEqual=" + DEFAULT_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement is greater than or equal to UPDATED_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.greaterThanOrEqual=" + UPDATED_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is less than or equal to DEFAULT_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.lessThanOrEqual=" + DEFAULT_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement is less than or equal to SMALLER_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.lessThanOrEqual=" + SMALLER_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is less than DEFAULT_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.lessThan=" + DEFAULT_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement is less than UPDATED_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.lessThan=" + UPDATED_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqIncrementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqIncrement is greater than DEFAULT_SEQ_INCREMENT
        defaultFillRuleItemShouldNotBeFound("seqIncrement.greaterThan=" + DEFAULT_SEQ_INCREMENT);

        // Get all the fillRuleItemList where seqIncrement is greater than SMALLER_SEQ_INCREMENT
        defaultFillRuleItemShouldBeFound("seqIncrement.greaterThan=" + SMALLER_SEQ_INCREMENT);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue equals to DEFAULT_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.equals=" + DEFAULT_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue equals to UPDATED_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.equals=" + UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsInShouldWork() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue in DEFAULT_SEQ_START_VALUE or UPDATED_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.in=" + DEFAULT_SEQ_START_VALUE + "," + UPDATED_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue equals to UPDATED_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.in=" + UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is not null
        defaultFillRuleItemShouldBeFound("seqStartValue.specified=true");

        // Get all the fillRuleItemList where seqStartValue is null
        defaultFillRuleItemShouldNotBeFound("seqStartValue.specified=false");
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is greater than or equal to DEFAULT_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.greaterThanOrEqual=" + DEFAULT_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue is greater than or equal to UPDATED_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.greaterThanOrEqual=" + UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is less than or equal to DEFAULT_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.lessThanOrEqual=" + DEFAULT_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue is less than or equal to SMALLER_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.lessThanOrEqual=" + SMALLER_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsLessThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is less than DEFAULT_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.lessThan=" + DEFAULT_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue is less than UPDATED_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.lessThan=" + UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void getAllFillRuleItemsBySeqStartValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        // Get all the fillRuleItemList where seqStartValue is greater than DEFAULT_SEQ_START_VALUE
        defaultFillRuleItemShouldNotBeFound("seqStartValue.greaterThan=" + DEFAULT_SEQ_START_VALUE);

        // Get all the fillRuleItemList where seqStartValue is greater than SMALLER_SEQ_START_VALUE
        defaultFillRuleItemShouldBeFound("seqStartValue.greaterThan=" + SMALLER_SEQ_START_VALUE);
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

        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
        FillRuleItem testFillRuleItem = fillRuleItemList.get(fillRuleItemList.size() - 1);
        assertThat(testFillRuleItem.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testFillRuleItem.getFieldParamType()).isEqualTo(UPDATED_FIELD_PARAM_TYPE);
        assertThat(testFillRuleItem.getFieldParamValue()).isEqualTo(UPDATED_FIELD_PARAM_VALUE);
        assertThat(testFillRuleItem.getDatePattern()).isEqualTo(UPDATED_DATE_PATTERN);
        assertThat(testFillRuleItem.getSeqLength()).isEqualTo(UPDATED_SEQ_LENGTH);
        assertThat(testFillRuleItem.getSeqIncrement()).isEqualTo(UPDATED_SEQ_INCREMENT);
        assertThat(testFillRuleItem.getSeqStartValue()).isEqualTo(UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fillRuleItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFillRuleItemWithPatch() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();

        // Update the fillRuleItem using partial update
        FillRuleItem partialUpdatedFillRuleItem = new FillRuleItem();
        partialUpdatedFillRuleItem.setId(fillRuleItem.getId());

        partialUpdatedFillRuleItem
            .fieldParamType(UPDATED_FIELD_PARAM_TYPE)
            .fieldParamValue(UPDATED_FIELD_PARAM_VALUE)
            .seqLength(UPDATED_SEQ_LENGTH)
            .seqStartValue(UPDATED_SEQ_START_VALUE);

        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFillRuleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFillRuleItem))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
        FillRuleItem testFillRuleItem = fillRuleItemList.get(fillRuleItemList.size() - 1);
        assertThat(testFillRuleItem.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testFillRuleItem.getFieldParamType()).isEqualTo(UPDATED_FIELD_PARAM_TYPE);
        assertThat(testFillRuleItem.getFieldParamValue()).isEqualTo(UPDATED_FIELD_PARAM_VALUE);
        assertThat(testFillRuleItem.getDatePattern()).isEqualTo(DEFAULT_DATE_PATTERN);
        assertThat(testFillRuleItem.getSeqLength()).isEqualTo(UPDATED_SEQ_LENGTH);
        assertThat(testFillRuleItem.getSeqIncrement()).isEqualTo(DEFAULT_SEQ_INCREMENT);
        assertThat(testFillRuleItem.getSeqStartValue()).isEqualTo(UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateFillRuleItemWithPatch() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFillRuleItem))
            )
            .andExpect(status().isOk());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
        FillRuleItem testFillRuleItem = fillRuleItemList.get(fillRuleItemList.size() - 1);
        assertThat(testFillRuleItem.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testFillRuleItem.getFieldParamType()).isEqualTo(UPDATED_FIELD_PARAM_TYPE);
        assertThat(testFillRuleItem.getFieldParamValue()).isEqualTo(UPDATED_FIELD_PARAM_VALUE);
        assertThat(testFillRuleItem.getDatePattern()).isEqualTo(UPDATED_DATE_PATTERN);
        assertThat(testFillRuleItem.getSeqLength()).isEqualTo(UPDATED_SEQ_LENGTH);
        assertThat(testFillRuleItem.getSeqIncrement()).isEqualTo(UPDATED_SEQ_INCREMENT);
        assertThat(testFillRuleItem.getSeqStartValue()).isEqualTo(UPDATED_SEQ_START_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fillRuleItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFillRuleItem() throws Exception {
        int databaseSizeBeforeUpdate = fillRuleItemRepository.findAll().size();
        fillRuleItem.setId(longCount.incrementAndGet());

        // Create the FillRuleItem
        FillRuleItemDTO fillRuleItemDTO = fillRuleItemMapper.toDto(fillRuleItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFillRuleItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fillRuleItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FillRuleItem in the database
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFillRuleItem() throws Exception {
        // Initialize the database
        fillRuleItemRepository.save(fillRuleItem);

        int databaseSizeBeforeDelete = fillRuleItemRepository.findAll().size();

        // Delete the fillRuleItem
        restFillRuleItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, fillRuleItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FillRuleItem> fillRuleItemList = fillRuleItemRepository.findAll();
        assertThat(fillRuleItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
