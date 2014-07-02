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
import com.codenvy.ide.core.Git;

import org.junit.AfterClass;
import org.junit.Test;

/** @author Roman Iuvshin */
public class ClonePublicOrganizationRepositoryTest extends GitServices {

    private static final String PROJECT = "testRepository";

    private static final String PUBLIC_ORGANIZATION_REPO = "git@github.com:codenvy/testRepository.git";

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void clonePublicOrganizationRepositoryTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        // clone public organization test repository with default remote name.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(PUBLIC_ORGANIZATION_REPO);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PUBLIC_ORGANIZATION_REPO + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.clickOnCancelButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);


        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);


        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);


        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository("origin", PUBLIC_ORGANIZATION_REPO);
        IDE.GIT.clickCloseRemoteRepositoryForm();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeView();
        IDE.GIT.waitMergeExpandRemoteBranchIcon();
        IDE.GIT.clickOnExpandRemotelBranchIcon();
        IDE.GIT.waitItemInMergeList("origin/master");
        IDE.GIT.waitItemInMergeList("origin/newBranchForTest");
        IDE.GIT.clickCancelMergeBtn();
    }
}
