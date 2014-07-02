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
package com.codenvy.ide.operation.browse;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.operation.java.ServicesJavaTextFuction;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Tests for Show/Hide Hidden Files command.
 *
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 *
 */
public class ShowHideHiddenFilesTest extends BaseTest {
    private static final String PROJECT = "test9";

    private static final String HIDDEN_FILE_NAME = ".htaccess";

    private ServicesJavaTextFuction serv = new ServicesJavaTextFuction();

    @BeforeClass
    public static void setUp() {
        try {
            final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

            try {
                Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
            } catch (Exception e) {
            }

        } catch (Exception e) {
            fail("Cant create project ");
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
    public void showHideHiddenFilesTest() throws Exception {


        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        serv.expandAllNodesForCalcInPackageExplorer();


        // create new text file
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(HIDDEN_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.LOADER.waitClosed();
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_HIDDEN_FILES);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(HIDDEN_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_HIDDEN_FILES);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(HIDDEN_FILE_NAME);
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_HIDDEN_FILES);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp/" + HIDDEN_FILE_NAME);
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_HIDDEN_FILES);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + "src" + "/" + "main" + "/" + "webapp/" + HIDDEN_FILE_NAME);

    }

}
