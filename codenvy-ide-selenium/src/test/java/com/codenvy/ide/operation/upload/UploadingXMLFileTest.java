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
package com.codenvy.ide.operation.upload;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

/**
 * @author Musienko Maxim
 *
 */
public class UploadingXMLFileTest extends BaseTest {

    private static String       PROJECT           = OpenXmlLocalFileTest.class
                                                                              .getSimpleName();

    private static String       XML_NAME          = "untitled.xml";

    private static final String FILE_PATH         = "src/test/resources/org/exoplatform/ide/operation/file/upload/pom.xml";

    private String              checkFragmentText =

                                                    "<dependency>\n         <groupId>org.springframework</groupId>\n         <artifactId>spring-webmvc</artifactId>\n         <version>3.0.5.RELEASE</version>\n      </dependency>";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }


    @Test
    public void testOpenXML() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH,
                        MimeType.APPLICATION_XML);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "pom.xml");
        IDE.EXPLORER.openItem(PROJECT + "/" + "pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(checkFragmentText);
        IDE.MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.SHOW_PROPERTIES);
        IDE.PROPERTIES.waitMimeTypePropertyContainsText(MimeType.APPLICATION_XML);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }
}
