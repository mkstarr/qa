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

import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Evgen Vidolob */
public class JavaCodeAssistantTest extends CodeAssistantBaseTest {
    private static final String FILE_NAME = "GreetingController.java";

    private static String shouldContainsVars = "request : HttpServletRequest\n" + "response : HttpServletResponse\n"
                                               + "userName : String\n" + "result : String\n"
                                               +
                                               "handleRequest(HttpServletRequest request, " +
                                               "HttpServletResponse response) : ModelAndView";

    private static String shuldContainsOverride = "GreetingController() - Default constructor\n"
                                                  + "toString() : String - Override method in 'Object'\n" +
                                                  "hashCode() : int - Override method in 'Object'\n"
                                                  + "finalize() : void - Override method in 'Object'\n"
                                                  + "equals(Object arg0) : boolean - Override method in 'Object'\n"
                                                  + "clone() : Object - Override method in 'Object'\n" +
                                                  "GreetingController - helloworld";

    @BeforeClass
    public static void beforeTest() throws Exception {
        try {
            createProject(JavaCodeAssistantTest.class.getSimpleName(),
                          "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/JavaTestProject.zip");
        } catch (Exception e) {
            fail("Can't create test folder");
        }
    }

    @Test
    public void testJavaCodeAssistant() throws Exception {
        openJavaProject();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);

        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.CODE_ASSISTANT_JAVA.waitForJavaToolingInitialized(FILE_NAME);


        // step 1 got line 24, open form and check data in coeassist
        IDE.GOTOLINE.goToLine(24);

        IDE.CODE_ASSISTANT_JAVA.openForm();
        String proposalsText = IDE.CODE_ASSISTANT_JAVA.getAllFormProposalsText();

        assertFalse(proposalsText.isEmpty());
        assertTrue(proposalsText.contains(shouldContainsVars));
        IDE.CODE_ASSISTANT_JAVA.closeForm();

        // step 2 type iList and check data in codeassist for List
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("List");

        // delay for reparse codeassistant
        Thread.sleep(2000);
        IDE.CODE_ASSISTANT_JAVA.openForm();

        // delay for user keypress emulation
        IDE.CODE_ASSISTANT_JAVA.waitFromImportContent("java.util");
        IDE.CODE_ASSISTANT_JAVA.selectImportProposal("java.util");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        // delay for user keypress emulation
        Thread.sleep(500);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("List<E>");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("import java.util.List;");

        IDE.JAVAEDITOR.moveCursorLeft(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.BACK_SPACE.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("String");
        IDE.JAVAEDITOR.moveCursorRight(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(" ");

        // sleep to re-parse file
        // step 3 check work cursor in codeassist and insert first value
        Thread.sleep(TestConstants.SLEEP);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        proposalsText = IDE.CODE_ASSISTANT_JAVA.getAllFormProposalsText();

        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("strings : List<java.lang.String>");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("list : List<java.lang.String>");

        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(1);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("List<String> strings");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(" = new ArrayLis");

        // step 3 open form, select first element and insert
        IDE.CODE_ASSISTANT_JAVA.openForm();
        proposalsText = IDE.CODE_ASSISTANT_JAVA.getAllFormProposalsText();

        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("ArrayList(int arg0) - java.util.ArrayList");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("ArrayList() - java.util.ArrayList");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("ArrayList(Collection arg0) - java.util.ArrayList");

        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(1);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("List<String> strings = new ArrayList");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("import java.util.ArrayList;");

        IDE.JAVAEDITOR.moveCursorLeft(3);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.BACK_SPACE.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("String");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";\n");

        // sleep to re-parse file
        // step 4 type next java-values, open form and check
        Thread.sleep(TestConstants.SLEEP);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("s");
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("strings : List<java.lang.String>");

        IDE.CODE_ASSISTANT_JAVA.selectProposalPanel();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(".clea");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";\n");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("strings.clear();");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\"test\".");
        IDE.CODE_ASSISTANT_JAVA.openForm();

        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("trim() : String - String");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("intern() : String - String");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("endsWith(String arg0) : boolean - String");

        IDE.CODE_ASSISTANT_JAVA.closeForm();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("to");
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("C");
        IDE.CODE_ASSISTANT_JAVA.selectProposalPanel();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("\"test\".toCharArray()");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.CODE_ASSISTANT_JAVA.waitForJavaToolingInitialized(FILE_NAME);

        IDE.GOTOLINE.goToLine(33);

        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.selectProposalPanel();
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(1);
        Thread.sleep(TestConstants.SLEEP);
        IDE.CODE_ASSISTANT_JAVA.moveCursorUp(1);

        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText(shuldContainsOverride);
        IDE.CODE_ASSISTANT_JAVA.doudleClickSelectedItem("toString() : String");
        // for close form
        Thread.sleep(TestConstants.SLEEP / 3);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("public String toString()");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("return super.toString();");

        // step 5 checking work cursor in codeassistant after select class
        IDE.GOTOLINE.goToLine(22);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ARROW_UP.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("System.");
        Thread.sleep(TestConstants.SLEEP / 2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.BACK_SPACE.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(".");
        Thread.sleep(TestConstants.SLEEP / 2);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("System.class");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(".");
        Thread.sleep(TestConstants.SLEEP);
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.moveCursorDown(1);
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("System.class.newInstance()");
    }
}
