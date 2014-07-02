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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 @author Roman Iuvshin
 *
 */
public class RenameInMavenMultiModuleProjectTest extends BaseTest {

    private static final String PROJECT = RenameInMavenMultiModuleProjectTest.class.getSimpleName();

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @Test
    public void renameFolderTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();

        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectMavenMultiModuleTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Maven Multi Module Project");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClickByPath(PROJECT + "/my-webapp");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClickByPath(PROJECT + "/my-webapp/src");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClickByPath(PROJECT + "/my-webapp/src/main");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClickByPath(PROJECT + "/my-webapp/src/main/webapp");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClickByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/jsp");
        IDE.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/jsp");
        IDE.RENAME.rename("newFolderName");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/newFolderName");
    }

    @Test
    public void renameFilesTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/web.xml");
        // rename xml file
        IDE.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/web.xml");
        IDE.RENAME.rename("renamedWeb.xml");
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/my-webapp/src/main/webapp/WEB-INF/renamedWeb.xml");
        // rename jsp file
        IDE.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/my-webapp/src/main/webapp/index.jsp");
        IDE.RENAME.rename("renamedNndex.jsp");
        IDE.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/my-webapp/src/main/webapp/renamedNndex.jsp");
    }

    @Test
    public void renameJavaClassTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-lib");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("hello");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("hello");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SayHello.java");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("SayHello.java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);

        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("NewName.java");
        IDE.REFACTORING.clickRenameButton();
        IDE.REFACTORING.waitRenameFormIsClosed();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewName.java");
    }
}