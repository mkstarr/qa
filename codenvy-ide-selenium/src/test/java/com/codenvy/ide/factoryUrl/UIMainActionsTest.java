package com.codenvy.ide.factoryUrl;

/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [${YEAR}] Codenvy, S.A.
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Musienko Maxim */
public class UIMainActionsTest extends BaseTest {


    private static final String PROJECT = UIMainActionsTest.class.getSimpleName();

    private static Map<String, Link> project;

    private List<String> maihWelcomeTabLinks =
            Arrays.asList("Code", "Commit", "Build", "Package", "Test", "Run", "Debug", "PaaS Deploy", "Collaborate", "Share");

    @BeforeClass

    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
    }


    @Test
    public void loadingBehaviorTest() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 15, 200);
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();
        driver.get(factoryURL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Setting Up Builders']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Opening Your Project']")));
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        //
    }


    @Test
    public void welcomeLinksTets() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        String currentWin = driver.getWindowHandle();

        //wait main links
        waitWelcomeIframeAndSwitch();

        //check Code link (select frame wit links, wait opening window by link, switch to window check some element,
        // switch to main window and close window linked by 'Code' link)
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(0))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.cssSelector("div.entry-content"), "This is how the Drakula theme looks like:"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Commit link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(1))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("add-files-to-index"), "Add Files to Index"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Build link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(2))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("configuring-project-dependencies"), "Configuring Project Dependencies"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Package link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(3))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("getting-started-with-maven"), "Getting Started With Maven"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Test link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(4))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("surefire-reports"), "Surefire Reports"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


        //check Run link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(5))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.className("entry-title"), "Running Applications"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Debug link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(6))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.className("entry-title"), "How to Debug a Java Application"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


        //check Paas Deploy link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(7))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("deploying-a-new-application-gae"), "Deploying a New Application GAE"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Collaborate link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(8))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.className("entry-title"), "Chat and Real-Time Collaboration"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


        //check Share link
        driver.findElement(By.linkText(maihWelcomeTabLinks.get(9))).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.className("entry-title"), "Inviting Developers to Your Workspace"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


        //check Home Page link
        driver.findElement(By.linkText("Home Page")).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Free Sign Up']")));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


        //check Support link
        driver.findElement(By.linkText("Support")).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.tagName("body"), "Registration and login"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Docs link
        driver.findElement(By.linkText("Docs")).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.id("home-splash-discover"), "Discover"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);

        //check Feature Vote link
        driver.findElement(By.linkText("Feature Vote")).click();
        waitOpeneningWintowByLinkAndSwitchToWin(currentWin);
        wait.until(ExpectedConditions
                           .textToBePresentInElement(By.tagName("body"), "Codenvy Feedback"));
        closeCurentWinSwitchToMainAndWaitLinks(currentWin);


    }

    private void closeCurentWinSwitchToMainAndWaitLinks(String currentWin) {
        driver.close();
        driver.switchTo().window(currentWin);
        waitWelcomeIframeAndSwitch();
    }

    /** @param currentWin */
    private void waitOpeneningWintowByLinkAndSwitchToWin(String currentWin) {
        IDE.DEBUGER.waitOpenedSomeWin();
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {
            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public void waitWelcomeIframeAndSwitch() {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@src [contains(., '.html')]]")));
        driver.switchTo().frame(1);
        for (String link : maihWelcomeTabLinks) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(link)));
        }
    }

}
