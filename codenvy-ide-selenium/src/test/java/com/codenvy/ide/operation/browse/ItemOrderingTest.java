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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:dmitry.ndp@gmail.com">Dmytro Nochevnov</a>
 *
 */
public class ItemOrderingTest extends BaseTest {

    private static final String PROJECT = ItemOrderingTest.class.getSimpleName();

    private static final String TEST_FOLDER_1 = "folder-1";

    private static final String UPPERCASE_TEST_FOLDER_1 = "Folder-1";

    private static final String TEST_FOLDER_1_2 = "folder-1-2";

    private static final String TEST_FILE_1 = "file-1";

    private static final String UPPERCASE_TEST_FILE_1 = "File-1";

    private static final String TEST_FILE_1_2 = "file-1-2";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }

    @Test
    public void testItemOrdering() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);

        // close welcome tab for easy numbered tabs and editors
        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(0);

        // create test files
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(TEST_FILE_1_2);
        IDE.FILE.clickCreateButton();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE_1_2);
        IDE.EDITOR.closeFile(0);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(UPPERCASE_TEST_FILE_1);
        IDE.FILE.clickCreateButton();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + UPPERCASE_TEST_FILE_1);
        IDE.EDITOR.closeFile(0);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(TEST_FILE_1);
        IDE.FILE.clickCreateButton();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE_1);
        IDE.EDITOR.closeFile(0);

        // create test folders
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.FOLDER.createFolder(TEST_FOLDER_1_2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER_1_2);

        IDE.EXPLORER.selectItem(PROJECT);
        IDE.FOLDER.createFolder(UPPERCASE_TEST_FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + UPPERCASE_TEST_FOLDER_1);

        IDE.EXPLORER.selectItem(PROJECT);
        IDE.FOLDER.createFolder(TEST_FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER_1);

        // check all elements in explorer
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + TEST_FILE_1_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + UPPERCASE_TEST_FILE_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + TEST_FILE_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + TEST_FOLDER_1_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + UPPERCASE_TEST_FOLDER_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + TEST_FOLDER_1);

        // search all xml files and check
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.TOOLBAR.runCommand(MenuCommands.File.SEARCH);
        IDE.SEARCH.waitPerformSearchOpened();
        IDE.SEARCH.setMimeTypeValue(MimeType.TEXT_XML);
        IDE.SEARCH.setMimeTypeValue("\n");
        IDE.SEARCH.clickSearchButton();
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + TEST_FILE_1_2);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + UPPERCASE_TEST_FILE_1);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + TEST_FILE_1);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

}
