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
package com.codenvy.ide.operation.contextmenu;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Anna Shumilova
 *
 */
public class ProjectExplorerContextMenuTest extends BaseTest {
    private final static String PROJECT = ProjectExplorerContextMenuTest.class.getSimpleName();

    private static final String FOLDER1 = "folder1";

    private static final String FOLDER2 = "folder2";

    private static final String GOGLE_CHROME_LOCATOR =
            "//div[@id='ideProjectExplorerItemTreeGrid']//div[@class='ide-Tree-label' and text()='" + PROJECT + "'" +
            "]";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            VirtualFileSystemUtils.createFolder(link, FOLDER1);
            VirtualFileSystemUtils.createFolder(link, FOLDER2);
        } catch (Exception e) {
        }
    }

    /** Clear tests results. */
    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSelectItemByRightMouseClick() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();

        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER2);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER1);
        IDE.STATUSBAR.waitTextInstatusBarIsPresent(" " + PROJECT + " / " + FOLDER1);

        IDE.EXPLORER.selectItemByRightClick(PROJECT + "/" + FOLDER2);
        IDE.STATUSBAR.waitTextInstatusBarIsPresent(" " + PROJECT + " / " + FOLDER2);
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.closeContextMenu();
    }

    @Test
    public void testRootContextMenuState() throws Exception {
        driver.navigate().refresh();

        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER2);
        //need for Google Chrome browser. Because if we use IDE.EXPLORER.selectItemByRightClick(PROJECT) method
        //in Chrome browser is select FOLDER1
        if (BROWSER_COMMAND.toString().equals("*googlechrome")) {

            WebElement elem = driver.findElement(By.xpath(GOGLE_CHROME_LOCATOR));
            new Actions(driver).contextClick(elem).build().perform();
        } else {
            IDE.EXPLORER.selectItemByRightClick(PROJECT);
        }
        IDE.STATUSBAR.waitTextInstatusBarIsPresent(" " + PROJECT);
        IDE.CONTEXT_MENU.waitOpened();

        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.File.DELETE));
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled("Rename..."));
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled("Open..."));
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled("Close"));
        assertTrue(IDE.CONTEXT_MENU.isCommandEnabled("Properties..."));

        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled("Paste Item(s)"));
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.CUT_MENU));
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.COPY_MENU));
        assertFalse(IDE.CONTEXT_MENU.isCommandEnabled(MenuCommands.Edit.PASTE_MENU));
    }

    @Test
    public void testFolderContextMenuState() throws Exception {
        driver.navigate().refresh();

        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER2);

        IDE.EXPLORER.selectItemByRightClick(PROJECT + "/" + FOLDER1);
        IDE.STATUSBAR.waitTextInstatusBarIsPresent(PROJECT + " / " + FOLDER1);
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.File.DELETE);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Edit.CUT_MENU);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Edit.COPY_MENU);
        IDE.CONTEXT_MENU.waitWhileCommandIsDisabled(MenuCommands.Edit.PASTE_MENU);


        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.CUT_MENU);
        IDE.CONTEXT_MENU.waitClosed();

        IDE.EXPLORER.selectItemByRightClick(PROJECT + "/" + FOLDER2);
        IDE.STATUSBAR.waitTextInstatusBarIsPresent(PROJECT + " / " + FOLDER2);

        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.File.DELETE);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Edit.CUT_MENU);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Edit.COPY_MENU);
        IDE.CONTEXT_MENU.waitWhileCommandIsEnabled(MenuCommands.Edit.PASTE_MENU);

        IDE.CONTEXT_MENU.runCommand(MenuCommands.Edit.PASTE_MENU);
        IDE.CONTEXT_MENU.waitClosed();

        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FOLDER1);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + FOLDER2 + "/" + FOLDER1);
    }

}
