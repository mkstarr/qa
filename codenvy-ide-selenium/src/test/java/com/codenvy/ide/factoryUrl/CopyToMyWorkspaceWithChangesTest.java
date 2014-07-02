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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */

public class CopyToMyWorkspaceWithChangesTest extends BaseTest {

    private static final String PROJECT = "copyToMYWS";

    private static final String NEW_PRJ_NAME = "changeName";

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
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + NEW_PRJ_NAME).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(NEW_PRJ_NAME);
        }
    }

    @Test
    public void сopyToMyWorkspaceWithChangesTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();

        driver.get(LOGIN_URL);
        Thread.sleep(1000);
        waitIdeLoginPage();
        IDE.LOGIN.waitTenantAllLoginPage();
        IDE.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, USER_PASSWORD);

        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }

        driver.get(factoryURL);
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        // do some changes
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        IDE.RENAME.rename(NEW_PRJ_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_PRJ_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor("<!-- thisIsChange -->");
        IDE.EDITOR.waitFileContentModificationMark("pom.xml");
        // check copy to my WS with not saved files
        IDE.FACTORY_URL.waitCopyToMyWorkspaceButton();
        IDE.FACTORY_URL.clickOnCopyToMyWorkspaceButton();
        IDE.INFORMATION_DIALOG.waitOpened();
        IDE.INFORMATION_DIALOG.waitMessage("Please, save all changes before copying projects.");
        IDE.INFORMATION_DIALOG.clickOk();
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitNoContentModificationMark("pom.xml");
        // deleting project on regular WS
        VirtualFileSystemUtils.delete(PROJECT);
        // check that project was deleted
        assertTrue(VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 404);

        IDE.FACTORY_URL.waitCopyToMyWorkspaceButton();
        IDE.FACTORY_URL.clickOnCopyToMyWorkspaceButton();
        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }

        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_PRJ_NAME);
        Assert.assertTrue(driver.getCurrentUrl().equals(WORKSPACE_URL));
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("<!-- thisIsChange -->");
    }
}
