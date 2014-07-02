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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class FactoryUrlByOptionalWSNameTest extends BaseTest {

    private final static String PROJECT = "testFactoryV1";
    private static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/project/javascriptProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkButtonDisabledStateTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitItemPresent(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String git_url = IDE.GIT.getGitUrl();
        String currentFactory = PROTOCOL + "://" + IDE_HOST + "/factory?v=1.1&projectattributes.pname=" + PROJECT + "&vcs=git&vcsurl=" + git_url +
                                "&action=openproject&projectattributes.ptype=JavaScript";
        IDE.LOGIN.logout();
        driver.get(currentFactory);
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT);
    }
}
