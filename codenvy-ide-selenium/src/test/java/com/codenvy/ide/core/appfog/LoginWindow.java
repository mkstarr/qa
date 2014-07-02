/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.core.appfog;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** @author Dmytro Nochevnov */
public class LoginWindow extends AbstractTestModule {

    interface Locators {
        String LOGIN_WINDOW_ID     = "ideLoginView-window";
        String SERVER_FIELD_NAME   = "ideLoginViewTargetField";
        String EMAIL_FIELD_NAME    = "ideLoginViewEmailField";
        String PASSWORD_FIELD_NAME = "ideLoginViewPasswordField";
        String LOGIN_BUTTON_ID     = "ideLoginViewLoginButton";
        String ERROR_MESSAGE_XPATH =
                "//div[@id='ideLoginView-window']/div/table/tbody/tr[2]/td[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[4]";
    }

    @FindBy(id = Locators.LOGIN_WINDOW_ID)
    WebElement loginWindow;

    @FindBy(xpath = Locators.ERROR_MESSAGE_XPATH)
    WebElement errorMessageField;

    @FindBy(name = Locators.SERVER_FIELD_NAME)
    WebElement serverField;

    @FindBy(name = Locators.EMAIL_FIELD_NAME)
    WebElement emailField;

    @FindBy(name = Locators.PASSWORD_FIELD_NAME)
    WebElement passwordField;

    @FindBy(id = Locators.LOGIN_BUTTON_ID)
    WebElement loginButton;

    LoginWindow(IDE ide) {
        super(ide);
    }

    public void login(String email, String password) throws Exception {
        waitCodenvyWindowOpened(loginWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        emailField.clear();
        emailField.sendKeys(email);

        passwordField.sendKeys(password);

        loginButton.click();

        IDE().LOADER.waitClosed();

        // verify on error message inside the window
        if (isCodenvyWindowDisplayed(loginWindow)) {
            throw new PaaSException(getErrorMessage());
        }
    }

    public String getErrorMessage() {
        try {
            return errorMessageField.isDisplayed() ? errorMessageField.getText() : null;
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
}   
