/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
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
import com.codenvy.ide.IDE;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Zaryana Dombrovskaya
 */
public class CheckMainWelcomeElementsPaidUserTest extends BaseTest {

    private String SUPPORT_IFRAME_LOCATOR = "//iframe[@class='uvw-dialog-iframe']";

    private String CLOSE_SUUPORT_DIALOG_BTN = "//div[@id[contains(., 'uvw-dialog')]]//button";

    private String CHECKED_SENF_BTN = "//button[text()='Send message']";

    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");
    public static String USER_LOGIN = IDE_SETTINGS.getString("ide.test.premium.user");
    public static final String URL = PROTOCOL + "://" + IDE_HOST + "/site/login";

    private WebDriverWait wait;

    @Override
    @Before
    public void start() throws IOException {
    }


    @BeforeClass
    public static void before() throws Exception {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new IDE(URL, driver);
        try {
            driver.manage().window().maximize();
            driver.get(URL);
            IDE.LOGIN.waitTenantAllLoginPage();
            IDE.LOGIN.tenantLogin(USER_LOGIN, USER_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void documentationTest() throws Exception {
        wait = new WebDriverWait(driver, 10);
        String currentWinHandle = driver.getWindowHandle();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.WELCOME_PAGE.waitDocumentationBtn();
        IDE.WELCOME_PAGE.clickOnDocumentationBtn();
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='User Docs']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='SDK Docs']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='API Docs']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);
    }

    @Test
    public void suportTest() throws Exception {
        wait = new WebDriverWait(driver, 10);
        IDE.EXPLORER.waitOpened();
        IDE.WELCOME_PAGE.waitSupportBtn();
        IDE.WELCOME_PAGE.clickSupportLink();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SUPPORT_IFRAME_LOCATOR)));
        WebElement supportIframe = driver.findElement(By.xpath(SUPPORT_IFRAME_LOCATOR));
        driver.switchTo().frame(supportIframe);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CHECKED_SENF_BTN)));
        IDE.selectMainFrame();
        driver.findElement(By.xpath(CLOSE_SUUPORT_DIALOG_BTN)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(SUPPORT_IFRAME_LOCATOR)));
    }

    @Test
    public void invitePeopleTest() throws Exception {
        IDE.WELCOME_PAGE.waitInvitePeopleBtn();
        IDE.WELCOME_PAGE.clickonInvitePeopleBtn();
        IDE.ASK_DIALOG.waitClosed();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
    }
}
