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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.Utils;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.commons.ParsingResponseException;
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

/** @author Musienko Maxim */
public class CreateAccountFromTmpyWsWithOauchTest extends BaseTest {
    private static final String PROJECT = "testTenantOuachCreation1";

    private static final String PROJECT_2 = "testTenantOuachCreation12";

    private static String tenatAfterOuachCreation;

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
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException, ParsingResponseException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
        Utils.deleteWorkSpacetByName(tenatAfterOuachCreation);
        Utils.deleteOrganisationIdByName(tenatAfterOuachCreation);
        Utils.deleteUserByEmail(BaseTest.IDE_SETTINGS.getString("ide.mailbox.oauch.creation") + "@gmail.com");
    }


    @Test
    public void createAccountFromTemporaryWorkspaceTest() throws Exception {

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
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.WELCOME);
        IDE.WELCOME_PAGE.waitCreateNewProjectFromScratchBtn();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT_2);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectPHPTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple PHP project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(PROJECT_2);

        IDE.FACTORY_URL.waitCreateAccountButton();
        IDE.FACTORY_URL.clickOnCreateAccountButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Create your free account']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='email']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='domain']")));

        IDE.LOGIN.googleOauthBtnClick();
        IDE.GOOGLE.waitOauthPageOpened();
        IDE.GOOGLE.typeLogin(BaseTest.IDE_SETTINGS.getString("ide.mailbox.oauch.creation"));
        IDE.GOOGLE.typePassword(BaseTest.IDE_SETTINGS.getString("appfog.password"));
        IDE.GOOGLE.clickSignIn();

        IDE.EXPLORER.waitOpened();
        tenatAfterOuachCreation = driver.getCurrentUrl().replace(PROTOCOL + "://" + IDE_HOST + "/ide/", "");
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT_2);
    }


}
