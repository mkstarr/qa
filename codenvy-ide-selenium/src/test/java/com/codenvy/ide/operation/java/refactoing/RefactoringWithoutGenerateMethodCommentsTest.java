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

import static org.junit.Assert.assertFalse;

/**
 * @author Musienko Maxim
 *
 */
public class RefactoringWithoutGenerateMethodCommentsTest extends RefactService {
    private static final String PROJECT = RefactoringWithoutGenerateMethodCommentsTest.class.getSimpleName();

    private final String COMMENT_FOR_GETTER_BIDVAL = "/**\n    * @return the bidVal\n    */";

    private final String COMMENTFOR_SETETR_BIDVAL = "/**\n    * @param bidVal the bidVal to set\n    */";

    private final String COMMENT_FOR_GETTER_NAME = "/**\n    * @return the name\n    */";

    private final String COMMENT_FOR_SETTER_NAME = "/**\n    * @param name the name to set\n    */";

    private final String COMMENT_FOR_GETTER_PBL = "/**\n    * @return the pbl\n    */";

    private final String COMMENT_FOR_SETTER_PBL = "/**\n    * @param pbl the pbl to set\n    */";

    private final String COMMENT_FOR_GETTER_VAL = "/**\n    * @return the val\n    */";

    private final String COMMENT_FOR_SETTER_VAL = "/**\n    * @param val the val to set\n    */";

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
    public void generateGettersAndSettersWithoutComments() throws Exception {
        openOneJavaClassForRefactoring(PROJECT);
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnSelectAllButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(4);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(4);
        IDE.REFACTORING.clickGenerateMethodCommentsChkBox();
        IDE.REFACTORING.waitGenerateMethodCommandsUnchecked();
        IDE.REFACTORING.clickOkGettersAndSettersForm();

        String content =
                VirtualFileSystemUtils
                        .getContent(REST_URL + "contentbypath/" + PROJECT +
                                    "/src/main/java/helloworld/GreetingController.java");

        assertFalse(content.equals(COMMENT_FOR_GETTER_BIDVAL) && content.equals(COMMENTFOR_SETETR_BIDVAL)
                    && content.equals(COMMENT_FOR_GETTER_NAME) && content.equals(COMMENT_FOR_SETTER_NAME)
                    && content.equals(COMMENT_FOR_GETTER_PBL) && content.equals(COMMENT_FOR_SETTER_PBL)
                    && content.equals(COMMENT_FOR_GETTER_VAL) && content.equals(COMMENT_FOR_SETTER_VAL));

    }
}
