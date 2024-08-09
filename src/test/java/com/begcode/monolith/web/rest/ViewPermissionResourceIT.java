package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.ViewPermissionAsserts.*;
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
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.domain.enumeration.TargetType;
import com.begcode.monolith.domain.enumeration.ViewPermissionType;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.repository.ViewPermissionRepository;
import com.begcode.monolith.service.ViewPermissionService;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import com.begcode.monolith.service.mapper.ViewPermissionMapper;
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
 * Integration tests for the {@link ViewPermissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class ViewPermissionResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ViewPermissionType DEFAULT_TYPE = ViewPermissionType.MENU;
    private static final ViewPermissionType UPDATED_TYPE = ViewPermissionType.BUTTON;

    private static final String DEFAULT_LOCALE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LOCALE_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GROUP = false;
    private static final Boolean UPDATED_GROUP = true;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_LINK = "BBBBBBBBBB";

    private static final TargetType DEFAULT_TARGET = TargetType.BLANK;
    private static final TargetType UPDATED_TARGET = TargetType.SELF;

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final Boolean DEFAULT_HIDE = false;
    private static final Boolean UPDATED_HIDE = true;

    private static final Boolean DEFAULT_HIDE_IN_BREADCRUMB = false;
    private static final Boolean UPDATED_HIDE_IN_BREADCRUMB = true;

    private static final Boolean DEFAULT_SHORTCUT = false;
    private static final Boolean UPDATED_SHORTCUT = true;

    private static final Boolean DEFAULT_SHORTCUT_ROOT = false;
    private static final Boolean UPDATED_SHORTCUT_ROOT = true;

    private static final Boolean DEFAULT_REUSE = false;
    private static final Boolean UPDATED_REUSE = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final String DEFAULT_API_PERMISSION_CODES = "AAAAAAAAAA";
    private static final String UPDATED_API_PERMISSION_CODES = "BBBBBBBBBB";

    private static final String DEFAULT_COMPONENT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_REDIRECT = "AAAAAAAAAA";
    private static final String UPDATED_REDIRECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/view-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ViewPermissionRepository viewPermissionRepository;

    @Mock
    private ViewPermissionRepository viewPermissionRepositoryMock;

    @Autowired
    private ViewPermissionMapper viewPermissionMapper;

    @Mock
    private ViewPermissionService viewPermissionServiceMock;

    @Autowired
    private MockMvc restViewPermissionMockMvc;

    private ViewPermission viewPermission;

    private ViewPermission insertedViewPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPermission createEntity() {
        ViewPermission viewPermission = new ViewPermission()
            .text(DEFAULT_TEXT)
            .type(DEFAULT_TYPE)
            .localeKey(DEFAULT_LOCALE_KEY)
            .group(DEFAULT_GROUP)
            .link(DEFAULT_LINK)
            .externalLink(DEFAULT_EXTERNAL_LINK)
            .target(DEFAULT_TARGET)
            .icon(DEFAULT_ICON)
            .disabled(DEFAULT_DISABLED)
            .hide(DEFAULT_HIDE)
            .hideInBreadcrumb(DEFAULT_HIDE_IN_BREADCRUMB)
            .shortcut(DEFAULT_SHORTCUT)
            .shortcutRoot(DEFAULT_SHORTCUT_ROOT)
            .reuse(DEFAULT_REUSE)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .order(DEFAULT_ORDER)
            .apiPermissionCodes(DEFAULT_API_PERMISSION_CODES)
            .componentFile(DEFAULT_COMPONENT_FILE)
            .redirect(DEFAULT_REDIRECT);
        return viewPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPermission createUpdatedEntity() {
        ViewPermission viewPermission = new ViewPermission()
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE)
            .localeKey(UPDATED_LOCALE_KEY)
            .group(UPDATED_GROUP)
            .link(UPDATED_LINK)
            .externalLink(UPDATED_EXTERNAL_LINK)
            .target(UPDATED_TARGET)
            .icon(UPDATED_ICON)
            .disabled(UPDATED_DISABLED)
            .hide(UPDATED_HIDE)
            .hideInBreadcrumb(UPDATED_HIDE_IN_BREADCRUMB)
            .shortcut(UPDATED_SHORTCUT)
            .shortcutRoot(UPDATED_SHORTCUT_ROOT)
            .reuse(UPDATED_REUSE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .apiPermissionCodes(UPDATED_API_PERMISSION_CODES)
            .componentFile(UPDATED_COMPONENT_FILE)
            .redirect(UPDATED_REDIRECT);
        return viewPermission;
    }

    @BeforeEach
    public void initTest() {
        viewPermission = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedViewPermission != null) {
            viewPermissionRepository.deleteById(insertedViewPermission.getId());
            insertedViewPermission = null;
        }
    }

    @Test
    @Transactional
    void createViewPermission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);
        var returnedViewPermissionDTO = om.readValue(
            restViewPermissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPermissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ViewPermissionDTO.class
        );

        // Validate the ViewPermission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedViewPermission = viewPermissionMapper.toEntity(returnedViewPermissionDTO);
        assertViewPermissionUpdatableFieldsEquals(returnedViewPermission, getPersistedViewPermission(returnedViewPermission));

        insertedViewPermission = returnedViewPermission;
    }

    @Test
    @Transactional
    void createViewPermissionWithExistingId() throws Exception {
        // Create the ViewPermission with an existing ID
        viewPermission.setId(1L);
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewPermission.setText(null);

        // Create the ViewPermission, which fails.
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        restViewPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewPermission.setCode(null);

        // Create the ViewPermission, which fails.
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        restViewPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewPermissions() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].localeKey").value(hasItem(DEFAULT_LOCALE_KEY)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].externalLink").value(hasItem(DEFAULT_EXTERNAL_LINK)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInBreadcrumb").value(hasItem(DEFAULT_HIDE_IN_BREADCRUMB.booleanValue())))
            .andExpect(jsonPath("$.[*].shortcut").value(hasItem(DEFAULT_SHORTCUT.booleanValue())))
            .andExpect(jsonPath("$.[*].shortcutRoot").value(hasItem(DEFAULT_SHORTCUT_ROOT.booleanValue())))
            .andExpect(jsonPath("$.[*].reuse").value(hasItem(DEFAULT_REUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].apiPermissionCodes").value(hasItem(DEFAULT_API_PERMISSION_CODES)))
            .andExpect(jsonPath("$.[*].componentFile").value(hasItem(DEFAULT_COMPONENT_FILE)))
            .andExpect(jsonPath("$.[*].redirect").value(hasItem(DEFAULT_REDIRECT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewPermissionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(viewPermissionServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restViewPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viewPermissionServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewPermissionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(viewPermissionServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restViewPermissionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(viewPermissionRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getViewPermission() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get the viewPermission
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, viewPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewPermission.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.localeKey").value(DEFAULT_LOCALE_KEY))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.booleanValue()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.externalLink").value(DEFAULT_EXTERNAL_LINK))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.hide").value(DEFAULT_HIDE.booleanValue()))
            .andExpect(jsonPath("$.hideInBreadcrumb").value(DEFAULT_HIDE_IN_BREADCRUMB.booleanValue()))
            .andExpect(jsonPath("$.shortcut").value(DEFAULT_SHORTCUT.booleanValue()))
            .andExpect(jsonPath("$.shortcutRoot").value(DEFAULT_SHORTCUT_ROOT.booleanValue()))
            .andExpect(jsonPath("$.reuse").value(DEFAULT_REUSE.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.apiPermissionCodes").value(DEFAULT_API_PERMISSION_CODES))
            .andExpect(jsonPath("$.componentFile").value(DEFAULT_COMPONENT_FILE))
            .andExpect(jsonPath("$.redirect").value(DEFAULT_REDIRECT));
    }

    @Test
    @Transactional
    void getViewPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        Long id = viewPermission.getId();

        defaultViewPermissionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultViewPermissionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultViewPermissionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where text equals to
        defaultViewPermissionFiltering("text.equals=" + DEFAULT_TEXT, "text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where text in
        defaultViewPermissionFiltering("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT, "text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where text is not null
        defaultViewPermissionFiltering("text.specified=true", "text.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where text contains
        defaultViewPermissionFiltering("text.contains=" + DEFAULT_TEXT, "text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where text does not contain
        defaultViewPermissionFiltering("text.doesNotContain=" + UPDATED_TEXT, "text.doesNotContain=" + DEFAULT_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where type equals to
        defaultViewPermissionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where type in
        defaultViewPermissionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where type is not null
        defaultViewPermissionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLocaleKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where localeKey equals to
        defaultViewPermissionFiltering("localeKey.equals=" + DEFAULT_LOCALE_KEY, "localeKey.equals=" + UPDATED_LOCALE_KEY);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLocaleKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where localeKey in
        defaultViewPermissionFiltering(
            "localeKey.in=" + DEFAULT_LOCALE_KEY + "," + UPDATED_LOCALE_KEY,
            "localeKey.in=" + UPDATED_LOCALE_KEY
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLocaleKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where localeKey is not null
        defaultViewPermissionFiltering("localeKey.specified=true", "localeKey.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLocaleKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where localeKey contains
        defaultViewPermissionFiltering("localeKey.contains=" + DEFAULT_LOCALE_KEY, "localeKey.contains=" + UPDATED_LOCALE_KEY);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLocaleKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where localeKey does not contain
        defaultViewPermissionFiltering("localeKey.doesNotContain=" + UPDATED_LOCALE_KEY, "localeKey.doesNotContain=" + DEFAULT_LOCALE_KEY);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where group equals to
        defaultViewPermissionFiltering("group.equals=" + DEFAULT_GROUP, "group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where group in
        defaultViewPermissionFiltering("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP, "group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where group is not null
        defaultViewPermissionFiltering("group.specified=true", "group.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where link equals to
        defaultViewPermissionFiltering("link.equals=" + DEFAULT_LINK, "link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where link in
        defaultViewPermissionFiltering("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK, "link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where link is not null
        defaultViewPermissionFiltering("link.specified=true", "link.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where link contains
        defaultViewPermissionFiltering("link.contains=" + DEFAULT_LINK, "link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where link does not contain
        defaultViewPermissionFiltering("link.doesNotContain=" + UPDATED_LINK, "link.doesNotContain=" + DEFAULT_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where externalLink equals to
        defaultViewPermissionFiltering("externalLink.equals=" + DEFAULT_EXTERNAL_LINK, "externalLink.equals=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where externalLink in
        defaultViewPermissionFiltering(
            "externalLink.in=" + DEFAULT_EXTERNAL_LINK + "," + UPDATED_EXTERNAL_LINK,
            "externalLink.in=" + UPDATED_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where externalLink is not null
        defaultViewPermissionFiltering("externalLink.specified=true", "externalLink.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where externalLink contains
        defaultViewPermissionFiltering("externalLink.contains=" + DEFAULT_EXTERNAL_LINK, "externalLink.contains=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where externalLink does not contain
        defaultViewPermissionFiltering(
            "externalLink.doesNotContain=" + UPDATED_EXTERNAL_LINK,
            "externalLink.doesNotContain=" + DEFAULT_EXTERNAL_LINK
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where target equals to
        defaultViewPermissionFiltering("target.equals=" + DEFAULT_TARGET, "target.equals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where target in
        defaultViewPermissionFiltering("target.in=" + DEFAULT_TARGET + "," + UPDATED_TARGET, "target.in=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where target is not null
        defaultViewPermissionFiltering("target.specified=true", "target.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where icon equals to
        defaultViewPermissionFiltering("icon.equals=" + DEFAULT_ICON, "icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where icon in
        defaultViewPermissionFiltering("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON, "icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where icon is not null
        defaultViewPermissionFiltering("icon.specified=true", "icon.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where icon contains
        defaultViewPermissionFiltering("icon.contains=" + DEFAULT_ICON, "icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where icon does not contain
        defaultViewPermissionFiltering("icon.doesNotContain=" + UPDATED_ICON, "icon.doesNotContain=" + DEFAULT_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where disabled equals to
        defaultViewPermissionFiltering("disabled.equals=" + DEFAULT_DISABLED, "disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where disabled in
        defaultViewPermissionFiltering("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED, "disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where disabled is not null
        defaultViewPermissionFiltering("disabled.specified=true", "disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hide equals to
        defaultViewPermissionFiltering("hide.equals=" + DEFAULT_HIDE, "hide.equals=" + UPDATED_HIDE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hide in
        defaultViewPermissionFiltering("hide.in=" + DEFAULT_HIDE + "," + UPDATED_HIDE, "hide.in=" + UPDATED_HIDE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hide is not null
        defaultViewPermissionFiltering("hide.specified=true", "hide.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb equals to
        defaultViewPermissionFiltering(
            "hideInBreadcrumb.equals=" + DEFAULT_HIDE_IN_BREADCRUMB,
            "hideInBreadcrumb.equals=" + UPDATED_HIDE_IN_BREADCRUMB
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb in
        defaultViewPermissionFiltering(
            "hideInBreadcrumb.in=" + DEFAULT_HIDE_IN_BREADCRUMB + "," + UPDATED_HIDE_IN_BREADCRUMB,
            "hideInBreadcrumb.in=" + UPDATED_HIDE_IN_BREADCRUMB
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb is not null
        defaultViewPermissionFiltering("hideInBreadcrumb.specified=true", "hideInBreadcrumb.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcut equals to
        defaultViewPermissionFiltering("shortcut.equals=" + DEFAULT_SHORTCUT, "shortcut.equals=" + UPDATED_SHORTCUT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcut in
        defaultViewPermissionFiltering("shortcut.in=" + DEFAULT_SHORTCUT + "," + UPDATED_SHORTCUT, "shortcut.in=" + UPDATED_SHORTCUT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcut is not null
        defaultViewPermissionFiltering("shortcut.specified=true", "shortcut.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcutRoot equals to
        defaultViewPermissionFiltering("shortcutRoot.equals=" + DEFAULT_SHORTCUT_ROOT, "shortcutRoot.equals=" + UPDATED_SHORTCUT_ROOT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcutRoot in
        defaultViewPermissionFiltering(
            "shortcutRoot.in=" + DEFAULT_SHORTCUT_ROOT + "," + UPDATED_SHORTCUT_ROOT,
            "shortcutRoot.in=" + UPDATED_SHORTCUT_ROOT
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where shortcutRoot is not null
        defaultViewPermissionFiltering("shortcutRoot.specified=true", "shortcutRoot.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where reuse equals to
        defaultViewPermissionFiltering("reuse.equals=" + DEFAULT_REUSE, "reuse.equals=" + UPDATED_REUSE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where reuse in
        defaultViewPermissionFiltering("reuse.in=" + DEFAULT_REUSE + "," + UPDATED_REUSE, "reuse.in=" + UPDATED_REUSE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where reuse is not null
        defaultViewPermissionFiltering("reuse.specified=true", "reuse.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where code equals to
        defaultViewPermissionFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where code in
        defaultViewPermissionFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where code is not null
        defaultViewPermissionFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where code contains
        defaultViewPermissionFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where code does not contain
        defaultViewPermissionFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where description equals to
        defaultViewPermissionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where description in
        defaultViewPermissionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where description is not null
        defaultViewPermissionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where description contains
        defaultViewPermissionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where description does not contain
        defaultViewPermissionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order equals to
        defaultViewPermissionFiltering("order.equals=" + DEFAULT_ORDER, "order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order in
        defaultViewPermissionFiltering("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER, "order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order is not null
        defaultViewPermissionFiltering("order.specified=true", "order.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order is greater than or equal to
        defaultViewPermissionFiltering("order.greaterThanOrEqual=" + DEFAULT_ORDER, "order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order is less than or equal to
        defaultViewPermissionFiltering("order.lessThanOrEqual=" + DEFAULT_ORDER, "order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order is less than
        defaultViewPermissionFiltering("order.lessThan=" + UPDATED_ORDER, "order.lessThan=" + DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where order is greater than
        defaultViewPermissionFiltering("order.greaterThan=" + SMALLER_ORDER, "order.greaterThan=" + DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes equals to
        defaultViewPermissionFiltering(
            "apiPermissionCodes.equals=" + DEFAULT_API_PERMISSION_CODES,
            "apiPermissionCodes.equals=" + UPDATED_API_PERMISSION_CODES
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes in
        defaultViewPermissionFiltering(
            "apiPermissionCodes.in=" + DEFAULT_API_PERMISSION_CODES + "," + UPDATED_API_PERMISSION_CODES,
            "apiPermissionCodes.in=" + UPDATED_API_PERMISSION_CODES
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes is not null
        defaultViewPermissionFiltering("apiPermissionCodes.specified=true", "apiPermissionCodes.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes contains
        defaultViewPermissionFiltering(
            "apiPermissionCodes.contains=" + DEFAULT_API_PERMISSION_CODES,
            "apiPermissionCodes.contains=" + UPDATED_API_PERMISSION_CODES
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes does not contain
        defaultViewPermissionFiltering(
            "apiPermissionCodes.doesNotContain=" + UPDATED_API_PERMISSION_CODES,
            "apiPermissionCodes.doesNotContain=" + DEFAULT_API_PERMISSION_CODES
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where componentFile equals to
        defaultViewPermissionFiltering("componentFile.equals=" + DEFAULT_COMPONENT_FILE, "componentFile.equals=" + UPDATED_COMPONENT_FILE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where componentFile in
        defaultViewPermissionFiltering(
            "componentFile.in=" + DEFAULT_COMPONENT_FILE + "," + UPDATED_COMPONENT_FILE,
            "componentFile.in=" + UPDATED_COMPONENT_FILE
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where componentFile is not null
        defaultViewPermissionFiltering("componentFile.specified=true", "componentFile.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where componentFile contains
        defaultViewPermissionFiltering(
            "componentFile.contains=" + DEFAULT_COMPONENT_FILE,
            "componentFile.contains=" + UPDATED_COMPONENT_FILE
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where componentFile does not contain
        defaultViewPermissionFiltering(
            "componentFile.doesNotContain=" + UPDATED_COMPONENT_FILE,
            "componentFile.doesNotContain=" + DEFAULT_COMPONENT_FILE
        );
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where redirect equals to
        defaultViewPermissionFiltering("redirect.equals=" + DEFAULT_REDIRECT, "redirect.equals=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where redirect in
        defaultViewPermissionFiltering("redirect.in=" + DEFAULT_REDIRECT + "," + UPDATED_REDIRECT, "redirect.in=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where redirect is not null
        defaultViewPermissionFiltering("redirect.specified=true", "redirect.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where redirect contains
        defaultViewPermissionFiltering("redirect.contains=" + DEFAULT_REDIRECT, "redirect.contains=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        // Get all the viewPermissionList where redirect does not contain
        defaultViewPermissionFiltering("redirect.doesNotContain=" + UPDATED_REDIRECT, "redirect.doesNotContain=" + DEFAULT_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByParentIsEqualToSomething() throws Exception {
        ViewPermission parent = ViewPermissionResourceIT.createEntity();
        viewPermission.setParent(parent);
        viewPermissionRepository.insert(viewPermission);
        Long parentId = parent.getId();
        // Get all the viewPermissionList where parent equals to parentId
        defaultViewPermissionShouldBeFound("parentId.equals=" + parentId);

        // Get all the viewPermissionList where parent equals to (parentId + 1)
        defaultViewPermissionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllViewPermissionsByAuthoritiesIsEqualToSomething() throws Exception {
        Authority authorities = AuthorityResourceIT.createEntity();
        // viewPermission.addAuthorities(authorities);
        viewPermissionRepository.insert(viewPermission);
        Long authoritiesId = authorities.getId();
        // Get all the viewPermissionList where authorities equals to authoritiesId
        defaultViewPermissionShouldBeFound("authoritiesId.equals=" + authoritiesId);

        // Get all the viewPermissionList where authorities equals to (authoritiesId + 1)
        defaultViewPermissionShouldNotBeFound("authoritiesId.equals=" + (authoritiesId + 1));
    }

    private void defaultViewPermissionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultViewPermissionShouldBeFound(shouldBeFound);
        defaultViewPermissionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultViewPermissionShouldBeFound(String filter) throws Exception {
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].localeKey").value(hasItem(DEFAULT_LOCALE_KEY)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].externalLink").value(hasItem(DEFAULT_EXTERNAL_LINK)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].hide").value(hasItem(DEFAULT_HIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].hideInBreadcrumb").value(hasItem(DEFAULT_HIDE_IN_BREADCRUMB.booleanValue())))
            .andExpect(jsonPath("$.[*].shortcut").value(hasItem(DEFAULT_SHORTCUT.booleanValue())))
            .andExpect(jsonPath("$.[*].shortcutRoot").value(hasItem(DEFAULT_SHORTCUT_ROOT.booleanValue())))
            .andExpect(jsonPath("$.[*].reuse").value(hasItem(DEFAULT_REUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].apiPermissionCodes").value(hasItem(DEFAULT_API_PERMISSION_CODES)))
            .andExpect(jsonPath("$.[*].componentFile").value(hasItem(DEFAULT_COMPONENT_FILE)))
            .andExpect(jsonPath("$.[*].redirect").value(hasItem(DEFAULT_REDIRECT)));

        // Check, that the count call also returns 1
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultViewPermissionShouldNotBeFound(String filter) throws Exception {
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingViewPermission() throws Exception {
        // Get the viewPermission
        restViewPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingViewPermission() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPermission
        ViewPermission updatedViewPermission = viewPermissionRepository.findById(viewPermission.getId()).orElseThrow();
        updatedViewPermission
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE)
            .localeKey(UPDATED_LOCALE_KEY)
            .group(UPDATED_GROUP)
            .link(UPDATED_LINK)
            .externalLink(UPDATED_EXTERNAL_LINK)
            .target(UPDATED_TARGET)
            .icon(UPDATED_ICON)
            .disabled(UPDATED_DISABLED)
            .hide(UPDATED_HIDE)
            .hideInBreadcrumb(UPDATED_HIDE_IN_BREADCRUMB)
            .shortcut(UPDATED_SHORTCUT)
            .shortcutRoot(UPDATED_SHORTCUT_ROOT)
            .reuse(UPDATED_REUSE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .apiPermissionCodes(UPDATED_API_PERMISSION_CODES)
            .componentFile(UPDATED_COMPONENT_FILE)
            .redirect(UPDATED_REDIRECT);
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(updatedViewPermission);

        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedViewPermissionToMatchAllProperties(updatedViewPermission);
    }

    @Test
    @Transactional
    void putNonExistingViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPermission using partial update
        ViewPermission partialUpdatedViewPermission = new ViewPermission();
        partialUpdatedViewPermission.setId(viewPermission.getId());

        partialUpdatedViewPermission
            .text(UPDATED_TEXT)
            .group(UPDATED_GROUP)
            .disabled(UPDATED_DISABLED)
            .hideInBreadcrumb(UPDATED_HIDE_IN_BREADCRUMB)
            .shortcutRoot(UPDATED_SHORTCUT_ROOT);

        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewPermission))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewPermissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedViewPermission, viewPermission),
            getPersistedViewPermission(viewPermission)
        );
    }

    @Test
    @Transactional
    void fullUpdateViewPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPermission using partial update
        ViewPermission partialUpdatedViewPermission = new ViewPermission();
        partialUpdatedViewPermission.setId(viewPermission.getId());

        partialUpdatedViewPermission
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE)
            .localeKey(UPDATED_LOCALE_KEY)
            .group(UPDATED_GROUP)
            .link(UPDATED_LINK)
            .externalLink(UPDATED_EXTERNAL_LINK)
            .target(UPDATED_TARGET)
            .icon(UPDATED_ICON)
            .disabled(UPDATED_DISABLED)
            .hide(UPDATED_HIDE)
            .hideInBreadcrumb(UPDATED_HIDE_IN_BREADCRUMB)
            .shortcut(UPDATED_SHORTCUT)
            .shortcutRoot(UPDATED_SHORTCUT_ROOT)
            .reuse(UPDATED_REUSE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .apiPermissionCodes(UPDATED_API_PERMISSION_CODES)
            .componentFile(UPDATED_COMPONENT_FILE)
            .redirect(UPDATED_REDIRECT);

        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewPermission))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewPermissionUpdatableFieldsEquals(partialUpdatedViewPermission, getPersistedViewPermission(partialUpdatedViewPermission));
    }

    @Test
    @Transactional
    void patchNonExistingViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(viewPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewPermission() throws Exception {
        // Initialize the database
        insertedViewPermission = viewPermissionRepository.saveAndGet(viewPermission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the viewPermission
        restViewPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return viewPermissionRepository.selectCount(null);
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

    protected ViewPermission getPersistedViewPermission(ViewPermission viewPermission) {
        return viewPermissionRepository.findById(viewPermission.getId()).orElseThrow();
    }

    protected void assertPersistedViewPermissionToMatchAllProperties(ViewPermission expectedViewPermission) {
        assertViewPermissionAllPropertiesEquals(expectedViewPermission, getPersistedViewPermission(expectedViewPermission));
    }

    protected void assertPersistedViewPermissionToMatchUpdatableProperties(ViewPermission expectedViewPermission) {
        assertViewPermissionAllUpdatablePropertiesEquals(expectedViewPermission, getPersistedViewPermission(expectedViewPermission));
    }
}
