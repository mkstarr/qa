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
package com.codenvy.ide.packageexplorer;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerShowApplicationLogsFromContextMenuTest extends BaseTest {
    private static final String PROJECT = "ShowLogsTestPrj";

    final static String filePath =
            "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void showApplicationLogsFromContextMenuTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.Run.RUN_APPLICATION);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.Run.STOP_APPLICATION);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.Run.SHOW_LOGS);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Run.RUN_APPLICATION);

        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(3, 120);
        assertTrue(IDE.OUTPUT.getOutputMessage(3).contains("run"));
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Run.SHOW_LOGS);
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForMessageShow(4, 120);
        assertTrue(IDE.OUTPUT.getOutputMessage(4).contains("INFO: Server startup in"));
    }
}