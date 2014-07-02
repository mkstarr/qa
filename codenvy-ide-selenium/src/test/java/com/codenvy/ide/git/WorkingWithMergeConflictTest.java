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
public class WorkingWithMergeConflictTest extends GitServices {

    private MergeLocalBranchIntoAnotherTest testInst = new MergeLocalBranchIntoAnotherTest();

    private static final String PROJECT = "workWithMergeConf";

    private String FIRST_GIT_ST_MESS = "nothing to commit, working directory clean";

    private final String MASTER_BRANCH = "master";

    private final String TEST_BRANCH = "newbranch";

    private final String FILE_1 = "GreetingController.java";

    private final String FILE_2 = "index.jsp";

    private final String FILE_3 = "some1248.txt";

    private final String FILE_4 = "NewClass.java";

    private final String CHECK_OUT_CONFLICT_MESS =
            "error: Your local changes to the following files would be overwritten by checkout:\n" +
            "src/main/java/helloworld/GreetingController.java\n" +
            "src/main/webapp/index.jsp\n" +
            "Please, commit your changes or stash them before you can switch branches.";

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
    public void mergeBranchTest() throws Exception {
        // step 1 Open project and switch on test branch
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openNodesAndCheckIcons(PROJECT);
        testInst.switchOnTestBranch();
        // step 2 Change two files: file1 - autosave and file2 which is non-autosave. Add new file3 to project. Then add them to index.
        // Remove file4 from index. Commit all changes. Check state of the icons
        addAndChangeFiles("test branch");
        addToIndexAndCommitAll(PROJECT + " commit");
        checkIconsAfterFirsCommitInProjectExplorer();
        // step 4 check git status
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(FIRST_GIT_ST_MESS);
        // step 5 checkout to master branch ceck opened files, close files, check reopened files in Package and project explorer.
        testInst.switchOnMasterBranch();
        checkOpenedFilesInMasterBranch();
        closeAndCheckReopenedFiles();
        checkFilesInProjectExplorerInMasteBranch();
        // step 6 check git status
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);


        IDE.OUTPUT.waitForSubTextPresent(FIRST_GIT_ST_MESS);


        // step 7 Checkout to test branch, All changes made in test branch earlier should be returned.
        testInst.switchOnTestBranch();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.EDITOR.selectTab(FILE_2);
        IDE.EDITOR.waitContentIsPresent("<!--> some changes for **" + "test branch");
        IDE.EDITOR.selectTab(FILE_1);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("//some changes for" + "test branch");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_3);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FILE_4);
        // step 8 Checkout to master branch, All changes made in test branch earlier should be returned.
        testInst.switchOnMasterBranch();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.EDITOR.selectTab(FILE_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//new change from master\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        // close for save on vfs
        IDE.EDITOR.closeFile(FILE_1);
        IDE.EDITOR.selectTab(FILE_2);
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor("<!-->001001<-->");
        IDE.TOOLBAR.runCommand(MenuCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(FILE_2);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.GIT.waitAddToIndexFormDisappear();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(TEST_BRANCH);
        IDE.GIT.clickCheckOutBtn();
        IDE.OUTPUT.waitForSubTextPresent(CHECK_OUT_CONFLICT_MESS);
    }

    // --------------------------------- related methods -----------------------------------------

    /**
     * add, changed and remove from index files on step 2
     *
     * @throws Exception
     * @throws InterruptedException
     */
    private void addAndChangeFiles(String changeMess) throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_1);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//some changes for" + changeMess + "\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("<!--> some changes for **" + changeMess + "** <-->");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark(FILE_2);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        // create file 3
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE_3);
        IDE.FILE.clickCreateButton();
        IDE.FILE.waitCreateNewFileWindowIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_3);

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_4);
        removeFromIndex();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FILE_4);
    }

    /**
     * @param commitMessage
     * @throws Exception
     */
    protected void addToIndexAndCommitAll(String commitMessage) throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
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
        IDE.LOADER.waitClosed();
        checkIconsInPackageAfterFirstCommit();
    }

    /**
     * method check icons state in changed files in Project explorer on step 2
     *
     * @throws Exception
     */
    private void checkIconsAfterFirsCommitInProjectExplorer() throws Exception {
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src/main/java/helloworld" + "/" + FILE_4);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp/index.jsp");
        IDE.EXPLORER.waitElementWithGitIcon(FILE_1, Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon(FILE_2, Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon(FILE_3, Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
    }

    /**
     * remove file 4 from index in tes branch on step 3
     *
     * @throws Exception
     */
    private void removeFromIndex() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.GIT.waitRemoveIndexFormIsClosed();
    }

    /** method check icons in package explorer after first commit no step 2 */
    private void checkIconsInPackageAfterFirstCommit() {
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_1, Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_2, Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(FILE_3, Git.GitIcons.IN_REPOSITORY);
    }

    /**
     * method check after switch in master branch on step 4
     *
     * @throws Exception
     */
    private void checkOpenedFilesInMasterBranch() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FILE_3);
        IDE.EDITOR.selectTab(FILE_2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<!--> some changes for **");
        IDE.EDITOR.selectTab(FILE_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("//some changes for");
        IDE.EDITOR.waitTabNotPresent(FILE_3);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_4);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_4);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("final double pi = 3.14159265;");
    }

    /**
     * this method close all earlier opened files. Reopen them and check content on step4
     *
     * @throws Exception
     */
    private void closeAndCheckReopenedFiles() throws Exception {
        IDE.EDITOR.closeFile(FILE_1);
        IDE.EDITOR.closeFile(FILE_2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_1);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("//some changes for");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_2);
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<!--> some changes for **");
    }


    /**
     * switch to project explorer check changes files in project explorer and return to package explorer
     *
     * @throws Exception
     */
    private void checkFilesInProjectExplorerInMasteBranch() throws Exception {
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");
        IDE.EXPLORER.openItem(PROJECT + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main");
        IDE.EXPLORER.openItem(PROJECT + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld" + "/" + FILE_4);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + FILE_3);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp/index.jsp");
        IDE.EXPLORER.waitElementWithGitIcon(FILE_1, Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon(FILE_2, Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon(FILE_4, Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
    }


}
