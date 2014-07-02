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
import com.codenvy.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.Map;

/**
 * @author Zaryana Dombrovskaya
 */
public class CheckFeedbackFromTopMenuInTemporaryWorkspaceTest extends BaseTest {

    private WebDriverWait wait;

    private static final String PROJECT = "supportInTemporary";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkSubmitFeedbackFormCalledFromTopMenu() throws Exception {

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js");
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();
        driver.get(factoryURL);
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.EXPLORER.waitForItem(PROJECT);
        wait = new WebDriverWait(driver, 30);
        String currentWinHandle = driver.getWindowHandle();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SUBMIT_FEEDBACK);
        switchToNonCurrentWindow(currentWinHandle);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='← Codenvy Feedback']")));
        driver.close();
        driver.switchTo().window(currentWinHandle);
    }
}
