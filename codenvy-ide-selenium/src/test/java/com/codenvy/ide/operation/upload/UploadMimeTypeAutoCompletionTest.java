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

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author Evgen Vidolob
 *
 */
public class UploadMimeTypeAutoCompletionTest extends BaseTest {
    private static final String PROJECT   = UploadMimeTypeAutoCompletionTest.class.getSimpleName();

    private static final String FILE_PATH = "src/test/resources/org/exoplatform/ide/operation/file/upload/Example.html";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
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
    public void testMimeTypeAutoCompletion() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FILE);
        IDE.UPLOAD.waitOpened();
        try {
            File file = new File(FILE_PATH);
            IDE.UPLOAD.setUploadFilePath(file.getCanonicalPath());
        } catch (Exception e) {
        }
        IDE.UPLOAD.typeToMimeTypeField("application/");
        IDE.UPLOAD.waitMimeTypeContainsProposes("application/javascript", "application/java");
        IDE.UPLOAD.waitMimeTypeNotContainsProposes("text/javascript");

        IDE.UPLOAD.typeToMimeTypeField("text/");
        IDE.UPLOAD.waitMimeTypeContainsProposes("text/javascript", "text/html", "text/css", "text/plain", "text/xml");
        IDE.UPLOAD.waitMimeTypeNotContainsProposes("application/javascript");

        String mimeTypeToSelect = "text/html";
        IDE.UPLOAD.selectMimeTypeByName(mimeTypeToSelect);
        IDE.UPLOAD.waitMimeTypeIsSet(mimeTypeToSelect);
        assertEquals(mimeTypeToSelect, IDE.UPLOAD.getMimeTypeValue());
    }
}
