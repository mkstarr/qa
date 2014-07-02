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
package com.codenvy.ide.git;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */

public class ClonePrivateRepositoryTest extends GitServices {

    private static final String PROJECT = "codenvywso2conf";

    private static String CURRENT_WINDOW = null;

    private static String PRIVATE_REPO_URL = "https://git.cloudpreview.wso2.com/git/codenvy.com/codenvywso2conf.git";

    private WebDriverWait wait;

    private static String AUTH_LINK = "//a[@id='authorizeLink']";

    private static String USER_NAME = "//input[@id='username']";

    private static String PASSWORD = "//input[@id='password']";

    private static String LOGIN_BTN = "//button[@id='loginBtn']";

    private static String APPROVE_BTN = "//input[@id='approve']";

    private static String temp_ws_url;


    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void gitClonePrivateRepository() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        // Goto Codenvy and clone private organization test repository with default remote name.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(PRIVATE_REPO_URL);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();

        IDE.ASK_DIALOG.waitOpened();
        assertTrue(IDE.ASK_DIALOG.getTitle().contains("Authorization pending"));
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        switchToApplicationWindow(CURRENT_WINDOW);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AUTH_LINK)));
        driver.findElement(By.xpath(AUTH_LINK)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(USER_NAME)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PASSWORD)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LOGIN_BTN)));
        driver.findElement(By.xpath(USER_NAME)).sendKeys("admin@codenvy.com");
        driver.findElement(By.xpath(PASSWORD)).sendKeys(BaseTest.readCredential("wso2.pass"));


        driver.findElement(By.xpath(LOGIN_BTN)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(APPROVE_BTN)));
        driver.findElement(By.xpath(APPROVE_BTN)).click();
        driver.switchTo().window(CURRENT_WINDOW);
        IDE.LOADER.waitClosed();

        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PRIVATE_REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project type updated.");
        IDE.LOADER.waitClosed();

        temp_ws_url = driver.getCurrentUrl();
        IDE.LOGIN.logout();
    }

    @Test
    public void checkThatPermanentFreeWSNotConvertedToPrivateAfterCloningPrivateWS() throws Exception {
        driver.get(temp_ws_url);
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.OPEN.waitProject(PROJECT);
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
