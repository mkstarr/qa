package com.codenvy.ide.paas.heroku;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.GitServices;
import com.codenvy.ide.paas.PaaSLogout;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Musienko Maxim
 *
 */
public class HerokuCreateAndDeployNewProjectTest extends BaseTest {
    private static String login              = "maxura@ukr.net";
    private static String password           = "vfrcbv_1978";
    private        String defaultHerokuStack = "cedar";
    private String currentWin;
    private String createdNewAppContent = "Heroku | Welcome to your new app!\n" +
                                          "Refer to the documentation if you need help deploying.";

    private String[] outputMess         =
            {"[INFO] Logged in Heroku successfully", "[INFO] Application", "[gitUrl : git@heroku.com:", "name : "};
    private String   correctPushMessage = "Successfully pushed to git@heroku.com:";
    private String   demoAppContent     = "Browse the documentation\n" +
                                          "Rails API\n" +
                                          "Ruby standard library\n" +
                                          "Ruby core\n" +
                                          "Rails Guides\n" +
                                          "Welcome aboard\n" +
                                          "You’re riding Ruby on Rails!\n" +
                                          "Getting started\n" +
                                          "Deploy project to CloudFoundry - use menu PaaS > CloudFoundry > Create application...\n" +
                                          "Deploy project to Heroku - use menu PaaS > Heroku > Create application...";

    private Pattern pattern;
    private Matcher mtch;
    private String            regexpPattern    = "(?<=name : ).*\\n";
    private GitServices       gitService       = new GitServices();
    private HerokuApiServices herokuApiService = new HerokuApiServices(login, password);

    @BeforeClass
    public static void precond() throws IOException {
        PaaSLogout.logoutFromHeroku();
        new HerokuApiServices(login, password).removeHerokuIdeSshKey();
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete("untitled");
        } catch (Exception e) {
            fail("Can't delete test project");
        }
    }

    @Before
    public void waitLoadIde() throws InterruptedException, ParserConfigurationException, SAXException, XPathExpressionException,
                                     IOException {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        herokuApiService.checkOverLoadApp();
    }

    @Test
    public void createAndDeployNewProject() throws Exception {
        //1 Create new test project Ruby on Rails for Heroku.
        createProjectFromScrutch();
        IDE.HEROKU.loginToAccount(login, password);
        waitMainMessagesAfterCreatedApp();
        String outputMess = IDE.OUTPUT.getAllMessagesFromOutput();
        IDE.HEROKU.launchHerokuProjectForm();
        checkCompareInfoInOtputAndProjectMenus(outputMess);
        IDE.HEROKU.closeHerokuProjectForm();

        //2 Go to created application link in Output panel.
        IDE.OUTPUT.clickOnAppLinkWithParticalText("herokuapp.com");
        currentWin = driver.getWindowHandle();
        IDE.PERSPECTIVE.waitOpenedSomeWin();
        switchToNonCurrentWindow(currentWin);
        checkNewAppOnHeroku(createdNewAppContent);
        closeCurrentWindowOrTab();

        //3 Push test application to Git.
        switchToIdeWindow(currentWin);
        gitService.puchChanges();
        IDE.OUTPUT.waitForSubTextPresent(correctPushMessage);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("herokuapp.com");
        IDE.PERSPECTIVE.waitOpenedSomeWin();
        switchToNonCurrentWindow(currentWin);
        checkNewAppOnHeroku(demoAppContent);
        closeCurrentWindowOrTab();
        switchToIdeWindow(currentWin);

        //4 Open "PaaS > Heroku > Applications" window. Check the app is present
        IDE.HEROKU.launchManageApplication();
        String appName = getProjectNameFromOutput(outputMess);
        assertTrue(IDE.HEROKU.manageApplicationForm.getAllTextFromManageApplicationForm().contains(appName));

        //5 Go to Heroku https://dashboard.heroku.com/apps (get apps from Heroku side, check just created app is present)
        assertTrue(herokuApiService.listApplications().contains(appName));

    }


    /** makes all steps for create default heroku project from scratch */
    private void createProjectFromScrutch() {
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.CREATE_PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectRubyTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectHerokuPaaS();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Ruby on Rails application. v1.8 (Heroku compatible)");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratchClosed();
    }

    /** check messages in the IDE output panel after creating the test application */
    private void waitMainMessagesAfterCreatedApp() {
        for (String mainMess : outputMess) {
            IDE.OUTPUT.waitForSubTextPresent(mainMess);
        }
        IDE.OUTPUT.waitLinkWithParticalLinkText("herokuapp.com");
    }

    /**
     * check message about created application from the output panel and Project->Paas->Heroku Application
     * name and URL application should be same in output panel and Herokup PaaS form
     * also check default Heroku stack in the Heroku project form. (should be 'cedar')
     *
     * @param outputMess
     *         message from the output panel
     */
    private void checkCompareInfoInOtputAndProjectMenus(String outputMess) {
        assertTrue(outputMess.contains(IDE.HEROKU.projectForm.projectFormgetAppName()));
        assertTrue(outputMess.contains(IDE.HEROKU.projectForm.projectFormGetUrlApp()));
        assertTrue(IDE.HEROKU.projectForm.projectFormGetStackName().equals(defaultHerokuStack));
    }

    /**
     * @param content
     *         wait expected content on Heroku demo application
     */
    private void checkNewAppOnHeroku(String content) {
        driver.navigate().refresh();
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        driver.navigate().refresh();
        new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElement(By.tagName("body"), content));
    }


    /**
     * switch to IDE window
     *
     * @param ideWin
     *         ide browser window handle
     */
    private void switchToIdeWindow(String ideWin) {
        driver.switchTo().window(ideWin);
    }


    /** invoke webdriver close method for closing current window or tab */
    private void closeCurrentWindowOrTab() {
        driver.close();
    }

    /**
     * get name Heroku app with regexp
     *
     * @param messageAfterCorrectCreatiob
     * @return name Heroku application or null if the application was not found
     */
    public String getProjectNameFromOutput(String messageAfterCorrectCreatiob) {
        pattern = Pattern.compile(regexpPattern);
        mtch = pattern.matcher(messageAfterCorrectCreatiob);
        String retStr = null;
        while (mtch.find()) {
            retStr = mtch.group();
        }
        return retStr.trim();
    }

}
