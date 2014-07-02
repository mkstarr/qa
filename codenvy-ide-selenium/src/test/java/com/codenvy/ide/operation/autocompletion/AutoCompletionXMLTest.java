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
package com.codenvy.ide.operation.autocompletion;

import com.codenvy.ide.MenuCommands;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertTrue;

/** @author Evgen Vidolob */
public class AutoCompletionXMLTest extends CodeAssistantBaseTest {

    @BeforeClass
    public static void createProject() throws Exception {
        createProject(AutoCompletionXMLTest.class.getSimpleName());
    }

    @After
    public void forceClosedTabs() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
    }

    @Test
    public void testXMLAutocompletion() throws Throwable {
        openProject();
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();

        String text = IDE.EDITOR.getTextFromCodeEditor();
        assertTrue(text.startsWith("<?xml version='1.0' encoding='UTF-8'?>"));

        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString() + Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor("<root>\n\n</root>");
        IDE.EDITOR.moveCursorUp(1);

        IDE.EDITOR.typeTextIntoEditor("<rot>\n\n</rot>");
        IDE.EDITOR.moveCursorUp(1);

        IDE.EDITOR.typeTextIntoEditor("<rt>\n\n</rt>");
        IDE.EDITOR.moveCursorUp(1);

        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.typeToInput("ro");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("rot");
        IDE.CODEASSISTANT.insertSelectedItem();

        String textAfter = IDE.EDITOR.getTextFromCodeEditor();
        assertTrue(textAfter.contains("<root></root>"));
    }

}
