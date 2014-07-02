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
package com.codenvy.ide.operation.browse;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Ann Zhuleva
 *
 */
public class ExploringWorkspacePanelTest extends BaseTest {

    private static final String PROJECT = ExploringWorkspacePanelTest.class.getSimpleName();

    private static final String FOLDER_2_2 = "folder-2-2";

    private static final String FOLDER_2_1 = "folder-2-1";

    private static final String FOLDER_2 = "folder-2";

    private static final String FOLDER_1_2 = "folder-1-2";

    private static final String FOLDER_1_1 = "folder-1-1";

    private static final String FOLDER_1 = "folder-1";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1 + "/" + FOLDER_1_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_1 + "/" + FOLDER_1_2);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2 + "/" + FOLDER_2_1);
            VirtualFileSystemUtils.createFolder(link, FOLDER_2 + "/" + FOLDER_2_2);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }

    /**
     * IDE-2 Exploring "Workspace" panel
     *
     * @throws Exception
     */
    @Test
    public void testExplodeCollapseFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2);

        // step1. Open folder 2. Check visible subfolders of folder #2. Check subfolders folder #1 is not visible.
        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_2);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);

        // step2. Close folder 2. Check not visible subfolders of folder #2.
        // Open folder one and check visible subfolders of of folder#1.
        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);

        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_2);

        // step 3 collapse all folders and check their
        IDE.EXPLORER.clickOpenCloseButton(PROJECT + "/" + FOLDER_2);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);

        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_2);

        // step 4 Roll up project folder and checking - workspace is empty
        IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_2);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_1);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_2 + "/" + FOLDER_2_2);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_1);
        IDE.EXPLORER.waitItemNotVisible(PROJECT + "/" + FOLDER_1 + "/" + FOLDER_1_2);

        // step 5 Collapse project and check all folders present
        IDE.EXPLORER.clickOpenCloseButton(PROJECT);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_1);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + FOLDER_2);
    }
}
