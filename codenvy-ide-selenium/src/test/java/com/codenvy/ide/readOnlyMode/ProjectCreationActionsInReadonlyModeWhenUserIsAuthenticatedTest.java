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
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;


/**
 @author Roman Iuvshin
 *
 */
public class ProjectCreationActionsInReadonlyModeWhenUserIsAuthenticatedTest extends BaseTest {

    private static final String PROJECT = "checkMe";

    protected static Map<String, Link> project;

    private static final String LABEL =
            "You are browsing a workspace with read-only privileges, and you are not allowed to create a new project.\n" +
            "Would you want to switch to your workspace?";

    private static final String LABEL_2 =
            "You are browsing a workspace with read-only privileges, and you are not allowed to create a new project.\n" +
            "Would you want to switch to your workspace?";

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/change-variable-proj.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void ProjectCreationActionsInReadonlyModeWhenUserIsAuthenticatedTest() throws Exception {
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/codenvy");
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.LOADER.waitClosed();
        IDE.READ_ONLY_MODE.clickOnReadOnlyModeIndicator();
        checkReadOnlyModeView(LABEL_2);
        IDE.WELCOME_PAGE.waitCreateNewProjectFromScratchBtn();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        checkReadOnlyModeView(LABEL);
        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        checkReadOnlyModeView(LABEL);
        IDE.WELCOME_PAGE.waitCloneGitRepositoryBtn();
        IDE.WELCOME_PAGE.clickOnCloneGitRepoBtn();
        checkReadOnlyModeView(LABEL);

        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.READ_ONLY_MODE.clickOnReadOnlyModeIndicator();
        IDE.READ_ONLY_MODE.waitReadOnlyModeViewOpenedWhenIsAuthenticated();
        IDE.READ_ONLY_MODE.clickOnCancelButton();

        IDE.READ_ONLY_MODE.waitDisabledInviteUsersButton();
    }

    private void checkReadOnlyModeView(String label) throws Exception {
        IDE.READ_ONLY_MODE.waitReadOnlyModeViewOpenedWhenIsAuthenticated();
        IDE.READ_ONLY_MODE.waitTextOnlabel(label);
        IDE.READ_ONLY_MODE.clickOnJoinButton();
        IDE.EXPLORER.waitOpened();
        IDE.READ_ONLY_MODE.waitReadonlyIndicatorNotVisible();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        driver.get(PROTOCOL + "://" + IDE_HOST + "/ide/codenvy");
    }

}
