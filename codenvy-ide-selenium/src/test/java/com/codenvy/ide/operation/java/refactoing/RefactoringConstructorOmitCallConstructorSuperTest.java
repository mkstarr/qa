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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class RefactoringConstructorOmitCallConstructorSuperTest extends RefactService {
    private static final String PROJECT = RefactoringConstructorOmitCallConstructorSuperTest.class.getSimpleName();

    private final String DELETE_FRAGMENT =
            "public Base(String inStr, double inVal)\n   {\n      this.initString = inStr;\n      \n      this.value = inVal;\n   }";

    private final String GENERATING_CONSTRUCTOR_WITH_OMIT =
            "   public ExtClass(double wideValue, String[] arr)\n   {\n      this.wideValue = wideValue;\n      this"
            +
            ".arr = arr;\n   }";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/operation/java/RefactoringOmitSuperTest.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @After
    public void closeFile() throws Exception {

        if (IDE.REFACTORING.generateConstrictorFormIsPresent()) {
            IDE.REFACTORING.constructorCancelClickBtn();
            IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        }

    }

    @Test
    public void checkOmitConstructorStateCallChkBox() throws Exception {
        openOmitCallConstructorSuperPrj(PROJECT);
        IDE.GOTOLINE.goToLine(5);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_CONSTRUCTOR);
        IDE.REFACTORING.waitGenerateConstructorForm();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(2);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(2);

        IDE.REFACTORING.waitOmitDefaultConstructorChkBoxIsDisabled();
        IDE.REFACTORING.constructorCancelClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        IDE.EDITOR.forcedClosureFile("ExtClass.java");
    }

    //TODO https://jira.codenvycorp.com/browse/IDE-2923 still actual for this test
    @Test
    public void generateWithOmitConstructorStateCallChkBox() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("Base.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.GOTOLINE.goToLine(10);
        for (int i = 0; i < 7; i++) {
            IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        }

        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(DELETE_FRAGMENT);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("Base.java");
        IDE.LOADER.waitClosed();

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("ExtClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitAllErrorMarkersIsDisAppear();


        IDE.GOTOLINE.goToLine(5);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_CONSTRUCTOR);
        IDE.REFACTORING.waitGenerateConstructorForm();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(2);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(2);
        IDE.REFACTORING.waitOmitDefaultConstructorChkBoxIsUnCheked();
        IDE.REFACTORING.constructorOmitConstructorClick();
        IDE.REFACTORING.waitOmitDefaultConstructorChkBoxIsChecked();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(GENERATING_CONSTRUCTOR_WITH_OMIT);
    }

}
