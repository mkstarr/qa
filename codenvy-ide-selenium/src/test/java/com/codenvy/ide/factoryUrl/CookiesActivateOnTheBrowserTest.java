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
package com.codenvy.ide.factoryUrl;

import junit.framework.Assert;

import com.codenvy.ide.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

/** @author Roman Iuvshin */

public class CookiesActivateOnTheBrowserTest extends BaseTest {

    private WebDriverWait wait;

    /**
     * login using oauth
     *
     * @see com.codenvy.ide.BaseTest#start()
     */
    @Override
    @Before
    public void start() {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                capabilities.setCapability("chrome.switches", Arrays.asList("--disable-restore-session-state"));
                driver = new ChromeDriver(capabilities);
                break;
            default:
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                profile.setPreference("network.cookie.cookieBehavior", 2);
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(LOGIN_URL);
            waitIdeLoginPage();
            IDE.LOGIN.waitTenantAllLoginPage();
            IDE.LOGIN.tenantLogin(USER_NAME, USER_PASSWORD);

        } catch (Exception e) {
        }
    }

    @Test
    public void cookiesActivateOnTheBrowserTest() throws InterruptedException {
        wait = new WebDriverWait(driver, 30);

        if (BROWSER_COMMAND.equals("FIREFOX")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'YOUR COOKIES ARE DISABLED')]")));
            Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/error/error-cookies-disabled"));
        } else {
            // TODO for current version of CHROME  we can't disable cookies from code.
        }
    }
}
