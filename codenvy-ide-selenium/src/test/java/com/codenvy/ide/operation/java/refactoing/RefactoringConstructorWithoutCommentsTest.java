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

import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */

//TODO should be completed
public class RefactoringConstructorWithoutCommentsTest extends RefactService {
    private static final String PROJECT = "WithoutConstructor";

    private String publicAccessGenerateTwofields =
            "   public GreetingController(int val, double bidVal)\n   {\n      super();\n      this.val = val;\n     " +
            " this.bidVal = bidVal;\n   }\n";


    private String publicAccessGenerateOneFields =
            "   public GreetingController(double bidVal)\n   {\n      super();\n      this.bidVal = bidVal;\n   }\n";

    private String publicAccessComment = "   /**\n    * @param pbl\n    * @param bidVal\n    */\n\n";

    protected static Map<String, Link> project;


    public RefactoringConstructorWithoutCommentsTest() {
        if (BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX)) {
            publicAccessGenerateTwofields = "   public GreetingController(double bidVal, int val)\n" +
                                            "   {\n" +
                                            "      super();\n" +
                                            "      this.bidVal = bidVal;\n" +
                                            "      this.val = val;\n" +
                                            "   }";

            publicAccessGenerateOneFields = "   public GreetingController(double bidVal)\n" +
                                            "   {\n" +
                                            "      super();\n" +
                                            "      this.bidVal = bidVal;\n" +
                                            "   }";
        }
    }

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
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @After
    public void reopenFile() throws Exception {
        IDE.EDITOR.forcedClosureFile(1);
        IDE.EDITOR.waitTabNotPresent("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

    }

    @Test
    public void generateSameConstructorsWithoutComments() throws Exception {
        openRefacrProjectForGettersAndSetters(PROJECT);
        recallMenu();
        IDE.REFACTORING.constructorSelectFieldByName("pbl");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(2);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(2);
        IDE.REFACTORING.constructorGenerateCommentsChkBoxClick();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(publicAccessGenerateTwofields);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(publicAccessComment);
    }

    @Test
    public void generateOneConstructorsWithoutComments() throws Exception {
        recallMenu();
        IDE.REFACTORING.constructorSelectFieldByName("pbl");
        IDE.REFACTORING.constructorSelectFieldByName("val");
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(1);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(1);
        IDE.REFACTORING.constructorGenerateCommentsChkBoxClick();
        IDE.REFACTORING.constructorOkClickBtn();
        IDE.REFACTORING.waitGenerateConstructorFormIsClosed();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(publicAccessGenerateOneFields);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(publicAccessComment);
    }

    private void recallMenu() throws Exception {
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_CONSTRUCTOR);
        IDE.REFACTORING.waitGenerateConstructorForm();
        IDE.REFACTORING.waitNumConstructorFieldsIsHiglighted(3);
        IDE.REFACTORING.waitNumConstructorFieldsCheckBoxIsCheked(3);

    }
}
