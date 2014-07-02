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
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/** @author Musienko Maxim */
public class PushChangeNonUpdatedRepoTest extends GitServices {

    private String cloneUri = "git@github.com:exoinvitemain/testRepo.git";

    private String successPushedMess = "Successfully pushed to git@github.com:exoinvitemain/testRepo.git";

    private String successPulledMess = "Successfully pulled from git@github.com:exoinvitemain/testRepo.git";

    private static final String PROJECT_1 = "first_project";

    private final static String PROJECT_2 = "second_project4";

    private final String FILE_FOR_CHANGED = "README.md";

    private final String FILE_FOR_CHANGED_2 = "PushChangeNonUpdatedRepoTest.txt";

    private String pushConflictMessage = " ! [rejected]        master -> master (fetch first)\n" +
                                         "error: failed to push some refs to 'git@github.com:exoinvitemain/testRepo.git'";


    private static final String MESSAGE_FOR_CHANGE = "//some change_" + System.currentTimeMillis();

    protected static Map<String, Link> project;

    public PushChangeNonUpdatedRepoTest() {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            successPushedMess = "Successfully pushed to git@github.com:codenvymain/testRepo.git";
            successPulledMess = "uccessfully pulled from git@github.com:codenvymain/testRepo.git";
            cloneUri = "git@github.com:codenvymain/testRepo.git";
            pushConflictMessage = pushConflictMessage.replaceAll("exoinvitemain", "codenvymain");
        }
    }


    @AfterClass
    public static void tearDown() throws Exception {

        VirtualFileSystemUtils.delete(PROJECT_1);
        VirtualFileSystemUtils.delete(PROJECT_2);
    }

    @Test
    public void pushNoneUpdateTest() throws Exception {
        // step 0 preconditions clone 2 repositories in 2 projects, add ssh keys for remote operations
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();

        cloneProject(PROJECT_1, cloneUri);
        openNodesAndCheckIcons(PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        cloneProject(PROJECT_2, cloneUri);
        openNodesAndCheckIcons(PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        // step 1 open project change some file commit and push
        IDE.OPEN.openProject(PROJECT_1);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGED);
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText(MESSAGE_FOR_CHANGE);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark(FILE_FOR_CHANGED);
        IDE.EDITOR.forcedClosureFile(FILE_FOR_CHANGED);
        addToIndexAndCommitAll("commit-to-first-project", PROJECT_1, FILE_FOR_CHANGED);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitForSubTextPresent(successPushedMess);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        // step 2 open second project change another file, add to index, commit, push
        // check conflict, pull
        IDE.OPEN.openProject(PROJECT_2);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_FOR_CHANGED_2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGED_2);

        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText(MESSAGE_FOR_CHANGE);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark(FILE_FOR_CHANGED_2);
        IDE.EDITOR.forcedClosureFile(FILE_FOR_CHANGED_2);
        addToIndexAndCommitAll("commit-to-second-project", PROJECT_2, FILE_FOR_CHANGED_2);

        // step 3
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();

        IDE.OUTPUT.waitForSubTextPresent(pushConflictMessage);

        // step 4, valid pull
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.GIT.waitGitPullFormDisappear();
        IDE.OUTPUT.waitForSubTextPresent(successPulledMess);
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.clickClearButton();

        // step 5
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitForSubTextPresent(successPushedMess);
    }

    protected void addToIndexAndCommitAll(String commitMessage, String Project, String changedFile) throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.GIT.waitAddToIndexFormDisappear();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit" + commitMessage);
        IDE.GIT.clickCommitButton();
        IDE.GIT.waitCommitFormDisappear();

        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
    }

    @Override
    public void openNodesAndCheckIcons(String project) {
        // checking icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(project, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);

        // check that nested nodes also has git icons
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("WEB-INF", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("commenttest", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_FOR_CHANGED, Git.GitIcons.IN_REPOSITORY);
    }

}
