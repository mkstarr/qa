/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

/**
 * @author Musienko Maxim
 *
 */
public class OpenCssFileTest extends BaseTest {
    private static String       CSS_FILE2  = "Example2.js";
    private final String        cssContent =
                                             "p {\n   font-family: Verdana, sans-serif;\n }\n h2 {\n   font-size: 110%;\n   color: red;\n   background: white;\n }\n .note {\n   color: red;\n   background: yellow;\n   font-weight: bold;\n }\n p.warning {\n   background: url(warning.png) no-repeat fixed top;\n }\n #paragraph1 {\n   margin: 0;\n }\n a:hover {\n   text-decoration: none;\n }\n #news p {\n   color: red;\n }";
    private static String       PROJECT    = OpenCssFileTest.class.getSimpleName();

    private static final String FILE_PATH  = "src/test/resources/org/exoplatform/ide/operation/file/upload/Example.css";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testOpenCSS() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        IDE.UPLOAD.open(MenuCommands.File.OPEN_LOCAL_FILE, FILE_PATH, MimeType.TEXT_CSS);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(cssContent);
        IDE.EDITOR.saveAs(1, CSS_FILE2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + CSS_FILE2);

        IDE.PROPERTIES.openProperties();
        IDE.PROPERTIES.waitMimeTypePropertyContainsText(MimeType.TEXT_CSS);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
