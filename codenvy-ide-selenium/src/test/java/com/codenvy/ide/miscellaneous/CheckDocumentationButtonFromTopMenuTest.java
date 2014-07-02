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
import com.codenvy.ide.MenuCommands;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class CheckDocumentationButtonFromTopMenuTest extends BaseTest {
    private static String currentWinHandle = null;

    private WebDriverWait wait;

    @Test
    public void checkDocumentationCalledFromTopMenu() throws Exception {
        currentWinHandle = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 30);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.DOCUMENTATION);
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='User Docs']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='SDK Docs']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='API Docs']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);
    }

}
