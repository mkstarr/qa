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
package com.codenvy.ide.factoryUrl;

import junit.framework.Assert;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Roman Iuvshin */

public class CopyToMyWorkspaceWithSameProjectNameTest extends BaseTest {

    private static final String PROJECT = "copyToMYWS1";

    private static final String PROJECT_2 = "copyToMYWS1_1";

    private static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT_2).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT_2);
        }
    }

    @Test
    public void copyToMyWorkspaceFewProjectsTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();

        driver.get(LOGIN_URL);
        waitIdeLoginPage();
        IDE.LOGIN.waitTenantAllLoginPage();
        IDE.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, USER_PASSWORD);

        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }

        driver.get(factoryURL);
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.LOADER.waitClosed();

        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();

        IDE.FACTORY_URL.waitCopyToMyWorkspaceButton();
        IDE.FACTORY_URL.clickOnCopyToMyWorkspaceButton();

        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }

        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_2);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT_2);
        Assert.assertTrue(driver.getCurrentUrl().equals(WORKSPACE_URL));
    }
}
