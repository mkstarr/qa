package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/** @author Musienko Maxim */
public class CloneToTemporaryWsWithoutSshTest extends BaseTest {
    private String gitProjectForTempWsUrl =


            LOGIN_URL_VFS + "/factory?v=1.1&vcs=git&vcsurl=git@github.com:exoinvitemain/factoryUrlRepo.git&action=openproject&vcsinfo=true";


    @Override
    @Before
    public void start() throws Exception {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
//                driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
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
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.EXPLORER.waitOpened();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        waitOpenedGithubWin();
        switchToNonCurrentWindow(currentWin);
        IDE.GITHUB.waitAuthorizationPageOpened();

        IDE.GITHUB.waitAuthorizationPageOpened();
        IDE.GITHUB.typeLogin(USER_NAME);
        IDE.GITHUB.typePass(USER_PASSWORD);
        IDE.GITHUB.clickOnSignInButton();
        driver.switchTo().window(currentWin);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("Unnamed");
    }

    public void waitOpenedGithubWin() {
        IDE.DEBUGER.waitOpenedSomeWin();
    }


}
