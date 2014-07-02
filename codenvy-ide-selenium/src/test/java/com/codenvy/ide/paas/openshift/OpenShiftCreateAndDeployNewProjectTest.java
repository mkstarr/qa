package com.codenvy.ide.paas.openshift;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.openshift.ManageApplicationsWindow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Zaryana Dombrovskaya
 */
public class OpenShiftCreateAndDeployNewProjectTest extends BaseTest {


    private final static String PROJECT_NAME = "createos" + getCurrentTimeForProjectName();

    private static String currentWindow = null;

    private WebDriverWait wait;

    private String outputMessageLogin = "[INFO] Logged in OpenShift successfully.";

    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.OPEN_SHIFT.login();
        IDE.OUTPUT.waitOnMessage(outputMessageLogin);
        IDE.OUTPUT.clearPanel();
        IDE.OPEN_SHIFT.updateSSHKey();
        IDE.OUTPUT.clearPanel();
    }

    @Test
    public void createAndDeployProject() throws Exception{

        currentWindow = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.OPEN_SHIFT.createProject(PROJECT_NAME);
        IDE.OUTPUT.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.OPENSHIFT);
        IDE.LOADER.waitClosed();
        IDE.OPEN_SHIFT.checkApplicationPropertyWithProjectMenu();

        IDE.MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.OpenShift.OPENSHIFT, MenuCommands.PaaS.OpenShift.APPLICATIONS);
        IDE.LOADER.waitClosed();

        IDE.OPEN_SHIFT.checkApplicationPropertyWithPaaSMenu(PROJECT_NAME);

        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.clickOnAppLinkWithParticalText("rhcloud");

        switchToNonCurrentWindow(currentWindow);
        //wait for the element in opened link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'Welcome to your JBoss EAP application on OpenShift')]")));
        driver.switchTo().window(currentWindow);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.OPENSHIFT);
    }

    @After
    public void tearDown() throws Exception{
        IDE.OPEN_SHIFT.deleteSSHKey();
        IDE.OPEN_SHIFT.deleteApplicationFromProjectMenu();
        try {
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
     //TODO delete project from OpenShift
    }

    public static String getCurrentTimeForProjectName() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("[/:]", "").replaceAll(" ", "").toLowerCase();
    }

}
