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
import org.openqa.selenium.Keys;

import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerCopyAndPasteFromContextMenuTest extends BaseTest {

    private static final String PROJECT = "CopyPastePrj";

    private static final String FILE_NAME = "index.jsp";

    final static String filePath =
            "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    private static final String SOURCE_FOLDER_NAME = "src/main/java";

    private static final String FOLDER_NAME = "WEB-INF";

    private static final String PACKAGE_NAME = "helloworld";

    private static final String MAVEN_DEPENDENCIES = "Maven Dependencies";

    private static final String MAVEN_DEPENDENCIES_ITEM = "junit-3.8.1.jar";

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
    public void copyFileFromContextMenuTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        openFolders();
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(FILE_NAME);
        copy();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.Edit.PASTE_MENU);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Edit.PASTE_MENU);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + FILE_NAME);
    }

    @Test
    public void copyFolderFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(FOLDER_NAME);
        copy();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.Edit.PASTE_MENU);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Edit.PASTE_MENU);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + FOLDER_NAME);
    }

    @Test
    public void copyPackageFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SOURCE_FOLDER_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(SOURCE_FOLDER_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PACKAGE_NAME);
        copy();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(SOURCE_FOLDER_NAME);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.Edit.PASTE_MENU);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Edit.PASTE_MENU);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + PACKAGE_NAME);
    }

    @Test
    public void copyReferencedLibrariesFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(MAVEN_DEPENDENCIES);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(MAVEN_DEPENDENCIES);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.Edit.COPY_MENU);
        IDE.PACKAGE_EXPLORER.typeKeys(Keys.ESCAPE.toString());
        IDE.PACKAGE_EXPLORER.waitContextMenuDisappear();

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(MAVEN_DEPENDENCIES);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(MAVEN_DEPENDENCIES_ITEM);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.Edit.COPY_MENU);
        IDE.PACKAGE_EXPLORER.typeKeys(Keys.ESCAPE.toString());
        IDE.PACKAGE_EXPLORER.waitContextMenuDisappear();
    }

    /** open folders */
    private void openFolders() {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
    }

    /**
     * @throws Exception
     * @throws InterruptedException
     */
    private void copy() throws Exception {
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.Edit.COPY_MENU);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.Edit.COPY_MENU);
        IDE.LOADER.waitClosed();
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.PASTE_TOOLBAR);
    }
}