package com.begcode.monolith.web.rest;

import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.domain.User;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.repository.DepartmentRepository;
import com.begcode.monolith.service.DepartmentService;
import com.begcode.monolith.service.dto.DepartmentDTO;
import com.begcode.monolith.service.mapper.DepartmentMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
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

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;
    private static final Long SMALLER_CREATE_USER_ID = 1L - 1L;

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .contact(DEFAULT_CONTACT)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .createTime(DEFAULT_CREATE_TIME);
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
            .contact(UPDATED_CONTACT)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME);
        return department;
    }

    @BeforeEach
    public void initTest() {
        department = createEntity();
    }

    @Test
    @Transactional
    void createDepartment() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();
        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);
        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate + 1);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDepartment.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDepartment.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testDepartment.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartment.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testDepartment.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testDepartment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    void createDepartmentWithExistingId() throws Exception {
        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

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
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].createUserId").value(hasItem(DEFAULT_CREATE_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))));
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
        departmentRepository.save(department);

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
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.createUserId").value(DEFAULT_CREATE_USER_ID.intValue()))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        Long id = department.getId();

        defaultDepartmentShouldBeFound("id.equals=" + id);
        defaultDepartmentShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where name equals to DEFAULT_NAME
        defaultDepartmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the departmentList where name equals to UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDepartmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the departmentList where name equals to UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where name is not null
        defaultDepartmentShouldBeFound("name.specified=true");

        // Get all the departmentList where name is null
        defaultDepartmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where name contains DEFAULT_NAME
        defaultDepartmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the departmentList where name contains UPDATED_NAME
        defaultDepartmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where name does not contain DEFAULT_NAME
        defaultDepartmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the departmentList where name does not contain UPDATED_NAME
        defaultDepartmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where code equals to DEFAULT_CODE
        defaultDepartmentShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the departmentList where code equals to UPDATED_CODE
        defaultDepartmentShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDepartmentShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the departmentList where code equals to UPDATED_CODE
        defaultDepartmentShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where code is not null
        defaultDepartmentShouldBeFound("code.specified=true");

        // Get all the departmentList where code is null
        defaultDepartmentShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where code contains DEFAULT_CODE
        defaultDepartmentShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the departmentList where code contains UPDATED_CODE
        defaultDepartmentShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where code does not contain DEFAULT_CODE
        defaultDepartmentShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the departmentList where code does not contain UPDATED_CODE
        defaultDepartmentShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where address equals to DEFAULT_ADDRESS
        defaultDepartmentShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the departmentList where address equals to UPDATED_ADDRESS
        defaultDepartmentShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultDepartmentShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the departmentList where address equals to UPDATED_ADDRESS
        defaultDepartmentShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where address is not null
        defaultDepartmentShouldBeFound("address.specified=true");

        // Get all the departmentList where address is null
        defaultDepartmentShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where address contains DEFAULT_ADDRESS
        defaultDepartmentShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the departmentList where address contains UPDATED_ADDRESS
        defaultDepartmentShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where address does not contain DEFAULT_ADDRESS
        defaultDepartmentShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the departmentList where address does not contain UPDATED_ADDRESS
        defaultDepartmentShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where phoneNum equals to DEFAULT_PHONE_NUM
        defaultDepartmentShouldBeFound("phoneNum.equals=" + DEFAULT_PHONE_NUM);

        // Get all the departmentList where phoneNum equals to UPDATED_PHONE_NUM
        defaultDepartmentShouldNotBeFound("phoneNum.equals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where phoneNum in DEFAULT_PHONE_NUM or UPDATED_PHONE_NUM
        defaultDepartmentShouldBeFound("phoneNum.in=" + DEFAULT_PHONE_NUM + "," + UPDATED_PHONE_NUM);

        // Get all the departmentList where phoneNum equals to UPDATED_PHONE_NUM
        defaultDepartmentShouldNotBeFound("phoneNum.in=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where phoneNum is not null
        defaultDepartmentShouldBeFound("phoneNum.specified=true");

        // Get all the departmentList where phoneNum is null
        defaultDepartmentShouldNotBeFound("phoneNum.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where phoneNum contains DEFAULT_PHONE_NUM
        defaultDepartmentShouldBeFound("phoneNum.contains=" + DEFAULT_PHONE_NUM);

        // Get all the departmentList where phoneNum contains UPDATED_PHONE_NUM
        defaultDepartmentShouldNotBeFound("phoneNum.contains=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByPhoneNumNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where phoneNum does not contain DEFAULT_PHONE_NUM
        defaultDepartmentShouldNotBeFound("phoneNum.doesNotContain=" + DEFAULT_PHONE_NUM);

        // Get all the departmentList where phoneNum does not contain UPDATED_PHONE_NUM
        defaultDepartmentShouldBeFound("phoneNum.doesNotContain=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where logo equals to DEFAULT_LOGO
        defaultDepartmentShouldBeFound("logo.equals=" + DEFAULT_LOGO);

        // Get all the departmentList where logo equals to UPDATED_LOGO
        defaultDepartmentShouldNotBeFound("logo.equals=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where logo in DEFAULT_LOGO or UPDATED_LOGO
        defaultDepartmentShouldBeFound("logo.in=" + DEFAULT_LOGO + "," + UPDATED_LOGO);

        // Get all the departmentList where logo equals to UPDATED_LOGO
        defaultDepartmentShouldNotBeFound("logo.in=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where logo is not null
        defaultDepartmentShouldBeFound("logo.specified=true");

        // Get all the departmentList where logo is null
        defaultDepartmentShouldNotBeFound("logo.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where logo contains DEFAULT_LOGO
        defaultDepartmentShouldBeFound("logo.contains=" + DEFAULT_LOGO);

        // Get all the departmentList where logo contains UPDATED_LOGO
        defaultDepartmentShouldNotBeFound("logo.contains=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLogoNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where logo does not contain DEFAULT_LOGO
        defaultDepartmentShouldNotBeFound("logo.doesNotContain=" + DEFAULT_LOGO);

        // Get all the departmentList where logo does not contain UPDATED_LOGO
        defaultDepartmentShouldBeFound("logo.doesNotContain=" + UPDATED_LOGO);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where contact equals to DEFAULT_CONTACT
        defaultDepartmentShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the departmentList where contact equals to UPDATED_CONTACT
        defaultDepartmentShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultDepartmentShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the departmentList where contact equals to UPDATED_CONTACT
        defaultDepartmentShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where contact is not null
        defaultDepartmentShouldBeFound("contact.specified=true");

        // Get all the departmentList where contact is null
        defaultDepartmentShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where contact contains DEFAULT_CONTACT
        defaultDepartmentShouldBeFound("contact.contains=" + DEFAULT_CONTACT);

        // Get all the departmentList where contact contains UPDATED_CONTACT
        defaultDepartmentShouldNotBeFound("contact.contains=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByContactNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where contact does not contain DEFAULT_CONTACT
        defaultDepartmentShouldNotBeFound("contact.doesNotContain=" + DEFAULT_CONTACT);

        // Get all the departmentList where contact does not contain UPDATED_CONTACT
        defaultDepartmentShouldBeFound("contact.doesNotContain=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId equals to DEFAULT_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.equals=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentList where createUserId equals to UPDATED_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.equals=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId in DEFAULT_CREATE_USER_ID or UPDATED_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.in=" + DEFAULT_CREATE_USER_ID + "," + UPDATED_CREATE_USER_ID);

        // Get all the departmentList where createUserId equals to UPDATED_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.in=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId is not null
        defaultDepartmentShouldBeFound("createUserId.specified=true");

        // Get all the departmentList where createUserId is null
        defaultDepartmentShouldNotBeFound("createUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId is greater than or equal to DEFAULT_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.greaterThanOrEqual=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentList where createUserId is greater than or equal to UPDATED_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.greaterThanOrEqual=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId is less than or equal to DEFAULT_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.lessThanOrEqual=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentList where createUserId is less than or equal to SMALLER_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.lessThanOrEqual=" + SMALLER_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId is less than DEFAULT_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.lessThan=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentList where createUserId is less than UPDATED_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.lessThan=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createUserId is greater than DEFAULT_CREATE_USER_ID
        defaultDepartmentShouldNotBeFound("createUserId.greaterThan=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentList where createUserId is greater than SMALLER_CREATE_USER_ID
        defaultDepartmentShouldBeFound("createUserId.greaterThan=" + SMALLER_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime equals to DEFAULT_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the departmentList where createTime equals to UPDATED_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the departmentList where createTime equals to UPDATED_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime is not null
        defaultDepartmentShouldBeFound("createTime.specified=true");

        // Get all the departmentList where createTime is null
        defaultDepartmentShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime is greater than or equal to DEFAULT_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.greaterThanOrEqual=" + DEFAULT_CREATE_TIME);

        // Get all the departmentList where createTime is greater than or equal to UPDATED_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.greaterThanOrEqual=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime is less than or equal to DEFAULT_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.lessThanOrEqual=" + DEFAULT_CREATE_TIME);

        // Get all the departmentList where createTime is less than or equal to SMALLER_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.lessThanOrEqual=" + SMALLER_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime is less than DEFAULT_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.lessThan=" + DEFAULT_CREATE_TIME);

        // Get all the departmentList where createTime is less than UPDATED_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.lessThan=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByCreateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        // Get all the departmentList where createTime is greater than DEFAULT_CREATE_TIME
        defaultDepartmentShouldNotBeFound("createTime.greaterThan=" + DEFAULT_CREATE_TIME);

        // Get all the departmentList where createTime is greater than SMALLER_CREATE_TIME
        defaultDepartmentShouldBeFound("createTime.greaterThan=" + SMALLER_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByChildrenIsEqualToSomething() throws Exception {
        Department children = DepartmentResourceIT.createEntity();
        // department.addChildren(children);
        departmentRepository.insert(department);
        Long childrenId = children.getId();
        // Get all the departmentList where children equals to childrenId
        defaultDepartmentShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the departmentList where children equals to (childrenId + 1)
        defaultDepartmentShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
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

    @Test
    @Transactional
    void getAllDepartmentsByUsersIsEqualToSomething() throws Exception {
        User users = UserResourceIT.createEntity();
        // department.addUsers(users);
        departmentRepository.insert(department);
        Long usersId = users.getId();
        // Get all the departmentList where users equals to usersId
        defaultDepartmentShouldBeFound("usersId.equals=" + usersId);

        // Get all the departmentList where users equals to (usersId + 1)
        defaultDepartmentShouldNotBeFound("usersId.equals=" + (usersId + 1));
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
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].createUserId").value(hasItem(DEFAULT_CREATE_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))));

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
        departmentRepository.save(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).orElseThrow();
        updatedDepartment
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .phoneNum(UPDATED_PHONE_NUM)
            .logo(UPDATED_LOGO)
            .contact(UPDATED_CONTACT)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME);
        DepartmentDTO departmentDTO = departmentMapper.toDto(updatedDepartment);

        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartment.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDepartment.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testDepartment.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDepartment.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testDepartment.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testDepartment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment.code(UPDATED_CODE).createTime(UPDATED_CREATE_TIME);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartment.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDepartment.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testDepartment.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDepartment.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testDepartment.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testDepartment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentWithPatch() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department using partial update
        Department partialUpdatedDepartment = new Department();
        partialUpdatedDepartment.setId(department.getId());

        partialUpdatedDepartment
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .address(UPDATED_ADDRESS)
            .phoneNum(UPDATED_PHONE_NUM)
            .logo(UPDATED_LOGO)
            .contact(UPDATED_CONTACT)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME);

        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartment))
            )
            .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartment.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testDepartment.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testDepartment.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDepartment.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testDepartment.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testDepartment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();
        department.setId(longCount.incrementAndGet());

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartment() throws Exception {
        // Initialize the database
        departmentRepository.save(department);

        int databaseSizeBeforeDelete = departmentRepository.findAll().size();

        // Delete the department
        restDepartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, department.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
