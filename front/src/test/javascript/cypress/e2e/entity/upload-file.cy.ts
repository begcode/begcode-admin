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

describe('UploadFile e2e test', () => {
  const basePageUrl = '/files';
  const uploadFilePageUrl = '/files/upload-file';
  const uploadFilePageUrlPattern = new RegExp('/upload-file(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const uploadFileSample = { url: 'https://-atmosphere.biz' };
  const localStorageValue: any = {};

  let uploadFile: any;

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
    cy.intercept('GET', '/api/upload-files+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/upload-files').as('postEntityRequest');
    cy.intercept('DELETE', '/api/upload-files/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (uploadFile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/upload-files/${uploadFile.id}`,
      }).then(() => {
        uploadFile = undefined;
      });
    }
  });

  it('UploadFiles menu should load UploadFiles page', () => {
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
    cy.get(`[data-cy="${uploadFilePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UploadFile').should('exist');
    cy.url().should('match', uploadFilePageUrlPattern);
  });

  describe('UploadFile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uploadFilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UploadFile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/upload-file/new$'));
        cy.getEntityCreateUpdateHeading('UploadFile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadFilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/upload-files',
          body: uploadFileSample,
        }).then(({ body }) => {
          uploadFile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/upload-files+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/upload-files?page=0&size=20>; rel="last",<http://localhost/api/upload-files?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [uploadFile],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uploadFilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UploadFile page', () => {
        cy.visit(uploadFilePageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('uploadFile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadFilePageUrlPattern);
      });

      it('edit button click should load edit UploadFile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('UploadFile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadFilePageUrlPattern);
      });

      it.skip('edit button click should load edit UploadFile page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('UploadFile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadFilePageUrlPattern);
      });

      it('last delete button click should delete instance of UploadFile', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('uploadFile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadFilePageUrlPattern);

        uploadFile = undefined;
      });
    });
  });

  describe('new UploadFile page', () => {
    beforeEach(() => {
      cy.visit(`${uploadFilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UploadFile');
    });

    it('should create an instance of UploadFile', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_businessTitle"]`).type('敲 何尝', { delay: 100 });
      cy.get(`[id="form_item_businessTitle"]`).should('have.value', '敲 何尝');

      cy.get(`[id="form_item_businessDesc"]`).type('an', { delay: 100 });
      cy.get(`[id="form_item_businessDesc"]`).should('have.value', 'an');

      cy.get(`[id="form_item_businessStatus"]`).type('masterpiece bifocals', { delay: 100 });
      cy.get(`[id="form_item_businessStatus"]`).should('have.value', 'masterpiece bifocals');

      cy.get(`[id="form_item_url"]`).type('https://-arena.info', { delay: 100 });
      cy.get(`[id="form_item_url"]`).should('have.value', 'https://-arena.info');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uploadFile = response?.body;
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
        cy.get(`[rowid="${uploadFile.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_businessTitle"]').type('敲 何尝');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${uploadFile.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${uploadFile.id}"]`).should('exist');
      });
      cy.url().should('match', uploadFilePageUrlPattern);
    });
  });
});
