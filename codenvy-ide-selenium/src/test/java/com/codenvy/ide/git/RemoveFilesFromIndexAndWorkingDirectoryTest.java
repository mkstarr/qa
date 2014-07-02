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
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Roman Iuvshin */
public class RemoveFilesFromIndexAndWorkingDirectoryTest extends BaseTest {

    private static final String PROJECT = ResetIndexTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String STATUS_MESSAGE = "[INFO] Removal was successful.\n" +
                                                 "[INFO] Removal was successful.\n" +
                                                 "# On branch master\n" +
                                                 "# Changes to be committed:\n" +
                                                 "#   (use \"git reset HEAD ...\" to unstage)\n" +
                                                 "#\n" +
                                                 "# deleted:    src/main/java/helloworld/GreetingController.java\n" +
                                                 "# deleted:    src/main/webapp/index.jsp\n" +
                                                 "#";

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
    public void removeFilesFromIndexAndWorkingDirectory() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // opening few files
        openSrcFoldersInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("index.jsp");
        IDE.EDITOR.waitActiveFile();
        openPackageInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        // select first file and call git remove
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("index.jsp");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("index.jsp");
        // select second file and call git remove
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingController.java");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("GreetingController.java");
        // check that files disappear from project explorer
        IDE.EXPLORER.selectProjectTab(PROJECT);
        checkNodesInProjectExplorer();
        IDE.EXPLORER.waitItemNotPresent("GreetingController.java");
        IDE.EXPLORER.waitItemNotPresent("index.jsp");
        // check that editor tabs was closed
        IDE.EDITOR.waitTabNotPresent("GreetingController.java");
        IDE.EDITOR.waitTabNotPresent("index.jsp");

        // Check git status.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
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

    private void checkNodesInProjectExplorer() throws Exception {
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp");
    }

}