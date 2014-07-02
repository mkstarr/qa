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
public class DeleteBranchTest extends BaseTest {
    private static final String PROJECT = DeleteBranchTest.class.getSimpleName();

    private final String MASTER_BRANCH = "master";

    private final String TEST_BRANCH = "newbranch";


    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/git/gitSampleProjectTwoBranches.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete("CheckDelBranch");
    }

    // TODO https://jira.codenvycorp.com/browse/IDE-2758

    @Test
    public void checkoutBrenchTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        //1 Open "Git > Branches..." window and delete test branch.
        deleteBranch();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.GIT.waitDisappearNameBranchInBranchForm(TEST_BRANCH);
        IDE.GIT.clickCloseBranchBtn();
        IDE.GIT.waitBranhesIsClosed();
        String cloneUrl = getRepositoryUrl();

        // clone and check that branch was deleted
        cloneRepo(cloneUrl);
        checkClonedRepowithoutBranch();
    }

    private void deleteBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(TEST_BRANCH);
        IDE.GIT.selectBranchInList(TEST_BRANCH);
        IDE.GIT.clickDeleteBranchBtn();
    }

    private void checkClonedRepowithoutBranch() throws Exception {
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("CheckDelBranch");
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.GIT.waitDisappearNameBranchInBranchForm(TEST_BRANCH);
    }

    private String getRepositoryUrl() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String urlForClone = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        return urlForClone;

    }

    private void cloneRepo(String urlForClone) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(urlForClone);
        IDE.GIT.typeProjectNameInCloneRepositoryForm("CheckDelBranch");
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("CheckDelBranch");
    }

}
