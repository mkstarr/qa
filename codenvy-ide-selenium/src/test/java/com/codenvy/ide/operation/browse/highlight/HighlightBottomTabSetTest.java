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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.*;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 *
 */
public class HighlightBottomTabSetTest extends BaseTest {
    private static final String PROJECT = HighlightBottomTabSetTest.class.getSimpleName();

    private static String FOLDER_NAME = HighlightBottomTabSetTest.class.getSimpleName();

    private static String FILE_NAME = "HighlightBottomTabSetTestFILE";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/HtmlCodeOutline.html";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_HTML, filePath);
        } catch (Exception e) {
        }

    }

    @Test
    public void testHighlightBottomTabSet() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitTabPresent(1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.waitOpened();
        assertTrue(IDE.PROPERTIES.isActive());
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(FOLDER_NAME);
        } catch (Exception e) {
        }
    }
}
