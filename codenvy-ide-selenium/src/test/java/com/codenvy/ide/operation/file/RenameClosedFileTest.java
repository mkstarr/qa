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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 *
 */

public class RenameClosedFileTest extends BaseTest {

    private static final String PROJECT = RenameClosedFileTest.class.getSimpleName();

    private static final String ORIG_FILE_NAME_1 = "fileforrename1.txt";

    private static final String ORIG_FILE_NAME_2 = "fileforrename2.txt";

    private static final String ORIG_FILE_NAME_3 = "fileforrename3.txt";

    private static final String ORIG_FILE_NAME_4 = "fileforrename4.txt";

    private static final String RENAMED_FILE_NAME_1 = "RenamedTestFile1.txt";

    private static final String RENAMED_FILE_NAME_2 = "RenamedTestFile2.xml";

    private static final String FILE_CONTENT = "file for rename";

    private static final String PATH = "src/test/resources/org/exoplatform/ide/operation/file/fileforrename.txt";

    private static final String UNKNOWN_MIME_TYPE = "unknown-mime-type/*";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, ORIG_FILE_NAME_1, MimeType.TEXT_PLAIN, PATH);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, ORIG_FILE_NAME_2, MimeType.TEXT_PLAIN, PATH);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, ORIG_FILE_NAME_3, MimeType.TEXT_PLAIN, PATH);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, ORIG_FILE_NAME_4, MimeType.TEXT_PLAIN, PATH);
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

    // IDE-121 Rename Closed File
    @Test
    public void testRenameClosedFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + ORIG_FILE_NAME_1);
        IDE.EXPLORER.selectItem(PROJECT + "/" + ORIG_FILE_NAME_1);

        IDE.RENAME.rename(RENAMED_FILE_NAME_1);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + RENAMED_FILE_NAME_1);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + RENAMED_FILE_NAME_1);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + ORIG_FILE_NAME_1);
    }

    @Test
    public void testChangeMimeType() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + ORIG_FILE_NAME_2);
        IDE.EXPLORER.selectItem(PROJECT + "/" + ORIG_FILE_NAME_2);

        IDE.RENAME.rename(null, MimeType.TEXT_XML);

        IDE.EXPLORER.openItem(PROJECT + "/" + ORIG_FILE_NAME_2);
        IDE.EDITOR.waitActiveFile();
        String textFromEditor = IDE.EDITOR.getTextFromCodeEditor();
        assertEquals(FILE_CONTENT, textFromEditor);

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.openProperties();
        assertEquals(MimeType.TEXT_XML, IDE.PROPERTIES.getContentType());
        IDE.PROPERTIES.closeProperties();
        IDE.EDITOR.closeFile(1);
    }

    /**
     * Test for opening file with unknown mime-type.
     *
     * @throws Exception
     */
    @Test
    public void testChangeMimeTypeToUnknown() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + ORIG_FILE_NAME_3);
        IDE.EXPLORER.selectItem(PROJECT + "/" + ORIG_FILE_NAME_3);

        IDE.RENAME.rename(null, UNKNOWN_MIME_TYPE);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + ORIG_FILE_NAME_3);
        IDE.EXPLORER.openItem(PROJECT + "/" + ORIG_FILE_NAME_3);
        IDE.EDITOR.waitActiveFile();
        String textFromEditor = IDE.EDITOR.getTextFromCodeEditor();
        assertEquals(FILE_CONTENT, textFromEditor);

        IDE.PROPERTIES.openProperties();
        assertEquals(UNKNOWN_MIME_TYPE, IDE.PROPERTIES.getContentType());
        IDE.LOADER.waitClosed();
        IDE.PROPERTIES.closeProperties();
        IDE.EDITOR.closeFile(1);
    }

    @Test
    public void testRenameAndChangeMimeType() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + ORIG_FILE_NAME_4);
        IDE.EXPLORER.selectItem(PROJECT + "/" + ORIG_FILE_NAME_4);

        IDE.RENAME.rename(RENAMED_FILE_NAME_2, MimeType.TEXT_XML);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + RENAMED_FILE_NAME_2);
        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + RENAMED_FILE_NAME_2);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + ORIG_FILE_NAME_4);

        IDE.EXPLORER.openItem(PROJECT + "/" + RENAMED_FILE_NAME_2);
        IDE.EDITOR.waitActiveFile();
        String textFromEditor = IDE.EDITOR.getTextFromCodeEditor();
        assertEquals(FILE_CONTENT, textFromEditor);

        IDE.PROPERTIES.openProperties();
        assertEquals(MimeType.TEXT_XML, IDE.PROPERTIES.getContentType());
        IDE.LOADER.waitClosed();
        IDE.PROPERTIES.closeProperties();
        IDE.EDITOR.closeFile(1);
    }
}
