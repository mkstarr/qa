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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * IDE-156:HotKeys customization.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Musienko Maxim
 * @author Oksana Vereshchaka
 */
public class HotkeysInFCKEditorTest extends BaseTest {
    private static final String PROJECT = HotkeysInFCKEditorTest.class.getSimpleName();

    private static final String TEST_FOLDER = "CK_HotkeysFolder";

    private static final String FILE_NAME = "test.html";

    private static final String TEXT = "test_text";

    @BeforeClass
    public static void setUp() throws Exception {
        try {

            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, TEST_FOLDER);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, TEST_FOLDER + "/" + FILE_NAME, MimeType.TEXT_HTML,
                                         "src/test/resources/org/exoplatform/ide/operation/file/newHtmlFile.html");
        } catch (IOException e) {
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

    /**
     * IDE-156:HotKeys customization ----- 3-5 ------------
     *
     * @throws Exception
     */

    @Test
    public void testFormatingTextHotkeysForFCKEditor() throws Exception {
        // step one open test file, switch to ck_editor,
        // delete content (hotkey ctrl+a, press del), checking
        // press short key ctrl+z and check restore
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FOLDER);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.EDITOR.waitTabPresent(1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.CK_EDITOR.clickDesignButton();
        IDE.CK_EDITOR.waitToolsCkEditor();
        IDE.CK_EDITOR.waitIsTextPresent("Hello");
        IDE.CK_EDITOR.deleteFileContentInCKEditor();
        IDE.CK_EDITOR.getTextFromCKEditor();
        IDE.CK_EDITOR.waitIsCkEditorClear();

        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "z");
        IDE.CK_EDITOR.waitIsTextPresent("Hello");


        // check bold formating
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "a");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "b");

        IDE.CK_EDITOR.waitBoldTextPresent("Hello");
        // check bold-italic formating
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "a");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "i");

        IDE.CK_EDITOR.waitItalicBoldTextPresent("Hello");
        // check italic firmating
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "b");
        IDE.CK_EDITOR.waitItalicTextPresent("Hello");
        // check no formating
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "i");
        IDE.CK_EDITOR.waitIsTextPresent("Hello");
        assertEquals("Hello", IDE.CK_EDITOR.getTextFromCKEditor());
        IDE.EDITOR.closeTabIgnoringChanges(1);
    }

    @Test
    public void testTypicalHotkeysInFCKEditor() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.CK_EDITOR.clickDesignButton();
        IDE.CK_EDITOR.waitToolsCkEditor();
        IDE.CK_EDITOR.waitIsTextPresent("Hello");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "a");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.DELETE.toString());

        IDE.CK_EDITOR.waitIsCkEditorClear();
        IDE.CK_EDITOR.typeTextIntoCkEditor(TEXT);
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "a");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "c");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "v");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "v");
        IDE.CK_EDITOR.waitIsTextPresent(TEXT + TEXT);
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "a");
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "x");
        IDE.CK_EDITOR.waitIsCkEditorClear();
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "v");
        IDE.CK_EDITOR.waitIsTextPresent(TEXT + TEXT);
        IDE.CK_EDITOR.typeTextIntoCkEditor(Keys.CONTROL.toString() + "s");
        IDE.EDITOR.waitNoContentModificationMark(FILE_NAME);

    }
}
