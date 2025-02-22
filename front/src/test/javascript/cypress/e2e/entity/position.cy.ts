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

describe('Position e2e test', () => {
  const basePageUrl = '/settings';
  const positionPageUrl = '/settings/position';
  const positionPageUrlPattern = new RegExp('/position(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const positionSample = { code: '没有', name: 'during which' };
  const localStorageValue: any = {};

  let position: any;

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
    cy.intercept('GET', '/api/positions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/positions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/positions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (position) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/positions/${position.id}`,
      }).then(() => {
        position = undefined;
      });
    }
  });

  it('Positions menu should load Positions page', () => {
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
    cy.get(`[data-cy="${positionPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Position').should('exist');
    cy.url().should('match', positionPageUrlPattern);
  });

  describe('Position page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(positionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Position page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/position/new$'));
        cy.getEntityCreateUpdateHeading('Position');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', positionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/positions',
          body: positionSample,
        }).then(({ body }) => {
          position = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/positions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/positions?page=0&size=20>; rel="last",<http://localhost/api/positions?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [position],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(positionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Position page', () => {
        cy.visit(positionPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('position');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', positionPageUrlPattern);
      });

      it('edit button click should load edit Position page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Position');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', positionPageUrlPattern);
      });

      it.skip('edit button click should load edit Position page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Position');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', positionPageUrlPattern);
      });

      it('last delete button click should delete instance of Position', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('position').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', positionPageUrlPattern);

        position = undefined;
      });
    });
  });

  describe('new Position page', () => {
    beforeEach(() => {
      cy.visit(`${positionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Position');
    });

    it('should create an instance of Position', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_code"]`).type('barring', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', 'barring');

      cy.get(`[id="form_item_name"]`).type('一道', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '一道');

      cy.get(`[id="form_item_sortNo"]`).type('15323', { delay: 100 });
      cy.get(`[id="form_item_sortNo"]`).should('have.value', '15323');

      cy.get(`[id="form_item_description"]`).type('liberty', { delay: 100 });
      cy.get(`[id="form_item_description"]`).should('have.value', 'liberty');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        position = response?.body;
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
        cy.get(`[rowid="${position.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_code"]').type('barring');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${position.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${position.id}"]`).should('exist');
      });
      cy.url().should('match', positionPageUrlPattern);
    });
  });
});
