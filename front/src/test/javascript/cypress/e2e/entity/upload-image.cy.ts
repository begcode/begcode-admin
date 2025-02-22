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

describe('UploadImage e2e test', () => {
  const basePageUrl = '/files';
  const uploadImagePageUrl = '/files/upload-image';
  const uploadImagePageUrlPattern = new RegExp('/upload-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const uploadImageSample = { url: 'https://-shanty.info/' };
  const localStorageValue: any = {};

  let uploadImage: any;

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
    cy.intercept('GET', '/api/upload-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/upload-images').as('postEntityRequest');
    cy.intercept('DELETE', '/api/upload-images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (uploadImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/upload-images/${uploadImage.id}`,
      }).then(() => {
        uploadImage = undefined;
      });
    }
  });

  it('UploadImages menu should load UploadImages page', () => {
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
    cy.get(`[data-cy="${uploadImagePageUrl}"]`).click();
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UploadImage').should('exist');
    cy.url().should('match', uploadImagePageUrlPattern);
  });

  describe('UploadImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uploadImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UploadImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/files/upload-image/new$'));
        cy.getEntityCreateUpdateHeading('UploadImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/upload-images',
          body: uploadImageSample,
        }).then(({ body }) => {
          uploadImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/upload-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/upload-images?page=0&size=20>; rel="last",<http://localhost/api/upload-images?page=0&size=20>; rel="first"',
              },
              body: {
                total: 1,
                records: [uploadImage],
              },
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uploadImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UploadImage page', () => {
        cy.visit(uploadImagePageUrl);
        cy.wait(300);
        cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]')
          .first()
          .then(el => {
            if (el) {
              cy.get('.vxe-table--main-wrapper [data-cy="buttonGroupDropdown"]').first().click({ force: true });
            }
          });
        cy.get(entityDetailsButtonSelector).first().click({ force: true });
        cy.getEntityDetailsHeading('uploadImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadImagePageUrlPattern);
      });

      it('edit button click should load edit UploadImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('UploadImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadImagePageUrlPattern);
      });

      it.skip('edit button click should load edit UploadImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click({ force: true });
        cy.getEntityCreateUpdateHeading('UploadImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadImagePageUrlPattern);
      });

      it('last delete button click should delete instance of UploadImage', () => {
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('uploadImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uploadImagePageUrlPattern);

        uploadImage = undefined;
      });
    });
  });

  describe('new UploadImage page', () => {
    beforeEach(() => {
      cy.visit(`${uploadImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UploadImage');
    });

    it('should create an instance of UploadImage', () => {
      cy.wait(3000);

      cy.get(`[id="form_item_businessTitle"]`).type('around why', { delay: 100 });
      cy.get(`[id="form_item_businessTitle"]`).should('have.value', 'around why');

      cy.get(`[id="form_item_businessDesc"]`).type('披', { delay: 100 });
      cy.get(`[id="form_item_businessDesc"]`).should('have.value', '披');

      cy.get(`[id="form_item_businessStatus"]`).type('an gah', { delay: 100 });
      cy.get(`[id="form_item_businessStatus"]`).should('have.value', 'an gah');

      cy.get(`[id="form_item_url"]`).type('https://-word.biz', { delay: 100 });
      cy.get(`[id="form_item_url"]`).should('have.value', 'https://-word.biz');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uploadImage = response?.body;
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
        cy.get(`[rowid="${uploadImage.id}"]`).should('exist');
        cy.get(listSearchInputSelector).clear();
        cy.get(listSearchMoreSelector).click();
        cy.get(listSearchFormSelector).should('exist');
        cy.get('[id="form_item_businessTitle"]').type('around why');
        cy.get(searchFormSubmitSelector).click();
        cy.get(entityTableSelector).should('exist');
        cy.get(`[rowid="${uploadImage.id}"]`).should('exist');
        cy.wait(300);
        cy.get(searchFormResetSelector).click();
        cy.wait(300);
        cy.get(formAdvanceToggleSelector).click();
        cy.get(`[rowid="${uploadImage.id}"]`).should('exist');
      });
      cy.url().should('match', uploadImagePageUrlPattern);
    });
  });
});
