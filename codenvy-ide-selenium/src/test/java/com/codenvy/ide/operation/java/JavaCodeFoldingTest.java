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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 @author Roman Iuvshin
 *
 */
public class JavaCodeFoldingTest extends BaseTest {
    private static final String PROJECT = JavaCodeFoldingTest.class.getSimpleName();

    private static final String FILE_CONTENT      = "/* TEST COPY RIGHT\n" +
                                                    " *\n" +
                                                    " * This is free software; you can redistribute it and/or modify " +
                                                    "it\n" +
                                                    " * under the terms of the GNU Lesser General Public License as\n" +
                                                    " * published by the Free Software Foundation; either version 2.1" +
                                                    " of\n" +
                                                    " * the License, or (at your option) any later version.\n" +
                                                    " * You should have received a copy of the GNU Lesser General " +
                                                    "Public\n" +
                                                    " * License along with this software; if not, write to the Free\n" +
                                                    " * Software Foundation, Inc., 51 Franklin St, Fifth Floor, " +
                                                    "Boston, MA\n" +
                                                    " * 02110-1301 USA, or see the FSF site: http://www.fsf.org.\n" +
                                                    " */\n" +
                                                    "package helloworld;\n" +
                                                    "\n" +
                                                    "import org.springframework.web.servlet.ModelAndView;\n" +
                                                    "import org.springframework.web.servlet.mvc.Controller;\n" +
                                                    "import javax.servlet.http.HttpServletRequest;\n" +
                                                    "import javax.servlet.http.HttpServletResponse;\n" +
                                                    "\n" +
                                                    "public class FoldingExample implements Controller\n" +
                                                    "{\n" +
                                                    "   @Override\n" +
                                                    "   public ModelAndView handleRequest(HttpServletRequest request," +
                                                    " HttpServletResponse response) throws Exception\n" +
                                                    "   {\n" +
                                                    "      String userName = request.getParameter(\"user\");\n" +
                                                    "      String result = \"\";\n" +
                                                    "      if (userName != null)\n" +
                                                    "      {\n" +
                                                    "         result = \"Hello, \" + userName + \"!\";\n" +
                                                    "      }\n" +
                                                    "      ModelAndView view = new ModelAndView(\"hello_view\");\n" +
                                                    "      view.addObject(\"greeting\", result);\n" +
                                                    "      return view;\n" +
                                                    "   }\n" +
                                                    "\n" +
                                                    "   public String hjk(HttpServletRequest request, " +
                                                    "HttpServletResponse response) throws Exception\n" +
                                                    "   {\n" +
                                                    "      ModelAndView view2 = new ModelAndView(\"hello_view2\");\n" +
                                                    "      return view2.getViewName();\n" +
                                                    "   }\n" +
                                                    "\n" +
                                                    "   static class StaticNestedClass\n" +
                                                    "   {\n" +
                                                    "      static final String str = \"blabla\";\n" +
                                                    "   }\n" +
                                                    "}\n";
    private static final String COLLAPSED_COMMENT = "/* TEST COPY RIGHT\n" +
                                                    "package helloworld;";

    private static final String COLLAPSED_IMPORTS = "package helloworld;\n" +
                                                    "\n" +
                                                    "import org.springframework.web.servlet.ModelAndView;\n" +
                                                    "\n" +
                                                    "public class FoldingExample implements Controller";

    private static final String COLLAPSED_METHOD = "   }\n" +
                                                   "\n" +
                                                   "   public String hjk(HttpServletRequest request, " +
                                                   "HttpServletResponse response) throws Exception\n" +
                                                   "\n" +
                                                   "   static class StaticNestedClass";

    private static final String COLLAPSED_NESTED_CLASS = "   }\n" +
                                                         "\n" +
                                                         "   static class StaticNestedClass\n" +
                                                         "}";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/FormatTextTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void verifyThatFoldingButtonsAppearInExpectedLinesTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("FoldingExample.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("FoldingExample.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // check that folding buttons displays in expected places
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(1);
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(14);
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(21);
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(35);
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(41);
    }

    @Test
    public void commentsFoldingTest() throws Exception {
        checkCodeFolding(COLLAPSED_COMMENT, 1);
    }

    @Test
    public void importsFoldingTest() throws Exception {
        checkCodeFolding(COLLAPSED_IMPORTS, 14);
    }

    @Test
    public void methodsFoldingTest() throws Exception {
        checkCodeFolding(COLLAPSED_METHOD, 35);
    }

    @Test
    public void nestedClassesFoldingTest() throws Exception {
        checkCodeFolding(COLLAPSED_NESTED_CLASS, 41);
    }

    private void checkCodeFolding(String collapsedCode, int lineNumber) throws Exception {
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
        IDE.JAVAEDITOR.clickOnGutterCollapseButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(collapsedCode);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(FILE_CONTENT);
        IDE.JAVAEDITOR.clickOnGutterExpandButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(collapsedCode);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
        // check expand button <-->
        IDE.JAVAEDITOR.waitExpandButtonDisappearInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.clickOnGutterCollapseButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(collapsedCode);
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(lineNumber);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(collapsedCode);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
        IDE.JAVAEDITOR.waitExpandButtonDisappearInSpecifiedLineNumber(lineNumber);
    }
}