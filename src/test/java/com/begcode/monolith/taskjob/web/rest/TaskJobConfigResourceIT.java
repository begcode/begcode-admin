package com.begcode.monolith.taskjob.web.rest;

import static com.begcode.monolith.taskjob.domain.TaskJobConfigAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.JobStatus;
import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.dto.TaskJobConfigDTO;
import com.begcode.monolith.taskjob.service.mapper.TaskJobConfigMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TaskJobConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class TaskJobConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CRON_EXPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_CRON_EXPRESSION = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETER = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final JobStatus DEFAULT_JOB_STATUS = JobStatus.NORMAL;
    private static final JobStatus UPDATED_JOB_STATUS = JobStatus.PAUSED;

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

    private static final String ENTITY_API_URL = "/api/task-job-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskJobConfigRepository taskJobConfigRepository;

    @Autowired
    private TaskJobConfigMapper taskJobConfigMapper;

    @Autowired
    private MockMvc restTaskJobConfigMockMvc;

    private TaskJobConfig taskJobConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskJobConfig createEntity() {
        TaskJobConfig taskJobConfig = new TaskJobConfig()
            .name(DEFAULT_NAME)
            .jobClassName(DEFAULT_JOB_CLASS_NAME)
            .cronExpression(DEFAULT_CRON_EXPRESSION)
            .parameter(DEFAULT_PARAMETER)
            .description(DEFAULT_DESCRIPTION)
            .jobStatus(DEFAULT_JOB_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return taskJobConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskJobConfig createUpdatedEntity() {
        TaskJobConfig taskJobConfig = new TaskJobConfig()
            .name(UPDATED_NAME)
            .jobClassName(UPDATED_JOB_CLASS_NAME)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .parameter(UPDATED_PARAMETER)
            .description(UPDATED_DESCRIPTION)
            .jobStatus(UPDATED_JOB_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return taskJobConfig;
    }

    @BeforeEach
    public void initTest() {
        taskJobConfig = createEntity();
    }

    @Test
    @Transactional
    void createTaskJobConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);
        var returnedTaskJobConfigDTO = om.readValue(
            restTaskJobConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskJobConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskJobConfigDTO.class
        );

        // Validate the TaskJobConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTaskJobConfig = taskJobConfigMapper.toEntity(returnedTaskJobConfigDTO);
        assertTaskJobConfigUpdatableFieldsEquals(returnedTaskJobConfig, getPersistedTaskJobConfig(returnedTaskJobConfig));
    }

    @Test
    @Transactional
    void createTaskJobConfigWithExistingId() throws Exception {
        // Create the TaskJobConfig with an existing ID
        taskJobConfig.setId(1L);
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskJobConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskJobConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigs() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskJobConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].jobClassName").value(hasItem(DEFAULT_JOB_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION)))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTaskJobConfig() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get the taskJobConfig
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, taskJobConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskJobConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.jobClassName").value(DEFAULT_JOB_CLASS_NAME))
            .andExpect(jsonPath("$.cronExpression").value(DEFAULT_CRON_EXPRESSION))
            .andExpect(jsonPath("$.parameter").value(DEFAULT_PARAMETER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.jobStatus").value(DEFAULT_JOB_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTaskJobConfigsByIdFiltering() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        Long id = taskJobConfig.getId();

        defaultTaskJobConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTaskJobConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTaskJobConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name equals to
        defaultTaskJobConfigFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name in
        defaultTaskJobConfigFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name is not null
        defaultTaskJobConfigFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name contains
        defaultTaskJobConfigFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name does not contain
        defaultTaskJobConfigFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName equals to
        defaultTaskJobConfigFiltering("jobClassName.equals=" + DEFAULT_JOB_CLASS_NAME, "jobClassName.equals=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName in
        defaultTaskJobConfigFiltering(
            "jobClassName.in=" + DEFAULT_JOB_CLASS_NAME + "," + UPDATED_JOB_CLASS_NAME,
            "jobClassName.in=" + UPDATED_JOB_CLASS_NAME
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName is not null
        defaultTaskJobConfigFiltering("jobClassName.specified=true", "jobClassName.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName contains
        defaultTaskJobConfigFiltering("jobClassName.contains=" + DEFAULT_JOB_CLASS_NAME, "jobClassName.contains=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName does not contain
        defaultTaskJobConfigFiltering(
            "jobClassName.doesNotContain=" + UPDATED_JOB_CLASS_NAME,
            "jobClassName.doesNotContain=" + DEFAULT_JOB_CLASS_NAME
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression equals to
        defaultTaskJobConfigFiltering(
            "cronExpression.equals=" + DEFAULT_CRON_EXPRESSION,
            "cronExpression.equals=" + UPDATED_CRON_EXPRESSION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression in
        defaultTaskJobConfigFiltering(
            "cronExpression.in=" + DEFAULT_CRON_EXPRESSION + "," + UPDATED_CRON_EXPRESSION,
            "cronExpression.in=" + UPDATED_CRON_EXPRESSION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression is not null
        defaultTaskJobConfigFiltering("cronExpression.specified=true", "cronExpression.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression contains
        defaultTaskJobConfigFiltering(
            "cronExpression.contains=" + DEFAULT_CRON_EXPRESSION,
            "cronExpression.contains=" + UPDATED_CRON_EXPRESSION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression does not contain
        defaultTaskJobConfigFiltering(
            "cronExpression.doesNotContain=" + UPDATED_CRON_EXPRESSION,
            "cronExpression.doesNotContain=" + DEFAULT_CRON_EXPRESSION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter equals to
        defaultTaskJobConfigFiltering("parameter.equals=" + DEFAULT_PARAMETER, "parameter.equals=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter in
        defaultTaskJobConfigFiltering("parameter.in=" + DEFAULT_PARAMETER + "," + UPDATED_PARAMETER, "parameter.in=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter is not null
        defaultTaskJobConfigFiltering("parameter.specified=true", "parameter.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter contains
        defaultTaskJobConfigFiltering("parameter.contains=" + DEFAULT_PARAMETER, "parameter.contains=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter does not contain
        defaultTaskJobConfigFiltering("parameter.doesNotContain=" + UPDATED_PARAMETER, "parameter.doesNotContain=" + DEFAULT_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description equals to
        defaultTaskJobConfigFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description in
        defaultTaskJobConfigFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description is not null
        defaultTaskJobConfigFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description contains
        defaultTaskJobConfigFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description does not contain
        defaultTaskJobConfigFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus equals to
        defaultTaskJobConfigFiltering("jobStatus.equals=" + DEFAULT_JOB_STATUS, "jobStatus.equals=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus in
        defaultTaskJobConfigFiltering(
            "jobStatus.in=" + DEFAULT_JOB_STATUS + "," + UPDATED_JOB_STATUS,
            "jobStatus.in=" + UPDATED_JOB_STATUS
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus is not null
        defaultTaskJobConfigFiltering("jobStatus.specified=true", "jobStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy equals to
        defaultTaskJobConfigFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy in
        defaultTaskJobConfigFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is not null
        defaultTaskJobConfigFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is greater than or equal to
        defaultTaskJobConfigFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is less than or equal to
        defaultTaskJobConfigFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is less than
        defaultTaskJobConfigFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is greater than
        defaultTaskJobConfigFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate equals to
        defaultTaskJobConfigFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate in
        defaultTaskJobConfigFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is not null
        defaultTaskJobConfigFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy equals to
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy in
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is not null
        defaultTaskJobConfigFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is greater than or equal to
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is less than or equal to
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is less than
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is greater than
        defaultTaskJobConfigFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate equals to
        defaultTaskJobConfigFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate in
        defaultTaskJobConfigFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is not null
        defaultTaskJobConfigFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultTaskJobConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTaskJobConfigShouldBeFound(shouldBeFound);
        defaultTaskJobConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskJobConfigShouldBeFound(String filter) throws Exception {
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskJobConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].jobClassName").value(hasItem(DEFAULT_JOB_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION)))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskJobConfigShouldNotBeFound(String filter) throws Exception {
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskJobConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaskJobConfig() throws Exception {
        // Get the taskJobConfig
        restTaskJobConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaskJobConfig() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskJobConfig
        TaskJobConfig updatedTaskJobConfig = taskJobConfigRepository.findById(taskJobConfig.getId()).orElseThrow();
        updatedTaskJobConfig
            .name(UPDATED_NAME)
            .jobClassName(UPDATED_JOB_CLASS_NAME)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .parameter(UPDATED_PARAMETER)
            .description(UPDATED_DESCRIPTION)
            .jobStatus(UPDATED_JOB_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(updatedTaskJobConfig);

        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskJobConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskJobConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskJobConfigToMatchAllProperties(updatedTaskJobConfig);
    }

    @Test
    @Transactional
    void putNonExistingTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskJobConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskJobConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskJobConfigWithPatch() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskJobConfig using partial update
        TaskJobConfig partialUpdatedTaskJobConfig = new TaskJobConfig();
        partialUpdatedTaskJobConfig.setId(taskJobConfig.getId());

        partialUpdatedTaskJobConfig
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskJobConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskJobConfig))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskJobConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaskJobConfig, taskJobConfig),
            getPersistedTaskJobConfig(taskJobConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaskJobConfigWithPatch() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskJobConfig using partial update
        TaskJobConfig partialUpdatedTaskJobConfig = new TaskJobConfig();
        partialUpdatedTaskJobConfig.setId(taskJobConfig.getId());

        partialUpdatedTaskJobConfig
            .name(UPDATED_NAME)
            .jobClassName(UPDATED_JOB_CLASS_NAME)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .parameter(UPDATED_PARAMETER)
            .description(UPDATED_DESCRIPTION)
            .jobStatus(UPDATED_JOB_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskJobConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskJobConfig))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskJobConfigUpdatableFieldsEquals(partialUpdatedTaskJobConfig, getPersistedTaskJobConfig(partialUpdatedTaskJobConfig));
    }

    @Test
    @Transactional
    void patchNonExistingTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskJobConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskJobConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskJobConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskJobConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskJobConfig() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taskJobConfig
        restTaskJobConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskJobConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskJobConfigRepository.selectCount(null);
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

    protected TaskJobConfig getPersistedTaskJobConfig(TaskJobConfig taskJobConfig) {
        return taskJobConfigRepository.findById(taskJobConfig.getId()).orElseThrow();
    }

    protected void assertPersistedTaskJobConfigToMatchAllProperties(TaskJobConfig expectedTaskJobConfig) {
        assertTaskJobConfigAllPropertiesEquals(expectedTaskJobConfig, getPersistedTaskJobConfig(expectedTaskJobConfig));
    }

    protected void assertPersistedTaskJobConfigToMatchUpdatableProperties(TaskJobConfig expectedTaskJobConfig) {
        assertTaskJobConfigAllUpdatablePropertiesEquals(expectedTaskJobConfig, getPersistedTaskJobConfig(expectedTaskJobConfig));
    }
}
