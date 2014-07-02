/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class BitBucket extends AbstractTestModule {
    /** @param ide */
    public BitBucket(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private String bitbucketUrl = "https://bitbucket.org";

    private interface Locators {
        String NAME_FIELD_FORM_ID     = "id_username";
        String PASSWORD_FIELD_FORM_ID = "id_password";
        String LOGIN_BTN              = "//a[text()='Log in']";
        String LOGIN_FORM_ID          = "login";
        String SIGN_IN_BTN            = "submit";
        String CLOUD_IDE              = "//a[@class='aui-button aui-button-split-main launch-ide']";
        String CLOUD_IDE_BUTTON       = "//button[@class='aui-button aui-dropdown2-trigger aui-button-split-more']";
        String CLOUD_IDE_CODENVY      = "//a[text()='Codenvy-Bitbucket']";
        String CODENVY_LINK           = "//a[@class='aui-button aui-button-split-main launch-ide']";
        String CLOUD_IDE_LIST         = "cloud-ide-vendors";
        String PROJECT_SOURCE         = "repo-source-link";
        String TEST_FILE              = "//a[@title='changelog.txt']";

    }

    @FindBy(xpath = Locators.LOGIN_BTN)
    private WebElement loginBtn;

    @FindBy(id = Locators.LOGIN_FORM_ID)
    private WebElement loginForm;


    @FindBy(id = Locators.NAME_FIELD_FORM_ID)
    private WebElement loginField;

    @FindBy(id = Locators.PASSWORD_FIELD_FORM_ID)
    private WebElement passwordField;


    @FindBy(name = Locators.SIGN_IN_BTN)
    private WebElement signInBtn;

    @FindBy(xpath = Locators.CLOUD_IDE)
    private WebElement cloudIde;

    @FindBy(xpath = Locators.CLOUD_IDE_BUTTON)
    private WebElement cloudIdeButton;

    @FindBy(id = Locators.CLOUD_IDE_LIST)
    private WebElement cloudIdeList;

    @FindBy(xpath = Locators.CLOUD_IDE_CODENVY)
    private WebElement cloudIdeCodenvy;

    @FindBy(xpath = Locators.CODENVY_LINK)
    private WebElement codenvyFactoryLink;

    @FindBy(id = Locators.PROJECT_SOURCE)
    private WebElement projectSource;

    @FindBy(xpath = Locators.TEST_FILE)
    private WebElement testFile;

    /** go to bitbucket main page and wait login button */
    public void goToBitbacket() {
        driver().get(bitbucketUrl);
        waitLoginButton();
    }


    /** wait login btn on bitbucket main page */
    public void waitLoginButton() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.LOGIN_BTN)));
    }

    /** wait login bitbucket form */
    public void waitLoginForm() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(loginBtn));
    }

    /** click on bitbucket btn in main github form */
    public void clickOnLoginBtn() {
        loginBtn.click();
    }

    /** wait cloud ide list */
    public void waitCloudIdeForm() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(cloudIde));
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(cloudIdeButton));
    }


    public void waitCloudIdeList() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(cloudIdeList));
    }

    /** click on cloud ide list button */
    public void clickOnCloudIdeListBtn() {
        waitCloudIdeForm();
        cloudIdeButton.click();
        waitCloudIdeList();
    }



    /** wait codenvy cloud ide in list */
    public void waitCloudIdeCodenvy() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(cloudIdeCodenvy));
    }

    /** click on codenvy cloud ide in list */
    public void clickOnCloudIdeCodenvy() {
        waitCloudIdeCodenvy();
        cloudIdeCodenvy.click();
    }

    public void waitCodenvyFactory() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(codenvyFactoryLink));
    }

    public String createFactoryUrl() {
        waitCodenvyFactory();
        String factoryUrl = codenvyFactoryLink.getAttribute("href");


        return factoryUrl;
    }

    /** login to bitbucket and sign into bitbucket workspace */
    public void signInToBitBucket(String login, String password) {
        clickOnLoginBtn();
        waitLoginForm();
        loginField.clear();
        passwordField.clear();
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        signInBtn.click();
    }

    /**
     * wait project with specified text into project list
     *
     * @param project
     */
    public void selectProjectInProjectList(String project) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(project)));
        driver().findElement(By.linkText(project)).click();
    }

}