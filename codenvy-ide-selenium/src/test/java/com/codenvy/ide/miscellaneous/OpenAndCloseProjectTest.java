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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertTrue;

/** @author Roman Iuvshin */
public class OpenAndCloseProjectTest extends BaseTest {
    private static final String PROJECT = OpenAndCloseProjectTest.class.getSimpleName();

    public static final String ASK_DIALOG = "All opened files will be closed.\n" +
                                            "Do you want to continue?";

    private static final String PROJECT2 = OpenAndCloseProjectTest.class.getSimpleName() + "2";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/FormatTextTest.zip";

        final String filePath2 = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

        try {
            Map<String, Link> project;
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
            project = VirtualFileSystemUtils.importZipProject(PROJECT2, filePath2);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            VirtualFileSystemUtils.delete(PROJECT2);
        } catch (Exception e) {
        }
    }

    @Test
    public void openAndReopenProjectTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT2);

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

        IDE.OPEN.openProject(PROJECT2);

        IDE.ASK_DIALOG.waitOpened();
        assertTrue(IDE.ASK_DIALOG.getQuestion().equals(ASK_DIALOG));
        IDE.ASK_DIALOG.clickYes();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SumController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SumController.java");

        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

    }

    @Test
    public void closeProjectTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);

        IDE.ASK_DIALOG.waitOpened();
        assertTrue(IDE.ASK_DIALOG.getQuestion().equals(ASK_DIALOG));
        IDE.ASK_DIALOG.clickYes();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(PROJECT2);
    }
}
