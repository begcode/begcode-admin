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

describe('FormConfig e2e test', () => {
  const basePageUrl = '/settings';
  const formConfigPageUrl = '/settings/form-config';
  const formConfigPageUrlPattern = new RegExp('/form-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const formConfigSample = { formKey: 'library pfft 都', formName: '剪 than below' };
  const localStorageValue: any = {};

  let formConfig: any;

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
    cy.intercept('GET', '/api/form-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/form-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/form-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/form-configs/${formConfig.id}`,
      }).then(() => {
        formConfig = undefined;
      });
    }
  });

  it('FormConfigs menu should load FormConfigs page', () => {
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
    cy.get(`[data-cy="${formConfigPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormConfig').should('exist');
    cy.url().should('match', formConfigPageUrlPattern);
  });

  describe('FormConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/form-config/new$'));
        cy.getEntityCreateUpdateHeading('FormConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', formConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/form-configs',
          body: formConfigSample,
        }).then(({ body }) => {
          formConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/form-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/form-configs?page=0&size=20>; rel="last",<http://localhost/api/form-configs?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [formConfig],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormConfig page', () => {
        cy.visit(formConfigPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('formConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', formConfigPageUrlPattern);
      });

      it('edit button click should load edit FormConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('FormConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', formConfigPageUrlPattern);
      });

      it.skip('edit button click should load edit FormConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('FormConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', formConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of FormConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('formConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', formConfigPageUrlPattern);

        formConfig = undefined;
      });
    });
  });

  describe('new FormConfig page', () => {
    beforeEach(() => {
      cy.visit(`${formConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormConfig');
    });

    it('should create an instance of FormConfig', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_formKey"]`).type('by 只', { delay: 100 });
      cy.get(`[id="form_item_formKey"]`).should('have.value', 'by 只');

      cy.get(`[id="form_item_formName"]`).type('没有', { delay: 100 });
      cy.get(`[id="form_item_formName"]`).should('have.value', '没有');

      cy.get(`[id="form_item_formType"]`).click().wait(100).type('DATA_FORM{enter}', { force: true });

      cy.get(`[id="form_item_multiItems"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_multiItems"]`).click();
      cy.get(`[id="form_item_multiItems"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        formConfig = response?.body;
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
        cy.get(`[rowid="${formConfig.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_formKey"]').type('by 只');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${formConfig.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${formConfig.id}"]`).should('exist');
      });
      cy.url().should('match', formConfigPageUrlPattern);
    });
  });
});
