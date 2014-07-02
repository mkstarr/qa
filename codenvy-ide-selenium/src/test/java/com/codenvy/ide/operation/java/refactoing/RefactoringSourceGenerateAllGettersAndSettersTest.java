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
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Response;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Roman Iuvshin
 *
 */
public class RefactoringSourceGenerateAllGettersAndSettersTest extends RefactService {
    private static final String PROJECT = RefactoringSourceGenerateAllGettersAndSettersTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String GETTERS =
            "   /**\n    * @return the bidVal\n    */\n   public double getBidVal()\n   {\n      return bidVal;\n   " +
            "}\n   /**\n    * @param bidVal the bidVal to set\n    */\n   public void setBidVal(double bidVal)\n   " +
            "{\n      this.bidVal = bidVal;\n   }\n   /**\n    * @return the name\n    */\n   public static String " +
            "getName()\n   {\n      return name;\n   }\n   /**\n    * @param name the name to set\n    */\n   public " +
            "static void setName(String name)\n   {\n      GreetingController.name = name;\n   }\n   /**\n    * " +
            "@return the pbl\n    */\n   public String getPbl()\n   {\n      return pbl;\n   }\n   /**\n    * @param " +
            "pbl the pbl to set\n    */\n   public void setPbl(String pbl)\n   {\n      this.pbl = pbl;\n   }\n   " +
            "/**\n    * @return the val\n    */\n   public int getVal()\n   {\n      return val;\n   }\n   /**\n    *" +
            " @param val the val to set\n    */\n   public void setVal(int val)\n   {\n      this.val = val;\n   }";

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
    public void generateAllGettersAndSettersTest() throws Exception {
        openRefacrProjectForGettersAndSetters(PROJECT);

        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnSelectAllButton();
        IDE.REFACTORING.clickOkGettersAndSettersForm();
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        Response response =
                VirtualFileSystemUtils.get(
                        REST_URL + "contentbypath/" + PROJECT + "/src/main/java/helloworld/GreetingController.java");
        assertTrue(response.getData().contains(GETTERS));
    }
}
