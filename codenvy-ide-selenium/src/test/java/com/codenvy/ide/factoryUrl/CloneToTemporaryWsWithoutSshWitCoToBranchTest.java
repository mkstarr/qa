/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
import com.codenvy.ide.MenuCommands;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class CloneToTemporaryWsWithoutSshWitCoToBranchTest extends BaseTest {
    private String gitProjectForTempWsUrl =


            LOGIN_URL_VFS + "/factory?v=1.1&vcs=git&vcsurl=git@github" +
            ".com:exoinvitemain/factoryUrlRepo.git&action=openproject&vcsinfo=true&vcsbranch=CoDenVyTesT";

    String idCommit = "e2e6388424f60048c8325b2d32c4218600a9c101";


    @Override
    @Before
    public void start() throws Exception {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile(); //ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(gitProjectForTempWsUrl);
        } catch (Exception e) {
        }
    }


    @Test
    public void cloneProjectToTempWsWithoutSsh() throws Exception {
        IDE.EXPLORER.waitOpened();
        String currentWin = driver.getWindowHandle();
        IDE.EXPLORER.waitOpened();
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        waitOpenedGithubWin();
        switchToNonCurrentWindow(currentWin);
        IDE.GITHUB.waitAuthorizationPageOpened();
        IDE.GITHUB.typeLogin(USER_NAME);
        IDE.GITHUB.typePass(USER_PASSWORD);
        IDE.GITHUB.clickOnSignInButton();
        driver.switchTo().window(currentWin);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("Unnamed");
        IDE.OUTPUT.waitForSubTextPresent("Switching to CoDenVyTesT branch.");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitBranchIsCheckout("CoDenVyTesT");
        IDE.GIT.clickCloseBranchBtn();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        waitExpectedCommitId();

    }

    public void waitOpenedGithubWin() {
        IDE.DEBUGER.waitOpenedSomeWin();
    }

    public void waitExpectedCommitId() {
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return IDE.GIT.getCommitRevisionFromHistoryPanel().contains(idCommit);
            }
        });

    }

}
