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

/**
 * @author <a href="mailto:riuvshin@exoplatform.com">Roman Iuvshin</a>
 * @version $Id: 12:56:57 PM  Mar 14, 2013 $
 */
public class CheckSupportButtonsFromTopMenuTest extends BaseTest {

    private WebDriverWait wait;

    @Test
    public void checkContactSupportFormCalledFromTopMenu() throws Exception {
        wait = new WebDriverWait(driver, 30);
        String currentWinHandle = driver.getWindowHandle();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.CONTACT_SUPPORT);
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Codenvy Group Discussion']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);
    }

    @Test
    public void checkSubmitFeedbackFormCalledFromTopMenu() throws Exception {
        wait = new WebDriverWait(driver, 30);
        String currentWinHandle = driver.getWindowHandle();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SUBMIT_FEEDBACK);
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='← Codenvy Feedback']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);
    }
}
