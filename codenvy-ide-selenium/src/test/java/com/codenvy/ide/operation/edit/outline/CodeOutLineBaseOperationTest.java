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
package com.codenvy.ide.operation.edit.outline;

import com.codenvy.ide.*;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="musienko.maxim@gmail.com">Musienko Maksim</a>
 *
 */
public class CodeOutLineBaseOperationTest extends BaseTest {
    private final static String PROJECT   = CodeOutLineBaseOperationTest.class.getSimpleName();

    private final static String FILE_NAME = "XmlCodeOutline.xml";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/XmlCodeOutline.xml";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_XML, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void codeOutLineBaseOperationTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitElementIsSelect("web-app");
        // click on second groovy code node
        IDE.OUTLINE.expandSelectItem();
        IDE.OUTLINE.selectRow(2);
        IDE.OUTLINE.waitElementIsSelect("display-name");

        // check, than cursor go to line
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");
        IDE.GOTOLINE.goToLine(12);
        IDE.STATUSBAR.waitCursorPositionAt("12 : 1");
        IDE.OUTLINE.waitItemAtPosition("param-value", 4);

        IDE.OUTLINE.waitItemAtPosition("CDATA", 6);

        IDE.GOTOLINE.goToLine(28);
        IDE.OUTLINE.waitItemAtPosition("filter-name", 8);
        IDE.STATUSBAR.waitCursorPositionAt("28 : 1");


        IDE.OUTLINE.waitItemAtPosition("param-value", 4);
        IDE.OUTLINE.waitItemAtPosition("context-param", 5);
        IDE.OUTLINE.waitItemAtPosition("filter-class", 9);
    }
}
