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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** The Object containing GoogleAuthPage page`s webelenents */
public class Google extends AbstractTestModule {
    /** @param ide */
    public Google(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String LOGIN_FIELD_ID = "Email";

        String PASS_FIELD_ID = "Passwd";

        String SIGN_IN_BTN_ID = "signIn";

        String APPROVE_ACCESS_BTN_ID = "submit_approve_access";

        String MAIL_BOX_ENTRY = "//span[text()='Gmail']";

        String ALLOW_BUTTON_ID = "submit_approve_access";

        String SECURITU_BUTTON_ID = "nav-security";

        String SECURITY_ACCOUNT_PERMISIONS_BUTTON_ID = "nav-security/permissions";

        String SECURITY_VIEW_ALL = "//a[text()='View all']";

        String REVOKE_CONTACTS_BUTTON =
                "//span[text()='Are you sure you want to revoke access?']/parent::div/following-sibling::div/button[@name='ok']";

        String REVOKE_PROFILE_DATA_BUTTON =
                "//div[text()='Codenvy has access to:']/parent::div//div[text()='Revoke access']";

    }

    @FindBy(id = Locators.LOGIN_FIELD_ID)
    public WebElement loginField;

    @FindBy(id = Locators.SECURITY_ACCOUNT_PERMISIONS_BUTTON_ID)
    public WebElement securityAccounPermisionsButton;


    @FindBy(id = Locators.PASS_FIELD_ID)
    public WebElement passField;

    @FindBy(id = Locators.SIGN_IN_BTN_ID)
    public WebElement signInBtn;

    @FindBy(id = Locators.APPROVE_ACCESS_BTN_ID)
    private WebElement approveBtn;

    @FindBy(xpath = Locators.MAIL_BOX_ENTRY)
    private WebElement mailBox;

    @FindBy(id = Locators.ALLOW_BUTTON_ID)
    private WebElement allowBtn;

    @FindBy(id = Locators.SECURITU_BUTTON_ID)
    public WebElement securityBtn;

    @FindBy(xpath = Locators.SECURITY_VIEW_ALL)
    private WebElement securityViewAll;

    @FindBy(xpath = Locators.REVOKE_CONTACTS_BUTTON)
    private WebElement revokeContactsBtn;

    @FindBy(xpath = Locators.REVOKE_PROFILE_DATA_BUTTON)
    private WebElement revokeProfileDataBtn;

    /** wait approve access button */
    public void waitMailbox() {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return mailBox != null && mailBox.isDisplayed();
            }
        });
    }

    /** wait security permissions button on google account page */
    public void waitAccountPermButton() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(securityAccounPermisionsButton));
    }


    /** Wait web elements for login on google. */
    public void waitOauthPageOpened() {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return signInBtn.isDisplayed() && passField.isDisplayed() && loginField.isDisplayed();
            }
        });
    }

    /** wait approve access button */
    public void waitApproveAccessPage() {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver elem) {
                return approveBtn != null && approveBtn.isDisplayed();
            }
        });
    }

    /**
     * type login on auth google page
     *
     * @param login
     */
    public void typeLogin(String login) {
        loginField.sendKeys(login);
    }

    /**
     * type password on auth google page
     *
     * @param login
     *         login
     */
    public void typePassword(String login) {
        passField.sendKeys(login);
    }

    /** click for sign in button */
    public void clickSignIn() {
        signInBtn.click();
    }

    /** click for approve button */
    public void clickApproveBtn() {
        approveBtn.click();
    }

    /** Wait for allow button */
    public void waitAllowApplicationButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.elementToBeClickable(By.id(Locators.ALLOW_BUTTON_ID)));
    }

    /** Click on allow button */
    public void clickOnAllowButton() {
        allowBtn.click();
    }

    /** Wait for security button */
    public void waitSecurityButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(securityBtn));
    }

    /** Click on security button */
    public void clickOnSecurityButton() {
        securityBtn.click();
    }


    /** Wait for view all on security page */
    public void waitSecurityViewAll() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(securityViewAll));
    }

    /** Click on view all on security page */
    public void clickOnSecurityViewAll() {
        securityViewAll.click();
    }

    /** Wait for revoke button */
    public void waitRevokeContactsButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(revokeContactsBtn));
    }

    /** Wait for revoke button disappear */
    public void waitRevokeContactsButtonDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.REVOKE_CONTACTS_BUTTON)));
    }

    /** Click on revoke contacts button */
    public void clickOnRevokeContactsButton() {
        revokeContactsBtn.click();
    }

    /** Wait for revoke button */
    public void waitRevokeProfileDataButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(revokeProfileDataBtn));
    }

    /** Wait for revoke button disappear */
    public void waitRevokeProfileDataButtonDisappear() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.REVOKE_PROFILE_DATA_BUTTON)));
    }

    /** Click on revoke contacts button */
    public void clickOnRevokeProfileDataButton() {
        revokeProfileDataBtn.click();
    }

    /**
     * open google account page
     *
     * @throws Exception
     */
    public void openGoogleAccountPage() throws Exception {
        driver().get("https://accounts.google.com");
        waitSecurityButton();
    }

    /** Delete google tokens profile and contacts data */
    public void deleteGoogleTokens() {
        waitSecurityButton();
        clickOnSecurityButton();
        waitSecurityViewAll();
        clickOnSecurityViewAll();

        waitRevokeProfileDataButton();
        clickOnRevokeProfileDataButton();
        waitRevokeContactsButton();
        clickOnRevokeContactsButton();
        waitRevokeContactsButtonDisappear();
    }

}
