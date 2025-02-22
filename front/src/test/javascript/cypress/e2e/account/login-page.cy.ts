import { titleLoginSelector, usernameLoginSelector, passwordLoginSelector, submitLoginSelector } from '../../support/commands';

describe('login modal', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';

  beforeEach(() => {
    cy.visit('');
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/authenticate/withoutCaptcha').as('authenticate');
  });

  it('greets with signin', () => {
    cy.get(titleLoginSelector).should('be.visible');
  });

  it('requires username', () => {
    cy.get(passwordLoginSelector).type('a-password');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response?.statusCode).to.equal(401));
    // login page should stay open when login fails
    cy.get(titleLoginSelector).should('be.visible');
  });

  it('requires password', () => {
    cy.get(usernameLoginSelector).type('a-login');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response?.statusCode).to.equal(401));
    cy.get(titleLoginSelector).should('be.visible');
  });

  it('errors when password is incorrect', () => {
    cy.get(usernameLoginSelector).type(username);
    cy.get(passwordLoginSelector).type('bad-password');
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response?.statusCode).to.equal(401));
    cy.get(titleLoginSelector).should('be.visible');
  });

  it('go to login page when successfully logs in', () => {
    cy.get(usernameLoginSelector).clear().type(username);
    cy.get(passwordLoginSelector).clear().type(password);
    cy.get(submitLoginSelector).click();
    cy.wait('@authenticate').then(({ response }) => expect(response?.statusCode).to.equal(200));
    cy.hash().should('eq', '');
  });
});
