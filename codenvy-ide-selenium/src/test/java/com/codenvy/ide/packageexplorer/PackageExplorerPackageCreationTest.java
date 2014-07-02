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

import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerPackageCreationTest extends BaseTest {
    private static final String PROJECT = "PackageCreationPrj";

    private static final String PACKAGE_NAME = "org.codenvy";

    private static final String WRONG_PACKAGE_NAME = "ThisISWROng";

    final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void createPackageTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src/main/java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitCreateNewPackageForm();
        IDE.PACKAGE_EXPLORER.typeNewPackageName(PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.clickCreateNewPackageButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PACKAGE_NAME);

        //checking that structure was created
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT + "/src/main/java/org/codenvy");
    }

    @Test
    public void createPackageWithWrongNameTest() throws Exception {
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src/main/java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitCreateNewPackageForm();
        IDE.PACKAGE_EXPLORER.typeNewPackageName(Keys.BACK_SPACE.toString());
        IDE.PACKAGE_EXPLORER.waitEmptyNameFieldWarningInCreatePackageForm();
        IDE.PACKAGE_EXPLORER.typeNewPackageName(WRONG_PACKAGE_NAME);
        IDE.PACKAGE_EXPLORER.waitConventionWarningInCreatePackageForm();
        IDE.PACKAGE_EXPLORER.clickCreateNewPackageButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(WRONG_PACKAGE_NAME);
    }

    @Test
    public void checkCreatePackageButtonTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.waitSubCommandDisabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src/main/java");
        IDE.MENU.waitSubCommandEnabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("pom.xml");
        IDE.MENU.waitSubCommandDisabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("Referenced Libraries");
        IDE.MENU.waitSubCommandDisabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("Referenced Libraries");
        IDE.MENU.waitSubCommandDisabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src");
        IDE.MENU.waitSubCommandDisabled(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.PACKAGE);
    }
}
