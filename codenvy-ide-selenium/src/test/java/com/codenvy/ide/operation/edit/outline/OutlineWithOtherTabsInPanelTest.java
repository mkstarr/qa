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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.vfs.shared.Link;
import org.junit.*;
import java.util.Map;
import static org.junit.Assert.fail;

/**
 * @author Musienko Maxim
 *
 */
public class OutlineWithOtherTabsInPanelTest extends BaseTest {

    private final static String TEXT_FILE_NAME = "file1.txt";

    private final static String JSP_FILE_NAME  = "file2.jsp";

    private final static String XML_FILE_NAME  = "file3.xml";

    private final static String PROJECT        = OutlineWithOtherTabsInPanelTest.class.getSimpleName();

    private static final String EMPTY_ZIP_PATH =
                                                 "src/test/resources/org/exoplatform/ide/operation/edit/outline/empty-repository.zip";

    @BeforeClass
    public static void setUp() {
        final String textFilePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/sample-text.txt";
        final String htmlFilePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/test-jsp.jsp";
        final String xmlFilePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/sample-xml.xml";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, EMPTY_ZIP_PATH);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, TEXT_FILE_NAME, MimeType.TEXT_PLAIN, textFilePath);
            VirtualFileSystemUtils.createFileFromLocal(link, XML_FILE_NAME, MimeType.TEXT_XML, xmlFilePath);
            VirtualFileSystemUtils.createFileFromLocal(link, JSP_FILE_NAME, MimeType.APPLICATION_JSP, htmlFilePath);
        } catch (Exception e) {
           // fail("Can't create folder and files");
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
    public void testOutlineWithOtherTabsInPanel() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEXT_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + JSP_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + XML_FILE_NAME);

        // Open XML file
        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        // TODO Pause for build outline tree
        // after implementation method for check ready state, should be remove
        Thread.sleep(2000);

        // Open Outline panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitOutlineTreeVisible();

        // Open JSP file
        IDE.EXPLORER.openItem(PROJECT + "/" + JSP_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.OUTLINE.waitOpened();
        // Check Outline visible
        IDE.OUTLINE.waitOutlineTreeVisible();

        // Open text file
        IDE.EXPLORER.openItem(PROJECT + "/" + TEXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.OUTLINE.waitNotAvailable();

        // Type text to file and save (create new version)
        IDE.EDITOR.typeTextIntoEditor("hello");
        IDE.EDITOR.waitFileContentModificationMark(TEXT_FILE_NAME);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(TEXT_FILE_NAME);
    }
}
