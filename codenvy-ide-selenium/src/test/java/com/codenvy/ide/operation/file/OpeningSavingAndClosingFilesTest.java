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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/** @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a> */
public class OpeningSavingAndClosingFilesTest extends BaseTest {

    private static String PROJECT = OpeningSavingAndClosingFilesTest.class.getSimpleName();

    private static String HTML_FILE_NAME = "newHtmlFile.html";

    private static String CSS_FILE_NAME = "newCssFile.css";

    private static String JS_FILE_NAME = "newJavaScriptFile.js";

    private static String XML_FILE_NAME = "newXMLFile.xml";

    private static String TXT_FILE_NAME = "newTextFile.txt";

    private static String DEFAULT_CONTENT_CSS_FILE =
            "/*Some example CSS*/\n\n@import url (\"something.css\")\nbody {\n  margin 0;\n  padding 3em 6em;\n  "
            +
            "font-family: tahoma, arial, sans-serif;\n  color #000;\n}\n  #navigation a {\n    font-weigt: bold;\n  "
            +
            "text-decoration: none !important;\n}\n}\n";

    private static String DEFAULT_CONTENT_HTML_FILE =
            "<html>\n" +
            "<head>\n" +
            "    <title>HTML Example</title>\n" +
            "    <script type='text/javascript'>\n" +
            "        function foo(bar, baz) {\n" +
            "        alert('quux');\n" +
            "        return bar + baz + 1;\n" +
            "        }\n" +
            "    </script>\n" +
            "    <style type='text/css'>\n" +
            "        div.border {\n" +
            "        border: 1px solid black;\n" +
            "        padding: 3px;\n" +
            "        }\n" +
            "        #foo code {\n" +
            "        font-family: courier, monospace;\n" +
            "        font-size: 80%;\n" +
            "        color: #448888;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<p>Hello</p>\n" +
            "\n" +
            "<div style=\"color: red; width: 200px; height: 35px\"></div>\n" +
            "</body>\n" +
            "</html>";

    private static String DEFAULT_CONTENT_JS_FILE =
            "//Here you see some JavaScript code. Mess around with it to get\n//acquinted with CodeMirror's features"
            +
            ".\n\n// Press enter inside the objects and your new line will \n// intended.\n\nvar keyBindings ={\n  "
            +
            "enter:\"newline-and-indent\",\n  tab:\"reindent-selection\",\n  ctrl_z: \"undo\","
            +
            "\n  ctrl_y:\"redo\"\n  };\n  var regex =/foo|bar/i;\n  function example (x){\n  var y=44.4;\n  return "
            +
            "x+y;\n  }\n";

    private static String DEFAULT_CONTENT_XML_FILE =
            "<?xml version='1.0' encoding='UTF-8'?>\n<Module>\n  <UserPref>name=\"last_location\" "
            +
            "datatype=\"hidden\"</UserPref>\n</Module>";

    private static String DEFAULT_CONTENT_TXT_FILE = "text content";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE_NAME, MimeType.TEXT_HTML, PATH + HTML_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, CSS_FILE_NAME, MimeType.TEXT_CSS, PATH + CSS_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, JS_FILE_NAME, MimeType.APPLICATION_JAVASCRIPT, PATH
                                                                                                            +


                                                                                                            JS_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, XML_FILE_NAME, MimeType.TEXT_XML, PATH + XML_FILE_NAME);
            VirtualFileSystemUtils.createFileFromLocal(link, TXT_FILE_NAME, MimeType.TEXT_PLAIN, PATH + TXT_FILE_NAME);
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

    @Test
    public void testOpeningSavingAndClosingTabsWithFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        // step 1 check all files is present, open all files, checks default
        // content.
        checkAllFilesPresent();

        // close welcome tab for easy indexing of tabs and iframes in testcase
        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.EDITOR.waitTabNotPresent(0);
        checkDefaultContentInOpenFiles();

        // step 2 change file (add string "change file" into CSS, gadget, html
        // and js file)
        IDE.EDITOR.selectTab("newCssFile.css");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("Change file\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);

        IDE.EDITOR.selectTab("newHtmlFile.html");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("Change file\n");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark("newGroovyFile.groovy");

        IDE.EDITOR.selectTab("newJavaScriptFile.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("Change file\n");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        // step 3 close all files
        IDE.EDITOR.clickCloseEditorButton(CSS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(CSS_FILE_NAME);

        IDE.EDITOR.clickCloseEditorButton(HTML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(HTML_FILE_NAME);

        IDE.EDITOR.clickCloseEditorButton(JS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(JS_FILE_NAME);

        IDE.EDITOR.clickCloseEditorButton(TXT_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(TXT_FILE_NAME);

        IDE.EDITOR.clickCloseEditorButton(XML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabNotPresent(XML_FILE_NAME);

        // step 4 reopen all files check changed and not changed file. Check
        // save button state
        driver.navigate().refresh();
        checkAllFilesPresent();
        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.EDITOR.waitTabNotPresent(0);
        checkStatusReopenedFiles();
    }

    /**
     * method is check all status (change and not change) after previous steps
     *
     * @throws Exception
     */
    private void checkStatusReopenedFiles() throws Exception {
        IDE.EXPLORER.openItem(PROJECT + "/" + CSS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        assertEquals("Change file\n" + DEFAULT_CONTENT_CSS_FILE, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        assertEquals("Change file\n" + DEFAULT_CONTENT_HTML_FILE, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + JS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        assertEquals("Change file\n" + DEFAULT_CONTENT_JS_FILE, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitActiveFile();
        IDE.TOOLBAR.waitForButtonDisabled("Save");
        IDE.EDITOR.selectTab(XML_FILE_NAME);
        assertEquals(DEFAULT_CONTENT_XML_FILE, IDE.EDITOR.getTextFromCodeEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + TXT_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitActiveFile();
        IDE.TOOLBAR.waitForButtonDisabled("Save");
        IDE.EDITOR.selectTab(TXT_FILE_NAME);
        assertEquals(DEFAULT_CONTENT_TXT_FILE, IDE.EDITOR.getTextFromCodeEditor());
    }

    /** @throws Exception */
    // TODO CHECK THIS TEST
    private void checkDefaultContentInOpenFiles() throws Exception {
        IDE.EXPLORER.openItem(PROJECT + "/" + CSS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        assertEquals(DEFAULT_CONTENT_CSS_FILE, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.selectTab(HTML_FILE_NAME);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(DEFAULT_CONTENT_HTML_FILE);

        IDE.EXPLORER.openItem(PROJECT + "/" + JS_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.selectTab(JS_FILE_NAME);
        assertEquals(DEFAULT_CONTENT_JS_FILE, IDE.JAVAEDITOR.getVisibleTextFromJavaEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.selectTab(XML_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        assertEquals(DEFAULT_CONTENT_XML_FILE, IDE.EDITOR.getTextFromCodeEditor());

        IDE.EXPLORER.openItem(PROJECT + "/" + TXT_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.selectTab(TXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        assertEquals(DEFAULT_CONTENT_TXT_FILE, IDE.EDITOR.getTextFromCodeEditor());
    }

    /** @throws Exception */
    private void checkAllFilesPresent() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/" + CSS_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + JS_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TXT_FILE_NAME);
    }

}
