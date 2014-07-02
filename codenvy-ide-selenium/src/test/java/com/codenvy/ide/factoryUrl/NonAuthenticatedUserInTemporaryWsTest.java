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

public class NonAuthenticatedUserInTemporaryWsTest extends BaseTest {

    private static final String PROJECT = "notAutorised";

    private static final String CONTENT_1 = "This project has been factory-created into a secured private temporary workspace on Codenvy.";

    private static final String CONTENT_2 = "Your code is private and this temporary workspace will remain only accessible by you.";

    private static final String CONTENT_3 = "Codenvy is your complete cloud environment to:";

    private static final String CONTENT_4 = "To save your work, Login or Create your free Account.";

    private static final String CONTENT_5 = "A temporary workspace will be deleted if you are inactive or close the browser.";

    private static Map<String, Link> project;


    private WebDriverWait wait;


    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
    }

    @Test
    public void nonAuthenticatedUserInTemporaryWsTest() throws Exception {
        wait = new WebDriverWait(driver, 30);

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();
        driver.get(factoryURL);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // check welcome panel
        IDE.FACTORY_URL.waitWelcomePanelTitle("Welcome");
        IDE.FACTORY_URL.selectWelcomeIframe();
//        IDE.FACTORY_URL.waitTextFragmentInWelcomePanel(CONTENT_1);
//        IDE.FACTORY_URL.waitTextFragmentInWelcomePanel(CONTENT_2);
//        IDE.FACTORY_URL.waitTextFragmentInWelcomePanel(CONTENT_3);
//        IDE.FACTORY_URL.waitTextFragmentInWelcomePanel(CONTENT_4);
//        IDE.FACTORY_URL.waitTextFragmentInWelcomePanel(CONTENT_5);
        driver.switchTo().defaultContent();
        // check login & create accoun buttons
        IDE.FACTORY_URL.waitLoginButton();
        IDE.FACTORY_URL.clickOnLoginButton();
        IDE.LOGIN.waitTenantLoginPage();
        assertTrue(driver.getCurrentUrl().contains(LOGIN_URL));
        driver.navigate().back();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.OPEN.openProject(PROJECT);
        IDE.FACTORY_URL.waitCreateAccountButton();
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.clickOnCreateAccountButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Create your free account']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='domain']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'google')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'github')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(.,'By signing up, you agree to the')]")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Already have an account? Sign in.']")));
        assertTrue(driver.getCurrentUrl().contains(PROTOCOL + "://" + IDE_HOST + "/site/create-account"));
    }
}
