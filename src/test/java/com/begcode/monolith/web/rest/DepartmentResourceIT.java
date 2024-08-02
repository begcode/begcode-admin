package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.DepartmentAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.DepartmentService;
import com.begcode.monolith.service.dto.DepartmentDTO;
import com.begcode.monolith.service.mapper.DepartmentMapper;
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
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class DepartmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentRepository departmentRepositoryMock;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Mock
    private DepartmentService departmentServiceMock;

    @Autowired
    private MockMvc restDepartmentMockMvc;

    private Department department;

    private Department insertedDepartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createEntity() {
        Department department = new Department()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .address(DEFAULT_ADDRESS)
            .phoneNum(DEFAULT_PHONE_NUM)
            .logo(DEFAULT_LOGO)
            .contact(DEFAULT_CONTACT);
        return department;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createUpdatedEntity() {
        Department department = new Department()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .phoneNum(UPDATED_PHONE_NUM)
            .logo(UPDATED_LOGO)
            .contact(UPDATED_CONTACT);
        return department;
    }

    @BeforeEach
    public void initTest() {
        department = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartment != null) {
            departmentRepository.deleteById(insertedDepartment.getId());
            insertedDepartment = null;
        }
    }

    @Test
    @Transactional
    void createDepartment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);
        var returnedDepartmentDTO = om.readValue(
            restDepartmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepartmentDTO.class
        );

        // Validate the Department in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDepartment = departmentMapper.toEntity(returnedDepartmentDTO);
        assertDepartmentUpdatableFieldsEquals(returnedDepartment, getPersistedDepartment(returnedDepartment));

        insertedDepartment = returnedDepartment;
    }

    @Test
    @Transactional
    void createDepartmentWithExistingId() throws Exception {
        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(departmentServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restDepartmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departmentServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departmentServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restDepartmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(departmentRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get the department
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL_ID, department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNum").value(DEFAULT_PHONE_NUM))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        Long id = department.getId();

        defaultDepartmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDepartmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDepartmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where name equals to
        defaultDepartmentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where name in
        defaultDepartmentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where name is not null
        defaultDepartmentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where name contains
        defaultDepartmentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where name does not contain
        defaultDepartmentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where code equals to
        defaultDepartmentFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where code in
        defaultDepartmentFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where code is not null
        defaultDepartmentFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where code contains
        defaultDepartmentFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where code does not contain
        defaultDepartmentFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where address equals to
        defaultDepartmentFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where address in
        defaultDepartmentFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where address is not null
        defaultDepartmentFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where address contains
        defaultDepartmentFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where address does not contain
        defaultDepartmentFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where phoneNum equals to
        defaultDepartmentFiltering("phoneNum.equals=" + DEFAULT_PHONE_NUM, "phoneNum.equals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where phoneNum in
        defaultDepartmentFiltering("phoneNum.in=" + DEFAULT_PHONE_NUM + "," + UPDATED_PHONE_NUM, "phoneNum.in=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where phoneNum is not null
        defaultDepartmentFiltering("phoneNum.specified=true", "phoneNum.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where phoneNum contains
        defaultDepartmentFiltering("phoneNum.contains=" + DEFAULT_PHONE_NUM, "phoneNum.contains=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where phoneNum does not contain
        defaultDepartmentFiltering("phoneNum.doesNotContain=" + UPDATED_PHONE_NUM, "phoneNum.doesNotContain=" + DEFAULT_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where logo equals to
        defaultDepartmentFiltering("logo.equals=" + DEFAULT_LOGO, "logo.equals=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where logo in
        defaultDepartmentFiltering("logo.in=" + DEFAULT_LOGO + "," + UPDATED_LOGO, "logo.in=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where logo is not null
        defaultDepartmentFiltering("logo.specified=true", "logo.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where logo contains
        defaultDepartmentFiltering("logo.contains=" + DEFAULT_LOGO, "logo.contains=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where logo does not contain
        defaultDepartmentFiltering("logo.doesNotContain=" + UPDATED_LOGO, "logo.doesNotContain=" + DEFAULT_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where contact equals to
        defaultDepartmentFiltering("contact.equals=" + DEFAULT_CONTACT, "contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where contact in
        defaultDepartmentFiltering("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT, "contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where contact is not null
        defaultDepartmentFiltering("contact.specified=true", "contact.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where contact contains
        defaultDepartmentFiltering("contact.contains=" + DEFAULT_CONTACT, "contact.contains=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        // Get all the departmentList where contact does not contain
        defaultDepartmentFiltering("contact.doesNotContain=" + UPDATED_CONTACT, "contact.doesNotContain=" + DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAuthoritiesIsEqualToSomething() throws Exception {
        Authority authorities = AuthorityResourceIT.createEntity();
        // department.addAuthorities(authorities);
        departmentRepository.insert(department);
        Long authoritiesId = authorities.getId();
        // Get all the departmentList where authorities equals to authoritiesId
        defaultDepartmentShouldBeFound("authoritiesId.equals=" + authoritiesId);

        // Get all the departmentList where authorities equals to (authoritiesId + 1)
        defaultDepartmentShouldNotBeFound("authoritiesId.equals=" + (authoritiesId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentsByParentIsEqualToSomething() throws Exception {
        Department parent = DepartmentResourceIT.createEntity();
        department.setParent(parent);
        departmentRepository.insert(department);
        Long parentId = parent.getId();
        // Get all the departmentList where parent equals to parentId
        defaultDepartmentShouldBeFound("parentId.equals=" + parentId);

        // Get all the departmentList where parent equals to (parentId + 1)
        defaultDepartmentShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultDepartmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDepartmentShouldBeFound(shouldBeFound);
        defaultDepartmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentShouldBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));

        // Check, that the count call also returns 1
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentShouldNotBeFound(String filter) throws Exception {
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).orElseThrow();
        updatedDepartment
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .phoneNum(UPDATED_PHONE_NUM)
            .logo(UPDATED_LOGO)
            .contact(UPDATED_CONTACT);
        DepartmentDTO departmentDTO = departmentMapper.toDto(updatedDepartment);

        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartmentToMatchAllProperties(updatedDepartment);
    }

    @Test
    @Transactional
    void putNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment.contact(UPDATED_CONTACT);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartment, department),
            getPersistedDepartment(department)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .phoneNum(UPDATED_PHONE_NUM)
            .logo(UPDATED_LOGO)
            .contact(UPDATED_CONTACT);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartmentUpdatableFieldsEquals(partialUpdatedDepartment, getPersistedDepartment(partialUpdatedDepartment));
    }

    @Test
    @Transactional
    void patchNonExistingDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartment() throws Exception {
        // Initialize the database
        insertedDepartment = departmentRepository.saveAndGet(department);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the department
        restDepartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, department.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departmentRepository.selectCount(null);
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

    protected Department getPersistedDepartment(Department department) {
        return departmentRepository.findById(department.getId()).orElseThrow();
    }

    protected void assertPersistedDepartmentToMatchAllProperties(Department expectedDepartment) {
        assertDepartmentAllPropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }

    protected void assertPersistedDepartmentToMatchUpdatableProperties(Department expectedDepartment) {
        assertDepartmentAllUpdatablePropertiesEquals(expectedDepartment, getPersistedDepartment(expectedDepartment));
    }
}
