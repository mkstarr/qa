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
import com.codenvy.ide.MenuCommands;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

/**
 * @author Zaryana Dombrovskaya
 */
public class CheckSupportFeedbackItemInLoginPageTest extends BaseTest {

    private WebDriverWait wait;

    @Override
    @Before
    public void start() throws Exception {

        String URL = PROTOCOL + "://" + IDE_HOST + "/site/login";

        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(URL, driver);
        try {
            driver.manage().window().maximize();
            driver.get(URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void checkContactSupportFormCalledFromTopMenu() throws Exception {
        wait = new WebDriverWait(driver, 30);
        String currentWinHandle = driver.getWindowHandle();
        driver.findElement(By.partialLinkText("Feedback & support")).click();
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Codenvy Group Discussion']")));
    }

}
