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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Roman Iuvshin */
public class ShellGitRemoteRepositoriesTest extends BaseTest {

    private static final String PROJECT = ShellGitRemoteRepositoriesTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void shellGitRemoteListCommand() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        // Open "Git > Remote > Remotes..." window and add test repository as remote with Read+Write access.
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.waitAddRemoteRepositoriesForm();
        IDE.GIT.typeRemoteRepositoryName("newRemoteRepo");
        IDE.GIT.typeRemoteRepositoryLocation("https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.GIT.clickAddRemoteRepository();
        IDE.GIT.waitAddRemoteRepositoriesForm();
        IDE.GIT.typeRemoteRepositoryName("otherRepo");
        IDE.GIT.typeRemoteRepositoryLocation("https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.clickOkOnAddRemoteRepositoryForm();

        IDE.GIT.waitRemoteRepository("newRemoteRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.waitRemoteRepository("otherRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();

        IDE.SHELL.setIDEWindowHandle(driver.getWindowHandle());
        IDE.SHELL.callShellFromIde();
        IDE.SHELL.waitContainsTextInShell("Welcome to Codenvy Shell");
        IDE.SHELL.waitContainsTextAfterCommandInShell(PROJECT + "$");
        IDE.SHELL.typeAndExecuteCommand("git remote list");
        IDE.SHELL.waitContainsTextInShell("newRemoteRepo");
        IDE.SHELL.waitContainsTextInShell("otherRepo");
    }

    @Test
    public void shellGitRemoteAddCommand() throws Exception {
        // add remote repo from shell
        IDE.LOADER.waitClosed();
        IDE.SHELL.typeAndExecuteCommand("git remote add -n newAddedRemoteRepo -u https://github.com/exoinvitemain/testRepo.git");
        IDE.SHELL.typeAndExecuteCommand("git remote list");
        IDE.SHELL.waitContainsTextInShell("newAddedRemoteRepo");
        IDE.SHELL.switchToIDE();

        // Open "Git > Remote > Remotes..." window and check added repos
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository("newAddedRemoteRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();
    }

    @Test
    public void shellGitRemoteRmCommand() throws Exception {
        IDE.SHELL.switchOnShellWindow();
        IDE.SHELL.waitContainsTextInShell("Welcome to Codenvy Shell");
        // remove some repos
        IDE.SHELL.typeAndExecuteCommand("git remote rm newAddedRemoteRepo");
        IDE.SHELL.typeAndExecuteCommand("git remote rm otherRepo");
        // check that repos removed from shell
        IDE.SHELL.typeAndExecuteCommand("clear");
        IDE.SHELL.typeAndExecuteCommand("git remote list");
        IDE.SHELL.waitNotContainsTextInShell("newAddedRemoteRepo");
        IDE.SHELL.waitNotContainsTextInShell("otherRepo");
        IDE.SHELL.waitContainsTextInShell("newRemoteRepo");
        // check that repos removed from IDE
        IDE.SHELL.switchToIDE();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepositoryNotPresent("newAddedRemoteRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.waitRemoteRepositoryNotPresent("otherRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.waitRemoteRepository("newRemoteRepo", "https://github.com/exoinvitemain/testRepo.git");
        IDE.GIT.clickCloseRemoteRepositoryForm();
    }
}
