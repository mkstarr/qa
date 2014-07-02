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
package com.codenvy.ide.readOnlyMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

/**
 * @author Musienko Maxim
 *
 */
public class ContextMenuInProjectExplorerTest extends BaseTest {
    private static String              PROJECT   = ContextMenuInPackageExplorerTest.class.getSimpleName();
    protected static Map<String, Link> project;
    String                             valueType = String.valueOf(System.currentTimeMillis());

    @Override
    @Before
    public void start() {
    }

    @BeforeClass
    public static void before() throws Exception {

        try {
            project =
                      VirtualFileSystemUtils
                                            .importZipProject(PROJECT,
                                                              "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(WORKSPACE_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(WORKSPACE_URL);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }


    @Test
    public void checkRootFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/pom.xml");
        IDE.EXPLORER.waitForItem(PROJECT + "/src");
        IDE.EXPLORER.selectItemByRightClick(PROJECT, true);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Project.CLOSE_PROJECT);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Project.OPEN_PROJECT);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Project.PROJECT_PROPERTIES);
        assertEquals(IDE.CONTEXT_MENU.getAmountItemsInContextMenu(), 3);
    }

    // TODO can not implement invocation of the context menu in project explorer
    // @Test
    public void checkFileOpening() throws Exception {
        IDE.EXPLORER.selectItemByRightClick(PROJECT + "/src");
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled("Open");
        assertTrue(IDE.CONTEXT_MENU.getAmountItemsInContextMenu() == 1);
        IDE.CONTEXT_MENU.runCommand("Open");
        IDE.EXPLORER.waitForItem(PROJECT + "/src" + "/main");
    }

    // TODO can not implement invocation of the context menu in project explorer
    // @Test
    public void checkFOlderOpening() throws Exception {
        IDE.EXPLORER.selectItemByRightClick(PROJECT + "/pom.xml");
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled("Open");
        assertTrue(IDE.CONTEXT_MENU.getAmountItemsInContextMenu() == 1);
        IDE.CONTEXT_MENU.runCommand("Open");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

}
