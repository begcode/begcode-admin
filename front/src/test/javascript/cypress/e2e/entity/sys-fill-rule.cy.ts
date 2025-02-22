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

describe('SysFillRule e2e test', () => {
  const basePageUrl = '/settings';
  const sysFillRulePageUrl = '/settings/sys-fill-rule';
  const sysFillRulePageUrlPattern = new RegExp('/sys-fill-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const sysFillRuleSample = {};
  const localStorageValue: any = {};

  let sysFillRule: any;

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
    cy.intercept('GET', '/api/sys-fill-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sys-fill-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sys-fill-rules/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sysFillRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sys-fill-rules/${sysFillRule.id}`,
      }).then(() => {
        sysFillRule = undefined;
      });
    }
  });

  it('SysFillRules menu should load SysFillRules page', () => {
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
    cy.get(`[data-cy="${sysFillRulePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SysFillRule').should('exist');
    cy.url().should('match', sysFillRulePageUrlPattern);
  });

  describe('SysFillRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sysFillRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SysFillRule page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/sys-fill-rule/new$'));
        cy.getEntityCreateUpdateHeading('SysFillRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysFillRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sys-fill-rules',
          body: sysFillRuleSample,
        }).then(({ body }) => {
          sysFillRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sys-fill-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sys-fill-rules?page=0&size=20>; rel="last",<http://localhost/api/sys-fill-rules?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [sysFillRule],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sysFillRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SysFillRule page', () => {
        cy.visit(sysFillRulePageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('sysFillRule');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysFillRulePageUrlPattern);
      });

      it('edit button click should load edit SysFillRule page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SysFillRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysFillRulePageUrlPattern);
      });

      it.skip('edit button click should load edit SysFillRule page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('SysFillRule');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysFillRulePageUrlPattern);
      });

      it('last delete button click should delete instance of SysFillRule', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('sysFillRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysFillRulePageUrlPattern);

        sysFillRule = undefined;
      });
    });
  });

  describe('new SysFillRule page', () => {
    beforeEach(() => {
      cy.visit(`${sysFillRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SysFillRule');
    });

    it('should create an instance of SysFillRule', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('大约', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '大约');

      cy.get(`[id="form_item_code"]`).type('甜 冷 apud', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', '甜 冷 apud');

      cy.get(`[id="form_item_desc"]`).type('挡', { delay: 100 });
      cy.get(`[id="form_item_desc"]`).should('have.value', '挡');

      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_enabled"]`).click();
      cy.get(`[id="form_item_enabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_resetFrequency"]`).click().wait(100).type('CUSTOM{enter}', { force: true });

      cy.get(`[id="form_item_seqValue"]`).type('9263', { delay: 100 });
      cy.get(`[id="form_item_seqValue"]`).should('have.value', '9263');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_fillValue"]`).type('扭', { delay: 100 });
      cy.get(`[id="form_item_fillValue"]`).should('have.value', '扭');

      cy.get(`[id="form_item_implClass"]`).type('amid bah', { delay: 100 });
      cy.get(`[id="form_item_implClass"]`).should('have.value', 'amid bah');

      cy.get(`[id="form_item_params"]`).type('优秀', { delay: 100 });
      cy.get(`[id="form_item_params"]`).should('have.value', '优秀');

      cy.get(`[id="form_item_resetStartTime"]`).click({ force: true }).wait(50).type('2023-12-31T18:05', { force: true, delay: 100 });
      cy.get(`[id="form_item_resetStartTime"]`).invoke('attr', 'title').should('eq', '2023-12-31T18:05');

      cy.get(`[id="form_item_resetEndTime"]`).click({ force: true }).wait(50).type('2023-12-31T22:31', { force: true, delay: 100 });
      cy.get(`[id="form_item_resetEndTime"]`).invoke('attr', 'title').should('eq', '2023-12-31T22:31');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_resetTime"]`).click({ force: true }).wait(50).type('2023-12-31T20:32', { force: true, delay: 100 });
      cy.get(`[id="form_item_resetTime"]`).invoke('attr', 'title').should('eq', '2023-12-31T20:32');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        sysFillRule = response?.body;
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
        cy.get(`[rowid="${sysFillRule.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('大约');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${sysFillRule.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${sysFillRule.id}"]`).should('exist');
      });
      cy.url().should('match', sysFillRulePageUrlPattern);
    });
  });
});
