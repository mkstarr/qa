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

import java.util.Map;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

/**
 * IDE-54:Save All Files
 * <p/>
 * Created by The eXo Platform SAS.
 * 
 * @author Oksana Vereshchaka
 *
 */
public class SaveAllFilesTest extends BaseTest
{

    private static final String PROJECT   = SaveAllFilesTest.class.getSimpleName();

    private static final String FOLDER_1  = "SaveAllFilesTest-1";

    private static final String FOLDER_2  = "SaveAllFilesTest-2";

    private static final String SAVED_XML = "Saved_File.xml";

    private static final String SAVED_PHP = "Saved File.php";

    private static final String NEW_HTML  = "Untitled_file.html";

    private static final String NEW_TEXT  = "Untitled_file.txt";

    @BeforeClass
    public static void setUp()
    {
        try
        {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2);
        } catch (Exception e)
        {
        }
    }

    @AfterClass
    public static void tearDown()
    {
        try
        {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e)
        {
        }
    }

    @Test
    public void saveAllFiles() throws Exception
    {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        String pathFolder1 = PROJECT + "/" + FOLDER_1;
        IDE.EXPLORER.waitForItem(pathFolder1);
        String pathFolder2 = PROJECT + "/" + FOLDER_2;
        IDE.EXPLORER.waitForItem(pathFolder2);

        /*
         * 1. Create file "Saved File.xml" in "Folder1"
         */
        IDE.EXPLORER.selectItem(pathFolder1);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(SAVED_XML);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();

        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);

        String pathSavedXml = pathFolder1 + "/" + SAVED_XML;

        IDE.EXPLORER.waitForItem(pathSavedXml);
        IDE.EDITOR.closeFile(SAVED_XML);

        /*
         * 2. Create "Saved File.php" in "Folder2"
         */
        IDE.EXPLORER.selectItem(pathFolder2);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.PHP);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(SAVED_PHP);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);

        String pathSavedPhp = pathFolder2 + "/" + SAVED_PHP;
        IDE.EXPLORER.waitForItem(pathSavedPhp);
        IDE.EDITOR.closeFile(SAVED_PHP);

        /*
         * 3. Save All command must be disabled
         */
        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        /*
         * 4. Save All command must be disabled
         */
        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);

        /*
         * 5. Open and change content of files "Saved File.xml" and "Saved File.php"
         */
        IDE.EXPLORER.openItem(pathSavedXml);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("<root>admin</root>");

        IDE.EXPLORER.openItem(pathSavedPhp);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("//changed content of file");

        /*
         * 6. Save All command must be enabled.
         */
        IDE.MENU.waitCommandEnabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);

        /*
         * 7. Run command "Save All" from menu
         */
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        IDE.LOADER.waitClosed();
        /*
         * 8. Files "Untitled file.html" and "Untitled file.txt" must have marker "*" in editor Files "Saved File.xml" and "Saved File.php"
         * must be without marker "*" in editor.
         */
        IDE.EXPLORER.selectItem(pathFolder1);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_HTML);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitNoContentModificationMark(NEW_HTML);

        IDE.EXPLORER.selectItem(pathFolder1);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor("change in the file");
        IDE.EDITOR.typeTextIntoEditor("ping");
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("pong");


        IDE.EDITOR.waitNoContentModificationMark(NEW_HTML);
        IDE.EDITOR.waitFileContentModificationMark("Untitled file.txt");
        IDE.EDITOR.waitNoContentModificationMark(SAVED_XML);
        IDE.EDITOR.waitNoContentModificationMark(SAVED_PHP);
        /*
         * 9. Close "Untitled file.html" 
         */
        IDE.EXPLORER.selectItem(pathFolder1);
        IDE.EDITOR.selectTab(1);
        IDE.EDITOR.closeFile(NEW_HTML);

        /*
         * 10. Save "Untitled file.txt" to Folder2
         */
       
        IDE.EXPLORER.selectItem(pathFolder2);
        IDE.EDITOR.selectTab(3);
        IDE.EDITOR.saveAs(3, NEW_TEXT);
        String pathTxt = pathFolder2 + "/" + NEW_TEXT;
        IDE.EXPLORER.waitForItem(pathTxt);
        IDE.EDITOR.closeFile(NEW_TEXT);

        /*
         * 11. Open "Untitled file.groovy" and "Untitled file.xml"
         */
        IDE.EXPLORER.openItem(pathFolder1+"/"+NEW_HTML);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.openItem(pathTxt);
        IDE.EDITOR.waitActiveFile();
        IDE.EXPLORER.openItem(pathTxt);


        /*
         * 12. Now Save As command must be disabled and all files in editor must does not have a marker "*"
         */
        IDE.MENU.waitCommandDisabled(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        IDE.EDITOR.waitNoContentModificationMark(SAVED_XML);
        IDE.EDITOR.waitNoContentModificationMark(SAVED_PHP);
        IDE.EDITOR.waitNoContentModificationMark(NEW_HTML);
        IDE.EDITOR.waitNoContentModificationMark(NEW_TEXT);

        IDE.EDITOR.closeFile(SAVED_XML);
        IDE.EDITOR.closeFile(SAVED_PHP);
        IDE.EDITOR.closeFile(NEW_HTML);
        IDE.EDITOR.closeFile(NEW_TEXT);
    }
}
