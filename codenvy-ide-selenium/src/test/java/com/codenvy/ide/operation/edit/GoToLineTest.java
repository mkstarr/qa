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
package com.codenvy.ide.operation.edit;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.GoToLine;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:roman.iyvshyn@exoplatform.com">Roman Iyvshyn</a>
 *
 */
public class GoToLineTest extends BaseTest {
    private static String PROJECT = GoToLineTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
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
    public void goToLine() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        // Open new HTML file in editor.
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        // Open new Groovy file in editor.
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JSP);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();

        // Go to menu and click "View->Go To Line".
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.GO_TO_LINE);
        IDE.GOTOLINE.waitOpened();
        IDE.GOTOLINE.waitGoToLineViewPresent();
        assertEquals(String.format(GoToLine.RANGE_LABEL, 1, 13), IDE.GOTOLINE.getLineNumberRangeLabel());

        // Type empty value an check form (form should remain unchanged)
        IDE.GOTOLINE.typeIntoLineNumberField("");
        IDE.GOTOLINE.clickGoButton();
        IDE.GOTOLINE.waitGoToLineViewPresent();

        // Print "abc" in input field.
        IDE.GOTOLINE.typeIntoLineNumberField("abc");
        IDE.GOTOLINE.clickGoButton();
        IDE.WARNING_DIALOG.waitOpened();
        assertEquals("Can't parse line number.", IDE.WARNING_DIALOG.getWarningMessage());
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        // Type "100" (above range maximum) and click "Go" button.
        IDE.GOTOLINE.typeIntoLineNumberField("100");
        IDE.GOTOLINE.clickGoButton();
        IDE.WARNING_DIALOG.waitOpened();
        assertEquals("Line number out of range", IDE.WARNING_DIALOG.getWarningMessage());
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        // Type "2" and click "Go" button.
        IDE.GOTOLINE.typeIntoLineNumberField("2");
        IDE.GOTOLINE.clickGoButton();
        IDE.GOTOLINE.waitClosed();
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("2 : 1");

        // Select HTML file's tab.
        IDE.EDITOR.selectTab(1);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        // Go to menu and click "View->Go To Line".
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.GO_TO_LINE);
        IDE.GOTOLINE.waitOpened();
        IDE.GOTOLINE.typeIntoLineNumberField("1\n");
        IDE.GOTOLINE.waitClosed();
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        // Go to status bar - right down corner , where row and column numbers
        // are displayed, hover on them with the mouse and click on it.
        IDE.STATUSBAR.clickOnCursorPositionControl();
        IDE.GOTOLINE.waitOpened();
        assertEquals(String.format(GoToLine.RANGE_LABEL, 1, 8), IDE.GOTOLINE.getLineNumberRangeLabel());

        // Print "2" and click "Go".
        IDE.GOTOLINE.typeIntoLineNumberField("2");
        IDE.GOTOLINE.clickGoButton();
        IDE.GOTOLINE.waitClosed();

        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("2 : 1");
    }
}
