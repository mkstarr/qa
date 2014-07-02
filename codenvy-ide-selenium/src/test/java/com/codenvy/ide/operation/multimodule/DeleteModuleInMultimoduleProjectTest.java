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
package com.codenvy.ide.operation.multimodule;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

/**
 @author Roman Iuvshin
 *
 */
public class DeleteModuleInMultimoduleProjectTest extends BaseTest {


    private static final String PROJECT = "testPrjForDeleteModule";

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void deleteModuleTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectMavenMultiModuleTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Maven Multi Module Project");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-webapp");

        // remove frist module
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("my-lib");
        IDE.DELETE.deleteSelectedItems();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");

        // remove second module
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("my-webapp");
        IDE.DELETE.deleteSelectedItems();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("my-webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
    }
}
