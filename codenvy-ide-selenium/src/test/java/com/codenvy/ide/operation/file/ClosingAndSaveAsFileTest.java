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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class ClosingAndSaveAsFileTest extends BaseTest {

    private static String PROJECT = ClosingAndSaveAsFileTest.class.getSimpleName();

    private static final String FILE = "testfile";

    private static final String FILE2 = "new XML file.xml";

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
    public void testClosingAndSaveAsFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(2);

        IDE.EDITOR.clickCloseEditorButton(1);

        IDE.EDITOR.selectTab(1);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE.ASK_FOR_VALUE_DIALOG.waitOpened();
        IDE.ASK_FOR_VALUE_DIALOG.setValue(FILE);
        IDE.ASK_FOR_VALUE_DIALOG.clickOkButton();
        IDE.ASK_FOR_VALUE_DIALOG.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE);

        assertTrue(IDE.EDITOR.isTabPresentInEditorTabset(1));
        assertEquals(FILE, IDE.EDITOR.getTabTitle(1));
    }

    @Test
    public void testSaveAsFileAfterTryingToCloseNewFile() throws Exception {
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE2);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(FILE2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("change");
        IDE.JAVAEDITOR.waitFileContentModificationMark(FILE2);

        IDE.EDITOR.clickCloseEditorButton(FILE2);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickNo();
        IDE.ASK_DIALOG.waitClosed();

        IDE.EDITOR.saveAs(1, "new_" + FILE2);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + "new_" + FILE2);
    }

}
