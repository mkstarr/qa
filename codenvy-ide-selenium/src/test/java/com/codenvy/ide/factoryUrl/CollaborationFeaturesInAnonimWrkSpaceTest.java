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

import com.codenvy.ide.IDE;
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

import static com.thoughtworks.selenium.SeleneseTestBase.assertFalse;

/** @author Musienko Maxim */
public class CollaborationFeaturesInAnonimWrkSpaceTest extends CollaborationService {

    private final String REPO_INIT_MESSAGE = "[INFO] Repository was successfully initialized.";

    private static final String PROJECT = CollaborationFeaturesInAnonimWrkSpaceTest.class.getSimpleName();

    private String factoryUrl;

    private String gettedTemporyUrl;

    private String first_mess_collab_1 = "Hello,  how are you?";

    private String expectedMess_coll_1 = "me: \n" +
                                         "Hello, how are you?";

    private String anonimusPrefix = "AnonymousUser_";


    private final String AUTOGENERATE_METHOD_TEXT =
            "/**\n * \n */\nprivate void autogenMethod()\n{\n   // TODO Auto-generated method stub\n\n}";

    protected static Map<String, Link> project;


    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
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
                IDE2 = new IDE("", driver2);
                try {
                    driver2.manage().window().maximize();
                } catch (Exception e) {
                }
        }


    }


    @Test
    public void chatInTemporarySpaceTest() throws Exception {
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

        //run and init second browser for collaboration mode in the IDE
        initSecondBrowser();

        //go to in temporary workspase as 1 user and wait project
        driver.get(factoryUrl);

        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();

        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        gettedTemporyUrl = driver.getCurrentUrl();
        //go to in temporary workspase as 2 user and wait project
        driver2.get(gettedTemporyUrl);
        IDE2.EXPLORER.waitOpened();
        IDE2.EXPLORER.waitForItemInProjectList(PROJECT);

        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        IDE.LOADER.waitClosed();
        IDE.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE.CHAT.waitChatOpened();
        IDE.CHAT.waitChatParticipants(anonimusPrefix);


        IDE2.MENU.waitCommandEnabled(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.TOOLBAR.runCommand(ToolbarCommands.Collaboration.COLLABORATION);
        IDE2.CHAT.waitChatOpened();
        IDE2.CHAT.waitChatParticipants(anonimusPrefix);
        //anonymous users must have different names
        assertFalse(IDE.CHAT.getCurrentPartipicant().equals(IDE2.CHAT.getCurrentPartipicant()));


        IDE.CHAT.typeAndSendMessage(first_mess_collab_1);
        IDE.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE.CHAT.waitWhileMessageAppearInChat(expectedMess_coll_1);


        IDE2.CHAT.waitWhileMessageAppearInChat(getTime());
        IDE2.CHAT.waitWhileMessageAppearInChat(IDE2.CHAT.getCurrentPartipicant());
        IDE2.CHAT.waitWhileMessageAppearInChat(": \nHello, how are you?");
        openPrjInSecondChat();

        IDE.CHAT.waitLinkInChat("GreetingController.java");
        IDE.CHAT.clickOnLinkInChat("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("ModelAndView view = new ModelAndView(\"hello_view\");");
    }

    @Test
    public void collaborationFeaturesInJavaEditor() throws Exception {
        IDE.GOTOLINE.goToLine(14);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("/**autogenMethod*/");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/**autogenMethod*/");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/**autogenMethod*/");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("/**autogenMethod*/");

    }

    private void openPrjInSecondChat() throws Exception {
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
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
