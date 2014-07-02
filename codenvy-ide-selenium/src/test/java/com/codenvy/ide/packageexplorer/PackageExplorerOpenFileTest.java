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
package com.codenvy.ide.packageexplorer;

import junit.extensions.TestSetup;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 @author Roman Iuvshin
 *
 */
public class PackageExplorerOpenFileTest extends BaseTest {

    private static final String PROJECT = "OpenFileProject";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    static Map<String, Link> project;

    private static final String FILE_PATH_1 = "src/test/resources/org/exoplatform/ide/operation/file/newCssFile.css";

    public static final String FILE_NAME_1 = "newCssFile.css";

    public static final String FILE_CONTENT_1 = "/*Some example CSS*/\n" +
                                                "\n" +
                                                "@import url (\"something.css\")\n" +
                                                "body {\n" +
                                                "  margin 0;\n" +
                                                "  padding 3em 6em;\n" +
                                                "  font-family: tahoma, arial, sans-serif;\n" +
                                                "  color #000;\n" +
                                                "}\n" +
                                                "  #navigation a {\n" +
                                                "    font-weigt: bold;\n" +
                                                "  text-decoration: none !important;\n" +
                                                "}\n" +
                                                "}";

    public static final String FILE_PATH_2 = "src/test/resources/org/exoplatform/ide/operation/file/newXMLFile.xml";

    public static final String FILE_NAME_2 = "newXMLFile.xml";

    public static final  String FILE_CONTENT_2 = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                                                 "<Module>\n" +
                                                 "  <UserPref>name=\"last_location\" datatype=\"hidden\"</UserPref>\n" +
                                                 "</Module>";

    private static final String FILE_PATH_3    = "src/test/resources/org/exoplatform/ide/operation/file/upload/Example.html";

    private static final String FILE_NAME_3 = "Example.html";

    public static final String FILE_CONTENT_3 = "<html>\n" +
                                                "  <head>\n" +
                                                "    <title></title>\n" +
                                                "  </head>\n" +
                                                "  <body>\n" +
                                                "  </body>\n" +
                                                "</html>";

    @BeforeClass
    public static void setUp() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, PATH);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void openLocalXmlFile() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        // open local xml file
        IDE.UPLOAD.open(MenuCommands.File.OPEN_LOCAL_FILE, FILE_PATH_2, MimeType.TEXT_XML);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(FILE_CONTENT_2);
        IDE.EDITOR.closeFile(FILE_NAME_2);
    }

    @Test
    public void openLocalHTMLFile() throws Exception {
        // open local xml file
        IDE.UPLOAD.open(MenuCommands.File.OPEN_LOCAL_FILE, FILE_PATH_3, MimeType.TEXT_HTML);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_3);
        IDE.EDITOR.closeFile(FILE_NAME_3);
    }

    @Test
    public void openLocalCssFileTest() throws Exception {
        // open local css file
        IDE.UPLOAD.open(MenuCommands.File.OPEN_LOCAL_FILE, FILE_PATH_1, MimeType.TEXT_CSS);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_1);
        IDE.EDITOR.closeFile(FILE_NAME_1);
    }
}