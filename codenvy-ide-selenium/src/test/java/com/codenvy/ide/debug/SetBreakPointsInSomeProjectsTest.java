/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.debug;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */

public class SetBreakPointsInSomeProjectsTest extends DebuggerServices {

    private static final String PROJECT = SetBreakPointsInSomeProjectsTest.class.getSimpleName();

    private static final String PROJECT2 = SetBreakPointsInSomeProjectsTest.class.getSimpleName() + "2";

    protected static Map<String, Link> project;

    protected static Map<String, Link> project2;

    private static final String USER = "user";

    private static final String SUBMIT = "submit";

    private WebDriver debuger_app_instance = new FirefoxDriver();

    private WebDriverWait wait;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/change-variable-proj.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            project2 =
                    VirtualFileSystemUtils.importZipProject(PROJECT2,
                                                            "src/test/resources/org/exoplatform/ide/debug/change-variable-proj.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(PROJECT2);
        debuger_app_instance.quit();
    }

    @Test
    public void setBreackPointTest() throws Exception {
        String currentWin = driver.getWindowHandle();

        // run debug app 2 check link and get link adress
        runDebugApp(PROJECT);

        // step 2 check link and get link location
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitLinkWithParticalLinkText("run");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToDemoAppWindowAndClose(currentWin);
        driver.switchTo().window(currentWin);
        // step 3 return from demo app to IDE window and set breakpoint
        // check setting breackpoint is correctly
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.clickOnDebugerTab();
        isDebugerButtonsWithoutBreakPoints();
        IDE.DEBUGER.clickOnDebugerTab();
        IDE.DEBUGER.waitOpened();
        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        // step 4 set breakpoint
        IDE.DEBUGER.setBreakPoint(22);
        IDE.DEBUGER.waitToggleBreackPointIsSet(22);
        IDE.DEBUGER.waitBreackPointsTabContainetWithSpecifiedValue("[line :");
        assertTrue(IDE.DEBUGER.getTextFromBreackPointTabContainer().contains(
                "helloworld.GreetingController - [line : 22]"));

        // step 5 switch on debug app, type 'selenium' value, click ok button
        // return close browser window with application and return to ide. Check changes
        openProjectAndClass(PROJECT2);
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.clickClearButton();
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.DEBUG_APPLICATION);
        IDE.BUILD.waitOpened();
        String builderMessageSecondPrj = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessageSecondPrj.startsWith(Build.Messages.BUILDING_PROJECT));
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.DEBUGER.waitTabOfDebuger();
        // check check breakpoint container is empty, set new breakpoint and check, that only new breakpoint is present in panel
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitLinkWithParticalLinkText("run");
        String lnk = IDE.OUTPUT.getUrlTextText("run");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");
        IDE.DEBUGER.waitOpenedSomeWin();
        driver.switchTo().window(currentWin);
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.clickOnDebugerTab();
        IDE.DEBUGER.waitBreackPointsTabContainerIsEmpty();
        IDE.DEBUGER.setBreakPoint(19);
        IDE.DEBUGER.waitToggleBreackPointIsSet(19);
        IDE.DEBUGER.waitBreackPointsTabContainetWithSpecifiedValue("helloworld.GreetingController - [line : 19]");
        typeAndSendOnDemoApp(lnk);
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.DEBUGER.waitActiveBreackPointIsSet(19);
        IDE.DEBUGER.clickDisconnectBtnClick();
        IDE.DEBUGER.waitDebugerIsClosed();
        IDE.OUTPUT.waitForSubTextPresent("stopped.");
    }

    /** @throws Exception */
    private void openProjectAndClass(String project) throws Exception {
        IDE.OPEN.openProject(project);
        if (IDE.ASK_DIALOG.isOpened()) {
            IDE.ASK_DIALOG.clickYes();
            IDE.ASK_DIALOG.waitClosed();
        }
        //  IDE.TOOLBAR.runCommand(ToolbarCommands.PackageExplorer.PACKAGE_EXPLORER);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    /**
     * Type value to field demo application, press enter and close window
     *
     * @param value
     * @param winHandle
     * @throws InterruptedException
     */
    private void typeValueAndSubmitDemoappAndExit(String value, String winHandle) throws InterruptedException {
        driver.findElement(By.name(USER)).sendKeys(value);
        // driver.findElement(By.name(SUBMIT)).click();
        try {
            Robot robot = new Robot();
            Thread.sleep(1000);
            robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
            Thread.sleep(1000);
            robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void switchToDemoAppWindowAndClose(String winHandl) {
        Set<String> driverWindows = driver.getWindowHandles();
        for (String wins : driverWindows) {
            if (!wins.equals(winHandl)) {
                wait = new WebDriverWait(driver, 10);
                driver.switchTo().window(wins);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(USER)));
                driver.close();
            }
        }
    }

    /**
     * @param lnk
     * @throws InterruptedException
     */
    private void typeAndSendOnDemoApp(String lnk) throws InterruptedException {
        debuger_app_instance.get(lnk);
        new WebDriverWait(debuger_app_instance, 10).until(ExpectedConditions.visibilityOfElementLocated(By.name(USER)));
        new WebDriverWait(debuger_app_instance, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.name(SUBMIT)));
        WebElement field_2 = debuger_app_instance.findElement(By.name(USER));
        WebElement submitBtn_2 = debuger_app_instance.findElement(By.name(SUBMIT));
        field_2.sendKeys("selenium");
        try {
            Robot robot = new Robot();
            Thread.sleep(1000);
            robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
            robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }
}
