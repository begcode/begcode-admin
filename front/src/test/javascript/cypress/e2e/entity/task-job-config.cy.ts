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

describe('TaskJobConfig e2e test', () => {
  const basePageUrl = '/taskjob';
  const taskJobConfigPageUrl = '/taskjob/task-job-config';
  const taskJobConfigPageUrlPattern = new RegExp('/task-job-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const taskJobConfigSample = {};
  const localStorageValue: any = {};

  let taskJobConfig: any;

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
    cy.intercept('GET', '/api/task-job-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/task-job-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/task-job-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taskJobConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/task-job-configs/${taskJobConfig.id}`,
      }).then(() => {
        taskJobConfig = undefined;
      });
    }
  });

  it('TaskJobConfigs menu should load TaskJobConfigs page', () => {
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
    cy.get(`[data-cy="${taskJobConfigPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaskJobConfig').should('exist');
    cy.url().should('match', taskJobConfigPageUrlPattern);
  });

  describe('TaskJobConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taskJobConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaskJobConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/taskjob/task-job-config/new$'));
        cy.getEntityCreateUpdateHeading('TaskJobConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taskJobConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/task-job-configs',
          body: taskJobConfigSample,
        }).then(({ body }) => {
          taskJobConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/task-job-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/task-job-configs?page=0&size=20>; rel="last",<http://localhost/api/task-job-configs?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [taskJobConfig],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taskJobConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaskJobConfig page', () => {
        cy.visit(taskJobConfigPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('taskJobConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taskJobConfigPageUrlPattern);
      });

      it('edit button click should load edit TaskJobConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('TaskJobConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taskJobConfigPageUrlPattern);
      });

      it.skip('edit button click should load edit TaskJobConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('TaskJobConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taskJobConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of TaskJobConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('taskJobConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taskJobConfigPageUrlPattern);

        taskJobConfig = undefined;
      });
    });
  });

  describe('new TaskJobConfig page', () => {
    beforeEach(() => {
      cy.visit(`${taskJobConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaskJobConfig');
    });

    it('should create an instance of TaskJobConfig', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('别 the', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '别 the');

      cy.get(`[id="form_item_jobClassName"]`).type('必然 amidst', { delay: 100 });
      cy.get(`[id="form_item_jobClassName"]`).should('have.value', '必然 amidst');

      cy.get(`[id="form_item_cronExpression"]`).type('抬', { delay: 100 });
      cy.get(`[id="form_item_cronExpression"]`).should('have.value', '抬');

      cy.get(`[id="form_item_parameter"]`).type('甜 advice because', { delay: 100 });
      cy.get(`[id="form_item_parameter"]`).should('have.value', '甜 advice because');

      cy.get(`[id="form_item_description"]`).type('inasmuch unlike consequently', { delay: 100 });
      cy.get(`[id="form_item_description"]`).should('have.value', 'inasmuch unlike consequently');

      cy.get(`[id="form_item_jobStatus"]`).click().wait(100).type('ERROR{enter}', { force: true });

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taskJobConfig = response?.body;
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
        cy.get(`[rowid="${taskJobConfig.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('别 the');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${taskJobConfig.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${taskJobConfig.id}"]`).should('exist');
      });
      cy.url().should('match', taskJobConfigPageUrlPattern);
    });
  });
});
