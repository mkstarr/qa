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
package com.codenvy.ide.operation.multimodule;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class MavenMultimoduleProjectTest extends MultimoduleSevices {

    private WebDriverWait wait;

    private static final String PROJECT = MavenMultimoduleProjectTest.class.getSimpleName();

    private static final String BUILD_MY_LIB_MESSAGE = "<artifactId>my-lib</artifactId>";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/multimoduleMaven.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void buildAndPublishParentModule() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);

        //TODO On this moment in multimodule codeassist does not works.
        // check for waiting progress bar unnecessary
        //IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.OUTPUT.waitForMessageShow(1, 60);
        IDE.BUILD.selectBuilderOutputTab();
        IDE.BUILD.waitBuilderMessage("Building project " + PROJECT + "\nFinished building project " + PROJECT
                                     + ".\nResult: Successful");
        IDE.BUILD.selectBuilderOutputTab();
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitForSubTextPresent("<artifactId>MavenMultimoduleProject</artifactId>");

    }

    @Test
    public void changeMyLibAndPrjBuildWithWrongValues() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("my-lib");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/test/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("hello");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("hello");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SayHello.java");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SayHello.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(12);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.moveCursorLeft(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("+" + "\" How are you?\"");
        // need due to file automatically saving invoked by timer

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("+" + "\" How are you?\"");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("my-lib");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.selectBuilderOutputTab();
        IDE.BUILD.waitBuilderContainText("Result: Failed");
        IDE.BUILD.clickClearButton();
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.clickClearButton();

    }

    @Test
    public void changeAssertValueInTestAndRebuild() throws Exception {
        IDE.OUTPUT.closeOutputTab();
        IDE.OUTPUT.waitClosed();
        IDE.BUILD.closeBuildPanel();
        IDE.BUILD.waitClosed();

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/test/java");
        IDE.PACKAGE_EXPLORER.waitItemOffNodePoint("src/test/java", "hello");
        IDE.PACKAGE_EXPLORER.openItemOffNodePointWithDoubleClick("src/test/java", "hello");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SayHelloTest.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SayHelloTest.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        //need for correct setting cursor, before go to line
        Thread.sleep(1000);
        IDE.GOTOLINE.goToLine(36);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.LEFT_SHIFT.toString() + Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.DELETE.toString());
        IDE.JAVAEDITOR
           .waitWhileJavaEditorWillNotContainSpecifiedText(
                   "assertTrue(\"Hello, eXo\".equals(sayHello.sayHello(\"eXo\")));");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("assertTrue(\"Hello, eXo How are you?\".equals(sayHello.sayHello(\"eXo\")));\n");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("sayHello.sayHello(name))");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("Hello, eXo How are you?");
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitForSubTextPresent(BUILD_MY_LIB_MESSAGE);
    }

    @Test
    public void runAndCheckApplication() throws Exception {
        String CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer("my-webapp");
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.RUN_APPLICATION);
        IDE.PROGRESS_BAR.waitProgressBarControl();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitForMessageShow(3, 60);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("run");
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToNonCurrentWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Enter your name: ']")));
        driver.findElement(By.xpath("//input[@type='text']")).sendKeys("codenvy");
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        wait
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath("//span[text()='Hello, codenvy How are you?']")));
    }
}
