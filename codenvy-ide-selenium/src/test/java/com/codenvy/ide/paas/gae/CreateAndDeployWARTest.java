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

package com.codenvy.ide.paas.gae;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.fail;

/**
 * @author Zaryana Dombrovskaya
 */
public class CreateAndDeployWARTest extends BaseTest {

    private final static String PROJECT_NAME = "createwar" + getCurrentTimeForProjectName();      //Application Identifier must be between 6 and 30 characters. Lowercase letters, digits, and hyphens are acceptable characters. Leading and trailing hyphens are prohibited.

    private static String currentWindow = null;

    private WebDriverWait wait;

    private String outputMessage = "[INFO] Project successfully built.\n" +
            "[INFO] Deploy application " + PROJECT_NAME + " to Google App Engine started.\n" +
            "[INFO] Project " + PROJECT_NAME + " is successfully deployed to Google App Engine.\n" +
            "Application URL : http://" + PROJECT_NAME + ".appspot.com";

    private String buildPanelMessage = "Building project " + PROJECT_NAME + "\n" +
            "Finished building project " + PROJECT_NAME + ".\n" +
            "Result: Successful";


    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
        IDE.GAE.login();
    }


    @Test
    public void createWarApplication() throws Exception {

        currentWindow = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.GAE.createWarProject(PROJECT_NAME);
        IDE.GAE.createApplication(PROJECT_NAME, PROJECT_NAME);
        //TODO test sometimes falls because long deploys to appEngine - check it manual
        IDE.GAE.checkBuildPanel(buildPanelMessage);
        IDE.OUTPUT.clickOnOutputTab();
        IDE.GAE.checkOutputPanel(outputMessage);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("appspot");

        switchToNonCurrentWindow(currentWindow);
        //wait for the element in opened link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea")));

        driver.switchTo().window(currentWindow);

        IDE.MENU.waitSubCommandEnabled(MenuCommands.Project.PROJECT, MenuCommands.Project.PaaS.PAAS, MenuCommands.Project.PaaS.GAE);
    }


    @After
    public void tearDown() throws Exception{
        try {
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
        IDE.GOOGLE.openGoogleAccountPage();
        IDE.GOOGLE.deleteGoogleTokens();
        IDE.GAE.applicationAppEngineWindow.openGoogleAppEngine();
        IDE.GAE.deleteApplication(PROJECT_NAME);
    }


    public static String getCurrentTimeForProjectName() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("[/:]", "").replaceAll(" ", "").toLowerCase();
    }

}
