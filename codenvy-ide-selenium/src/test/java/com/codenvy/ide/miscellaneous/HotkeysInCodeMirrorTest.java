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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.preferences.AbstractCustomizeHotkeys;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This test contains Thread.sleep, because we need delay after typing text or pressing hotkeys as it users do.
 *
 * @author Musienko Maxim
 * @author Oksana Vereshchaka
 */
public class HotkeysInCodeMirrorTest extends AbstractCustomizeHotkeys {
    private static final String PROJECT = HotkeysInCodeMirrorTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
        }
    }

    @After
    public void after() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
    }

    @AfterClass
    public static void tearDownTest() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testHotkeysWithinCodeEditor() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        //----- 1 ------------
        //Create new text file
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        //type text to editor
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("Text File");
        IDE.EDITOR.waitFileContentModificationMark("Untitled file.txt");
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE_AS);
        //----- 2 ------------
        //Press Ctrl+F
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "f");
        //Find-replace form appeared
        IDE.FINDREPLACE.waitOpened();
        IDE.FINDREPLACE.waitFindButtonAppeared();
        IDE.FINDREPLACE.waitFindFieldAppeared();
        IDE.FINDREPLACE.typeInFindField("abcdefghi");
        IDE.FINDREPLACE.waitCloseButtonAppeared();

        //close form
        IDE.FINDREPLACE.closeView();
        IDE.FINDREPLACE.waitClosed();

        //----- 3 ------------
        //check Ctrl+D
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitContentIsClear();

        //assertEquals("", IDE.EDITOR.getTextFromCodeEditor());

        //----- 4 ------------
        //check Ctrl+L
        //check go to line window dialog appeared
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "l");
        IDE.GOTOLINE.waitOpened();
        IDE.GOTOLINE.clickCancelButton();

    }

    @Test
    public void testCopyPasetHotkeys() throws Exception {
        IDE.selectMainFrame();
        IDE.EXPLORER.waitForItem(PROJECT);
        //----- 1 ------------
        //Create new text file
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("tetx2.txt");
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.waitActiveFile();

        //type text to editor
        final String textToEdit = "text to edit";
        IDE.EDITOR.typeTextIntoEditor(textToEdit);

        //check Ctrl+C, Ctrl+V
        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_LEFT.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "a");
        //used delay to imitate the user's typing

        //TODO Used two "Ctrl+c" because after first pressing does not copy to clipboard
        //If, maybe this problem will fixed in latest versions WebDriver, one "Ctrl+c" should remove.
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "c");
        //IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "c");

        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_DOWN.toString());

        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());

        //paste text by pressing Ctrl+V
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "v");

        //check text
        IDE.EDITOR.waitContentIsPresent(textToEdit + "\n" + textToEdit);
        assertEquals(textToEdit + "\n" + textToEdit, IDE.EDITOR.getTextFromCodeEditor());

        //----- 2 ------------
        //check Ctrl+X
        //delete all text
        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_UP.toString());
        Thread.sleep(500);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        Thread.sleep(500);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        Thread.sleep(500);

        final String textToCut = "text to cut";
        IDE.EDITOR.typeTextIntoEditor(textToCut);

        //select all text
        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_LEFT.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "a");
        Thread.sleep(500);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "x");
        Thread.sleep(500);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "v");
        Thread.sleep(500);
        IDE.EDITOR.waitContentIsPresent(textToCut);
        assertEquals(textToCut, IDE.EDITOR.getTextFromCodeEditor());
    }

    @Test
    public void testUndoRedoHotkeys() throws Exception {
        IDE.selectMainFrame();
        IDE.EXPLORER.waitForItem(PROJECT);

        //Create new text file
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("text3.txt");
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.waitActiveFile();

        final String textToRevert = "a";

        IDE.EDITOR.typeTextIntoEditor(textToRevert);
        Thread.sleep(500);
        //change text
        IDE.EDITOR.typeTextIntoEditor("5");
        Thread.sleep(500);
        IDE.EDITOR.waitContentIsPresent(textToRevert + "5");
        assertEquals(textToRevert + "5", IDE.EDITOR.getTextFromCodeEditor());
        Thread.sleep(500);
        //press Ctrl+Z
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "z");
        Thread.sleep(500);
        IDE.EDITOR.waitContentIsPresent(textToRevert);
        assertEquals(textToRevert, IDE.EDITOR.getTextFromCodeEditor());

        // press Ctrl+Y
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "y");
        IDE.EDITOR.waitContentIsPresent(textToRevert + "5");
        assertEquals(textToRevert + "5", IDE.EDITOR.getTextFromCodeEditor());
    }

}