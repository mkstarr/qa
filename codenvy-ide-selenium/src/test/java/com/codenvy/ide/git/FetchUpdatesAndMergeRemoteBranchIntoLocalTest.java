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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Map;

/** @author Roman Iuvshin */
public class FetchUpdatesAndMergeRemoteBranchIntoLocalTest extends GitServices {

    protected static Map<String, Link> project;

    private static String cloneUri = "git@github.com:exoinvitemain/testRepo.git";

    private static final String PROJECT_1 = "first_projectFetch";

    private final static String PROJECT_2 = "second_projectFetch";

    private final String FILE_FOR_CHANGE = "FetchUpdatesAndMergeRemoteBranchIntoLocalTest.txt";

    private final String FILE_FOR_CHANGE_2 = "FetchUpdatesAndMergeRemoteBranchIntoLocalTest.java";

    private static final String NEW_FILE_NAME = "testFile.css";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String COMMIT_MESSAGE = String.valueOf(System.currentTimeMillis());

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user";

    private static String PUSH_MESSAGE = "Successfully pushed to " + cloneUri;

    private static final String MESSAGE_FOR_CHANGE_CONTENT = String.valueOf(System.currentTimeMillis()) + "_<!--change content-->\n";

    private static final String FETCH_MESSAGE = "Successfully fetched from " + cloneUri;

    private static final String MERGE_MESSAGE = "Fast-forward\n" +
                                                "Merged commits:\n";

    private static final String PROJECT_NAME = "testProjectFetch";


    public FetchUpdatesAndMergeRemoteBranchIntoLocalTest() {
        //set values for ff browser in it choose

        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            PUSH_MESSAGE = "Successfully pushed to " + "git@github.com:codenvymain/testRepo.git";
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            // restore file for test
            IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
            if (IDE.ASK_DIALOG.isOpened() == true) {
                IDE.ASK_DIALOG.clickYes();
                IDE.ASK_DIALOG.waitClosed();
            }
            IDE.OPEN.openProject(PROJECT_1);
            IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
            IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_1);
            // create new file, add it to index, commit and push it
            createNewFileAndPushItToGithub(NEW_FILE_NAME, PROJECT_1, ADDED_TO_INDEX_MESSAGE, COMMIT_MESSAGE, COMMIT_OUTPUT_MESSAGE_1,
                                           COMMIT_OUTPUT_MESSAGE_2, PUSH_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            VirtualFileSystemUtils.delete(PROJECT_1);
            VirtualFileSystemUtils.delete(PROJECT_2);
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        }
        IDE.GITHUB.deleteNewBranch("NewBranch");
    }


    @Test
    public void fetchUpdatesAndMergeRemoteBranchIntoLocalTest() throws Exception {
        // step 0 preconditions clone 2 repositories in 2 projects, add ssh keys for remote operations
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        cloneProject(PROJECT_1, cloneUri);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        cloneProject(PROJECT_2, cloneUri);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        // step 1 open project change some file commit an push
        IDE.OPEN.openProject(PROJECT_1);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_FOR_CHANGE);

        // Change autosave file1, non-autosave file2, add they to index.
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGE);
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile(FILE_FOR_CHANGE);
        // change content in java file
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGE_2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//" + MESSAGE_FOR_CHANGE_CONTENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(FILE_FOR_CHANGE_2);
        // add to index
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        // checking git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_FOR_CHANGE_2, Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_FOR_CHANGE, Git.GitIcons.CHANGED);


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
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_FOR_CHANGE_2, Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_FOR_CHANGE, Git.GitIcons.IN_REPOSITORY);

        // Open "Remote > Push" window and push changes to "refs/heads/master" branch of test remote repository.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUSH_MESSAGE);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // Open second project.
        // Open "Git > Remote > Fetch" window and fetch changes from master remote branch of test remote repository to master local branch.
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.OPEN.openProject(PROJECT_2);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        //add branch
        IDE.GITHUB.addNewBranchOnGithubUsingApi("NewBranch");

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.FETCH);
        IDE.GIT.waitFetchForm();
        IDE.GIT.clickFetchButtonOnFetchForm();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(FETCH_MESSAGE);


        //check added branch
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitFieldWithNewNameBranchForm("NewBranch");


        // Open changed in first project files file1, file2 and removed file3.
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_FOR_CHANGE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGE);
        IDE.EDITOR.waitActiveFile();
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_FOR_CHANGE_2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_FOR_CHANGE_2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(1);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NEW_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        // Open "Git > Merge..." window, choose remote branch master and merge.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeView();
        IDE.GIT.waitMergeExpandRemoteBranchIcon();
        IDE.GIT.clickOnExpandRemotelBranchIcon();
        IDE.GIT.waitItemInMergeList("origin/master");
        IDE.GIT.selectItemInMegeForm("origin/master");
        IDE.GIT.clickMergeBtn();
        IDE.GIT.waitMergeViewIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(MERGE_MESSAGE);
        IDE.OUTPUT.waitForSubTextPresent("New HEAD commit:");

        // check merging
        // Opened file1 and file2 should become updated content and stayed as being in repository in Project/Package explorers.
        // File3 should be closed and removed from Project/Package explorers.
        IDE.EDITOR.selectTab(FILE_FOR_CHANGE);
        IDE.EDITOR.waitContentIsPresent(MESSAGE_FOR_CHANGE_CONTENT.trim());
        IDE.EDITOR.selectTab(FILE_FOR_CHANGE_2);
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.EDITOR.waitTabNotPresent(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(NEW_FILE_NAME);

        // Merge remote branch master into HEAD branch again.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeView();
        IDE.GIT.waitMergeExpandRemoteBranchIcon();
        IDE.GIT.clickOnExpandRemotelBranchIcon();
        IDE.GIT.waitItemInMergeList("origin/master");
        IDE.GIT.selectItemInMegeForm("origin/master");
        IDE.GIT.clickMergeBtn();
        IDE.GIT.waitMergeViewIsClosed();
        IDE.OUTPUT.waitForSubTextPresent("Already up-to-date");

        // clone and check second project
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String gitUrl = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        // closing project and cloning repo
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();

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
        // check that removed file not present
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(NEW_FILE_NAME);
        // View and check git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView("commit_for_test_" + COMMIT_MESSAGE);
        IDE.GIT.selectCommitInHistory("commit_for_test_" + COMMIT_MESSAGE);
        // check that hash Rev.A, Date, Diff
        IDE.OUTPUT.waitForSubTextPresent(IDE.GIT.getCommitRevisionFromHistoryPanel());
        IDE.OUTPUT.waitForSubTextPresent(IDE.GIT.getCommitDateFromHistoryPanel());
    }

    private void openPackageInPackageExplorer() {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_FOR_CHANGE_2);
    }

}
