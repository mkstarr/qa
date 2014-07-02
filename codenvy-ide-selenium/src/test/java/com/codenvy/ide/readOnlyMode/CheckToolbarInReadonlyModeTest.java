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
package com.codenvy.ide.readOnlyMode;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.ToolbarCommands;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 @author Roman Iuvshin
 *
 */
public class CheckToolbarInReadonlyModeTest extends BaseTest {

     @Override
    @Before
    public void start() {
    }

    @BeforeClass
    public static void before() throws Exception {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(WORKSPACE_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(WORKSPACE_URL);
        } catch (Exception e) {
        }
    }

    @Test
    public void checkToolbarCommandsInReadOnlyMode() {
        // check available commands
        IDE.TOOLBAR.waitButtonPresentAtRight(ToolbarCommands.File.SEARCH);
        IDE.TOOLBAR.waitButtonPresentAtRight(ToolbarCommands.File.REFRESH);
        IDE.TOOLBAR.waitButtonPresentAtRight(ToolbarCommands.File.PACKAGE_EXPLORER);
        // check unavailable commands
        IDE.TOOLBAR.waitButtonNotPresentAtRight("New");
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.SAVE);
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.SAVE_AS);
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.CUT_SELECTED_ITEM);
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.PASTE);
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.COPY_SELECTED_ITEM);
        IDE.TOOLBAR.waitButtonNotPresentAtRight(ToolbarCommands.File.DELETE);
    }
}
