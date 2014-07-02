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

public class ApplicationSettingsAppEngineWindow extends AbstractTestModule {

    interface Locators {
        String GAE_APP_SETTINGS           = "//li[@id='ae-nav-administration']//a[contains(.,'Application Settings')]";
        String GAE_DISABLE_APP            = "//input[@value='Disable Application...']";
        String GAE_DISABLE_APP_NOW        = "//input[@value='Disable Application Now']";
        String GAE_PERMANENT_DELETION_APP = "//input[@value='Request Permanent Deletion']";
        String GAE_REVERT_DELETION_APP    = "//input[@value='Revert Deletion Request']";
    }

    @FindBy(xpath = Locators.GAE_APP_SETTINGS)
    WebElement gaeApplicationSettings;

    @FindBy(xpath = Locators.GAE_DISABLE_APP)
    WebElement gaeDisableApplication;

    @FindBy(xpath = Locators.GAE_DISABLE_APP_NOW)
    WebElement gaeDisableApplicationNow;

    @FindBy(xpath = Locators.GAE_PERMANENT_DELETION_APP)
    WebElement gaePermanentDeletionApplication;

    @FindBy(xpath = Locators.GAE_REVERT_DELETION_APP)
    WebElement gaeRevertDeletionApplication;


    ApplicationSettingsAppEngineWindow(IDE ide) {
        super(ide);
    }

    /** wait Application Settings item in AppEngine */
    public void waitApplicationSettings() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeApplicationSettings));
    }


    /** click on Application Settings item in AppEngine */
    public void clickGaeApplicationSettings() {
        waitApplicationSettings();
        gaeApplicationSettings.click();
    }

    /** wait Disable Application button in AppEngine */
    public void waitDisableApplication() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeDisableApplication));
    }

    /** click on Disable Application button in AppEngine */
    public void clickGaeDisableApplication() {
        waitDisableApplication();
        gaeDisableApplication.click();
    }

    /** wait Disable Application Now button in AppEngine */
    public void waitGaeDisableApplicationNow() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeDisableApplicationNow));
    }

    /** click on Disable Application Now button in AppEngine */
    public void clickGaeDisableApplicationNow() {
        waitGaeDisableApplicationNow();
        gaeDisableApplicationNow.click();
    }

    /** wait Permanent Deletion button in AppEngine */
    public void waitGaePermanentDeletionApplication() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaePermanentDeletionApplication));
    }

    /** click on Permanent Deletion button in AppEngine */
    public void clickGaePermanentDeletionApplication() {
        waitGaePermanentDeletionApplication();
        gaePermanentDeletionApplication.click();
    }

    /** wait Revert Deletion button in AppEngine */
    public void waitGaeRevertDeletionApplication() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(gaeRevertDeletionApplication));
    }


    /** delete Application in AppEngine */
    public void deleteGaeApplication(String projectName) {
        IDE().GAE.applicationAppEngineWindow.waitAndClickApplication(projectName);
        clickGaeApplicationSettings();
        clickGaeDisableApplication();
        clickGaeDisableApplicationNow();
        clickGaePermanentDeletionApplication();
        waitGaeRevertDeletionApplication();
    }

}