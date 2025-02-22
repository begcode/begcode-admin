import {
  currentPasswordSelector,
  newPasswordSelector,
  confirmPasswordSelector,
  changePasswordModalSelector,
  form_item_input_invalid,
  titleLoginSelector,
  usernameLoginSelector,
  passwordLoginSelector,
  submitLoginSelector,
  changePasswordSubmitSelector,
} from '../../support/commands';

describe('/account/password', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const localStorageValue: any = {};

  beforeEach(() => {
    if (Object.keys(localStorageValue).length === 0) {
      cy.visit('/dashboard/analysis');
      cy.get(titleLoginSelector).should('be.visible');
      cy.get(usernameLoginSelector).clear().type(username);
      cy.get(passwordLoginSelector).clear().type(password);
      cy.get(submitLoginSelector).click();
      // cy.visit('');
      cy.wait(1000);
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
    cy.clickOnPasswordItem();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/account/change-password').as('passwordSave');
  });

  it('should be accessible through menu', () => {
    cy.wait(100);
    cy.get(changePasswordModalSelector).should('be.visible');
  });

  it('requires current password', () => {
    cy.wait(100);
    cy.get(changePasswordModalSelector).should('be.visible');
    cy.get(changePasswordSubmitSelector).click();
    cy.get(currentPasswordSelector).parent().should('have.class', form_item_input_invalid);
    cy.wait(200);
    cy.get(currentPasswordSelector).type('wrong-current-password');
    cy.get(changePasswordSubmitSelector).click();
    cy.wait(200);
    cy.get(currentPasswordSelector).should('not.have.class', form_item_input_invalid);
    cy.wait(1000);
  });

  it('requires new password', () => {
    cy.get(changePasswordModalSelector).should('be.visible');
    cy.get(changePasswordSubmitSelector).click();
    cy.get(newPasswordSelector).parent().should('have.class', form_item_input_invalid);
    cy.wait(200);
    cy.get(newPasswordSelector).type('jhipster');
    cy.get(changePasswordSubmitSelector).click();
    cy.get(currentPasswordSelector).should('not.have.class', form_item_input_invalid);
    cy.wait(500);
  });

  it('requires confirm new password', () => {
    cy.get(changePasswordModalSelector).should('be.visible');
    cy.get(newPasswordSelector).type('jhipster');
    cy.get(changePasswordSubmitSelector).click();
    cy.get(confirmPasswordSelector).parent().should('have.class', form_item_input_invalid);
    cy.wait(500);
    cy.get(confirmPasswordSelector).type('jhipster');
    cy.get(changePasswordSubmitSelector).click();
    cy.get(confirmPasswordSelector).should('not.have.class', form_item_input_invalid);
    cy.wait(500);
  });

  it('should fail to update password when using incorrect current password', () => {
    cy.get(changePasswordModalSelector).should('be.visible');
    cy.get(currentPasswordSelector).type('wrong-current-password');
    cy.get(newPasswordSelector).type('jhipster');
    cy.get(confirmPasswordSelector).type('jhipster');
    cy.get(changePasswordSubmitSelector).click();
    cy.wait('@passwordSave').then(({ response }) => expect(response?.statusCode).to.equal(400));
  });

  it('should be able to update password', () => {
    cy.get(changePasswordModalSelector).should('be.visible');
    cy.get(currentPasswordSelector).type(password);
    cy.get(newPasswordSelector).type(password);
    cy.get(confirmPasswordSelector).type(password);
    cy.get(changePasswordSubmitSelector).click();
    cy.wait('@passwordSave').then(({ response }) => expect(response?.statusCode).to.equal(200));
  });
});
