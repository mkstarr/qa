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
import com.codenvy.ide.MenuCommands;
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
public class CheckSupportButtonsFromTopMenuPaidUserTest extends BaseTest {

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
    public void checkContactSupportFormCalledFromTopMenu() throws Exception {
        wait = new WebDriverWait(driver, 30);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.CONTACT_SUPPORT);

        // wait iframe with support form
        WebElement iframe = driver.findElement(By.xpath("//iframe[@class='uvw-dialog-iframe'][last()]"));
        driver.switchTo().frame(iframe);
        // wait contact us form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='contact_us']")));
        driver.findElement(By.xpath("//form[@id='contact_us']")).isDisplayed();

        driver.switchTo().defaultContent();
        // close support form
        driver.findElement(By.xpath("(//button[text()='Close Dialog'])[last()]")).click();
    }

    @Test
    public void checkSubmitFeedbackFormCalledFromTopMenu() throws Exception {
        wait = new WebDriverWait(driver, 30);

        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SUBMIT_FEEDBACK);
        // wait iframe with support form
        WebElement iframe = driver.findElement(By.xpath("//iframe[@class='uvw-dialog-iframe'][last()]"));
        driver.switchTo().frame(iframe);
        // wait feedback form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='create_idea']")));
        driver.findElement(By.xpath("//form[@id='create_idea']")).isDisplayed();
    }
}
