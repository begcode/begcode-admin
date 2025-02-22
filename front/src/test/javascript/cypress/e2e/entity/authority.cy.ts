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

describe('Authority e2e test', () => {
  const basePageUrl = '/system';
  const authorityPageUrl = '/system/authority';
  const authorityPageUrlPattern = new RegExp('/authority(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const authoritySample = {};
  const localStorageValue: any = {};

  let authority: any;

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
    cy.intercept('GET', '/api/authorities/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/authorities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/authorities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (authority) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/authorities/${authority.id}`,
      }).then(() => {
        authority = undefined;
      });
    }
  });

  it('Authorities menu should load Authorities page', () => {
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
    cy.get(`[data-cy="${authorityPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Authority').should('exist');
    cy.url().should('match', authorityPageUrlPattern);
  });

  describe('Authority page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(authorityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Authority page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system/authority/new$'));
        cy.getEntityCreateUpdateHeading('Authority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', authorityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/authorities',
          body: authoritySample,
        }).then(({ body }) => {
          authority = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/authorities/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/authorities?page=0&size=20>; rel="last",<http://localhost/api/authorities?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [authority],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(authorityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Authority page', () => {
        cy.visit(authorityPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('authority');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', authorityPageUrlPattern);
      });

      it('edit button click should load edit Authority page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Authority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', authorityPageUrlPattern);
      });

      it.skip('edit button click should load edit Authority page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Authority');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', authorityPageUrlPattern);
      });

      it('last delete button click should delete instance of Authority', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('authority').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', authorityPageUrlPattern);

        authority = undefined;
      });
    });
  });

  describe('new Authority page', () => {
    beforeEach(() => {
      cy.visit(`${authorityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Authority');
    });

    it('should create an instance of Authority', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('mmm which', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', 'mmm which');

      cy.get(`[id="form_item_code"]`).type('urge per 勇敢', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', 'urge per 勇敢');

      cy.get(`[id="form_item_info"]`).type('above', { delay: 100 });
      cy.get(`[id="form_item_info"]`).should('have.value', 'above');

      cy.get(`[id="form_item_order"]`).type('24999', { delay: 100 });
      cy.get(`[id="form_item_order"]`).should('have.value', '24999');

      cy.get(`[id="form_item_display"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_display"]`).click();
      cy.get(`[id="form_item_display"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        authority = response?.body;
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
        cy.get(`[rowid="${authority.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('mmm which');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${authority.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${authority.id}"]`).should('exist');
      });
      cy.url().should('match', authorityPageUrlPattern);
    });
  });
});
