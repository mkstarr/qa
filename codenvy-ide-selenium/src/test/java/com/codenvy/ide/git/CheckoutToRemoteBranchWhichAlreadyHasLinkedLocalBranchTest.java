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
package com.codenvy.ide.git;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

/** @author Roman Iuvshin */
public class CheckoutToRemoteBranchWhichAlreadyHasLinkedLocalBranchTest extends GitServices {

    private static final String PROJECT = "testRepo";

    private static final String REPO_URL = "git@github.com:exoinvitemain/testRepo.git";

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void renameAndDeleteRemoteBranchTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        // Clone test repository again with specific remote name.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(REPO_URL);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_URL + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.LOADER.waitClosed();

        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.clickOnCancelButton();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // Open "Branches" window.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm("origin/master");
        IDE.GIT.waitFieldWithNewNameBranchForm("master");
        IDE.GIT.selectBranchInList("origin/master");

        // Checkout to the master remote branch.
        IDE.GIT.clickCheckOutBtn();
        IDE.LOADER.waitClosed();
        IDE.GIT.clickCloseBranchBtn();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent("fatal: A branch named 'master' already exists");

    }
}
