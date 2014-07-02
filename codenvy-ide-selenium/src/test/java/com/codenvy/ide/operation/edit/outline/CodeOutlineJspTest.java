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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Evgen Vidolob
 *
 */
public class CodeOutlineJspTest extends BaseTest {

    private final static String FILE_NAME = "JspCodeOutline.jsp";

    private final static String PROJECT = CodeOutlineJspTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/test-jsp.jsp";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.APPLICATION_JSP, filePath);
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
    public void testCodeOutlineJSP() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        //TODO Pause for build outline tree
        //after implementation method for check ready state, should be remove
        Thread.sleep(2000);

        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitOutlineTreeVisible();

        checkTreeCorrectlyCreated();
        checkClickOnTreeElements();

        IDE.EDITOR.closeFile(FILE_NAME);
        IDE.EDITOR.waitTabNotPresent(FILE_NAME);
    }

    private void checkTreeCorrectlyCreated() throws Exception {
        IDE.GOTOLINE.goToLine(2);
        IDE.OUTLINE.waitElementIsSelect("head");
        IDE.OUTLINE.waitItemAtPosition("html", 1);
        IDE.OUTLINE.waitItemAtPosition("script", 3);
        IDE.OUTLINE.waitItemAtPosition("body", 4);

        IDE.GOTOLINE.goToLine(4);
        IDE.OUTLINE.waitElementIsSelect("java code");
        IDE.GOTOLINE.goToLine(9);
        IDE.OUTLINE.waitItemAtPosition("a : String", 5);
        IDE.GOTOLINE.goToLine(14);
        IDE.OUTLINE.waitElementIsSelect("curentState");
        IDE.OUTLINE.waitItemAtPosition("identity", 9);
        IDE.OUTLINE.waitItemAtPosition("i", 10);
        IDE.OUTLINE.waitItemAtPosition("a", 11);

    }

    private void checkClickOnTreeElements() throws Exception {
        // check variable a
        // click on 5 row need for reparse tree
        IDE.OUTLINE.selectRow(11);
        IDE.STATUSBAR.waitCursorPositionAt("23 : 1");

        // check javascript variable a
        IDE.OUTLINE.selectRow(5);
        IDE.STATUSBAR.waitCursorPositionAt("9 : 1");

        // check tag head
        IDE.OUTLINE.selectRow(2);
        IDE.STATUSBAR.waitCursorPositionAt("2 : 1");

        // check javascript javacode
        IDE.OUTLINE.selectRow(7);
        IDE.STATUSBAR.waitCursorPositionAt("13 : 1");
    }

}
