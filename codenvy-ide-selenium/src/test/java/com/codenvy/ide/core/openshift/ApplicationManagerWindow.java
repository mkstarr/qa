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

package com.codenvy.ide.core.openshift;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Zaryana Dombrovskaya */
public class ApplicationManagerWindow extends AbstractTestModule {

    ApplicationManagerWindow(IDE ide) {
        super(ide);
    }

    private static int TIMEOUT = TestConstants.PAGE_LOAD_PERIOD / 100;


    interface Locators {

        String NAME       = "ideOpenShiftProjectViewNameField";
        String PUBLIC_URL = "ideOpenShiftProjectViewUrlField";
        String TYPE       = "ideOpenShiftProjectViewTypeField";
        String STATUS     = "ideOpenShiftProjectViewStatusLabel";

        String APPLICATION_MANAGER_WINDOW_ID = "ideOpenShiftProjectView-window";
        String CLOSE_BUTTON                  = "ideOpenShiftProjectViewCloseButton";
        String DELETE_APPLICATION_BUTTON     = "ideOpenShiftProjectViewDeleteButton";

        String DELETE_YES_BUTTON_DIALOG = "//*[@id='ideAskModalView-window']//div[contains(.,'Delete OpenShift application')]";

        String YES_BUTTON = "YesButton";
    }

    @FindBy(id = Locators.APPLICATION_MANAGER_WINDOW_ID)
    WebElement applicationManagerWindow;

    @FindBy(id = Locators.CLOSE_BUTTON)
    WebElement closeButtonProjectMenu;

    @FindBy(id = Locators.DELETE_APPLICATION_BUTTON)
    WebElement deleteApplicationFromProjectMenuButton;

    @FindBy(name = Locators.NAME)
    WebElement applicationName;

    @FindBy(name = Locators.PUBLIC_URL)
    WebElement applicationPublicUrl;

    @FindBy(id = Locators.TYPE)
    WebElement applicationType;

    @FindBy(id = Locators.STATUS)
    WebElement applicationStatus;

    @FindBy(xpath = Locators.DELETE_YES_BUTTON_DIALOG)
    WebElement deleteApplicationDialog;

    @FindBy(id = Locators.YES_BUTTON)
    WebElement yesButton;

    /**
     * Wait for
     * application window
     * in Project > PaaS > OpenShift
     */
    public void waitApplicationManagerWindow() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(applicationManagerWindow));
    }

    /**
     * Wait application
     * manager
     * is invisible
     */
    public void waitDisappearProjectMenu() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(
                By.id(Locators.APPLICATION_MANAGER_WINDOW_ID)));
    }

    public void waitApplicationName() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationName));
    }

    /** get Application NAME */
    public String getApplicationName() {
        waitApplicationName();
        return applicationName.getAttribute("value");
    }


    public void waitApplicationPublicUrl() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationPublicUrl));
    }

    /** get Application PUBLIC URL */
    public String getApplicationPublicUrl() {
        waitApplicationPublicUrl();
        return applicationPublicUrl.getText();
    }

    public void waitApplicationType() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationType));
    }

    /** get Application TYPE */
    public String getApplicationType() {
        waitApplicationType();
        return applicationType.getText();
    }

    public void waitApplicationStatus() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(applicationStatus));
    }

    /** get Application STATUS */
    public String getApplicationStatus() {
        waitApplicationStatus();
        return applicationStatus.getText();
    }


    /**
     * Wait for application status
     * in Project > PaaS > OpenShift
     */
    public void waitDeleteApplicationButtom() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(deleteApplicationFromProjectMenuButton));
    }


    public void clickDeleteApplicationFromProject() {
        waitDeleteApplicationButtom();
        deleteApplicationFromProjectMenuButton.click();
    }

    public void waitCloseApplicationMenu() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(closeButtonProjectMenu));
    }


    public void clickCloseApplicationMenu() {
        waitCloseApplicationMenu();
        closeButtonProjectMenu.click();
    }

    public void waitDeleteApplicationWindow() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(deleteApplicationDialog));
    }

    public void waitYesDeleteButton() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(yesButton));
    }

    public void clickYesInDeleteDialog() {
        waitDeleteApplicationWindow();
        waitYesDeleteButton();
        yesButton.click();
    }
}
