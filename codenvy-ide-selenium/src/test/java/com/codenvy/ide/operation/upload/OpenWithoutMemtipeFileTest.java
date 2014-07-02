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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.*;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test for "Open local file" form.
 * 
 * @author Evgen Vidolob
 *
 */
public class OpenWithoutMemtipeFileTest extends BaseTest {
    private static final String FILE_PATH = "src/test/resources/org/exoplatform/ide/operation/file/upload/test";

    private static final String PROJECT   = OpenWithoutMemtipeFileTest.class.getSimpleName();

    @BeforeClass
    public static void beforeTest() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testOpenFileWithoutExtention() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        // call Open Local File form
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.OPEN_LOCAL_FILE);
        IDE.UPLOAD.waitOpenLocalFileViewOpened();
        assertFalse(IDE.UPLOAD.isUploadButtonEnabled());

        // select file from local driver without file extention
        try {
            File file = new File(FILE_PATH);
            IDE.UPLOAD.setUploadFilePath(file.getCanonicalPath());
        } catch (Exception e) {
        }

        String fileName = FILE_PATH.substring(FILE_PATH.lastIndexOf("/") + 1, FILE_PATH.length());


        IDE.UPLOAD.waitNameInPathToFileIsSet(fileName);
        IDE.UPLOAD.waitMimeTypeIsSet("text/plain");
        IDE.UPLOAD.waitNameIsUploadBtnEnabl();
        IDE.UPLOAD.setMimeType(MimeType.TEXT_HTML);
        IDE.UPLOAD.waitNameIsUploadBtnEnabl();
        IDE.UPLOAD.clickUploadButton();
        IDE.UPLOAD.waitClosed();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
    }

    @AfterClass
    public static void afterTest() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
