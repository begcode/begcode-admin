package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.AuthorityAsserts.*;
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
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.repository.AuthorityRepository;
import com.begcode.monolith.service.AuthorityService;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.mapper.AuthorityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Integration tests for the {@link AuthorityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class AuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final Boolean DEFAULT_DISPLAY = false;
    private static final Boolean UPDATED_DISPLAY = true;

    private static final String ENTITY_API_URL = "/api/authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Mock
    private AuthorityRepository authorityRepositoryMock;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Mock
    private AuthorityService authorityServiceMock;

    @Autowired
    private MockMvc restAuthorityMockMvc;

    private Authority authority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createEntity() {
        Authority authority = new Authority()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .info(DEFAULT_INFO)
            .order(DEFAULT_ORDER)
            .display(DEFAULT_DISPLAY);
        return authority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authority createUpdatedEntity() {
        Authority authority = new Authority()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .info(UPDATED_INFO)
            .order(UPDATED_ORDER)
            .display(UPDATED_DISPLAY);
        return authority;
    }

    @BeforeEach
    public void initTest() {
        authority = createEntity();
    }

    @Test
    @Transactional
    void createAuthority() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);
        var returnedAuthorityDTO = om.readValue(
            restAuthorityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuthorityDTO.class
        );

        // Validate the Authority in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuthority = authorityMapper.toEntity(returnedAuthorityDTO);
        assertAuthorityUpdatableFieldsEquals(returnedAuthority, getPersistedAuthority(returnedAuthority));
    }

    @Test
    @Transactional
    void createAuthorityWithExistingId() throws Exception {
        // Create the Authority with an existing ID
        authority.setId(1L);
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuthoritiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(authorityServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restAuthorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(authorityServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAuthoritiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(authorityServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restAuthorityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(authorityRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getAuthority() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get the authority
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL_ID, authority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY.booleanValue()));
    }

    @Test
    @Transactional
    void getAuthoritiesByIdFiltering() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        Long id = authority.getId();

        defaultAuthorityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAuthorityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAuthorityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where name equals to
        defaultAuthorityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where name in
        defaultAuthorityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where name is not null
        defaultAuthorityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthoritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where name contains
        defaultAuthorityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where name does not contain
        defaultAuthorityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where code equals to
        defaultAuthorityFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where code in
        defaultAuthorityFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where code is not null
        defaultAuthorityFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthoritiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where code contains
        defaultAuthorityFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where code does not contain
        defaultAuthorityFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where info equals to
        defaultAuthorityFiltering("info.equals=" + DEFAULT_INFO, "info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where info in
        defaultAuthorityFiltering("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO, "info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where info is not null
        defaultAuthorityFiltering("info.specified=true", "info.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthoritiesByInfoContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where info contains
        defaultAuthorityFiltering("info.contains=" + DEFAULT_INFO, "info.contains=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByInfoNotContainsSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where info does not contain
        defaultAuthorityFiltering("info.doesNotContain=" + UPDATED_INFO, "info.doesNotContain=" + DEFAULT_INFO);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order equals to
        defaultAuthorityFiltering("order.equals=" + DEFAULT_ORDER, "order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order in
        defaultAuthorityFiltering("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER, "order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order is not null
        defaultAuthorityFiltering("order.specified=true", "order.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order is greater than or equal to
        defaultAuthorityFiltering("order.greaterThanOrEqual=" + DEFAULT_ORDER, "order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order is less than or equal to
        defaultAuthorityFiltering("order.lessThanOrEqual=" + DEFAULT_ORDER, "order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order is less than
        defaultAuthorityFiltering("order.lessThan=" + UPDATED_ORDER, "order.lessThan=" + DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where order is greater than
        defaultAuthorityFiltering("order.greaterThan=" + SMALLER_ORDER, "order.greaterThan=" + DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByDisplayIsEqualToSomething() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where display equals to
        defaultAuthorityFiltering("display.equals=" + DEFAULT_DISPLAY, "display.equals=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByDisplayIsInShouldWork() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where display in
        defaultAuthorityFiltering("display.in=" + DEFAULT_DISPLAY + "," + UPDATED_DISPLAY, "display.in=" + UPDATED_DISPLAY);
    }

    @Test
    @Transactional
    void getAllAuthoritiesByDisplayIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        // Get all the authorityList where display is not null
        defaultAuthorityFiltering("display.specified=true", "display.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthoritiesByViewPermissionsIsEqualToSomething() throws Exception {
        ViewPermission viewPermissions = ViewPermissionResourceIT.createEntity();
        // authority.addViewPermissions(viewPermissions);
        authorityRepository.insert(authority);
        Long viewPermissionsId = viewPermissions.getId();
        // Get all the authorityList where viewPermissions equals to viewPermissionsId
        defaultAuthorityShouldBeFound("viewPermissionsId.equals=" + viewPermissionsId);

        // Get all the authorityList where viewPermissions equals to (viewPermissionsId + 1)
        defaultAuthorityShouldNotBeFound("viewPermissionsId.equals=" + (viewPermissionsId + 1));
    }

    @Test
    @Transactional
    void getAllAuthoritiesByApiPermissionsIsEqualToSomething() throws Exception {
        ApiPermission apiPermissions = ApiPermissionResourceIT.createEntity();
        // authority.addApiPermissions(apiPermissions);
        authorityRepository.insert(authority);
        Long apiPermissionsId = apiPermissions.getId();
        // Get all the authorityList where apiPermissions equals to apiPermissionsId
        defaultAuthorityShouldBeFound("apiPermissionsId.equals=" + apiPermissionsId);

        // Get all the authorityList where apiPermissions equals to (apiPermissionsId + 1)
        defaultAuthorityShouldNotBeFound("apiPermissionsId.equals=" + (apiPermissionsId + 1));
    }

    @Test
    @Transactional
    void getAllAuthoritiesByParentIsEqualToSomething() throws Exception {
        Authority parent = AuthorityResourceIT.createEntity();
        authority.setParent(parent);
        authorityRepository.insert(authority);
        Long parentId = parent.getId();
        // Get all the authorityList where parent equals to parentId
        defaultAuthorityShouldBeFound("parentId.equals=" + parentId);

        // Get all the authorityList where parent equals to (parentId + 1)
        defaultAuthorityShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllAuthoritiesByUsersIsEqualToSomething() throws Exception {
        User users = UserResourceIT.createEntity();
        // authority.addUsers(users);
        authorityRepository.insert(authority);
        Long usersId = users.getId();
        // Get all the authorityList where users equals to usersId
        defaultAuthorityShouldBeFound("usersId.equals=" + usersId);

        // Get all the authorityList where users equals to (usersId + 1)
        defaultAuthorityShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    @Test
    @Transactional
    void getAllAuthoritiesByDepartmentIsEqualToSomething() throws Exception {
        Department department = DepartmentResourceIT.createEntity();
        // authority.addDepartment(department);
        authorityRepository.insert(authority);
        Long departmentId = department.getId();
        // Get all the authorityList where department equals to departmentId
        defaultAuthorityShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the authorityList where department equals to (departmentId + 1)
        defaultAuthorityShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    private void defaultAuthorityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAuthorityShouldBeFound(shouldBeFound);
        defaultAuthorityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuthorityShouldBeFound(String filter) throws Exception {
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY.booleanValue())));

        // Check, that the count call also returns 1
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuthorityShouldNotBeFound(String filter) throws Exception {
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAuthority() throws Exception {
        // Get the authority
        restAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuthority() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority
        Authority updatedAuthority = authorityRepository.findById(authority.getId()).orElseThrow();
        updatedAuthority.name(UPDATED_NAME).code(UPDATED_CODE).info(UPDATED_INFO).order(UPDATED_ORDER).display(UPDATED_DISPLAY);
        AuthorityDTO authorityDTO = authorityMapper.toDto(updatedAuthority);

        restAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authorityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuthorityToMatchAllProperties(updatedAuthority);
    }

    @Test
    @Transactional
    void putNonExistingAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authorityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthorityWithPatch() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority using partial update
        Authority partialUpdatedAuthority = new Authority();
        partialUpdatedAuthority.setId(authority.getId());

        partialUpdatedAuthority.code(UPDATED_CODE).order(UPDATED_ORDER);

        restAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthority))
            )
            .andExpect(status().isOk());

        // Validate the Authority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAuthority, authority),
            getPersistedAuthority(authority)
        );
    }

    @Test
    @Transactional
    void fullUpdateAuthorityWithPatch() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authority using partial update
        Authority partialUpdatedAuthority = new Authority();
        partialUpdatedAuthority.setId(authority.getId());

        partialUpdatedAuthority.name(UPDATED_NAME).code(UPDATED_CODE).info(UPDATED_INFO).order(UPDATED_ORDER).display(UPDATED_DISPLAY);

        restAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthority))
            )
            .andExpect(status().isOk());

        // Validate the Authority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthorityUpdatableFieldsEquals(partialUpdatedAuthority, getPersistedAuthority(partialUpdatedAuthority));
    }

    @Test
    @Transactional
    void patchNonExistingAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authorityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authority.setId(longCount.incrementAndGet());

        // Create the Authority
        AuthorityDTO authorityDTO = authorityMapper.toDto(authority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(authorityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthority() throws Exception {
        // Initialize the database
        authorityRepository.save(authority);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the authority
        restAuthorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, authority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return authorityRepository.selectCount(null);
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

    protected Authority getPersistedAuthority(Authority authority) {
        return authorityRepository.findById(authority.getId()).orElseThrow();
    }

    protected void assertPersistedAuthorityToMatchAllProperties(Authority expectedAuthority) {
        assertAuthorityAllPropertiesEquals(expectedAuthority, getPersistedAuthority(expectedAuthority));
    }

    protected void assertPersistedAuthorityToMatchUpdatableProperties(Authority expectedAuthority) {
        assertAuthorityAllUpdatablePropertiesEquals(expectedAuthority, getPersistedAuthority(expectedAuthority));
    }
}
