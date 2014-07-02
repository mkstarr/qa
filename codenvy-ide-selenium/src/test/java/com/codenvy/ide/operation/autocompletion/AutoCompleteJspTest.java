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

import com.codenvy.ide.MimeType;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Evgen Vidolob
 *
 */
public class AutoCompleteJspTest extends CodeAssistantBaseTest {

    private static final String FILE_NAME = "JSPtest.jsp";

    @BeforeClass
    public static void beforeTest() throws Exception {
        try {
            createProject(AutoCompleteJspTest.class.getSimpleName());
            VirtualFileSystemUtils.createFileFromLocal(project.get(Link.REL_CREATE_FILE), FILE_NAME,
                                                       MimeType.APPLICATION_JSP,
                                                       "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/jsp/testJsp" +
                                                       ".jsp");
        } catch (Exception e) {
            fail("Can't create test folder");
        }


    }

    @Test
    public void testAutocompleteJsp() throws Exception {
        openProject();
        openFile(FILE_NAME);
        IDE.GOTOLINE.goToLine(6);
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("background-attachment");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("counter-increment");

        IDE.CODEASSISTANT.insertSelectedItem();
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains("!important"));

        IDE.GOTOLINE.goToLine(11);

        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("application:javax.servlet.ServletContext");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("config:javax.servlet.ServletConfig");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("exception:java.lang.Throwable");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("out:javax.servlet.jsp.JspWriter");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("page:java.lang.Object");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("pageContext:javax.servlet.jsp.PageContext");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("request:javax.servlet.http.HttpServletRequest");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("response:javax.servlet.http.HttpServletResponse");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("session:javax.servlet.http.HttpSession");

        IDE.CODEASSISTANT.closeForm();
        Thread.sleep(TestConstants.SLEEP_SHORT);

        IDE.EDITOR.typeTextIntoEditor("Collection");
        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("Collection");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("Collections");

        IDE.CODEASSISTANT.insertSelectedItem();
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains("Collection"));

        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.GOTOLINE.goToLine(18);

        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("a");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("Window");
        IDE.CODEASSISTANT.closeForm();

        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.GOTOLINE.goToLine(24);

        IDE.EDITOR.typeTextIntoEditor("<t");
        IDE.CODEASSISTANT.openForm();

        IDE.CODEASSISTANT.waitForElementInCodeAssistant("table");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("textarea");

        IDE.CODEASSISTANT.closeForm();

        Thread.sleep(TestConstants.SLEEP_SHORT);
        IDE.GOTOLINE.goToLine(4);

        IDE.EDITOR.typeTextIntoEditor("<jsp:");
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:attribute");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:body");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:element");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:fallback");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:forward");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:getProperty");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:include");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:invoke");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:output");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:plugin");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:text");
        IDE.CODEASSISTANT.waitForElementInCodeAssistant("jsp:useBean");

        IDE.CODEASSISTANT.closeForm();

        IDE.EDITOR.typeTextIntoEditor("<jsp:use");
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.typeToInput("\n");
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains("<jsp:useBean id=\"\"></jsp:useBean>"));
    }
}
