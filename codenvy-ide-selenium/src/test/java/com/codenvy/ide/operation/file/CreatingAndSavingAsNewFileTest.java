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
import static org.junit.Assert.fail;

/**
 * IDE-10: Creating and "Saving As" new files.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */
public class CreatingAndSavingAsNewFileTest extends BaseTest {
    // IDE-10: Creating and "Saving As" new files.

    private static final String PROJECT = CreatingAndSavingAsNewFileTest.class.getSimpleName();

    private static final String JSP_FILE_NAME = "TestJSP.jsp";

    private static final String TXT_FILE_NAME = "TestTextFile.txt";

    private static final String XML_FILE_NAME = "TestXmlFile.xml";

    private static final String HTML_FILE_NAME = "TestHtmlFile.html";

    private static final String JS_FILE_NAME = "TestJavascriptFile.js";

    private static final String CSS_FILE_NAME = "TestCssFile.css";

    private static final String PHP_FILE_NAME = "TestPhp.php";


    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);

        } catch (Exception e) {
            fail("Cant create project ");
        }
    }

    @Test
    public void testCreatingAndSavingAsNewFiles() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);

        createFileAndSaveAs(MenuCommands.New.XML_FILE, "xml", XML_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.TEXT_FILE, "txt", TXT_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.HTML_FILE, "html", HTML_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.JAVASCRIPT_FILE, "js", JS_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.CSS_FILE, "css", CSS_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.JSP, "jsp", JSP_FILE_NAME);
        createFileAndSaveAs(MenuCommands.New.PHP, "php", PHP_FILE_NAME);

    }

    private void createFileAndSaveAs(String menuTitle, String fileExtention, String fileName)
            throws InterruptedException, Exception {
        // different wait methods for colab editor and codemiror
        if ((fileName.contains("js") && !fileName.contains("jsp")) || fileName.contains("css") ||
            fileName.contains("html") || fileName.contains("php")) {
            IDE.TOOLBAR.runCommandFromNewPopupMenu(menuTitle);
            IDE.FILE.waitCreateNewFileWindow();
            IDE.FILE.clickCreateButton();
            IDE.JAVAEDITOR.waitJavaEditorIsActive();
        } else {
            IDE.TOOLBAR.runCommandFromNewPopupMenu(menuTitle);
            IDE.FILE.waitCreateNewFileWindow();
            IDE.FILE.clickCreateButton();

            IDE.EDITOR.waitActiveFile();
        }
        IDE.EDITOR.saveAs(1, fileName);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + fileName);
        IDE.EDITOR.closeFile(fileName);
        IDE.EDITOR.waitTabNotPresent(fileName);

        IDE.EXPLORER.waitItemPresent(PROJECT + "/" + fileName);
        assertEquals(200,
                     VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/" + fileName).getStatusCode());
    }
}
