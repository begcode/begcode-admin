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

describe('SysLog e2e test', () => {
  const basePageUrl = '/log';
  const sysLogPageUrl = '/log/sys-log';
  const sysLogPageUrlPattern = new RegExp('/sys-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const sysLogSample = {};
  const localStorageValue: any = {};

  let sysLog: any;

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
    cy.intercept('GET', '/api/sys-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sys-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sys-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sysLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sys-logs/${sysLog.id}`,
      }).then(() => {
        sysLog = undefined;
      });
    }
  });

  it('SysLogs menu should load SysLogs page', () => {
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
    cy.get(`[data-cy="${sysLogPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SysLog').should('exist');
    cy.url().should('match', sysLogPageUrlPattern);
  });

  describe('SysLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sysLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SysLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/log/sys-log/new$'));
        cy.getEntityCreateUpdateHeading('SysLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sys-logs',
          body: sysLogSample,
        }).then(({ body }) => {
          sysLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sys-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sys-logs?page=0&size=20>; rel="last",<http://localhost/api/sys-logs?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [sysLog],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sysLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SysLog page', () => {
        cy.visit(sysLogPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('sysLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysLogPageUrlPattern);
      });

      it('edit button click should load edit SysLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SysLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysLogPageUrlPattern);
      });

      it.skip('edit button click should load edit SysLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SysLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysLogPageUrlPattern);
      });

      it('last delete button click should delete instance of SysLog', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('sysLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysLogPageUrlPattern);

        sysLog = undefined;
      });
    });
  });

  describe('new SysLog page', () => {
    beforeEach(() => {
      cy.visit(`${sysLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SysLog');
    });

    it('should create an instance of SysLog', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_requestUrl"]`).type('撬 including', { delay: 100 });
      cy.get(`[id="form_item_requestUrl"]`).should('have.value', '撬 including');

      cy.get(`[id="form_item_logType"]`).click().wait(100).type('LOGIN{enter}', { force: true });

      cy.get(`[id="form_item_logContent"]`).type('熟练 击', { delay: 100 });
      cy.get(`[id="form_item_logContent"]`).should('have.value', '熟练 击');

      cy.get(`[id="form_item_operateType"]`).click().wait(100).type('EXPORT{enter}', { force: true });

      cy.get(`[id="form_item_userid"]`).type('that', { delay: 100 });
      cy.get(`[id="form_item_userid"]`).should('have.value', 'that');

      cy.get(`[id="form_item_username"]`).type('oh vase 不', { delay: 100 });
      cy.get(`[id="form_item_username"]`).should('have.value', 'oh vase 不');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_ip"]`).type('长 极其', { delay: 100 });
      cy.get(`[id="form_item_ip"]`).should('have.value', '长 极其');

      cy.get(`[id="form_item_method"]`).type('甜', { delay: 100 });
      cy.get(`[id="form_item_method"]`).should('have.value', '甜');

      cy.get(`[id="form_item_requestParam"]`).type('../fake-data/blob/hipster.txt', { delay: 100 });
      cy.get(`[id="form_item_requestParam"]`).should('have.value', '../fake-data/blob/hipster.txt');

      cy.get(`[id="form_item_requestType"]`).type('mmm 蠕动 gadzooks', { delay: 100 });
      cy.get(`[id="form_item_requestType"]`).should('have.value', 'mmm 蠕动 gadzooks');

      cy.get(`[id="form_item_costTime"]`).type('6895', { delay: 100 });
      cy.get(`[id="form_item_costTime"]`).should('have.value', '6895');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        sysLog = response?.body;
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
        cy.get(`[rowid="${sysLog.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_requestUrl"]').type('撬 including');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${sysLog.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${sysLog.id}"]`).should('exist');
      });
      cy.url().should('match', sysLogPageUrlPattern);
    });
  });
});
