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
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Roman Iuvshin */
public class RemoveFilesFromIndexOnlyTest extends BaseTest {

    private static final String PROJECT = "testPrj";

    protected static Map<String, Link> project;

    private static final String REMOVAL_MESSAGE = "[INFO] Removal was successful.\n[INFO] Removal was successful.";

    private static final String STATUS_MESSAGE = "# On branch master\n" +
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
    public static void removeFilesFromIndexOnly() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void resetIndexTest() throws Exception {
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
        IDE.GIT.selectRemoveFromIndexOnlyCheckbox();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.REMOVED);
        // select second file and call git remove
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingController.java");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.GIT.waitRemoveFromIndexForm();
        IDE.GIT.selectRemoveFromIndexOnlyCheckbox();
        IDE.GIT.clickRemoveButtonInRemoveFromIndexForm();
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.REMOVED);
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REMOVAL_MESSAGE);
        // check that file icons was changed in project explorer
        IDE.EXPLORER.selectProjectTab(PROJECT);
        openFoldersInProjectExplorer();
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.REMOVED);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.REMOVED);
        // check that editor tabs still opened
        IDE.EDITOR.waitTabPresent("GreetingController.java");
        IDE.EDITOR.waitTabPresent("index.jsp");

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

    private void openFoldersInProjectExplorer() throws Exception {
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");
        //   IDE.EXPLORER.openItem(PROJECT + "/src");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main");
        // IDE.EXPLORER.openItem(PROJECT + "/src/main");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java");
        //  IDE.EXPLORER.openItem(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/java/helloworld");
        //  IDE.EXPLORER.openItem(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src/main/webapp");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/webapp");
    }
}
