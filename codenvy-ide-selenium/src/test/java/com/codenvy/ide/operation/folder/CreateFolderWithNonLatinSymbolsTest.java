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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 *
 */
public class CreateFolderWithNonLatinSymbolsTest extends BaseTest {
    private static String PROJECT = CreateFolderWithNonLatinSymbolsTest.class.getSimpleName();

    private static String FOLDER_NAME = "Папка з кирилічними символами";

    @BeforeClass
    public static void beforeTest() throws IOException {
        VirtualFileSystemUtils.createDefaultProject(PROJECT);
    }

    /**
     * Test added to Ignore, because at the moment not solved a problem with encoding Cyrillic characters to URL. For
     * example: create new file with cyrillic name, save him, and get URL in IDE. In URL IDE we  shall see encoding
     * characters in file name
     *
     * @throws Exception
     */
    @Test
    public void testCreateFolderWithNonLatinSymbols() throws Exception {
        IDE.EXPLORER.waitOpened();

        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);

        IDE.FOLDER.createFolder(FOLDER_NAME);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + FOLDER_NAME);

        assertEquals(200, VirtualFileSystemUtils
                .get(REST_URL + "itembypath/" + PROJECT + "/" + URLEncoder.encode(FOLDER_NAME, "UTF-8"))
                .getStatusCode());
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
