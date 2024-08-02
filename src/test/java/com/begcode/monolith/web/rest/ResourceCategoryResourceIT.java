package com.begcode.monolith.web.rest;

import static com.begcode.monolith.domain.ResourceCategoryAsserts.*;
import static com.begcode.monolith.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.IntegrationTest;
import com.begcode.monolith.config.WithMockMyUser;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.repository.ResourceCategoryRepository;
import com.begcode.monolith.service.ResourceCategoryService;
import com.begcode.monolith.service.dto.ResourceCategoryDTO;
import com.begcode.monolith.service.mapper.ResourceCategoryMapper;
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
 * Integration tests for the {@link ResourceCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockMyUser
public class ResourceCategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;
    private static final Integer SMALLER_ORDER_NUMBER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/resource-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResourceCategoryRepository resourceCategoryRepository;

    @Mock
    private ResourceCategoryRepository resourceCategoryRepositoryMock;

    @Autowired
    private ResourceCategoryMapper resourceCategoryMapper;

    @Mock
    private ResourceCategoryService resourceCategoryServiceMock;

    @Autowired
    private MockMvc restResourceCategoryMockMvc;

    private ResourceCategory resourceCategory;

    private ResourceCategory insertedResourceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceCategory createEntity() {
        ResourceCategory resourceCategory = new ResourceCategory()
            .title(DEFAULT_TITLE)
            .code(DEFAULT_CODE)
            .orderNumber(DEFAULT_ORDER_NUMBER);
        return resourceCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceCategory createUpdatedEntity() {
        ResourceCategory resourceCategory = new ResourceCategory()
            .title(UPDATED_TITLE)
            .code(UPDATED_CODE)
            .orderNumber(UPDATED_ORDER_NUMBER);
        return resourceCategory;
    }

    @BeforeEach
    public void initTest() {
        resourceCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResourceCategory != null) {
            resourceCategoryRepository.deleteById(insertedResourceCategory.getId());
            insertedResourceCategory = null;
        }
    }

    @Test
    @Transactional
    void createResourceCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);
        var returnedResourceCategoryDTO = om.readValue(
            restResourceCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResourceCategoryDTO.class
        );

        // Validate the ResourceCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResourceCategory = resourceCategoryMapper.toEntity(returnedResourceCategoryDTO);
        assertResourceCategoryUpdatableFieldsEquals(returnedResourceCategory, getPersistedResourceCategory(returnedResourceCategory));

        insertedResourceCategory = returnedResourceCategory;
    }

    @Test
    @Transactional
    void createResourceCategoryWithExistingId() throws Exception {
        // Create the ResourceCategory with an existing ID
        resourceCategory.setId(1L);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResourceCategories() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResourceCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(resourceCategoryServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restResourceCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(resourceCategoryServiceMock, times(1)).findAll(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResourceCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(resourceCategoryServiceMock.findAll(any())).thenReturn(new Page().setRecords(new ArrayList<>()));

        restResourceCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(resourceCategoryRepositoryMock, times(1)).findAll();
    }

    @Test
    @Transactional
    void getResourceCategory() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get the resourceCategory
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceCategory.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    void getResourceCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        Long id = resourceCategory.getId();

        defaultResourceCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultResourceCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultResourceCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where title equals to
        defaultResourceCategoryFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where title in
        defaultResourceCategoryFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where title is not null
        defaultResourceCategoryFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where title contains
        defaultResourceCategoryFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where title does not contain
        defaultResourceCategoryFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where code equals to
        defaultResourceCategoryFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where code in
        defaultResourceCategoryFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where code is not null
        defaultResourceCategoryFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where code contains
        defaultResourceCategoryFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where code does not contain
        defaultResourceCategoryFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber equals to
        defaultResourceCategoryFiltering("orderNumber.equals=" + DEFAULT_ORDER_NUMBER, "orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber in
        defaultResourceCategoryFiltering(
            "orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER,
            "orderNumber.in=" + UPDATED_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber is not null
        defaultResourceCategoryFiltering("orderNumber.specified=true", "orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber is greater than or equal to
        defaultResourceCategoryFiltering(
            "orderNumber.greaterThanOrEqual=" + DEFAULT_ORDER_NUMBER,
            "orderNumber.greaterThanOrEqual=" + UPDATED_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber is less than or equal to
        defaultResourceCategoryFiltering(
            "orderNumber.lessThanOrEqual=" + DEFAULT_ORDER_NUMBER,
            "orderNumber.lessThanOrEqual=" + SMALLER_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber is less than
        defaultResourceCategoryFiltering("orderNumber.lessThan=" + UPDATED_ORDER_NUMBER, "orderNumber.lessThan=" + DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByOrderNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        // Get all the resourceCategoryList where orderNumber is greater than
        defaultResourceCategoryFiltering(
            "orderNumber.greaterThan=" + SMALLER_ORDER_NUMBER,
            "orderNumber.greaterThan=" + DEFAULT_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllResourceCategoriesByParentIsEqualToSomething() throws Exception {
        ResourceCategory parent = ResourceCategoryResourceIT.createEntity();
        resourceCategory.setParent(parent);
        resourceCategoryRepository.insert(resourceCategory);
        Long parentId = parent.getId();
        // Get all the resourceCategoryList where parent equals to parentId
        defaultResourceCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the resourceCategoryList where parent equals to (parentId + 1)
        defaultResourceCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultResourceCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultResourceCategoryShouldBeFound(shouldBeFound);
        defaultResourceCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResourceCategoryShouldBeFound(String filter) throws Exception {
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));

        // Check, that the count call also returns 1
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResourceCategoryShouldNotBeFound(String filter) throws Exception {
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResourceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResourceCategory() throws Exception {
        // Get the resourceCategory
        restResourceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResourceCategory() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceCategory
        ResourceCategory updatedResourceCategory = resourceCategoryRepository.findById(resourceCategory.getId()).orElseThrow();
        updatedResourceCategory.title(UPDATED_TITLE).code(UPDATED_CODE).orderNumber(UPDATED_ORDER_NUMBER);
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(updatedResourceCategory);

        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResourceCategoryToMatchAllProperties(updatedResourceCategory);
    }

    @Test
    @Transactional
    void putNonExistingResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resourceCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceCategory using partial update
        ResourceCategory partialUpdatedResourceCategory = new ResourceCategory();
        partialUpdatedResourceCategory.setId(resourceCategory.getId());

        partialUpdatedResourceCategory.code(UPDATED_CODE).orderNumber(UPDATED_ORDER_NUMBER);

        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResourceCategory, resourceCategory),
            getPersistedResourceCategory(resourceCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateResourceCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resourceCategory using partial update
        ResourceCategory partialUpdatedResourceCategory = new ResourceCategory();
        partialUpdatedResourceCategory.setId(resourceCategory.getId());

        partialUpdatedResourceCategory.title(UPDATED_TITLE).code(UPDATED_CODE).orderNumber(UPDATED_ORDER_NUMBER);

        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResourceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ResourceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResourceCategoryUpdatableFieldsEquals(
            partialUpdatedResourceCategory,
            getPersistedResourceCategory(partialUpdatedResourceCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resourceCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResourceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resourceCategory.setId(longCount.incrementAndGet());

        // Create the ResourceCategory
        ResourceCategoryDTO resourceCategoryDTO = resourceCategoryMapper.toDto(resourceCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resourceCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResourceCategory() throws Exception {
        // Initialize the database
        insertedResourceCategory = resourceCategoryRepository.saveAndGet(resourceCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resourceCategory
        restResourceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resourceCategoryRepository.selectCount(null);
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

    protected ResourceCategory getPersistedResourceCategory(ResourceCategory resourceCategory) {
        return resourceCategoryRepository.findById(resourceCategory.getId()).orElseThrow();
    }

    protected void assertPersistedResourceCategoryToMatchAllProperties(ResourceCategory expectedResourceCategory) {
        assertResourceCategoryAllPropertiesEquals(expectedResourceCategory, getPersistedResourceCategory(expectedResourceCategory));
    }

    protected void assertPersistedResourceCategoryToMatchUpdatableProperties(ResourceCategory expectedResourceCategory) {
        assertResourceCategoryAllUpdatablePropertiesEquals(
            expectedResourceCategory,
            getPersistedResourceCategory(expectedResourceCategory)
        );
    }
}
