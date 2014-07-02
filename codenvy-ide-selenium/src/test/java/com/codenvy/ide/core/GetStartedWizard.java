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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 @author Roman Iuvshin
 *
 */

public class GetStartedWizard extends AbstractTestModule {

    public GetStartedWizard(IDE ide) {
        super(ide);
    }

    private interface Locators {

        String GET_STARTED_WIZARD = "//div[@view-id='codenvyGetStartedView']";

        String GET_STARTED_WIZARD_CHOOSE_A_TECHNOLOGY = "//div[@view-id='codenvyGetStartedView' and contains(.,'Choose a technology')]";

        String SKIP_BUTTON = "Skip >";

        String GET_STARTED_BUTTON = "//button[text()='Get Started']";

        String PROJECT_NAME = "codenvyGetStartedWizardProjectName";

        String PHP_PROJECT_ID = "CREATE-PROJECT-PHP";

        String JSP_PROJECT_ID = "CREATE-PROJECT-Servlet/JSP";

        String JAR_PROJECT_ID = "CREATE-PROJECT-Jar";

        String JS_PROJECT_ID = "CREATE-PROJECT-JavaScript";

        String PYTHON_PROJECT_ID = "CREATE-PROJECT-Python";

        String RUBY_PROJECT_ID = "CREATE-PROJECT-Rails";

        String SPRING_PROJECT_ID = "CREATE-PROJECT-Spring";

        String MULTI_MODULE_PROJECT_ID = "CREATE-PROJECT-Maven Multi-module";

        String NODE_JS_PROJECT_ID = "CREATE-PROJECT-nodejs";

        String NEXT_BUTTON = "//button[text()='Next']";

        String BACK_BUTTON = "//button[text()='Back']";

        String FINISH_BUTTON = "//button[text()='Finish']";

        String NONE_PAAS = "CREATE-PROJECT-none";
    }

    @FindBy(xpath = Locators.GET_STARTED_WIZARD)
    private WebElement view;
    
    @FindBy(linkText = Locators.SKIP_BUTTON)
    private WebElement skipBtn;

    @FindBy(xpath = Locators.GET_STARTED_BUTTON)
    private WebElement getStartedBtn;

    @FindBy(id = Locators.PROJECT_NAME)
    private WebElement projectNameInput;

    @FindBy(xpath = Locators.NEXT_BUTTON)
    private WebElement nextBtn;

    @FindBy(xpath = Locators.BACK_BUTTON)
    private WebElement backBtn;

    @FindBy(xpath = Locators.FINISH_BUTTON)
    private WebElement finishBtn;

    @FindBy(id = Locators.NONE_PAAS)
    private WebElement nonePaas;

    @FindBy(id = Locators.PHP_PROJECT_ID)
    private WebElement phpProject;

    @FindBy(id = Locators.JSP_PROJECT_ID)
    private WebElement jspProject;

    @FindBy(id = Locators.JAR_PROJECT_ID)
    private WebElement jarProject;

    @FindBy(id = Locators.JS_PROJECT_ID)
    private WebElement jsProject;

    @FindBy(id = Locators.PYTHON_PROJECT_ID)
    private WebElement pythonProject;

    @FindBy(id = Locators.RUBY_PROJECT_ID)
    private WebElement rubyProject;

    @FindBy(id = Locators.SPRING_PROJECT_ID)
    private WebElement springProject;

    @FindBy(id = Locators.MULTI_MODULE_PROJECT_ID)
    private WebElement multiModuleProject;

    @FindBy(id = Locators.NODE_JS_PROJECT_ID)
    private WebElement nodeJsProject;

    /** click skip button */
    public void clickSkipButton() {
        skipBtn.click();
    }

    /** Wait get started wizzard  appear */
    public void waitGetStartedWizardOpened() {
        new WebDriverWait(driver(), 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.GET_STARTED_WIZARD)));
    }

    /** Wait get started wizzard  Choose a technology page */
    public void waitGetStartedWizardChooseAtechnology() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.GET_STARTED_WIZARD_CHOOSE_A_TECHNOLOGY)));
    }

    /** Wait get started wizzard disappear */
    public void waitGetStartedWizardClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.GET_STARTED_WIZARD)));
    }

    /** skip wizard */
    public void waitAndCloseWizard() throws InterruptedException{
        waitGetStartedWizardOpened();
        IDE().LOADER.waitClosed();
        clickSkipButton();
    }

    /** select php project */
    public void selectPhpProject() {
        phpProject.click();
    }

    /** select jsp project */
    public void selectJspProject() {
        jspProject.click();
    }

    /** select jar project */
    public void selectJarProject() {
        jarProject.click();
    }

    /** select js project */
    public void selectJsProject() {
        jsProject.click();
    }

    /** select python project */
    public void selectPythonProject() {
        pythonProject.click();
    }

    /** select ruby project */
    public void selectRubyProject() {
        rubyProject.click();
    }

    /** select spring project */
    public void selectSpringProject() {
        springProject.click();
    }

    /** select multi module project */
    public void selectMultiModuleProject() {
        multiModuleProject.click();
    }

    /** select node.js project */
    public void selectNodeJsProject() {
        nodeJsProject.click();
    }


    /** select paas None */
    public void selectNonePaas() {
        nonePaas.click();
    }

    /** click on get started button */
    public void clickOnGetStartedButton() {
        getStartedBtn.click();
    }

    /** click on next button */
    public void clickOnNextButton() {
        nextBtn.click();
    }

    /** click on next button */
    public void clickOnBackButton() {
        backBtn.click();
    }

    /** click on finish button */
    public void clickOnFinishButton() {
        finishBtn.click();
    }

    /** type new project name */
    public void typeProjectName(String name) {
        projectNameInput.clear();
        projectNameInput.sendKeys(name);
    }
    
    @Deprecated
    public boolean isOpened() {
        try {
            return view != null && view.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TODO remove 
     * To make ensure the wizard is closed.
     */
    public void makeSureWizardIsClosed() {
        if (isOpened()) {
            try {
                waitAndCloseWizard();
            } catch (InterruptedException e) {
                throw new TimeoutException(e);
            }
        }
    }
}
