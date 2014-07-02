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

import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.Before;
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

public class DebugStepOverTest extends DebuggerServices {
    private static final String PROJECT = DebugStepOverTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String USER = "user";

    private static final String SUBMIT = "submit";

    WebDriver debuger_app_instance = new FirefoxDriver();

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

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
        debuger_app_instance.quit();
    }

    @Test
    public void debugStepOverTest() throws Exception {
        String currentWin = driver.getWindowHandle();

        //run debug app 2 check link and get link adress
        runDebugApp(PROJECT);

        //step 2 check link and get link location
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitLinkWithParticalLinkText("run");
        String lnk = IDE.OUTPUT.getUrlTextText("run");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToDemoAppWindowAndWait(currentWin);
        driver.switchTo().window(currentWin);
        //step 3 return from demo app to IDE window and set breakpoint
        // check setting breackpoint is correctly
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.clickOnDebugerTab();
        isDebugerButtonsWithoutBreakPoints();
        IDE.DEBUGER.clickOnDebugerTab();
        IDE.DEBUGER.waitOpened();
        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        //step 4 set breakpoint
        IDE.DEBUGER.setBreakPoint(23);

        IDE.DEBUGER.waitToggleBreackPointIsSet(23);
        IDE.DEBUGER.waitBreackPointsTabContainetWithSpecifiedValue("[line :");
        assertTrue(IDE.DEBUGER.getTextFromBreackPointTabContainer().contains(
                "helloworld.GreetingController - [line : 23]"));

        typeAndSendOnDemoApp(lnk);
        IDE.DEBUGER.waitInVariableTabContainerWithSpecidiedValue("selenium");
        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        IDE.DEBUGER.waitActiveBreackPointIsSet(23);
        IDE.DEBUGER.waitStepIntoBtnIsEnabled(true);
        IDE.DEBUGER.clickStepIntoBrnClick();
        IDE.EDITOR.waitTabPresent("NewClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.DEBUGER.waitStepOverBtnIsEnabled(true);
        IDE.DEBUGER.waitActiveBreackPointIsSet(7);
        IDE.DEBUGER.clickStepOverBtnClick();
        IDE.EDITOR.waitTabPresent("NewClass.java");
        IDE.JAVAEDITOR.selectTab("NewClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.DEBUGER.waitInVariableTabContainerWithSpecidiedValue("3.14159265");
        IDE.DEBUGER.waitActiveBreackPointIsSet(8);
        IDE.DEBUGER.clickStepReturnBtnClick();
        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.DEBUGER.waitActiveBreackPointIsSet(23);

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * wait demo app wit main element
     *
     * @param winHandl
     */
    public void switchToDemoAppWindowAndWait(String winHandl) {
        Set<String> driverWindows = driver.getWindowHandles();
        for (String wins : driverWindows) {
            if (!wins.equals(winHandl)) {
                WebDriverWait wait = new WebDriverWait(driver, 10);
                driver.switchTo().window(wins);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(USER)));

            }
        }
    }

}
