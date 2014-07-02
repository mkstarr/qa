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
import com.codenvy.ide.TestConstants;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertTrue;

/** @author Evgen Vidolob */
public class AutoCompletionJavaScriptInContiguousEditors extends CodeAssistantBaseTest {

    private final static String PROJECT = AutoCompletionJavaScriptInContiguousEditors.class.getSimpleName();


    @BeforeClass
    public static void createProject() throws Exception {
        createProject(PROJECT);
    }


    @Test
    public void testHTML() throws InterruptedException, Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.moveCursorDown(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString() + Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("<script>\n</script>");
        IDE.JAVAEDITOR.moveCursorDown(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n<script>\n</script>\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("<style>\n</style>\n<script>\n</script>");
        IDE.JAVAEDITOR.moveCursorUp(9);
        IDE.JAVAEDITOR.moveCursorRight(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("function a () {\nreturn 1;\n}\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("var b = function() {\nreturn 2;\n}\n");
        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("a()");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("b()");
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(1);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        String textAfter = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        assertTrue(textAfter.split("b").length >= 2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString() + "\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("function topFunc(x) {");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("  // Local variables get a different colour than global ones.");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("  var localVarOfTopFunc = 44;");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(" function privateFunc1OfTopFunc() {");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("  var localVarOfPrivateFuncOfTopFunc = 1;");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("   };");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("   var privateFunc2OfTopFunc = function() {");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("   };");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("    };");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ENTER.toString());
        IDE.JAVAEDITOR.moveCursorUp(1);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("topFunc(x)");
        IDE.CODE_ASSISTANT_JAVA.checkElementNotPresent("localVarOfTopFunc");
        IDE.CODE_ASSISTANT_JAVA.checkElementNotPresent("privateFunc1OfTopFunc()");
        IDE.CODE_ASSISTANT_JAVA.checkElementNotPresent("localVarOfPrivateFuncOfTopFunc");
        IDE.CODE_ASSISTANT_JAVA.checkElementNotPresent("privateFunc2OfTopFunc()");
        IDE.CODE_ASSISTANT_JAVA.closeForm();
        IDE.JAVAEDITOR.closeTabIgnoringChanges(1);
    }


    @Test
    public void javaScriptIntoJSPEditorTest() throws Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JSP);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(3);
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor(" <script type=\"text/javascript\">\n    \n    </script>");
        IDE.EDITOR.moveCursorUp(1);


        IDE.EDITOR.typeTextIntoEditor("function a () {\nreturn 1;\n}\n");
        IDE.EDITOR.typeTextIntoEditor("var b = function() {\nreturn 2;\n}\n");
        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("a()");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("b()");
        IDE.CODEASSISTANT.moveCursorDown(1);
        IDE.CODEASSISTANT.insertSelectedItem();
        String textAfter = IDE.EDITOR.getTextFromCodeEditor();
        assertTrue(textAfter.split("b").length >= 2);
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString() + "\n");
        IDE.EDITOR.typeTextIntoEditor("function topFunc(x) {");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("  // Local variables get a different colour than global ones.");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("  var localVarOfTopFunc = 44;");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor(" function privateFunc1OfTopFunc() {");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("  var localVarOfPrivateFuncOfTopFunc = 1;");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("   };");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("   var privateFunc2OfTopFunc = function() {");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("   };");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("    };");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.moveCursorUp(1);
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("topFunc()");
        IDE.CODEASSISTANT.checkElementNotPresent("localVarOfTopFunc");
        IDE.CODEASSISTANT.checkElementNotPresent("privateFunc1OfTopFunc()");
        IDE.CODEASSISTANT.checkElementNotPresent("localVarOfPrivateFuncOfTopFunc");
        IDE.CODEASSISTANT.checkElementNotPresent("privateFunc2OfTopFunc()");
        IDE.CODEASSISTANT.closeForm();
        IDE.EDITOR.closeTabIgnoringChanges(1);
    }

}
