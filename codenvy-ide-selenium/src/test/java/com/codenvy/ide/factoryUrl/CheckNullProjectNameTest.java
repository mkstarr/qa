/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

/** @author Musienko Maxim */
public class CheckNullProjectNameTest extends BaseTest {

    public static String PROJECT = "null";

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void copyPrivareLoginAsLoginUserTest() throws Exception {

        driver.get(new CopyPrivateProjectToMyWorkspaceTest().getFactoryUrlFromPrivateRepo());
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        String currentWin = driver.getWindowHandle();
        if (IDE.GITHUB.apicheckPresentTockensInNotPresentOnGithub()) {
            IDE.ASK_DIALOG.waitOpened();
            IDE.ASK_DIALOG.clickYes();
            waitOpenedGithubWin();
            switchToNonCurrentWindow(currentWin);
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.typeLogin(USER_NAME);
            IDE.GITHUB.typePass(USER_PASSWORD);
            IDE.GITHUB.clickOnSignInButton();
            IDE.GITHUB.clickOnAuthorizeBtn();
            driver.switchTo().window(currentWin);
        }
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-cloud-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("null");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("Unnamed");

    }

    private void waitOpenedGithubWin() {
        IDE.DEBUGER.waitOpenedSomeWin();
    }
}