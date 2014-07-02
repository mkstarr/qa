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

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Zaryana Dombrovskaya */

public class ApplicationAppEngineWindow extends AbstractTestModule {

    interface Locators {
        String GAE_APPLICATION        = "//table[@id='ae-apps-all']//a[contains(.,'%s')]";
        String GAE_APP_STATUS_RUNNING = "//table[@id='ae-apps-all']//a[contains(.,'%s')]//";
        String GAE_APP_STATUS_DISABLE = "//table[@id='ae-apps-all']//a[contains(.,'%s')]//";
    }


    ApplicationAppEngineWindow(IDE ide) {
        super(ide);

    }

    /** open google account page */
    public void openGoogleAppEngine() {
        driver().get("https://appengine.google.com");
    }

    /**
     * wait and click on application in AppEngine
     *
     * @param projectName
     */
    public void waitAndClickApplication(String projectName) {
        new WebDriverWait(driver(), 40)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.GAE_APPLICATION, projectName))));
        driver().findElement(By.xpath(String.format(Locators.GAE_APPLICATION, projectName))).click();
    }

    /**
     * wait Application Status Running in AppEngine
     *
     * @param projectName
     */
    public void waitGaeApplicationStatusRunning(String projectName) {                 //for PaaSTwoSidedTest
        new WebDriverWait(driver(), 40).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.GAE_APP_STATUS_RUNNING, projectName))));
        ;
    }

    /**
     * wait Application Status Disable in AppEngine
     *
     * @param projectName
     */
    public void waitGaeApplicationStatusDisabled(String projectName) {                 //for PaaSTwoSidedTest
        new WebDriverWait(driver(), 40).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.GAE_APP_STATUS_DISABLE, projectName))));
        ;
    }

}
