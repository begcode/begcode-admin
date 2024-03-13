package com.begcode.monolith.settings.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.settings.domain.Dictionary;
import com.begcode.monolith.settings.repository.DictionaryRepository;
import com.begcode.monolith.settings.service.dto.DictionaryDTO;
import com.begcode.monolith.settings.service.mapper.DictionaryMapper;
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
 * Integration tests for the {@link DictionaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class DictionaryResourceIT {

    private static final String DEFAULT_DICT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DICT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DICT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DICT_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final Integer DEFAULT_SORT_VALUE = 1;
    private static final Integer UPDATED_SORT_VALUE = 2;
    private static final Integer SMALLER_SORT_VALUE = 1 - 1;

    private static final Boolean DEFAULT_BUILT_IN = false;
    private static final Boolean UPDATED_BUILT_IN = true;

    private static final Boolean DEFAULT_SYNC_ENUM = false;
    private static final Boolean UPDATED_SYNC_ENUM = true;

    private static final String ENTITY_API_URL = "/api/dictionaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private MockMvc restDictionaryMockMvc;

    private Dictionary dictionary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dictionary createEntity() {
        Dictionary dictionary = new Dictionary()
            .dictName(DEFAULT_DICT_NAME)
            .dictKey(DEFAULT_DICT_KEY)
            .disabled(DEFAULT_DISABLED)
            .sortValue(DEFAULT_SORT_VALUE)
            .builtIn(DEFAULT_BUILT_IN)
            .syncEnum(DEFAULT_SYNC_ENUM);
        return dictionary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dictionary createUpdatedEntity() {
        Dictionary dictionary = new Dictionary()
            .dictName(UPDATED_DICT_NAME)
            .dictKey(UPDATED_DICT_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .syncEnum(UPDATED_SYNC_ENUM);
        return dictionary;
    }

    @BeforeEach
    public void initTest() {
        dictionary = createEntity();
    }

    @Test
    @Transactional
    void createDictionary() throws Exception {
        int databaseSizeBeforeCreate = dictionaryRepository.findAll().size();
        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);
        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeCreate + 1);
        Dictionary testDictionary = dictionaryList.get(dictionaryList.size() - 1);
        assertThat(testDictionary.getDictName()).isEqualTo(DEFAULT_DICT_NAME);
        assertThat(testDictionary.getDictKey()).isEqualTo(DEFAULT_DICT_KEY);
        assertThat(testDictionary.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testDictionary.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testDictionary.getBuiltIn()).isEqualTo(DEFAULT_BUILT_IN);
        assertThat(testDictionary.getSyncEnum()).isEqualTo(DEFAULT_SYNC_ENUM);
    }

    @Test
    @Transactional
    void createDictionaryWithExistingId() throws Exception {
        // Create the Dictionary with an existing ID
        dictionary.setId(1L);
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        int databaseSizeBeforeCreate = dictionaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDictNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dictionaryRepository.findAll().size();
        // set the field null
        dictionary.setDictName(null);

        // Create the Dictionary, which fails.
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDictKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = dictionaryRepository.findAll().size();
        // set the field null
        dictionary.setDictKey(null);

        // Create the Dictionary, which fails.
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDictionaries() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dictName").value(hasItem(DEFAULT_DICT_NAME)))
            .andExpect(jsonPath("$.[*].dictKey").value(hasItem(DEFAULT_DICT_KEY)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].builtIn").value(hasItem(DEFAULT_BUILT_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].syncEnum").value(hasItem(DEFAULT_SYNC_ENUM.booleanValue())));
    }

    @Test
    @Transactional
    void getDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get the dictionary
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL_ID, dictionary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dictionary.getId().intValue()))
            .andExpect(jsonPath("$.dictName").value(DEFAULT_DICT_NAME))
            .andExpect(jsonPath("$.dictKey").value(DEFAULT_DICT_KEY))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.sortValue").value(DEFAULT_SORT_VALUE))
            .andExpect(jsonPath("$.builtIn").value(DEFAULT_BUILT_IN.booleanValue()))
            .andExpect(jsonPath("$.syncEnum").value(DEFAULT_SYNC_ENUM.booleanValue()));
    }

    @Test
    @Transactional
    void getDictionariesByIdFiltering() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        Long id = dictionary.getId();

        defaultDictionaryShouldBeFound("id.equals=" + id);
        defaultDictionaryShouldNotBeFound("id.notEquals=" + id);

        defaultDictionaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDictionaryShouldNotBeFound("id.greaterThan=" + id);

        defaultDictionaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDictionaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName equals to DEFAULT_DICT_NAME
        defaultDictionaryShouldBeFound("dictName.equals=" + DEFAULT_DICT_NAME);

        // Get all the dictionaryList where dictName equals to UPDATED_DICT_NAME
        defaultDictionaryShouldNotBeFound("dictName.equals=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName in DEFAULT_DICT_NAME or UPDATED_DICT_NAME
        defaultDictionaryShouldBeFound("dictName.in=" + DEFAULT_DICT_NAME + "," + UPDATED_DICT_NAME);

        // Get all the dictionaryList where dictName equals to UPDATED_DICT_NAME
        defaultDictionaryShouldNotBeFound("dictName.in=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName is not null
        defaultDictionaryShouldBeFound("dictName.specified=true");

        // Get all the dictionaryList where dictName is null
        defaultDictionaryShouldNotBeFound("dictName.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName contains DEFAULT_DICT_NAME
        defaultDictionaryShouldBeFound("dictName.contains=" + DEFAULT_DICT_NAME);

        // Get all the dictionaryList where dictName contains UPDATED_DICT_NAME
        defaultDictionaryShouldNotBeFound("dictName.contains=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameNotContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName does not contain DEFAULT_DICT_NAME
        defaultDictionaryShouldNotBeFound("dictName.doesNotContain=" + DEFAULT_DICT_NAME);

        // Get all the dictionaryList where dictName does not contain UPDATED_DICT_NAME
        defaultDictionaryShouldBeFound("dictName.doesNotContain=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey equals to DEFAULT_DICT_KEY
        defaultDictionaryShouldBeFound("dictKey.equals=" + DEFAULT_DICT_KEY);

        // Get all the dictionaryList where dictKey equals to UPDATED_DICT_KEY
        defaultDictionaryShouldNotBeFound("dictKey.equals=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey in DEFAULT_DICT_KEY or UPDATED_DICT_KEY
        defaultDictionaryShouldBeFound("dictKey.in=" + DEFAULT_DICT_KEY + "," + UPDATED_DICT_KEY);

        // Get all the dictionaryList where dictKey equals to UPDATED_DICT_KEY
        defaultDictionaryShouldNotBeFound("dictKey.in=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey is not null
        defaultDictionaryShouldBeFound("dictKey.specified=true");

        // Get all the dictionaryList where dictKey is null
        defaultDictionaryShouldNotBeFound("dictKey.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey contains DEFAULT_DICT_KEY
        defaultDictionaryShouldBeFound("dictKey.contains=" + DEFAULT_DICT_KEY);

        // Get all the dictionaryList where dictKey contains UPDATED_DICT_KEY
        defaultDictionaryShouldNotBeFound("dictKey.contains=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyNotContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey does not contain DEFAULT_DICT_KEY
        defaultDictionaryShouldNotBeFound("dictKey.doesNotContain=" + DEFAULT_DICT_KEY);

        // Get all the dictionaryList where dictKey does not contain UPDATED_DICT_KEY
        defaultDictionaryShouldBeFound("dictKey.doesNotContain=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled equals to DEFAULT_DISABLED
        defaultDictionaryShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the dictionaryList where disabled equals to UPDATED_DISABLED
        defaultDictionaryShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultDictionaryShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the dictionaryList where disabled equals to UPDATED_DISABLED
        defaultDictionaryShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled is not null
        defaultDictionaryShouldBeFound("disabled.specified=true");

        // Get all the dictionaryList where disabled is null
        defaultDictionaryShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue equals to DEFAULT_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.equals=" + DEFAULT_SORT_VALUE);

        // Get all the dictionaryList where sortValue equals to UPDATED_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue in DEFAULT_SORT_VALUE or UPDATED_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE);

        // Get all the dictionaryList where sortValue equals to UPDATED_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is not null
        defaultDictionaryShouldBeFound("sortValue.specified=true");

        // Get all the dictionaryList where sortValue is null
        defaultDictionaryShouldNotBeFound("sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is greater than or equal to DEFAULT_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the dictionaryList where sortValue is greater than or equal to UPDATED_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is less than or equal to DEFAULT_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE);

        // Get all the dictionaryList where sortValue is less than or equal to SMALLER_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is less than DEFAULT_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.lessThan=" + DEFAULT_SORT_VALUE);

        // Get all the dictionaryList where sortValue is less than UPDATED_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.lessThan=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is greater than DEFAULT_SORT_VALUE
        defaultDictionaryShouldNotBeFound("sortValue.greaterThan=" + DEFAULT_SORT_VALUE);

        // Get all the dictionaryList where sortValue is greater than SMALLER_SORT_VALUE
        defaultDictionaryShouldBeFound("sortValue.greaterThan=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn equals to DEFAULT_BUILT_IN
        defaultDictionaryShouldBeFound("builtIn.equals=" + DEFAULT_BUILT_IN);

        // Get all the dictionaryList where builtIn equals to UPDATED_BUILT_IN
        defaultDictionaryShouldNotBeFound("builtIn.equals=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn in DEFAULT_BUILT_IN or UPDATED_BUILT_IN
        defaultDictionaryShouldBeFound("builtIn.in=" + DEFAULT_BUILT_IN + "," + UPDATED_BUILT_IN);

        // Get all the dictionaryList where builtIn equals to UPDATED_BUILT_IN
        defaultDictionaryShouldNotBeFound("builtIn.in=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn is not null
        defaultDictionaryShouldBeFound("builtIn.specified=true");

        // Get all the dictionaryList where builtIn is null
        defaultDictionaryShouldNotBeFound("builtIn.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum equals to DEFAULT_SYNC_ENUM
        defaultDictionaryShouldBeFound("syncEnum.equals=" + DEFAULT_SYNC_ENUM);

        // Get all the dictionaryList where syncEnum equals to UPDATED_SYNC_ENUM
        defaultDictionaryShouldNotBeFound("syncEnum.equals=" + UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum in DEFAULT_SYNC_ENUM or UPDATED_SYNC_ENUM
        defaultDictionaryShouldBeFound("syncEnum.in=" + DEFAULT_SYNC_ENUM + "," + UPDATED_SYNC_ENUM);

        // Get all the dictionaryList where syncEnum equals to UPDATED_SYNC_ENUM
        defaultDictionaryShouldNotBeFound("syncEnum.in=" + UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum is not null
        defaultDictionaryShouldBeFound("syncEnum.specified=true");

        // Get all the dictionaryList where syncEnum is null
        defaultDictionaryShouldNotBeFound("syncEnum.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDictionaryShouldBeFound(String filter) throws Exception {
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dictName").value(hasItem(DEFAULT_DICT_NAME)))
            .andExpect(jsonPath("$.[*].dictKey").value(hasItem(DEFAULT_DICT_KEY)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sortValue").value(hasItem(DEFAULT_SORT_VALUE)))
            .andExpect(jsonPath("$.[*].builtIn").value(hasItem(DEFAULT_BUILT_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].syncEnum").value(hasItem(DEFAULT_SYNC_ENUM.booleanValue())));

        // Check, that the count call also returns 1
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDictionaryShouldNotBeFound(String filter) throws Exception {
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDictionary() throws Exception {
        // Get the dictionary
        restDictionaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();

        // Update the dictionary
        Dictionary updatedDictionary = dictionaryRepository.findById(dictionary.getId()).orElseThrow();
        updatedDictionary
            .dictName(UPDATED_DICT_NAME)
            .dictKey(UPDATED_DICT_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .syncEnum(UPDATED_SYNC_ENUM);
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(updatedDictionary);

        restDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
        Dictionary testDictionary = dictionaryList.get(dictionaryList.size() - 1);
        assertThat(testDictionary.getDictName()).isEqualTo(UPDATED_DICT_NAME);
        assertThat(testDictionary.getDictKey()).isEqualTo(UPDATED_DICT_KEY);
        assertThat(testDictionary.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDictionary.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testDictionary.getBuiltIn()).isEqualTo(UPDATED_BUILT_IN);
        assertThat(testDictionary.getSyncEnum()).isEqualTo(UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void putNonExistingDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDictionaryWithPatch() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();

        // Update the dictionary using partial update
        Dictionary partialUpdatedDictionary = new Dictionary();
        partialUpdatedDictionary.setId(dictionary.getId());

        partialUpdatedDictionary.builtIn(UPDATED_BUILT_IN).syncEnum(UPDATED_SYNC_ENUM);

        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDictionary))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
        Dictionary testDictionary = dictionaryList.get(dictionaryList.size() - 1);
        assertThat(testDictionary.getDictName()).isEqualTo(DEFAULT_DICT_NAME);
        assertThat(testDictionary.getDictKey()).isEqualTo(DEFAULT_DICT_KEY);
        assertThat(testDictionary.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testDictionary.getSortValue()).isEqualTo(DEFAULT_SORT_VALUE);
        assertThat(testDictionary.getBuiltIn()).isEqualTo(UPDATED_BUILT_IN);
        assertThat(testDictionary.getSyncEnum()).isEqualTo(UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void fullUpdateDictionaryWithPatch() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();

        // Update the dictionary using partial update
        Dictionary partialUpdatedDictionary = new Dictionary();
        partialUpdatedDictionary.setId(dictionary.getId());

        partialUpdatedDictionary
            .dictName(UPDATED_DICT_NAME)
            .dictKey(UPDATED_DICT_KEY)
            .disabled(UPDATED_DISABLED)
            .sortValue(UPDATED_SORT_VALUE)
            .builtIn(UPDATED_BUILT_IN)
            .syncEnum(UPDATED_SYNC_ENUM);

        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDictionary))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
        Dictionary testDictionary = dictionaryList.get(dictionaryList.size() - 1);
        assertThat(testDictionary.getDictName()).isEqualTo(UPDATED_DICT_NAME);
        assertThat(testDictionary.getDictKey()).isEqualTo(UPDATED_DICT_KEY);
        assertThat(testDictionary.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDictionary.getSortValue()).isEqualTo(UPDATED_SORT_VALUE);
        assertThat(testDictionary.getBuiltIn()).isEqualTo(UPDATED_BUILT_IN);
        assertThat(testDictionary.getSyncEnum()).isEqualTo(UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void patchNonExistingDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dictionaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dictionaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        int databaseSizeBeforeDelete = dictionaryRepository.findAll().size();

        // Delete the dictionary
        restDictionaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, dictionary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dictionary> dictionaryList = dictionaryRepository.findAll();
        assertThat(dictionaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
