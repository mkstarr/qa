/*
 * 
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2013] Codenvy, S.A. 
 *  All Rights Reserved.
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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

/**
 @author Roman Iuvshin
 *
 */

public class CreateJavaClassInDifferentSourceFolderTest extends BaseTest {

    private static final String PROJECT = "Test_prj_class";

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void createJavaClassInDifferentSourceFolderTest() throws Exception {

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Spring MVC application with AJAX usage.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/test/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/test/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("org.springframework.samples.mvc.ajax.account");
        IDE.PACKAGE_EXPLORER.openContextMenuOnSelectedItemInPackageExplorer("org.springframework.samples.mvc.ajax.account");
        IDE.PACKAGE_EXPLORER.clickOnItemInContextMenu(MenuCommands.New.NEW);
        IDE.MENU.clickOnNewMenuItem(MenuCommands.New.JAVA_CLASS);
        IDE.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE.CREATE_NEW_CLASS.waitSourceFolderValue("src/test/java");
        IDE.CREATE_NEW_CLASS.waitPackageValue("org.springframework.samples.mvc.ajax.account");
        IDE.CREATE_NEW_CLASS.typeClassName("NewClassInDifferentPackage");
        IDE.CREATE_NEW_CLASS.clickCreateBtn();
        IDE.PACKAGE_EXPLORER.waitItemPresentByPath(PROJECT + "/src/test/java/org/springframework/samples/mvc/ajax/account/NewClassInDifferentPackage.java" );
    }
}
