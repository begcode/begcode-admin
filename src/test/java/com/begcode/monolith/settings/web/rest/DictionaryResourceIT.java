package com.begcode.monolith.settings.web.rest;

import static com.begcode.monolith.settings.domain.DictionaryAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
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

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

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
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);
        var returnedDictionaryDTO = om.readValue(
            restDictionaryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dictionaryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DictionaryDTO.class
        );

        // Validate the Dictionary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDictionary = dictionaryMapper.toEntity(returnedDictionaryDTO);
        assertDictionaryUpdatableFieldsEquals(returnedDictionary, getPersistedDictionary(returnedDictionary));
    }

    @Test
    @Transactional
    void createDictionaryWithExistingId() throws Exception {
        // Create the Dictionary with an existing ID
        dictionary.setId(1L);
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDictNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dictionary.setDictName(null);

        // Create the Dictionary, which fails.
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDictKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dictionary.setDictKey(null);

        // Create the Dictionary, which fails.
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        restDictionaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dictionaryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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

        defaultDictionaryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDictionaryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDictionaryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName equals to
        defaultDictionaryFiltering("dictName.equals=" + DEFAULT_DICT_NAME, "dictName.equals=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName in
        defaultDictionaryFiltering("dictName.in=" + DEFAULT_DICT_NAME + "," + UPDATED_DICT_NAME, "dictName.in=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName is not null
        defaultDictionaryFiltering("dictName.specified=true", "dictName.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName contains
        defaultDictionaryFiltering("dictName.contains=" + DEFAULT_DICT_NAME, "dictName.contains=" + UPDATED_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictNameNotContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictName does not contain
        defaultDictionaryFiltering("dictName.doesNotContain=" + UPDATED_DICT_NAME, "dictName.doesNotContain=" + DEFAULT_DICT_NAME);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey equals to
        defaultDictionaryFiltering("dictKey.equals=" + DEFAULT_DICT_KEY, "dictKey.equals=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey in
        defaultDictionaryFiltering("dictKey.in=" + DEFAULT_DICT_KEY + "," + UPDATED_DICT_KEY, "dictKey.in=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey is not null
        defaultDictionaryFiltering("dictKey.specified=true", "dictKey.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey contains
        defaultDictionaryFiltering("dictKey.contains=" + DEFAULT_DICT_KEY, "dictKey.contains=" + UPDATED_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDictKeyNotContainsSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where dictKey does not contain
        defaultDictionaryFiltering("dictKey.doesNotContain=" + UPDATED_DICT_KEY, "dictKey.doesNotContain=" + DEFAULT_DICT_KEY);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled equals to
        defaultDictionaryFiltering("disabled.equals=" + DEFAULT_DISABLED, "disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled in
        defaultDictionaryFiltering("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED, "disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDictionariesByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where disabled is not null
        defaultDictionaryFiltering("disabled.specified=true", "disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue equals to
        defaultDictionaryFiltering("sortValue.equals=" + DEFAULT_SORT_VALUE, "sortValue.equals=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue in
        defaultDictionaryFiltering("sortValue.in=" + DEFAULT_SORT_VALUE + "," + UPDATED_SORT_VALUE, "sortValue.in=" + UPDATED_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is not null
        defaultDictionaryFiltering("sortValue.specified=true", "sortValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is greater than or equal to
        defaultDictionaryFiltering(
            "sortValue.greaterThanOrEqual=" + DEFAULT_SORT_VALUE,
            "sortValue.greaterThanOrEqual=" + UPDATED_SORT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is less than or equal to
        defaultDictionaryFiltering("sortValue.lessThanOrEqual=" + DEFAULT_SORT_VALUE, "sortValue.lessThanOrEqual=" + SMALLER_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsLessThanSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is less than
        defaultDictionaryFiltering("sortValue.lessThan=" + UPDATED_SORT_VALUE, "sortValue.lessThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesBySortValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where sortValue is greater than
        defaultDictionaryFiltering("sortValue.greaterThan=" + SMALLER_SORT_VALUE, "sortValue.greaterThan=" + DEFAULT_SORT_VALUE);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn equals to
        defaultDictionaryFiltering("builtIn.equals=" + DEFAULT_BUILT_IN, "builtIn.equals=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn in
        defaultDictionaryFiltering("builtIn.in=" + DEFAULT_BUILT_IN + "," + UPDATED_BUILT_IN, "builtIn.in=" + UPDATED_BUILT_IN);
    }

    @Test
    @Transactional
    void getAllDictionariesByBuiltInIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where builtIn is not null
        defaultDictionaryFiltering("builtIn.specified=true", "builtIn.specified=false");
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsEqualToSomething() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum equals to
        defaultDictionaryFiltering("syncEnum.equals=" + DEFAULT_SYNC_ENUM, "syncEnum.equals=" + UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsInShouldWork() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum in
        defaultDictionaryFiltering("syncEnum.in=" + DEFAULT_SYNC_ENUM + "," + UPDATED_SYNC_ENUM, "syncEnum.in=" + UPDATED_SYNC_ENUM);
    }

    @Test
    @Transactional
    void getAllDictionariesBySyncEnumIsNullOrNotNull() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        // Get all the dictionaryList where syncEnum is not null
        defaultDictionaryFiltering("syncEnum.specified=true", "syncEnum.specified=false");
    }

    private void defaultDictionaryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDictionaryShouldBeFound(shouldBeFound);
        defaultDictionaryShouldNotBeFound(shouldNotBeFound);
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

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(dictionaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDictionaryToMatchAllProperties(updatedDictionary);
    }

    @Test
    @Transactional
    void putNonExistingDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dictionaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDictionaryWithPatch() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dictionary using partial update
        Dictionary partialUpdatedDictionary = new Dictionary();
        partialUpdatedDictionary.setId(dictionary.getId());

        partialUpdatedDictionary.dictName(UPDATED_DICT_NAME).syncEnum(UPDATED_SYNC_ENUM);

        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDictionary))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDictionaryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDictionary, dictionary),
            getPersistedDictionary(dictionary)
        );
    }

    @Test
    @Transactional
    void fullUpdateDictionaryWithPatch() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedDictionary))
            )
            .andExpect(status().isOk());

        // Validate the Dictionary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDictionaryUpdatableFieldsEquals(partialUpdatedDictionary, getPersistedDictionary(partialUpdatedDictionary));
    }

    @Test
    @Transactional
    void patchNonExistingDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dictionaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDictionary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dictionary.setId(longCount.incrementAndGet());

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDictionaryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dictionaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dictionary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.save(dictionary);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dictionary
        restDictionaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, dictionary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dictionaryRepository.selectCount(null);
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

    protected Dictionary getPersistedDictionary(Dictionary dictionary) {
        return dictionaryRepository.findById(dictionary.getId()).orElseThrow();
    }

    protected void assertPersistedDictionaryToMatchAllProperties(Dictionary expectedDictionary) {
        assertDictionaryAllPropertiesEquals(expectedDictionary, getPersistedDictionary(expectedDictionary));
    }

    protected void assertPersistedDictionaryToMatchUpdatableProperties(Dictionary expectedDictionary) {
        assertDictionaryAllUpdatablePropertiesEquals(expectedDictionary, getPersistedDictionary(expectedDictionary));
    }
}
