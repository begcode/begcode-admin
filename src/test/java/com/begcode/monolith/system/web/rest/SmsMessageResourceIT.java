package com.begcode.monolith.system.web.rest;

import static com.begcode.monolith.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SendStatus;
import com.begcode.monolith.system.domain.SmsMessage;
import com.begcode.monolith.system.repository.SmsMessageRepository;
import com.begcode.monolith.system.service.dto.SmsMessageDTO;
import com.begcode.monolith.system.service.mapper.SmsMessageMapper;
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
 * Integration tests for the {@link SmsMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
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

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_LAST_MODIFIED_BY = 1L;
    private static final Long UPDATED_LAST_MODIFIED_BY = 2L;
    private static final Long SMALLER_LAST_MODIFIED_BY = 1L - 1L;

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/sms-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @Autowired
    private SmsMessageMapper smsMessageMapper;

    @Autowired
    private MockMvc restSmsMessageMockMvc;

    private SmsMessage smsMessage;

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

    @Test
    @Transactional
    void createSmsMessage() throws Exception {
        int databaseSizeBeforeCreate = smsMessageRepository.findAll().size();
        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);
        restSmsMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeCreate + 1);
        SmsMessage testSmsMessage = smsMessageList.get(smsMessageList.size() - 1);
        assertThat(testSmsMessage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSmsMessage.getSendType()).isEqualTo(DEFAULT_SEND_TYPE);
        assertThat(testSmsMessage.getReceiver()).isEqualTo(DEFAULT_RECEIVER);
        assertThat(testSmsMessage.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSmsMessage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSmsMessage.getSendTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testSmsMessage.getSendStatus()).isEqualTo(DEFAULT_SEND_STATUS);
        assertThat(testSmsMessage.getRetryNum()).isEqualTo(DEFAULT_RETRY_NUM);
        assertThat(testSmsMessage.getFailResult()).isEqualTo(DEFAULT_FAIL_RESULT);
        assertThat(testSmsMessage.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testSmsMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSmsMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSmsMessage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSmsMessage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createSmsMessageWithExistingId() throws Exception {
        // Create the SmsMessage with an existing ID
        smsMessage.setId(1L);
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        int databaseSizeBeforeCreate = smsMessageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSmsMessages() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

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
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getSmsMessage() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

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
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getSmsMessagesByIdFiltering() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        Long id = smsMessage.getId();

        defaultSmsMessageShouldBeFound("id.equals=" + id);
        defaultSmsMessageShouldNotBeFound("id.notEquals=" + id);

        defaultSmsMessageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSmsMessageShouldNotBeFound("id.greaterThan=" + id);

        defaultSmsMessageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSmsMessageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where title equals to DEFAULT_TITLE
        defaultSmsMessageShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the smsMessageList where title equals to UPDATED_TITLE
        defaultSmsMessageShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSmsMessageShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the smsMessageList where title equals to UPDATED_TITLE
        defaultSmsMessageShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where title is not null
        defaultSmsMessageShouldBeFound("title.specified=true");

        // Get all the smsMessageList where title is null
        defaultSmsMessageShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where title contains DEFAULT_TITLE
        defaultSmsMessageShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the smsMessageList where title contains UPDATED_TITLE
        defaultSmsMessageShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where title does not contain DEFAULT_TITLE
        defaultSmsMessageShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the smsMessageList where title does not contain UPDATED_TITLE
        defaultSmsMessageShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendType equals to DEFAULT_SEND_TYPE
        defaultSmsMessageShouldBeFound("sendType.equals=" + DEFAULT_SEND_TYPE);

        // Get all the smsMessageList where sendType equals to UPDATED_SEND_TYPE
        defaultSmsMessageShouldNotBeFound("sendType.equals=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendType in DEFAULT_SEND_TYPE or UPDATED_SEND_TYPE
        defaultSmsMessageShouldBeFound("sendType.in=" + DEFAULT_SEND_TYPE + "," + UPDATED_SEND_TYPE);

        // Get all the smsMessageList where sendType equals to UPDATED_SEND_TYPE
        defaultSmsMessageShouldNotBeFound("sendType.in=" + UPDATED_SEND_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendType is not null
        defaultSmsMessageShouldBeFound("sendType.specified=true");

        // Get all the smsMessageList where sendType is null
        defaultSmsMessageShouldNotBeFound("sendType.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where receiver equals to DEFAULT_RECEIVER
        defaultSmsMessageShouldBeFound("receiver.equals=" + DEFAULT_RECEIVER);

        // Get all the smsMessageList where receiver equals to UPDATED_RECEIVER
        defaultSmsMessageShouldNotBeFound("receiver.equals=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where receiver in DEFAULT_RECEIVER or UPDATED_RECEIVER
        defaultSmsMessageShouldBeFound("receiver.in=" + DEFAULT_RECEIVER + "," + UPDATED_RECEIVER);

        // Get all the smsMessageList where receiver equals to UPDATED_RECEIVER
        defaultSmsMessageShouldNotBeFound("receiver.in=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where receiver is not null
        defaultSmsMessageShouldBeFound("receiver.specified=true");

        // Get all the smsMessageList where receiver is null
        defaultSmsMessageShouldNotBeFound("receiver.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where receiver contains DEFAULT_RECEIVER
        defaultSmsMessageShouldBeFound("receiver.contains=" + DEFAULT_RECEIVER);

        // Get all the smsMessageList where receiver contains UPDATED_RECEIVER
        defaultSmsMessageShouldNotBeFound("receiver.contains=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByReceiverNotContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where receiver does not contain DEFAULT_RECEIVER
        defaultSmsMessageShouldNotBeFound("receiver.doesNotContain=" + DEFAULT_RECEIVER);

        // Get all the smsMessageList where receiver does not contain UPDATED_RECEIVER
        defaultSmsMessageShouldBeFound("receiver.doesNotContain=" + UPDATED_RECEIVER);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where params equals to DEFAULT_PARAMS
        defaultSmsMessageShouldBeFound("params.equals=" + DEFAULT_PARAMS);

        // Get all the smsMessageList where params equals to UPDATED_PARAMS
        defaultSmsMessageShouldNotBeFound("params.equals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where params in DEFAULT_PARAMS or UPDATED_PARAMS
        defaultSmsMessageShouldBeFound("params.in=" + DEFAULT_PARAMS + "," + UPDATED_PARAMS);

        // Get all the smsMessageList where params equals to UPDATED_PARAMS
        defaultSmsMessageShouldNotBeFound("params.in=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where params is not null
        defaultSmsMessageShouldBeFound("params.specified=true");

        // Get all the smsMessageList where params is null
        defaultSmsMessageShouldNotBeFound("params.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where params contains DEFAULT_PARAMS
        defaultSmsMessageShouldBeFound("params.contains=" + DEFAULT_PARAMS);

        // Get all the smsMessageList where params contains UPDATED_PARAMS
        defaultSmsMessageShouldNotBeFound("params.contains=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByParamsNotContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where params does not contain DEFAULT_PARAMS
        defaultSmsMessageShouldNotBeFound("params.doesNotContain=" + DEFAULT_PARAMS);

        // Get all the smsMessageList where params does not contain UPDATED_PARAMS
        defaultSmsMessageShouldBeFound("params.doesNotContain=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime equals to DEFAULT_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.equals=" + DEFAULT_SEND_TIME);

        // Get all the smsMessageList where sendTime equals to UPDATED_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.equals=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime in DEFAULT_SEND_TIME or UPDATED_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.in=" + DEFAULT_SEND_TIME + "," + UPDATED_SEND_TIME);

        // Get all the smsMessageList where sendTime equals to UPDATED_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.in=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime is not null
        defaultSmsMessageShouldBeFound("sendTime.specified=true");

        // Get all the smsMessageList where sendTime is null
        defaultSmsMessageShouldNotBeFound("sendTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime is greater than or equal to DEFAULT_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.greaterThanOrEqual=" + DEFAULT_SEND_TIME);

        // Get all the smsMessageList where sendTime is greater than or equal to UPDATED_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.greaterThanOrEqual=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime is less than or equal to DEFAULT_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.lessThanOrEqual=" + DEFAULT_SEND_TIME);

        // Get all the smsMessageList where sendTime is less than or equal to SMALLER_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.lessThanOrEqual=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime is less than DEFAULT_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.lessThan=" + DEFAULT_SEND_TIME);

        // Get all the smsMessageList where sendTime is less than UPDATED_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.lessThan=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendTime is greater than DEFAULT_SEND_TIME
        defaultSmsMessageShouldNotBeFound("sendTime.greaterThan=" + DEFAULT_SEND_TIME);

        // Get all the smsMessageList where sendTime is greater than SMALLER_SEND_TIME
        defaultSmsMessageShouldBeFound("sendTime.greaterThan=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendStatus equals to DEFAULT_SEND_STATUS
        defaultSmsMessageShouldBeFound("sendStatus.equals=" + DEFAULT_SEND_STATUS);

        // Get all the smsMessageList where sendStatus equals to UPDATED_SEND_STATUS
        defaultSmsMessageShouldNotBeFound("sendStatus.equals=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendStatus in DEFAULT_SEND_STATUS or UPDATED_SEND_STATUS
        defaultSmsMessageShouldBeFound("sendStatus.in=" + DEFAULT_SEND_STATUS + "," + UPDATED_SEND_STATUS);

        // Get all the smsMessageList where sendStatus equals to UPDATED_SEND_STATUS
        defaultSmsMessageShouldNotBeFound("sendStatus.in=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllSmsMessagesBySendStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where sendStatus is not null
        defaultSmsMessageShouldBeFound("sendStatus.specified=true");

        // Get all the smsMessageList where sendStatus is null
        defaultSmsMessageShouldNotBeFound("sendStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum equals to DEFAULT_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.equals=" + DEFAULT_RETRY_NUM);

        // Get all the smsMessageList where retryNum equals to UPDATED_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.equals=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum in DEFAULT_RETRY_NUM or UPDATED_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.in=" + DEFAULT_RETRY_NUM + "," + UPDATED_RETRY_NUM);

        // Get all the smsMessageList where retryNum equals to UPDATED_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.in=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum is not null
        defaultSmsMessageShouldBeFound("retryNum.specified=true");

        // Get all the smsMessageList where retryNum is null
        defaultSmsMessageShouldNotBeFound("retryNum.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum is greater than or equal to DEFAULT_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.greaterThanOrEqual=" + DEFAULT_RETRY_NUM);

        // Get all the smsMessageList where retryNum is greater than or equal to UPDATED_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.greaterThanOrEqual=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum is less than or equal to DEFAULT_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.lessThanOrEqual=" + DEFAULT_RETRY_NUM);

        // Get all the smsMessageList where retryNum is less than or equal to SMALLER_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.lessThanOrEqual=" + SMALLER_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum is less than DEFAULT_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.lessThan=" + DEFAULT_RETRY_NUM);

        // Get all the smsMessageList where retryNum is less than UPDATED_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.lessThan=" + UPDATED_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRetryNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where retryNum is greater than DEFAULT_RETRY_NUM
        defaultSmsMessageShouldNotBeFound("retryNum.greaterThan=" + DEFAULT_RETRY_NUM);

        // Get all the smsMessageList where retryNum is greater than SMALLER_RETRY_NUM
        defaultSmsMessageShouldBeFound("retryNum.greaterThan=" + SMALLER_RETRY_NUM);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where failResult equals to DEFAULT_FAIL_RESULT
        defaultSmsMessageShouldBeFound("failResult.equals=" + DEFAULT_FAIL_RESULT);

        // Get all the smsMessageList where failResult equals to UPDATED_FAIL_RESULT
        defaultSmsMessageShouldNotBeFound("failResult.equals=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where failResult in DEFAULT_FAIL_RESULT or UPDATED_FAIL_RESULT
        defaultSmsMessageShouldBeFound("failResult.in=" + DEFAULT_FAIL_RESULT + "," + UPDATED_FAIL_RESULT);

        // Get all the smsMessageList where failResult equals to UPDATED_FAIL_RESULT
        defaultSmsMessageShouldNotBeFound("failResult.in=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where failResult is not null
        defaultSmsMessageShouldBeFound("failResult.specified=true");

        // Get all the smsMessageList where failResult is null
        defaultSmsMessageShouldNotBeFound("failResult.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where failResult contains DEFAULT_FAIL_RESULT
        defaultSmsMessageShouldBeFound("failResult.contains=" + DEFAULT_FAIL_RESULT);

        // Get all the smsMessageList where failResult contains UPDATED_FAIL_RESULT
        defaultSmsMessageShouldNotBeFound("failResult.contains=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByFailResultNotContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where failResult does not contain DEFAULT_FAIL_RESULT
        defaultSmsMessageShouldNotBeFound("failResult.doesNotContain=" + DEFAULT_FAIL_RESULT);

        // Get all the smsMessageList where failResult does not contain UPDATED_FAIL_RESULT
        defaultSmsMessageShouldBeFound("failResult.doesNotContain=" + UPDATED_FAIL_RESULT);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where remark equals to DEFAULT_REMARK
        defaultSmsMessageShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the smsMessageList where remark equals to UPDATED_REMARK
        defaultSmsMessageShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultSmsMessageShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the smsMessageList where remark equals to UPDATED_REMARK
        defaultSmsMessageShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where remark is not null
        defaultSmsMessageShouldBeFound("remark.specified=true");

        // Get all the smsMessageList where remark is null
        defaultSmsMessageShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where remark contains DEFAULT_REMARK
        defaultSmsMessageShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the smsMessageList where remark contains UPDATED_REMARK
        defaultSmsMessageShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where remark does not contain DEFAULT_REMARK
        defaultSmsMessageShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the smsMessageList where remark does not contain UPDATED_REMARK
        defaultSmsMessageShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy equals to DEFAULT_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the smsMessageList where createdBy equals to UPDATED_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the smsMessageList where createdBy equals to UPDATED_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy is not null
        defaultSmsMessageShouldBeFound("createdBy.specified=true");

        // Get all the smsMessageList where createdBy is null
        defaultSmsMessageShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the smsMessageList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the smsMessageList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy is less than DEFAULT_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the smsMessageList where createdBy is less than UPDATED_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdBy is greater than DEFAULT_CREATED_BY
        defaultSmsMessageShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the smsMessageList where createdBy is greater than SMALLER_CREATED_BY
        defaultSmsMessageShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate equals to DEFAULT_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the smsMessageList where createdDate equals to UPDATED_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the smsMessageList where createdDate equals to UPDATED_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate is not null
        defaultSmsMessageShouldBeFound("createdDate.specified=true");

        // Get all the smsMessageList where createdDate is null
        defaultSmsMessageShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the smsMessageList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the smsMessageList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate is less than DEFAULT_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the smsMessageList where createdDate is less than UPDATED_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultSmsMessageShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the smsMessageList where createdDate is greater than SMALLER_CREATED_DATE
        defaultSmsMessageShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is not null
        defaultSmsMessageShouldBeFound("lastModifiedBy.specified=true");

        // Get all the smsMessageList where lastModifiedBy is null
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is greater than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy is greater than or equal to UPDATED_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is less than or equal to DEFAULT_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy is less than or equal to SMALLER_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is less than DEFAULT_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.lessThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy is less than UPDATED_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.lessThan=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedBy is greater than DEFAULT_LAST_MODIFIED_BY
        defaultSmsMessageShouldNotBeFound("lastModifiedBy.greaterThan=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the smsMessageList where lastModifiedBy is greater than SMALLER_LAST_MODIFIED_BY
        defaultSmsMessageShouldBeFound("lastModifiedBy.greaterThan=" + SMALLER_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is not null
        defaultSmsMessageShouldBeFound("lastModifiedDate.specified=true");

        // Get all the smsMessageList where lastModifiedDate is null
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllSmsMessagesByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        // Get all the smsMessageList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultSmsMessageShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the smsMessageList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultSmsMessageShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
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
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

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
        smsMessageRepository.save(smsMessage);

        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
        SmsMessage testSmsMessage = smsMessageList.get(smsMessageList.size() - 1);
        assertThat(testSmsMessage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSmsMessage.getSendType()).isEqualTo(UPDATED_SEND_TYPE);
        assertThat(testSmsMessage.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testSmsMessage.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSmsMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsMessage.getSendTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testSmsMessage.getSendStatus()).isEqualTo(UPDATED_SEND_STATUS);
        assertThat(testSmsMessage.getRetryNum()).isEqualTo(UPDATED_RETRY_NUM);
        assertThat(testSmsMessage.getFailResult()).isEqualTo(UPDATED_FAIL_RESULT);
        assertThat(testSmsMessage.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsMessage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSmsMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSmsMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSmsMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsMessageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsMessageWithPatch() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();

        // Update the smsMessage using partial update
        SmsMessage partialUpdatedSmsMessage = new SmsMessage();
        partialUpdatedSmsMessage.setId(smsMessage.getId());

        partialUpdatedSmsMessage
            .sendType(UPDATED_SEND_TYPE)
            .receiver(UPDATED_RECEIVER)
            .content(UPDATED_CONTENT)
            .retryNum(UPDATED_RETRY_NUM)
            .failResult(UPDATED_FAIL_RESULT)
            .remark(UPDATED_REMARK);

        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsMessage))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
        SmsMessage testSmsMessage = smsMessageList.get(smsMessageList.size() - 1);
        assertThat(testSmsMessage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSmsMessage.getSendType()).isEqualTo(UPDATED_SEND_TYPE);
        assertThat(testSmsMessage.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testSmsMessage.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSmsMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsMessage.getSendTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testSmsMessage.getSendStatus()).isEqualTo(DEFAULT_SEND_STATUS);
        assertThat(testSmsMessage.getRetryNum()).isEqualTo(UPDATED_RETRY_NUM);
        assertThat(testSmsMessage.getFailResult()).isEqualTo(UPDATED_FAIL_RESULT);
        assertThat(testSmsMessage.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSmsMessage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSmsMessage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSmsMessage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSmsMessageWithPatch() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsMessage))
            )
            .andExpect(status().isOk());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
        SmsMessage testSmsMessage = smsMessageList.get(smsMessageList.size() - 1);
        assertThat(testSmsMessage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSmsMessage.getSendType()).isEqualTo(UPDATED_SEND_TYPE);
        assertThat(testSmsMessage.getReceiver()).isEqualTo(UPDATED_RECEIVER);
        assertThat(testSmsMessage.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSmsMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsMessage.getSendTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testSmsMessage.getSendStatus()).isEqualTo(UPDATED_SEND_STATUS);
        assertThat(testSmsMessage.getRetryNum()).isEqualTo(UPDATED_RETRY_NUM);
        assertThat(testSmsMessage.getFailResult()).isEqualTo(UPDATED_FAIL_RESULT);
        assertThat(testSmsMessage.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSmsMessage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSmsMessage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSmsMessage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSmsMessage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsMessage() throws Exception {
        int databaseSizeBeforeUpdate = smsMessageRepository.findAll().size();
        smsMessage.setId(longCount.incrementAndGet());

        // Create the SmsMessage
        SmsMessageDTO smsMessageDTO = smsMessageMapper.toDto(smsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(smsMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsMessage in the database
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsMessage() throws Exception {
        // Initialize the database
        smsMessageRepository.save(smsMessage);

        int databaseSizeBeforeDelete = smsMessageRepository.findAll().size();

        // Delete the smsMessage
        restSmsMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmsMessage> smsMessageList = smsMessageRepository.findAll();
        assertThat(smsMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
