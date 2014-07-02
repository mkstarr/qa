package com.codenvy.ide.packageexplorer;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 @author Roman Iuvshin
 *
 */
public class PackageExplorerUploadTest extends BaseTest {

    private static final String PROJECT = "UploadToPkgExplorer";

    private static final String FILE_PATH_1 = "src/test/resources/org/exoplatform/ide/operation/file/upload/Example.html";

    private static final String FILE_NAME_1 = "Example.html";

    public static final String FILE_CONTENT_1 = "<html>\n" +
                                                "  <head>\n" +
                                                "    <title></title>\n" +
                                                "  </head>\n" +
                                                "  <body>\n" +
                                                "  </body>\n" +
                                                "</html>";

    public static final String FILE_PATH_2 = "src/test/resources/org/exoplatform/ide/operation/file/newXMLFile.xml";

    public static final String FILE_NAME_2 = "newXMLFile.xml";

    public static final String FILE_CONTENT_2 = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                                                "<Module>\n" +
                                                "  <UserPref>name=\"last_location\" datatype=\"hidden\"</UserPref>\n" +
                                                "</Module>";

    public static final String FILE_PATH_3 = "src/test/resources/org/exoplatform/ide/operation/file/newCssFile.css";

    public static final String FILE_NAME_3 = "newCssFile.css";

    public static final String FILE_CONTENT_3 = "/*Some example CSS*/\n" +
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

    public static final String FILE_PATH_4 = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.java";

    public static final String FILE_NAME_4 = "JavaCommentsTest.java";

    public static final String FILE_CONTENT_4 = "/*\n" +
                                                " * CODENVY CONFIDENTIAL\n" +
                                                " * __________________\n" +
                                                " *\n" +
                                                " * [2012] - [2013] Codenvy, S.A.\n" +
                                                " * All Rights Reserved.\n" +
                                                " *\n" +
                                                " * NOTICE:  All information contained herein is, and remains\n" +
                                                " * the property of Codenvy S.A. and its suppliers,\n" +
                                                " * if any.  The intellectual and technical concepts contained\n" +
                                                " * herein are proprietary to Codenvy S.A.\n" +
                                                " * and its suppliers and may be covered by U.S. and Foreign Patents,\n" +
                                                " * patents in process, and are protected by trade secret or copyright law.\n" +
                                                " * Dissemination of this information or reproduction of this material\n" +
                                                " * is strictly forbidden unless prior written permission is obtained\n" +
                                                " * from Codenvy S.A..\n" +
                                                " */\n" +
                                                "package org.exoplatform.ide.operation.java;\n" +
                                                "\n" +
                                                "import java.util.ArrayList;\n" +
                                                "\n" +
                                                "public class JavaCommentsTest\n" +
                                                "{\n" +
                                                "   private ArrayList<Integer> numbers = new ArrayList<Integer>();\n" +
                                                "\n" +
                                                "   public JavaCommentsTest()\n" +
                                                "   {\n" +
                                                "      numbers.add(1);\n" +
                                                "      numbers.add(2);\n" +
                                                "      numbers.add(3);\n" +
                                                "      numbers.add(4);\n" +
                                                "      numbers.add(5);\n" +
                                                "      numbers.add(6);\n";

    public static final String FILE_PATH_5 = "src/test/resources/org/exoplatform/ide/operation/file/newJavaScriptFile.js";

    public static final String FILE_NAME_5 = "newJavaScriptFile.js";

    public static final String FILE_CONTENT_5 = "//Here you see some JavaScript code. Mess around with it to get\n" +
                                                "//acquinted with CodeMirror's features.\n" +
                                                "\n" +
                                                "// Press enter inside the objects and your new line will \n" +
                                                "// intended.\n" +
                                                "\n" +
                                                "var keyBindings ={\n" +
                                                "  enter:\"newline-and-indent\",\n" +
                                                "  tab:\"reindent-selection\",\n" +
                                                "  ctrl_z: \"undo\",\n" +
                                                "  ctrl_y:\"redo\"\n" +
                                                "  };\n" +
                                                "  var regex =/foo|bar/i;\n" +
                                                "  function example (x){\n" +
                                                "  var y=44.4;\n" +
                                                "  return x+y;\n" +
                                                "  }\n";

    public static final String ZIPPED_FOLDER_PATH =
            "src/test/resources/org/exoplatform/ide/operation/file/upload/zippedFolderForUpload.zip";

    private final static String PATH = "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    static Map<String, Link> project;

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
    public void uploadZippedFolder() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();


        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        // upload zipped folder
        uploadzippedfolder();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("test");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("test");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("innerFolder");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-codestyle-intellij.xml");
    }

    @Test
    public void uploadFilesTest() throws Exception {

        // upload html file
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH_1, MimeType.TEXT_HTML);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME_1);
        // check content
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME_1);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_1);
        IDE.EDITOR.closeFile(FILE_NAME_1);

        // upload xml file
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH_2, MimeType.TEXT_XML);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME_2);
        // check content
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME_2);
        IDE.EDITOR.selectTab(FILE_NAME_2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(FILE_CONTENT_2);
        IDE.EDITOR.closeFile(FILE_NAME_2);

        // upload css file
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH_3, MimeType.TEXT_CSS);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME_3);
        // check content
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME_3);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_3);
        IDE.EDITOR.closeFile(FILE_NAME_3);
    }

    @Test
    public void uploadFileInToFolder() throws Exception {
        // upload java file in to package
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("helloworld");
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH_4, MimeType.APPLICATION_JAVA);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME_4);
        // check content
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME_4);
        IDE.EDITOR.selectTab(FILE_NAME_4);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_4);
        IDE.EDITOR.closeFile(FILE_NAME_4);

        // upload java script file in to folder
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src");
        IDE.UPLOAD.open(MenuCommands.File.UPLOAD_FILE, FILE_PATH_5, MimeType.APPLICATION_JAVASCRIPT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME_5);
        // check content
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME_5);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILE_CONTENT_5);
        IDE.EDITOR.closeFile(FILE_NAME_5);
    }



    private void uploadzippedfolder() throws Exception {
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FOLDER);
        IDE.UPLOAD.waitOpened();

        try {
            File file = new File(ZIPPED_FOLDER_PATH);
            IDE.UPLOAD.setUploadFilePath(file.getCanonicalPath());
        } catch (Exception e) {
        }

        assertEquals(ZIPPED_FOLDER_PATH.substring(ZIPPED_FOLDER_PATH.lastIndexOf("/") + 1, ZIPPED_FOLDER_PATH.length()),
                     IDE.UPLOAD.getFilePathValue());
        IDE.UPLOAD.clickUploadButton();
        IDE.UPLOAD.waitClosed();
        IDE.LOADER.waitClosed();
    }
}