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
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.fail;

/** @author Evgen Vidolob */
public class JspTagsTest extends BaseTest {

    private static final String PROJECT = JspTagsTest.class.getSimpleName();

    private static final String FOLDER_NAME = JspTagsTest.class.getSimpleName();

    private static final String FILE_NAME = "JspTagsTest.jsp";

    private String docMessage =
            "A jsp:useBean action associates an instance of a Java programming language object defined within a given" +
            " scope and available with a given id with a newly declared scripting variable of the same id. When a " +
            "<jsp:useBean> action is used in an scriptless page, or in an scriptless context (as in the body of an " +
            "action so indicated), there are no Java scripting variables created but instead an EL variable is " +
            "created.";

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
                                         "/testJspTag.jsp");
        } catch (Exception e) {
            fail("Can't create test folder");
        }
    }

    @Test
    public void testJspTag() throws Exception {

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FOLDER_NAME + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.GOTOLINE.goToLine(10);

        IDE.EDITOR.typeTextIntoEditor("<jsp:");
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        IDE.CODEASSISTANT.openForm();
        IDE.CODEASSISTANT.waitForDocPanelOpened();

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

        IDE.CODEASSISTANT.typeToInput("use");
        IDE.CODEASSISTANT.waitForDocPanelOpened();
        IDE.CODEASSISTANT.checkDocFormPresent();
        IDE.CODEASSISTANT.insertSelectedItem();

        IDE.EDITOR.getTextFromCodeEditor().contains("<jsp:useBean id=\"\"></jsp:useBean>");

        // IDE.EDITOR.closeUnsavedFileAndDoNotSave(0));
        // IDE.EDITOR.closeTabIgnoringChanges(0);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(FOLDER_NAME);
        } catch (Exception e) {
        }
    }
}
