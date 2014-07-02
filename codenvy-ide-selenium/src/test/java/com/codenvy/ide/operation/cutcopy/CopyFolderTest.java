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
 * IDE-116:Copy folder.
 *
 * @author Vitaliy Gulyy
 *
 */
public class CopyFolderTest extends BaseTest {
    private static final String PROJECT = CopyFolderTest.class.getSimpleName();

    private static final String FILE_1 = "test";

    private final static String FOLDER_1 = "Test-1";

    private final static String FOLDER_1_1 = "Test-1.1";

    private static final String FILE_CONTENT_1 = "file content";

    /** BeforeClass create such structure: FOLDER_1 FOLDER_1_1 FILE_GROOVY - file with sample content */
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1 + "/" + FOLDER_1_1);
            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_1 + "/" + FOLDER_1_1 + "/" + FILE_1, MimeType.TEXT_XML,
                                FILE_CONTENT_1);
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

    /*
     * Create folder "/Test 1" Create folder "/Test 1/Test 1.1" Create new
     * groovy script
     *
     * Type "groovy file content" Save file as "/Test 1/Test 1.1/test.groovy"
     *
     * Select folder "/Test 1/Test 1.1"
     *
     * Check Paste must be disabled Check Copy must be enabled
     *
     * Call "Edit/Copy" in menu
     *
     * Check Paste must be enabled
     *
     * Select root in workspace tree and call "Edit/Paste"
     *
     * Edit currently opened file Call "Ctrl+S" Close opened file
     *
     * Open "/Test 1.1/test.groovy" Check it content
     */
    @Test
    public void copyOperationTestIde116() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1 + "/" + FILE_1);

        // Open file:
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1 + "/" + FILE_1);
        IDE.EDITOR.waitActiveFile();

        // Select folder "/Test 1/Test 1.1"
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.COPY_TOOLBAR);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.COPY_TOOLBAR);

        // Check Paste must be disabled
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.Edit.PASTE_TOOLBAR);
        IDE.TOOLBAR.waitForButtonDisabled(MenuCommands.Edit.PASTE_TOOLBAR);

        // Call "Edit/Copy" in menu
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);

        // Check Paste must be enabled
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.PASTE_TOOLBAR);

        // Select root in workspace tree and call "Edit/Paste"
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.PASTE_MENU);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1);

        // Change text in file.
        IDE.EDITOR.selectTab(FILE_1);
        IDE.EDITOR.typeTextIntoEditor("updated");
        IDE.EDITOR.waitFileContentModificationMark(FILE_1);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(FILE_1);
        IDE.EDITOR.closeFile(1);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1_1 + "/" + FILE_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1_1 + "/" + FILE_1);
        IDE.EDITOR.waitActiveFile();
        assertEquals(FILE_CONTENT_1, IDE.EDITOR.getTextFromCodeEditor());
    }
}
