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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author Evgen Vidolob
 *
 */
public class SearchLoadFileTest extends BaseTest {

    private static final String PROJECT = SearchLoadFileTest.class.getSimpleName();

    private final static String xmlFileName = "newXMLFile.xml";

    private final static String renameFileName = "fileforrename.txt";

    private final static String TEST_FOLDER = "testFolder";

    static String pathToPlain = "src/test/resources/org/exoplatform/ide/operation/file/fileforrename.txt";

    static String pathToXml = "src/test/resources/org/exoplatform/ide/operation/file/newXMLFile.xml";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils.createFolder(link, TEST_FOLDER);

            VirtualFileSystemUtils.createFileFromLocal(linkFile, TEST_FOLDER + "/" + xmlFileName, MimeType.TEXT_XML,
                                                       pathToXml);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, TEST_FOLDER + "/" + renameFileName, MimeType.TEXT_PLAIN,
                                         pathToPlain);

        } catch (IOException e) {
        }
    }

    /**
     * IDE-33:Load found file in the Content Panel
     *
     * @throws Exception
     */
    @Test
    public void testLoadFoundFile() throws Exception {
        // step 1 open project an folders, search and check result
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.WELCOME_PAGE.close();
        IDE.WELCOME_PAGE.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER);
        IDE.EXPLORER.selectItem(PROJECT + "/" + TEST_FOLDER);
        IDE.SEARCH.performSearch("/" + PROJECT + "/" + TEST_FOLDER, "", MimeType.TEXT_XML);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + TEST_FOLDER + "/" + xmlFileName);

        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + TEST_FOLDER + "/" + xmlFileName);
        IDE.SEARCH_RESULT.openItem(PROJECT + "/" + TEST_FOLDER + "/" + xmlFileName);
        IDE.EDITOR.waitActiveFile();

        chekButtonState();

        // step 2 check GOTO folder and close file
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GO_TO_FOLDER);
        IDE.EDITOR.waitTabPresent(xmlFileName);
        IDE.EDITOR.closeFile(xmlFileName);
    }

    private void chekButtonState() throws Exception {
        IDE.TOOLBAR.waitButtonPresentAtLeft(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.DELETE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.CUT_SELECTED_ITEM);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.COPY_SELECTED_ITEM);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.PASTE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.REFRESH);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SEARCH);
        IDE.TOOLBAR.waitForButtonEnabled(MenuCommands.Edit.FORMAT);
        IDE.MENU.isTopMenuEnabled(MenuCommands.File.FILE);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }
}
