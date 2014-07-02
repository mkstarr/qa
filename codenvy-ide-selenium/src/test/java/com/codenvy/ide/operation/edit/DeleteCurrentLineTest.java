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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author <a href="mailto:roman.iyvshyn@exoplatform.com">Roman Iyvshyn</a>
 *
 */
public class DeleteCurrentLineTest extends BaseTest {
    private static final String PROJECT = DeleteCurrentLineTest.class.getSimpleName();

    private static final String FILE_NAME_1 = "file-" + DeleteCurrentLineTest.class.getSimpleName();

    private static final String FILE_NAME_2 = "file-" + DeleteCurrentLineTest.class.getSimpleName() + "2";

    private static final String WAIT_APPEAR_STATUSBAR =
            "//div[@id='exoIDEStatusbar']//div[@control-id='__editor_cursor_position']//table[@class='exo-statusText" +
            "-table']//td[@class='exo-statusText-table-middle']/nobr";

    private interface Lines {
        public static final String LINE_1 = "<html>\n";

        public static final String LINE_2 = "<head>\n";

        public static final String LINE_3 = "<title> </title>\n";

        public static final String LINE_4 = "</head>\n";

        public static final String LINE_5 = "<body>\n";

        public static final String LINE_6 = "</body>\n";

        public static final String LINE_7 = "</html>\n";

        public static final String DEFAULT_TEXT = LINE_1 + LINE_2 + LINE_3 + LINE_4 + LINE_5 + LINE_6 + LINE_7;

        public static final String TEXT_LINE_1 = "line 1";
    }

    private String currentTextInEditor;

    @BeforeClass
    public static void setUp() {
        final String filePath1 = "src/test/resources/org/exoplatform/ide/operation/edit/delete-current-line.html";
        final String filePath2 = "src/test/resources/org/exoplatform/ide/operation/edit/delete-current-line.txt";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME_1, MimeType.TEXT_HTML, filePath1);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME_2, MimeType.TEXT_PLAIN, filePath2);
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
    public void deleteLine() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME_1);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        currentTextInEditor = Lines.DEFAULT_TEXT;
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        // ----- 1 -----------
        // Click on "Edit->Delete Current Line" top menu command.
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        currentTextInEditor = Lines.LINE_2 + Lines.LINE_3 + Lines.LINE_4 + Lines.LINE_5 + Lines.LINE_6 + Lines.LINE_7;
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor);

        // ----- 2 -----------
        // Press "Ctrl+D" keys.
        IDE.JAVAEDITOR.deleteLinesInEditor(1);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");

        currentTextInEditor = Lines.LINE_3 + Lines.LINE_4 + Lines.LINE_5 + Lines.LINE_6 + Lines.LINE_7;
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor);
        // ----- 3 -----------
        // Move cursor down on 2 lines
        IDE.JAVAEDITOR.moveCursorDown(2);

        // ----- 4 -----------
        // Press "Ctrl+D"
        IDE.JAVAEDITOR.deleteLinesInEditor(1);
        IDE.STATUSBAR.waitCursorPositionControl();
        currentTextInEditor = Lines.LINE_3 + Lines.LINE_4 + Lines.LINE_6 + Lines.LINE_7;
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor);
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");

        // ----- 5 -----------
        // Click on "Edit->Delete Current Line" top menu command
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        currentTextInEditor = Lines.LINE_3 + Lines.LINE_4 + Lines.LINE_7;
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor);
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");

        // ----- 6 -----------
        // Click on "Edit->Delete Current Line" top menu command
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        currentTextInEditor = Lines.LINE_3 + Lines.LINE_4.trim();
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(currentTextInEditor + "\n");
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");

        // ----- 7 -----------
        // Open empty text file
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME_2);
        IDE.EDITOR.waitActiveFile();
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");
        IDE.EDITOR.typeTextIntoEditor(Lines.TEXT_LINE_1);

        // ----- 8 -----------
        // Go to line 2 and click on "Edit->Delete Current Line" top menu
        // command
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("");

        // ----- 9 -----------
        // Press "Ctrl+D" keys
        IDE.EDITOR.deleteLinesInEditor(1);

        IDE.JAVAEDITOR.selectTab(1);

        IDE.EDITOR.selectTab(2);
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("");
        IDE.EDITOR.deleteLinesInEditor(1);
        IDE.STATUSBAR.waitCursorPositionAt("1 : 1");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("");
    }
}
