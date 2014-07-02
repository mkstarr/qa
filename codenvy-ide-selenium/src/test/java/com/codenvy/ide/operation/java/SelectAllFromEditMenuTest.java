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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SelectAllFromEditMenuTest extends ServicesJavaTextFuction {

    private static final String PROJECT = SelectAllFromEditMenuTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void selectAllTesUi() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openSpringJavaTetsFile();
        IDE.JAVAEDITOR.setCursorToActiveJavaEditor();

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SELECT_ALL);
        // need for setting selection area
        Thread.sleep(500);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.BACK_SPACE.toString());

        IDE.JAVAEDITOR.waitWhileJavaEditorIsEmpty();

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);

        Thread.sleep(TestConstants.WAIT_FOR_FILE_SAVING);

        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class SumController extends AbstractController {");

        String chekOnVfs =
                VirtualFileSystemUtils.getContent(
                        REST_URL + "contentbypath/" + PROJECT +
                        "/src/main/java/sumcontroller/SumController.java");
        assertTrue(chekOnVfs.contains("public class SumController extends AbstractController {"));

    }

}
