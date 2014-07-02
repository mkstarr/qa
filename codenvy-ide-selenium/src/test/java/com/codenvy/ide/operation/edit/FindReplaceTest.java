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
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Ann Zhuleva
 *
 */
public class FindReplaceTest extends BaseTest {
    private final static String FILE_CONTENT_FOR_FIND    = "html";

    private final static String FILE_CONTENT_FOR_REPLACE = "testtag";

    private static final String FILE_NAME_HTML           = "findReplace.html";

    private final static String PROJECT                  = FindReplaceTest.class.getSimpleName();

    private final static String HTML_FILE_CONTENT        = "<html>\n" + "<head>\n" + "<title></title>\n" + "</head>\n"
                                                           + "<body>\n" + "</body>\n" + "</html>";

    @BeforeClass
    public static void beforeTest() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFile(link, FILE_NAME_HTML, MimeType.TEXT_HTML, HTML_FILE_CONTENT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testFindReplace() throws Exception {
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME_HTML);

        // Open file
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME_HTML);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.Editor.FIND_REPLACE);
        IDE.TOOLBAR.runCommand(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.waitOpened();
        IDE.FINDREPLACE.waitReplaceButtonDisabled();
        IDE.FINDREPLACE.waitReplaceFindButtonDisabled();
        IDE.FINDREPLACE.waitFindButtonDisabled();
        IDE.FINDREPLACE.waitReplaceAllButtonDisabled();
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);
        // Print "html" word in "Find" field and click "Find"
        IDE.FINDREPLACE.typeInFindField(FILE_CONTENT_FOR_FIND);

        IDE.FINDREPLACE.waitFindButtonEnabled();
        IDE.FINDREPLACE.waitReplaceAllButtonEnabled();
        IDE.FINDREPLACE.waitReplaceButtonDisabled();
        IDE.FINDREPLACE.waitReplaceFindButtonDisabled();

        IDE.FINDREPLACE.clickFindButton();
        // Check buttons enabled
        IDE.FINDREPLACE.waitFindButtonEnabled();
        IDE.FINDREPLACE.waitReplaceAllButtonEnabled();
        IDE.FINDREPLACE.waitReplaceButtonEnabled();
        IDE.FINDREPLACE.waitReplaceFindButtonEnabled();
        IDE.FINDREPLACE.waitFindResultEmpty();

        IDE.FINDREPLACE.typeInReplaceField(FILE_CONTENT_FOR_REPLACE);
        IDE.FINDREPLACE.clickReplaceAllButton();
        // check that buttons are disabled after clicking on replace button.

        IDE.FINDREPLACE.waitFindButtonEnabled();
        IDE.FINDREPLACE.waitReplaceAllButtonEnabled();
        IDE.FINDREPLACE.waitReplaceButtonDisabled();
        IDE.FINDREPLACE.waitReplaceFindButtonDisabled();

        // check that all tags was replaced
        IDE.FINDREPLACE.clickFindButton();
        IDE.FINDREPLACE.waitFindResultNotFound();

        // check that file content was changed.
        IDE.EDITOR.selectTab(FILE_NAME_HTML);
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().contains(FILE_CONTENT_FOR_REPLACE));
    }

    @After
    public void afterTest() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
