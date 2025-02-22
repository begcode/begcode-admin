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

describe('SmsMessage e2e test', () => {
  const basePageUrl = '/system';
  const smsMessagePageUrl = '/system/sms-message';
  const smsMessagePageUrlPattern = new RegExp('/sms-message(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const smsMessageSample = {};
  const localStorageValue: any = {};

  let smsMessage: any;

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
    cy.intercept('GET', '/api/sms-messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sms-messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sms-messages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (smsMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sms-messages/${smsMessage.id}`,
      }).then(() => {
        smsMessage = undefined;
      });
    }
  });

  it('SmsMessages menu should load SmsMessages page', () => {
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
    cy.get(`[data-cy="${smsMessagePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SmsMessage').should('exist');
    cy.url().should('match', smsMessagePageUrlPattern);
  });

  describe('SmsMessage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(smsMessagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SmsMessage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system/sms-message/new$'));
        cy.getEntityCreateUpdateHeading('SmsMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsMessagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sms-messages',
          body: smsMessageSample,
        }).then(({ body }) => {
          smsMessage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sms-messages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sms-messages?page=0&size=20>; rel="last",<http://localhost/api/sms-messages?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [smsMessage],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(smsMessagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SmsMessage page', () => {
        cy.visit(smsMessagePageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('smsMessage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsMessagePageUrlPattern);
      });

      it('edit button click should load edit SmsMessage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsMessagePageUrlPattern);
      });

      it.skip('edit button click should load edit SmsMessage page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SmsMessage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsMessagePageUrlPattern);
      });

      it('last delete button click should delete instance of SmsMessage', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('smsMessage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', smsMessagePageUrlPattern);

        smsMessage = undefined;
      });
    });
  });

  describe('new SmsMessage page', () => {
    beforeEach(() => {
      cy.visit(`${smsMessagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SmsMessage');
    });

    it('should create an instance of SmsMessage', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_title"]`).type('whenever 整齐', { delay: 100 });
      cy.get(`[id="form_item_title"]`).should('have.value', 'whenever 整齐');

      cy.get(`[id="form_item_sendType"]`).click().wait(100).type('EMAIL{enter}', { force: true });

      cy.get(`[id="form_item_receiver"]`).type('熟练', { delay: 100 });
      cy.get(`[id="form_item_receiver"]`).should('have.value', '熟练');

      cy.get(`[id="form_item_params"]`).type('network and reconsideration', { delay: 100 });
      cy.get(`[id="form_item_params"]`).should('have.value', 'network and reconsideration');

      cy.setTinyMceContent('form_item_content_editor', 'fake-data/blob/hipster.txt');

      cy.get(`[id="form_item_sendTime"]`).click({ force: true }).wait(50).type('2024-01-01T03:30', { force: true, delay: 100 });
      cy.get(`[id="form_item_sendTime"]`).invoke('attr', 'title').should('eq', '2024-01-01T03:30');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_sendStatus"]`).click().wait(100).type('NOT_TRY{enter}', { force: true });

      cy.get(`[id="form_item_retryNum"]`).type('31631', { delay: 100 });
      cy.get(`[id="form_item_retryNum"]`).should('have.value', '31631');

      cy.get(`[id="form_item_failResult"]`).type('全', { delay: 100 });
      cy.get(`[id="form_item_failResult"]`).should('have.value', '全');

      cy.get(`[id="form_item_remark"]`).type('boo 轻松 as', { delay: 100 });
      cy.get(`[id="form_item_remark"]`).should('have.value', 'boo 轻松 as');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        smsMessage = response?.body;
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
        cy.get(`[rowid="${smsMessage.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_title"]').type('whenever 整齐');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${smsMessage.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${smsMessage.id}"]`).should('exist');
      });
      cy.url().should('match', smsMessagePageUrlPattern);
    });
  });
});
