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
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * http://jira.exoplatform.com/browse/IDE-412
 * <p/>
 * Create new file and save it. Then close and reopen file.
 *
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class ReopenJustCreatedFileTest extends BaseTest {
    private static final String PROJECT = ReopenJustCreatedFileTest.class.getSimpleName();

    private static final String FILE_NAME = "file-" + ReopenJustCreatedFileTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
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
    public void testReopenJustCreatedFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.closeFile(1);
        IDE.EDITOR.waitTabNotPresent(1);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        assertEquals(FILE_NAME, IDE.EDITOR.getTabTitle(1));
    }
}
