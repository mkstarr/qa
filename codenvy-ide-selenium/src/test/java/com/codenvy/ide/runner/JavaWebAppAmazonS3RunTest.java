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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Musienko Maxim */

public class JavaWebAppAmazonS3RunTest extends BaseTest {

    private static final String PROJECT = JavaWebAppAmazonS3RunTest.class.getSimpleName();

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
    public void javaWebAppAmazonS3RunTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaWebApplicationTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC
           .selectProjectTemplate("A demonstration of accessing Amazon S3 buckets and objects using the AWS Java SDK.");
//TODO REMOVED DUE TO DISABLING JREBEL
//       IDE.CREATE_PROJECT_FROM_SCRATHC.waitForJRebelCheckbox();
//       IDE.CREATE_PROJECT_FROM_SCRATHC.clickOnJRebelCheckbox();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(3, 120);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");

        switchToNonCurrentWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
        driver.findElement(By.xpath("//*[@id='start']/p[2]/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));
    }

    @Test
    public void stopJavaWebAppAmazonS3Test() throws Exception {

        wait = new WebDriverWait(driver, 60);

        driver.switchTo().window(CURRENT_WINDOW);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.STOP_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(4, 120);
        assertTrue(IDE.OUTPUT.getOutputMessage(4).contains("stopped"));

        switchToNonCurrentWindow(CURRENT_WINDOW);
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'404')]")));
    }
}
