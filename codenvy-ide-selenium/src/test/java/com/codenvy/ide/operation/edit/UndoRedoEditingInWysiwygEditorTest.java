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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */
public class UndoRedoEditingInWysiwygEditorTest extends BaseTest {

    private final static String PROJECT = UndoRedoEditingInWysiwygEditorTest.class.getSimpleName();

    private final static String HTML_FILE = "EditFileInWysiwygEditor.html";

    private final static String HTML_FILE_2 = "EditFileInWysiwygEditor2.html";


    @BeforeClass
    public static void setUp() {
        String htmlPath = "src/test/resources/org/exoplatform/ide/operation/edit/" + HTML_FILE;
        String htmlPath2 = "src/test/resources/org/exoplatform/ide/operation/edit/" + HTML_FILE;

        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE, MimeType.TEXT_HTML, htmlPath);
            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE_2, MimeType.TEXT_HTML, htmlPath2);
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
    public void undoRedoEditingInWysiwydEditorFromEditMenu() throws Exception {

        // step 1 open project and walidation error marks
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE);
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.EDITOR.clickDesignButton();
        IDE.CK_EDITOR.typeTextIntoCkEditor("1");
        IDE.CK_EDITOR.typeTextIntoCkEditor("\n");
        IDE.CK_EDITOR.typeTextIntoCkEditor("2");
        // delay for emulation of the user input
        assertEquals("1" + "\n" + "2", IDE.CK_EDITOR.getTextFromCKEditor());

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");
        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");
        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.UNDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "");

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.REDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.REDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.REDO);
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1" + "\n" + "2");
        IDE.EDITOR.forcedClosureFile(1);
    }

    @Test
    public void undoRedoEditingFromShortKeys() throws Exception {

        // step 1 open project and walidation error marks
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE_2);
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.EDITOR.clickDesignButton();

        IDE.CK_EDITOR.typeTextIntoCkEditor("1");
        IDE.CK_EDITOR.typeTextIntoCkEditor("\n");
        IDE.CK_EDITOR.typeTextIntoCkEditor("2");

        assertEquals("1" + "\n" + "2", IDE.CK_EDITOR.getTextFromCKEditor());

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "z");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "z");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "z");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "");

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "y");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "y");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1");

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "y");
        assertEquals(IDE.CK_EDITOR.getTextFromCKEditor(), "1" + "\n" + "2");
    }
}
