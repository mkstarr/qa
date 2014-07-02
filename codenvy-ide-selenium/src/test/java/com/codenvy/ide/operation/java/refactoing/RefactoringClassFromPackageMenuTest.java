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

/**
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 *
 */
public class RefactoringClassFromPackageMenuTest extends RefactService {

    private static final String PROJECT = RefactoringClassFromPackageMenuTest.class.getSimpleName();

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
    public void changeClassFromPackageFromMenuInOpenedFiles() throws Exception {
        //Open project and rename base class
        openRefacrProject(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingController.java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("GreetingControllerRefact");
        IDE.REFACTORING.clickRenameButton();
        IDE.REFACTORING.waitRenameFormIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingControllerRefact.java");
        // check changes in extended class
        IDE.JAVAEDITOR.selectTab("RefactMethods.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class RefactMethods extends GreetingControllerRefact");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "s");
        IDE.JAVAEDITOR.waitNoContentModificationMark("RefactMethods.java");

        IDE.JAVAEDITOR.selectTab("GreetingControllerRefact.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class GreetingControllerRefact implements Controller");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "s");
        IDE.JAVAEDITOR.waitNoContentModificationMark("GreetingControllerRefact.java");
        IDE.LOADER.waitClosed();
    }

    @Test
    public void changeClassFromPackageFromContextMenuInOpenedFiles() throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingControllerRefact.java");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer("GreetingControllerRefact.java");
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.runCommand(MenuCommands.File.RENAME);
        IDE.CONTEXT_MENU.waitClosed();
        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("GreetingController");
        IDE.REFACTORING.clickRenameButton();
        IDE.REFACTORING.waitRenameFormIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");

        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class GreetingController implements Controller");

        IDE.JAVAEDITOR.selectTab("RefactMethods.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class RefactMethods extends GreetingController");

        //for next test
        IDE.EDITOR.forcedClosureFile(1);
        IDE.EDITOR.forcedClosureFile(1);

    }

    @Test
    public void changeClassFromEditMenuInClosedFiles() throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingController.java");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("GreetingControllerRefact");
        IDE.REFACTORING.clickRenameButton();
        IDE.REFACTORING.waitRenameFormIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingControllerRefact.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingControllerRefact.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class GreetingControllerRefact implements Controller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("RefactMethods.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("RefactMethods.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class RefactMethods extends GreetingControllerRefact");

        //for next test
        IDE.EDITOR.forcedClosureFile(1);
        IDE.EDITOR.forcedClosureFile(1);
    }

    @Test
    public void changeClassFromContextMenuInClosedFiles() throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("GreetingControllerRefact.java");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer("GreetingControllerRefact.java");
        IDE.CONTEXT_MENU.waitOpened();
        IDE.CONTEXT_MENU.runCommand(MenuCommands.File.RENAME);
        IDE.CONTEXT_MENU.waitClosed();
        IDE.REFACTORING.waitRenameForm();
        IDE.REFACTORING.typeNewName("GreetingController");
        IDE.REFACTORING.clickRenameButton();
        IDE.REFACTORING.waitRenameFormIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class GreetingController implements Controller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("RefactMethods.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("RefactMethods.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillContainSpecifiedText("public class RefactMethods extends GreetingController");
    }

}
