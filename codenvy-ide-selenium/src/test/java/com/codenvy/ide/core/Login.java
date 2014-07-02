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
package com.codenvy.ide.core;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Oksana Vereshchaka */
public class Login extends AbstractTestModule {
    public Login(IDE IDE) {
        super(IDE);
    }

    private interface Locators {

        String USERNAME = "//form[@class='login-form']//input[@placeholder='email']";

        String PASSWORD = "//form[@class='login-form']//input[@placeholder='password']";

        String LOGIN_ON_TENANT_BTN = "//form[@class='login-form']//input[@value='Login']";

        String LOGIN_STANDALONE = "loginButton";

        String GOOGLE_OAUTH_BUTTON = "a.oauth-button.google";

        String GITHUB_OAUTH_BUTTON = "a.oauth-button.github";

        String IDE_LOGOUT = "//a[text()='Logout']";

        String IDE_SIGN_IN = "//a[text()='Sign In']";

        String FORGOT_PASSWORD = "Forgot your password?";
    }

    @FindBy(xpath = Locators.USERNAME)
    private WebElement name;

    @FindBy(xpath = Locators.LOGIN_ON_TENANT_BTN)
    private WebElement loginTenantButton;

    @FindBy(xpath = Locators.PASSWORD)
    private WebElement password;

    @FindBy(id = Locators.LOGIN_STANDALONE)
    private WebElement loginButton;

    @FindBy(xpath = Locators.IDE_LOGOUT)
    private WebElement logoutButton;

    @FindBy(linkText = "IDE")
    private WebElement cloudIdeAdditionMenu;

    @FindBy(css = Locators.GOOGLE_OAUTH_BUTTON)
    private WebElement googleOauthBtn;

    @FindBy(css = Locators.GITHUB_OAUTH_BUTTON)
    private WebElement gitHubOauthBtn;

    @FindBy(linkText = Locators.FORGOT_PASSWORD)
    private WebElement forgotPasword;

    /** click on google button in tenant authorization form */
    public void googleOauthBtnClick() {
        googleOauthBtn.click();
    }

    /** click on github button in tenant authorization form */
    public void githubBtnClick() {
        gitHubOauthBtn.click();
    }

    /**
     * Logout from IDE click on IDE - > Logout
     *
     * @throws Exception
     */
    public void logout() throws Exception {
        IDE().LOADER.waitClosed();
        waitLogoutButtonInIDEMenu();
        logoutButton.click();
        IDE().LOGIN.waitTenantLoginPage();
    }

    /** Open IDE addition menu where "Logout" is sub-menu item */
    private void openCloudIdeAdditionMenu() {
        if (cloudIdeAdditionMenu != null) {
            cloudIdeAdditionMenu.click();
        }
    }

    /**
     * login on tenant page
     *
     * @param userName
     * @param password
     * @throws Exception
     */
    public void tenantLogin(String userName, String password) throws Exception {
        IDE().INPUT.typeToElement(name, userName, true);
        IDE().INPUT.typeToElement(this.password, password, true);
        tenantLogin();
    }

    /** wait for Logout button in IDE top menu */
    public void waitLogoutButtonInIDEMenu() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.IDE_LOGOUT)));
    }

    /** wait for SingIn button in IDE top menu */
    public void waitSignInButtonInIDEMenu() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.IDE_SIGN_IN)));
    }

    /** wait login jsp page on cloud-ide */
    public void waitTenantLoginPage() {
        waitTenantAllLoginPage();
    }

    /** wait login jsp page on cloud-ide */
    public void waitTenantAllLoginPage() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return name != null && name.isDisplayed() && password != null && password.isDisplayed()
                           && loginTenantButton != null && loginTenantButton.isDisplayed() &&
                           gitHubOauthBtn.isDisplayed()
                           && googleOauthBtn.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** click on button Login */
    public void tenantLogin() {
        try {
            loginTenantButton.click();
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** click on button Login */
    public void login() {
        loginButton.click();
    }

    /**
     * login as invite user on cloud page
     *
     * @throws Exception
     */
    public void loginAsTenantUser() throws Exception {
        tenantLogin(BaseTest.NOT_ROOT_USER_NAME, BaseTest.NOT_ROOT_USER_PASSWORD);
    }

    /**
     * login as root user on cloud page
     *
     * @throws Exception
     */
    public void loginAsTenantRoot() throws Exception {
        tenantLogin(BaseTest.USER_NAME, BaseTest.USER_PASSWORD);
    }

    /** wait forgot password link on login page */
    public void waitForgotPasswordLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(forgotPasword));
    }

    /** click on forgot password link on login page */
    public void clickForgotPassword() {
        waitForgotPasswordLink();
        forgotPasword.click();
    }

}
