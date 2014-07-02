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

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CommitFilesTest extends BaseTest {

    private static final String PROJECT = CommitFilesTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String MESSAGE_FOR_CHANGE_CONTENT = "<!--change content-->";

    private static final String NEW_FILE_NAME = "newFile.css";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String COMMIT_MESSAGE = "message for commit";

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user";

    private static final String NOTHING_TO_COMMIT_MESSAGE = "# On branch master\n" +
                                                            "nothing to commit, working directory clean";

    private static final String DIFF_MESSAGE = "diff --git a/newFile.css b/newFile.css\n" +
                                               "new file mode 100644\n" +
                                               "index 0000000..e69de29\n" +
                                               "diff --git a/src/main/java/helloworld/GreetingController.java " +
                                               "b/src/main/java/helloworld/GreetingController.java\n" +
                                               "index 5603d52..d3eed52 100644\n" +
                                               "--- a/src/main/java/helloworld/GreetingController.java\n" +
                                               "+++ b/src/main/java/helloworld/GreetingController.java\n" +
                                               "@@ -11,7 +11,7 @@ import javax.servlet.http.HttpServletResponse;\n" +
                                               " \n" +
                                               " public class GreetingController implements Controller\n" +
                                               " {\n" +
                                               "-\n" +
                                               "+//<!--change content-->\n" +
                                               "    @Override\n" +
                                               "    public ModelAndView handleRequest(HttpServletRequest request, " +
                                               "HttpServletResponse response) throws Exception\n" +
                                               "    {\n" +
                                               "diff --git a/src/main/webapp/index.jsp b/src/main/webapp/index.jsp\n" +
                                               "index e504a65..1c41fba 100644\n" +
                                               "--- a/src/main/webapp/index.jsp\n" +
                                               "+++ b/src/main/webapp/index.jsp\n" +
                                               "@@ -1,4 +1,4 @@\n" +
                                               "-\n" +
                                               "+<!--change content-->\n" +
                                               " <%\n" +
                                               "    response.sendRedirect(\"spring/hello\");\n" +
                                               " %>\n" +
                                               "\\ No newline at end of file";

    private static final String PROJECT_NAME = "repoToCheck";

    @BeforeClass
    public static void before() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(PROJECT_NAME);
    }

    @Test
    public void commitFilesTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // changing few files and adding new one file
        openSrcFoldersInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("index.jsp");
        // change content in java file
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(14);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//" + MESSAGE_FOR_CHANGE_CONTENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
        // create new file
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.closeFile(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        // check git icons of created and modified files in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);

        // Select root directory of project. and call "Git > Add...".
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);

        // Call  "Git > Commit" and enter a log message for current commit.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_1);
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_2);
        // check git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.IN_REPOSITORY);
        // check git icons in project explorer
        IDE.EXPLORER.selectProjectTab(PROJECT);
        openFoldersInProjectExplorer();
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.IN_REPOSITORY);
        // back to package explorer
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();

        // Check git status.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(NOTHING_TO_COMMIT_MESSAGE);

        // Commit files again.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(NOTHING_TO_COMMIT_MESSAGE);

        // View git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE);
        IDE.GIT.selectCommitInHistory(COMMIT_MESSAGE);
        // check that hash Rev.A same as in output
        String outputMess = IDE.OUTPUT.getAllMessagesFromOutput();
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        // check that commit date same as in output
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        // check diff message
        assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().contains(DIFF_MESSAGE));
        IDE.GIT.clickCloseShowHistoryForm();

        // clone current repo and check commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String gitUrl = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        // closing project and cloning repo
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
        // View  and check git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE);
        IDE.GIT.selectCommitInHistory(COMMIT_MESSAGE);
        // check that hash Rev.A, Date, Diff same as in original repo
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().contains(DIFF_MESSAGE));
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
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
    }

    private void openFoldersInProjectExplorer() throws Exception {
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");
        IDE.EXPLORER.openItem(PROJECT + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main");
        IDE.EXPLORER.openItem(PROJECT + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp/index.jsp");
    }

}