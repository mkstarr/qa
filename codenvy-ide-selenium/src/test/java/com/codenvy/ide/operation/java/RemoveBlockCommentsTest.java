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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertFalse;

/**
 * @author Anna Shumilova
 *
 */
public class RemoveBlockCommentsTest extends ServicesJavaTextFuction {

    private static final String PROJECT    = RemoveBlockCommentsTest.class.getSimpleName();

    private static final String FILE_NAME  = "JavaRemoveCommentsTest.java";

    private static final String FILE_NAME2 = "JavaRemoveCommentsTest2.java";

    private static final String FILE_NAME3 = "JavaRemoveCommentsTest3.java";

    private static final String FILE_NAME4 = "JavaRemoveCommentsTest4.java";

    private static final String FILE_NAME5 = "JavaRemoveCommentsTest5.java";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaRemoveCommentsTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @After
    public void closeTab() {
        try {
            IDE.EDITOR.closeFile(1);

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

    @After
    public final void closeEditor() {
        try {
            IDE.EDITOR.forcedClosureFile(1);
        } catch (Exception e) {
        }
    }

    @Test
    public void removeBlockComment() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openJavaRemoveCommenTest();
        IDE.GOTOLINE.goToLine(30);

        for (int i = 0; i < 3; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 23; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "\\");
        // need wait for reparce
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(5);");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }

    @Test
    public void removeBlockCommentByInnerSelection() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME2);
        waitJavaRemoveCommentTestIsReady();

        IDE.GOTOLINE.goToLine(31);
        IDE.JAVAEDITOR.moveCursorRight(6);
        for (int i = 0; i < 1; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 22; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "\\");
        // need wait for reparce
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(5);");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }

    @Test
    public void removeBlockCommentByStartSelection() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME3);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME3);
        waitJavaRemoveCommentTestIsReady();

        IDE.GOTOLINE.goToLine(29);
        IDE.JAVAEDITOR.moveCursorRight(6);
        for (int i = 0; i < 2; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 22; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "\\");
        String content = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(5);");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }

    @Test
    public void removeBlockCommentByEndSelection() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME4);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME4);
        waitJavaRemoveCommentTestIsReady();

        IDE.GOTOLINE.goToLine(32);
        IDE.JAVAEDITOR.moveCursorRight(6);
        for (int i = 0; i < 2; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        }

        for (int i = 0; i < 22; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_RIGHT);
        }
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT + "\\");
        String content = IDE.JAVAEDITOR.getVisibleTextFromJavaEditor();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(2);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(5);");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }

    @Test
    public void removeBlockCommentInTwoBlosks() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME5);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME5);
        waitJavaRemoveCommentTestIsReady();
        IDE.GOTOLINE.goToLine(11);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.SHIFT.toString() + Keys.ARROW_DOWN);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REMOVE_BLOCK_COMMENT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(1);");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("/*numbers.add(2);");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
    }
}