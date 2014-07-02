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
import com.codenvy.ide.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * IDE-13:Saving previously edited file.
 *
 * @author Oksana Vereshchaka
 *
 */

public class SavingPreviouslyEditedFileTest extends BaseTest {
    private static final String PROJECT = SavingPreviouslyEditedFileTest.class.getSimpleName();

    private static final String XML_FILE_NAME = "XmlFile.xml";

    private static final String TEXT_FILE_NAME = "textFile.txt";

    private static final String NEW_TEXT_FILE_NAME = "newText.txt";

    private static final String DEFAULT_XML_CONTENT = "<?xml version='1.0' encoding='UTF-8'?>";

    private final static String XML_TEXT1 = "<?xml version='1.0' encoding='UTF-8'?>\n" + "<test>\n"
                                            + "<settings>param</settings>\n<bean>\n<name>MineBean</name>\n" + "</bean>";

    private final static String XML_TEXT2 = "\n</test>";

    private static final String FORMATTED_XML_TEXT = "<?xml version='1.0' encoding='UTF-8'?>\n" + "<test>\n"
                                                     + "  <settings>param</settings>\n" + "  <bean>\n" +
                                                     "    <name>MineBean</name>\n" + "  </bean>\n" + "</test>";

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
    public void testSavePreviouslyEditedFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.EXPLORER.selectItem(PROJECT);

        // Create new XML file:
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(XML_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        assertEquals(XML_FILE_NAME, IDE.EDITOR.getTabTitle(1));
        assertEquals(DEFAULT_XML_CONTENT, IDE.EDITOR.getTextFromCodeEditor());

        IDE.EXPLORER.waitForItem(PROJECT + "/" + XML_FILE_NAME);

        IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + XML_FILE_NAME);

        IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + XML_FILE_NAME);

        IDE.EDITOR.closeFile(XML_FILE_NAME);

        // Check that the files created on the server
        Response response = VirtualFileSystemUtils.get(REST_URL + "contentbypath/" + PROJECT + "/" + XML_FILE_NAME);
        assertEquals(200, response.getStatusCode());
        assertEquals(DEFAULT_XML_CONTENT + "\n", response.getData());

        // Edit file and save it:
        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor(XML_TEXT1);
        IDE.EDITOR.typeTextIntoEditor(XML_TEXT2);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(XML_FILE_NAME);
        IDE.EDITOR.closeFile(XML_FILE_NAME);

        // Open file:
        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        assertEquals(FORMATTED_XML_TEXT, IDE.EDITOR.getTextFromCodeEditor());

        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);

        // Edit file and save
        IDE.EDITOR.typeTextIntoEditor("<root>admin</root>");

        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE);

        // add save file command because if file not saved, appear pop up window
        // with warning info see IDE-1599
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();

    }

    /**
     * Type one letter to just created and saved file.
     *
     * @throws Exception
     */
    @Test
    public void testEditAndSaveJustCreatedFile() throws Exception {
        IDE.EXPLORER.selectItem(PROJECT);

        // Open new file and save it:
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(TEXT_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEXT_FILE_NAME);

        // File is not modified:
        IDE.EDITOR.waitNoContentModificationMark(TEXT_FILE_NAME);
        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);

        // Modify file:
        IDE.EDITOR.typeTextIntoEditor("X");
        IDE.EDITOR.waitFileContentModificationMark(TEXT_FILE_NAME);

        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE);

        // add save file command because if file not saved, appear pop up window
        // with warning info see IDE-1599
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
    }

    /**
     * Bug http://jira.exoplatform.org/browse/IDE-342.
     * <p/>
     * Create, save and close file. Than open file and type one letter.
     *
     * @throws Exception
     */
    @Test
    public void testOpenEditAndSaveJustCreatedFile() throws Exception {
        IDE.EXPLORER.selectItem(PROJECT);

        // Open new file and save it:
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_TEXT_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + NEW_TEXT_FILE_NAME);

        IDE.EDITOR.closeFile(NEW_TEXT_FILE_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + NEW_TEXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        // File is not modified:
        IDE.EDITOR.waitNoContentModificationMark(NEW_TEXT_FILE_NAME);
        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.File.SAVE);

        // Modify file:
        IDE.EDITOR.typeTextIntoEditor("X");
        IDE.EDITOR.waitFileContentModificationMark(NEW_TEXT_FILE_NAME);

        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE);

        // add save file command because if file not saved, appear pop up window
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
    }
}
