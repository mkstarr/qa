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
package com.codenvy.ide.operation.cutcopy;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Response;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * IDE-113:Copy folders and files.
 *
 * @author Evgen Vidolob
 *
 */
public class CopyFoldersAndFilesTest extends BaseTest {

    private static final String PROJECT = CopyFoldersAndFilesTest.class.getSimpleName();

    private static final String FOLDER_1 = CopyFoldersAndFilesTest.class.getSimpleName() + "-1";

    private static final String FOLDER_2 = CopyFoldersAndFilesTest.class.getSimpleName() + "-2";

    private static final String FOLDER_1_1 = CopyFoldersAndFilesTest.class.getSimpleName() + "-1-1";

    private static final String FOLDER_1_2 = CopyFoldersAndFilesTest.class.getSimpleName() + "-1-2";

    private static final String FILE_TEXT = "file_txt";

    private static final String FILE_XML = "test_xml";

    private static final String RANDOM_CONTENT_1 = UUID.randomUUID().toString();

    private static final String RANDOM_CONTENT_2 = UUID.randomUUID().toString();

    /**
     * BeforeClass create such structure: PROJECT FOLDER_1 FILE_GADGET - file with sample content FILE_GROOVY - file
     * with
     * sample content FOLDER_1_1 FOLDER_1_2 FOLDER_2
     */
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1 + "/" + FOLDER_1_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1 + "/" + FOLDER_1_2);
            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_1 + "/" + FILE_TEXT, MimeType.TEXT_PLAIN, RANDOM_CONTENT_1);
            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_1 + "/" + FILE_XML, MimeType.TEXT_XML, RANDOM_CONTENT_2);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    /**
     * IDE-113.
     *
     * @throws Exception
     */
    @Test
    public void testCopyFoldersAndFiles() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1);

        // Open files:
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_XML);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_XML);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.TOOLBAR.waitForButtonDisabled(MenuCommands.Edit.PASTE_TOOLBAR);

        // Call the "Edit->Copy Items" topmenu command.
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_2);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1);

        checkFilesAndFoldersOnServer();

        assertTrue(IDE.EDITOR.isTabPresentInEditorTabset(FILE_XML));
        assertFalse(IDE.EDITOR.isTabPresentInEditorTabset(FILE_TEXT));

        IDE.MENU.waitCommandVisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.TOOLBAR.waitForButtonDisabled(MenuCommands.Edit.PASTE_TOOLBAR);
    }

    private void checkFilesAndFoldersOnServer() throws Exception {
        assertEquals(200, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1)
                                                .getStatusCode());
        assertEquals(200,
                     VirtualFileSystemUtils
                             .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1 + "/" + FOLDER_1_2)
                             .getStatusCode());
        assertEquals(200,
                     VirtualFileSystemUtils
                             .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1 + "/" + FILE_XML)
                             .getStatusCode());
        assertEquals(404,
                     VirtualFileSystemUtils
                             .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1 + "/" + FILE_TEXT)
                             .getStatusCode());
        assertEquals(404,
                     VirtualFileSystemUtils
                             .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1 + "/" + FOLDER_1_1)
                             .getStatusCode());

        Response response =
                VirtualFileSystemUtils.get(REST_URL + "contentbypath/" + PROJECT + "/" + FOLDER_1 + "/" + FILE_XML);
        assertEquals(200, response.getStatusCode());

        assertEquals(RANDOM_CONTENT_2, response.getData());

        response =
                VirtualFileSystemUtils
                        .get(REST_URL + "contentbypath/" + PROJECT + "/" + FOLDER_2 + "/" + FOLDER_1 + "/" + FILE_XML);
        assertEquals(200, response.getStatusCode());
        assertEquals(RANDOM_CONTENT_2, response.getData());
    }

}
