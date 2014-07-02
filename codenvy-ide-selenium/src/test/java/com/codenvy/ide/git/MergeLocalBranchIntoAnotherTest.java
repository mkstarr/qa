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

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertTrue;


/** @author Musienko Maxim */
public class MergeLocalBranchIntoAnotherTest extends GitServices {
    private static final String PROJECT = "mergeLocBranch";

    private final String MASTER_BRANCH = "master";

    private final String TEST_BRANCH = "newbranch";

    private final String MERGE_DIFF_MASSAGE =
            "diff --git a/src/main/java/helloworld/GreetingController.java b/src/main/java/helloworld/GreetingController.java\nindex "
            +
            "5603d52..e4cc4d0 100644\n--- a/src/main/java/helloworld/GreetingController.java\n+++ "
            +
            "b/src/main/java/helloworld/GreetingController.java\n@@ -1,4 +1,5 @@\n package helloworld;\n+//some changes\n \n import org"
            +
            ".springframework.web.servlet.ModelAndView;\n import org.springframework.web.servlet.mvc.Controller;";

    private String MASTER_COMMIT_DIFF_MESSAGE =
            "diff --git a/src/main/java/helloworld/GreetingController.java b/src/main/java/helloworld/GreetingController.java\nindex "
            +
            "e4cc4d0..5603d52 100644\n--- a/src/main/java/helloworld/GreetingController.java\n+++ "
            +
            "b/src/main/java/helloworld/GreetingController.java\n@@ -1,5 +1,4 @@\n package helloworld;\n-//some changes\n \n import org"
            +
            ".springframework.web.servlet.ModelAndView;\n import org.springframework.web.servlet.mvc.Controller;\ndiff --git "
            +
            "a/src/main/webapp/index.jsp b/src/main/webapp/index.jsp\nindex e504a65..fad2909 100644\n--- a/src/main/webapp/index.jsp\n+++"
            +
            " b/src/main/webapp/index.jsp\n@@ -1,4 +1,4 @@\n-\n+<!-->***<-->\n <%\n    response.sendRedirect(\"spring/hello\");\n %>\n\\ "
            +
            "No newline at end of file";

    private              String TEST_BRANCH_COMMIT_DIFF_MESSAGE =
            "diff --git a/src/main/java/helloworld/GreetingController.java b/src/main/java/helloworld/GreetingController.java\nindex "
            +
            "5603d52..e4cc4d0 100644\n--- a/src/main/java/helloworld/GreetingController.java\n+++ "
            +
            "b/src/main/java/helloworld/GreetingController.java\n@@ -1,4 +1,5 @@\n package helloworld;\n+//some changes\n \n import org"
            +
            ".springframework.web.servlet.ModelAndView;\n import org.springframework.web.servlet.mvc.Controller;";
    private static final String CLONED_PRJ                      = "step5";

    private String CONSOLE_DIFF_FRAGMENT_1 =
            "--- a/src/main/java/helloworld/GreetingController.java\n+++ b/src/main/java/helloworld/GreetingController.java\n";
    private String CONSOLE_DIFF_FRAGMENT_2 =
            "package helloworld;\n+//some changes\n \n import org.springframework.web.servlet.ModelAndView;\n import org.springframework" +
            ".web.servlet.mvc.Controller";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/git/gitSampleProjectTwoBranches.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(CLONED_PRJ);
    }

    @Test
    public void mergeBranchTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openNodesAndCheckIcons(PROJECT);

        // 1 Edit and save any file of test project then add it to index and commit.
        switchOnTestBranch();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//some changes" + "\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
        addToIndexAndCommit("GreetingController.java", "testCommit");
        // 2 Checkout to master branch and edit other file then save, add it to index and commit.
        switchOnMasterBranch();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("index.jsp");
        IDE.OUTPUT.clickClearButton();
        IDE.OUTPUT.waitOutputCleaned();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("<!-->***<-->");

        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark("index.jsp");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("index.jsp");
        IDE.EDITOR.closeFile("index.jsp");
        addToIndexAndCommit("index.jsp", "masterCommit");
        switchOnTestBranch();


        // 3 Open "Git > Merge..." window, select test local branch and merge it with master branch.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeExpandLocBranchIcon();
        IDE.GIT.clickOnExpandLocalBranchIcon();
        IDE.GIT.waitItemInMergeList("master");
        IDE.GIT.selectItemInMegeForm("master");
        IDE.GIT.clickMergeBtn();

        checkOutputMessagesAfterFirstMrege();
        checkIconsInPackageExplorerAfterFirstMerge();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.selectProjectTab(PROJECT);
        checkIconsInExplorerAfterFirstMerge();
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);

        // 4 View Git History.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView("Merge branch 'master' into newbranch");
        IDE.GIT.waitCommitInHistoryView("commitmasterCommit");
        IDE.GIT.waitCommitInHistoryView("committestCommit");

        IDE.GIT.waitDiffContainerEqualsText(MERGE_DIFF_MASSAGE);
        IDE.GIT.selectCommitInHistory("commitmasterCommit");
        IDE.GIT.waitDiffContainerEqualsText(MASTER_COMMIT_DIFF_MESSAGE);

        IDE.GIT.selectCommitInHistory("committestCommit");
        IDE.GIT.waitDiffContainerEqualsText(TEST_BRANCH_COMMIT_DIFF_MESSAGE);

        // step5
        String cloneUrl = getUrlForClone();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(cloneUrl);
        IDE.GIT.typeProjectNameInCloneRepositoryForm("step5");
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.GIT.waitGitCloneFormDisappear();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(CLONED_PRJ);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView("Merge branch 'master' into newbranch");
        IDE.LOADER.waitClosed();
        assertTrue(IDE.GIT.getCommitDateFromHistoryPanel().contains(getCurrentDate()));
        assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().length() > 30);

        // step 6
        IDE.GIT.waitDiffContainerContainText(CONSOLE_DIFF_FRAGMENT_1);
        IDE.GIT.waitDiffContainerContainText(CONSOLE_DIFF_FRAGMENT_2);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        // step 7
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeExpandLocBranchIcon();
        IDE.GIT.clickOnExpandLocalBranchIcon();
        IDE.GIT.waitItemInMergeList("master");
        IDE.GIT.selectItemInMegeForm("master");
        IDE.GIT.clickMergeBtn();
        IDE.GIT.waitMergeViewIsClosed();
        IDE.OUTPUT.waitForSubTextPresent("Already up-to-date");

        // step 8
        openNodesAndCheckIcons(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//second changes" + "\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
        addToIndexAndCommit("GreetingController.java", "commit2");
        IDE.OUTPUT.clickClearButton();
        IDE.OUTPUT.waitOutputCleaned();

        // step 9
        switchOnMasterBranch();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeExpandLocBranchIcon();
        IDE.GIT.clickOnExpandLocalBranchIcon();
        IDE.GIT.waitItemInMergeList("newbranch");
        IDE.GIT.selectItemInMegeForm("newbranch");
        IDE.GIT.clickMergeBtn();
        IDE.GIT.waitMergeViewIsClosed();
        checkOutputMessagesAfterLastMerge();

    }

    /**
     * return url for clone on step 5
     *
     * @throws Exception
     * @throws InterruptedException
     */
    private String getUrlForClone() throws Exception, InterruptedException {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String url = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        IDE.GIT.waitGitUrlFormDisappear();
        return url;
    }

    /** Make all operation for select of the test branch */
    protected void switchOnTestBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(TEST_BRANCH);
        IDE.GIT.clickCheckOutBtn();
        IDE.LOADER.waitClosed();
        IDE.GIT.waitBranchIsCheckout(TEST_BRANCH);
        IDE.GIT.clickCloseBranchBtn();
        IDE.GIT.waitBranhesIsClosed();
    }

    /** Make all operation for select of the master branch */
    protected void switchOnMasterBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(MASTER_BRANCH);
        IDE.GIT.clickCheckOutBtn();
        IDE.LOADER.waitClosed();
        IDE.GIT.waitBranchIsCheckout(MASTER_BRANCH);
        IDE.LOADER.waitClosed();
        IDE.GIT.clickCloseBranchBtn();
        IDE.GIT.waitBranhesIsClosed();
    }

    /**
     * add to index and commit selected file with specified message
     *
     * @param workFile
     * @param commitMessage
     * @throws Exception
     */
    protected void addToIndexAndCommit(String workFile, String commitMessage) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.GIT.waitAddToIndexFormDisappear();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit" + commitMessage);
        IDE.GIT.clickCommitButton();
        IDE.GIT.waitCommitFormDisappear();

        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.LOADER.waitClosed();
    }


    /**
     *
     */
    private void checkIconsInPackageExplorerAfterFirstMerge() {
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);

        // check that nested nodes also has git icons
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("WEB-INF", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("NewClass.java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
    }


    /**
     *
     */
    private void checkIconsInExplorerAfterFirstMerge() {
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("NewClass.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);

    }


    /**
     * get current year, day and hour for check in commits
     *
     * @return
     */
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy MMM d ", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }


    /** @throws Exception */
    private void checkOutputMessagesAfterFirstMrege() throws Exception {
        IDE.OUTPUT.waitForSubTextPresent("Merged");
        IDE.OUTPUT.waitForSubTextPresent("Merged commits:");
        IDE.OUTPUT.waitForSubTextPresent("New HEAD commit:");
        assertTrue(IDE.OUTPUT.getOutputMessage(3).length() == 166);
    }

    /** @throws Exception */
    private void checkOutputMessagesAfterLastMerge() throws Exception {
        IDE.OUTPUT.waitForSubTextPresent("Fast-forward");
        IDE.OUTPUT.waitForSubTextPresent("Merged commits:");
        IDE.OUTPUT.waitForSubTextPresent("New HEAD commit:");
        assertTrue(IDE.OUTPUT.getOutputMessage(1).length() == 172);
    }
}
