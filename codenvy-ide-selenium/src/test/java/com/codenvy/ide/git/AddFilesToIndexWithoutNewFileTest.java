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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Roman Iuvshin */
public class AddFilesToIndexWithoutNewFileTest extends GitServices {

    private static final String PROJECT = AddFilesToIndexWithoutNewFileTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String MESSAGE_FOR_CHANGE_CONTENT = "<!--change content-->";

    private static final String NEW_FILE_NAME = "newFile.css";

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String STATUS_MESSAGE = "# On branch master\n" +
                                                 "# Changes to be committed:\n" +
                                                 "#   (use \"git reset HEAD ...\" to unstage)\n" +
                                                 "#\n" +
                                                 "# modified:   src/main/java/helloworld/GreetingController.java\n" +
                                                 "# modified:   src/main/webapp/index.jsp\n" +
                                                 "#\n" +
                                                 "# Untracked files:\n" +
                                                 "#   (use \"git add ...\" to include in what will be committed)\n" +
                                                 "#\n" +
                                                 "# newFile.css";

    private static final String STATUS_MESSAGE_AFTER_EDIT = "# On branch master\n" +
                                                            "# Changes to be committed:\n" +
                                                            "#   (use \"git reset HEAD ...\" to unstage)\n" +
                                                            "#\n" +
                                                            "# modified:   src/main/java/helloworld/GreetingController.java\n" +
                                                            "# modified:   src/main/webapp/index.jsp\n" +
                                                            "#\n" +
                                                            "# Changes not staged for commit:\n" +
                                                            "#   (use \"git add ...\" to update what will be committed)\n" +
                                                            "#   (use \"git checkout -- ...\" to discard changes in working directory)\n" +
                                                            "#\n" +
                                                            "# modified:   src/main/java/helloworld/GreetingController.java\n" +
                                                            "# modified:   src/main/webapp/index.jsp\n" +
                                                            "#\n" +
                                                            "# Untracked files:\n" +
                                                            "#   (use \"git add ...\" to include in what will be committed)\n" +
                                                            "#\n" +
                                                            "# newFile.css";

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
    public void addFilesToIndexWithOutNewFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // changing few files and adding new one file
        openSrcFoldersInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("index.jsp");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        // change content in java file
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(14);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//" + MESSAGE_FOR_CHANGE_CONTENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        // create new file
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.closeFile(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);

        // check git icons of created and modified files in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);

        // check git icons of created and modified files in project explorer
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.EXPLORER.waitOpened();
        openFoldersInProjectExplorer();
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.MODIFIED);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.MODIFIED);
        IDE.EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);

        // back in package explorer
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // Select root directory of project. and call "Git > Add...".
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickOnUpdateCheckBoxInAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        // checking git icons in package explorer
        openPackageInPackageExplorer();
        openSrcFoldersInPackageExplorer();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);
        // check git icons of created and modified files in project explorer
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.CHANGED);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.CHANGED);
        IDE.EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);

        // back to package explorer and  Check git status.
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(STATUS_MESSAGE);

        // Change file1, file2 and file3.
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
        IDE.EDITOR.closeAndSaveChanges("GreetingController.java");
        // change content in new file
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NEW_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(MESSAGE_FOR_CHANGE_CONTENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(NEW_FILE_NAME);
        // check git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.CHANGED);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);
        // check git icons in project explorer
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.CHANGED);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.CHANGED);
        IDE.EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);
        // back to package explorer and  Check git status.
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(STATUS_MESSAGE_AFTER_EDIT);
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