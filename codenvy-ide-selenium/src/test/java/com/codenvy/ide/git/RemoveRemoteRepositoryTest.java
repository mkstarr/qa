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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Roman Iuvshin */

public class RemoveRemoteRepositoryTest extends GitServices {

    private static final String PROJECT = RemoveRemoteRepositoryTest.class.getSimpleName();

    private static final String REPO_INIT_MESSAGE = "[INFO] Repository was successfully initialized.";

    @BeforeClass
    public static void before() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void removeRemoteRepositoryTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitItemPresent(PROJECT);

        // initialize git repository
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.GIT.waitInitializeLocalRepositoryForm();
        IDE.GIT.typeInitializationRepositoryName(PROJECT);
        IDE.GIT.clickOkInitializeLocalRepository();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(REPO_INIT_MESSAGE);
        // checking git menu buttons
        checkMenuStateWhenRepositoryInited();

        // Add two remote repositories in test project.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();

        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.waitAddRemoteRepositoriesForm();
        IDE.GIT.typeRemoteRepositoryName("testRepo");
        IDE.GIT.typeRemoteRepositoryLocation("git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.waitAddRemoteRepositoriesForm();
        IDE.GIT.typeRemoteRepositoryName("kernel");
        IDE.GIT.typeRemoteRepositoryLocation("git@github.com:exoinvitemain/kernel.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();

        IDE.GIT.waitRemoteRepository("testRepo", "git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.waitRemoteRepository("kernel", "git@github.com:exoinvitemain/kernel.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();


        // Open "Git > Remote > Remotes" window and remove one of remote repository.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository("testRepo", "git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.waitRemoteRepository("kernel", "git@github.com:exoinvitemain/kernel.git");
        IDE.GIT.clickRemoveRemoteRepository();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.GIT.waitRemoteRepositoryNotPresent("testRepo", "git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();

        // Call command Git Pull.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.waitRemoteRepositoryInGitPullForm("kernel");
        IDE.GIT.waitRemoteRepositoryInGitPullFormDisappear("testRepo");
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.OUTPUT.waitForSubTextPresent("Successfully pulled from git@github.com:exoinvitemain/kernel.git");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.LOADER.waitClosed();

        // Open "Git > Remote > Remotes" window and remove last remote repository.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository("kernel", "git@github.com:exoinvitemain/kernel.git");
        IDE.GIT.clickRemoveRemoteRepository();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.GIT.waitRemoteRepositoryNotPresent("kernel", "git@github.com:exoinvitemain/kernel.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();

        // Call command Git Pull.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.WARNING_DIALOG.waitOpened();
        IDE.WARNING_DIALOG.waitTextInDialogPresent("No remote repositories are found.");
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        // Call command Git Push.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.WARNING_DIALOG.waitOpened();
        IDE.WARNING_DIALOG.waitTextInDialogPresent("No remote repositories are found.");
        IDE.WARNING_DIALOG.clickOk();
        IDE.WARNING_DIALOG.waitClosed();

        // Open Merge dialog.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.GIT.waitMergeView();
        IDE.GIT.waitMergeExpandRemoteBranchIconNotPresent();
        IDE.GIT.waitItemInMergeListIsNotPresent("kernel");
        IDE.GIT.waitItemInMergeListIsNotPresent("testRepo");
    }
}
