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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * IDE-96 Go to folder test
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Ann Zhuleva
 *
 */
public class GoToFolderTest extends BaseTest {

    private final static String PROJECT = GoToFolderTest.class.getSimpleName();

    private final static String FOLDER_1 = "GoToFolderTest1";

    private final static String FOLDER_2 = "GoToFolderTest2";

    private final static String FILE_1 = "GoToFolderTestFile1.xml";

    private final static String FILE_2 = "GoToFolderTestFile2.xml";

    private static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/file/empty.xml";
        try {
            project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FOLDER_1 + "/" + FILE_1, MimeType.TEXT_PLAIN, filePath);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FOLDER_2 + "/" + FILE_2, MimeType.TEXT_PLAIN, filePath);

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
    public void testGoToFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2);

        // Open first folder and file in it
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);

        // Select second file
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        // Go to folder and go
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GO_TO_FOLDER);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);
        IDE.EDITOR.closeFile(FILE_1);
        IDE.EDITOR.waitTabNotPresent(FILE_1);
        // close all folders, refresh Project Explorer Three. And reproduce goto
        // folder operations with
        // second file
        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_2);
        //      IDE.EXPLORER.selectItem(PROJECT);
        //      IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        //      IDE.TOOLBAR.runCommand(MenuCommands.File.REFRESH_TOOLBAR);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);

        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GO_TO_FOLDER);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EDITOR.closeFile(FILE_2);
        IDE.EDITOR.waitTabNotPresent(FILE_2);

        // Checking that refresh button works properly.
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        IDE.TOOLBAR.runCommand(MenuCommands.File.REFRESH_TOOLBAR);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);

    }

    @Test
    public void testGoToFolderSearchPanel() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);

        IDE.TOOLBAR.runCommand(MenuCommands.File.SEARCH);
        IDE.SEARCH.waitPerformSearchOpened();
        IDE.SEARCH.clickSearchButton();
        IDE.SEARCH_RESULT.waitOpened();

        IDE.SEARCH_RESULT.waitForItemByName(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.SEARCH_RESULT.openItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EDITOR.waitActiveFile();

        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GO_TO_FOLDER);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FILE_2);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + FOLDER_1 + "/" + FILE_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);

    }
}
