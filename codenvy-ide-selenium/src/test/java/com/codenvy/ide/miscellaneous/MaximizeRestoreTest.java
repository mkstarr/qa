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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Perspective;
import com.codenvy.ide.operation.file.OpeningSavingAndClosingFilesTest;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */

public class MaximizeRestoreTest extends BaseTest {
    private static String PROJECT = MaximizeRestoreTest.class.getSimpleName();

    private static String path = "src/test/resources/org/exoplatform/ide/operation/edit/outline/XmlCodeOutline.xml";

    private static String FILE_NAME = OpeningSavingAndClosingFilesTest.class.getSimpleName() + ".xml";

    @BeforeClass
    public static void setUp() {

        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_XML, path);
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
    public void maximizeRestoreNavigationPanel() throws Exception {
        // step 1 check maximize/restore for Navigation panel
        IDE.EXPLORER.waitOpened();
        IDE.PERSPECTIVE.maximizePanel(Perspective.Panel.NAVIGATION);
        IDE.PERSPECTIVE.waitMaximized(Perspective.Panel.NAVIGATION);
        IDE.PERSPECTIVE.restorePanel(Perspective.Panel.NAVIGATION);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.NAVIGATION);

        // step 2 check maximize/restore for Editor panel
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.PERSPECTIVE.maximizePanel(Perspective.Panel.EDITOR);
        IDE.PERSPECTIVE.waitMaximized(Perspective.Panel.EDITOR);
        IDE.PERSPECTIVE.restorePanel(Perspective.Panel.EDITOR);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.EDITOR);

        // step 3 check maximize/restore for Editor panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.waitOpened();

        IDE.PERSPECTIVE.maximizePanel(Perspective.Panel.OPERATION);
        IDE.PERSPECTIVE.waitMaximized(Perspective.Panel.OPERATION);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.EDITOR);
        IDE.PERSPECTIVE.restorePanel(Perspective.Panel.OPERATION);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.OPERATION);

        // step 4 check maximize/restore for Outline panel
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.PERSPECTIVE.maximizePanel(Perspective.Panel.INFORMATION);
        IDE.PERSPECTIVE.waitMaximized(Perspective.Panel.INFORMATION);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.EDITOR);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.OPERATION);
        IDE.PERSPECTIVE.restorePanel(Perspective.Panel.INFORMATION);
        IDE.PERSPECTIVE.waitRestored(Perspective.Panel.INFORMATION);

    }
}
