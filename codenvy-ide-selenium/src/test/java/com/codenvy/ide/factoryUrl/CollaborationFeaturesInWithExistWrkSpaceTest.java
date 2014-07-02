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

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.collaboration.CollaborationService;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** @author Musienko Maxim */
public class CollaborationFeaturesInWithExistWrkSpaceTest extends CollaborationService {
    private final String REPO_INIT_MESSAGE = "[INFO] Repository was successfully initialized.";

    private static final String PROJECT = "CollabFeaturesWithExistWrkSps";

    private String factoryUrl;

    private String temporaryworkspaceUrl;

    protected static Map<String, Link> project;


    private String first_mess_collab_1 = "Hello,  how are you?";

    private String expectedMess_coll_1 = "me: \n" +
                                         "Hello, how are you?";

    private String anonimusPrefix = "AnonymousUser_";

    private String first_mess_collab_2 = "Thaks, i'm fine";

    private String firstUser = NOT_ROOT_USER_NAME.split("@")[0];

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
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }


    //override Base method
    public void initSecondBrowser() throws Exception {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                driver2 = new FirefoxDriver(profile);

                IDE2 = new com.codenvy.ide.IDE(LOGIN_URL, driver2);
                try {

                    driver2.manage().window().maximize();


                } catch (Exception e) {
                }

                break;
            default:
                driver2 = new ChromeDriver();
                IDE2 = new com.codenvy.ide.IDE("", driver2);
                try {

                    driver2.manage().window().maximize();


                } catch (Exception e) {
                }
        }


    }

    @Test
    public void collaborationActions() throws Exception {
        //step1 open project as first user, get factory URL
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.GIT.waitInitializeLocalRepositoryForm();
        IDE.GIT.typeInitializationRepositoryName(PROJECT);
        IDE.GIT.clickOkInitializeLocalRepository();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_INIT_MESSAGE);
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        factoryUrl = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();

        //login as additional user in IDE
        driver.get(LOGIN_URL);
        IDE.LOGIN.waitTenantLoginPage();
        IDE.LOGIN.loginAsTenantUser();

        //got to getted factory url
        driver.get(factoryUrl);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        temporaryworkspaceUrl = driver.getCurrentUrl();
        IDE.LOADER.waitClosed();
        IDE.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE.CHAT.waitChatOpened();

        //init second collaborator and go to factory url
        initSecondBrowser();
        driver2.get(temporaryworkspaceUrl);
        IDE2.EXPLORER.waitOpened();
        IDE2.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE2.MENU.waitCommandEnabled(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE2.CHAT.waitChatOpened();


        //chat scenario
        IDE.CHAT.waitChatParticipants(anonimusPrefix);
        IDE.CHAT.waitWhileMessageAppearInChat(IDE.CHAT.getCurrentPartipicant());
        IDE.CHAT.typeAndSendMessage(first_mess_collab_1);
        IDE.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE.CHAT.waitWhileMessageAppearInChat(expectedMess_coll_1);

        IDE2.CHAT.waitChatParticipants(firstUser);
        IDE2.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE2.CHAT.waitWhileMessageAppearInChat(IDE2.CHAT.getCurrentPartipicant());
        IDE2.CHAT.waitWhileMessageAppearInChat(firstUser);
        IDE2.CHAT.waitWhileMessageAppearInChat(": \nHello, how are you?");
        IDE2.CHAT.typeAndSendMessage(first_mess_collab_2);
        IDE.CHAT.waitWhileMessageAppearInChat(": \n" +
                                              first_mess_collab_2);
        IDE.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE.CHAT.waitWhileMessageAppearInChat(anonimusPrefix);
    }

    /**
     * return current time hours an minutes. Use for check time in collabor. chat
     *
     * @return
     */
    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date).toString();

    }

}



