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

import java.io.IOException;
import java.util.Map;

/** @author Iuvshyn Roman */
public class CursorPositionStatusBarTest extends BaseTest {
    private final static String PROJECT = CursorPositionStatusBarTest.class.getSimpleName();

    private final static String FILE_NAME = "CursorPositionStatusBar.html";

    @BeforeClass
    public static void setUp() {
        try {

            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, "text/html",


                                                       "src/test/resources/org/exoplatform/ide/miscellaneous/CursorPositionStatusBar.html");
        } catch (IOException e) {
        }
    }

    @Test
    public void testCursorPositionInStatusBar() throws Exception {
        // step 1 (open project and file, check first cursor position in
        // statusbar)
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitTabPresent(1);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        // step 2 move cursor on 6 position to the right and check cursor
        // position
        IDE.JAVAEDITOR.moveCursorRight(6);
        IDE.STATUSBAR.waitCursorPositionAt("1 : 7");

        // step 3 move cursor on 6 position to the down and check cursor
        // position
        IDE.JAVAEDITOR.moveCursorDown(6);
        IDE.STATUSBAR.waitCursorPositionAt("7 : 7");

        // step 4 select previous tab and check save cursor position
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.LOADER.waitClosed();
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitTabPresent(2);
        // This action is necessary for correctly position the cursor in of
        // previous tab (selectTab(2)and selectTab(1);)
        IDE.EDITOR.selectTab(2);
        IDE.EDITOR.selectTab(1);
        IDE.STATUSBAR.waitCursorPositionAt("7 : 7");

        // step 5 refresh browser and check reset cursor position

        IDE.EDITOR.selectTab(2);
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
