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
package com.codenvy.ide.paas.openshift;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.fail;

/**
 * @author Zaryana Dombrovskaya
 */
public class OpenShiftUpdateApplicationTest extends BaseTest {

    private final static String PROJECT_NAME = "updateos" + getCurrentTimeForProjectName();

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
        IDE.OPEN_SHIFT.createProject(PROJECT_NAME);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.clearPanel();
    }

    @Test
    public void updateProject() throws Exception {

        currentWindow = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.OPEN_SHIFT.editApplication(PROJECT_NAME);
        IDE.OPEN_SHIFT.updateApplication();
        IDE.OUTPUT.waitForSubTextPresent("rhcloud");
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.clickOnAppLinkWithParticalText("rhcloud");

        switchToNonCurrentWindow(currentWindow);
        //wait for the element in opened link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(.,(Test))]")));
        driver.switchTo().window(currentWindow);
    }

    @After
    public void tearDown() throws Exception {
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
