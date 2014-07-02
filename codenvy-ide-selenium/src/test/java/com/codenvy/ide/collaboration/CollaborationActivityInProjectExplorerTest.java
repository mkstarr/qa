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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Musienko Maxim */
public class CollaborationActivityInProjectExplorerTest extends CollaborationService {
    private static final String PROJECT = CollaborationActivityInProjectExplorerTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/operation/java/calc.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void checkOwnerCreateFileInProjectExplorer() throws Exception {
        //step one run two browsers, close package explorer and expand all nodes of the test projects
        initSecondBrowser();

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        IDE.LOADER.waitClosed();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW, MenuCommands.Window.ShowView.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/src");
        IDE.EXPLORER.waitForItem(PROJECT + "/pom.xml");

        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.PACKAGE_EXPLORER.closePackageExplorer();
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE2.EXPLORER.waitForItem(PROJECT + "/src");
        IDE2.EXPLORER.waitForItem(PROJECT + "/pom.xml");

        expandProjectInOwnerBrowser();
        expandProjectInInviteBrowser();

        //step 2 create new file in owner browser and check in two browsers
        IDE.EXPLORER.selectItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.typeNewFileName("owner.xml");
        IDE.FILE.clickCreateButton();
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner.xml");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner.xml");
        IDE.EDITOR.waitTabPresent("owner.xml");
        IDE.EDITOR.forcedClosureFile(1);

        //step 3 delete file in invited browser and check in two browsers
        IDE2.EXPLORER.selectItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner.xml");
        IDE2.TOOLBAR.runCommand(ToolbarCommands.File.DELETE);
        IDE2.DELETE.waitOpened();
        IDE2.DELETE.clickOkButton();
        IDE2.DELETE.waitClosed();
        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner.xml");
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner.xml");

        //step 4 create folder in owner browser and check in two browsers
        IDE.EXPLORER.selectItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.FOLDER);
        IDE.FOLDER.waitOpened();

        IDE.FOLDER.typeFolderName("owner-folder");
        IDE.FOLDER.clickCreateButton();
        IDE.FOLDER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner-folder");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner-folder");

        //step 5 delete webinf folder  in owner browser and check in two browsers
        IDE.EXPLORER.selectItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.DELETE);
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();

        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/spring.xml");
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner-folder");
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/jsp");
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/web.xml");

        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/spring.xml");
        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/owner-folder");
        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/jsp");
        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/web.xml");

        IDE2.EXPLORER.selectItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller" + "/SumController.java");
        IDE2.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE2.REFACTORING.waitRenameForm();
        IDE2.REFACTORING.typeNewName("SumControllerChekRename");
        IDE2.REFACTORING.clickRenameButton();
        IDE2.REFACTORING.waitRenameFormIsClosed();
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller"
                                  + "/SumControllerChekRename.java");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller"
                                 + "/SumControllerChekRename.java");
    }

    protected void expandProjectInOwnerBrowser() throws Exception {
        IDE.EXPLORER.openItem(PROJECT + "/src");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main");
        IDE.EXPLORER.openItem(PROJECT + "/src" + "/main");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp");

        IDE.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/java");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller");
        IDE.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller" + "/SumController.java");

        IDE.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/webapp");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/calc.jsp");

        IDE.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/jsp");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/web.xml");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/spring.xml");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");

    }

    protected void expandProjectInInviteBrowser() throws Exception {
        IDE2.EXPLORER.openItem(PROJECT + "/src");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main");
        IDE2.EXPLORER.openItem(PROJECT + "/src" + "/main");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp");

        IDE2.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/java");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller");
        IDE2.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/java" + "/sumcontroller" + "/SumController.java");

        IDE2.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/webapp");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/calc.jsp");

        IDE2.EXPLORER.openItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/jsp");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/web.xml");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF" + "/spring.xml");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src" + "/main" + "/webapp" + "/WEB-INF");

    }

}