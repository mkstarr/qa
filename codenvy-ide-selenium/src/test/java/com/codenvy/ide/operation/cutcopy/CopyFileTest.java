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

/**
 * IDE-115:Copy file.
 * 
 * @author Vitaliy Gulyy
 *
 */
public class CopyFileTest extends BaseTest {
    private static final String PROJECT        = CopyFileTest.class.getSimpleName();

    private static final String FOLDER_1       = "folder";

    private static final String FILE_TEXT      = "testplain";

    private static final String FILE_CONTENT_1 = "world";

    private static final String FILE_CONTENT_2 = "hello ";

    /** BeforeClass create such structure: FOLDER_1 FILE_HTML - file with sample content */
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils
                                  .createFile(linkFile, FOLDER_1 + "/" + FILE_TEXT, MimeType.TEXT_PLAIN, FILE_CONTENT_1);

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
    public void testCopyFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);

        /*
         * Check Cut and Copy commands must be enabled
         */
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.COPY_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.COPY_TOOLBAR);

        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.CUT_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.CUT_TOOLBAR);

        /*
         * Check Paste command must be disabled
         */
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.TOOLBAR.waitForButtonDisabled(MenuCommands.Edit.PASTE_TOOLBAR);

        /*
         * Click Copy command on toolbar
         */
        IDE.TOOLBAR.runCommand(MenuCommands.Edit.COPY_TOOLBAR);

        /*
         * Check Paste must be enabled
         */
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.PASTE_TOOLBAR);

        /*
         * Select project in workspace panel
         */
        IDE.EXPLORER.selectItem(PROJECT);

        /*
         * Click Paste command
         */
        IDE.TOOLBAR.runCommand(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_TEXT);

        /*
         * Check Paste command must be disabled
         */
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.TOOLBAR.waitForButtonDisabled(MenuCommands.Edit.PASTE_TOOLBAR);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_TEXT);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(FILE_CONTENT_2);
        IDE.EDITOR.waitFileContentModificationMark(FILE_TEXT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(FILE_TEXT);
        IDE.EDITOR.closeFile(FILE_TEXT);

        /*
         * Open files
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_TEXT);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_TEXT);
        IDE.EDITOR.waitActiveFile();

        /*
         * Check files content
         */
        IDE.EDITOR.selectTab(1);
        assertEquals(FILE_CONTENT_1, IDE.EDITOR.getTextFromCodeEditor());
        IDE.EDITOR.selectTab(2);
        assertEquals(FILE_CONTENT_2 + FILE_CONTENT_1, IDE.EDITOR.getTextFromCodeEditor());
    }
}
