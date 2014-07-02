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
package com.codenvy.ide.operation.edit;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author Roman Iuvshin
 *
 */
public class ShowHideLineNumbersTest extends BaseTest {
    private static final String PROJECT      = ShowHideLineNumbersTest.class.getSimpleName();
    private              String INSERT_TO_JS = "function testLine(ln) \n{\n  return ln.valueOf();\n}";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);

        } catch (Exception e) {
            fail("Cant create project ");
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
    public void showHideLineNumbersTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);

        createFileAndCheckShowHideLinenumbers(MenuCommands.New.TEXT_FILE);
        createFileAndCheckShowHideLinenumbers(MenuCommands.New.XML_FILE);
        createFileAndCheckShowHideLinenumbers(MenuCommands.New.JSP);
    }

    private void createFileAndCheckShowHideLinenumbers(String menuTitle) throws InterruptedException, Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(menuTitle);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitLineNumbersVisible();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.HIDE_LINE_NUMBERS);
        IDE.EDITOR.waitLineNumbersNotVisible();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SHOW_LINE_NUMBERS);
        IDE.EDITOR.waitLineNumbersVisible();
        IDE.EDITOR.forcedClosureFile(1);
    }
}
