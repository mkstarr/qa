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
package com.codenvy.ide.operation.edit;

import com.codenvy.ide.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */
public class UndoRedoEditingInCodeEditorTest extends BaseTest {
    private static String UNDO_REDO_TXT = "undo-redo.txt";

    private static String PROJECT = UndoRedoEditingInCodeEditorTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testUndoRedoEditingInCodeEditor() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.saveAs(1, UNDO_REDO_TXT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + UNDO_REDO_TXT);
        IDE.EDITOR.typeTextIntoEditor("1");
        IDE.EDITOR.waitFileContentModificationMark(UNDO_REDO_TXT);

        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);

        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor("2");
        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.EDITOR.typeTextIntoEditor("3");

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals("12", IDE.EDITOR.getTextFromCodeEditor());

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "z");
        assertEquals("1", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        assertEquals("", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.REDO);
        assertEquals("1", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);
        assertEquals("12", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "y");
        assertEquals("123", IDE.EDITOR.getTextFromCodeEditor());

        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals("12", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor("a");
        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "y");
        assertEquals("a12", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals("12", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(UNDO_REDO_TXT);

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.REDO);
        assertEquals("a12", IDE.EDITOR.getTextFromCodeEditor());

        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "z");
        assertEquals("12", IDE.EDITOR.getTextFromCodeEditor());
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.typeTextIntoEditor("text");
        Thread.sleep(TestConstants.REDRAW_PERIOD);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.selectTab(1);
        IDE.EDITOR.waitActiveFile();
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.selectTab(2);
        IDE.EDITOR.waitActiveFile();
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.REDO);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.UNDO);
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandDisabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);

        IDE.EDITOR.forcedClosureFile(1);
        IDE.EDITOR.forcedClosureFile(1);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

}
