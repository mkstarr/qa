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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Musienko Maxim
 *
 */
public class RefactoringAllowSettersForFinalFieldChekBoxTest
                                                            extends RefactService {
    private static final String        PROJECT                 = RefactoringAllowSettersForFinalFieldChekBoxTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private final String               CONTENTAFTERREFACTORING =
                                                                 "/**\n    * @param pi the pi to set\n    */\n   public void setPi(double pi)\n   {\n      this.pi = pi;\n   }\n   double pi = 3.14159;\n   \n   public String pbl = \"public string\";\n\n   protected int val = 24546;\n\n   public static String name = \"My name is...\";\n\n   private double bidVal = 4654.44665645;";

    @BeforeClass
    public static void before() {

        try {
            project =
                      VirtualFileSystemUtils.importZipProject(PROJECT,


                                                              "src/test/resources/org/exoplatform/ide/operation/java/RefactoringWitFinalVarTest.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void tryGenerateDettersWithoutCheckBox() throws Exception {
        openOneJavaClassForRefactoring(PROJECT);
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.clickOnExpandClassFieldWithName("pi");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("getPi()");
        IDE.REFACTORING.waitCustomGetterOrSettersByNameIsDissapear("setPi(double)");

    }

    @Test
    public void tryGenerateDettersWithCheckBox() throws Exception {
        IDE.REFACTORING.clickOnCheckBoxAllowSettersFialFields();
        IDE.REFACTORING.clickOnExpandClassFieldWithName("pi");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("getPi()");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("setPi(double)");
        IDE.REFACTORING.clickOnGetterOrSetterWithName("setPi(double)");
        IDE.REFACTORING.clickOkGettersAndSettersForm();
        IDE.REFACTORING.waitGenerateGettersAndSettersFormIsClose();
        IDE.LOADER.waitClosed();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(CONTENTAFTERREFACTORING);
//        String content =
//                         VirtualFileSystemUtils
//                                               .getContent(WS_URL + "contentbypath/" + PROJECT
//                                                           + "/src/main/java/helloworld/GreetingController.java");
//        assertTrue(content.contains(CONTENTAFTERREFACTORING));
    }

}
