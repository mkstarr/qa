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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/**
 * @author Anna Shumilova
 *
 */
public class AddBlockCommentsTest extends ServicesJavaTextFuction {
    private static final String PROJECT = AddBlockCommentsTest.class.getSimpleName();

    private static final String FILE_NAME = "JavaCommentsTest.java";

    private static final String FILE_NAME2 = "JavaCommentsTest2.java";

    private static final String FILE_NAME3 = "JavaCommentsTest3.java";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @After
    public void closeTab() {
        try {
            IDE.EDITOR.forcedClosureFile(1);

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
    public void addBlockComment() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openJavaCommenTest();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.GOTOLINE.goToLine(28);
        IDE.JAVAEDITOR.moveCursorRight(6);

        for (int i = 0; i < 5; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 20; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "/");
        String content = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/*     numbers.add(1);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("numbers.add(6);*/");
    }

    @Test
    public void overrideBlockComment() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME2);
        waitJavaCommentTestIsReady();

        IDE.GOTOLINE.goToLine(30);
        IDE.JAVAEDITOR.moveCursorRight(6);


        for (int i = 0; i < 2; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 15; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "/");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("numbers.add(4);*/");

        IDE.GOTOLINE.goToLine(33);
        IDE.JAVAEDITOR.moveCursorRight(6);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.END.toString());

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "/");

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/*numbers.add(5);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("numbers.add(6);*/");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("numbers.add(4);*/");
    }

    @Test
    public void addAndRemoveBlockCommentFromEditMenu() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME3);
        waitJavaCommentTestIsReady();
        IDE.GOTOLINE.goToLine(29);
        for (int i = 0; i < 6; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.ADD_BLOCK_COMMENT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/*      numbers.add(1);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("      numbers.add(6);\n*/");

        IDE.GOTOLINE.goToLine(29);
        for (int i = 0; i < 6; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REMOVE_BLOCK_COMMENT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*      numbers.add(1);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("      numbers.add(6);\n*/");
    }

}
