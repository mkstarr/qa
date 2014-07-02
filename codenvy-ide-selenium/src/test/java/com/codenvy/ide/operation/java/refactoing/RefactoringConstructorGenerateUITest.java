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

import java.io.IOException;
import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class RefactoringConstructorGenerateUITest extends RefactService {
    private static final String PROJECT = RefactoringConstructorGenerateUITest.class.getSimpleName();

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
    public void selectAllDeselectAllTest() throws Exception {
        openOneJavaClassForRefactoring(PROJECT);
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_CONSTRUCTOR);
        IDE.REFACTORING.waitGenerateConstructorForm();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(3);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(3);

        IDE.REFACTORING.constructorDeselectAllClickBtn();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(0);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(0);

        IDE.REFACTORING.constructorSelectAllClickBtn();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(3);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(3);
    }

    @Test
    public void selectStepByStepFeildsTest() throws Exception {
        IDE.REFACTORING.constructorSelectFieldByName("bidVal");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(2);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(2);

        IDE.REFACTORING.constructorSelectFieldByName("pbl");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(1);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(1);

        IDE.REFACTORING.constructorSelectFieldByName("val");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(0);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(0);

        IDE.REFACTORING.constructorSelectFieldByName("bidVal");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(1);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(1);

        IDE.REFACTORING.constructorSelectFieldByName("pbl");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(2);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(2);

        IDE.REFACTORING.constructorSelectFieldByName("val");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(3);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(3);

    }

}
