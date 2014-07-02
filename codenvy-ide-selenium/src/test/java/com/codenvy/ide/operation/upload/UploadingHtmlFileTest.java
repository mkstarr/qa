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
package com.codenvy.ide.operation.upload;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Evgen Vidolob
 *
 */
public class UploadingHtmlFileTest extends BaseTest {
    private static final String PROJECT = UploadingHtmlFileTest.class.getSimpleName();
    private final String        htmlContent = "<html>\n  <head>\n    <title></title>\n  </head>\n  <body>\n  </body>\n</html>";
    private static String HTML_NAME = "Example.html";

    private static final String FILE_PATH = "src/test/resources/org/exoplatform/ide/operation/file/upload/Example.html";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testUploadingHtml() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH, MimeType.TEXT_HTML);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        String text = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(htmlContent);


        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.waitMimeTypePropertyContainsText(MimeType.TEXT_HTML);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

}
