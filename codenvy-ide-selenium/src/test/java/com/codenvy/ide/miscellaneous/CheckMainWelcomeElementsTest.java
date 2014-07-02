/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class CheckMainWelcomeElementsTest extends BaseTest {

    private WebDriverWait wait;

    @Test
    public void documentationTest() throws Exception {
        wait = new WebDriverWait(driver, 10);
        String currentWinHandle = driver.getWindowHandle();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.WELCOME_PAGE.waitDocumentationBtn();
        IDE.WELCOME_PAGE.clickOnDocumentationBtn();
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
        String currentWinHandle = driver.getWindowHandle();
        IDE.WELCOME_PAGE.waitSupportBtn();
        IDE.WELCOME_PAGE.clickSupportLink();
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Codenvy Group Discussion']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);

    }

    @Test
    public void invitePeopleTest() throws Exception {
        IDE.WELCOME_PAGE.waitInvitePeopleBtn();
        IDE.WELCOME_PAGE.clickonInvitePeopleBtn();
        IDE.ASK_DIALOG.waitClosed();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
    }
}
