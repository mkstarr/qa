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
package com.codenvy.ide.core.aws;

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
        String LOGIN_WINDOW_ID       = "ideLoginView-window";
        String ACCESS_KEY_FIELD_NAME = "ideLoginViewAccessKeyField";
        String SECRET_KEY_FIELD_NAME = "ideLoginViewSecretKeyField";
        String LOGIN_BUTTON_ID       = "ideLoginViewLoginButton";
        String ERROR_MESSAGE_ID      = "ideLoginViewLoginResult";
    }

    @FindBy(id = Locators.LOGIN_WINDOW_ID)
    WebElement loginWindow;

    @FindBy(id = Locators.ERROR_MESSAGE_ID)
    WebElement errorMessageField;

    @FindBy(name = Locators.ACCESS_KEY_FIELD_NAME)
    WebElement accessKeyField;

    @FindBy(name = Locators.SECRET_KEY_FIELD_NAME)
    WebElement secretKeyField;

    @FindBy(id = Locators.LOGIN_BUTTON_ID)
    WebElement loginButton;

    LoginWindow(IDE ide) {
        super(ide);
    }

    public void login(String email, String password) throws Exception {
        waitCodenvyWindowOpened(loginWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        accessKeyField.clear();
        accessKeyField.sendKeys(email);

        secretKeyField.sendKeys(password);

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
