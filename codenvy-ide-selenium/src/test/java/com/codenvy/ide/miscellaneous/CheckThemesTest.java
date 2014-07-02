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

/** @author Roman Iuvshin */
public class CheckThemesTest extends BaseTest {
    private static final String PROJECT = CheckThemesTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/FormatTextTest.zip";

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
    public void changeThemeTest() throws Exception {
        IDE.EXPLORER.waitOpened();
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

        //check that default theme applied
        IDE.THEME.waitCurrentThemeIDState("Default");

        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu("Theme");

        IDE.THEME.waitApplyButtonState("false");
        IDE.THEME.waitTheme("Default");
        IDE.THEME.waitTheme("Darkula");
        IDE.THEME.selectTheme("Darkula");
        IDE.THEME.waitApplyButtonState("true");
        IDE.THEME.clickOnApplyButton();
        IDE.THEME.waitApplyButtonState("false");

        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

        // verify that darcula theme applied
        IDE.THEME.waitCurrentThemeIDState("Darkula");
    }

    @Test
    public void checkThemeAfterRefreshBrowser() throws Exception {
        // check that current theme is Darcula
        IDE.THEME.waitCurrentThemeIDState("Darkula");
        driver.navigate().refresh();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.EDITOR.waitTabPresent("FoldingExample.java");
        // check that theme still Darcula after refresh.
        IDE.THEME.waitCurrentThemeIDState("Darkula");
    }
}
