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
package com.codenvy.ide.operation.folder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */
public class DeleteFolderTest extends BaseTest {
    private final static String PROJECT = DeleteFolderTest.class.getSimpleName();

    private final static String FOLDER_NAME_TOOLBAR = "deleteFolderToolBarTest";

    private final static String FOLDER_NAME_MENU = "deleteFolderMenuTest";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_TOOLBAR);
            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_MENU);
        } catch (Exception e) {
        }
    }

    /**
     * Test to delete folder using ToolBar button. (TestCase IDE-18)
     *
     * @throws Exception
     */
    @Test
    public void testDeleteFolderFromToolbar() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_TOOLBAR);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_TOOLBAR);

        IDE.TOOLBAR.runCommand(ToolbarCommands.File.DELETE);
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();

        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FOLDER_NAME_TOOLBAR);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + FOLDER_NAME_TOOLBAR);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_NAME_TOOLBAR)
                                                .getStatusCode());
    }

    /**
     * Test to delete folder using Main Menu. (TestCase IDE-18)
     *
     * @throws Exception
     */
    @Test
    public void testDeleteFolderFromMainMenu() throws Exception {
        driver.navigate().refresh();

        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_MENU);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_MENU);

        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.DELETE);
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();

        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FOLDER_NAME_MENU);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + FOLDER_NAME_MENU);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_NAME_MENU)
                                                .getStatusCode());
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }
}
