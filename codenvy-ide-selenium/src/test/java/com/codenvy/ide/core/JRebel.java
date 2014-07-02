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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 @author Roman Iuvshin
 *
 */
public class JRebel extends AbstractTestModule {

    public JRebel(IDE ide) {
        super(ide);
    }

    //Locators basic elements Dialog About Menu
    private interface Locators {

        String JREBEL_FORM = "//div[@view-id='ideJRebelUserInfoView']";

        String FIRST_NAME_FIELD_ID = "jrebelprofilefirstname";

        String LAST_NAME_FIELD_ID = "jrebelprofilelastname";

        String PHONE_FIELD_ID = "jrebelprofilephone";

        String JREBEL_OK_BUTTON_ID = "eXoJRebelUserInfoViewOkButton";
    }

    //WebElemnts
    @FindBy(id = Locators.JREBEL_OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(id = Locators.FIRST_NAME_FIELD_ID)
    private WebElement firstNameField;

    @FindBy(id = Locators.LAST_NAME_FIELD_ID)
    private WebElement lastNameField;

    @FindBy(id = Locators.PHONE_FIELD_ID)
    private WebElement phoneField;

    /** wait JRebel form */
    public void waitJRebelForm() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.JREBEL_FORM)));
    }

    /** wait JRebel form */
    public void waitJRebelFormDisappear() {
        new WebDriverWait(driver(), 30).until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.JREBEL_FORM)));
    }

    /**
     * type first name
     * @param firstName - user first name
     */
    public void typeFirstName(String firstName) {
        firstNameField.sendKeys(firstName);
    }

    /**
     * type last name
     * @param lastName - user last name
     */
    public void typeLastName(String lastName) {
        lastNameField.sendKeys(lastName);
    }

    /**
     * type phone
     * @param phone - user phone
     */
    public void typePhoneNumber(String phone) {
        phoneField.sendKeys(phone);
    }

    public void clickOkButton(){
        okButton.click();
        waitJRebelFormDisappear();
    }

}
