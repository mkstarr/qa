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
package com.codenvy.ide.builder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** @author Artem Zatsarynnyy */
public class BuildSuccessedTest extends BaseTest {

    private static final String PROJECT = BuildSuccessedTest.class.getSimpleName();

    public static final String PATH = "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    private static final String DEPENDENCY =
            "\n<dependency>\n  <groupId>cloud-ide</groupId>\n  <artifactId>spring-demo</artifactId>\n"
            + "  <version>1.0</version>\n  <type>war</type>\n</dependency>";

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT, PATH);
            //need for full complete import zip folder in DavFs
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void after() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            //need for full deleting zip folder in DavFs
        } catch (Exception e) {
        }
    }

    @Test
    public void testBuild() throws Exception {
        IDE.EXPLORER.waitOpened();

        // Open project
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.LOADER.waitClosed();
        // Building of project is started
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));

        // Wait until building is finished.
        IDE.STATUSBAR.waitDiasspearBuildStatus();
        IDE.LOADER.waitClosed();
        // check clear output button
        IDE.BUILD.clickClearButton();

        String emptyMessage = IDE.BUILD.getOutputMessage();
        assertEquals("", emptyMessage);

        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='gwt-TabLayoutPanelTabs']//div[@tab-title='Build project']")).click();

        // Get success message
        IDE.OUTPUT.waitForMessageShow(1, 15);
        String buildSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));

        IDE.OUTPUT.clickClearButton();

    }

    @Test
    public void testBuildAndPublish() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 15);

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.OUTPUT.waitForMessageShow(1, 60);

        IDE.BUILD.selectBuilderOutputTab();
        IDE.BUILD.waitBuilderMessage("Building project " + PROJECT + "\nFinished building project " + PROJECT
                                     + ".\nResult: Successful");

        // Close Build project view because Output view is not visible
        driver.findElement(By.xpath("//div[@class='tabTitleCloseButton' and @tab-title='Build project']")).click();

        //cheking output tab
        IDE.OUTPUT.waitForMessageShow(1, 60);
        String buildAndPublishSuccessMessage = IDE.OUTPUT.getOutputMessage(1);
        assertTrue(buildAndPublishSuccessMessage.endsWith(Output.Messages.BUILD_SUCCESS));
        String urlToArtifact = IDE.OUTPUT.getOutputMessage(2);
        assertTrue(urlToArtifact.contains(Build.Messages.URL_TO_ARTIFACT));
        String dependency_mess = IDE.OUTPUT.getOutputMessage(3);
        assertTrue(dependency_mess.contains(Build.Messages.DEPENDENCY_MESS));
        assertTrue(dependency_mess.contains(DEPENDENCY));

        IDE.OUTPUT.waitForMessageShow(3, 60);
        IDE.OUTPUT.clickOnAppLinkWithParticalText("http://");

        // switching to application window
        switchToRepositoryWindow(CURRENT_WINDOW);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody//a[text()='spring-demo-1.0.war']")));
    }

    /** @param currentWin */
    private void switchToRepositoryWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {
            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
