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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class ChekProjectTabNameAfterRenameTest extends BaseTest {
    private final static String FILE_NAME = "CursorPositionStatusBar.html";

    private final static String PROJECT = "checkRename";

    private final static String NEW = PROJECT + "2";


    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(NEW);
    }

    @BeforeClass
    public static void setUp() {

        String filePath = "src/test/resources/org/exoplatform/ide/miscellaneous/CursorPositionStatusBar.html";
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFileFromLocal(link, FILE_NAME, MimeType.TEXT_HTML, filePath);
        } catch (IOException e) {
        }
    }

    @Test
    public void testChangeNameOnTabOfTheProject() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.PERSPECTIVE.waitTabsWithSpecifiedNameOnExplorer(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.RENAME.rename(NEW);
        IDE.PERSPECTIVE.waitTabsWithSpecifiedNameOnExplorer(NEW);
    }

}
