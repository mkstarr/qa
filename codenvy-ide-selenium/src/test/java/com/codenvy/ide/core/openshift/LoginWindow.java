/*
 *
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

package com.codenvy.ide.core.openshift;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import java.util.ResourceBundle;

/** @author Zaryana Dombrovskaya */

public class LoginWindow extends AbstractTestModule {

//    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");
//    public static String LOGIN = IDE_SETTINGS.getString("openshift.email");
//    public static String PASSWORD = IDE_SETTINGS.getString("openshift.password");

    String login    = "zaryana.dombrovskaya@gmail.com";
    String password = "totasya123";

    LoginWindow(IDE ide) {
        super(ide);
    }

    interface Locators {
        String LOGIN_FORM     = "ideLoginView-window";
        String LOGIN_FIELD    = "input[name='ideLoginViewEmailField']";
        String PASSWORD_FIELD = "input[name='ideLoginViewPasswordField']";
        String LOGIN_BUTTON   = "ideLoginViewLoginButton";
        String CANCEL_BUTTON  = "ideLoginViewCancelButton";
    }

    @FindBy(id = Locators.LOGIN_FORM)
    WebElement loginForm;

    @FindBy(css = Locators.LOGIN_FIELD)
    WebElement loginField;

    @FindBy(css = Locators.PASSWORD_FIELD)
    WebElement passwordField;

    @FindBy(id = Locators.LOGIN_BUTTON)
    WebElement loginButton;

    @FindBy(id = Locators.CANCEL_BUTTON)
    WebElement cancelButton;

    /** wait appearance Heroku login form */
    public void waitLoginWindow() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(loginForm));
    }

    /**
     * type login
     *
     * @param login
     */
    public void typeLogin(String login) {
        loginField.sendKeys(login);
    }

    /**
     * type password
     *
     * @param password
     */
    public void typePassword(String password) {
        passwordField.sendKeys(password);
    }

    /** click on login button */
    public void clickOnLoginButton() {
        loginButton.click();
    }

    public void login() throws InterruptedException {

        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT, MenuCommands.PaaS.OpenShift.SWITCH_ACCOUNT);

        waitLoginWindow();
        typeLogin(login);
        typePassword(password);
        clickOnLoginButton();
        IDE().LOADER.waitClosed();
    }

}
