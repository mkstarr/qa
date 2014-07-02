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
public class CollaborationActivityInPrjAndPkgExplorersTest extends CollaborationService {
    private static final String PROJECT = CollaborationActivityInPrjAndPkgExplorersTest.class.getSimpleName();

    protected static Map<String, Link> project;

    CollaborationActivityInProjectExplorerTest instance = new CollaborationActivityInProjectExplorerTest();

    private final String PACKAGE_NAME = "org.ua.codenvy";

    private final String NEW_CLASS_NAME = "CollabCheck";

    private final String JSP_CONTENT =
            "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>\n<html>\n  <head>\n    " +
            "<title>Summer</title>\n  </head>\n  <body>\n    <form>\n        <input type=\"text\" name=\"x\" " +
            "value=\"${x}\"/>\n        +\n        <input type=\"text\" name=\"y\" value=\"${y}\"/>\n        = " +
            "${sum}\n        <br/>\n        <input type=\"submit\" value=\"Calc\"/>\n    </form>\n  </body>\n</html>";

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
    public void checkNewPackageInExplorers() throws Exception {
        //step one run two browsers. In first browser (owner tenant) wait package explorer and expand main items
        initSecondBrowser();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        expandMainItemsInPackageExplorer();

        //step two In second (ivited user browser) wait project explorer and expand main nodes
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.PACKAGE_EXPLORER.closePackageExplorer();
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE2.EXPLORER.waitForItem(PROJECT + "/src");
        IDE2.EXPLORER.waitForItem(PROJECT + "/pom.xml");
        expandProjectInInviteBrowser();

        //step three, create new package in owner browser and check creation in two browsers
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src/main/java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitCreateNewPackageForm();
        IDE.PACKAGE_EXPLORER.typeNewPackageName(PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.clickCreateNewPackageButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PACKAGE_NAME);

        //checking creation in the browser by invited user
        IDE2.EXPLORER.waitForItem(PROJECT + "/src/main/java/org");
        IDE2.EXPLORER.openItem(PROJECT + "/src/main/java/org");
        IDE2.EXPLORER.waitForItem(PROJECT + "/src/main/java/org/ua");
        IDE2.EXPLORER.openItem(PROJECT + "/src/main/java/org/ua");

        IDE2.EXPLORER.waitForItem(PROJECT + "/src/main/java/org/ua/codenvy");
        IDE2.EXPLORER.selectItem(PROJECT + "/src/main/java/org/ua/codenvy");

        //step four create new class in the browser by invited use and check in the two browsers
        IDE2.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVA_CLASS);
        IDE2.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE2.CREATE_NEW_CLASS.waitItemIsPresentInKindList("Class");
        IDE2.CREATE_NEW_CLASS.waitItemIsPresentInPackageList("sumcontroller");
        IDE2.CREATE_NEW_CLASS.waitItemIsPresentInSourceFolderList("src/main/java");
        IDE2.CREATE_NEW_CLASS.typeClassName(NEW_CLASS_NAME);
        IDE2.CREATE_NEW_CLASS.waitCreateButtonIsEnabled();
        IDE2.CREATE_NEW_CLASS.clickCreateBtn();
        IDE2.CREATE_NEW_CLASS.waitFormIsClosed();
        IDE2.EXPLORER.waitForItem(PROJECT + "/src/main/java/org/ua/codenvy/" + "CollabCheck.java");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("org.ua.codenvy");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_CLASS_NAME + ".java");

        //delete calc file by owner and check in two browsers
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("calc.jsp");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.DELETE);
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("calc.jsp");
        IDE2.EXPLORER.waitItemNotPresent(PROJECT + "/src" + "/main" + "/webapp" + "/calc.jsp");
    }

    protected void expandMainItemsInPackageExplorer() {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SumController.java");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("calc.jsp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("WEB-INF");

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
