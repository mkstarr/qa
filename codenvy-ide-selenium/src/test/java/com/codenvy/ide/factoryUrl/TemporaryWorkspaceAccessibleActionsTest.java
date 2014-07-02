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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output;
import com.codenvy.ide.git.GitServices;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Roman Iuvshin */
public class TemporaryWorkspaceAccessibleActionsTest extends GitServices {

    private static final String PROJECT = "accesibleAct";

    private static final String TEST_STRING = "Spring3";

    private static Map<String, Link> project;

    private static final String DEPENDENCY =
            "\n<dependency>\n  <groupId>cloud-ide</groupId>\n  <artifactId>spring-demo</artifactId>\n"
            + "  <version>1.0</version>\n  <type>war</type>\n</dependency>";

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
    }

    @Test
    public void createTemporaryWorkspaceTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();
        driver.get(factoryURL);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
    }

    @Test
    public void projectCreationFeaturesAreEnabledTest() throws Exception {
        IDE.OUTPUT.clickClearButton();
        IDE.FACTORY_URL.waitWelcomePanelTitle("Welcome");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.CREATE_PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickCancelButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratchClosed();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.WELCOME);
        IDE.WELCOME_PAGE.waitCloneGitRepositoryBtn();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickCancelButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratchClosed();
    }

    @Test
    public void buildFeaturesAreEnabledTest() throws Exception {
        // build check
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.BUILD.waitBuilderMessage(Build.Messages.BUILDING_PROJECT);
        // Wait until building is finished.
        IDE.STATUSBAR.waitDiasspearBuildStatus();
        IDE.LOADER.waitClosed();
        // check clear output button
        IDE.BUILD.clickClearButton();
        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='gwt-TabLayoutPanelTabs']//div[@tab-title='Build project']")).click();
        // Get success message
        IDE.OUTPUT.waitForSubTextPresent(Output.Messages.BUILD_SUCCESS);
        IDE.OUTPUT.clickClearButton();

        // build and publish check
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
        IDE.OUTPUT.waitForSubTextPresent(Output.Messages.BUILD_SUCCESS);

        IDE.OUTPUT.waitForSubTextPresent(Build.Messages.URL_TO_ARTIFACT);
        IDE.OUTPUT.waitForSubTextPresent(Build.Messages.DEPENDENCY_MESS);
        IDE.OUTPUT.waitForSubTextPresent(DEPENDENCY);
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void runFeaturesAreEnabledTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForSubTextPresent("run");
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.STOP_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForSubTextPresent("stopped");
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void debugFeaturesAreEnabledTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.DEBUG_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.waitDisconnectBtnIsEnabled(true);
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.STOP_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForSubTextPresent("stopped");
        IDE.OUTPUT.clickClearButton();
    }

    @Test
    public void editionOfFilesIsEnabledTest() throws Exception {
        // editing of non auto-save file
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("</project>");
        IDE.GOTOLINE.goToLine(35);
        IDE.EDITOR.typeTextIntoEditor("<!-- " + TEST_STRING + "-->\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("pom.xml");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(TEST_STRING);
        IDE.EDITOR.closeFile("pom.xml");
        // editing of auto-save file
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.HOME.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//" + TEST_STRING + "\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(TEST_STRING);
        IDE.EDITOR.closeFile("GreetingController.java");
    }

    @Test
    public void versioningFeaturesAreEnabledTest() throws Exception {
        checkMenuStateWhenRepositoryNotInited();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.GIT.waitInitializeLocalRepositoryForm();
        IDE.GIT.typeInitializationRepositoryName(PROJECT);
        IDE.GIT.clickOkInitializeLocalRepository();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Repository was successfully initialized.");
        checkMenuStateWhenRepositoryInitedInTempWs();
    }

    @Test
    public void deployFeaturesAreEnabledTest() throws Exception {
        IDE.MENU
           .waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.CREATE_APPLICATION);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                                       MenuCommands.PaaS.ElasticBeanstalk.CREATE_APPLICATION);
    }

    @Test
    public void projectPropertiesCanBeEditedTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        IDE.PROPERTIES.selectRowInProjectPreperties(0);
        IDE.PROPERTIES.clickOnEditBtn();
        IDE.PROPERTIES.waitEditDialogFormIsOpen();
        IDE.PROPERTIES.clickEditFormCancelBtn();
        IDE.PROPERTIES.waitEditDialogFormIsClosed();
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
    }

    @Test
    public void projectCanBeDeletedTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItemInProjectListNotPresent(PROJECT);
    }
}
