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

package com.codenvy.ide.core.gae;

import com.codenvy.ide.IDE;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Zaryana Dombrovskaya */
public class CreateApplicationWindow extends AbstractTestModule {

    private String templateWar = "Google App Engine Java project. Illustrates simple examples that use the Search API.";

    private String templatePhp = "Simple PHP project for Google AppEngine.";

    private static String currentWindow = null;

    private WebDriverWait wait;

    interface Locators {
        String GAE_CREATE_BUTTON     = "//div[@view-id='exoCreateApplicationView']//td[text()='Create']";
        String GAE_DEPLOY_BUTTON     = "//div[@view-id='exoCreateApplicationView']//td[text()='Deploy']";
        String GAE_APP_ID            = "ae-createapp-id";
        String GAE_APP_TITLE         = "title";
        String GAE_ACCEPT_AGREEMENT  = "tos_accept";
        String GAE_APP_CREATE_BUTTON = "ae-createapp-submit";
    }

    @FindBy(xpath = Locators.GAE_CREATE_BUTTON)
    WebElement createApplicationButton;

    @FindBy(xpath = Locators.GAE_DEPLOY_BUTTON)
    WebElement deployApplicationButton;

    @FindBy(id = Locators.GAE_APP_ID)
    WebElement gaeApplicationId;

    @FindBy(id = Locators.GAE_APP_TITLE)
    WebElement gaeApplicationTitle;

    @FindBy(id = Locators.GAE_ACCEPT_AGREEMENT)
    WebElement gaeAcceptAgreement;

    @FindBy(id = Locators.GAE_APP_CREATE_BUTTON)
    WebElement gaeApplicationCreateButton;

    CreateApplicationWindow(IDE ide) {
        super(ide);
    }

    /** wait for create application button in IDE */
    public void waitCreateApplicationButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(createApplicationButton));
    }

    /** click on create application button in IDE */
    public void clickCreateApplicationButton() {
        waitCreateApplicationButton();
        createApplicationButton.click();
    }

    /** wait for deploy application button in IDE */
    public void waitDeployApplicationButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(deployApplicationButton));
    }

    /** click on deploy application button in IDE */
    public void clickDeployApplicationButton() {
        waitDeployApplicationButton();
        deployApplicationButton.click();
    }

    public void waitGaeApplicationId() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeApplicationId));
    }

    public void waitGaeApplicationTitle() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeApplicationTitle));
    }

    public void waitGaeApplicationCreateButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeApplicationCreateButton));
    }

    /** wait create application form in AppEngine */
    public void waitCreateApplicationForm() {
        waitGaeApplicationId();
        waitGaeApplicationTitle();
        waitGaeApplicationCreateButton();
    }

    /**
     * type application id in AppEngine
     *
     * @param ApplicationId
     */
    public void typeApplicationId(String ApplicationId) {
        gaeApplicationId.sendKeys(ApplicationId);
    }

    /**
     * type application title in AppEngine
     *
     * @param ApplicationTitle
     */
    public void typeApplicationTitle(String ApplicationTitle) {
        gaeApplicationTitle.sendKeys(ApplicationTitle);
    }

    public boolean isPresented() {
        try {
            return gaeAcceptAgreement != null && gaeAcceptAgreement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** accept agreement in AppEngine */
    public void acceptGaeAgreement() {
        if (isPresented()) {
            gaeAcceptAgreement.click();
        } else {
        }
    }

    /** click create application in AppEngine */
    public void clickGaeCreateApplicationButton() {
        gaeApplicationCreateButton.click();
    }

    /**
     * create WAR project with Google AppEngine template
     *
     * @param projectName
     * @throws Exception
     */
    public void createWarProject(String projectName) throws Exception {
        IDE().WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.typeProjectName(projectName);
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectJavaWebApplicationTechnology();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectGAEPaaS();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate(templateWar);
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
    }

    /**
     * create PHP project with Google AppEngine template
     *
     * @param projectName
     * @throws Exception
     */
    public void createPhpProject(String projectName) throws Exception {
        IDE().WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.typeProjectName(projectName);
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectPHPTechnology();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectGAEPaaS();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate(templatePhp);
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
    }

    /**
     * create Application in AppEngine
     *
     * @param applicationId
     * @param applicationTitle
     * @throws Exception
     */
    public void createApplication(String applicationId, String applicationTitle) throws Exception {

        currentWindow = driver().getWindowHandle();
        wait = new WebDriverWait(driver(), 60);

        clickCreateApplicationButton();

        switchToNonCurrentWindow(currentWindow);

        waitCreateApplicationForm();
        typeApplicationId(applicationId);
        typeApplicationTitle(applicationTitle);
        acceptGaeAgreement();
        clickGaeCreateApplicationButton();

        driver().switchTo().window(currentWindow);

        clickDeployApplicationButton();
        IDE().LOADER.waitClosed();
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
    }
}
