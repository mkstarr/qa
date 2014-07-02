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
package com.codenvy.ide.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 @author Roman Iuvshin
 *
 */
public class OpenProjectsAndFilesByURLTest extends BaseTest {

    private static final String PROJECT   = "SpringProject";
    private static final String PROJECT_2 = "JSProject";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT_2, "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(PROJECT_2);
    }

    @Test
    public void openProjectsAndFilesByURLTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));

        // open other project by url
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT_2);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT_2 + "/index.html");
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));

        // files
        // open file from first project by url
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT + "/pom.xml");
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.EDITOR.waitTabPresent("pom.xml");
        IDE.EDITOR.selectTab("pom.xml");
        IDE.EDITOR.waitActiveFile();
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));

        // open file from other project
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT_2 + "/index.html");
        IDE.EXPLORER.waitOpened();
        IDE.EDITOR.waitTabPresent("index.html");
        IDE.EDITOR.selectTab("index.html");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(PROJECT_2 + "/index.html");
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));


        // open java file from first project
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT + "/src/main/java/helloworld/GreetingController.java");
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.EDITOR.waitTabPresent("GreetingController.java");
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));

        // open file from other project
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME + "/" + PROJECT_2 + "/js/script.js");
        IDE.EXPLORER.waitOpened();
        IDE.EDITOR.waitTabPresent("script.js");
        IDE.EDITOR.selectTab("script.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.waitForItem(PROJECT_2 + "/js/script.js");
        // check that url does not changed in browser
        Assert.assertTrue(driver.getCurrentUrl().equals(PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));

    }
}