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
package com.codenvy.ide.operation.multimodule;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertTrue;

/**
 * @author Roman Iuvshin
 *
 */
public class CreateNewModuleInMultiModuleProjectTest extends MultimoduleSevices {

    private static final String       PROJECT                     = CreateNewModuleInMultiModuleProjectTest.class.getSimpleName();

    private static final CharSequence MODULES                     =
                                                                    "   <modules>\n      <module>my-lib</module>\n      <module>my-webapp</module>\n   </modules>";

    private static final String       MODULE_NAME                 = "New_module";

    private static final String       MODULES_UPDATED_FIRST_PART  =
                                                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n    <modelVersion>4.0.0</modelVersion>\n    <groupId>";

    private static final String       MODULES_UPDATED_SECOND_PART =
                                                                    "</groupId>\n    <artifactId>CreateNewModuleInMultiModuleProjectTest</artifactId>\n    <packaging>pom</packaging>\n    <version>1.0-SNAPSHOT</version>\n    <properties>\n        <maven.compiler.source>1.6</maven.compiler.source>\n        <maven.compiler.target>1.6</maven.compiler.target>\n    </properties>\n    <modules>\n        <module>my-lib</module>\n        <module>my-webapp</module>\n        <module>New_module</module>\n    </modules>\n</project>";

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void createNewModuleTest() throws Exception {
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
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains(MODULES));
        IDE.EDITOR.closeFile("pom.xml");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.CREATE_MODULE);
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(MODULE_NAME);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaLibraryTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple JAR project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(MODULES_UPDATED_FIRST_PART);
        IDE.EDITOR.waitContentIsPresent(MODULES_UPDATED_SECOND_PART);
        IDE.EDITOR.closeFile("pom.xml");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        IDE.STATUSBAR.waitDiasspearBuildStatus();
        IDE.LOADER.waitClosed();

        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='gwt-TabLayoutPanelTabs']//div[@tab-title='Build project']")).click();

        // Get success message
        IDE.OUTPUT.waitForMessageShow(1, 15);
        String buildSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));
    }
}
