/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class CreateLocalBranchTest extends GitServices {
    private static final String PROJECT = CreateLocalBranchTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete("CheckBranch");
    }

    // TODO this test do not check branches in console git
    /*
     * 1.Open "Git > Branches..." window and create test branch. Test branch should appear in opened window. Manual 2.Goto system console
     * and clone repository of test project then execute command "git branch -a". Test branch should be there. Manual
     */
    // TODO after fix IDE-2677 test should be pass


    @Test
    public void createNewBranchTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        openNodesAndCheckIcons(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitBranhesCreateBtn();
        IDE.GIT.clickBranhesCreateBtn();
        IDE.GIT.waitCreateNewBranchFieldIsOpen();
        IDE.GIT.typeInFieldNewBranchName("NewBranch");
        IDE.GIT.clickCreateNewBranchOkBtn();
        IDE.GIT.waitCreateNewBranchFieldIsClose();
        IDE.GIT.waitFieldWithNewNameBranchForm("NewBranch");
        IDE.GIT.clickCloseBranchBtn();
        IDE.GIT.waitBranhesIsClosed();
    }

    @Test
    public void checkJustCreatedBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String urlForClone = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(urlForClone);
        IDE.GIT.typeProjectNameInCloneRepositoryForm("CheckBranch");
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("CheckBranch");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitFieldWithNewNameBranchForm("NewBranch");
    }
}
