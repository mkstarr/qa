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
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:roman.iyvshyn@exoplatform.com">Roman Iyvshyn</a>
 * @author <a href="mailto:njusha.exo@gmail.com">Nadia Zavalko</a>
 *
 */
public class CutFileTest extends BaseTest {
    private static final String PROJECT = CutFileTest.class.getSimpleName();

    private static final String FILE_NAME_1 = "CutFileTest.txt";

    private static final String FOLDER_NAME_1 = CutFileTest.class.getSimpleName() + "-1";

    private static final String FOLDER_NAME_2 = CutFileTest.class.getSimpleName() + "-2";

    private static final String RANDOM_CONTENT = UUID.randomUUID().toString();

    private static final String CUT_OPENED_FILE = "Can't cut opened file %s";

    private static final String CUT_FOLDER_WITH_OPENED_FILE = "Can't cut folder %s, it contains open file %s";

    static final String WRONG_DESTINATION =
            "Item " + "'/" + PROJECT + "/" + FOLDER_NAME_2 + "/" + FILE_NAME_1 + "'" + " already exists.";


    private static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        try {
            project = VirtualFileSystemUtils.createDefaultProject(PROJECT);

            Link link = project.get(Link.REL_CREATE_FOLDER);

            Link linkFile = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_2);

            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_NAME_1 + "/" + FILE_NAME_1, MimeType.TEXT_PLAIN, RANDOM_CONTENT);
            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_NAME_2 + "/" + FILE_NAME_1, MimeType.TEXT_PLAIN, RANDOM_CONTENT);

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

    // IDE-114
    @Test
    public void testCutFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME_1);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.EDITOR.waitActiveFile();

        // Check Paste disabled:
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        // Check Copy enabled:
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.CUT_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.COPY_TOOLBAR);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        // Check Paste disabled:
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        // Check Copy enabled:
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.CUT_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.COPY_TOOLBAR);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);

        // Cut opened file causes warning message:
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.WARNING_DIALOG.waitOpened();
        assertEquals(String.format(CUT_OPENED_FILE, FILE_NAME_1), IDE.WARNING_DIALOG.getWarningMessage());
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();
        // Check Paste disabled:
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        // Cut folder, which contains opened file:
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.WARNING_DIALOG.waitOpened();
        assertEquals(String.format(CUT_FOLDER_WITH_OPENED_FILE, FOLDER_NAME_1, FILE_NAME_1),
                     IDE.WARNING_DIALOG.getWarningMessage());
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();
        // Check Paste disabled:
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        // Close file :
        IDE.EDITOR.closeFile(FILE_NAME_1);
        IDE.EDITOR.waitTabNotPresent(FILE_NAME_1);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_2);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.WARNING_DIALOG.waitOpened();


        IDE.WARNING_DIALOG.waitTextInDialogPresent(WRONG_DESTINATION);

        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.selectItem(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        assertEquals(404, VirtualFileSystemUtils
                .get(REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1)
                .getStatusCode());
        assertEquals(200,
                     VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FILE_NAME_1).getStatusCode());

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME_1);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FILE_NAME_1);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME_1);
        IDE.EDITOR.waitActiveFile();
        assertEquals(RANDOM_CONTENT, IDE.EDITOR.getTextFromCodeEditor());
        IDE.EDITOR.closeFile(FILE_NAME_1);
    }
}
