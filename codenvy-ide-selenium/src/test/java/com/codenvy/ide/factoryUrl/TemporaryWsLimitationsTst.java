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
package com.codenvy.ide.factoryUrl;

import junit.framework.Assert;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Roman Iuvshin */

public class TemporaryWsLimitationsTst extends BaseTest {
    private static final String PROJECT = "accesibleAct";

    private static final String TEST_STRING = Long.toString(System.currentTimeMillis());

    private static Map<String, Link> project;

    private static final String DEPENDENCY =
            "\n<dependency>\n  <groupId>cloud-ide</groupId>\n  <artifactId>spring-demo</artifactId>\n"
            + "  <version>1.0</version>\n  <type>war</type>\n</dependency>";
    private WebDriverWait wait;

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
    public void onlyOneTemporaryWsForOneFactoryURLTest() throws Exception {
        wait = new WebDriverWait(driver, 60);

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.LOGIN.logout();

        List<String> wsNames = new ArrayList<String>();

        // check temporary ws limit
        for (int i = 0; i < 20; i++) {
            System.out.println("attempt: " + i);
            driver.get(factoryURL);
            IDE.FACTORY_URL.waitWelcomeIframe();
            IDE.selectMainFrame();
            IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
            String url = driver.getCurrentUrl();
            System.out.println("CURRENT URL: " + url);
            System.out.println("IS CONTAINS: " + wsNames.contains(url));
            Assert.assertFalse(wsNames.contains(url));
            wsNames.add(url);
        }
        for (int i = 0; i < wsNames.size(); i++) {
            System.out.println(wsNames.get(i));
        }
        driver.get(factoryURL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'TOO MANY FACTORIES OPENED')]")));

    }
}