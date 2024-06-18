package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.UReportFileAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.UReportFile;
import com.begcode.monolith.repository.UReportFileRepository;
import com.begcode.monolith.service.dto.UReportFileDTO;
import com.begcode.monolith.service.mapper.UReportFileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link UReportFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class UReportFileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/u-report-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UReportFileRepository uReportFileRepository;

    @Autowired
    private UReportFileMapper uReportFileMapper;

    @Autowired
    private MockMvc restUReportFileMockMvc;

    private UReportFile uReportFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UReportFile createEntity() {
        UReportFile uReportFile = new UReportFile().name(DEFAULT_NAME).createAt(DEFAULT_CREATE_AT).updateAt(DEFAULT_UPDATE_AT);
        return uReportFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UReportFile createUpdatedEntity() {
        UReportFile uReportFile = new UReportFile().name(UPDATED_NAME).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);
        return uReportFile;
    }

    @BeforeEach
    public void initTest() {
        uReportFile = createEntity();
    }

    @Test
    @Transactional
    void createUReportFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);
        var returnedUReportFileDTO = om.readValue(
            restUReportFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uReportFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UReportFileDTO.class
        );

        // Validate the UReportFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUReportFile = uReportFileMapper.toEntity(returnedUReportFileDTO);
        assertUReportFileUpdatableFieldsEquals(returnedUReportFile, getPersistedUReportFile(returnedUReportFile));
    }

    @Test
    @Transactional
    void createUReportFileWithExistingId() throws Exception {
        // Create the UReportFile with an existing ID
        uReportFile.setId(1L);
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUReportFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uReportFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uReportFile.setName(null);

        // Create the UReportFile, which fails.
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        restUReportFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uReportFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUReportFiles() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uReportFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))));
    }

    @Test
    @Transactional
    void getUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get the uReportFile
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL_ID, uReportFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uReportFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createAt").value(sameInstant(DEFAULT_CREATE_AT)))
            .andExpect(jsonPath("$.updateAt").value(sameInstant(DEFAULT_UPDATE_AT)));
    }

    @Test
    @Transactional
    void getUReportFilesByIdFiltering() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        Long id = uReportFile.getId();

        defaultUReportFileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUReportFileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUReportFileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where name equals to
        defaultUReportFileFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where name in
        defaultUReportFileFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where name is not null
        defaultUReportFileFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where name contains
        defaultUReportFileFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where name does not contain
        defaultUReportFileFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt equals to
        defaultUReportFileFiltering("createAt.equals=" + DEFAULT_CREATE_AT, "createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt in
        defaultUReportFileFiltering("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT, "createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt is not null
        defaultUReportFileFiltering("createAt.specified=true", "createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt is greater than or equal to
        defaultUReportFileFiltering("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt is less than or equal to
        defaultUReportFileFiltering("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt is less than
        defaultUReportFileFiltering("createAt.lessThan=" + UPDATED_CREATE_AT, "createAt.lessThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where createAt is greater than
        defaultUReportFileFiltering("createAt.greaterThan=" + SMALLER_CREATE_AT, "createAt.greaterThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt equals to
        defaultUReportFileFiltering("updateAt.equals=" + DEFAULT_UPDATE_AT, "updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt in
        defaultUReportFileFiltering("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT, "updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt is not null
        defaultUReportFileFiltering("updateAt.specified=true", "updateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt is greater than or equal to
        defaultUReportFileFiltering("updateAt.greaterThanOrEqual=" + DEFAULT_UPDATE_AT, "updateAt.greaterThanOrEqual=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt is less than or equal to
        defaultUReportFileFiltering("updateAt.lessThanOrEqual=" + DEFAULT_UPDATE_AT, "updateAt.lessThanOrEqual=" + SMALLER_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt is less than
        defaultUReportFileFiltering("updateAt.lessThan=" + UPDATED_UPDATE_AT, "updateAt.lessThan=" + DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        // Get all the uReportFileList where updateAt is greater than
        defaultUReportFileFiltering("updateAt.greaterThan=" + SMALLER_UPDATE_AT, "updateAt.greaterThan=" + DEFAULT_UPDATE_AT);
    }

    private void defaultUReportFileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUReportFileShouldBeFound(shouldBeFound);
        defaultUReportFileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUReportFileShouldBeFound(String filter) throws Exception {
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uReportFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))));

        // Check, that the count call also returns 1
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUReportFileShouldNotBeFound(String filter) throws Exception {
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUReportFile() throws Exception {
        // Get the uReportFile
        restUReportFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uReportFile
        UReportFile updatedUReportFile = uReportFileRepository.findById(uReportFile.getId()).orElseThrow();
        updatedUReportFile.name(UPDATED_NAME).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(updatedUReportFile);

        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uReportFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUReportFileToMatchAllProperties(updatedUReportFile);
    }

    @Test
    @Transactional
    void putNonExistingUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uReportFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUReportFileWithPatch() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uReportFile using partial update
        UReportFile partialUpdatedUReportFile = new UReportFile();
        partialUpdatedUReportFile.setId(uReportFile.getId());

        partialUpdatedUReportFile.name(UPDATED_NAME).updateAt(UPDATED_UPDATE_AT);

        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUReportFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUReportFile))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUReportFileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUReportFile, uReportFile),
            getPersistedUReportFile(uReportFile)
        );
    }

    @Test
    @Transactional
    void fullUpdateUReportFileWithPatch() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uReportFile using partial update
        UReportFile partialUpdatedUReportFile = new UReportFile();
        partialUpdatedUReportFile.setId(uReportFile.getId());

        partialUpdatedUReportFile.name(UPDATED_NAME).createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT);

        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUReportFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUReportFile))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUReportFileUpdatableFieldsEquals(partialUpdatedUReportFile, getPersistedUReportFile(partialUpdatedUReportFile));
    }

    @Test
    @Transactional
    void patchNonExistingUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUReportFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uReportFile.setId(longCount.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uReportFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UReportFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.save(uReportFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uReportFile
        restUReportFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, uReportFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uReportFileRepository.selectCount(null);
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

    protected UReportFile getPersistedUReportFile(UReportFile uReportFile) {
        return uReportFileRepository.findById(uReportFile.getId()).orElseThrow();
    }

    protected void assertPersistedUReportFileToMatchAllProperties(UReportFile expectedUReportFile) {
        assertUReportFileAllPropertiesEquals(expectedUReportFile, getPersistedUReportFile(expectedUReportFile));
    }

    protected void assertPersistedUReportFileToMatchUpdatableProperties(UReportFile expectedUReportFile) {
        assertUReportFileAllUpdatablePropertiesEquals(expectedUReportFile, getPersistedUReportFile(expectedUReportFile));
    }
}
