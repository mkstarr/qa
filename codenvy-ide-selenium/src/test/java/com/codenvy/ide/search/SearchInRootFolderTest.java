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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Ann Zhuleva
 *
 */
public class SearchInRootFolderTest extends BaseTest {
    private static final String PROJECT = SearchInRootFolderTest.class.getSimpleName();

    private static final String folder1Name = "Users";

    private static final String folder2Name = "Test";

    private final String xmlFileName = "test.xml";

    private final String xmlFileMimeType = MimeType.TEXT_XML;

    private final String copyofXmlFileName = "Copy Of test.xml";

    private final String xmlFileContent =
            "<CD>\n<TITLE>Hide your heart</TITLE>\n<ARTIST>Bonnie Tyler</ARTIST>\n<COUNTRY>UK</COUNTRY>\n<COMPANY>CBS" +
            " Records</COMPANY>\n<PRICE>9.90</PRICE>\n<YEAR>1988</YEAR>\n</";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
        }
    }

    /**
     * IDE-31:Searching file from root folder test.
     *
     * @throws Exception
     */
    @Test
    public void testSearchInRootFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.FOLDER.createFolder(folder1Name);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder1Name);
        IDE.EXPLORER.selectItem(PROJECT + "/" + folder1Name);
        // Create and save
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(xmlFileName);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder1Name + "/"+xmlFileName);
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(xmlFileContent);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();

        // Create second folder
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.FOLDER.createFolder(folder2Name);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder2Name);
        IDE.EXPLORER.selectItem(PROJECT + "/" + folder2Name);
        // Save in second folder first time
        IDE.EDITOR.saveAs(1, copyofXmlFileName);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder2Name + "/" + copyofXmlFileName);

        // Save in second folder second time
        IDE.EXPLORER.selectItem(PROJECT + "/" + folder2Name);
        IDE.EDITOR.saveAs(1, xmlFileName);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder2Name + "/" + xmlFileName);

        IDE.EXPLORER.selectItem(PROJECT);

        IDE.SEARCH.performSearch("/" + PROJECT, "Hello", "text/html");
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT);

        assertEquals(1, IDE.SEARCH_RESULT.getResultCount());
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        IDE.SEARCH.performSearch("/" + PROJECT, "Bonnie Tyler", xmlFileMimeType);
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT);
        assertEquals(4, IDE.SEARCH_RESULT.getResultCount());

        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + folder1Name + "/" + xmlFileName);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + folder2Name + "/" + xmlFileName);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + folder2Name + "/" + copyofXmlFileName);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

}
