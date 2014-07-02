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
public class RecoverPasswordPage extends AbstractTestModule {
    public RecoverPasswordPage(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private interface Locators {

        String MAIL_FOR_RECOVERY_FIELD = "//input[@placeholder='Enter your email address']";

        String SIGN_UP_LINK = "Don't have an account? Sign up.";

        String SEND_MY_PASSFORD_BTN = "input[value='Send my password']";

        String ERROR_CONTAINER = "//div[@class='error-container expanded' and text()='%s']";

    }


    @FindBy(xpath = Locators.MAIL_FOR_RECOVERY_FIELD)
    private WebElement recoveryPasswordMAilField;


    @FindBy(linkText = Locators.SIGN_UP_LINK)
    private WebElement signUpLink;

    @FindBy(css = Locators.SEND_MY_PASSFORD_BTN)
    private WebElement sendMyPasswortBtn;


    /** wait the field for recovery password on page */
    public void waitRecoveryPassField() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(recoveryPasswordMAilField));
    }

    /** wait the 'Don't have an account? Sign up.' link on page */
    public void waitSendMyPassBtn() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(sendMyPasswortBtn));
    }

    /** wait the field for recovery password on page */
    public void waitSignUpLink() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(signUpLink));
    }

    /** wait the field for recovery password on page */
    public void waitSignUpLink(String errorMess) {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.ERROR_CONTAINER, errorMess))));
    }


    /** wait the field for recovery password on page */
    public void waitMainElements() {
        waitRecoveryPassField();
        waitSendMyPassBtn();
        waitSignUpLink();
    }

    /**
     * type to email field your data
     *
     * @param mail
     *         (your mail)
     */
    public void typeToMailField(String mail) {
        recoveryPasswordMAilField.clear();
        recoveryPasswordMAilField.sendKeys(mail);
    }

    /** click on send my password button */
    public void clickOnSendMyPassBtn() {
        sendMyPasswortBtn.click();
    }

    /** wait message on page after send data for recovery password */
    public void waitResetMessage() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(.,'sent a reset password link to your email.')]")));
    }

}
