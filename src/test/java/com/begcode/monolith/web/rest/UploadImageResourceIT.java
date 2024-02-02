package com.begcode.monolith.web.rest;

import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.UploadImage;
import com.begcode.monolith.repository.UploadImageRepository;
import com.begcode.monolith.service.UploadImageService;
import com.begcode.monolith.service.dto.UploadImageDTO;
import com.begcode.monolith.service.mapper.UploadImageMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UploadImageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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

    @Test
    @Transactional
    void createUploadImage() throws Exception {
        int databaseSizeBeforeCreate = uploadImageRepository.findAll().size();
        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);
        restUploadImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeCreate + 1);
        UploadImage testUploadImage = uploadImageList.get(uploadImageList.size() - 1);
        assertThat(testUploadImage.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testUploadImage.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUploadImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUploadImage.getExt()).isEqualTo(DEFAULT_EXT);
        assertThat(testUploadImage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUploadImage.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testUploadImage.getFolder()).isEqualTo(DEFAULT_FOLDER);
        assertThat(testUploadImage.getOwnerEntityName()).isEqualTo(DEFAULT_OWNER_ENTITY_NAME);
        assertThat(testUploadImage.getOwnerEntityId()).isEqualTo(DEFAULT_OWNER_ENTITY_ID);
        assertThat(testUploadImage.getBusinessTitle()).isEqualTo(DEFAULT_BUSINESS_TITLE);
        assertThat(testUploadImage.getBusinessDesc()).isEqualTo(DEFAULT_BUSINESS_DESC);
        assertThat(testUploadImage.getBusinessStatus()).isEqualTo(DEFAULT_BUSINESS_STATUS);
        assertThat(testUploadImage.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUploadImage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testUploadImage.getSmartUrl()).isEqualTo(DEFAULT_SMART_URL);
        assertThat(testUploadImage.getMediumUrl()).isEqualTo(DEFAULT_MEDIUM_URL);
        assertThat(testUploadImage.getReferenceCount()).isEqualTo(DEFAULT_REFERENCE_COUNT);
        assertThat(testUploadImage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUploadImage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUploadImage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUploadImage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createUploadImageWithExistingId() throws Exception {
        // Create the UploadImage with an existing ID
        uploadImage.setId(1L);
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        int databaseSizeBeforeCreate = uploadImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUploadImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = uploadImageRepository.findAll().size();
        // set the field null
        uploadImage.setUrl(null);

        // Create the UploadImage, which fails.
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        restUploadImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUploadImages() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

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
        uploadImageRepository.save(uploadImage);

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
        uploadImageRepository.save(uploadImage);

        Long id = uploadImage.getId();

        defaultUploadImageShouldBeFound("id.equals=" + id);
        defaultUploadImageShouldNotBeFound("id.notEquals=" + id);

        defaultUploadImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUploadImageShouldNotBeFound("id.greaterThan=" + id);

        defaultUploadImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUploadImageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where url equals to DEFAULT_URL
        defaultUploadImageShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the uploadImageList where url equals to UPDATED_URL
        defaultUploadImageShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where url in DEFAULT_URL or UPDATED_URL
        defaultUploadImageShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the uploadImageList where url equals to UPDATED_URL
        defaultUploadImageShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where url is not null
        defaultUploadImageShouldBeFound("url.specified=true");

        // Get all the uploadImageList where url is null
        defaultUploadImageShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where url contains DEFAULT_URL
        defaultUploadImageShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the uploadImageList where url contains UPDATED_URL
        defaultUploadImageShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where url does not contain DEFAULT_URL
        defaultUploadImageShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the uploadImageList where url does not contain UPDATED_URL
        defaultUploadImageShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fullName equals to DEFAULT_FULL_NAME
        defaultUploadImageShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the uploadImageList where fullName equals to UPDATED_FULL_NAME
        defaultUploadImageShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultUploadImageShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the uploadImageList where fullName equals to UPDATED_FULL_NAME
        defaultUploadImageShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fullName is not null
        defaultUploadImageShouldBeFound("fullName.specified=true");

        // Get all the uploadImageList where fullName is null
        defaultUploadImageShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fullName contains DEFAULT_FULL_NAME
        defaultUploadImageShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the uploadImageList where fullName contains UPDATED_FULL_NAME
        defaultUploadImageShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fullName does not contain DEFAULT_FULL_NAME
        defaultUploadImageShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the uploadImageList where fullName does not contain UPDATED_FULL_NAME
        defaultUploadImageShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where name equals to DEFAULT_NAME
        defaultUploadImageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the uploadImageList where name equals to UPDATED_NAME
        defaultUploadImageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUploadImageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the uploadImageList where name equals to UPDATED_NAME
        defaultUploadImageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where name is not null
        defaultUploadImageShouldBeFound("name.specified=true");

        // Get all the uploadImageList where name is null
        defaultUploadImageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where name contains DEFAULT_NAME
        defaultUploadImageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the uploadImageList where name contains UPDATED_NAME
        defaultUploadImageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where name does not contain DEFAULT_NAME
        defaultUploadImageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the uploadImageList where name does not contain UPDATED_NAME
        defaultUploadImageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ext equals to DEFAULT_EXT
        defaultUploadImageShouldBeFound("ext.equals=" + DEFAULT_EXT);

        // Get all the uploadImageList where ext equals to UPDATED_EXT
        defaultUploadImageShouldNotBeFound("ext.equals=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ext in DEFAULT_EXT or UPDATED_EXT
        defaultUploadImageShouldBeFound("ext.in=" + DEFAULT_EXT + "," + UPDATED_EXT);

        // Get all the uploadImageList where ext equals to UPDATED_EXT
        defaultUploadImageShouldNotBeFound("ext.in=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ext is not null
        defaultUploadImageShouldBeFound("ext.specified=true");

        // Get all the uploadImageList where ext is null
        defaultUploadImageShouldNotBeFound("ext.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ext contains DEFAULT_EXT
        defaultUploadImageShouldBeFound("ext.contains=" + DEFAULT_EXT);

        // Get all the uploadImageList where ext contains UPDATED_EXT
        defaultUploadImageShouldNotBeFound("ext.contains=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByExtNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ext does not contain DEFAULT_EXT
        defaultUploadImageShouldNotBeFound("ext.doesNotContain=" + DEFAULT_EXT);

        // Get all the uploadImageList where ext does not contain UPDATED_EXT
        defaultUploadImageShouldBeFound("ext.doesNotContain=" + UPDATED_EXT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where type equals to DEFAULT_TYPE
        defaultUploadImageShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the uploadImageList where type equals to UPDATED_TYPE
        defaultUploadImageShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultUploadImageShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the uploadImageList where type equals to UPDATED_TYPE
        defaultUploadImageShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where type is not null
        defaultUploadImageShouldBeFound("type.specified=true");

        // Get all the uploadImageList where type is null
        defaultUploadImageShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where type contains DEFAULT_TYPE
        defaultUploadImageShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the uploadImageList where type contains UPDATED_TYPE
        defaultUploadImageShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where type does not contain DEFAULT_TYPE
        defaultUploadImageShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the uploadImageList where type does not contain UPDATED_TYPE
        defaultUploadImageShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where path equals to DEFAULT_PATH
        defaultUploadImageShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the uploadImageList where path equals to UPDATED_PATH
        defaultUploadImageShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where path in DEFAULT_PATH or UPDATED_PATH
        defaultUploadImageShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the uploadImageList where path equals to UPDATED_PATH
        defaultUploadImageShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where path is not null
        defaultUploadImageShouldBeFound("path.specified=true");

        // Get all the uploadImageList where path is null
        defaultUploadImageShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where path contains DEFAULT_PATH
        defaultUploadImageShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the uploadImageList where path contains UPDATED_PATH
        defaultUploadImageShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByPathNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where path does not contain DEFAULT_PATH
        defaultUploadImageShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the uploadImageList where path does not contain UPDATED_PATH
        defaultUploadImageShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where folder equals to DEFAULT_FOLDER
        defaultUploadImageShouldBeFound("folder.equals=" + DEFAULT_FOLDER);

        // Get all the uploadImageList where folder equals to UPDATED_FOLDER
        defaultUploadImageShouldNotBeFound("folder.equals=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where folder in DEFAULT_FOLDER or UPDATED_FOLDER
        defaultUploadImageShouldBeFound("folder.in=" + DEFAULT_FOLDER + "," + UPDATED_FOLDER);

        // Get all the uploadImageList where folder equals to UPDATED_FOLDER
        defaultUploadImageShouldNotBeFound("folder.in=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where folder is not null
        defaultUploadImageShouldBeFound("folder.specified=true");

        // Get all the uploadImageList where folder is null
        defaultUploadImageShouldNotBeFound("folder.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where folder contains DEFAULT_FOLDER
        defaultUploadImageShouldBeFound("folder.contains=" + DEFAULT_FOLDER);

        // Get all the uploadImageList where folder contains UPDATED_FOLDER
        defaultUploadImageShouldNotBeFound("folder.contains=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFolderNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where folder does not contain DEFAULT_FOLDER
        defaultUploadImageShouldNotBeFound("folder.doesNotContain=" + DEFAULT_FOLDER);

        // Get all the uploadImageList where folder does not contain UPDATED_FOLDER
        defaultUploadImageShouldBeFound("folder.doesNotContain=" + UPDATED_FOLDER);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityName equals to DEFAULT_OWNER_ENTITY_NAME
        defaultUploadImageShouldBeFound("ownerEntityName.equals=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadImageList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultUploadImageShouldNotBeFound("ownerEntityName.equals=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityName in DEFAULT_OWNER_ENTITY_NAME or UPDATED_OWNER_ENTITY_NAME
        defaultUploadImageShouldBeFound("ownerEntityName.in=" + DEFAULT_OWNER_ENTITY_NAME + "," + UPDATED_OWNER_ENTITY_NAME);

        // Get all the uploadImageList where ownerEntityName equals to UPDATED_OWNER_ENTITY_NAME
        defaultUploadImageShouldNotBeFound("ownerEntityName.in=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityName is not null
        defaultUploadImageShouldBeFound("ownerEntityName.specified=true");

        // Get all the uploadImageList where ownerEntityName is null
        defaultUploadImageShouldNotBeFound("ownerEntityName.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityName contains DEFAULT_OWNER_ENTITY_NAME
        defaultUploadImageShouldBeFound("ownerEntityName.contains=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadImageList where ownerEntityName contains UPDATED_OWNER_ENTITY_NAME
        defaultUploadImageShouldNotBeFound("ownerEntityName.contains=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityName does not contain DEFAULT_OWNER_ENTITY_NAME
        defaultUploadImageShouldNotBeFound("ownerEntityName.doesNotContain=" + DEFAULT_OWNER_ENTITY_NAME);

        // Get all the uploadImageList where ownerEntityName does not contain UPDATED_OWNER_ENTITY_NAME
        defaultUploadImageShouldBeFound("ownerEntityName.doesNotContain=" + UPDATED_OWNER_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId equals to DEFAULT_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.equals=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.equals=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId in DEFAULT_OWNER_ENTITY_ID or UPDATED_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.in=" + DEFAULT_OWNER_ENTITY_ID + "," + UPDATED_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId equals to UPDATED_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.in=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId is not null
        defaultUploadImageShouldBeFound("ownerEntityId.specified=true");

        // Get all the uploadImageList where ownerEntityId is null
        defaultUploadImageShouldNotBeFound("ownerEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId is greater than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.greaterThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId is greater than or equal to UPDATED_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.greaterThanOrEqual=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId is less than or equal to DEFAULT_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.lessThanOrEqual=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId is less than or equal to SMALLER_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.lessThanOrEqual=" + SMALLER_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId is less than DEFAULT_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.lessThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId is less than UPDATED_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.lessThan=" + UPDATED_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByOwnerEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where ownerEntityId is greater than DEFAULT_OWNER_ENTITY_ID
        defaultUploadImageShouldNotBeFound("ownerEntityId.greaterThan=" + DEFAULT_OWNER_ENTITY_ID);

        // Get all the uploadImageList where ownerEntityId is greater than SMALLER_OWNER_ENTITY_ID
        defaultUploadImageShouldBeFound("ownerEntityId.greaterThan=" + SMALLER_OWNER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessTitle equals to DEFAULT_BUSINESS_TITLE
        defaultUploadImageShouldBeFound("businessTitle.equals=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadImageList where businessTitle equals to UPDATED_BUSINESS_TITLE
        defaultUploadImageShouldNotBeFound("businessTitle.equals=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessTitle in DEFAULT_BUSINESS_TITLE or UPDATED_BUSINESS_TITLE
        defaultUploadImageShouldBeFound("businessTitle.in=" + DEFAULT_BUSINESS_TITLE + "," + UPDATED_BUSINESS_TITLE);

        // Get all the uploadImageList where businessTitle equals to UPDATED_BUSINESS_TITLE
        defaultUploadImageShouldNotBeFound("businessTitle.in=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessTitle is not null
        defaultUploadImageShouldBeFound("businessTitle.specified=true");

        // Get all the uploadImageList where businessTitle is null
        defaultUploadImageShouldNotBeFound("businessTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessTitle contains DEFAULT_BUSINESS_TITLE
        defaultUploadImageShouldBeFound("businessTitle.contains=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadImageList where businessTitle contains UPDATED_BUSINESS_TITLE
        defaultUploadImageShouldNotBeFound("businessTitle.contains=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessTitleNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessTitle does not contain DEFAULT_BUSINESS_TITLE
        defaultUploadImageShouldNotBeFound("businessTitle.doesNotContain=" + DEFAULT_BUSINESS_TITLE);

        // Get all the uploadImageList where businessTitle does not contain UPDATED_BUSINESS_TITLE
        defaultUploadImageShouldBeFound("businessTitle.doesNotContain=" + UPDATED_BUSINESS_TITLE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessDesc equals to DEFAULT_BUSINESS_DESC
        defaultUploadImageShouldBeFound("businessDesc.equals=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadImageList where businessDesc equals to UPDATED_BUSINESS_DESC
        defaultUploadImageShouldNotBeFound("businessDesc.equals=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessDesc in DEFAULT_BUSINESS_DESC or UPDATED_BUSINESS_DESC
        defaultUploadImageShouldBeFound("businessDesc.in=" + DEFAULT_BUSINESS_DESC + "," + UPDATED_BUSINESS_DESC);

        // Get all the uploadImageList where businessDesc equals to UPDATED_BUSINESS_DESC
        defaultUploadImageShouldNotBeFound("businessDesc.in=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessDesc is not null
        defaultUploadImageShouldBeFound("businessDesc.specified=true");

        // Get all the uploadImageList where businessDesc is null
        defaultUploadImageShouldNotBeFound("businessDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessDesc contains DEFAULT_BUSINESS_DESC
        defaultUploadImageShouldBeFound("businessDesc.contains=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadImageList where businessDesc contains UPDATED_BUSINESS_DESC
        defaultUploadImageShouldNotBeFound("businessDesc.contains=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessDescNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessDesc does not contain DEFAULT_BUSINESS_DESC
        defaultUploadImageShouldNotBeFound("businessDesc.doesNotContain=" + DEFAULT_BUSINESS_DESC);

        // Get all the uploadImageList where businessDesc does not contain UPDATED_BUSINESS_DESC
        defaultUploadImageShouldBeFound("businessDesc.doesNotContain=" + UPDATED_BUSINESS_DESC);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessStatus equals to DEFAULT_BUSINESS_STATUS
        defaultUploadImageShouldBeFound("businessStatus.equals=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadImageList where businessStatus equals to UPDATED_BUSINESS_STATUS
        defaultUploadImageShouldNotBeFound("businessStatus.equals=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessStatus in DEFAULT_BUSINESS_STATUS or UPDATED_BUSINESS_STATUS
        defaultUploadImageShouldBeFound("businessStatus.in=" + DEFAULT_BUSINESS_STATUS + "," + UPDATED_BUSINESS_STATUS);

        // Get all the uploadImageList where businessStatus equals to UPDATED_BUSINESS_STATUS
        defaultUploadImageShouldNotBeFound("businessStatus.in=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessStatus is not null
        defaultUploadImageShouldBeFound("businessStatus.specified=true");

        // Get all the uploadImageList where businessStatus is null
        defaultUploadImageShouldNotBeFound("businessStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessStatus contains DEFAULT_BUSINESS_STATUS
        defaultUploadImageShouldBeFound("businessStatus.contains=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadImageList where businessStatus contains UPDATED_BUSINESS_STATUS
        defaultUploadImageShouldNotBeFound("businessStatus.contains=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadImagesByBusinessStatusNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where businessStatus does not contain DEFAULT_BUSINESS_STATUS
        defaultUploadImageShouldNotBeFound("businessStatus.doesNotContain=" + DEFAULT_BUSINESS_STATUS);

        // Get all the uploadImageList where businessStatus does not contain UPDATED_BUSINESS_STATUS
        defaultUploadImageShouldBeFound("businessStatus.doesNotContain=" + UPDATED_BUSINESS_STATUS);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt equals to DEFAULT_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the uploadImageList where createAt equals to UPDATED_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the uploadImageList where createAt equals to UPDATED_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt is not null
        defaultUploadImageShouldBeFound("createAt.specified=true");

        // Get all the uploadImageList where createAt is null
        defaultUploadImageShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uploadImageList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uploadImageList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt is less than DEFAULT_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the uploadImageList where createAt is less than UPDATED_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createAt is greater than DEFAULT_CREATE_AT
        defaultUploadImageShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the uploadImageList where createAt is greater than SMALLER_CREATE_AT
        defaultUploadImageShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize equals to DEFAULT_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the uploadImageList where fileSize equals to UPDATED_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the uploadImageList where fileSize equals to UPDATED_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize is not null
        defaultUploadImageShouldBeFound("fileSize.specified=true");

        // Get all the uploadImageList where fileSize is null
        defaultUploadImageShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the uploadImageList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the uploadImageList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize is less than DEFAULT_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the uploadImageList where fileSize is less than UPDATED_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultUploadImageShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the uploadImageList where fileSize is greater than SMALLER_FILE_SIZE
        defaultUploadImageShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where smartUrl equals to DEFAULT_SMART_URL
        defaultUploadImageShouldBeFound("smartUrl.equals=" + DEFAULT_SMART_URL);

        // Get all the uploadImageList where smartUrl equals to UPDATED_SMART_URL
        defaultUploadImageShouldNotBeFound("smartUrl.equals=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where smartUrl in DEFAULT_SMART_URL or UPDATED_SMART_URL
        defaultUploadImageShouldBeFound("smartUrl.in=" + DEFAULT_SMART_URL + "," + UPDATED_SMART_URL);

        // Get all the uploadImageList where smartUrl equals to UPDATED_SMART_URL
        defaultUploadImageShouldNotBeFound("smartUrl.in=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where smartUrl is not null
        defaultUploadImageShouldBeFound("smartUrl.specified=true");

        // Get all the uploadImageList where smartUrl is null
        defaultUploadImageShouldNotBeFound("smartUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where smartUrl contains DEFAULT_SMART_URL
        defaultUploadImageShouldBeFound("smartUrl.contains=" + DEFAULT_SMART_URL);

        // Get all the uploadImageList where smartUrl contains UPDATED_SMART_URL
        defaultUploadImageShouldNotBeFound("smartUrl.contains=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesBySmartUrlNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where smartUrl does not contain DEFAULT_SMART_URL
        defaultUploadImageShouldNotBeFound("smartUrl.doesNotContain=" + DEFAULT_SMART_URL);

        // Get all the uploadImageList where smartUrl does not contain UPDATED_SMART_URL
        defaultUploadImageShouldBeFound("smartUrl.doesNotContain=" + UPDATED_SMART_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where mediumUrl equals to DEFAULT_MEDIUM_URL
        defaultUploadImageShouldBeFound("mediumUrl.equals=" + DEFAULT_MEDIUM_URL);

        // Get all the uploadImageList where mediumUrl equals to UPDATED_MEDIUM_URL
        defaultUploadImageShouldNotBeFound("mediumUrl.equals=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where mediumUrl in DEFAULT_MEDIUM_URL or UPDATED_MEDIUM_URL
        defaultUploadImageShouldBeFound("mediumUrl.in=" + DEFAULT_MEDIUM_URL + "," + UPDATED_MEDIUM_URL);

        // Get all the uploadImageList where mediumUrl equals to UPDATED_MEDIUM_URL
        defaultUploadImageShouldNotBeFound("mediumUrl.in=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where mediumUrl is not null
        defaultUploadImageShouldBeFound("mediumUrl.specified=true");

        // Get all the uploadImageList where mediumUrl is null
        defaultUploadImageShouldNotBeFound("mediumUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where mediumUrl contains DEFAULT_MEDIUM_URL
        defaultUploadImageShouldBeFound("mediumUrl.contains=" + DEFAULT_MEDIUM_URL);

        // Get all the uploadImageList where mediumUrl contains UPDATED_MEDIUM_URL
        defaultUploadImageShouldNotBeFound("mediumUrl.contains=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByMediumUrlNotContainsSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where mediumUrl does not contain DEFAULT_MEDIUM_URL
        defaultUploadImageShouldNotBeFound("mediumUrl.doesNotContain=" + DEFAULT_MEDIUM_URL);

        // Get all the uploadImageList where mediumUrl does not contain UPDATED_MEDIUM_URL
        defaultUploadImageShouldBeFound("mediumUrl.doesNotContain=" + UPDATED_MEDIUM_URL);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount equals to DEFAULT_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.equals=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount equals to UPDATED_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.equals=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount in DEFAULT_REFERENCE_COUNT or UPDATED_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.in=" + DEFAULT_REFERENCE_COUNT + "," + UPDATED_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount equals to UPDATED_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.in=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount is not null
        defaultUploadImageShouldBeFound("referenceCount.specified=true");

        // Get all the uploadImageList where referenceCount is null
        defaultUploadImageShouldNotBeFound("referenceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount is greater than or equal to DEFAULT_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.greaterThanOrEqual=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount is greater than or equal to UPDATED_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.greaterThanOrEqual=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount is less than or equal to DEFAULT_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.lessThanOrEqual=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount is less than or equal to SMALLER_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.lessThanOrEqual=" + SMALLER_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount is less than DEFAULT_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.lessThan=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount is less than UPDATED_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.lessThan=" + UPDATED_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByReferenceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where referenceCount is greater than DEFAULT_REFERENCE_COUNT
        defaultUploadImageShouldNotBeFound("referenceCount.greaterThan=" + DEFAULT_REFERENCE_COUNT);

        // Get all the uploadImageList where referenceCount is greater than SMALLER_REFERENCE_COUNT
        defaultUploadImageShouldBeFound("referenceCount.greaterThan=" + SMALLER_REFERENCE_COUNT);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy equals to DEFAULT_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the uploadImageList where createdBy equals to UPDATED_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the uploadImageList where createdBy equals to UPDATED_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy is not null
        defaultUploadImageShouldBeFound("createdBy.specified=true");

        // Get all the uploadImageList where createdBy is null
        defaultUploadImageShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the uploadImageList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the uploadImageList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy is less than DEFAULT_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the uploadImageList where createdBy is less than UPDATED_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdBy is greater than DEFAULT_CREATED_BY
        defaultUploadImageShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the uploadImageList where createdBy is greater than SMALLER_CREATED_BY
        defaultUploadImageShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdDate equals to DEFAULT_CREATED_DATE
        defaultUploadImageShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the uploadImageList where createdDate equals to UPDATED_CREATED_DATE
        defaultUploadImageShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultUploadImageShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the uploadImageList where createdDate equals to UPDATED_CREATED_DATE
        defaultUploadImageShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where createdDate is not null
        defaultUploadImageShouldBeFound("createdDate.specified=true");

        // Get all the uploadImageList where createdDate is null
        defaultUploadImageShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is not null
        defaultUploadImageShouldBeFound("lastModifiedBy.specified=true");

        // Get all the uploadImageList where lastModifiedBy is null
        defaultUploadImageShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is greater than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy is greater than or equal to UPDATED_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is less than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy is less than or equal to SMALLER_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is less than DEFAULT_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy is less than UPDATED_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedBy is greater than DEFAULT_LAST_MODIFIED_BY
        defaultUploadImageShouldNotBeFound("lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the uploadImageList where lastModifiedBy is greater than SMALLER_LAST_MODIFIED_BY
        defaultUploadImageShouldBeFound("lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultUploadImageShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the uploadImageList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUploadImageShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultUploadImageShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the uploadImageList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultUploadImageShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllUploadImagesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        // Get all the uploadImageList where lastModifiedDate is not null
        defaultUploadImageShouldBeFound("lastModifiedDate.specified=true");

        // Get all the uploadImageList where lastModifiedDate is null
        defaultUploadImageShouldNotBeFound("lastModifiedDate.specified=false");
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
        uploadImageRepository.save(uploadImage);

        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
        UploadImage testUploadImage = uploadImageList.get(uploadImageList.size() - 1);
        assertThat(testUploadImage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadImage.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUploadImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUploadImage.getExt()).isEqualTo(UPDATED_EXT);
        assertThat(testUploadImage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUploadImage.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testUploadImage.getFolder()).isEqualTo(UPDATED_FOLDER);
        assertThat(testUploadImage.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadImage.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadImage.getBusinessTitle()).isEqualTo(UPDATED_BUSINESS_TITLE);
        assertThat(testUploadImage.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadImage.getBusinessStatus()).isEqualTo(UPDATED_BUSINESS_STATUS);
        assertThat(testUploadImage.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadImage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testUploadImage.getSmartUrl()).isEqualTo(UPDATED_SMART_URL);
        assertThat(testUploadImage.getMediumUrl()).isEqualTo(UPDATED_MEDIUM_URL);
        assertThat(testUploadImage.getReferenceCount()).isEqualTo(UPDATED_REFERENCE_COUNT);
        assertThat(testUploadImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUploadImage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUploadImage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadImage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uploadImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uploadImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUploadImageWithPatch() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();

        // Update the uploadImage using partial update
        UploadImage partialUpdatedUploadImage = new UploadImage();
        partialUpdatedUploadImage.setId(uploadImage.getId());

        partialUpdatedUploadImage
            .url(UPDATED_URL)
            .fullName(UPDATED_FULL_NAME)
            .ownerEntityName(UPDATED_OWNER_ENTITY_NAME)
            .ownerEntityId(UPDATED_OWNER_ENTITY_ID)
            .businessTitle(UPDATED_BUSINESS_TITLE)
            .businessDesc(UPDATED_BUSINESS_DESC)
            .businessStatus(UPDATED_BUSINESS_STATUS)
            .createAt(UPDATED_CREATE_AT)
            .smartUrl(UPDATED_SMART_URL)
            .mediumUrl(UPDATED_MEDIUM_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUploadImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUploadImage))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
        UploadImage testUploadImage = uploadImageList.get(uploadImageList.size() - 1);
        assertThat(testUploadImage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadImage.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUploadImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUploadImage.getExt()).isEqualTo(DEFAULT_EXT);
        assertThat(testUploadImage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUploadImage.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testUploadImage.getFolder()).isEqualTo(DEFAULT_FOLDER);
        assertThat(testUploadImage.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadImage.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadImage.getBusinessTitle()).isEqualTo(UPDATED_BUSINESS_TITLE);
        assertThat(testUploadImage.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadImage.getBusinessStatus()).isEqualTo(UPDATED_BUSINESS_STATUS);
        assertThat(testUploadImage.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadImage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testUploadImage.getSmartUrl()).isEqualTo(UPDATED_SMART_URL);
        assertThat(testUploadImage.getMediumUrl()).isEqualTo(UPDATED_MEDIUM_URL);
        assertThat(testUploadImage.getReferenceCount()).isEqualTo(DEFAULT_REFERENCE_COUNT);
        assertThat(testUploadImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUploadImage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUploadImage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadImage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUploadImageWithPatch() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUploadImage))
            )
            .andExpect(status().isOk());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
        UploadImage testUploadImage = uploadImageList.get(uploadImageList.size() - 1);
        assertThat(testUploadImage.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUploadImage.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUploadImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUploadImage.getExt()).isEqualTo(UPDATED_EXT);
        assertThat(testUploadImage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUploadImage.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testUploadImage.getFolder()).isEqualTo(UPDATED_FOLDER);
        assertThat(testUploadImage.getOwnerEntityName()).isEqualTo(UPDATED_OWNER_ENTITY_NAME);
        assertThat(testUploadImage.getOwnerEntityId()).isEqualTo(UPDATED_OWNER_ENTITY_ID);
        assertThat(testUploadImage.getBusinessTitle()).isEqualTo(UPDATED_BUSINESS_TITLE);
        assertThat(testUploadImage.getBusinessDesc()).isEqualTo(UPDATED_BUSINESS_DESC);
        assertThat(testUploadImage.getBusinessStatus()).isEqualTo(UPDATED_BUSINESS_STATUS);
        assertThat(testUploadImage.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUploadImage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testUploadImage.getSmartUrl()).isEqualTo(UPDATED_SMART_URL);
        assertThat(testUploadImage.getMediumUrl()).isEqualTo(UPDATED_MEDIUM_URL);
        assertThat(testUploadImage.getReferenceCount()).isEqualTo(UPDATED_REFERENCE_COUNT);
        assertThat(testUploadImage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUploadImage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUploadImage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUploadImage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uploadImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUploadImage() throws Exception {
        int databaseSizeBeforeUpdate = uploadImageRepository.findAll().size();
        uploadImage.setId(longCount.incrementAndGet());

        // Create the UploadImage
        UploadImageDTO uploadImageDTO = uploadImageMapper.toDto(uploadImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUploadImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uploadImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UploadImage in the database
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUploadImage() throws Exception {
        // Initialize the database
        uploadImageRepository.save(uploadImage);

        int databaseSizeBeforeDelete = uploadImageRepository.findAll().size();

        // Delete the uploadImage
        restUploadImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, uploadImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UploadImage> uploadImageList = uploadImageRepository.findAll();
        assertThat(uploadImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
