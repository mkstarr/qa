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

/**
 * @author Musienko Maxim
 *
 */
public class ResetPasswordPage extends AbstractTestModule {
    public ResetPasswordPage(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private interface Locators {
        String RESET_YOUR_PASS_LABLE         = "//h2[text()='Reset your password']";
        String RESET_MAIL_LABLE              = "//h2[text()='%s']";
        String TYPE_NEW_PASSWORD_FIELD       = "input[placeholder='Type your new password']";
        String CONFIRM_PASSWORD_FIELD        = "input[placeholder='Type it again']";
        String RESET_MY_PASSWORD_BTN         = "//input[@value='Reset my password']";
        String ERROR_MESS_FOR_EMPTY_PASS     = "//div[@class='error-container expanded'and text()='Please provide your account password.']";
        String ERROR_MESS_FOR_NOT_IDENT_PASS =
                "//div[@class='error-container expanded' and text()='Please type your new password again. Both passwords must match.']";

    }

    @FindBy(xpath = Locators.RESET_YOUR_PASS_LABLE)
    private WebElement recoveryPasswordMAilField;

    @FindBy(css = Locators.TYPE_NEW_PASSWORD_FIELD)
    private WebElement newPasswordField;

    @FindBy(css = Locators.CONFIRM_PASSWORD_FIELD)
    private WebElement confirmPassField;

    @FindBy(xpath = Locators.RESET_MY_PASSWORD_BTN)
    private WebElement resetPassworBtn;


    /** wait label on setup password page with the name of e-mail box from which user come on this page */
    public void waitLabelWithUsedEmail(String nameEmailBox) {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.RESET_MAIL_LABLE, nameEmailBox))));
    }


    /**
     * type password into 'Type new password field'
     *
     * @param newPass
     */
    public void typeNewPassword(String newPass) {
        newPasswordField.clear();
        newPasswordField.sendKeys(newPass);
    }

    /**
     * confirm password into 'Confirm password field'
     *
     * @param newPass
     */
    public void confirmNewPassword(String newPass) {
        confirmPassField.clear();
        confirmPassField.sendKeys(newPass);
    }

    /** click on reset Btn on setup password page */
    public void clickResetBtn() {
        resetPassworBtn.click();
    }

    /** wait error message if user does not enter new password */
    public void waitErrorMessForEmptyFields() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESS_FOR_EMPTY_PASS)));
    }

    /** wait error message if entered passwords are not same */
    public void waitErrorMessForNotIdentPass() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESS_FOR_NOT_IDENT_PASS)));
    }

}
