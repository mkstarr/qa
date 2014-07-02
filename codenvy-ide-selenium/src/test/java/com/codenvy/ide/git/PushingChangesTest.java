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
package com.codenvy.ide.git;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class PushingChangesTest extends GitServices {

    private static final String PROJECT = "testRepo";

    private static final String REPO_URL = "git@github.com:exoinvitemain/testRepo.git";

    private static final String MESSAGE_FOR_CHANGE_CONTENT = String.valueOf(System.currentTimeMillis()) + "_<!--change content-->\n";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String COMMIT_MESSAGE = String.valueOf(System.currentTimeMillis());

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user ";
    // + USER_NAME;

    private static final String NEW_FILE_NAME = "newFile.css";

    private static final String PUSH_MESSAGE = "Successfully pushed to git@github.com:exoinvitemain/testRepo.git";

    private static final String PUSH_MESSAGE_2 = "Everything up-to-date";

    private static final String PROJECT_NAME = "testPush";
//Everything up-to-date

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(PROJECT_NAME);
    }

    @Test
    public void cloneRemotePublicPersonalRepository() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        // Clone repo from remote repository with Read+Write access.

        // Clone test repository again with specific remote name.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.LOADER.waitClosed();

        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.clickOnCancelButton();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();


        // create new file, add it to index, commit and push it
        createNewFileAndPushItToGithub(NEW_FILE_NAME, PROJECT, ADDED_TO_INDEX_MESSAGE, COMMIT_MESSAGE, COMMIT_OUTPUT_MESSAGE_1,
                                       COMMIT_OUTPUT_MESSAGE_2, PUSH_MESSAGE);

        // Change autosave file1, non-autosave file2, add they to index.
        openSrcFoldersInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("index.jsp");
        // change content in java file
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("PushingChangesTest.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//" + MESSAGE_FOR_CHANGE_CONTENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("PushingChangesTest.java");
        // add to index
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        // checking git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("PushingChangesTest.java", Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.CHANGED);


        // Remove file3 from index.
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(NEW_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(NEW_FILE_NAME);

        // Commit changes into master branch with specific comment.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit_for_test_" + COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_1);
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_2);
        // check git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("PushingChangesTest.java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);

        // Open "Remote > Push" window and push changes to "refs/heads/master" branch of test remote repository.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUSH_MESSAGE);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        // Call Push again.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.LOADER.waitClosed();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUSH_MESSAGE_2);

        // check pushed changes
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT_NAME);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_NAME);
        IDE.LOADER.waitClosed();

        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.clickOnCancelButton();

        // View and check git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView("commit_for_test_" + COMMIT_MESSAGE);
        IDE.GIT.selectCommitInHistory("commit_for_test_" + COMMIT_MESSAGE);
        // check that hash Rev.A, Date, Diff same as in original repo
        assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().contains(MESSAGE_FOR_CHANGE_CONTENT));
        assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().contains("//" + MESSAGE_FOR_CHANGE_CONTENT));
    }

    /** @param currentWin */
    private void switchToGithubLoginWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {

            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    private void openSrcFoldersInPackageExplorer() {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("index.jsp");
    }

    private void openPackageInPackageExplorer() {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("PushingChangesTest.java");
    }
}
