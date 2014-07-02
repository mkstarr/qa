/*
 * 
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012]  [2013] Codenvy, S.A. 
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
package com.codenvy.ide.wso2;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.IDE;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 @author Roman Iuvshin
 *
 */

public class WSO2FactoryUrlTest extends BaseTest {

    private static final String PROJECT = "webapp_project";

    private static String CURRENT_WINDOW = null;

    private static String WSO2_URL = "https://git.cloudpreview.wso2.com/git/codenvy.com/codenvywso2conf.git";

    private static String WSO2_FACTORY_URL =
            PROTOCOL + "://" + IDE_HOST + "/factory?v=1.0&pname=webapp_project&wname=webapp_workspace&vcs=git&vcsurl=" + WSO2_URL +
            "&action=openproject&ptype=War&vcsinfo=true";

    private WebDriverWait wait;

    private static String AUTH_LINK = "//a[@id='authorizeLink']";

    private static String USER_NAME = "//input[@id='username']";

    private static String PASSWORD = "//input[@id='password']";

    private static String LOGIN_BTN = "//button[@id='loginBtn']";

    private static String APPROVE_BTN = "//input[@id='approve']";

    private static String temp_ws_url;

    private static IDE IDE2;

    private static WebDriver driver2;


    @Override
    @Before
    public void start() throws IOException {
    }


    @BeforeClass
    public static void prepare() throws IOException {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(LOGIN_URL);
        } catch (Exception e) {
        }

    }

    @AfterClass
    public static void tearDown() {
        killSecondBrowser();
    }

    @Test
    public void wso2FactoryUrlTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        driver.get(WSO2_FACTORY_URL);
        IDE.ASK_DIALOG.waitOpened();
        assertTrue(IDE.ASK_DIALOG.getTitle().contains("Authorization pending"));
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        switchToNonCurrentWindow(CURRENT_WINDOW);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AUTH_LINK)));
        driver.findElement(By.xpath(AUTH_LINK)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(USER_NAME)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PASSWORD)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LOGIN_BTN)));
        driver.findElement(By.xpath(USER_NAME)).sendKeys("admin@codenvy-dev.com");
        driver.findElement(By.xpath(PASSWORD)).sendKeys(BaseTest.readCredential("wso2.pass"));
        driver.findElement(By.xpath(LOGIN_BTN)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(APPROVE_BTN)));
        driver.findElement(By.xpath(APPROVE_BTN)).click();
        driver.switchTo().window(CURRENT_WINDOW);
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.OUTPUT.waitForSubTextPresent(WSO2_URL + " cloned successful!");
        temp_ws_url = driver.getCurrentUrl();
    }

    @Test
    public void checkThatWSBecomePrivateAfterClonningPrivateRepo() throws Exception {
        startSecondBrowser();
        driver2.get(temp_ws_url);
        IDE2.FACTORY_URL.waitWelcomeIframe();
        IDE2.selectMainFrame();
        IDE2.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE2.EXPLORER.waitForItemInProjectListNotPresent(PROJECT);
        IDE2.OPEN.waitProjectNotPresent(PROJECT);
    }

    private void startSecondBrowser() throws Exception {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                driver2 = new FirefoxDriver(profile);

                IDE2 = new com.codenvy.ide.IDE(LOGIN_URL, driver2);
                try {

                    driver2.manage().window().maximize();
                    driver2.get(LOGIN_URL);

                } catch (Exception e) {
                }

                break;
            default:
                driver2 = new ChromeDriver();

                IDE2 = new IDE(LOGIN_URL, driver2);
                try {

                    driver2.manage().window().maximize();
                    driver2.get(LOGIN_URL);

                } catch (Exception e) {
                }
        }
    }

    private static void killSecondBrowser() {
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
}
