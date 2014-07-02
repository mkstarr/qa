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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class JavaDocTest extends BaseTest {

    private static final String PROJECT = JavaDocTest.class.getSimpleName();

    private static final String FILE_NAME = "GreetingController.java";

    private static Object[] modelAndViewJavaDoc = new String[]{
            "org.springframework.web.servlet.ModelAndView",
            "Holder for both Model and View in the web MVC framework. Note that these are entirely distinct. "
            + "This class merely holds both to make it possible for a controller to return both model and view "
            + "in a single return value.",
            "Represents a model and view returned by a handler, to be resolved by a DispatcherServlet. The "
            + "view can take the form of a String view name which will need to be resolved by a ViewResolver "
            + "object; alternatively a View object can be specified directly. The model is a Map, "
            + "allowing the use of multiple objects keyed by name. @author Rod Johnson @author Juergen Hoeller "
            + "@author Rob Harrop @see DispatcherServlet @see ViewResolver @see HandlerAdapter#handle @see org"
            + ".springframework.web.servlet.mvc.Controller#handleRequest"};

    private static Object[] stringJavaDoc =
            new String[]{
                    "java.lang.String",
                    "The String class represents character strings. All string literals in Java programs, "
                    + "such as \"abc\", are implemented as instances of this class.",
                    "Strings are constant; their values cannot be changed after they are created. String buffers "
                    + "support mutable strings. Because String objects are immutable they can be shared. For example:",
                    "    String str = \"abc\";",
                    "is equivalent to:",
                    "    char data[] = {'a', 'b', 'c'};",
                    "    String str = new String(data);",
                    "Here are some more examples of how strings can be used:",
                    "    System.out.println(\"abc\");",
                    "    String cde = \"cde\";",
                    "    System.out.println(\"abc\" + cde);",
                    "    String c = \"abc\".substring(2,3);",
                    "    String d = cde.substring(1, 2);",

                    "The class String includes methods for examining individual characters of the sequence, "
                    + "for comparing strings, for searching strings, for extracting substrings, "
                    + "and for creating a copy of a string with all characters translated to uppercase or to lowercase."
                    + " Case mapping is based on the Unicode Standard version specified by the {@link java.lang"
                    + ".Character Character} class.",

                    "The Java language provides special support for the string concatenation operator ( + ), " +
                    "and for conversion of other objects to strings. String concatenation is implemented through the"};

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/JavaTestProject.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils
                    .importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void generateJavaDocTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);

        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.GOTOLINE.goToLine(11);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("/**");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        // for reparse insertion in staging
        Thread.sleep(500);
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().contains("/**\n"));
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().contains(" * \n"));
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().contains(" */"));
    }

    @Test
    public void checkJavadocInfo() throws Exception {
        IDE.GOTOLINE.goToLine(18);
        IDE.JAVAEDITOR.moveCursorRight(12);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "q");
        IDE.JAVAEDITOR.waitJavaDocContainer();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "q");
        IDE.JAVAEDITOR
           .waitJavaDocContainerWithSpecifiedText(modelAndViewJavaDoc);
        assertThat(IDE.JAVAEDITOR.getTextFromJavaDocContainer().split("\n"))
                .contains(modelAndViewJavaDoc);
        IDE.JAVAEDITOR.setCursorToActiveJavaEditor();
        IDE.GOTOLINE.goToLine(20);
        IDE.JAVAEDITOR.moveCursorRight(9);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "q");
        IDE.JAVAEDITOR.waitJavaDocContainer();


        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText(stringJavaDoc);
    }
}
