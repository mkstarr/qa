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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CloneRemotePublicPersonalRepositoryAndInviteCollaboratorTest extends BaseTest {

    private static final String PROJECT = "testRepo";

    private String REPO_URL;

    public CloneRemotePublicPersonalRepositoryAndInviteCollaboratorTest() {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            REPO_URL = "git@github.com:exoinvitemain/testRepo.git";
        } else {
            REPO_URL = "git@github.com:codenvymain/testRepo.git";
        }
    }


    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
        IDE.MAIL_CHECK.cleanMailBox(THIRD_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
    }

    @Test
    public void cloneRemotePublicPersonalRepository() throws Exception {
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
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository("origin", REPO_URL);
        IDE.GIT.clickCloseRemoteRepositoryForm();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeView();
        IDE.GIT.waitMergeExpandRemoteBranchIcon();
        IDE.GIT.clickOnExpandRemotelBranchIcon();
        IDE.GIT.waitItemInMergeList("origin/master");
        IDE.GIT.clickCancelMergeBtn();
    }

    @Test
    public void inviteCollaboratorsOfClonedPublicPersonalRepoTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.INVITE_GITHUB_COLLABORATORS);
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(THIRD_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitChekBoxUnchekedGithubForm(THIRD_GITHUB_USER_FOR_INVITE);
        assertTrue(IDE.INVITE_FORM.isInviteButtonDisabled());
        IDE.INVITE_FORM.clickOnCheckBoxGithubForm(THIRD_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitCheckboxIsCheckedGithubForm(THIRD_GITHUB_USER_FOR_INVITE);
        assertTrue(IDE.INVITE_FORM.isInviteButtonEnabled());
        IDE.INVITE_FORM.inviteClick();
        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();

        IDE.LOGIN.logout();
        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(THIRD_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        //IDE.LOGIN.logout();
    }

    /** @param currentWin */
    private void switchToGithubLoginWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {

            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
