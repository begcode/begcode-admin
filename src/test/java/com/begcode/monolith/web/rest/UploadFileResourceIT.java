package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.UploadFileAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.UploadFile;
import com.begcode.monolith.repository.UploadFileRepository;
import com.begcode.monolith.service.UploadFileService;
import com.begcode.monolith.service.dto.UploadFileDTO;
import com.begcode.monolith.service.mapper.UploadFileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UploadFileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class UploadFileResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_THUMB = "AAAAAAAAAA";
    private static final String UPDATED_THUMB = "BBBBBBBBBB";

    private static final String DEFAULT_EXT = "AAAAAAAAAA";
    private static final String UPDATED_EXT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FOLDER = "AAAAAAAAAA";
    private static final String UPDATED_FOLDER = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_OWNER_ENTITY_ID = 1L;
    private static final Long UPDATED_OWNER_ENTITY_ID = 2L;
    private static final Long SMALLER_OWNER_ENTITY_ID = 1L - 1L;

    private static final String DEFAULT_BUSINESS_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final Long DEFAULT_REFERENCE_COUNT = 1L;
    private static final Long UPDATED_REFERENCE_COUNT = 2L;
    private static final Long SMALLER_REFERENCE_COUNT = 1L - 1L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;
    private static final Long SMALLER_CREATED_BY = 1L - 1L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;
    private static final Long SMALLER_LAST_MODIFIED_BY = 1L - 1L;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/upload-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Mock
    private UploadFileRepository uploadFileRepositoryMock;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Mock
    private UploadFileService uploadFileServiceMock;

    @Autowired
    private MockMvc restUploadFileMockMvc;

    private UploadFile uploadFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadFile createEntity() {
        UploadFile uploadFile = new UploadFile()
            .url(DEFAULT_URL)
            .fullName(DEFAULT_FULL_NAME)
            .name(DEFAULT_NAME)
            .thumb(DEFAULT_THUMB)
            .ext(DEFAULT_EXT)
            .type(DEFAULT_TYPE)
            .path(DEFAULT_PATH)
            .folder(DEFAULT_FOLDER)
            .ownerEntityName(DEFAULT_OWNER_ENTITY_NAME)
            .ownerEntityId(DEFAULT_OWNER_ENTITY_ID)
            .businessTitle(DEFAULT_BUSINESS_TITLE)
            .businessDesc(DEFAULT_BUSINESS_DESC)
            .businessStatus(DEFAULT_BUSINESS_STATUS)
            .createAt(DEFAULT_CREATE_AT)
            .fileSize(DEFAULT_FILE_SIZE)
            .referenceCount(DEFAULT_REFERENCE_COUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return uploadFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadFile createUpdatedEntity() {
        UploadFile uploadFile = new UploadFile()
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
            .thumb(UPDATED_THUMB)
            .ext(UPDATED_EXT)
            .type(UPDATED_TYPE)
            .path(UPDATED_PATH)
            .folder(UPDATED_FOLDER)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessTitle(UPDATED_BUSINESS_TITLE)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .fileSize(UPDATED_FILE_SIZE)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return uploadFile;
    }

    @BeforeEach
    public void initTest() {
        uploadFile = createEntity();
    }

    @Test
    @Transactional
    void createUploadFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);
        var returnedUploadFileDTO = om.readValue(
            restUploadFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadFileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UploadFileDTO.class
        );

        // Validate the UploadFile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUploadFile = uploadFileMapper.toEntity(returnedUploadFileDTO);
        assertUploadFileUpdatableFieldsEquals(returnedUploadFile, getPersistedUploadFile(returnedUploadFile));
    }

    @Test
    @Transactional
    void createUploadFileWithExistingId() throws Exception {
        // Create the UploadFile with an existing ID
        uploadFile.setId(1L);
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uploadFile.setUrl(null);

        // Create the UploadFile, which fails.
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        restUploadFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUploadFiles() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].thumb").value(hasItem(DEFAULT_THUMB)))
            .andExpect(jsonPath("$.[*].ext").value(hasItem(DEFAULT_EXT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].folder").value(hasItem(DEFAULT_FOLDER)))
            .andExpect(jsonPath("$.[*].ownerEntityName").value(hasItem(DEFAULT_OWNER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].ownerEntityId").value(hasItem(DEFAULT_OWNER_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessTitle").value(hasItem(DEFAULT_BUSINESS_TITLE)))
            .andExpect(jsonPath("$.[*].businessDesc").value(hasItem(DEFAULT_BUSINESS_DESC)))
            .andExpect(jsonPath("$.[*].businessStatus").value(hasItem(DEFAULT_BUSINESS_STATUS)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].referenceCount").value(hasItem(DEFAULT_REFERENCE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUploadFilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(uploadFileServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restUploadFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(uploadFileServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUploadFilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(uploadFileServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restUploadFileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(uploadFileRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get the uploadFile
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL_ID, uploadFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uploadFile.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.thumb").value(DEFAULT_THUMB))
            .andExpect(jsonPath("$.ext").value(DEFAULT_EXT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.folder").value(DEFAULT_FOLDER))
            .andExpect(jsonPath("$.ownerEntityName").value(DEFAULT_OWNER_ENTITY_NAME))
            .andExpect(jsonPath("$.ownerEntityId").value(DEFAULT_OWNER_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.businessTitle").value(DEFAULT_BUSINESS_TITLE))
            .andExpect(jsonPath("$.businessDesc").value(DEFAULT_BUSINESS_DESC))
            .andExpect(jsonPath("$.businessStatus").value(DEFAULT_BUSINESS_STATUS))
            .andExpect(jsonPath("$.createAt").value(sameInstant(DEFAULT_CREATE_AT)))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.referenceCount").value(DEFAULT_REFERENCE_COUNT.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUploadFilesByIdFiltering() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        Long id = uploadFile.getId();

        defaultUploadFileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUploadFileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUploadFileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url equals to
        defaultUploadFileFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url in
        defaultUploadFileFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url is not null
        defaultUploadFileFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url contains
        defaultUploadFileFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url does not contain
        defaultUploadFileFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName equals to
        defaultUploadFileFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName in
        defaultUploadFileFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName is not null
        defaultUploadFileFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName contains
        defaultUploadFileFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName does not contain
        defaultUploadFileFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name equals to
        defaultUploadFileFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name in
        defaultUploadFileFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name is not null
        defaultUploadFileFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name contains
        defaultUploadFileFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name does not contain
        defaultUploadFileFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb equals to
        defaultUploadFileFiltering("thumb.equals=" + DEFAULT_THUMB, "thumb.equals=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb in
        defaultUploadFileFiltering("thumb.in=" + DEFAULT_THUMB + "," + UPDATED_THUMB, "thumb.in=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb is not null
        defaultUploadFileFiltering("thumb.specified=true", "thumb.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb contains
        defaultUploadFileFiltering("thumb.contains=" + DEFAULT_THUMB, "thumb.contains=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb does not contain
        defaultUploadFileFiltering("thumb.doesNotContain=" + UPDATED_THUMB, "thumb.doesNotContain=" + DEFAULT_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext equals to
        defaultUploadFileFiltering("ext.equals=" + DEFAULT_EXT, "ext.equals=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext in
        defaultUploadFileFiltering("ext.in=" + DEFAULT_EXT + "," + UPDATED_EXT, "ext.in=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext is not null
        defaultUploadFileFiltering("ext.specified=true", "ext.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext contains
        defaultUploadFileFiltering("ext.contains=" + DEFAULT_EXT, "ext.contains=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext does not contain
        defaultUploadFileFiltering("ext.doesNotContain=" + UPDATED_EXT, "ext.doesNotContain=" + DEFAULT_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type equals to
        defaultUploadFileFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type in
        defaultUploadFileFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type is not null
        defaultUploadFileFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type contains
        defaultUploadFileFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type does not contain
        defaultUploadFileFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path equals to
        defaultUploadFileFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path in
        defaultUploadFileFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path is not null
        defaultUploadFileFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path contains
        defaultUploadFileFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path does not contain
        defaultUploadFileFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder equals to
        defaultUploadFileFiltering("folder.equals=" + DEFAULT_FOLDER, "folder.equals=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder in
        defaultUploadFileFiltering("folder.in=" + DEFAULT_FOLDER + "," + UPDATED_FOLDER, "folder.in=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder is not null
        defaultUploadFileFiltering("folder.specified=true", "folder.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder contains
        defaultUploadFileFiltering("folder.contains=" + DEFAULT_FOLDER, "folder.contains=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder does not contain
        defaultUploadFileFiltering("folder.doesNotContain=" + UPDATED_FOLDER, "folder.doesNotContain=" + DEFAULT_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName equals to
        defaultUploadFileFiltering(
            "ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName in
        defaultUploadFileFiltering(
            "ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName is not null
        defaultUploadFileFiltering("ownerEntityName.specified=true", "ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName contains
        defaultUploadFileFiltering(
            "ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName does not contain
        defaultUploadFileFiltering(
            "ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId equals to
        defaultUploadFileFiltering("ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID, "ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId in
        defaultUploadFileFiltering(
            "ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is not null
        defaultUploadFileFiltering("ownerEntityId.specified=true", "ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is greater than or equal to
        defaultUploadFileFiltering(
            "ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is less than or equal to
        defaultUploadFileFiltering(
            "ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is less than
        defaultUploadFileFiltering(
            "ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is greater than
        defaultUploadFileFiltering(
            "ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle equals to
        defaultUploadFileFiltering("businessTitle.equals=" + DEFAULT_BUSINESS_TITLE, "businessTitle.equals=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle in
        defaultUploadFileFiltering(
            "businessTitle.in=" + DEFAULT_BUSINESS_TITLE + "," + UPDATED_BUSINESS_TITLE,
            "businessTitle.in=" + UPDATED_BUSINESS_TITLE
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle is not null
        defaultUploadFileFiltering("businessTitle.specified=true", "businessTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle contains
        defaultUploadFileFiltering("businessTitle.contains=" + DEFAULT_BUSINESS_TITLE, "businessTitle.contains=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle does not contain
        defaultUploadFileFiltering(
            "businessTitle.doesNotContain=" + UPDATED_BUSINESS_TITLE,
            "businessTitle.doesNotContain=" + DEFAULT_BUSINESS_TITLE
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc equals to
        defaultUploadFileFiltering("businessDesc.equals=" + DEFAULT_BUSINESS_DESC, "businessDesc.equals=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc in
        defaultUploadFileFiltering(
            "businessDesc.in=" + DEFAULT_BUSINESS_DESC + "," + UPDATED_BUSINESS_DESC,
            "businessDesc.in=" + UPDATED_BUSINESS_DESC
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc is not null
        defaultUploadFileFiltering("businessDesc.specified=true", "businessDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc contains
        defaultUploadFileFiltering("businessDesc.contains=" + DEFAULT_BUSINESS_DESC, "businessDesc.contains=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc does not contain
        defaultUploadFileFiltering(
            "businessDesc.doesNotContain=" + UPDATED_BUSINESS_DESC,
            "businessDesc.doesNotContain=" + DEFAULT_BUSINESS_DESC
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus equals to
        defaultUploadFileFiltering("businessStatus.equals=" + DEFAULT_BUSINESS_STATUS, "businessStatus.equals=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus in
        defaultUploadFileFiltering(
            "businessStatus.in=" + DEFAULT_BUSINESS_STATUS + "," + UPDATED_BUSINESS_STATUS,
            "businessStatus.in=" + UPDATED_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus is not null
        defaultUploadFileFiltering("businessStatus.specified=true", "businessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus contains
        defaultUploadFileFiltering(
            "businessStatus.contains=" + DEFAULT_BUSINESS_STATUS,
            "businessStatus.contains=" + UPDATED_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus does not contain
        defaultUploadFileFiltering(
            "businessStatus.doesNotContain=" + UPDATED_BUSINESS_STATUS,
            "businessStatus.doesNotContain=" + DEFAULT_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt equals to
        defaultUploadFileFiltering("createAt.equals=" + DEFAULT_CREATE_AT, "createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt in
        defaultUploadFileFiltering("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT, "createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is not null
        defaultUploadFileFiltering("createAt.specified=true", "createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is greater than or equal to
        defaultUploadFileFiltering("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is less than or equal to
        defaultUploadFileFiltering("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is less than
        defaultUploadFileFiltering("createAt.lessThan=" + UPDATED_CREATE_AT, "createAt.lessThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is greater than
        defaultUploadFileFiltering("createAt.greaterThan=" + SMALLER_CREATE_AT, "createAt.greaterThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize equals to
        defaultUploadFileFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize in
        defaultUploadFileFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is not null
        defaultUploadFileFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is greater than or equal to
        defaultUploadFileFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is less than or equal to
        defaultUploadFileFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is less than
        defaultUploadFileFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is greater than
        defaultUploadFileFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount equals to
        defaultUploadFileFiltering("referenceCount.equals=" + DEFAULT_REFERENCE_COUNT, "referenceCount.equals=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount in
        defaultUploadFileFiltering(
            "referenceCount.in=" + DEFAULT_REFERENCE_COUNT + "," + UPDATED_REFERENCE_COUNT,
            "referenceCount.in=" + UPDATED_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is not null
        defaultUploadFileFiltering("referenceCount.specified=true", "referenceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is greater than or equal to
        defaultUploadFileFiltering(
            "referenceCount.greaterThanOrEqual=" + DEFAULT_REFERENCE_COUNT,
            "referenceCount.greaterThanOrEqual=" + UPDATED_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is less than or equal to
        defaultUploadFileFiltering(
            "referenceCount.lessThanOrEqual=" + DEFAULT_REFERENCE_COUNT,
            "referenceCount.lessThanOrEqual=" + SMALLER_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is less than
        defaultUploadFileFiltering(
            "referenceCount.lessThan=" + UPDATED_REFERENCE_COUNT,
            "referenceCount.lessThan=" + DEFAULT_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is greater than
        defaultUploadFileFiltering(
            "referenceCount.greaterThan=" + SMALLER_REFERENCE_COUNT,
            "referenceCount.greaterThan=" + DEFAULT_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy equals to
        defaultUploadFileFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy in
        defaultUploadFileFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is not null
        defaultUploadFileFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is greater than or equal to
        defaultUploadFileFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is less than or equal to
        defaultUploadFileFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is less than
        defaultUploadFileFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is greater than
        defaultUploadFileFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate equals to
        defaultUploadFileFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate in
        defaultUploadFileFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate is not null
        defaultUploadFileFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy equals to
        defaultUploadFileFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy in
        defaultUploadFileFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is not null
        defaultUploadFileFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is greater than or equal to
        defaultUploadFileFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is less than or equal to
        defaultUploadFileFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is less than
        defaultUploadFileFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is greater than
        defaultUploadFileFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate equals to
        defaultUploadFileFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate in
        defaultUploadFileFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate is not null
        defaultUploadFileFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByCategoryIsEqualToSomething() throws Exception {
        ResourceCategory category = ResourceCategoryResourceIT.createEntity();
        uploadFile.setCategory(category);
        uploadFileRepository.insert(uploadFile);
        Long categoryId = category.getId();
        // Get all the uploadFileList where category equals to categoryId
        defaultUploadFileShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the uploadFileList where category equals to (categoryId + 1)
        defaultUploadFileShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultUploadFileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUploadFileShouldBeFound(shouldBeFound);
        defaultUploadFileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUploadFileShouldBeFound(String filter) throws Exception {
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].thumb").value(hasItem(DEFAULT_THUMB)))
            .andExpect(jsonPath("$.[*].ext").value(hasItem(DEFAULT_EXT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].folder").value(hasItem(DEFAULT_FOLDER)))
            .andExpect(jsonPath("$.[*].ownerEntityName").value(hasItem(DEFAULT_OWNER_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].ownerEntityId").value(hasItem(DEFAULT_OWNER_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessTitle").value(hasItem(DEFAULT_BUSINESS_TITLE)))
            .andExpect(jsonPath("$.[*].businessDesc").value(hasItem(DEFAULT_BUSINESS_DESC)))
            .andExpect(jsonPath("$.[*].businessStatus").value(hasItem(DEFAULT_BUSINESS_STATUS)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].referenceCount").value(hasItem(DEFAULT_REFERENCE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUploadFileShouldNotBeFound(String filter) throws Exception {
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUploadFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUploadFile() throws Exception {
        // Get the uploadFile
        restUploadFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadFile
        UploadFile updatedUploadFile = uploadFileRepository.findById(uploadFile.getId()).orElseThrow();
        updatedUploadFile
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
            .thumb(UPDATED_THUMB)
            .ext(UPDATED_EXT)
            .type(UPDATED_TYPE)
            .path(UPDATED_PATH)
            .folder(UPDATED_FOLDER)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessTitle(UPDATED_BUSINESS_TITLE)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .fileSize(UPDATED_FILE_SIZE)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(updatedUploadFile);

        restUploadFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUploadFileToMatchAllProperties(updatedUploadFile);
    }

    @Test
    @Transactional
    void putNonExistingUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUploadFileWithPatch() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadFile using partial update
        UploadFile partialUpdatedUploadFile = new UploadFile();
        partialUpdatedUploadFile.setId(uploadFile.getId());

        partialUpdatedUploadFile
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
            .thumb(UPDATED_THUMB)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessTitle(UPDATED_BUSINESS_TITLE)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUploadFile))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadFileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUploadFile, uploadFile),
            getPersistedUploadFile(uploadFile)
        );
    }

    @Test
    @Transactional
    void fullUpdateUploadFileWithPatch() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadFile using partial update
        UploadFile partialUpdatedUploadFile = new UploadFile();
        partialUpdatedUploadFile.setId(uploadFile.getId());

        partialUpdatedUploadFile
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
            .thumb(UPDATED_THUMB)
            .ext(UPDATED_EXT)
            .type(UPDATED_TYPE)
            .path(UPDATED_PATH)
            .folder(UPDATED_FOLDER)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessTitle(UPDATED_BUSINESS_TITLE)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .fileSize(UPDATED_FILE_SIZE)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUploadFile))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadFileUpdatableFieldsEquals(partialUpdatedUploadFile, getPersistedUploadFile(partialUpdatedUploadFile));
    }

    @Test
    @Transactional
    void patchNonExistingUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uploadFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUploadFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uploadFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadFile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uploadFile
        restUploadFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, uploadFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uploadFileRepository.selectCount(null);
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

    protected UploadFile getPersistedUploadFile(UploadFile uploadFile) {
        return uploadFileRepository.findById(uploadFile.getId()).orElseThrow();
    }

    protected void assertPersistedUploadFileToMatchAllProperties(UploadFile expectedUploadFile) {
        assertUploadFileAllPropertiesEquals(expectedUploadFile, getPersistedUploadFile(expectedUploadFile));
    }

    protected void assertPersistedUploadFileToMatchUpdatableProperties(UploadFile expectedUploadFile) {
        assertUploadFileAllUpdatablePropertiesEquals(expectedUploadFile, getPersistedUploadFile(expectedUploadFile));
    }
}
