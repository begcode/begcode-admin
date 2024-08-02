package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.UploadImageAsserts.*;
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
import com.begcode.monolith.domain.UploadImage;
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.UploadImageService;
import com.begcode.monolith.service.dto.UploadImageDTO;
import com.begcode.monolith.service.mapper.UploadImageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
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
 * Integration tests for the {@link UploadImageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class UploadImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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

    private static final String DEFAULT_SMART_URL = "AAAAAAAAAA";
    private static final String UPDATED_SMART_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIUM_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIUM_URL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/upload-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UploadImageRepository uploadImageRepository;

    @Mock
    private UploadImageRepository uploadImageRepositoryMock;

    @Autowired
    private UploadImageMapper uploadImageMapper;

    @Mock
    private UploadImageService uploadImageServiceMock;

    @Autowired
    private MockMvc restUploadImageMockMvc;

    private UploadImage uploadImage;

    private UploadImage insertedUploadImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadImage createEntity() {
        UploadImage uploadImage = new UploadImage()
            .url(DEFAULT_URL)
            .fullName(DEFAULT_FULL_NAME)
            .name(DEFAULT_NAME)
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
            .smartUrl(DEFAULT_SMART_URL)
            .mediumUrl(DEFAULT_MEDIUM_URL)
            .referenceCount(DEFAULT_REFERENCE_COUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return uploadImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UploadImage createUpdatedEntity() {
        UploadImage uploadImage = new UploadImage()
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
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
            .smartUrl(UPDATED_SMART_URL)
            .mediumUrl(UPDATED_MEDIUM_URL)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return uploadImage;
    }

    @BeforeEach
    public void initTest() {
        uploadImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUploadImage != null) {
            uploadImageRepository.deleteById(insertedUploadImage.getId());
            insertedUploadImage = null;
        }
    }

    @Test
    @Transactional
    void createUploadImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);
        var returnedUploadImageDTO = om.readValue(
            restUploadImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UploadImageDTO.class
        );

        // Validate the UploadImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUploadImage = uploadImageMapper.toEntity(returnedUploadImageDTO);
        assertUploadImageUpdatableFieldsEquals(returnedUploadImage, getPersistedUploadImage(returnedUploadImage));

        insertedUploadImage = returnedUploadImage;
    }

    @Test
    @Transactional
    void createUploadImageWithExistingId() throws Exception {
        // Create the UploadImage with an existing ID
        uploadImage.setId(1L);
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        uploadImage.setUrl(null);

        // Create the UploadImage, which fails.
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        restUploadImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUploadImages() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
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
            .andExpect(jsonPath("$.[*].smartUrl").value(hasItem(DEFAULT_SMART_URL)))
            .andExpect(jsonPath("$.[*].mediumUrl").value(hasItem(DEFAULT_MEDIUM_URL)))
            .andExpect(jsonPath("$.[*].referenceCount").value(hasItem(DEFAULT_REFERENCE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUploadImagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(uploadImageServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restUploadImageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(uploadImageServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUploadImagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(uploadImageServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restUploadImageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(uploadImageRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getUploadImage() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get the uploadImage
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL_ID, uploadImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uploadImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
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
            .andExpect(jsonPath("$.smartUrl").value(DEFAULT_SMART_URL))
            .andExpect(jsonPath("$.mediumUrl").value(DEFAULT_MEDIUM_URL))
            .andExpect(jsonPath("$.referenceCount").value(DEFAULT_REFERENCE_COUNT.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUploadImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        Long id = uploadImage.getId();

        defaultUploadImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUploadImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUploadImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where url equals to
        defaultUploadImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where url in
        defaultUploadImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where url is not null
        defaultUploadImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where url contains
        defaultUploadImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where url does not contain
        defaultUploadImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fullName equals to
        defaultUploadImageFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fullName in
        defaultUploadImageFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fullName is not null
        defaultUploadImageFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fullName contains
        defaultUploadImageFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fullName does not contain
        defaultUploadImageFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where name equals to
        defaultUploadImageFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where name in
        defaultUploadImageFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where name is not null
        defaultUploadImageFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where name contains
        defaultUploadImageFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where name does not contain
        defaultUploadImageFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ext equals to
        defaultUploadImageFiltering("ext.equals=" + DEFAULT_EXT, "ext.equals=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ext in
        defaultUploadImageFiltering("ext.in=" + DEFAULT_EXT + "," + UPDATED_EXT, "ext.in=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ext is not null
        defaultUploadImageFiltering("ext.specified=true", "ext.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ext contains
        defaultUploadImageFiltering("ext.contains=" + DEFAULT_EXT, "ext.contains=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ext does not contain
        defaultUploadImageFiltering("ext.doesNotContain=" + UPDATED_EXT, "ext.doesNotContain=" + DEFAULT_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where type equals to
        defaultUploadImageFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where type in
        defaultUploadImageFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where type is not null
        defaultUploadImageFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where type contains
        defaultUploadImageFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where type does not contain
        defaultUploadImageFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where path equals to
        defaultUploadImageFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where path in
        defaultUploadImageFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where path is not null
        defaultUploadImageFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where path contains
        defaultUploadImageFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where path does not contain
        defaultUploadImageFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where folder equals to
        defaultUploadImageFiltering("folder.equals=" + DEFAULT_FOLDER, "folder.equals=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where folder in
        defaultUploadImageFiltering("folder.in=" + DEFAULT_FOLDER + "," + UPDATED_FOLDER, "folder.in=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where folder is not null
        defaultUploadImageFiltering("folder.specified=true", "folder.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where folder contains
        defaultUploadImageFiltering("folder.contains=" + DEFAULT_FOLDER, "folder.contains=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where folder does not contain
        defaultUploadImageFiltering("folder.doesNotContain=" + UPDATED_FOLDER, "folder.doesNotContain=" + DEFAULT_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityName equals to
        defaultUploadImageFiltering(
            "ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityName in
        defaultUploadImageFiltering(
            "ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityName is not null
        defaultUploadImageFiltering("ownerEntityName.specified=true", "ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityName contains
        defaultUploadImageFiltering(
            "ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME,
            "ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityName does not contain
        defaultUploadImageFiltering(
            "ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME,
            "ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId equals to
        defaultUploadImageFiltering("ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID, "ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId in
        defaultUploadImageFiltering(
            "ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId is not null
        defaultUploadImageFiltering("ownerEntityId.specified=true", "ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId is greater than or equal to
        defaultUploadImageFiltering(
            "ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId is less than or equal to
        defaultUploadImageFiltering(
            "ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID,
            "ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId is less than
        defaultUploadImageFiltering(
            "ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID,
            "ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where ownerEntityId is greater than
        defaultUploadImageFiltering(
            "ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID,
            "ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessTitle equals to
        defaultUploadImageFiltering("businessTitle.equals=" + DEFAULT_BUSINESS_TITLE, "businessTitle.equals=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessTitle in
        defaultUploadImageFiltering(
            "businessTitle.in=" + DEFAULT_BUSINESS_TITLE + "," + UPDATED_BUSINESS_TITLE,
            "businessTitle.in=" + UPDATED_BUSINESS_TITLE
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessTitle is not null
        defaultUploadImageFiltering("businessTitle.specified=true", "businessTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessTitle contains
        defaultUploadImageFiltering("businessTitle.contains=" + DEFAULT_BUSINESS_TITLE, "businessTitle.contains=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessTitle does not contain
        defaultUploadImageFiltering(
            "businessTitle.doesNotContain=" + UPDATED_BUSINESS_TITLE,
            "businessTitle.doesNotContain=" + DEFAULT_BUSINESS_TITLE
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessDesc equals to
        defaultUploadImageFiltering("businessDesc.equals=" + DEFAULT_BUSINESS_DESC, "businessDesc.equals=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessDesc in
        defaultUploadImageFiltering(
            "businessDesc.in=" + DEFAULT_BUSINESS_DESC + "," + UPDATED_BUSINESS_DESC,
            "businessDesc.in=" + UPDATED_BUSINESS_DESC
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessDesc is not null
        defaultUploadImageFiltering("businessDesc.specified=true", "businessDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessDesc contains
        defaultUploadImageFiltering("businessDesc.contains=" + DEFAULT_BUSINESS_DESC, "businessDesc.contains=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessDesc does not contain
        defaultUploadImageFiltering(
            "businessDesc.doesNotContain=" + UPDATED_BUSINESS_DESC,
            "businessDesc.doesNotContain=" + DEFAULT_BUSINESS_DESC
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessStatus equals to
        defaultUploadImageFiltering("businessStatus.equals=" + DEFAULT_BUSINESS_STATUS, "businessStatus.equals=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessStatus in
        defaultUploadImageFiltering(
            "businessStatus.in=" + DEFAULT_BUSINESS_STATUS + "," + UPDATED_BUSINESS_STATUS,
            "businessStatus.in=" + UPDATED_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessStatus is not null
        defaultUploadImageFiltering("businessStatus.specified=true", "businessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessStatus contains
        defaultUploadImageFiltering(
            "businessStatus.contains=" + DEFAULT_BUSINESS_STATUS,
            "businessStatus.contains=" + UPDATED_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where businessStatus does not contain
        defaultUploadImageFiltering(
            "businessStatus.doesNotContain=" + UPDATED_BUSINESS_STATUS,
            "businessStatus.doesNotContain=" + DEFAULT_BUSINESS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt equals to
        defaultUploadImageFiltering("createAt.equals=" + DEFAULT_CREATE_AT, "createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt in
        defaultUploadImageFiltering("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT, "createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt is not null
        defaultUploadImageFiltering("createAt.specified=true", "createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt is greater than or equal to
        defaultUploadImageFiltering("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt is less than or equal to
        defaultUploadImageFiltering("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT, "createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt is less than
        defaultUploadImageFiltering("createAt.lessThan=" + UPDATED_CREATE_AT, "createAt.lessThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createAt is greater than
        defaultUploadImageFiltering("createAt.greaterThan=" + SMALLER_CREATE_AT, "createAt.greaterThan=" + DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize equals to
        defaultUploadImageFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize in
        defaultUploadImageFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize is not null
        defaultUploadImageFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize is greater than or equal to
        defaultUploadImageFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize is less than or equal to
        defaultUploadImageFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize is less than
        defaultUploadImageFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where fileSize is greater than
        defaultUploadImageFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where smartUrl equals to
        defaultUploadImageFiltering("smartUrl.equals=" + DEFAULT_SMART_URL, "smartUrl.equals=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where smartUrl in
        defaultUploadImageFiltering("smartUrl.in=" + DEFAULT_SMART_URL + "," + UPDATED_SMART_URL, "smartUrl.in=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where smartUrl is not null
        defaultUploadImageFiltering("smartUrl.specified=true", "smartUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where smartUrl contains
        defaultUploadImageFiltering("smartUrl.contains=" + DEFAULT_SMART_URL, "smartUrl.contains=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where smartUrl does not contain
        defaultUploadImageFiltering("smartUrl.doesNotContain=" + UPDATED_SMART_URL, "smartUrl.doesNotContain=" + DEFAULT_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where mediumUrl equals to
        defaultUploadImageFiltering("mediumUrl.equals=" + DEFAULT_MEDIUM_URL, "mediumUrl.equals=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where mediumUrl in
        defaultUploadImageFiltering("mediumUrl.in=" + DEFAULT_MEDIUM_URL + "," + UPDATED_MEDIUM_URL, "mediumUrl.in=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where mediumUrl is not null
        defaultUploadImageFiltering("mediumUrl.specified=true", "mediumUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where mediumUrl contains
        defaultUploadImageFiltering("mediumUrl.contains=" + DEFAULT_MEDIUM_URL, "mediumUrl.contains=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where mediumUrl does not contain
        defaultUploadImageFiltering("mediumUrl.doesNotContain=" + UPDATED_MEDIUM_URL, "mediumUrl.doesNotContain=" + DEFAULT_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount equals to
        defaultUploadImageFiltering("referenceCount.equals=" + DEFAULT_REFERENCE_COUNT, "referenceCount.equals=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount in
        defaultUploadImageFiltering(
            "referenceCount.in=" + DEFAULT_REFERENCE_COUNT + "," + UPDATED_REFERENCE_COUNT,
            "referenceCount.in=" + UPDATED_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount is not null
        defaultUploadImageFiltering("referenceCount.specified=true", "referenceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount is greater than or equal to
        defaultUploadImageFiltering(
            "referenceCount.greaterThanOrEqual=" + DEFAULT_REFERENCE_COUNT,
            "referenceCount.greaterThanOrEqual=" + UPDATED_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount is less than or equal to
        defaultUploadImageFiltering(
            "referenceCount.lessThanOrEqual=" + DEFAULT_REFERENCE_COUNT,
            "referenceCount.lessThanOrEqual=" + SMALLER_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount is less than
        defaultUploadImageFiltering(
            "referenceCount.lessThan=" + UPDATED_REFERENCE_COUNT,
            "referenceCount.lessThan=" + DEFAULT_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where referenceCount is greater than
        defaultUploadImageFiltering(
            "referenceCount.greaterThan=" + SMALLER_REFERENCE_COUNT,
            "referenceCount.greaterThan=" + DEFAULT_REFERENCE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy equals to
        defaultUploadImageFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy in
        defaultUploadImageFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy is not null
        defaultUploadImageFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy is greater than or equal to
        defaultUploadImageFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy is less than or equal to
        defaultUploadImageFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy is less than
        defaultUploadImageFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdBy is greater than
        defaultUploadImageFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdDate equals to
        defaultUploadImageFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdDate in
        defaultUploadImageFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where createdDate is not null
        defaultUploadImageFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy equals to
        defaultUploadImageFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy in
        defaultUploadImageFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is not null
        defaultUploadImageFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is greater than or equal to
        defaultUploadImageFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is less than or equal to
        defaultUploadImageFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is less than
        defaultUploadImageFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is greater than
        defaultUploadImageFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedDate equals to
        defaultUploadImageFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedDate in
        defaultUploadImageFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        // Get all the uploadImageList where lastModifiedDate is not null
        defaultUploadImageFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByCategoryIsEqualToSomething() throws Exception {
        ResourceCategory category = ResourceCategoryResourceIT.createEntity();
        uploadImage.setCategory(category);
        uploadImageRepository.insert(uploadImage);
        Long categoryId = category.getId();
        // Get all the uploadImageList where category equals to categoryId
        defaultUploadImageShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the uploadImageList where category equals to (categoryId + 1)
        defaultUploadImageShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultUploadImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUploadImageShouldBeFound(shouldBeFound);
        defaultUploadImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUploadImageShouldBeFound(String filter) throws Exception {
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uploadImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
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
            .andExpect(jsonPath("$.[*].smartUrl").value(hasItem(DEFAULT_SMART_URL)))
            .andExpect(jsonPath("$.[*].mediumUrl").value(hasItem(DEFAULT_MEDIUM_URL)))
            .andExpect(jsonPath("$.[*].referenceCount").value(hasItem(DEFAULT_REFERENCE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUploadImageShouldNotBeFound(String filter) throws Exception {
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUploadImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUploadImage() throws Exception {
        // Get the uploadImage
        restUploadImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUploadImage() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadImage
        UploadImage updatedUploadImage = uploadImageRepository.findById(uploadImage.getId()).orElseThrow();
        updatedUploadImage
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
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
            .smartUrl(UPDATED_SMART_URL)
            .mediumUrl(UPDATED_MEDIUM_URL)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(updatedUploadImage);

        restUploadImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUploadImageToMatchAllProperties(updatedUploadImage);
    }

    @Test
    @Transactional
    void putNonExistingUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uploadImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUploadImageWithPatch() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadImage using partial update
        UploadImage partialUpdatedUploadImage = new UploadImage();
        partialUpdatedUploadImage.setId(uploadImage.getId());

        partialUpdatedUploadImage
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .path(UPDATED_PATH)
            .folder(UPDATED_FOLDER)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .fileSize(UPDATED_FILE_SIZE)
            .mediumUrl(UPDATED_MEDIUM_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUploadImage))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUploadImage, uploadImage),
            getPersistedUploadImage(uploadImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateUploadImageWithPatch() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uploadImage using partial update
        UploadImage partialUpdatedUploadImage = new UploadImage();
        partialUpdatedUploadImage.setId(uploadImage.getId());

        partialUpdatedUploadImage
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .name(UPDATED_NAME)
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
            .smartUrl(UPDATED_SMART_URL)
            .mediumUrl(UPDATED_MEDIUM_URL)
            .referenceCount(UPDATED_REFERENCE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUploadImage))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUploadImageUpdatableFieldsEquals(partialUpdatedUploadImage, getPersistedUploadImage(partialUpdatedUploadImage));
    }

    @Test
    @Transactional
    void patchNonExistingUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uploadImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUploadImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uploadImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUploadImage() throws Exception {
        // Initialize the database
        insertedUploadImage = uploadImageRepository.saveAndGet(uploadImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the uploadImage
        restUploadImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, uploadImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return uploadImageRepository.selectCount(null);
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

    protected UploadImage getPersistedUploadImage(UploadImage uploadImage) {
        return uploadImageRepository.findById(uploadImage.getId()).orElseThrow();
    }

    protected void assertPersistedUploadImageToMatchAllProperties(UploadImage expectedUploadImage) {
        assertUploadImageAllPropertiesEquals(expectedUploadImage, getPersistedUploadImage(expectedUploadImage));
    }

    protected void assertPersistedUploadImageToMatchUpdatableProperties(UploadImage expectedUploadImage) {
        assertUploadImageAllUpdatablePropertiesEquals(expectedUploadImage, getPersistedUploadImage(expectedUploadImage));
    }
}
