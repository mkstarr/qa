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

import com.codenvy.ide.*;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */

public class FormatOfTextInTheContentPanelTest extends BaseTest {
    private static String PROJECT = FormatOfTextInTheContentPanelTest.class.getSimpleName();

    private static String FORMAT_HTML_FILE_NAME = "formating.html";

    private static String NON_FORMAT_HTML_FILE_NAME = "non-formating.html";

    private static String FORMAT_CSS_FILE_NAME = "formating.css";

    private static String NON_FORMAT_CSS_FILE_NAME = "non-formating.css";

    private static String FORMAT_JS_FILE_NAME = "formating.js";

    private static String NON_FORMAT_JS_FILE_NAME = "non-formating.js";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/formating/";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, NON_FORMAT_HTML_FILE_NAME, MimeType.TEXT_HTML, PATH
                                                                                                            +


                                                                                                            NON_FORMAT_HTML_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, NON_FORMAT_CSS_FILE_NAME, MimeType.TEXT_CSS, PATH
                                                                                                          +
                                                                                                          NON_FORMAT_CSS_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, NON_FORMAT_JS_FILE_NAME, MimeType.APPLICATION_JAVASCRIPT,
                                                       PATH + NON_FORMAT_JS_FILE_NAME);
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

    @After
    public void closeTab() {
        try {
            IDE.EDITOR.forcedClosureFile(1);
        } catch (Exception e) {
        }
    }

    @Test
    public void testFormatingHtml() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + NON_FORMAT_HTML_FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + NON_FORMAT_HTML_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);

        String postFormating = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        String formatingSource = Utils.readFileAsString(PATH + FORMAT_HTML_FILE_NAME);
        assertEquals(formatingSource + "\n", postFormating);
    }

    @Test
    public void testFormatingCss() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + NON_FORMAT_CSS_FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + NON_FORMAT_CSS_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        String postFormating = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        String formatingSource = Utils.readFileAsString(PATH + FORMAT_CSS_FILE_NAME);
        assertEquals(formatingSource + "\n", postFormating);
    }

    @Test
    public void testFormatingJS() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + NON_FORMAT_JS_FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + NON_FORMAT_JS_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        String postFormating = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        String formatingSource = Utils.readFileAsString(PATH + FORMAT_JS_FILE_NAME);
        assertEquals(formatingSource + "\n", postFormating);
    }
}
