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
package com.codenvy.ide.operation.browse;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * IDE-14 Opening file if some files were deleted from the same folder.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Ann Zhuleva
 *
 */
public class OpeningFilesTest extends BaseTest {
    private static final String PROJECT = OpeningFilesTest.class.getSimpleName();

    private static final String folderName = OpeningFilesTest.class.getSimpleName();

    private static final String file1Name = "File1";

    private static final String file2Name = "File2";

    private static final String file1Content = "New text file content for test.";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            VirtualFileSystemUtils.createFolder(link, folderName);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }

    @Test
    public void testDeleteFileAndOpenFromOneFolder() throws Exception {
        // open project and check
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folderName);
        IDE.EXPLORER.selectItem(PROJECT + "/" + folderName);

        // close welcome tab for easy numbered tabs and editors
        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(0);

        // create txt file. Change content
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(file1Name);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(0);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(file1Content);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folderName + "/" + file1Name);
        IDE.EDITOR.closeFile(file1Name);

        // create html file. Change content
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(file2Name);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(0);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folderName + "/" + file2Name);
        IDE.EDITOR.closeFile(0);

        // Delete second file and check
        IDE.EXPLORER.selectItem(PROJECT + "/" + folderName + "/" + file2Name);
        IDE.TOOLBAR.runCommand("Delete Item(s)...");
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + folderName + "/" + file2Name);

        // open first file and check the saved content
        IDE.EXPLORER.openItem(PROJECT + "/" + folderName + "/" + file1Name);
        IDE.EDITOR.waitActiveFile();
        assertEquals(file1Content, IDE.EDITOR.getTextFromCodeEditor());
        IDE.EDITOR.closeFile(0);

        // delete first file, delete folder and check deleting
        IDE.EXPLORER.selectItem(PROJECT + "/" + folderName);
        IDE.TOOLBAR.runCommand("Delete Item(s)...");
        IDE.DELETE.waitOpened();
        IDE.DELETE.clickOkButton();
        IDE.DELETE.waitClosed();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + folderName);
        IDE.EXPLORER.waitItemPresent(PROJECT);
    }

}
