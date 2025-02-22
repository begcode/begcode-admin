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

describe('Dictionary e2e test', () => {
  const basePageUrl = '/settings';
  const dictionaryPageUrl = '/settings/dictionary';
  const dictionaryPageUrlPattern = new RegExp('/dictionary(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const dictionarySample = { dictName: '粗', dictKey: 'woot sermon oof' };
  const localStorageValue: any = {};

  let dictionary: any;

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
    cy.intercept('GET', '/api/dictionaries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dictionaries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dictionaries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dictionary) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dictionaries/${dictionary.id}`,
      }).then(() => {
        dictionary = undefined;
      });
    }
  });

  it('Dictionaries menu should load Dictionaries page', () => {
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
    cy.get(`[data-cy="${dictionaryPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dictionary').should('exist');
    cy.url().should('match', dictionaryPageUrlPattern);
  });

  describe('Dictionary page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dictionaryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dictionary page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/dictionary/new$'));
        cy.getEntityCreateUpdateHeading('Dictionary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dictionaryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dictionaries',
          body: dictionarySample,
        }).then(({ body }) => {
          dictionary = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dictionaries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/dictionaries?page=0&size=20>; rel="last",<http://localhost/api/dictionaries?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [dictionary],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dictionaryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dictionary page', () => {
        cy.visit(dictionaryPageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('dictionary');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dictionaryPageUrlPattern);
      });

      it('edit button click should load edit Dictionary page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Dictionary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dictionaryPageUrlPattern);
      });

      it.skip('edit button click should load edit Dictionary page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Dictionary');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dictionaryPageUrlPattern);
      });

      it('last delete button click should delete instance of Dictionary', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('dictionary').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dictionaryPageUrlPattern);

        dictionary = undefined;
      });
    });
  });

  describe('new Dictionary page', () => {
    beforeEach(() => {
      cy.visit(`${dictionaryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dictionary');
    });

    it('should create an instance of Dictionary', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_dictName"]`).type('好在', { delay: 100 });
      cy.get(`[id="form_item_dictName"]`).should('have.value', '好在');

      cy.get(`[id="form_item_dictKey"]`).type('boohoo 猛然', { delay: 100 });
      cy.get(`[id="form_item_dictKey"]`).should('have.value', 'boohoo 猛然');

      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_disabled"]`).click();
      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_sortValue"]`).type('5417', { delay: 100 });
      cy.get(`[id="form_item_sortValue"]`).should('have.value', '5417');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        dictionary = response?.body;
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
        cy.get(`[rowid="${dictionary.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_dictName"]').type('好在');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${dictionary.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${dictionary.id}"]`).should('exist');
      });
      cy.url().should('match', dictionaryPageUrlPattern);
    });
  });
});
