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

describe('Announcement e2e test', () => {
  const basePageUrl = '/system';
  const announcementPageUrl = '/system/announcement';
  const announcementPageUrlPattern = new RegExp('/announcement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const announcementSample = { title: 'atop undershirt ouch', receiverType: 'DEPARTMENT' };
  const localStorageValue: any = {};

  let announcement: any;

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
    cy.intercept('GET', '/api/announcements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/announcements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/announcements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (announcement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/announcements/${announcement.id}`,
      }).then(() => {
        announcement = undefined;
      });
    }
  });

  it('Announcements menu should load Announcements page', () => {
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
    cy.get(`[data-cy="${announcementPageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Announcement').should('exist');
    cy.url().should('match', announcementPageUrlPattern);
  });

  describe('Announcement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(announcementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Announcement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/system/announcement/new$'));
        cy.getEntityCreateUpdateHeading('Announcement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', announcementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/announcements',
          body: announcementSample,
        }).then(({ body }) => {
          announcement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/announcements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/announcements?page=0&size=20>; rel="last",<http://localhost/api/announcements?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [announcement],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(announcementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Announcement page', () => {
        cy.visit(announcementPageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('announcement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', announcementPageUrlPattern);
      });

      it('edit button click should load edit Announcement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Announcement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', announcementPageUrlPattern);
      });

      it.skip('edit button click should load edit Announcement page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('Announcement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', announcementPageUrlPattern);
      });

      it('last delete button click should delete instance of Announcement', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('announcement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', announcementPageUrlPattern);

        announcement = undefined;
      });
    });
  });

  describe('new Announcement page', () => {
    beforeEach(() => {
      cy.visit(`${announcementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Announcement');
    });

    it('should create an instance of Announcement', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_title"]`).type('until pfft 仍然', { delay: 100 });
      cy.get(`[id="form_item_title"]`).should('have.value', 'until pfft 仍然');

      cy.get(`[id="form_item_summary"]`).type('../fake-data/blob/hipster.txt', { delay: 100 });
      cy.get(`[id="form_item_summary"]`).should('have.value', '../fake-data/blob/hipster.txt');

      cy.setTinyMceContent('form_item_content_editor', 'fake-data/blob/hipster.txt');

      cy.get(`[id="form_item_startTime"]`).click({ force: true }).wait(50).type('2023-12-31T07:24', { force: true, delay: 100 });
      cy.get(`[id="form_item_startTime"]`).invoke('attr', 'title').should('eq', '2023-12-31T07:24');

      cy.get(`[id="form_item_endTime"]`).click({ force: true }).wait(50).type('2023-12-31T09:40', { force: true, delay: 100 });
      cy.get(`[id="form_item_endTime"]`).invoke('attr', 'title').should('eq', '2023-12-31T09:40');

      cy.get(`[id="form_item_priority"]`).click().wait(100).type('HIGH{enter}', { force: true });

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(`[id="form_item_category"]`).click().wait(100).type('NOTICE{enter}', { force: true });

      cy.get(`[id="form_item_receiverType"]`).click().wait(100).type('AUTHORITY{enter}', { force: true });

      cy.get(`[id="form_item_openType"]`).click().wait(100).type('URL{enter}', { force: true });

      cy.get(`[id="form_item_openPage"]`).type('索性 尽', { delay: 100 });
      cy.get(`[id="form_item_openPage"]`).should('have.value', '索性 尽');

      cy.get(`[id="form_item_receiverIds"]`).type('../fake-data/blob/hipster.txt', { force: true, delay: 100 });
      cy.wait(200);
      cy.get(`[data-cy="selectModalCancelButton"]`).click();

      cy.get('.vben-layout-content.full').first().scrollTo('center', { ensureScrollable: false });

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        announcement = response?.body;
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
        cy.get(`[rowid="${announcement.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_title"]').type('until pfft 仍然');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${announcement.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${announcement.id}"]`).should('exist');
      });
      cy.url().should('match', announcementPageUrlPattern);
    });
  });
});
