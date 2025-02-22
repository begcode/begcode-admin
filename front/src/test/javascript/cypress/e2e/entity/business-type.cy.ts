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

describe('BusinessType e2e test', () => {
  const basePageUrl = '/settings';
  const businessTypePageUrl = '/settings/business-type';
  const businessTypePageUrlPattern = new RegExp('/business-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const businessTypeSample = {};
  const localStorageValue: any = {};

  let businessType: any;

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
    cy.intercept('GET', '/api/business-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/business-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/business-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (businessType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-types/${businessType.id}`,
      }).then(() => {
        businessType = undefined;
      });
    }
  });

  it('BusinessTypes menu should load BusinessTypes page', () => {
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
    cy.get(`[data-cy="${businessTypePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BusinessType').should('exist');
    cy.url().should('match', businessTypePageUrlPattern);
  });

  describe('BusinessType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(businessTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BusinessType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/business-type/new$'));
        cy.getEntityCreateUpdateHeading('BusinessType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', businessTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/business-types',
          body: businessTypeSample,
        }).then(({ body }) => {
          businessType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/business-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/business-types?page=0&size=20>; rel="last",<http://localhost/api/business-types?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [businessType],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(businessTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BusinessType page', () => {
        cy.visit(businessTypePageUrl);
        cy.wait(300);
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('businessType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', businessTypePageUrlPattern);
      });

      it('edit button click should load edit BusinessType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('BusinessType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', businessTypePageUrlPattern);
      });

      it.skip('edit button click should load edit BusinessType page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('BusinessType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', businessTypePageUrlPattern);
      });

      it('last delete button click should delete instance of BusinessType', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('businessType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', businessTypePageUrlPattern);

        businessType = undefined;
      });
    });
  });

  describe('new BusinessType page', () => {
    beforeEach(() => {
      cy.visit(`${businessTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BusinessType');
    });

    it('should create an instance of BusinessType', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('聪明', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '聪明');

      cy.get(`[id="form_item_code"]`).type('pace after', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', 'pace after');

      cy.get(`[id="form_item_description"]`).type('geez 绿油油 硬', { delay: 100 });
      cy.get(`[id="form_item_description"]`).should('have.value', 'geez 绿油油 硬');

      cy.get(`[id="form_item_icon"]`).type('整 多亏', { delay: 100 });
      cy.get(`[id="form_item_icon"]`).should('have.value', '整 多亏');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        businessType = response?.body;
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
        cy.get(`[rowid="${businessType.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('聪明');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${businessType.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${businessType.id}"]`).should('exist');
      });
      cy.url().should('match', businessTypePageUrlPattern);
    });
  });
});
