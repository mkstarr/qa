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
package com.codenvy.ide.packageexplorer;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerSimpleOperationsTest extends BaseTest {
    private static final String PROJECT = "SimpleOpsPrj";

    private static final String FILE_NAME = "JavaCommentsTest.java";

    private static final String XML_FILE_NAME = "test.xml";

    final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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

    /**
     * Close and open package explorer using toolbar button.
     *
     * @throws Exception
     */
    @Test
    public void closeAndOpenPackageExplorerTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.LOADER.waitClosed();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.LOADER.waitClosed();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
    }

    /**
     * Open folders, packages and files in Package Explorer
     *
     * @throws Exception
     */
    @Test
    public void openFolderPackageAndFileInPackageExplorerTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        //close folders
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.EDITOR.closeFile(FILE_NAME);
    }

    /**
     * Copy file in Package Explorer
     *
     * @throws Exception
     */
    @Test
    public void copyFileInPackageExplorerTest() throws Exception {
        // open folders
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        // copying
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.COPY_SELECTED_ITEM);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        // pasting copied file in to root
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("pom.xml");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/" + FILE_NAME);
    }

    /**
     * Deleting file from Package Explorer
     *
     * @throws Exception
     */
    @Test
    public void deleteInPackageExplorerTest() throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemByPath(PROJECT + "/" + FILE_NAME);
        IDE.DELETE.deleteSelectedItems();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FILE_NAME);
    }

    /**
     * Cutting file in Package Explorer
     *
     * @throws Exception
     */
    @Test
    public void cutInPackageExplorerTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(FILE_NAME);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.CUT_SELECTED_ITEM);
        IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);

        // pasting
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("src");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);
        IDE.LOADER.waitClosed();
//      // checking that file was removed
      IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(FILE_NAME);
//      //CHECK STYLE OF PACKAGE
//      // check that file appeared in src folder
      IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
    }

   /**
    * Copy folder with nested folders in Package Explorer
    *
    * @throws Exception
    */
   @Test
   public void copyFoldersInPackageExplorerTest() throws Exception
   {
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
      IDE.LOADER.waitClosed();
      // copying
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("main");
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.COPY_SELECTED_ITEM);
      IDE.TOOLBAR.waitForButtonEnabled(ToolbarCommands.File.PASTE);
      IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
      // pasting copied file in to root
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.PASTE);
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
      IDE.LOADER.waitClosed();
   }

   /**
    * Delete folder with nested folders in Package Explorer
    *
    * @throws Exception
    */
   @Test
   public void deleteFoldersInPackageExplorerTest() throws Exception
   {
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
      IDE.LOADER.waitClosed();
      // deleting
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("main");
      IDE.DELETE.deleteSelectedItems();
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("main");
   }

   /**
    * Checking refresh in Package Explorer
    *
    * @throws Exception
    */
   @Test
   public void refreshPackageExplorerTest() throws Exception
   {
      // adding new file in to project via vfs and refresh.
      Link link = project.get(Link.REL_CREATE_FILE);
      VirtualFileSystemUtils.createFileFromLocal(link, XML_FILE_NAME, MimeType.TEXT_XML, filePath);
      IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
      IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(XML_FILE_NAME);
   }
}
