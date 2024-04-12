package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.PositionAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.Position;
import com.begcode.monolith.repository.PositionRepository;
import com.begcode.monolith.service.dto.PositionDTO;
import com.begcode.monolith.service.mapper.PositionMapper;
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
 * Integration tests for the {@link PositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class PositionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_NO = 1;
    private static final Integer UPDATED_SORT_NO = 2;
    private static final Integer SMALLER_SORT_NO = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity() {
        Position position = new Position().code(DEFAULT_CODE).name(DEFAULT_NAME).sortNo(DEFAULT_SORT_NO).description(DEFAULT_DESCRIPTION);
        return position;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity() {
        Position position = new Position().code(UPDATED_CODE).name(UPDATED_NAME).sortNo(UPDATED_SORT_NO).description(UPDATED_DESCRIPTION);
        return position;
    }

    @BeforeEach
    public void initTest() {
        position = createEntity();
    }

    @Test
    @Transactional
    void createPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        var returnedPositionDTO = om.readValue(
            restPositionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PositionDTO.class
        );

        // Validate the Position in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPosition = positionMapper.toEntity(returnedPositionDTO);
        assertPositionUpdatableFieldsEquals(returnedPosition, getPersistedPosition(returnedPosition));
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setCode(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        position.setName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get the position
        restPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sortNo").value(DEFAULT_SORT_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPositionsByIdFiltering() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        Long id = position.getId();

        defaultPositionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPositionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPositionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where code equals to
        defaultPositionFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where code in
        defaultPositionFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where code is not null
        defaultPositionFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where code contains
        defaultPositionFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where code does not contain
        defaultPositionFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where name equals to
        defaultPositionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where name in
        defaultPositionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where name is not null
        defaultPositionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where name contains
        defaultPositionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where name does not contain
        defaultPositionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo equals to
        defaultPositionFiltering("sortNo.equals=" + DEFAULT_SORT_NO, "sortNo.equals=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo in
        defaultPositionFiltering("sortNo.in=" + DEFAULT_SORT_NO + "," + UPDATED_SORT_NO, "sortNo.in=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo is not null
        defaultPositionFiltering("sortNo.specified=true", "sortNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo is greater than or equal to
        defaultPositionFiltering("sortNo.greaterThanOrEqual=" + DEFAULT_SORT_NO, "sortNo.greaterThanOrEqual=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo is less than or equal to
        defaultPositionFiltering("sortNo.lessThanOrEqual=" + DEFAULT_SORT_NO, "sortNo.lessThanOrEqual=" + SMALLER_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsLessThanSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo is less than
        defaultPositionFiltering("sortNo.lessThan=" + UPDATED_SORT_NO, "sortNo.lessThan=" + DEFAULT_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where sortNo is greater than
        defaultPositionFiltering("sortNo.greaterThan=" + SMALLER_SORT_NO, "sortNo.greaterThan=" + DEFAULT_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where description equals to
        defaultPositionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where description in
        defaultPositionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where description is not null
        defaultPositionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where description contains
        defaultPositionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positionList where description does not contain
        defaultPositionFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    private void defaultPositionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPositionShouldBeFound(shouldBeFound);
        defaultPositionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionShouldBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionShouldNotBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).orElseThrow();
        updatedPosition.code(UPDATED_CODE).name(UPDATED_NAME).sortNo(UPDATED_SORT_NO).description(UPDATED_DESCRIPTION);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPositionToMatchAllProperties(updatedPosition);
    }

    @Test
    @Transactional
    void putNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.code(UPDATED_CODE).name(UPDATED_NAME);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosition, position), getPersistedPosition(position));
    }

    @Test
    @Transactional
    void fullUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.code(UPDATED_CODE).name(UPDATED_NAME).sortNo(UPDATED_SORT_NO).description(UPDATED_DESCRIPTION);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPositionUpdatableFieldsEquals(partialUpdatedPosition, getPersistedPosition(partialUpdatedPosition));
    }

    @Test
    @Transactional
    void patchNonExistingPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        position.setId(longCount.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the position
        restPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, position.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return positionRepository.selectCount(null);
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

    protected Position getPersistedPosition(Position position) {
        return positionRepository.findById(position.getId()).orElseThrow();
    }

    protected void assertPersistedPositionToMatchAllProperties(Position expectedPosition) {
        assertPositionAllPropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }

    protected void assertPersistedPositionToMatchUpdatableProperties(Position expectedPosition) {
        assertPositionAllUpdatablePropertiesEquals(expectedPosition, getPersistedPosition(expectedPosition));
    }
}
