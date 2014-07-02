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
package com.codenvy.ide.operation.folder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.*;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */
public class RenameFolderTest extends BaseTest {
    private final static String PROJECT = RenameFolderTest.class.getSimpleName();

    private final static String FOLDER_NAME = "FirstName";

    private final static String NEW_FOLDER_NAME = "FolderRenamed";

    private final String ORIG_URL = REST_URL + "itembypath/" + PROJECT + "/" + FOLDER_NAME;

    private final String RENAME_URL = REST_URL + "itembypath/" + PROJECT + "/" + NEW_FOLDER_NAME;

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);

            VirtualFileSystemUtils.createFolder(link, FOLDER_NAME);
        } catch (Exception e) {
        }
    }

    /**
     * Test the rename folder operation (TestCase IDE-51).
     *
     * @throws Exception
     */
    @Test
    public void testRenameFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME);

        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER_NAME);

        IDE.RENAME.rename(NEW_FOLDER_NAME);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + NEW_FOLDER_NAME);
        IDE.EXPLORER.waitItemNotPresent(PROJECT + "/" + FOLDER_NAME);

        assertEquals(404, VirtualFileSystemUtils.get(ORIG_URL).getStatusCode());
        assertEquals(200, VirtualFileSystemUtils.get(RENAME_URL).getStatusCode());
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

}
