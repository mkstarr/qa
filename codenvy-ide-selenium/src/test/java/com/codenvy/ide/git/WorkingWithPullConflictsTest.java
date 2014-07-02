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
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;


/** @author Musienko Maxim */
public class WorkingWithPullConflictsTest extends GitServices {
    private MergeLocalBranchIntoAnotherTest testInst = new MergeLocalBranchIntoAnotherTest();

    private String cloneUri = "git@github.com:exoinvitemain/testRepo.git";

    private String pushedMess = "Successfully pushed to git@github.com:exoinvitemain/testRepo.git";

    private static final String PROJECT_1 = "first_project";


    private final static String PROJECT_2 = "second_project5";

    private final String fileForChange = "JavaCommentsTest.java";

    private final String fileForChange2 = "GitpullTets.txt";

    private final String secondMergeConflictMessage =
            "CONFLICT (content): Merge conflict in src/main/java/commenttest/JavaCommentsTest.java\n" +
            "Auto-merging GitpullTets.txt\n" +
            "CONFLICT (content): Merge conflict in GitpullTets.txt\n" +
            "Automatic merge failed; fix conflicts and then commit the result.";

    private final String firstMergeConflictMessage =
            "error: Your local changes to the following files would be overwritten by merge:\n" +
            "GitpullTets.txt\n" +
            "src/main/java/commenttest/JavaCommentsTest.java\n" +
            "Please, commit your changes or stash them before you can merge.\n" +
            "Aborting";


    private static final String CHANGE_STRING = "//first change" + System.currentTimeMillis() + "\n";


    private final String headConfPrefixConfMess = "<<<<<<< HEAD\n" +
                                                  "//second change\n" +
                                                  "//first change";

    private final String refMasterPrefixConfMess2 = "=======\n" +
                                                    CHANGE_STRING +
                                                    ">>>>>>> refs/heads/master";


    private final String jGitErrorLog = "(TypeError)";

    protected static Map<String, Link> project;


    public WorkingWithPullConflictsTest() {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            cloneUri = cloneUri.replace("exoinvitemain", "codenvymain");
            pushedMess = pushedMess.replace("exoinvitemain", "codenvymain");
        }
    }


    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT_1);
        VirtualFileSystemUtils.delete(PROJECT_2);
    }


    @Test
    public void pullConflictTest() throws Exception {

        // step 1 preconditions clone 2 repositories in 2 projects, add ssh keys for remote operations
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();

        cloneProject(PROJECT_1, cloneUri);
        openNodesAndCheckIcons(PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        cloneProject(PROJECT_2, cloneUri);
        openNodesAndCheckIcons(PROJECT_2);

        // step 2 change file in second project commit and push to remote repo
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(fileForChange);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(CHANGE_STRING);
        // for update on server side
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(fileForChange);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(fileForChange2);
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.typeTextIntoEditor(CHANGE_STRING);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(fileForChange2);


        addToIndexAndCommitAll("commit_in_first_cloned", PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitForSubTextPresent(pushedMess);

        // Open first project, change the same file and commit
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT_1);
        IDE.OPEN.openProject(PROJECT_1);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_1);
        openNodesAndCheckIcons(PROJECT_1);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(fileForChange);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//second change\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(fileForChange2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("//second change\n");
        IDE.EDITOR.waitContentIsPresent("//second change\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);

        //IDE.EDITOR.closeFile(fileForChange);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.GIT.waitGitPullFormDisappear();
        IDE.OUTPUT.waitForSubTextPresent(firstMergeConflictMessage);


        addToIndexAndCommitAll("coomit_in_first_cloned", PROJECT_1);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.GIT.waitGitPullFormDisappear();
        IDE.OUTPUT.waitForSubTextPresent(secondMergeConflictMessage);
        IDE.OUTPUT.waitForSubTextIsNotPresent(jGitErrorLog);
        IDE.EDITOR.waitTabPresent(fileForChange);
        IDE.EDITOR.waitTabPresent(fileForChange2);

        IDE.EDITOR.selectTab(fileForChange2);
        IDE.EDITOR.waitContentIsPresent(headConfPrefixConfMess);
        IDE.EDITOR.waitContentIsPresent(refMasterPrefixConfMess2);

        IDE.EDITOR.selectTab(fileForChange);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(refMasterPrefixConfMess2);

        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(fileForChange, Git.GitIcons.CONFLICT);
        IDE.EXPLORER.selectProjectTab(PROJECT_1);
        IDE.EXPLORER.waitItemPresent(PROJECT_1);
        IDE.EXPLORER.waitItemPresent(PROJECT_1 + "/src");
        IDE.EXPLORER.openItem(PROJECT_1 + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT_1 + "/src/main");
        IDE.EXPLORER.openItem(PROJECT_1 + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT_1 + "/src/main/java");
        IDE.EXPLORER.openItem(PROJECT_1 + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT_1 + "/src/main/java/commenttest");
        IDE.EXPLORER.openItem(PROJECT_1 + "/src/main/java/commenttest");
        IDE.EXPLORER.waitItemPresent(PROJECT_1 + "/src/main/java/commenttest/" + fileForChange);
        IDE.EXPLORER.waitElementWithGitIcon(fileForChange, Git.GitIcons.CONFLICT);
    }

    protected void addToIndexAndCommitAll(String commitMessage, String Project) throws Exception {
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
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(fileForChange, Git.GitIcons.IN_REPOSITORY);
    }
}
