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

describe('RegionCode e2e test', () => {
  const basePageUrl = '/settings';
  const regionCodePageUrl = '/settings/region-code';
  const regionCodePageUrlPattern = new RegExp('/region-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const regionCodeSample = {};
  const localStorageValue: any = {};

  let regionCode: any;

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
    cy.intercept('GET', '/api/region-codes/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/region-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/region-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (regionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/region-codes/${regionCode.id}`,
      }).then(() => {
        regionCode = undefined;
      });
    }
  });

  it('RegionCodes menu should load RegionCodes page', () => {
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
    cy.get(`[data-cy="${regionCodePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RegionCode').should('exist');
    cy.url().should('match', regionCodePageUrlPattern);
  });

  describe('RegionCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(regionCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RegionCode page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/region-code/new$'));
        cy.getEntityCreateUpdateHeading('RegionCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', regionCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/region-codes',
          body: regionCodeSample,
        }).then(({ body }) => {
          regionCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/region-codes/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/region-codes?page=0&size=20>; rel="last",<http://localhost/api/region-codes?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [regionCode],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(regionCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RegionCode page', () => {
        cy.visit(regionCodePageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('regionCode');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', regionCodePageUrlPattern);
      });

      it('edit button click should load edit RegionCode page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('RegionCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', regionCodePageUrlPattern);
      });

      it.skip('edit button click should load edit RegionCode page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('RegionCode');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', regionCodePageUrlPattern);
      });

      it('last delete button click should delete instance of RegionCode', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('regionCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', regionCodePageUrlPattern);

        regionCode = undefined;
      });
    });
  });

  describe('new RegionCode page', () => {
    beforeEach(() => {
      cy.visit(`${regionCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RegionCode');
    });

    it('should create an instance of RegionCode', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('坚固', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '坚固');

      cy.get(`[id="form_item_areaCode"]`).type('and modulo', { delay: 100 });
      cy.get(`[id="form_item_areaCode"]`).should('have.value', 'and modulo');

      cy.get(`[id="form_item_cityCode"]`).type('next atop unless', { delay: 100 });
      cy.get(`[id="form_item_cityCode"]`).should('have.value', 'next atop unless');

      cy.get(`[id="form_item_mergerName"]`).type('now', { delay: 100 });
      cy.get(`[id="form_item_mergerName"]`).should('have.value', 'now');

      cy.get(`[id="form_item_shortName"]`).type('共 oh 稍微', { delay: 100 });
      cy.get(`[id="form_item_shortName"]`).should('have.value', '共 oh 稍微');

      cy.get(`[id="form_item_zipCode"]`).type('380472', { delay: 100 });
      cy.get(`[id="form_item_zipCode"]`).should('have.value', '380472');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_level"]`).click().wait(100).type('CITY{enter}', { force: true });

      cy.get(`[id="form_item_lng"]`).type('20660.87', { delay: 100 });
      cy.get(`[id="form_item_lng"]`).should('have.value', '20660.87');

      cy.get(`[id="form_item_lat"]`).type('16317.42', { delay: 100 });
      cy.get(`[id="form_item_lat"]`).should('have.value', '16317.42');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        regionCode = response?.body;
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
        cy.get(`[rowid="${regionCode.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('坚固');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${regionCode.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${regionCode.id}"]`).should('exist');
      });
      cy.url().should('match', regionCodePageUrlPattern);
    });
  });
});
