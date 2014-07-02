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

import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class RefactoringRenameSharedVariableTest extends RefactService {
    private static final String PROJECT = RefactoringRenameSharedVariableTest.class.getSimpleName();

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
    public void renameSharedVariableTest() throws Exception {
        openRefacrProject(PROJECT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("newNameOfTheVar");

        IDE.EDITOR.selectTab("GreetingController.java");

        IDE.GOTOLINE.goToLine(15);
        IDE.JAVAEDITOR.moveCursorRight(18);

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ALT.toString() + Keys.SHIFT.toString() + "r");

        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("newNameOfTheVar");
        IDE.REFACTORING.clickRenameButton();
        IDE.LOADER.waitClosed();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("public String newNameOfTheVar");


        IDE.EDITOR.selectTab("RefactMethods.java");

        // checking that variable changed in child class
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("newNameOfTheVar");
    }
}
