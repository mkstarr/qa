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
public class CollaborationNotifyUsersTest extends CollaborationService {

    private static final String PROJECT = CollaborationNotifyUsersTest.class.getSimpleName();

    protected static Map<String, Link> project;

    public static final  String       FILE_NAME = "NewClass.java";
    private static final CharSequence MESSAGE   = "All users close file, now you may perform operation.";

    @BeforeClass
    public static void before() {

        try {
            project = VirtualFileSystemUtils
                    .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
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
    public void checkRenameNotifyUsersTest() throws Exception {
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
        // open java class as second userf
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();

        // try to rename same file from first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.NOTIFICATIONS.waitNotifyUsersFormAppear();
        IDE.NOTIFICATIONS.clickOnNotifyUsersButton();

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();


        //  User musienko.maxim@gmail.com wants to perform refactoring NewClass.java and asks you to close file NewClass.java
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(
                "User " + USER_NAME + " wants to perform refactoring " + FILE_NAME + " and asks you to close file " + FILE_NAME));

        //second user close notification and file
        IDE2.NOTIFICATIONS.closeNotification();
        IDE2.EDITOR.closeFile(FILE_NAME);

        //first user check notify that all users closed such file
        IDE.NOTIFICATIONS.waitNotifyUsersFormDisappear();
        IDE.INFORMATION_DIALOG.waitOpened();
        assertTrue(IDE.INFORMATION_DIALOG.getMessage().contains(MESSAGE));
        IDE.INFORMATION_DIALOG.clickOk();
        IDE.INFORMATION_DIALOG.waitClosed();

        //-------------------------------------------
        // first user open file, second try to rename
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // try to rename same file from second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE2.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE2.NOTIFICATIONS.waitNotifyUsersFormAppear();
        IDE2.NOTIFICATIONS.clickOnNotifyUsersButton();

        // check notification for second user
        IDE.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE.NOTIFICATIONS.getNotificationContent().contains(
                "User " + NOT_ROOT_USER_NAME + " wants to perform refactoring " + FILE_NAME + " and asks you to close file " + FILE_NAME));

        //first user close notification and file
        IDE.NOTIFICATIONS.closeNotification();
        IDE.EDITOR.closeFile(FILE_NAME);

        // second user check notify that all users closed such file
        IDE2.NOTIFICATIONS.waitNotifyUsersFormDisappear();
        IDE2.INFORMATION_DIALOG.waitOpened();
        assertTrue(IDE2.INFORMATION_DIALOG.getMessage().contains(MESSAGE));
        IDE2.INFORMATION_DIALOG.clickOk();
        IDE2.INFORMATION_DIALOG.waitClosed();
    }

    @Test
    public void checkDeleteNotifyUsersTest() throws Exception {
        String mess1 = "User " + USER_NAME + " wants to delete " + FILE_NAME + " and ask you to close file " + FILE_NAME;
        String mess2 = "User " + NOT_ROOT_USER_NAME + " wants to delete " + FILE_NAME + " and ask you to close file " + FILE_NAME;
        checkNotifications(ToolbarCommands.File.DELETE, mess1, mess2);
    }

    @Test
    public void checkCutNotifyUsersTest() throws Exception {
        String mess1 = "User " + USER_NAME + " wants to move " + FILE_NAME + " and ask you to close file " + FILE_NAME;
        String mess2 = "User " + NOT_ROOT_USER_NAME + " wants to move " + FILE_NAME + " and ask you to close file " + FILE_NAME;
        checkNotifications(ToolbarCommands.File.CUT_SELECTED_ITEM, mess1, mess2);
    }

    private void checkNotifications(String command, String popupMessage1, String popupMessage2) throws Exception {
        // open java file as second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();

        // first user trying to cut file
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE.TOOLBAR.runCommand(command);
        IDE.NOTIFICATIONS.waitNotifyUsersFormAppear();
        IDE.NOTIFICATIONS.clickOnNotifyUsersButton();

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(popupMessage1));

        // second user close notification and file
        IDE2.NOTIFICATIONS.closeNotification();
        IDE2.EDITOR.closeFile(FILE_NAME);

        //first user check notify that all users closed such file
        IDE.NOTIFICATIONS.waitNotifyUsersFormDisappear();
        IDE.INFORMATION_DIALOG.waitOpened();
        assertTrue(IDE.INFORMATION_DIALOG.getMessage().contains(MESSAGE));
        IDE.INFORMATION_DIALOG.clickOk();
        IDE.INFORMATION_DIALOG.waitClosed();

        //-------------------------------------------
        // first user open file, second try to rename
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // try to delete same file from second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE2.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE.TOOLBAR.runCommand(command);
        IDE2.NOTIFICATIONS.waitNotifyUsersFormAppear();
        IDE2.NOTIFICATIONS.clickOnNotifyUsersButton();

        // check notification for second user
        IDE.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE.NOTIFICATIONS.getNotificationContent().contains(popupMessage2));

        //first user close notification and file
        IDE.NOTIFICATIONS.closeNotification();
        IDE.EDITOR.closeFile(FILE_NAME);

        // second user check notify that all users closed such file
        IDE2.NOTIFICATIONS.waitNotifyUsersFormDisappear();
        IDE2.INFORMATION_DIALOG.waitOpened();
        assertTrue(IDE2.INFORMATION_DIALOG.getMessage().contains(MESSAGE));
        IDE2.INFORMATION_DIALOG.clickOk();
        IDE2.INFORMATION_DIALOG.waitClosed();
    }
}