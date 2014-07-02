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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Evgen Vidolob
 *
 */
public class HighlightOutlineTest extends BaseTest {

    private static final String PROJECT = HighlightOutlineTest.class.getSimpleName();

    private final static String FILE_NAME = "XmlCodeOutline.xml";

    @BeforeClass
    public static void setUp() {
        String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/XmlCodeOutline.xml";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_XML, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testHighlightOutline() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);

        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.EDITOR.waitTabPresent(1);

        // open outline panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitActive();
        IDE.OUTLINE.waitHiglightBorderPresent();

        // reopen file
        IDE.EDITOR.closeFile(FILE_NAME);

        IDE.EDITOR.waitTabNotPresent(FILE_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.EDITOR.waitHighlighterInEditor(1);
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitActive();
        IDE.EDITOR.selectTab(FILE_NAME);
        IDE.EDITOR.typeTextIntoEditor("\nFOO\nBAR");
        // wait for some redrawing in IDE
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.SAVE);
        IDE.OUTLINE.waitNotActive();
        IDE.EDITOR.closeTabIgnoringChanges(FILE_NAME);
    }
}
