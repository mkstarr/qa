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
package com.codenvy.ide.readOnlyMode;

import java.io.IOException;
import java.util.Map;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

/**
 * @author Musienko Maxim
 *
 */


public class ActionsForReadonlyWithEditFilesTest extends BaseTest {
    private static String PROJECT = ActionsForReadonlyWithEditFilesTest.class.getSimpleName();
    protected static Map<String, Link> project;
    String valueType = String.valueOf(System.currentTimeMillis());

    @Override
    @Before
    public void start() {
    }

    @BeforeClass
    public static void before() throws Exception {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Choose browser Web driver:
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

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void tryEditJavaFileTest() throws Exception {

        IDE.EXPLORER.waitOpened();
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openSpringJavaTetsFile();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitTadWitReadOnlyMode("GreetingController.java");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(valueType);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(valueType);
    }

    @Test
    public void tryEditXmlFileTest() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTadWitReadOnlyMode("pom.xml");
        IDE.EDITOR.typeTextIntoEditor(valueType);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText(valueType);
    }

    @Ignore
    @Test
    public void contextMenuVisibleTest() throws Exception {
        // TODO should be complete after specification details
    }


    private void openSpringJavaTetsFile() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
    }

}
