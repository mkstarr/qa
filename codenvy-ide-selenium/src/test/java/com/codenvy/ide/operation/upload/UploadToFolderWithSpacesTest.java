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
package com.codenvy.ide.operation.upload;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Ann Zhuleva
 *
 */
public class UploadToFolderWithSpacesTest extends BaseTest {
    private static final String PROJECT = UploadToFolderWithSpacesTest.class.getSimpleName();

    private static String TEST_FILE = "Test File@.txt";

    private static String FOLDER = "Folder @1";

    private static final String FILE_PATH =
            "src/test/resources/org/exoplatform/ide/operation/file/upload/Test File@.txt";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testUploadingFileWithSpaces() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        // Create folder with spaces:
        IDE.FOLDER.createFolder(FOLDER);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER);
        IDE.EXPLORER.selectItem(PROJECT + "/" + FOLDER);

        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FILE);
        IDE.UPLOAD.waitOpened();
        try {
            File file = new File(FILE_PATH);
            IDE.UPLOAD.setUploadFilePath(file.getCanonicalPath());
        } catch (Exception e) {
        }

        IDE.UPLOAD.clickUploadButton();
        IDE.UPLOAD.waitClosed();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER + "/" + TEST_FILE);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
