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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class RenameOpenedFileTest extends BaseTest {

    private final static String PROJECT = RenameOpenedFileTest.class.getSimpleName();

    private final static String FILE1 = "fileforrename.txt";

    private final static String FILE2 = "Renamed Test File.txt";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/" + FILE1;

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FILE1, MimeType.TEXT_PLAIN, PATH);
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

    // IDE-81 Rename Opened File
    @Test
    public void testRenameOpenedFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE1);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.selectItem(PROJECT + "/" + FILE1);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.RENAME.waitOpened();

        // Try to rename opened file - warning message must be shown and Mime
        // Type field disabled.
        assertEquals("Can't change mime-type to opened file", IDE.RENAME.getWarningMessage());
        assertFalse(IDE.RENAME.isMimeTypeFieldEnabled());
        assertFalse(IDE.RENAME.isRenameButtonEnabled());

        IDE.RENAME.setNewName(FILE2);
        IDE.RENAME.clickRenameButton();
        IDE.RENAME.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE2);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + FILE1);

        assertEquals(404, VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FILE1).getStatusCode());
        assertEquals(200,
                     VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + FILE2.replaceAll(" ", "%20"))
                                           .getStatusCode());

        assertEquals(FILE2, IDE.EDITOR.getTabTitle(1));
        IDE.EDITOR.closeFile(1);
    }

}
