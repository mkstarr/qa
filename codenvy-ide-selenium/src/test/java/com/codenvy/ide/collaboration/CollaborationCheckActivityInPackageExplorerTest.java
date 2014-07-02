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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Roman Iuvshin */
public class CollaborationCheckActivityInPackageExplorerTest extends CollaborationService {
    private static final String PROJECT = CollaborationCheckActivityInPackageExplorerTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String CONTENT =
            "package helloworld;\n\npublic class NewClass\n{\n   public double bigValuenewMTd()\n   {\n      final " +
            "double pi = 3.14159265;\n      return pi;\n   }\n}";

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void deleteItemsInPackageExplorerTest() throws Exception {

        initSecondBrowser();

        // opening project from first user
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        // opening project from second user
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        //check file from first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("index.jsp");
        //check file from second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("index.jsp");

        // delete file from first user and check on second
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("index.jsp");
        IDE.DELETE.deleteSelectedItems();
        //check that file was deleted in first and second users
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("index.jsp");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("index.jsp");

        // delete folder from second user and check on first
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer("WEB-INF");
        IDE2.DELETE.deleteSelectedItems();

        //check that folder was deleted for two users
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("WEB-INF");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("WEB-INF");
    }

    @Test
    public void createNewItemsTest() throws Exception {
        // creating folder from second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE2.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.FOLDER);
        IDE2.FOLDER.waitOpened();
        IDE2.FOLDER.typeFolderName("new_folder");
        IDE2.FOLDER.clickCreateButton();
        IDE2.FOLDER.waitClosed();

        //checking that folder was created for first and second users
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("new_folder");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("new_folder");

        //creating file from first user
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("newFile.css");
        IDE.FILE.clickCreateButton();

        // checking that file was created for first and second users
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("newFile.css");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("newFile.css");

        IDE.EDITOR.closeFile("newFile.css");
    }

    @Test
    public void renameItemsTest() throws Exception {
        // select file and rename it
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("newFile.css");
        IDE.RENAME.rename("newFileRenamed.css");
        /// check that file was renamed for both users
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("newFileRenamed.css");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("newFileRenamed.css");

        // select folder and rename it
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer("new_folder");
        IDE2.RENAME.rename("renamed_folder");
        // check that folder was renamed for both users
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("renamed_folder");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("renamed_folder");

        //check that items with old names does not visible
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("newFile.css");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("new_folder");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("newFile.css");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("new_folder");
    }
}
