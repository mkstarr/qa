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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** @author Musienko Maxim */
public class JarBuildAndPublishTest extends BaseTest {
    private static final String PROJECT   = JarBuildAndPublishTest.class.getSimpleName();
    private              String buildMess = "Result: Successful";

    private List<String> mainStringsMess =
            Arrays.asList("<dependency>", "</dependency>", "<version>1.0-SNAPSHOT</version", "<artifactId>jarBuild</artifactId>");

    private List<String> mainArtifactLinks =
            Arrays.asList("maven-metadata.xml", "maven-metadata.xml.md5", "jarBuild", "maven-metadata.xml.sha1", "sources.jar",
                          "sources.jar.md5", "sources.jar.sha1");


    public static final String PATH = "src/test/resources/org/exoplatform/ide/miscellaneous/jarProject.zip";

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
        } catch (Exception e) {
        }
    }

    @Test
    public void jarBuildAndPublishTest() throws Exception {
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        String currentWindow = driver.getWindowHandle();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);
        IDE.BUILD.waitOpened();
        IDE.BUILD.waitBuilderContainText("Building project " + PROJECT);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.OUTPUT.waitOpened();
        waitMainMessStringInOutput();
        IDE.OUTPUT.waitLinkWithParticalLinkText("http://repo");
        IDE.OUTPUT.clickOnAppLinkWithParticalText("http://repo");
        IDE.DEBUGER.waitOpenedSomeWin();
        switchToRepositoryWindow(currentWindow);
        waitMainArtifactElementsOnPage();

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
