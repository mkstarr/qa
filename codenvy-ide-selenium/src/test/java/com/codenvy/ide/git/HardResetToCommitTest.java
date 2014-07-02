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

/** @author Roman Iuvshin */
public class HardResetToCommitTest extends BaseTest {

    private static final String PROJECT = "tstMixPrj";

    protected static Map<String, Link> project;

    private static final String ADDED_TO_INDEX_MESSAGE = "[INFO] Successfully added to index.";

    private static final String STATUS_MESSAGE = "# On branch master\n" +
                                                 "nothing to commit, working directory clean";

    private static final String PREVIOUS_COMMIT_MESSAGE = "Added files to repo.";

    private static final String COMMIT_OUTPUT_MESSAGE_1 = "Committed with revision";

    private static final String COMMIT_OUTPUT_MESSAGE_2 = "by user ";
    //+ USER_NAME;

    private static final String MESSAGE_FOR_CHANGE_CONTENT = "<!--change content-->";

    private static final String NEW_FILE_NAME = "newFile.css";

    private static final String COMMIT_MESSAGE = "test commit";

    private static final String FIRST_FILE_CONTENT = "<!--change content-->";

    private static final String SECOND_FILE_CONTENT = "//<!--change content-->";

    private static final String RESETED_FILE_CONTENT = "<!DOCTYPE web-app PUBLIC\n" +
                                                       " \"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\"\n" +
                                                       " \"http://java.sun.com/dtd/web-app_2_3.dtd\" >\n" +
                                                       "\n" +
                                                       "<web-app>\n" +
                                                       "   <display-name>Spring Web Application</display-name>\n" +
                                                       "   <servlet>\n" +
                                                       "      <servlet-name>spring</servlet-name>\n" +
                                                       "      <servlet-class>org.springframework.web.servlet" +
                                                       ".DispatcherServlet</servlet-class>\n" +
                                                       "      <load-on-startup>1</load-on-startup>\n" +
                                                       "   </servlet>\n" +
                                                       "   <servlet-mapping>\n" +
                                                       "      <servlet-name>spring</servlet-name>\n" +
                                                       "      <url-pattern>/spring/*</url-pattern>\n" +
                                                       "   </servlet-mapping>\n" +
                                                       "</web-app>";

    @BeforeClass
    public static void before() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void removeFilesFromIndexOnly() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void hardResetToCommitTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

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

        //Remove file from index.
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("WEB-INF");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("WEB-INF");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("web.xml");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("web.xml");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("web.xml");

        //add them to index and commit project.
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(ADDED_TO_INDEX_MESSAGE);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(COMMIT_MESSAGE);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_1);
        IDE.OUTPUT.waitForSubTextPresent(COMMIT_OUTPUT_MESSAGE_2);
        //Open file1-file3.
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("index.jsp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NEW_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        //Open "Git > Reset..." window and reset to previous commit with type reset "hard".
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.RESET);
        IDE.GIT.waitResetToCommitForm();
        IDE.GIT.selectHardResetCheckbox();
        IDE.GIT.selectCommitInResetToCommitForm(PREVIOUS_COMMIT_MESSAGE);
        IDE.GIT.clickResetButtonInResetToCommitForm();

        // output panel should display message about successfully reset.
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent("Successfully reseted.");
        IDE.LOADER.waitClosed();
        // Opened file1 and file2 should stayed as unchanged.
        IDE.EDITOR.selectTab("index.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText(FIRST_FILE_CONTENT);
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(SECOND_FILE_CONTENT);

        // File1 and file2 should stay marked as being in repository in Project / Package Explorers.
        // File3 should disappear from Project/Package explorers.
        // File4 should appear in Project/Package explorers and its content should be correct.
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("web.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("web.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(RESETED_FILE_CONTENT);

        IDE.EXPLORER.selectProjectTab(PROJECT);
        openFoldersInProjectExplorer();
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("web.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitItemNotPresent(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // Open git history.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        //Reseted commit should be absence.
        IDE.GIT.waitCommitInHistoryViewIsNotPresent(USER_NAME, COMMIT_MESSAGE);

        // Check git status.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(STATUS_MESSAGE);
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
        IDE.EXPLORER.openItem(PROJECT + "/src/main/webapp/WEB-INF");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp/WEB-INF/spring-servlet.xml");
    }

}
