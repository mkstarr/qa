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
package com.codenvy.ide.operation.edit;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 * @author <a href="dnochevnov@exoplatform.com">Dmytro Nochevnov</a>
 *
 */
public class EditFileInWysiwygEditorTest extends BaseTest {

    // IDE-123 Edit file in WYSIWYG editor

    private final static String FILE_NAME = "EditFileInWysiwygEditor.html";

    private final static String PROJECT = EditFileInWysiwygEditorTest.class.getSimpleName();

    private final static String WARNINNG_MASSAGE = "Table height must be a number.";

    private final static String CODE_AFTER_MODIFED_TABLE_MOZILLA = "<html webdriver=\"true\">\n" + " <head>\n"
                                                                   + "  <title></title>\n" + " </head>\n" + " <body>\n"
                                                                   +
                                                                   "  <table border=\"1\" cellpadding=\"1\" " +
                                                                   "cellspacing=\"1\" style=\"height: 50px; width: " +
                                                                   "200px;\">\n"
                                                                   + "   <tbody>\n" + "    <tr>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n"
                                                                   + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n"
                                                                   + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n"
                                                                   + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n" + "     <td>\n" +
                                                                   "      &nbsp;</td>\n"
                                                                   + "    </tr>\n" + "   </tbody>\n" + "  </table>\n" +
                                                                   "  <br />\n" + " </body>\n" + "</html>\n";

    private final static String CODE_AFTER_MODIFED_TABLE = "<html>\n" + " <head>\n" + "  <title></title>\n"
                                                           + " </head>\n" + " <body>\n"
                                                           +
                                                           "  <table border=\"1\" cellpadding=\"1\" cellspacing=\"1\"" +
                                                           " style=\"height: 50px; width: 200px;\">\n"
                                                           + "   <tbody>\n" + "    <tr>\n" + "     <td>\n" +
                                                           "      &nbsp;</td>\n" + "     <td>\n" + "      &nbsp;</td>\n"
                                                           + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                           "      &nbsp;</td>\n" + "     <td>\n" + "      &nbsp;</td>\n"
                                                           + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                           "      &nbsp;</td>\n" + "     <td>\n" + "      &nbsp;</td>\n"
                                                           + "    </tr>\n" + "    <tr>\n" + "     <td>\n" +
                                                           "      &nbsp;</td>\n" + "     <td>\n" + "      &nbsp;</td>\n"
                                                           + "    </tr>\n" + "   </tbody>\n" + "  </table>\n" +
                                                           "  <br />\n" + " </body>\n" + "</html>";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/" + FILE_NAME;
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_HTML, filePath);
        } catch (IOException e) {
        }
    }

    @Test
    public void editFileInWysiwygEditor() throws Exception {

        // step one open project switch between codeeditor and ck-editor. Check
        // content
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.WELCOME_PAGE.close();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.clickDesignButton();
        assertEquals(FILE_NAME, IDE.EDITOR.getTabTitle(0).replace(" *", ""));
        IDE.CK_EDITOR.waitToolsCkEditor();
        IDE.CK_EDITOR.waitToolsCkEditor();
        IDE.CK_EDITOR.clickOnToolCkEditor("Table");
        IDE.CK_EDITOR.waitCkEditorTableOpen();
        IDE.CK_EDITOR.typeToHeightwisiwyngtable("qwe");
        IDE.CK_EDITOR.clickOkWyswygTable();
        IDE.WARNING_DIALOG.waitOpened();
        assertEquals(IDE.WARNING_DIALOG.getWarningMessage(), WARNINNG_MASSAGE);
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();
        IDE.CK_EDITOR.clickOnToolCkEditor("Table");
        IDE.CK_EDITOR.waitCkEditorTableOpen();
        IDE.CK_EDITOR.typeToHeightwisiwyngtable("50");

        // step 3 Create default table and check
        IDE.CK_EDITOR.clickOkWyswygTable();
        IDE.CK_EDITOR.switchToCkEditorIframe(1);
        isDefaultTableCreated();

        // step 4 Add row in table and check
        IDE.CK_EDITOR.clickOnCellTableCkEditor(3, 1);
        IDE.CK_EDITOR.moveCursorUp();
        IDE.CK_EDITOR.callContextMenuCellTableCkEditor(3, 1);
        IDE.selectMainFrame();
        IDE.CK_EDITOR.switchToContextMenuIframe();
        IDE.CK_EDITOR.waitContextMenu("Row");
        IDE.CK_EDITOR.moveCursorToRowContextMenu("Row");
        IDE.selectMainFrame();
        IDE.CK_EDITOR.switchToContextSubMenuIframe();
        IDE.CK_EDITOR.waitContextSubMenu("Insert Row After");
        IDE.CK_EDITOR.clickOnContextSubMenu("Insert Row After");
        IDE.selectMainFrame();
        IDE.CK_EDITOR.switchToCkEditorIframe(1);
        isInsertTableCreated();

        // step 4 select code editor and check html code
        IDE.selectMainFrame();
        IDE.JAVAEDITOR.clickSourceButton();
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();

        if (System.getProperty("browser", "GOOGLE_CHROME").equals("GOOGLE_CHROME")) {
            assertEquals(CODE_AFTER_MODIFED_TABLE, IDE.EDITOR.getTextFromCodeEditor());
        } else {
            assertEquals(CODE_AFTER_MODIFED_TABLE_MOZILLA, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());
        }

        // step 5 save the file and check table in preview panel
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Run.SHOW_PREVIEW);
        IDE.TOOLBAR.runCommand(ToolbarCommands.Run.SHOW_PREVIEW);
        IDE.PREVIEW.waitHtmlPreviewOpened();
        IDE.PREVIEW.selectPreviewHtmlIFrame();
        isInsertTableCreated();
        //IDE.selectMainFrame();
    }

    private void isDefaultTableCreated() {
        IDE.CK_EDITOR.waitTablePresent(1, 1);
        IDE.CK_EDITOR.waitTablePresent(1, 2);
        IDE.CK_EDITOR.waitTablePresent(2, 1);
        IDE.CK_EDITOR.waitTablePresent(2, 2);
        IDE.CK_EDITOR.waitTablePresent(3, 1);
        IDE.CK_EDITOR.waitTablePresent(3, 2);
    }

    private void isInsertTableCreated() {
        IDE.PREVIEW.waitTablePresent(1, 1);
        IDE.PREVIEW.waitTablePresent(1, 2);
        IDE.PREVIEW.waitTablePresent(2, 1);
        IDE.PREVIEW.waitTablePresent(2, 2);
        IDE.PREVIEW.waitTablePresent(3, 1);
        IDE.PREVIEW.waitTablePresent(3, 2);
        IDE.PREVIEW.waitTablePresent(4, 1);
        IDE.PREVIEW.waitTablePresent(4, 2);
    }

    @AfterClass
    public static void tearDown() throws Exception {

        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

}