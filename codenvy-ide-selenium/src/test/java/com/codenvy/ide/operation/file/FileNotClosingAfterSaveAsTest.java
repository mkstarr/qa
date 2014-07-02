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

import java.io.IOException;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 *
 */

public class FileNotClosingAfterSaveAsTest extends BaseTest {

    private static final String PROJECT = FileNotClosingAfterSaveAsTest.class.getSimpleName();

    private static final String FILE_NAME_1 = "file-" + FileNotClosingAfterSaveAsTest.class.getSimpleName() + "-"
                                              + System.currentTimeMillis();

    private static final String FILE_NAME_2 = "file-" + FileNotClosingAfterSaveAsTest.class.getSimpleName() + "-"
                                              + System.currentTimeMillis() + "5";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {

        }
    }

    @Test
    public void testFileNotClosingAfterSaveAs() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.saveAs(1, FILE_NAME_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME_1);
        IDE.EDITOR.closeFile(1);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME_1);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("test test test");
        IDE.EDITOR.closeTabIgnoringChanges(1);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.EDITOR.saveAs(1, FILE_NAME_2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

}
