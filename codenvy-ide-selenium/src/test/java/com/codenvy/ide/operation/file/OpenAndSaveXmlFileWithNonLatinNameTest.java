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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * IDE-48: Opening and Saving new XML file with non-latin name.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */
public class OpenAndSaveXmlFileWithNonLatinNameTest extends BaseTest {
    private static final String FILE_NAME = "файл";

    private static final String PROJECT = OpenAndSaveXmlFileWithNonLatinNameTest.class.getSimpleName();

    private static String XML_CONTENT = "<?xml version='1.0' encoding='UTF-8'?>\n" + "<test>\n"
                                        + "<settings>value</settings>\n" + "</test>";

    private static String XML_CONTENT_2 = "<?xml version='1.0' encoding='UTF-8'?>\n" + "<test>\n"
                                          + "<settings>param</settings>\n" + "<bean>\n" + "<name>MineBean</name>\n" +
                                          "</bean>\n" + "</test>";

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
    public void testOpenAndSaveXmlFileWithNonLatinName() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        assertEquals("Untitled file.xml", IDE.EDITOR.getTabTitle(1));

        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE_AS);

        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(XML_CONTENT);

        IDE.EDITOR.saveAs(1, FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.closeFile(FILE_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(XML_CONTENT_2);

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE);
        // File name ends with *
        assertEquals(FILE_NAME + " *", IDE.EDITOR.getTabTitle(1));

        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(FILE_NAME);

        // Save command disabled
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);
    }
}
