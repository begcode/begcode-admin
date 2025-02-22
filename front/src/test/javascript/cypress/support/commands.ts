/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/no-use-before-define */
// eslint-disable-next-line spaced-comment
/// <reference types="cypress" />

// ***********************************************
// This commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

// ***********************************************
// Begin Specific Selector Attributes for Cypress
// ***********************************************

// Navbar
export const navbarSelector = '[data-cy="navbar"]';
export const adminMenuSelector = '[data-cy="adminMenu"]';
export const accountMenuSelector = '[data-cy="accountMenu"]';
export const registerItemSelector = '[data-cy="register"]';
export const settingsItemSelector = '[data-cy="settings"]';
export const passwordItemSelector = '[data-cy="passwordItem"]';
export const loginItemSelector = '[data-cy="login"]';
export const logoutItemSelector = '[data-cy="logout"]';
export const entityItemSelector = '[data-cy="entity"]';
export const userDropdownSelector = '.vben-header-user-dropdown';
export const showMenuSelector = '.vben-layout-header-trigger';
export const leftMenuSelector = '.vben-menu';

// Login
export const titleLoginSelector = '[data-cy="loginTitle"]';
export const errorLoginSelector = '[data-cy="loginError"]';
export const usernameLoginSelector = '[data-cy="username"]';
export const passwordLoginSelector = '[data-cy="password"]';
export const forgetYourPasswordSelector = '[data-cy="forgetYourPasswordSelector"]';
export const submitLoginSelector = '[data-cy="submit"]';

// Register
export const titleRegisterSelector = '[data-cy="registerTitle"]';
export const usernameRegisterSelector = '[data-cy="username"]';
export const emailRegisterSelector = '[data-cy="email"]';
export const firstPasswordRegisterSelector = '[data-cy="firstPassword"]';
export const secondPasswordRegisterSelector = '[data-cy="secondPassword"]';
export const submitRegisterSelector = '[data-cy="submit"]';

// Settings
export const firstNameSettingsSelector = '[data-cy="firstname"]';
export const lastNameSettingsSelector = '[data-cy="lastname"]';
export const emailSettingsSelector = '[data-cy="email"]';
export const languageSettingsSelector = '[data-cy="langKey"]';
export const submitSettingsSelector = '[data-cy="submit"]';

// Password
export const currentPasswordSelector = '[id="form_item_currentPassword"]';
export const newPasswordSelector = '[id="form_item_newPassword"]';
export const confirmPasswordSelector = '[id="form_item_confirmpassword"]';
export const submitPasswordSelector = '[data-cy="submit"]';
export const changePasswordMenuSelector = '[data-cy="changePasswordMenu"]';
export const changePasswordModalSelector = '[data-cy="changePasswordModal"]';
export const changePasswordSubmitSelector = '[data-cy="changePasswordModal"] .ant-btn-primary';

// Reset Password
export const emailResetPasswordSelector = '[data-cy="emailResetPassword"]';
export const submitInitResetPasswordSelector = '[data-cy="submit"]';

// Administration
export const userManagementPageHeadingSelector = '[data-cy="userManagementPageHeading"]';
export const swaggerFrameSelector = 'iframe[data-cy="swagger-frame"]';
export const swaggerPageSelector = '[id="swagger-ui"]';
export const metricsPageHeadingSelector = '[data-cy="metricsPageHeading"]';
export const healthPageHeadingSelector = '[data-cy="healthPageHeading"]';
export const logsPageHeadingSelector = '[data-cy="logsPageHeading"]';
export const configurationPageHeadingSelector = '[data-cy="configurationPageHeading"]';

// ***********************************************
// End Specific Selector Attributes for Cypress
// ***********************************************
export const form_item_input_invalid = 'ant-input-affix-wrapper-status-error';
export const classInvalid = 'invalid';
export const classValid = 'valid';
Cypress.Commands.add('authenticatedRequest', data => {
  const bearerToken = localStorage.getItem(Cypress.env('jwtStorageName'));
  if (bearerToken) {
    return cy.request({
      ...data,
      auth: {
        bearer: bearerToken,
      },
    });
  }
  return cy.request(data);
});

Cypress.Commands.add('login', (username: string, password: string) => {
  cy.session(
    [username, password],
    () => {
      cy.request({
        method: 'GET',
        url: '/api/account',
        failOnStatusCode: false,
      });
      cy.authenticatedRequest({
        method: 'POST',
        body: { username, password },
        url: Cypress.env('authenticationUrl'),
      }).then(({ body: { id_token } }) => {
        localStorage.setItem(Cypress.env('jwtStorageName'), id_token);
      });
    },
    {
      validate() {
        cy.authenticatedRequest({ url: '/api/account' }).its('status').should('eq', 200);
      },
    },
  );
});

Cypress.Commands.add('setTinyMceContent', (tinyMceId = 'tinymce', content = '') => {
  cy.window().should('have.property', 'tinymce'); // wait for tinyMCE
  cy.wait(500);
  cy.window().then(win => {
    const editor = (win as any).tinymce.EditorManager.get().find((edtr: any) => edtr.id === tinyMceId);

    editor.setContent(content, { format: 'text' });
    cy.wait(250);
    cy.wrap(editor.getContent({ format: 'text' })).should('eq', content);
  });
});

declare global {
  namespace Cypress {
    interface Chainable {
      authenticatedRequest(data): Cypress.Chainable;
      login(username: string, password: string): Cypress.Chainable;
      setTinyMceContent(tinyMceId: string, content: string): Cypress.Chainable;
    }
  }
}

import 'cypress-audit/commands';
// Convert this to a module instead of script (allows import/export)
export {};
