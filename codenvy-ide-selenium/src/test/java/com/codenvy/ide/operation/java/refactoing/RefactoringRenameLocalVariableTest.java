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
package com.codenvy.ide.operation.java.refactoing;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Roman Iuvshin
 *
 */
public class RefactoringRenameLocalVariableTest extends RefactService {
    private static final String PROJECT = RefactoringRenameLocalVariableTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/operation/java/RefactoringTest.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void renameLocalVariableFromMenuEditTest() throws Exception {
        openRefacrProject(PROJECT);
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.GOTOLINE.goToLine(32);
        IDE.JAVAEDITOR.moveCursorRight(15);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REFACTOR, MenuCommands.Edit.RENAME);
        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("refactored");
        IDE.REFACTORING.clickRenameButton();
        IDE.LOADER.waitClosed();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        //checking that all fields was changed
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String refactored = \"\";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("refactored = \"Hello, \" + userName + \"!\";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("view.addObject(\"greeting\", refactored);");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "s");
        IDE.LOADER.waitClosed();
    }

    @Test
    public void renameLocalVariableUsingKeyboardShortcutsTest() throws Exception {
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.GOTOLINE.goToLine(32);
        IDE.JAVAEDITOR.moveCursorRight(15);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ALT.toString() + Keys.SHIFT.toString() + "r");

        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("renamed");
        IDE.REFACTORING.clickRenameButton();
        IDE.LOADER.waitClosed();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        //checking that all fields was changed
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String renamed = \"\";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("renamed = \"Hello, \" + userName + \"!\";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("view.addObject(\"greeting\", renamed);");

    }
}
