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
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Evgen Vidolob
 */
public class CookiesTest extends BaseTest {

    private final static String PROJECT = CookiesTest.class.getSimpleName();

    private final static String FILE_NAME = "zxcvjnklzxbvlczkxbvlkbnlsf";

    private final static String TEST_FOLDER = "Test";

    private final static String COOKIE_PREFIX = "domain=" + IDE_HOST;

    @BeforeClass
    public static void setUp() {
        try {

            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, TEST_FOLDER);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, TEST_FOLDER + "/" + FILE_NAME, MimeType.TEXT_XML,


                                         "src/test/resources/org/exoplatform/ide/operation/file/usingKeyboardTestGoogleGadget.xml");
        } catch (IOException e) {
        }
    }

    @Test
    public void testCookies() throws Exception {

        //goto ide, open project, folder in project and test-file
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FOLDER);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FOLDER + "/" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        //get all cookies and split string. Add substrins in string array
        String[] cookies = driver.manage().getCookies().toString().split("; ");
        //check cookies is not empty
        assertTrue(cookies.length > 0);

        //Chek value cookies
        List<Boolean> listUserCookies = new ArrayList<Boolean>();
        for (int i = 0; i < cookies.length; i++) {
            System.out.println(cookies[i]);
            if (cookies[i].startsWith(COOKIE_PREFIX)) {
                listUserCookies.add(true);
            }
        }
        assertTrue(listUserCookies.size() >= 4);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
