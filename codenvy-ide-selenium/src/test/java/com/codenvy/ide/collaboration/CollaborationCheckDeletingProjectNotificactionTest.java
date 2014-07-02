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
public class CollaborationCheckDeletingProjectNotificactionTest extends CollaborationService {

    private static final String PROJECT = CollaborationCheckDeletingProjectNotificactionTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            killSecondBrowser();
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkDeletingProjectNotificactionTest() throws Exception {
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

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        IDE.TOOLBAR.runCommand(ToolbarCommands.File.DELETE);

        IDE.NOTIFICATIONS.waitNotifyUsersFormAppear();
        IDE.NOTIFICATIONS.clickOnNotifyUsersButton();

        // check notification for second user
        IDE2.NOTIFICATIONS.waitNotificationPopup();
        assertTrue(IDE2.NOTIFICATIONS.getNotificationContent().contains(USER_NAME + " wants to delete " + PROJECT
                                                                        + " and asks you to close this project."));
        // second user close notification and project
        IDE2.NOTIFICATIONS.closeNotification();
        IDE2.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        // first user check notify that all users closed such file
        IDE.NOTIFICATIONS.waitNotifyUsersFormDisappear();
        IDE.INFORMATION_DIALOG.waitOpened();

        IDE.INFORMATION_DIALOG.clickOk();
        IDE.INFORMATION_DIALOG.waitClosed();

        // deleting project
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        // check that project was deleted for first user
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitItemNotPresent(PROJECT);

        // check that project was deleted for second user
        IDE2.EXPLORER.waitItemNotPresent(PROJECT);
    }

}
