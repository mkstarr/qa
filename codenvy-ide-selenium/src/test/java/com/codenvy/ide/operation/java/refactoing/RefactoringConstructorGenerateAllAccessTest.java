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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class RefactoringConstructorGenerateAllAccessTest extends RefactService {
    private static final String PROJECT = RefactoringConstructorGenerateAllAccessTest.class.getSimpleName();

    private final String[] commentGen = {"/**\n", "* @param pbl\n", "* @param val\n", "* @param bidVal\n", "*/" };
    //"/**\n      * @param pbl\n      * @param val\n      * @param bidVal\n      */";

    private final String[] publicAccessGenerate =
            {"public GreetingController_2(", "int val", "double bidVal", "{\n      super();\n", "this.pbl =", "pbl;\n",
             "this.val = val;\n", "this.bidVal = bidVal;\n" };


    private final String[] protectedAccessGenerate =
            {"protected GreetingController(", "int val", "double bidVal", "{\n      super();\n", "this.pbl =", "pbl;\n",
             "this.val = val;\n", "this.bidVal = bidVal;\n" };


//    "public GreetingController_2(String pbl, int val, double bidVal)\n   {\n      super();\n      this.pbl = "
//            +
//            "pbl;\n      this.val = val;\n      this.bidVal = bidVal;\n   }";

//    private final String PROTECTED_ACCESS_GENERATE =
//            "protected GreetingController(String pbl, int val, double bidVal)\n   {\n      super();\n      this.pbl ="
//            +
//            " pbl;\n      this.val = val;\n      this.bidVal = bidVal;\n   }";


    private final String[] defaultAccessGenerate =
            {"GreetingController(", "int val", "double bidVal", "{\n      super();\n", "this.pbl =", "pbl;\n",
             "this.val = val;\n", "this.bidVal = bidVal;\n" };


    private final String[] privateAccessGenerate =
            {"private GreetingController(", "int val", "double bidVal", "{\n      super();\n", "this.pbl =", "pbl;\n",
             "this.val = val;\n", "this.bidVal = bidVal;\n" };


//    private final String DEFAULT_ACCESS_GENERATE =
//            "GreetingController(String pbl, int val, double bidVal)\n   {\n      super();\n      this.pbl = pbl;\n   "
//            +
//            "   this.val = val;\n      this.bidVal = bidVal;\n   }";

//    private final String PRIVATE_ACCESS_GENERATE =
//            "private GreetingController(String pbl, int val, double bidVal)\n   {\n      super();\n      this.pbl = "
//            +
//            "pbl;\n      this.val = val;\n      this.bidVal = bidVal;\n   }";

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

    @After
    public void closeAndIsNotSaveFile() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
        IDE.EDITOR.waitTabNotPresent("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    @Test
    public void generatePublicAccessForAllFields() throws Exception {
        openSecondJavaClassForRefactoring(PROJECT);
        recallMenu();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        // Warn on this moment does not generate see issue IDE-2293
        checkGeneratedComment();
        checkPublicAccessGenerateConstructor();
    }

    @Test
    public void generateProtectedAccessForAllFields() throws Exception {
        recallMenu();
        IDE.REFACTORING.constructorProtectedRadioBtnClick();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        checkProtectedAccessGenerateConstructor();
    }

    @Test
    public void generateDefaultAccessForAllFields() throws Exception {
        recallMenu();
        IDE.REFACTORING.constructorDefaultRadioBtnClick();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        checkDefaultcAccessGenerateConstructor();
    }

    @Test
    public void generatePrivatetAccessForAllFields() throws Exception {
        recallMenu();
        IDE.REFACTORING.constructorPrivatedRadioBtnClick();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        checkPrivateAccessGenerateConstructor();
    }

    private void recallMenu() throws Exception {
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_CONSTRUCTOR);
        IDE.REFACTORING.waitGenerateConstructorForm();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(3);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(3);

    }


    private void checkPublicAccessGenerateConstructor() {
        for (String partsComment : publicAccessGenerate) {
            IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(partsComment);
        }
    }

    private void checkDefaultcAccessGenerateConstructor() {
        for (String partsComment : defaultAccessGenerate) {
            IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(partsComment);
        }
    }


    private void checkProtectedAccessGenerateConstructor() {
        for (String partsComment : protectedAccessGenerate) {
            IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(partsComment);
        }
    }

    private void checkPrivateAccessGenerateConstructor() {
        for (String partsComment : privateAccessGenerate) {
            IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(partsComment);
        }
    }

    private void checkGeneratedComment() {
        for (String partsComment : commentGen) {
            IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(partsComment);
        }
    }

}
