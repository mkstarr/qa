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
package com.codenvy.ide.operation.contextmenu;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Anna Shumilova
 *
 */
public class EditorContextMenuTest extends BaseTest {
    private final static String PROJECT = EditorContextMenuTest.class.getSimpleName();

    private final static String FILE_NAME = "contextmenu.txt";

    private final static String FILE_CONTENT = "Testing context menu.\nTesting context menu.";

    private final static String FILE_CONTENT_2 = "sting context menu.\nTesting context menu.";

    private final static String EDIT_CONTENT = " Test Undo/Redo.";

    /** Create test folder and test data object file. */
    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/contextmenu.txt";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_PLAIN, filePath);
        } catch (Exception e) {
        }
    }

    /** Clear tests results. */
    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @After
    public void forceCloseEditorTab() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
    }

    @Test
    public void testSelectAll() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();

        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.moveCursorDown(2);
        IDE.EDITOR.openContextMenu();
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.SELECT_ALL);
        IDE.CONTEXT_MENU.waitClosed();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "c");
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "v");
        assertEquals(FILE_CONTENT, IDE.EDITOR.getTextFromCodeEditor());
    }

    @Test
    public void testDeleteText() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + Keys.HOME.toString());
        IDE.EDITOR
           .typeTextIntoEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT.toString() + Keys.ARROW_RIGHT.toString());
        IDE.EDITOR.openContextMenu();
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.DELETE);
        IDE.CONTEXT_MENU.waitClosed();
        assertEquals(FILE_CONTENT_2, IDE.EDITOR.getTextFromCodeEditor());
    }

    @Test
    public void testUndoRedoChanges() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.EDITOR.selectTab(FILE_NAME);
        IDE.EDITOR.openContextMenu();
        IDE.CONTEXT_MENU.waitOpened();
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.REDO_TYPING));
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.UNDO_TYPING));
        IDE.CONTEXT_MENU.closeContextMenu();
        IDE.CONTEXT_MENU.waitClosed();

        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_DOWN.toString() + Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor(EDIT_CONTENT);
        IDE.EDITOR.openContextMenu();
        IDE.CONTEXT_MENU.waitOpened();
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.UNDO_TYPING));
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.REDO_TYPING));
        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.UNDO_TYPING);
        IDE.CONTEXT_MENU.waitClosed();

        assertEquals(FILE_CONTENT, IDE.EDITOR.getTextFromCodeEditor());

        IDE.EDITOR.openContextMenu();
        IDE.CONTEXT_MENU.waitOpened();
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.UNDO_TYPING));
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.REDO_TYPING));
        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.REDO_TYPING);
        IDE.CONTEXT_MENU.waitClosed();

        assertEquals(FILE_CONTENT + EDIT_CONTENT, IDE.EDITOR.getTextFromCodeEditor());
    }
}
