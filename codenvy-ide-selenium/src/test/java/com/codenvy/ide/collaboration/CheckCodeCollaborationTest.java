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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */

public class CheckCodeCollaborationTest extends CollaborationService {

    private static final String PROJECT = CheckCodeCollaborationTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String CODE_VARIABLE = "int value = 100500;";

    private static final String VARIABLE_1 = "   private int number = 17;";

    private static final String VARIABLE_2 = "   private String name = \"Name\";";

    private static final String FILE_CONTENT = "package helloworld;\n" +
                                               "\n" +
                                               "public class NewClass\n" +
                                               "{\n" +
                                               "   private int number = 17;\n" +
                                               "int value = 100500;\n" +
                                               "   public double bigValuenewMTd()\n" +
                                               "   {\n" +
                                               "      final double pi = 3.14159265 * value;\n" +
                                               "      return pi;\n" +
                                               "   }\n" +
                                               "   private String name = \"Name\";\n" +
                                               "}\n\n";


    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void typeInEditorTest() throws Exception {
        initSecondBrowser();

        // opening project from first user
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        // opening project from second user
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        // open class as first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("NewClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickContinueBtn();
        IDE.ASK_DIALOG.waitClosed();
        // open class as second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("NewClass.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE2.PROGRESS_BAR.waitProgressBarControlClose();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);

        // type as first user and check for changes from second user
        IDE.GOTOLINE.goToLine(5);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.moveCursorUp(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(CODE_VARIABLE);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(CODE_VARIABLE);

        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(CODE_VARIABLE);

        // type as second user and check for changes from first user
        IDE2.GOTOLINE.goToLine(8);
        IDE2.JAVAEDITOR.moveCursorRight(34);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(" * value");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("final double pi = 3.14159265 * value;");

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("final double pi = 3.14159265 * value;");
    }

    @Test
    public void simultaneousEditingTest() throws Exception {

        IDE.GOTOLINE.goToLine(5);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.moveCursorUp(1);

        IDE2.GOTOLINE.goToLine(11);
        IDE2.JAVAEDITOR.moveCursorDown(1);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE2.JAVAEDITOR.moveCursorUp(1);

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(VARIABLE_1);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(VARIABLE_2);
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().equals(FILE_CONTENT));
    }
}
