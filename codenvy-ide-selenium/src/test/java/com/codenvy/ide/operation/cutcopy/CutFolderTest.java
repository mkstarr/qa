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

import com.codenvy.ide.*;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * IDE-112 : Cut folder
 *
 * @author Evgen Vidolob
 *
 */
public class CutFolderTest extends BaseTest {
    private static final String PROJECT = CutFolderTest.class.getSimpleName();

    private static final String FILE_CONTENT = "file content";

    private static final String FOLDER1 = "folder1";

    private static final String FOLDER2 = "folder2";

    private static final String FILE = "file.xml";

    private static final String WARNING_SAME_DIR = "Can't move items in the same directory!";

    private static final String WRONG_MESS = "Item " + "'" + "/" + PROJECT + "/" + FOLDER2 + "'" + " already exists.";

    /**
     * Create next folders' structure in the workspace root: folder 1/ folder 2/ file.xml - file with sample content
     * folder 2/
     */
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER1);
            VirtualFileSystemUtils.createFolder(link, FOLDER1 + "/" + FOLDER2);

            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER1 + "/" + FOLDER2 + "/" + FILE, MimeType.TEXT_XML, FILE_CONTENT);

            // VirtualFileSystemUtils.put(FILE_CONTENT.getBytes(), MimeType.APPLICATION_GROOVY, WS_URL + PROJECT + "/"
            // + FOLDER1 + "/" + FOLDER2 + "/" + FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER2);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCutFolderOperation() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1);

        // Open folder 1
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1 + "/" + FOLDER2);

        // Open folder 2
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER1 + "/" + FOLDER2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1 + "/" + FOLDER2 + "/" + FILE);

        // Paste commands are disabled, Cut/Copy are enabled
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.CUT_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.COPY_TOOLBAR);

        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER1 + "/" + FOLDER2);
        IDE.TOOLBAR.runCommand(MenuCommands.Edit.CUT_TOOLBAR);

        // Paste commands are enabled.
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER1 + "/" + FOLDER2 + "/" + FILE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        // Paste in the same folder:
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER1);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);

        IDE.WARNING_DIALOG.waitOpened();
        assertTrue(IDE.WARNING_DIALOG.getWarningMessage().contains(WARNING_SAME_DIR));
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        // Paste in folder, which contains with the same name:
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.TOOLBAR.runCommand(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.WARNING_DIALOG.waitOpened();

        IDE.WARNING_DIALOG.waitTextInDialogPresent(WRONG_MESS);
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER2);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        checkItemsOnWebDav();

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER2 + "/" + FOLDER2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER2 + "/" + FOLDER2 + "/" + FILE);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER2 + "/" + FOLDER2 + "/" + FILE);
        IDE.EDITOR.waitActiveFile();
        assertEquals(FILE_CONTENT, IDE.EDITOR.getTextFromCodeEditor());

        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER2 + "/" + FOLDER2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER2 + "/" + FOLDER2 + "/" + FILE);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + FOLDER1 + "/" + FOLDER2);
        IDE.EDITOR.closeFile(1);
    }

    /**
     * Check, that FOLDER_1, FOLDER_2, FOLDER_2/FOLDER2, FOLDER_2/FOLDER_2/FILE_1 are present on webdav.
     * <p/>
     * And FOLDER_1/FOLDER_2 are not present.
     *
     * @throws Exception
     */
    private void checkItemsOnWebDav() throws Exception {
        assertEquals(200, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER1).getStatusCode());
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER1 + "/" + FOLDER2)
                                                .getStatusCode());
        assertEquals(200, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER2).getStatusCode());
        assertEquals(200, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER2 + "/" + FOLDER2)
                                                .getStatusCode());
        assertEquals(200, VirtualFileSystemUtils
                .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER2 + "/" + FOLDER2 + "/" + FILE)
                .getStatusCode());
    }

}
