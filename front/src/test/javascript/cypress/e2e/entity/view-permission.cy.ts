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

describe('ViewPermission e2e test', () => {
  const basePageUrl = '/system';
  const viewPermissionPageUrl = '/system/view-permission';
  const viewPermissionPageUrlPattern = new RegExp('/view-permission(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const viewPermissionSample = { text: 'brr if', code: '提 bah' };
  const localStorageValue: any = {};

  let viewPermission: any;

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
    cy.intercept('GET', '/api/view-permissions/tree+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/view-permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/view-permissions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (viewPermission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/view-permissions/${viewPermission.id}`,
      }).then(() => {
        viewPermission = undefined;
      });
    }
  });

  it('ViewPermissions menu should load ViewPermissions page', () => {
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
    cy.get(`[data-cy="${viewPermissionPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ViewPermission').should('exist');
    cy.url().should('match', viewPermissionPageUrlPattern);
  });

  describe('ViewPermission page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(viewPermissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ViewPermission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system/view-permission/new$'));
        cy.getEntityCreateUpdateHeading('ViewPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewPermissionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/view-permissions',
          body: viewPermissionSample,
        }).then(({ body }) => {
          viewPermission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/view-permissions/tree+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/view-permissions?page=0&size=20>; rel="last",<http://localhost/api/view-permissions?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [viewPermission],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(viewPermissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ViewPermission page', () => {
        cy.visit(viewPermissionPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('viewPermission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewPermissionPageUrlPattern);
      });

      it('edit button click should load edit ViewPermission page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ViewPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewPermissionPageUrlPattern);
      });

      it.skip('edit button click should load edit ViewPermission page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('ViewPermission');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewPermissionPageUrlPattern);
      });

      it('last delete button click should delete instance of ViewPermission', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('viewPermission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewPermissionPageUrlPattern);

        viewPermission = undefined;
      });
    });
  });

  describe('new ViewPermission page', () => {
    beforeEach(() => {
      cy.visit(`${viewPermissionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ViewPermission');
    });

    it('should create an instance of ViewPermission', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_text"]`).type('below', { delay: 100 });
      cy.get(`[id="form_item_text"]`).should('have.value', 'below');

      cy.get(`[id="form_item_type"]`).click().wait(100).type('MENU{enter}', { force: true });

      cy.get(`[id="form_item_localeKey"]`).type('坐 很 马虎', { delay: 100 });
      cy.get(`[id="form_item_localeKey"]`).should('have.value', '坐 很 马虎');

      cy.get(`[id="form_item_group"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_group"]`).click();
      cy.get(`[id="form_item_group"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_link"]`).type('一道 略微 who', { delay: 100 });
      cy.get(`[id="form_item_link"]`).should('have.value', '一道 略微 who');

      cy.get(`[id="form_item_externalLink"]`).type('快 更加 eek', { delay: 100 });
      cy.get(`[id="form_item_externalLink"]`).should('have.value', '快 更加 eek');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_target"]`).click().wait(100).type('PARENT{enter}', { force: true });

      cy.get(`[id="form_item_icon"]`).type('许多 干脆', { delay: 100 });
      cy.get(`[id="form_item_icon"]`).should('have.value', '许多 干脆');

      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_disabled"]`).click();
      cy.get(`[id="form_item_disabled"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_hide"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_hide"]`).click();
      cy.get(`[id="form_item_hide"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_hideInBreadcrumb"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_hideInBreadcrumb"]`).click();
      cy.get(`[id="form_item_hideInBreadcrumb"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_shortcut"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_shortcut"]`).click();
      cy.get(`[id="form_item_shortcut"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_shortcutRoot"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_shortcutRoot"]`).click();
      cy.get(`[id="form_item_shortcutRoot"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_reuse"]`).invoke('attr', 'aria-checked').should('eq', 'false');
      cy.get(`[id="form_item_reuse"]`).click();
      cy.get(`[id="form_item_reuse"]`).invoke('attr', 'aria-checked').should('eq', 'true');

      cy.get(`[id="form_item_code"]`).type('却', { delay: 100 });
      cy.get(`[id="form_item_code"]`).should('have.value', '却');

      cy.get(`[id="form_item_description"]`).type('afore', { delay: 100 });
      cy.get(`[id="form_item_description"]`).should('have.value', 'afore');

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_order"]`).type('28647', { delay: 100 });
      cy.get(`[id="form_item_order"]`).should('have.value', '28647');

      cy.get(`[id="form_item_apiPermissionCodes"]`).type('马虎 bog', { delay: 100 });
      cy.get(`[id="form_item_apiPermissionCodes"]`).should('have.value', '马虎 bog');

      cy.get(`[id="form_item_componentFile"]`).type('psst 少 verse', { delay: 100 });
      cy.get(`[id="form_item_componentFile"]`).should('have.value', 'psst 少 verse');

      cy.get(`[id="form_item_redirect"]`).type('好些', { delay: 100 });
      cy.get(`[id="form_item_redirect"]`).should('have.value', '好些');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        viewPermission = response?.body;
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
        cy.get(`[rowid="${viewPermission.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_text"]').type('below');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${viewPermission.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${viewPermission.id}"]`).should('exist');
      });
      cy.url().should('match', viewPermissionPageUrlPattern);
    });
  });
});
