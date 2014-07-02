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
import com.codenvy.ide.JsonHelper;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.GitServices;

import org.junit.AfterClass;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/** @author Musienko Maxim */
public class FactoryUrlWithIDCommitAndVCSBranchTest extends BaseTest {


    private static final String PROJECT = "factoryUrlRepo" + System.currentTimeMillis();

    private String PUBLIC_ORGANIZATION_REPO = "git@github.com:exoinvitemain/factoryUrlRepo.git";

    private final String masterBranch = "master";

    private final String originMasterBranch = "origin/master";

    private final String secondBranch = "new--branch__with_check";

    private String otherVcsBranch = "&vcsbranch=" + secondBranch;

    private String hashCommit = "bed8cdbc1814a74a1445e220c05b19af7b42e2d8";

    private String someCommitFromMAsterBranch =
            "&commitid=" + hashCommit + "&action=openproject&projectattributes.ptype=Spring" + otherVcsBranch + "&vcsinfo=true";

    private JsonHelper jsonHelper;

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void clonePublicOrganizationRepositoryTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        // clone repository
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(PUBLIC_ORGANIZATION_REPO);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();

//        //!!!DONT FORGET!!!
//        IDE.INVITE_FORM.waitInviteDevelopersOpened();
//        IDE.LOADER.waitClosed();
//        IDE.INVITE_FORM.clickOnCancelButton();
//        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
//        //!!!!!!!!!!!!!

        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUBLIC_ORGANIZATION_REPO + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.LOADER.waitClosed();
        //got to temporary WS
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();

        jsonHelper = new JsonHelper(factoryURL);

        String factory_url_v1 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.1&projectattributes.pname=" + PROJECT +  "&vcs=git&vcsurl=" +
                jsonHelper.getValueByKey("vcsurl") + someCommitFromMAsterBranch;
        System.out.println(factory_url_v1);

        driver.get(factory_url_v1);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitBranchIsCheckout("temp");
        IDE.GIT.waitFieldWithNewNameBranchForm(masterBranch);
        IDE.GIT.waitFieldWithNewNameBranchForm(originMasterBranch);
        IDE.GIT.clickCloseBranchBtn();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.GIT.waitGitShowHistoryForm();
        assertTrue(IDE.GIT.getCommitRevisionFromHistoryPanel().contains(hashCommit));


    }
}

