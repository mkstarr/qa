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
package com.codenvy.ide.git;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/** @author Musienko Maxim */
public class GitPullTest extends GitServices {

    private static final String PULLTEST_COMMIT = "pulltest_commit" + System.currentTimeMillis();

    private String CLONE_URI = "git@github.com:exoinvitemain/testRepo.git";

    private static final String PROJECT_1 = "first_projectPull";

    private static String PUSH_MESSAGE = "Successfully pushed to git@github.com:exoinvitemain/testRepo.git";

    private static String PULL_MESSAGE = "Successfully pulled from git@github.com:exoinvitemain/testRepo.git";

    private final static String PROJECT_2 = "second_projectPull";

    private final String AUTOSAVE_FILE = "GitPullTest.java";

    private final String NONE_AUTOSAVE_FILE = "GitpullTets.txt";

    private static final String REMOVED_FILE = "PullRemovedFile.txt";

    private static final String MESSAGE_FOR_CHANGE = "//first change " + System.currentTimeMillis();

    protected static Map<String, Link> project;

    private static final String PROJECT_NAME = "testPrjPull";

    public GitPullTest() {

        //set values for ff browser in it select
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            CLONE_URI = "git@github.com:codenvymain/testRepo.git";
            PUSH_MESSAGE = "Successfully pushed to git@github.com:codenvymain/testRepo.git";
            PULL_MESSAGE = "Successfully pulled from git@github.com:codenvymain/testRepo.git";
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
            if (IDE.ASK_DIALOG.isOpened() == true) {
                IDE.ASK_DIALOG.clickYes();
                IDE.ASK_DIALOG.waitClosed();
            }
            IDE.EXPLORER.waitOpened();
            IDE.OPEN.openProject(PROJECT_1);
            IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
            IDE.PACKAGE_EXPLORER.selectRoot();
            IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
            IDE.FILE.waitCreateNewFileWindow();
            IDE.FILE.typeNewFileName(REMOVED_FILE);
            IDE.FILE.clickCreateButton();
            IDE.EDITOR.waitActiveFile();
            IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE);
            IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
            IDE.LOADER.waitClosed();
            IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(REMOVED_FILE);
            commitForReturnFile("repare");

            IDE.OUTPUT.waitOpened();
            IDE.OUTPUT.clickClearButton();

            IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
            IDE.GIT.waitPushView();
            IDE.GIT.clickPushBtn();
            IDE.GIT.waitPushFormIsClosed();
            IDE.OUTPUT.waitForSubTextPresent(PUSH_MESSAGE);
            IDE.EDITOR.closeFile(REMOVED_FILE);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            VirtualFileSystemUtils.delete(PROJECT_1);
            VirtualFileSystemUtils.delete(PROJECT_2);
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        }
    }

    @Test
    public void pullTest() throws Exception {

        // prepare: clone 2 projects, check git icons. Add ssh keys
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(CLONE_URI);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT_2);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.GIT.waitGitCloneFormDisappear();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_2);
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.clickOnCancelButton();
        IDE.INVITE_FORM.waitInviteDevelopersClosed();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(CLONE_URI);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT_1);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.GIT.waitGitCloneFormDisappear();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_1);
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.clickOnCancelButton();
        IDE.INVITE_FORM.waitInviteDevelopersClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_1);


        // step 1 Open first project, change two files: file1 - autosave and file2 which is non-autosave,
        // then add they to index. Remove file3 from index. Commit and push it to master branch of remote repository.
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(AUTOSAVE_FILE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(AUTOSAVE_FILE);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(MESSAGE_FOR_CHANGE);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(MESSAGE_FOR_CHANGE);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(AUTOSAVE_FILE);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText(MESSAGE_FOR_CHANGE);

        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.closeFile(NONE_AUTOSAVE_FILE);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(REMOVED_FILE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(REMOVED_FILE);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(REMOVED_FILE);


        addToIndexAndCommitAll(PULLTEST_COMMIT, PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUSH_MESSAGE);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        // step 2 Open second project, open changed in first project files file1, file2 and removed file3.
        // Open "Git > Remote > Pull" window and pull changes from master remote branch of test remote repository to master local branch.
        IDE.OPEN.openProject(PROJECT_2);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(AUTOSAVE_FILE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(AUTOSAVE_FILE);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NONE_AUTOSAVE_FILE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.waitActiveFile();

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.OUTPUT.waitForSubTextPresent("Successfully pulled from git@github.com:exoinvitemain/testRepo.git");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(REMOVED_FILE);
        IDE.EDITOR.selectTab(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.waitContentIsPresent(MESSAGE_FOR_CHANGE);
        IDE.EDITOR.selectTab(AUTOSAVE_FILE);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(MESSAGE_FOR_CHANGE);


        // step 3 Call Git Pull command again.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.OUTPUT.waitForSubTextPresent("Already up-to-date.");

        // step 4 clone second project and check git log. Git diff <older-commit-SHA1> <latest-commit-SHA1>
        // clone current repo and check commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String gitUrl = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        // closing project and cloning repo
        IDE.EDITOR.closeFile(AUTOSAVE_FILE);
        IDE.EDITOR.closeFile(NONE_AUTOSAVE_FILE);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(gitUrl);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT_NAME);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(gitUrl + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_NAME);
        IDE.LOADER.waitClosed();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitDiffContainerContainText("-//first change ");
        IDE.GIT.waitDiffContainerContainText("+" + MESSAGE_FOR_CHANGE);
    }

    protected void addToIndexAndCommitAll(String commitMessage, String Project) throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit" + commitMessage);
        IDE.GIT.clickCommitButton();

        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);
    }

    /**
     * return just deleted file from index to git repository. This
     *
     * @param commitMessage
     * @throws Exception
     */
    protected static void commitForReturnFile(String commitMessage) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.GIT.waitAddToIndexFormDisappear();
        IDE.PACKAGE_EXPLORER.selectRoot();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit" + commitMessage);
        IDE.GIT.clickCommitButton();
        IDE.GIT.waitCommitFormDisappear();

        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
    }
}
