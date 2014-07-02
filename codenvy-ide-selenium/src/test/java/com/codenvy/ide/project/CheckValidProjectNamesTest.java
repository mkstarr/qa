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
package com.codenvy.ide.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

/**
 @author Roman Iuvshin
 *
 */
public class CheckValidProjectNamesTest extends BaseTest {

    private static final String PROJECT = CheckValidProjectNamesTest.class.getSimpleName();

    private static String old_name = PROJECT;

    private static final String INCORRECT_NAME =
            "Project name must contain only Latin letters, digits or these following special characters -._";

    private static final String INCORRECT_NAME_WITH_ =
            "Project name cannot start with character _";

    private static String REPO_URL;

    static {
        if(BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            REPO_URL =  "git@github.com:exoinvitemain/phpTestRepo.git";
        } else {
            REPO_URL =  "git@github.com:codenvymain/phpTestRepo.git";
        }
    }

    private static List<String> names =
            Arrays.asList("_test", "_Test123", "_12323Test", "_.-dawtta", "!test", "@test", "#test", "$test", "%test",
                          "^test", "&test", "*test", "(test", ")test", "+test", "=test", "/test",
                          "|test", "\"test", "<test", ">test", ",test", "]test", "[test", "}test",
                          "{test", "`test", "~test", " test", "test s", "s test", "test;", "test:",
                          "test'", "проэкт", "ἱερογλύφος", "!@#$%^&*())_+_", "test123",
                          "test.123", "test-123", "test_123", "test_123.test-1245",
                          "test-test", "test_test", "test.test", "Test-tEst_teSt.tesT123",
                          ".Test", "._Test", "._-Test", ".-_._---.---.___.-.-.-");

    private static List<String> names_with_ = Arrays.asList("_test", "_Test123", "_12323Test", "_.-dawtta");

    private static List<String> not_valid_names = Arrays.asList("!test", "@test", "#test", "$test", "%test",
                                                                "^test", "&test", "*test", "(test", ")test", "+test", "=test", "/test",
                                                                "|test", "\"test", "<test", ">test", ",test", "]test", "[test", "}test",
                                                                "{test", "`test", "~test", " test", "test s", "s test", "test;", "test:",
                                                                "test'", "проэкт", "ἱερογλύφος", "!@#$%^&*())_+_");

    private List<String> valid_names =
            Arrays.asList("test123", "test.123", "test-123", "test_123", "test_123.test-1245", "test-test",
                          "test_test", "test.test", "Test-tEst_teSt.tesT123", ".Test",
                          "._Test", "._-Test", ".-_._---.---.___.-.-.-");

    @AfterClass
    public static void TearDown() {
        try {
            for (String name : names) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+name);
                VirtualFileSystemUtils.delete(name);
            }
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @Test
    public void checkValidProjectNamesInGetStartedWizzard() throws Exception {
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();

        // checking names with "_"
        for (String name : names_with_) {
            checkNotValidNamesInGetStartedWizzard(name, INCORRECT_NAME_WITH_);
        }

        // checking not valid names
        for (String name : not_valid_names) {
            checkNotValidNamesInGetStartedWizzard(name, INCORRECT_NAME);
        }

        // checking valid names
        for (String name : valid_names) {
            checkValidNamesInGetStartedWizzard(name);
        }
    }

    @Test
    public void checkValidProjectNamesInCreateNewProjectWizzard() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();

        // checking names with "_"
        for (String name : names_with_) {
            checkNotValidNamesInCreateProjectWizzard(name, INCORRECT_NAME_WITH_);
        }

        // checking not valid names
        for (String name : not_valid_names) {
            checkNotValidNamesInCreateProjectWizzard(name, INCORRECT_NAME);
        }

        // checking valid names
        for (String name : valid_names) {
            checkValidNamesInCreateProjectWizzard(name);
        }
    }

    @Test
    public void checkValidProjectNamesInRename() throws Exception {
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectPHPTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple PHP project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);

        // checking names with "_"
        for (String name : names_with_) {
            checkNotValidNamesInRename(name, INCORRECT_NAME_WITH_);
        }

        // checking not valid names
        for (String name : not_valid_names) {
            checkNotValidNamesInRename(name, INCORRECT_NAME);
        }
        IDE.RENAME.clickCancelButton();

        // checking valid names
        for (String name : valid_names) {
            checkValidNamesInRename(name);
        }
        IDE.EXPLORER.selectItem(".-_._---.---.___.-.-.-");
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitItemNotPresent(".-_._---.---.___.-.-.-");
    }

    @Test
    public void checkValidProjectNamesInCloneForm() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();

        // checking names with "_"
        for (String name : names_with_) {
            checkNotValidNamesInCloneForm(name, INCORRECT_NAME_WITH_);
        }

        // checking not valid names
        for (String name : not_valid_names) {
            checkNotValidNamesInCloneForm(name, INCORRECT_NAME);
        }
        IDE.GIT.clickCancelButtonOnCloneRemoteRepositoryForm();

        // checking valid names
        for (String name : valid_names) {
            checkValidNamesInCloneForm(name);
        }
    }

    @Test
    public void checkValidProjectNamesInImportForm() throws Exception {
        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormOpened();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.selectProjectByName("phpTestRepo");

        // checking names with "_"
        for (String name : names_with_) {
            checkNotValidNamesInImportForm(name, INCORRECT_NAME_WITH_);
        }

        // checking not valid names
        for (String name : not_valid_names) {
            checkNotValidNamesInImportForm(name, INCORRECT_NAME);
        }
        IDE.IMPORT_FROM_GITHUB.cancelBtnClick();

        // checking valid names
        for (String name : valid_names) {
            checkValidNamesInImportForm(name);
        }
    }

    private void checkNotValidNamesInImportForm(String prjName, String warningMessage) throws Exception {
        IDE.IMPORT_FROM_GITHUB.typeNameOfTheProject(prjName);
        IDE.IMPORT_FROM_GITHUB.finishBtnClick();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage(warningMessage);
        IDE.INFORMATION_DIALOG.clickOk();
    }

    private void checkValidNamesInImportForm(String prjName) throws Exception {
        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormOpened();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.selectProjectByName("phpTestRepo");
        IDE.IMPORT_FROM_GITHUB.typeNameOfTheProject(prjName);
        IDE.IMPORT_FROM_GITHUB.finishBtnClick();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormClosed();
        IDE.IMPORT_FROM_GITHUB.waitCloningProgressFormClosed();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.clickClearButton();
        IDE.EXPLORER.waitItemPresent(prjName);
        IDE.EXPLORER.selectItem(prjName);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitItemNotPresent(prjName);
    }


    private void checkNotValidNamesInCloneForm(String prjName, String warningMessage) throws Exception {
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(prjName);
        IDE.GIT.justClickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage(warningMessage);
        IDE.INFORMATION_DIALOG.clickOk();
    }

    private void checkValidNamesInCloneForm(String prjName) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(prjName);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.clickClearButton();
        IDE.EXPLORER.waitForItem(prjName);
        IDE.EXPLORER.selectItem(prjName);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitItemNotPresent(prjName);
    }

    private void checkNotValidNamesInRename(String prjName, String warningMessage) throws Exception {
        IDE.RENAME.waitOpened();
        IDE.RENAME.setNewName(prjName);
        IDE.RENAME.clickRenameButton();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage(warningMessage);
        IDE.INFORMATION_DIALOG.clickOk();
    }

    private void checkValidNamesInRename(String prjName) throws Exception {
        IDE.EXPLORER.waitForItem(old_name);
        IDE.EXPLORER.selectItem(old_name);
        IDE.RENAME.rename(prjName);
        IDE.EXPLORER.waitForItem(prjName);
        old_name = prjName;
    }

    private void checkNotValidNamesInGetStartedWizzard(String prjName, String warningMessage) throws Exception {
        IDE.GET_STARTED_WIZARD.typeProjectName(prjName);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage(warningMessage);
        IDE.INFORMATION_DIALOG.clickOk();
    }

    private void checkValidNamesInGetStartedWizzard(String prjName) throws Exception {
        IDE.GET_STARTED_WIZARD.typeProjectName(prjName);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardChooseAtechnology();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.clickOnBackButton();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
    }

    private void checkNotValidNamesInCreateProjectWizzard(String prjName, String warningMessage) throws Exception {
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(prjName);
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage(warningMessage);
        IDE.INFORMATION_DIALOG.clickOk();
    }

    private void checkValidNamesInCreateProjectWizzard(String prjName) throws Exception {
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(prjName);
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.LOADER.waitClosed();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickBackButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
    }
}
