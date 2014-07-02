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
package com.codenvy.ide.search;

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
 * @author Evgen Vidolob
 *
 */
public class SearchByMimeTypeInDifferentFolderTest extends BaseTest {

    private static final String PROJECT = SearchByMimeTypeInDifferentFolderTest.class.getSimpleName();

    private static final String FOLDER_NAME_1 = "Users";

    private static final String FILE_NAME_1 = "Example.js";

    private static final String FOLDER_NAME_2 = "Test";

    private static final String FILE_NAME_2 = "CopyOfExample.js";

    private static final String FILE_CONTENT = "// CodeMirror main module"
                                               + "var CodeMirrorConfig = window.CodeMirrorConfig || {};\n"

                                               + "var CodeMirror = (function(){\n" +
                                               "function setDefaults(object, defaults) {\n"
                                               + "for (var option in defaults) {\n" +
                                               "if (!object.hasOwnProperty(option))\n"
                                               + "object[option] = defaults[option];\n" + "}\n" + "}\n" +
                                               "function forEach(array, action) {\n"
                                               + "for (var i = 0; i < array.length; i++)\n" + "action(array[i]);\n" +
                                               "}";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_1);

            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_NAME_1 + "/" + FILE_NAME_1, MimeType.APPLICATION_JAVASCRIPT,
                                FILE_CONTENT);

            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME_2);

            VirtualFileSystemUtils
                    .createFile(linkFile, FOLDER_NAME_2 + "/" + FILE_NAME_2, MimeType.APPLICATION_JAVASCRIPT,
                                FILE_CONTENT);
        } catch (Exception e) {
        }
    }

    /** IDE-32:Searching file by Mime Type from subfolder test. */
    @Test
    public void testSearchByMimeType() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME_1);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_1);

        IDE.SEARCH.performSearch("/" + PROJECT + "/" + FOLDER_NAME_1, "", MimeType.APPLICATION_JAVASCRIPT);
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.SEARCH_RESULT.waitItemNotPresent(PROJECT + "/" + FOLDER_NAME_2 + "/" + FILE_NAME_2);

        IDE.SEARCH_RESULT.openItem(PROJECT + "/" + FOLDER_NAME_1 + "/" + FILE_NAME_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.SEARCH_RESULT.close();
        IDE.SEARCH_RESULT.waitClosed();

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME_2);

        IDE.SEARCH.performSearch("/" + PROJECT + "/" + FOLDER_NAME_2, "", MimeType.APPLICATION_JAVASCRIPT);

        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + FOLDER_NAME_2 + "/" + FILE_NAME_2);
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + FOLDER_NAME_2 + "/" + FILE_NAME_2);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }
}
