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

import com.codenvy.ide.*;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DeleteSeveralFilesSimultaniouslyTest extends BaseTest {

    private static String PROJECT = DeleteSeveralFilesSimultaniouslyTest.class.getSimpleName();

    private static String HTML_FILE_NAME = "newHtmlFile.html";

    private static String CSS_FILE = "newCssFile.css";

    private static String XML_FILE_NAME = "newXMLFile.xml";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/";

    /**
     *
     */
    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE_NAME, MimeType.TEXT_HTML, PATH + HTML_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, CSS_FILE, MimeType.TEXT_CSS, PATH
                                                                                                        +


                                                                                                        CSS_FILE);
            VirtualFileSystemUtils
                    .createFileFromLocal(link, XML_FILE_NAME, MimeType.APPLICATION_XML, PATH + XML_FILE_NAME);
        } catch (Exception e) {
        }
    }

    /**
     *
     */
    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    /** @throws Exception */
    @Test
    public void testDeleteSeveralFilesSimultaniously() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + CSS_FILE);

        IDE.EXPLORER.selectItem(PROJECT + "/" + CSS_FILE);
        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.DELETE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.DELETE);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + CSS_FILE);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + CSS_FILE)
                                                .getStatusCode());

        IDE.EXPLORER.selectItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.DELETE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.DELETE);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + XML_FILE_NAME);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + XML_FILE_NAME)
                                                .getStatusCode());

        IDE.EXPLORER.selectItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.DELETE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.DELETE);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + HTML_FILE_NAME);
        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + HTML_FILE_NAME)
                                                .getStatusCode());
    }
}
