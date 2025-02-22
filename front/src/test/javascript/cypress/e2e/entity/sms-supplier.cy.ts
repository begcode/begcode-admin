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

describe('SmsSupplier e2e test', () => {
  const basePageUrl = '/files';
  const smsSupplierPageUrl = '/files/sms-supplier';
  const smsSupplierPageUrlPattern = new RegExp('/sms-supplier(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const smsSupplierSample = {};
  const localStorageValue: any = {};

  let smsSupplier: any;

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
    cy.intercept('GET', '/api/sms-suppliers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sms-suppliers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sms-suppliers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (smsSupplier) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sms-suppliers/${smsSupplier.id}`,
      }).then(() => {
        smsSupplier = undefined;
      });
    }
  });

  it('SmsSuppliers menu should load SmsSuppliers page', () => {
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
    cy.get(`[data-cy="${smsSupplierPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SmsSupplier').should('exist');
    cy.url().should('match', smsSupplierPageUrlPattern);
  });

  describe('SmsSupplier page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(smsSupplierPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SmsSupplier page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/sms-supplier/new$'));
        cy.getEntityCreateUpdateHeading('SmsSupplier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsSupplierPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sms-suppliers',
          body: smsSupplierSample,
        }).then(({ body }) => {
          smsSupplier = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sms-suppliers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sms-suppliers?page=0&size=20>; rel="last",<http://localhost/api/sms-suppliers?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [smsSupplier],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(smsSupplierPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SmsSupplier page', () => {
        cy.visit(smsSupplierPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('smsSupplier');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsSupplierPageUrlPattern);
      });

      it('edit button click should load edit SmsSupplier page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsSupplier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsSupplierPageUrlPattern);
      });

      it.skip('edit button click should load edit SmsSupplier page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsSupplier');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsSupplierPageUrlPattern);
      });

      it('last delete button click should delete instance of SmsSupplier', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('smsSupplier').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsSupplierPageUrlPattern);

        smsSupplier = undefined;
      });
    });
  });

  describe('new SmsSupplier page', () => {
    beforeEach(() => {
      cy.visit(`${smsSupplierPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SmsSupplier');
    });

    it('should create an instance of SmsSupplier', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_provider"]`).click().wait(100).type('TENCENT{enter}', { force: true });

      cy.get(`[id="form_item_configData"]`).type('when', { delay: 100 });
      cy.get(`[id="form_item_configData"]`).should('have.value', 'when');

      cy.get(`[id="form_item_signName"]`).type('攥 撕', { delay: 100 });
      cy.get(`[id="form_item_signName"]`).should('have.value', '攥 撕');

      cy.get(`[id="form_item_remark"]`).type('because inevitable boohoo', { delay: 100 });
      cy.get(`[id="form_item_remark"]`).should('have.value', 'because inevitable boohoo');

      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_enabled"]`).click();
      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        smsSupplier = response?.body;
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
        cy.get(`[rowid="${smsSupplier.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_configData"]').type('when');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${smsSupplier.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${smsSupplier.id}"]`).should('exist');
      });
      cy.url().should('match', smsSupplierPageUrlPattern);
    });
  });
});
