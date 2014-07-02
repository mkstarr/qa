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
package com.codenvy.ide.runner;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Roman Iuvshin
 *
 */
public class SpringApplicationJRebelRunAndUpdateTst extends BaseTest {
    private static final String PROJECT = SpringApplicationJRebelRunAndUpdateTst.class.getSimpleName();

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    private static final String CONTENT_FOR_CHANGE = "NEW PROJECT NAME";

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }

    @Test
    public void springApplicationJRebelRunAndUpdateTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Spring application.");
//TODO REMOVED DUE TO DISABLING JREBEL
//        IDE.CREATE_PROJECT_FROM_SCRATHC.waitForJRebelCheckbox();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(3, 120);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");

        // switching to application window
        switchToNonCurrentWindow(CURRENT_WINDOW);

        // checking application
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("test");
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Hello, test!']")));

        // changing code and updating application
        driver.switchTo().window(CURRENT_WINDOW);

        IDE.EXPLORER.waitForItem(PROJECT + "/src");
        IDE.EXPLORER.openItem(PROJECT + "/src");
        IDE.EXPLORER.waitForItem(PROJECT + "/src/main");
        IDE.EXPLORER.openItem(PROJECT + "/src/main");
        IDE.EXPLORER.waitForItem(PROJECT + "/src/main/java");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java");
        IDE.EXPLORER.waitForItem(PROJECT + "/src/main/java/helloworld");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java/helloworld");

        for(int i = 0; i < 5; i++){

            changeApplicationContent("test_"+i);

            //updating application
            IDE.OUTPUT.clickClearButton();
            IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.UPDATE_APPLICATION);
            IDE.PROGRESS_BAR.waitProgressBarControlClose();
            IDE.OUTPUT.waitForSubTextPresent("[JRebel] Updated application");

            //check that application was updated
            switchToNonCurrentWindow(CURRENT_WINDOW);
            driver.navigate().refresh();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));
            driver.findElement(By.xpath("//input[@type='text']")).sendKeys("test");
            driver.findElement(By.xpath("//input[@type='submit']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(.,'test_" + i +"')]")));

            driver.switchTo().window(CURRENT_WINDOW);
        }

        // check JRebel form
        IDE.OUTPUT.clickClearButton();
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.UPDATE_APPLICATION);
        IDE.LOADER.waitClosed();
        IDE.JREBEL.waitJRebelForm();
        IDE.JREBEL.typeFirstName(Long.toString(System.currentTimeMillis()));
        IDE.JREBEL.typeLastName(Long.toString(System.currentTimeMillis()));
        IDE.JREBEL.typePhoneNumber(Long.toString(System.currentTimeMillis()));
        IDE.JREBEL.clickOkButton();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.clickClearButton();
        changeApplicationContent("check_me");
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.UPDATE_APPLICATION);
        IDE.LOADER.waitClosed();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForSubTextPresent("[JRebel] Updated application");

        switchToNonCurrentWindow(CURRENT_WINDOW);
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("test");
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(.,'check_me')]")));
    }

    /**
     * Open related to application UI file and change it for checking updating feature
     *
     * @throws Exception
     */
    private void changeApplicationContent(String contentForChange) throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.EXPLORER.openItem(PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.JAVAEDITOR.moveCursorDown(21);
        IDE.JAVAEDITOR.moveCursorRight(18);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(contentForChange + " ");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(contentForChange);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile("GreetingController.java");
    }
}
