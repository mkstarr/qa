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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.sitecldide.CLDIDE;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */

public class LogoutTest extends BaseTest {

    private static String CURRENT_WINDOW;

    @Test
    public void logoutTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        CLDIDE CLDIDE = new CLDIDE(driver);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.LOGIN.waitLogoutButtonInIDEMenu();
        // open profile page
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Profile")));
        driver.findElement(By.linkText("Profile")).click();

        switchToNonCurrentWindow(CURRENT_WINDOW);
        CLDIDE.PROFILE.waitUserEmailInProfilePage(BaseTest.USER_NAME);
        driver.close();
        // back to ide tab
        driver.switchTo().window(CURRENT_WINDOW);
        IDE.LOGIN.logout();
        IDE.LOGIN.waitTenantLoginPage();

        // login as second user
        IDE.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, USER_PASSWORD);
        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.LOGIN.waitLogoutButtonInIDEMenu();
        driver.findElement(By.linkText("Profile")).click();
        // open profile page
        switchToNonCurrentWindow(CURRENT_WINDOW);
        driver.navigate().refresh();
        CLDIDE.PROFILE.waitUserEmailInProfilePage(BaseTest.NOT_ROOT_USER_NAME);

        // back to ide tab
        driver.switchTo().window(CURRENT_WINDOW);
        IDE.LOGIN.logout();
        IDE.LOGIN.waitTenantLoginPage();
        driver.get(WORKSPACE_URL);
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.LOGIN.waitSignInButtonInIDEMenu();
    }
}
