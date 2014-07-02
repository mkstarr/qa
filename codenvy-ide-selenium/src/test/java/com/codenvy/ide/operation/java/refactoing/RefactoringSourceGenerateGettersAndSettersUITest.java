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

import static org.junit.Assert.assertTrue;

/**
 * @author Roman Iuvshin
 * @author Musienko Maxim
 *
 */
public class RefactoringSourceGenerateGettersAndSettersUITest extends RefactService {
    private static final String PROJECT = RefactoringSourceGenerateGettersAndSettersUITest.class.getSimpleName();

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
    public void generateGettersAndSettersPopupNotificationsTest() throws Exception {
        openRefacrProjectForGettersAndSetters(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.WARNING_DIALOG.waitOpened();
        assertTrue(IDE.WARNING_DIALOG.getWarningMessage().equals(
                "The Generate Getters and Setters operation is only applicable to types and fields in source files."));
        IDE.WARNING_DIALOG.clickOk();
    }

    @Test
    public void selectAndDeselectNotExpandedItems() throws Exception {
        IDE.GOTOLINE.goToLine(14);
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE,
                            MenuCommands.Source.GENERATE_GETTERS_AND_SETTERS);
        IDE.REFACTORING.waitGenerateGettersAndSettersForm();
        IDE.REFACTORING.clickOnSelectAllButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(4);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(4);

        IDE.REFACTORING.clickOnDeselectButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(0);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(0);

    }

    @Test
    public void selectGettersTest() throws Exception {
        IDE.REFACTORING.clickOnSelectGettersButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(4);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(4);
        IDE.REFACTORING.waitRowIsSelectWitName("getName()");
        IDE.REFACTORING.waitRowIsSelectWitName("getPbl()");
        IDE.REFACTORING.waitRowIsSelectWitName("getVal()");
        IDE.REFACTORING.clickOnDeselectButton();
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(0);
    }

    @Test
    public void selectSettersTest() throws Exception {
        IDE.REFACTORING.clickOnSelectSettersButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(3);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(3);
        IDE.REFACTORING.waitRowIsSelectWitName("setBidVal(double)");
        IDE.REFACTORING.waitRowIsSelectWitName("setPbl(String)");
        IDE.REFACTORING.waitRowIsSelectWitName("setVal(int)");
        IDE.REFACTORING.clickOnDeselectButton();
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(0);
    }

    @Test
    public void selectAndDeselectAllExpandItemsTest() throws Exception {
        IDE.REFACTORING.clickOnSelectAllButton();
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(11);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(11);
        IDE.REFACTORING.waitRowIsSelectWitName("bidVal");
        IDE.REFACTORING.waitRowIsSelectWitName("getBidVal()");
        IDE.REFACTORING.waitRowIsSelectWitName("setBidVal(double)");
        IDE.REFACTORING.waitRowIsSelectWitName("name");
        IDE.REFACTORING.waitRowIsSelectWitName("getName()");
        IDE.REFACTORING.waitRowIsSelectWitName("pbl");
        IDE.REFACTORING.waitRowIsSelectWitName("getPbl()");
        IDE.REFACTORING.waitRowIsSelectWitName("setPbl(String)");
        IDE.REFACTORING.waitRowIsSelectWitName("val");
        IDE.REFACTORING.waitRowIsSelectWitName("getVal()");
        IDE.REFACTORING.waitRowIsSelectWitName("setVal(int)");
        IDE.REFACTORING.clickOnDeselectButton();
        IDE.REFACTORING.waitIsNumberAreasRowsIsSelected(0);
        IDE.REFACTORING.waitIsNumberAreasInputsIsSelected(0);

    }

}
