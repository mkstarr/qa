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
package com.codenvy.ide.operation.browse.highlight;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 *
 */
public class HighlightEditorsTabTest extends BaseTest {

    private static String PROJECT = HighlightEditorsTabTest.class.getSimpleName();

    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/newHtmlFile.html";

    private static String HTML_FILE_NAME = "newHtmlFile.html";

    private static String HTML_FILE_NAME_2 = "newHtmlFile2.html";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE_NAME, MimeType.TEXT_HTML, PATH);

            VirtualFileSystemUtils.createFileFromLocal(link, HTML_FILE_NAME_2, MimeType.TEXT_HTML, PATH);
        } catch (IOException e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testHighlightEditorTab() throws Exception {

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        // step 1 open file after close 'welcome' tab
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE_NAME_2);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE_NAME);

        IDE.EDITOR.clickCloseEditorButton(0);
        IDE.EDITOR.waitTabNotPresent(0);

        // step 2 check highlight googlegadget in ckeditor
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_NAME_2);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitHighlighterInEditor(0);
        IDE.EDITOR.forcedClosureFile(0);

        IDE.EDITOR.waitTabNotPresent(0);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitHighlighterInEditor(1);

    }

}
