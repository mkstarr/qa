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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static junit.framework.Assert.assertTrue;

/**
 @author Roman Iuvshin
 *
 */
public class JavaFoldSelectionTest extends BaseTest {
    private static final String PROJECT                           = JavaFoldSelectionTest.class.getSimpleName();

    private static final String FILE_CONTENT                      = "/* TEST COPY RIGHT\n" +
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

    private static final String FOLDED_PART_OF_COMMENT            = "/* TEST COPY RIGHT\n" +
                                                                    " *\n" +
                                                                    " * This is free software; you can redistribute it and/or modify it\n" +
                                                                    " * under the terms of the GNU Lesser General Public License as\n" +
                                                                    " * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, " +
                                                                    "MA\n" +
                                                                    " * 02110-1301 USA, or see the FSF site: http://www.fsf.org.\n" +
                                                                    " */";

    private static final String FOLDED_PART_OF_IMPORTS_AND_CODE   = "import org.springframework.web.servlet.ModelAndView;\n" +
                                                                    "import org.springframework.web.servlet.mvc.Controller;\n" +
                                                                    "      if (userName != null)\n" +
                                                                    "      {\n" +
                                                                    "         result = \"Hello, \" + userName + \"!\";\n" +
                                                                    "      }\n" +
                                                                    "      ModelAndView view = new ModelAndView(\"hello_view\");\n" +
                                                                    "      view.addObject(\"greeting\", result);\n" +
                                                                    "      return view;\n" +
                                                                    "   }";

    private static final String FOLDED_PART_OF_IMPORTS_AND_CODE_2 = "import org.springframework.web.servlet.ModelAndView;\n" +
                                                                    "import org.springframework.web.servlet.mvc.Controller;\n" +
                                                                    "      String userName = request.getParameter(\"user\");\n" +
                                                                    "      String result = \"\";";

    private static final String FOLDED_FEW_METHODS                = "   }\n" +
                                                                    "\n" +
                                                                    "   public String hjk(HttpServletRequest request, " +
                                                                    "HttpServletResponse response) throws Exception\n" +
                                                                    "}";

    private static final String FOLDED_PART_OF_COMMENT_AND_CODE   = "/* TEST COPY RIGHT\n" +
                                                                    " *\n" +
                                                                    " * This is free software; you can redistribute it and/or modify it\n" +
                                                                    " * under the terms of the GNU Lesser General Public License as\n" +
                                                                    " * published by the Free Software Foundation; either version 2.1 of\n"
                                                                    +
                                                                    " * the License, or (at your option) any later version.\n" +
                                                                    "      {\n" +
                                                                    "         result = \"Hello, \" + userName + \"!\";\n" +
                                                                    "      }";

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

    @After
    public void reopenEditor() throws Exception {
        IDE.EDITOR.closeFile("FoldingExample.java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("FoldingExample.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("FoldingExample.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    @Test
    public void foldAllContent() throws Exception {
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

        // select all content and fold it.
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.JAVAEDITOR.callContextMenuIntoJavaEditor();
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled("Select All");
        IDE.CONTEXT_MENU.runCommand("Select All");
        IDE.CONTEXT_MENU.waitClosed();

        IDE.JAVAEDITOR.callContextMenuIntoJavaEditor();
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled("Fold Selection");
        IDE.CONTEXT_MENU.runCommand("Fold Selection");
        IDE.CONTEXT_MENU.waitClosed();
        // check that selected text is folded and expand button appear in right line
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().equals("/* TEST COPY RIGHT\n"));
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(1);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(1);

        // expand folded content
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(1);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
    }

    @Test
    public void foldPartOfComment() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        // select fold part of the comment.
        IDE.GOTOLINE.goToLine(4);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        // select 4 lines of the comment
        for (int i = 0; i < 4; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        // fold selected lines from context menu
        IDE.JAVAEDITOR.callContextMenuIntoJavaEditor();
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled("Fold Selection");
        IDE.CONTEXT_MENU.runCommand("Fold Selection");
        IDE.CONTEXT_MENU.waitClosed();

        // check that selected text is folded and expand button appear in right line
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_COMMENT);
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(4);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(4);

        // expand folded text and check content
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(4);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
    }

    @Test
    public void foldFewMethods() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        // select and fold few methods
        IDE.GOTOLINE.goToLine(35);

        // select 4 lines of the comment
        for (int i = 0; i < 9; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FOLD_SELECTION);

        // check that selected text is folded and expand button appear in right line
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_FEW_METHODS);
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(35);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(35);

        // expand folding and check content
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(35);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
    }

    @Test
    public void foldPartOfCommentAndCode() throws Exception
    {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        // select and fold few methods
        IDE.GOTOLINE.goToLine(6);

        // select 4 lines of the comment
        for (int i = 0; i < 20; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FOLD_SELECTION);

        // check that selected text is folded and expand button appear in right line
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_COMMENT_AND_CODE);
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(6);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(6);

        // expand folding and check content
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(6);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
    }

    @Test
    public void foldPartOfImportsAndCode() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(15);

        // select 10 lines of the code include imports and part of code
        for (int i = 0; i < 10; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        // call fold selection from edit menu
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FOLD_SELECTION);

        // check that selected text is folded and expand button appear in right line
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_IMPORTS_AND_CODE);
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(15);

        // expand folded text and check content
        IDE.JAVAEDITOR.clickOnGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);

        // collapse folding and check that expand button appear
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.clickOnGutterCollapseButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_IMPORTS_AND_CODE);

        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(15);

        // expand folding
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);

        //
        // repeat same case but with different number of the selected lines
        //

        reopenForFirefox();

        IDE.GOTOLINE.goToLine(15);
        IDE.STATUSBAR.waitCursorPositionAt("15 : 1");
        // select 9 lines of the code include imports and part of code
        for (int i = 0; i < 8; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        // call fold selection from edit menu
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FOLD_SELECTION);

        // check that selected text is folded and expand button appear in right line
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_IMPORTS_AND_CODE_2);
        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(15);

        // expand folded text and check content
        IDE.JAVAEDITOR.clickOnGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);

        // collapse folding and check that expand button appear
        IDE.JAVAEDITOR.waitGutterCollapseButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.clickOnGutterCollapseButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FOLDED_PART_OF_IMPORTS_AND_CODE_2);

        IDE.JAVAEDITOR.waitGutterExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitExpandButtonInSpecifiedLineNumber(15);

        // expand folding
        IDE.JAVAEDITOR.clickOnExpandButtonInSpecifiedLineNumber(15);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT);
    }

    // TODO
    // Temporary solution for incorrect work of the 'Go to line' feature after fold selection operation in Firefox browser
    private void reopenForFirefox() throws Exception {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<" + BROWSER_COMMAND.toString());
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<" + BROWSER_COMMAND.equals(BROWSER_COMMAND.FIREFOX));

        if (BROWSER_COMMAND.equals(BROWSER_COMMAND.FIREFOX)){
            IDE.EDITOR.closeFile("FoldingExample.java");
            IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("FoldingExample.java");
            IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("FoldingExample.java");
            IDE.JAVAEDITOR.waitJavaEditorIsActive();
            IDE.PROGRESS_BAR.waitProgressBarControlClose();
        }
    }
}
