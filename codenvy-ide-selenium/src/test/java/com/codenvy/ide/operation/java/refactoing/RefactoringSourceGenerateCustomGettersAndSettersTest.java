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
 * @author Roman Iuvshin
 *
 */
public class RefactoringSourceGenerateCustomGettersAndSettersTest extends RefactService {
    private static final String PROJECT = RefactoringSourceGenerateCustomGettersAndSettersTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String GETTER_PRIVATE =
            "   /**\n    * @return the val\n    */\n   private final int getVal()\n   {\n      return val;\n   }";

    private static final String SETTER_PROTECTED =
            "   /**\n    * @param val the val to set\n    */\n   protected synchronized void setVal(int val)\n   {\n " +
            "     this.val = val;\n   }";

    private static final String GETTER_2 =
            "   /**\n    * @return the pbl\n    */\n   public String getPbl()\n   {\n      return pbl;\n   }";

    private static final String SETTER_2 =
            "   /**\n    * @param pbl the pbl to set\n    */\n   public void setPbl(String pbl)\n   {\n      this.pbl" +
            " = pbl;\n   }";

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
    public void generatePrivateFinalGetterTest() throws Exception {
        openRefacrProjectForGettersAndSetters(PROJECT);

        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnExpandClassFieldWithName("val");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("getVal()");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("setVal(int)");
        IDE.REFACTORING.clickOnGetterOrSetterWithName("getVal()");
        IDE.REFACTORING.clickOnPrivateLabel();
        IDE.REFACTORING.clickOnFinalLabel();
        IDE.REFACTORING.clickOkGettersAndSettersForm();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(GETTER_PRIVATE);
    }

    @Test
    public void generateProtectedSynchronizedSetterTest() throws Exception {
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnExpandClassFieldWithName("val");
        IDE.REFACTORING.waitCustomGetterOrSettersByName("setVal(int)");
        IDE.REFACTORING.clickOnGetterOrSetterWithName("setVal(int)");
        IDE.REFACTORING.clickOnProtectedLabel();
        IDE.REFACTORING.clickOnSynchronizedLabel();
        IDE.REFACTORING.clickOkGettersAndSettersForm();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(SETTER_PROTECTED);
    }
}
