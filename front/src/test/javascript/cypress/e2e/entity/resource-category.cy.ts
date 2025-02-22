import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
  listSearchInputSelector,
  listSearchButtonSelector,
  listSearchMoreSelector,
  listSearchFormSelector,
  searchFormSubmitSelector,
  searchFormResetSelector,
  formAdvanceToggleSelector,
} from '../../support/entity';
import {
  passwordLoginSelector,
  leftMenuSelector,
  submitLoginSelector,
  showMenuSelector,
  titleLoginSelector,
  usernameLoginSelector,
} from '../../support/commands';

describe('ResourceCategory e2e test', () => {
  const basePageUrl = '/files';
  const resourceCategoryPageUrl = '/files/resource-category';
  const resourceCategoryPageUrlPattern = new RegExp('/resource-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const resourceCategorySample = {};
  const localStorageValue: any = {};

  let resourceCategory: any;

  beforeEach(() => {
    if (Object.keys(localStorageValue).length === 0) {
      cy.visit('/dashboard/analysis');
      cy.get(titleLoginSelector).should('be.visible');
      cy.get(usernameLoginSelector).clear().type(username);
      cy.get(passwordLoginSelector).clear().type(password);
      cy.get(submitLoginSelector).click();
      cy.wait(1000);
      cy.authenticatedRequest({
        method: 'POST',
        body: { username, password },
        url: Cypress.env('authenticationUrl'),
      }).then(({ body: { id_token } }) => {
        localStorage.setItem(Cypress.env('jwtStorageName'), id_token);
        localStorageValue[Cypress.env('jwtStorageName')] = id_token;
      });
      cy.getAllLocalStorage().then(ls => {
        Object.assign(localStorageValue, ls!['http://localhost:3100']);
      });
    } else {
      Object.keys(localStorageValue).forEach(key => {
        localStorage.setItem(key, localStorageValue[key]);
        sessionStorage.setItem(key, localStorageValue[key]);
      });
      cy.wait(100);
      cy.visit('/dashboard/analysis');
      cy.wait(1000);
    }
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/resource-categories/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/resource-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/resource-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (resourceCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/resource-categories/${resourceCategory.id}`,
      }).then(() => {
        resourceCategory = undefined;
      });
    }
  });

  it('ResourceCategories menu should load ResourceCategories page', () => {
    cy
      .get(leftMenuSelector)
      ?.invoke('width')
      .then(value => {
        if (!value || value < 100) {
          cy.get(showMenuSelector).click();
        }
      });
    cy.wait(500);
    cy.get(`[data-cy="${basePageUrl}"]`).click();
    cy.wait(500);
    cy.get(`[data-cy="${resourceCategoryPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ResourceCategory').should('exist');
    cy.url().should('match', resourceCategoryPageUrlPattern);
  });

  describe('ResourceCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(resourceCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ResourceCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/resource-category/new$'));
        cy.getEntityCreateUpdateHeading('ResourceCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/resource-categories',
          body: resourceCategorySample,
        }).then(({ body }) => {
          resourceCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/resource-categories/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/resource-categories?page=0&size=20>; rel="last",<http://localhost/api/resource-categories?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [resourceCategory],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(resourceCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ResourceCategory page', () => {
        cy.visit(resourceCategoryPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('resourceCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceCategoryPageUrlPattern);
      });

      it('edit button click should load edit ResourceCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ResourceCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceCategoryPageUrlPattern);
      });

      it.skip('edit button click should load edit ResourceCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ResourceCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ResourceCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('resourceCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', resourceCategoryPageUrlPattern);

        resourceCategory = undefined;
      });
    });
  });

  describe('new ResourceCategory page', () => {
    beforeEach(() => {
      cy.visit(`${resourceCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ResourceCategory');
    });

    it('should create an instance of ResourceCategory', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_title"]`).type('yowza absent boo', { delay: 100 });
      cy.get(`[id="form_item_title"]`).should('have.value', 'yowza absent boo');

      cy.get(`[id="form_item_code"]`).type('fireplace boo', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', 'fireplace boo');

      cy.get(`[id="form_item_orderNumber"]`).type('30845', { delay: 100 });
      cy.get(`[id="form_item_orderNumber"]`).should('have.value', '30845');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        resourceCategory = response?.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
        cy.get(entityCreateSaveButtonSelector).contains('更新');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait(200);
        cy.get(listSearchInputSelector).clear().type('nectarine');
        cy.get(listSearchButtonSelector).click();
        cy.wait(500);
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${resourceCategory.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_title"]').type('yowza absent boo');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${resourceCategory.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${resourceCategory.id}"]`).should('exist');
      });
      cy.url().should('match', resourceCategoryPageUrlPattern);
    });
  });
});
