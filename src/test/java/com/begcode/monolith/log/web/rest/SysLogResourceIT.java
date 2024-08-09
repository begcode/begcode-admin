package com.begcode.monolith.log.web.rest;

import static com.begcode.monolith.log.domain.SysLogAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.LogType;
import com.begcode.monolith.domain.enumeration.OperateType;
import com.begcode.monolith.log.domain.SysLog;
import com.begcode.monolith.log.repository.SysLogRepository;
import com.begcode.monolith.log.service.dto.SysLogDTO;
import com.begcode.monolith.log.service.mapper.SysLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SysLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class SysLogResourceIT {

    private static final LogType DEFAULT_LOG_TYPE = LogType.LOGIN;
    private static final LogType UPDATED_LOG_TYPE = LogType.OPERATE;

    private static final String DEFAULT_LOG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_LOG_CONTENT = "BBBBBBBBBB";

    private static final OperateType DEFAULT_OPERATE_TYPE = OperateType.LIST;
    private static final OperateType UPDATED_OPERATE_TYPE = OperateType.ADD;

    private static final String DEFAULT_USERID = "AAAAAAAAAA";
    private static final String UPDATED_USERID = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_URL = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_PARAM = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_PARAM = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_COST_TIME = 1L;
    private static final Long UPDATED_COST_TIME = 2L;
    private static final Long SMALLER_COST_TIME = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/sys-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SysLogRepository sysLogRepository;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private MockMvc restSysLogMockMvc;

    private SysLog sysLog;

    private SysLog insertedSysLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysLog createEntity() {
        SysLog sysLog = new SysLog()
            .logType(DEFAULT_LOG_TYPE)
            .logContent(DEFAULT_LOG_CONTENT)
            .operateType(DEFAULT_OPERATE_TYPE)
            .userid(DEFAULT_USERID)
            .username(DEFAULT_USERNAME)
            .ip(DEFAULT_IP)
            .method(DEFAULT_METHOD)
            .requestUrl(DEFAULT_REQUEST_URL)
            .requestParam(DEFAULT_REQUEST_PARAM)
            .requestType(DEFAULT_REQUEST_TYPE)
            .costTime(DEFAULT_COST_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return sysLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysLog createUpdatedEntity() {
        SysLog sysLog = new SysLog()
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return sysLog;
    }

    @BeforeEach
    public void initTest() {
        sysLog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSysLog != null) {
            sysLogRepository.deleteById(insertedSysLog.getId());
            insertedSysLog = null;
        }
    }

    @Test
    @Transactional
    void createSysLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);
        var returnedSysLogDTO = om.readValue(
            restSysLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SysLogDTO.class
        );

        // Validate the SysLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSysLog = sysLogMapper.toEntity(returnedSysLogDTO);
        assertSysLogUpdatableFieldsEquals(returnedSysLog, getPersistedSysLog(returnedSysLog));

        insertedSysLog = returnedSysLog;
    }

    @Test
    @Transactional
    void createSysLogWithExistingId() throws Exception {
        // Create the SysLog with an existing ID
        sysLog.setId(1L);
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysLogs() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].logContent").value(hasItem(DEFAULT_LOG_CONTENT)))
            .andExpect(jsonPath("$.[*].operateType").value(hasItem(DEFAULT_OPERATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].requestUrl").value(hasItem(DEFAULT_REQUEST_URL)))
            .andExpect(jsonPath("$.[*].requestParam").value(hasItem(DEFAULT_REQUEST_PARAM.toString())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].costTime").value(hasItem(DEFAULT_COST_TIME.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSysLog() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get the sysLog
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL_ID, sysLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysLog.getId().intValue()))
            .andExpect(jsonPath("$.logType").value(DEFAULT_LOG_TYPE.toString()))
            .andExpect(jsonPath("$.logContent").value(DEFAULT_LOG_CONTENT))
            .andExpect(jsonPath("$.operateType").value(DEFAULT_OPERATE_TYPE.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.requestUrl").value(DEFAULT_REQUEST_URL))
            .andExpect(jsonPath("$.requestParam").value(DEFAULT_REQUEST_PARAM.toString()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.costTime").value(DEFAULT_COST_TIME.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSysLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        Long id = sysLog.getId();

        defaultSysLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSysLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSysLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logType equals to
        defaultSysLogFiltering("logType.equals=" + DEFAULT_LOG_TYPE, "logType.equals=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logType in
        defaultSysLogFiltering("logType.in=" + DEFAULT_LOG_TYPE + "," + UPDATED_LOG_TYPE, "logType.in=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logType is not null
        defaultSysLogFiltering("logType.specified=true", "logType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logContent equals to
        defaultSysLogFiltering("logContent.equals=" + DEFAULT_LOG_CONTENT, "logContent.equals=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logContent in
        defaultSysLogFiltering("logContent.in=" + DEFAULT_LOG_CONTENT + "," + UPDATED_LOG_CONTENT, "logContent.in=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logContent is not null
        defaultSysLogFiltering("logContent.specified=true", "logContent.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logContent contains
        defaultSysLogFiltering("logContent.contains=" + DEFAULT_LOG_CONTENT, "logContent.contains=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where logContent does not contain
        defaultSysLogFiltering("logContent.doesNotContain=" + UPDATED_LOG_CONTENT, "logContent.doesNotContain=" + DEFAULT_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where operateType equals to
        defaultSysLogFiltering("operateType.equals=" + DEFAULT_OPERATE_TYPE, "operateType.equals=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where operateType in
        defaultSysLogFiltering(
            "operateType.in=" + DEFAULT_OPERATE_TYPE + "," + UPDATED_OPERATE_TYPE,
            "operateType.in=" + UPDATED_OPERATE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where operateType is not null
        defaultSysLogFiltering("operateType.specified=true", "operateType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where userid equals to
        defaultSysLogFiltering("userid.equals=" + DEFAULT_USERID, "userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where userid in
        defaultSysLogFiltering("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID, "userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where userid is not null
        defaultSysLogFiltering("userid.specified=true", "userid.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where userid contains
        defaultSysLogFiltering("userid.contains=" + DEFAULT_USERID, "userid.contains=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where userid does not contain
        defaultSysLogFiltering("userid.doesNotContain=" + UPDATED_USERID, "userid.doesNotContain=" + DEFAULT_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where username equals to
        defaultSysLogFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where username in
        defaultSysLogFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where username is not null
        defaultSysLogFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where username contains
        defaultSysLogFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where username does not contain
        defaultSysLogFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where ip equals to
        defaultSysLogFiltering("ip.equals=" + DEFAULT_IP, "ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where ip in
        defaultSysLogFiltering("ip.in=" + DEFAULT_IP + "," + UPDATED_IP, "ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where ip is not null
        defaultSysLogFiltering("ip.specified=true", "ip.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByIpContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where ip contains
        defaultSysLogFiltering("ip.contains=" + DEFAULT_IP, "ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where ip does not contain
        defaultSysLogFiltering("ip.doesNotContain=" + UPDATED_IP, "ip.doesNotContain=" + DEFAULT_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where method equals to
        defaultSysLogFiltering("method.equals=" + DEFAULT_METHOD, "method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where method in
        defaultSysLogFiltering("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD, "method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where method is not null
        defaultSysLogFiltering("method.specified=true", "method.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where method contains
        defaultSysLogFiltering("method.contains=" + DEFAULT_METHOD, "method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where method does not contain
        defaultSysLogFiltering("method.doesNotContain=" + UPDATED_METHOD, "method.doesNotContain=" + DEFAULT_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestUrl equals to
        defaultSysLogFiltering("requestUrl.equals=" + DEFAULT_REQUEST_URL, "requestUrl.equals=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestUrl in
        defaultSysLogFiltering("requestUrl.in=" + DEFAULT_REQUEST_URL + "," + UPDATED_REQUEST_URL, "requestUrl.in=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestUrl is not null
        defaultSysLogFiltering("requestUrl.specified=true", "requestUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestUrl contains
        defaultSysLogFiltering("requestUrl.contains=" + DEFAULT_REQUEST_URL, "requestUrl.contains=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestUrl does not contain
        defaultSysLogFiltering("requestUrl.doesNotContain=" + UPDATED_REQUEST_URL, "requestUrl.doesNotContain=" + DEFAULT_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestType equals to
        defaultSysLogFiltering("requestType.equals=" + DEFAULT_REQUEST_TYPE, "requestType.equals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestType in
        defaultSysLogFiltering(
            "requestType.in=" + DEFAULT_REQUEST_TYPE + "," + UPDATED_REQUEST_TYPE,
            "requestType.in=" + UPDATED_REQUEST_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestType is not null
        defaultSysLogFiltering("requestType.specified=true", "requestType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestType contains
        defaultSysLogFiltering("requestType.contains=" + DEFAULT_REQUEST_TYPE, "requestType.contains=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where requestType does not contain
        defaultSysLogFiltering("requestType.doesNotContain=" + UPDATED_REQUEST_TYPE, "requestType.doesNotContain=" + DEFAULT_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime equals to
        defaultSysLogFiltering("costTime.equals=" + DEFAULT_COST_TIME, "costTime.equals=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime in
        defaultSysLogFiltering("costTime.in=" + DEFAULT_COST_TIME + "," + UPDATED_COST_TIME, "costTime.in=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime is not null
        defaultSysLogFiltering("costTime.specified=true", "costTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime is greater than or equal to
        defaultSysLogFiltering("costTime.greaterThanOrEqual=" + DEFAULT_COST_TIME, "costTime.greaterThanOrEqual=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime is less than or equal to
        defaultSysLogFiltering("costTime.lessThanOrEqual=" + DEFAULT_COST_TIME, "costTime.lessThanOrEqual=" + SMALLER_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime is less than
        defaultSysLogFiltering("costTime.lessThan=" + UPDATED_COST_TIME, "costTime.lessThan=" + DEFAULT_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where costTime is greater than
        defaultSysLogFiltering("costTime.greaterThan=" + SMALLER_COST_TIME, "costTime.greaterThan=" + DEFAULT_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy equals to
        defaultSysLogFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy in
        defaultSysLogFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy is not null
        defaultSysLogFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy is greater than or equal to
        defaultSysLogFiltering("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy is less than or equal to
        defaultSysLogFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy is less than
        defaultSysLogFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdBy is greater than
        defaultSysLogFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdDate equals to
        defaultSysLogFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdDate in
        defaultSysLogFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where createdDate is not null
        defaultSysLogFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy equals to
        defaultSysLogFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy in
        defaultSysLogFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy is not null
        defaultSysLogFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy is greater than or equal to
        defaultSysLogFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy is less than or equal to
        defaultSysLogFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy is less than
        defaultSysLogFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedBy is greater than
        defaultSysLogFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedDate equals to
        defaultSysLogFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedDate in
        defaultSysLogFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSysLogsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        // Get all the sysLogList where lastModifiedDate is not null
        defaultSysLogFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultSysLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSysLogShouldBeFound(shouldBeFound);
        defaultSysLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSysLogShouldBeFound(String filter) throws Exception {
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].logContent").value(hasItem(DEFAULT_LOG_CONTENT)))
            .andExpect(jsonPath("$.[*].operateType").value(hasItem(DEFAULT_OPERATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].requestUrl").value(hasItem(DEFAULT_REQUEST_URL)))
            .andExpect(jsonPath("$.[*].requestParam").value(hasItem(DEFAULT_REQUEST_PARAM.toString())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].costTime").value(hasItem(DEFAULT_COST_TIME.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSysLogShouldNotBeFound(String filter) throws Exception {
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSysLog() throws Exception {
        // Get the sysLog
        restSysLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSysLog() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysLog
        SysLog updatedSysLog = sysLogRepository.findById(sysLog.getId()).orElseThrow();
        updatedSysLog
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SysLogDTO sysLogDTO = sysLogMapper.toDto(updatedSysLog);

        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysLogDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSysLogToMatchAllProperties(updatedSysLog);
    }

    @Test
    @Transactional
    void putNonExistingSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysLogDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sysLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysLogWithPatch() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysLog using partial update
        SysLog partialUpdatedSysLog = new SysLog();
        partialUpdatedSysLog.setId(sysLog.getId());

        partialUpdatedSysLog
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysLog))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSysLog, sysLog), getPersistedSysLog(sysLog));
    }

    @Test
    @Transactional
    void fullUpdateSysLogWithPatch() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sysLog using partial update
        SysLog partialUpdatedSysLog = new SysLog();
        partialUpdatedSysLog.setId(sysLog.getId());

        partialUpdatedSysLog
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSysLog))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSysLogUpdatableFieldsEquals(partialUpdatedSysLog, getPersistedSysLog(partialUpdatedSysLog));
    }

    @Test
    @Transactional
    void patchNonExistingSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sysLog.setId(longCount.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sysLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysLog() throws Exception {
        // Initialize the database
        insertedSysLog = sysLogRepository.saveAndGet(sysLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sysLog
        restSysLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sysLogRepository.selectCount(null);
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

    protected SysLog getPersistedSysLog(SysLog sysLog) {
        return sysLogRepository.findById(sysLog.getId()).orElseThrow();
    }

    protected void assertPersistedSysLogToMatchAllProperties(SysLog expectedSysLog) {
        assertSysLogAllPropertiesEquals(expectedSysLog, getPersistedSysLog(expectedSysLog));
    }

    protected void assertPersistedSysLogToMatchUpdatableProperties(SysLog expectedSysLog) {
        assertSysLogAllUpdatablePropertiesEquals(expectedSysLog, getPersistedSysLog(expectedSysLog));
    }
}
