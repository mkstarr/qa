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

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerCreationContextMenuTest extends BaseTest {
    private static final String PROJECT = "CreationPrj";

    final static String filePath =
            "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    private static final String NEW_FOLDER_NAME = "newFolder";

    private static final String FILE_NAME = "file.txt";

    private static final String NEW_PACKAGE_NAME = "newpackagename";

    private static final String SOURCE_FOLDER = "src/main/java";

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
    public void createNewFolderFromContextMenuTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.New.NEW);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.New.NEW);

        IDE.MENU.clickOnNewMenuItem(MenuCommands.New.FOLDER);
        IDE.FOLDER.waitOpened();
        IDE.FOLDER.typeFolderName(NEW_FOLDER_NAME);
        IDE.FOLDER.clickCreateButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FOLDER_NAME);
    }

    @Test
    public void createNewFileFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.New.NEW);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.New.NEW);

        IDE.MENU.clickOnNewMenuItem(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE.ASK_FOR_VALUE_DIALOG.waitOpened();
        IDE.ASK_FOR_VALUE_DIALOG.setValue(FILE_NAME);
        IDE.ASK_FOR_VALUE_DIALOG.clickOkButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
    }

    @Test
    public void createNewPackageFromContextMenuTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer(SOURCE_FOLDER);
        IDE.PACKAGE_EXPLORER.waitElementInContextMenuEnabled(MenuCommands.New.NEW);
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.New.NEW);
        IDE.MENU.clickOnNewMenuItem(MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.waitCreateNewPackageForm();
        IDE.PACKAGE_EXPLORER.typeNewPackageName(NEW_PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.clickCreateNewPackageButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_PACKAGE_NAME);
    }
}