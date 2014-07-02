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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Musienko Maxim
 *
 */
public class ProfilePage extends AbstractTestModule {
    public ProfilePage(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private interface Locators {
        String LOGIN_FORM_CONTAINER = "form.change-password-form";

        String PROFILE_FORM = "form.cloud-ide-profile";

        String NEW_PASS_FIELD = "input[name=change_password]";

        String CONFIRM_PASS = "input[name=confirm_password]";

        String FIRST_NAME = "input[name=first_name]";

        String LAST_NAME = "input[name='last_name']";

        String PHONE_NUMBER = "input[name=phone_work]";

        String COMPANY = "input[name=company]";

        String JOB_TITILE = "select[type=selectbox]";

        String SUBMIT_PASSWORD_BTN = "//form[@class='change-password-form']//input[@type='submit' and @class='light-button']";

        String UPDATE_PROFILE_BTN = "input[value='Update profile']";

        String ERROR_MESSAGE_NOT_MATCHED_PASSWORD =
                "//div[@id='profileServiceErrorMessage' and text()='Please type your new password again. Both passwords must match.']";

        String ERROR_MESSAGE_EMPTY_PASSWORD =
                "//div[@id='profileServiceErrorMessage'  and text()='Please type your new password again. Both passwords must match.']";

        String ERROR_MESSAGE_FOR_EMPTY_FIRST_NAME = "//div [@id='profileServiceErrorMessage' and text()='Please, enter your first name']";

        String ERROR_MESSAGE_FOR_EMPTY_LAST_NAME = "//div [@id='profileServiceErrorMessage' and text()='Please, enter your last name']";

        String ERROR_MESSAGE_FOR_EMPTY_PHONE_NUMBER =
                "//div [@id='profileServiceErrorMessage' and text()='Please, enter your phone number']";

        String ERROR_MESSAGE_FOR_EMPTY_COMPANY = "//div [@id='profileServiceErrorMessage' and text()='Please, enter your company']";

        String ERROR_MESSAGE_FOR_EMPTY_ROLE = "//div [@id='profileServiceErrorMessage' and text()='Please, select your role']";

        String SUCCESS_MESSAGE_FOR_CHANGED_PROFILE =
                "//div [@id='profileServiceErrorMessage' and text()='Your profile was updated successfully']";

        String SUCCESS_MESSAGE_FOR_CHANGED_PASSWORD =
                "//div [@id='profileServiceErrorMessage' and text()='Your password was changed successfully']";

        String YOUR_EMAIL_ADRESS_VALUE_ID = "//*[@id='account_value' and text()='%s']";

    }


    public interface JobList {
        String SELECT_ROLE = "Please select role...";

        String DEVELOPER = "Developer";

        String ARCHITECT = "Architect";

        String PM = "Product Manager";

        String PROJECT = "Project Manager";

        String CEO_CTO = "CEO/CTO";

        String STUDENT = "Student";

        String OTHER = "Other";
    }

    @FindBy(css = ProfilePage.Locators.LOGIN_FORM_CONTAINER)
    WebElement loginFormContainer;

    @FindBy(css = Locators.PROFILE_FORM)
    WebElement profileContainer;

    @FindBy(css = Locators.NEW_PASS_FIELD)
    WebElement newPassField;

    @FindBy(css = Locators.CONFIRM_PASS)
    WebElement confirmPassField;

    @FindBy(css = Locators.FIRST_NAME)
    WebElement firstNameField;

    @FindBy(css = Locators.LAST_NAME)
    WebElement lastName;

    @FindBy(css = Locators.PHONE_NUMBER)
    WebElement phoneNumberField;

    @FindBy(css = Locators.COMPANY)
    WebElement companyField;

    @FindBy(css = Locators.JOB_TITILE)
    WebElement jobTytleDropDownList;

    @FindBy(css = Locators.UPDATE_PROFILE_BTN)
    WebElement updateProfileBtn;

    @FindBy(xpath = Locators.SUBMIT_PASSWORD_BTN)
    WebElement submitPassBtn;

    @FindBy(xpath = Locators.ERROR_MESSAGE_NOT_MATCHED_PASSWORD)
    WebElement notMatchedPassError;

    @FindBy(xpath = Locators.ERROR_MESSAGE_EMPTY_PASSWORD)
    WebElement errorEmptyPass;


    /** wait main login form on profile page */
    public void waitloginSectionProfilePage() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(loginFormContainer));
    }

    /** wait profile section on page with the main fields */
    public void waitProfileSection() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(profileContainer));
    }

    /** wait err mess if we enter not same passwords into  new password field and confirm password field */
    public void waitErrorMessWithNotMatchedPass() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(notMatchedPassError));
    }

    /** wait err mess if we enter not same passwords into  new password field and confirm password field */
    public void waitEmailAdressWithValue(String email) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(notMatchedPassError));
    }

    /** wait err mess if we do not enter password into new password field and confirm password field */
    public void waitErrorMessWithEmptyPassField() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(errorEmptyPass));
    }


    /** wait err mess if we do not enter name into 'first name' field */
    public void waitErrorMessWithEmptyFirstNameField() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESSAGE_FOR_EMPTY_FIRST_NAME)));
    }

    /** wait err mess if we do not enter laste name into 'last name' field */
    public void waitErrorMessWithEmptyLastNameField() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESSAGE_FOR_EMPTY_LAST_NAME)));
    }

    /** wait err mess if we do not enter number name into 'phone number' field */
    public void waitErrorMessWithEmptyPhoneNumberField() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESSAGE_FOR_EMPTY_PHONE_NUMBER)));
    }


    /** wait err mess if we do not enter name into 'company' field */
    public void waitErrorMessWithEmptyCompanyField() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESSAGE_FOR_EMPTY_COMPANY)));
    }


    /** wait err mess if we do not select role from drop down list */
    public void waitErrorMessWithNotSelectedRole() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ERROR_MESSAGE_FOR_EMPTY_ROLE)));
    }


    /** wait success mess if we have changed password correctly */
    public void waitConfirmMessForChangedPass() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.SUCCESS_MESSAGE_FOR_CHANGED_PASSWORD)));
    }


    /** wait success mess if we have changed profile correctly */
    public void waitConfirmMessForChangedProfile() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.SUCCESS_MESSAGE_FOR_CHANGED_PROFILE)));
    }


    /** wait first name field with specific placeholder text* */
    public void waitFirstNameFieldWitSpecificText(final String text) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return getPlaceholderTxtFirsName().equals(text);

            }
        });
    }


    /** wait last name field with specific placeholder text* */
    public void waitLastNameFieldWitSpecificText(final String text) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return getPlaceholderTxtLastName().equals(text);

            }
        });
    }


    /** wait last name field with specific placeholder text* */
    public void waitPhoneNumberFieldWithText(final String text) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return phoneNumberField.getAttribute("value").equals(text);

            }
        });
    }


    /** wait company name field with specific placeholder text* */
    public void waitCompanyFieldWithSpecificText(final String text) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return companyField.getAttribute("value").equals(text);

            }
        });
    }


    /** wait role with specific setted text* */
    public void waitRoleListdWithSpecificText(final String text) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return jobTytleDropDownList.getAttribute("value").equals(text);

            }
        });
    }


    /** wait main elements UI on profile page */
    public void waitProfilePage() {
        waitProfileSection();
        waitloginSectionProfilePage();
    }

    /**
     * set new password
     *
     * @param pass
     */
    public void typeNewPass(String pass) {
        newPassField.clear();
        newPassField.sendKeys(pass);
    }


    /**
     * set confirming pass
     *
     * @param pass
     */
    public void typeConfirmedPass(String pass) {
        confirmPassField.clear();
        confirmPassField.sendKeys(pass);
    }


    /**
     * set new first name
     *
     * @param firstName
     */
    public void typeFirstName(String firstName) {
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }


    /**
     * set new last name
     *
     * @param firstName
     */
    public void typeLastName(String firstName) {
        lastName.clear();
        lastName.sendKeys(firstName);
    }

    /**
     * return placeholder text from 'First name field' field
     *
     * @return
     */
    public String getPlaceholderTxtFirsName() {
        return firstNameField.getAttribute("value");
    }

    /**
     * return placeholder text from 'last name field' field
     *
     * @return
     */
    public String getPlaceholderTxtLastName() {
        return lastName.getAttribute("value");
    }

    /**
     * set your phone number in phone number field
     *
     * @param phone
     */
    public void typePhone(String phone) {
        phoneNumberField.clear();
        phoneNumberField.sendKeys(phone);
    }

    /**
     * set company name in the company field on page
     *
     * @param company
     */
    public void typeCompany(String company) {
        companyField.clear();
        companyField.sendKeys(company);
    }


    public String getCurrentValueFromDropList() {
        return jobTytleDropDownList.getCssValue("value");
    }


    /**
     * select job in drop downList by name
     *
     * @param typOfJob
     */
    public void selectJobFromDropList(String typOfJob) {
        Select box = new Select(jobTytleDropDownList);
        box.selectByVisibleText(typOfJob);
    }

    /** click on submit password button */
    public void clickSubmitPass() {
        submitPassBtn.click();
    }


    /** click on update profile button */
    public void clickUpdateProfileBtn() {
        updateProfileBtn.click();

    }

    /** clear all fields except role dropdown* */
    public void clearProfileFields() {
        firstNameField.clear();
        lastName.clear();
        phoneNumberField.clear();
        companyField.clear();
    }

    /**
     * wait for user email in profile page
     *
     * @param email
     *         email
     */
    public void waitUserEmailInProfilePage(String email) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.YOUR_EMAIL_ADRESS_VALUE_ID, email))));
    }

}
