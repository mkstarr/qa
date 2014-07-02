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
package com.codenvy.ide.preferences;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;


public class FormatterTest extends BaseTest {

    private static final String PROJECT = FormatterTest.class.getSimpleName();

    private final String VISIBLE_EXO_TEXT =
            "package helloworld;\n\nimport org.springframework.web.servlet.ModelAndView;\nimport org.springframework.web.servlet.mvc" +
            ".Controller;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\nimport javax.servlet.http.HttpServletRequest;\nimport " +
            "javax.servlet.http.HttpServletResponse;\n\npublic class GreetingController implements Controller\n{\n\n   @Override\n   " +
            "public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception\n   {\n      " +
            "String userName = request.getParameter(\"user\");\n      String result = \"\";\n      if (userName != null)\n      {\n      " +
            "   result = \"Hello, \" + userName + \"!\";\n      }\n\n      ModelAndView view = new ModelAndView(\"hello_view\");\n      " +
            "view.addObject(\"greeting\", result);\n      return view;\n   }\n}\n";

    private final String VISIBLE_ECLIPSE_TEXT =
            "package helloworld;\n\nimport org.springframework.web.servlet.ModelAndView;\nimport org.springframework.web.servlet.mvc" +
            ".Controller;\n\nimport java.util.HashMap;\nimport java.util.Map;\n\nimport javax.servlet.http.HttpServletRequest;\nimport " +
            "javax.servlet.http.HttpServletResponse;\n\npublic class GreetingController implements Controller {\n\n    @Override\n    " +
            "public ModelAndView handleRequest(HttpServletRequest request,\n            HttpServletResponse response) throws Exception " +
            "{\n        String userName = request.getParameter(\"user\");\n        String result = \"\";\n        if (userName != null) " +
            "{\n            result = \"Hello, \" + userName + \"!\";\n        }\n\n        ModelAndView view = new ModelAndView" +
            "(\"hello_view\");\n        view.addObject(\"greeting\", result);\n        return view;\n    }\n}\n";

    private final String FILE_NAME = "GreetingController.java";

    private final String FOLDER_NAME = "src/main/java";

    private static final String PACKAGE_NAME = "helloworld";

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void eXoFormatterTest() throws Exception {

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Spring application.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.FORMATTER);
        IDE.FORMATTER.waitOpened();
        IDE.FORMATTER.selectCodenvyFormatter();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FOLDER_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FOLDER_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.TOOLBAR.runCommand(MenuCommands.Edit.FORMAT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(VISIBLE_EXO_TEXT);
        IDE.LOADER.waitClosed();

        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.FORMATTER);
        IDE.FORMATTER.waitOpened();
        IDE.FORMATTER.selectEclipseFormatter();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(VISIBLE_ECLIPSE_TEXT);
        IDE.LOADER.waitClosed();
    }
}
