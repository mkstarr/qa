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
package com.codenvy.ide.search;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Evgen Vidolob
 *
 */
public class SearchAdvancedTest extends BaseTest {
    private static final String PROJECT = SearchAdvancedTest.class.getSimpleName();

    private static final String fileName = "TheTestFile.xml";

    private final String fileContent =

            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
            +
            "<note>\n<to>Tove</to>\n<from>Jani</from>\n<heading>Reminder</heading>\n<body>Don\'t forget me this " +
            "weekend!</body>\n</note>";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
        }
    }

    /**
     * IDE-34:Advanced search test.
     *
     * @throws Exception
     */
    @Test
    public void testAdvancedSearch() throws Exception {
        //step1
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(fileName);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(fileContent);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + fileName);
        IDE.EXPLORER.selectItem(PROJECT);

        // Step 2
        IDE.SEARCH.performSearch("/" + PROJECT, "text");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT);
        IDE.SEARCH_RESULT.waitItemNotPresent(PROJECT + "/" + fileContent);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 3
        IDE.SEARCH.performSearch("/" + PROJECT, "", "text/javascript");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT);
        IDE.SEARCH_RESULT.waitItemNotPresent(PROJECT + "/" + fileContent);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 4
        IDE.SEARCH.performSearch("/" + PROJECT, "Reminder", "text/javascript");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT);
        IDE.SEARCH_RESULT.waitItemNotPresent(PROJECT + "/" + fileContent);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 5
        IDE.SEARCH.performSearch("/" + PROJECT, "");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + fileName);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 6
        IDE.SEARCH.performSearch("/" + PROJECT, "Reminder");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + fileName);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 7
        IDE.SEARCH.performSearch("/" + PROJECT, "", "text/xml");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + fileName);
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        // Step 8
        IDE.SEARCH.performSearch("/" + PROJECT, "Reminder", "text/xml");
        IDE.LOADER.waitClosed();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + fileName);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }
}
