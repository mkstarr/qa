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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CollaborationCheckNotificationsTest extends CollaborationService {

    private static final String PROJECT = CollaborationCheckNotificationsTest.class.getSimpleName();

    private static final String NEW_FILE_NAME = "NewCssFile.css";

    private static final String NAME_FOR_RENAME = "renamedCssFile.css";

    public static final String JAVA_FILE = "GreetingController.java";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
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
    public void checkDeletedFileNotification() throws Exception {
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
        // delete file as first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("NewClass.java");
        IDE.DELETE.deleteSelectedItems();

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(
                "User " + USER_NAME + " deleted: /" + PROJECT + "/src/main/java/helloworld/NewClass.java"));
        IDE2.NOTIFICATIONS.waitUntilNotificationPopupDisappear();
    }

    @Test
    public void checkCreateNewFileNotification() throws Exception {
        // creating new file on second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE2.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.CSS_FILE);
        IDE2.FILE.waitCreateNewFileWindow();
        IDE2.FILE.typeNewFileName(NEW_FILE_NAME);
        IDE2.FILE.clickCreateButton();
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.EDITOR.closeFile(NEW_FILE_NAME);
        // check notification for first user
        IDE.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE.NOTIFICATIONS.getNotificationContent().contains(
                "User " + NOT_ROOT_USER_NAME + " created: /" + PROJECT + "/" + NEW_FILE_NAME));
        IDE.NOTIFICATIONS.waitUntilNotificationPopupDisappear();
    }

    @Test
    public void checkCopyFileNotification() throws Exception {
        // copy file from package to root from first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(JAVA_FILE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(JAVA_FILE);
        // copy file
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.COPY_SELECTED_ITEM);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.COPY_SELECTED_ITEM);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        // closing folders
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + JAVA_FILE);

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(
                "User " + USER_NAME + " created: /" + PROJECT + "/" + JAVA_FILE));
        IDE2.NOTIFICATIONS.waitUntilNotificationPopupDisappear();
        IDE2.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + JAVA_FILE);
    }

    @Test
    public void checkMoveFileNotification() throws Exception {
        // cut file from second user
        IDE2.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + JAVA_FILE);
        IDE2.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/" + JAVA_FILE);
        IDE2.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.CUT_SELECTED_ITEM);
        IDE2.TOOLBAR.runCommand(ToolbarCommands.File.CUT_SELECTED_ITEM);
        IDE2.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE2.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/src");
        IDE2.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/src");
        // move file to src folder
        IDE2.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);
        IDE2.LOADER.waitClosed();
        IDE2.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/src/" + JAVA_FILE);

        // check notification for first user
        IDE.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE.NOTIFICATIONS.getNotificationContent().contains(
                "User " + NOT_ROOT_USER_NAME + " move: /" + PROJECT + "/" + JAVA_FILE + " to " + "/" + PROJECT + "/src/" + JAVA_FILE));
        IDE.NOTIFICATIONS.waitUntilNotificationPopupDisappear();

    }

    @Test
    public void checkRenameNotification() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(NEW_FILE_NAME);
        IDE.RENAME.rename(NAME_FOR_RENAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NAME_FOR_RENAME);

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(
                "User " + USER_NAME + " rename: /" + PROJECT + "/" + NEW_FILE_NAME + " to " + NAME_FOR_RENAME));
        IDE2.NOTIFICATIONS.waitUntilNotificationPopupDisappear();
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(NAME_FOR_RENAME);
    }
}