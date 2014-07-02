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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

/**
 * @author Roman Iuvshin
 *
 */
public class ShowViewTest extends BaseTest {

    private static final String PROJECT = ShowViewTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() throws Exception {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @Test
    public void hideAndShowDifferentViews() throws Exception {
        //package explorer
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();

        //project explorer
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PROJECT_EXPLORER);
        IDE.EXPLORER.waitClosed();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PROJECT_EXPLORER);
        IDE.EXPLORER.waitOpened();

        //close all views
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PROJECT_EXPLORER);
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PACKAGE_EXPLORER);

        //check that all views are closed
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitClosed();
        IDE.NAVIGATOR.waitNavigatorClosed();

        //open all views and check that it opened
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PROJECT_EXPLORER);
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW,
                            MenuCommands.Window.ShowView.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
    }

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }
}
