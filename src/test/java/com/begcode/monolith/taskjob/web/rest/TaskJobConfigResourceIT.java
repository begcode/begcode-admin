package com.begcode.monolith.taskjob.web.rest;

import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.JobStatus;
import com.begcode.monolith.taskjob.domain.TaskJobConfig;
import com.begcode.monolith.taskjob.repository.TaskJobConfigRepository;
import com.begcode.monolith.taskjob.service.dto.TaskJobConfigDTO;
import com.begcode.monolith.taskjob.service.mapper.TaskJobConfigMapper;
import com.begcode.monolith.web.rest.TestUtil;
import com.begcode.monolith.web.rest.TestUtil;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TaskJobConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
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

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;
    private static final Long SMALLER_LAST_MODIFIED_BY = 1L - 1L;

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/task-job-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        int databaseSizeBeforeCreate = taskJobConfigRepository.findAll().size();
        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);
        restTaskJobConfigMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeCreate + 1);
        TaskJobConfig testTaskJobConfig = taskJobConfigList.get(taskJobConfigList.size() - 1);
        assertThat(testTaskJobConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaskJobConfig.getJobClassName()).isEqualTo(DEFAULT_JOB_CLASS_NAME);
        assertThat(testTaskJobConfig.getCronExpression()).isEqualTo(DEFAULT_CRON_EXPRESSION);
        assertThat(testTaskJobConfig.getParameter()).isEqualTo(DEFAULT_PARAMETER);
        assertThat(testTaskJobConfig.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTaskJobConfig.getJobStatus()).isEqualTo(DEFAULT_JOB_STATUS);
        assertThat(testTaskJobConfig.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTaskJobConfig.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTaskJobConfig.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTaskJobConfig.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTaskJobConfigWithExistingId() throws Exception {
        // Create the TaskJobConfig with an existing ID
        taskJobConfig.setId(1L);
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        int databaseSizeBeforeCreate = taskJobConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskJobConfigMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeCreate);
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
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
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
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getTaskJobConfigsByIdFiltering() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        Long id = taskJobConfig.getId();

        defaultTaskJobConfigShouldBeFound("id.equals=" + id);
        defaultTaskJobConfigShouldNotBeFound("id.notEquals=" + id);

        defaultTaskJobConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskJobConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskJobConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskJobConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name equals to DEFAULT_NAME
        defaultTaskJobConfigShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the taskJobConfigList where name equals to UPDATED_NAME
        defaultTaskJobConfigShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTaskJobConfigShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the taskJobConfigList where name equals to UPDATED_NAME
        defaultTaskJobConfigShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name is not null
        defaultTaskJobConfigShouldBeFound("name.specified=true");

        // Get all the taskJobConfigList where name is null
        defaultTaskJobConfigShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name contains DEFAULT_NAME
        defaultTaskJobConfigShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the taskJobConfigList where name contains UPDATED_NAME
        defaultTaskJobConfigShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where name does not contain DEFAULT_NAME
        defaultTaskJobConfigShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the taskJobConfigList where name does not contain UPDATED_NAME
        defaultTaskJobConfigShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName equals to DEFAULT_JOB_CLASS_NAME
        defaultTaskJobConfigShouldBeFound("jobClassName.equals=" + DEFAULT_JOB_CLASS_NAME);

        // Get all the taskJobConfigList where jobClassName equals to UPDATED_JOB_CLASS_NAME
        defaultTaskJobConfigShouldNotBeFound("jobClassName.equals=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName in DEFAULT_JOB_CLASS_NAME or UPDATED_JOB_CLASS_NAME
        defaultTaskJobConfigShouldBeFound("jobClassName.in=" + DEFAULT_JOB_CLASS_NAME + "," + UPDATED_JOB_CLASS_NAME);

        // Get all the taskJobConfigList where jobClassName equals to UPDATED_JOB_CLASS_NAME
        defaultTaskJobConfigShouldNotBeFound("jobClassName.in=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName is not null
        defaultTaskJobConfigShouldBeFound("jobClassName.specified=true");

        // Get all the taskJobConfigList where jobClassName is null
        defaultTaskJobConfigShouldNotBeFound("jobClassName.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName contains DEFAULT_JOB_CLASS_NAME
        defaultTaskJobConfigShouldBeFound("jobClassName.contains=" + DEFAULT_JOB_CLASS_NAME);

        // Get all the taskJobConfigList where jobClassName contains UPDATED_JOB_CLASS_NAME
        defaultTaskJobConfigShouldNotBeFound("jobClassName.contains=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobClassNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobClassName does not contain DEFAULT_JOB_CLASS_NAME
        defaultTaskJobConfigShouldNotBeFound("jobClassName.doesNotContain=" + DEFAULT_JOB_CLASS_NAME);

        // Get all the taskJobConfigList where jobClassName does not contain UPDATED_JOB_CLASS_NAME
        defaultTaskJobConfigShouldBeFound("jobClassName.doesNotContain=" + UPDATED_JOB_CLASS_NAME);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression equals to DEFAULT_CRON_EXPRESSION
        defaultTaskJobConfigShouldBeFound("cronExpression.equals=" + DEFAULT_CRON_EXPRESSION);

        // Get all the taskJobConfigList where cronExpression equals to UPDATED_CRON_EXPRESSION
        defaultTaskJobConfigShouldNotBeFound("cronExpression.equals=" + UPDATED_CRON_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression in DEFAULT_CRON_EXPRESSION or UPDATED_CRON_EXPRESSION
        defaultTaskJobConfigShouldBeFound("cronExpression.in=" + DEFAULT_CRON_EXPRESSION + "," + UPDATED_CRON_EXPRESSION);

        // Get all the taskJobConfigList where cronExpression equals to UPDATED_CRON_EXPRESSION
        defaultTaskJobConfigShouldNotBeFound("cronExpression.in=" + UPDATED_CRON_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression is not null
        defaultTaskJobConfigShouldBeFound("cronExpression.specified=true");

        // Get all the taskJobConfigList where cronExpression is null
        defaultTaskJobConfigShouldNotBeFound("cronExpression.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression contains DEFAULT_CRON_EXPRESSION
        defaultTaskJobConfigShouldBeFound("cronExpression.contains=" + DEFAULT_CRON_EXPRESSION);

        // Get all the taskJobConfigList where cronExpression contains UPDATED_CRON_EXPRESSION
        defaultTaskJobConfigShouldNotBeFound("cronExpression.contains=" + UPDATED_CRON_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCronExpressionNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where cronExpression does not contain DEFAULT_CRON_EXPRESSION
        defaultTaskJobConfigShouldNotBeFound("cronExpression.doesNotContain=" + DEFAULT_CRON_EXPRESSION);

        // Get all the taskJobConfigList where cronExpression does not contain UPDATED_CRON_EXPRESSION
        defaultTaskJobConfigShouldBeFound("cronExpression.doesNotContain=" + UPDATED_CRON_EXPRESSION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter equals to DEFAULT_PARAMETER
        defaultTaskJobConfigShouldBeFound("parameter.equals=" + DEFAULT_PARAMETER);

        // Get all the taskJobConfigList where parameter equals to UPDATED_PARAMETER
        defaultTaskJobConfigShouldNotBeFound("parameter.equals=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter in DEFAULT_PARAMETER or UPDATED_PARAMETER
        defaultTaskJobConfigShouldBeFound("parameter.in=" + DEFAULT_PARAMETER + "," + UPDATED_PARAMETER);

        // Get all the taskJobConfigList where parameter equals to UPDATED_PARAMETER
        defaultTaskJobConfigShouldNotBeFound("parameter.in=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter is not null
        defaultTaskJobConfigShouldBeFound("parameter.specified=true");

        // Get all the taskJobConfigList where parameter is null
        defaultTaskJobConfigShouldNotBeFound("parameter.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter contains DEFAULT_PARAMETER
        defaultTaskJobConfigShouldBeFound("parameter.contains=" + DEFAULT_PARAMETER);

        // Get all the taskJobConfigList where parameter contains UPDATED_PARAMETER
        defaultTaskJobConfigShouldNotBeFound("parameter.contains=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByParameterNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where parameter does not contain DEFAULT_PARAMETER
        defaultTaskJobConfigShouldNotBeFound("parameter.doesNotContain=" + DEFAULT_PARAMETER);

        // Get all the taskJobConfigList where parameter does not contain UPDATED_PARAMETER
        defaultTaskJobConfigShouldBeFound("parameter.doesNotContain=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description equals to DEFAULT_DESCRIPTION
        defaultTaskJobConfigShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the taskJobConfigList where description equals to UPDATED_DESCRIPTION
        defaultTaskJobConfigShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTaskJobConfigShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the taskJobConfigList where description equals to UPDATED_DESCRIPTION
        defaultTaskJobConfigShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description is not null
        defaultTaskJobConfigShouldBeFound("description.specified=true");

        // Get all the taskJobConfigList where description is null
        defaultTaskJobConfigShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description contains DEFAULT_DESCRIPTION
        defaultTaskJobConfigShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the taskJobConfigList where description contains UPDATED_DESCRIPTION
        defaultTaskJobConfigShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where description does not contain DEFAULT_DESCRIPTION
        defaultTaskJobConfigShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the taskJobConfigList where description does not contain UPDATED_DESCRIPTION
        defaultTaskJobConfigShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus equals to DEFAULT_JOB_STATUS
        defaultTaskJobConfigShouldBeFound("jobStatus.equals=" + DEFAULT_JOB_STATUS);

        // Get all the taskJobConfigList where jobStatus equals to UPDATED_JOB_STATUS
        defaultTaskJobConfigShouldNotBeFound("jobStatus.equals=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus in DEFAULT_JOB_STATUS or UPDATED_JOB_STATUS
        defaultTaskJobConfigShouldBeFound("jobStatus.in=" + DEFAULT_JOB_STATUS + "," + UPDATED_JOB_STATUS);

        // Get all the taskJobConfigList where jobStatus equals to UPDATED_JOB_STATUS
        defaultTaskJobConfigShouldNotBeFound("jobStatus.in=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByJobStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where jobStatus is not null
        defaultTaskJobConfigShouldBeFound("jobStatus.specified=true");

        // Get all the taskJobConfigList where jobStatus is null
        defaultTaskJobConfigShouldNotBeFound("jobStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy equals to DEFAULT_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the taskJobConfigList where createdBy equals to UPDATED_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the taskJobConfigList where createdBy equals to UPDATED_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is not null
        defaultTaskJobConfigShouldBeFound("createdBy.specified=true");

        // Get all the taskJobConfigList where createdBy is null
        defaultTaskJobConfigShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the taskJobConfigList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the taskJobConfigList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is less than DEFAULT_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the taskJobConfigList where createdBy is less than UPDATED_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTaskJobConfigShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the taskJobConfigList where createdBy is greater than SMALLER_CREATED_BY
        defaultTaskJobConfigShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate equals to UPDATED_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate equals to UPDATED_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is not null
        defaultTaskJobConfigShouldBeFound("createdDate.specified=true");

        // Get all the taskJobConfigList where createdDate is null
        defaultTaskJobConfigShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is less than DEFAULT_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate is less than UPDATED_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultTaskJobConfigShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the taskJobConfigList where createdDate is greater than SMALLER_CREATED_DATE
        defaultTaskJobConfigShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is not null
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.specified=true");

        // Get all the taskJobConfigList where lastModifiedBy is null
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is greater than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy is greater than or equal to UPDATED_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is less than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy is less than or equal to SMALLER_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is less than DEFAULT_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy is less than UPDATED_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedBy is greater than DEFAULT_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldNotBeFound("lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the taskJobConfigList where lastModifiedBy is greater than SMALLER_LAST_MODIFIED_BY
        defaultTaskJobConfigShouldBeFound("lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is not null
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.specified=true");

        // Get all the taskJobConfigList where lastModifiedDate is null
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTaskJobConfigsByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        // Get all the taskJobConfigList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the taskJobConfigList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultTaskJobConfigShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
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
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

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

        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
        TaskJobConfig testTaskJobConfig = taskJobConfigList.get(taskJobConfigList.size() - 1);
        assertThat(testTaskJobConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskJobConfig.getJobClassName()).isEqualTo(UPDATED_JOB_CLASS_NAME);
        assertThat(testTaskJobConfig.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testTaskJobConfig.getParameter()).isEqualTo(UPDATED_PARAMETER);
        assertThat(testTaskJobConfig.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTaskJobConfig.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testTaskJobConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaskJobConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaskJobConfig.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaskJobConfig.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskJobConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskJobConfigWithPatch() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();

        // Update the taskJobConfig using partial update
        TaskJobConfig partialUpdatedTaskJobConfig = new TaskJobConfig();
        partialUpdatedTaskJobConfig.setId(taskJobConfig.getId());

        partialUpdatedTaskJobConfig
            .name(UPDATED_NAME)
            .jobClassName(UPDATED_JOB_CLASS_NAME)
            .cronExpression(UPDATED_CRON_EXPRESSION)
            .parameter(UPDATED_PARAMETER)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY);

        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskJobConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskJobConfig))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
        TaskJobConfig testTaskJobConfig = taskJobConfigList.get(taskJobConfigList.size() - 1);
        assertThat(testTaskJobConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskJobConfig.getJobClassName()).isEqualTo(UPDATED_JOB_CLASS_NAME);
        assertThat(testTaskJobConfig.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testTaskJobConfig.getParameter()).isEqualTo(UPDATED_PARAMETER);
        assertThat(testTaskJobConfig.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTaskJobConfig.getJobStatus()).isEqualTo(DEFAULT_JOB_STATUS);
        assertThat(testTaskJobConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaskJobConfig.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTaskJobConfig.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTaskJobConfig.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTaskJobConfigWithPatch() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskJobConfig))
            )
            .andExpect(status().isOk());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
        TaskJobConfig testTaskJobConfig = taskJobConfigList.get(taskJobConfigList.size() - 1);
        assertThat(testTaskJobConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaskJobConfig.getJobClassName()).isEqualTo(UPDATED_JOB_CLASS_NAME);
        assertThat(testTaskJobConfig.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testTaskJobConfig.getParameter()).isEqualTo(UPDATED_PARAMETER);
        assertThat(testTaskJobConfig.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTaskJobConfig.getJobStatus()).isEqualTo(UPDATED_JOB_STATUS);
        assertThat(testTaskJobConfig.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaskJobConfig.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaskJobConfig.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaskJobConfig.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskJobConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskJobConfigRepository.findAll().size();
        taskJobConfig.setId(longCount.incrementAndGet());

        // Create the TaskJobConfig
        TaskJobConfigDTO taskJobConfigDTO = taskJobConfigMapper.toDto(taskJobConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskJobConfigMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskJobConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskJobConfig in the database
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskJobConfig() throws Exception {
        // Initialize the database
        taskJobConfigRepository.save(taskJobConfig);

        int databaseSizeBeforeDelete = taskJobConfigRepository.findAll().size();

        // Delete the taskJobConfig
        restTaskJobConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskJobConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskJobConfig> taskJobConfigList = taskJobConfigRepository.findAll();
        assertThat(taskJobConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
