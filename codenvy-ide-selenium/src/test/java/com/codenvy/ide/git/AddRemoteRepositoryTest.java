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

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Roman Iuvshin */
public class AddRemoteRepositoryTest extends GitServices {

    private static final String PROJECT = AddRemoteRepositoryTest.class.getSimpleName();

    private static final String REPO_INIT_MESSAGE = "[INFO] Repository was successfully initialized.";

    private String NAME_REMOTE_REPO_WITH_READ_WRITE = "newRemoteRepo";

    private String NAME_REMOTE_REPO_WITH_READ_ONLY = "newRemoteRepo-r-Only";


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
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + "jsTestProject").getStatusCode() == 200)
            VirtualFileSystemUtils.delete("jsTestProject");
    }

    @Test
    public void addRemoteRepositoryTest() throws Exception {
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


        // Open "Git > Remote > Remotes..." window and add test repository as remote with Read+Write access.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.waitAddRemoteRepositoriesForm();
        IDE.GIT.typeRemoteRepositoryName(NAME_REMOTE_REPO_WITH_READ_WRITE);
        IDE.GIT.typeRemoteRepositoryLocation("git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();
        IDE.GIT.waitRemoteRepository(NAME_REMOTE_REPO_WITH_READ_WRITE, "git@github.com:exoinvitemain/testRepo.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();

        // Perform Git Pull.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent("Successfully pulled from git@github.com:exoinvitemain/testRepo.git");

        IDE.EXPLORER.waitItemPresent(PROJECT + "/pom.xml");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/README.md");
        IDE.EXPLORER.waitItemPresent(PROJECT + "/src");

        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("README.md", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.typeRemoteRepositoryName(NAME_REMOTE_REPO_WITH_READ_ONLY);
        IDE.GIT.typeRemoteRepositoryLocation("git://github.com/exoinvitemain/jsTestProject.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();
        IDE.GIT.waitRemoteRepository(NAME_REMOTE_REPO_WITH_READ_ONLY, "git://github.com/exoinvitemain/jsTestProject.git");
        IDE.GIT.selectRemoteRepoIntoRemoteRepositoriesList(NAME_REMOTE_REPO_WITH_READ_ONLY);
        IDE.GIT.clickCloseRemoteRepositoryForm();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.selectOnPullFormRemoteBranchInDropDown(NAME_REMOTE_REPO_WITH_READ_ONLY);
        IDE.GIT.clickPullButtonOnGitPullForm();
    }
}
