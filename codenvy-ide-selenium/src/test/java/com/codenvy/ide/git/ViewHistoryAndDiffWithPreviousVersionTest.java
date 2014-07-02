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
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class ViewHistoryAndDiffWithPreviousVersionTest extends BaseTest {

    private static final String PROJECT = ViewHistoryAndDiffWithPreviousVersionTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String TEXT_FILE_NAME = "test.txt";

    private static final String TEXT_FILE_CONTENT = "first";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String TEXT_FILE_CONTENT_FOR_EDIT = "second";

    private static final String COMMIT_MESSAGE = "first commit";

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user ";//+ USER_NAME;

    private static final String COMMIT_MESSAGE_2 = "second commit";

    private static final String TEXT_FILE_CONTENT_FOR_EDIT_2 = "third";

    private static final String COMMIT_MESSAGE_3 = "third commit";

    private static final String REMOVAL_MESSAGE = "[INFO] Removal was successful.";

    private static final String COMMIT_MESSAGE_4 = "remove file";

    private String diffMessage4 = "deleted file mode 100644\n" +
                                  "index \n" +
                                  "--- a/test.txt\n" +
                                  "+++ /dev/null\n" +
                                  "@@ -1,3 +0,0 @@\n" +
                                  "-\n" +
                                  "-first\n" +
                                  "-second\n" +
                                  "-third\n" +
                                  "\\ No newline at end of file";

    private String diffMessage3 = "diff --git a/test.txt b/test.txt\n" +
                                  "index \n" +
                                  "--- a/test.txt\n" +
                                  "+++ b/test.txt\n" +
                                  "@@ -1,2 +1,3 @@\n" +
                                  " first\n" +
                                  "-second\n" +
                                  "\\ No newline at end of file\n" +
                                  "+second\n" +
                                  "+third\n" +
                                  "\\ No newline at end of file";

    private String diffMessage2 = "diff --git a/test.txt b/test.txt\n" +
                                  "index \n" +
                                  "--- a/test.txt\n" +
                                  "+++ b/test.txt\n" +
                                  "@@ -1 +1,2 @@\n" +
                                  "-first\n" +
                                  "\\ No newline at end of file\n" +
                                  "+first\n" +
                                  "+second\n" +
                                  "\\ No newline at end of file";

    private String diffMessage1 = "diff --git a/test.txt b/test.txt\n" +
                                  "new file mode 100644\n" +
                                  "index \n" +
                                  "--- /dev/null\n" +
                                  "+++ b/test.txt\n" +
                                  "@@ -0,0 +1 @@\n" +
                                  "+first\n" +
                                  "\\ No newline at end of file";


    public ViewHistoryAndDiffWithPreviousVersionTest() {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            diffMessage4 = "diff --git a/test.txt b/test.txt\n" +
                           "deleted file mode 100644\n" +
                           "index \n" +
                           "--- a/test.txt\n" +
                           "+++ /dev/null\n" +
                           "@@ -1,3 +0,0 @@\n" +
                           "-\n" +
                           "-first\n" +
                           "-second\n" +
                           "-third";

            diffMessage3 = "diff --git a/test.txt b/test.txt\n" +
                           "index \n" +
                           "--- a/test.txt\n" +
                           "+++ b/test.txt\n" +
                           "@@ -1,2 +1,3 @@\n" +
                           " first\n" +
                           " second\n" +
                           "+third";

            diffMessage2 = "diff --git a/test.txt b/test.txt\n" +
                           "index \n" +
                           "--- a/test.txt\n" +
                           "+++ b/test.txt\n" +
                           "@@ -1 +1,2 @@\n" +
                           " first\n" +
                           "+second";

            diffMessage1 = "diff --git a/test.txt b/test.txt\n" +
                           "new file mode \n" +
                           "index \n" +
                           "--- /dev/null\n" +
                           "+++ b/test.txt\n" +
                           "@@ -0,0 +1 @@\n" +
                           "+first\n";

        }
    }


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
    }

    @Test
    public void viewHistoryAndDiffWithPreviousVersion() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        //Create test file test.txt with content "first"
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(TEXT_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(TEXT_FILE_CONTENT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile(TEXT_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.UNTRACKED);
        // add to index created file
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.ADDED);
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        // commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_1);
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_2);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.IN_REPOSITORY);

        // Add row "second" into the test.txt, add it to index and commit
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(TEXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor(TEXT_FILE_CONTENT_FOR_EDIT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile(TEXT_FILE_NAME);
        // add to index created file
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.CHANGED);
        // commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE_2);
        IDE.GIT.clickCommitButton();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.IN_REPOSITORY);

        // Add row "third" into the test.txt, add it to index and commit
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(TEXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor(TEXT_FILE_CONTENT_FOR_EDIT_2);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile(TEXT_FILE_NAME);
        // add to index created file
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.CHANGED);
        // commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE_3);
        IDE.GIT.clickCommitButton();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(TEXT_FILE_NAME, Git.GitIcons.IN_REPOSITORY);

        // Remove file from index and commit changes
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(TEXT_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(TEXT_FILE_NAME);
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REMOVAL_MESSAGE);
        // commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE_4);
        IDE.GIT.clickCommitButton();

        // Call "Git > Show History..." to view Git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE);
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE_2);
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE_3);
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESSAGE_4);

        // check last commit (remove)
        String outputMess = IDE.OUTPUT.getAllMessagesFromOutput();
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        validateHistoryMessage(diffMessage4);
        // check third commit (edit)
        IDE.GIT.selectCommitInHistory(COMMIT_MESSAGE_3);
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        validateHistoryMessage(diffMessage3);
        // check second commit (edit)
        IDE.GIT.selectCommitInHistory(COMMIT_MESSAGE_2);
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        validateHistoryMessage(diffMessage2);
        // check first commit (edit)
        IDE.GIT.selectCommitInHistory(COMMIT_MESSAGE);
        assertTrue(outputMess.contains(IDE.GIT.getCommitRevisionFromHistoryPanel()));
        assertTrue(outputMess.contains(IDE.GIT.getCommitDateFromHistoryPanel()));
        validateHistoryMessage(diffMessage1);
    }

    private void validateHistoryMessage(String mess) throws Exception {
        String[] splittedStr = mess.split("\n");
        for (String s : splittedStr) {
            try {

                assertTrue(IDE.GIT.getCommitDiffFromHistoryPanel().contains(s));
            } catch (Exception e) {
                try {
                    throw new Exception("Can no find changes in git history for your repository");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }

    }
}
