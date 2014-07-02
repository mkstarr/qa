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
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

/** @author Evgen Vidolob */
public class JspImplicitObjectsTest extends BaseTest {

    private static final String PROJECT = JspImplicitObjectsTest.class.getSimpleName();

    private static final String FOLDER_NAME = JspImplicitObjectsTest.class.getSimpleName();

    private static final String FILE_NAME = "JspImplicitObjectsTest.jsp";

    private String docMessage =
            "The servlet context obtained from the servlet conﬁguration object (as in the call getServletConfig()" +
            ".getContext())";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FOLDER_NAME + "/" + FILE_NAME, MimeType.APPLICATION_JSP,
                                         "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/jsp" +
                                         "/testImplicitObject.jsp");
        } catch (Exception e) {
            fail("Can't create test folder");
        }
    }

    @Test
    public void testJspImplicitObjects() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.GOTOLINE.goToLine(10);

        IDE.CODEASSISTANT.openForm();

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

        IDE.CODEASSISTANT.waitForDocPanelOpened();
        IDE.CODEASSISTANT.checkDocFormPresent();
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(FOLDER_NAME);
        } catch (Exception e) {
        }
    }

}
