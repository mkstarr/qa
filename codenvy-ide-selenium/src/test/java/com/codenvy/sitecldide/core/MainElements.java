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
package com.codenvy.sitecldide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class MainElements extends AbstractTestModule {


    public MainElements(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private interface Locators {

        String EMAIL_FIELD = "//input[@placeholder='Sign up with your email']";


        String LOGO = "a.logo>h1";

        String FIELD_FOR_SET_WORKSPACE = "//input[@placeholder='Choose your workspace name']";

        String SIGN_UP_BTN = "input.signup-button.blue-button";

        String ALREADY_EXIST_TENANT_MESS =
                "//div[@class='error-container expanded' and text()='You are the owner of another persistent workspace.']";

        String NOT_ALLOWED_MESSAGE = "//div[@class='error-container expanded']";
    }

    @FindBy(xpath = Locators.EMAIL_FIELD)
    private WebElement emailField;

    private WebElement headerLoginBtnLink;

    @FindBy(css = Locators.LOGO)
    private WebElement logo;

    @FindBy(css = Locators.SIGN_UP_BTN)
    private WebElement signUpBtn;

    @FindBy(xpath = Locators.FIELD_FOR_SET_WORKSPACE)
    private WebElement wrkSpaceNameField;

    @FindBy(xpath = Locators.ALREADY_EXIST_TENANT_MESS)
    private WebElement alreadyExistErrorMessage;

    @FindBy(xpath = Locators.NOT_ALLOWED_MESSAGE)
    private WebElement NotAllowedMessage;

    /** wait appearance email field on cldide main page */
    public void waitEmailField() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(emailField));
    }


    /** wait appearance error mess for already exist error mess */
    public void waitAlreadyExistErrorMess() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(alreadyExistErrorMessage));
    }

    /** wait appearance error for reserved tenant name */
    public void waitInvalidTenantNameErrorMess() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.textToBePresentInElement(By.tagName("body"),
                                                                                          "This workspace name is reserved, " +
                                                                                          "please choose another name"));
    }


    /** wait appearance button Login link in header */
    public void waitHeaderLoginBtnLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(headerLoginBtnLink));
    }


    /** wait appearance field for enter workspace name */
    public void waitWorkSpaceField() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(wrkSpaceNameField));
    }


    /**
     * enter email to email field
     *
     * @param email
     */
    public void typeEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public String getEmail() {
        return emailField.getText();
    }

    /** wait text message about email not allowed */
    public void waitNotAllowedMess() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(NotAllowedMessage));
    }

    /** get text message about email not allowed */
    public String getNotAllowedMess() {
        waitNotAllowedMess();
        return NotAllowedMessage.getText();
    }

    /**
     * enter domain name to field
     *
     * @param domain
     */
    public void typeDomenName(String domain) {
        wrkSpaceNameField.clear();
        wrkSpaceNameField.sendKeys(domain);
    }

    /** click on sign up btn on page */
    public void clickSignUpBtn() {
        signUpBtn.click();
    }

}
