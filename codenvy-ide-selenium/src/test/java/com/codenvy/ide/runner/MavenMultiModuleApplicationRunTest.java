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
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output.Messages;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 @author Roman Iuvshin
 *
 */
public class MavenMultiModuleApplicationRunTest extends BaseTest {

    private static final String PROJECT = MavenMultiModuleApplicationRunTest.class.getSimpleName();

    private static String CURRENT_WINDOW = null;

    private static final String HOST = IDE_HOST.split("\\.")[0];

    private static final String DEPENDENCY = "<dependency>\n" +
                                             "  <groupId>com." + HOST.replace(".com", "") + ".workspace" + "\n" + "</groupId>\n" +
                                             "  <artifactId>" + PROJECT + "</artifactId>\n" +
                                             "  <version>1.0-SNAPSHOT</version>\n" +
                                             "  <type>pom</type>\n" +
                                             "</dependency>";

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
    public void mavenMultiModuleApplicationRunTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();

        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectMavenMultiModuleTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Maven Multi Module Project");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // for run module firstly need to build and publish project
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.OUTPUT.waitForMessageShow(1, 60);
        IDE.BUILD.selectBuilderOutputTab();
        IDE.LOADER.waitClosed();
        IDE.BUILD.waitBuilderMessage("Building project " + PROJECT + "\nFinished building project " + PROJECT
                                     + ".\nResult: Successful");

        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='tabTitleCloseButton' and @tab-title='Build project']")).click();

        //cheking output tab
        IDE.OUTPUT.waitForMessageShow(1, 60);
        String buildAndPublishSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildAndPublishSuccessMessage.endsWith(Messages.BUILD_SUCCESS));
        String urlToArtifact = IDE.OUTPUT.getOutputMessage(2);
        assertTrue(urlToArtifact.contains(Build.Messages.URL_TO_ARTIFACT));
        String dependency_mess = IDE.OUTPUT.getOutputMessage(3);

        assertTrue(dependency_mess.contains(Build.Messages.DEPENDENCY_MESS));
        for (String messStr : DEPENDENCY.split("\n")) {
            assertTrue(dependency_mess.contains(messStr));
        }


        // select runable module and run application
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-webapp");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("my-webapp");
        // clear output
        IDE.OUTPUT.clickClearButton();

        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(3, 120);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");

        // switching to application window
        switchToNonCurrentWindow(CURRENT_WINDOW);

        // checking application
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));

        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("test");
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Hello, test!']")));
    }

    @Test
    public void stopMavenMultiModuleApplicationTest() throws Exception {
        // stopping application and check that it stopped
        wait = new WebDriverWait(driver, 60);

        driver.switchTo().window(CURRENT_WINDOW);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.STOP_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(4, 120);
        assertTrue(IDE.OUTPUT.getOutputMessage(4).contains("stopped"));

        switchToNonCurrentWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("test");
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'404')]")));
    }
}