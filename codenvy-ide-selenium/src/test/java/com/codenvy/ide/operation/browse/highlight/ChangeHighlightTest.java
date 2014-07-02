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
package com.codenvy.ide.operation.browse.highlight;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 *
 */
public class ChangeHighlightTest extends BaseTest {
    private static final String FILE_NAME_2 = "xmlFile";

    private static String PROJECT = ChangeHighlightTest.class.getSimpleName();

    private static String FILE_NAME = "xmlFile2";

    @BeforeClass
    public static void setUp() throws Exception {
        VirtualFileSystemUtils.createDefaultProject(PROJECT);
    }

    @Test
    public void testChangeHighlihtTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);
        assertTrue(IDE.EXPLORER.isActive());

        //close welcome page
        IDE.EDITOR.waitTabPresent(0);
        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(0);

        //Open new file:
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(0);
        IDE.EDITOR.waitActiveFile();
        assertTrue(IDE.EDITOR.isActive(0));
        assertFalse(IDE.EXPLORER.isActive());
        IDE.EDITOR.typeTextIntoEditor("test");

        //Close file:
        IDE.EDITOR.closeTabIgnoringChanges(FILE_NAME);
        IDE.EDITOR.waitTabNotPresent(0);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        assertFalse(IDE.EXPLORER.isActive());

        IDE.EXPLORER.selectItem(PROJECT);
        assertTrue(IDE.EXPLORER.isActive());

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE_NAME_2);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(0);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME_2);

        IDE.EXPLORER.selectItem(PROJECT);
        assertTrue(IDE.EXPLORER.isActive());
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

}
