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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.IDE;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;

/** @author Roman Iuvshin */
public class CollaborationService extends BaseTest {

    protected static IDE IDE2;

    protected static WebDriver driver2;

    protected static void killSecondBrowser() {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                //  if first driver is google chrome kill firefox
                driver2.quit();
                break;
            default:
                //  if first driver is firefox kill google chrome
                driver2.quit();
        }
    }

    /** @throws IOException */
    public void initSecondBrowser() throws Exception {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                driver2 = new FirefoxDriver(profile);

                IDE2 = new IDE(LOGIN_URL, driver2);
                try {

                    driver2.manage().window().maximize();
                    driver2.get(LOGIN_URL);

                    IDE2.LOGIN.waitTenantLoginPage();

                    IDE2.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, NOT_ROOT_USER_PASSWORD);

                    if (IDE2.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
                        IDE2.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
                        IDE2.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
                    }

                } catch (Exception e) {
                }

                break;
            default:
                driver2 = new ChromeDriver();

                IDE2 = new IDE(LOGIN_URL, driver2);
                try {

                    driver2.manage().window().maximize();
                    driver2.get(LOGIN_URL);
                    IDE2.LOGIN.waitTenantLoginPage();
                    IDE2.LOGIN.waitTenantAllLoginPage();
                    IDE2.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, NOT_ROOT_USER_PASSWORD);

                    if (IDE2.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
                        IDE2.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
                        IDE2.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
                    }

                } catch (Exception e) {
                }
        }
    }

}
