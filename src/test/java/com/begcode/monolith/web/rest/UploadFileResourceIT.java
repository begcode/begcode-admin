package com.begcode.monolith.web.rest;

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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        int databaseSizeBeforeCreate = uploadFileRepository.findAll().size();
        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);
        restUploadFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isCreated());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeCreate + 1);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testUploadFile.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUploadFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUploadFile.getThumb()).isEqualTo(DEFAULT_THUMB);
        assertThat(testUploadFile.getExt()).isEqualTo(DEFAULT_EXT);
        assertThat(testUploadFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUploadFile.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testUploadFile.getFolder()).isEqualTo(DEFAULT_FOLDER);
        assertThat(testUploadFile.getOwnerEntityName()).isEqualTo(DEFAULT_OWNER_ENTITY_NAME);
        assertThat(testUploadFile.getOwnerEntityId()).isEqualTo(DEFAULT_OWNER_ENTITY_ID);
        assertThat(testUploadFile.getBusinessTitle()).isEqualTo(DEFAULT_BUSINESS_TITLE);
        assertThat(testUploadFile.getBusinessDesc()).isEqualTo(DEFAULT_BUSINESS_DESC);
        assertThat(testUploadFile.getBusinessStatus()).isEqualTo(DEFAULT_BUSINESS_STATUS);
        assertThat(testUploadFile.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUploadFile.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testUploadFile.getReferenceCount()).isEqualTo(DEFAULT_REFERENCE_COUNT);
        assertThat(testUploadFile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUploadFile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUploadFile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUploadFile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createUploadFileWithExistingId() throws Exception {
        // Create the UploadFile with an existing ID
        uploadFile.setId(1L);
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        int databaseSizeBeforeCreate = uploadFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadFileRepository.findAll().size();
        // set the field null
        uploadFile.setUrl(null);

        // Create the UploadFile, which fails.
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        restUploadFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isBadRequest());

        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeTest);
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

        defaultUploadFileShouldBeFound("id.equals=" + id);
        defaultUploadFileShouldNotBeFound("id.notEquals=" + id);

        defaultUploadFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUploadFileShouldNotBeFound("id.greaterThan=" + id);

        defaultUploadFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUploadFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url equals to DEFAULT_URL
        defaultUploadFileShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the uploadFileList where url equals to UPDATED_URL
        defaultUploadFileShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url in DEFAULT_URL or UPDATED_URL
        defaultUploadFileShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the uploadFileList where url equals to UPDATED_URL
        defaultUploadFileShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url is not null
        defaultUploadFileShouldBeFound("url.specified=true");

        // Get all the uploadFileList where url is null
        defaultUploadFileShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url contains DEFAULT_URL
        defaultUploadFileShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the uploadFileList where url contains UPDATED_URL
        defaultUploadFileShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where url does not contain DEFAULT_URL
        defaultUploadFileShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the uploadFileList where url does not contain UPDATED_URL
        defaultUploadFileShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName equals to DEFAULT_FULL_NAME
        defaultUploadFileShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the uploadFileList where fullName equals to UPDATED_FULL_NAME
        defaultUploadFileShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultUploadFileShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the uploadFileList where fullName equals to UPDATED_FULL_NAME
        defaultUploadFileShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName is not null
        defaultUploadFileShouldBeFound("fullName.specified=true");

        // Get all the uploadFileList where fullName is null
        defaultUploadFileShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName contains DEFAULT_FULL_NAME
        defaultUploadFileShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the uploadFileList where fullName contains UPDATED_FULL_NAME
        defaultUploadFileShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fullName does not contain DEFAULT_FULL_NAME
        defaultUploadFileShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the uploadFileList where fullName does not contain UPDATED_FULL_NAME
        defaultUploadFileShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name equals to DEFAULT_NAME
        defaultUploadFileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the uploadFileList where name equals to UPDATED_NAME
        defaultUploadFileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUploadFileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the uploadFileList where name equals to UPDATED_NAME
        defaultUploadFileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name is not null
        defaultUploadFileShouldBeFound("name.specified=true");

        // Get all the uploadFileList where name is null
        defaultUploadFileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name contains DEFAULT_NAME
        defaultUploadFileShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the uploadFileList where name contains UPDATED_NAME
        defaultUploadFileShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where name does not contain DEFAULT_NAME
        defaultUploadFileShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the uploadFileList where name does not contain UPDATED_NAME
        defaultUploadFileShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb equals to DEFAULT_THUMB
        defaultUploadFileShouldBeFound("thumb.equals=" + DEFAULT_THUMB);

        // Get all the uploadFileList where thumb equals to UPDATED_THUMB
        defaultUploadFileShouldNotBeFound("thumb.equals=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb in DEFAULT_THUMB or UPDATED_THUMB
        defaultUploadFileShouldBeFound("thumb.in=" + DEFAULT_THUMB + "," + UPDATED_THUMB);

        // Get all the uploadFileList where thumb equals to UPDATED_THUMB
        defaultUploadFileShouldNotBeFound("thumb.in=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb is not null
        defaultUploadFileShouldBeFound("thumb.specified=true");

        // Get all the uploadFileList where thumb is null
        defaultUploadFileShouldNotBeFound("thumb.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb contains DEFAULT_THUMB
        defaultUploadFileShouldBeFound("thumb.contains=" + DEFAULT_THUMB);

        // Get all the uploadFileList where thumb contains UPDATED_THUMB
        defaultUploadFileShouldNotBeFound("thumb.contains=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByThumbNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where thumb does not contain DEFAULT_THUMB
        defaultUploadFileShouldNotBeFound("thumb.doesNotContain=" + DEFAULT_THUMB);

        // Get all the uploadFileList where thumb does not contain UPDATED_THUMB
        defaultUploadFileShouldBeFound("thumb.doesNotContain=" + UPDATED_THUMB);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext equals to DEFAULT_EXT
        defaultUploadFileShouldBeFound("ext.equals=" + DEFAULT_EXT);

        // Get all the uploadFileList where ext equals to UPDATED_EXT
        defaultUploadFileShouldNotBeFound("ext.equals=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext in DEFAULT_EXT or UPDATED_EXT
        defaultUploadFileShouldBeFound("ext.in=" + DEFAULT_EXT + "," + UPDATED_EXT);

        // Get all the uploadFileList where ext equals to UPDATED_EXT
        defaultUploadFileShouldNotBeFound("ext.in=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext is not null
        defaultUploadFileShouldBeFound("ext.specified=true");

        // Get all the uploadFileList where ext is null
        defaultUploadFileShouldNotBeFound("ext.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext contains DEFAULT_EXT
        defaultUploadFileShouldBeFound("ext.contains=" + DEFAULT_EXT);

        // Get all the uploadFileList where ext contains UPDATED_EXT
        defaultUploadFileShouldNotBeFound("ext.contains=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByExtNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ext does not contain DEFAULT_EXT
        defaultUploadFileShouldNotBeFound("ext.doesNotContain=" + DEFAULT_EXT);

        // Get all the uploadFileList where ext does not contain UPDATED_EXT
        defaultUploadFileShouldBeFound("ext.doesNotContain=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type equals to DEFAULT_TYPE
        defaultUploadFileShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the uploadFileList where type equals to UPDATED_TYPE
        defaultUploadFileShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultUploadFileShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the uploadFileList where type equals to UPDATED_TYPE
        defaultUploadFileShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type is not null
        defaultUploadFileShouldBeFound("type.specified=true");

        // Get all the uploadFileList where type is null
        defaultUploadFileShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type contains DEFAULT_TYPE
        defaultUploadFileShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the uploadFileList where type contains UPDATED_TYPE
        defaultUploadFileShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where type does not contain DEFAULT_TYPE
        defaultUploadFileShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the uploadFileList where type does not contain UPDATED_TYPE
        defaultUploadFileShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path equals to DEFAULT_PATH
        defaultUploadFileShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the uploadFileList where path equals to UPDATED_PATH
        defaultUploadFileShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path in DEFAULT_PATH or UPDATED_PATH
        defaultUploadFileShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the uploadFileList where path equals to UPDATED_PATH
        defaultUploadFileShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path is not null
        defaultUploadFileShouldBeFound("path.specified=true");

        // Get all the uploadFileList where path is null
        defaultUploadFileShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path contains DEFAULT_PATH
        defaultUploadFileShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the uploadFileList where path contains UPDATED_PATH
        defaultUploadFileShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where path does not contain DEFAULT_PATH
        defaultUploadFileShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the uploadFileList where path does not contain UPDATED_PATH
        defaultUploadFileShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder equals to DEFAULT_FOLDER
        defaultUploadFileShouldBeFound("folder.equals=" + DEFAULT_FOLDER);

        // Get all the uploadFileList where folder equals to UPDATED_FOLDER
        defaultUploadFileShouldNotBeFound("folder.equals=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder in DEFAULT_FOLDER or UPDATED_FOLDER
        defaultUploadFileShouldBeFound("folder.in=" + DEFAULT_FOLDER + "," + UPDATED_FOLDER);

        // Get all the uploadFileList where folder equals to UPDATED_FOLDER
        defaultUploadFileShouldNotBeFound("folder.in=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder is not null
        defaultUploadFileShouldBeFound("folder.specified=true");

        // Get all the uploadFileList where folder is null
        defaultUploadFileShouldNotBeFound("folder.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder contains DEFAULT_FOLDER
        defaultUploadFileShouldBeFound("folder.contains=" + DEFAULT_FOLDER);

        // Get all the uploadFileList where folder contains UPDATED_FOLDER
        defaultUploadFileShouldNotBeFound("folder.contains=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFolderNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where folder does not contain DEFAULT_FOLDER
        defaultUploadFileShouldNotBeFound("folder.doesNotContain=" + DEFAULT_FOLDER);

        // Get all the uploadFileList where folder does not contain UPDATED_FOLDER
        defaultUploadFileShouldBeFound("folder.doesNotContain=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName equals to DEFAULT_OWNER_ENTITY_NAME
        defaultUploadFileShouldBeFound("ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadFileList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultUploadFileShouldNotBeFound("ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName in DEFAULT_OWNER_ENTITY_NAME or UPDATED_OWNER_ENTITY_NAME
        defaultUploadFileShouldBeFound("ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME);

        // Get all the uploadFileList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultUploadFileShouldNotBeFound("ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName is not null
        defaultUploadFileShouldBeFound("ownerEntityName.specified=true");

        // Get all the uploadFileList where ownerEntityName is null
        defaultUploadFileShouldNotBeFound("ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName contains DEFAULT_OWNER_ENTITY_NAME
        defaultUploadFileShouldBeFound("ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadFileList where ownerEntityName contains UPDATED_OWNER_ENTITY_NAME
        defaultUploadFileShouldNotBeFound("ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityName does not contain DEFAULT_OWNER_ENTITY_NAME
        defaultUploadFileShouldNotBeFound("ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadFileList where ownerEntityName does not contain UPDATED_OWNER_ENTITY_NAME
        defaultUploadFileShouldBeFound("ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId equals to DEFAULT_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId in DEFAULT_OWNER_ENTITY_ID or UPDATED_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is not null
        defaultUploadFileShouldBeFound("ownerEntityId.specified=true");

        // Get all the uploadFileList where ownerEntityId is null
        defaultUploadFileShouldNotBeFound("ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is greater than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId is greater than or equal to UPDATED_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is less than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId is less than or equal to SMALLER_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is less than DEFAULT_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId is less than UPDATED_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where ownerEntityId is greater than DEFAULT_OWNER_ENTITY_ID
        defaultUploadFileShouldNotBeFound("ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadFileList where ownerEntityId is greater than SMALLER_OWNER_ENTITY_ID
        defaultUploadFileShouldBeFound("ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle equals to DEFAULT_BUSINESS_TITLE
        defaultUploadFileShouldBeFound("businessTitle.equals=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadFileList where businessTitle equals to UPDATED_BUSINESS_TITLE
        defaultUploadFileShouldNotBeFound("businessTitle.equals=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle in DEFAULT_BUSINESS_TITLE or UPDATED_BUSINESS_TITLE
        defaultUploadFileShouldBeFound("businessTitle.in=" + DEFAULT_BUSINESS_TITLE + "," + UPDATED_BUSINESS_TITLE);

        // Get all the uploadFileList where businessTitle equals to UPDATED_BUSINESS_TITLE
        defaultUploadFileShouldNotBeFound("businessTitle.in=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle is not null
        defaultUploadFileShouldBeFound("businessTitle.specified=true");

        // Get all the uploadFileList where businessTitle is null
        defaultUploadFileShouldNotBeFound("businessTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle contains DEFAULT_BUSINESS_TITLE
        defaultUploadFileShouldBeFound("businessTitle.contains=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadFileList where businessTitle contains UPDATED_BUSINESS_TITLE
        defaultUploadFileShouldNotBeFound("businessTitle.contains=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessTitleNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessTitle does not contain DEFAULT_BUSINESS_TITLE
        defaultUploadFileShouldNotBeFound("businessTitle.doesNotContain=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadFileList where businessTitle does not contain UPDATED_BUSINESS_TITLE
        defaultUploadFileShouldBeFound("businessTitle.doesNotContain=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc equals to DEFAULT_BUSINESS_DESC
        defaultUploadFileShouldBeFound("businessDesc.equals=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadFileList where businessDesc equals to UPDATED_BUSINESS_DESC
        defaultUploadFileShouldNotBeFound("businessDesc.equals=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc in DEFAULT_BUSINESS_DESC or UPDATED_BUSINESS_DESC
        defaultUploadFileShouldBeFound("businessDesc.in=" + DEFAULT_BUSINESS_DESC + "," + UPDATED_BUSINESS_DESC);

        // Get all the uploadFileList where businessDesc equals to UPDATED_BUSINESS_DESC
        defaultUploadFileShouldNotBeFound("businessDesc.in=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc is not null
        defaultUploadFileShouldBeFound("businessDesc.specified=true");

        // Get all the uploadFileList where businessDesc is null
        defaultUploadFileShouldNotBeFound("businessDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc contains DEFAULT_BUSINESS_DESC
        defaultUploadFileShouldBeFound("businessDesc.contains=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadFileList where businessDesc contains UPDATED_BUSINESS_DESC
        defaultUploadFileShouldNotBeFound("businessDesc.contains=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessDescNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessDesc does not contain DEFAULT_BUSINESS_DESC
        defaultUploadFileShouldNotBeFound("businessDesc.doesNotContain=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadFileList where businessDesc does not contain UPDATED_BUSINESS_DESC
        defaultUploadFileShouldBeFound("businessDesc.doesNotContain=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus equals to DEFAULT_BUSINESS_STATUS
        defaultUploadFileShouldBeFound("businessStatus.equals=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadFileList where businessStatus equals to UPDATED_BUSINESS_STATUS
        defaultUploadFileShouldNotBeFound("businessStatus.equals=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus in DEFAULT_BUSINESS_STATUS or UPDATED_BUSINESS_STATUS
        defaultUploadFileShouldBeFound("businessStatus.in=" + DEFAULT_BUSINESS_STATUS + "," + UPDATED_BUSINESS_STATUS);

        // Get all the uploadFileList where businessStatus equals to UPDATED_BUSINESS_STATUS
        defaultUploadFileShouldNotBeFound("businessStatus.in=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus is not null
        defaultUploadFileShouldBeFound("businessStatus.specified=true");

        // Get all the uploadFileList where businessStatus is null
        defaultUploadFileShouldNotBeFound("businessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus contains DEFAULT_BUSINESS_STATUS
        defaultUploadFileShouldBeFound("businessStatus.contains=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadFileList where businessStatus contains UPDATED_BUSINESS_STATUS
        defaultUploadFileShouldNotBeFound("businessStatus.contains=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadFilesByBusinessStatusNotContainsSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where businessStatus does not contain DEFAULT_BUSINESS_STATUS
        defaultUploadFileShouldNotBeFound("businessStatus.doesNotContain=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadFileList where businessStatus does not contain UPDATED_BUSINESS_STATUS
        defaultUploadFileShouldBeFound("businessStatus.doesNotContain=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt equals to DEFAULT_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the uploadFileList where createAt equals to UPDATED_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the uploadFileList where createAt equals to UPDATED_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is not null
        defaultUploadFileShouldBeFound("createAt.specified=true");

        // Get all the uploadFileList where createAt is null
        defaultUploadFileShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uploadFileList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uploadFileList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is less than DEFAULT_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the uploadFileList where createAt is less than UPDATED_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createAt is greater than DEFAULT_CREATE_AT
        defaultUploadFileShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the uploadFileList where createAt is greater than SMALLER_CREATE_AT
        defaultUploadFileShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize equals to DEFAULT_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the uploadFileList where fileSize equals to UPDATED_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the uploadFileList where fileSize equals to UPDATED_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is not null
        defaultUploadFileShouldBeFound("fileSize.specified=true");

        // Get all the uploadFileList where fileSize is null
        defaultUploadFileShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the uploadFileList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the uploadFileList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is less than DEFAULT_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the uploadFileList where fileSize is less than UPDATED_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultUploadFileShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the uploadFileList where fileSize is greater than SMALLER_FILE_SIZE
        defaultUploadFileShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount equals to DEFAULT_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.equals=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount equals to UPDATED_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.equals=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount in DEFAULT_REFERENCE_COUNT or UPDATED_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.in=" + DEFAULT_REFERENCE_COUNT + "," + UPDATED_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount equals to UPDATED_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.in=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is not null
        defaultUploadFileShouldBeFound("referenceCount.specified=true");

        // Get all the uploadFileList where referenceCount is null
        defaultUploadFileShouldNotBeFound("referenceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is greater than or equal to DEFAULT_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.greaterThanOrEqual=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount is greater than or equal to UPDATED_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.greaterThanOrEqual=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is less than or equal to DEFAULT_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.lessThanOrEqual=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount is less than or equal to SMALLER_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.lessThanOrEqual=" + SMALLER_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is less than DEFAULT_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.lessThan=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount is less than UPDATED_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.lessThan=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByReferenceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where referenceCount is greater than DEFAULT_REFERENCE_COUNT
        defaultUploadFileShouldNotBeFound("referenceCount.greaterThan=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadFileList where referenceCount is greater than SMALLER_REFERENCE_COUNT
        defaultUploadFileShouldBeFound("referenceCount.greaterThan=" + SMALLER_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy equals to DEFAULT_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the uploadFileList where createdBy equals to UPDATED_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the uploadFileList where createdBy equals to UPDATED_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is not null
        defaultUploadFileShouldBeFound("createdBy.specified=true");

        // Get all the uploadFileList where createdBy is null
        defaultUploadFileShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the uploadFileList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the uploadFileList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is less than DEFAULT_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the uploadFileList where createdBy is less than UPDATED_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdBy is greater than DEFAULT_CREATED_BY
        defaultUploadFileShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the uploadFileList where createdBy is greater than SMALLER_CREATED_BY
        defaultUploadFileShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate equals to DEFAULT_CREATED_DATE
        defaultUploadFileShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the uploadFileList where createdDate equals to UPDATED_CREATED_DATE
        defaultUploadFileShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultUploadFileShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the uploadFileList where createdDate equals to UPDATED_CREATED_DATE
        defaultUploadFileShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where createdDate is not null
        defaultUploadFileShouldBeFound("createdDate.specified=true");

        // Get all the uploadFileList where createdDate is null
        defaultUploadFileShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is not null
        defaultUploadFileShouldBeFound("lastModifiedBy.specified=true");

        // Get all the uploadFileList where lastModifiedBy is null
        defaultUploadFileShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is greater than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy is greater than or equal to UPDATED_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is less than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy is less than or equal to SMALLER_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is less than DEFAULT_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy is less than UPDATED_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedBy is greater than DEFAULT_LAST_MODIFIED_BY
        defaultUploadFileShouldNotBeFound("lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadFileList where lastModifiedBy is greater than SMALLER_LAST_MODIFIED_BY
        defaultUploadFileShouldBeFound("lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultUploadFileShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the uploadFileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUploadFileShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultUploadFileShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the uploadFileList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUploadFileShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadFilesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        // Get all the uploadFileList where lastModifiedDate is not null
        defaultUploadFileShouldBeFound("lastModifiedDate.specified=true");

        // Get all the uploadFileList where lastModifiedDate is null
        defaultUploadFileShouldNotBeFound("lastModifiedDate.specified=false");
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

        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadFile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUploadFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUploadFile.getThumb()).isEqualTo(UPDATED_THUMB);
        assertThat(testUploadFile.getExt()).isEqualTo(UPDATED_EXT);
        assertThat(testUploadFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUploadFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testUploadFile.getFolder()).isEqualTo(UPDATED_FOLDER);
        assertThat(testUploadFile.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadFile.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadFile.getBusinessTitle()).isEqualTo(UPDATED_BUSINESS_TITLE);
        assertThat(testUploadFile.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadFile.getBusinessStatus()).isEqualTo(UPDATED_BUSINESS_STATUS);
        assertThat(testUploadFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testUploadFile.getReferenceCount()).isEqualTo(UPDATED_REFERENCE_COUNT);
        assertThat(testUploadFile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUploadFile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUploadFile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadFile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUploadFileWithPatch() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();

        // Update the uploadFile using partial update
        UploadFile partialUpdatedUploadFile = new UploadFile();
        partialUpdatedUploadFile.setId(uploadFile.getId());

        partialUpdatedUploadFile
            .url(UPDATED_URL)
            .path(UPDATED_PATH)
            .folder(UPDATED_FOLDER)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .createAt(UPDATED_CREATE_AT)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUploadFile))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadFile.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUploadFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUploadFile.getThumb()).isEqualTo(DEFAULT_THUMB);
        assertThat(testUploadFile.getExt()).isEqualTo(DEFAULT_EXT);
        assertThat(testUploadFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUploadFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testUploadFile.getFolder()).isEqualTo(UPDATED_FOLDER);
        assertThat(testUploadFile.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadFile.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadFile.getBusinessTitle()).isEqualTo(DEFAULT_BUSINESS_TITLE);
        assertThat(testUploadFile.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadFile.getBusinessStatus()).isEqualTo(DEFAULT_BUSINESS_STATUS);
        assertThat(testUploadFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadFile.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testUploadFile.getReferenceCount()).isEqualTo(UPDATED_REFERENCE_COUNT);
        assertThat(testUploadFile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUploadFile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUploadFile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadFile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUploadFileWithPatch() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUploadFile))
            )
            .andExpect(status().isOk());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
        UploadFile testUploadFile = uploadFileList.get(uploadFileList.size() - 1);
        assertThat(testUploadFile.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadFile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUploadFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUploadFile.getThumb()).isEqualTo(UPDATED_THUMB);
        assertThat(testUploadFile.getExt()).isEqualTo(UPDATED_EXT);
        assertThat(testUploadFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUploadFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testUploadFile.getFolder()).isEqualTo(UPDATED_FOLDER);
        assertThat(testUploadFile.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadFile.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadFile.getBusinessTitle()).isEqualTo(UPDATED_BUSINESS_TITLE);
        assertThat(testUploadFile.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadFile.getBusinessStatus()).isEqualTo(UPDATED_BUSINESS_STATUS);
        assertThat(testUploadFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testUploadFile.getReferenceCount()).isEqualTo(UPDATED_REFERENCE_COUNT);
        assertThat(testUploadFile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUploadFile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUploadFile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadFile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uploadFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUploadFile() throws Exception {
        int databaseSizeBeforeUpdate = uploadFileRepository.findAll().size();
        uploadFile.setId(longCount.incrementAndGet());

        // Create the UploadFile
        UploadFileDTO uploadFileDTO = uploadFileMapper.toDto(uploadFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadFileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uploadFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadFile in the database
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUploadFile() throws Exception {
        // Initialize the database
        uploadFileRepository.save(uploadFile);

        int databaseSizeBeforeDelete = uploadFileRepository.findAll().size();

        // Delete the uploadFile
        restUploadFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, uploadFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UploadFile> uploadFileList = uploadFileRepository.findAll();
        assertThat(uploadFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
