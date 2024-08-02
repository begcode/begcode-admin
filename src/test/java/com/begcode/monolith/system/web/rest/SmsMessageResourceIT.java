package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.system.domain.SmsMessageAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import com.begcode.monolith.system.domain.SmsMessage;
import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
import com.begcode.monolith.system.service.mapper.SmsMessageMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SmsMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockMyUser
public class SmsMessageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final MessageSendType DEFAULT_SEND_TYPE = MessageSendType.EMAIL;
    private static final MessageSendType UPDATED_SEND_TYPE = MessageSendType.SMS;

    private static final String DEFAULT_RECEIVER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final SendStatus DEFAULT_SEND_STATUS = SendStatus.WAITING;
    private static final SendStatus UPDATED_SEND_STATUS = SendStatus.SUCCESS;

    private static final Integer DEFAULT_RETRY_NUM = 1;
    private static final Integer UPDATED_RETRY_NUM = 2;
    private static final Integer SMALLER_RETRY_NUM = 1 - 1;

    private static final String DEFAULT_FAIL_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_FAIL_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/sms-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @Autowired
    private SmsMessageMapper smsMessageMapper;

    @Autowired
    private MockMvc restSmsMessageMockMvc;

    private SmsMessage smsMessage;

    private SmsMessage insertedSmsMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsMessage createEntity() {
        SmsMessage smsMessage = new SmsMessage()
            .title(DEFAULT_TITLE)
            .sendType(DEFAULT_SEND_TYPE)
            .receiver(DEFAULT_RECEIVER)
            .params(DEFAULT_PARAMS)
            .content(DEFAULT_CONTENT)
            .sendTime(DEFAULT_SEND_TIME)
            .sendStatus(DEFAULT_SEND_STATUS)
            .retryNum(DEFAULT_RETRY_NUM)
            .failResult(DEFAULT_FAIL_RESULT)
            .remark(DEFAULT_REMARK)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return smsMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsMessage createUpdatedEntity() {
        SmsMessage smsMessage = new SmsMessage()
            .title(UPDATED_TITLE)
            .sendType(UPDATED_SEND_TYPE)
            .receiver(UPDATED_RECEIVER)
            .params(UPDATED_PARAMS)
            .content(UPDATED_CONTENT)
            .sendTime(UPDATED_SEND_TIME)
            .sendStatus(UPDATED_SEND_STATUS)
            .retryNum(UPDATED_RETRY_NUM)
            .failResult(UPDATED_FAIL_RESULT)
            .remark(UPDATED_REMARK)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return smsMessage;
    }

    @BeforeEach
    public void initTest() {
        smsMessage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSmsMessage != null) {
            smsMessageRepository.deleteById(insertedSmsMessage.getId());
            insertedSmsMessage = null;
        }
    }

    @Test
    @Transactional
    void createSmsMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);
        var returnedSmsMessageDTO = om.readValue(
            restSmsMessageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsMessageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SmsMessageDTO.class
        );

        // Validate the SmsMessage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSmsMessage = smsMessageMapper.toEntity(returnedSmsMessageDTO);
        assertSmsMessageUpdatableFieldsEquals(returnedSmsMessage, getPersistedSmsMessage(returnedSmsMessage));

        insertedSmsMessage = returnedSmsMessage;
    }

    @Test
    @Transactional
    void createSmsMessageWithExistingId() throws Exception {
        // Create the SmsMessage with an existing ID
        smsMessage.setId(1L);
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSmsMessages() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].sendType").value(hasItem(DEFAULT_SEND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].receiver").value(hasItem(DEFAULT_RECEIVER)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].retryNum").value(hasItem(DEFAULT_RETRY_NUM)))
            .andExpect(jsonPath("$.[*].failResult").value(hasItem(DEFAULT_FAIL_RESULT)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSmsMessage() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get the smsMessage
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, smsMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsMessage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.sendType").value(DEFAULT_SEND_TYPE.toString()))
            .andExpect(jsonPath("$.receiver").value(DEFAULT_RECEIVER))
            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.sendTime").value(sameInstant(DEFAULT_SEND_TIME)))
            .andExpect(jsonPath("$.sendStatus").value(DEFAULT_SEND_STATUS.toString()))
            .andExpect(jsonPath("$.retryNum").value(DEFAULT_RETRY_NUM))
            .andExpect(jsonPath("$.failResult").value(DEFAULT_FAIL_RESULT))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSmsMessagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        Long id = smsMessage.getId();

        defaultSmsMessageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSmsMessageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSmsMessageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where title equals to
        defaultSmsMessageFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where title in
        defaultSmsMessageFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where title is not null
        defaultSmsMessageFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where title contains
        defaultSmsMessageFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where title does not contain
        defaultSmsMessageFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendType equals to
        defaultSmsMessageFiltering("sendType.equals=" + DEFAULT_SEND_TYPE, "sendType.equals=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendType in
        defaultSmsMessageFiltering("sendType.in=" + DEFAULT_SEND_TYPE + "," + UPDATED_SEND_TYPE, "sendType.in=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendType is not null
        defaultSmsMessageFiltering("sendType.specified=true", "sendType.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where receiver equals to
        defaultSmsMessageFiltering("receiver.equals=" + DEFAULT_RECEIVER, "receiver.equals=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where receiver in
        defaultSmsMessageFiltering("receiver.in=" + DEFAULT_RECEIVER + "," + UPDATED_RECEIVER, "receiver.in=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where receiver is not null
        defaultSmsMessageFiltering("receiver.specified=true", "receiver.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where receiver contains
        defaultSmsMessageFiltering("receiver.contains=" + DEFAULT_RECEIVER, "receiver.contains=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where receiver does not contain
        defaultSmsMessageFiltering("receiver.doesNotContain=" + UPDATED_RECEIVER, "receiver.doesNotContain=" + DEFAULT_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where params equals to
        defaultSmsMessageFiltering("params.equals=" + DEFAULT_PARAMS, "params.equals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where params in
        defaultSmsMessageFiltering("params.in=" + DEFAULT_PARAMS + "," + UPDATED_PARAMS, "params.in=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where params is not null
        defaultSmsMessageFiltering("params.specified=true", "params.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where params contains
        defaultSmsMessageFiltering("params.contains=" + DEFAULT_PARAMS, "params.contains=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where params does not contain
        defaultSmsMessageFiltering("params.doesNotContain=" + UPDATED_PARAMS, "params.doesNotContain=" + DEFAULT_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime equals to
        defaultSmsMessageFiltering("sendTime.equals=" + DEFAULT_SEND_TIME, "sendTime.equals=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime in
        defaultSmsMessageFiltering("sendTime.in=" + DEFAULT_SEND_TIME + "," + UPDATED_SEND_TIME, "sendTime.in=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime is not null
        defaultSmsMessageFiltering("sendTime.specified=true", "sendTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime is greater than or equal to
        defaultSmsMessageFiltering("sendTime.greaterThanOrEqual=" + DEFAULT_SEND_TIME, "sendTime.greaterThanOrEqual=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime is less than or equal to
        defaultSmsMessageFiltering("sendTime.lessThanOrEqual=" + DEFAULT_SEND_TIME, "sendTime.lessThanOrEqual=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime is less than
        defaultSmsMessageFiltering("sendTime.lessThan=" + UPDATED_SEND_TIME, "sendTime.lessThan=" + DEFAULT_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendTime is greater than
        defaultSmsMessageFiltering("sendTime.greaterThan=" + SMALLER_SEND_TIME, "sendTime.greaterThan=" + DEFAULT_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendStatus equals to
        defaultSmsMessageFiltering("sendStatus.equals=" + DEFAULT_SEND_STATUS, "sendStatus.equals=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendStatus in
        defaultSmsMessageFiltering(
            "sendStatus.in=" + DEFAULT_SEND_STATUS + "," + UPDATED_SEND_STATUS,
            "sendStatus.in=" + UPDATED_SEND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where sendStatus is not null
        defaultSmsMessageFiltering("sendStatus.specified=true", "sendStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum equals to
        defaultSmsMessageFiltering("retryNum.equals=" + DEFAULT_RETRY_NUM, "retryNum.equals=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum in
        defaultSmsMessageFiltering("retryNum.in=" + DEFAULT_RETRY_NUM + "," + UPDATED_RETRY_NUM, "retryNum.in=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum is not null
        defaultSmsMessageFiltering("retryNum.specified=true", "retryNum.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum is greater than or equal to
        defaultSmsMessageFiltering("retryNum.greaterThanOrEqual=" + DEFAULT_RETRY_NUM, "retryNum.greaterThanOrEqual=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum is less than or equal to
        defaultSmsMessageFiltering("retryNum.lessThanOrEqual=" + DEFAULT_RETRY_NUM, "retryNum.lessThanOrEqual=" + SMALLER_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum is less than
        defaultSmsMessageFiltering("retryNum.lessThan=" + UPDATED_RETRY_NUM, "retryNum.lessThan=" + DEFAULT_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where retryNum is greater than
        defaultSmsMessageFiltering("retryNum.greaterThan=" + SMALLER_RETRY_NUM, "retryNum.greaterThan=" + DEFAULT_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where failResult equals to
        defaultSmsMessageFiltering("failResult.equals=" + DEFAULT_FAIL_RESULT, "failResult.equals=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where failResult in
        defaultSmsMessageFiltering(
            "failResult.in=" + DEFAULT_FAIL_RESULT + "," + UPDATED_FAIL_RESULT,
            "failResult.in=" + UPDATED_FAIL_RESULT
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where failResult is not null
        defaultSmsMessageFiltering("failResult.specified=true", "failResult.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where failResult contains
        defaultSmsMessageFiltering("failResult.contains=" + DEFAULT_FAIL_RESULT, "failResult.contains=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where failResult does not contain
        defaultSmsMessageFiltering("failResult.doesNotContain=" + UPDATED_FAIL_RESULT, "failResult.doesNotContain=" + DEFAULT_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where remark equals to
        defaultSmsMessageFiltering("remark.equals=" + DEFAULT_REMARK, "remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where remark in
        defaultSmsMessageFiltering("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK, "remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where remark is not null
        defaultSmsMessageFiltering("remark.specified=true", "remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where remark contains
        defaultSmsMessageFiltering("remark.contains=" + DEFAULT_REMARK, "remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where remark does not contain
        defaultSmsMessageFiltering("remark.doesNotContain=" + UPDATED_REMARK, "remark.doesNotContain=" + DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy equals to
        defaultSmsMessageFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy in
        defaultSmsMessageFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy is not null
        defaultSmsMessageFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy is greater than or equal to
        defaultSmsMessageFiltering(
            "createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY,
            "createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy is less than or equal to
        defaultSmsMessageFiltering("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY, "createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy is less than
        defaultSmsMessageFiltering("createdBy.lessThan=" + UPDATED_CREATED_BY, "createdBy.lessThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdBy is greater than
        defaultSmsMessageFiltering("createdBy.greaterThan=" + SMALLER_CREATED_BY, "createdBy.greaterThan=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdDate equals to
        defaultSmsMessageFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdDate in
        defaultSmsMessageFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where createdDate is not null
        defaultSmsMessageFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy equals to
        defaultSmsMessageFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy in
        defaultSmsMessageFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is not null
        defaultSmsMessageFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is greater than or equal to
        defaultSmsMessageFiltering(
            "lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is less than or equal to
        defaultSmsMessageFiltering(
            "lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is less than
        defaultSmsMessageFiltering(
            "lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is greater than
        defaultSmsMessageFiltering(
            "lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY,
            "lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedDate equals to
        defaultSmsMessageFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedDate in
        defaultSmsMessageFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is not null
        defaultSmsMessageFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultSmsMessageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSmsMessageShouldBeFound(shouldBeFound);
        defaultSmsMessageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsMessageShouldBeFound(String filter) throws Exception {
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].sendType").value(hasItem(DEFAULT_SEND_TYPE.toString())))
            .andExpect(jsonPath("$.[*].receiver").value(hasItem(DEFAULT_RECEIVER)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].retryNum").value(hasItem(DEFAULT_RETRY_NUM)))
            .andExpect(jsonPath("$.[*].failResult").value(hasItem(DEFAULT_FAIL_RESULT)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsMessageShouldNotBeFound(String filter) throws Exception {
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsMessageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmsMessage() throws Exception {
        // Get the smsMessage
        restSmsMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSmsMessage() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsMessage
        SmsMessage updatedSmsMessage = smsMessageRepository.findById(smsMessage.getId()).orElseThrow();
        updatedSmsMessage
            .title(UPDATED_TITLE)
            .sendType(UPDATED_SEND_TYPE)
            .receiver(UPDATED_RECEIVER)
            .params(UPDATED_PARAMS)
            .content(UPDATED_CONTENT)
            .sendTime(UPDATED_SEND_TIME)
            .sendStatus(UPDATED_SEND_STATUS)
            .retryNum(UPDATED_RETRY_NUM)
            .failResult(UPDATED_FAIL_RESULT)
            .remark(UPDATED_REMARK)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(updatedSmsMessage);

        restSmsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSmsMessageToMatchAllProperties(updatedSmsMessage);
    }

    @Test
    @Transactional
    void putNonExistingSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smsMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsMessageWithPatch() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsMessage using partial update
        SmsMessage partialUpdatedSmsMessage = new SmsMessage();
        partialUpdatedSmsMessage.setId(smsMessage.getId());

        partialUpdatedSmsMessage
            .params(UPDATED_PARAMS)
            .content(UPDATED_CONTENT)
            .sendTime(UPDATED_SEND_TIME)
            .sendStatus(UPDATED_SEND_STATUS)
            .failResult(UPDATED_FAIL_RESULT)
            .remark(UPDATED_REMARK)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsMessage))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsMessageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSmsMessage, smsMessage),
            getPersistedSmsMessage(smsMessage)
        );
    }

    @Test
    @Transactional
    void fullUpdateSmsMessageWithPatch() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smsMessage using partial update
        SmsMessage partialUpdatedSmsMessage = new SmsMessage();
        partialUpdatedSmsMessage.setId(smsMessage.getId());

        partialUpdatedSmsMessage
            .title(UPDATED_TITLE)
            .sendType(UPDATED_SEND_TYPE)
            .receiver(UPDATED_RECEIVER)
            .params(UPDATED_PARAMS)
            .content(UPDATED_CONTENT)
            .sendTime(UPDATED_SEND_TIME)
            .sendStatus(UPDATED_SEND_STATUS)
            .retryNum(UPDATED_RETRY_NUM)
            .failResult(UPDATED_FAIL_RESULT)
            .remark(UPDATED_REMARK)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmsMessage))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmsMessageUpdatableFieldsEquals(partialUpdatedSmsMessage, getPersistedSmsMessage(partialUpdatedSmsMessage));
    }

    @Test
    @Transactional
    void patchNonExistingSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(smsMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsMessage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsMessage() throws Exception {
        // Initialize the database
        insertedSmsMessage = smsMessageRepository.saveAndGet(smsMessage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the smsMessage
        restSmsMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return smsMessageRepository.selectCount(null);
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

    protected SmsMessage getPersistedSmsMessage(SmsMessage smsMessage) {
        return smsMessageRepository.findById(smsMessage.getId()).orElseThrow();
    }

    protected void assertPersistedSmsMessageToMatchAllProperties(SmsMessage expectedSmsMessage) {
        assertSmsMessageAllPropertiesEquals(expectedSmsMessage, getPersistedSmsMessage(expectedSmsMessage));
    }

    protected void assertPersistedSmsMessageToMatchUpdatableProperties(SmsMessage expectedSmsMessage) {
        assertSmsMessageAllUpdatablePropertiesEquals(expectedSmsMessage, getPersistedSmsMessage(expectedSmsMessage));
    }
}
