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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 @author Roman Iuvshin
 *
 */
public class ProjectCreationActionsInReadonlyModeWhenUserIsNotAuthenticatedTest extends BaseTest {
    private static final String LABEL = "You are using Codenvy with read-only privileges and you are not allowed to create a new project.\n"+
            "In order to create a new project, you must start by creating your Codenvy account and login.";

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
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(WORKSPACE_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(WORKSPACE_URL);
        } catch (Exception e) {
        }
    }

    @Test
    public void projectCreationActionsInReadonlyModeWhenUserIsNotAuthenticatedTest() {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.READ_ONLY_MODE.clickOnReadOnlyModeIndicator();
        checkReadOnlyModeView();
        IDE.WELCOME_PAGE.waitCreateNewProjectFromScratchBtn();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        checkReadOnlyModeView();
        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        checkReadOnlyModeView();
        IDE.WELCOME_PAGE.waitCloneGitRepositoryBtn();
        IDE.WELCOME_PAGE.clickOnCloneGitRepoBtn();
        checkReadOnlyModeView();

        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.READ_ONLY_MODE.clickOnReadOnlyModeIndicator();
        IDE.READ_ONLY_MODE.waitReadOnlyModeViewOpenedWhenNotAuthenticated();
        IDE.READ_ONLY_MODE.clickOnCancelButton();

        IDE.READ_ONLY_MODE.waitDisabledInviteUsersButton();
    }


    private void checkReadOnlyModeView() {
        IDE.READ_ONLY_MODE.waitReadOnlyModeViewOpenedWhenNotAuthenticated();
        IDE.READ_ONLY_MODE.waitTextOnlabel(LABEL);
        IDE.READ_ONLY_MODE.clickOnJoinButton();
        IDE.LOGIN.waitTenantAllLoginPage();
        driver.get(WORKSPACE_URL);
    }
}
