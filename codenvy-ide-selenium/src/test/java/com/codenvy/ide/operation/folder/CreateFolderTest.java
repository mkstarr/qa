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
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */
public class CreateFolderTest extends BaseTest {

    private static String FOLDER_NAME_DEFAULT = "New Folder";

    private static String PROJECT = CreateFolderTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateFolderNotInProject() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.TOOLBAR.waitButtonFromNewPopupMenuDisabled(MenuCommands.New.FOLDER);
        Assert.assertNull(IDE.EXPLORER.getCurrentProject());
    }

    /**
     * Test to create folder using main menu (TestCase IDE-3).
     *
     * @throws Exception
     */
    @Test
    public void testCreateFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.TOOLBAR.waitButtonFromNewPopupMenuEnabled(MenuCommands.New.FOLDER);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.FOLDER);
        IDE.FOLDER.waitOpened();
        IDE.FOLDER.clickCreateButton();
        IDE.FOLDER.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_DEFAULT);
        assertEquals(200, VirtualFileSystemUtils
                .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_NAME_DEFAULT.replace(" ", "%20"))
                .getStatusCode());

    }

    /** Checks the present of create folder form elements. */
    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

}
