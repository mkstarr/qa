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
package com.codenvy.ide.runner;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.fail;

/**
 @author Roman Iuvshin
 *
 */

public class AndroidAppRunTest extends BaseTest {

    private static final String PROJECT = AndroidAppRunTest.class.getSimpleName();

    private static final String MANYMO_TOKEN = "C088N4i6pBp5aFXYLQYgQ46sXmkAGFk7GkJiSwMF";

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @Test
    public void androidAppRunTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectAndroidTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectManymoPaaS();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Android project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitActiveFinishButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.ASK_FOR_VALUE_DIALOG.waitAskForValueForm();
        IDE.ASK_FOR_VALUE_DIALOG.setValueInForm(MANYMO_TOKEN);
        IDE.LOADER.waitClosed();
        IDE.ASK_FOR_VALUE_DIALOG.clickOkBtn();

        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Application " + PROJECT + " started on");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("manymo");

        switchToNonCurrentWindow(CURRENT_WINDOW);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='emulator-status']//div[contains(.,'Connected')]")));
    }
}
