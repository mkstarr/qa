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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Musienko Maxim */

public class JavaScriptApplicationRunTest extends BaseTest {

    private static final String PROJECT = JavaScriptApplicationRunTest.class.getSimpleName();

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't delete test project");
        }
    }

    @Test
    public void javaScriptApplicationRunTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaScriptTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple JavaScript project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForMessageShow(1, 120);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");

        switchToNonCurrentWindow(CURRENT_WINDOW);

        new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.getTitle().equals("Sample Page");
            }
        });
    }

    @Test
    public void stopJavaWebApplicationTest() throws Exception {

        wait = new WebDriverWait(driver, 60);

        driver.switchTo().window(CURRENT_WINDOW);

        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.STOP_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(2, 120);
        assertTrue(IDE.OUTPUT.getOutputMessage(2).contains("stopped"));

        switchToNonCurrentWindow(CURRENT_WINDOW);

        assertEquals(driver.getTitle(), "Sample Page");
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'404')]")));
    }
}
