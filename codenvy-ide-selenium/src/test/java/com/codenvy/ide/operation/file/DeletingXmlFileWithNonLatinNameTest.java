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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * IDE-49: Deleting XML file with non-latin name.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 *
 */

public class DeletingXmlFileWithNonLatinNameTest extends BaseTest {
    private static final String PROJECT = DeletingXmlFileWithNonLatinNameTest.class.getSimpleName();

    private static String FILE_NAME = "ТестФайл.xml";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);

        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeletingXmlFileWithNonLatinName() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FILE_NAME);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FILE_NAME);
        IDE.DELETE.deleteSelectedItems();
        IDE.EXPLORER.waitForItemNotPresent(PROJECT + "/" + FILE_NAME);

        assertEquals(404, VirtualFileSystemUtils
                .get(REST_URL + "itembypath/" + PROJECT + "/" + URLEncoder.encode(FILE_NAME, "UTF-8"))
                .getStatusCode());
    }

}
