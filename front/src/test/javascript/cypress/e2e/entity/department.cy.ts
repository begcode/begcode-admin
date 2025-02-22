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

describe('Department e2e test', () => {
  const basePageUrl = '/settings';
  const departmentPageUrl = '/settings/department';
  const departmentPageUrlPattern = new RegExp('/department(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const departmentSample = {};
  const localStorageValue: any = {};

  let department: any;

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
    cy.intercept('GET', '/api/departments/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/departments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/departments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (department) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/departments/${department.id}`,
      }).then(() => {
        department = undefined;
      });
    }
  });

  it('Departments menu should load Departments page', () => {
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
    cy.get(`[data-cy="${departmentPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Department').should('exist');
    cy.url().should('match', departmentPageUrlPattern);
  });

  describe('Department page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(departmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Department page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/settings/department/new$'));
        cy.getEntityCreateUpdateHeading('Department');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/departments',
          body: departmentSample,
        }).then(({ body }) => {
          department = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/departments/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/departments?page=0&size=20>; rel="last",<http://localhost/api/departments?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [department],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(departmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Department page', () => {
        cy.visit(departmentPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('department');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentPageUrlPattern);
      });

      it('edit button click should load edit Department page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Department');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentPageUrlPattern);
      });

      it.skip('edit button click should load edit Department page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Department');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentPageUrlPattern);
      });

      it('last delete button click should delete instance of Department', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('department').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentPageUrlPattern);

        department = undefined;
      });
    });
  });

  describe('new Department page', () => {
    beforeEach(() => {
      cy.visit(`${departmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Department');
    });

    it('should create an instance of Department', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_name"]`).type('共 细 关闭', { delay: 100 });
      cy.get(`[id="form_item_name"]`).should('have.value', '共 细 关闭');

      cy.get(`[id="form_item_code"]`).type('短 yuck onto', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', '短 yuck onto');

      cy.get(`[id="form_item_address"]`).type('excellence if baggie', { delay: 100 });
      cy.get(`[id="form_item_address"]`).should('have.value', 'excellence if baggie');

      cy.get(`[id="form_item_phoneNum"]`).type('多么 increase', { delay: 100 });
      cy.get(`[id="form_item_phoneNum"]`).should('have.value', '多么 increase');

      cy.get(`[id="form_item_logo"]`).type('小 拖', { delay: 100 });
      cy.get(`[id="form_item_logo"]`).should('have.value', '小 拖');

      cy.get(`[id="form_item_contact"]`).type('撒 必定 更加', { delay: 100 });
      cy.get(`[id="form_item_contact"]`).should('have.value', '撒 必定 更加');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        department = response?.body;
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
        cy.get(`[rowid="${department.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_name"]').type('共 细 关闭');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${department.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${department.id}"]`).should('exist');
      });
      cy.url().should('match', departmentPageUrlPattern);
    });
  });
});
