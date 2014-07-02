/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

/**
 * @author Musienko Maxim
 *
 */
public class ImportASampleProject extends AbstractTestModule {
    /** @param ide */
    public ImportASampleProject(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String IMPORT_FROM_A_SAMPLE_PROJECT_FORM_ID = "GithubSamplesView-window";

        String SELECT_SAMPLE_BY_NAME_PRJ = "//table[@id='ideGithubSamplesGrid']//div[text()='%s']";

        String CLOSE_WINDOW_BTN = "//div[@id='GithubSamplesView-window']//img[@title='Close']";

        String CANCEL_BUTTON_ID = "ideImportSamplesCloseButtonId";

        String NEXT_BUTTON_ID = "ideImportSamplesNextButtonId";

        String NEXT_BUTTON_ID_ENABLED = "//div[@id='ideImportSamplesNextButtonId' and @ button-enabled ='true']";

        String NEXT_BUTTON_ID_DISABLED = "//div[@id='ideImportSamplesNextButtonId' and @ button-enabled ='false']";

        String NAME_FIELD = "//div[@id='GithubSamplesView-window']//input";

        String CLONE_JOB_PROGRESSOR_WINDOW = "ide.modalJob.view-window";

    }

    @FindBy(id = Locators.IMPORT_FROM_A_SAMPLE_PROJECT_FORM_ID)
    WebElement sampleProjectFormId;

    @FindBy(xpath = Locators.CLOSE_WINDOW_BTN)
    WebElement closeWindowBtn;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelBtn;

    @FindBy(id = Locators.NEXT_BUTTON_ID)
    WebElement nextBtn;

    @FindBy(xpath = Locators.NEXT_BUTTON_ID_ENABLED)
    WebElement nextBtnEnabled;

    @FindBy(xpath = Locators.NEXT_BUTTON_ID_DISABLED)
    WebElement nextBtnDisabled;

    @FindBy(xpath = Locators.NAME_FIELD)
    WebElement nameInputField;

    /** wait while in input field appears selected project */
    public void waitIntoNameInputFileldIsSelected(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                WebElement elem = driver().findElement(By.xpath(Locators.NAME_FIELD));
                return IDE().INPUT.getValue(elem).equals(name);
            }
        });

    }

    /** wait appearance import sample project form */
    public void waitImportFormOpened() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(sampleProjectFormId));
    }

    /** wait while next button becomes enabled */
    public void waitNextBtnIsEnabled() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(nextBtnEnabled));
    }

    /** wait while next button becomes disabled */
    public void waitNextBtnIsDisabled() {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOf(nextBtnDisabled));
    }

    /** wait while next button becomes disabled */
    public void waitCloneProgressorClosed() {
        new WebDriverWait(driver(), 60).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators












                                                                                                                  .CLONE_JOB_PROGRESSOR_WINDOW)));
    }

    /** wait while next button becomes disabled */
    public void waitCloneProgressorOpened() {
        new WebDriverWait(driver(), 60).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators.CLONE_JOB_PROGRESSOR_WINDOW)));
    }

    /** wait appearance import sample project form */
    public void waitProjectByName(final String name) {
        new WebDriverWait(driver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.SELECT_SAMPLE_BY_NAME_PRJ, name))));
    }

    /** wait appearance import sample project form */
    public void selectProjectByName(final String name) {
        driver().findElement(By.xpath(String.format(Locators.SELECT_SAMPLE_BY_NAME_PRJ, name))).click();
    }

    /** close form with close-window button */
    public void closeWindowBtnClick() {
        closeWindowBtn.click();
    }

    /** click on next button */
    public void clickNextBtn() {
        nextBtn.click();
    }

}
