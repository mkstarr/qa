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
import java.util.Map;

/** @author Musienko Maxim */
public class CheckoutBranchTest extends GitServices {
    private static final String PROJECT = CheckoutBranchTest.class.getSimpleName();

    private final String MASTER_BRANCH = "master";

    private final String TEST_BRANCH = "newbranch";

    private final String NEW_CLASS_CONTENT =
            "package helloworld;\n\npublic class NewClass\n{\n   public double bigValuenewMTd()\n   {\n      final double pi = 3" +
            ".14159265;\n      return pi;\n   }\n}";

    private final String conflictMessage = "error: Your local changes to the following files would be overwritten by checkout:\n" +
                                           "src/main/java/helloworld/GreetingController.java\n" +
                                           "src/main/webapp/index.jsp\n" +
                                           "Please, commit your changes or stash them before you can switch branches.\n" +
                                           "Aborting";

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
    }

    @Test
    public void checkoutBranchTest() throws Exception {

        // step 1 select test branch
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        openNodesAndCheckIcons(PROJECT);
        switchOnTestBranch();

        // step 2 change 1 autosaved and non-autosave file, add new file chek status icons
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//some changes" + "\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);

        // IDE.EDITOR.closeFile("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor("<!--><-->");
        IDE.EDITOR.typeTextIntoEditor("\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("index.jsp");
        // IDE.EDITOR.closeFile("index.jsp");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // check changes in project and package explorers
        // TODO
        // IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src");
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVASCRIPT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("some.js");
        IDE.FILE.clickCreateButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("some.js");
        // IDE.EDITOR.closeFile("some.js");
        // IDE.EDITOR.waitTabNotPresent("some.js");
        // IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // TODO
        // IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("some.js", Git.GitIcons.UNTRACKED);
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.EXPLORER.waitElementWithGitIcon("some.js", Git.GitIcons.UNTRACKED);
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/webapp");
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("some.js", Git.GitIcons.UNTRACKED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("NewClass.java");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.GIT.waitRemoveIndexFormIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("NewClass.java");
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/NewClass.java");
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.OUTPUT.clickClearButton();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        addToIndexContent();
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Successfully added to index.");
        IDE.OUTPUT.clickClearButton();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("first commit");
        IDE.GIT.clickCommitButton();
        IDE.GIT.waitCommitFormDisappear();
        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
        IDE.LOADER.waitClosed();


        // step 3 check git status
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent("nothing to commit, working directory clean");
        IDE.LOADER.waitClosed();

        // step 4 Checkout in main branch and check changed files
        swtichOnMasterBranch();
        // content in opened files should be reset
        IDE.EDITOR.selectTab("index.jsp");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<!--><-->");

        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("//some changes");
        // close files
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE.EDITOR.closeFile("index.jsp");
        // reopen files and check content in reopen file
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("//some changes" + "\n");
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<!--><-->");
        IDE.EDITOR.forcedClosureFile(1);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("some.js");
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + "src" + "/some.js");
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/NewClass.java");
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/NewClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(NEW_CLASS_CONTENT);
        IDE.EXPLORER.waitElementWithoutGitIcon(PROJECT + "/" + "src" + "/some.js", Git.GitIcons.MODIFIED);
        IDE.EXPLORER.waitElementWithoutGitIcon(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/GreetingController.java",
                                               Git.GitIcons.MODIFIED);
        // step 5 check Git status
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent("nothing to commit, working directory clean");
        IDE.LOADER.waitClosed();

        // step 6 switch to test branch again and check earlier changes
        switchOnTestBranch();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/NewClass.java");
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/GreetingController.java");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("//some changes");
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/GreetingController.java");
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/webapp" + "/index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("<!--><-->");
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE.EDITOR.forcedClosureFile("index.jsp");

        // step 7 change files in master branch (this creates conflict) and check message with conflict.
        swtichOnMasterBranch();
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/java" + "/helloworld" + "/GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//changein master\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("GreetingController.java");
        IDE.EXPLORER.openItem(PROJECT + "/" + "src" + "/main" + "/webapp" + "/index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("<!-->****<-->");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("index.jsp");
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        addToIndexContent();
        checkswitchwithConflict();
        IDE.OUTPUT.waitForSubTextPresent(conflictMessage);
    }

    /** @throws Exception */
    private void switchOnTestBranch() throws Exception {
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

    /** @throws Exception */
    private void checkswitchwithConflict() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(TEST_BRANCH);
        IDE.GIT.clickCheckOutBtn();
    }


    /** @throws Exception */
    private void swtichOnMasterBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(MASTER_BRANCH);
        IDE.GIT.clickCheckOutBtn();
        IDE.LOADER.waitClosed();
        IDE.GIT.waitBranchIsCheckout(MASTER_BRANCH);
        IDE.GIT.clickCloseBranchBtn();
        IDE.GIT.waitBranhesIsClosed();
    }

    /** @throws Exception */
    private void addToIndexContent() throws Exception {
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
    }

}
