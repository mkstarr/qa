/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;

/** @author Musienko Maxim */
public class CopyPrivateProjectInReadOnlyTest extends BaseTest {
    private static final String                              PROJECT  = "null";
    private              CopyPrivateProjectToMyWorkspaceTest testInst = new CopyPrivateProjectToMyWorkspaceTest();

    private static String URLToPrivateRepoWs;

    private String welcomeTabMessage =
            "This project has been factory-created into a temporary workspace on Codenvy.\n" +
            "\n" +
            "Codenvy is your complete cloud environment to:\n" +
            "\n" +
            "Code, Commit\n" +
            "Build, Package, Test\n" +
            "Run, Debug\n" +
            "PaaS Deploy\n" +
            "Collaborate, Share\n" +
            "\n" +
            "Home Page | Docs | Support | Feature Vote\n" +
            "\n" +
            "\n" +
            "A temporary workspace will be deleted if you are inactive or close the browser.\n" +
            "\n" +
            "To save your work, Login or Create your free Account.\n" +
            "\n" +
            "Terms of Service";


    private String discoverTabMessage = "Welcome back!\n" +
                                        "\n" +
                                        "\n" +
                                        "This project has been factory-created into a secured private temporary workspace. Your code is " +
                                        "private and this temporary workspace will remain only accessible by you.\n" +
                                        "\n" +
                                        "You have full code, build, run and deploy capabilities in this temporary workspace.\n" +
                                        "\n" +
                                        "This workspace, and its contents, will be deleted if you are inactive or close the browser.\n" +
                                        "\n" +
                                        "Your work can be persisted to your named workspace. We will move all projects from this " +
                                        "temporary workspace into your permanent workspace.\n" +
                                        "\n" +
                                        "Terms of Service";

    @Override
    @Before
    public void start() throws Exception {


        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile(); //ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void copyPrivateRepoUnLoginUserTest() throws Exception {
        driver.get(testInst.getFactoryUrlFromPrivateRepo());
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.EXPLORER.waitOpened();
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        String currentWin = driver.getWindowHandle();
        URLToPrivateRepoWs = driver.getCurrentUrl();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        switchToNonCurrentWindow(currentWin);
        IDE.GITHUB.waitAuthorizationPageOpened();
        IDE.GITHUB.typeLogin(USER_NAME);
        IDE.GITHUB.typePass(USER_PASSWORD);
        IDE.GITHUB.clickOnSignInButton();
        if (IDE.GITHUB.apicheckPresentTockensInNotPresentOnGithub()) {
            IDE.GITHUB.clickOnAuthorizeBtn();
        }
        driver.switchTo().window(currentWin);
        IDE.FACTORY_URL.waitWelcomeIframeAndGotoMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-cloud-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.FACTORY_URL.waitContentIntoInformationPanel(welcomeTabMessage);
        IDE.FACTORY_URL.clickOnLoginButton();
        IDE.LOGIN.waitTenantLoginPage();
        IDE.LOGIN.loginAsTenantRoot();
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        checkReadOnlyStatus();

        driver.manage().deleteAllCookies();
        driver.get(URLToPrivateRepoWs);
        checkReadOnlyStatus();
    }

    /** check read only status menu */
    void checkReadOnlyStatus() throws InterruptedException {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.LOADER.waitClosed();
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_PATH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD_ZIPPED_FOLDER);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.CLOSE);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.SEARCH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.REFRESH);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.NEW);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FOLDER);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_URL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_LOCAL_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.DELETE);

    }
}
