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
package com.codenvy.ide.builder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */
public class WarBuildAndPublishTest extends BaseTest {
    private static final String PROJECT   = WarBuildAndPublishTest.class.getSimpleName();
    private              String buildMess = "Result: Successful";

    private List<String> mainStringsMess =
            Arrays.asList("<dependency>", "</dependency>", "<version>1.0-SNAPSHOT</version", "<type>war</type>", "<groupId>", "<groupId>",
                          "codenvy");

    private List<String> mainArtifactLinks =
            Arrays.asList("maven-metadata.xml", "maven-metadata.xml.md5", "maven-metadata.xml.sha1", "Parent Directory",
                          "war.sha1", "war.md5");

    @AfterClass
    public static void after() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void warBuildAndPublishTest() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaWebApplicationTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Java Web project.");
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        String currentWindow = driver.getWindowHandle();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.BUILD.waitBuilderContainText("Building project " + PROJECT);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitOpened();
        waitMainMessStringInOutput();
        IDE.OUTPUT.waitLinkWithParticalLinkText("http://repo");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("http://repo");
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToNonCurrentWindow(currentWindow);
        waitMainArtifactElementsOnPage();
        walidateArtifactUrl();
    }


    public void walidateArtifactUrl() {
        String[] mostParts =
                {"repo." + IDE_HOST + "/", "repository/com/" + IDE_HOST.replaceFirst(".com", "") + "/", "workspace", PROJECT + "/",
                 "1.0-SNAPSHOT"};

        System.out.println("<<<<<<<<<<<getted_URL<<<<<<<<<<<<<<<<<<<<<<<:" + driver.getCurrentUrl());

        String currentUrl = driver.getCurrentUrl();
        for (String mostPart : mostParts) {
            assertTrue(currentUrl.contains(mostPart));
            System.out.println("************parts****************" + mostPart);
        }
    }


    /** wait main messages in output panel */
    public void waitMainMessStringInOutput() {
        for (String mainStringsMes : mainStringsMess) {
            try {
                IDE.OUTPUT.waitForSubTextPresent(mainStringsMes);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /** wait on page main elements */
    public void waitMainArtifactElementsOnPage() {
        for (String mainArtifactLink : mainArtifactLinks) {
            new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(mainArtifactLink)));
        }

    }

    /**
     * @param currentWin
     *         switch on second window with just built artifact
     */
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
