package com.begcode.monolith.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
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
 * Integration tests for the {@link ViewPermissionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ViewPermissionResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ViewPermissionType DEFAULT_TYPE = ViewPermissionType.MENU;
    private static final ViewPermissionType UPDATED_TYPE = ViewPermissionType.BUTTON;

    private static final String DEFAULT_I_18_N = "AAAAAAAAAA";
    private static final String UPDATED_I_18_N = "BBBBBBBBBB";

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .i18n(DEFAULT_I_18_N)
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
            .i18n(UPDATED_I_18_N)
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

    @Test
    @Transactional
    void createViewPermission() throws Exception {
        int databaseSizeBeforeCreate = viewPermissionRepository.findAll().size();
        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);
        restViewPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        ViewPermission testViewPermission = viewPermissionList.get(viewPermissionList.size() - 1);
        assertThat(testViewPermission.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testViewPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testViewPermission.getI18n()).isEqualTo(DEFAULT_I_18_N);
        assertThat(testViewPermission.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testViewPermission.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testViewPermission.getExternalLink()).isEqualTo(DEFAULT_EXTERNAL_LINK);
        assertThat(testViewPermission.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testViewPermission.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testViewPermission.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testViewPermission.getHide()).isEqualTo(DEFAULT_HIDE);
        assertThat(testViewPermission.getHideInBreadcrumb()).isEqualTo(DEFAULT_HIDE_IN_BREADCRUMB);
        assertThat(testViewPermission.getShortcut()).isEqualTo(DEFAULT_SHORTCUT);
        assertThat(testViewPermission.getShortcutRoot()).isEqualTo(DEFAULT_SHORTCUT_ROOT);
        assertThat(testViewPermission.getReuse()).isEqualTo(DEFAULT_REUSE);
        assertThat(testViewPermission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testViewPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testViewPermission.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testViewPermission.getApiPermissionCodes()).isEqualTo(DEFAULT_API_PERMISSION_CODES);
        assertThat(testViewPermission.getComponentFile()).isEqualTo(DEFAULT_COMPONENT_FILE);
        assertThat(testViewPermission.getRedirect()).isEqualTo(DEFAULT_REDIRECT);
    }

    @Test
    @Transactional
    void createViewPermissionWithExistingId() throws Exception {
        // Create the ViewPermission with an existing ID
        viewPermission.setId(1L);
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        int databaseSizeBeforeCreate = viewPermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewPermissionRepository.findAll().size();
        // set the field null
        viewPermission.setText(null);

        // Create the ViewPermission, which fails.
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        restViewPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewPermissionRepository.findAll().size();
        // set the field null
        viewPermission.setCode(null);

        // Create the ViewPermission, which fails.
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        restViewPermissionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewPermissions() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].i18n").value(hasItem(DEFAULT_I_18_N)))
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
        viewPermissionRepository.save(viewPermission);

        // Get the viewPermission
        restViewPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, viewPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewPermission.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.i18n").value(DEFAULT_I_18_N))
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
        viewPermissionRepository.save(viewPermission);

        Long id = viewPermission.getId();

        defaultViewPermissionShouldBeFound("id.equals=" + id);
        defaultViewPermissionShouldNotBeFound("id.notEquals=" + id);

        defaultViewPermissionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultViewPermissionShouldNotBeFound("id.greaterThan=" + id);

        defaultViewPermissionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultViewPermissionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where text equals to DEFAULT_TEXT
        defaultViewPermissionShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the viewPermissionList where text equals to UPDATED_TEXT
        defaultViewPermissionShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultViewPermissionShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the viewPermissionList where text equals to UPDATED_TEXT
        defaultViewPermissionShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where text is not null
        defaultViewPermissionShouldBeFound("text.specified=true");

        // Get all the viewPermissionList where text is null
        defaultViewPermissionShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where text contains DEFAULT_TEXT
        defaultViewPermissionShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the viewPermissionList where text contains UPDATED_TEXT
        defaultViewPermissionShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTextNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where text does not contain DEFAULT_TEXT
        defaultViewPermissionShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the viewPermissionList where text does not contain UPDATED_TEXT
        defaultViewPermissionShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where type equals to DEFAULT_TYPE
        defaultViewPermissionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the viewPermissionList where type equals to UPDATED_TYPE
        defaultViewPermissionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultViewPermissionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the viewPermissionList where type equals to UPDATED_TYPE
        defaultViewPermissionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where type is not null
        defaultViewPermissionShouldBeFound("type.specified=true");

        // Get all the viewPermissionList where type is null
        defaultViewPermissionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByI18nIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where i18n equals to DEFAULT_I_18_N
        defaultViewPermissionShouldBeFound("i18n.equals=" + DEFAULT_I_18_N);

        // Get all the viewPermissionList where i18n equals to UPDATED_I_18_N
        defaultViewPermissionShouldNotBeFound("i18n.equals=" + UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByI18nIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where i18n in DEFAULT_I_18_N or UPDATED_I_18_N
        defaultViewPermissionShouldBeFound("i18n.in=" + DEFAULT_I_18_N + "," + UPDATED_I_18_N);

        // Get all the viewPermissionList where i18n equals to UPDATED_I_18_N
        defaultViewPermissionShouldNotBeFound("i18n.in=" + UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByI18nIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where i18n is not null
        defaultViewPermissionShouldBeFound("i18n.specified=true");

        // Get all the viewPermissionList where i18n is null
        defaultViewPermissionShouldNotBeFound("i18n.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByI18nContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where i18n contains DEFAULT_I_18_N
        defaultViewPermissionShouldBeFound("i18n.contains=" + DEFAULT_I_18_N);

        // Get all the viewPermissionList where i18n contains UPDATED_I_18_N
        defaultViewPermissionShouldNotBeFound("i18n.contains=" + UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByI18nNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where i18n does not contain DEFAULT_I_18_N
        defaultViewPermissionShouldNotBeFound("i18n.doesNotContain=" + DEFAULT_I_18_N);

        // Get all the viewPermissionList where i18n does not contain UPDATED_I_18_N
        defaultViewPermissionShouldBeFound("i18n.doesNotContain=" + UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where group equals to DEFAULT_GROUP
        defaultViewPermissionShouldBeFound("group.equals=" + DEFAULT_GROUP);

        // Get all the viewPermissionList where group equals to UPDATED_GROUP
        defaultViewPermissionShouldNotBeFound("group.equals=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where group in DEFAULT_GROUP or UPDATED_GROUP
        defaultViewPermissionShouldBeFound("group.in=" + DEFAULT_GROUP + "," + UPDATED_GROUP);

        // Get all the viewPermissionList where group equals to UPDATED_GROUP
        defaultViewPermissionShouldNotBeFound("group.in=" + UPDATED_GROUP);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where group is not null
        defaultViewPermissionShouldBeFound("group.specified=true");

        // Get all the viewPermissionList where group is null
        defaultViewPermissionShouldNotBeFound("group.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where link equals to DEFAULT_LINK
        defaultViewPermissionShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the viewPermissionList where link equals to UPDATED_LINK
        defaultViewPermissionShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where link in DEFAULT_LINK or UPDATED_LINK
        defaultViewPermissionShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the viewPermissionList where link equals to UPDATED_LINK
        defaultViewPermissionShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where link is not null
        defaultViewPermissionShouldBeFound("link.specified=true");

        // Get all the viewPermissionList where link is null
        defaultViewPermissionShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where link contains DEFAULT_LINK
        defaultViewPermissionShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the viewPermissionList where link contains UPDATED_LINK
        defaultViewPermissionShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where link does not contain DEFAULT_LINK
        defaultViewPermissionShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the viewPermissionList where link does not contain UPDATED_LINK
        defaultViewPermissionShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where externalLink equals to DEFAULT_EXTERNAL_LINK
        defaultViewPermissionShouldBeFound("externalLink.equals=" + DEFAULT_EXTERNAL_LINK);

        // Get all the viewPermissionList where externalLink equals to UPDATED_EXTERNAL_LINK
        defaultViewPermissionShouldNotBeFound("externalLink.equals=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where externalLink in DEFAULT_EXTERNAL_LINK or UPDATED_EXTERNAL_LINK
        defaultViewPermissionShouldBeFound("externalLink.in=" + DEFAULT_EXTERNAL_LINK + "," + UPDATED_EXTERNAL_LINK);

        // Get all the viewPermissionList where externalLink equals to UPDATED_EXTERNAL_LINK
        defaultViewPermissionShouldNotBeFound("externalLink.in=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where externalLink is not null
        defaultViewPermissionShouldBeFound("externalLink.specified=true");

        // Get all the viewPermissionList where externalLink is null
        defaultViewPermissionShouldNotBeFound("externalLink.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where externalLink contains DEFAULT_EXTERNAL_LINK
        defaultViewPermissionShouldBeFound("externalLink.contains=" + DEFAULT_EXTERNAL_LINK);

        // Get all the viewPermissionList where externalLink contains UPDATED_EXTERNAL_LINK
        defaultViewPermissionShouldNotBeFound("externalLink.contains=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByExternalLinkNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where externalLink does not contain DEFAULT_EXTERNAL_LINK
        defaultViewPermissionShouldNotBeFound("externalLink.doesNotContain=" + DEFAULT_EXTERNAL_LINK);

        // Get all the viewPermissionList where externalLink does not contain UPDATED_EXTERNAL_LINK
        defaultViewPermissionShouldBeFound("externalLink.doesNotContain=" + UPDATED_EXTERNAL_LINK);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where target equals to DEFAULT_TARGET
        defaultViewPermissionShouldBeFound("target.equals=" + DEFAULT_TARGET);

        // Get all the viewPermissionList where target equals to UPDATED_TARGET
        defaultViewPermissionShouldNotBeFound("target.equals=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where target in DEFAULT_TARGET or UPDATED_TARGET
        defaultViewPermissionShouldBeFound("target.in=" + DEFAULT_TARGET + "," + UPDATED_TARGET);

        // Get all the viewPermissionList where target equals to UPDATED_TARGET
        defaultViewPermissionShouldNotBeFound("target.in=" + UPDATED_TARGET);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByTargetIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where target is not null
        defaultViewPermissionShouldBeFound("target.specified=true");

        // Get all the viewPermissionList where target is null
        defaultViewPermissionShouldNotBeFound("target.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where icon equals to DEFAULT_ICON
        defaultViewPermissionShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the viewPermissionList where icon equals to UPDATED_ICON
        defaultViewPermissionShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultViewPermissionShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the viewPermissionList where icon equals to UPDATED_ICON
        defaultViewPermissionShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where icon is not null
        defaultViewPermissionShouldBeFound("icon.specified=true");

        // Get all the viewPermissionList where icon is null
        defaultViewPermissionShouldNotBeFound("icon.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where icon contains DEFAULT_ICON
        defaultViewPermissionShouldBeFound("icon.contains=" + DEFAULT_ICON);

        // Get all the viewPermissionList where icon contains UPDATED_ICON
        defaultViewPermissionShouldNotBeFound("icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByIconNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where icon does not contain DEFAULT_ICON
        defaultViewPermissionShouldNotBeFound("icon.doesNotContain=" + DEFAULT_ICON);

        // Get all the viewPermissionList where icon does not contain UPDATED_ICON
        defaultViewPermissionShouldBeFound("icon.doesNotContain=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where disabled equals to DEFAULT_DISABLED
        defaultViewPermissionShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the viewPermissionList where disabled equals to UPDATED_DISABLED
        defaultViewPermissionShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultViewPermissionShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the viewPermissionList where disabled equals to UPDATED_DISABLED
        defaultViewPermissionShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where disabled is not null
        defaultViewPermissionShouldBeFound("disabled.specified=true");

        // Get all the viewPermissionList where disabled is null
        defaultViewPermissionShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hide equals to DEFAULT_HIDE
        defaultViewPermissionShouldBeFound("hide.equals=" + DEFAULT_HIDE);

        // Get all the viewPermissionList where hide equals to UPDATED_HIDE
        defaultViewPermissionShouldNotBeFound("hide.equals=" + UPDATED_HIDE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hide in DEFAULT_HIDE or UPDATED_HIDE
        defaultViewPermissionShouldBeFound("hide.in=" + DEFAULT_HIDE + "," + UPDATED_HIDE);

        // Get all the viewPermissionList where hide equals to UPDATED_HIDE
        defaultViewPermissionShouldNotBeFound("hide.in=" + UPDATED_HIDE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hide is not null
        defaultViewPermissionShouldBeFound("hide.specified=true");

        // Get all the viewPermissionList where hide is null
        defaultViewPermissionShouldNotBeFound("hide.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb equals to DEFAULT_HIDE_IN_BREADCRUMB
        defaultViewPermissionShouldBeFound("hideInBreadcrumb.equals=" + DEFAULT_HIDE_IN_BREADCRUMB);

        // Get all the viewPermissionList where hideInBreadcrumb equals to UPDATED_HIDE_IN_BREADCRUMB
        defaultViewPermissionShouldNotBeFound("hideInBreadcrumb.equals=" + UPDATED_HIDE_IN_BREADCRUMB);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb in DEFAULT_HIDE_IN_BREADCRUMB or UPDATED_HIDE_IN_BREADCRUMB
        defaultViewPermissionShouldBeFound("hideInBreadcrumb.in=" + DEFAULT_HIDE_IN_BREADCRUMB + "," + UPDATED_HIDE_IN_BREADCRUMB);

        // Get all the viewPermissionList where hideInBreadcrumb equals to UPDATED_HIDE_IN_BREADCRUMB
        defaultViewPermissionShouldNotBeFound("hideInBreadcrumb.in=" + UPDATED_HIDE_IN_BREADCRUMB);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByHideInBreadcrumbIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where hideInBreadcrumb is not null
        defaultViewPermissionShouldBeFound("hideInBreadcrumb.specified=true");

        // Get all the viewPermissionList where hideInBreadcrumb is null
        defaultViewPermissionShouldNotBeFound("hideInBreadcrumb.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcut equals to DEFAULT_SHORTCUT
        defaultViewPermissionShouldBeFound("shortcut.equals=" + DEFAULT_SHORTCUT);

        // Get all the viewPermissionList where shortcut equals to UPDATED_SHORTCUT
        defaultViewPermissionShouldNotBeFound("shortcut.equals=" + UPDATED_SHORTCUT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcut in DEFAULT_SHORTCUT or UPDATED_SHORTCUT
        defaultViewPermissionShouldBeFound("shortcut.in=" + DEFAULT_SHORTCUT + "," + UPDATED_SHORTCUT);

        // Get all the viewPermissionList where shortcut equals to UPDATED_SHORTCUT
        defaultViewPermissionShouldNotBeFound("shortcut.in=" + UPDATED_SHORTCUT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcut is not null
        defaultViewPermissionShouldBeFound("shortcut.specified=true");

        // Get all the viewPermissionList where shortcut is null
        defaultViewPermissionShouldNotBeFound("shortcut.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcutRoot equals to DEFAULT_SHORTCUT_ROOT
        defaultViewPermissionShouldBeFound("shortcutRoot.equals=" + DEFAULT_SHORTCUT_ROOT);

        // Get all the viewPermissionList where shortcutRoot equals to UPDATED_SHORTCUT_ROOT
        defaultViewPermissionShouldNotBeFound("shortcutRoot.equals=" + UPDATED_SHORTCUT_ROOT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcutRoot in DEFAULT_SHORTCUT_ROOT or UPDATED_SHORTCUT_ROOT
        defaultViewPermissionShouldBeFound("shortcutRoot.in=" + DEFAULT_SHORTCUT_ROOT + "," + UPDATED_SHORTCUT_ROOT);

        // Get all the viewPermissionList where shortcutRoot equals to UPDATED_SHORTCUT_ROOT
        defaultViewPermissionShouldNotBeFound("shortcutRoot.in=" + UPDATED_SHORTCUT_ROOT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByShortcutRootIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where shortcutRoot is not null
        defaultViewPermissionShouldBeFound("shortcutRoot.specified=true");

        // Get all the viewPermissionList where shortcutRoot is null
        defaultViewPermissionShouldNotBeFound("shortcutRoot.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where reuse equals to DEFAULT_REUSE
        defaultViewPermissionShouldBeFound("reuse.equals=" + DEFAULT_REUSE);

        // Get all the viewPermissionList where reuse equals to UPDATED_REUSE
        defaultViewPermissionShouldNotBeFound("reuse.equals=" + UPDATED_REUSE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where reuse in DEFAULT_REUSE or UPDATED_REUSE
        defaultViewPermissionShouldBeFound("reuse.in=" + DEFAULT_REUSE + "," + UPDATED_REUSE);

        // Get all the viewPermissionList where reuse equals to UPDATED_REUSE
        defaultViewPermissionShouldNotBeFound("reuse.in=" + UPDATED_REUSE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByReuseIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where reuse is not null
        defaultViewPermissionShouldBeFound("reuse.specified=true");

        // Get all the viewPermissionList where reuse is null
        defaultViewPermissionShouldNotBeFound("reuse.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where code equals to DEFAULT_CODE
        defaultViewPermissionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the viewPermissionList where code equals to UPDATED_CODE
        defaultViewPermissionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultViewPermissionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the viewPermissionList where code equals to UPDATED_CODE
        defaultViewPermissionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where code is not null
        defaultViewPermissionShouldBeFound("code.specified=true");

        // Get all the viewPermissionList where code is null
        defaultViewPermissionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where code contains DEFAULT_CODE
        defaultViewPermissionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the viewPermissionList where code contains UPDATED_CODE
        defaultViewPermissionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where code does not contain DEFAULT_CODE
        defaultViewPermissionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the viewPermissionList where code does not contain UPDATED_CODE
        defaultViewPermissionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where description equals to DEFAULT_DESCRIPTION
        defaultViewPermissionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the viewPermissionList where description equals to UPDATED_DESCRIPTION
        defaultViewPermissionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultViewPermissionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the viewPermissionList where description equals to UPDATED_DESCRIPTION
        defaultViewPermissionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where description is not null
        defaultViewPermissionShouldBeFound("description.specified=true");

        // Get all the viewPermissionList where description is null
        defaultViewPermissionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where description contains DEFAULT_DESCRIPTION
        defaultViewPermissionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the viewPermissionList where description contains UPDATED_DESCRIPTION
        defaultViewPermissionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where description does not contain DEFAULT_DESCRIPTION
        defaultViewPermissionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the viewPermissionList where description does not contain UPDATED_DESCRIPTION
        defaultViewPermissionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order equals to DEFAULT_ORDER
        defaultViewPermissionShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the viewPermissionList where order equals to UPDATED_ORDER
        defaultViewPermissionShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultViewPermissionShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the viewPermissionList where order equals to UPDATED_ORDER
        defaultViewPermissionShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order is not null
        defaultViewPermissionShouldBeFound("order.specified=true");

        // Get all the viewPermissionList where order is null
        defaultViewPermissionShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order is greater than or equal to DEFAULT_ORDER
        defaultViewPermissionShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the viewPermissionList where order is greater than or equal to UPDATED_ORDER
        defaultViewPermissionShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order is less than or equal to DEFAULT_ORDER
        defaultViewPermissionShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the viewPermissionList where order is less than or equal to SMALLER_ORDER
        defaultViewPermissionShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order is less than DEFAULT_ORDER
        defaultViewPermissionShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the viewPermissionList where order is less than UPDATED_ORDER
        defaultViewPermissionShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where order is greater than DEFAULT_ORDER
        defaultViewPermissionShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the viewPermissionList where order is greater than SMALLER_ORDER
        defaultViewPermissionShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes equals to DEFAULT_API_PERMISSION_CODES
        defaultViewPermissionShouldBeFound("apiPermissionCodes.equals=" + DEFAULT_API_PERMISSION_CODES);

        // Get all the viewPermissionList where apiPermissionCodes equals to UPDATED_API_PERMISSION_CODES
        defaultViewPermissionShouldNotBeFound("apiPermissionCodes.equals=" + UPDATED_API_PERMISSION_CODES);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes in DEFAULT_API_PERMISSION_CODES or UPDATED_API_PERMISSION_CODES
        defaultViewPermissionShouldBeFound("apiPermissionCodes.in=" + DEFAULT_API_PERMISSION_CODES + "," + UPDATED_API_PERMISSION_CODES);

        // Get all the viewPermissionList where apiPermissionCodes equals to UPDATED_API_PERMISSION_CODES
        defaultViewPermissionShouldNotBeFound("apiPermissionCodes.in=" + UPDATED_API_PERMISSION_CODES);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes is not null
        defaultViewPermissionShouldBeFound("apiPermissionCodes.specified=true");

        // Get all the viewPermissionList where apiPermissionCodes is null
        defaultViewPermissionShouldNotBeFound("apiPermissionCodes.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes contains DEFAULT_API_PERMISSION_CODES
        defaultViewPermissionShouldBeFound("apiPermissionCodes.contains=" + DEFAULT_API_PERMISSION_CODES);

        // Get all the viewPermissionList where apiPermissionCodes contains UPDATED_API_PERMISSION_CODES
        defaultViewPermissionShouldNotBeFound("apiPermissionCodes.contains=" + UPDATED_API_PERMISSION_CODES);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByApiPermissionCodesNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where apiPermissionCodes does not contain DEFAULT_API_PERMISSION_CODES
        defaultViewPermissionShouldNotBeFound("apiPermissionCodes.doesNotContain=" + DEFAULT_API_PERMISSION_CODES);

        // Get all the viewPermissionList where apiPermissionCodes does not contain UPDATED_API_PERMISSION_CODES
        defaultViewPermissionShouldBeFound("apiPermissionCodes.doesNotContain=" + UPDATED_API_PERMISSION_CODES);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where componentFile equals to DEFAULT_COMPONENT_FILE
        defaultViewPermissionShouldBeFound("componentFile.equals=" + DEFAULT_COMPONENT_FILE);

        // Get all the viewPermissionList where componentFile equals to UPDATED_COMPONENT_FILE
        defaultViewPermissionShouldNotBeFound("componentFile.equals=" + UPDATED_COMPONENT_FILE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where componentFile in DEFAULT_COMPONENT_FILE or UPDATED_COMPONENT_FILE
        defaultViewPermissionShouldBeFound("componentFile.in=" + DEFAULT_COMPONENT_FILE + "," + UPDATED_COMPONENT_FILE);

        // Get all the viewPermissionList where componentFile equals to UPDATED_COMPONENT_FILE
        defaultViewPermissionShouldNotBeFound("componentFile.in=" + UPDATED_COMPONENT_FILE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where componentFile is not null
        defaultViewPermissionShouldBeFound("componentFile.specified=true");

        // Get all the viewPermissionList where componentFile is null
        defaultViewPermissionShouldNotBeFound("componentFile.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where componentFile contains DEFAULT_COMPONENT_FILE
        defaultViewPermissionShouldBeFound("componentFile.contains=" + DEFAULT_COMPONENT_FILE);

        // Get all the viewPermissionList where componentFile contains UPDATED_COMPONENT_FILE
        defaultViewPermissionShouldNotBeFound("componentFile.contains=" + UPDATED_COMPONENT_FILE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByComponentFileNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where componentFile does not contain DEFAULT_COMPONENT_FILE
        defaultViewPermissionShouldNotBeFound("componentFile.doesNotContain=" + DEFAULT_COMPONENT_FILE);

        // Get all the viewPermissionList where componentFile does not contain UPDATED_COMPONENT_FILE
        defaultViewPermissionShouldBeFound("componentFile.doesNotContain=" + UPDATED_COMPONENT_FILE);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsEqualToSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where redirect equals to DEFAULT_REDIRECT
        defaultViewPermissionShouldBeFound("redirect.equals=" + DEFAULT_REDIRECT);

        // Get all the viewPermissionList where redirect equals to UPDATED_REDIRECT
        defaultViewPermissionShouldNotBeFound("redirect.equals=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsInShouldWork() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where redirect in DEFAULT_REDIRECT or UPDATED_REDIRECT
        defaultViewPermissionShouldBeFound("redirect.in=" + DEFAULT_REDIRECT + "," + UPDATED_REDIRECT);

        // Get all the viewPermissionList where redirect equals to UPDATED_REDIRECT
        defaultViewPermissionShouldNotBeFound("redirect.in=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectIsNullOrNotNull() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where redirect is not null
        defaultViewPermissionShouldBeFound("redirect.specified=true");

        // Get all the viewPermissionList where redirect is null
        defaultViewPermissionShouldNotBeFound("redirect.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where redirect contains DEFAULT_REDIRECT
        defaultViewPermissionShouldBeFound("redirect.contains=" + DEFAULT_REDIRECT);

        // Get all the viewPermissionList where redirect contains UPDATED_REDIRECT
        defaultViewPermissionShouldNotBeFound("redirect.contains=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByRedirectNotContainsSomething() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        // Get all the viewPermissionList where redirect does not contain DEFAULT_REDIRECT
        defaultViewPermissionShouldNotBeFound("redirect.doesNotContain=" + DEFAULT_REDIRECT);

        // Get all the viewPermissionList where redirect does not contain UPDATED_REDIRECT
        defaultViewPermissionShouldBeFound("redirect.doesNotContain=" + UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void getAllViewPermissionsByChildrenIsEqualToSomething() throws Exception {
        ViewPermission children = ViewPermissionResourceIT.createEntity();
        // viewPermission.addChildren(children);
        viewPermissionRepository.insert(viewPermission);
        Long childrenId = children.getId();
        // Get all the viewPermissionList where children equals to childrenId
        defaultViewPermissionShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the viewPermissionList where children equals to (childrenId + 1)
        defaultViewPermissionShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
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
            .andExpect(jsonPath("$.[*].i18n").value(hasItem(DEFAULT_I_18_N)))
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
        viewPermissionRepository.save(viewPermission);

        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();

        // Update the viewPermission
        ViewPermission updatedViewPermission = viewPermissionRepository.findById(viewPermission.getId()).orElseThrow();
        updatedViewPermission
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE)
            .i18n(UPDATED_I_18_N)
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
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
        ViewPermission testViewPermission = viewPermissionList.get(viewPermissionList.size() - 1);
        assertThat(testViewPermission.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testViewPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testViewPermission.getI18n()).isEqualTo(UPDATED_I_18_N);
        assertThat(testViewPermission.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testViewPermission.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testViewPermission.getExternalLink()).isEqualTo(UPDATED_EXTERNAL_LINK);
        assertThat(testViewPermission.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testViewPermission.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testViewPermission.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testViewPermission.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testViewPermission.getHideInBreadcrumb()).isEqualTo(UPDATED_HIDE_IN_BREADCRUMB);
        assertThat(testViewPermission.getShortcut()).isEqualTo(UPDATED_SHORTCUT);
        assertThat(testViewPermission.getShortcutRoot()).isEqualTo(UPDATED_SHORTCUT_ROOT);
        assertThat(testViewPermission.getReuse()).isEqualTo(UPDATED_REUSE);
        assertThat(testViewPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testViewPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testViewPermission.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testViewPermission.getApiPermissionCodes()).isEqualTo(UPDATED_API_PERMISSION_CODES);
        assertThat(testViewPermission.getComponentFile()).isEqualTo(UPDATED_COMPONENT_FILE);
        assertThat(testViewPermission.getRedirect()).isEqualTo(UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void putNonExistingViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewPermissionWithPatch() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();

        // Update the viewPermission using partial update
        ViewPermission partialUpdatedViewPermission = new ViewPermission();
        partialUpdatedViewPermission.setId(viewPermission.getId());

        partialUpdatedViewPermission
            .group(UPDATED_GROUP)
            .target(UPDATED_TARGET)
            .disabled(UPDATED_DISABLED)
            .hide(UPDATED_HIDE)
            .apiPermissionCodes(UPDATED_API_PERMISSION_CODES)
            .componentFile(UPDATED_COMPONENT_FILE)
            .redirect(UPDATED_REDIRECT);

        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewPermission))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
        ViewPermission testViewPermission = viewPermissionList.get(viewPermissionList.size() - 1);
        assertThat(testViewPermission.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testViewPermission.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testViewPermission.getI18n()).isEqualTo(DEFAULT_I_18_N);
        assertThat(testViewPermission.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testViewPermission.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testViewPermission.getExternalLink()).isEqualTo(DEFAULT_EXTERNAL_LINK);
        assertThat(testViewPermission.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testViewPermission.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testViewPermission.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testViewPermission.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testViewPermission.getHideInBreadcrumb()).isEqualTo(DEFAULT_HIDE_IN_BREADCRUMB);
        assertThat(testViewPermission.getShortcut()).isEqualTo(DEFAULT_SHORTCUT);
        assertThat(testViewPermission.getShortcutRoot()).isEqualTo(DEFAULT_SHORTCUT_ROOT);
        assertThat(testViewPermission.getReuse()).isEqualTo(DEFAULT_REUSE);
        assertThat(testViewPermission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testViewPermission.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testViewPermission.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testViewPermission.getApiPermissionCodes()).isEqualTo(UPDATED_API_PERMISSION_CODES);
        assertThat(testViewPermission.getComponentFile()).isEqualTo(UPDATED_COMPONENT_FILE);
        assertThat(testViewPermission.getRedirect()).isEqualTo(UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void fullUpdateViewPermissionWithPatch() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();

        // Update the viewPermission using partial update
        ViewPermission partialUpdatedViewPermission = new ViewPermission();
        partialUpdatedViewPermission.setId(viewPermission.getId());

        partialUpdatedViewPermission
            .text(UPDATED_TEXT)
            .type(UPDATED_TYPE)
            .i18n(UPDATED_I_18_N)
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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViewPermission))
            )
            .andExpect(status().isOk());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
        ViewPermission testViewPermission = viewPermissionList.get(viewPermissionList.size() - 1);
        assertThat(testViewPermission.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testViewPermission.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testViewPermission.getI18n()).isEqualTo(UPDATED_I_18_N);
        assertThat(testViewPermission.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testViewPermission.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testViewPermission.getExternalLink()).isEqualTo(UPDATED_EXTERNAL_LINK);
        assertThat(testViewPermission.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testViewPermission.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testViewPermission.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testViewPermission.getHide()).isEqualTo(UPDATED_HIDE);
        assertThat(testViewPermission.getHideInBreadcrumb()).isEqualTo(UPDATED_HIDE_IN_BREADCRUMB);
        assertThat(testViewPermission.getShortcut()).isEqualTo(UPDATED_SHORTCUT);
        assertThat(testViewPermission.getShortcutRoot()).isEqualTo(UPDATED_SHORTCUT_ROOT);
        assertThat(testViewPermission.getReuse()).isEqualTo(UPDATED_REUSE);
        assertThat(testViewPermission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testViewPermission.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testViewPermission.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testViewPermission.getApiPermissionCodes()).isEqualTo(UPDATED_API_PERMISSION_CODES);
        assertThat(testViewPermission.getComponentFile()).isEqualTo(UPDATED_COMPONENT_FILE);
        assertThat(testViewPermission.getRedirect()).isEqualTo(UPDATED_REDIRECT);
    }

    @Test
    @Transactional
    void patchNonExistingViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewPermission() throws Exception {
        int databaseSizeBeforeUpdate = viewPermissionRepository.findAll().size();
        viewPermission.setId(longCount.incrementAndGet());

        // Create the ViewPermission
        ViewPermissionDTO viewPermissionDTO = viewPermissionMapper.toDto(viewPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viewPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPermission in the database
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewPermission() throws Exception {
        // Initialize the database
        viewPermissionRepository.save(viewPermission);

        int databaseSizeBeforeDelete = viewPermissionRepository.findAll().size();

        // Delete the viewPermission
        restViewPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViewPermission> viewPermissionList = viewPermissionRepository.findAll();
        assertThat(viewPermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
