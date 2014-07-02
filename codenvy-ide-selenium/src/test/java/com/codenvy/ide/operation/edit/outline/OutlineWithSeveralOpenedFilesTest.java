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
package com.codenvy.ide.operation.edit.outline;

/**
 * @author Musienko Maxim
 *
 *
 */

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class OutlineWithSeveralOpenedFilesTest extends BaseTest {

    private final static String PROJECT = OutlineWithSeveralOpenedFilesTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() throws Exception {
        VirtualFileSystemUtils.createDefaultProject(PROJECT);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    // Check, that Outline tab correctly works, when we
    // try to navigate on different tabs in editor
    @Test
    public void testOutlineWhenSeveralFilesOpen() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        // TODO after resolve issue 2155 block str 8798 can be removed
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();

        // TODO Pause for build outline tree
        // after implementation method for check ready state, should be remove
        Thread.sleep(2000);

        // no outline panel
        IDE.OUTLINE.waitOutlineTreeNotVisible();

        // 2
        // show outline
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitOutlineTreeVisible();

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JSP);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();

        // check outline present
        IDE.OUTLINE.waitOutlineTreeVisible();

        // 4
        // Close Outline tab
        IDE.OUTLINE.closeOutline();
        IDE.OUTLINE.waitClosed();
        IDE.OUTLINE.waitOutlineTreeNotVisible();
        Thread.sleep(1000);
        // 5
        // go to javascript file
        IDE.EDITOR.selectTab(1);
        IDE.EDITOR.waitActiveFile();
        IDE.OUTLINE.waitOutlineTreeNotVisible();
    }

}
