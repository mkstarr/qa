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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;

/** @author Zaryana Dombrovskaya */
public class OpenShiftPaaS extends AbstractTestModule {

    public static LoginWindow              loginWindow;
    public static ManageApplicationsWindow manageApplicationsWindow;
    public static ApplicationManagerWindow applicationManagerWindow;

    public OpenShiftPaaS(IDE ide) {
        super(ide);
        PageFactory.initElements(driver(), loginWindow = new LoginWindow(this.IDE()));
        PageFactory.initElements(driver(), manageApplicationsWindow = new ManageApplicationsWindow(this.IDE()));
        PageFactory.initElements(driver(), applicationManagerWindow = new ApplicationManagerWindow(this.IDE()));
    }

    private String template = "OpenShift Java Web project.";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String COMMIT_MESSAGE = String.valueOf(System.currentTimeMillis());

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user ";

    private static final String PUSH_MESSAGE = "Successfully pushed to git@github.com:exoinvitemain/testRepo.git";


    public void login() throws Exception {
        loginWindow.login();
    }

    public void updateSSHKey() throws Exception {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT,
                              MenuCommands.PaaS.OpenShift.UPDATE_SSH_PUBLIC_KEY);
        IDE().LOADER.waitClosed();
    }

    public void deleteSSHKey() throws Exception {
        IDE().MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE().PREFERENCES.waitPreferencesOpen();
        IDE().PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE().LOADER.waitClosed();
        IDE().SSH.waitSshView();
        IDE().SSH.clickDeleteKeyInGridPosition("rhcloud.com");
        IDE().ASK_DIALOG.waitOpened();
        IDE().ASK_DIALOG.clickYes();
        IDE().ASK_DIALOG.waitClosed();
        IDE().SSH.waitDisappearContentInSshListGrig("rhcloud.com");
        IDE().PREFERENCES.clickOnCloseFormBtn();
    }

    public void createProject(String projectName) throws Exception {
        IDE().WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE().CREATE_PROJECT_FROM_SCRATHC.typeProjectName(projectName);
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectJavaWebApplicationTechnology();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectOpenShiftPaaS();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE().CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate(template);
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE().LOADER.waitClosed();
        IDE().CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE().LOADER.waitClosed();
        IDE().PROGRESSOR_WINDOW.waitWindowClosed(200);
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
    }


    public void checkApplicationPropertyWithProjectMenu() {

        String applicationNameFromProjectMenu = IDE().OPEN_SHIFT.applicationManagerWindow.getApplicationName();
        String applicationUrlFromProjectMenu = IDE().OPEN_SHIFT.applicationManagerWindow.getApplicationPublicUrl();
        String applicationTypeFromProjectMenu = IDE().OPEN_SHIFT.applicationManagerWindow.getApplicationType();
        String applicationStatusFromProjectMenu = IDE().OPEN_SHIFT.applicationManagerWindow.getApplicationStatus();

        IDE().OPEN_SHIFT.applicationManagerWindow.clickCloseApplicationMenu();

        System.out.println(applicationNameFromProjectMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationNameFromProjectMenu);

        System.out.println(applicationUrlFromProjectMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationUrlFromProjectMenu);

        System.out.println(applicationTypeFromProjectMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationTypeFromProjectMenu);

        System.out.println(applicationStatusFromProjectMenu);
        assertEquals("STARTED", applicationStatusFromProjectMenu);
    }

    public void checkApplicationPropertyWithPaaSMenu(String projectName) {

        IDE().OPEN_SHIFT.manageApplicationsWindow.waitProjectByName(projectName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.selectProjectByName(projectName);

        String applicationNameFromPaaSMenu = IDE().OPEN_SHIFT.manageApplicationsWindow.getApplicationName();
        String applicationUrlFromPaaSMenu = IDE().OPEN_SHIFT.manageApplicationsWindow.getApplicationPublicUrl();
        String applicationTypeFromPaaSMenu = IDE().OPEN_SHIFT.manageApplicationsWindow.getApplicationType();
        String applicationGitUrlFromPaaSMenu = IDE().OPEN_SHIFT.manageApplicationsWindow.getApplicationGitUrl();

        IDE().OPEN_SHIFT.manageApplicationsWindow.clickCloseApplicationMenu();
        IDE().OUTPUT.clickOnOutputTab();
        System.out.println(applicationNameFromPaaSMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationNameFromPaaSMenu);

        System.out.println(applicationUrlFromPaaSMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationTypeFromPaaSMenu);

        System.out.println(applicationTypeFromPaaSMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationUrlFromPaaSMenu);

        System.out.println(applicationGitUrlFromPaaSMenu);
        IDE().OUTPUT.waitForSubTextPresent(applicationGitUrlFromPaaSMenu);

    }

    public void editApplication(String projectName) throws Exception {
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer(projectName);
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("index.html");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("index.html");
        IDE().JAVAEDITOR.waitJavaEditorIsActive();
        IDE().GOTOLINE.goToLine(212);
        IDE().JAVAEDITOR.moveCursorRight(16);
        IDE().JAVAEDITOR.typeTextIntoJavaEditor("Test ");
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE().LOADER.waitClosed();
        IDE().EDITOR.closeFile("index.html");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("index.html");
        IDE().JAVAEDITOR.waitJavaEditorIsActive();
        IDE().GOTOLINE.goToLine(212);
        IDE().JAVAEDITOR.getAllTextFromJavaEditor("Test");
    }

    public void updateApplication() throws Exception {
        // Add
        IDE().MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE().GIT.waitAddToIndexForm();
        IDE().GIT.clickAddButtonInAddToIndexForm();
        IDE().LOADER.waitClosed();
        IDE().OUTPUT.waitOpened();
        IDE().OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        // Commit
        IDE().MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE().GIT.waitCommitForm();
        IDE().GIT.typeCommitMessage("update_open_shift" + COMMIT_MESSAGE);
        IDE().GIT.clickCommitButton();
        IDE().OUTPUT.waitOpened();
        IDE().OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_1);
        IDE().OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_2);
        // Push
        IDE().MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE().GIT.waitPushView();
        IDE().GIT.clickPushBtn();
        IDE().OUTPUT.waitOpened();
        IDE().OUTPUT.waitForSubTextPresent(PUSH_MESSAGE);
        IDE().PROGRESS_BAR.waitProgressBarControlClose();

    }

    public void deleteApplicationFromProjectMenu() throws Exception {
        IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.OPENSHIFT);
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.applicationManagerWindow.clickDeleteApplicationFromProject();
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.applicationManagerWindow.clickYesInDeleteDialog();
        IDE().LOADER.waitClosed();
    }

    /** add cartridge */
    public void addCartridge(String projectName, String cartridgeName) throws Exception {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT, MenuCommands.PaaS.OpenShift.APPLICATIONS);
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.manageApplicationsWindow.selectProjectByName(projectName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.waitCartridgesTable();
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickAddCartridgeButton();
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.manageApplicationsWindow.waitAddCartridgeWindow();
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickOnListOfCartridge();
        IDE().OPEN_SHIFT.manageApplicationsWindow.selectCartridge(cartridgeName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickAddCartridgeButttonInAddCartridgeWindow();
        IDE().LOADER.waitClosed();
        Thread.sleep(5000);
        IDE().OPEN_SHIFT.manageApplicationsWindow.selectProjectByName(projectName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.waitCartridgeAppearInTable(cartridgeName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickCloseApplicationMenu();
        IDE().LOADER.waitClosed();
    }

    /** delete cartridge from project */
    public void deleteCartridge(String projectName, String cartridgeName) throws Exception {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT, MenuCommands.PaaS.OpenShift.APPLICATIONS);
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.manageApplicationsWindow.selectProjectByName(projectName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.waitCartridgesTable();
        IDE().OPEN_SHIFT.manageApplicationsWindow.waitCartridgeAppearInTable(cartridgeName);
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickDeleteCartridge(cartridgeName);
        IDE().LOADER.waitClosed();
        IDE().OPEN_SHIFT.manageApplicationsWindow.clickCloseApplicationMenu();
        IDE().LOADER.waitClosed();

    }
}
