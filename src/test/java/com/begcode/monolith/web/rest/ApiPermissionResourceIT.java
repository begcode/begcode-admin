package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.ApiPermissionAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.enumeration.ApiPermissionState;
import com.begcode.monolith.domain.enumeration.ApiPermissionType;
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.repository.ApiPermissionRepository;
import com.begcode.monolith.service.ApiPermissionService;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.service.mapper.ApiPermissionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Integration tests for the {@link ApiPermissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class ApiPermissionResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ApiPermissionType DEFAULT_TYPE = ApiPermissionType.BUSINESS;
    private static final ApiPermissionType UPDATED_TYPE = ApiPermissionType.API;

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final ApiPermissionState DEFAULT_STATUS = ApiPermissionState.CONFIGURABLE;
    private static final ApiPermissionState UPDATED_STATUS = ApiPermissionState.PERMIT_ALL;

    private static final String ENTITY_API_URL = "/api/api-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApiPermissionRepository apiPermissionRepository;

    @Mock
    private ApiPermissionRepository apiPermissionRepositoryMock;

    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Mock
    private ApiPermissionService apiPermissionServiceMock;

    @Autowired
    private MockMvc restApiPermissionMockMvc;

    private ApiPermission apiPermission;

    private ApiPermission insertedApiPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiPermission createEntity() {
        ApiPermission apiPermission = new ApiPermission()
            .serviceName(DEFAULT_SERVICE_NAME)
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .method(DEFAULT_METHOD)
            .url(DEFAULT_URL)
            .status(DEFAULT_STATUS);
        return apiPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiPermission createUpdatedEntity() {
        ApiPermission apiPermission = new ApiPermission()
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL)
            .status(UPDATED_STATUS);
        return apiPermission;
    }

    @BeforeEach
    public void initTest() {
        apiPermission = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApiPermission != null) {
            apiPermissionRepository.deleteById(insertedApiPermission.getId());
            insertedApiPermission = null;
        }
    }

    @Test
    @Transactional
    void createApiPermission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);
        var returnedApiPermissionDTO = om.readValue(
            restApiPermissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiPermissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApiPermissionDTO.class
        );

        // Validate the ApiPermission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApiPermission = apiPermissionMapper.toEntity(returnedApiPermissionDTO);
        assertApiPermissionUpdatableFieldsEquals(returnedApiPermission, getPersistedApiPermission(returnedApiPermission));

        insertedApiPermission = returnedApiPermission;
    }

    @Test
    @Transactional
    void createApiPermissionWithExistingId() throws Exception {
        // Create the ApiPermission with an existing ID
        apiPermission.setId(1L);
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApiPermissions() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApiPermissionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(apiPermissionServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restApiPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(apiPermissionServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApiPermissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(apiPermissionServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restApiPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(apiPermissionRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getApiPermission() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get the apiPermission
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, apiPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiPermission.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getApiPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        Long id = apiPermission.getId();

        defaultApiPermissionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultApiPermissionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultApiPermissionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where serviceName equals to
        defaultApiPermissionFiltering("serviceName.equals=" + DEFAULT_SERVICE_NAME, "serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where serviceName in
        defaultApiPermissionFiltering(
            "serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME,
            "serviceName.in=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where serviceName is not null
        defaultApiPermissionFiltering("serviceName.specified=true", "serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where serviceName contains
        defaultApiPermissionFiltering("serviceName.contains=" + DEFAULT_SERVICE_NAME, "serviceName.contains=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where serviceName does not contain
        defaultApiPermissionFiltering(
            "serviceName.doesNotContain=" + UPDATED_SERVICE_NAME,
            "serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where name equals to
        defaultApiPermissionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where name in
        defaultApiPermissionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where name is not null
        defaultApiPermissionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where name contains
        defaultApiPermissionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where name does not contain
        defaultApiPermissionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where code equals to
        defaultApiPermissionFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where code in
        defaultApiPermissionFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where code is not null
        defaultApiPermissionFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where code contains
        defaultApiPermissionFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where code does not contain
        defaultApiPermissionFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where description equals to
        defaultApiPermissionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where description in
        defaultApiPermissionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where description is not null
        defaultApiPermissionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where description contains
        defaultApiPermissionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where description does not contain
        defaultApiPermissionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where type equals to
        defaultApiPermissionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where type in
        defaultApiPermissionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where type is not null
        defaultApiPermissionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where method equals to
        defaultApiPermissionFiltering("method.equals=" + DEFAULT_METHOD, "method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where method in
        defaultApiPermissionFiltering("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD, "method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where method is not null
        defaultApiPermissionFiltering("method.specified=true", "method.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where method contains
        defaultApiPermissionFiltering("method.contains=" + DEFAULT_METHOD, "method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where method does not contain
        defaultApiPermissionFiltering("method.doesNotContain=" + UPDATED_METHOD, "method.doesNotContain=" + DEFAULT_METHOD);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where url equals to
        defaultApiPermissionFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where url in
        defaultApiPermissionFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where url is not null
        defaultApiPermissionFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where url contains
        defaultApiPermissionFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where url does not contain
        defaultApiPermissionFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where status equals to
        defaultApiPermissionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where status in
        defaultApiPermissionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllApiPermissionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        // Get all the apiPermissionList where status is not null
        defaultApiPermissionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllApiPermissionsByParentIsEqualToSomething() throws Exception {
        ApiPermission parent = ApiPermissionResourceIT.createEntity();
        apiPermission.setParent(parent);
        apiPermissionRepository.insert(apiPermission);
        Long parentId = parent.getId();
        // Get all the apiPermissionList where parent equals to parentId
        defaultApiPermissionShouldBeFound("parentId.equals=" + parentId);

        // Get all the apiPermissionList where parent equals to (parentId + 1)
        defaultApiPermissionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllApiPermissionsByAuthoritiesIsEqualToSomething() throws Exception {
        Authority authorities = AuthorityResourceIT.createEntity();
        // apiPermission.addAuthorities(authorities);
        apiPermissionRepository.insert(apiPermission);
        Long authoritiesId = authorities.getId();
        // Get all the apiPermissionList where authorities equals to authoritiesId
        defaultApiPermissionShouldBeFound("authoritiesId.equals=" + authoritiesId);

        // Get all the apiPermissionList where authorities equals to (authoritiesId + 1)
        defaultApiPermissionShouldNotBeFound("authoritiesId.equals=" + (authoritiesId + 1));
    }

    private void defaultApiPermissionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultApiPermissionShouldBeFound(shouldBeFound);
        defaultApiPermissionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApiPermissionShouldBeFound(String filter) throws Exception {
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApiPermissionShouldNotBeFound(String filter) throws Exception {
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApiPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApiPermission() throws Exception {
        // Get the apiPermission
        restApiPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApiPermission() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiPermission
        ApiPermission updatedApiPermission = apiPermissionRepository.findById(apiPermission.getId()).orElseThrow();
        updatedApiPermission
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL)
            .status(UPDATED_STATUS);
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(updatedApiPermission);

        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApiPermissionToMatchAllProperties(updatedApiPermission);
    }

    @Test
    @Transactional
    void putNonExistingApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiPermission using partial update
        ApiPermission partialUpdatedApiPermission = new ApiPermission();
        partialUpdatedApiPermission.setId(apiPermission.getId());

        partialUpdatedApiPermission.name(UPDATED_NAME).code(UPDATED_CODE).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiPermission))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiPermissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApiPermission, apiPermission),
            getPersistedApiPermission(apiPermission)
        );
    }

    @Test
    @Transactional
    void fullUpdateApiPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiPermission using partial update
        ApiPermission partialUpdatedApiPermission = new ApiPermission();
        partialUpdatedApiPermission.setId(apiPermission.getId());

        partialUpdatedApiPermission
            .serviceName(UPDATED_SERVICE_NAME)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .method(UPDATED_METHOD)
            .url(UPDATED_URL)
            .status(UPDATED_STATUS);

        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiPermission))
            )
            .andExpect(status().isOk());

        // Validate the ApiPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiPermissionUpdatableFieldsEquals(partialUpdatedApiPermission, getPersistedApiPermission(partialUpdatedApiPermission));
    }

    @Test
    @Transactional
    void patchNonExistingApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiPermission.setId(longCount.incrementAndGet());

        // Create the ApiPermission
        ApiPermissionDTO apiPermissionDTO = apiPermissionMapper.toDto(apiPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiPermissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(apiPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiPermission() throws Exception {
        // Initialize the database
        insertedApiPermission = apiPermissionRepository.saveAndGet(apiPermission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the apiPermission
        restApiPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return apiPermissionRepository.selectCount(null);
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

    protected ApiPermission getPersistedApiPermission(ApiPermission apiPermission) {
        return apiPermissionRepository.findById(apiPermission.getId()).orElseThrow();
    }

    protected void assertPersistedApiPermissionToMatchAllProperties(ApiPermission expectedApiPermission) {
        assertApiPermissionAllPropertiesEquals(expectedApiPermission, getPersistedApiPermission(expectedApiPermission));
    }

    protected void assertPersistedApiPermissionToMatchUpdatableProperties(ApiPermission expectedApiPermission) {
        assertApiPermissionAllUpdatablePropertiesEquals(expectedApiPermission, getPersistedApiPermission(expectedApiPermission));
    }
}
