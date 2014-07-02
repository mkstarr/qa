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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ImportFromGitHub extends AbstractTestModule {
    /** @param ide */
    public ImportFromGitHub(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String IMPORT_FROM_GITHUB_FORM_ID = "ideImportFromGithubView-window";

        String PROJECT_BY_NAME_SELECTOR = "//table[@id='ideGithubProjectsGrid']//div[text()='%s']";

        String NAME_FIELD = "ideImportFromGithubViewNameField";

        String READ_ONLY_CHECK_BOX = "ideImportFromGithubViewReadOnlyModeField";

        String FINISH_BTN_ID = "ideImportFromGithubViewFinishButton";

        String CANCEL_BTN_ID = "ideImportFromGithubViewCancelButton";

        String LOGIN_FORM = "ideGithubLoginView-window";

        String OK_BUTTON_ON_LOGIN_FORM = "ideGithubLoginViewAuthButton";

        String CLONING_PROGRESS_FORM = "ide.modalJob.view-window";

        String EXPAND_ACCOUNTS_MENU_BUTTON = "//div/img[@image-id='suggest-image']";

        String ACCOUNTS_LIST = "//div[@id='exoSuggestPanel']";

        String SELECT_ACCOUNT = "//td[@role='menuitem' and text()='%s']";

        String IMPORT_PROJECTS_ROWS = "//table[@id='ideGithubProjectsGrid']//tbody//tr[@__gwt_row]";

    }

    @FindBy(id = Locators.IMPORT_FROM_GITHUB_FORM_ID)
    WebElement importFromGithubView;

    @FindBy(name = Locators.NAME_FIELD)
    WebElement nameSelectField;

    @FindBy(name = Locators.READ_ONLY_CHECK_BOX)
    WebElement readOnlyCheckBox;

    @FindBy(id = Locators.FINISH_BTN_ID)
    WebElement finishBtn;

    @FindBy(id = Locators.CANCEL_BTN_ID)
    WebElement cancelBtn;

    @FindBy(id = Locators.LOGIN_FORM)
    WebElement loginForm;

    @FindBy(id = Locators.OK_BUTTON_ON_LOGIN_FORM)
    WebElement okButtonOnLoginForm;

    @FindBy(id = Locators.CLONING_PROGRESS_FORM)
    WebElement cloningProgressForm;

    @FindBy(xpath = Locators.EXPAND_ACCOUNTS_MENU_BUTTON)
    WebElement expandAccountsMenuBtn;

    /** wait opening login form to github */
    public void waitLoginFormToGithub() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return loginForm.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait opening button 'Import from github' form opened in IDE */
    public void waitImportFromGithubFormOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return importFromGithubView.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait while for  load form will closed */
    public void waitImportFromGithubFormClosed() {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                        .id(Locators


                                                                                                                    .IMPORT_FROM_GITHUB_FORM_ID)));
    }

    /**
     * wait while for cloning form will closed
     *
     * @throws Exception
     */

    public void waitCloningProgressFormClosed() throws Exception {
        new WebDriverWait(driver(), 120).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                       .id(Locators
                                                                                                                   .CLONING_PROGRESS_FORM)));
        IDE().LOADER.waitClosed();
    }

    /** wait checkBox readOnly is selected form opened in IDE */
    public void waitReadOnlyCheckBoxIsChecked() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return readOnlyCheckBox.isSelected();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait checkBox readOnly is unselected form opened in IDE */
    public void waitReadOnlyCheckBoxIUnChecked() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return !readOnlyCheckBox.isSelected();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * select project by name in 'Import from github' form
     *
     * @param name
     */
    public void selectProjectByName(String name) {
        driver().findElement(By.xpath(String.format(Locators.PROJECT_BY_NAME_SELECTOR, name))).click();
    }

    /**
     * type project in Name Feld
     *
     * @param name
     */
    public void typeNameOfTheProject(String name) {
        nameSelectField.clear();
        nameSelectField.sendKeys(name);
    }

    /** click on 'Read only' checkBox */
    public void readOnlyCheckBoxClick() {
        readOnlyCheckBox.click();
    }

    /** click on 'Finish' button */
    public void finishBtnClick() {
        finishBtn.click();
    }

    /** click on 'Cancel' button */
    public void cancelBtnClick() {
        cancelBtn.click();
    }

    /** click ok on login to github form */
    public void clickOkOnLoginForm() {
        okButtonOnLoginForm.click();
    }

    public void clickOnExpandGitHubAccountsButton() {
        expandAccountsMenuBtn.click();
    }

    public void chooseGitHubAccount(String accountName) {
        WebElement element = driver().findElement(By.xpath(String.format(Locators.SELECT_ACCOUNT, accountName)));
        element.click();
    }

    public void waitGitHubAccountsList() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ACCOUNTS_LIST)));
    }

    public void waitProjectInImportList(String projectName) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.PROJECT_BY_NAME_SELECTOR, projectName))));
        IDE().LOADER.waitClosed();
    }

    public void waitGitHubAccountsListDisappear() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.ACCOUNTS_LIST)));
    }

    /**
     * return count of a rows imported projects
     *
     * @return
     */
    public int getProjectsCount() {
        List<WebElement> projects = driver().findElements(By.xpath(Locators.IMPORT_PROJECTS_ROWS));
        return projects.size();
    }


    /**
     * select github account
     *
     * @throws Exception
     */
    public void selectGitHubAccount(String accountName) throws Exception {
        clickOnExpandGitHubAccountsButton();
        waitGitHubAccountsList();
        chooseGitHubAccount(accountName);
        waitGitHubAccountsListDisappear();
    }

}
