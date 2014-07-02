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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class FactoryURLShareButtonsTest extends BaseTest {

    private static final String PROJECT = "FactoryTests";

    protected static Map<String, Link> project;

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
        IDE.MAIL_CHECK.cleanMailBox(USER_FOR_FACTORY_URL_CHECK, USER_PASSWORD);
    }

    @Test
    public void facebookShareButtonTest() throws Exception {
        wait = new WebDriverWait(driver, 30);

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        // check facebook sharing
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.clickOnShareViaFaceBook();

        CURRENT_WINDOW = driver.getWindowHandle();
        switchToApplicationWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html[@id='facebook']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='login_form']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='cancel']")));
        driver.findElement(By.xpath("//input[@name='cancel']")).click();

        driver.switchTo().window(CURRENT_WINDOW);
    }

    @Test
    public void googlePlusShareButtonTest() {
        wait = new WebDriverWait(driver, 30);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnShareViaGooglePlus();

        CURRENT_WINDOW = driver.getWindowHandle();
        switchToApplicationWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='gaia_loginform']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.google-header-bar")));
        driver.close();
        driver.switchTo().window(CURRENT_WINDOW);
    }

    @Test
    public void twitterShareButtonTest() {
        wait = new WebDriverWait(driver, 30);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnShareViaTwitter();

        CURRENT_WINDOW = driver.getWindowHandle();
        switchToApplicationWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[contains(.,'factory?id')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.button")));
        driver.close();
        driver.switchTo().window(CURRENT_WINDOW);
    }

    @Test
    public void sendFactoryURLViaEmailTest() throws Exception {
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnShareViaEmail();
        IDE.FACTORY_URL.waitSendFactoryURLViaEmailFormOpened();
        IDE.FACTORY_URL.typeEmailInSendFactoryURLViaEmailForm(USER_FOR_FACTORY_URL_CHECK);
        IDE.FACTORY_URL.clickSendButton();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();
        IDE.MAIL_CHECK.waitAndGetFactoryURL(USER_FOR_FACTORY_URL_CHECK, USER_PASSWORD);
        IDE.MAIL_CHECK.openFactoryURL();
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        assertTrue(driver.getCurrentUrl().contains(IDE_HOST + "/ide/tmp-"));
    }

    /** @param currentWin */
    protected void switchToApplicationWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {
            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
