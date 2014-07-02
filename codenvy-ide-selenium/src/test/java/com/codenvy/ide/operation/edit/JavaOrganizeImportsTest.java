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
package com.codenvy.ide.operation.edit;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.operation.autocompletion.CodeAssistantBaseTest;
import com.codenvy.ide.operation.autocompletion.JavaCodeAssistantTest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Evgen Vidolob
 *
 */
public class JavaOrganizeImportsTest extends CodeAssistantBaseTest {
    private static final String FILE_NAME = "GreetingController.java";

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
    public void organizeImportTest() throws Exception {
        // precondition
        openJavaProject();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.SHOW_SYNTAX_ERROR_HIGHLIGHTING);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.CODEASSISTANT.waitForJavaToolingInitialized(FILE_NAME);


        // step 1 check organize import

        IDE.JAVAEDITOR.waitWarningMarkerPresentInPosition(6);
        IDE.JAVAEDITOR.waitWarningMarkerPresentInPosition(7);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT.toString() + "O");
        assertThat(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor()).doesNotContain("import java.util.HashMap;")
                .doesNotContain(
                        "import java.util.Map;");

        // step 3 add two new objects
        IDE.GOTOLINE.goToLine(21);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("Array a;\n Element b;");
        // To editor parse text

        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(21);
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(22);

        // step 3 organize import for new objects
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT.toString() + "O");
        IDE.ORGINIZEIMPORT.waitForWindowOpened();
        String ss = IDE.ORGINIZEIMPORT.getTextFromImportList();
        assertThat(ss).contains("java.sql.Array").contains("java.lang.reflect.Array");
        IDE.ORGINIZEIMPORT.selectValueInImportList("java.lang.reflect.Array");

        // driver.findElement(By.id("ideOrganizeImportNext")).click();
        IDE.ORGINIZEIMPORT.nextBtnclick();
        IDE.ORGINIZEIMPORT.waitForValueInImportList("javax.lang.model.element.Element");
        IDE.ORGINIZEIMPORT.waitForValueInImportList("javax.xml.bind.Element");
        IDE.ORGINIZEIMPORT.waitForValueInImportList("org.w3c.dom.Element");
        IDE.ORGINIZEIMPORT.selectValueInImportList("org.w3c.dom.Element");
        IDE.ORGINIZEIMPORT.finishBtnclick();
        IDE.ORGINIZEIMPORT.waitForWindowClosed();
        // step 4 check complete of the organize import
        assertThat(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor()).contains("import org.w3c.dom.Element;")
                .contains(
                        "import java.lang.reflect.Array;");
    }
}
