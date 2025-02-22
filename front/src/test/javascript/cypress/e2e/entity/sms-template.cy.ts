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

describe('SmsTemplate e2e test', () => {
  const basePageUrl = '/files';
  const smsTemplatePageUrl = '/files/sms-template';
  const smsTemplatePageUrlPattern = new RegExp('/sms-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const smsTemplateSample = {};
  const localStorageValue: any = {};

  let smsTemplate: any;

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
    cy.intercept('GET', '/api/sms-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sms-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sms-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (smsTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sms-templates/${smsTemplate.id}`,
      }).then(() => {
        smsTemplate = undefined;
      });
    }
  });

  it('SmsTemplates menu should load SmsTemplates page', () => {
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
    cy.get(`[data-cy="${smsTemplatePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SmsTemplate').should('exist');
    cy.url().should('match', smsTemplatePageUrlPattern);
  });

  describe('SmsTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(smsTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SmsTemplate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/sms-template/new$'));
        cy.getEntityCreateUpdateHeading('SmsTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sms-templates',
          body: smsTemplateSample,
        }).then(({ body }) => {
          smsTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sms-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sms-templates?page=0&size=20>; rel="last",<http://localhost/api/sms-templates?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [smsTemplate],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(smsTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SmsTemplate page', () => {
        cy.visit(smsTemplatePageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('smsTemplate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsTemplatePageUrlPattern);
      });

      it('edit button click should load edit SmsTemplate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsTemplatePageUrlPattern);
      });

      it.skip('edit button click should load edit SmsTemplate page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsTemplate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of SmsTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('smsTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsTemplatePageUrlPattern);

        smsTemplate = undefined;
      });
    });
  });

  describe('new SmsTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${smsTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SmsTemplate');
    });

    it('should create an instance of SmsTemplate', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('渐渐', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '渐渐');

      cy.get(`[id="form_item_code"]`).type('fooey indeed', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', 'fooey indeed');

      cy.get(`[id="form_item_sendType"]`).click().wait(100).type('SMS{enter}', { force: true });

      cy.get(`[id="form_item_content"]`).type('about who', { delay: 100 });
      cy.get(`[id="form_item_content"]`).should('have.value', 'about who');

      cy.get(`[id="form_item_testJson"]`).type('what drat', { delay: 100 });
      cy.get(`[id="form_item_testJson"]`).should('have.value', 'what drat');

      cy.get(`[id="form_item_type"]`).click().wait(100).type('MESSAGE{enter}', { force: true });

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_remark"]`).type('ascot', { delay: 100 });
      cy.get(`[id="form_item_remark"]`).should('have.value', 'ascot');

      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_enabled"]`).click();
      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        smsTemplate = response?.body;
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
        cy.get(`[rowid="${smsTemplate.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('渐渐');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${smsTemplate.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${smsTemplate.id}"]`).should('exist');
      });
      cy.url().should('match', smsTemplatePageUrlPattern);
    });
  });
});
