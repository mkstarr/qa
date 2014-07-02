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
public class RefactoringSourceGenerateAllGettersTest extends RefactService {
    private static final String PROJECT = RefactoringSourceGenerateAllGettersTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String GETTERS =
            "   /**\n    * @return the bidVal\n    */\n   public double getBidVal()\n   {\n      return bidVal;\n   " +
            "}\n   /**\n    * @return the name\n    */\n   public static String getName()\n   {\n      return name;\n" +
            "   }\n   /**\n    * @return the pbl\n    */\n   public String getPbl()\n   {\n      return pbl;\n   }\n " +
            "  /**\n    * @return the val\n    */\n   public int getVal()\n   {\n      return val;\n   }";

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
    public void generateAllGettersTest() throws Exception {
        openRefacrProjectForGettersAndSetters(PROJECT);

        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnSelectGettersButton();
        IDE.REFACTORING.clickOkGettersAndSettersForm();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(GETTERS);
    }
}
