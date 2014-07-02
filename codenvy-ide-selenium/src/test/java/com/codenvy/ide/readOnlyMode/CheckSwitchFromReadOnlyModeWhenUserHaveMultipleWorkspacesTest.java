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
package com.codenvy.ide.readOnlyMode;

import com.codenvy.ide.BaseTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 @author Roman Iuvshin
 *
 */
public class CheckSwitchFromReadOnlyModeWhenUserHaveMultipleWorkspacesTest extends BaseTest {

    @Override
    @Before
    public void start() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(LOGIN_URL);
            IDE.LOGIN.waitTenantAllLoginPage();
            IDE.LOGIN.tenantLogin(NOT_ROOT_USER_NAME, USER_PASSWORD);
        } catch (Exception e) {
        }
    }

    @Test
    public void checkSwitchFromReadOnlyModeWhenUserHaveMultipleWorkspacesTest() throws InterruptedException {
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/codenvy");
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.LOADER.waitClosed();
        IDE.READ_ONLY_MODE.clickOnReadOnlyModeIndicator();
        IDE.READ_ONLY_MODE.waitReadOnlyModeViewOpenedWhenIsAuthenticated();
        IDE.READ_ONLY_MODE.clickOnJoinButton();
        IDE.SELECT_WORKSPACE.waitSelectWorkspacesPageOpened();
        IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
        IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(ADDITIONAL_TENANT_NAME);
    }

}
