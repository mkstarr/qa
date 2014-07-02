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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerRenameFromContextMenuTest extends BaseTest {
    private static final String PROJECT = "RenamePrj";

    private static final String NEW_PROJECT_NAME = "javanewname";

    private static final String FILE_NAME = "index.jsp";

    private static final String NEW_FILE_NAME = "newFileName.jsp";

    final static String filePath =
            "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    private static final String SOURCE_FOLDER_NAME = "src/main/java";

    private static final String NEW_SOURCE_FOLDER_NAME = "src/main/" + NEW_PROJECT_NAME;

    private static final String MAVEN_DEPENDENCIES = "Maven Dependencies";

    private static final String REF_LIB_ITEM = "junit-3.8.1.jar";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + NEW_PROJECT_NAME).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(NEW_PROJECT_NAME);
        }
    }

    @Test
    public void renameProjectFromContextMenuTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        //rename project
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        rename(NEW_PROJECT_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_PROJECT_NAME);
    }

    @Test
    public void renameFileFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");

        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(FILE_NAME);
        rename(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
    }

    @Test
    public void renameSourceFolderFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(SOURCE_FOLDER_NAME);
        rename(NEW_PROJECT_NAME);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(35);
        IDE.EDITOR.moveCursorRight(32);
        IDE.EDITOR.typeTextIntoEditor("newname");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_SOURCE_FOLDER_NAME);
    }

    @Test
    public void renameReferencedLibrariesFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(MAVEN_DEPENDENCIES);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.File.RENAME);
        IDE.PACKAGE_EXPLORER.typeKeys(Keys.ESCAPE.toString());
        IDE.PACKAGE_EXPLORER.waitContextMenuDisappear();

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("Maven Dependencies");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(REF_LIB_ITEM);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(REF_LIB_ITEM);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuDisabled(MenuCommands.File.RENAME);
        IDE.PACKAGE_EXPLORER.typeKeys(Keys.ESCAPE.toString());
        IDE.PACKAGE_EXPLORER.waitContextMenuDisappear();
    }

    /**
     * @throws Exception
     * @throws InterruptedException
     */
    private void rename(String newName) throws Exception, InterruptedException {
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.File.RENAME);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.File.RENAME);
        IDE.RENAME.waitOpened();
        IDE.RENAME.setNewName(newName);
        IDE.RENAME.clickRenameButton();
        IDE.LOADER.waitClosed();
    }
}