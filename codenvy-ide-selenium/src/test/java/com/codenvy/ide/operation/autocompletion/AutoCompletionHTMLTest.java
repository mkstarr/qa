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

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

/** @author Evgen Vidolob */
public class AutoCompletionHTMLTest extends CodeAssistantBaseTest {

    @BeforeClass
    public static void createProject() throws Exception {
        createProject("HtmlTestProject");

    }

    @After
    public void forceClosedTabs() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
    }


    @Test
    public void testHTMLFile() throws InterruptedException, Exception {
        IDE.OPEN.openProject("HtmlTestProject");
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.moveCursorDown(4);
        htmlTest();
    }


    @Test
    public void testJSPFile() throws InterruptedException, Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JSP);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.moveCursorDown(4);
        jspTest();
    }


    private void htmlTest() throws Exception {
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n<t");

        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(3);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("<textarea></textarea>");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("<p ");

        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(2);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("<p aria-atomic=\"\"");
    }


    private void jspTest() throws Exception {

        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor("\n<t");

        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.moveCursorDown(3);
        IDE.CODEASSISTANT.insertSelectedItem();

        IDE.EDITOR.waitContentIsPresent("<textarea></textarea>");

        IDE.EDITOR.typeTextIntoEditor("<p ");

        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.moveCursorDown(2);
        IDE.CODEASSISTANT.insertSelectedItem();

        Thread.sleep(2000);
        IDE.EDITOR.moveCursorRight(1);
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.insertSelectedItem();
        IDE.EDITOR.waitContentIsPresent("<textarea><p class=\"\"></p></textarea>");
    }


}
