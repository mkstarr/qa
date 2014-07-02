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
package com.codenvy.ide.operation.autocompletion;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

/** @author Evgen Vidolob */
public class AutoCompletionCSSTest extends CodeAssistantBaseTest {

    @BeforeClass
    public static void createProject() throws Exception {
        createProject(AutoCompletionCSSTest.class.getSimpleName());
    }


    @Test
    public void testPlainCSS() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(AutoCompletionCSSTest.class.getSimpleName());
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("css_file.html");
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        cssTest("css_file.html");
    }

    @Test
    public void testHTML() throws Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("html_file.html");
        IDE.FILE.clickCreateButton();

        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.moveCursorDown(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString() + Keys.ENTER);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("<script>\n</script>");
        IDE.JAVAEDITOR.moveCursorDown(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString() + Keys.ENTER);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("<style>\n\n</style>");
        IDE.JAVAEDITOR.moveCursorUp(1);

        //cssTest("html_file.html"); use it  instead of code below when autosaving in html will work

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(".main{\n");
        // need for reparse code
        Thread.sleep(500);
        IDE.JAVAEDITOR.moveCursorUp(1);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("list-st");
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(3);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("list-style-type");
        IDE.EDITOR.closeTabIgnoringChanges("html_file.html");
    }

    private void cssTest(String fileName) throws Exception {
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(".main{\n");
        // need for reparse code
        Thread.sleep(500);
        IDE.JAVAEDITOR.moveCursorUp(1);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("list-st");
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(3);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("list-style-type");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(fileName);
    }
}
