/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */
public class GitPaginationProblemTest extends BaseTest {
    private static final String PROJECT = "factory_prj";

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
    public void checkButtonDisabledStateTest() throws Exception {
        //go to the IDE, call Factory URL, check init states into customize section
        IDE.EXPLORER.waitOpened();
        String currentWin = driver.getWindowHandle();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js");
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryUrl = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();
        driver.get(factoryUrl);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.WELCOME);
        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();

        waitAppearGithubWin();
        switchToNonCurrentWindow(currentWin);


        IDE.GITHUB.waitAuthorizationPageOpened();
        IDE.GITHUB.typeLogin("Karlsson82");
        IDE.GITHUB.typePass("myprofit82");
        IDE.GITHUB.clickOnSignInButton();
        driver.switchTo().window(currentWin);
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormOpened();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.selectProjectByName("android");
//sc
        for (int i = 0; i < 30; i++) {
            new Actions(driver).sendKeys(Keys.ARROW_DOWN.toString()).build().perform();

        }
        IDE.LOADER.waitClosed();

        assertTrue(IDE.IMPORT_FROM_GITHUB.getProjectsCount() > 38);


    }

    private void waitAppearGithubWin() {
        IDE.DEBUGER.waitOpenedSomeWin();
    }
}
