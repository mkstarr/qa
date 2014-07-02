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

import java.io.IOException;
import java.util.Map;

/** @author Roman Iuvshin */
public class RenameLocalBranchTest extends GitServices {

    private static final String PROJECT = RenameLocalBranchTest.class.getSimpleName();

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
        VirtualFileSystemUtils.delete("CheckRenamedBranch");
    }

    @Test
    public void renameLocalBranchTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitFieldWithNewNameBranchForm("newbranch");
        IDE.GIT.selectBranchInList("newbranch");
        IDE.GIT.clickOnRenameBranchButton();
        IDE.GIT.waitCreateNewBranchFieldIsOpen();
        IDE.GIT.typeNewBranchNameForRename("changedName");
        IDE.GIT.clickCreateNewBranchOkBtn();
        IDE.LOADER.waitClosed();

        // check that branch was renamed
        IDE.GIT.waitFieldWithNewNameBranchForm("changedName");
        IDE.GIT.waitDisappearNameBranchInBranchForm("newbranch");
        IDE.GIT.clickCloseBranchBtn();

        // check renamed branch after clone
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String urlForClone = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(urlForClone);
        IDE.GIT.typeProjectNameInCloneRepositoryForm("CheckRenamedBranch");
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();

        //TODO UNCOMMENT AFTER FIX IDE-2758
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("CheckRenamedBranch");

        //REMOVE NEX LINE AFTER FIX IDE-2758
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitFieldWithNewNameBranchForm("changedName");
        IDE.GIT.waitDisappearNameBranchInBranchForm("newbranch");
    }
}
