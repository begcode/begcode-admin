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

describe('OssConfig e2e test', () => {
  const basePageUrl = '/files';
  const ossConfigPageUrl = '/files/oss-config';
  const ossConfigPageUrlPattern = new RegExp('/oss-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const ossConfigSample = { provider: 'MINIO', platform: '优秀 抛' };
  const localStorageValue: any = {};

  let ossConfig: any;

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
    cy.intercept('GET', '/api/oss-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/oss-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/oss-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ossConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/oss-configs/${ossConfig.id}`,
      }).then(() => {
        ossConfig = undefined;
      });
    }
  });

  it('OssConfigs menu should load OssConfigs page', () => {
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
    cy.get(`[data-cy="${ossConfigPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OssConfig').should('exist');
    cy.url().should('match', ossConfigPageUrlPattern);
  });

  describe('OssConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ossConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OssConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/oss-config/new$'));
        cy.getEntityCreateUpdateHeading('OssConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ossConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/oss-configs',
          body: ossConfigSample,
        }).then(({ body }) => {
          ossConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/oss-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/oss-configs?page=0&size=20>; rel="last",<http://localhost/api/oss-configs?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [ossConfig],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ossConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OssConfig page', () => {
        cy.visit(ossConfigPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('ossConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ossConfigPageUrlPattern);
      });

      it('edit button click should load edit OssConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('OssConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ossConfigPageUrlPattern);
      });

      it.skip('edit button click should load edit OssConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('OssConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ossConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of OssConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('ossConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ossConfigPageUrlPattern);

        ossConfig = undefined;
      });
    });
  });

  describe('new OssConfig page', () => {
    beforeEach(() => {
      cy.visit(`${ossConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OssConfig');
    });

    it('should create an instance of OssConfig', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_provider"]`).click().wait(100).type('ALI{enter}', { force: true });

      cy.get(`[id="form_item_platform"]`).type('伟大 magazine 渐渐', { delay: 100 });
      cy.get(`[id="form_item_platform"]`).should('have.value', '伟大 magazine 渐渐');

      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_enabled"]`).click();
      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_remark"]`).type('bah style', { delay: 100 });
      cy.get(`[id="form_item_remark"]`).should('have.value', 'bah style');

      cy.get(`[id="form_item_configData"]`).type('短', { delay: 100 });
      cy.get(`[id="form_item_configData"]`).should('have.value', '短');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ossConfig = response?.body;
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
        cy.get(`[rowid="${ossConfig.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_platform"]').type('伟大 magazine 渐渐');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${ossConfig.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${ossConfig.id}"]`).should('exist');
      });
      cy.url().should('match', ossConfigPageUrlPattern);
    });
  });
});
