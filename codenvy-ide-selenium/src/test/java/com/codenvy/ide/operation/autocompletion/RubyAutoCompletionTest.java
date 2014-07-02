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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Evgen Vidolob */
public class RubyAutoCompletionTest extends BaseTest {
    private static final String PROJECT = RubyAutoCompletionTest.class.getSimpleName();

    private static final String FILE_NAME = "RubyCodeAssistantTest.rb";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.APPLICATION_RUBY,
                                                       "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/ruby/rubyAutocompletion.rb");
        } catch (Exception e) {
            fail("Can't create test folder");
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
    public void testRubyAutocompletion() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.GOTOLINE.goToLine(26);

        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("h");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("w");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("@i");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("@@ins");
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("$cl");

        IDE.CODEASSISTANT.typeToInput("@@");
        IDE.CODEASSISTANT.typeToInput("\n");

        IDE.EDITOR.typeTextIntoEditor(".");
        // Pause is necessary for parsing tokens by CodeMirror
        Thread.sleep(TestConstants.REDRAW_PERIOD);

        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("prec_f()");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("between?(arg1, arg2, arg3)");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("abs()");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("next()");

        IDE.CODEASSISTANT.typeToInput("ro");
        IDE.CODEASSISTANT.typeToInput("\n");

        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains("@@ins.round()"));

        IDE.GOTOLINE.goToLine(32);

        IDE.EDITOR.typeTextIntoEditor("M");
        // this method fix problem of returning cursor in codeeditor before
        // character "M"
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        // Pause is necessary for parsing tokens by CodeMirror
        Thread.sleep(TestConstants.REDRAW_PERIOD);
        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("MDA");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("MyClass");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("Method");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("Math");

        IDE.CODEASSISTANT.typeToInput("\n");
        IDE.EDITOR.typeTextIntoEditor(".");
        // Pause is necessary for parsing tokens by CodeMirror
        Thread.sleep(TestConstants.REDRAW_PERIOD);
        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("finite?()");

        IDE.CODEASSISTANT.typeToInput("inf");
        IDE.CODEASSISTANT.typeToInput("\n");
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains("MDA.infinite?()"));

        IDE.GOTOLINE.goToLine(33);

        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("g");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("num");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("$cl");

        IDE.CODEASSISTANT.typeToInput("\n");
        IDE.EDITOR.typeTextIntoEditor(".");
        // Pause is necessary for parsing tokens by CodeMirror
        Thread.sleep(TestConstants.REDRAW_PERIOD);
        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("get");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("set");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("hello");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("initialize");

        IDE.CODEASSISTANT.closeForm();
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }
}
