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

describe('ApiPermission e2e test', () => {
  const basePageUrl = '/system';
  const apiPermissionPageUrl = '/system/api-permission';
  const apiPermissionPageUrlPattern = new RegExp('/api-permission(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const apiPermissionSample = {};
  const localStorageValue: any = {};

  let apiPermission: any;

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
    cy.intercept('GET', '/api/api-permissions/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/api-permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/api-permissions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (apiPermission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/api-permissions/${apiPermission.id}`,
      }).then(() => {
        apiPermission = undefined;
      });
    }
  });

  it('ApiPermissions menu should load ApiPermissions page', () => {
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
    cy.get(`[data-cy="${apiPermissionPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApiPermission').should('exist');
    cy.url().should('match', apiPermissionPageUrlPattern);
  });

  describe('ApiPermission page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(apiPermissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApiPermission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system/api-permission/new$'));
        cy.getEntityCreateUpdateHeading('ApiPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', apiPermissionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/api-permissions',
          body: apiPermissionSample,
        }).then(({ body }) => {
          apiPermission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/api-permissions/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/api-permissions?page=0&size=20>; rel="last",<http://localhost/api/api-permissions?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [apiPermission],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(apiPermissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApiPermission page', () => {
        cy.visit(apiPermissionPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('apiPermission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', apiPermissionPageUrlPattern);
      });

      it('edit button click should load edit ApiPermission page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ApiPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', apiPermissionPageUrlPattern);
      });

      it.skip('edit button click should load edit ApiPermission page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ApiPermission');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', apiPermissionPageUrlPattern);
      });

      it('last delete button click should delete instance of ApiPermission', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('apiPermission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', apiPermissionPageUrlPattern);

        apiPermission = undefined;
      });
    });
  });

  describe('new ApiPermission page', () => {
    beforeEach(() => {
      cy.visit(`${apiPermissionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApiPermission');
    });

    it('should create an instance of ApiPermission', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_serviceName"]`).type('多么', { delay: 100 });
      cy.get(`[id="form_item_serviceName"]`).should('have.value', '多么');

      cy.get(`[id="form_item_name"]`).type('粗', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '粗');

      cy.get(`[id="form_item_code"]`).type('伟大 嚼 indeed', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', '伟大 嚼 indeed');

      cy.get(`[id="form_item_description"]`).type('撒 提', { delay: 100 });
      cy.get(`[id="form_item_description"]`).should('have.value', '撒 提');

      cy.get(`[id="form_item_type"]`).click().wait(100).type('MICRO_SERVICE{enter}', { force: true });

      cy.get(`[id="form_item_method"]`).type('save versus kick-off', { delay: 100 });
      cy.get(`[id="form_item_method"]`).should('have.value', 'save versus kick-off');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_url"]`).type('https://-cyclone.com', { delay: 100 });
      cy.get(`[id="form_item_url"]`).should('have.value', 'https://-cyclone.com');

      cy.get(`[id="form_item_status"]`).click().wait(100).type('CONFIGURABLE{enter}', { force: true });

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        apiPermission = response?.body;
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
        cy.get(`[rowid="${apiPermission.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_serviceName"]').type('多么');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${apiPermission.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${apiPermission.id}"]`).should('exist');
      });
      cy.url().should('match', apiPermissionPageUrlPattern);
    });
  });
});
