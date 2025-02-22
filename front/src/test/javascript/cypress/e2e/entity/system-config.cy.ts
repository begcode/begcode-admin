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

describe('SystemConfig e2e test', () => {
  const basePageUrl = '/settings';
  const systemConfigPageUrl = '/settings/system-config';
  const systemConfigPageUrlPattern = new RegExp('/system-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const systemConfigSample = { categoryName: '扭 小 omelet', categoryKey: 'meh ugh underneath' };
  const localStorageValue: any = {};

  let systemConfig: any;

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
    cy.intercept('GET', '/api/system-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/system-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/system-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (systemConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-configs/${systemConfig.id}`,
      }).then(() => {
        systemConfig = undefined;
      });
    }
  });

  it('SystemConfigs menu should load SystemConfigs page', () => {
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
    cy.get(`[data-cy="${systemConfigPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SystemConfig').should('exist');
    cy.url().should('match', systemConfigPageUrlPattern);
  });

  describe('SystemConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(systemConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SystemConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/system-config/new$'));
        cy.getEntityCreateUpdateHeading('SystemConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/system-configs',
          body: systemConfigSample,
        }).then(({ body }) => {
          systemConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/system-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/system-configs?page=0&size=20>; rel="last",<http://localhost/api/system-configs?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [systemConfig],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(systemConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SystemConfig page', () => {
        cy.visit(systemConfigPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('systemConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemConfigPageUrlPattern);
      });

      it('edit button click should load edit SystemConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SystemConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemConfigPageUrlPattern);
      });

      it.skip('edit button click should load edit SystemConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SystemConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of SystemConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('systemConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', systemConfigPageUrlPattern);

        systemConfig = undefined;
      });
    });
  });

  describe('new SystemConfig page', () => {
    beforeEach(() => {
      cy.visit(`${systemConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SystemConfig');
    });

    it('should create an instance of SystemConfig', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_categoryName"]`).type('modeling 更加', { delay: 100 });
      cy.get(`[id="form_item_categoryName"]`).should('have.value', 'modeling 更加');

      cy.get(`[id="form_item_categoryKey"]`).type('duh', { delay: 100 });
      cy.get(`[id="form_item_categoryKey"]`).should('have.value', 'duh');

      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_disabled"]`).click();
      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_sortValue"]`).type('26532', { delay: 100 });
      cy.get(`[id="form_item_sortValue"]`).should('have.value', '26532');

      cy.get(`[id="form_item_builtIn"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_builtIn"]`).click();
      cy.get(`[id="form_item_builtIn"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        systemConfig = response?.body;
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
        cy.get(`[rowid="${systemConfig.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_categoryName"]').type('modeling 更加');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${systemConfig.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${systemConfig.id}"]`).should('exist');
      });
      cy.url().should('match', systemConfigPageUrlPattern);
    });
  });
});
