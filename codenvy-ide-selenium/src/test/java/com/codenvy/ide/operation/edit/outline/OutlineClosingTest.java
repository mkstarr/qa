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
package com.codenvy.ide.operation.edit.outline;

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
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 *
 */
// http://jira.exoplatform.org/browse/IDE-417
public class OutlineClosingTest extends BaseTest {
    private final static String PROJECT = OutlineClosingTest.class.getSimpleName();

    private static final String FILE_NAME = "newXMLFile.xml";

    @BeforeClass
    public static void setUp() throws Exception {

        Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
        Link link = project.get(Link.REL_CREATE_FILE);
        VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_XML,
                                                   "src/test/resources/org/exoplatform/ide/operation/file/newXMLFile" +
                                                   ".xml");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void test() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        openAndCloseOutline();

        openAndCloseOutline();

        openAndCloseOutline();
    }

    /**
     * @throws Exception
     * @throws InterruptedException
     */
    private void openAndCloseOutline() throws Exception, InterruptedException {
        // open outline panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitOutlineTreeVisible();

        // check for presence of tab outline
        IDE.OUTLINE.waitOutlineTreeVisible();
        IDE.OUTLINE.waitOutlineViewVisible();
        IDE.OUTLINE.waitItemAtPosition("Module", 1);
        IDE.OUTLINE.closeOutline();
        IDE.OUTLINE.waitClosed();
        IDE.OUTLINE.waitOutlineTreeNotVisible();
        IDE.OUTLINE.waitOutlineViewInvisible();
    }
}
