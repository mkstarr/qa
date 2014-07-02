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
package com.codenvy.ide.builder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Roman Iuvshin */
public class MavenMultiModulesBuildTest extends BaseTest {

    private static final String PROJECT = "MMM" + System.currentTimeMillis();

    private static final String FIRST_MODULE = "my-lib";

    private static final String ERROR_MESSAGE = "Non-resolvable parent POM: Could not find artifact";

    private static final String DEPENDENCY = "<dependency>\n" +
                                             "  <groupId>com." + IDE_HOST.split("\\.")[0] + "\n" + "</groupId>\n" +
                                             "  <artifactId>" + PROJECT + "</artifactId>\n" +
                                             "  <version>1.0-SNAPSHOT</version>\n" +
                                             "  <type>pom</type>\n" +
                                             "</dependency>";

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @Test
    public void buildMavenMultiModuleProjectTest() throws Exception {
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
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        // Wait until building is finished.
        IDE.STATUSBAR.waitDiasspearBuildStatus();
        IDE.LOADER.waitClosed();
        // check clear output button
        IDE.BUILD.clickClearButton();
        String emptyMessage = IDE.BUILD.getOutputMessage();
        assertEquals("", emptyMessage);
        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='gwt-TabLayoutPanelTabs']//div[@tab-title='Build project']")).click();
        // Get success message
        IDE.OUTPUT.waitForMessageShow(1, 15);
        String buildSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void buildSingleModuleFailedTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FIRST_MODULE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FIRST_MODULE);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        // check failed build status
        IDE.STATUSBAR.waitBuildFailStatus();
        builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.contains(ERROR_MESSAGE));
        IDE.BUILD.clickClearButton();
        //check output message
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitOpened();
        // Get error message
        IDE.OUTPUT.waitForMessageShow(1, 15);
        String buildErrorMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildErrorMessage.endsWith(Output.Messages.BUILD_FAILED));
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void buildAndPublishMavenMultiModuleProjectTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(1, 60);
        IDE.BUILD.selectBuilderOutputTab();
        IDE.BUILD.waitBuilderMessage("Building project " + PROJECT + "\nFinished building project " + PROJECT
                                     + ".\nResult: Successful");
        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='tabTitleCloseButton' and @tab-title='Build project']")).click();
        //cheking output tab
        IDE.OUTPUT.waitForMessageShow(1, 60);
        String buildAndPublishSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildAndPublishSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));
        String urlToArtifact = IDE.OUTPUT.getOutputMessage(2);
        assertTrue(urlToArtifact.contains(Build.Messages.URL_TO_ARTIFACT));
        String dependency_mess = IDE.OUTPUT.getOutputMessage(3);
        assertTrue(dependency_mess.contains(Build.Messages.DEPENDENCY_MESS));
        for (String dependencyPart : DEPENDENCY.split("\n")) {
            assertTrue(dependency_mess.contains(dependencyPart));
        }
    }

    @Test
    public void buildSingleModuleSuccessTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FIRST_MODULE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FIRST_MODULE);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        // Wait until building is finished.
        IDE.STATUSBAR.waitDiasspearBuildStatus();
        IDE.LOADER.waitClosed();
        // check clear output button
        IDE.BUILD.clickClearButton();
        String emptyMessage = IDE.BUILD.getOutputMessage();
        assertEquals("", emptyMessage);
        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='gwt-TabLayoutPanelTabs']//div[@tab-title='Build project']")).click();
        // Get success message
        IDE.OUTPUT.waitForMessageShow(1, 15);
        String buildSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));
        IDE.OUTPUT.clickClearButton();
    }
}
