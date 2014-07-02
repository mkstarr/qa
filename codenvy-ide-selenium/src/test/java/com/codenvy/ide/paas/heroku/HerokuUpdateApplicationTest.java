package com.codenvy.ide.paas.heroku;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.GitServices;
import com.codenvy.ide.paas.PaaSLogout;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Musienko Maxim
 *
 */
public class HerokuUpdateApplicationTest extends BaseTest {

    //TODO set date and unic prefix with getCurrentTimeForProjectName after appropriate util class
    private final static String            PROJECT_NAME             = HerokuUpdateApplicationTest.class.getSimpleName();
    private static       String            login                    = "maxura@ukr.net";
    private static       String            password                 = "vfrcbv_1978";
    private static       HerokuApiServices herokuApiService         = new HerokuApiServices(login, password);
    private final        String            defaultBackgroundDemoCss = "background-color: #f0f0f0;";
    private final        String            changeBackGroundDemoCSS  = "background-color: #bf7e01;\n";
    private final        String            deployKeyMess            = "[INFO] Public keys are successfully deployed on Heroku.";
    private final        String            pushMess                 = "Successfully pushed to git@heroku.com";
    private final        String            loginMess                = "[INFO] Logged in Heroku successfully.";
    private final        String            defaulBackGroundDemoApp  = "rgba(240, 240, 240, 1)";
    private final        String            changedBackGroundDemoApp = "rgba(191, 126, 1, 1)";
    private static String mainWinHandle;

    private final String[] createAppMess =
            {"[INFO] Application", "[gitUrl : git@heroku.com:", "webUrl :", "herokuapp.com/", "name :", "] is successfully created."};

    private       String      demoAppContent = "Browse the documentation\n" +
                                               "Rails API\n" +
                                               "Ruby standard library\n" +
                                               "Ruby core\n" +
                                               "Rails Guides\n" +
                                               "Welcome aboard\n" +
                                               "You’re riding Ruby on Rails!\n" +
                                               "Getting started\n" +
                                               "Deploy project to CloudFoundry - use menu PaaS > CloudFoundry > Create application...\n" +
                                               "Deploy project to Heroku - use menu PaaS > Heroku > Create application...";
    private final GitServices gitServInst    = new GitServices();

    @BeforeClass
    public static void uploadTestHerokuTstProject() {
        try {


            VirtualFileSystemUtils
                    .importZipProject(PROJECT_NAME,
                                      "src/test/resources/org/exoplatform/ide/herokuPaas/Heroku_ruby_on_rails.zip");

            PaaSLogout.logoutFromHeroku();
            herokuApiService.checkOverLoadApp();
            herokuApiService.removeHerokuIdeSshKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        herokuApiService.checkOverLoadApp();
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT_NAME);
    }

    @Test
    public void updateApplicationTest() throws Exception {
        preconditions();

        IDE.HEROKU.switchAccountFromPaasMenu(login, password);
        IDE.OUTPUT.waitForSubTextPresent(loginMess);
        IDE.HEROKU.deployPublicKey();
        IDE.OUTPUT.waitForSubTextPresent(deployKeyMess);
        IDE.HEROKU.createApplicationFromPaasMenu();
        waitCreateAppMess();
        gitServInst.puchChanges();
        IDE.OUTPUT.waitForSubTextPresent(pushMess);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("herokuapp.com");
        IDE.PERSPECTIVE.waitOpenedSomeWin();
        switchToNonCurrentWindow(mainWinHandle);
        checkNewDefaultAppOnHeroku(demoAppContent);
        switchToMainWindow();
        editBackground();
        gitServInst.addToIndex();
        gitServInst.commmitChanges("commit" + System.currentTimeMillis());
        gitServInst.puchChanges();

        IDE.OUTPUT.clickOnAppLinkWithParticalText("herokuapp.com");
        IDE.PERSPECTIVE.waitOpenedSomeWin();
        switchToNonCurrentWindow(mainWinHandle);
        checkApdatedAppOnHeroku(demoAppContent);


    }

    /**
     * Opens  project, opens file for UI changes, waits opening file. Checks availability css necessary style for changing.
     *
     * @throws Exception
     */
    private void preconditions() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT_NAME);
        gitServInst.gitInit();
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/config.ru");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/public");
        IDE.EXPLORER.waitForItem(PROJECT_NAME + "/public" + "/index.html");
        IDE.EXPLORER.openItem(PROJECT_NAME + "/public" + "/index.html");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(defaultBackgroundDemoCss);
        IDE.EXPLORER.selectItem(PROJECT_NAME);
        mainWinHandle = driver.getWindowHandle();
    }

    /**
     * Go to editor on the background color value and change it
     *
     * @throws Exception
     */
    private void editBackground() throws Exception {
        IDE.GOTOLINE.goToLine(10);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(changeBackGroundDemoCSS);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.JAVAEDITOR.waitNoContentModificationMark("index.html");
        IDE.EXPLORER.selectItem(PROJECT_NAME);
    }

    /** waits in output panel main parts of message about creating current application */
    private void waitCreateAppMess() {
        for (String createAppMes : createAppMess) {
            IDE.OUTPUT.waitForSubTextPresent(createAppMes);
        }
    }

    /**
     * @param content
     *         wait expected content on Heroku demo application
     */
    private void checkNewDefaultAppOnHeroku(String content) {
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        driver.navigate().refresh();
        new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElement(By.tagName("body"), content));
        assertTrue(driver.findElement(By.tagName("body")).getCssValue("background-color").equals(defaulBackGroundDemoApp));
        driver.close();
    }


    /**
     * wait appearance Heroku demo app
     *
     * @param content
     *         wait expected content on Heroku demo application
     */
    private void checkApdatedAppOnHeroku(String content) throws InterruptedException {
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        driver.navigate().refresh();
        new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElement(By.tagName("body"), content));
        assertTrue(driver.findElement(By.tagName("body")).getCssValue("background-color").equals(changedBackGroundDemoApp));
    }

    /** return driver to main app window */
    private void switchToMainWindow() {
        driver.switchTo().window(mainWinHandle);
    }

}
