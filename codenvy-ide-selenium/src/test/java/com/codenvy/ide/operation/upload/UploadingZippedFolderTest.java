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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Make possibility upload ZIPed folder (IDE-482).
 * 
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class UploadingZippedFolderTest extends BaseTest {
    private static final String PROJECT   = UploadingZippedFolderTest.class.getSimpleName();

    private final String        content   =
                                            "      <id>repository.exoplatform.org</id>\n      <username>1</username>\n      <password>2</password>\n";
    private static final String FILE_PATH = "src/test/resources/org/exoplatform/ide/operation/file/upload/sample.zip";

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
    public void testUploadingZippedFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();

        uploadZippedFolder(FILE_PATH);

        final String testFolder = "test";
        final String folder = "folder";
        final String projectFolder = "project";
        final String exoFolder = "exo";
        final String settingsFile = "settings.xml";
        final String sampleFile = "sample.txt";
        final String mineFile = "mine.xml";

        IDE.EXPLORER.selectItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + testFolder);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + testFolder);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + sampleFile);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + settingsFile);

        IDE.EXPLORER.openItem(PROJECT + "/" + folder);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + folder + "/" + projectFolder);

        IDE.EXPLORER.openItem(PROJECT + "/" + testFolder);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + testFolder + "/" + mineFile);

        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + testFolder + "/" + exoFolder);
        IDE.EXPLORER.waitItemVisible(PROJECT + "/" + testFolder + "/" + mineFile);

        IDE.EXPLORER.openItem(PROJECT + "/" + settingsFile);
        IDE.EDITOR.waitActiveFile();

        IDE.EDITOR.waitContentIsPresent(content);

        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.waitOpened();
        IDE.PROPERTIES.waitMimeTypePropertyContainsText(MimeType.TEXT_XML);
    }

    protected void uploadZippedFolder(String filePath) throws Exception {
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FOLDER);
        IDE.UPLOAD.waitOpened();
        try {
            File file = new File(filePath);
            IDE.UPLOAD.setUploadFilePath(file.getCanonicalPath());
        } catch (Exception e) {
        }

        IDE.UPLOAD.waitNameInPathToFileIsSet("sample.zip");
        IDE.UPLOAD.clickUploadButton();
        IDE.UPLOAD.waitClosed();
        IDE.LOADER.waitClosed();
    }
}
